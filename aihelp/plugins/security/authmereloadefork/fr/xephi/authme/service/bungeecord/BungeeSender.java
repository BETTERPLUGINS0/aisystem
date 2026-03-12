package fr.xephi.authme.service.bungeecord;

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
import java.util.Locale;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.Messenger;

public class BungeeSender implements SettingsDependent {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(BungeeSender.class);
   private final AuthMe plugin;
   private final BukkitService bukkitService;
   private boolean isEnabled;
   private String destinationServerOnLogin;

   @Inject
   BungeeSender(AuthMe plugin, BukkitService bukkitService, Settings settings) {
      this.plugin = plugin;
      this.bukkitService = bukkitService;
      this.reload(settings);
   }

   public void reload(Settings settings) {
      this.isEnabled = (Boolean)settings.getProperty(HooksSettings.BUNGEECORD);
      this.destinationServerOnLogin = (String)settings.getProperty(HooksSettings.BUNGEECORD_SERVER);
      if (this.isEnabled) {
         Messenger messenger = this.plugin.getServer().getMessenger();
         if (!messenger.isOutgoingChannelRegistered(this.plugin, "BungeeCord")) {
            messenger.registerOutgoingPluginChannel(this.plugin, "BungeeCord");
         }
      }

   }

   public boolean isEnabled() {
      return this.isEnabled;
   }

   private void sendBungeecordMessage(Player player, String... data) {
      ByteArrayDataOutput out = ByteStreams.newDataOutput();
      String[] var4 = data;
      int var5 = data.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String element = var4[var6];
         out.writeUTF(element);
      }

      this.bukkitService.sendBungeeMessage(player, out.toByteArray());
   }

   private void sendForwardedBungeecordMessage(Player player, String subChannel, String... data) {
      ByteArrayDataOutput out = ByteStreams.newDataOutput();
      out.writeUTF("Forward");
      out.writeUTF("ONLINE");
      out.writeUTF(subChannel);
      ByteArrayDataOutput dataOut = ByteStreams.newDataOutput();
      String[] var6 = data;
      int var7 = data.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         String element = var6[var8];
         dataOut.writeUTF(element);
      }

      byte[] dataBytes = dataOut.toByteArray();
      out.writeShort(dataBytes.length);
      out.write(dataBytes);
      this.bukkitService.sendBungeeMessage(player, out.toByteArray());
   }

   public void connectPlayerOnLogin(Player player) {
      if (this.isEnabled && !this.destinationServerOnLogin.isEmpty()) {
         this.bukkitService.scheduleSyncDelayedTask(() -> {
            this.sendBungeecordMessage(player, "Connect", this.destinationServerOnLogin);
         }, 10L);
      }
   }

   public void sendAuthMeBungeecordMessage(Player player, MessageType type) {
      if (this.isEnabled) {
         if (!this.plugin.isEnabled()) {
            this.logger.debug("Tried to send a " + type + " bungeecord message but the plugin was disabled!");
         } else {
            if (type.isBroadcast()) {
               this.sendForwardedBungeecordMessage(player, "AuthMe.v2.Broadcast", type.getId(), player.getName().toLowerCase(Locale.ROOT));
            } else {
               this.sendBungeecordMessage(player, "AuthMe.v2", type.getId(), player.getName().toLowerCase(Locale.ROOT));
            }

         }
      }
   }
}
