package com.nisovin.shopkeepers.shopobjects;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.data.container.DelegateDataContainer;
import com.nisovin.shopkeepers.util.java.Validate;
import org.checkerframework.checker.nullness.qual.PolyNull;

public class ShopObjectData extends DelegateDataContainer {
   @PolyNull
   public static ShopObjectData of(@PolyNull DataContainer dataContainer) {
      if (dataContainer == null) {
         return null;
      } else {
         return dataContainer instanceof ShopObjectData ? (ShopObjectData)dataContainer : new ShopObjectData(dataContainer);
      }
   }

   public static ShopObjectData ofNonNull(DataContainer dataContainer) {
      Validate.notNull(dataContainer, (String)"dataContainer is null");
      return (ShopObjectData)Unsafe.assertNonNull(of(dataContainer));
   }

   protected ShopObjectData(DataContainer dataContainer) {
      super(dataContainer);
   }
}
