package com.nisovin.shopkeepers.compat;

import com.nisovin.shopkeepers.util.java.Validate;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ServerVersion {
   private final String minecraftVersion;
   private final String mappingsVersion;

   public ServerVersion(String minecraftVersion, String mappingsVersion) {
      Validate.notEmpty(minecraftVersion, "minecraftVersion is empty");
      Validate.notEmpty(mappingsVersion, "mappingsVersion is empty");
      this.minecraftVersion = minecraftVersion;
      this.mappingsVersion = mappingsVersion;
   }

   public String getMinecraftVersion() {
      return this.minecraftVersion;
   }

   public String getMappingsVersion() {
      return this.mappingsVersion;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + this.minecraftVersion.hashCode();
      result = 31 * result + this.mappingsVersion.hashCode();
      return result;
   }

   public boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ServerVersion)) {
         return false;
      } else {
         ServerVersion other = (ServerVersion)obj;
         if (!this.mappingsVersion.equals(other.mappingsVersion)) {
            return false;
         } else {
            return this.minecraftVersion.equals(other.minecraftVersion);
         }
      }
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("ServerVersion [minecraftVersion=");
      builder.append(this.minecraftVersion);
      builder.append(", mappingsVersion=");
      builder.append(this.mappingsVersion);
      builder.append("]");
      return builder.toString();
   }
}
