package fr.xephi.authme.service;

import fr.xephi.authme.initialization.HasCleanup;
import fr.xephi.authme.initialization.SettingsDependent;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.SecuritySettings;
import fr.xephi.authme.util.RandomStringUtils;
import fr.xephi.authme.util.expiring.ExpiringMap;
import fr.xephi.authme.util.expiring.TimedCounter;
import java.util.concurrent.TimeUnit;

public class RecoveryCodeService implements SettingsDependent, HasCleanup {
   private final ExpiringMap<String, String> recoveryCodes;
   private final TimedCounter<String> playerTries;
   private int recoveryCodeLength;
   private int recoveryCodeExpiration;
   private int recoveryCodeMaxTries;

   @Inject
   RecoveryCodeService(Settings settings) {
      this.recoveryCodeLength = (Integer)settings.getProperty(SecuritySettings.RECOVERY_CODE_LENGTH);
      this.recoveryCodeExpiration = (Integer)settings.getProperty(SecuritySettings.RECOVERY_CODE_HOURS_VALID);
      this.recoveryCodeMaxTries = (Integer)settings.getProperty(SecuritySettings.RECOVERY_CODE_MAX_TRIES);
      this.recoveryCodes = new ExpiringMap((long)this.recoveryCodeExpiration, TimeUnit.HOURS);
      this.playerTries = new TimedCounter((long)this.recoveryCodeExpiration, TimeUnit.HOURS);
   }

   public boolean isRecoveryCodeNeeded() {
      return this.recoveryCodeLength > 0 && this.recoveryCodeExpiration > 0;
   }

   public String generateCode(String player) {
      String code = RandomStringUtils.generateHex(this.recoveryCodeLength);
      this.playerTries.put(player, this.recoveryCodeMaxTries);
      this.recoveryCodes.put(player, code);
      return code;
   }

   public boolean isCodeValid(String player, String code) {
      String storedCode = (String)this.recoveryCodes.get(player);
      this.playerTries.decrement(player);
      return storedCode != null && storedCode.equals(code);
   }

   public boolean hasTriesLeft(String player) {
      return this.playerTries.get(player) > 0;
   }

   public int getTriesLeft(String player) {
      return this.playerTries.get(player);
   }

   public void removeCode(String player) {
      this.recoveryCodes.remove(player);
      this.playerTries.remove(player);
   }

   public void reload(Settings settings) {
      this.recoveryCodeLength = (Integer)settings.getProperty(SecuritySettings.RECOVERY_CODE_LENGTH);
      this.recoveryCodeExpiration = (Integer)settings.getProperty(SecuritySettings.RECOVERY_CODE_HOURS_VALID);
      this.recoveryCodeMaxTries = (Integer)settings.getProperty(SecuritySettings.RECOVERY_CODE_MAX_TRIES);
      this.recoveryCodes.setExpiration((long)this.recoveryCodeExpiration, TimeUnit.HOURS);
   }

   public void performCleanup() {
      this.recoveryCodes.removeExpiredEntries();
      this.playerTries.removeExpiredEntries();
   }
}
