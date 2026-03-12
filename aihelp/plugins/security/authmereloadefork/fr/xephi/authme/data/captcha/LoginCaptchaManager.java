package fr.xephi.authme.data.captcha;

import fr.xephi.authme.initialization.HasCleanup;
import fr.xephi.authme.initialization.SettingsDependent;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.SecuritySettings;
import fr.xephi.authme.util.expiring.TimedCounter;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.bukkit.entity.Player;

public class LoginCaptchaManager implements CaptchaManager, SettingsDependent, HasCleanup {
   private final TimedCounter<String> playerCounts;
   private final CaptchaCodeStorage captchaCodeStorage = new CaptchaCodeStorage(30L, 4);
   private boolean isEnabled;
   private int threshold;

   @Inject
   LoginCaptchaManager(Settings settings) {
      this.playerCounts = new TimedCounter(9L, TimeUnit.MINUTES);
      this.reload(settings);
   }

   public void increaseLoginFailureCount(String name) {
      if (this.isEnabled) {
         String playerLower = name.toLowerCase(Locale.ROOT);
         this.playerCounts.increment(playerLower);
      }

   }

   public boolean isCaptchaRequired(String playerName) {
      return this.isEnabled && this.playerCounts.get(playerName.toLowerCase(Locale.ROOT)) >= this.threshold;
   }

   public String getCaptchaCodeOrGenerateNew(String name) {
      return this.captchaCodeStorage.getCodeOrGenerateNew(name);
   }

   public boolean checkCode(Player player, String code) {
      String nameLower = player.getName().toLowerCase(Locale.ROOT);
      boolean isCodeCorrect = this.captchaCodeStorage.checkCode(nameLower, code);
      if (isCodeCorrect) {
         this.playerCounts.remove(nameLower);
      }

      return isCodeCorrect;
   }

   public void resetLoginFailureCount(String name) {
      if (this.isEnabled) {
         this.playerCounts.remove(name.toLowerCase(Locale.ROOT));
      }

   }

   public void reload(Settings settings) {
      int expirationInMinutes = (Integer)settings.getProperty(SecuritySettings.CAPTCHA_COUNT_MINUTES_BEFORE_RESET);
      this.captchaCodeStorage.setExpirationInMinutes((long)expirationInMinutes);
      int captchaLength = (Integer)settings.getProperty(SecuritySettings.CAPTCHA_LENGTH);
      this.captchaCodeStorage.setCaptchaLength(captchaLength);
      int countTimeout = (Integer)settings.getProperty(SecuritySettings.CAPTCHA_COUNT_MINUTES_BEFORE_RESET);
      this.playerCounts.setExpiration((long)countTimeout, TimeUnit.MINUTES);
      this.isEnabled = (Boolean)settings.getProperty(SecuritySettings.ENABLE_LOGIN_FAILURE_CAPTCHA);
      this.threshold = (Integer)settings.getProperty(SecuritySettings.MAX_LOGIN_TRIES_BEFORE_CAPTCHA);
   }

   public void performCleanup() {
      this.playerCounts.removeExpiredEntries();
      this.captchaCodeStorage.removeExpiredEntries();
   }
}
