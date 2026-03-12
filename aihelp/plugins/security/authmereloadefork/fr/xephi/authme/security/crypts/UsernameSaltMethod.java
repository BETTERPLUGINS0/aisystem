package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.HashUtils;
import fr.xephi.authme.security.crypts.description.HasSalt;
import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.SaltType;
import fr.xephi.authme.security.crypts.description.Usage;

@Recommendation(Usage.DO_NOT_USE)
@HasSalt(SaltType.USERNAME)
public abstract class UsernameSaltMethod implements EncryptionMethod {
   public abstract HashedPassword computeHash(String var1, String var2);

   public String computeHash(String password, String salt, String name) {
      return this.computeHash(password, name).getHash();
   }

   public boolean comparePassword(String password, HashedPassword hashedPassword, String name) {
      return HashUtils.isEqual(hashedPassword.getHash(), this.computeHash(password, name).getHash());
   }

   public String generateSalt() {
      return null;
   }

   public boolean hasSeparateSalt() {
      return false;
   }
}
