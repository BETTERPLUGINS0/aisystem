package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.ChatMessage;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.reader.ChatMessageProcessor;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.reader.impl.ChatMessageProcessorLegacy;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.reader.impl.ChatMessageProcessor_v1_16;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.reader.impl.ChatMessageProcessor_v1_19;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.reader.impl.ChatMessageProcessor_v1_19_1;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.reader.impl.ChatMessageProcessor_v1_19_3;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.reader.impl.ChatMessageProcessor_v1_21_5;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.reader.impl.ChatMessageProcessor_v1_7;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public class WrapperPlayServerChatMessage extends PacketWrapper<WrapperPlayServerChatMessage> {
   private static final ChatMessageProcessor CHAT_V1_7_PROCESSOR = new ChatMessageProcessor_v1_7();
   private static final ChatMessageProcessor CHAT_V1_8_PROCESSOR = new ChatMessageProcessorLegacy();
   private static final ChatMessageProcessor CHAT_V1_16_PROCESSOR = new ChatMessageProcessor_v1_16();
   private static final ChatMessageProcessor CHAT_V1_19_PROCESSOR = new ChatMessageProcessor_v1_19();
   private static final ChatMessageProcessor CHAT_V1_19_1_PROCESSOR = new ChatMessageProcessor_v1_19_1();
   private static final ChatMessageProcessor CHAT_V1_19_3_PROCESSOR = new ChatMessageProcessor_v1_19_3();
   private static final ChatMessageProcessor CHAT_V1_21_5_PROCESSOR = new ChatMessageProcessor_v1_21_5();
   private ChatMessage message;

   public WrapperPlayServerChatMessage(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerChatMessage(ChatMessage message) {
      super((PacketTypeCommon)PacketType.Play.Server.CHAT_MESSAGE);
      this.message = message;
   }

   public void read() {
      this.message = this.getProcessor().readChatMessage(this);
   }

   public void write() {
      this.getProcessor().writeChatMessage(this, this.message);
   }

   public void copy(WrapperPlayServerChatMessage wrapper) {
      this.message = wrapper.message;
   }

   public ChatMessage getMessage() {
      return this.message;
   }

   public void setMessage(ChatMessage message) {
      this.message = message;
   }

   @ApiStatus.Internal
   protected ChatMessageProcessor getProcessor() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
         return CHAT_V1_21_5_PROCESSOR;
      } else if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
         return CHAT_V1_19_3_PROCESSOR;
      } else if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_1)) {
         return CHAT_V1_19_1_PROCESSOR;
      } else if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
         return CHAT_V1_19_PROCESSOR;
      } else if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
         return CHAT_V1_16_PROCESSOR;
      } else {
         return this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8) ? CHAT_V1_8_PROCESSOR : CHAT_V1_7_PROCESSOR;
      }
   }
}
