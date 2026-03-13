package com.nisovin.shopkeepers.playershops;

import com.nisovin.shopkeepers.util.bukkit.PermissionUtils;
import com.nisovin.shopkeepers.util.java.ConversionUtils;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.checkerframework.checker.nullness.qual.Nullable;

public class MaxShopsPermission implements Comparable<MaxShopsPermission> {
   public static final MaxShopsPermission UNLIMITED = new MaxShopsPermission(Integer.MAX_VALUE, "shopkeeper.maxshops.unlimited");
   private static final String PERMISSION_PREFIX = "shopkeeper.maxshops.";
   private final int maxShops;
   private final String permission;

   public static MaxShopsPermission parse(String maxShopsPermissionOption) {
      Integer maxShops = ConversionUtils.parseInt(maxShopsPermissionOption);
      if (maxShops != null && maxShops > 0) {
         String permission = "shopkeeper.maxshops." + maxShops;
         return new MaxShopsPermission(maxShops, permission);
      } else {
         throw new IllegalArgumentException("Invalid max shops permission option: " + maxShopsPermissionOption);
      }
   }

   public static List<MaxShopsPermission> parseList(String maxShopsPermissionOptionsList, Consumer<? super String> invalidPermissionOptionCallback) {
      Validate.notNull(maxShopsPermissionOptionsList, (String)"maxShopsPermissionOptionsList is null");
      Validate.notNull(invalidPermissionOptionCallback, (String)"invalidPermissionOptionCallback is null");
      String[] permissionOptions = StringUtils.removeWhitespace(maxShopsPermissionOptionsList).split(",");
      List<MaxShopsPermission> maxShopsPermissions = new ArrayList(permissionOptions.length);
      String[] var4 = permissionOptions;
      int var5 = permissionOptions.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String permissionOption = var4[var6];

         MaxShopsPermission maxShopsPermission;
         try {
            maxShopsPermission = parse(permissionOption);
         } catch (IllegalArgumentException var10) {
            invalidPermissionOptionCallback.accept(permissionOption);
            continue;
         }

         assert maxShopsPermission != null;

         maxShopsPermissions.add(maxShopsPermission);
      }

      return maxShopsPermissions;
   }

   public MaxShopsPermission(int maxShops, String permission) {
      Validate.isTrue(maxShops > 0, "maxShops has to be positive");
      Validate.notEmpty(permission, "permission is null or empty");
      this.maxShops = maxShops;
      this.permission = permission;
   }

   public int getMaxShops() {
      return this.maxShops;
   }

   public boolean isUnlimited() {
      return this.maxShops == Integer.MAX_VALUE;
   }

   public String getPermission() {
      return this.permission;
   }

   public void registerPermission() {
      PermissionUtils.registerPermission(this.permission, (node) -> {
         return this.createPermission();
      });
   }

   private Permission createPermission() {
      String description;
      if (this.isUnlimited()) {
         description = "Allows ownership of an unlimited number of shopkeepers";
      } else {
         description = "Allows ownership of up to " + this.maxShops + " shopkeeper(s)";
      }

      return new Permission(this.permission, description, PermissionDefault.FALSE);
   }

   public boolean hasPermission(Permissible permissible) {
      return PermissionUtils.hasPermission(permissible, this.permission);
   }

   public int compareTo(MaxShopsPermission other) {
      return Integer.compare(this.maxShops, other.maxShops);
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("MaxShopsPermission [maxShops=");
      builder.append(this.maxShops);
      builder.append(", permission=");
      builder.append(this.permission);
      builder.append("]");
      return builder.toString();
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + this.maxShops;
      result = 31 * result + this.permission.hashCode();
      return result;
   }

   public boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof MaxShopsPermission)) {
         return false;
      } else {
         MaxShopsPermission other = (MaxShopsPermission)obj;
         if (this.maxShops != other.maxShops) {
            return false;
         } else {
            return this.permission.equals(other.permission);
         }
      }
   }
}
