package com.nisovin.shopkeepers.shopkeeper.migration;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopobjects.ShopObject;
import com.nisovin.shopkeepers.util.java.ClassUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.HashMap;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.Nullable;

public class MigrationPhase {
   public static final MigrationPhase EARLY = new MigrationPhase("early");
   public static final MigrationPhase DEFAULT = new MigrationPhase("default");
   public static final MigrationPhase LATE = new MigrationPhase("late");
   private final String name;

   public static MigrationPhase ofShopkeeperClass(Class<? extends Shopkeeper> shopkeeperClass) {
      return MigrationPhase.ShopkeeperClassMigrationPhase.of(shopkeeperClass);
   }

   public static MigrationPhase ofShopObjectClass(Class<? extends ShopObject> shopObjectClass) {
      return MigrationPhase.ShopObjectClassMigrationPhase.of(shopObjectClass);
   }

   private MigrationPhase(String name) {
      Validate.notEmpty(name, "name is empty");
      this.name = name;
   }

   public final String getName() {
      return this.name;
   }

   public boolean isApplicable(MigrationPhase migrationPhase) {
      return this.equals(migrationPhase);
   }

   public String toString() {
      return "MigrationPhase " + this.getName();
   }

   public static final class ShopkeeperClassMigrationPhase extends MigrationPhase {
      private static final Map<Class<? extends Shopkeeper>, MigrationPhase.ShopkeeperClassMigrationPhase> CACHE = new HashMap();
      private final Class<? extends Shopkeeper> shopkeeperClass;

      private static MigrationPhase.ShopkeeperClassMigrationPhase of(Class<? extends Shopkeeper> shopkeeperClass) {
         MigrationPhase.ShopkeeperClassMigrationPhase migrationPhase = (MigrationPhase.ShopkeeperClassMigrationPhase)CACHE.computeIfAbsent(shopkeeperClass, MigrationPhase.ShopkeeperClassMigrationPhase::new);

         assert migrationPhase != null;

         return migrationPhase;
      }

      private static String getName(Class<? extends Shopkeeper> shopkeeperClass) {
         Validate.notNull(shopkeeperClass, (String)"shopkeeperClass is null");
         return ClassUtils.getSimpleTypeName(shopkeeperClass);
      }

      private ShopkeeperClassMigrationPhase(Class<? extends Shopkeeper> shopkeeperClass) {
         super(getName(shopkeeperClass));
         this.shopkeeperClass = shopkeeperClass;
      }

      public final Class<? extends Shopkeeper> getShopkeeperClass() {
         return this.shopkeeperClass;
      }

      public boolean isApplicable(MigrationPhase migrationPhase) {
         if (this == migrationPhase) {
            return true;
         } else if (!(migrationPhase instanceof MigrationPhase.ShopkeeperClassMigrationPhase)) {
            return false;
         } else {
            MigrationPhase.ShopkeeperClassMigrationPhase other = (MigrationPhase.ShopkeeperClassMigrationPhase)migrationPhase;
            return this.shopkeeperClass.isAssignableFrom(other.shopkeeperClass);
         }
      }

      public int hashCode() {
         int prime = true;
         int result = 1;
         int result = 31 * result + this.shopkeeperClass.hashCode();
         return result;
      }

      public boolean equals(@Nullable Object obj) {
         if (this == obj) {
            return true;
         } else if (!(obj instanceof MigrationPhase.ShopkeeperClassMigrationPhase)) {
            return false;
         } else {
            MigrationPhase.ShopkeeperClassMigrationPhase other = (MigrationPhase.ShopkeeperClassMigrationPhase)obj;
            return this.shopkeeperClass == other.shopkeeperClass;
         }
      }
   }

   public static final class ShopObjectClassMigrationPhase extends MigrationPhase {
      private static final Map<Class<? extends ShopObject>, MigrationPhase.ShopObjectClassMigrationPhase> CACHE = new HashMap();
      private final Class<? extends ShopObject> shopObjectClass;

      private static MigrationPhase.ShopObjectClassMigrationPhase of(Class<? extends ShopObject> shopObjectClass) {
         MigrationPhase.ShopObjectClassMigrationPhase migrationPhase = (MigrationPhase.ShopObjectClassMigrationPhase)CACHE.computeIfAbsent(shopObjectClass, MigrationPhase.ShopObjectClassMigrationPhase::new);

         assert migrationPhase != null;

         return migrationPhase;
      }

      private static String getName(Class<? extends ShopObject> shopObjectClass) {
         Validate.notNull(shopObjectClass, (String)"shopObjectClass is null");
         return ClassUtils.getSimpleTypeName(shopObjectClass);
      }

      private ShopObjectClassMigrationPhase(Class<? extends ShopObject> shopObjectClass) {
         super(getName(shopObjectClass));
         this.shopObjectClass = shopObjectClass;
      }

      public final Class<? extends ShopObject> getShopObjectClass() {
         return this.shopObjectClass;
      }

      public boolean isApplicable(MigrationPhase migrationPhase) {
         if (this == migrationPhase) {
            return true;
         } else if (!(migrationPhase instanceof MigrationPhase.ShopObjectClassMigrationPhase)) {
            return false;
         } else {
            MigrationPhase.ShopObjectClassMigrationPhase other = (MigrationPhase.ShopObjectClassMigrationPhase)migrationPhase;
            return this.shopObjectClass.isAssignableFrom(other.shopObjectClass);
         }
      }

      public int hashCode() {
         int prime = true;
         int result = 1;
         int result = 31 * result + this.shopObjectClass.hashCode();
         return result;
      }

      public boolean equals(@Nullable Object obj) {
         if (this == obj) {
            return true;
         } else if (!(obj instanceof MigrationPhase.ShopObjectClassMigrationPhase)) {
            return false;
         } else {
            MigrationPhase.ShopObjectClassMigrationPhase other = (MigrationPhase.ShopObjectClassMigrationPhase)obj;
            return this.shopObjectClass == other.shopObjectClass;
         }
      }
   }
}
