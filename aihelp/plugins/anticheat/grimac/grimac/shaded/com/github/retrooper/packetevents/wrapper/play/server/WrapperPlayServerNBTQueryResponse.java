package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerNBTQueryResponse extends PacketWrapper<WrapperPlayServerNBTQueryResponse> {
   private int transactionId;
   private NBTCompound tag;

   public WrapperPlayServerNBTQueryResponse(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerNBTQueryResponse(int transactionId, NBTCompound tag) {
      super((PacketTypeCommon)PacketType.Play.Server.NBT_QUERY_RESPONSE);
      this.transactionId = transactionId;
      this.tag = tag;
   }

   public void read() {
      this.transactionId = this.readVarInt();
      this.tag = this.readNBT();
   }

   public void write() {
      this.writeVarInt(this.transactionId);
      this.writeNBT(this.tag);
   }

   public void copy(WrapperPlayServerNBTQueryResponse wrapper) {
      this.transactionId = wrapper.transactionId;
      this.tag = wrapper.tag;
   }

   public int getTransactionId() {
      return this.transactionId;
   }

   public void setTransactionId(int transactionId) {
      this.transactionId = transactionId;
   }

   public NBTCompound getTag() {
      return this.tag;
   }

   public void setTag(NBTCompound tag) {
      this.tag = tag;
   }
}
