package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.ChatType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.LastSeenMessages;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.filter.FilterMask;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.time.Instant;
import java.util.UUID;

public class ChatMessage_v1_21_5 extends ChatMessage_v1_19_3 {
   int globalIndex;

   public ChatMessage_v1_21_5(int globalIndex, UUID senderUUID, int index, byte[] signature, String plainContent, Instant timestamp, long salt, LastSeenMessages.Packed lastSeenMessagesPacked, @Nullable Component unsignedChatContent, FilterMask filterMask, ChatType.Bound chatFormatting) {
      super(senderUUID, index, signature, plainContent, timestamp, salt, lastSeenMessagesPacked, unsignedChatContent, filterMask, chatFormatting);
      this.globalIndex = globalIndex;
   }

   public int getGlobalIndex() {
      return this.globalIndex;
   }

   public void setGlobalIndex(int globalIndex) {
      this.globalIndex = globalIndex;
   }
}
