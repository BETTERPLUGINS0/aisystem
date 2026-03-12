package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.HashUtils;
import fr.xephi.authme.security.crypts.description.AsciiRestricted;
import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.Usage;
import fr.xephi.authme.util.RandomStringUtils;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Recommendation(Usage.DO_NOT_USE)
@AsciiRestricted
public class PhpFusion extends SeparateSaltMethod {
   public String computeHash(String password, String salt, String name) {
      String algo = "HmacSHA256";
      String keyString = HashUtils.sha1(salt);

      try {
         SecretKeySpec key = new SecretKeySpec(keyString.getBytes("UTF-8"), algo);
         Mac mac = Mac.getInstance(algo);
         mac.init(key);
         byte[] bytes = mac.doFinal(password.getBytes("ASCII"));
         StringBuilder hash = new StringBuilder();
         byte[] var10 = bytes;
         int var11 = bytes.length;

         for(int var12 = 0; var12 < var11; ++var12) {
            byte aByte = var10[var12];
            String hex = Integer.toHexString(255 & aByte);
            if (hex.length() == 1) {
               hash.append('0');
            }

            hash.append(hex);
         }

         return hash.toString();
      } catch (InvalidKeyException | NoSuchAlgorithmException | UnsupportedEncodingException var15) {
         throw new UnsupportedOperationException("Cannot create PHPFUSION hash for " + name, var15);
      }
   }

   public String generateSalt() {
      return RandomStringUtils.generateHex(12);
   }
}
