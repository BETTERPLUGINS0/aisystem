package fr.xephi.authme.security.crypts;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.libs.com.google.common.primitives.Ints;
import fr.xephi.authme.libs.de.rtner.security.auth.spi.PBKDF2Engine;
import fr.xephi.authme.libs.de.rtner.security.auth.spi.PBKDF2Parameters;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.security.crypts.description.AsciiRestricted;
import java.util.Base64;

@AsciiRestricted
public class Pbkdf2Django extends HexSaltedMethod {
   private static final int DEFAULT_ITERATIONS = 24000;
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(Pbkdf2Django.class);

   public String computeHash(String password, String salt, String name) {
      String result = "pbkdf2_sha256$24000$" + salt + "$";
      PBKDF2Parameters params = new PBKDF2Parameters("HmacSHA256", "ASCII", salt.getBytes(), 24000);
      PBKDF2Engine engine = new PBKDF2Engine(params);
      return result + Base64.getEncoder().encodeToString(engine.deriveKey(password, 32));
   }

   public boolean comparePassword(String password, HashedPassword hashedPassword, String unusedName) {
      String[] line = hashedPassword.getHash().split("\\$");
      if (line.length != 4) {
         return false;
      } else {
         Integer iterations = Ints.tryParse(line[1]);
         if (iterations == null) {
            this.logger.warning("Cannot read number of rounds for Pbkdf2Django: '" + line[1] + "'");
            return false;
         } else {
            String salt = line[2];
            byte[] derivedKey = Base64.getDecoder().decode(line[3]);
            PBKDF2Parameters params = new PBKDF2Parameters("HmacSHA256", "ASCII", salt.getBytes(), iterations, derivedKey);
            PBKDF2Engine engine = new PBKDF2Engine(params);
            return engine.verifyKey(password);
         }
      }
   }

   public int getSaltLength() {
      return 12;
   }
}
