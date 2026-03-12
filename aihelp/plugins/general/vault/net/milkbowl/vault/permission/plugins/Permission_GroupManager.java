package net.milkbowl.vault.permission.plugins;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.milkbowl.vault.permission.Permission;
import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.data.Group;
import org.anjocaido.groupmanager.data.User;
import org.anjocaido.groupmanager.dataholder.OverloadedWorldHolder;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public class Permission_GroupManager extends Permission {
   private final String name = "GroupManager";
   private GroupManager groupManager;

   public Permission_GroupManager(Plugin plugin) {
      this.plugin = plugin;
      Bukkit.getServer().getPluginManager().registerEvents(new Permission_GroupManager.PermissionServerListener(this), plugin);
      if (this.groupManager == null) {
         Plugin perms = plugin.getServer().getPluginManager().getPlugin("GroupManager");
         if (perms != null && perms.isEnabled()) {
            this.groupManager = (GroupManager)perms;
            log.info(String.format("[%s][Permission] %s hooked.", plugin.getDescription().getName(), "GroupManager"));
         }
      }

   }

   public String getName() {
      this.getClass();
      return "GroupManager";
   }

   public boolean isEnabled() {
      return this.groupManager != null && this.groupManager.isEnabled();
   }

   public boolean playerHas(String worldName, String playerName, String permission) {
      AnjoPermissionsHandler handler;
      if (worldName == null) {
         handler = this.groupManager.getWorldsHolder().getWorldPermissionsByPlayerName(playerName);
      } else {
         handler = this.groupManager.getWorldsHolder().getWorldPermissions(worldName);
      }

      return handler == null ? false : handler.permission(playerName, permission);
   }

   public boolean playerAdd(String worldName, String playerName, String permission) {
      OverloadedWorldHolder owh;
      if (worldName == null) {
         owh = this.groupManager.getWorldsHolder().getWorldDataByPlayerName(playerName);
      } else {
         owh = this.groupManager.getWorldsHolder().getWorldData(worldName);
      }

      if (owh == null) {
         return false;
      } else {
         User user = owh.getUser(playerName);
         if (user == null) {
            return false;
         } else {
            user.addPermission(permission);
            Player p = Bukkit.getPlayer(playerName);
            if (p != null) {
               GroupManager.BukkitPermissions.updatePermissions(p);
            }

            return true;
         }
      }
   }

   public boolean playerRemove(String worldName, String playerName, String permission) {
      OverloadedWorldHolder owh;
      if (worldName == null) {
         owh = this.groupManager.getWorldsHolder().getWorldDataByPlayerName(playerName);
      } else {
         owh = this.groupManager.getWorldsHolder().getWorldData(worldName);
      }

      if (owh == null) {
         return false;
      } else {
         User user = owh.getUser(playerName);
         if (user == null) {
            return false;
         } else {
            user.removePermission(permission);
            Player p = Bukkit.getPlayer(playerName);
            if (p != null) {
               GroupManager.BukkitPermissions.updatePermissions(p);
            }

            return true;
         }
      }
   }

   public boolean groupHas(String worldName, String groupName, String permission) {
      OverloadedWorldHolder owh;
      if (worldName == null) {
         owh = this.groupManager.getWorldsHolder().getDefaultWorld();
      } else {
         owh = this.groupManager.getWorldsHolder().getWorldData(worldName);
      }

      if (owh == null) {
         return false;
      } else {
         Group group = owh.getGroup(groupName);
         return group == null ? false : group.hasSamePermissionNode(permission);
      }
   }

   public boolean groupAdd(String worldName, String groupName, String permission) {
      OverloadedWorldHolder owh;
      if (worldName == null) {
         owh = this.groupManager.getWorldsHolder().getDefaultWorld();
      } else {
         owh = this.groupManager.getWorldsHolder().getWorldData(worldName);
      }

      if (owh == null) {
         return false;
      } else {
         Group group = owh.getGroup(groupName);
         if (group == null) {
            return false;
         } else {
            group.addPermission(permission);
            return true;
         }
      }
   }

   public boolean groupRemove(String worldName, String groupName, String permission) {
      OverloadedWorldHolder owh;
      if (worldName == null) {
         owh = this.groupManager.getWorldsHolder().getDefaultWorld();
      } else {
         owh = this.groupManager.getWorldsHolder().getWorldData(worldName);
      }

      if (owh == null) {
         return false;
      } else {
         Group group = owh.getGroup(groupName);
         if (group == null) {
            return false;
         } else {
            group.removePermission(permission);
            return true;
         }
      }
   }

   public boolean playerInGroup(String worldName, String playerName, String groupName) {
      AnjoPermissionsHandler handler;
      if (worldName == null) {
         handler = this.groupManager.getWorldsHolder().getWorldPermissionsByPlayerName(playerName);
      } else {
         handler = this.groupManager.getWorldsHolder().getWorldPermissions(worldName);
      }

      return handler == null ? false : handler.inGroup(playerName, groupName);
   }

   public boolean playerAddGroup(String worldName, String playerName, String groupName) {
      OverloadedWorldHolder owh;
      if (worldName == null) {
         owh = this.groupManager.getWorldsHolder().getWorldDataByPlayerName(playerName);
      } else {
         owh = this.groupManager.getWorldsHolder().getWorldData(worldName);
      }

      if (owh == null) {
         return false;
      } else {
         User user = owh.getUser(playerName);
         if (user == null) {
            return false;
         } else {
            Group group = owh.getGroup(groupName);
            if (group == null) {
               return false;
            } else {
               if (user.getGroup().equals(owh.getDefaultGroup())) {
                  user.setGroup(group);
               } else if (group.getInherits().contains(user.getGroup().getName().toLowerCase())) {
                  user.setGroup(group);
               } else {
                  user.addSubGroup(group);
               }

               Player p = Bukkit.getPlayer(playerName);
               if (p != null) {
                  GroupManager.BukkitPermissions.updatePermissions(p);
               }

               return true;
            }
         }
      }
   }

   public boolean playerRemoveGroup(String worldName, String playerName, String groupName) {
      OverloadedWorldHolder owh;
      if (worldName == null) {
         owh = this.groupManager.getWorldsHolder().getWorldDataByPlayerName(playerName);
      } else {
         owh = this.groupManager.getWorldsHolder().getWorldData(worldName);
      }

      if (owh == null) {
         return false;
      } else {
         User user = owh.getUser(playerName);
         if (user == null) {
            return false;
         } else {
            boolean success = false;
            if (user.getGroup().getName().equalsIgnoreCase(groupName)) {
               user.setGroup(owh.getDefaultGroup());
               success = true;
            } else {
               Group group = owh.getGroup(groupName);
               if (group != null) {
                  success = user.removeSubGroup(group);
               }
            }

            if (success) {
               Player p = Bukkit.getPlayer(playerName);
               if (p != null) {
                  GroupManager.BukkitPermissions.updatePermissions(p);
               }
            }

            return success;
         }
      }
   }

   public String[] getPlayerGroups(String worldName, String playerName) {
      AnjoPermissionsHandler handler;
      if (worldName == null) {
         handler = this.groupManager.getWorldsHolder().getWorldPermissionsByPlayerName(playerName);
      } else {
         handler = this.groupManager.getWorldsHolder().getWorldPermissions(worldName);
      }

      return handler == null ? null : handler.getGroups(playerName);
   }

   public String getPrimaryGroup(String worldName, String playerName) {
      AnjoPermissionsHandler handler;
      if (worldName == null) {
         handler = this.groupManager.getWorldsHolder().getWorldPermissionsByPlayerName(playerName);
      } else {
         handler = this.groupManager.getWorldsHolder().getWorldPermissions(worldName);
      }

      return handler == null ? null : handler.getGroup(playerName);
   }

   public String[] getGroups() {
      Set<String> groupNames = new HashSet();
      Iterator var2 = Bukkit.getServer().getWorlds().iterator();

      while(true) {
         Collection groups;
         do {
            OverloadedWorldHolder owh;
            do {
               if (!var2.hasNext()) {
                  return (String[])groupNames.toArray(new String[0]);
               }

               World world = (World)var2.next();
               owh = this.groupManager.getWorldsHolder().getWorldData(world.getName());
            } while(owh == null);

            groups = owh.getGroupList();
         } while(groups == null);

         Iterator var6 = groups.iterator();

         while(var6.hasNext()) {
            Group group = (Group)var6.next();
            groupNames.add(group.getName());
         }
      }
   }

   public boolean hasSuperPermsCompat() {
      return true;
   }

   public boolean hasGroupSupport() {
      return true;
   }

   public class PermissionServerListener implements Listener {
      Permission_GroupManager permission = null;

      public PermissionServerListener(Permission_GroupManager permission) {
         this.permission = permission;
      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginEnable(PluginEnableEvent event) {
         if (this.permission.groupManager == null) {
            Plugin p = event.getPlugin();
            if (p.getDescription().getName().equals("GroupManager")) {
               this.permission.groupManager = (GroupManager)p;
               Permission_GroupManager.log.info(String.format("[%s][Permission] %s hooked.", Permission_GroupManager.this.plugin.getDescription().getName(), "GroupManager"));
            }
         }

      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginDisable(PluginDisableEvent event) {
         if (this.permission.groupManager != null && event.getPlugin().getDescription().getName().equals("GroupManager")) {
            this.permission.groupManager = null;
            Permission_GroupManager.log.info(String.format("[%s][Permission] %s un-hooked.", Permission_GroupManager.this.plugin.getDescription().getName(), "GroupManager"));
         }

      }
   }
}
