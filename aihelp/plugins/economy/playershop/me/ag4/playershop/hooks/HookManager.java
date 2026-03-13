package me.ag4.playershop.hooks;

import me.ag4.playershop.files.Config;
import org.bukkit.Bukkit;
import org.geysermc.floodgate.api.FloodgateApi;

public class HookManager {
   public boolean isFloodgateEnabled() {
      if (Bukkit.getPluginManager().isPluginEnabled("Floodgate")) {
         FloodgateApi api = FloodgateApi.getInstance();
         return api != null;
      } else {
         return false;
      }
   }

   public boolean isWorldGuardEnabled() {
      return Config.IS_WORLDGUARD_ENABLE.toBoolean();
   }
}
