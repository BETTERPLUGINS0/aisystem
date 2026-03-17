package advancedplugins.pm2.cv.models.api.utils;

import advancedplugins.pm2.cv.models.api.utils.logger.LogUtil;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CompatibilityManager implements Listener {
   private final PluginManager pluginManager = Bukkit.getPluginManager();
   private final Set<String> allPlugins = Sets.newConcurrentHashSet();
   private final Map<String, CompatibilityManager.CompatibilityConfiguration> compatibilities = Maps.newConcurrentMap();

   public CompatibilityManager(JavaPlugin var1) {
      Plugin[] var2 = this.pluginManager.getPlugins();
      int var3 = var2.length;
      Plugin[] var4 = var2;
      int var5 = var2.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Plugin var7 = var4[var6];
         this.allPlugins.add(var7.getName());
      }

      this.pluginManager.registerEvents(this, var1);
   }

   public void registerSupport(String var1, CompatibilityManager.CompatibilityConfiguration var2) {
      if (this.allPlugins.contains(var1)) {
         Plugin var3 = this.pluginManager.getPlugin(var1);
         if (var3 != null && var3.isEnabled() && var2.tryApply(var3)) {
            LogUtil.log("Compatibility applied: " + var3.getName());
         } else {
            this.compatibilities.put(var1, var2);
         }
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onPluginLoad(PluginEnableEvent var1) {
      Plugin var2 = var1.getPlugin();
      if (var2.isEnabled()) {
         CompatibilityManager.CompatibilityConfiguration var3 = (CompatibilityManager.CompatibilityConfiguration)this.compatibilities.get(var2.getName());
         if (var3 != null) {
            if (!var3.tryApply(var2)) {
               LogUtil.error("Failed to apply compatibility support for " + var2.getName() + ".");
            } else {
               LogUtil.log("Compatibility applied: " + var2.getName());
            }
         }
      }

   }

   @FunctionalInterface
   public interface CompatibilityConfiguration {
      boolean tryApply(Plugin var1);
   }
}
