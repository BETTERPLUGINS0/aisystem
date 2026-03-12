package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.HashUtils;
import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.Usage;
import java.util.Locale;

@Recommendation(Usage.RECOMMENDED)
public class XAuth extends HexSaltedMethod {
   private static String getWhirlpool(String message) {
      Whirlpool w = new Whirlpool();
      byte[] digest = new byte[64];
      w.NESSIEinit();
      w.NESSIEadd(message);
      w.NESSIEfinalize(digest);
      return Whirlpool.display(digest);
   }

   public String computeHash(String password, String salt, String name) {
      String hash = getWhirlpool(salt + password).toLowerCase(Locale.ROOT);
      int saltPos = password.length() >= hash.length() ? hash.length() - 1 : password.length();
      return hash.substring(0, saltPos) + salt + hash.substring(saltPos);
   }

   public boolean comparePassword(String password, HashedPassword hashedPassword, String name) {
      String hash = hashedPassword.getHash();
      int saltPos = password.length() >= hash.length() ? hash.length() - 1 : password.length();
      if (saltPos + 12 > hash.length()) {
         return false;
      } else {
         String salt = hash.substring(saltPos, saltPos + 12);
         return HashUtils.isEqual(hash, this.computeHash(password, salt, name));
      }
   }

   public int getSaltLength() {
      return 12;
   }
}
