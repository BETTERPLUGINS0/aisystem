package fr.xephi.authme.libs.com.github.Anon8281.universalScheduler;

import fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.bukkitScheduler.BukkitScheduler;
import fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.foliaScheduler.FoliaScheduler;
import fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.paperScheduler.PaperScheduler;
import fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler;
import fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.utils.JavaUtil;
import org.bukkit.plugin.Plugin;

public class UniversalScheduler {
   public static final boolean isFolia = JavaUtil.classExists("io.papermc.paper.threadedregions.RegionizedServer");
   public static final boolean isExpandedSchedulingAvailable = JavaUtil.classExists("io.papermc.paper.threadedregions.scheduler.ScheduledTask");

   public static TaskScheduler getScheduler(Plugin plugin) {
      return (TaskScheduler)(isFolia ? new FoliaScheduler(plugin) : (isExpandedSchedulingAvailable ? new PaperScheduler(plugin) : new BukkitScheduler(plugin)));
   }
}
