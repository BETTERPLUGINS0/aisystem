package fr.xephi.authme.service;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.security.HashAlgorithm;
import fr.xephi.authme.security.crypts.HashedPassword;
import fr.xephi.authme.security.crypts.Sha256;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.SecuritySettings;
import java.util.Iterator;
import java.util.List;

public final class MigrationService {
   private static ConsoleLogger logger = ConsoleLoggerFactory.get(MigrationService.class);

   private MigrationService() {
   }

   public static void changePlainTextToSha256(Settings settings, DataSource dataSource, Sha256 authmeSha256) {
      if (HashAlgorithm.PLAINTEXT == settings.getProperty(SecuritySettings.PASSWORD_HASH)) {
         logger.warning("Your HashAlgorithm has been detected as plaintext and is now deprecated; it will be changed and hashed now to the AuthMe default hashing method");
         logger.warning("Don't stop your server; wait for the conversion to have been completed!");
         List<PlayerAuth> allAuths = dataSource.getAllAuths();
         Iterator var4 = allAuths.iterator();

         while(var4.hasNext()) {
            PlayerAuth auth = (PlayerAuth)var4.next();
            String hash = auth.getPassword().getHash();
            if (hash.startsWith("$SHA$")) {
               logger.warning("Skipping conversion for " + auth.getNickname() + "; detected SHA hash");
            } else {
               HashedPassword hashedPassword = authmeSha256.computeHash(hash, auth.getNickname());
               auth.setPassword(hashedPassword);
               dataSource.updatePassword(auth);
            }
         }

         settings.setProperty(SecuritySettings.PASSWORD_HASH, HashAlgorithm.SHA256);
         settings.save();
         logger.info("Migrated " + allAuths.size() + " accounts from plaintext to SHA256");
      }

   }
}
