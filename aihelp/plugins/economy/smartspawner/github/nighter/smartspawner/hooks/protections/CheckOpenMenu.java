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

public class CheckOpenMenu {
   public static boolean CanPlayerOpenMenu(@NotNull Player player, @NotNull Location location) {
      if (!player.isOp() && !player.hasPermission("*")) {
         IntegrationManager integrationManager = SmartSpawner.getInstance().getIntegrationManager();
         if (integrationManager.isHasGriefPrevention() && !GriefPrevention.canPlayerOpenMenuOnClaim(player, location)) {
            return false;
         } else if (integrationManager.isHasWorldGuard() && !WorldGuard.canPlayerInteractInRegion(player, location)) {
            return false;
         } else if (integrationManager.isHasLands() && !Lands.CanPlayerInteractContainer(player, location)) {
            return false;
         } else if (integrationManager.isHasTowny() && !Towny.canPlayerInteractSpawner(player, location)) {
            return false;
         } else if (integrationManager.isHasSuperiorSkyblock2() && SuperiorSkyblock2.canPlayerOpenMenu(player, location)) {
            return false;
         } else if (integrationManager.isHasBentoBox() && !BentoBoxAPI.canPlayerOpenMenu(player, location)) {
            return false;
         } else if (integrationManager.isHasSimpleClaimSystem() && !SimpleClaimSystem.canPlayerOpenMenuOnClaim(player, location)) {
            return false;
         } else if (integrationManager.isHasMinePlots() && !MinePlots.canPlayerOpenMenu(player, location)) {
            return false;
         } else if (integrationManager.isHasIridiumSkyblock() && !IridiumSkyblock.canPlayerOpenMenu(player, location)) {
            return false;
         } else if (integrationManager.isHasPlotSquared() && !PlotSquared.canInteract(player, location)) {
            return false;
         } else if (integrationManager.isHasResidence() && !Residence.canInteract(player, location)) {
            return false;
         } else {
            return !integrationManager.isHasRedProtect() || RedProtectAPI.canPlayerOpenMenuOnClaim(player, location);
         }
      } else {
         return true;
      }
   }
}
