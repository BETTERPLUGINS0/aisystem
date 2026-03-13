package me.casperge.realisticseasons.api;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.IEntityCondition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.season.Season;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;

public class SeasonCondition implements IEntityCondition {
   private RealisticSeasons main;
   private List<Season> seasons;
   private List<Season> allSeasons;

   public SeasonCondition(MythicLineConfig var1, RealisticSeasons var2) {
      this.allSeasons = Arrays.asList(Season.WINTER, Season.SUMMER, Season.FALL, Season.SPRING);
      this.main = var2;
      String var3 = var1.getString(new String[]{"season", "s"});
      if (var3 != null) {
         String[] var4 = var3.split(",");
         ArrayList var5 = new ArrayList();
         Season[] var6 = Season.values();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            Season var9 = var6[var8];
            String[] var10 = var4;
            int var11 = var4.length;

            for(int var12 = 0; var12 < var11; ++var12) {
               String var13 = var10[var12];
               if (var13.trim().equalsIgnoreCase(var9.getConfigName())) {
                  var5.add(var9);
               }
            }
         }

         if (var5.isEmpty()) {
            Bukkit.getLogger().severe("[RealisticSeasons] Could not load MythicMobs condition: season(s) named " + var3 + " not found.");
         } else {
            this.seasons = var5;
         }
      }
   }

   public boolean check(AbstractEntity var1) {
      if (this.seasons == null) {
         return false;
      } else if (var1 != null) {
         World var7 = Bukkit.getWorld(var1.getLocation().getWorld().getName());
         if (var7 == null) {
            return false;
         } else {
            Environment var8 = var7.getEnvironment();
            if (!var8.equals(Environment.THE_END) && !var8.equals(Environment.NETHER)) {
               Season var9 = this.main.getSeasonManager().getSeason(var7);
               Iterator var11 = this.seasons.iterator();

               Season var6;
               do {
                  if (!var11.hasNext()) {
                     return false;
                  }

                  var6 = (Season)var11.next();
               } while(var6 != var9);

               return true;
            } else {
               return true;
            }
         }
      } else {
         int var2 = 0;
         Season var3 = null;
         Iterator var4 = this.main.getSeasonManager().worldData.keySet().iterator();

         while(var4.hasNext()) {
            World var5 = (World)var4.next();
            if (this.allSeasons.contains(this.main.getSeasonManager().worldData.get(var5))) {
               ++var2;
               var3 = (Season)this.main.getSeasonManager().worldData.get(var5);
            }
         }

         if (var2 == 1 || var2 >= 1 && this.main.getSettings().syncedWorlds.size() > 1) {
            var4 = this.seasons.iterator();

            while(var4.hasNext()) {
               Season var10 = (Season)var4.next();
               if (var10 == var3) {
                  return true;
               }
            }
         }

         return false;
      }
   }
}
