package net.milkbowl.vault.chat.plugins;

import com.overmc.overpermissions.api.GroupManager;
import com.overmc.overpermissions.api.PermissionGroup;
import com.overmc.overpermissions.api.PermissionUser;
import com.overmc.overpermissions.api.UserManager;
import com.overmc.overpermissions.internal.OverPermissions;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public class Chat_OverPermissions extends Chat {
   protected final Plugin plugin;
   private OverPermissions overPerms;
   private UserManager userManager;
   private GroupManager groupManager;

   public Chat_OverPermissions(Plugin plugin, Permission perms) {
      super(perms);
      this.plugin = plugin;
      plugin.getServer().getPluginManager().registerEvents(new Chat_OverPermissions.PermissionServerListener(this), plugin);
      if (this.overPerms == null) {
         Plugin p = plugin.getServer().getPluginManager().getPlugin("OverPermissions");
         if (p != null) {
            this.overPerms = (OverPermissions)p;
            this.userManager = this.overPerms.getUserManager();
            this.groupManager = this.overPerms.getGroupManager();
            plugin.getLogger().info(String.format("[%s][Chat] %s hooked.", plugin.getDescription().getName(), "OverPermissions"));
         }
      }

   }

   public String getName() {
      return "OverPermissions_Chat";
   }

   public boolean isEnabled() {
      return this.overPerms != null;
   }

   public String getPlayerPrefix(String world, String player) {
      return this.getPlayerInfoString(world, player, "prefix", "");
   }

   public void setPlayerPrefix(String world, String player, String prefix) {
      this.setPlayerInfoString(world, player, "prefix", prefix);
   }

   public String getPlayerSuffix(String world, String player) {
      return this.getPlayerInfoString(world, player, "suffix", "");
   }

   public void setPlayerSuffix(String world, String player, String suffix) {
      this.setPlayerInfoString(world, player, "suffix", suffix);
   }

   public String getGroupPrefix(String world, String group) {
      return this.getGroupInfoString(world, group, "prefix", "");
   }

   public void setGroupPrefix(String world, String group, String prefix) {
      this.setGroupInfoString(world, group, "prefix", prefix);
   }

   public String getGroupSuffix(String world, String group) {
      return this.getGroupInfoString(world, group, "suffix", "");
   }

   public void setGroupSuffix(String world, String group, String suffix) {
      this.setGroupInfoString(world, group, "prefix", suffix);
   }

   public int getPlayerInfoInteger(String world, String player, String node, int defaultValue) {
      String s = this.getPlayerInfoString(world, player, node, (String)null);
      if (s == null) {
         return defaultValue;
      } else {
         try {
            return Integer.valueOf(s);
         } catch (NumberFormatException var7) {
            return defaultValue;
         }
      }
   }

   public void setPlayerInfoInteger(String world, String player, String node, int value) {
      this.setPlayerInfoString(world, player, node, String.valueOf(value));
   }

   public int getGroupInfoInteger(String world, String group, String node, int defaultValue) {
      String s = this.getGroupInfoString(world, group, node, (String)null);
      if (s == null) {
         return defaultValue;
      } else {
         try {
            return Integer.valueOf(s);
         } catch (NumberFormatException var7) {
            return defaultValue;
         }
      }
   }

   public void setGroupInfoInteger(String world, String group, String node, int value) {
      this.setGroupInfoString(world, group, node, String.valueOf(value));
   }

   public double getPlayerInfoDouble(String world, String player, String node, double defaultValue) {
      String s = this.getPlayerInfoString(world, player, node, (String)null);
      if (s == null) {
         return defaultValue;
      } else {
         try {
            return Double.valueOf(s);
         } catch (NumberFormatException var8) {
            return defaultValue;
         }
      }
   }

   public void setPlayerInfoDouble(String world, String player, String node, double value) {
      this.setPlayerInfoString(world, player, node, String.valueOf(value));
   }

   public double getGroupInfoDouble(String world, String group, String node, double defaultValue) {
      String s = this.getGroupInfoString(world, group, node, (String)null);
      if (s == null) {
         return defaultValue;
      } else {
         try {
            return Double.valueOf(s);
         } catch (NumberFormatException var8) {
            return defaultValue;
         }
      }
   }

   public void setGroupInfoDouble(String world, String group, String node, double value) {
      this.setGroupInfoString(world, group, node, String.valueOf(value));
   }

   public boolean getPlayerInfoBoolean(String world, String player, String node, boolean defaultValue) {
      String s = this.getPlayerInfoString(world, player, node, (String)null);
      if (s == null) {
         return defaultValue;
      } else {
         Boolean val = Boolean.valueOf(s);
         return val != null ? val : defaultValue;
      }
   }

   public void setPlayerInfoBoolean(String world, String player, String node, boolean value) {
      this.setPlayerInfoString(world, player, node, String.valueOf(value));
   }

   public boolean getGroupInfoBoolean(String world, String group, String node, boolean defaultValue) {
      String s = this.getGroupInfoString(world, group, node, (String)null);
      if (s == null) {
         return defaultValue;
      } else {
         Boolean val = Boolean.valueOf(s);
         return val != null ? val : defaultValue;
      }
   }

   public void setGroupInfoBoolean(String world, String group, String node, boolean value) {
      this.setGroupInfoString(world, group, node, String.valueOf(value));
   }

   public String getPlayerInfoString(String world, String playerName, String node, String defaultValue) {
      if (!this.userManager.doesUserExist(playerName)) {
         return defaultValue;
      } else {
         PermissionUser user = this.userManager.getPermissionUser(playerName);
         if (world == null) {
            return !user.hasGlobalMeta(node) ? defaultValue : user.getGlobalMeta(node);
         } else {
            return !user.hasMeta(node, world) ? defaultValue : user.getMeta(node, world);
         }
      }
   }

   public void setPlayerInfoString(String world, String playerName, String node, String value) {
      if (this.userManager.canUserExist(playerName)) {
         PermissionUser user = this.userManager.getPermissionUser(playerName);
         if (world != null) {
            if (value == null) {
               user.removeMeta(node, world);
            } else {
               user.setMeta(node, value, world);
            }
         } else if (value == null) {
            user.removeGlobalMeta(node);
         } else {
            user.setGlobalMeta(node, value);
         }

      }
   }

   public String getGroupInfoString(String world, String groupName, String node, String defaultValue) {
      if (!this.groupManager.doesGroupExist(groupName)) {
         return defaultValue;
      } else {
         PermissionGroup group = this.overPerms.getGroupManager().getGroup(groupName);
         if (world == null) {
            return !group.hasGlobalMeta(node) ? defaultValue : group.getGlobalMeta(node);
         } else {
            return !group.hasMeta(node, world) ? defaultValue : group.getMeta(node, world);
         }
      }
   }

   public void setGroupInfoString(String world, String groupName, String node, String value) {
      if (this.overPerms.getGroupManager().doesGroupExist(groupName)) {
         PermissionGroup group = this.overPerms.getGroupManager().getGroup(groupName);
         if (world != null) {
            if (value == null) {
               group.removeMeta(node, world);
            } else {
               group.setMeta(node, value, world);
            }
         } else if (value == null) {
            group.removeGlobalMeta(node);
         } else {
            group.setGlobalMeta(node, value);
         }

      }
   }

   public class PermissionServerListener implements Listener {
      Chat_OverPermissions chat = null;

      public PermissionServerListener(Chat_OverPermissions chat) {
         this.chat = chat;
      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginEnable(PluginEnableEvent event) {
         if (this.chat.overPerms == null) {
            Plugin chat = Chat_OverPermissions.this.plugin.getServer().getPluginManager().getPlugin("OverPermissions");
            if (chat != null) {
               this.chat.overPerms = (OverPermissions)chat;
               Chat_OverPermissions.this.plugin.getLogger().info(String.format("[%s][Chat] %s hooked.", Chat_OverPermissions.this.plugin.getDescription().getName(), Chat_OverPermissions.this.getName()));
            }
         }

      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginDisable(PluginDisableEvent event) {
         if (this.chat.overPerms != null && event.getPlugin().getDescription().getName().equals("OverPermissions")) {
            this.chat.overPerms = null;
            Chat_OverPermissions.this.plugin.getLogger().info(String.format("[%s][Chat] %s un-hooked.", Chat_OverPermissions.this.plugin.getDescription().getName(), Chat_OverPermissions.this.getName()));
         }

      }
   }
}
