package ac.grim.grimac.utils.inventory;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.utils.inventory.inventory.AbstractContainerMenu;
import ac.grim.grimac.utils.inventory.slot.EquipmentSlot;
import ac.grim.grimac.utils.inventory.slot.ResultSlot;
import ac.grim.grimac.utils.inventory.slot.Slot;
import ac.grim.grimac.utils.lists.CorrectingPlayerInventoryStorage;
import lombok.Generated;

public class Inventory extends AbstractContainerMenu {
   public static final int SLOT_OFFHAND = 45;
   public static final int HOTBAR_OFFSET = 36;
   public static final int ITEMS_START = 9;
   public static final int ITEMS_END = 45;
   public static final int SLOT_HELMET = 4;
   public static final int SLOT_CHESTPLATE = 5;
   public static final int SLOT_LEGGINGS = 6;
   public static final int SLOT_BOOTS = 7;
   private static final int TOTAL_SIZE = 46;
   public int selected = 0;
   private final CorrectingPlayerInventoryStorage inventoryStorage;

   public Inventory(GrimPlayer player, CorrectingPlayerInventoryStorage inventoryStorage) {
      super(player);
      this.setPlayerInventory(this);
      this.inventoryStorage = inventoryStorage;
      this.addSlot(new ResultSlot(inventoryStorage, 0));

      int i;
      for(i = 0; i < 4; ++i) {
         this.addSlot(new Slot(inventoryStorage, i));
      }

      for(i = 0; i < 4; ++i) {
         this.addSlot(new EquipmentSlot(EquipmentType.byArmorID(i), inventoryStorage, i + 4));
      }

      for(i = 0; i < 36; ++i) {
         this.addSlot(new Slot(inventoryStorage, i + 9));
      }

      this.addSlot(new Slot(inventoryStorage, 45));
   }

   public ItemStack getHelmet() {
      return this.inventoryStorage.getItem(4);
   }

   public ItemStack getChestplate() {
      return this.inventoryStorage.getItem(5);
   }

   public ItemStack getLeggings() {
      return this.inventoryStorage.getItem(6);
   }

   public ItemStack getBoots() {
      return this.inventoryStorage.getItem(7);
   }

   public ItemStack getOffhand() {
      return this.inventoryStorage.getItem(45);
   }

   public boolean hasItemType(ItemType item) {
      for(int i = 0; i < this.inventoryStorage.items.length; ++i) {
         if (this.inventoryStorage.getItem(i).getType() == item) {
            return true;
         }
      }

      return false;
   }

   public ItemStack getHeldItem() {
      return this.inventoryStorage.getItem(this.selected + 36);
   }

   public void setHeldItem(ItemStack item) {
      this.inventoryStorage.setItem(this.selected + 36, item);
   }

   public ItemStack getOffhandItem() {
      return this.inventoryStorage.getItem(45);
   }

   public boolean add(ItemStack p_36055_) {
      return this.add(-1, p_36055_);
   }

   public int getFreeSlot() {
      for(int i = 0; i < this.inventoryStorage.items.length; ++i) {
         if (this.inventoryStorage.getItem(i).isEmpty()) {
            return i;
         }
      }

      return -1;
   }

   public int getSlotWithRemainingSpace(ItemStack toAdd) {
      if (this.hasRemainingSpaceForItem(this.getHeldItem(), toAdd)) {
         return this.selected;
      } else if (this.hasRemainingSpaceForItem(this.getOffhandItem(), toAdd)) {
         return 40;
      } else {
         for(int i = 9; i <= 45; ++i) {
            if (this.hasRemainingSpaceForItem(this.inventoryStorage.getItem(i), toAdd)) {
               return i;
            }
         }

         return -1;
      }
   }

   private boolean hasRemainingSpaceForItem(ItemStack one, ItemStack two) {
      return !one.isEmpty() && ItemStack.isSameItemSameTags(one, two) && one.getAmount() < one.getMaxStackSize() && one.getAmount() < this.getMaxStackSize();
   }

   private int addResource(ItemStack resource) {
      int i = this.getSlotWithRemainingSpace(resource);
      if (i == -1) {
         i = this.getFreeSlot();
      }

      return i == -1 ? resource.getAmount() : this.addResource(i, resource);
   }

