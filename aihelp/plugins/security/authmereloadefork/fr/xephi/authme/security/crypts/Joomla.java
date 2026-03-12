package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.HashUtils;
import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.Usage;

@Recommendation(Usage.ACCEPTABLE)
public class Joomla extends HexSaltedMethod {
   public String computeHash(String password, String salt, String name) {
      return HashUtils.md5(password + salt) + ":" + salt;
   }

   public boolean comparePassword(String password, HashedPassword hashedPassword, String unusedName) {
      String hash = hashedPassword.getHash();
      String[] hashParts = hash.split(":");
      return hashParts.length == 2 && HashUtils.isEqual(hash, this.computeHash(password, hashParts[1], (String)null));
   }

   public int getSaltLength() {
      return 32;
   }
}
