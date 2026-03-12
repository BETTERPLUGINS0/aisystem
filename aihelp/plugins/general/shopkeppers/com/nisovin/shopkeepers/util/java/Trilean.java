package com.nisovin.shopkeepers.util.java;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import org.checkerframework.checker.nullness.qual.Nullable;

public enum Trilean {
   FALSE(false) {
      public Trilean and(Trilean other) {
         return FALSE;
      }

      public Trilean or(Trilean other) {
         return other;
      }

      public Trilean not() {
         return TRUE;
      }
   },
   TRUE(true) {
      public Trilean and(Trilean other) {
         return other;
      }

      public Trilean or(Trilean other) {
         return TRUE;
      }

      public Trilean not() {
         return FALSE;
      }
   },
   UNDEFINED((Boolean)null) {
      public Trilean and(Trilean other) {
         return other == FALSE ? FALSE : UNDEFINED;
      }

      public Trilean or(Trilean other) {
         return other == TRUE ? TRUE : UNDEFINED;
      }

      public Trilean not() {
         return UNDEFINED;
      }
   };

   @Nullable
   private final Boolean booleanValue;

   private Trilean(@Nullable Boolean param3) {
      this.booleanValue = booleanValue;
   }

   public abstract Trilean and(Trilean var1);

   public abstract Trilean or(Trilean var1);

   public abstract Trilean not();

   public boolean toBoolean() {
      return this.booleanValue == null ? false : (Boolean)Unsafe.assertNonNull(this.booleanValue);
   }

   @Nullable
   public Boolean toNullableBoolean() {
      return this.booleanValue;
   }

   public static Trilean fromBoolean(boolean value) {
      return value ? TRUE : FALSE;
   }

   public static Trilean fromNullableBoolean(@Nullable Boolean value) {
      return value != null ? fromBoolean(value) : UNDEFINED;
   }

   // $FF: synthetic method
   private static Trilean[] $values() {
      return new Trilean[]{FALSE, TRUE, UNDEFINED};
   }
}
