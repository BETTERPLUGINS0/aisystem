package com.nisovin.shopkeepers.dependencies.towny;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownBlockType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class TownyDependency {
   public static final String PLUGIN_NAME = "Towny";

   @Nullable
   public static Plugin getPlugin() {
      return Bukkit.getPluginManager().getPlugin("Towny");
   }

   public static boolean isPluginEnabled() {
      return Bukkit.getPluginManager().isPluginEnabled("Towny");
   }

   public static boolean isCommercialArea(Location location) {
      if (!isPluginEnabled()) {
         return false;
      } else {
         TownBlock townBlock = TownyAPI.getInstance().getTownBlock(location);
         return townBlock != null && townBlock.getType() == TownBlockType.COMMERCIAL;
      }
   }

   private TownyDependency() {
   }
}
