package ac.grim.grimac.platform.bukkit.manager;

import ac.grim.grimac.platform.api.PlatformPlugin;
import ac.grim.grimac.platform.api.manager.PlatformPluginManager;
import ac.grim.grimac.platform.bukkit.BukkitPlatformPlugin;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class BukkitPlatformPluginManager implements PlatformPluginManager {
   public PlatformPlugin[] getPlugins() {
      Plugin[] bukkitPlugins = Bukkit.getPluginManager().getPlugins();
      PlatformPlugin[] plugins = new PlatformPlugin[bukkitPlugins.length];

      for(int i = 0; i < bukkitPlugins.length; ++i) {
         plugins[i] = new BukkitPlatformPlugin(bukkitPlugins[i]);
      }

      return plugins;
   }

   @Nullable
   public PlatformPlugin getPlugin(String pluginName) {
      Plugin bukkitPlugin = Bukkit.getPluginManager().getPlugin(pluginName);
      return bukkitPlugin == null ? null : new BukkitPlatformPlugin(bukkitPlugin);
   }
}
