package com.bergerkiller.bukkit.tc.controller.player.network;

import com.bergerkiller.bukkit.common.Task;
import com.bergerkiller.bukkit.common.events.PacketReceiveEvent;
import com.bergerkiller.bukkit.common.events.PacketSendEvent;
import com.bergerkiller.bukkit.common.protocol.PacketListener;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.wrappers.RelativeFlags;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.generated.net.minecraft.network.protocol.PacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.common.ClientboundKeepAlivePacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutPositionHandle;
import java.util.Collections;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

final class PlayerClientSynchronizerProviderLegacyImpl implements PlayerClientSynchronizer.Provider, PacketListener {
   private final TrainCarts traincarts;
   private final Task cleanupTask;
   private final Map<Player, PlayerClientSynchronizerProviderLegacyImpl.SyncQueue> queues = new IdentityHashMap();
   private Map<Player, PlayerClientSynchronizerProviderLegacyImpl.SyncQueue> queuesVisible = Collections.emptyMap();
   private boolean disabled = false;

   public PlayerClientSynchronizerProviderLegacyImpl(TrainCarts traincarts) {
      this.traincarts = traincarts;
      this.cleanupTask = new Task(traincarts) {
         public void run() {
            PlayerClientSynchronizerProviderLegacyImpl.this.cleanupQuitPlayerQueues();
         }
      };
   }

   public PlayerClientSynchronizer forViewer(AttachmentViewer viewer) {
      PlayerClientSynchronizer queue = (PlayerClientSynchronizer)this.queuesVisible.get(viewer.getPlayer());
      if (queue == null) {
         synchronized(this) {
            if (this.disabled || !viewer.isConnected()) {
               return this.createNoOp(viewer.getPlayer());
            }

            queue = (PlayerClientSynchronizer)this.queues.computeIfAbsent(viewer.getPlayer(), (p) -> {
               return new PlayerClientSynchronizerProviderLegacyImpl.SyncQueue(viewer);
            });
            this.updateVisibleQueueMap();
         }
      }

      return queue;
   }

   public void enable() {
      this.traincarts.register(this, new PacketType[]{PacketType.IN_KEEP_ALIVE});
      this.traincarts.register(new Listener() {
         @EventHandler(
            priority = EventPriority.MONITOR
         )
         public void onPlayerQuit(PlayerQuitEvent event) {
            PlayerClientSynchronizerProviderLegacyImpl.SyncQueue queue = (PlayerClientSynchronizerProviderLegacyImpl.SyncQueue)PlayerClientSynchronizerProviderLegacyImpl.this.queuesVisible.get(event.getPlayer());
            if (queue != null) {
               queue.setHasQuit();
            }

         }
      });
      this.cleanupTask.start(20L, 20L);
      this.disabled = false;
   }

   public synchronized void disable() {
      this.cleanupTask.stop();
      this.queues.values().forEach(PlayerClientSynchronizerProviderLegacyImpl.SyncQueue::setHasQuit);
      this.queues.clear();
      this.queuesVisible = Collections.emptyMap();
      this.disabled = true;
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      PlayerClientSynchronizerProviderLegacyImpl.SyncQueue queue = (PlayerClientSynchronizerProviderLegacyImpl.SyncQueue)this.queuesVisible.get(event.getPlayer());
      if (queue != null && queue.hasPendingAcknowledgements()) {
         long keepAliveId = (Long)event.getPacket().read(PacketType.IN_KEEP_ALIVE.key);
         PlayerClientSynchronizerProviderLegacyImpl.PendingAcknowledgement ack = queue.acknowledge(keepAliveId);
         if (ack != null) {
            ack.call();
            event.setCancelled(true);
         }
      }

   }

   public void onPacketSend(PacketSendEvent event) {
   }

   private synchronized void cleanupQuitPlayerQueues() {
      if (this.queues.values().removeIf(PlayerClientSynchronizerProviderLegacyImpl.SyncQueue::hasQuitSomeTimeAgo)) {
         this.updateVisibleQueueMap();
      }

   }

   private void updateVisibleQueueMap() {
      if (this.queues.isEmpty()) {
         this.queuesVisible = Collections.emptyMap();
      } else {
         this.queuesVisible = new IdentityHashMap(this.queues);
      }

   }

   private static class SyncQueue implements PlayerClientSynchronizer {
      private static final RelativeFlags NO_CHANGE_RELATIVE_FLAGS;
      private static final IntFunction<PacketPlayOutPositionHandle> NO_CHANGE_ACK_PACKET;
      private final AttachmentViewer viewer;
      private final Player player;
      private boolean hasQuit = false;
      private int quitTickTime;
      private final Deque<PlayerClientSynchronizerProviderLegacyImpl.PendingAcknowledgement> pending = new LinkedList();

