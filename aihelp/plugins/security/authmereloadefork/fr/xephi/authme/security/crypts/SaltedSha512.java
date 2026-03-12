package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.HashUtils;
import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.Usage;
import fr.xephi.authme.util.RandomStringUtils;

@Recommendation(Usage.RECOMMENDED)
public class SaltedSha512 extends SeparateSaltMethod {
   public String computeHash(String password, String salt, String name) {
      return HashUtils.sha512(password + salt);
   }

   public String generateSalt() {
      return RandomStringUtils.generateHex(32);
   }
}
