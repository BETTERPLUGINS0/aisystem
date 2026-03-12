package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.reader;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.ChatMessage;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

public interface ChatMessageProcessor {
   ChatMessage readChatMessage(@NotNull PacketWrapper<?> wrapper);

   void writeChatMessage(@NotNull PacketWrapper<?> wrapper, @NotNull ChatMessage data);
}
