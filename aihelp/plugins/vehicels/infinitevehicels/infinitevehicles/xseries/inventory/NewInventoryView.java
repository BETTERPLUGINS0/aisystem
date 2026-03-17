package me.PM2.infinitevehicles.xseries.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

class NewInventoryView extends BukkitInventoryView {
   private final InventoryView view;

   public NewInventoryView(Object var1) {
      this.view = (InventoryView)var1;
   }

   public Inventory getTopInventory() {
      return this.view.getTopInventory();
   }

   public Inventory getBottomInventory() {
      return this.view.getBottomInventory();
   }

   public HumanEntity getPlayer() {
      return this.view.getPlayer();
   }

   public InventoryType getType() {
      return this.view.getType();
   }

   public void setItem(int var1, ItemStack var2) {
      this.view.setItem(var1, var2);
   }

   public ItemStack getItem(int var1) {
      return this.view.getItem(var1);
   }

   public void setCursor(ItemStack var1) {
      this.view.setCursor(var1);
   }

   public ItemStack getCursor() {
      return this.view.getCursor();
   }

   public int convertSlot(int var1) {
      return this.view.convertSlot(var1);
   }

   public void close() {
      this.view.close();
   }

   public int countSlots() {
      return this.view.countSlots();
   }

   public String getTitle() {
      return this.view.getTitle();
   }

   public InventoryView object() {
      return this.view;
   }
}
