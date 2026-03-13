package me.casperge.realisticseasons.api.landplugins;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.season.Season;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

public class GriefPrevention implements LandPlugin {
   private RealisticSeasons main;
   private static Priority priority;
   me.ryanhamshire.GriefPrevention.GriefPrevention inst;
   boolean seasons = true;
   boolean blockChanges = true;
   boolean mobSpawns = true;
   boolean fixedTemp = false;
   int fixedTempValue = 25;

   public GriefPrevention(RealisticSeasons var1) {
      this.main = var1;
      this.load();
   }

   private void load() {
      Bukkit.getLogger().info("[RealisticSeasons] Loading GriefPrevention integration");
      File var1 = new File(this.main.getDataFolder(), "griefprevention.yml");
      if (!var1.exists()) {
         try {
            InputStream var2 = this.main.getResource("griefprevention.yml");
            FileUtils.copyInputStreamToFile(var2, var1);
         } catch (IOException var3) {
            var3.printStackTrace();
         }
      }

      YamlConfiguration var4 = YamlConfiguration.loadConfiguration(var1);
      this.seasons = var4.getBoolean("claimed-areas.seasons-enabled");
      this.blockChanges = var4.getBoolean("claimed-areas.seasonal-block-changes");
      this.mobSpawns = var4.getBoolean("claimed-areas.seasonal-mob-spawns");
      this.fixedTemp = var4.getBoolean("claimed-areas.fixed-temperature.enabled");
      this.fixedTempValue = var4.getInt("claimed-areas.fixed-temperature.value");
   }

   public boolean hasBlockChanges(int var1, int var2, World var3) {
      return this.blockChanges || this.getGPInstance().dataStore.getClaims(var1, var2).isEmpty();
   }

   public boolean hasMobSpawns(int var1, int var2, World var3) {
      return this.mobSpawns || this.getGPInstance().dataStore.getClaims(var1, var2).isEmpty();
   }

   public boolean hasSeasonEffects(int var1, int var2, World var3) {
      return this.seasons || this.getGPInstance().dataStore.getClaims(var1, var2).isEmpty();
   }

   public Integer getPermanentTemperature(int var1, int var2, World var3) {
      return this.fixedTemp && !this.getGPInstance().dataStore.getClaims(var1, var2).isEmpty() ? this.fixedTempValue : null;
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

   private me.ryanhamshire.GriefPrevention.GriefPrevention getGPInstance() {
      if (this.inst == null) {
         this.inst = me.ryanhamshire.GriefPrevention.GriefPrevention.instance;
      }

      return this.inst;
   }

   static {
      priority = Priority.MEDIUM;
   }
}
