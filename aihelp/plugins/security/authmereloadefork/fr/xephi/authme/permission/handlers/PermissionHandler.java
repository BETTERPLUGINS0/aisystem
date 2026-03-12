package fr.xephi.authme.permission.handlers;

import fr.xephi.authme.data.limbo.UserGroup;
import fr.xephi.authme.permission.PermissionNode;
import fr.xephi.authme.permission.PermissionsSystemType;
import fr.xephi.authme.util.Utils;
import java.util.Collection;
import java.util.UUID;
import org.bukkit.OfflinePlayer;

public interface PermissionHandler {
   boolean addToGroup(OfflinePlayer var1, UserGroup var2);

   boolean hasGroupSupport();

   boolean hasPermissionOffline(String var1, PermissionNode var2);

   default boolean isInGroup(OfflinePlayer player, UserGroup group) {
      return this.getGroups(player).contains(group);
   }

   boolean removeFromGroup(OfflinePlayer var1, UserGroup var2);

   boolean setGroup(OfflinePlayer var1, UserGroup var2);

   Collection<UserGroup> getGroups(OfflinePlayer var1);

   default UserGroup getPrimaryGroup(OfflinePlayer player) {
      Collection<UserGroup> groups = this.getGroups(player);
      return Utils.isCollectionEmpty(groups) ? null : (UserGroup)groups.iterator().next();
   }

   PermissionsSystemType getPermissionSystem();

   default void loadUserData(UUID uuid) throws PermissionLoadUserException {
   }
}
