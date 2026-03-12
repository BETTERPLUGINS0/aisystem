package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerBlockAction extends PacketWrapper<WrapperPlayServerBlockAction> {
   private Vector3i blockPosition;
   private int actionID;
   private int actionData;
   private int blockTypeID;

   public WrapperPlayServerBlockAction(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerBlockAction(Vector3i blockPosition, int actionID, int actionParam, int blockTypeID) {
      super((PacketTypeCommon)PacketType.Play.Server.BLOCK_ACTION);
      this.blockPosition = blockPosition;
      this.actionID = actionID;
      this.actionData = actionParam;
      this.blockTypeID = blockTypeID;
   }

   public void read() {
      if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
         int x = this.readInt();
         int y = this.readShort();
         int z = this.readInt();
         this.blockPosition = new Vector3i(x, y, z);
      } else {
         this.blockPosition = this.readBlockPosition();
      }

      this.actionID = this.readUnsignedByte();
      this.actionData = this.readUnsignedByte();
      this.blockTypeID = this.readVarInt();
   }

   public void write() {
      if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
         this.writeInt(this.blockPosition.x);
         this.writeShort(this.blockPosition.y);
         this.writeInt(this.blockPosition.z);
      } else {
         this.writeBlockPosition(this.blockPosition);
      }

      this.writeByte(this.actionID);
      this.writeByte(this.actionData);
      this.writeVarInt(this.blockTypeID);
   }

   public void copy(WrapperPlayServerBlockAction wrapper) {
      this.blockPosition = wrapper.blockPosition;
      this.actionID = wrapper.actionID;
      this.actionData = wrapper.actionData;
      this.blockTypeID = wrapper.blockTypeID;
   }

   public Vector3i getBlockPosition() {
      return this.blockPosition;
   }

   public void setBlockPosition(Vector3i blockPosition) {
      this.blockPosition = blockPosition;
   }

   public int getActionId() {
      return this.actionID;
   }

   public void setActionId(int actionID) {
      this.actionID = actionID;
   }

   public int getActionData() {
      return this.actionData;
   }

   public void setActionData(int actionData) {
      this.actionData = actionData;
   }

   public int getBlockTypeId() {
      return this.blockTypeID;
   }

   public void setBlockTypeId(int blockTypeID) {
      this.blockTypeID = blockTypeID;
   }

   public WrappedBlockState getBlockType() {
      return WrappedBlockState.getByGlobalId(this.serverVersion.toClientVersion(), this.blockTypeID);
   }

   public void setBlockType(WrappedBlockState blockType) {
      this.blockTypeID = blockType.getGlobalId();
   }
}
