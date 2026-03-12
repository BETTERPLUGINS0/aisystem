package fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.bukkitScheduler;

import fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler;
import fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.scheduling.tasks.MyScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

public class BukkitScheduler implements TaskScheduler {
   final Plugin plugin;

   public BukkitScheduler(Plugin plugin) {
      this.plugin = plugin;
   }

   public boolean isGlobalThread() {
      return Bukkit.getServer().isPrimaryThread();
   }

   public boolean isEntityThread(Entity entity) {
      return Bukkit.getServer().isPrimaryThread();
   }

   public boolean isRegionThread(Location location) {
      return Bukkit.getServer().isPrimaryThread();
   }

   public MyScheduledTask runTask(Runnable runnable) {
      return new BukkitScheduledTask(Bukkit.getScheduler().runTask(this.plugin, runnable));
   }

   public MyScheduledTask runTaskLater(Runnable runnable, long delay) {
      return new BukkitScheduledTask(Bukkit.getScheduler().runTaskLater(this.plugin, runnable, delay));
   }

   public MyScheduledTask runTaskTimer(Runnable runnable, long delay, long period) {
      return new BukkitScheduledTask(Bukkit.getScheduler().runTaskTimer(this.plugin, runnable, delay, period));
   }

   public MyScheduledTask runTaskAsynchronously(Runnable runnable) {
      return new BukkitScheduledTask(Bukkit.getScheduler().runTaskAsynchronously(this.plugin, runnable));
   }

   public MyScheduledTask runTaskLaterAsynchronously(Runnable runnable, long delay) {
      return new BukkitScheduledTask(Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, runnable, delay));
   }

   public MyScheduledTask runTaskTimerAsynchronously(Runnable runnable, long delay, long period) {
      return new BukkitScheduledTask(Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, runnable, delay, period));
   }

   public MyScheduledTask runTask(Plugin plugin, Runnable runnable) {
      return new BukkitScheduledTask(Bukkit.getScheduler().runTask(plugin, runnable));
   }

   public MyScheduledTask runTaskLater(Plugin plugin, Runnable runnable, long delay) {
      return new BukkitScheduledTask(Bukkit.getScheduler().runTaskLater(plugin, runnable, delay));
   }

   public MyScheduledTask runTaskTimer(Plugin plugin, Runnable runnable, long delay, long period) {
      return new BukkitScheduledTask(Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, period));
   }

   public MyScheduledTask runTaskAsynchronously(Plugin plugin, Runnable runnable) {
      return new BukkitScheduledTask(Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable));
   }

   public MyScheduledTask runTaskLaterAsynchronously(Plugin plugin, Runnable runnable, long delay) {
      return new BukkitScheduledTask(Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay));
   }

   public MyScheduledTask runTaskTimerAsynchronously(Plugin plugin, Runnable runnable, long delay, long period) {
      return new BukkitScheduledTask(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, period));
   }

   public void execute(Runnable runnable) {
      Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, runnable);
   }

   public void cancelTasks() {
      Bukkit.getScheduler().cancelTasks(this.plugin);
   }

   public void cancelTasks(Plugin plugin) {
      Bukkit.getScheduler().cancelTasks(plugin);
   }
}
