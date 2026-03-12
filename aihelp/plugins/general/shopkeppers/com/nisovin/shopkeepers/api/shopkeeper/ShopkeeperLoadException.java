package com.nisovin.shopkeepers.api.shopkeeper;

import org.checkerframework.checker.nullness.qual.Nullable;

public class ShopkeeperLoadException extends Exception {
   private static final long serialVersionUID = 2258914671011268570L;

   public ShopkeeperLoadException(@Nullable String message) {
      super(message);
   }

   public ShopkeeperLoadException(@Nullable String message, @Nullable Throwable cause) {
      super(message, cause);
   }
}
