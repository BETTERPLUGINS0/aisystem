package com.nisovin.shopkeepers.shopkeeper;

import com.nisovin.shopkeepers.api.shopkeeper.TradingRecipe;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import java.util.Objects;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class TradingRecipeDraft {
   public static final TradingRecipeDraft EMPTY = new TradingRecipeDraft((UnmodifiableItemStack)null, (UnmodifiableItemStack)null, (UnmodifiableItemStack)null);
   @Nullable
   protected final UnmodifiableItemStack resultItem;
   @Nullable
   protected final UnmodifiableItemStack item1;
   @Nullable
   protected final UnmodifiableItemStack item2;

   public TradingRecipeDraft(@Nullable @ReadOnly ItemStack resultItem, @Nullable @ReadOnly ItemStack item1, @Nullable @ReadOnly ItemStack item2) {
      this(UnmodifiableItemStack.of(resultItem), UnmodifiableItemStack.of(item1), UnmodifiableItemStack.of(item2));
   }

   public TradingRecipeDraft(@Nullable UnmodifiableItemStack resultItem, @Nullable UnmodifiableItemStack item1, @Nullable UnmodifiableItemStack item2) {
      this.resultItem = ItemUtils.getNullIfEmpty(resultItem);
      this.item1 = ItemUtils.getNullIfEmpty(item1);
      this.item2 = ItemUtils.getNullIfEmpty(item2);
   }

   @Nullable
   public UnmodifiableItemStack getResultItem() {
      return this.resultItem;
   }

   @Nullable
   public UnmodifiableItemStack getItem1() {
      return this.item1;
   }

   @Nullable
   public final UnmodifiableItemStack getItem2() {
      return this.item2;
   }

   @Nullable
   public final UnmodifiableItemStack getRecipeItem1() {
      return this.item1 != null ? this.item1 : this.item2;
   }

   @Nullable
   public final UnmodifiableItemStack getRecipeItem2() {
      return this.item1 != null ? this.item2 : null;
   }

   public final boolean isEmpty() {
      return this.resultItem == null && this.item1 == null && this.item2 == null;
   }

   public final boolean isValid() {
      return this.resultItem != null && (this.item1 != null || this.item2 != null);
   }

   public final boolean areItemsEqual(@Nullable @ReadOnly ItemStack resultItem, @Nullable @ReadOnly ItemStack item1, @Nullable @ReadOnly ItemStack item2) {
      if (!ItemUtils.equals(this.resultItem, resultItem)) {
         return false;
      } else if (!ItemUtils.equals(this.item1, item1)) {
         return false;
      } else {
         return ItemUtils.equals(this.item2, item2);
      }
   }

   public final boolean areItemsEqual(@Nullable UnmodifiableItemStack resultItem, @Nullable UnmodifiableItemStack item1, @Nullable UnmodifiableItemStack item2) {
      return this.areItemsEqual(ItemUtils.asItemStackOrNull(resultItem), ItemUtils.asItemStackOrNull(item1), ItemUtils.asItemStackOrNull(item2));
   }

   public final boolean areItemsEqual(@Nullable TradingRecipeDraft otherRecipe) {
      return otherRecipe == null ? false : this.areItemsEqual(otherRecipe.resultItem, otherRecipe.item1, otherRecipe.item2);
   }

   public final boolean areItemsEqual(@Nullable TradingRecipe otherRecipe) {
      return otherRecipe == null ? false : this.areItemsEqual(otherRecipe.getResultItem(), otherRecipe.getItem1(), otherRecipe.getItem2());
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("TradingRecipeDraft [resultItem=");
      builder.append(this.resultItem);
      builder.append(", item1=");
      builder.append(this.item1);
      builder.append(", item2=");
      builder.append(this.item2);
      builder.append("]");
      return builder.toString();
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + Objects.hashCode(this.resultItem);
      result = 31 * result + Objects.hashCode(this.item1);
      result = 31 * result + Objects.hashCode(this.item2);
      return result;
   }

   public boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (!(obj instanceof TradingRecipeDraft)) {
         return false;
      } else {
         TradingRecipeDraft other = (TradingRecipeDraft)obj;
         if (!Objects.equals(this.resultItem, other.resultItem)) {
            return false;
         } else if (!Objects.equals(this.item1, other.item1)) {
            return false;
         } else {
            return Objects.equals(this.item2, other.item2);
         }
      }
   }
}
