package net.milkbowl.vault.permission.plugins;

import com.lightniinja.kperms.KGroup;
import com.lightniinja.kperms.KPermsPlugin;
import com.lightniinja.kperms.KPlayer;
import com.lightniinja.kperms.Utilities;
import java.util.List;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public class Permission_KPerms extends Permission {
   private final Plugin vault;
   private KPermsPlugin kperms = null;

   public Permission_KPerms(Plugin plugin) {
      this.vault = plugin;
      Bukkit.getServer().getPluginManager().registerEvents(new Permission_KPerms.PermissionServerListener(this), this.vault);
      if (this.kperms == null) {
         Plugin perms = plugin.getServer().getPluginManager().getPlugin("KPerms");
         if (perms != null && perms.isEnabled()) {
            this.kperms = (KPermsPlugin)perms;
            plugin.getLogger().info(String.format("[%s][Permission] %s hooked.", plugin.getDescription().getName(), "KPerms"));
         }
      }

   }

   public String getName() {
      return "KPerms";
   }

   public boolean isEnabled() {
      return this.kperms.isEnabled();
   }

   public boolean hasSuperPermsCompat() {
      return true;
   }

   public boolean hasGroupSupport() {
      return true;
   }

   public boolean playerHas(String world, String player, String permission) {
      return (new KPlayer(player, this.kperms)).hasPermission(permission);
   }

   public boolean playerAdd(String world, String player, String permission) {
      return (new KPlayer(player, this.kperms)).addPermission(permission);
   }

   public boolean playerRemove(String world, String player, String permission) {
      return (new KPlayer(player, this.kperms)).removePermission(permission);
   }

   public boolean groupHas(String world, String group, String permission) {
      return (new KGroup(group, this.kperms)).hasPermission(permission);
   }

   public boolean groupAdd(String world, String group, String permission) {
      return (new KGroup(group, this.kperms)).addPermission(permission);
   }

   public boolean groupRemove(String world, String group, String permission) {
      return (new KGroup(group, this.kperms)).removePermission(permission);
   }

   public boolean playerInGroup(String world, String player, String group) {
      return (new KPlayer(player, this.kperms)).isMemberOfGroup(group);
   }

   public boolean playerAddGroup(String world, String player, String group) {
      return (new KPlayer(player, this.kperms)).addGroup(group);
   }

   public boolean playerRemoveGroup(String world, String player, String group) {
      return (new KPlayer(player, this.kperms)).removeGroup(group);
   }

   public String[] getPlayerGroups(String world, String player) {
      List<String> groups = (new KPlayer(player, this.kperms)).getGroups();
      String[] gr = new String[groups.size()];
      gr = (String[])groups.toArray(gr);
      return gr;
   }

   public String getPrimaryGroup(String world, String player) {
      return (new KPlayer(player, this.kperms)).getPrimaryGroup();
   }

   public String[] getGroups() {
      return (new Utilities(this.kperms)).getGroups();
   }

   private class PermissionServerListener implements Listener {
      private final Permission_KPerms bridge;

      public PermissionServerListener(Permission_KPerms bridge) {
         this.bridge = bridge;
      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginEnable(PluginEnableEvent event) {
         if (this.bridge.kperms == null) {
            Plugin plugin = event.getPlugin();
            if (plugin.getDescription().getName().equals("KPerms")) {
               this.bridge.kperms = (KPermsPlugin)plugin;
               Permission_KPerms.log.info(String.format("[%s][Permission] %s hooked.", Permission_KPerms.this.vault.getDescription().getName(), "KPerms"));
            }
         }

      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginDisable(PluginDisableEvent event) {
         if (this.bridge.kperms != null && event.getPlugin().getDescription().getName().equals(this.bridge.kperms.getName())) {
            this.bridge.kperms = null;
            Permission_KPerms.log.info(String.format("[%s][Permission] %s un-hooked.", Permission_KPerms.this.vault.getDescription().getName(), "KPerms"));
         }

      }
   }
}
