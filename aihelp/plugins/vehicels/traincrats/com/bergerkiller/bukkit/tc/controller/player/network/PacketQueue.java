package com.bergerkiller.bukkit.tc.controller.player.network;

import com.bergerkiller.bukkit.common.internal.CommonCapabilities;
import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.protocol.PlayerGameInfo;
import com.bergerkiller.bukkit.common.utils.PacketUtil;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.dep.me.m56738.smoothcoasters.api.NetworkInterface;
import com.bergerkiller.bukkit.tc.utils.CircularFIFOQueue;
import com.bergerkiller.bukkit.tc.utils.CircularFIFOQueueStampedRW;
import com.bergerkiller.generated.net.minecraft.network.protocol.PacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutCustomPayloadHandle;
import org.bukkit.entity.Player;

public class PacketQueue implements NetworkInterface {
   private final TrainCarts plugin;
   private final Player player;
   private final CircularFIFOQueue<CommonPacket> queue;
   private volatile Thread thread;

   public static PacketQueue create(TrainCarts plugin, Player player, PlayerGameInfo playerGameInfo) {
      CircularFIFOQueue<CommonPacket> fifoQueue = new CircularFIFOQueueStampedRW();
      return (PacketQueue)(CommonCapabilities.HAS_BUNDLE_PACKET && playerGameInfo.evaluateVersion(">=", "1.19.4") ? new BundlerPacketQueue(plugin, player, playerGameInfo, fifoQueue) : new PacketQueue(plugin, player, playerGameInfo, fifoQueue));
   }

   public static PacketQueue createNoOp(TrainCarts plugin, Player player) {
      return new PacketQueue(plugin, player);
   }

   private PacketQueue(TrainCarts plugin, Player player) {
      this.plugin = plugin;
      this.player = player;
      this.queue = CircularFIFOQueue.forward(this::processPacket);
      this.thread = null;
   }

   protected PacketQueue(TrainCarts plugin, Player player, PlayerGameInfo playerGameInfo, CircularFIFOQueue<CommonPacket> queue) {
      this.plugin = plugin;
      this.player = player;
      this.queue = queue;
      this.queue.setWakeCallback(this::startProcessingPackets);
      this.thread = null;
   }

   public void send(PacketHandle packet) {
      this.queue.put(packet.toCommonPacket());
   }

   public void send(CommonPacket packet) {
      this.queue.put(packet);
   }

   public void sendSilent(CommonPacket packet) {
      this.queue.put(new PacketQueue.SilentCommonPacket(packet.getHandle(), packet.getType()));
   }

   public void sendSilent(PacketHandle packet) {
      this.queue.put(new PacketQueue.SilentCommonPacket(packet.getRaw(), packet.getPacketType()));
   }

   public void sendMessage(Player player, String channel, byte[] message) {
      if (player != this.player) {
         throw new IllegalArgumentException("Wrong network interface used, interface is of " + this.player.getName() + " but updated " + player.getName());
      } else {
         if (this.plugin.getSmoothCoastersAPI().getVersion(player) < 5) {
            this.queue.put(PacketPlayOutCustomPayloadHandle.createNew(channel, message).toCommonPacket());
         } else {
            this.send((PacketHandle)PacketPlayOutCustomPayloadHandle.createNew(channel, message));
         }

      }
   }

   public void abort() {
      this.queue.abort();
   }

   public void syncBegin() {
      while(!this.queue.isEmpty()) {
         Thread.yield();
      }

   }

   public void syncEnd() {
   }

   private void startProcessingPackets() {
      if (this.thread == null && !this.queue.isAborted()) {
         Thread newThread = new Thread(this::processPacketsThread, "TC-PacketWriterThread-" + this.player.getEntityId());
         newThread.setDaemon(true);
         this.thread = newThread;
         newThread.start();
      }

   }

   private void processPacketsThread() {
      CircularFIFOQueue queue = this.queue;

      while(true) {
         while(true) {
            try {
               this.processPacket((CommonPacket)queue.take(60000L));
            } catch (CircularFIFOQueue.EmptyQueueException var3) {
               if (queue.runIfEmpty(() -> {
                  this.thread = null;
               })) {
                  return;
               }
            }
         }
      }
   }

   private void processPacket(CommonPacket packet) {
      PacketUtil.sendPacket(this.player, packet, !(packet instanceof PacketQueue.SilentCommonPacket));
   }

   public String toString() {
      return "PacketQueue{player=" + this.player.getName() + "}";
   }

   public static final class SilentCommonPacket extends CommonPacket {
      public SilentCommonPacket(Object packetHandle, PacketType packetType) {
         super(packetHandle, packetType);
      }
   }
}
