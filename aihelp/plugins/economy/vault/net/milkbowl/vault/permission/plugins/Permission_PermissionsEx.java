package net.milkbowl.vault.permission.plugins;

import java.util.List;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Permission_PermissionsEx extends Permission {
   private final String name = "PermissionsEx";
   private PermissionsEx permission = null;

   public Permission_PermissionsEx(Plugin plugin) {
      this.plugin = plugin;
      Bukkit.getServer().getPluginManager().registerEvents(new Permission_PermissionsEx.PermissionServerListener(this), plugin);
      if (this.permission == null) {
         Plugin perms = plugin.getServer().getPluginManager().getPlugin("PermissionsEx");
         if (perms != null && perms.isEnabled()) {
            try {
               if (Double.valueOf(perms.getDescription().getVersion()) < 1.16D) {
                  log.info(String.format("[%s][Permission] %s below 1.16 is not compatible with Vault! Falling back to SuperPerms only mode. PLEASE UPDATE!", plugin.getDescription().getName(), "PermissionsEx"));
               }
            } catch (NumberFormatException var4) {
            }

            this.permission = (PermissionsEx)perms;
            log.info(String.format("[%s][Permission] %s hooked.", plugin.getDescription().getName(), "PermissionsEx"));
         }
      }

   }

   public boolean isEnabled() {
      return this.permission == null ? false : this.permission.isEnabled();
   }

   public String getName() {
      return "PermissionsEx";
   }

   public boolean playerInGroup(String worldName, OfflinePlayer op, String groupName) {
      PermissionUser user = this.getUser(op);
      return user == null ? false : user.inGroup(groupName, worldName);
   }

   public boolean playerInGroup(String worldName, String playerName, String groupName) {
      return PermissionsEx.getPermissionManager().getUser(playerName).inGroup(groupName, worldName);
   }

   public boolean playerAddGroup(String worldName, OfflinePlayer op, String groupName) {
      PermissionGroup group = PermissionsEx.getPermissionManager().getGroup(groupName);
      PermissionUser user = this.getUser(op);
      if (group != null && user != null) {
         user.addGroup(groupName, worldName);
         return true;
      } else {
         return false;
      }
   }

   public boolean playerAddGroup(String worldName, String playerName, String groupName) {
      PermissionGroup group = PermissionsEx.getPermissionManager().getGroup(groupName);
      PermissionUser user = PermissionsEx.getPermissionManager().getUser(playerName);
      if (group != null && user != null) {
         user.addGroup(groupName, worldName);
         return true;
      } else {
         return false;
      }
   }

   public boolean playerRemoveGroup(String worldName, OfflinePlayer op, String groupName) {
      PermissionUser user = this.getUser(op);
      user.removeGroup(groupName, worldName);
      return true;
   }

   public boolean playerRemoveGroup(String worldName, String playerName, String groupName) {
      PermissionsEx.getPermissionManager().getUser(playerName).removeGroup(groupName, worldName);
      return true;
   }

   public boolean playerAdd(String worldName, OfflinePlayer op, String permission) {
      PermissionUser user = this.getUser(op);
      if (user == null) {
         return false;
      } else {
         user.addPermission(permission, worldName);
         return true;
      }
   }

   public boolean playerAdd(String worldName, String playerName, String permission) {
      PermissionUser user = this.getUser(playerName);
      if (user == null) {
         return false;
      } else {
         user.addPermission(permission, worldName);
         return true;
      }
   }

   public boolean playerRemove(String worldName, OfflinePlayer op, String permission) {
      PermissionUser user = this.getUser(op);
      if (user == null) {
         return false;
      } else {
         user.removePermission(permission, worldName);
         return true;
      }
   }

   public boolean playerRemove(String worldName, String playerName, String permission) {
      PermissionUser user = this.getUser(playerName);
      if (user == null) {
         return false;
      } else {
         user.removePermission(permission, worldName);
         return true;
      }
   }

   public boolean groupAdd(String worldName, String groupName, String permission) {
      PermissionGroup group = PermissionsEx.getPermissionManager().getGroup(groupName);
      if (group == null) {
         return false;
      } else {
         group.addPermission(permission, worldName);
         return true;
      }
   }

   public boolean groupRemove(String worldName, String groupName, String permission) {
      PermissionGroup group = PermissionsEx.getPermissionManager().getGroup(groupName);
      if (group == null) {
         return false;
      } else {
         group.removePermission(permission, worldName);
         return true;
      }
   }

   public boolean groupHas(String worldName, String groupName, String permission) {
      PermissionGroup group = PermissionsEx.getPermissionManager().getGroup(groupName);
      return group == null ? false : group.has(permission, worldName);
   }

   private PermissionUser getUser(OfflinePlayer op) {
      return PermissionsEx.getPermissionManager().getUser(op.getUniqueId());
   }

   private PermissionUser getUser(String playerName) {
      return PermissionsEx.getPermissionManager().getUser(playerName);
   }

   public String[] getPlayerGroups(String world, OfflinePlayer op) {
      PermissionUser user = this.getUser(op);
      return user == null ? null : (String[])user.getParentIdentifiers(world).toArray(new String[0]);
   }

   public String[] getPlayerGroups(String world, String playerName) {
      PermissionUser user = this.getUser(playerName);
      return user == null ? null : (String[])user.getParentIdentifiers(world).toArray(new String[0]);
   }

   public String getPrimaryGroup(String world, OfflinePlayer op) {
      PermissionUser user = this.getUser(op);
      if (user == null) {
         return null;
      } else {
         return user.getParentIdentifiers(world).size() > 0 ? (String)user.getParentIdentifiers(world).get(0) : null;
      }
   }

   public String getPrimaryGroup(String world, String playerName) {
      PermissionUser user = PermissionsEx.getPermissionManager().getUser(playerName);
      if (user == null) {
         return null;
      } else {
         return user.getParentIdentifiers(world).size() > 0 ? (String)user.getParentIdentifiers(world).get(0) : null;
      }
   }

   public boolean playerHas(String worldName, OfflinePlayer op, String permission) {
      PermissionUser user = this.getUser(op);
      return user != null ? user.has(permission, worldName) : false;
   }

   public boolean playerHas(String worldName, String playerName, String permission) {
      PermissionUser user = this.getUser(playerName);
      return user != null ? user.has(permission, worldName) : false;
   }

   public boolean playerAddTransient(String worldName, Player player, String permission) {
      PermissionUser pPlayer = this.getUser((OfflinePlayer)player);
      if (pPlayer != null) {
         pPlayer.addTimedPermission(permission, worldName, 0);
         return true;
      } else {
         return false;
      }
   }

   public boolean playerAddTransient(Player player, String permission) {
      return this.playerAddTransient((String)null, player, permission);
   }

   public boolean playerRemoveTransient(Player player, String permission) {
      return this.playerRemoveTransient((String)null, player, permission);
   }

   public boolean playerRemoveTransient(String worldName, Player player, String permission) {
      PermissionUser pPlayer = this.getUser((OfflinePlayer)player);
      if (pPlayer != null) {
         pPlayer.removeTimedPermission(permission, worldName);
         return true;
      } else {
         return false;
      }
   }

   public String[] getGroups() {
      List<PermissionGroup> groups = PermissionsEx.getPermissionManager().getGroupList();
      if (groups != null && !groups.isEmpty()) {
         String[] groupNames = new String[groups.size()];

         for(int i = 0; i < groups.size(); ++i) {
            groupNames[i] = ((PermissionGroup)groups.get(i)).getName();
         }

         return groupNames;
      } else {
         return null;
      }
   }

   public boolean hasSuperPermsCompat() {
      return true;
   }

   public boolean hasGroupSupport() {
      return true;
   }

   public class PermissionServerListener implements Listener {
      Permission_PermissionsEx permission = null;

      public PermissionServerListener(Permission_PermissionsEx permission) {
         this.permission = permission;
      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginEnable(PluginEnableEvent event) {
         if (this.permission.permission == null) {
            Plugin perms = event.getPlugin();
            if (perms.getDescription().getName().equals("PermissionsEx")) {
               try {
                  if (Double.valueOf(perms.getDescription().getVersion()) < 1.16D) {
                     Permission_PermissionsEx.log.info(String.format("[%s][Permission] %s below 1.16 is not compatible with Vault! Falling back to SuperPerms only mode. PLEASE UPDATE!", Permission_PermissionsEx.this.plugin.getDescription().getName(), "PermissionsEx"));
                     return;
                  }
               } catch (NumberFormatException var4) {
               }

               this.permission.permission = (PermissionsEx)perms;
               Permission_PermissionsEx.log.info(String.format("[%s][Permission] %s hooked.", Permission_PermissionsEx.this.plugin.getDescription().getName(), "PermissionsEx"));
            }
         }

      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginDisable(PluginDisableEvent event) {
         if (this.permission.permission != null && event.getPlugin().getDescription().getName().equals("PermissionsEx")) {
            this.permission.permission = null;
            Permission_PermissionsEx.log.info(String.format("[%s][Permission] %s un-hooked.", Permission_PermissionsEx.this.plugin.getDescription().getName(), "PermissionsEx"));
         }

      }
   }
}
