package github.nighter.smartspawner.api;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class SmartSpawnerProvider {
   private static final String PLUGIN_NAME = "SmartSpawner";

   public static SmartSpawnerAPI getAPI() {
      Plugin plugin = Bukkit.getPluginManager().getPlugin("SmartSpawner");
      if (plugin == null) {
         return null;
      } else {
         return !(plugin instanceof SmartSpawnerPlugin) ? null : ((SmartSpawnerPlugin)plugin).getAPI();
      }
   }
}
