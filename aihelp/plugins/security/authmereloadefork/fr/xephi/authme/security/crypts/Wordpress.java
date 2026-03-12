package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.HashUtils;
import fr.xephi.authme.security.MessageDigestAlgorithm;
import fr.xephi.authme.security.crypts.description.HasSalt;
import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.SaltType;
import fr.xephi.authme.security.crypts.description.Usage;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

@Recommendation(Usage.ACCEPTABLE)
@HasSalt(
   value = SaltType.TEXT,
   length = 9
)
public class Wordpress extends UnsaltedMethod {
   private static final String itoa64 = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
   private final SecureRandom randomGen = new SecureRandom();

   private String encode64(byte[] src, int count) {
      StringBuilder output = new StringBuilder();
      int i = 0;
      if (src.length < count) {
         byte[] t = new byte[count];
         System.arraycopy(src, 0, t, 0, src.length);
         Arrays.fill(t, src.length, count - 1, (byte)0);
         src = t;
      }

      do {
         int value = src[i] + (src[i] < 0 ? 256 : 0);
         ++i;
         output.append("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(value & 63));
         if (i < count) {
            value |= src[i] + (src[i] < 0 ? 256 : 0) << 8;
         }

         output.append("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(value >> 6 & 63));
         if (i++ >= count) {
            break;
         }

         if (i < count) {
            value |= src[i] + (src[i] < 0 ? 256 : 0) << 16;
         }

         output.append("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(value >> 12 & 63));
         if (i++ >= count) {
            break;
         }

         output.append("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(value >> 18 & 63));
      } while(i < count);

      return output.toString();
   }

   private String crypt(String password, String setting) {
      String output = "*0";
      if ((setting.length() < 2 ? setting : setting.substring(0, 2)).equalsIgnoreCase(output)) {
         output = "*1";
      }

      String id = setting.length() < 3 ? setting : setting.substring(0, 3);
      if (!id.equals("$P$") && !id.equals("$H$")) {
         return output;
      } else {
         int countLog2 = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".indexOf(setting.charAt(3));
         if (countLog2 >= 7 && countLog2 <= 30) {
            int count = 1 << countLog2;
            String salt = setting.substring(4, 12);
            if (salt.length() != 8) {
               return output;
            } else {
               MessageDigest md = HashUtils.getDigest(MessageDigestAlgorithm.MD5);
               byte[] pass = this.stringToUtf8(password);
               byte[] hash = md.digest(this.stringToUtf8(salt + password));

               do {
                  byte[] t = new byte[hash.length + pass.length];
                  System.arraycopy(hash, 0, t, 0, hash.length);
                  System.arraycopy(pass, 0, t, hash.length, pass.length);
                  hash = md.digest(t);
                  --count;
               } while(count > 0);

               output = setting.substring(0, 12);
               output = output + this.encode64(hash, 16);
               return output;
            }
         } else {
            return output;
         }
      }
   }

   private String gensaltPrivate(byte[] input) {
      String output = "$P$";
      int iterationCountLog2 = 8;
      output = output + "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(Math.min(iterationCountLog2 + 5, 30));
      output = output + this.encode64(input, 6);
      return output;
   }

   private byte[] stringToUtf8(String string) {
      try {
         return string.getBytes("UTF-8");
      } catch (UnsupportedEncodingException var3) {
         throw new UnsupportedOperationException("This system doesn't support UTF-8!", var3);
      }
   }

   public String computeHash(String password) {
      byte[] random = new byte[6];
      this.randomGen.nextBytes(random);
      return this.crypt(password, this.gensaltPrivate(this.stringToUtf8(new String(random))));
   }

   public boolean comparePassword(String password, HashedPassword hashedPassword, String name) {
      String hash = hashedPassword.getHash();
      String comparedHash = this.crypt(password, hash);
      return HashUtils.isEqual(hash, comparedHash);
   }
}
