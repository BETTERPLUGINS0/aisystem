package com.bergerkiller.bukkit.tc.controller.global;

import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.utils.ListCallbackCollector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import org.bukkit.entity.Player;

public class TrainCartsPlayerStore implements TrainCarts.Provider {
   private final TrainCarts traincarts;
   private final Map<UUID, TrainCartsPlayer> players = new HashMap();

   public TrainCartsPlayerStore(TrainCarts traincarts) {
      this.traincarts = traincarts;
   }

   public TrainCarts getTrainCarts() {
      return this.traincarts;
   }

   public synchronized TrainCartsPlayer get(UUID playerUUID) {
      return (TrainCartsPlayer)this.players.computeIfAbsent(playerUUID, (u) -> {
         return new TrainCartsPlayer(this.traincarts, u);
      });
   }

   public synchronized TrainCartsPlayer get(Player player) {
      return (TrainCartsPlayer)this.players.computeIfAbsent(player.getUniqueId(), (u) -> {
         return new TrainCartsPlayer(this.traincarts, player);
      });
   }

   public synchronized List<TrainCartsPlayer> find(Predicate<TrainCartsPlayer> condition) {
      ListCallbackCollector<TrainCartsPlayer> collector = new ListCallbackCollector();
      Iterator var3 = this.players.values().iterator();

      while(var3.hasNext()) {
         TrainCartsPlayer player = (TrainCartsPlayer)var3.next();
         if (condition.test(player)) {
            collector.accept(player);
         }
      }

      return collector.result();
   }
}
