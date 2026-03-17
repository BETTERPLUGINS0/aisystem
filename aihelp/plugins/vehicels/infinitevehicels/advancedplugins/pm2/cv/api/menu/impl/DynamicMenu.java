package advancedplugins.pm2.cv.api.menu.impl;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.menu.ClickableItem;
import java.util.function.Consumer;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public abstract class DynamicMenu extends ChestMenu {
   private final InventoryType inventoryType;

   public DynamicMenu(String var1, int var2, InventoryType var3) {
      this(var3);
      this.setTitle(var1);
      this.setRows(var2);
   }

   public DynamicMenu(InventoryType var1) {
      this.inventoryType = var1;
   }

   public void setItem(int var1, ItemStack var2, @NonNull Consumer<InventoryClickEvent> var3) {
      if (var3 == null) {
         throw new NullPointerException("action is marked non-null but is null");
      } else if (var2 == null) {
         this.removeItem(var1);
      } else if (var1 >= 0 && var1 < this.getInventory().getSize()) {
         try {
            this.getInventory().setItem(var1, var2);
         } catch (ArrayIndexOutOfBoundsException var5) {
            InfiniteVehicles.getPlugin().getLogger().warning("Tried to set item in slot " + var1 + " but the inventory size is " + this.getInventory().getSize() + " (title: " + this.getTitle() + " and available slots are 0 to " + (this.getInventory().getSize() - 1) + ")");
            Thread.dumpStack();
            return;
         }

         this.itemsStorage.put(var1, new ClickableItem(var2, var3));
      } else {
         InfiniteVehicles.getPlugin().getLogger().warning("Tried to set item in slot " + var1 + " but the inventory size is " + this.getInventory().getSize() + " (title: " + this.getTitle() + " and available slots are 0 to " + (this.getInventory().getSize() - 1) + ")");
         Thread.dumpStack();
      }
   }

   public void setItem(int var1, ItemStack var2) {
      this.getInventory().setItem(var1, var2);
      this.itemsStorage.put(var1, new ClickableItem(var2, (var0) -> {
      }));
   }

   public ClickableItem getItem(int var1) {
      return (ClickableItem)this.itemsStorage.getOrDefault(var1, (Object)null);
   }

   public void removeItem(int var1) {
      this.getInventory().setItem(var1, (ItemStack)null);
      this.itemsStorage.remove(var1);
   }

   public int firstEmpty() {
      return this.getInventory().firstEmpty();
   }

   public void open(Player var1) {
      this.onOpen(var1);
      var1.openInventory(this.getInventory());
      InfiniteVehicles.getPlugin().getMenuManager().setByPlayer(var1, this);
   }

   public InventoryType inventoryType() {
      return this.inventoryType;
   }

   public InventoryType getInventoryType() {
      return this.inventoryType;
   }
}
