package com.bergerkiller.bukkit.tc.controller.player;

import com.bergerkiller.bukkit.common.collections.FastIdentityHashMap;
import com.bergerkiller.bukkit.common.protocol.PlayerGameInfo;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.controller.player.network.PacketQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TrainCartsAttachmentViewerMap {
   private final TrainCarts plugin;
   private final FastIdentityHashMap<Player, TrainCartsAttachmentViewer> viewers = new FastIdentityHashMap();
   private final List<PacketQueue> queuesList = new ArrayList();

   public TrainCartsAttachmentViewerMap(TrainCarts plugin) {
      this.plugin = plugin;
   }

   public synchronized TrainCartsAttachmentViewer getViewer(Player player) {
      TrainCartsAttachmentViewer viewer = (TrainCartsAttachmentViewer)this.viewers.get(player);
      if (viewer == null) {
         PlayerGameInfo playerGameInfo = PlayerGameInfo.of(player);
         PacketQueue packetQueue;
         if (!player.isValid() && Bukkit.getPlayer(player.getUniqueId()) != player) {
            packetQueue = PacketQueue.createNoOp(this.plugin, player);
            viewer = new TrainCartsAttachmentViewer(this.plugin, player, playerGameInfo, packetQueue);
         } else {
            packetQueue = PacketQueue.create(this.plugin, player, playerGameInfo);
            viewer = new TrainCartsAttachmentViewer(this.plugin, player, playerGameInfo, packetQueue);
            this.viewers.put(player, viewer);
            this.queuesList.add(packetQueue);
         }
      }

      return viewer;
   }

   public synchronized void remove(Player player) {
      TrainCartsAttachmentViewer viewer = (TrainCartsAttachmentViewer)this.viewers.remove(player);
      if (viewer != null) {
         PacketQueue packetQueue = viewer.getPacketQueue();
         this.queuesList.remove(packetQueue);
         packetQueue.abort();
      }

   }

   public synchronized void forAllPacketQueues(Consumer<PacketQueue> operation) {
      this.queuesList.forEach(operation);
   }
}
