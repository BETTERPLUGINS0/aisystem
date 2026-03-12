package fr.xephi.authme.service.velocity;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.initialization.SettingsDependent;
import fr.xephi.authme.libs.com.google.common.io.ByteArrayDataOutput;
import fr.xephi.authme.libs.com.google.common.io.ByteStreams;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.HooksSettings;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.Messenger;

public class VelocitySender implements SettingsDependent {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(VelocitySender.class);
   private final AuthMe plugin;
   private final BukkitService bukkitService;
   private boolean isEnabled;

   @Inject
   VelocitySender(AuthMe plugin, BukkitService bukkitService, Settings settings) {
      this.plugin = plugin;
      this.bukkitService = bukkitService;
      this.reload(settings);
   }

   public void reload(Settings settings) {
      this.isEnabled = (Boolean)settings.getProperty(HooksSettings.VELOCITY);
      if (this.isEnabled) {
         Messenger messenger = this.plugin.getServer().getMessenger();
         if (!messenger.isOutgoingChannelRegistered(this.plugin, "authmevelocity:main")) {
            messenger.registerOutgoingPluginChannel(this.plugin, "authmevelocity:main");
         }
      }

   }

   public boolean isEnabled() {
      return this.isEnabled;
   }

   private void sendForwardedVelocityMessage(Player player, VMessageType type, String playerName) {
      ByteArrayDataOutput out = ByteStreams.newDataOutput();
      out.writeUTF(type.toString());
      out.writeUTF(playerName);
      this.bukkitService.sendVelocityMessage(player, out.toByteArray());
   }

   public void sendAuthMeVelocityMessage(Player player, VMessageType type) {
      if (this.isEnabled) {
         if (!this.plugin.isEnabled()) {
            this.logger.debug("Tried to send a " + type + " velocity message but the plugin was disabled!");
         } else {
            this.sendForwardedVelocityMessage(player, type, player.getName());
         }
      }
   }
}
