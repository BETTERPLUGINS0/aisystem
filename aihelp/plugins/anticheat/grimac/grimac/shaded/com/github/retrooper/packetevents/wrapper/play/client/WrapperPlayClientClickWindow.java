package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.HashedStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.jetbrains.annotations.UnknownNullability;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

public class WrapperPlayClientClickWindow extends PacketWrapper<WrapperPlayClientClickWindow> {
   private static final int MAX_SLOT_COUNT = 128;
   private int windowID;
   @Nullable
   private Integer stateID;
   private int slot;
   private int button;
   @Nullable
   private Integer actionNumber;
   private WrapperPlayClientClickWindow.WindowClickType windowClickType;
   @Nullable
   private Map<Integer, ItemStack> slots;
   @Nullable
   private Map<Integer, Optional<HashedStack>> hashedSlots;
   @Nullable
   private ItemStack carriedItemStack;
   @Nullable
   private HashedStack carriedHashedStack;

   public WrapperPlayClientClickWindow(PacketReceiveEvent event) {
      super(event);
   }

   @ApiStatus.Obsolete
   public WrapperPlayClientClickWindow(int windowID, Optional<Integer> stateID, int slot, int button, Optional<Integer> actionNumber, WrapperPlayClientClickWindow.WindowClickType windowClickType, Optional<Map<Integer, ItemStack>> slots, ItemStack carriedItemStack) {
      super((PacketTypeCommon)PacketType.Play.Client.CLICK_WINDOW);
      this.windowID = windowID;
      this.stateID = (Integer)stateID.orElse((Object)null);
      this.slot = slot;
      this.button = button;
      this.actionNumber = (Integer)actionNumber.orElse((Object)null);
      this.windowClickType = windowClickType;
      this.slots = (Map)slots.orElse((Object)null);
      this.carriedItemStack = carriedItemStack;
   }

   public WrapperPlayClientClickWindow(int windowID, @Nullable Integer stateID, int slot, int button, WrapperPlayClientClickWindow.WindowClickType windowClickType, @Nullable Map<Integer, Optional<HashedStack>> hashedSlots, @Nullable Optional<HashedStack> carriedHashedStack) {
      super((PacketTypeCommon)PacketType.Play.Client.CLICK_WINDOW);
      this.windowID = windowID;
      this.stateID = stateID;
      this.slot = slot;
      this.button = button;
      this.windowClickType = windowClickType;
      this.hashedSlots = hashedSlots;
      this.carriedHashedStack = carriedHashedStack != null ? (HashedStack)carriedHashedStack.orElse((Object)null) : null;
   }

