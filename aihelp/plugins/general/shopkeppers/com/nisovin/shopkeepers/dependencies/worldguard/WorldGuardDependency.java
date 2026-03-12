package com.nisovin.shopkeepers.dependencies.worldguard;

import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class WorldGuardDependency {
   public static final String PLUGIN_NAME = "WorldGuard";
   private static final String FLAG_ALLOW_SHOP = "allow-shop";

   @Nullable
   public static Plugin getPlugin() {
      return Bukkit.getPluginManager().getPlugin("WorldGuard");
   }

   public static boolean isPluginEnabled() {
      return Bukkit.getPluginManager().isPluginEnabled("WorldGuard");
   }

   public static void registerAllowShopFlag() {
      if (getPlugin() != null) {
         WorldGuardDependency.Internal.registerAllowShopFlag();
      }
   }

   public static boolean isShopAllowed(@Nullable Player player, Location location) {
      Validate.notNull(location, (String)"location is null");
      Plugin wgPlugin = getPlugin();
      return wgPlugin != null && wgPlugin.isEnabled() ? WorldGuardDependency.Internal.isShopAllowed(wgPlugin, player, location) : true;
   }

   private WorldGuardDependency() {
   }

   private static class Internal {
      public static void registerAllowShopFlag() {
         Log.info("Registering WorldGuard flag 'allow-shop'.");

         try {
            StateFlag allowShopFlag = new StateFlag("allow-shop", false);
            WorldGuard.getInstance().getFlagRegistry().register(allowShopFlag);
         } catch (IllegalStateException | FlagConflictException var1) {
            Log.info("Couldn't register WorldGuard flag 'allow-shop': " + var1.getMessage());
         }

      }

      public static boolean isShopAllowed(Plugin worldGuardPlugin, @Nullable Player player, Location location) {
         assert worldGuardPlugin instanceof WorldGuardPlugin && worldGuardPlugin.isEnabled();

         assert location != null;

         WorldGuardPlugin wgPlugin = (WorldGuardPlugin)worldGuardPlugin;
         RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
         boolean allowShopFlag = false;
         Flag<?> shopFlag = WorldGuard.getInstance().getFlagRegistry().get("allow-shop");
         if (shopFlag != null) {
            if (shopFlag.requiresSubject() && player == null) {
               return true;
            }

            if (shopFlag instanceof StateFlag) {
               allowShopFlag = query.testState(BukkitAdapter.adapt(location), player != null ? wgPlugin.wrapPlayer(player) : null, new StateFlag[]{(StateFlag)shopFlag});
            } else if (shopFlag instanceof BooleanFlag) {
               Boolean shopFlagValue = (Boolean)query.queryValue(BukkitAdapter.adapt(location), player != null ? wgPlugin.wrapPlayer(player) : null, (BooleanFlag)shopFlag);
               allowShopFlag = Boolean.TRUE.equals(shopFlagValue);
            }
         }

         if (Settings.requireWorldGuardAllowShopFlag) {
            return allowShopFlag;
         } else if (allowShopFlag) {
            return true;
         } else {
            return player == null ? true : query.testState(BukkitAdapter.adapt(location), wgPlugin.wrapPlayer(player), new StateFlag[]{Flags.BUILD});
         }
      }
   }
}
