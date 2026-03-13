package com.dfsek.terra.bukkit.world.inventory;

import com.dfsek.terra.api.inventory.Item;
import com.dfsek.terra.api.inventory.ItemStack;
import com.dfsek.terra.api.inventory.item.ItemMeta;
import com.dfsek.terra.bukkit.world.BukkitAdapter;

public class BukkitItemStack implements ItemStack {
   private final org.bukkit.inventory.ItemStack delegate;

   public BukkitItemStack(org.bukkit.inventory.ItemStack delegate) {
      this.delegate = delegate;
   }

   public int getAmount() {
      return this.delegate.getAmount();
   }

   public void setAmount(int i) {
      this.delegate.setAmount(i);
   }

   public Item getType() {
      return BukkitAdapter.adapt(this.delegate.getType());
   }

   public ItemMeta getItemMeta() {
      return BukkitItemMeta.newInstance(this.delegate.getItemMeta());
   }

   public void setItemMeta(ItemMeta meta) {
      this.delegate.setItemMeta(((BukkitItemMeta)meta).getHandle());
   }

   public org.bukkit.inventory.ItemStack getHandle() {
      return this.delegate;
   }
}
