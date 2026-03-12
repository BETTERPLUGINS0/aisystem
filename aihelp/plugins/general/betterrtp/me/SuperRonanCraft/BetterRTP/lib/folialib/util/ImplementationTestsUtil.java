package me.SuperRonanCraft.BetterRTP.lib.folialib.util;

import java.util.function.Consumer;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class ImplementationTestsUtil {
   private static final boolean IS_CANCELLED_SUPPORTED;
   private static final boolean IS_TASK_CONSUMERS_SUPPORTED;

   public static boolean isCancelledSupported() {
      return IS_CANCELLED_SUPPORTED;
   }

   public static boolean isTaskConsumersSupported() {
      return IS_TASK_CONSUMERS_SUPPORTED;
   }

   static {
      boolean isCancelledSupported = false;

      try {
         Class<BukkitTask> bukkitTaskClass = BukkitTask.class;
         bukkitTaskClass.getDeclaredMethod("isCancelled");
         isCancelledSupported = true;
      } catch (NoSuchMethodException var4) {
      }

      IS_CANCELLED_SUPPORTED = isCancelledSupported;
      boolean taskConsumersSupported = false;

      try {
         Class<BukkitScheduler> bukkitSchedulerClass = BukkitScheduler.class;
         bukkitSchedulerClass.getDeclaredMethod("runTask", Plugin.class, Consumer.class);
         taskConsumersSupported = true;
      } catch (NoSuchMethodException var3) {
      }

      IS_TASK_CONSUMERS_SUPPORTED = taskConsumersSupported;
   }
}
