package ac.grim.grimac.internal.platform.bukkit.resolver;

import ac.grim.grimac.api.plugin.BasicGrimPlugin;
import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.internal.plugin.resolver.GrimExtensionManager;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public final class BukkitResolverRegistrar {
   private final Map<Plugin, GrimPlugin> pluginCache = new ConcurrentHashMap();

   public void registerAll(GrimExtensionManager extensionManager) {
      extensionManager.setFailureHandler(this::createFailureException);
      extensionManager.registerResolver(this::resolvePluginInstance);
      extensionManager.registerResolver(this::resolveStringName);
      extensionManager.registerResolver(this::resolveClass);
   }

   public GrimPlugin resolvePlugin(Plugin bukkitPlugin) {
      return (GrimPlugin)this.pluginCache.computeIfAbsent(bukkitPlugin, (plugin) -> {
         PluginDescriptionFile desc = plugin.getDescription();
         return new BasicGrimPlugin(plugin.getLogger(), plugin.getDataFolder(), desc.getVersion(), desc.getDescription(), desc.getAuthors());
      });
   }

   private GrimPlugin resolvePluginInstance(Object context) {
      if (context instanceof Plugin) {
         Plugin bukkitPlugin = (Plugin)context;
         return this.resolvePlugin(bukkitPlugin);
      } else {
         return null;
      }
   }

   private GrimPlugin resolveStringName(Object context) {
      if (context instanceof String) {
         String pluginName = (String)context;
         Plugin bukkitPlugin = Bukkit.getPluginManager().getPlugin(pluginName);
         return bukkitPlugin != null ? this.resolvePlugin(bukkitPlugin) : null;
      } else {
         return null;
      }
   }

   private GrimPlugin resolveClass(Object context) {
      if (context instanceof Class) {
         try {
            JavaPlugin providingPlugin = JavaPlugin.getProvidingPlugin((Class)context);
            return this.resolvePlugin(providingPlugin);
         } catch (IllegalStateException | IllegalArgumentException var3) {
            return null;
         }
      } else {
         return null;
      }
   }

   private RuntimeException createFailureException(Object failedContext) {
      String message = "Failed to resolve GrimPlugin context from the provided object of type '%s'.\n\nPlease ensure you are passing one of the following:\n  - The main instance of your plugin (e.g., 'this' from your class extending JavaPlugin).\n  - The plugin name as a String (e.g., \"MyPluginName\").\n  - Any Class from your plugin's JAR file (e.g., MyListener.class).\n  - A pre-existing GrimPlugin instance.\n".formatted(new Object[]{failedContext.getClass().getName()});
      return new IllegalArgumentException(message);
   }
}
