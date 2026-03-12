package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.login.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;

public class WrapperLoginServerDisconnect extends PacketWrapper<WrapperLoginServerDisconnect> {
   private Component reason;

   public WrapperLoginServerDisconnect(PacketSendEvent event) {
      super(event);
   }

   public WrapperLoginServerDisconnect(Component reason) {
      super((PacketTypeCommon)PacketType.Login.Server.DISCONNECT);
      this.reason = reason;
   }

   public void read() {
      this.reason = this.readComponentAsJSON();
   }

   public void write() {
      this.writeComponentAsJSON(this.reason);
   }

   public void copy(WrapperLoginServerDisconnect wrapper) {
      this.reason = wrapper.reason;
   }

   public Component getReason() {
      return this.reason;
   }

   public void setReason(Component reason) {
      this.reason = reason;
   }
}
