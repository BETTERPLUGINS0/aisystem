package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.ChatType;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.time.Instant;
import java.util.UUID;

public class ChatMessage_v1_19 extends ChatMessage_v1_16 {
   @Nullable
   private Component unsignedChatContent;
   private Component senderDisplayName;
   @Nullable
   private Component teamName;
   private Instant timestamp;
   private long salt;
   private byte[] signature;

   public ChatMessage_v1_19(Component chatContent, @Nullable Component unsignedChatContent, ChatType type, UUID senderUUID, Component senderDisplayName, @Nullable Component teamName, Instant timestamp, long salt, byte[] signature) {
      super(chatContent, type, senderUUID);
      this.unsignedChatContent = unsignedChatContent;
      this.senderDisplayName = senderDisplayName;
      this.teamName = teamName;
      this.timestamp = timestamp;
      this.salt = salt;
      this.signature = signature;
   }

   @Nullable
   public Component getUnsignedChatContent() {
      return this.unsignedChatContent;
   }

   public Component getSenderDisplayName() {
      return this.senderDisplayName;
   }

   @Nullable
   public Component getTeamName() {
      return this.teamName;
   }

   public Instant getTimestamp() {
      return this.timestamp;
   }

   public long getSalt() {
      return this.salt;
   }

   public byte[] getSignature() {
      return this.signature;
   }

   public void setUnsignedChatContent(@Nullable Component unsignedChatContent) {
      this.unsignedChatContent = unsignedChatContent;
   }

   public void setSenderDisplayName(Component senderDisplayName) {
      this.senderDisplayName = senderDisplayName;
   }

   public void setTeamName(@Nullable Component teamName) {
      this.teamName = teamName;
   }

   public void setTimestamp(Instant timestamp) {
      this.timestamp = timestamp;
   }

   public void setSalt(long salt) {
      this.salt = salt;
   }

   public void setSignature(byte[] signature) {
      this.signature = signature;
   }
}
