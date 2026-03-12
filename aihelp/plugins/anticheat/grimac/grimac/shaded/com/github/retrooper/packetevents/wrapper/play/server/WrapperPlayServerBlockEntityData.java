package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.TileEntityType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.blockentity.BlockEntityType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.blockentity.BlockEntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerBlockEntityData extends PacketWrapper<WrapperPlayServerBlockEntityData> {
   private Vector3i position;
   private BlockEntityType type;
   private NBTCompound nbt;

   public WrapperPlayServerBlockEntityData(PacketSendEvent event) {
      super(event);
   }

   /** @deprecated */
   @Deprecated
   public WrapperPlayServerBlockEntityData(Vector3i position, TileEntityType type, NBTCompound nbt) {
      this(position, type.getId(), nbt);
   }

   /** @deprecated */
   @Deprecated
   public WrapperPlayServerBlockEntityData(Vector3i position, int type, NBTCompound nbt) {
      this(position, BlockEntityTypes.getById(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), type), nbt);
   }

   public WrapperPlayServerBlockEntityData(Vector3i position, BlockEntityType type, NBTCompound nbt) {
      super((PacketTypeCommon)PacketType.Play.Server.BLOCK_ENTITY_DATA);
      this.position = position;
      this.type = type;
      this.nbt = nbt;
   }

   public void read() {
      this.position = this.readBlockPosition();
      int typeId = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18) ? this.readVarInt() : this.readUnsignedByte();
      this.type = BlockEntityTypes.getById(this.serverVersion.toClientVersion(), typeId);
      this.nbt = this.readNBT();
   }

   public void write() {
      this.writeBlockPosition(this.position);
      int typeId = this.type.getId(this.serverVersion.toClientVersion());
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18)) {
         this.writeVarInt(typeId);
      } else {
         this.writeByte(typeId);
      }

      this.writeNBT(this.nbt);
   }

   public void copy(WrapperPlayServerBlockEntityData wrapper) {
      this.position = wrapper.position;
      this.type = wrapper.type;
      this.nbt = wrapper.nbt;
   }

   public Vector3i getPosition() {
      return this.position;
   }

   public void setPosition(Vector3i position) {
      this.position = position;
   }

   /** @deprecated */
   @Deprecated
   public int getType() {
      return this.type.getId(this.serverVersion.toClientVersion());
   }

   public BlockEntityType getBlockEntityType() {
      return this.type;
   }

   /** @deprecated */
   @Deprecated
   public TileEntityType getAsTileType() {
      return TileEntityType.getById(this.getType());
   }

   /** @deprecated */
   @Deprecated
   public void setType(int type) {
      this.setType(BlockEntityTypes.getById(this.serverVersion.toClientVersion(), type));
   }

   public void setType(BlockEntityType blockEntityType) {
      this.type = blockEntityType;
   }

   /** @deprecated */
   @Deprecated
   public void setType(TileEntityType type) {
      this.setType(type.getId());
   }

   public NBTCompound getNBT() {
      return this.nbt;
   }

   public void setNBT(NBTCompound nbt) {
      this.nbt = nbt;
   }
}
