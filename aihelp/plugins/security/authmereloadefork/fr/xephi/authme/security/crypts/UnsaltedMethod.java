package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.HashUtils;
import fr.xephi.authme.security.crypts.description.HasSalt;
import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.SaltType;
import fr.xephi.authme.security.crypts.description.Usage;

@Recommendation(Usage.DO_NOT_USE)
@HasSalt(SaltType.NONE)
public abstract class UnsaltedMethod implements EncryptionMethod {
   public abstract String computeHash(String var1);

   public HashedPassword computeHash(String password, String name) {
      return new HashedPassword(this.computeHash(password));
   }

   public String computeHash(String password, String salt, String name) {
      return this.computeHash(password);
   }

   public boolean comparePassword(String password, HashedPassword hashedPassword, String name) {
      return HashUtils.isEqual(hashedPassword.getHash(), this.computeHash(password));
   }

   public String generateSalt() {
      return null;
   }

   public boolean hasSeparateSalt() {
      return false;
   }
}
