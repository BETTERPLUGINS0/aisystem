package ac.grim.grimac.checks.impl.misc;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.client.WrapperConfigClientPluginMessage;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import java.util.Set;
import lombok.Generated;

public class ClientBrand extends Check implements PacketCheck {
   private static final String CHANNEL;
   private String brand = "vanilla";
   private boolean hasBrand = false;

   public ClientBrand(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.PLUGIN_MESSAGE) {
         WrapperPlayClientPluginMessage packet = new WrapperPlayClientPluginMessage(event);
         this.handle(packet.getChannelName(), packet.getData());
      } else if (event.getPacketType() == PacketType.Configuration.Client.PLUGIN_MESSAGE) {
         WrapperConfigClientPluginMessage packet = new WrapperConfigClientPluginMessage(event);
         this.handle(packet.getChannelName(), packet.getData());
      }

   }

   private void handle(String channel, byte[] data) {
      if (channel.equals(CHANNEL)) {
         if (data.length <= 64 && data.length != 0) {
            if (!this.hasBrand) {
               byte[] minusLength = new byte[data.length - 1];
               System.arraycopy(data, 1, minusLength, 0, minusLength.length);
               this.brand = (new String(minusLength)).replace(" (Velocity)", "");
               this.brand = MessageUtil.stripColor(this.brand);
               if (!GrimAPI.INSTANCE.getConfigManager().isIgnoredClient(this.brand)) {
                  String message = GrimAPI.INSTANCE.getConfigManager().getConfig().getStringElse("client-brand-format", "%prefix% &f%player% joined using %brand%");
                  Component component = MessageUtil.replacePlaceholders(this.player, MessageUtil.miniMessage(message));
                  GrimAPI.INSTANCE.getAlertManager().sendBrand(component, (Set)null);
               }
            }
         } else {
            this.brand = "sent " + data.length + " bytes as brand";
         }

         boolean hasReachHacks = this.brand.contains("forge") && this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_18_2) && this.player.getClientVersion().isOlderThan(ClientVersion.V_1_19_4);
         if (hasReachHacks && GrimAPI.INSTANCE.getConfigManager().isBlockBlacklistedForgeClients()) {
            this.player.disconnect(MessageUtil.miniMessage(MessageUtil.replacePlaceholders(this.player, GrimAPI.INSTANCE.getConfigManager().getDisconnectBlacklistedForge())));
         }

         this.hasBrand = true;
      }
   }

   @Generated
   public String getBrand() {
      return this.brand;
   }

   static {
      CHANNEL = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13) ? "minecraft:brand" : "MC|Brand";
   }
}
