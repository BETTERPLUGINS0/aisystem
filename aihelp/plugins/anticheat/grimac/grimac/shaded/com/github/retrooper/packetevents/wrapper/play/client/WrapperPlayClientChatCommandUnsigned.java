package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientChatCommandUnsigned extends PacketWrapper<WrapperPlayClientChatCommandUnsigned> {
   private String command;

   public WrapperPlayClientChatCommandUnsigned(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientChatCommandUnsigned(String command) {
      super((PacketTypeCommon)PacketType.Play.Client.CHAT_COMMAND_UNSIGNED);
      this.command = command;
   }

   public void read() {
      this.command = this.readString(256);
   }

   public void write() {
      this.writeString(this.command, 256);
   }

   public void copy(WrapperPlayClientChatCommandUnsigned wrapper) {
      this.command = wrapper.command;
   }

   public String getCommand() {
      return this.command;
   }

   public void setCommand(String command) {
      this.command = command;
   }
}
