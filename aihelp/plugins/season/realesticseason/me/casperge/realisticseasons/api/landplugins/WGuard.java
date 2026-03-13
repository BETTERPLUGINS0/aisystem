package me.casperge.realisticseasons.api.landplugins;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.association.RegionAssociable;
import com.sk89q.worldguard.protection.flags.EnumFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.IntegerFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.season.Season;
import org.bukkit.World;

public class WGuard implements LandPlugin {
   private static StateFlag SEASON_EFFECTS;
   private static StateFlag BLOCK_CHANGES;
   private static StateFlag MOB_SPAWNING;
   private static EnumFlag<WGSeason> SEASON_FLAG;
   private static IntegerFlag TEMPERATURE;
   private static IntegerFlag TEMPERATUREMODIFIER;
   private RealisticSeasons main;
   private static Priority priority;

   public WGuard(RealisticSeasons var1) {
      this.main = var1;
      this.registerFlags();
   }

   private void registerFlags() {
      FlagRegistry var1 = WorldGuard.getInstance().getFlagRegistry();
      EnumFlag var2 = new EnumFlag("permanent-season", WGSeason.class);
      var1.register(var2);
      SEASON_FLAG = var2;
      IntegerFlag var3 = new IntegerFlag("permanent-temperature");
      var1.register(var3);
      TEMPERATURE = var3;
      IntegerFlag var4 = new IntegerFlag("temperature-modifier");
      var1.register(var4);
      TEMPERATUREMODIFIER = var4;

      StateFlag var5;
      Flag var6;
      try {
         var5 = new StateFlag("season-effects", true);
         var1.register(var5);
         SEASON_EFFECTS = var5;
      } catch (FlagConflictException var9) {
         var6 = var1.get("season-effects");
         if (var6 instanceof StateFlag) {
            SEASON_EFFECTS = (StateFlag)var6;
         }
      }

      try {
         var5 = new StateFlag("season-block-changes", true);
         var1.register(var5);
         BLOCK_CHANGES = var5;
      } catch (FlagConflictException var8) {
         var6 = var1.get("season-block-changes");
         if (var6 instanceof StateFlag) {
            BLOCK_CHANGES = (StateFlag)var6;
         }
      }

      try {
         var5 = new StateFlag("season-mob-spawning", true);
         var1.register(var5);
         MOB_SPAWNING = var5;
      } catch (FlagConflictException var7) {
         var6 = var1.get("season-mob-spawning");
         if (var6 instanceof StateFlag) {
            MOB_SPAWNING = (StateFlag)var6;
         }
      }

   }

   public boolean hasBlockChanges(int var1, int var2, World var3) {
      if (!this.getPermanentSeason(var1, var2, var3).equals(Season.DISABLED)) {
         return false;
      } else {
         ApplicableRegionSet var4 = this.getApplicableRegions(var1, var2, var3);
         return var4 == null ? false : var4.testState((RegionAssociable)null, new StateFlag[]{BLOCK_CHANGES});
      }
   }

   public boolean hasMobSpawns(int var1, int var2, World var3) {
      if (!this.getPermanentSeason(var1, var2, var3).equals(Season.DISABLED)) {
         return false;
      } else {
         ApplicableRegionSet var4 = this.getApplicableRegions(var1, var2, var3);
         return var4 == null ? false : var4.testState((RegionAssociable)null, new StateFlag[]{MOB_SPAWNING});
      }
   }

   public boolean hasSeasonEffects(int var1, int var2, World var3) {
      ApplicableRegionSet var4 = this.getApplicableRegions(var1, var2, var3);
      if (var4 == null) {
         return true;
      } else {
         return !this.getPermanentSeason(var1, var2, var3).equals(Season.DISABLED) ? false : var4.testState((RegionAssociable)null, new StateFlag[]{SEASON_EFFECTS});
      }
   }

   public Integer getPermanentTemperature(int var1, int var2, World var3) {
      ApplicableRegionSet var4 = this.getApplicableRegions(var1, var2, var3);
      return var4 == null ? null : (Integer)var4.queryValue((RegionAssociable)null, TEMPERATURE);
   }

   public Integer getTemperatureModifier(int var1, int var2, World var3) {
      ApplicableRegionSet var4 = this.getApplicableRegions(var1, var2, var3);
      return var4 == null ? null : (Integer)var4.queryValue((RegionAssociable)null, TEMPERATUREMODIFIER);
   }

   public Season getPermanentSeason(int var1, int var2, World var3) {
      ApplicableRegionSet var4 = this.getApplicableRegions(var1, var2, var3);
      if (var4 == null) {
         return Season.DISABLED;
      } else {
         WGSeason var5 = (WGSeason)var4.queryValue((RegionAssociable)null, SEASON_FLAG);
         return var5 == null ? Season.DISABLED : WGSeason.getRSSeason(var5);
      }
   }

   private ApplicableRegionSet getApplicableRegions(int var1, int var2, World var3) {
      var1 <<= 4;
      var2 <<= 4;
      RegionContainer var4 = WorldGuard.getInstance().getPlatform().getRegionContainer();
      RegionManager var5;
      if (var4 != null && (var5 = var4.get(BukkitAdapter.adapt(var3))) != null) {
         BlockVector3 var6 = BlockVector3.at(var1, this.main.getNMSUtils().getMinHeight(var3), var2);
         BlockVector3 var7 = BlockVector3.at(var1 + 15, this.main.getNMSUtils().getMaxHeight(var3), var2 + 15);
         ProtectedCuboidRegion var8 = new ProtectedCuboidRegion("ThisIsAnId", var6, var7);
         return var5.getApplicableRegions(var8);
      } else {
         return null;
      }
   }

   public Priority getPriority() {
      return priority;
   }

   static {
      priority = Priority.LOW;
   }
}
