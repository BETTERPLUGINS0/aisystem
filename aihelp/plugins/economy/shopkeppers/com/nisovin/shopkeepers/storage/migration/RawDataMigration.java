package com.nisovin.shopkeepers.storage.migration;

public interface RawDataMigration {
   String getName();

   String apply(String var1) throws RawDataMigrationException;
}
