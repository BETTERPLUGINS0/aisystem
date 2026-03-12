package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.reader.impl;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.ChatType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.ChatMessage;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.ChatMessage_v1_19;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.reader.ChatMessageProcessor;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.time.Instant;
import java.util.UUID;

public class ChatMessageProcessor_v1_19 implements ChatMessageProcessor {
   public ChatMessage readChatMessage(@NotNull PacketWrapper<?> wrapper) {
      Component chatContent = wrapper.readComponent();
      Component unsignedChatContent = (Component)wrapper.readOptional(PacketWrapper::readComponent);
      ChatType type = (ChatType)wrapper.readMappedEntity((IRegistry)ChatTypes.getRegistry());
      UUID senderUUID = wrapper.readUUID();
      Component senderDisplayName = wrapper.readComponent();
      Component teamName = (Component)wrapper.readOptional(PacketWrapper::readComponent);
      Instant timestamp = wrapper.readTimestamp();
      long salt = wrapper.readLong();
      byte[] signature = wrapper.readByteArray();
      return new ChatMessage_v1_19(chatContent, unsignedChatContent, type, senderUUID, senderDisplayName, teamName, timestamp, salt, signature);
   }

   public void writeChatMessage(@NotNull PacketWrapper<?> wrapper, @NotNull ChatMessage data) {
      ChatMessage_v1_19 newData = (ChatMessage_v1_19)data;
      wrapper.writeComponent(newData.getChatContent());
      wrapper.writeOptional(newData.getUnsignedChatContent(), PacketWrapper::writeComponent);
      wrapper.writeMappedEntity(newData.getType());
      wrapper.writeUUID(newData.getSenderUUID());
      wrapper.writeComponent(newData.getSenderDisplayName());
      wrapper.writeOptional(newData.getTeamName(), PacketWrapper::writeComponent);
      wrapper.writeTimestamp(newData.getTimestamp());
      wrapper.writeLong(newData.getSalt());
      wrapper.writeByteArray(newData.getSignature());
   }
}
