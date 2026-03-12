package com.nisovin.shopkeepers.util.bukkit;

import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class SchedulerUtils {
   public static Executor createSyncExecutor(Plugin plugin) {
      return (runnable) -> {
         runOnMainThreadOrOmit(plugin, runnable);
      };
   }

   public static Executor createAsyncExecutor(Plugin plugin) {
      return (runnable) -> {
         runAsyncTaskOrOmit(plugin, runnable);
      };
   }

   public static int getActiveAsyncTasks(Plugin plugin) {
      Validate.notNull(plugin, (String)"plugin is null");
      int workers = 0;
      Iterator var2 = Bukkit.getScheduler().getActiveWorkers().iterator();

      while(var2.hasNext()) {
         BukkitWorker worker = (BukkitWorker)var2.next();
         if (worker.getOwner().equals(plugin)) {
            ++workers;
         }
      }

      return workers;
   }

   private static void validatePluginTask(Plugin plugin, Runnable task) {
      Validate.notNull(plugin, (String)"plugin is null");
      Validate.notNull(task, (String)"task is null");
   }

   public static boolean isMainThread() {
      return Bukkit.isPrimaryThread();
   }

   public static boolean runOnMainThreadOrOmit(Plugin plugin, Runnable task) {
      validatePluginTask(plugin, task);
      if (isMainThread()) {
         task.run();
         return true;
      } else {
         return runTaskOrOmit(plugin, task) != null;
      }
   }

   @Nullable
   public static BukkitTask runTaskOrOmit(Plugin plugin, Runnable task) {
      return runTaskLaterOrOmit(plugin, task, 0L);
   }

   @Nullable
   public static BukkitTask runTaskLaterOrOmit(Plugin plugin, Runnable task, long delay) {
      validatePluginTask(plugin, task);
      if (plugin.isEnabled()) {
         try {
            return Bukkit.getScheduler().runTaskLater(plugin, task, delay);
         } catch (IllegalPluginAccessException var5) {
         }
      }

      return null;
   }

   @Nullable
   public static BukkitTask runAsyncTaskOrOmit(Plugin plugin, Runnable task) {
      return runAsyncTaskLaterOrOmit(plugin, task, 0L);
   }

   @Nullable
   public static BukkitTask runAsyncTaskLaterOrOmit(Plugin plugin, Runnable task, long delay) {
      validatePluginTask(plugin, task);
      if (plugin.isEnabled()) {
         try {
            return Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, delay);
         } catch (IllegalPluginAccessException var5) {
         }
      }

      return null;
   }

   public static int awaitAsyncTasksCompletion(Plugin plugin, int asyncTasksTimeoutSeconds, @Nullable Logger logger) {
      Validate.notNull(plugin, (String)"plugin is null");
      Validate.isTrue(asyncTasksTimeoutSeconds >= 0, "asyncTasksTimeoutSeconds cannot be negative");
      int activeAsyncTasks = getActiveAsyncTasks(plugin);
      if (activeAsyncTasks > 0 && asyncTasksTimeoutSeconds > 0) {
         if (logger != null) {
            logger.info("Waiting up to " + asyncTasksTimeoutSeconds + " seconds for " + activeAsyncTasks + " remaining async tasks to finish ...");
         }

         long asyncTasksTimeoutMillis = TimeUnit.SECONDS.toMillis((long)asyncTasksTimeoutSeconds);
         long waitStartNanos = System.nanoTime();
         long waitDurationMillis = 0L;

         do {
            try {
               Thread.sleep(25L);
            } catch (InterruptedException var11) {
               Thread.currentThread().interrupt();
            }

            activeAsyncTasks = getActiveAsyncTasks(plugin);
            waitDurationMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - waitStartNanos);
         } while(waitDurationMillis <= asyncTasksTimeoutMillis && activeAsyncTasks > 0);

         if (waitDurationMillis > 1L && logger != null) {
            logger.info("Waited " + waitDurationMillis + " ms for async tasks to finish.");
         }
      }

      if (activeAsyncTasks > 0 && logger != null) {
         logger.severe("There are still " + activeAsyncTasks + " remaining async tasks active! Disabling anyway now.");
      }

      return activeAsyncTasks;
   }

   private SchedulerUtils() {
   }
}
