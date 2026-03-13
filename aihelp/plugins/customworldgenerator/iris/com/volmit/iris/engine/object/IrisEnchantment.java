package com.volmit.iris.engine.object;

import com.volmit.iris.Iris;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.RegistryListEnchantment;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.math.RNG;
import lombok.Generated;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

@Snippet("enchantment")
@Desc("Represents an enchantment & level")
public class IrisEnchantment {
   @Required
   @RegistryListEnchantment
   @Desc("The enchantment")
   private String enchantment;
   @MinNumber(1.0D)
   @Desc("Minimum amount of this loot")
   private int minLevel = 1;
   @MinNumber(1.0D)
   @Desc("Maximum amount of this loot")
   private int maxLevel = 1;
   @MinNumber(0.0D)
   @MaxNumber(1.0D)
   @Desc("The chance that this enchantment is applied (0 to 1)")
   private double chance = 1.0D;

   public void apply(RNG rng, ItemMeta meta) {
      try {
         Enchantment var3 = Enchantment.getByKey(NamespacedKey.minecraft(this.getEnchantment()));
         if (var3 == null) {
            Iris.warn("Unknown Enchantment: " + this.getEnchantment());
            return;
         }

         if (var1.nextDouble() < this.chance) {
            if (var2 instanceof EnchantmentStorageMeta) {
               ((EnchantmentStorageMeta)var2).addStoredEnchant(var3, this.getLevel(var1), true);
               return;
            }

            var2.addEnchant(var3, this.getLevel(var1), true);
         }
      } catch (Throwable var4) {
         Iris.reportError(var4);
      }

   }

   public int getLevel(RNG rng) {
      return var1.i(this.getMinLevel(), this.getMaxLevel());
   }

   @Generated
   public IrisEnchantment() {
   }

   @Generated
   public IrisEnchantment(final String enchantment, final int minLevel, final int maxLevel, final double chance) {
      this.enchantment = var1;
      this.minLevel = var2;
      this.maxLevel = var3;
      this.chance = var4;
   }

   @Generated
   public String getEnchantment() {
      return this.enchantment;
   }

   @Generated
   public int getMinLevel() {
      return this.minLevel;
   }

   @Generated
   public int getMaxLevel() {
      return this.maxLevel;
   }

   @Generated
   public double getChance() {
      return this.chance;
   }

   @Generated
   public IrisEnchantment setEnchantment(final String enchantment) {
      this.enchantment = var1;
      return this;
   }

   @Generated
   public IrisEnchantment setMinLevel(final int minLevel) {
      this.minLevel = var1;
      return this;
   }

   @Generated
   public IrisEnchantment setMaxLevel(final int maxLevel) {
      this.maxLevel = var1;
      return this;
   }

   @Generated
   public IrisEnchantment setChance(final double chance) {
      this.chance = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisEnchantment)) {
         return false;
      } else {
         IrisEnchantment var2 = (IrisEnchantment)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getMinLevel() != var2.getMinLevel()) {
            return false;
         } else if (this.getMaxLevel() != var2.getMaxLevel()) {
            return false;
         } else if (Double.compare(this.getChance(), var2.getChance()) != 0) {
            return false;
         } else {
            String var3 = this.getEnchantment();
            String var4 = var2.getEnchantment();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisEnchantment;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var6 = var2 * 59 + this.getMinLevel();
      var6 = var6 * 59 + this.getMaxLevel();
      long var3 = Double.doubleToLongBits(this.getChance());
      var6 = var6 * 59 + (int)(var3 >>> 32 ^ var3);
      String var5 = this.getEnchantment();
      var6 = var6 * 59 + (var5 == null ? 43 : var5.hashCode());
      return var6;
   }

   @Generated
   public String toString() {
      String var10000 = this.getEnchantment();
      return "IrisEnchantment(enchantment=" + var10000 + ", minLevel=" + this.getMinLevel() + ", maxLevel=" + this.getMaxLevel() + ", chance=" + this.getChance() + ")";
   }
}
