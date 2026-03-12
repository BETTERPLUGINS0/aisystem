package fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.foliaScheduler;

import fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler;
import fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.scheduling.tasks.MyScheduledTask;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

public class FoliaScheduler implements TaskScheduler {
   final Plugin plugin;
   private final RegionScheduler regionScheduler = Bukkit.getServer().getRegionScheduler();
   private final GlobalRegionScheduler globalRegionScheduler = Bukkit.getServer().getGlobalRegionScheduler();
   private final AsyncScheduler asyncScheduler = Bukkit.getServer().getAsyncScheduler();

   public FoliaScheduler(Plugin plugin) {
      this.plugin = plugin;
   }

   public boolean isGlobalThread() {
      return Bukkit.getServer().isGlobalTickThread();
   }

   public boolean isTickThread() {
      return Bukkit.getServer().isPrimaryThread();
   }

   public boolean isEntityThread(Entity entity) {
      return Bukkit.getServer().isOwnedByCurrentRegion(entity);
   }

   public boolean isRegionThread(Location location) {
      return Bukkit.getServer().isOwnedByCurrentRegion(location);
   }

   public MyScheduledTask runTask(Runnable runnable) {
      return new FoliaScheduledTask(this.globalRegionScheduler.run(this.plugin, (task) -> {
         runnable.run();
      }));
   }

   public MyScheduledTask runTaskLater(Runnable runnable, long delay) {
      return (MyScheduledTask)(delay <= 0L ? this.runTask(runnable) : new FoliaScheduledTask(this.globalRegionScheduler.runDelayed(this.plugin, (task) -> {
         runnable.run();
      }, delay)));
   }

   public MyScheduledTask runTaskTimer(Runnable runnable, long delay, long period) {
      delay = this.getOneIfNotPositive(delay);
      return new FoliaScheduledTask(this.globalRegionScheduler.runAtFixedRate(this.plugin, (task) -> {
         runnable.run();
      }, delay, period));
   }

   public MyScheduledTask runTask(Plugin plugin, Runnable runnable) {
      return new FoliaScheduledTask(this.globalRegionScheduler.run(plugin, (task) -> {
         runnable.run();
      }));
   }

   public MyScheduledTask runTaskLater(Plugin plugin, Runnable runnable, long delay) {
      return (MyScheduledTask)(delay <= 0L ? this.runTask(plugin, runnable) : new FoliaScheduledTask(this.globalRegionScheduler.runDelayed(plugin, (task) -> {
         runnable.run();
      }, delay)));
   }

   public MyScheduledTask runTaskTimer(Plugin plugin, Runnable runnable, long delay, long period) {
      delay = this.getOneIfNotPositive(delay);
      return new FoliaScheduledTask(this.globalRegionScheduler.runAtFixedRate(plugin, (task) -> {
         runnable.run();
      }, delay, period));
   }

   public MyScheduledTask runTask(Location location, Runnable runnable) {
      return new FoliaScheduledTask(this.regionScheduler.run(this.plugin, location, (task) -> {
         runnable.run();
      }));
   }

   public MyScheduledTask runTaskLater(Location location, Runnable runnable, long delay) {
      return (MyScheduledTask)(delay <= 0L ? this.runTask(runnable) : new FoliaScheduledTask(this.regionScheduler.runDelayed(this.plugin, location, (task) -> {
         runnable.run();
      }, delay)));
   }

   public MyScheduledTask runTaskTimer(Location location, Runnable runnable, long delay, long period) {
      delay = this.getOneIfNotPositive(delay);
      return new FoliaScheduledTask(this.regionScheduler.runAtFixedRate(this.plugin, location, (task) -> {
         runnable.run();
      }, delay, period));
   }

   public MyScheduledTask runTask(Entity entity, Runnable runnable) {
      return new FoliaScheduledTask(entity.getScheduler().run(this.plugin, (task) -> {
         runnable.run();
      }, (Runnable)null));
   }

   public MyScheduledTask runTaskLater(Entity entity, Runnable runnable, long delay) {
      return (MyScheduledTask)(delay <= 0L ? this.runTask(entity, runnable) : new FoliaScheduledTask(entity.getScheduler().runDelayed(this.plugin, (task) -> {
         runnable.run();
      }, (Runnable)null, delay)));
   }

   public MyScheduledTask runTaskTimer(Entity entity, Runnable runnable, long delay, long period) {
      delay = this.getOneIfNotPositive(delay);
      return new FoliaScheduledTask(entity.getScheduler().runAtFixedRate(this.plugin, (task) -> {
         runnable.run();
      }, (Runnable)null, delay, period));
   }

   public MyScheduledTask runTaskAsynchronously(Runnable runnable) {
      return new FoliaScheduledTask(this.asyncScheduler.runNow(this.plugin, (task) -> {
         runnable.run();
      }));
   }

   public MyScheduledTask runTaskLaterAsynchronously(Runnable runnable, long delay) {
      delay = this.getOneIfNotPositive(delay);
      return new FoliaScheduledTask(this.asyncScheduler.runDelayed(this.plugin, (task) -> {
         runnable.run();
      }, delay * 50L, TimeUnit.MILLISECONDS));
   }

   public MyScheduledTask runTaskTimerAsynchronously(Runnable runnable, long delay, long period) {
      return new FoliaScheduledTask(this.asyncScheduler.runAtFixedRate(this.plugin, (task) -> {
         runnable.run();
      }, delay * 50L, period * 50L, TimeUnit.MILLISECONDS));
   }

   public MyScheduledTask runTaskAsynchronously(Plugin plugin, Runnable runnable) {
      return new FoliaScheduledTask(this.asyncScheduler.runNow(plugin, (task) -> {
         runnable.run();
      }));
   }

   public MyScheduledTask runTaskLaterAsynchronously(Plugin plugin, Runnable runnable, long delay) {
      delay = this.getOneIfNotPositive(delay);
      return new FoliaScheduledTask(this.asyncScheduler.runDelayed(plugin, (task) -> {
         runnable.run();
      }, delay * 50L, TimeUnit.MILLISECONDS));
   }

   public MyScheduledTask runTaskTimerAsynchronously(Plugin plugin, Runnable runnable, long delay, long period) {
      delay = this.getOneIfNotPositive(delay);
      return new FoliaScheduledTask(this.asyncScheduler.runAtFixedRate(plugin, (task) -> {
         runnable.run();
      }, delay * 50L, period * 50L, TimeUnit.MILLISECONDS));
   }

   public void execute(Runnable runnable) {
      this.globalRegionScheduler.execute(this.plugin, runnable);
   }

   public void execute(Location location, Runnable runnable) {
      this.regionScheduler.execute(this.plugin, location, runnable);
   }

   public void execute(Entity entity, Runnable runnable) {
      entity.getScheduler().execute(this.plugin, runnable, (Runnable)null, 1L);
   }

   public void cancelTasks() {
      this.globalRegionScheduler.cancelTasks(this.plugin);
      this.asyncScheduler.cancelTasks(this.plugin);
   }

   public void cancelTasks(Plugin plugin) {
      this.globalRegionScheduler.cancelTasks(plugin);
      this.asyncScheduler.cancelTasks(plugin);
   }

   private long getOneIfNotPositive(long x) {
      return x <= 0L ? 1L : x;
   }
}
