package ac.grim.grimac.checks.impl.chat;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.common.client.WrapperCommonClientSettings;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSettings;

@CheckData(
   name = "ChatD",
   description = "Chatting while chat is hidden",
   experimental = true
)
public class ChatD extends Check implements PacketCheck {
   private boolean hidden;

   public ChatD(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if ((event.getPacketType() == PacketType.Play.Client.CHAT_MESSAGE || event.getPacketType() == PacketType.Play.Client.CHAT_COMMAND_UNSIGNED || event.getPacketType() == PacketType.Play.Client.CHAT_COMMAND) && this.hidden && this.flagAndAlert() && this.shouldModifyPackets()) {
         event.setCancelled(true);
         this.player.onPacketCancel();
      }

      if (event.getPacketType() == PacketType.Play.Client.CLIENT_SETTINGS || event.getPacketType() == PacketType.Configuration.Client.CLIENT_SETTINGS) {
         this.hidden = (new WrapperPlayClientSettings(event)).getChatVisibility() == WrapperCommonClientSettings.ChatVisibility.HIDDEN;
      }

   }
}
