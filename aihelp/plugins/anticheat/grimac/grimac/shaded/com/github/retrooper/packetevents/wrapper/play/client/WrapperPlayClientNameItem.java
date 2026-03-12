package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientNameItem extends PacketWrapper<WrapperPlayClientNameItem> {
   private String itemName;

   public WrapperPlayClientNameItem(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientNameItem(String itemName) {
      super((PacketTypeCommon)PacketType.Play.Client.NAME_ITEM);
      this.itemName = itemName;
   }

   public void read() {
      this.itemName = this.readString();
   }

   public void write() {
      this.writeString(this.itemName);
   }

   public void copy(WrapperPlayClientNameItem wrapper) {
      this.itemName = wrapper.itemName;
   }

   public String getItemName() {
      return this.itemName;
   }

   public void setItemName(String itemName) {
      this.itemName = itemName;
   }
}
