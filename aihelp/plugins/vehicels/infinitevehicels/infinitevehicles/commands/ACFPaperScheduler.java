package me.PM2.infinitevehicles.commands;

import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import java.util.concurrent.TimeUnit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class ACFPaperScheduler extends ACFBukkitScheduler {
   private final AsyncScheduler scheduler;
   private ScheduledTask localeTask;

   public ACFPaperScheduler(@NotNull AsyncScheduler scheduler) {
      this.scheduler = var1;
   }

   public void registerSchedulerDependencies(BukkitCommandManager manager) {
      var1.registerDependency(AsyncScheduler.class, this.scheduler);
   }

   public void createDelayedTask(Plugin plugin, Runnable task, long delay) {
      this.scheduler.runDelayed(var1, (var1x) -> {
         var2.run();
      }, var3 / 20L, TimeUnit.SECONDS);
   }

   public void createLocaleTask(Plugin plugin, Runnable task, long delay, long period) {
      this.localeTask = this.scheduler.runAtFixedRate(var1, (var1x) -> {
         var2.run();
      }, var3 / 20L, var5 / 20L, TimeUnit.SECONDS);
   }

   public void cancelLocaleTask() {
      this.localeTask.cancel();
   }
}
