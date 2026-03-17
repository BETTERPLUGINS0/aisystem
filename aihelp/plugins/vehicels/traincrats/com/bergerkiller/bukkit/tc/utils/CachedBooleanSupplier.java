package com.bergerkiller.bukkit.tc.utils;

import java.util.function.BooleanSupplier;

public final class CachedBooleanSupplier implements BooleanSupplier {
   private final BooleanSupplier getter;
   private Boolean result = null;

   public static CachedBooleanSupplier of(BooleanSupplier supplier) {
      return new CachedBooleanSupplier(supplier);
   }

   private CachedBooleanSupplier(BooleanSupplier supplier) {
      this.getter = supplier;
   }

   public boolean getAsBoolean() {
      Boolean result = this.result;
      if (result == null) {
         this.result = result = this.getter.getAsBoolean();
      }

      return result;
   }
}
