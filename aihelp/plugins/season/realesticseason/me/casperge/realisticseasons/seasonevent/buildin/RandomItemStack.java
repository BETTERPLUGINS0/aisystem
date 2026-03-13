package me.casperge.realisticseasons.seasonevent.buildin;

import me.casperge.realisticseasons.utils.JavaUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class RandomItemStack {
   private int min;
   private int max;
   private Material material;

   public RandomItemStack(Material var1, int var2, int var3) {
      this.material = var1;
      this.min = var2;
      this.max = var3;
   }

   public ItemStack generateStack() {
      return this.max == this.min ? new ItemStack(this.material, this.min) : new ItemStack(this.material, JavaUtils.getRandom().nextInt(this.max - this.min + 1) + this.min);
   }
}
