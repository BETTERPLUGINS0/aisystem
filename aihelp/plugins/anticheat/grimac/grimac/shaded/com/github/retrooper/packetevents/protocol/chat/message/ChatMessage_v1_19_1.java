package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.ChatType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.LastSeenMessages;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.filter.FilterMask;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.time.Instant;
import java.util.UUID;

public class ChatMessage_v1_19_1 extends ChatMessage_v1_16 {
   private String plainContent;
   @Nullable
   private Component unsignedChatContent;
   private ChatType.Bound chatFormatting;
   @Nullable
   private byte[] previousSignature;
   private byte[] signature;
   private Instant timestamp;
   private long salt;
   private LastSeenMessages lastSeenMessages;
   private FilterMask filterMask;

   public ChatMessage_v1_19_1(String plainContent, Component decoratedChatContent, @Nullable Component unsignedChatContent, UUID senderUUID, ChatType.Bound chatFormatting, @Nullable byte[] previousSignature, byte[] signature, Instant timestamp, long salt, LastSeenMessages lastSeenMessages, FilterMask filterMask) {
      super(decoratedChatContent, chatFormatting.getType(), senderUUID);
      this.plainContent = plainContent;
      this.unsignedChatContent = unsignedChatContent;
      this.chatFormatting = chatFormatting;
      this.previousSignature = previousSignature;
      this.signature = signature;
      this.timestamp = timestamp;
      this.salt = salt;
      this.lastSeenMessages = lastSeenMessages;
      this.filterMask = filterMask;
   }

   public String getPlainContent() {
      return this.plainContent;
   }

   public void setPlainContent(String plainContent) {
      this.plainContent = plainContent;
   }

   public boolean isChatContentDecorated() {
      return !this.getChatContent().equals(Component.text(this.plainContent));
   }

   @Nullable
   public Component getUnsignedChatContent() {
      return this.unsignedChatContent;
   }

   public void setUnsignedChatContent(@Nullable Component unsignedChatContent) {
      this.unsignedChatContent = unsignedChatContent;
   }

   public ChatType getType() {
      return this.chatFormatting.getType();
   }

   public void setType(ChatType type) {
      this.chatFormatting.setType(type);
   }

   public ChatType.Bound getChatFormatting() {
      return this.chatFormatting;
   }

   public void setChatFormatting(ChatType.Bound chatFormatting) {
      this.chatFormatting = chatFormatting;
   }

   /** @deprecated */
   @Deprecated
   public ChatType.Bound getChatType() {
      return this.chatFormatting;
   }

   /** @deprecated */
   @Deprecated
   public void setChatType(ChatType.Bound chatFormatting) {
      this.chatFormatting = chatFormatting;
   }

   @Nullable
   public byte[] getPreviousSignature() {
      return this.previousSignature;
   }

   public void setPreviousSignature(@Nullable byte[] previousSignature) {
      this.previousSignature = previousSignature;
   }

   public byte[] getSignature() {
      return this.signature;
   }

   public void setSignature(byte[] signature) {
      this.signature = signature;
   }

   public Instant getTimestamp() {
      return this.timestamp;
   }

   public void setTimestamp(Instant timestamp) {
      this.timestamp = timestamp;
   }

   public long getSalt() {
      return this.salt;
   }

   public void setSalt(long salt) {
      this.salt = salt;
   }

   public LastSeenMessages getLastSeenMessages() {
      return this.lastSeenMessages;
   }

   public void setLastSeenMessages(LastSeenMessages lastSeenMessages) {
      this.lastSeenMessages = lastSeenMessages;
   }

   public FilterMask getFilterMask() {
      return this.filterMask;
   }

   public void setFilterMask(FilterMask filterMask) {
      this.filterMask = filterMask;
   }

   /** @deprecated */
   @Deprecated
   public static class ChatTypeBoundNetwork {
      private ChatType type;
      private Component name;
      @Nullable
      private Component targetName;

      public ChatTypeBoundNetwork(ChatType type, Component name, @Nullable Component targetName) {
         this.type = type;
         this.name = name;
         this.targetName = targetName;
      }

      public ChatType getType() {
         return this.type;
      }

      public void setType(ChatType type) {
         this.type = type;
      }

      public Component getName() {
         return this.name;
      }

      public void setName(Component name) {
         this.name = name;
      }

      @Nullable
      public Component getTargetName() {
         return this.targetName;
      }

      public void setTargetName(@Nullable Component targetName) {
         this.targetName = targetName;
      }
   }
}
