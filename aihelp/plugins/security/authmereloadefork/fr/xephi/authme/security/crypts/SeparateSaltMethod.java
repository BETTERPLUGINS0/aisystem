package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.HashUtils;

public abstract class SeparateSaltMethod implements EncryptionMethod {
   public abstract String computeHash(String var1, String var2, String var3);

   public HashedPassword computeHash(String password, String name) {
      String salt = this.generateSalt();
      return new HashedPassword(this.computeHash(password, salt, name), salt);
   }

   public abstract String generateSalt();

   public boolean comparePassword(String password, HashedPassword hashedPassword, String name) {
      return HashUtils.isEqual(hashedPassword.getHash(), this.computeHash(password, hashedPassword.getSalt(), (String)null));
   }

   public boolean hasSeparateSalt() {
      return true;
   }
}
