package fr.xephi.authme.security.totp;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.initialization.HasCleanup;
import fr.xephi.authme.libs.com.google.common.collect.HashBasedTable;
import fr.xephi.authme.libs.com.google.common.collect.Table;
import fr.xephi.authme.libs.com.google.common.primitives.Ints;
import fr.xephi.authme.libs.com.warrenstrange.googleauth.GoogleAuthenticator;
import fr.xephi.authme.libs.com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import fr.xephi.authme.libs.com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import fr.xephi.authme.libs.com.warrenstrange.googleauth.IGoogleAuthenticator;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.PluginSettings;
import java.util.Locale;
import org.bukkit.entity.Player;

public class TotpAuthenticator implements HasCleanup {
   private static final int CODE_RETENTION_MINUTES = 5;
   private final IGoogleAuthenticator authenticator = this.createGoogleAuthenticator();
   private final Settings settings;
   private final Table<String, Integer, Long> usedCodes = HashBasedTable.create();

   @Inject
   TotpAuthenticator(Settings settings) {
      this.settings = settings;
   }

   protected IGoogleAuthenticator createGoogleAuthenticator() {
      return new GoogleAuthenticator();
   }

   public boolean checkCode(PlayerAuth auth, String totpCode) {
      return this.checkCode(auth.getNickname(), auth.getTotpKey(), totpCode);
   }

   public boolean checkCode(String playerName, String totpKey, String inputCode) {
      String nameLower = playerName.toLowerCase(Locale.ROOT);
      Integer totpCode = Ints.tryParse(inputCode);
      if (totpCode != null && !this.usedCodes.contains(nameLower, totpCode) && this.authenticator.authorize(totpKey, totpCode)) {
         this.usedCodes.put(nameLower, totpCode, System.currentTimeMillis());
         return true;
      } else {
         return false;
      }
   }

   public TotpAuthenticator.TotpGenerationResult generateTotpKey(Player player) {
      GoogleAuthenticatorKey credentials = this.authenticator.createCredentials();
      String qrCodeUrl = GoogleAuthenticatorQRGenerator.getOtpAuthURL((String)this.settings.getProperty(PluginSettings.SERVER_NAME), player.getName(), credentials);
      return new TotpAuthenticator.TotpGenerationResult(credentials.getKey(), qrCodeUrl);
   }

   public void performCleanup() {
      long threshold = System.currentTimeMillis() - 300000L;
      this.usedCodes.values().removeIf((value) -> {
         return value < threshold;
      });
   }

   public static final class TotpGenerationResult {
      private final String totpKey;
      private final String authenticatorQrCodeUrl;

      public TotpGenerationResult(String totpKey, String authenticatorQrCodeUrl) {
         this.totpKey = totpKey;
         this.authenticatorQrCodeUrl = authenticatorQrCodeUrl;
      }

      public String getTotpKey() {
         return this.totpKey;
      }

      public String getAuthenticatorQrCodeUrl() {
         return this.authenticatorQrCodeUrl;
      }
   }
}
