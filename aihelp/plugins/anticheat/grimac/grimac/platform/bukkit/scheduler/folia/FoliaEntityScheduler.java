package ac.grim.grimac.platform.bukkit.scheduler.folia;

import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.platform.api.entity.GrimEntity;
import ac.grim.grimac.platform.api.scheduler.EntityScheduler;
import ac.grim.grimac.platform.api.scheduler.TaskHandle;
import ac.grim.grimac.platform.bukkit.GrimACBukkitLoaderPlugin;
import ac.grim.grimac.platform.bukkit.entity.BukkitGrimEntity;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;

public class FoliaEntityScheduler implements EntityScheduler {
   public void execute(@NotNull GrimEntity entity, @NotNull GrimPlugin plugin, @NotNull Runnable task, @Nullable Runnable retired, long delay) {
      ((BukkitGrimEntity)entity).getBukkitEntity().getScheduler().execute(GrimACBukkitLoaderPlugin.LOADER, task, retired, delay);
   }

   public TaskHandle run(@NotNull GrimEntity entity, @NotNull GrimPlugin plugin, @NotNull Runnable task, @Nullable Runnable retired) {
      ScheduledTask scheduled = ((BukkitGrimEntity)entity).getBukkitEntity().getScheduler().run(GrimACBukkitLoaderPlugin.LOADER, (ignored) -> {
         task.run();
      }, retired);
      return scheduled == null ? null : new FoliaTaskHandle(scheduled);
   }

   public TaskHandle runDelayed(@NotNull GrimEntity entity, @NotNull GrimPlugin plugin, @NotNull Runnable task, @Nullable Runnable retired, long delayTicks) {
      ScheduledTask scheduled = ((BukkitGrimEntity)entity).getBukkitEntity().getScheduler().runDelayed(GrimACBukkitLoaderPlugin.LOADER, (ignored) -> {
         task.run();
      }, retired, delayTicks);
      return scheduled == null ? null : new FoliaTaskHandle(scheduled);
   }

   public TaskHandle runAtFixedRate(@NotNull GrimEntity entity, @NotNull GrimPlugin plugin, @NotNull Runnable task, @Nullable Runnable retired, long initialDelayTicks, long periodTicks) {
      ScheduledTask scheduled = ((BukkitGrimEntity)entity).getBukkitEntity().getScheduler().runAtFixedRate(GrimACBukkitLoaderPlugin.LOADER, (ignored) -> {
         task.run();
      }, retired, initialDelayTicks, periodTicks);
      return scheduled == null ? null : new FoliaTaskHandle(scheduled);
   }
}
