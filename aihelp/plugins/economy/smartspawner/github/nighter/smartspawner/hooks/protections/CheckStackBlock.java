package github.nighter.smartspawner.hooks.protections;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.hooks.IntegrationManager;
import github.nighter.smartspawner.hooks.protections.api.BentoBoxAPI;
import github.nighter.smartspawner.hooks.protections.api.GriefPrevention;
import github.nighter.smartspawner.hooks.protections.api.IridiumSkyblock;
import github.nighter.smartspawner.hooks.protections.api.Lands;
import github.nighter.smartspawner.hooks.protections.api.MinePlots;
import github.nighter.smartspawner.hooks.protections.api.PlotSquared;
import github.nighter.smartspawner.hooks.protections.api.RedProtectAPI;
import github.nighter.smartspawner.hooks.protections.api.Residence;
import github.nighter.smartspawner.hooks.protections.api.SimpleClaimSystem;
import github.nighter.smartspawner.hooks.protections.api.SuperiorSkyblock2;
import github.nighter.smartspawner.hooks.protections.api.Towny;
import github.nighter.smartspawner.hooks.protections.api.WorldGuard;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CheckStackBlock {
   public static boolean CanPlayerPlaceBlock(@NotNull Player player, @NotNull Location location) {
      if (!player.isOp() && !player.hasPermission("*")) {
         IntegrationManager integrationManager = SmartSpawner.getInstance().getIntegrationManager();
         if (integrationManager.isHasGriefPrevention() && !GriefPrevention.canPlayerStackClaimBlock(player, location)) {
            return false;
         } else if (integrationManager.isHasWorldGuard() && !WorldGuard.canPlayerStackBlockInRegion(player, location)) {
            return false;
         } else if (integrationManager.isHasLands() && !Lands.canPlayerStackClaimBlock(player, location)) {
            return false;
         } else if (integrationManager.isHasTowny() && !Towny.canPlayerInteractSpawner(player, location)) {
            return false;
         } else if (integrationManager.isHasSuperiorSkyblock2() && SuperiorSkyblock2.canPlayerStackBlock(player, location)) {
            return false;
         } else if (integrationManager.isHasBentoBox() && !BentoBoxAPI.canPlayerStackBlock(player, location)) {
            return false;
         } else if (integrationManager.isHasSimpleClaimSystem() && !SimpleClaimSystem.canPlayerStackClaimBlock(player, location)) {
            return false;
         } else if (integrationManager.isHasMinePlots() && !MinePlots.canPlayerStackBlock(player, location)) {
            return false;
         } else if (integrationManager.isHasIridiumSkyblock() && !IridiumSkyblock.canPlayerStackBlock(player, location)) {
            return false;
         } else if (integrationManager.isHasPlotSquared() && !PlotSquared.canInteract(player, location)) {
            return false;
         } else if (integrationManager.isHasResidence() && !Residence.canStack(player, location)) {
            return false;
         } else {
            return !integrationManager.isHasRedProtect() || RedProtectAPI.canPlayerStackClaimBlock(player, location);
         }
      } else {
         return true;
      }
   }
}
