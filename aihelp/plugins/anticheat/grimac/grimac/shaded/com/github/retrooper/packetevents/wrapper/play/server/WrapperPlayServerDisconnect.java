package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;

public class WrapperPlayServerDisconnect extends PacketWrapper<WrapperPlayServerDisconnect> {
   private Component reason;

   public WrapperPlayServerDisconnect(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerDisconnect(Component reason) {
      super((PacketTypeCommon)PacketType.Play.Server.DISCONNECT);
      this.reason = reason;
   }

   public void read() {
      this.reason = this.readComponent();
   }

   public void write() {
      this.writeComponent(this.reason);
   }

   public void copy(WrapperPlayServerDisconnect wrapper) {
      this.reason = wrapper.reason;
   }

   public Component getReason() {
      return this.reason;
   }

   public void setReason(Component reason) {
      this.reason = reason;
   }
}
