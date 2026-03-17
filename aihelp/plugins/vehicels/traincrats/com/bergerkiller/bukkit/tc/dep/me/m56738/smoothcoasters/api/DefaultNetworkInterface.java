package com.bergerkiller.bukkit.tc.dep.me.m56738.smoothcoasters.api;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class DefaultNetworkInterface implements NetworkInterface {
   private final Plugin plugin;

   public DefaultNetworkInterface(Plugin plugin) {
      this.plugin = plugin;
   }

   public void sendMessage(Player player, String channel, byte[] data) {
      player.sendPluginMessage(this.plugin, channel, data);
   }
}
