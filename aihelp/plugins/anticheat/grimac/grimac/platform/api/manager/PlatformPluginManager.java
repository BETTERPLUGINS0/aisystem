package ac.grim.grimac.platform.api.manager;

import ac.grim.grimac.platform.api.PlatformPlugin;

public interface PlatformPluginManager {
   PlatformPlugin[] getPlugins();

   PlatformPlugin getPlugin(String var1);

   default boolean isPluginEnabled(String pluginName) {
      PlatformPlugin plugin = this.getPlugin(pluginName);
      return plugin != null && plugin.isEnabled();
   }
}
