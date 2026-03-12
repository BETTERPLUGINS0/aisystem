package emanondev.itemedit.utility;

import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public final class SchedulerUtils {
   private SchedulerUtils() {
      throw new UnsupportedOperationException();
   }

   public static void runAsync(@NotNull Plugin plugin, @NotNull Runnable task) {
      if (VersionUtils.hasFoliaAPI()) {
         foliaSchedulerInvoker(plugin.getServer(), "getAsyncScheduler", task, (scheduler, taskConsumer) -> {
            ReflectionUtils.invokeMethod(scheduler, "runNow", Plugin.class, plugin, Consumer.class, taskConsumer);
         });
      } else {
         Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
      }
   }

   public static void run(@NotNull Plugin plugin, @NotNull Runnable task) {
      if (VersionUtils.hasFoliaAPI()) {
         foliaSchedulerInvoker(plugin.getServer(), "getAsyncScheduler", task, (scheduler, taskConsumer) -> {
            ReflectionUtils.invokeMethod(scheduler, "runNow", Plugin.class, plugin, Consumer.class, taskConsumer);
         });
      } else {
         Bukkit.getScheduler().runTask(plugin, task);
      }
   }

   public static void runLater(@NotNull Plugin plugin, @Range(from = 1L,to = Long.MAX_VALUE) long delayTicks, @NotNull Runnable task) {
      if (VersionUtils.hasFoliaAPI()) {
         foliaSchedulerInvoker(plugin.getServer(), "getAsyncScheduler", task, (scheduler, taskConsumer) -> {
            ReflectionUtils.invokeMethod(scheduler, "runDelayed", Plugin.class, plugin, Consumer.class, taskConsumer, Long.TYPE, delayTicks * 50L, TimeUnit.class, TimeUnit.MILLISECONDS);
         });
      } else {
         Bukkit.getScheduler().runTaskLater(plugin, task, delayTicks);
      }
   }

   public static void runAsync(@NotNull Plugin plugin, @NotNull Location location, @NotNull Runnable task) {
      if (VersionUtils.hasFoliaAPI()) {
         foliaSchedulerInvoker(plugin.getServer(), "getRegionScheduler", task, (scheduler, taskConsumer) -> {
            ReflectionUtils.invokeMethod(scheduler, "run", Plugin.class, plugin, Location.class, location, Consumer.class, taskConsumer);
         });
      } else {
         Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
      }
   }

   public static void run(@NotNull Plugin plugin, @NotNull Location location, @NotNull Runnable task) {
      if (VersionUtils.hasFoliaAPI()) {
         foliaSchedulerInvoker(plugin.getServer(), "getRegionScheduler", task, (scheduler, taskConsumer) -> {
            ReflectionUtils.invokeMethod(scheduler, "run", Plugin.class, plugin, Location.class, location, Consumer.class, taskConsumer);
         });
      } else {
         Bukkit.getScheduler().runTask(plugin, task);
      }
   }

   public static void runLater(@NotNull Plugin plugin, @NotNull Location location, @Range(from = 1L,to = Long.MAX_VALUE) long delayTicks, @NotNull Runnable task) {
      if (VersionUtils.hasFoliaAPI()) {
         foliaSchedulerInvoker(plugin.getServer(), "getRegionScheduler", task, (scheduler, taskConsumer) -> {
            ReflectionUtils.invokeMethod(scheduler, "runDelayed", Plugin.class, plugin, Location.class, location, Consumer.class, taskConsumer, Long.TYPE, delayTicks);
         });
      } else {
         Bukkit.getScheduler().runTaskLater(plugin, task, delayTicks);
      }
   }

   public static void runAsync(@NotNull Plugin plugin, @NotNull Player player, @NotNull Runnable task) {
      if (VersionUtils.hasFoliaAPI()) {
         foliaSchedulerInvoker(player, "getScheduler", task, (scheduler, taskConsumer) -> {
            ReflectionUtils.invokeMethod(scheduler, "run", Plugin.class, plugin, Consumer.class, taskConsumer, Runnable.class, (Object)null);
         });
      } else {
         Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
      }
   }

   public static void run(@NotNull Plugin plugin, @NotNull Player player, @NotNull Runnable task) {
      if (VersionUtils.hasFoliaAPI()) {
         foliaSchedulerInvoker(player, "getScheduler", task, (scheduler, taskConsumer) -> {
            ReflectionUtils.invokeMethod(scheduler, "run", Plugin.class, plugin, Consumer.class, taskConsumer, Runnable.class, (Object)null);
         });
      } else {
         Bukkit.getScheduler().runTask(plugin, task);
      }
   }

   public static void runLater(@NotNull Plugin plugin, @NotNull Player player, @Range(from = 1L,to = Long.MAX_VALUE) long delayTicks, @NotNull Runnable task) {
      if (VersionUtils.hasFoliaAPI()) {
         foliaSchedulerInvoker(player, "getScheduler", task, (scheduler, taskConsumer) -> {
            ReflectionUtils.invokeMethod(scheduler, "runDelayed", Plugin.class, plugin, Consumer.class, taskConsumer, Runnable.class, (Object)null, Long.TYPE, delayTicks);
         });
      } else {
         Bukkit.getScheduler().runTaskLater(plugin, task, delayTicks);
      }
   }

   private static void foliaSchedulerInvoker(Object from, String schedulerName, Runnable task, BiConsumer<Object, Consumer<?>> invoke) {
      Object scheduler = ReflectionUtils.invokeMethod(from, schedulerName);
      Consumer<?> taskConsumer = (scheduledTask) -> {
         task.run();
      };
      invoke.accept(scheduler, taskConsumer);
   }
}
