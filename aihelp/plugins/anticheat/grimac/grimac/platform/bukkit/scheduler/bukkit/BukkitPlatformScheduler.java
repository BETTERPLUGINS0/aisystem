package ac.grim.grimac.platform.bukkit.scheduler.bukkit;

import ac.grim.grimac.platform.api.scheduler.PlatformScheduler;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import lombok.Generated;

public class BukkitPlatformScheduler implements PlatformScheduler {
   @NotNull
   private final BukkitAsyncScheduler asyncScheduler = new BukkitAsyncScheduler();
   @NotNull
   private final BukkitGlobalRegionScheduler globalRegionScheduler = new BukkitGlobalRegionScheduler();
   @NotNull
   private final BukkitEntityScheduler entityScheduler = new BukkitEntityScheduler();
   @NotNull
   private final BukkitRegionScheduler regionScheduler = new BukkitRegionScheduler();

   @NotNull
   @Generated
   public BukkitAsyncScheduler getAsyncScheduler() {
      return this.asyncScheduler;
   }

   @NotNull
   @Generated
   public BukkitGlobalRegionScheduler getGlobalRegionScheduler() {
      return this.globalRegionScheduler;
   }

   @NotNull
   @Generated
   public BukkitEntityScheduler getEntityScheduler() {
      return this.entityScheduler;
   }

   @NotNull
   @Generated
   public BukkitRegionScheduler getRegionScheduler() {
      return this.regionScheduler;
   }
}
