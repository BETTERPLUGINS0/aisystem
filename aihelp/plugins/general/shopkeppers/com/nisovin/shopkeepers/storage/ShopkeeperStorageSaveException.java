package com.nisovin.shopkeepers.storage;

import org.checkerframework.checker.nullness.qual.Nullable;

public class ShopkeeperStorageSaveException extends Exception {
   private static final long serialVersionUID = 3348780528378613697L;

   public ShopkeeperStorageSaveException(@Nullable String message) {
      super(message);
   }

   public ShopkeeperStorageSaveException(@Nullable String message, @Nullable Throwable cause) {
      super(message, cause);
   }
}
