package com.nisovin.shopkeepers.debug.events;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.debug.Debug;
import com.nisovin.shopkeepers.debug.DebugOptions;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.Bukkit;

public class EventDebugger {
   private final SKShopkeepersPlugin plugin;

   public EventDebugger(SKShopkeepersPlugin plugin) {
      Validate.notNull(plugin, (String)"plugin is null");
      this.plugin = plugin;
   }

   public void onEnable() {
      if (Settings.debug) {
         Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            boolean logAllEvent = Debug.isDebugging(DebugOptions.logAllEvents);
            boolean printListeners = Debug.isDebugging(DebugOptions.printListeners);
            if (logAllEvent || printListeners) {
               DebugListener.register(logAllEvent, printListeners);
            }

         }, 10L);
      }

   }

   public void onDisable() {
   }
}
