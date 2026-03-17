package advancedplugins.pm2.cv.api.menu.impl;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.menu.ClickableItem;
import advancedplugins.pm2.cv.api.menu.Menu;
import advancedplugins.pm2.cv.api.util.ColorUtil;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class ChestMenu extends Menu {
   private String title;
   private Inventory inventory;

   public ChestMenu(String var1, int var2) {
      this.setTitle(var1);
      this.setRows(var2);
   }

   public <T extends ChestMenu> T setRows(int var1) {
      var1 = Math.max(1, Math.min(6, var1));
      if (this.inventoryType() == InventoryType.CHEST) {
         this.inventory = Bukkit.createInventory((InventoryHolder)null, var1 * 9, ColorUtil.translate(this.title == null ? "" : this.title));
      } else {
         this.inventory = Bukkit.createInventory((InventoryHolder)null, this.inventoryType(), ColorUtil.translate(this.title == null ? "" : this.title));
      }

      this.itemsStorage = new ConcurrentHashMap();
      return this;
   }

   private void check() {
      if (this.inventory == null) {
         throw new IllegalStateException("Inventory is null, please make sure to set title and rows!");
      }
   }

   public void setItem(int var1, ItemStack var2, @NonNull Consumer<InventoryClickEvent> var3) {
      if (var3 == null) {
         throw new NullPointerException("action is marked non-null but is null");
      } else if (var2 != null) {
         this.check();
         this.inventory.setItem(var1, var2);
         this.itemsStorage.put(var1, new ClickableItem(var2, var3));
      }
   }

   public void setItem(int var1, ItemStack var2) {
      this.setItem(var1, var2, (var0) -> {
      });
   }

   public ClickableItem getItem(int var1) {
      this.check();
      return (ClickableItem)this.itemsStorage.getOrDefault(var1, (Object)null);
   }

   public void removeItem(int var1) {
      this.check();
      this.inventory.setItem(var1, (ItemStack)null);
      this.itemsStorage.remove(var1);
   }

   public int firstEmpty() {
      this.check();
      return this.inventory.firstEmpty();
   }

   public void open(Player var1) {
      this.check();
      this.getInventory().clear();
      this.getItemsStorage().clear();
      if (this.title.contains("%player%") || this.title.contains("%player_name%")) {
         this.inventory = Bukkit.createInventory((InventoryHolder)null, this.getInventory().getSize(), ColorUtil.translate(this.title.replace("%player%", var1.getName()).replace("%player_name%", var1.getName())));
      }

      this.onOpen(var1);
      var1.openInventory(this.inventory);
      InfiniteVehicles.getPlugin().getMenuManager().setByPlayer(var1, this);
   }

   public InventoryType inventoryType() {
      return InventoryType.CHEST;
   }

   public String getTitle() {
      return this.title;
   }

   public Inventory getInventory() {
      return this.inventory;
   }

   public ChestMenu() {
   }

   public void setTitle(String var1) {
      this.title = var1;
   }

   public void setInventory(Inventory var1) {
      this.inventory = var1;
   }
}
