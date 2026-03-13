package me.ag4.playershop.api;

import java.util.HashMap;
import java.util.function.Consumer;
import me.ag4.playershop.PlayerShop;
import me.ag4.playershop.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class InventoryAPI implements Listener {
   private final Inventory inventory;
   private final HashMap<Integer, Consumer<ItemStack>> clickData1 = new HashMap();
   private Consumer<InventoryClickEvent> clickData2;
   private Consumer<InventoryClickEvent> clickData3;
   private final HashMap<Integer, Runnable> clickData4 = new HashMap();
   private Runnable closeRunnable;
   private Boolean clickable;

   public InventoryAPI(String title, int size) {
      Bukkit.getPluginManager().registerEvents(this, PlayerShop.getInstance());
      this.inventory = Bukkit.createInventory((InventoryHolder)null, size, Utils.hex(title));
   }

   public InventoryAPI(String title, int size, boolean clickable) {
      Bukkit.getPluginManager().registerEvents(this, PlayerShop.getPlugin(PlayerShop.class));
      this.inventory = Bukkit.createInventory((InventoryHolder)null, size, Utils.hex(title));
      this.clickable = clickable;
   }

   public InventoryAPI(String title, int size, Material border, boolean clickable) {
      Bukkit.getPluginManager().registerEvents(this, PlayerShop.getPlugin(PlayerShop.class));
      this.inventory = Bukkit.createInventory((InventoryHolder)null, size, Utils.hex(title));
      this.clickable = clickable;

      for(int i = 0; i < size; ++i) {
         this.inventory.setItem(i, Utils.createItem(border, " "));
      }

   }

   public InventoryAPI(String title, InventoryType inventoryType, Material border) {
      Bukkit.getPluginManager().registerEvents(this, PlayerShop.getPlugin(PlayerShop.class));
      this.inventory = Bukkit.createInventory((InventoryHolder)null, inventoryType, Utils.hex(title));

      for(int i = 0; i < this.inventory.getSize(); ++i) {
         this.inventory.setItem(i, Utils.createItem(border, " "));
      }

   }

   public InventoryAPI(String title, int size, Material border) {
      Bukkit.getPluginManager().registerEvents(this, PlayerShop.getPlugin(PlayerShop.class));
      this.inventory = Bukkit.createInventory((InventoryHolder)null, size, Utils.hex(title));

      for(int i = 0; i < size; ++i) {
         this.inventory.setItem(i, Utils.createItem(border, " "));
      }

   }

   public Inventory getInventory() {
      return this.inventory;
   }

   public void setItem(ItemStack item, int slot) {
      this.inventory.setItem(slot, item);
   }

   public void setItem(int slot, ItemStack item, Runnable handler) {
      this.inventory.setItem(slot, item);
      this.clickData4.put(slot, handler);
   }

   public void onClick(int slot, Consumer<ItemStack> handler) {
      this.clickData1.put(slot, handler);
   }

   public void onClick(Consumer<InventoryClickEvent> handler) {
      this.clickData2 = handler;
   }

   public void onClickInventory(Consumer<InventoryClickEvent> handler) {
      this.clickData3 = handler;
   }

   public void onClick(int slot, Runnable handler) {
      this.clickData4.put(slot, handler);
   }

   public void onClose(Runnable handler) {
      this.closeRunnable = handler;
   }

   @EventHandler
   private void onClick(InventoryClickEvent e) {
      if (e.getInventory().equals(this.inventory)) {
         if (Utils.isTopInventory(e.getRawSlot(), e.getView())) {
            Consumer<ItemStack> consumer = (Consumer)this.clickData1.get(e.getSlot());
            if (consumer != null) {
               ItemStack clickedItem = e.getCurrentItem();
               consumer.accept(clickedItem);
            }

            if (this.clickData2 != null) {
               this.clickData2.accept(e);
            }

            Runnable handler = (Runnable)this.clickData4.get(e.getSlot());
            if (handler != null) {
               handler.run();
            }
         }

         if (this.clickData3 != null) {
            this.clickData3.accept(e);
         }

         if (this.clickable == null || !this.clickable) {
            e.setCancelled(true);
         }

      }
   }

   @EventHandler
   private void onClick(InventoryCloseEvent e) {
      if (e.getInventory().equals(this.inventory)) {
         if (this.closeRunnable != null) {
            this.closeRunnable.run();
         }

         HandlerList.unregisterAll(this);
      }
   }
}
