package fr.xephi.authme.permission.handlers;

import fr.xephi.authme.data.limbo.UserGroup;
import fr.xephi.authme.permission.PermissionNode;
import fr.xephi.authme.permission.PermissionsSystemType;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.tyrannyofheaven.bukkit.zPermissions.ZPermissionsService;

public class ZPermissionsHandler implements PermissionHandler {
   private ZPermissionsService zPermissionsService;

   public ZPermissionsHandler() throws PermissionHandlerException {
      ZPermissionsService zPermissionsService = (ZPermissionsService)Bukkit.getServicesManager().load(ZPermissionsService.class);
      if (zPermissionsService == null) {
         throw new PermissionHandlerException("Failed to get the ZPermissions service!");
      } else {
         this.zPermissionsService = zPermissionsService;
      }
   }

   public boolean addToGroup(OfflinePlayer player, UserGroup group) {
      return Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "permissions player " + player.getName() + " addgroup " + group.getGroupName());
   }

   public boolean hasGroupSupport() {
      return true;
   }

   public boolean hasPermissionOffline(String name, PermissionNode node) {
      Map<String, Boolean> perms = this.zPermissionsService.getPlayerPermissions((String)null, (Set)null, name);
      return (Boolean)perms.getOrDefault(node.getNode(), false);
   }

   public boolean removeFromGroup(OfflinePlayer player, UserGroup group) {
      return Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "permissions player " + player.getName() + " removegroup " + group.getGroupName());
   }

   public boolean setGroup(OfflinePlayer player, UserGroup group) {
      return Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "permissions player " + player.getName() + " setgroup " + group.getGroupName());
   }

   public Collection<UserGroup> getGroups(OfflinePlayer player) {
      return (Collection)this.zPermissionsService.getPlayerGroups(player.getName()).stream().map(UserGroup::new).collect(Collectors.toList());
   }

   public UserGroup getPrimaryGroup(OfflinePlayer player) {
      return new UserGroup(this.zPermissionsService.getPlayerPrimaryGroup(player.getName()));
   }

   public PermissionsSystemType getPermissionSystem() {
      return PermissionsSystemType.Z_PERMISSIONS;
   }
}
