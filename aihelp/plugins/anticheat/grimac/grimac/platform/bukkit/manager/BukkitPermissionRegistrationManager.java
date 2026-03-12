package ac.grim.grimac.platform.bukkit.manager;

import ac.grim.grimac.platform.api.manager.PermissionRegistrationManager;
import ac.grim.grimac.platform.api.permissions.PermissionDefaultValue;
import ac.grim.grimac.platform.bukkit.utils.convert.BukkitConversionUtils;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

public class BukkitPermissionRegistrationManager implements PermissionRegistrationManager {
   public void registerPermission(String name, PermissionDefaultValue defaultValue) {
      Permission bukkitPermission = Bukkit.getPluginManager().getPermission(name);
      if (bukkitPermission == null) {
         Bukkit.getPluginManager().addPermission(new Permission(name, BukkitConversionUtils.toBukkitPermissionDefault(defaultValue)));
      } else {
         bukkitPermission.setDefault(BukkitConversionUtils.toBukkitPermissionDefault(defaultValue));
      }

   }
}
