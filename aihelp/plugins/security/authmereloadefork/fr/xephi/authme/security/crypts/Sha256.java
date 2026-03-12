package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.HashUtils;
import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.Usage;

@Recommendation(Usage.RECOMMENDED)
public class Sha256 extends HexSaltedMethod {
   public String computeHash(String password, String salt, String name) {
      return "$SHA$" + salt + "$" + HashUtils.sha256(HashUtils.sha256(password) + salt);
   }

   public boolean comparePassword(String password, HashedPassword hashedPassword, String name) {
      String hash = hashedPassword.getHash();
      String[] line = hash.split("\\$");
      return line.length == 4 && HashUtils.isEqual(hash, this.computeHash(password, line[2], name));
   }

   public int getSaltLength() {
      return 16;
   }
}
