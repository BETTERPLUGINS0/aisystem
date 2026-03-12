package fr.xephi.authme.service.bungeecord;

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
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class BungeeReceiver implements PluginMessageListener, SettingsDependent {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(BungeeReceiver.class);
   private final AuthMe plugin;
   private final BukkitService bukkitService;
   private final ProxySessionManager proxySessionManager;
   private final Management management;
   private boolean isEnabled;

   @Inject
   BungeeReceiver(AuthMe plugin, BukkitService bukkitService, ProxySessionManager proxySessionManager, Management management, Settings settings) {
      this.plugin = plugin;
      this.bukkitService = bukkitService;
      this.proxySessionManager = proxySessionManager;
      this.management = management;
      this.reload(settings);
   }

   public void reload(Settings settings) {
      this.isEnabled = (Boolean)settings.getProperty(HooksSettings.BUNGEECORD);
      if (this.isEnabled) {
         this.isEnabled = (Boolean)this.bukkitService.isBungeeCordConfiguredForSpigot().orElse(false);
      }

      if (this.isEnabled) {
         Messenger messenger = this.plugin.getServer().getMessenger();
         if (!messenger.isIncomingChannelRegistered(this.plugin, "BungeeCord")) {
            messenger.registerIncomingPluginChannel(this.plugin, "BungeeCord", this);
         }
      }

   }

   private void handleBroadcast(ByteArrayDataInput in) {
      short dataLength = in.readShort();
      byte[] dataBytes = new byte[dataLength];
      in.readFully(dataBytes);
      ByteArrayDataInput dataIn = ByteStreams.newDataInput(dataBytes);
      String typeId = dataIn.readUTF();
      Optional<MessageType> type = MessageType.fromId(typeId);
      if (!type.isPresent()) {
         this.logger.debug("Received unsupported forwarded bungeecord message type! ({0})", (Object)typeId);
      } else {
         try {
            String var7 = dataIn.readUTF();
         } catch (IllegalStateException var9) {
            this.logger.warning("Received invalid forwarded plugin message of type " + ((MessageType)type.get()).name() + ": argument is missing!");
            return;
         }

         switch((MessageType)type.get()) {
         case LOGIN:
         case LOGOUT:
         default:
         }
      }
   }

   private void handle(ByteArrayDataInput in) {
      String typeId = in.readUTF();
      Optional<MessageType> type = MessageType.fromId(typeId);
      if (!type.isPresent()) {
         this.logger.debug("Received unsupported bungeecord message type! ({0})", (Object)typeId);
      } else {
         String argument;
         try {
            argument = in.readUTF();
         } catch (IllegalStateException var6) {
            this.logger.warning("Received invalid plugin message of type " + ((MessageType)type.get()).name() + ": argument is missing!");
            return;
         }

         switch((MessageType)type.get()) {
         case PERFORM_LOGIN:
            this.performLogin(argument);
         default:
         }
      }
   }

   public void onPluginMessageReceived(String channel, Player player, byte[] data) {
      if (this.isEnabled) {
         ByteArrayDataInput in = ByteStreams.newDataInput(data);
         String subChannel = in.readUTF();
         if ("AuthMe.v2.Broadcast".equals(subChannel)) {
            this.handleBroadcast(in);
         } else if ("AuthMe.v2".equals(subChannel)) {
            this.handle(in);
         }

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
