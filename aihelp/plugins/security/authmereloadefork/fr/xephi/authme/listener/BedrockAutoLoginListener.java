package fr.xephi.authme.listener;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.message.Messages;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.HooksSettings;
import fr.xephi.authme.settings.properties.SecuritySettings;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.geysermc.floodgate.api.FloodgateApi;

public class BedrockAutoLoginListener implements Listener {
   private final AuthMeApi authmeApi = AuthMeApi.getInstance();
   @Inject
   private BukkitService bukkitService;
   @Inject
   private AuthMe plugin;
   @Inject
   private Messages messages;
   @Inject
   private Settings settings;

   private boolean isBedrockPlayer(UUID uuid) {
      return (Boolean)this.settings.getProperty(HooksSettings.HOOK_FLOODGATE_PLAYER) && (Boolean)this.settings.getProperty(SecuritySettings.FORCE_LOGIN_BEDROCK) && FloodgateApi.getInstance().isFloodgateId(uuid) && Bukkit.getServer().getPluginManager().getPlugin("floodgate") != null;
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onPlayerJoin(PlayerJoinEvent event) {
      Player player = event.getPlayer();
      String name = event.getPlayer().getName();
      UUID uuid = event.getPlayer().getUniqueId();
      this.bukkitService.runTaskLater(player, () -> {
         if (this.isBedrockPlayer(uuid) && !this.authmeApi.isAuthenticated(player) && this.authmeApi.isRegistered(name)) {
            this.authmeApi.forceLogin(player, true);
            this.messages.send(player, MessageKey.BEDROCK_AUTO_LOGGED_IN);
         }

      }, 20L);
   }
}
