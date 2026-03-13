package github.nighter.smartspawner.hooks.protections.api;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WorldGuard {
   public static boolean canPlayerBreakBlockInRegion(@NotNull Player player, @NotNull Location location) {
      if (!player.isOp() && !player.hasPermission("worldguard.region.bypass")) {
         LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
         com.sk89q.worldedit.util.Location loc = new com.sk89q.worldedit.util.Location(BukkitAdapter.adapt(location.getWorld()), location.getX(), location.getY(), location.getZ());
         RegionContainer container = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer();
         RegionQuery query = container.createQuery();
         return query.testBuild(loc, localPlayer, new StateFlag[]{Flags.BLOCK_BREAK});
      } else {
         return true;
      }
   }

   public static boolean canPlayerStackBlockInRegion(@NotNull Player player, @NotNull Location location) {
      if (!player.isOp() && !player.hasPermission("worldguard.region.bypass")) {
         LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapOfflinePlayer(player);
         com.sk89q.worldedit.util.Location loc = new com.sk89q.worldedit.util.Location(BukkitAdapter.adapt(location.getWorld()), location.getX(), location.getY(), location.getZ());
         RegionContainer container = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer();
         RegionQuery query = container.createQuery();
         return query.testBuild(loc, localPlayer, new StateFlag[]{Flags.BLOCK_PLACE});
      } else {
         return true;
      }
   }

   public static boolean canPlayerInteractInRegion(@NotNull Player player, Location location) {
      if (!player.isOp() && !player.hasPermission("worldguard.region.bypass")) {
         LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
         com.sk89q.worldedit.util.Location loc = new com.sk89q.worldedit.util.Location(BukkitAdapter.adapt(location.getWorld()), location.getX(), location.getY(), location.getZ());
         RegionContainer container = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer();
         RegionQuery query = container.createQuery();
         return query.testBuild(loc, localPlayer, new StateFlag[]{Flags.INTERACT});
      } else {
         return true;
      }
   }
}
