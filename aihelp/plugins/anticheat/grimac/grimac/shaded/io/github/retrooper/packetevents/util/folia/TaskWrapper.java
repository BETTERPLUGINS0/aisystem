package ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.folia;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class TaskWrapper {
   private BukkitTask bukkitTask;
   private ScheduledTask scheduledTask;

   public TaskWrapper(@NotNull BukkitTask bukkitTask) {
      this.bukkitTask = bukkitTask;
   }

   public TaskWrapper(@NotNull ScheduledTask scheduledTask) {
      this.scheduledTask = scheduledTask;
   }

   public Plugin getOwner() {
      return this.bukkitTask != null ? this.bukkitTask.getOwner() : this.scheduledTask.getOwningPlugin();
   }

   public boolean isCancelled() {
      return this.bukkitTask != null ? this.bukkitTask.isCancelled() : this.scheduledTask.isCancelled();
   }

   public void cancel() {
      if (this.bukkitTask != null) {
         this.bukkitTask.cancel();
      } else {
         this.scheduledTask.cancel();
      }

   }
}
