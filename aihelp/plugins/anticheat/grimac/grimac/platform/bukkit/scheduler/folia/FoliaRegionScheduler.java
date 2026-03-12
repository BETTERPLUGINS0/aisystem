package ac.grim.grimac.platform.bukkit.scheduler.folia;

import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.platform.api.scheduler.RegionScheduler;
import ac.grim.grimac.platform.api.scheduler.TaskHandle;
import ac.grim.grimac.platform.api.world.PlatformWorld;
import ac.grim.grimac.platform.bukkit.GrimACBukkitLoaderPlugin;
import ac.grim.grimac.platform.bukkit.world.BukkitPlatformWorld;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.math.Location;
import org.bukkit.Bukkit;

public class FoliaRegionScheduler implements RegionScheduler {
   private final io.papermc.paper.threadedregions.scheduler.RegionScheduler regionScheduler = Bukkit.getRegionScheduler();

   public void execute(@NotNull GrimPlugin plugin, @NotNull PlatformWorld world, int chunkX, int chunkZ, @NotNull Runnable task) {
      this.regionScheduler.execute(GrimACBukkitLoaderPlugin.LOADER, ((BukkitPlatformWorld)world).getBukkitWorld(), chunkX, chunkZ, task);
   }

   public void execute(@NotNull GrimPlugin plugin, @NotNull Location location, @NotNull Runnable task) {
      this.execute(plugin, location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4, task);
   }

   public TaskHandle run(@NotNull GrimPlugin plugin, @NotNull PlatformWorld world, int chunkX, int chunkZ, @NotNull Runnable task) {
      return new FoliaTaskHandle(this.regionScheduler.run(GrimACBukkitLoaderPlugin.LOADER, ((BukkitPlatformWorld)world).getBukkitWorld(), chunkX, chunkZ, (ignored) -> {
         task.run();
      }));
   }

   public TaskHandle run(@NotNull GrimPlugin plugin, @NotNull Location location, @NotNull Runnable task) {
      return this.run(plugin, location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4, task);
   }

   public TaskHandle runDelayed(@NotNull GrimPlugin plugin, @NotNull PlatformWorld world, int chunkX, int chunkZ, @NotNull Runnable task, long delayTicks) {
      return new FoliaTaskHandle(this.regionScheduler.runDelayed(GrimACBukkitLoaderPlugin.LOADER, ((BukkitPlatformWorld)world).getBukkitWorld(), chunkX, chunkZ, (ignored) -> {
         task.run();
      }, delayTicks));
   }

   public TaskHandle runDelayed(@NotNull GrimPlugin plugin, @NotNull Location location, @NotNull Runnable task, long delayTicks) {
      return this.runDelayed(plugin, location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4, task, delayTicks);
   }

   public TaskHandle runAtFixedRate(@NotNull GrimPlugin plugin, @NotNull PlatformWorld world, int chunkX, int chunkZ, @NotNull Runnable task, long initialDelayTicks, long periodTicks) {
      return new FoliaTaskHandle(this.regionScheduler.runAtFixedRate(GrimACBukkitLoaderPlugin.LOADER, ((BukkitPlatformWorld)world).getBukkitWorld(), chunkX, chunkZ, (ignored) -> {
         task.run();
      }, initialDelayTicks, periodTicks));
   }

   public TaskHandle runAtFixedRate(@NotNull GrimPlugin plugin, @NotNull Location location, @NotNull Runnable task, long initialDelayTicks, long periodTicks) {
      return this.runAtFixedRate(plugin, location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4, task, initialDelayTicks, periodTicks);
   }
}
