package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.HashUtils;
import fr.xephi.authme.security.crypts.description.HasSalt;
import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.SaltType;
import fr.xephi.authme.security.crypts.description.Usage;
import fr.xephi.authme.util.RandomStringUtils;
import java.util.Locale;

@Recommendation(Usage.DO_NOT_USE)
@HasSalt(SaltType.USERNAME)
public class Smf implements EncryptionMethod {
   public HashedPassword computeHash(String password, String name) {
      return new HashedPassword(this.computeHash(password, (String)null, name), this.generateSalt());
   }

   public String computeHash(String password, String salt, String name) {
      return HashUtils.sha1(name.toLowerCase(Locale.ROOT) + password);
   }

   public boolean comparePassword(String password, HashedPassword hashedPassword, String name) {
      return HashUtils.isEqual(hashedPassword.getHash(), this.computeHash(password, (String)null, name));
   }

   public String generateSalt() {
      return RandomStringUtils.generate(4);
   }

   public boolean hasSeparateSalt() {
      return true;
   }
}
