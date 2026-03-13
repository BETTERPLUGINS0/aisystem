package com.nisovin.shopkeepers.api.shopkeeper;

import org.checkerframework.checker.nullness.qual.Nullable;

public class ShopkeeperCreateException extends Exception {
   private static final long serialVersionUID = -2026963951805397944L;

   public ShopkeeperCreateException(@Nullable String message) {
      super(message);
   }

   public ShopkeeperCreateException(@Nullable String message, @Nullable Throwable cause) {
      super(message, cause);
   }
}
