package fr.xephi.authme.service.velocity;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.ProxySessionManager;
import fr.xephi.authme.initialization.SettingsDependent;
import fr.xephi.authme.libs.com.google.common.io.ByteArrayDataInput;
import fr.xephi.authme.libs.com.google.common.io.ByteStreams;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.process.Management;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.HooksSettings;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class VelocityReceiver implements PluginMessageListener, SettingsDependent {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(VelocityReceiver.class);
   private final AuthMe plugin;
   private final BukkitService bukkitService;
   private final ProxySessionManager proxySessionManager;
   private final Management management;
   private boolean isEnabled;

   @Inject
   VelocityReceiver(AuthMe plugin, BukkitService bukkitService, ProxySessionManager proxySessionManager, Management management, Settings settings) {
      this.plugin = plugin;
      this.bukkitService = bukkitService;
      this.proxySessionManager = proxySessionManager;
      this.management = management;
      this.reload(settings);
   }

   public void reload(Settings settings) {
      this.isEnabled = (Boolean)settings.getProperty(HooksSettings.VELOCITY);
      if (this.isEnabled) {
         Messenger messenger = this.plugin.getServer().getMessenger();
         if (!messenger.isIncomingChannelRegistered(this.plugin, "authmevelocity:main")) {
            messenger.registerIncomingPluginChannel(this.plugin, "authmevelocity:main", this);
         }
      }

   }

   public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
      if (this.isEnabled) {
         if (channel.equals("authmevelocity:main")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
            String data = in.readUTF();
            String username = in.readUTF();
            this.processData(username, data);
            this.logger.debug("PluginMessage | AuthMeVelocity identifier processed");
         }

      }
   }

   private void processData(String username, String data) {
      if (VMessageType.LOGIN.toString().equals(data)) {
         this.performLogin(username);
      }

   }

   private void performLogin(String name) {
      Player player = this.bukkitService.getPlayerExact(name);
      if (player != null && player.isOnline()) {
         this.management.forceLogin(player, true);
         this.logger.info("The user " + player.getName() + " has been automatically logged in, as requested via plugin messaging.");
      } else {
         this.proxySessionManager.processProxySessionMessage(name);
         this.logger.info("The user " + name + " should be automatically logged in, as requested via plugin messaging but has not been detected, nickname has been added to autologin queue.");
      }

   }
}
