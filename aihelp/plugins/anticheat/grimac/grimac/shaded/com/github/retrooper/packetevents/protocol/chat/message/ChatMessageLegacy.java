package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.ChatType;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;

public class ChatMessageLegacy extends ChatMessage {
   public ChatMessageLegacy(Component chatContent, ChatType type) {
      super(chatContent, type);
   }
}