   public void read() {
      this.windowID = this.readContainerId();
      this.stateID = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17_1) ? this.readVarInt() : null;
      this.slot = this.readShort();
      this.button = this.readByte();
      this.actionNumber = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17) ? null : Integer.valueOf(this.readShort());
      this.windowClickType = WrapperPlayClientClickWindow.WindowClickType.getById(this.readVarInt());
      this.readSlots();
   }

   protected void readSlots() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
         this.hashedSlots = this.readMap((ew) -> {
            return Math.toIntExact((long)ew.readShort());
         }, HashedStack::readOptional, 128);
         this.carriedHashedStack = HashedStack.read(this);
      } else {
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            this.slots = this.readMap((packetWrapper) -> {
               return Math.toIntExact((long)packetWrapper.readShort());
            }, PacketWrapper::readItemStack);
         }

         this.carriedItemStack = this.readItemStack();
      }

   }

   public void copy(WrapperPlayClientClickWindow wrapper) {
      this.windowID = wrapper.windowID;
      this.stateID = wrapper.stateID;
      this.slot = wrapper.slot;
      this.button = wrapper.button;
      this.actionNumber = wrapper.actionNumber;
      this.windowClickType = wrapper.windowClickType;
      this.slots = wrapper.slots;
      this.hashedSlots = wrapper.hashedSlots;
      this.carriedItemStack = wrapper.carriedItemStack;
      this.carriedHashedStack = wrapper.carriedHashedStack;
   }

   public void write() {
      boolean v1_17 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17);
      this.writeContainerId(this.windowID);
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17_1)) {
         this.writeVarInt(this.stateID != null ? this.stateID : -1);
      }

      this.writeShort(this.slot);
      this.writeByte(this.button);
      if (!v1_17) {
         this.writeShort(this.actionNumber != null ? this.actionNumber : -1);
      }

      this.writeVarInt(this.windowClickType.ordinal());
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
         this.writeMap(this.hashedSlots != null ? this.hashedSlots : Collections.emptyMap(), PacketWrapper::writeShort, HashedStack::writeOptional);
         HashedStack.write(this, this.carriedHashedStack);
      } else {
         if (v1_17) {
            this.writeMap(this.slots != null ? this.slots : Collections.emptyMap(), PacketWrapper::writeShort, PacketWrapper::writeItemStack);
         }

         this.writeItemStack(this.carriedItemStack);
      }

   }

   public int getWindowId() {
      return this.windowID;
   }

   public void setWindowId(int windowID) {
      this.windowID = windowID;
   }

   public Optional<Integer> getStateId() {
      return Optional.ofNullable(this.stateID);
   }

   public void setStateID(Optional<Integer> stateID) {
      this.stateID = (Integer)stateID.orElse((Object)null);
   }

   public int getSlot() {
      return this.slot;
   }

   public void setSlot(int slot) {
      this.slot = slot;
   }

   public int getButton() {
      return this.button;
   }

   public void setButton(int button) {
      this.button = button;
   }

   @ApiStatus.Obsolete
   public Optional<Integer> getActionNumber() {
      return Optional.ofNullable(this.actionNumber);
   }

   @ApiStatus.Obsolete
   public void setActionNumber(int button) {
      this.actionNumber = button;
   }

   public WrapperPlayClientClickWindow.WindowClickType getWindowClickType() {
      return this.windowClickType;
   }

   public void setWindowClickType(WrapperPlayClientClickWindow.WindowClickType windowClickType) {
      this.windowClickType = windowClickType;
   }

   @ApiStatus.Obsolete
   public Optional<Map<Integer, ItemStack>> getSlots() {
      if (this.slots != null) {
         return Optional.of(this.slots);
      } else if (this.hashedSlots != null) {
         Map<Integer, ItemStack> ret = new HashMap(this.hashedSlots.size());
         Iterator var2 = this.hashedSlots.entrySet().iterator();

         while(var2.hasNext()) {
            Entry<Integer, Optional<HashedStack>> entry = (Entry)var2.next();
            HashedStack stack = (HashedStack)((Optional)entry.getValue()).orElse((Object)null);
            ret.put((Integer)entry.getKey(), stack != null ? stack.asItemStack() : ItemStack.EMPTY);
         }

         return Optional.of(ret);
      } else {
         return Optional.empty();
      }
   }

   @ApiStatus.Obsolete
   public void setSlots(Map<Integer, ItemStack> slots) {
      this.setSlots(Optional.ofNullable(slots));
   }

   @ApiStatus.Obsolete
   public void setSlots(Optional<Map<Integer, ItemStack>> slots) {
      this.slots = (Map)slots.orElse((Object)null);
      if (this.slots != null) {
         this.hashedSlots = new HashMap(this.slots.size());
         Iterator var2 = this.slots.entrySet().iterator();

         while(var2.hasNext()) {
            Entry<Integer, ItemStack> entry = (Entry)var2.next();
            this.hashedSlots.put((Integer)entry.getKey(), HashedStack.toOptionalFromItemStack((ItemStack)entry.getValue()));
         }
      } else {
         this.hashedSlots = null;
      }

   }

   @UnknownNullability
   public Map<Integer, Optional<HashedStack>> getHashedSlots() {
      return this.hashedSlots;
   }

   public void setHashedSlots(Map<Integer, Optional<HashedStack>> hashedSlots) {
      this.hashedSlots = hashedSlots;
   }

   @ApiStatus.Obsolete
   public ItemStack getCarriedItemStack() {
      if (this.carriedItemStack != null) {
         return this.carriedItemStack;
      } else {
         return this.carriedHashedStack != null ? this.carriedHashedStack.asItemStack() : ItemStack.EMPTY;
      }
   }

   @ApiStatus.Obsolete
   public void setCarriedItemStack(ItemStack carriedItemStack) {
      this.carriedItemStack = carriedItemStack;
      this.carriedHashedStack = HashedStack.fromItemStack(carriedItemStack);
   }

   public Optional<HashedStack> getCarriedHashedStack() {
      return Optional.ofNullable(this.carriedHashedStack);
   }

   public void setCarriedHashedStack(@Nullable HashedStack carriedHashedStack) {
      this.carriedHashedStack = carriedHashedStack;
   }

   public void setCarriedHashedStack(Optional<HashedStack> carriedHashedStack) {
      this.carriedHashedStack = (HashedStack)carriedHashedStack.orElse((Object)null);
   }

   public static enum WindowClickType {
      PICKUP,
      QUICK_MOVE,
      SWAP,
      CLONE,
      THROW,
      QUICK_CRAFT,
      PICKUP_ALL,
      UNKNOWN;

      public static final WrapperPlayClientClickWindow.WindowClickType[] VALUES = values();

      public static WrapperPlayClientClickWindow.WindowClickType getById(int id) {
         return id >= 0 && id < VALUES.length - 1 ? VALUES[id] : UNKNOWN;
      }

      // $FF: synthetic method
      private static WrapperPlayClientClickWindow.WindowClickType[] $values() {
         return new WrapperPlayClientClickWindow.WindowClickType[]{PICKUP, QUICK_MOVE, SWAP, CLONE, THROW, QUICK_CRAFT, PICKUP_ALL, UNKNOWN};
      }
   }
}
