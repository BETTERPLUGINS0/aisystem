package advancedplugins.pm2.cv.models.api.utils.future;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;

public abstract class AbstractFoliaFuture<T> extends AbstractFuture<T> {
   protected AbstractFoliaFuture() {
   }

   protected AbstractFoliaFuture(T var1) {
      super(var1);
   }

   protected final void executeAsync(Runnable var1) {
      Bukkit.getAsyncScheduler().runNow(ModelAPI.PLUGIN, (var1x) -> {
         var1.run();
      });
   }

   protected final void executeAsync(Runnable var1, int var2) {
      Bukkit.getAsyncScheduler().runDelayed(ModelAPI.PLUGIN, (var1x) -> {
         var1.run();
      }, (long)var2 * 50L, TimeUnit.MILLISECONDS);
   }
}
