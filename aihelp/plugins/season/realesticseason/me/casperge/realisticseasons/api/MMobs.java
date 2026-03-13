package me.casperge.realisticseasons.api;

import io.lumine.mythic.bukkit.events.MythicConditionLoadEvent;
import me.casperge.realisticseasons.RealisticSeasons;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MMobs implements Listener {
   private RealisticSeasons main;

   public MMobs(RealisticSeasons var1) {
      this.main = var1;
      Bukkit.getPluginManager().registerEvents(this, var1);
   }

   @EventHandler
   public void onMythicConditionLoad(MythicConditionLoadEvent var1) {
      if (var1.getConditionName().equalsIgnoreCase("SEASON")) {
         var1.register(new SeasonCondition(var1.getConfig(), this.main));
      }

   }
}
