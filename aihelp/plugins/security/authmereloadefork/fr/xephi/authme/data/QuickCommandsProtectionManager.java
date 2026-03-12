package fr.xephi.authme.data;

import fr.xephi.authme.initialization.HasCleanup;
import fr.xephi.authme.initialization.SettingsDependent;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.permission.PermissionsManager;
import fr.xephi.authme.permission.PlayerPermission;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.ProtectionSettings;
import fr.xephi.authme.util.expiring.ExpiringSet;
import java.util.concurrent.TimeUnit;
import org.bukkit.entity.Player;

public class QuickCommandsProtectionManager implements SettingsDependent, HasCleanup {
   private final PermissionsManager permissionsManager;
   private final ExpiringSet<String> latestJoin;

   @Inject
   public QuickCommandsProtectionManager(Settings settings, PermissionsManager permissionsManager) {
      this.permissionsManager = permissionsManager;
      long countTimeout = (long)(Integer)settings.getProperty(ProtectionSettings.QUICK_COMMANDS_DENIED_BEFORE_MILLISECONDS);
      this.latestJoin = new ExpiringSet(countTimeout, TimeUnit.MILLISECONDS);
      this.reload(settings);
   }

   private void setJoin(String name) {
      this.latestJoin.add(name);
   }

   private boolean shouldSavePlayer(Player player) {
      return this.permissionsManager.hasPermission(player, PlayerPermission.QUICK_COMMANDS_PROTECTION);
   }

   public void processJoin(Player player) {
      if (this.shouldSavePlayer(player)) {
         this.setJoin(player.getName());
      }

   }

   public boolean isAllowed(String name) {
      return !this.latestJoin.contains(name);
   }

   public void reload(Settings settings) {
      long countTimeout = (long)(Integer)settings.getProperty(ProtectionSettings.QUICK_COMMANDS_DENIED_BEFORE_MILLISECONDS);
      this.latestJoin.setExpiration(countTimeout, TimeUnit.MILLISECONDS);
   }

   public void performCleanup() {
      this.latestJoin.removeExpiredEntries();
   }
}
