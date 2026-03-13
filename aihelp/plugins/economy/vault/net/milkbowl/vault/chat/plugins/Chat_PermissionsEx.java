package net.milkbowl.vault.chat.plugins;

import java.util.logging.Logger;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Chat_PermissionsEx extends Chat {
   private final Logger log;
   private final String name = "PermissionsEx_Chat";
   private Plugin plugin = null;
   private PermissionsEx chat = null;

   public Chat_PermissionsEx(Plugin plugin, Permission perms) {
      super(perms);
      this.plugin = plugin;
      this.log = plugin.getLogger();
      Bukkit.getServer().getPluginManager().registerEvents(new Chat_PermissionsEx.PermissionServerListener(this), plugin);
      if (this.chat == null) {
         Plugin p = plugin.getServer().getPluginManager().getPlugin("PermissionsEx");
         if (p != null && p.isEnabled()) {
            this.chat = (PermissionsEx)p;
            this.log.info(String.format("[%s][Chat] %s hooked.", plugin.getDescription().getName(), "PermissionsEx_Chat"));
         }
      }

   }

   public String getName() {
      return "PermissionsEx_Chat";
   }

   public boolean isEnabled() {
      return this.chat == null ? false : this.chat.isEnabled();
   }

   private PermissionUser getUser(OfflinePlayer op) {
      return PermissionsEx.getPermissionManager().getUser(op.getUniqueId());
   }

   private PermissionUser getUser(String playerName) {
      return PermissionsEx.getPermissionManager().getUser(playerName);
   }

   public int getPlayerInfoInteger(String world, String playerName, String node, int defaultValue) {
      return this.getUser(playerName).getOptionInteger(node, world, defaultValue);
   }

   public double getPlayerInfoDouble(String world, String playerName, String node, double defaultValue) {
      return this.getUser(playerName).getOptionDouble(node, world, defaultValue);
   }

   public boolean getPlayerInfoBoolean(String world, String playerName, String node, boolean defaultValue) {
      return this.getUser(playerName).getOptionBoolean(node, world, defaultValue);
   }

   public String getPlayerInfoString(String world, String playerName, String node, String defaultValue) {
      return this.getUser(playerName).getOption(node, world, defaultValue);
   }

   public int getPlayerInfoInteger(String world, OfflinePlayer op, String node, int defaultValue) {
      return this.getUser(op).getOptionInteger(node, world, defaultValue);
   }

   public double getPlayerInfoDouble(String world, OfflinePlayer op, String node, double defaultValue) {
      return this.getUser(op).getOptionDouble(node, world, defaultValue);
   }

   public boolean getPlayerInfoBoolean(String world, OfflinePlayer op, String node, boolean defaultValue) {
      return this.getUser(op).getOptionBoolean(node, world, defaultValue);
   }

   public String getPlayerInfoString(String world, OfflinePlayer op, String node, String defaultValue) {
      return this.getUser(op).getOption(node, world, defaultValue);
   }

   public void setPlayerInfoInteger(String world, OfflinePlayer op, String node, int value) {
      PermissionUser user = this.getUser(op);
      if (user != null) {
         user.setOption(node, String.valueOf(value), world);
      }

   }

   public void setPlayerInfoDouble(String world, OfflinePlayer op, String node, double value) {
      PermissionUser user = this.getUser(op);
      if (user != null) {
         user.setOption(node, String.valueOf(value), world);
      }

   }

   public void setPlayerInfoBoolean(String world, OfflinePlayer op, String node, boolean value) {
      PermissionUser user = this.getUser(op);
      if (user != null) {
         user.setOption(node, String.valueOf(value), world);
      }

   }

   public void setPlayerInfoString(String world, OfflinePlayer op, String node, String value) {
      PermissionUser user = this.getUser(op);
      if (user != null) {
         user.setOption(node, String.valueOf(value), world);
      }

   }

   public void setPlayerInfoInteger(String world, String playerName, String node, int value) {
      PermissionUser user = this.getUser(playerName);
      if (user != null) {
         user.setOption(node, String.valueOf(value), world);
      }

   }

   public void setPlayerInfoDouble(String world, String playerName, String node, double value) {
      PermissionUser user = this.getUser(playerName);
      if (user != null) {
         user.setOption(node, String.valueOf(value), world);
      }

   }

   public void setPlayerInfoBoolean(String world, String playerName, String node, boolean value) {
      PermissionUser user = this.getUser(playerName);
      if (user != null) {
         user.setOption(node, String.valueOf(value), world);
      }

   }

   public void setPlayerInfoString(String world, String playerName, String node, String value) {
      PermissionUser user = this.getUser(playerName);
      if (user != null) {
         user.setOption(node, String.valueOf(value), world);
      }

   }

   public int getGroupInfoInteger(String world, String groupName, String node, int defaultValue) {
      PermissionGroup group = PermissionsEx.getPermissionManager().getGroup(groupName);
      return group == null ? defaultValue : group.getOptionInteger(node, world, defaultValue);
   }

   public void setGroupInfoInteger(String world, String groupName, String node, int value) {
      PermissionGroup group = PermissionsEx.getPermissionManager().getGroup(groupName);
      if (group != null) {
         group.setOption(node, world, String.valueOf(value));
      }
   }

   public double getGroupInfoDouble(String world, String groupName, String node, double defaultValue) {
      PermissionGroup group = PermissionsEx.getPermissionManager().getGroup(groupName);
      return group == null ? defaultValue : group.getOptionDouble(node, world, defaultValue);
   }

   public void setGroupInfoDouble(String world, String groupName, String node, double value) {
      PermissionGroup group = PermissionsEx.getPermissionManager().getGroup(groupName);
      if (group != null) {
         group.setOption(node, world, String.valueOf(value));
      }
   }

   public boolean getGroupInfoBoolean(String world, String groupName, String node, boolean defaultValue) {
      PermissionGroup group = PermissionsEx.getPermissionManager().getGroup(groupName);
      return group == null ? defaultValue : group.getOptionBoolean(node, world, defaultValue);
   }

   public void setGroupInfoBoolean(String world, String groupName, String node, boolean value) {
      PermissionGroup group = PermissionsEx.getPermissionManager().getGroup(groupName);
      if (group != null) {
         group.setOption(node, world, String.valueOf(value));
      }
   }

   public String getGroupInfoString(String world, String groupName, String node, String defaultValue) {
      PermissionGroup group = PermissionsEx.getPermissionManager().getGroup(groupName);
      return group == null ? defaultValue : group.getOption(node, world, defaultValue);
   }

   public void setGroupInfoString(String world, String groupName, String node, String value) {
      PermissionGroup group = PermissionsEx.getPermissionManager().getGroup(groupName);
      if (group != null) {
         group.setOption(node, world, value);
      }
   }

   public String getPlayerPrefix(String world, OfflinePlayer op) {
      PermissionUser user = this.getUser(op);
      return user != null ? user.getPrefix(world) : null;
   }

   public String getPlayerSuffix(String world, OfflinePlayer op) {
      PermissionUser user = this.getUser(op);
      return user != null ? user.getSuffix(world) : null;
   }

   public void setPlayerSuffix(String world, OfflinePlayer player, String suffix) {
      PermissionUser user = this.getUser(player);
      if (user != null) {
         user.setSuffix(suffix, world);
      }

   }

   public void setPlayerPrefix(String world, OfflinePlayer player, String prefix) {
      PermissionUser user = this.getUser(player);
      if (user != null) {
         user.setPrefix(prefix, world);
      }

   }

   public String getPlayerPrefix(String world, String playerName) {
      PermissionUser user = this.getUser(playerName);
      return user != null ? user.getPrefix(world) : null;
   }

   public String getPlayerSuffix(String world, String playerName) {
      PermissionUser user = this.getUser(playerName);
      return user != null ? user.getSuffix(world) : null;
   }

   public void setPlayerSuffix(String world, String player, String suffix) {
      PermissionUser user = this.getUser(player);
      if (user != null) {
         user.setSuffix(suffix, world);
      }

   }

   public void setPlayerPrefix(String world, String player, String prefix) {
      PermissionUser user = this.getUser(player);
      if (user != null) {
         user.setPrefix(prefix, world);
      }

   }

   public String getGroupPrefix(String world, String group) {
      PermissionGroup pGroup = PermissionsEx.getPermissionManager().getGroup(group);
      return group != null ? pGroup.getPrefix(world) : null;
   }

   public void setGroupPrefix(String world, String group, String prefix) {
      PermissionGroup pGroup = PermissionsEx.getPermissionManager().getGroup(group);
      if (group != null) {
         pGroup.setPrefix(prefix, world);
      }

   }

   public String getGroupSuffix(String world, String group) {
      PermissionGroup pGroup = PermissionsEx.getPermissionManager().getGroup(group);
      return group != null ? pGroup.getSuffix(world) : null;
   }

   public void setGroupSuffix(String world, String group, String suffix) {
      PermissionGroup pGroup = PermissionsEx.getPermissionManager().getGroup(group);
      if (group != null) {
         pGroup.setSuffix(suffix, world);
      }

   }

   public class PermissionServerListener implements Listener {
      Chat_PermissionsEx chat = null;

      public PermissionServerListener(Chat_PermissionsEx chat) {
         this.chat = chat;
      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginEnable(PluginEnableEvent event) {
         if (this.chat.chat == null) {
            Plugin perms = event.getPlugin();
            if (perms.getDescription().getName().equals("PermissionsEx") && perms.isEnabled()) {
               this.chat.chat = (PermissionsEx)perms;
               Chat_PermissionsEx.this.log.info(String.format("[%s][Chat] %s hooked.", Chat_PermissionsEx.this.plugin.getDescription().getName(), "PermissionsEx_Chat"));
            }
         }

      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginDisable(PluginDisableEvent event) {
         if (this.chat.chat != null && event.getPlugin().getDescription().getName().equals("PermissionsEx")) {
            this.chat.chat = null;
            Chat_PermissionsEx.this.log.info(String.format("[%s][Chat] %s un-hooked.", Chat_PermissionsEx.this.plugin.getDescription().getName(), "PermissionsEx_Chat"));
         }

      }
   }
}
