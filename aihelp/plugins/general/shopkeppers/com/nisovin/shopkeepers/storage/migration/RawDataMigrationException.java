package com.nisovin.shopkeepers.storage.migration;

public class RawDataMigrationException extends Exception {
   private static final long serialVersionUID = -2942869183804984729L;

   public RawDataMigrationException(String message) {
      super(message);
   }

   public RawDataMigrationException(Throwable cause) {
      super(cause);
   }

   public RawDataMigrationException(String message, Throwable cause) {
      super(message, cause);
   }
}
