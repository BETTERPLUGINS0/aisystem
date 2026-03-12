package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class WrapperPlayServerWindowItems extends PacketWrapper<WrapperPlayServerWindowItems> {
   private int windowID;
   private int stateID;
   private List<ItemStack> items;
   private Optional<ItemStack> carriedItem;

   public WrapperPlayServerWindowItems(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerWindowItems(int windowID, int stateID, List<ItemStack> items, @Nullable ItemStack carriedItem) {
      super((PacketTypeCommon)PacketType.Play.Server.WINDOW_ITEMS);
      this.windowID = windowID;
      this.stateID = stateID;
      this.items = items;
      this.carriedItem = Optional.ofNullable(carriedItem);
   }

   public void read() {
      this.windowID = this.readContainerId();
      boolean v1_17_1 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17_1);
      if (v1_17_1) {
         this.stateID = this.readVarInt();
      }

      int count = v1_17_1 ? this.readVarInt() : this.readShort();
      this.items = new ArrayList(count);

      for(int i = 0; i < count; ++i) {
         this.items.add(this.readItemStack());
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17_1)) {
         this.carriedItem = Optional.of(this.readItemStack());
      } else {
         this.carriedItem = Optional.empty();
      }

   }

   public void write() {
      this.writeContainerId(this.windowID);
      boolean v1_17_1 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17_1);
      if (v1_17_1) {
         this.writeVarInt(this.stateID);
      }

      if (v1_17_1) {
         this.writeVarInt(this.items.size());
      } else {
         this.writeShort(this.items.size());
      }

      Iterator var2 = this.items.iterator();

      while(var2.hasNext()) {
         ItemStack item = (ItemStack)var2.next();
         this.writeItemStack(item);
      }

      if (v1_17_1) {
         this.writeItemStack((ItemStack)this.carriedItem.orElse(ItemStack.EMPTY));
      }

   }

   public void copy(WrapperPlayServerWindowItems wrapper) {
      this.windowID = wrapper.windowID;
      this.stateID = wrapper.stateID;
      this.items = wrapper.items;
      this.carriedItem = wrapper.carriedItem;
   }

   public int getWindowId() {
      return this.windowID;
   }

   public void setWindowId(int windowID) {
      this.windowID = windowID;
   }

   public int getStateId() {
      return this.stateID;
   }

   public void setStateId(int stateID) {
      this.stateID = stateID;
   }

   public List<ItemStack> getItems() {
      return this.items;
   }

   public void setItems(List<ItemStack> items) {
      this.items = items;
   }

   public Optional<ItemStack> getCarriedItem() {
      return this.carriedItem;
   }

   public void setCarriedItem(@Nullable ItemStack carriedItem) {
      this.carriedItem = Optional.ofNullable(carriedItem);
   }
}
