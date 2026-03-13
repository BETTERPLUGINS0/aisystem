package github.nighter.smartspawner.hooks.protections;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.hooks.IntegrationManager;
import github.nighter.smartspawner.hooks.protections.api.GriefPrevention;
import github.nighter.smartspawner.hooks.protections.api.Lands;
import github.nighter.smartspawner.hooks.protections.api.MinePlots;
import github.nighter.smartspawner.hooks.protections.api.PlotSquared;
import github.nighter.smartspawner.hooks.protections.api.Residence;
import github.nighter.smartspawner.hooks.protections.api.SimpleClaimSystem;
import github.nighter.smartspawner.hooks.protections.api.Towny;
import github.nighter.smartspawner.hooks.protections.api.WorldGuard;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CheckBreakBlock {
   public static boolean CanPlayerBreakBlock(@NotNull Player player, @NotNull Location location) {
      if (!player.isOp() && !player.hasPermission("*")) {
         IntegrationManager integrationManager = SmartSpawner.getInstance().getIntegrationManager();
         if (integrationManager.isHasGriefPrevention() && !GriefPrevention.canPlayerBreakClaimBlock(player, location)) {
            return false;
         } else if (integrationManager.isHasWorldGuard() && !WorldGuard.canPlayerBreakBlockInRegion(player, location)) {
            return false;
         } else if (integrationManager.isHasLands() && !Lands.canPlayerBreakClaimBlock(player, location)) {
            return false;
         } else if (integrationManager.isHasTowny() && !Towny.canPlayerInteractSpawner(player, location)) {
            return false;
         } else if (integrationManager.isHasSimpleClaimSystem() && !SimpleClaimSystem.canPlayerBreakClaimBlock(player, location)) {
            return false;
         } else if (integrationManager.isHasPlotSquared() && !PlotSquared.canInteract(player, location)) {
            return false;
         } else if (integrationManager.isHasResidence() && !Residence.canPlayerBreakBlock(player, location)) {
            return false;
         } else {
            return !integrationManager.isHasMinePlots() || MinePlots.canPlayerBreakBlock(player, location);
         }
      } else {
         return true;
      }
   }
}
