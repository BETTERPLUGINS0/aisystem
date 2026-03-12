package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientTeleportConfirm extends PacketWrapper<WrapperPlayClientTeleportConfirm> {
   private int teleportID;

   public WrapperPlayClientTeleportConfirm(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientTeleportConfirm(int teleportID) {
      super((PacketTypeCommon)PacketType.Play.Client.TELEPORT_CONFIRM);
      this.teleportID = teleportID;
   }

   public void read() {
      this.teleportID = this.readVarInt();
   }

   public void write() {
      this.writeVarInt(this.teleportID);
   }

   public void copy(WrapperPlayClientTeleportConfirm wrapper) {
      this.teleportID = wrapper.teleportID;
   }

   public int getTeleportId() {
      return this.teleportID;
   }

   public void setTeleportId(int teleportID) {
      this.teleportID = teleportID;
   }
}
