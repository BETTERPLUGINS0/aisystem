package fr.xephi.authme.security.crypts;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.libs.de.mkammerer.argon2.Argon2Factory;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.security.crypts.description.HasSalt;
import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.SaltType;
import fr.xephi.authme.security.crypts.description.Usage;

@Recommendation(Usage.RECOMMENDED)
@HasSalt(
   value = SaltType.TEXT,
   length = 16
)
public class Argon2 extends UnsaltedMethod {
   private static ConsoleLogger logger = ConsoleLoggerFactory.get(Argon2.class);
   private fr.xephi.authme.libs.de.mkammerer.argon2.Argon2 argon2 = Argon2Factory.create();

   public static boolean isLibraryLoaded() {
      try {
         System.loadLibrary("argon2");
         return true;
      } catch (UnsatisfiedLinkError var1) {
         logger.logException("Cannot find argon2 library: https://github.com/AuthMe/AuthMeReloaded/wiki/Argon2-as-Password-Hash", var1);
         return false;
      }
   }

   public String computeHash(String password) {
      return this.argon2.hash(2, 65536, 1, (String)password);
   }

   public boolean comparePassword(String password, HashedPassword hashedPassword, String name) {
      return this.argon2.verify(hashedPassword.getHash(), password);
   }
}
