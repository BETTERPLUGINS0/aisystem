package me.casperge.realisticseasons.api.landplugins;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.flags.enums.FlagTarget;
import me.angeschossen.lands.api.flags.type.NaturalFlag;
import me.angeschossen.lands.api.land.LandWorld;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.season.Season;
import me.casperge.realisticseasons.utils.JavaUtils;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class Lands implements LandPlugin {
   LandsIntegration api;
   private RealisticSeasons main;
   private static Priority priority;
   NaturalFlag seasonEffects;
   NaturalFlag seasonBlockChanges;
   NaturalFlag seasonMobSpawning;
   NaturalFlag fixedTemperature;
   int fixedTemp = 25;

   public Lands(RealisticSeasons var1) {
      this.main = var1;
      this.api = LandsIntegration.of(var1);
      this.registerFlags();
   }

   private void registerFlags() {
      Bukkit.getLogger().info("[RealisticSeasons] Loading Lands' flags");
      File var1 = new File(this.main.getDataFolder(), "lands.yml");
      if (!var1.exists()) {
         try {
            InputStream var2 = this.main.getResource("lands.yml");
            FileUtils.copyInputStreamToFile(var2, var1);
         } catch (IOException var3) {
            var3.printStackTrace();
         }
      }

      JavaUtils.saveDefaultConfigValues("/lands.yml", "lands.yml");
      YamlConfiguration var4 = YamlConfiguration.loadConfiguration(var1);
      this.seasonEffects = NaturalFlag.of(this.api, FlagTarget.valueOf(var4.getString("flags.seasons.flag-target")), "seasons");
      ((NaturalFlag)((NaturalFlag)((NaturalFlag)this.seasonEffects.setDisplayName(var4.getString("flags.seasons.name"))).setIcon(new ItemStack(Material.valueOf(var4.getString("flags.seasons.item"))))).setDescription(var4.getString("flags.seasons.description"))).setDefaultState(true);
      this.seasonBlockChanges = NaturalFlag.of(this.api, FlagTarget.valueOf(var4.getString("flags.seasonblocks.flag-target")), "seasonblocks");
      ((NaturalFlag)((NaturalFlag)this.seasonBlockChanges.setDefaultState(true).setDisplayName(var4.getString("flags.seasonblocks.name"))).setIcon(new ItemStack(Material.valueOf(var4.getString("flags.seasonblocks.item"))))).setDescription(var4.getString("flags.seasonblocks.description"));
      this.seasonMobSpawning = NaturalFlag.of(this.api, FlagTarget.valueOf(var4.getString("flags.seasonmobs.flag-target")), "seasonmobs");
      ((NaturalFlag)((NaturalFlag)this.seasonMobSpawning.setDefaultState(true).setDisplayName(var4.getString("flags.seasonmobs.name"))).setIcon(new ItemStack(Material.valueOf(var4.getString("flags.seasonmobs.item"))))).setDescription(var4.getString("flags.seasonmobs.description"));
      this.fixedTemperature = NaturalFlag.of(this.api, FlagTarget.valueOf(var4.getString("flags.temperature.flag-target")), "temperature");
      ((NaturalFlag)((NaturalFlag)this.fixedTemperature.setDefaultState(true).setDisplayName(var4.getString("flags.temperature.name"))).setIcon(new ItemStack(Material.valueOf(var4.getString("flags.temperature.item"))))).setDescription(var4.getString("flags.temperature.description"));
      this.fixedTemp = var4.getInt("flags.temperature.temperature");
   }

   public boolean hasBlockChanges(int var1, int var2, World var3) {
      LandWorld var4 = this.api.getWorld(var3);
      if (var4 != null) {
         var1 <<= 4;
         var2 <<= 4;
         return var4.hasNaturalFlag(var3.getHighestBlockAt(var1, var2).getLocation(), this.seasonBlockChanges);
      } else {
         return true;
      }
   }

   public boolean hasMobSpawns(int var1, int var2, World var3) {
      LandWorld var4 = this.api.getWorld(var3);
      if (var4 != null) {
         var1 <<= 4;
         var2 <<= 4;
         return var4.hasNaturalFlag(var3.getHighestBlockAt(var1, var2).getLocation(), this.seasonMobSpawning);
      } else {
         return true;
      }
   }

   public boolean hasSeasonEffects(int var1, int var2, World var3) {
      LandWorld var4 = this.api.getWorld(var3);
      if (var4 != null) {
         var1 <<= 4;
         var2 <<= 4;
         return var4.hasNaturalFlag(var3.getHighestBlockAt(var1, var2).getLocation(), this.seasonEffects);
      } else {
         return true;
      }
   }

   public Integer getPermanentTemperature(int var1, int var2, World var3) {
      LandWorld var4 = this.api.getWorld(var3);
      if (var4 != null) {
         var1 <<= 4;
         var2 <<= 4;
         return !var4.hasNaturalFlag(var3.getHighestBlockAt(var1, var2).getLocation(), this.fixedTemperature) ? this.fixedTemp : null;
      } else {
         return null;
      }
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
      priority = Priority.LOWEST;
   }
}
