package fr.xephi.authme.security.totp;

import fr.xephi.authme.initialization.HasCleanup;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.util.expiring.ExpiringMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.bukkit.entity.Player;

public class GenerateTotpService implements HasCleanup {
   private static final int NEW_TOTP_KEY_EXPIRATION_MINUTES = 5;
   private final ExpiringMap<String, TotpAuthenticator.TotpGenerationResult> totpKeys;
   @Inject
   private TotpAuthenticator totpAuthenticator;

   GenerateTotpService() {
      this.totpKeys = new ExpiringMap(5L, TimeUnit.MINUTES);
   }

   public TotpAuthenticator.TotpGenerationResult generateTotpKey(Player player) {
      TotpAuthenticator.TotpGenerationResult credentials = this.totpAuthenticator.generateTotpKey(player);
      this.totpKeys.put(player.getName().toLowerCase(Locale.ROOT), credentials);
      return credentials;
   }

   public TotpAuthenticator.TotpGenerationResult getGeneratedTotpKey(Player player) {
      return (TotpAuthenticator.TotpGenerationResult)this.totpKeys.get(player.getName().toLowerCase(Locale.ROOT));
   }

   public void removeGenerateTotpKey(Player player) {
      this.totpKeys.remove(player.getName().toLowerCase(Locale.ROOT));
   }

   public boolean isTotpCodeCorrectForGeneratedTotpKey(Player player, String totpCode) {
      TotpAuthenticator.TotpGenerationResult totpDetails = (TotpAuthenticator.TotpGenerationResult)this.totpKeys.get(player.getName().toLowerCase(Locale.ROOT));
      return totpDetails != null && this.totpAuthenticator.checkCode(player.getName(), totpDetails.getTotpKey(), totpCode);
   }

   public void performCleanup() {
      this.totpKeys.removeExpiredEntries();
   }
}
