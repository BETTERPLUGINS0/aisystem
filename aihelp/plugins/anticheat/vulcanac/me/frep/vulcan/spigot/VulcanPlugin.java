package me.frep.vulcan.spigot;

import org.bukkit.plugin.java.JavaPlugin;

public final class VulcanPlugin extends JavaPlugin {
   public void onEnable() {
      loadConfig0();
      Vulcan_Xs.INSTANCE.Vulcan_x(this);
   }

   public void onDisable() {
      Vulcan_Xs.INSTANCE.Vulcan_Y(this);
   }
}
