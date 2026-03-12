package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.reader.impl;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.ChatType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.LastSeenMessages;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.filter.FilterMask;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.ChatMessage;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.ChatMessage_v1_19_3;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.reader.ChatMessageProcessor;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.time.Instant;
import java.util.UUID;

public class ChatMessageProcessor_v1_19_3 implements ChatMessageProcessor {
   public ChatMessage readChatMessage(@NotNull PacketWrapper<?> wrapper) {
      UUID senderUUID = wrapper.readUUID();
      int index = wrapper.readVarInt();
      byte[] signature = (byte[])wrapper.readOptional((w) -> {
         return w.readBytes(256);
      });
      String plainContent = wrapper.readString(256);
      Instant timestamp = wrapper.readTimestamp();
      long salt = wrapper.readLong();
      LastSeenMessages.Packed lastSeenMessagesPacked = wrapper.readLastSeenMessagesPacked();
      Component unsignedChatContent = (Component)wrapper.readOptional(PacketWrapper::readComponent);
      FilterMask filterMask = wrapper.readFilterMask();
      ChatType.Bound chatType = wrapper.readChatTypeBoundNetwork();
      return new ChatMessage_v1_19_3(senderUUID, index, signature, plainContent, timestamp, salt, lastSeenMessagesPacked, unsignedChatContent, filterMask, chatType);
   }

   public void writeChatMessage(@NotNull PacketWrapper<?> wrapper, @NotNull ChatMessage data) {
      ChatMessage_v1_19_3 newData = (ChatMessage_v1_19_3)data;
      wrapper.writeUUID(newData.getSenderUUID());
      wrapper.writeVarInt(newData.getIndex());
      wrapper.writeOptional(newData.getSignature(), PacketWrapper::writeBytes);
      wrapper.writeString(newData.getPlainContent());
      wrapper.writeTimestamp(newData.getTimestamp());
      wrapper.writeLong(newData.getSalt());
      wrapper.writeLastSeenMessagesPacked(newData.getLastSeenMessagesPacked());
      wrapper.writeOptional((Component)newData.getUnsignedChatContent().orElse((Object)null), PacketWrapper::writeComponent);
      wrapper.writeFilterMask(newData.getFilterMask());
      wrapper.writeChatTypeBoundNetwork(newData.getChatFormatting());
   }
}
