package me.gypopo.economyshopgui.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.inventory.ItemStack;

public class ShopPageItems {
   private final int size;
   private final String section;
   private final Map<Integer, ItemStack> items;
   private final List<String> itemOrder;

   public String getItem(int slot) {
      return (String)this.itemOrder.get(slot);
   }

   public void addItem(String itemPath) {
      this.itemOrder.add(itemPath);
   }

   public ItemStack getItem(String itemPath) {
      return (ItemStack)this.items.get(itemPath);
   }

   public void setItem(int slot, ItemStack shopItem) {
      this.items.put(slot, shopItem);
   }

   public int getSize() {
      return this.size;
   }

   public List<String> getItemOrder() {
      return this.itemOrder;
   }

   public Map<Integer, ItemStack> getItems() {
      return new LinkedHashMap(this.items);
   }

   public ShopPageItems(Map<Integer, ItemStack> items, List<String> itemOrder, String section, int size) {
      this.items = items;
      this.itemOrder = itemOrder;
      this.section = section;
      this.size = size;
   }

   public ShopPageItems(String section) {
      this.items = new HashMap();
      this.itemOrder = new ArrayList();
      this.section = section;
      this.size = 54;
   }
}
