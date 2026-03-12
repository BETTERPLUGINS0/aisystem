package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;

public class WrapperConfigServerDisconnect extends PacketWrapper<WrapperConfigServerDisconnect> {
   private Component reason;

   public WrapperConfigServerDisconnect(PacketSendEvent event) {
      super(event);
   }

   public WrapperConfigServerDisconnect(Component reason) {
      super((PacketTypeCommon)PacketType.Configuration.Server.DISCONNECT);
      this.reason = reason;
   }

   public void read() {
      this.reason = this.readComponent();
   }

   public void write() {
      this.writeComponent(this.reason);
   }

   public void copy(WrapperConfigServerDisconnect wrapper) {
      this.reason = wrapper.reason;
   }

   public Component getReason() {
      return this.reason;
   }

   public void setReason(Component reason) {
      this.reason = reason;
   }
}
