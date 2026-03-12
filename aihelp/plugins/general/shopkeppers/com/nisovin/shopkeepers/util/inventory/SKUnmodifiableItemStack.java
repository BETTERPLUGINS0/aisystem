package com.nisovin.shopkeepers.util.inventory;

import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.PolyNull;

@DelegateDeserialization(ItemStack.class)
public class SKUnmodifiableItemStack implements UnmodifiableItemStack {
   private final ItemStack delegate;

   @PolyNull
   public static UnmodifiableItemStack of(@PolyNull @ReadOnly ItemStack itemStack) {
      return itemStack == null ? null : new SKUnmodifiableItemStack(itemStack);
   }

   private SKUnmodifiableItemStack(@ReadOnly ItemStack itemStack) {
      assert itemStack != null;

      this.delegate = itemStack;
   }

   /** @deprecated */
   @Deprecated
   public ItemStack getInternalItemStack() {
      return this.delegate;
   }

   public ItemStack copy() {
      return this.delegate.clone();
   }

   public UnmodifiableItemStack shallowCopy() {
      return UnmodifiableItemStack.ofNonNull(this.delegate);
   }

   public Material getType() {
      return this.delegate.getType();
   }

   public int getAmount() {
      return this.delegate.getAmount();
   }

   public int getMaxStackSize() {
      return this.delegate.getMaxStackSize();
   }

   public boolean isSimilar(@Nullable @ReadOnly ItemStack itemStack) {
      return this.delegate.isSimilar(itemStack);
   }

   public boolean isSimilar(@Nullable UnmodifiableItemStack itemStack) {
      if (itemStack == null) {
         return false;
      } else {
         return itemStack == this ? true : itemStack.isSimilar(this.delegate);
      }
   }

   public boolean equals(@Nullable @ReadOnly ItemStack itemStack) {
      return this.delegate.equals(itemStack);
   }

   public boolean equals(@Nullable @ReadOnly Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (!(obj instanceof UnmodifiableItemStack)) {
         return false;
      } else {
         UnmodifiableItemStack other = (UnmodifiableItemStack)obj;
         return other.equals(this.delegate);
      }
   }

   public int hashCode() {
      return this.delegate.hashCode();
   }

   public boolean containsEnchantment(Enchantment enchantment) {
      return this.delegate.containsEnchantment(enchantment);
   }

   public int getEnchantmentLevel(Enchantment enchantment) {
      return this.delegate.getEnchantmentLevel(enchantment);
   }

   public Map<Enchantment, Integer> getEnchantments() {
      return this.delegate.getEnchantments();
   }

   public Map<String, Object> serialize() {
      return this.delegate.serialize();
   }

   @Nullable
   public ItemMeta getItemMeta() {
      return this.delegate.getItemMeta();
   }

   public boolean hasItemMeta() {
      return this.delegate.hasItemMeta();
   }

   public String toString() {
      return "Unmodifiable" + this.delegate.toString();
   }
}
