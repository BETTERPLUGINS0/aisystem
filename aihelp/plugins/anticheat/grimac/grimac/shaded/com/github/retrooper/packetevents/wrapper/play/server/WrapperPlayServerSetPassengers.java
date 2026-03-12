package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerSetPassengers extends PacketWrapper<WrapperPlayServerSetPassengers> {
   private int entityId;
   private int[] passengers;

   public WrapperPlayServerSetPassengers(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerSetPassengers(int entityId, int[] passengers) {
      super((PacketTypeCommon)PacketType.Play.Server.SET_PASSENGERS);
      this.entityId = entityId;
      this.passengers = passengers;
   }

   public void read() {
      this.entityId = this.readVarInt();
      this.passengers = this.readVarIntArray();
   }

   public void write() {
      this.writeVarInt(this.entityId);
      this.writeVarIntArray(this.passengers);
   }

   public void copy(WrapperPlayServerSetPassengers wrapper) {
      this.entityId = wrapper.entityId;
      this.passengers = wrapper.passengers;
   }

   public int getEntityId() {
      return this.entityId;
   }

   public void setEntityId(int entityId) {
      this.entityId = entityId;
   }

   public int[] getPassengers() {
      return this.passengers;
   }

   public void setPassengers(int[] passengers) {
      this.passengers = passengers;
   }
}
