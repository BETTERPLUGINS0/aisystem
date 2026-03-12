package com.nisovin.shopkeepers.shopkeeper;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.TradingRecipe;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SKTradingRecipe extends TradingRecipeDraft implements TradingRecipe {
   private final boolean outOfStock;

   public SKTradingRecipe(@ReadOnly ItemStack resultItem, @ReadOnly ItemStack item1, @Nullable @ReadOnly ItemStack item2) {
      this(resultItem, item1, item2, false);
   }

   public SKTradingRecipe(@ReadOnly ItemStack resultItem, @ReadOnly ItemStack item1, @Nullable @ReadOnly ItemStack item2, boolean outOfStock) {
      this(ItemUtils.nonNullUnmodifiableClone(resultItem), ItemUtils.nonNullUnmodifiableClone(item1), ItemUtils.unmodifiableClone(item2), outOfStock);
   }

   public SKTradingRecipe(UnmodifiableItemStack resultItem, UnmodifiableItemStack item1, @Nullable UnmodifiableItemStack item2) {
      this(resultItem, item1, item2, false);
   }

   public SKTradingRecipe(UnmodifiableItemStack resultItem, UnmodifiableItemStack item1, @Nullable UnmodifiableItemStack item2, boolean outOfStock) {
      super(resultItem, item1, item2);
      Validate.isTrue(!ItemUtils.isEmpty(resultItem), "resultItem is empty");
      Validate.isTrue(!ItemUtils.isEmpty(item1), "item1 is empty");
      this.outOfStock = outOfStock;
   }

   @NonNull
   public UnmodifiableItemStack getResultItem() {
      return (UnmodifiableItemStack)Unsafe.assertNonNull(this.resultItem);
   }

   @NonNull
   public UnmodifiableItemStack getItem1() {
      return (UnmodifiableItemStack)Unsafe.assertNonNull(this.item1);
   }

   public final boolean hasItem2() {
      return this.item2 != null;
   }

   public final boolean isOutOfStock() {
      return this.outOfStock;
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("SKTradingRecipe [resultItem=");
      builder.append(this.resultItem);
      builder.append(", item1=");
      builder.append(this.item1);
      builder.append(", item2=");
      builder.append(this.item2);
      builder.append(", outOfStock=");
      builder.append(this.outOfStock);
      builder.append("]");
      return builder.toString();
   }

   public int hashCode() {
      int prime = true;
      int result = super.hashCode();
      result = 31 * result + (this.outOfStock ? 1231 : 1237);
      return result;
   }

   public boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!super.equals(obj)) {
         return false;
      } else if (!(obj instanceof SKTradingRecipe)) {
         return false;
      } else {
         SKTradingRecipe other = (SKTradingRecipe)obj;
         return this.outOfStock == other.outOfStock;
      }
   }
}
