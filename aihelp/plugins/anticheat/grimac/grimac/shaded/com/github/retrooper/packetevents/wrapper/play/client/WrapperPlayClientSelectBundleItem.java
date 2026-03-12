package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientSelectBundleItem extends PacketWrapper<WrapperPlayClientSelectBundleItem> {
   private int slotId;
   private int selectedItemIndex;

   public WrapperPlayClientSelectBundleItem(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientSelectBundleItem(int slotId, int selectedItemIndex) {
      super((PacketTypeCommon)PacketType.Play.Client.SELECT_BUNDLE_ITEM);
      this.slotId = slotId;
      this.selectedItemIndex = selectedItemIndex;
   }

   public void read() {
      this.slotId = this.readVarInt();
      this.selectedItemIndex = this.readVarInt();
      if (this.selectedItemIndex < 0 && this.selectedItemIndex != -1) {
         throw new IllegalArgumentException("Invalid selectedItemIndex: " + this.selectedItemIndex);
      }
   }

   public void write() {
      this.writeVarInt(this.slotId);
      this.writeVarInt(this.selectedItemIndex);
   }

   public void copy(WrapperPlayClientSelectBundleItem wrapper) {
      this.slotId = wrapper.slotId;
      this.selectedItemIndex = wrapper.selectedItemIndex;
   }

   public int getSlotId() {
      return this.slotId;
   }

   public void setSlotId(int slotId) {
      this.slotId = slotId;
   }

   public int getSelectedItemIndex() {
      return this.selectedItemIndex;
   }

   public void setSelectedItemIndex(int selectedItemIndex) {
      this.selectedItemIndex = selectedItemIndex;
   }
}
