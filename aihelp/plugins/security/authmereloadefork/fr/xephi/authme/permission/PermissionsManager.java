package fr.xephi.authme.permission;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.limbo.UserGroup;
import fr.xephi.authme.initialization.Reloadable;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.permission.handlers.LuckPermsHandler;
import fr.xephi.authme.permission.handlers.PermissionHandler;
import fr.xephi.authme.permission.handlers.PermissionHandlerException;
import fr.xephi.authme.permission.handlers.PermissionLoadUserException;
import fr.xephi.authme.permission.handlers.PermissionsExHandler;
import fr.xephi.authme.permission.handlers.VaultHandler;
import fr.xephi.authme.permission.handlers.ZPermissionsHandler;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.PluginSettings;
import fr.xephi.authme.util.StringUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class PermissionsManager implements Reloadable {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(PermissionsManager.class);
   private final Server server;
   private final PluginManager pluginManager;
   private final Settings settings;
   private PermissionHandler handler = null;

   @Inject
   PermissionsManager(Server server, PluginManager pluginManager, Settings settings) {
      this.server = server;
      this.pluginManager = pluginManager;
      this.settings = settings;
   }

   public boolean isEnabled() {
      return this.handler != null;
   }

   @PostConstruct
   @VisibleForTesting
   void setup() {
      if ((Boolean)this.settings.getProperty(PluginSettings.FORCE_VAULT_HOOK)) {
         try {
            PermissionHandler handler = this.createPermissionHandler(PermissionsSystemType.VAULT);
            if (handler != null) {
               this.handler = handler;
               this.logger.info("Hooked into " + PermissionsSystemType.VAULT.getDisplayName() + "!");
               return;
            }
         } catch (PermissionHandlerException var7) {
            this.logger.logException("Failed to create Vault hook (forced):", var7);
         }
      } else {
         PermissionsSystemType[] var8 = PermissionsSystemType.values();
         int var2 = var8.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            PermissionsSystemType type = var8[var3];

            try {
               PermissionHandler handler = this.createPermissionHandler(type);
               if (handler != null) {
                  this.handler = handler;
                  this.logger.info("Hooked into " + type.getDisplayName() + "!");
                  return;
               }
            } catch (Exception var6) {
               this.logger.logException("Error while hooking into " + type.getDisplayName(), var6);
            }
         }
      }

      this.logger.info("No supported permissions system found! Permissions are disabled!");
   }

   private PermissionHandler createPermissionHandler(PermissionsSystemType type) throws PermissionHandlerException {
      Plugin plugin = this.pluginManager.getPlugin(type.getPluginName());
      if (plugin == null) {
         return null;
      } else if (!plugin.isEnabled()) {
         this.logger.info("Not hooking into " + type.getDisplayName() + " because it's disabled!");
         return null;
      } else {
         switch(type) {
         case LUCK_PERMS:
            return new LuckPermsHandler();
         case PERMISSIONS_EX:
            return new PermissionsExHandler();
         case Z_PERMISSIONS:
            return new ZPermissionsHandler();
         case VAULT:
            return new VaultHandler(this.server);
         default:
            throw new IllegalStateException("Unhandled permission type '" + type + "'");
         }
      }
   }

   private void unhook() {
      this.handler = null;
      this.logger.info("Unhooked from Permissions!");
   }

   public void reload() {
      this.unhook();
      this.setup();
   }

   public void onPluginEnable(String pluginName) {
      if (PermissionsSystemType.isPermissionSystem(pluginName)) {
         this.logger.info(pluginName + " plugin enabled, dynamically updating permissions hooks!");
         this.setup();
      }

   }

   public void onPluginDisable(String pluginName) {
      if (PermissionsSystemType.isPermissionSystem(pluginName)) {
         this.logger.info(pluginName + " plugin disabled, updating hooks!");
         this.setup();
      }

   }

   public PermissionsSystemType getPermissionSystem() {
      return this.isEnabled() ? this.handler.getPermissionSystem() : null;
   }

   public boolean hasPermission(CommandSender sender, PermissionNode permissionNode) {
      if (permissionNode == null) {
         return true;
      } else if (sender instanceof Player && this.isEnabled()) {
         Player player = (Player)sender;
         return player.hasPermission(permissionNode.getNode());
      } else {
         return permissionNode.getDefaultPermission().evaluate(sender);
      }
   }

   public boolean hasPermissionOffline(OfflinePlayer player, PermissionNode permissionNode) {
      if (permissionNode == null) {
         return true;
      } else {
         return !this.isEnabled() ? permissionNode.getDefaultPermission().evaluate(player) : this.handler.hasPermissionOffline(player.getName(), permissionNode);
      }
   }

   public boolean hasPermissionOffline(String name, PermissionNode permissionNode) {
      if (permissionNode == null) {
         return true;
      } else {
         return !this.isEnabled() ? permissionNode.getDefaultPermission().evaluate((ServerOperator)null) : this.handler.hasPermissionOffline(name, permissionNode);
      }
   }

   public boolean hasGroupSupport() {
      return this.isEnabled() && this.handler.hasGroupSupport();
   }

   public Collection<UserGroup> getGroups(OfflinePlayer player) {
      return (Collection)(this.isEnabled() ? this.handler.getGroups(player) : Collections.emptyList());
   }

   public UserGroup getPrimaryGroup(OfflinePlayer player) {
      return this.isEnabled() ? this.handler.getPrimaryGroup(player) : null;
   }

   public boolean isInGroup(OfflinePlayer player, UserGroup groupName) {
      return this.isEnabled() && this.handler.isInGroup(player, groupName);
   }

   public boolean addGroup(OfflinePlayer player, UserGroup groupName) {
      return this.isEnabled() && !StringUtils.isBlank(groupName.getGroupName()) ? this.handler.addToGroup(player, groupName) : false;
   }

   public boolean addGroups(OfflinePlayer player, Collection<UserGroup> groupNames) {
      if (!this.isEnabled()) {
         return false;
      } else {
         boolean result = false;
         Iterator var4 = groupNames.iterator();

         while(var4.hasNext()) {
            UserGroup group = (UserGroup)var4.next();
            if (!group.getGroupName().isEmpty()) {
               result |= this.handler.addToGroup(player, group);
            }
         }

         return result;
      }
   }

   public boolean removeGroup(OfflinePlayer player, UserGroup group) {
      return this.isEnabled() && this.handler.removeFromGroup(player, group);
   }

   public boolean removeGroups(OfflinePlayer player, Collection<UserGroup> groupNames) {
      if (!this.isEnabled()) {
         return false;
      } else {
         boolean result = false;
         Iterator var4 = groupNames.iterator();

         while(var4.hasNext()) {
            UserGroup group = (UserGroup)var4.next();
            if (!group.getGroupName().isEmpty()) {
               result |= this.handler.removeFromGroup(player, group);
            }
         }

         return result;
      }
   }

   public boolean setGroup(OfflinePlayer player, UserGroup group) {
      return this.isEnabled() && this.handler.setGroup(player, group);
   }

   public boolean removeAllGroups(OfflinePlayer player) {
      if (!this.isEnabled()) {
         return false;
      } else {
         Collection<UserGroup> groups = this.getGroups(player);
         return this.removeGroups(player, groups);
      }
   }

   public boolean loadUserData(OfflinePlayer offlinePlayer) {
      try {
         this.loadUserData(offlinePlayer.getUniqueId());
         return true;
      } catch (PermissionLoadUserException var3) {
         this.logger.logException("Unable to load the permission data of user " + offlinePlayer.getName(), var3);
         return false;
      }
   }

   public void loadUserData(UUID uuid) throws PermissionLoadUserException {
      if (this.isEnabled()) {
         this.handler.loadUserData(uuid);
      }
   }
}
