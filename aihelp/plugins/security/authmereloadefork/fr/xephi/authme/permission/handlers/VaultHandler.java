package fr.xephi.authme.permission.handlers;

import fr.xephi.authme.data.limbo.UserGroup;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.permission.PermissionNode;
import fr.xephi.authme.permission.PermissionsSystemType;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHandler implements PermissionHandler {
   private Permission vaultProvider;

   public VaultHandler(Server server) throws PermissionHandlerException {
      this.vaultProvider = this.getVaultPermission(server);
   }

   @VisibleForTesting
   Permission getVaultPermission(Server server) throws PermissionHandlerException {
      RegisteredServiceProvider<Permission> permissionProvider = server.getServicesManager().getRegistration(Permission.class);
      if (permissionProvider == null) {
         throw new PermissionHandlerException("Could not load permissions provider service");
      } else {
         Permission vaultPerms = (Permission)permissionProvider.getProvider();
         if (vaultPerms == null) {
            throw new PermissionHandlerException("Could not load Vault permissions provider");
         } else {
            return vaultPerms;
         }
      }
   }

   public boolean addToGroup(OfflinePlayer player, UserGroup group) {
      return this.vaultProvider.playerAddGroup((String)null, player, group.getGroupName());
   }

   public boolean hasGroupSupport() {
      return this.vaultProvider.hasGroupSupport();
   }

   public boolean hasPermissionOffline(String name, PermissionNode node) {
      return this.vaultProvider.has((String)null, name, node.getNode());
   }

   public boolean isInGroup(OfflinePlayer player, UserGroup group) {
      return this.vaultProvider.playerInGroup((String)null, player, group.getGroupName());
   }

   public boolean removeFromGroup(OfflinePlayer player, UserGroup group) {
      return this.vaultProvider.playerRemoveGroup((String)null, player, group.getGroupName());
   }

   public boolean setGroup(OfflinePlayer player, UserGroup group) {
      Iterator var3 = this.getGroups(player).iterator();

      while(var3.hasNext()) {
         UserGroup g = (UserGroup)var3.next();
         this.removeFromGroup(player, g);
      }

      return this.vaultProvider.playerAddGroup((String)null, player, group.getGroupName());
   }

   public List<UserGroup> getGroups(OfflinePlayer player) {
      String[] groups = this.vaultProvider.getPlayerGroups((String)null, player);
      return groups == null ? Collections.emptyList() : (List)Arrays.stream(groups).map(UserGroup::new).collect(Collectors.toList());
   }

   public UserGroup getPrimaryGroup(OfflinePlayer player) {
      return new UserGroup(this.vaultProvider.getPrimaryGroup((String)null, player));
   }

   public PermissionsSystemType getPermissionSystem() {
      return PermissionsSystemType.VAULT;
   }
}
