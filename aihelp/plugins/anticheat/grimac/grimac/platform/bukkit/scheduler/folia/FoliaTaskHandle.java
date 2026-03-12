package ac.grim.grimac.platform.bukkit.scheduler.folia;

import ac.grim.grimac.platform.api.scheduler.TaskHandle;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import java.util.Objects;

public class FoliaTaskHandle implements TaskHandle {
   @NotNull
   private final ScheduledTask task;

   @Contract(
      pure = true
   )
   public FoliaTaskHandle(@NotNull ScheduledTask task) {
      this.task = (ScheduledTask)Objects.requireNonNull(task);
   }

   public boolean isSync() {
      return false;
   }

   public boolean isCancelled() {
      return this.task.isCancelled();
   }

   public void cancel() {
      this.task.cancel();
   }
}
