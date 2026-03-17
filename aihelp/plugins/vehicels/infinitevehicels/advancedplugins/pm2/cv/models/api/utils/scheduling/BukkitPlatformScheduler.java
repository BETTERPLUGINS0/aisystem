package advancedplugins.pm2.cv.models.api.utils.scheduling;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

public class BukkitPlatformScheduler implements PlatformScheduler {
   public PlatformTask scheduleRepeating(Plugin var1, Runnable var2, long var3, long var5) {
      return new BukkitPlatformTask(Bukkit.getScheduler().runTaskTimer(var1, var2, var3, var5));
   }

   public PlatformTask scheduleRepeating(Plugin var1, Entity var2, Runnable var3, long var4, long var6) {
      return this.scheduleRepeating(var1, var3, var4, var6);
   }

   public PlatformTask scheduleRepeating(Plugin var1, Location var2, Runnable var3, long var4, long var6) {
      return this.scheduleRepeating(var1, var3, var4, var6);
   }

   public PlatformTask scheduleRepeatingAsync(Plugin var1, Runnable var2, long var3, long var5) {
      return new BukkitPlatformTask(Bukkit.getScheduler().runTaskTimerAsynchronously(var1, var2, var3, var5));
   }
}
