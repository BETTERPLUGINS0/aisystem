package me.gypopo.economyshopgui.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PermissionsCache {
   private static final LoadingCache<PermissionHolder, Boolean> PERMISSIONS_CACHE;

   public static boolean hasPermission(Player p, String permission) {
      try {
         return (Boolean)PERMISSIONS_CACHE.get(new PermissionHolder(p, permission));
      } catch (ExecutionException var3) {
         return p.hasPermission(permission);
      }
   }

   public static boolean hasPermission(CommandSender sender, String permission) {
      try {
         return !(sender instanceof Player) || (Boolean)PERMISSIONS_CACHE.get(new PermissionHolder((Player)sender, permission));
      } catch (ExecutionException var3) {
         return sender.hasPermission(permission);
      }
   }

   public static boolean hasPermission(Object sender, String permission) {
      try {
         return !(sender instanceof Player) || (Boolean)PERMISSIONS_CACHE.get(new PermissionHolder((Player)sender, permission));
      } catch (ExecutionException var3) {
         return ((CommandSender)sender).hasPermission(permission);
      }
   }

   static {
      PERMISSIONS_CACHE = CacheBuilder.newBuilder().expireAfterWrite(10L, TimeUnit.SECONDS).build(new CacheLoader<PermissionHolder, Boolean>() {
         public Boolean load(PermissionHolder key) throws Exception {
            Player p = Bukkit.getPlayer(key.getOwner());
            return p != null && p.hasPermission(key.getPermission());
         }
      });
   }
}
