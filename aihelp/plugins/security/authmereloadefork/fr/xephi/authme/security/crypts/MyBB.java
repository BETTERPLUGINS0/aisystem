package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.HashUtils;
import fr.xephi.authme.security.crypts.description.HasSalt;
import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.SaltType;
import fr.xephi.authme.security.crypts.description.Usage;
import fr.xephi.authme.util.RandomStringUtils;

@Recommendation(Usage.ACCEPTABLE)
@HasSalt(
   value = SaltType.TEXT,
   length = 8
)
public class MyBB extends SeparateSaltMethod {
   public String computeHash(String password, String salt, String name) {
      return HashUtils.md5(HashUtils.md5(salt) + HashUtils.md5(password));
   }

   public String generateSalt() {
      return RandomStringUtils.generateLowerUpper(8);
   }
}
