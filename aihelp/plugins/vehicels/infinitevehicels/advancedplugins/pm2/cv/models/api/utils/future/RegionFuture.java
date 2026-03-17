package advancedplugins.pm2.cv.models.api.utils.future;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class RegionFuture<T> extends AbstractFoliaFuture<T> {
   private final Location location;

   private RegionFuture(Location var1) {
      this.location = var1;
   }

   private RegionFuture(Location var1, T var2) {
      super(var2);
      this.location = var1;
   }

   static <T> RegionFuture<T> empty(Location var0) {
      return new RegionFuture(var0);
   }

   static <T> RegionFuture<T> completed(Location var0, T var1) {
      return new RegionFuture(var0, var1);
   }

   protected <U> AbstractFuture<U> createEmpty() {
      return empty(this.location);
   }

   protected void executeSync(Runnable var1) {
      Bukkit.getRegionScheduler().execute(ModelAPI.PLUGIN, this.location, var1);
   }

   protected void executeSync(Runnable var1, int var2) {
      Bukkit.getRegionScheduler().runDelayed(ModelAPI.PLUGIN, this.location, (var1x) -> {
         var1.run();
      }, (long)var2);
   }
}
