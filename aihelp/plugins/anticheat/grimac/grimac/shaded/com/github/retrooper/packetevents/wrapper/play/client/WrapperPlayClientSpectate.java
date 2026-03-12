package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.UUID;

public class WrapperPlayClientSpectate extends PacketWrapper<WrapperPlayClientSpectate> {
   private UUID targetUUID;

   public WrapperPlayClientSpectate(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientSpectate(UUID uuid) {
      super((PacketTypeCommon)PacketType.Play.Client.SPECTATE);
      this.targetUUID = uuid;
   }

   public void read() {
      this.targetUUID = this.readUUID();
   }

   public void write() {
      this.writeUUID(this.targetUUID);
   }

   public void copy(WrapperPlayClientSpectate wrapper) {
      this.targetUUID = wrapper.targetUUID;
   }

   public UUID getTargetUUID() {
      return this.targetUUID;
   }

   public void setTargetUUID(UUID uuid) {
      this.targetUUID = uuid;
   }
}
