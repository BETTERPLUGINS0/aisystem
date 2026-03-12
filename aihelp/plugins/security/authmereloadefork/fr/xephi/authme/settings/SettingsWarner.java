package fr.xephi.authme.settings;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.security.HashAlgorithm;
import fr.xephi.authme.security.crypts.Argon2;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.settings.properties.EmailSettings;
import fr.xephi.authme.settings.properties.HooksSettings;
import fr.xephi.authme.settings.properties.PluginSettings;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import fr.xephi.authme.settings.properties.SecuritySettings;
import java.util.Optional;

public class SettingsWarner {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(SettingsWarner.class);
   @Inject
   private Settings settings;
   @Inject
   private AuthMe authMe;
   @Inject
   private BukkitService bukkitService;

   SettingsWarner() {
   }

   public void logWarningsForMisconfigurations() {
      if (!(Boolean)this.settings.getProperty(RestrictionSettings.FORCE_SINGLE_SESSION)) {
         this.logger.warning("WARNING!!! By disabling ForceSingleSession, your server protection is inadequate!");
      }

      if (!(Boolean)this.settings.getProperty(EmailSettings.PORT25_USE_TLS) && (Integer)this.settings.getProperty(EmailSettings.SMTP_PORT) != 25) {
         this.logger.warning("Note: You have set Email.useTls to false but this only affects mail over port 25");
      }

      if ((Boolean)this.settings.getProperty(PluginSettings.SESSIONS_ENABLED) && (Integer)this.settings.getProperty(PluginSettings.SESSIONS_TIMEOUT) <= 0) {
         this.logger.warning("Warning: Session timeout needs to be positive in order to work!");
      }

      if (isTrue(this.bukkitService.isBungeeCordConfiguredForSpigot()) && !(Boolean)this.settings.getProperty(HooksSettings.BUNGEECORD) && !(Boolean)this.settings.getProperty(HooksSettings.VELOCITY)) {
         this.logger.warning("Note: Hooks.bungeecord is set to false but your server appears to be running in bungeecord mode (see your spigot.yml). In order to allow the datasource caching and the AuthMeBungee add-on to work properly you have to enable this option!");
      }

      if (!isTrue(this.bukkitService.isBungeeCordConfiguredForSpigot()) && (Boolean)this.settings.getProperty(HooksSettings.BUNGEECORD)) {
         this.logger.warning("Note: Hooks.bungeecord is set to true but your server appears to be running in non-bungeecord mode (see your spigot.yml). In order to prevent untrusted payload attack, BungeeCord hook will be automatically disabled!");
      }

      if (((HashAlgorithm)this.settings.getProperty(SecuritySettings.PASSWORD_HASH)).equals(HashAlgorithm.ARGON2) && !Argon2.isLibraryLoaded()) {
         this.logger.warning("WARNING!!! You use Argon2 Hash Algorithm method but we can't find the Argon2 library on your system! See https://github.com/AuthMe/AuthMeReloaded/wiki/Argon2-as-Password-Hash");
         this.authMe.stopOrUnload();
      }

   }

   private static boolean isTrue(Optional<Boolean> value) {
      return value.isPresent() && (Boolean)value.get();
   }
}
