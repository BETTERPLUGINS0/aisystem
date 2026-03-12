package ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.folia;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class GlobalRegionScheduler {
   private BukkitScheduler bukkitScheduler;
   private io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler globalRegionScheduler;

   protected GlobalRegionScheduler() {
      if (FoliaScheduler.isFolia) {
         this.globalRegionScheduler = Bukkit.getGlobalRegionScheduler();
      } else {
         this.bukkitScheduler = Bukkit.getScheduler();
      }

   }

   public void execute(@NotNull Plugin plugin, @NotNull Runnable run) {
      if (!FoliaScheduler.isFolia) {
         this.bukkitScheduler.runTask(plugin, run);
      } else {
         this.globalRegionScheduler.execute(plugin, run);
      }
   }

   public TaskWrapper run(@NotNull Plugin plugin, @NotNull Consumer<Object> task) {
      return !FoliaScheduler.isFolia ? new TaskWrapper(this.bukkitScheduler.runTask(plugin, () -> {
         task.accept((Object)null);
      })) : new TaskWrapper(this.globalRegionScheduler.run(plugin, (o) -> {
         task.accept((Object)null);
      }));
   }

   public TaskWrapper runDelayed(@NotNull Plugin plugin, @NotNull Consumer<Object> task, long delay) {
      if (delay < 1L) {
         delay = 1L;
      }

      return !FoliaScheduler.isFolia ? new TaskWrapper(this.bukkitScheduler.runTaskLater(plugin, () -> {
         task.accept((Object)null);
      }, delay)) : new TaskWrapper(this.globalRegionScheduler.runDelayed(plugin, (o) -> {
         task.accept((Object)null);
      }, delay));
   }

   public TaskWrapper runAtFixedRate(@NotNull Plugin plugin, @NotNull Consumer<Object> task, long initialDelayTicks, long periodTicks) {
      if (initialDelayTicks < 1L) {
         initialDelayTicks = 1L;
      }

      if (periodTicks < 1L) {
         periodTicks = 1L;
      }

      return !FoliaScheduler.isFolia ? new TaskWrapper(this.bukkitScheduler.runTaskTimer(plugin, () -> {
         task.accept((Object)null);
      }, initialDelayTicks, periodTicks)) : new TaskWrapper(this.globalRegionScheduler.runAtFixedRate(plugin, (o) -> {
         task.accept((Object)null);
      }, initialDelayTicks, periodTicks));
   }

   public void cancel(@NotNull Plugin plugin) {
      if (!FoliaScheduler.isFolia) {
         Bukkit.getScheduler().cancelTasks(plugin);
      } else {
         this.globalRegionScheduler.cancelTasks(plugin);
      }
   }
}
