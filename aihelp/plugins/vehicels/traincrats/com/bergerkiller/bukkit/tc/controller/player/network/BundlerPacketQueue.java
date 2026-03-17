package com.bergerkiller.bukkit.tc.controller.player.network;

import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PlayerGameInfo;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.utils.CircularFIFOQueue;
import com.bergerkiller.generated.net.minecraft.network.protocol.PacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.ClientboundBundlePacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityDestroyHandle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.StampedLock;
import org.bukkit.entity.Player;

class BundlerPacketQueue extends PacketQueue {
   private static final int MAX_PACKETS_PER_BUNDLE = 4092;
   private final StampedLock lock = new StampedLock();
   private final AtomicInteger bufferIndex = new AtomicInteger(Integer.MIN_VALUE);
   private final ArrayList<Object> fallbackBuffer = new ArrayList();
   private Object[] buffer = new Object[256];

   protected BundlerPacketQueue(TrainCarts plugin, Player player, PlayerGameInfo playerGameInfo, CircularFIFOQueue<CommonPacket> queue) {
      super(plugin, player, playerGameInfo, queue);
   }

   public void startBundling() {
      long writeLock = this.lock.writeLock();
      if (this.bufferIndex.get() < 0) {
         this.bufferIndex.set(0);
      }

      this.lock.unlockWrite(writeLock);
   }

   public void stopBundling() {
      long writeLock = this.lock.writeLock();

      try {
         int numBufferPackets = Math.min(this.buffer.length, this.bufferIndex.getAndSet(Integer.MIN_VALUE));
         if (numBufferPackets > 0) {
            int numPackets = numBufferPackets + this.fallbackBuffer.size();
            Object[] bundlePackets = new Object[numPackets];

            int i;
            for(i = numBufferPackets - 1; i >= 0; --i) {
               Object rawPacket;
               while((rawPacket = this.buffer[i]) == null) {
                  Thread.yield();
               }

               bundlePackets[i] = rawPacket;
            }

            int endIndex;
            if (this.fallbackBuffer.isEmpty()) {
               Arrays.fill(this.buffer, 0, numPackets, (Object)null);
            } else {
               i = this.buffer.length;

               for(endIndex = 0; i < numPackets; ++endIndex) {
                  bundlePackets[i] = this.fallbackBuffer.get(endIndex);
                  ++i;
               }

               this.fallbackBuffer.clear();
               this.fallbackBuffer.trimToSize();
               this.buffer = new Object[numPackets * 2];
            }

            if (numPackets > 4092) {
               i = 0;

               do {
                  endIndex = Math.min(i + 4092, numPackets);
                  Object[] singleBundlePackets = Arrays.copyOfRange(bundlePackets, i, endIndex);
                  super.send((PacketHandle)ClientboundBundlePacketHandle.createNew(Arrays.asList(singleBundlePackets)));
                  i = endIndex;
               } while(endIndex < numPackets);
            } else {
               super.send((PacketHandle)ClientboundBundlePacketHandle.createNew(Arrays.asList(bundlePackets)));
            }
         }
      } finally {
         this.lock.unlockWrite(writeLock);
      }

   }

   public void syncBegin() {
      super.syncBegin();
      this.startBundling();
   }

   public void syncEnd() {
      super.syncEnd();
      this.stopBundling();
   }

   public void send(CommonPacket packet) {
      this.handleSend(packet.getHandle(), () -> {
         super.send(packet);
      });
   }

   public void send(PacketHandle packet) {
      this.handleSend(packet.getRaw(), () -> {
         super.send(packet);
      });
   }

   public void sendSilent(CommonPacket packet) {
      this.handleSend(packet.getHandle(), () -> {
         super.sendSilent(packet);
      });
   }

   public void sendSilent(PacketHandle packet) {
      this.handleSend(packet.getRaw(), () -> {
         super.sendSilent(packet);
      });
   }

   private void handleSend(Object rawPacket, Runnable fallbackAction) {
      int index = this.bufferIndex.getAndIncrement();
      if (index >= 0) {
         Object[] buffer = this.buffer;
         if (index < buffer.length) {
            Iterable<Object> bundleSubPackets = PacketHandle.tryUnwrapBundlePacket(rawPacket);
            if (bundleSubPackets != null) {
               Iterator<Object> iter = bundleSubPackets.iterator();
               if (iter.hasNext()) {
                  buffer[index] = iter.next();

                  while(iter.hasNext()) {
                     this.handleSend(iter.next(), fallbackAction);
                  }
               } else {
                  buffer[index] = createDummyPacket();
               }
            } else {
               buffer[index] = rawPacket;
            }

            return;
         }
      }

      long readLock = this.lock.readLock();

      try {
         index = this.bufferIndex.getAndIncrement();
         if (index < 0) {
            this.bufferIndex.set(Integer.MIN_VALUE);
            fallbackAction.run();
            return;
         }

         Object[] buffer = this.buffer;
         Iterable<Object> bundleSubPackets = PacketHandle.tryUnwrapBundlePacket(rawPacket);
         if (bundleSubPackets != null) {
            Iterator<Object> iter = bundleSubPackets.iterator();
            if (iter.hasNext()) {
               while(true) {
                  if (index >= buffer.length) {
                     synchronized(this.fallbackBuffer) {
                        do {
                           this.fallbackBuffer.add(iter.next());
                        } while(iter.hasNext());

                        return;
                     }
                  }

                  buffer[index] = iter.next();
                  if (!iter.hasNext()) {
                     break;
                  }

                  index = this.bufferIndex.getAndIncrement();
               }
            } else if (index < buffer.length) {
               buffer[index] = createDummyPacket();
            }
         } else if (index < buffer.length) {
            buffer[index] = rawPacket;
         } else {
            synchronized(this.fallbackBuffer) {
               this.fallbackBuffer.add(rawPacket);
            }
         }
      } finally {
         this.lock.unlockRead(readLock);
      }

   }

   private static Object createDummyPacket() {
      return PacketPlayOutEntityDestroyHandle.createNewMultiple(new int[0]).getRaw();
   }
}
