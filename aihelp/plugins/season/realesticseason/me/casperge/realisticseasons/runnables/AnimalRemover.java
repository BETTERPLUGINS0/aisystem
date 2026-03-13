package me.casperge.realisticseasons.runnables;

import java.util.Iterator;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.season.Season;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.scheduler.BukkitRunnable;

public class AnimalRemover extends BukkitRunnable {
   private RealisticSeasons main;

   public AnimalRemover(RealisticSeasons var1) {
      this.main = var1;
   }

   public void run() {
      if (this.main.getSettings().removeAnimalsOnSeasonChange) {
         Iterator var1 = Bukkit.getWorlds().iterator();

         while(var1.hasNext()) {
            World var2 = (World)var1.next();
            if (var2.getEnvironment() != Environment.NETHER && var2.getEnvironment() != Environment.THE_END && this.main.getSeasonManager().getSeason(var2) != Season.DISABLED) {
               this.main.getAnimalUtils().checkRandomEntity(this.main.getSeasonManager().getSeason(var2), var2);
            }
         }
      } else {
         this.cancel();
      }

   }
}
