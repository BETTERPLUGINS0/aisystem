package ac.grim.grimac.platform.bukkit;

import ac.grim.grimac.platform.api.PlatformPlugin;
import org.bukkit.plugin.Plugin;

public class BukkitPlatformPlugin implements PlatformPlugin {
   private final Plugin plugin;

   public BukkitPlatformPlugin(Plugin plugin) {
      this.plugin = plugin;
   }

   public boolean isEnabled() {
      return this.plugin.isEnabled();
   }

   public String getName() {
      return this.plugin.getName();
   }

   public String getVersion() {
      return this.plugin.getDescription().getVersion();
   }
}
