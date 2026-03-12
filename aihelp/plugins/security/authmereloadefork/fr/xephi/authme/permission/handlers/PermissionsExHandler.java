package fr.xephi.authme.permission.handlers;

import fr.xephi.authme.data.limbo.UserGroup;
import fr.xephi.authme.permission.PermissionNode;
import fr.xephi.authme.permission.PermissionsSystemType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bukkit.OfflinePlayer;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PermissionsExHandler implements PermissionHandler {
   private PermissionManager permissionManager = PermissionsEx.getPermissionManager();

   public PermissionsExHandler() throws PermissionHandlerException {
      if (this.permissionManager == null) {
         throw new PermissionHandlerException("Could not get manager of PermissionsEx");
      }
   }

   public boolean addToGroup(OfflinePlayer player, UserGroup group) {
      if (!PermissionsEx.getPermissionManager().getGroupNames().contains(group)) {
         return false;
      } else {
         PermissionUser user = PermissionsEx.getUser(player.getName());
         user.addGroup(group.getGroupName());
         return true;
      }
   }

   public boolean hasGroupSupport() {
      return true;
   }

   public boolean hasPermissionOffline(String name, PermissionNode node) {
      PermissionUser user = this.permissionManager.getUser(name);
      return user.has(node.getNode());
   }

   public boolean isInGroup(OfflinePlayer player, UserGroup group) {
      PermissionUser user = this.permissionManager.getUser(player.getName());
      return user.inGroup(group.getGroupName());
   }

   public boolean removeFromGroup(OfflinePlayer player, UserGroup group) {
      PermissionUser user = this.permissionManager.getUser(player.getName());
      user.removeGroup(group.getGroupName());
      return true;
   }

   public boolean setGroup(OfflinePlayer player, UserGroup group) {
      List<String> groups = new ArrayList();
      groups.add(group.getGroupName());
      PermissionUser user = this.permissionManager.getUser(player.getName());
      user.setParentsIdentifier(groups);
      return true;
   }

   public List<UserGroup> getGroups(OfflinePlayer player) {
      PermissionUser user = this.permissionManager.getUser(player.getName());
      return (List)user.getParentIdentifiers((String)null).stream().map((i) -> {
         return new UserGroup(i, (Map)null);
      }).collect(Collectors.toList());
   }

   public PermissionsSystemType getPermissionSystem() {
      return PermissionsSystemType.PERMISSIONS_EX;
   }
}
