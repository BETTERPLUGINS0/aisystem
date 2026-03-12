package ac.grim.grimac.platform.bukkit.scheduler.folia;

import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.platform.api.scheduler.AsyncScheduler;
import ac.grim.grimac.platform.api.scheduler.TaskHandle;
import ac.grim.grimac.platform.bukkit.GrimACBukkitLoaderPlugin;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;

public class FoliaAsyncScheduler implements AsyncScheduler {
   private final io.papermc.paper.threadedregions.scheduler.AsyncScheduler scheduler = Bukkit.getAsyncScheduler();

   public TaskHandle runNow(@NotNull GrimPlugin plugin, @NotNull Runnable task) {
      return new FoliaTaskHandle(this.scheduler.runNow(GrimACBukkitLoaderPlugin.LOADER, (ignored) -> {
         task.run();
      }));
   }

   public TaskHandle runDelayed(@NotNull GrimPlugin plugin, @NotNull Runnable task, long delay, @NotNull TimeUnit timeUnit) {
      return new FoliaTaskHandle(this.scheduler.runDelayed(GrimACBukkitLoaderPlugin.LOADER, (ignored) -> {
         task.run();
      }, delay, timeUnit));
   }

   public TaskHandle runAtFixedRate(@NotNull GrimPlugin plugin, @NotNull Runnable task, long delay, long period, @NotNull TimeUnit timeUnit) {
      return new FoliaTaskHandle(this.scheduler.runAtFixedRate(GrimACBukkitLoaderPlugin.LOADER, (ignored) -> {
         task.run();
      }, delay, period, timeUnit));
   }

   public TaskHandle runAtFixedRate(@NotNull GrimPlugin plugin, @NotNull Runnable task, long initialDelayTicks, long periodTicks) {
      return new FoliaTaskHandle(this.scheduler.runAtFixedRate(GrimACBukkitLoaderPlugin.LOADER, (ignored) -> {
         task.run();
      }, initialDelayTicks * 50L, periodTicks * 50L, TimeUnit.MILLISECONDS));
   }

   public void cancel(@NotNull GrimPlugin plugin) {
      this.scheduler.cancelTasks(GrimACBukkitLoaderPlugin.LOADER);
   }
}
