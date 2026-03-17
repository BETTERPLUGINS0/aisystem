package advancedplugins.pm2.cv.api.util;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;

public final class Run {
   public static final Set<Integer> ASYNC_TASKS = new HashSet();

   public static void sync(Runnable var0) {
      if (Bukkit.isPrimaryThread()) {
         var0.run();
      } else {
         Bukkit.getScheduler().runTask(InfiniteVehicles.getPlugin(), var0);
      }

   }

   public static int syncDelayed(Runnable var0, long var1) {
      return Bukkit.getScheduler().scheduleSyncDelayedTask(InfiniteVehicles.getPlugin(), var0, var1);
   }

   public static int syncDelayed(Runnable var0) {
      return Bukkit.getScheduler().scheduleSyncDelayedTask(InfiniteVehicles.getPlugin(), var0);
   }

   private static int sumTask() {
      return Bukkit.getScheduler().getPendingTasks().stream().filter((var0) -> {
         return var0.getOwner().getDescription().getName().equals(InfiniteVehicles.getPlugin().getDescription().getName());
      }).toList().size() + Bukkit.getScheduler().getActiveWorkers().stream().filter((var0) -> {
         return var0.getOwner().getDescription().getName().equals(InfiniteVehicles.getPlugin().getDescription().getName());
      }).toList().size();
   }

   public static int async(Runnable var0) {
      return Bukkit.getScheduler().runTaskAsynchronously(InfiniteVehicles.getPlugin(), var0).getTaskId();
   }

   public static int timerAsynchronously(Runnable var0, long var1, long var3) {
      int var5 = Bukkit.getScheduler().runTaskTimerAsynchronously(InfiniteVehicles.getPlugin(), var0, var1, var3).getTaskId();
      ASYNC_TASKS.add(var5);
      return var5;
   }
}
