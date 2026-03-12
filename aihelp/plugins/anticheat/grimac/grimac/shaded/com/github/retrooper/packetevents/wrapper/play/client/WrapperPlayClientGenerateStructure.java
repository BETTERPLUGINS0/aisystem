package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientGenerateStructure extends PacketWrapper<WrapperPlayClientGenerateStructure> {
   private Vector3i blockPosition;
   private int levels;
   private boolean keepJigsaws;

   public WrapperPlayClientGenerateStructure(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientGenerateStructure(Vector3i blockPosition, int levels, boolean keepJigsaws) {
      super((PacketTypeCommon)PacketType.Play.Client.GENERATE_STRUCTURE);
      this.blockPosition = blockPosition;
      this.levels = levels;
      this.keepJigsaws = keepJigsaws;
   }

   public void read() {
      this.blockPosition = new Vector3i(this.readLong(), this.serverVersion);
      this.levels = this.readVarInt();
      this.keepJigsaws = this.readBoolean();
   }

   public void write() {
      this.writeLong(this.blockPosition.getSerializedPosition(this.serverVersion));
      this.writeVarInt(this.levels);
      this.writeBoolean(this.keepJigsaws);
   }

   public void copy(WrapperPlayClientGenerateStructure wrapper) {
      this.blockPosition = wrapper.blockPosition;
      this.levels = wrapper.levels;
      this.keepJigsaws = wrapper.keepJigsaws;
   }

   public Vector3i getBlockPosition() {
      return this.blockPosition;
   }

   public void setBlockPosition(Vector3i blockPosition) {
      this.blockPosition = blockPosition;
   }

   public int getLevels() {
      return this.levels;
   }

   public void setLevels(int levels) {
      this.levels = levels;
   }

   public boolean isKeepingJigsaws() {
      return this.keepJigsaws;
   }

   public void setKeepJigsaws(boolean keepJigsaws) {
      this.keepJigsaws = keepJigsaws;
   }
}
