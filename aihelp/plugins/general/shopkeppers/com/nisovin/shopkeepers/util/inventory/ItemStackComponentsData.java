package com.nisovin.shopkeepers.util.inventory;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.compat.Compat;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.data.container.DelegateDataContainer;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.PolyNull;

public final class ItemStackComponentsData extends DelegateDataContainer {
   @Nullable
   public static ItemStackComponentsData of(@ReadOnly ItemStack itemStack) {
      return Compat.getProvider().getItemStackComponentsData(itemStack);
   }

   @PolyNull
   public static ItemStackComponentsData of(@PolyNull DataContainer dataContainer) {
      if (dataContainer == null) {
         return null;
      } else if (dataContainer instanceof ItemStackComponentsData) {
         ItemStackComponentsData itemStackComponentsData = (ItemStackComponentsData)dataContainer;
         return itemStackComponentsData;
      } else {
         return new ItemStackComponentsData(dataContainer);
      }
   }

   public static ItemStackComponentsData ofNonNull(DataContainer dataContainer) {
      Validate.notNull(dataContainer, (String)"dataContainer is null");
      return (ItemStackComponentsData)Unsafe.assertNonNull(of(dataContainer));
   }

   protected ItemStackComponentsData(DataContainer dataContainer) {
      super(dataContainer);
   }
}
