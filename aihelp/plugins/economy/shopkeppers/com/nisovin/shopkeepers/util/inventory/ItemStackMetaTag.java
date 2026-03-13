package com.nisovin.shopkeepers.util.inventory;

import com.nisovin.shopkeepers.compat.Compat;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ItemStackMetaTag {
   @Nullable
   private final Object nmsTag;

   public static ItemStackMetaTag of(@Nullable @ReadOnly ItemStack itemStack) {
      return Compat.getProvider().getItemStackMetaTag(itemStack);
   }

   public ItemStackMetaTag(@Nullable Object nmsTag) {
      this.nmsTag = nmsTag;
   }

   @Nullable
   public Object getNmsTag() {
      return this.nmsTag;
   }

   public boolean isEmpty() {
      return this.nmsTag == null;
   }

   public boolean matches(ItemStackMetaTag other, boolean matchPartialLists) {
      return Compat.getProvider().matches(other, this, matchPartialLists);
   }
}
