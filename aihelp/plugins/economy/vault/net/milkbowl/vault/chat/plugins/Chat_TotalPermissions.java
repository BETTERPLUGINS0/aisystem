package net.milkbowl.vault.chat.plugins;

import net.ae97.totalpermissions.PermissionManager;
import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.permission.PermissionBase;
import net.ae97.totalpermissions.permission.PermissionGroup;
import net.ae97.totalpermissions.permission.PermissionUser;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public class Chat_TotalPermissions extends Chat {
   private final Plugin plugin;
   private TotalPermissions totalPermissions;
   private final String name = "TotalPermissions-Chat";

   public Chat_TotalPermissions(Plugin plugin, Permission perms) {
      super(perms);
      this.plugin = plugin;
      Bukkit.getServer().getPluginManager().registerEvents(new Chat_TotalPermissions.PermissionServerListener(this), plugin);
      if (this.totalPermissions == null) {
         Plugin chat = plugin.getServer().getPluginManager().getPlugin("TotalPermissions");
         if (chat != null && chat.isEnabled()) {
            this.totalPermissions = (TotalPermissions)chat;
            plugin.getLogger().info(String.format("[Chat] %s hooked.", "TotalPermissions-Chat"));
         }
      }

   }

   public String getName() {
      return "TotalPermissions-Chat";
   }

   public boolean isEnabled() {
      return this.totalPermissions == null ? false : this.totalPermissions.isEnabled();
   }

   public String getPlayerPrefix(String world, String player) {
      return this.getPlayerInfoString(world, player, "prefix", (String)null);
   }

   public void setPlayerPrefix(String world, String player, String prefix) {
      this.setPlayerInfoString(world, player, "prefix", prefix);
   }

   public String getPlayerSuffix(String world, String player) {
      return this.getPlayerInfoString(world, player, "suffix", (String)null);
   }

   public void setPlayerSuffix(String world, String player, String suffix) {
      this.setPlayerInfoString(world, player, "suffix", suffix);
   }

   public String getGroupPrefix(String world, String group) {
      return this.getGroupInfoString(world, group, "prefix", (String)null);
   }

   public void setGroupPrefix(String world, String group, String prefix) {
      this.setGroupInfoString(world, group, "prefix", prefix);
   }

   public String getGroupSuffix(String world, String group) {
      return this.getGroupInfoString(world, group, "suffix", (String)null);
   }

   public void setGroupSuffix(String world, String group, String suffix) {
      this.setGroupInfoString(world, group, "suffix", suffix);
   }

   public int getPlayerInfoInteger(String world, String player, String node, int defaultValue) {
      Object pre = this.getPlayerInfo(world, player, node);
      return pre instanceof Integer ? (Integer)pre : defaultValue;
   }

   public void setPlayerInfoInteger(String world, String player, String node, int value) {
      this.setPlayerInfo(world, player, node, value);
   }

   public int getGroupInfoInteger(String world, String group, String node, int defaultValue) {
      Object pre = this.getGroupInfo(world, group, node);
      return pre instanceof Integer ? (Integer)pre : defaultValue;
   }

   public void setGroupInfoInteger(String world, String group, String node, int value) {
      this.setGroupInfo(world, group, node, value);
   }

   public double getPlayerInfoDouble(String world, String player, String node, double defaultValue) {
      Object pre = this.getPlayerInfo(world, player, node);
      return pre instanceof Double ? (Double)pre : defaultValue;
   }

   public void setPlayerInfoDouble(String world, String player, String node, double value) {
      this.setPlayerInfo(world, player, node, value);
   }

   public double getGroupInfoDouble(String world, String group, String node, double defaultValue) {
      Object pre = this.getGroupInfo(world, group, node);
      return pre instanceof Double ? (Double)pre : defaultValue;
   }

   public void setGroupInfoDouble(String world, String group, String node, double value) {
      this.setGroupInfo(world, group, node, value);
   }

   public boolean getPlayerInfoBoolean(String world, String player, String node, boolean defaultValue) {
      Object pre = this.getPlayerInfo(world, player, node);
      return pre instanceof Boolean ? (Boolean)pre : defaultValue;
   }

   public void setPlayerInfoBoolean(String world, String player, String node, boolean value) {
      this.setPlayerInfo(world, player, node, value);
   }

   public boolean getGroupInfoBoolean(String world, String group, String node, boolean defaultValue) {
      Object pre = this.getGroupInfo(world, group, node);
      return pre instanceof Boolean ? (Boolean)pre : defaultValue;
   }

   public void setGroupInfoBoolean(String world, String group, String node, boolean value) {
      this.setGroupInfo(world, group, node, value);
   }

   public String getPlayerInfoString(String world, String player, String node, String defaultValue) {
      Object pre = this.getPlayerInfo(world, player, node);
      return pre instanceof String ? (String)pre : defaultValue;
   }

   public void setPlayerInfoString(String world, String player, String node, String value) {
      this.setPlayerInfo(world, player, node, value);
   }

   public String getGroupInfoString(String world, String group, String node, String defaultValue) {
      Object pre = this.getGroupInfo(world, group, node);
      return pre instanceof String ? (String)pre : defaultValue;
   }

   public void setGroupInfoString(String world, String group, String node, String value) {
      this.setGroupInfo(world, group, node, value);
   }

   private PermissionUser getUser(String name) {
      PermissionManager manager = this.totalPermissions.getManager();
      PermissionUser user = manager.getUser(name);
      return user;
   }

   private PermissionGroup getGroup(String name) {
      PermissionManager manager = this.totalPermissions.getManager();
      PermissionGroup group = manager.getGroup(name);
      return group;
   }

   private void setPlayerInfo(String world, String player, String node, Object value) {
      PermissionBase base = this.getUser(player);
      base.setOption(node, value, world);
   }

   private void setGroupInfo(String world, String group, String node, Object value) {
      PermissionBase base = this.getGroup(group);
      base.setOption(node, value, world);
   }

   private Object getPlayerInfo(String world, String player, String node) {
      PermissionBase base = this.getUser(player);
      return base.getOption(node);
   }

   private Object getGroupInfo(String world, String group, String node) {
      PermissionBase base = this.getUser(group);
      return base.getOption(node);
   }

   public class PermissionServerListener implements Listener {
      Chat_TotalPermissions chat = null;

      public PermissionServerListener(Chat_TotalPermissions chat) {
         this.chat = chat;
      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginEnable(PluginEnableEvent event) {
         if (this.chat.totalPermissions == null) {
            Plugin perms = event.getPlugin();
            if (perms != null && perms.getDescription().getName().equals("TotalPermissions") && perms.isEnabled()) {
               this.chat.totalPermissions = (TotalPermissions)perms;
               Chat_TotalPermissions.this.plugin.getLogger().info(String.format("[Chat] %s hooked.", this.chat.getName()));
            }
         }

      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginDisable(PluginDisableEvent event) {
         if (this.chat.totalPermissions != null && event.getPlugin().getDescription().getName().equals("TotalPermissions")) {
            this.chat.totalPermissions = null;
            Chat_TotalPermissions.this.plugin.getLogger().info(String.format("[Chat] %s un-hooked.", "TotalPermissions-Chat"));
         }

      }
   }
}
