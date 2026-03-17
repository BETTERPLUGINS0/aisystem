package me.PM2.infinitevehicles.commands;

import java.util.Locale;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

class ACFBukkitListener implements Listener {
   private BukkitCommandManager manager;
   private final Plugin plugin;

   public ACFBukkitListener(BukkitCommandManager manager, Plugin plugin) {
      this.manager = var1;
      this.plugin = var2;
   }

   @EventHandler
   public void onPluginDisable(PluginDisableEvent event) {
      if (this.plugin.getName().equalsIgnoreCase(var1.getPlugin().getName())) {
         this.manager.unregisterCommands();
      }
   }

   @EventHandler
   public void onPlayerJoin(PlayerJoinEvent event) {
      Player var2 = var1.getPlayer();
      if (this.manager.autoDetectFromClient) {
         this.manager.readPlayerLocale(var2);
         this.manager.getScheduler().createDelayedTask(this.plugin, () -> {
            this.manager.readPlayerLocale(var2);
         }, 20L);
      } else {
         this.manager.setIssuerLocale(var2, this.manager.getLocales().getDefaultLocale());
         this.manager.notifyLocaleChange(this.manager.getCommandIssuer(var2), (Locale)null, this.manager.getLocales().getDefaultLocale());
      }

   }

   @EventHandler
   public void onPlayerQuit(PlayerQuitEvent quitEvent) {
      UUID var2 = var1.getPlayer().getUniqueId();
      this.manager.issuersLocale.remove(var2);
      this.manager.issuersLocaleString.remove(var2);
   }
}
