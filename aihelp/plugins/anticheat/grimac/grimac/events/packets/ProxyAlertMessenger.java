package ac.grim.grimac.events.packets;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.platform.api.player.PlatformPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerAbstract;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;
import ac.grim.grimac.shaded.configuralize.DynamicConfig;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.utils.anticheat.LogUtil;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Set;

public class ProxyAlertMessenger extends PacketListenerAbstract {
   private static boolean usingProxy;

   public ProxyAlertMessenger() {
      usingProxy = getBooleanFromFile("spigot.yml", "settings.bungeecord") || getBooleanFromFile("paper.yml", "settings.velocity-support.enabled") || PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_19) && getBooleanFromFile("config/paper-global.yml", "proxies.velocity.enabled");
      if (usingProxy) {
         LogUtil.info("Registering an outgoing plugin channel...");
         GrimAPI.INSTANCE.getPlatformServer().registerOutgoingPluginChannel("BungeeCord");
      }

   }

   public static void sendPluginMessage(String message) {
      if (canSendAlerts()) {
         ByteArrayOutputStream messageBytes = new ByteArrayOutputStream();
         ByteArrayDataOutput out = ByteStreams.newDataOutput();
         out.writeUTF("Forward");
         out.writeUTF("ONLINE");
         out.writeUTF("GRIMAC");

         try {
            (new DataOutputStream(messageBytes)).writeUTF(message);
         } catch (IOException var4) {
            LogUtil.error("Something went wrong whilst forwarding an alert to other servers!", var4);
            return;
         }

         out.writeShort(messageBytes.toByteArray().length);
         out.write(messageBytes.toByteArray());
         ((PlatformPlayer)Iterables.getFirst(GrimAPI.INSTANCE.getPlatformPlayerFactory().getOnlinePlayers(), (Object)null)).sendPluginMessage("BungeeCord", out.toByteArray());
      }
   }

   public static boolean canSendAlerts() {
      return usingProxy && GrimAPI.INSTANCE.getConfigManager().getConfig().getBooleanElse("alerts.proxy.send", false) && !GrimAPI.INSTANCE.getPlatformPlayerFactory().getOnlinePlayers().isEmpty();
   }

   public static boolean canReceiveAlerts() {
      return usingProxy && GrimAPI.INSTANCE.getConfigManager().getConfig().getBooleanElse("alerts.proxy.receive", false) && GrimAPI.INSTANCE.getAlertManager().hasAlertListeners();
   }

   private static boolean getBooleanFromFile(String pathToFile, String pathToValue) {
      File file = new File(pathToFile);
      if (!file.exists()) {
         return false;
      } else {
         DynamicConfig config = new DynamicConfig();
         config.addSource(ProxyAlertMessenger.class, "temp", file);

         try {
            config.loadAll();
            return config.getBoolean(pathToValue);
         } catch (Exception var5) {
            return false;
         }
      }
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.PLUGIN_MESSAGE && canReceiveAlerts()) {
         WrapperPlayClientPluginMessage wrapper = new WrapperPlayClientPluginMessage(event);
         if (wrapper.getChannelName().equals("BungeeCord") || wrapper.getChannelName().equals("bungeecord:main")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(wrapper.getData());
            if (in.readUTF().equals("GRIMAC")) {
               byte[] messageBytes = new byte[in.readShort()];
               in.readFully(messageBytes);

               String alert;
               try {
                  alert = (new DataInputStream(new ByteArrayInputStream(messageBytes))).readUTF();
               } catch (IOException var7) {
                  LogUtil.error("Something went wrong whilst reading an alert forwarded from another server!", var7);
                  return;
               }

               Component message = MessageUtil.miniMessage(alert);
               GrimAPI.INSTANCE.getAlertManager().sendAlert(message, (Set)null);
            }
         }
      }
   }
}
