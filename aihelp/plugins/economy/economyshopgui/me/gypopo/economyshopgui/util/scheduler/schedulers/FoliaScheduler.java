package me.gypopo.economyshopgui.util.scheduler.schedulers;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.util.scheduler.ScheduledTask;
import me.gypopo.economyshopgui.util.scheduler.ServerScheduler;
import me.gypopo.economyshopgui.util.scheduler.tasks.FoliaTask;

public class FoliaScheduler implements ServerScheduler {
   private final GlobalRegionScheduler globalRegionScheduler;
   private final AsyncScheduler asyncScheduler;
   private final ScheduledExecutorService thread = Executors.newScheduledThreadPool(2, (new ThreadFactoryBuilder()).setNameFormat("ESGUI_UTIL_THREAD #%d").build());

   public FoliaScheduler(EconomyShopGUI plugin) {
      this.globalRegionScheduler = plugin.getServer().getGlobalRegionScheduler();
      this.asyncScheduler = plugin.getServer().getAsyncScheduler();
   }

   public ScheduledTask runTask(EconomyShopGUI plugin, Runnable run) {
      return new FoliaTask(this.globalRegionScheduler.run(plugin, (t) -> {
         run.run();
      }));
   }

   public ScheduledTask runTaskLater(EconomyShopGUI plugin, Runnable run, long delay) {
      return (ScheduledTask)(delay <= 0L ? this.runTask(plugin, run) : new FoliaTask(this.globalRegionScheduler.runDelayed(plugin, (t) -> {
         run.run();
      }, delay)));
   }

   public ScheduledTask runTaskTimer(EconomyShopGUI plugin, Runnable run, long delay, long period) {
      if (delay <= 0L) {
         delay = 1L;
      }

      return new FoliaTask(this.globalRegionScheduler.runAtFixedRate(plugin, (t) -> {
         run.run();
      }, delay, period));
   }

   public ScheduledTask runTaskAsync(EconomyShopGUI plugin, Runnable run) {
      return new FoliaTask(this.asyncScheduler.runNow(plugin, (t) -> {
         run.run();
      }));
   }

   public ScheduledTask runTaskLaterAsync(EconomyShopGUI plugin, Runnable run, long delay) {
      return (ScheduledTask)(delay <= 0L ? this.runTaskAsync(plugin, run) : new FoliaTask(this.asyncScheduler.runDelayed(plugin, (t) -> {
         run.run();
      }, delay * 50L, TimeUnit.MILLISECONDS)));
   }

   public ScheduledTask runTaskAsyncTimer(EconomyShopGUI plugin, Runnable run, long delay, long period) {
      if (delay <= 0L) {
         delay = 1L;
      }

      return new FoliaTask(this.asyncScheduler.runAtFixedRate(plugin, (t) -> {
         run.run();
      }, delay * 50L, period * 50L, TimeUnit.MILLISECONDS));
   }

   public void runKillableTask(Runnable run) {
      this.thread.execute(run);
   }

   public void runKillableTaskLater(Runnable run, long delay) {
      this.thread.schedule(run, delay * 50L, TimeUnit.MILLISECONDS);
   }

   public void cancelTasks(EconomyShopGUI plugin) {
      this.asyncScheduler.cancelTasks(plugin);
      this.globalRegionScheduler.cancelTasks(plugin);
      this.thread.shutdownNow();
   }
}
