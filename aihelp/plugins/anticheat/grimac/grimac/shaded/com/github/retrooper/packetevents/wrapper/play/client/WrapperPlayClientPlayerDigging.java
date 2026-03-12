package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.DiggingAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientPlayerDigging extends PacketWrapper<WrapperPlayClientPlayerDigging> {
   private DiggingAction action;
   private Vector3i blockPosition;
   private BlockFace blockFace;
   private int blockFaceId;
   private int sequence;

   public WrapperPlayClientPlayerDigging(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientPlayerDigging(DiggingAction action, Vector3i blockPosition, BlockFace blockFace, int sequence) {
      super((PacketTypeCommon)PacketType.Play.Client.PLAYER_DIGGING);
      this.action = action;
      this.blockPosition = blockPosition;
      this.blockFace = blockFace;
      this.blockFaceId = blockFace.getFaceValue();
      this.sequence = sequence;
   }

   public WrapperPlayClientPlayerDigging(DiggingAction action, Vector3i blockPosition, int blockFace, int sequence) {
      super((PacketTypeCommon)PacketType.Play.Client.PLAYER_DIGGING);
      this.action = action;
      this.blockPosition = blockPosition;
      this.blockFace = BlockFace.getBlockFaceByValue(blockFace);
      this.blockFaceId = blockFace;
      this.sequence = sequence;
   }

   public void read() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
         this.action = DiggingAction.getById(this.readVarInt());
      } else {
         this.action = DiggingAction.getById(this.readByte());
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
         this.blockPosition = this.readBlockPosition();
      } else {
         int x = this.readInt();
         int y = this.readUnsignedByte();
         int z = this.readInt();
         this.blockPosition = new Vector3i(x, y, z);
      }

      this.blockFaceId = this.readUnsignedByte();
      this.blockFace = BlockFace.getBlockFaceByValue(this.blockFaceId);
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
         this.sequence = this.readVarInt();
      }

   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
         this.writeVarInt(this.action.getId());
         this.writeBlockPosition(this.blockPosition);
      } else {
         this.writeByte(this.action.getId());
         this.writeInt(this.blockPosition.x);
         this.writeByte(this.blockPosition.y);
         this.writeInt(this.blockPosition.z);
      }

      this.writeByte(this.blockFaceId);
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
         this.writeVarInt(this.sequence);
      }

   }

   public void copy(WrapperPlayClientPlayerDigging wrapper) {
      this.action = wrapper.action;
      this.blockPosition = wrapper.blockPosition;
      this.blockFace = wrapper.blockFace;
      this.blockFaceId = wrapper.blockFaceId;
      this.sequence = wrapper.sequence;
   }

   public DiggingAction getAction() {
      return this.action;
   }

   public void setAction(DiggingAction action) {
      this.action = action;
   }

   public Vector3i getBlockPosition() {
      return this.blockPosition;
   }

   public void setBlockPosition(Vector3i blockPosition) {
      this.blockPosition = blockPosition;
   }

   public BlockFace getBlockFace() {
      return this.blockFace;
   }

   public void setBlockFace(BlockFace blockFace) {
      this.blockFace = blockFace;
      this.blockFaceId = blockFace.getFaceValue();
   }

   public int getBlockFaceId() {
      return this.blockFaceId;
   }

   public void setBlockFaceId(int faceId) {
      this.blockFace = BlockFace.getBlockFaceByValue(faceId);
      this.blockFaceId = faceId;
   }

   public int getSequence() {
      return this.sequence;
   }

   public void setSequence(int sequence) {
      this.sequence = sequence;
   }
}
