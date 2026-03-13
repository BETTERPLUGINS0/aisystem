package me.casperge.realisticseasons.api.landplugins;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.season.Season;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

public class Factions implements LandPlugin {
   private RealisticSeasons main;
   private static Priority priority;
   HashMap<Factions.Zones, Boolean> seasons = new HashMap();
   HashMap<Factions.Zones, Boolean> hasBlockChanges = new HashMap();
   HashMap<Factions.Zones, Boolean> mobSpawns = new HashMap();
   HashMap<Factions.Zones, Boolean> fixedTemp = new HashMap();
   HashMap<Factions.Zones, Integer> fixedTempValues = new HashMap();

   public Factions(RealisticSeasons var1) {
      this.main = var1;
      this.load();
   }

   private void load() {
      Bukkit.getLogger().info("[RealisticSeasons] Loading Factions integration");
      File var1 = new File(this.main.getDataFolder(), "factions.yml");
      if (!var1.exists()) {
         try {
            InputStream var2 = this.main.getResource("factions.yml");
            FileUtils.copyInputStreamToFile(var2, var1);
         } catch (IOException var7) {
            var7.printStackTrace();
         }
      }

      YamlConfiguration var8 = YamlConfiguration.loadConfiguration(var1);
      Factions.Zones[] var3 = Factions.Zones.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Factions.Zones var6 = var3[var5];
         this.seasons.put(var6, var8.getBoolean(var6.getConf() + ".seasons-enabled"));
         this.hasBlockChanges.put(var6, var8.getBoolean(var6.getConf() + ".seasonal-block-changes"));
         this.mobSpawns.put(var6, var8.getBoolean(var6.getConf() + ".seasonal-mob-spawns"));
         this.fixedTemp.put(var6, var8.getBoolean(var6.getConf() + ".fixed-temperature.enabled"));
         this.fixedTempValues.put(var6, var8.getInt(var6.getConf() + ".fixed-temperature.value"));
      }

   }

   public boolean hasBlockChanges(int var1, int var2, World var3) {
      FLocation var4 = new FLocation(var3.getName(), var1, var2);
      Faction var5 = Board.getInstance().getFactionAt(var4);
      if (var5 != null) {
         if (var5.isWarZone()) {
            if (!(Boolean)this.hasBlockChanges.get(Factions.Zones.WARZONE)) {
               return false;
            }
         } else if (var5.isSafeZone()) {
            if (!(Boolean)this.hasBlockChanges.get(Factions.Zones.SAFEZONE)) {
               return false;
            }
         } else if (var5.isWilderness()) {
            if (!(Boolean)this.hasBlockChanges.get(Factions.Zones.WILDERNESS)) {
               return false;
            }
         } else if (!(Boolean)this.hasBlockChanges.get(Factions.Zones.FACTION)) {
            return false;
         }
      }

      return true;
   }

   public boolean hasMobSpawns(int var1, int var2, World var3) {
      FLocation var4 = new FLocation(var3.getName(), var1, var2);
      Faction var5 = Board.getInstance().getFactionAt(var4);
      if (var5 != null) {
         if (var5.isWarZone()) {
            if (!(Boolean)this.mobSpawns.get(Factions.Zones.WARZONE)) {
               return false;
            }
         } else if (var5.isSafeZone()) {
            if (!(Boolean)this.mobSpawns.get(Factions.Zones.SAFEZONE)) {
               return false;
            }
         } else if (var5.isWilderness()) {
            if (!(Boolean)this.mobSpawns.get(Factions.Zones.WILDERNESS)) {
               return false;
            }
         } else if (!(Boolean)this.mobSpawns.get(Factions.Zones.FACTION)) {
            return false;
         }
      }

      return true;
   }

   public boolean hasSeasonEffects(int var1, int var2, World var3) {
      FLocation var4 = new FLocation(var3.getName(), var1, var2);
      Faction var5 = Board.getInstance().getFactionAt(var4);
      if (var5 != null) {
         if (var5.isWarZone()) {
            if (!(Boolean)this.seasons.get(Factions.Zones.WARZONE)) {
               return false;
            }
         } else if (var5.isSafeZone()) {
            if (!(Boolean)this.seasons.get(Factions.Zones.SAFEZONE)) {
               return false;
            }
         } else if (var5.isWilderness()) {
            if (!(Boolean)this.seasons.get(Factions.Zones.WILDERNESS)) {
               return false;
            }
         } else if (!(Boolean)this.seasons.get(Factions.Zones.FACTION)) {
            return false;
         }
      }

      return true;
   }

   public Integer getPermanentTemperature(int var1, int var2, World var3) {
      FLocation var4 = new FLocation(var3.getName(), var1, var2);
      Faction var5 = Board.getInstance().getFactionAt(var4);
      if (var5 != null) {
         if (var5.isWarZone()) {
            if ((Boolean)this.fixedTemp.get(Factions.Zones.WARZONE)) {
               return (Integer)this.fixedTempValues.get(Factions.Zones.WARZONE);
            }
         } else if (var5.isSafeZone()) {
            if ((Boolean)this.fixedTemp.get(Factions.Zones.SAFEZONE)) {
               return (Integer)this.fixedTempValues.get(Factions.Zones.SAFEZONE);
            }
         } else if (var5.isWilderness()) {
            if ((Boolean)this.fixedTemp.get(Factions.Zones.WILDERNESS)) {
               return (Integer)this.fixedTempValues.get(Factions.Zones.WILDERNESS);
            }
         } else if ((Boolean)this.fixedTemp.get(Factions.Zones.FACTION)) {
            return (Integer)this.fixedTempValues.get(Factions.Zones.FACTION);
         }
      }

      return null;
   }

   public Integer getTemperatureModifier(int var1, int var2, World var3) {
      return null;
   }

   public Season getPermanentSeason(int var1, int var2, World var3) {
      return Season.DISABLED;
   }

   public Priority getPriority() {
      return priority;
   }

   static {
      priority = Priority.HIGH;
   }

   static enum Zones {
      WILDERNESS("wilderness"),
      WARZONE("warzone"),
      SAFEZONE("safezone"),
      FACTION("claimed-areas");

      String conf;

      private Zones(String param3) {
         this.conf = var3;
      }

      public String getConf() {
         return this.conf;
      }

      // $FF: synthetic method
      private static Factions.Zones[] $values() {
         return new Factions.Zones[]{WILDERNESS, WARZONE, SAFEZONE, FACTION};
      }
   }
}
