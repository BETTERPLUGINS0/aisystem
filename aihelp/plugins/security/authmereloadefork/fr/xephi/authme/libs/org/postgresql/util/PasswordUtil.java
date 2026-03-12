package fr.xephi.authme.libs.org.postgresql.util;

import fr.xephi.authme.libs.org.postgresql.core.Utils;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.ScramFunctions;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.ScramMechanisms;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.stringprep.StringPreparations;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;

public class PasswordUtil {
   private static final int DEFAULT_ITERATIONS = 4096;
   private static final int DEFAULT_SALT_LENGTH = 16;

   private static SecureRandom getSecureRandom() {
      return PasswordUtil.SecureRandomHolder.INSTANCE;
   }

   public static String encodeScramSha256(char[] password, int iterations, byte[] salt) {
      Objects.requireNonNull(password, "password");
      Objects.requireNonNull(salt, "salt");
      if (iterations <= 0) {
         throw new IllegalArgumentException("iterations must be greater than zero");
      } else if (salt.length == 0) {
         throw new IllegalArgumentException("salt length must be greater than zero");
      } else {
         String var8;
         try {
            String passwordText = String.valueOf(password);
            byte[] saltedPassword = ScramFunctions.saltedPassword(ScramMechanisms.SCRAM_SHA_256, StringPreparations.SASL_PREPARATION, passwordText, salt, iterations);
            byte[] clientKey = ScramFunctions.clientKey(ScramMechanisms.SCRAM_SHA_256, saltedPassword);
            byte[] storedKey = ScramFunctions.storedKey(ScramMechanisms.SCRAM_SHA_256, clientKey);
            byte[] serverKey = ScramFunctions.serverKey(ScramMechanisms.SCRAM_SHA_256, saltedPassword);
            var8 = "SCRAM-SHA-256$" + iterations + ":" + Base64.toBase64String(salt) + "$" + Base64.toBase64String(storedKey) + ":" + Base64.toBase64String(serverKey);
         } finally {
            Arrays.fill(password, '\u0000');
         }

         return var8;
      }
   }

   public static String encodeScramSha256(char[] password) {
      Objects.requireNonNull(password, "password");

      String var3;
      try {
         SecureRandom rng = getSecureRandom();
         byte[] salt = rng.generateSeed(16);
         var3 = encodeScramSha256(password, 4096, salt);
      } finally {
         Arrays.fill(password, '\u0000');
      }

      return var3;
   }

   /** @deprecated */
   @Deprecated
   public static String encodeMd5(String user, char[] password) {
      Objects.requireNonNull(user, "user");
      Objects.requireNonNull(password, "password");
      ByteBuffer passwordBytes = null;
      boolean var15 = false;

      String var7;
      try {
         var15 = true;
         passwordBytes = StandardCharsets.UTF_8.encode(CharBuffer.wrap(password));
         byte[] userBytes = user.getBytes(StandardCharsets.UTF_8);
         MessageDigest md = MessageDigest.getInstance("MD5");
         md.update(passwordBytes);
         md.update(userBytes);
         byte[] digest = md.digest();
         byte[] encodedPassword = new byte[35];
         encodedPassword[0] = 109;
         encodedPassword[1] = 100;
         encodedPassword[2] = 53;
         MD5Digest.bytesToHex(digest, encodedPassword, 3);
         var7 = new String(encodedPassword, StandardCharsets.UTF_8);
         var15 = false;
      } catch (NoSuchAlgorithmException var16) {
         throw new IllegalStateException("Unable to encode password with MD5", var16);
      } finally {
         if (var15) {
            Arrays.fill(password, '\u0000');
            if (passwordBytes != null) {
               if (passwordBytes.hasArray()) {
                  Arrays.fill(passwordBytes.array(), (byte)0);
               } else {
                  int limit = passwordBytes.limit();

                  for(int i = 0; i < limit; ++i) {
                     passwordBytes.put(i, (byte)0);
                  }
               }
            }

         }
      }

      Arrays.fill(password, '\u0000');
      if (passwordBytes != null) {
         if (passwordBytes.hasArray()) {
            Arrays.fill(passwordBytes.array(), (byte)0);
         } else {
            int limit = passwordBytes.limit();

            for(int i = 0; i < limit; ++i) {
               passwordBytes.put(i, (byte)0);
            }
         }
      }

      return var7;
   }

   public static String encodePassword(String user, char[] password, String encryptionType) throws SQLException {
      Objects.requireNonNull(password, "password");
      Objects.requireNonNull(encryptionType, "encryptionType");
      byte var4 = -1;
      switch(encryptionType.hashCode()) {
      case -633128269:
         if (encryptionType.equals("scram-sha-256")) {
            var4 = 3;
         }
         break;
      case 3551:
         if (encryptionType.equals("on")) {
            var4 = 0;
         }
         break;
      case 107902:
         if (encryptionType.equals("md5")) {
            var4 = 2;
         }
         break;
      case 109935:
         if (encryptionType.equals("off")) {
            var4 = 1;
         }
      }

      switch(var4) {
      case 0:
      case 1:
      case 2:
         return encodeMd5(user, password);
      case 3:
         return encodeScramSha256(password);
      default:
         Arrays.fill(password, '\u0000');
         throw new PSQLException("Unable to determine encryption type: " + encryptionType, PSQLState.SYSTEM_ERROR);
      }
   }

   public static String genAlterUserPasswordSQL(String user, char[] password, String encryptionType) throws SQLException {
      String var5;
      try {
         String encodedPassword = encodePassword(user, password, encryptionType);
         StringBuilder sb = new StringBuilder();
         sb.append("ALTER USER ");
         Utils.escapeIdentifier(sb, user);
         sb.append(" PASSWORD '");
         Utils.escapeLiteral(sb, encodedPassword, true);
         sb.append("'");
         var5 = sb.toString();
      } finally {
         Arrays.fill(password, '\u0000');
      }

      return var5;
   }

   private static class SecureRandomHolder {
      static final SecureRandom INSTANCE = new SecureRandom();
   }
}
