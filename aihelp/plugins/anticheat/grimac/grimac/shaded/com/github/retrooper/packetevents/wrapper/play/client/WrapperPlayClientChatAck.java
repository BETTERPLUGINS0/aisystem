package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.LastSeenMessages;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientChatAck extends PacketWrapper<WrapperPlayClientChatAck> {
   private LastSeenMessages.LegacyUpdate lastSeenMessages;
   private int offset;

   public WrapperPlayClientChatAck(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientChatAck(LastSeenMessages.LegacyUpdate lastSeenMessages) {
      super((PacketTypeCommon)PacketType.Play.Client.CHAT_ACK);
      this.lastSeenMessages = lastSeenMessages;
   }

   public WrapperPlayClientChatAck(int offset) {
      super((PacketTypeCommon)PacketType.Play.Client.CHAT_ACK);
      this.offset = offset;
   }

   public void read() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
         this.offset = this.readVarInt();
      } else {
         this.lastSeenMessages = this.readLegacyLastSeenMessagesUpdate();
      }

   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
         this.writeVarInt(this.offset);
      } else {
         this.writeLegacyLastSeenMessagesUpdate(this.lastSeenMessages);
      }

   }

   public void copy(WrapperPlayClientChatAck wrapper) {
      this.lastSeenMessages = wrapper.lastSeenMessages;
      this.offset = wrapper.offset;
   }

   public int getOffset() {
      return this.offset;
   }

   public void setOffset(int offset) {
      this.offset = offset;
   }

   public LastSeenMessages.LegacyUpdate getLastSeenMessages() {
      return this.lastSeenMessages;
   }

   public void setLastSeenMessages(LastSeenMessages.LegacyUpdate lastSeenMessages) {
      this.lastSeenMessages = lastSeenMessages;
   }
}
