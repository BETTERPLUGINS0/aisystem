package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.crypts.description.HasSalt;
import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.SaltType;
import fr.xephi.authme.security.crypts.description.Usage;
import fr.xephi.authme.util.RandomStringUtils;

@Recommendation(Usage.ACCEPTABLE)
@HasSalt(SaltType.TEXT)
public abstract class HexSaltedMethod implements EncryptionMethod {
   public abstract int getSaltLength();

   public abstract String computeHash(String var1, String var2, String var3);

   public HashedPassword computeHash(String password, String name) {
      String salt = this.generateSalt();
      return new HashedPassword(this.computeHash(password, salt, (String)null));
   }

   public abstract boolean comparePassword(String var1, HashedPassword var2, String var3);

   public String generateSalt() {
      return RandomStringUtils.generateHex(this.getSaltLength());
   }

   public boolean hasSeparateSalt() {
      return false;
   }
}
