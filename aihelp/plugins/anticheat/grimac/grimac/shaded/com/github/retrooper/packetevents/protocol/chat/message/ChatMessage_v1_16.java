package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.ChatType;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.UUID;

public class ChatMessage_v1_16 extends ChatMessage {
   private UUID senderUUID;

   public ChatMessage_v1_16(Component chatContent, ChatType type, UUID senderUUID) {
      super(chatContent, type);
      this.senderUUID = senderUUID;
   }

   public UUID getSenderUUID() {
      return this.senderUUID;
   }

   public void setSenderUUID(UUID senderUUID) {
      this.senderUUID = senderUUID;
   }
}
