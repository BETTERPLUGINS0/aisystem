package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerBlockChange extends PacketWrapper<WrapperPlayServerBlockChange> {
   private Vector3i blockPosition;
   private int blockID;

   public WrapperPlayServerBlockChange(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerBlockChange(Vector3i blockPosition, WrappedBlockState state) {
      this(blockPosition, state.getGlobalId());
   }

   public WrapperPlayServerBlockChange(Vector3i blockPosition, int blockID) {
      super((PacketTypeCommon)PacketType.Play.Server.BLOCK_CHANGE);
      this.blockPosition = blockPosition;
      this.blockID = blockID;
   }

   public void read() {
      if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
         this.blockPosition = new Vector3i(this.readInt(), this.readUnsignedByte(), this.readInt());
         int block = this.readVarInt();
         int blockData = this.readUnsignedByte();
         this.blockID = block | blockData << 12;
      } else {
         this.blockPosition = this.readBlockPosition();
         this.blockID = this.readVarInt();
      }

   }

   public void write() {
      if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
         this.writeInt(this.blockPosition.getX());
         this.writeByte(this.blockPosition.getY());
         this.writeInt(this.blockPosition.getZ());
         this.writeVarInt(this.blockID & 255);
         this.writeByte(this.blockID >> 12);
      } else {
         this.writeBlockPosition(this.blockPosition);
         this.writeVarInt(this.blockID);
      }

   }

   public void copy(WrapperPlayServerBlockChange wrapper) {
      this.blockPosition = wrapper.blockPosition;
      this.blockID = wrapper.blockID;
   }

   public Vector3i getBlockPosition() {
      return this.blockPosition;
   }

   public void setBlockPosition(Vector3i blockPosition) {
      this.blockPosition = blockPosition;
   }

   public int getBlockId() {
      return this.blockID;
   }

   public void setBlockID(int blockID) {
      this.blockID = blockID;
   }

   public WrappedBlockState getBlockState() {
      return WrappedBlockState.getByGlobalId(this.serverVersion.toClientVersion(), this.blockID);
   }

   public void setBlockState(WrappedBlockState blockState) {
      this.blockID = blockState.getGlobalId();
   }
}
