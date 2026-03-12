package ac.grim.grimac.platform.bukkit.scheduler.bukkit;

import ac.grim.grimac.platform.api.scheduler.TaskHandle;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.Objects;
import org.bukkit.scheduler.BukkitTask;

public class BukkitTaskHandle implements TaskHandle {
   @NotNull
   private final BukkitTask task;

   @Contract(
      pure = true
   )
   public BukkitTaskHandle(@NotNull BukkitTask task) {
      this.task = (BukkitTask)Objects.requireNonNull(task);
   }

   public boolean isSync() {
      return this.task.isSync();
   }

   public boolean isCancelled() {
      return this.task.isCancelled();
   }

   public void cancel() {
      this.task.cancel();
   }
}
