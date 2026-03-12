package ac.grim.grimac.platform.bukkit.scheduler.folia;

import ac.grim.grimac.platform.api.scheduler.PlatformScheduler;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import lombok.Generated;

public class FoliaPlatformScheduler implements PlatformScheduler {
   @NotNull
   private final FoliaAsyncScheduler asyncScheduler = new FoliaAsyncScheduler();
   @NotNull
   private final FoliaGlobalRegionScheduler globalRegionScheduler = new FoliaGlobalRegionScheduler();
   @NotNull
   private final FoliaEntityScheduler entityScheduler = new FoliaEntityScheduler();
   @NotNull
   private final FoliaRegionScheduler regionScheduler = new FoliaRegionScheduler();

   @NotNull
   @Generated
   public FoliaAsyncScheduler getAsyncScheduler() {
      return this.asyncScheduler;
   }

   @NotNull
   @Generated
   public FoliaGlobalRegionScheduler getGlobalRegionScheduler() {
      return this.globalRegionScheduler;
   }

   @NotNull
   @Generated
   public FoliaEntityScheduler getEntityScheduler() {
      return this.entityScheduler;
   }

   @NotNull
   @Generated
   public FoliaRegionScheduler getRegionScheduler() {
      return this.regionScheduler;
   }
}
