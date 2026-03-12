package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.ChatCompletionAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.List;

public class WrapperPlayServerCustomChatCompletions extends PacketWrapper<WrapperPlayServerCustomChatCompletions> {
   private ChatCompletionAction action;
   private List<String> entries;

   public WrapperPlayServerCustomChatCompletions(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerCustomChatCompletions(ChatCompletionAction action, List<String> entries) {
      super((PacketTypeCommon)PacketType.Play.Server.CUSTOM_CHAT_COMPLETIONS);
      this.action = action;
      this.entries = entries;
   }

   public void read() {
      this.action = ChatCompletionAction.fromId(this.readVarInt());
      this.entries = this.readList(PacketWrapper::readString);
   }

   public void write() {
      this.writeVarInt(this.action.ordinal());
      this.writeList(this.entries, PacketWrapper::writeString);
   }

   public void copy(WrapperPlayServerCustomChatCompletions wrapper) {
      this.action = wrapper.action;
      this.entries = wrapper.entries;
   }

   public ChatCompletionAction getAction() {
      return this.action;
   }

   public void setAction(ChatCompletionAction action) {
      this.action = action;
   }

   public List<String> getEntries() {
      return this.entries;
   }

   public void setEntries(List<String> entries) {
      this.entries = entries;
   }
}
