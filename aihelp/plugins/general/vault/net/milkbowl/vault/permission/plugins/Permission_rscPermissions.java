package net.milkbowl.vault.permission.plugins;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import ru.simsonic.rscPermissions.MainPluginClass;
import ru.simsonic.rscPermissions.rscpAPI;

public class Permission_rscPermissions extends Permission {
   private final Plugin vault;
   private MainPluginClass rscp = null;
   private rscpAPI rscpAPI = null;

   public Permission_rscPermissions(Plugin plugin) {
      this.vault = plugin;
      Bukkit.getServer().getPluginManager().registerEvents(new Permission_rscPermissions.PermissionServerListener(this), this.vault);
      if (this.rscp == null) {
         Plugin perms = plugin.getServer().getPluginManager().getPlugin("rscPermissions");
         if (perms != null && perms.isEnabled()) {
            this.rscp = (MainPluginClass)perms;
            this.rscpAPI = this.rscp.API;
            plugin.getLogger().info(String.format("[%s][Permission] %s hooked.", plugin.getDescription().getName(), "rscPermissions"));
         }
      }

   }

   public String getName() {
      return "rscPermissions";
   }

   public boolean isEnabled() {
      return this.rscpAPI != null && this.rscpAPI.isEnabled();
   }

   public boolean hasSuperPermsCompat() {
      return this.rscpAPI.hasSuperPermsCompat();
   }

   public boolean hasGroupSupport() {
      return this.rscpAPI.hasGroupSupport();
   }

   public boolean playerHas(String world, String player, String permission) {
      return this.rscpAPI.playerHas(world, player, permission);
   }

   public boolean playerAdd(String world, String player, String permission) {
      return this.rscpAPI.playerAdd(world, player, permission);
   }

   public boolean playerRemove(String world, String player, String permission) {
      return this.rscpAPI.playerRemove(world, player, permission);
   }

   public boolean groupHas(String world, String group, String permission) {
      return this.rscpAPI.groupHas(world, group, permission);
   }

   public boolean groupAdd(String world, String group, String permission) {
      return this.rscpAPI.groupAdd(world, group, permission);
   }

   public boolean groupRemove(String world, String group, String permission) {
      return this.rscpAPI.groupRemove(world, group, permission);
   }

   public boolean playerInGroup(String world, String player, String group) {
      return this.rscpAPI.playerInGroup(world, player, group);
   }

   public boolean playerAddGroup(String world, String player, String group) {
      return this.rscpAPI.playerAddGroup(world, player, group);
   }

   public boolean playerRemoveGroup(String world, String player, String group) {
      return this.rscpAPI.playerRemoveGroup(world, player, group);
   }

   public String[] getPlayerGroups(String world, String player) {
      return this.rscpAPI.getPlayerGroups(world, player);
   }

   public String getPrimaryGroup(String world, String player) {
      return this.rscpAPI.getPrimaryGroup(world, player);
   }

   public String[] getGroups() {
      return this.rscpAPI.getGroups();
   }

   private class PermissionServerListener implements Listener {
      private final Permission_rscPermissions bridge;

      public PermissionServerListener(Permission_rscPermissions bridge) {
         this.bridge = bridge;
      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginEnable(PluginEnableEvent event) {
         if (this.bridge.rscp == null) {
            Plugin plugin = event.getPlugin();
            if (plugin.getDescription().getName().equals("rscPermissions")) {
               this.bridge.rscp = (MainPluginClass)plugin;
               this.bridge.rscpAPI = this.bridge.rscp.API;
               Permission_rscPermissions.log.info(String.format("[%s][Permission] %s hooked.", Permission_rscPermissions.this.vault.getDescription().getName(), "rscPermissions"));
            }
         }

      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      public void onPluginDisable(PluginDisableEvent event) {
         if (this.bridge.rscpAPI != null && event.getPlugin().getDescription().getName().equals(this.bridge.rscpAPI.getName())) {
            this.bridge.rscpAPI = null;
            this.bridge.rscp = null;
            Permission_rscPermissions.log.info(String.format("[%s][Permission] %s un-hooked.", Permission_rscPermissions.this.vault.getDescription().getName(), "rscPermissions"));
         }

      }
   }
}
