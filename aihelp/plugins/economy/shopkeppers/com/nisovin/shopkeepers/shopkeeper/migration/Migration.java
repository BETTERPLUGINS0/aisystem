package com.nisovin.shopkeepers.shopkeeper.migration;

import com.nisovin.shopkeepers.shopkeeper.ShopkeeperData;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.java.Validate;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class Migration {
   private final String name;
   private final MigrationPhase targetPhase;

   public Migration(String name, MigrationPhase targetPhase) {
      Validate.notEmpty(name, "name is null or empty");
      Validate.notNull(targetPhase, (String)"targetPhase is null");
      this.name = name;
      this.targetPhase = targetPhase;
   }

   public final String getName() {
      return this.name;
   }

   public final MigrationPhase getTargetPhase() {
      return this.targetPhase;
   }

   public abstract boolean migrate(ShopkeeperData var1, String var2) throws InvalidDataException;

   public final int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + this.name.hashCode();
      result = 31 * result + this.targetPhase.hashCode();
      return result;
   }

   public final boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof Migration)) {
         return false;
      } else {
         Migration other = (Migration)obj;
         if (!this.name.equals(other.name)) {
            return false;
         } else {
            return this.targetPhase.equals(other.targetPhase);
         }
      }
   }

   public final String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Migration [name=");
      builder.append(this.name);
      builder.append(", targetPhase=");
      builder.append(this.targetPhase);
      builder.append("]");
      return builder.toString();
   }
}
