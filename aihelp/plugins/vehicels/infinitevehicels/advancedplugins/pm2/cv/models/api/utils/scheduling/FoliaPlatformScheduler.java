package advancedplugins.pm2.cv.models.api.utils.scheduling;

import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

public class FoliaPlatformScheduler implements PlatformScheduler {
   public PlatformTask scheduleRepeating(Plugin var1, Runnable var2, long var3, long var5) {
      return new FoliaPlatformTask(Bukkit.getGlobalRegionScheduler().runAtFixedRate(var1, (var1x) -> {
         var2.run();
      }, var3, var5));
   }

   public PlatformTask scheduleRepeating(Plugin var1, Entity var2, Runnable var3, long var4, long var6) {
      return new FoliaPlatformTask(var2.getScheduler().runAtFixedRate(var1, (var1x) -> {
         var3.run();
      }, (Runnable)null, var4, var6));
   }

   public PlatformTask scheduleRepeating(Plugin var1, Location var2, Runnable var3, long var4, long var6) {
      return new FoliaPlatformTask(Bukkit.getRegionScheduler().runAtFixedRate(var1, var2, (var1x) -> {
         var3.run();
      }, var4, var6));
   }

   public PlatformTask scheduleRepeatingAsync(Plugin var1, Runnable var2, long var3, long var5) {
      return new FoliaPlatformTask(Bukkit.getAsyncScheduler().runAtFixedRate(var1, (var1x) -> {
         var2.run();
      }, var3 * 50L, var5 * 50L, TimeUnit.MILLISECONDS));
   }
}
