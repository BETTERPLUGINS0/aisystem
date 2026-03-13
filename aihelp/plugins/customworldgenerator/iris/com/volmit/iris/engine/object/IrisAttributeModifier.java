package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.math.RNG;
import lombok.Generated;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.inventory.meta.ItemMeta;

@Snippet("attribute-modifier")
@Desc("Represents an attribute modifier for an item or an entity. This allows you to create modifications to basic game attributes such as MAX_HEALTH or ARMOR_VALUE.")
public class IrisAttributeModifier {
   @Required
   @Desc("The Attribute type. This type is pulled from the game attributes. Zombie & Horse attributes will not work on non-zombie/horse entities.\nUsing an attribute on an item will have affects when held, or worn. There is no way to specify further granularity as the game picks this depending on the item type.")
   private Attribute attribute = null;
   @MinNumber(2.0D)
   @Required
   @Desc("The Attribute Name is used internally only for the game. This value should be unique to all other attributes applied to this item/entity. It is not shown in game.")
   private String name = "";
   @Desc("The application operation (add number is default). Add Number adds to the default value. \nAdd scalar_1 will multiply by 1 for example if the health is 20 and you multiply_scalar_1 by 0.5, the health will result in 30, not 10. Use negative values to achieve that.")
   private Operation operation;
   @Desc("Minimum amount for this modifier. Iris randomly chooses an amount, this is the minimum it can choose randomly for this attribute.")
   private double minAmount;
   @Desc("Maximum amount for this modifier Iris randomly chooses an amount, this is the maximum it can choose randomly for this attribute.")
   private double maxAmount;
   @MinNumber(0.0D)
   @MaxNumber(1.0D)
   @Desc("The chance that this attribute is applied (0 to 1). If the chance is 0.5 (50%), then Iris will only apply this attribute 50% of the time.")
   private double chance;

   public void apply(RNG rng, ItemMeta meta) {
      if (var1.nextDouble() < this.getChance()) {
         var2.addAttributeModifier(this.getAttribute(), new AttributeModifier(this.getName(), this.getAmount(var1), this.getOperation()));
      }

   }

   public void apply(RNG rng, Attributable meta) {
      if (var1.nextDouble() < this.getChance()) {
         var2.getAttribute(this.getAttribute()).addModifier(new AttributeModifier(this.getName(), this.getAmount(var1), this.getOperation()));
      }

   }

   public double getAmount(RNG rng) {
      return var1.d(this.getMinAmount(), this.getMaxAmount());
   }

   @Generated
   public IrisAttributeModifier(final Attribute attribute, final String name, final Operation operation, final double minAmount, final double maxAmount, final double chance) {
      this.operation = Operation.ADD_NUMBER;
      this.minAmount = 1.0D;
      this.maxAmount = 1.0D;
      this.chance = 1.0D;
      this.attribute = var1;
      this.name = var2;
      this.operation = var3;
      this.minAmount = var4;
      this.maxAmount = var6;
      this.chance = var8;
   }

   @Generated
   public IrisAttributeModifier() {
      this.operation = Operation.ADD_NUMBER;
      this.minAmount = 1.0D;
      this.maxAmount = 1.0D;
      this.chance = 1.0D;
   }

   @Generated
   public Attribute getAttribute() {
      return this.attribute;
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public Operation getOperation() {
      return this.operation;
   }

   @Generated
   public double getMinAmount() {
      return this.minAmount;
   }

   @Generated
   public double getMaxAmount() {
      return this.maxAmount;
   }

   @Generated
   public double getChance() {
      return this.chance;
   }

   @Generated
   public IrisAttributeModifier setAttribute(final Attribute attribute) {
      this.attribute = var1;
      return this;
   }

   @Generated
   public IrisAttributeModifier setName(final String name) {
      this.name = var1;
      return this;
   }

   @Generated
   public IrisAttributeModifier setOperation(final Operation operation) {
      this.operation = var1;
      return this;
   }

   @Generated
   public IrisAttributeModifier setMinAmount(final double minAmount) {
      this.minAmount = var1;
      return this;
   }

   @Generated
   public IrisAttributeModifier setMaxAmount(final double maxAmount) {
      this.maxAmount = var1;
      return this;
   }

   @Generated
   public IrisAttributeModifier setChance(final double chance) {
      this.chance = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisAttributeModifier)) {
         return false;
      } else {
         IrisAttributeModifier var2 = (IrisAttributeModifier)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (Double.compare(this.getMinAmount(), var2.getMinAmount()) != 0) {
            return false;
         } else if (Double.compare(this.getMaxAmount(), var2.getMaxAmount()) != 0) {
            return false;
         } else if (Double.compare(this.getChance(), var2.getChance()) != 0) {
            return false;
         } else {
            label54: {
               Attribute var3 = this.getAttribute();
               Attribute var4 = var2.getAttribute();
               if (var3 == null) {
                  if (var4 == null) {
                     break label54;
                  }
               } else if (var3.equals(var4)) {
                  break label54;
               }

               return false;
            }

            String var5 = this.getName();
            String var6 = var2.getName();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            Operation var7 = this.getOperation();
            Operation var8 = var2.getOperation();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisAttributeModifier;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = Double.doubleToLongBits(this.getMinAmount());
      int var12 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      long var5 = Double.doubleToLongBits(this.getMaxAmount());
      var12 = var12 * 59 + (int)(var5 >>> 32 ^ var5);
      long var7 = Double.doubleToLongBits(this.getChance());
      var12 = var12 * 59 + (int)(var7 >>> 32 ^ var7);
      Attribute var9 = this.getAttribute();
      var12 = var12 * 59 + (var9 == null ? 43 : var9.hashCode());
      String var10 = this.getName();
      var12 = var12 * 59 + (var10 == null ? 43 : var10.hashCode());
      Operation var11 = this.getOperation();
      var12 = var12 * 59 + (var11 == null ? 43 : var11.hashCode());
      return var12;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getAttribute());
      return "IrisAttributeModifier(attribute=" + var10000 + ", name=" + this.getName() + ", operation=" + String.valueOf(this.getOperation()) + ", minAmount=" + this.getMinAmount() + ", maxAmount=" + this.getMaxAmount() + ", chance=" + this.getChance() + ")";
   }
}
