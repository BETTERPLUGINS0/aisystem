package com.bergerkiller.bukkit.tc.controller.player.network;

import com.bergerkiller.bukkit.common.Task;
import com.bergerkiller.bukkit.common.events.PacketReceiveEvent;
import com.bergerkiller.bukkit.common.events.PacketSendEvent;
import com.bergerkiller.bukkit.common.internal.CommonCapabilities;
import com.bergerkiller.bukkit.common.protocol.PacketListener;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.wrappers.RelativeFlags;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.generated.net.minecraft.network.protocol.PacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.ClientboundBundlePacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutPositionHandle;
import com.bergerkiller.generated.net.minecraft.server.level.EntityPlayerHandle;
import java.util.ArrayList;
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

final class PlayerClientSynchronizerProviderModernImpl implements PlayerClientSynchronizer.Provider, PacketListener {
   private final TrainCarts traincarts;
   private final Task cleanupTask;
   private final Map<Player, PlayerClientSynchronizerProviderModernImpl.SyncQueue> queues = new IdentityHashMap();
   private Map<Player, PlayerClientSynchronizerProviderModernImpl.SyncQueue> queuesVisible = Collections.emptyMap();
   private boolean disabled = false;

   public PlayerClientSynchronizerProviderModernImpl(TrainCarts traincarts) {
      this.traincarts = traincarts;
      this.cleanupTask = new Task(traincarts) {
         public void run() {
            PlayerClientSynchronizerProviderModernImpl.this.cleanupQuitPlayerQueues();
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
               return new PlayerClientSynchronizerProviderModernImpl.SyncQueue(viewer);
            });
            this.updateVisibleQueueMap();
         }
      }

      return queue;
   }

   public void enable() {
      this.traincarts.register(this, new PacketType[]{PacketType.IN_TELEPORT_ACCEPT});
      this.traincarts.register(new Listener() {
         @EventHandler(
            priority = EventPriority.MONITOR
         )
         public void onPlayerQuit(PlayerQuitEvent event) {
            PlayerClientSynchronizerProviderModernImpl.SyncQueue queue = (PlayerClientSynchronizerProviderModernImpl.SyncQueue)PlayerClientSynchronizerProviderModernImpl.this.queuesVisible.get(event.getPlayer());
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
      this.queues.values().forEach(PlayerClientSynchronizerProviderModernImpl.SyncQueue::setHasQuit);
      this.queues.clear();
      this.queuesVisible = Collections.emptyMap();
      this.disabled = true;
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      PlayerClientSynchronizerProviderModernImpl.SyncQueue queue = (PlayerClientSynchronizerProviderModernImpl.SyncQueue)this.queuesVisible.get(event.getPlayer());
      if (queue != null && queue.hasPendingAcknowledgements()) {
         int id = (Integer)event.getPacket().read(PacketType.IN_TELEPORT_ACCEPT.teleportId);
         PlayerClientSynchronizerProviderModernImpl.PendingAcknowledgement ack = queue.acknowledge(id);
         if (ack != null) {
            ack.call();
            event.setCancelled(true);
         }
      }

   }

   public void onPacketSend(PacketSendEvent event) {
   }

   private synchronized void cleanupQuitPlayerQueues() {
      if (this.queues.values().removeIf(PlayerClientSynchronizerProviderModernImpl.SyncQueue::hasQuitSomeTimeAgo)) {
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
      private final Deque<PlayerClientSynchronizerProviderModernImpl.PendingAcknowledgement> pending = new LinkedList();

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

      public synchronized PlayerClientSynchronizerProviderModernImpl.PendingAcknowledgement acknowledge(int teleportId) {
         PlayerClientSynchronizerProviderModernImpl.PendingAcknowledgement first = (PlayerClientSynchronizerProviderModernImpl.PendingAcknowledgement)this.pending.pollFirst();
         if (first != null) {
            if (first.getTeleportId() == teleportId) {
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
            int teleportId = this.getSafeAwaitTeleportId();
            PacketPlayOutPositionHandle packet = (PacketPlayOutPositionHandle)positionPacketMaker.apply(teleportId);
            PlayerClientSynchronizerProviderModernImpl.PendingAcknowledgement pendingAck = new PlayerClientSynchronizerProviderModernImpl.PendingAcknowledgement(packet, callback);
            synchronized(this) {
               if (!this.hasQuit) {
                  this.pending.addLast(pendingAck);
                  this.viewer.send((PacketHandle)packet);
               }
            }
         }
      }

      public void synchronizeBundle(List<? extends PacketHandle> packets, Runnable startCallback, Runnable endCallback) {
         if (!this.hasQuit) {
            if (!CommonCapabilities.HAS_BUNDLE_PACKET) {
               synchronized(this) {
                  if (!this.hasQuit) {
                     this.synchronize(startCallback);
                     Iterator var15 = packets.iterator();

                     while(var15.hasNext()) {
                        PacketHandle packet = (PacketHandle)var15.next();
                        this.viewer.send(packet);
                     }

                     this.synchronize(endCallback);
                  }
               }
            } else {
               int teleportId = this.getSafeAwaitTeleportId();
               PacketPlayOutPositionHandle startSyncPacket = (PacketPlayOutPositionHandle)NO_CHANGE_ACK_PACKET.apply(teleportId);
               PacketPlayOutPositionHandle endSyncPacket = (PacketPlayOutPositionHandle)NO_CHANGE_ACK_PACKET.apply(teleportId);
               List<Object> rawPackets = new ArrayList(packets.size() + 2);
               rawPackets.add(startSyncPacket.getRaw());
               Iterator var8 = packets.iterator();

               while(var8.hasNext()) {
                  PacketHandle packet = (PacketHandle)var8.next();
                  rawPackets.add(packet.getRaw());
               }

               rawPackets.add(endSyncPacket.getRaw());
               ClientboundBundlePacketHandle bundlePacket = ClientboundBundlePacketHandle.createNew(rawPackets);
               PlayerClientSynchronizerProviderModernImpl.PendingAcknowledgement pendingStartAck = new PlayerClientSynchronizerProviderModernImpl.PendingAcknowledgement(startSyncPacket, (np) -> {
                  startCallback.run();
               });
               PlayerClientSynchronizerProviderModernImpl.PendingAcknowledgement pendingEndAck = new PlayerClientSynchronizerProviderModernImpl.PendingAcknowledgement(endSyncPacket, (np) -> {
                  endCallback.run();
               });
               synchronized(this) {
                  if (!this.hasQuit) {
                     this.pending.addLast(pendingStartAck);
                     this.pending.addLast(pendingEndAck);
                     this.viewer.send((PacketHandle)bundlePacket);
                  }
               }
            }
         }
      }

      public void synchronize(Runnable callback) {
         this.synchronize(NO_CHANGE_ACK_PACKET, (p) -> {
            callback.run();
         });
      }

      private int getSafeAwaitTeleportId() {
         EntityPlayerHandle handle = EntityPlayerHandle.fromBukkit(this.player);
         int id = handle.getPlayerConnection().getAwaitingTeleportId();
         id -= 20;
         if (id < 0) {
            id += Integer.MAX_VALUE;
         }

         return id;
      }

      static {
         NO_CHANGE_RELATIVE_FLAGS = RelativeFlags.RELATIVE_POSITION_ROTATION.withRelativeDelta().withRelativeDeltaRotation();
         NO_CHANGE_ACK_PACKET = (teleportId) -> {
            return PacketPlayOutPositionHandle.createNew(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0D, 0.0D, 0.0D, NO_CHANGE_RELATIVE_FLAGS, teleportId);
         };
      }
   }

   private static class PendingAcknowledgement {
      private final PacketPlayOutPositionHandle position;
      private final Consumer<PacketPlayOutPositionHandle> callback;

      public PendingAcknowledgement(PacketPlayOutPositionHandle position, Consumer<PacketPlayOutPositionHandle> callback) {
         this.position = position;
         this.callback = callback;
      }

      public int getTeleportId() {
         return this.position.getTeleportWaitTimer();
      }

      public void call() {
         this.callback.accept(this.position);
      }
   }
}
