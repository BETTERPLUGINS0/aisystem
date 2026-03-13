package me.ag4.playershop.hooks;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.ag4.playershop.PlayerShop;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class WorldGuardAPI {
   public static StateFlag playershop;

   public static void setupWorldGuardFlags() {
      try {
         FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
         StateFlag flag = new StateFlag("playershop", true);
         registry.register(flag);
         playershop = flag;
      } catch (NoClassDefFoundError var2) {
      }

   }

   public static boolean getFlagState(Block block, Player player) {
      RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
      Location loc = BukkitAdapter.adapt(block.getLocation());
      LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
      RegionQuery query = container.createQuery();
      ApplicableRegionSet set = query.getApplicableRegions(loc);
      return PlayerShop.getInstance().getConfig().getBoolean("WorldGuard.Enable") && set.testState(localPlayer, new StateFlag[]{playershop});
   }
}
