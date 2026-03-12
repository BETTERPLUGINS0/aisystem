package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.ChatType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;

public class ChatMessage {
   private Component chatContent;
   private ChatType type;

   protected ChatMessage(Component chatContent, ChatType type) {
      this.chatContent = chatContent;
      this.type = type;
   }

   public Component getChatContent() {
      return this.chatContent;
   }

   public String getChatContentJson(ClientVersion version) {
      return AdventureSerializer.serializer(version).asJson(this.getChatContent());
   }

   public void setChatContent(Component chatContent) {
      this.chatContent = chatContent;
   }

   public void setChatContentJson(ClientVersion version, String json) {
      this.setChatContent(AdventureSerializer.serializer(version).fromJson(json));
   }

   public ChatType getType() {
      return this.type;
   }

   public void setType(ChatType type) {
      this.type = type;
   }
}
