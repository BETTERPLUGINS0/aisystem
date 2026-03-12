package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientQueryBlockNBT extends PacketWrapper<WrapperPlayClientQueryBlockNBT> {
   private int transactionID;
   private Vector3i blockPosition;

   public WrapperPlayClientQueryBlockNBT(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientQueryBlockNBT(int transactionID, Vector3i blockPosition) {
      super((PacketTypeCommon)PacketType.Play.Client.QUERY_BLOCK_NBT);
      this.transactionID = transactionID;
      this.blockPosition = blockPosition;
   }

   public void read() {
      this.transactionID = this.readVarInt();
      this.blockPosition = this.readBlockPosition();
   }

   public void write() {
      this.writeVarInt(this.transactionID);
      this.writeBlockPosition(this.blockPosition);
   }

   public void copy(WrapperPlayClientQueryBlockNBT wrapper) {
      this.transactionID = wrapper.transactionID;
      this.blockPosition = wrapper.blockPosition;
   }

   public int getTransactionId() {
      return this.transactionID;
   }

   public void setTransactionId(int transactionID) {
      this.transactionID = transactionID;
   }

   public Vector3i getBlockPosition() {
      return this.blockPosition;
   }

   public void setBlockPosition(Vector3i blockPosition) {
      this.blockPosition = blockPosition;
   }
}
