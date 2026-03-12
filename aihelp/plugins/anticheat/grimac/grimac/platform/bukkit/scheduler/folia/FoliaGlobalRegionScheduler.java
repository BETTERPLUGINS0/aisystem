package ac.grim.grimac.platform.bukkit.scheduler.folia;

import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.platform.api.scheduler.GlobalRegionScheduler;
import ac.grim.grimac.platform.api.scheduler.TaskHandle;
import ac.grim.grimac.platform.bukkit.GrimACBukkitLoaderPlugin;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import org.bukkit.Bukkit;

public class FoliaGlobalRegionScheduler implements GlobalRegionScheduler {
   private final io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler globalRegionScheduler = Bukkit.getGlobalRegionScheduler();

   public void execute(@NotNull GrimPlugin plugin, @NotNull Runnable task) {
      this.globalRegionScheduler.execute(GrimACBukkitLoaderPlugin.LOADER, task);
   }

   public TaskHandle run(@NotNull GrimPlugin plugin, @NotNull Runnable task) {
      return new FoliaTaskHandle(this.globalRegionScheduler.run(GrimACBukkitLoaderPlugin.LOADER, (ignored) -> {
         task.run();
      }));
   }

   public TaskHandle runDelayed(@NotNull GrimPlugin plugin, @NotNull Runnable task, long delay) {
      return new FoliaTaskHandle(this.globalRegionScheduler.runDelayed(GrimACBukkitLoaderPlugin.LOADER, (ignored) -> {
         task.run();
      }, delay));
   }

   public TaskHandle runAtFixedRate(@NotNull GrimPlugin plugin, @NotNull Runnable task, long initialDelayTicks, long periodTicks) {
      return new FoliaTaskHandle(this.globalRegionScheduler.runAtFixedRate(GrimACBukkitLoaderPlugin.LOADER, (ignored) -> {
         task.run();
      }, initialDelayTicks, periodTicks));
   }

   public void cancel(@NotNull GrimPlugin plugin) {
      this.globalRegionScheduler.cancelTasks(GrimACBukkitLoaderPlugin.LOADER);
   }
}
