package com.nisovin.shopkeepers.api.util;

import com.google.common.base.Preconditions;
import com.nisovin.shopkeepers.api.internal.ApiInternals;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.PolyNull;

public interface UnmodifiableItemStack extends ConfigurationSerializable {
   @PolyNull
   static UnmodifiableItemStack of(@PolyNull ItemStack itemStack) {
      return ApiInternals.getInstance().createUnmodifiableItemStack(itemStack);
   }

   static UnmodifiableItemStack ofNonNull(ItemStack itemStack) {
      Preconditions.checkNotNull(itemStack, "itemStack is null");
      return (UnmodifiableItemStack)Unsafe.assertNonNull(of(itemStack));
   }

   ItemStack copy();

   UnmodifiableItemStack shallowCopy();

   Material getType();

   int getAmount();

   int getMaxStackSize();

   boolean isSimilar(@Nullable ItemStack var1);

   boolean isSimilar(@Nullable UnmodifiableItemStack var1);

   boolean equals(@Nullable ItemStack var1);

   boolean equals(@Nullable Object var1);

   boolean containsEnchantment(Enchantment var1);

   int getEnchantmentLevel(Enchantment var1);

   Map<Enchantment, Integer> getEnchantments();

   Map<String, Object> serialize();

   @Nullable
   ItemMeta getItemMeta();

   boolean hasItemMeta();
}
