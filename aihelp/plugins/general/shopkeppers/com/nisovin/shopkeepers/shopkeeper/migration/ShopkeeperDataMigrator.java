package com.nisovin.shopkeepers.shopkeeper.migration;

import com.nisovin.shopkeepers.shopkeeper.AbstractShopType;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopkeeper.ShopkeeperData;
import com.nisovin.shopkeepers.shopobjects.AbstractShopObject;
import com.nisovin.shopkeepers.shopobjects.AbstractShopObjectType;
import com.nisovin.shopkeepers.shopobjects.ShopObjectData;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class ShopkeeperDataMigrator {
   private static final List<Migration> migrations = new ArrayList();

   public static void registerMigration(Migration migration) {
      Validate.notNull(migration, (String)"migration is null");
      String migrationName = migration.getName();
      Iterator var2 = migrations.iterator();

      while(var2.hasNext()) {
         Migration otherMigration = (Migration)var2.next();
         if (otherMigration.getName().equalsIgnoreCase(migrationName)) {
            Validate.error("There already exists another shopkeeper data migration with the same name: " + migrationName);
         }
      }

      migrations.add(migration);
   }

   public static void logRegisteredMigrations() {
      Log.info("Registered shopkeeper data migrations:");
      migrations.forEach((migration) -> {
         String var10000 = migration.getName();
         Log.info("  - " + var10000 + " (" + migration.getTargetPhase().getName() + ")");
      });
   }

   public static boolean migrate(ShopkeeperData shopkeeperData, String logPrefix) throws InvalidDataException {
      Validate.notNull(shopkeeperData, (String)"shopkeeperData is null");
      Validate.notNull(logPrefix, (String)"logPrefix is null");
      boolean migrated = false;
      MigrationPhase currentPhase = MigrationPhase.EARLY;
      migrated |= migrate(currentPhase, shopkeeperData, logPrefix);
      currentPhase = MigrationPhase.DEFAULT;
      migrated |= migrate(currentPhase, shopkeeperData, logPrefix);
      AbstractShopType<?> shopType = (AbstractShopType)shopkeeperData.getOrNullIfMissing(AbstractShopkeeper.SHOP_TYPE);
      if (shopType != null) {
         currentPhase = MigrationPhase.ofShopkeeperClass(shopType.getShopkeeperClass());
         migrated |= migrate(currentPhase, shopkeeperData, logPrefix);
      }

      ShopObjectData shopObjectData = (ShopObjectData)shopkeeperData.getOrNullIfMissing(AbstractShopkeeper.SHOP_OBJECT_DATA);
      if (shopObjectData != null) {
         AbstractShopObjectType<?> shopObjectType = (AbstractShopObjectType)shopObjectData.getOrNullIfMissing(AbstractShopObject.SHOP_OBJECT_TYPE);
         if (shopObjectType != null) {
            currentPhase = MigrationPhase.ofShopObjectClass(shopObjectType.getShopObjectClass());
            migrated |= migrate(currentPhase, shopkeeperData, logPrefix);
         }
      }

      currentPhase = MigrationPhase.LATE;
      migrated |= migrate(currentPhase, shopkeeperData, logPrefix);
      return migrated;
   }

   private static boolean migrate(MigrationPhase currentPhase, ShopkeeperData shopkeeperData, String logPrefix) throws InvalidDataException {
      boolean migrated = false;
      Iterator var4 = migrations.iterator();

      while(var4.hasNext()) {
         Migration migration = (Migration)var4.next();
         if (migration.getTargetPhase().isApplicable(currentPhase)) {
            migrated |= migration.migrate(shopkeeperData, logPrefix);
         }
      }

      return migrated;
   }

   private ShopkeeperDataMigrator() {
   }
}
