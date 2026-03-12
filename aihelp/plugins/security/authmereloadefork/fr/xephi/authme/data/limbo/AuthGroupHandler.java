package fr.xephi.authme.data.limbo;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.initialization.Reloadable;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.permission.PermissionsManager;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.PluginSettings;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.PostConstruct;
import org.bukkit.entity.Player;

class AuthGroupHandler implements Reloadable {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(AuthGroupHandler.class);
   @Inject
   private PermissionsManager permissionsManager;
   @Inject
   private Settings settings;
   private UserGroup unregisteredGroup;
   private UserGroup registeredGroup;

   void setGroup(Player player, LimboPlayer limbo, AuthGroupType groupType) {
      if (this.useAuthGroups()) {
         Collection<UserGroup> previousGroups = limbo == null ? Collections.emptyList() : limbo.getGroups();
         switch(groupType) {
         case UNREGISTERED:
            this.permissionsManager.addGroup(player, this.unregisteredGroup);
            this.permissionsManager.removeGroup(player, this.registeredGroup);
            this.permissionsManager.removeGroups(player, (Collection)previousGroups);
            break;
         case REGISTERED_UNAUTHENTICATED:
            this.permissionsManager.addGroup(player, this.registeredGroup);
            this.permissionsManager.removeGroup(player, this.unregisteredGroup);
            this.permissionsManager.removeGroups(player, (Collection)previousGroups);
            break;
         case LOGGED_IN:
            this.permissionsManager.addGroups(player, (Collection)previousGroups);
            this.permissionsManager.removeGroup(player, this.unregisteredGroup);
            this.permissionsManager.removeGroup(player, this.registeredGroup);
            break;
         default:
            throw new IllegalStateException("Encountered unhandled auth group type '" + groupType + "'");
         }

         this.logger.debug(() -> {
            return player.getName() + " changed to " + groupType + ": has groups " + this.permissionsManager.getGroups(player);
         });
      }
   }

   private boolean useAuthGroups() {
      if (!(Boolean)this.settings.getProperty(PluginSettings.ENABLE_PERMISSION_CHECK)) {
         return false;
      } else if (!this.permissionsManager.hasGroupSupport()) {
         this.logger.warning("The current permissions system doesn't have group support, unable to set group!");
         return false;
      } else {
         return true;
      }
   }

   @PostConstruct
   public void reload() {
      this.unregisteredGroup = new UserGroup((String)this.settings.getProperty(PluginSettings.UNREGISTERED_GROUP));
      this.registeredGroup = new UserGroup((String)this.settings.getProperty(PluginSettings.REGISTERED_GROUP));
   }
}
