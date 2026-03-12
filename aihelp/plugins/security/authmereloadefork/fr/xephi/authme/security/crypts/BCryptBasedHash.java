package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.crypts.description.HasSalt;
import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.SaltType;
import fr.xephi.authme.security.crypts.description.Usage;
import java.nio.charset.StandardCharsets;

@Recommendation(Usage.RECOMMENDED)
@HasSalt(
   value = SaltType.TEXT,
   length = 22
)
public abstract class BCryptBasedHash implements EncryptionMethod {
   private final BCryptHasher bCryptHasher;

   public BCryptBasedHash(BCryptHasher bCryptHasher) {
      this.bCryptHasher = bCryptHasher;
   }

   public HashedPassword computeHash(String password, String name) {
      return this.bCryptHasher.hash(password);
   }

   public String computeHash(String password, String salt, String name) {
      return this.bCryptHasher.hashWithRawSalt(password, salt.getBytes(StandardCharsets.UTF_8));
   }

   public boolean comparePassword(String password, HashedPassword hashedPassword, String name) {
      return BCryptHasher.comparePassword(password, hashedPassword.getHash());
   }

   public String generateSalt() {
      return BCryptHasher.generateSalt();
   }

   public boolean hasSeparateSalt() {
      return false;
   }
}
