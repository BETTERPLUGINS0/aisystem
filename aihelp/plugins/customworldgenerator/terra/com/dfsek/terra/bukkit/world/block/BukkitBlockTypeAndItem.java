package com.dfsek.terra.bukkit.world.block;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.inventory.Item;
import com.dfsek.terra.api.inventory.ItemStack;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import org.bukkit.Material;

public class BukkitBlockTypeAndItem implements BlockType, Item {
   private final Material delegate;

   public BukkitBlockTypeAndItem(Material delegate) {
      this.delegate = delegate;
   }

   public Material getHandle() {
      return this.delegate;
   }

   public BlockState getDefaultState() {
      return BukkitAdapter.adapt(this.delegate.createBlockData());
   }

   public boolean isSolid() {
      return this.delegate.isOccluding();
   }

   public boolean isWater() {
      return this.delegate == Material.WATER;
   }

   public ItemStack newItemStack(int amount) {
      return BukkitAdapter.adapt(new org.bukkit.inventory.ItemStack(this.delegate, amount));
   }

   public double getMaxDurability() {
      return (double)this.delegate.getMaxDurability();
   }

   public int hashCode() {
      return this.delegate.hashCode();
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof BukkitBlockTypeAndItem)) {
         return false;
      } else {
         return this.delegate == ((BukkitBlockTypeAndItem)obj).delegate;
      }
   }
}
