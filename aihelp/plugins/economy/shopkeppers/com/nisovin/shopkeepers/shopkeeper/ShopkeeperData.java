package com.nisovin.shopkeepers.shopkeeper;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.shopkeeper.migration.ShopkeeperDataMigrator;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.data.container.DelegateDataContainer;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.java.Validate;
import org.checkerframework.checker.nullness.qual.PolyNull;

public class ShopkeeperData extends DelegateDataContainer {
   @PolyNull
   public static ShopkeeperData of(@PolyNull DataContainer dataContainer) {
      if (dataContainer == null) {
         return null;
      } else {
         return dataContainer instanceof ShopkeeperData ? (ShopkeeperData)dataContainer : new ShopkeeperData(dataContainer);
      }
   }

   public static ShopkeeperData ofNonNull(DataContainer dataContainer) {
      Validate.notNull(dataContainer, (String)"dataContainer is null");
      return (ShopkeeperData)Unsafe.assertNonNull(of(dataContainer));
   }

   protected ShopkeeperData(DataContainer dataContainer) {
      super(dataContainer);
   }

   public final boolean migrate(String logPrefix) throws InvalidDataException {
      return ShopkeeperDataMigrator.migrate(this, logPrefix);
   }
}
