package libs.com.ryderbelserion.vital.common.api.managers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import libs.com.ryderbelserion.vital.common.VitalAPI;
import libs.com.ryderbelserion.vital.common.api.Provider;
import libs.com.ryderbelserion.vital.common.api.interfaces.IPlugin;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PluginManager {
   private static final VitalAPI api = Provider.getApi();
   private static final ComponentLogger logger;
   private static final boolean isVerbose;
   private static final Map<String, IPlugin> plugins;

   public static void registerPlugin(@NotNull IPlugin plugin) {
      plugins.put(plugin.getName(), plugin);
      plugin.init();
   }

   @Nullable
   public static IPlugin getPlugin(@NotNull String name) {
      return (IPlugin)plugins.get(name);
   }

   public static boolean isEnabled(@NotNull String name) {
      IPlugin plugin = getPlugin(name);
      return plugin == null ? false : plugin.isEnabled();
   }

   public static void unregisterPlugin(@NotNull IPlugin plugin) {
      plugins.remove(plugin.getName());
      plugin.stop();
   }

   public static void printPlugins() {
      if (isVerbose) {
         getPlugins().forEach((name, plugin) -> {
            if (plugin.isEnabled() && !name.isEmpty()) {
               logger.info("{}: FOUND", name);
            } else {
               logger.info("{}: NOT FOUND", name);
            }
         });
      }

   }

   @NotNull
   public static Map<String, IPlugin> getPlugins() {
      return Collections.unmodifiableMap(plugins);
   }

   static {
      logger = api.getComponentLogger();
      isVerbose = api.isVerbose();
      plugins = new HashMap();
   }
}
