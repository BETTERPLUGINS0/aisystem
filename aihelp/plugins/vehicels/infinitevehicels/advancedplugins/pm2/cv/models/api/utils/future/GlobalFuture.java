package advancedplugins.pm2.cv.models.api.utils.future;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import org.bukkit.Bukkit;

public class GlobalFuture<T> extends AbstractFoliaFuture<T> {
   private GlobalFuture() {
   }

   private GlobalFuture(T var1) {
      super(var1);
   }

   static <T> GlobalFuture<T> empty() {
      return new GlobalFuture();
   }

   static <T> GlobalFuture<T> completed(T var0) {
      return new GlobalFuture(var0);
   }

   protected <U> AbstractFuture<U> createEmpty() {
      return empty();
   }

   protected void executeSync(Runnable var1) {
      Bukkit.getGlobalRegionScheduler().execute(ModelAPI.PLUGIN, var1);
   }

   protected void executeSync(Runnable var1, int var2) {
      Bukkit.getGlobalRegionScheduler().runDelayed(ModelAPI.PLUGIN, (var1x) -> {
         var1.run();
      }, (long)var2);
   }
}
