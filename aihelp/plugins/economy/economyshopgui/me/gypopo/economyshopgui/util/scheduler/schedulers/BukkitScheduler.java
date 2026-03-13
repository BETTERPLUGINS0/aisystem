package me.gypopo.economyshopgui.util.scheduler.schedulers;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.util.scheduler.ScheduledTask;
import me.gypopo.economyshopgui.util.scheduler.ServerScheduler;
import me.gypopo.economyshopgui.util.scheduler.tasks.BukkitTask;

public class BukkitScheduler implements ServerScheduler {
   private final org.bukkit.scheduler.BukkitScheduler scheduler;
   private final ScheduledExecutorService thread = Executors.newScheduledThreadPool(2, (new ThreadFactoryBuilder()).setNameFormat("ESGUI_UTIL_THREAD #%d").build());

   public BukkitScheduler(EconomyShopGUI plugin) {
      this.scheduler = plugin.getServer().getScheduler();
   }

   public ScheduledTask runTask(EconomyShopGUI plugin, Runnable run) {
      return new BukkitTask(this.scheduler.runTask(plugin, run));
   }

   public ScheduledTask runTaskLater(EconomyShopGUI plugin, Runnable run, long delay) {
      return new BukkitTask(this.scheduler.runTaskLater(plugin, run, delay));
   }

   public ScheduledTask runTaskTimer(EconomyShopGUI plugin, Runnable run, long delay, long period) {
      return new BukkitTask(this.scheduler.runTaskTimer(plugin, run, delay, period));
   }

   public ScheduledTask runTaskAsync(EconomyShopGUI plugin, Runnable run) {
      return new BukkitTask(this.scheduler.runTaskAsynchronously(plugin, run));
   }

   public ScheduledTask runTaskLaterAsync(EconomyShopGUI plugin, Runnable run, long delay) {
      return new BukkitTask(this.scheduler.runTaskLaterAsynchronously(plugin, run, delay));
   }

   public ScheduledTask runTaskAsyncTimer(EconomyShopGUI plugin, Runnable run, long delay, long period) {
      return new BukkitTask(this.scheduler.runTaskTimerAsynchronously(plugin, run, delay, period));
   }

   public void runKillableTask(Runnable run) {
      this.thread.execute(run);
   }

   public void runKillableTaskLater(Runnable run, long delay) {
      this.thread.schedule(run, delay * 50L, TimeUnit.MILLISECONDS);
   }

   public void cancelTasks(EconomyShopGUI plugin) {
      this.scheduler.cancelTasks(plugin);
      this.thread.shutdownNow();
   }
}
