package me.casperge.realisticseasons.temperature;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CustomTemperatureItem {
   private Material material;
   private int custommodeldata;
   private int modifier;
   private boolean onWearing;
   private boolean onHold;
   private boolean useCustomModelData;
   private boolean worksUnderwater;

   public CustomTemperatureItem(Material var1, int var2, int var3, boolean var4, boolean var5, boolean var6, boolean var7) {
      this.material = var1;
      this.custommodeldata = var2;
      this.modifier = var3;
      this.onWearing = var4;
      this.onHold = var5;
      this.useCustomModelData = var6;
      this.worksUnderwater = var7;
   }

   public boolean onWear() {
      return this.onWearing;
   }

   public boolean isActive(Player var1) {
      if (!this.worksUnderwater && var1.isInWater()) {
         return false;
      } else {
         if (this.onWearing) {
            ItemStack[] var2 = var1.getInventory().getArmorContents();
            if (var2.length != 0) {
               ItemStack[] var3 = var2;
               int var4 = var2.length;

               for(int var5 = 0; var5 < var4; ++var5) {
                  ItemStack var6 = var3[var5];
                  if (var6 != null && this.isItem(var6)) {
                     return true;
                  }
               }
            }
         }

         if (this.onHold) {
            if (var1.getInventory().getItemInMainHand() != null && this.isItem(var1.getInventory().getItemInMainHand())) {
               return true;
            }

            if (var1.getInventory().getItemInOffHand() != null && this.isItem(var1.getInventory().getItemInOffHand())) {
               return true;
            }
         }

         return false;
      }
   }

   public int getModifier() {
      return this.modifier;
   }

   public boolean isItem(ItemStack var1) {
      if (!var1.getType().equals(this.material)) {
         return false;
      } else if (!this.useCustomModelData) {
         return true;
      } else if (!var1.hasItemMeta()) {
         return false;
      } else if (!var1.getItemMeta().hasCustomModelData()) {
         return false;
      } else {
         return var1.getItemMeta().getCustomModelData() == this.custommodeldata;
      }
   }
}
