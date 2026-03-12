package fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.paperScheduler;

import fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.foliaScheduler.FoliaScheduler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class PaperScheduler extends FoliaScheduler {
   public PaperScheduler(Plugin plugin) {
      super(plugin);
   }

   public boolean isGlobalThread() {
      return Bukkit.getServer().isPrimaryThread();
   }
}
