package ac.grim.grimac.platform.bukkit.scheduler.bukkit;

import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.platform.api.scheduler.RegionScheduler;
import ac.grim.grimac.platform.api.scheduler.TaskHandle;
import ac.grim.grimac.platform.api.world.PlatformWorld;
import ac.grim.grimac.platform.bukkit.GrimACBukkitLoaderPlugin;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.math.Location;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

public class BukkitRegionScheduler implements RegionScheduler {
   private final BukkitScheduler bukkitScheduler = Bukkit.getScheduler();

   public void execute(@NotNull GrimPlugin plugin, @NotNull PlatformWorld world, int chunkX, int chunkZ, @NotNull Runnable task) {
      this.bukkitScheduler.runTask(GrimACBukkitLoaderPlugin.LOADER, task);
   }

   public void execute(@NotNull GrimPlugin plugin, @NotNull Location location, @NotNull Runnable task) {
      this.bukkitScheduler.runTask(GrimACBukkitLoaderPlugin.LOADER, task);
   }

   public TaskHandle run(@NotNull GrimPlugin plugin, @NotNull PlatformWorld world, int chunkX, int chunkZ, @NotNull Runnable task) {
      return new BukkitTaskHandle(this.bukkitScheduler.runTask(GrimACBukkitLoaderPlugin.LOADER, task));
   }

   public TaskHandle run(@NotNull GrimPlugin plugin, @NotNull Location location, @NotNull Runnable task) {
      return new BukkitTaskHandle(this.bukkitScheduler.runTask(GrimACBukkitLoaderPlugin.LOADER, task));
   }

   public TaskHandle runDelayed(@NotNull GrimPlugin plugin, @NotNull PlatformWorld world, int chunkX, int chunkZ, @NotNull Runnable task, long delayTicks) {
      return new BukkitTaskHandle(this.bukkitScheduler.runTaskLater(GrimACBukkitLoaderPlugin.LOADER, task, delayTicks));
   }

   public TaskHandle runDelayed(@NotNull GrimPlugin plugin, @NotNull Location location, @NotNull Runnable task, long delayTicks) {
      return new BukkitTaskHandle(this.bukkitScheduler.runTaskLater(GrimACBukkitLoaderPlugin.LOADER, task, delayTicks));
   }

   public TaskHandle runAtFixedRate(@NotNull GrimPlugin plugin, @NotNull PlatformWorld world, int chunkX, int chunkZ, @NotNull Runnable task, long initialDelayTicks, long periodTicks) {
      return new BukkitTaskHandle(this.bukkitScheduler.runTaskTimer(GrimACBukkitLoaderPlugin.LOADER, task, initialDelayTicks, periodTicks));
   }

   public TaskHandle runAtFixedRate(@NotNull GrimPlugin plugin, @NotNull Location location, @NotNull Runnable task, long initialDelayTicks, long periodTicks) {
      return new BukkitTaskHandle(this.bukkitScheduler.runTaskTimer(GrimACBukkitLoaderPlugin.LOADER, task, initialDelayTicks, periodTicks));
   }
}
