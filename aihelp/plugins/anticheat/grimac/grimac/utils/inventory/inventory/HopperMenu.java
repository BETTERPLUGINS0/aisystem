package ac.grim.grimac.utils.inventory.inventory;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.utils.inventory.Inventory;
import ac.grim.grimac.utils.inventory.InventoryStorage;
import ac.grim.grimac.utils.inventory.slot.Slot;

public class HopperMenu extends AbstractContainerMenu {
   public HopperMenu(GrimPlayer player, Inventory playerInventory) {
      super(player, playerInventory);
      InventoryStorage containerStorage = new InventoryStorage(5);

      for(int i = 0; i < 5; ++i) {
         this.addSlot(new Slot(containerStorage, i));
      }

      this.addFourRowPlayerInventory();
   }

   public ItemStack quickMoveStack(int slotID) {
      ItemStack itemstack = ItemStack.EMPTY;
      Slot slot = (Slot)this.slots.get(slotID);
      if (slot != null && slot.hasItem()) {
         ItemStack itemstack1 = slot.getItem();
         itemstack = itemstack1.copy();
         if (slotID < 5) {
            if (!this.moveItemStackTo(itemstack1, 5, this.slots.size(), true)) {
               return ItemStack.EMPTY;
            }
         } else if (!this.moveItemStackTo(itemstack1, 0, 5, false)) {
            return ItemStack.EMPTY;
         }

         if (itemstack1.isEmpty()) {
            slot.set(ItemStack.EMPTY);
         }
      }

      return itemstack;
   }
}
