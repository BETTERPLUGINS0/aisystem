package advancedplugins.pm2.cv.models.api.utils.future;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import org.bukkit.Bukkit;

public class LegacyFuture<T> extends AbstractFuture<T> {
   private LegacyFuture() {
   }

   private LegacyFuture(T var1) {
      super(var1);
   }

   static <T> LegacyFuture<T> empty() {
      return new LegacyFuture();
   }

   static <T> LegacyFuture<T> completed(T var0) {
      return new LegacyFuture(var0);
   }

   protected <U> AbstractFuture<U> createEmpty() {
      return empty();
   }

   protected void executeSync(Runnable var1) {
      if (Bukkit.isPrimaryThread()) {
         var1.run();
      } else {
         Bukkit.getScheduler().runTask(ModelAPI.PLUGIN, var1);
      }

   }

   protected void executeSync(Runnable var1, int var2) {
      Bukkit.getScheduler().runTaskLater(ModelAPI.PLUGIN, var1, (long)var2);
   }

   protected void executeAsync(Runnable var1) {
      if (Bukkit.isPrimaryThread()) {
         Bukkit.getScheduler().runTaskAsynchronously(ModelAPI.PLUGIN, var1);
      } else {
         var1.run();
      }

   }

   protected void executeAsync(Runnable var1, int var2) {
      Bukkit.getScheduler().runTaskLaterAsynchronously(ModelAPI.PLUGIN, var1, (long)var2);
   }
}
