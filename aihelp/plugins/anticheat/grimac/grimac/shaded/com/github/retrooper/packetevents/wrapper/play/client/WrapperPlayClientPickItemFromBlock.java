package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientPickItemFromBlock extends PacketWrapper<WrapperPlayClientPickItemFromBlock> {
   private Vector3i blockPos;
   private boolean includeData;

   public WrapperPlayClientPickItemFromBlock(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientPickItemFromBlock(Vector3i blockPos, boolean includeData) {
      super((PacketTypeCommon)PacketType.Play.Client.PICK_ITEM_FROM_BLOCK);
      this.blockPos = blockPos;
      this.includeData = includeData;
   }

   public void read() {
      this.blockPos = this.readBlockPosition();
      this.includeData = this.readBoolean();
   }

   public void write() {
      this.writeBlockPosition(this.blockPos);
      this.writeBoolean(this.includeData);
   }

   public void copy(WrapperPlayClientPickItemFromBlock wrapper) {
      this.blockPos = wrapper.blockPos;
      this.includeData = wrapper.includeData;
   }

   public Vector3i getBlockPos() {
      return this.blockPos;
   }

   public void setBlockPos(Vector3i blockPos) {
      this.blockPos = blockPos;
   }

   public boolean isIncludeData() {
      return this.includeData;
   }

   public void setIncludeData(boolean includeData) {
      this.includeData = includeData;
   }
}
