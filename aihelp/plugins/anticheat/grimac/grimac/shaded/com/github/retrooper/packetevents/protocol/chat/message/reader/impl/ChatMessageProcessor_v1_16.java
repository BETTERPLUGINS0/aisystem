package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.reader.impl;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.ChatType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.ChatMessage;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.ChatMessage_v1_16;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.reader.ChatMessageProcessor;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.UUID;

public class ChatMessageProcessor_v1_16 implements ChatMessageProcessor {
   public ChatMessage readChatMessage(@NotNull PacketWrapper<?> wrapper) {
      Component chatContent = wrapper.readComponent();
      ChatType type = (ChatType)wrapper.readMappedEntity((IRegistry)ChatTypes.getRegistry());
      UUID senderUUID = wrapper.readUUID();
      return new ChatMessage_v1_16(chatContent, type, senderUUID);
   }

   public void writeChatMessage(@NotNull PacketWrapper<?> wrapper, @NotNull ChatMessage data) {
      wrapper.writeComponent(data.getChatContent());
      wrapper.writeMappedEntity(data.getType());
      wrapper.writeUUID(((ChatMessage_v1_16)data).getSenderUUID());
   }
}
