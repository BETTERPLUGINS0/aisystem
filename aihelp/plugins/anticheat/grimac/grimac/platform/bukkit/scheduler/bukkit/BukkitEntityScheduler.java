package ac.grim.grimac.platform.bukkit.scheduler.bukkit;

import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.platform.api.entity.GrimEntity;
import ac.grim.grimac.platform.api.scheduler.EntityScheduler;
import ac.grim.grimac.platform.api.scheduler.TaskHandle;
import ac.grim.grimac.platform.bukkit.GrimACBukkitLoaderPlugin;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

public class BukkitEntityScheduler implements EntityScheduler {
   private final BukkitScheduler scheduler = Bukkit.getScheduler();

   public void execute(@NotNull GrimEntity entity, @NotNull GrimPlugin plugin, @NotNull Runnable run, @Nullable Runnable retired, long delay) {
      this.scheduler.runTaskLater(GrimACBukkitLoaderPlugin.LOADER, run, delay);
   }

   public TaskHandle run(@NotNull GrimEntity entity, @NotNull GrimPlugin plugin, @NotNull Runnable task, @Nullable Runnable retired) {
      return new BukkitTaskHandle(this.scheduler.runTask(GrimACBukkitLoaderPlugin.LOADER, task));
   }

   public TaskHandle runDelayed(@NotNull GrimEntity entity, @NotNull GrimPlugin plugin, @NotNull Runnable task, @Nullable Runnable retired, long delayTicks) {
      return new BukkitTaskHandle(this.scheduler.runTaskLater(GrimACBukkitLoaderPlugin.LOADER, task, delayTicks));
   }

   public TaskHandle runAtFixedRate(@NotNull GrimEntity entity, @NotNull GrimPlugin plugin, @NotNull Runnable task, @Nullable Runnable retired, long initialDelayTicks, long periodTicks) {
      return new BukkitTaskHandle(this.scheduler.runTaskTimer(GrimACBukkitLoaderPlugin.LOADER, task, initialDelayTicks, periodTicks));
   }
}
