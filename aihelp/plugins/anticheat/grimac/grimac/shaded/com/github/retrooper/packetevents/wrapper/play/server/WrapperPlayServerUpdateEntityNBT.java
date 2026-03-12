package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerUpdateEntityNBT extends PacketWrapper<WrapperPlayServerUpdateEntityNBT> {
   private int entityId;
   private NBTCompound nbtCompound;

   public WrapperPlayServerUpdateEntityNBT(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerUpdateEntityNBT(int entityId, NBTCompound nbtCompound) {
      super((PacketTypeCommon)PacketType.Play.Server.UPDATE_ENTITY_NBT);
      this.entityId = entityId;
      this.nbtCompound = nbtCompound;
   }

   public void read() {
      this.entityId = this.readVarInt();
      this.nbtCompound = this.readNBT();
   }

   public void write() {
      this.writeVarInt(this.entityId);
      this.writeNBT(this.nbtCompound);
   }

   public void copy(WrapperPlayServerUpdateEntityNBT wrapper) {
      this.entityId = wrapper.entityId;
      this.nbtCompound = wrapper.nbtCompound;
   }

   public int getEntityId() {
      return this.entityId;
   }

   public void setEntityId(int entityId) {
      this.entityId = entityId;
   }

   public NBTCompound getNBTCompound() {
      return this.nbtCompound;
   }

   public void setNBTCompound(NBTCompound nbtCompound) {
      this.nbtCompound = nbtCompound;
   }
}
