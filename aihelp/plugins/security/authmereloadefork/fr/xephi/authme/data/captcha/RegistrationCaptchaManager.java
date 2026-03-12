package fr.xephi.authme.data.captcha;

import fr.xephi.authme.initialization.HasCleanup;
import fr.xephi.authme.initialization.SettingsDependent;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.SecuritySettings;
import fr.xephi.authme.util.expiring.ExpiringSet;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.bukkit.entity.Player;

public class RegistrationCaptchaManager implements CaptchaManager, SettingsDependent, HasCleanup {
   private static final int MINUTES_VALID_FOR_REGISTRATION = 30;
   private final ExpiringSet<String> verifiedNamesForRegistration;
   private final CaptchaCodeStorage captchaCodeStorage = new CaptchaCodeStorage(30L, 4);
   private boolean isEnabled;

   @Inject
   RegistrationCaptchaManager(Settings settings) {
      this.verifiedNamesForRegistration = new ExpiringSet(30L, TimeUnit.MINUTES);
      this.reload(settings);
   }

   public boolean isCaptchaRequired(String name) {
      return this.isEnabled && !this.verifiedNamesForRegistration.contains(name.toLowerCase(Locale.ROOT));
   }

   public String getCaptchaCodeOrGenerateNew(String name) {
      return this.captchaCodeStorage.getCodeOrGenerateNew(name);
   }

   public boolean checkCode(Player player, String code) {
      String nameLower = player.getName().toLowerCase(Locale.ROOT);
      boolean isCodeCorrect = this.captchaCodeStorage.checkCode(nameLower, code);
      if (isCodeCorrect) {
         this.verifiedNamesForRegistration.add(nameLower);
      }

      return isCodeCorrect;
   }

   public void reload(Settings settings) {
      int captchaLength = (Integer)settings.getProperty(SecuritySettings.CAPTCHA_LENGTH);
      this.captchaCodeStorage.setCaptchaLength(captchaLength);
      this.isEnabled = (Boolean)settings.getProperty(SecuritySettings.ENABLE_CAPTCHA_FOR_REGISTRATION);
   }

   public void performCleanup() {
      this.verifiedNamesForRegistration.removeExpiredEntries();
      this.captchaCodeStorage.removeExpiredEntries();
   }
}
