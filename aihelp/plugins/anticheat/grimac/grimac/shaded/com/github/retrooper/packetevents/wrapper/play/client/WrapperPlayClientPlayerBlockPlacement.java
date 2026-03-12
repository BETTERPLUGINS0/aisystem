package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.InteractionHand;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3f;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Optional;

public class WrapperPlayClientPlayerBlockPlacement extends PacketWrapper<WrapperPlayClientPlayerBlockPlacement> {
   private InteractionHand interactionHand;
   private Vector3i blockPosition;
   private int faceId;
   private BlockFace face;
   private Vector3f cursorPosition;
   private Optional<ItemStack> itemStack;
   private Optional<Boolean> insideBlock;
   private Optional<Boolean> worldBorderHit;
   private int sequence;

   public WrapperPlayClientPlayerBlockPlacement(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientPlayerBlockPlacement(InteractionHand interactionHand, Vector3i blockPosition, BlockFace face, Vector3f cursorPosition, ItemStack itemStack, Boolean insideBlock, int sequence) {
      this(interactionHand, blockPosition, face, cursorPosition, itemStack, insideBlock, (Boolean)null, sequence);
   }

   public WrapperPlayClientPlayerBlockPlacement(InteractionHand interactionHand, Vector3i blockPosition, BlockFace face, Vector3f cursorPosition, ItemStack itemStack, Boolean insideBlock, Boolean worldBorderHit, int sequence) {
      super((PacketTypeCommon)PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT);
      this.interactionHand = interactionHand;
      this.blockPosition = blockPosition;
      this.face = face;
      this.faceId = face.getFaceValue();
      this.cursorPosition = cursorPosition;
      this.itemStack = Optional.ofNullable(itemStack);
      this.insideBlock = Optional.ofNullable(insideBlock);
      this.worldBorderHit = Optional.ofNullable(worldBorderHit);
      this.sequence = sequence;
   }

   public void read() {
      this.itemStack = Optional.empty();
      this.insideBlock = Optional.empty();
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
         this.interactionHand = InteractionHand.getById(this.readVarInt());
         this.blockPosition = this.readBlockPosition();
         this.faceId = this.readVarInt();
         this.face = BlockFace.getBlockFaceByValue(this.faceId);
         this.cursorPosition = new Vector3f(this.readFloat(), this.readFloat(), this.readFloat());
         this.insideBlock = Optional.of(this.readBoolean());
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
               this.worldBorderHit = Optional.of(this.readBoolean());
            }

            this.sequence = this.readVarInt();
         }
      } else {
         if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
            this.blockPosition = new Vector3i(this.readInt(), this.readUnsignedByte(), this.readInt());
         } else {
            this.blockPosition = this.readBlockPosition();
         }

         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            this.faceId = this.readVarInt();
            this.face = BlockFace.getBlockFaceByValue(this.faceId);
            this.interactionHand = InteractionHand.getById(this.readVarInt());
         } else {
            this.faceId = this.readUnsignedByte();
            this.face = BlockFace.getLegacyBlockFaceByValue(this.faceId);
            this.itemStack = Optional.of(this.readItemStack());
            this.interactionHand = InteractionHand.MAIN_HAND;
         }

         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_11)) {
            this.cursorPosition = new Vector3f(this.readFloat(), this.readFloat(), this.readFloat());
         } else {
            this.cursorPosition = new Vector3f((float)this.readUnsignedByte() / 16.0F, (float)this.readUnsignedByte() / 16.0F, (float)this.readUnsignedByte() / 16.0F);
         }
      }

   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
         this.writeVarInt(this.interactionHand.getId());
         this.writeBlockPosition(this.blockPosition);
         this.writeVarInt(this.faceId);
         this.writeFloat(this.cursorPosition.x);
         this.writeFloat(this.cursorPosition.y);
         this.writeFloat(this.cursorPosition.z);
         this.writeBoolean((Boolean)this.insideBlock.orElse(false));
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
               this.writeBoolean((Boolean)this.worldBorderHit.orElse(false));
            }

            this.writeVarInt(this.sequence);
         }
      } else {
         if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
            this.writeInt(this.blockPosition.x);
            this.writeByte(this.blockPosition.y);
            this.writeInt(this.blockPosition.z);
         } else {
            this.writeBlockPosition(this.blockPosition);
         }

         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            this.writeVarInt(this.faceId);
            this.writeVarInt(this.interactionHand.getId());
         } else {
            this.writeByte(this.faceId);
            this.writeItemStack((ItemStack)this.itemStack.orElse(ItemStack.EMPTY));
         }

         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_11)) {
            this.writeFloat(this.cursorPosition.x);
            this.writeFloat(this.cursorPosition.y);
            this.writeFloat(this.cursorPosition.z);
         } else {
            this.writeByte((int)(this.cursorPosition.x * 16.0F));
            this.writeByte((int)(this.cursorPosition.y * 16.0F));
            this.writeByte((int)(this.cursorPosition.z * 16.0F));
         }
      }

   }

   public void copy(WrapperPlayClientPlayerBlockPlacement wrapper) {
      this.interactionHand = wrapper.interactionHand;
      this.blockPosition = wrapper.blockPosition;
      this.face = wrapper.face;
      this.faceId = wrapper.faceId;
      this.cursorPosition = wrapper.cursorPosition;
      this.itemStack = wrapper.itemStack;
      this.insideBlock = wrapper.insideBlock;
      this.worldBorderHit = wrapper.worldBorderHit;
      this.sequence = wrapper.sequence;
   }

   public InteractionHand getHand() {
      return this.interactionHand;
   }

   public void setHand(InteractionHand interactionHand) {
      this.interactionHand = interactionHand;
   }

   public Vector3i getBlockPosition() {
      return this.blockPosition;
   }

   public void setBlockPosition(Vector3i blockPosition) {
      this.blockPosition = blockPosition;
   }

   public int getFaceId() {
      return this.faceId;
   }

   public void setFaceId(int faceId) {
      this.faceId = faceId;
      this.face = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9) ? BlockFace.getBlockFaceByValue(faceId) : BlockFace.getLegacyBlockFaceByValue(faceId);
   }

   public BlockFace getFace() {
      return this.face;
   }

   public void setFace(BlockFace face) {
      this.face = face;
      this.faceId = face.getFaceValue();
   }

   public Vector3f getCursorPosition() {
      return this.cursorPosition;
   }

   public void setCursorPosition(Vector3f cursorPosition) {
      this.cursorPosition = cursorPosition;
   }

   public Optional<ItemStack> getItemStack() {
      return this.itemStack;
   }

   public void setItemStack(Optional<ItemStack> itemStack) {
      this.itemStack = itemStack;
   }

   public Optional<Boolean> getInsideBlock() {
      return this.insideBlock != null ? this.insideBlock : Optional.empty();
   }

   public void setInsideBlock(Optional<Boolean> insideBlock) {
      this.insideBlock = insideBlock;
   }

   public Optional<Boolean> getWorldBorderHit() {
      return this.worldBorderHit != null ? this.worldBorderHit : Optional.empty();
   }

   public void setWorldBorderHit(Optional<Boolean> worldBorderHit) {
      this.worldBorderHit = worldBorderHit;
   }

   public int getSequence() {
      return this.sequence;
   }

   public void setSequence(int sequence) {
      this.sequence = sequence;
   }
}
