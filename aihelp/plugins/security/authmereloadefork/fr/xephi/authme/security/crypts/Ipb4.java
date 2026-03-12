package fr.xephi.authme.security.crypts;

import fr.xephi.authme.libs.at.favre.lib.crypto.bcrypt.IllegalBCryptFormatException;
import fr.xephi.authme.security.crypts.description.HasSalt;
import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.SaltType;
import fr.xephi.authme.security.crypts.description.Usage;
import fr.xephi.authme.util.RandomStringUtils;
import java.nio.charset.StandardCharsets;

@Recommendation(Usage.DOES_NOT_WORK)
@HasSalt(
   value = SaltType.TEXT,
   length = 22
)
public class Ipb4 implements EncryptionMethod {
   private BCryptHasher bCryptHasher;

   public Ipb4() {
      this.bCryptHasher = new BCryptHasher(fr.xephi.authme.libs.at.favre.lib.crypto.bcrypt.BCrypt.Version.VERSION_2A, 13);
   }

   public String computeHash(String password, String salt, String name) {
      String dummyHash = "$2a$10$" + salt + "3Cfb5GnwvKhJ20r.hMjmcNkIT9.Uh9K";

      try {
         fr.xephi.authme.libs.at.favre.lib.crypto.bcrypt.BCrypt.HashData parseResult = fr.xephi.authme.libs.at.favre.lib.crypto.bcrypt.BCrypt.Version.VERSION_2A.parser.parse(dummyHash.getBytes(StandardCharsets.UTF_8));
         return this.bCryptHasher.hashWithRawSalt(password, parseResult.rawSalt);
      } catch (IllegalArgumentException | IllegalBCryptFormatException var6) {
         throw new IllegalStateException("Cannot parse hash with salt '" + salt + "'", var6);
      }
   }

   public HashedPassword computeHash(String password, String name) {
      HashedPassword hash = this.bCryptHasher.hash(password);
      String salt = hash.getHash().substring(7, 29);
      return new HashedPassword(hash.getHash(), salt);
   }

   public boolean comparePassword(String password, HashedPassword hashedPassword, String name) {
      return BCryptHasher.comparePassword(password, hashedPassword.getHash());
   }

   public String generateSalt() {
      return RandomStringUtils.generateLowerUpper(22);
   }

   public boolean hasSeparateSalt() {
      return true;
   }
}
