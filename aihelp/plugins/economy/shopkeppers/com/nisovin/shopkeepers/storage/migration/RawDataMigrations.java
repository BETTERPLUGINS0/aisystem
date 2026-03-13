package com.nisovin.shopkeepers.storage.migration;

import com.nisovin.shopkeepers.util.logging.Log;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class RawDataMigrations {
   private static final List<? extends RawDataMigration> migrations = Arrays.asList(new RawDataMigration_1_20_5_PlayerProfiles());

   public static String applyMigrations(String data) throws RawDataMigrationException {
      if (data.isEmpty()) {
         return data;
      } else {
         String migratedData = data;
         Iterator var2 = migrations.iterator();

         while(var2.hasNext()) {
            RawDataMigration migration = (RawDataMigration)var2.next();
            Log.debug("Applying raw shopkeeper data migration: " + migration.getName());

            try {
               migratedData = migration.apply(migratedData);
            } catch (Exception var5) {
               throw new RawDataMigrationException("Raw shopkeeper data migration failed with an error!", var5);
            }
         }

         return migratedData;
      }
   }

   private RawDataMigrations() {
   }
}