      public SyncQueue(AttachmentViewer viewer) {
         this.viewer = viewer;
         this.player = viewer.getPlayer();
      }

      public synchronized boolean hasQuitSomeTimeAgo() {
         return this.hasQuit && CommonUtil.getServerTicks() - this.quitTickTime > 2;
      }

      public synchronized void setHasQuit() {
         this.hasQuit = true;
         this.quitTickTime = CommonUtil.getServerTicks();
         this.pending.clear();
      }

      public synchronized boolean hasPendingAcknowledgements() {
         return !this.pending.isEmpty();
      }

      public synchronized PlayerClientSynchronizerProviderLegacyImpl.PendingAcknowledgement acknowledge(long keepAliveId) {
         PlayerClientSynchronizerProviderLegacyImpl.PendingAcknowledgement first = (PlayerClientSynchronizerProviderLegacyImpl.PendingAcknowledgement)this.pending.pollFirst();
         if (first != null) {
            if (first.getKeepAliveId() == keepAliveId) {
               return first;
            }

            this.pending.addFirst(first);
         }

         return null;
      }

      public Player getPlayer() {
         return this.player;
      }

      public void synchronize(IntFunction<PacketPlayOutPositionHandle> positionPacketMaker, Consumer<PacketPlayOutPositionHandle> callback) {
         if (!this.hasQuit) {
            long keepAliveId = getSafeAwaitKeepAliveId();
            PacketPlayOutPositionHandle packet = (PacketPlayOutPositionHandle)positionPacketMaker.apply(0);
            ClientboundKeepAlivePacketHandle keepAlivePacket = ClientboundKeepAlivePacketHandle.createNew(keepAliveId);
            PlayerClientSynchronizerProviderLegacyImpl.PendingAcknowledgement pendingAck = new PlayerClientSynchronizerProviderLegacyImpl.PendingAcknowledgement(keepAliveId, packet, callback);
            synchronized(this) {
               if (!this.hasQuit) {
                  this.pending.addLast(pendingAck);
                  this.viewer.send((PacketHandle)packet);
                  this.viewer.send((PacketHandle)keepAlivePacket);
               }
            }
         }
      }

      public void synchronizeBundle(List<? extends PacketHandle> packets, Runnable startCallback, Runnable endCallback) {
         if (!this.hasQuit) {
            synchronized(this) {
               if (!this.hasQuit) {
                  this.synchronize(startCallback);
                  Iterator var5 = packets.iterator();

                  while(var5.hasNext()) {
                     PacketHandle packet = (PacketHandle)var5.next();
                     this.viewer.send(packet);
                  }

                  this.synchronize(endCallback);
               }
            }
         }
      }

      public void synchronize(Runnable callback) {
         if (!this.hasQuit) {
            long keepAliveId = getSafeAwaitKeepAliveId();
            ClientboundKeepAlivePacketHandle keepAlivePacket = ClientboundKeepAlivePacketHandle.createNew(keepAliveId);
            PlayerClientSynchronizerProviderLegacyImpl.PendingAcknowledgement pendingAck = new PlayerClientSynchronizerProviderLegacyImpl.PendingAcknowledgement(keepAliveId, (PacketPlayOutPositionHandle)null, (np) -> {
               callback.run();
            });
            synchronized(this) {
               if (!this.hasQuit) {
                  this.pending.addLast(pendingAck);
                  this.viewer.send((PacketHandle)keepAlivePacket);
               }
            }
         }
      }

      private static long getSafeAwaitKeepAliveId() {
         long timeStampMillis = System.nanoTime() / 1000000L;
         timeStampMillis -= 30000L;
         return timeStampMillis;
      }

      static {
         NO_CHANGE_RELATIVE_FLAGS = RelativeFlags.RELATIVE_POSITION_ROTATION.withRelativeDelta().withRelativeDeltaRotation();
         NO_CHANGE_ACK_PACKET = (teleportId) -> {
            return PacketPlayOutPositionHandle.createNew(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0D, 0.0D, 0.0D, NO_CHANGE_RELATIVE_FLAGS, teleportId);
         };
      }
   }

   private static class PendingAcknowledgement {
      private final long keepAliveId;
      private final PacketPlayOutPositionHandle position;
      private final Consumer<PacketPlayOutPositionHandle> callback;

      public PendingAcknowledgement(long keepAliveId, PacketPlayOutPositionHandle position, Consumer<PacketPlayOutPositionHandle> callback) {
         this.keepAliveId = keepAliveId;
         this.position = position;
         this.callback = callback;
      }

      public long getKeepAliveId() {
         return this.keepAliveId;
      }

      public void call() {
         this.callback.accept(this.position);
      }
   }
}
