package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.HashUtils;
import fr.xephi.authme.util.RandomStringUtils;
import java.nio.charset.StandardCharsets;

public class BCryptHasher {
   public static final int BYTES_IN_SALT = 16;
   public static final int SALT_LENGTH_ENCODED = 22;
   private final fr.xephi.authme.libs.at.favre.lib.crypto.bcrypt.BCrypt.Hasher hasher;
   private final int costFactor;

   public BCryptHasher(fr.xephi.authme.libs.at.favre.lib.crypto.bcrypt.BCrypt.Version version, int costFactor) {
      this.hasher = fr.xephi.authme.libs.at.favre.lib.crypto.bcrypt.BCrypt.with(version);
      this.costFactor = costFactor;
   }

   public HashedPassword hash(String password) {
      byte[] hash = this.hasher.hash(this.costFactor, password.getBytes(StandardCharsets.UTF_8));
      return new HashedPassword(new String(hash, StandardCharsets.UTF_8));
   }

   public String hashWithRawSalt(String password, byte[] rawSalt) {
      byte[] hash = this.hasher.hash(this.costFactor, rawSalt, password.getBytes(StandardCharsets.UTF_8));
      return new String(hash, StandardCharsets.UTF_8);
   }

   public static boolean comparePassword(String password, String hash) {
      if (HashUtils.isValidBcryptHash(hash)) {
         fr.xephi.authme.libs.at.favre.lib.crypto.bcrypt.BCrypt.Result result = fr.xephi.authme.libs.at.favre.lib.crypto.bcrypt.BCrypt.verifyer().verify(password.getBytes(StandardCharsets.UTF_8), hash.getBytes(StandardCharsets.UTF_8));
         return result.verified;
      } else {
         return false;
      }
   }

   public static String generateSalt() {
      return RandomStringUtils.generateLowerUpper(16);
   }
}
