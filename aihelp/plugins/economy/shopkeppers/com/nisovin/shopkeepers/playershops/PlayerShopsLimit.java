package com.nisovin.shopkeepers.playershops;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import org.bukkit.entity.Player;

public class PlayerShopsLimit {
   public void onEnable() {
      registerMaxShopsPermissions();
   }

   public void onDisable() {
   }

   public static void updateMaxShopsPermissions(Consumer<? super String> invalidPermissionOptionCallback) {
      Validate.notNull(invalidPermissionOptionCallback, (String)"invalidPermissionOptionCallback is null");
      String maxShopsPermissionOptions = Settings.maxShopsPermOptions;
      List<MaxShopsPermission> maxShopsPermissions = Settings.DerivedSettings.maxShopsPermissions;
      maxShopsPermissions.clear();
      maxShopsPermissions.add(MaxShopsPermission.UNLIMITED);
      maxShopsPermissions.addAll(MaxShopsPermission.parseList(maxShopsPermissionOptions, invalidPermissionOptionCallback));
      maxShopsPermissions.sort((Comparator)Unsafe.assertNonNull(Collections.reverseOrder()));
   }

   private static void registerMaxShopsPermissions() {
      Settings.DerivedSettings.maxShopsPermissions.forEach(MaxShopsPermission::registerPermission);
   }

   public static int getMaxShopsLimit(Player player) {
      if (Settings.maxShopsPerPlayer == -1) {
         return Integer.MAX_VALUE;
      } else {
         int maxShops = Settings.maxShopsPerPlayer;
         Iterator var2 = Settings.DerivedSettings.maxShopsPermissions.iterator();

         while(var2.hasNext()) {
            MaxShopsPermission maxShopsPermission = (MaxShopsPermission)var2.next();
            int permissionMaxShops = maxShopsPermission.getMaxShops();
            if (permissionMaxShops <= maxShops) {
               break;
            }

            if (maxShopsPermission.hasPermission(player)) {
               maxShops = permissionMaxShops;
               break;
            }
         }

         return maxShops;
      }
   }
}
