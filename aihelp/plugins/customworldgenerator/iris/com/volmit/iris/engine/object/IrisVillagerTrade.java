package com.volmit.iris.engine.object;

import com.volmit.iris.Iris;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.RegistryListItemType;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.math.RNG;
import java.util.List;
import lombok.Generated;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

@Snippet("villager-trade")
@Desc("Represents a villager trade.")
public class IrisVillagerTrade {
   @Required
   @RegistryListItemType
   @Desc("The first, required, ingredient for the trade.\nNote: this MUST be an item, and may not be a non-obtainable block!")
   private ItemStack ingredient1;
   @RegistryListItemType
   @Desc("The second, optional, ingredient for the trade.\nNote: this MUST be an item, and may not be a non-obtainable block!")
   private ItemStack ingredient2 = null;
   @Required
   @RegistryListItemType
   @Desc("The result of the trade.\nNote: this MUST be an item, and may not be a non-obtainable block!")
   private ItemStack result;
   @Desc("The min amount of times this trade can be done. Default 3")
   @MinNumber(1.0D)
   @MaxNumber(64.0D)
   private int minTrades = 3;
   @Desc("The max amount of times this trade can be done. Default 5")
   @MinNumber(1.0D)
   @MaxNumber(64.0D)
   private int maxTrades = 5;

   public boolean isValidItems() {
      KList var1 = new KList();
      if (this.ingredient1 == null) {
         var1.add((Object)"Ingredient 1 is null");
      }

      if (this.result == null) {
         var1.add((Object)"Result is null");
      }

      if (this.minTrades <= 0) {
         var1.add((Object)"Negative minimal trades");
      }

      if (this.maxTrades <= 0) {
         var1.add((Object)"Negative maximal trades");
      }

      if (this.minTrades > this.maxTrades) {
         var1.add((Object)"More minimal than maximal trades");
      }

      if (this.ingredient1 != null && !this.ingredient1.getType().isItem()) {
         var1.add((Object)"Ingredient 1 is not an item");
      }

      if (this.ingredient2 != null && !this.ingredient2.getType().isItem()) {
         var1.add((Object)"Ingredient 2 is not an item");
      }

      if (this.result != null && !this.result.getType().isItem()) {
         var1.add((Object)"Result is not an item");
      }

      if (var1.isEmpty()) {
         return true;
      } else {
         Iris.warn("Faulty item in cartographer item overrides: " + String.valueOf(this));
         var1.forEach((var0) -> {
            Iris.warn("   " + var0);
         });
         return false;
      }
   }

   public List<ItemStack> getIngredients() {
      if (!this.isValidItems()) {
         return null;
      } else {
         return this.ingredient2 == null ? new KList(new ItemStack[]{this.ingredient1}) : new KList(new ItemStack[]{this.ingredient1, this.ingredient2});
      }
   }

   public int getAmount() {
      return RNG.r.i(this.minTrades, this.maxTrades);
   }

   public MerchantRecipe convert() {
      MerchantRecipe var1 = new MerchantRecipe(this.getResult(), this.getAmount());
      var1.setIngredients(this.getIngredients());
      return var1;
   }

   @Generated
   public IrisVillagerTrade() {
   }

   @Generated
   public IrisVillagerTrade(final ItemStack ingredient1, final ItemStack ingredient2, final ItemStack result, final int minTrades, final int maxTrades) {
      this.ingredient1 = var1;
      this.ingredient2 = var2;
      this.result = var3;
      this.minTrades = var4;
      this.maxTrades = var5;
   }

   @Generated
   public ItemStack getIngredient1() {
      return this.ingredient1;
   }

   @Generated
   public ItemStack getIngredient2() {
      return this.ingredient2;
   }

   @Generated
   public ItemStack getResult() {
      return this.result;
   }

   @Generated
   public int getMinTrades() {
      return this.minTrades;
   }

   @Generated
   public int getMaxTrades() {
      return this.maxTrades;
   }

   @Generated
   public IrisVillagerTrade setIngredient1(final ItemStack ingredient1) {
      this.ingredient1 = var1;
      return this;
   }

   @Generated
   public IrisVillagerTrade setIngredient2(final ItemStack ingredient2) {
      this.ingredient2 = var1;
      return this;
   }

   @Generated
   public IrisVillagerTrade setResult(final ItemStack result) {
      this.result = var1;
      return this;
   }

   @Generated
   public IrisVillagerTrade setMinTrades(final int minTrades) {
      this.minTrades = var1;
      return this;
   }

   @Generated
   public IrisVillagerTrade setMaxTrades(final int maxTrades) {
      this.maxTrades = var1;
      return this;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getIngredient1());
      return "IrisVillagerTrade(ingredient1=" + var10000 + ", ingredient2=" + String.valueOf(this.getIngredient2()) + ", result=" + String.valueOf(this.getResult()) + ", minTrades=" + this.getMinTrades() + ", maxTrades=" + this.getMaxTrades() + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisVillagerTrade)) {
         return false;
      } else {
         IrisVillagerTrade var2 = (IrisVillagerTrade)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getMinTrades() != var2.getMinTrades()) {
            return false;
         } else if (this.getMaxTrades() != var2.getMaxTrades()) {
            return false;
         } else {
            label52: {
               ItemStack var3 = this.getIngredient1();
               ItemStack var4 = var2.getIngredient1();
               if (var3 == null) {
                  if (var4 == null) {
                     break label52;
                  }
               } else if (var3.equals(var4)) {
                  break label52;
               }

               return false;
            }

            ItemStack var5 = this.getIngredient2();
            ItemStack var6 = var2.getIngredient2();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            ItemStack var7 = this.getResult();
            ItemStack var8 = var2.getResult();
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
      return var1 instanceof IrisVillagerTrade;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var6 = var2 * 59 + this.getMinTrades();
      var6 = var6 * 59 + this.getMaxTrades();
      ItemStack var3 = this.getIngredient1();
      var6 = var6 * 59 + (var3 == null ? 43 : var3.hashCode());
      ItemStack var4 = this.getIngredient2();
      var6 = var6 * 59 + (var4 == null ? 43 : var4.hashCode());
      ItemStack var5 = this.getResult();
      var6 = var6 * 59 + (var5 == null ? 43 : var5.hashCode());
      return var6;
   }
}
