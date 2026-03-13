package com.nisovin.shopkeepers.util.bukkit;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.function.Function;
import java.util.function.Supplier;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

public final class PermissionUtils {
   private static boolean LOG_PERMISSION_CHECKS = true;

   public static <T> T runWithoutPermissionCheckLogging(Supplier<T> action) {
      if (!LOG_PERMISSION_CHECKS) {
         return action.get();
      } else {
         Object var1;
         try {
            LOG_PERMISSION_CHECKS = false;
            var1 = action.get();
         } finally {
            LOG_PERMISSION_CHECKS = true;
         }

         return var1;
      }
   }

   public static boolean hasPermission(Permissible permissible, String permission) {
      Validate.notNull(permissible, (String)"permissible is null");
      Validate.notEmpty(permission, "permission is null or empty");
      boolean hasPermission = permissible.hasPermission(permission);
      if (!hasPermission && permissible instanceof Player && LOG_PERMISSION_CHECKS) {
         Log.debug(() -> {
            String var10000 = ((Player)permissible).getName();
            return "Player '" + var10000 + "' does not have permission '" + permission + "'.";
         });
      }

      return hasPermission;
   }

   public static boolean registerPermission(String permissionNode, Function<String, Permission> permissionProvider) {
      Validate.notEmpty(permissionNode, "permissionNode is null or empty");
      Validate.notNull(permissionProvider, (String)"permissionProvider is null");
      PluginManager pluginManager = Bukkit.getPluginManager();
      if (pluginManager.getPermission(permissionNode) != null) {
         return false;
      } else {
         Permission permission = (Permission)Unsafe.assertNonNull((Permission)permissionProvider.apply(permissionNode));
         Validate.notNull(permission, (String)"permissionProvider returned a null permission");
         pluginManager.addPermission(permission);
         return true;
      }
   }

   public static boolean registerPermission(String permissionNode) {
      return registerPermission(permissionNode, (node) -> {
         return new Permission(node, PermissionDefault.FALSE);
      });
   }

   private PermissionUtils() {
   }
}
