package advancedplugins.pm2.cv.api.event;

import advancedplugins.pm2.cv.api.util.Run;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public abstract class InfiniteVehiclesEvent extends Event {
   public InfiniteVehiclesEvent() {
   }

   public InfiniteVehiclesEvent(boolean var1) {
      super(var1);
   }

   public boolean callEvent() {
      Bukkit.getPluginManager().callEvent(this);
      if (this instanceof Cancellable) {
         return !((Cancellable)this).isCancelled();
      } else {
         return true;
      }
   }

   public void callEventSynchronously(@Nullable Consumer<Boolean> var1) {
      if (this.isAsynchronous()) {
         throw new IllegalStateException(this.getEventName() + " cannot be triggered asynchronously from primary server thread.");
      } else {
         if (Bukkit.isPrimaryThread()) {
            boolean var2 = this.callEvent();
            if (var1 != null) {
               var1.accept(var2);
            }
         } else {
            Run.sync(() -> {
               boolean var2 = this.callEvent();
               if (var1 != null) {
                  var1.accept(var2);
               }

            });
         }

      }
   }
}