   private int addResource(int slot, ItemStack stack) {
      int i = stack.getAmount();
      ItemStack itemstack = this.inventoryStorage.getItem(slot);
      if (itemstack.isEmpty()) {
         itemstack = stack.copy();
         itemstack.setAmount(0);
         this.inventoryStorage.setItem(slot, itemstack);
      }

      int j = Math.min(i, itemstack.getMaxStackSize() - itemstack.getAmount());
      if (j > this.getMaxStackSize() - itemstack.getAmount()) {
         j = this.getMaxStackSize() - itemstack.getAmount();
      }

      if (j != 0) {
         i -= j;
         itemstack.grow(j);
      }

      return i;
   }

   public boolean add(int p_36041_, ItemStack p_36042_) {
      if (p_36042_.isEmpty()) {
         return false;
      } else if (p_36042_.isDamaged()) {
         if (p_36041_ == -1) {
            p_36041_ = this.getFreeSlot();
         }

         if (p_36041_ >= 0) {
            this.inventoryStorage.setItem(p_36041_, p_36042_.copy());
            p_36042_.setAmount(0);
            return true;
         } else if (this.player.gamemode == GameMode.CREATIVE) {
            p_36042_.setAmount(0);
            return true;
         } else {
            return false;
         }
      } else {
         int i;
         do {
            i = p_36042_.getAmount();
            if (p_36041_ == -1) {
               p_36042_.setAmount(this.addResource(p_36042_));
            } else {
               p_36042_.setAmount(this.addResource(p_36041_, p_36042_));
            }
         } while(!p_36042_.isEmpty() && p_36042_.getAmount() < i);

         if (p_36042_.getAmount() == i && this.player.gamemode == GameMode.CREATIVE) {
            p_36042_.setAmount(0);
            return true;
         } else {
            return p_36042_.getAmount() < i;
         }
      }
   }

   public ItemStack quickMoveStack(int slotID) {
      ItemStack original = ItemStack.EMPTY;
      Slot slot = (Slot)this.getSlots().get(slotID);
      if (slot != null && slot.hasItem()) {
         ItemStack toMove = slot.getItem();
         original = toMove.copy();
         EquipmentType equipmentslot = EquipmentType.getEquipmentSlotForItem(original);
         if (slotID == 0) {
            if (!this.moveItemStackTo(toMove, 9, 45, true)) {
               return ItemStack.EMPTY;
            }
         } else if (slotID >= 1 && slotID < 5) {
            if (!this.moveItemStackTo(toMove, 9, 45, false)) {
               return ItemStack.EMPTY;
            }
         } else if (slotID >= 5 && slotID < 9) {
            if (!this.moveItemStackTo(toMove, 9, 45, false)) {
               return ItemStack.EMPTY;
            }
         } else if (equipmentslot.isArmor() && !((Slot)this.getSlots().get(8 - equipmentslot.getIndex())).hasItem()) {
            int i = 8 - equipmentslot.getIndex();
            if (!this.moveItemStackTo(toMove, i, i + 1, false)) {
               return ItemStack.EMPTY;
            }
         } else if (equipmentslot == EquipmentType.OFFHAND && !((Slot)this.getSlots().get(45)).hasItem()) {
            if (!this.moveItemStackTo(toMove, 45, 46, false)) {
               return ItemStack.EMPTY;
            }
         } else if (slotID >= 9 && slotID < 36) {
            if (!this.moveItemStackTo(toMove, 36, 45, false)) {
               return ItemStack.EMPTY;
            }
         } else if (slotID >= 36 && slotID < 45) {
            if (!this.moveItemStackTo(toMove, 9, 36, false)) {
               return ItemStack.EMPTY;
            }
         } else if (!this.moveItemStackTo(toMove, 9, 45, false)) {
            return ItemStack.EMPTY;
         }

         if (toMove.isEmpty()) {
            slot.set(ItemStack.EMPTY);
         }

         if (toMove.getAmount() == original.getAmount()) {
            return ItemStack.EMPTY;
         }
      }

      return original;
   }

   public boolean canTakeItemForPickAll(ItemStack p_38908_, Slot p_38909_) {
      return p_38909_.inventoryStorageSlot != 0;
   }

   @Generated
   public CorrectingPlayerInventoryStorage getInventoryStorage() {
      return this.inventoryStorage;
   }
}
