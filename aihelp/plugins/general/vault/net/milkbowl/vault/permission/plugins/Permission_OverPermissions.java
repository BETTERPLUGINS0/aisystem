package net.milkbowl.vault.permission.plugins;

import com.overmc.overpermissions.api.GroupManager;
import com.overmc.overpermissions.api.PermissionGroup;
import com.overmc.overpermissions.api.PermissionUser;
import com.overmc.overpermissions.api.UserManager;
import com.overmc.overpermissions.internal.OverPermissions;
import java.util.ArrayList;
import java.util.Iterator;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public class Permission_OverPermissions extends Permission {
   private OverPermissions overPerms;
   private UserManager userManager;
   private GroupManager groupManager;

   public Permission_OverPermissions(Plugin plugin) {
      super.plugin = plugin;
      Bukkit.getServer().getPluginManager().registerEvents(new Permission_OverPermissions.PermissionServerListener(this), plugin);
      if (this.overPerms == null) {
         Plugin perms = plugin.getServer().getPluginManager().getPlugin("OverPermissions");
         if (perms != null && perms.isEnabled()) {
            this.overPerms = (OverPermissions)perms;
            this.userManager = this.overPerms.getUserManager();
            this.groupManager = this.overPerms.getGroupManager();
            log.info(String.format("[%s][Permission] %s hooked.", plugin.getDescription().getName(), "OverPermissions"));
         }
      }

   }

   public String getName() {
      return "OverPermissions";
   }

   public boolean isEnabled() {
      return this.overPerms != null && this.overPerms.isEnabled();
   }

   public boolean playerHas(String worldName, String playerName, String permission) {
      return !this.userManager.doesUserExist(playerName) ? false : this.userManager.getPermissionUser(playerName).getPermission(permission, worldName);
   }

   public boolean playerAdd(String worldName, String playerName, String permission) {
      return !this.userManager.canUserExist(playerName) ? false : this.userManager.getPermissionUser(playerName).addPermissionNode(permission, worldName);
   }

   public boolean playerRemove(String worldName, String playerName, String permission) {
      return !this.userManager.canUserExist(playerName) ? false : this.userManager.getPermissionUser(playerName).removePermissionNode(permission, worldName);
   }

   public boolean groupHas(String worldName, String groupName, String permission) {
      return !this.groupManager.doesGroupExist(groupName) ? false : this.groupManager.getGroup(groupName).getPermission(permission, worldName);
   }

   public boolean groupAdd(String worldName, String groupName, String permission) {
      if (!this.groupManager.doesGroupExist(groupName)) {
         return false;
      } else {
         return worldName == null ? this.groupManager.getGroup(groupName).addGlobalPermissionNode(permission) : this.groupManager.getGroup(groupName).addPermissionNode(permission, worldName);
      }
   }

   public boolean groupRemove(String worldName, String groupName, String permission) {
      if (!this.groupManager.doesGroupExist(groupName)) {
         return false;
      } else {
         return worldName == null ? this.groupManager.getGroup(groupName).removeGlobalPermissionNode(permission) : this.groupManager.getGroup(groupName).removePermissionNode(permission, worldName);
      }
   }

   public boolean playerInGroup(String worldName, String playerName, String groupName) {
      if (!this.groupManager.doesGroupExist(groupName)) {
         return false;
      } else {
         return !this.userManager.doesUserExist(playerName) ? false : this.userManager.getPermissionUser(playerName).getAllParents().contains(this.groupManager.getGroup(groupName));
      }
   }

   public boolean playerAddGroup(String worldName, String playerName, String groupName) {
      if (!this.groupManager.doesGroupExist(groupName)) {
         return false;
      } else {
         return !this.userManager.canUserExist(playerName) ? false : this.userManager.getPermissionUser(playerName).addParent(this.groupManager.getGroup(groupName));
      }
   }

   public boolean playerRemoveGroup(String worldName, String playerName, String groupName) {
      if (!this.groupManager.doesGroupExist(groupName)) {
         return false;
      } else {
         return !this.userManager.canUserExist(playerName) ? false : this.userManager.getPermissionUser(playerName).removeParent(this.groupManager.getGroup(groupName));
      }
   }

   public String[] getPlayerGroups(String worldName, String playerName) {
      ArrayList<String> ret = new ArrayList();
      if (!this.userManager.doesUserExist(playerName)) {
         return new String[0];
      } else {
         PermissionUser user = this.userManager.getPermissionUser(playerName);
         Iterator var5 = user.getAllParents().iterator();

         while(var5.hasNext()) {
            PermissionGroup parent = (PermissionGroup)var5.next();
            ret.add(parent.getName());
         }

         return (String[])ret.toArray(new String[ret.size()]);
      }
   }

   public String getPrimaryGroup(String worldName, String playerName) {
      String[] playerGroups = this.getPlayerGroups(worldName, playerName);
      return playerGroups.length == 0 ? null : playerGroups[0];
   }

   public String[] getGroups() {
      ArrayList<String> groupNames = new ArrayList();
      Iterator var2 = this.groupManager.getGroups().iterator();

      while(var2.hasNext()) {
         PermissionGroup s = (PermissionGroup)var2.next();
         groupNames.add(s.getName());
      }

      return (String[])groupNames.toArray(new String[groupNames.size()]);
   }

   public boolean hasSuperPermsCompat() {
      return true;
   }

   public boolean hasGroupSupport() {
      return true;
   }

   public class PermissionServerListener implements Listener {
      Permission_OverPermissions permission = null;

      public PermissionServerListener(Permission_OverPermissions permission) {
         this.permission = permission;
      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginEnable(PluginEnableEvent event) {
         if (this.permission.overPerms == null) {
            Plugin perms = Permission_OverPermissions.this.plugin.getServer().getPluginManager().getPlugin("OverPermissions");
            if (perms != null) {
               this.permission.overPerms = (OverPermissions)perms;
               Permission_OverPermissions.log.info(String.format("[%s][Permission] %s hooked.", Permission_OverPermissions.this.plugin.getDescription().getName(), "OverPermissions"));
            }
         }

      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginDisable(PluginDisableEvent event) {
         if (this.permission.overPerms != null && event.getPlugin().getDescription().getName().equals("OverPermissions")) {
            this.permission.overPerms = null;
            Permission_OverPermissions.log.info(String.format("[%s][Permission] %s un-hooked.", Permission_OverPermissions.this.plugin.getDescription().getName(), "OverPermissions"));
         }

      }
   }
}
