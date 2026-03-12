package fr.xephi.authme.security.crypts;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.libs.at.favre.lib.crypto.bcrypt.IllegalBCryptFormatException;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.security.HashUtils;
import fr.xephi.authme.security.crypts.description.HasSalt;
import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.SaltType;
import fr.xephi.authme.security.crypts.description.Usage;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

@Recommendation(Usage.RECOMMENDED)
@HasSalt(
   value = SaltType.TEXT,
   length = 22
)
public class Wbb4 implements EncryptionMethod {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(Wbb4.class);
   private BCryptHasher bCryptHasher;
   private SecureRandom random;

   public Wbb4() {
      this.bCryptHasher = new BCryptHasher(fr.xephi.authme.libs.at.favre.lib.crypto.bcrypt.BCrypt.Version.VERSION_2A, 8);
      this.random = new SecureRandom();
   }

   public HashedPassword computeHash(String password, String name) {
      byte[] salt = new byte[16];
      this.random.nextBytes(salt);
      String hash = this.hashInternal(password, salt);
      return new HashedPassword(hash);
   }

   public String computeHash(String password, String salt, String name) {
      return this.hashInternal(password, salt.getBytes(StandardCharsets.UTF_8));
   }

   public boolean comparePassword(String password, HashedPassword hashedPassword, String name) {
      try {
         fr.xephi.authme.libs.at.favre.lib.crypto.bcrypt.BCrypt.HashData hashData = fr.xephi.authme.libs.at.favre.lib.crypto.bcrypt.BCrypt.Version.VERSION_2A.parser.parse(hashedPassword.getHash().getBytes(StandardCharsets.UTF_8));
         byte[] salt = hashData.rawSalt;
         String computedHash = this.hashInternal(password, salt);
         return HashUtils.isEqual(hashedPassword.getHash(), computedHash);
      } catch (IllegalArgumentException | IllegalBCryptFormatException var7) {
         this.logger.logException("Invalid WBB4 hash:", var7);
         return false;
      }
   }

   private String hashInternal(String password, byte[] rawSalt) {
      return this.bCryptHasher.hashWithRawSalt(this.bCryptHasher.hashWithRawSalt(password, rawSalt), rawSalt);
   }

   public String generateSalt() {
      return BCryptHasher.generateSalt();
   }

   public boolean hasSeparateSalt() {
      return false;
   }
}
