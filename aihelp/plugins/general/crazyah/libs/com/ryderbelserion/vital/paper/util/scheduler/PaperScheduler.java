package libs.com.ryderbelserion.vital.paper.util.scheduler;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import libs.com.ryderbelserion.vital.common.api.interfaces.IScheduler;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PaperScheduler implements IScheduler {
   private final JavaPlugin plugin;

   public PaperScheduler(@NotNull JavaPlugin plugin) {
      this.plugin = plugin;
   }

   public void runDelayedTask(@NotNull final Consumer<IScheduler> task, long delay) {
      (new FoliaRunnable(this.plugin.getServer().getGlobalRegionScheduler()) {
         public void run() {
            task.accept(PaperScheduler.this);
         }
      }).runDelayed(this.plugin, delay);
   }

   public void runRepeatingTask(@NotNull final Consumer<IScheduler> task, long delay, long interval) {
      (new FoliaRunnable(this.plugin.getServer().getGlobalRegionScheduler()) {
         public void run() {
            task.accept(PaperScheduler.this);
         }
      }).runAtFixedRate(this.plugin, delay, interval);
   }

   public void runRepeatingTask(@NotNull final Consumer<IScheduler> task, long interval) {
      (new FoliaRunnable(this.plugin.getServer().getGlobalRegionScheduler()) {
         public void run() {
            task.accept(PaperScheduler.this);
         }
      }).runAtFixedRate(this.plugin, 0L, interval);
   }

   public void runRepeatingAsyncTask(@NotNull final Consumer<IScheduler> task, long delay, long interval) {
      (new FoliaRunnable(this.plugin.getServer().getAsyncScheduler(), (TimeUnit)null) {
         public void run() {
            task.accept(PaperScheduler.this);
         }
      }).runAtFixedRate(this.plugin, delay, interval);
   }

   public void runDelayedAsyncTask(@NotNull final Consumer<IScheduler> task, long delay) {
      (new FoliaRunnable(this.plugin.getServer().getAsyncScheduler(), (TimeUnit)null) {
         public void run() {
            task.accept(PaperScheduler.this);
         }
      }).runDelayed(this.plugin, delay);
   }
}
