package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientQueryEntityNBT extends PacketWrapper<WrapperPlayClientQueryEntityNBT> {
   private int transactionID;
   private int entityID;

   public WrapperPlayClientQueryEntityNBT(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientQueryEntityNBT(int transactionID, int entityID) {
      super((PacketTypeCommon)PacketType.Play.Client.QUERY_ENTITY_NBT);
      this.transactionID = transactionID;
      this.entityID = entityID;
   }

   public void read() {
      this.transactionID = this.readVarInt();
      this.entityID = this.readVarInt();
   }

   public void write() {
      this.writeVarInt(this.transactionID);
      this.writeVarInt(this.entityID);
   }

   public void copy(WrapperPlayClientQueryEntityNBT wrapper) {
      this.transactionID = wrapper.transactionID;
      this.entityID = wrapper.entityID;
   }

   public int getTransactionId() {
      return this.transactionID;
   }

   public void setTransactionId(int transactionID) {
      this.transactionID = transactionID;
   }

   public int getEntityId() {
      return this.entityID;
   }

   public void setEntityId(int entityID) {
      this.entityID = entityID;
   }
}
