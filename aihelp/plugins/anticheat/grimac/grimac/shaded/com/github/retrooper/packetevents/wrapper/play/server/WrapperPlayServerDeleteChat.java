package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerDeleteChat extends PacketWrapper<WrapperPlayServerDeleteChat> {
   private byte[] signature;

   public WrapperPlayServerDeleteChat(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerDeleteChat(byte[] signature) {
      super((PacketTypeCommon)PacketType.Play.Server.DELETE_CHAT);
      this.signature = signature;
   }

   public void read() {
      this.signature = this.readByteArray();
   }

   public void write() {
      this.writeByteArray(this.signature);
   }

   public void copy(WrapperPlayServerDeleteChat wrapper) {
      this.signature = wrapper.signature;
   }

   public byte[] getSignature() {
      return this.signature;
   }

   public void setSignature(byte[] signature) {
      this.signature = signature;
   }
}
