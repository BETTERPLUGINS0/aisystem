package fr.xephi.authme.security.crypts;

import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.HooksSettings;

public class BCrypt extends BCryptBasedHash {
   @Inject
   public BCrypt(Settings settings) {
      super(createHasher(settings));
   }

   private static BCryptHasher createHasher(Settings settings) {
      int bCryptLog2Rounds = (Integer)settings.getProperty(HooksSettings.BCRYPT_LOG2_ROUND);
      return new BCryptHasher(fr.xephi.authme.libs.at.favre.lib.crypto.bcrypt.BCrypt.Version.VERSION_2A, bCryptLog2Rounds);
   }
}
