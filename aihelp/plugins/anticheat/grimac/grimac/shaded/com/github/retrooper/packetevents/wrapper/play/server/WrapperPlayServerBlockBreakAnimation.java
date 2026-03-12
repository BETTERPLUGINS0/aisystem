package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerBlockBreakAnimation extends PacketWrapper<WrapperPlayServerBlockBreakAnimation> {
   private int entityID;
   private Vector3i blockPosition;
   private byte destroyStage;

   public WrapperPlayServerBlockBreakAnimation(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerBlockBreakAnimation(int entityID, Vector3i blockPosition, byte destroyStage) {
      super((PacketTypeCommon)PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
      this.entityID = entityID;
      this.blockPosition = blockPosition;
      this.destroyStage = destroyStage;
   }

   public void read() {
      this.entityID = this.readVarInt();
      if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
         int x = this.readInt();
         int y = this.readInt();
         int z = this.readInt();
         this.blockPosition = new Vector3i(x, y, z);
      } else {
         this.blockPosition = this.readBlockPosition();
      }

      this.destroyStage = (byte)this.readUnsignedByte();
   }

   public void write() {
      this.writeVarInt(this.entityID);
      if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
         this.writeInt(this.blockPosition.x);
         this.writeInt(this.blockPosition.y);
         this.writeInt(this.blockPosition.z);
      } else {
         this.writeBlockPosition(this.blockPosition);
      }

      this.writeByte(this.destroyStage);
   }

   public void copy(WrapperPlayServerBlockBreakAnimation wrapper) {
      this.entityID = wrapper.entityID;
      this.blockPosition = wrapper.blockPosition;
      this.destroyStage = wrapper.destroyStage;
   }

   public int getEntityId() {
      return this.entityID;
   }

   public void setEntityId(int entityID) {
      this.entityID = entityID;
   }

   public Vector3i getBlockPosition() {
      return this.blockPosition;
   }

   public void setBlockPosition(Vector3i blockPosition) {
      this.blockPosition = blockPosition;
   }

   public byte getDestroyStage() {
      return this.destroyStage;
   }

   public void setDestroyStage(byte destroyStage) {
      this.destroyStage = destroyStage;
   }
}
