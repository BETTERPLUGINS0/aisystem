package me.PM2.infinitevehicles.commands;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class ACFBukkitScheduler {
   private int localeTask;

   public void registerSchedulerDependencies(BukkitCommandManager manager) {
      var1.registerDependency(BukkitScheduler.class, Bukkit.getScheduler());
   }

   public void createDelayedTask(Plugin plugin, Runnable task, long delay) {
      Bukkit.getScheduler().runTaskLater(var1, var2, var3);
   }

   public void createLocaleTask(Plugin plugin, Runnable task, long delay, long period) {
      this.localeTask = Bukkit.getScheduler().runTaskTimer(var1, var2, var3, var5).getTaskId();
   }

   public void cancelLocaleTask() {
      Bukkit.getScheduler().cancelTask(this.localeTask);
   }
}
