package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Reader;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.server.AuthSwitchPacket;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.AuthenticationPlugin;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class NativePasswordPlugin implements AuthenticationPlugin {
   public static final String TYPE = "mysql_native_password";
   private String authenticationData;
   private byte[] seed;

   public static byte[] encryptPassword(CharSequence password, byte[] seed) {
      try {
         if (password == null) {
            return new byte[0];
         } else {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] bytePwd = password.toString().getBytes(StandardCharsets.UTF_8);
            byte[] stage1 = messageDigest.digest(bytePwd);
            messageDigest.reset();
            byte[] stage2 = messageDigest.digest(stage1);
            messageDigest.reset();
            messageDigest.update(seed);
            messageDigest.update(stage2);
            byte[] digest = messageDigest.digest();
            byte[] returnBytes = new byte[digest.length];

            for(int i = 0; i < digest.length; ++i) {
               returnBytes[i] = (byte)(stage1[i] ^ digest[i]);
            }

            return returnBytes;
         }
      } catch (NoSuchAlgorithmException var9) {
         throw new RuntimeException("Could not use SHA-1, failing", var9);
      }
   }

   public String type() {
      return "mysql_native_password";
   }

   public void initialize(String authenticationData, byte[] seed, Configuration conf) {
      this.seed = seed;
      this.authenticationData = authenticationData;
   }

   public ReadableByteBuf process(Writer out, Reader in, Context context) throws IOException {
      if (this.authenticationData == null) {
         out.writeEmptyPacket();
      } else {
         byte[] truncatedSeed = AuthSwitchPacket.getTruncatedSeed(this.seed);
         out.writeBytes(encryptPassword(this.authenticationData, truncatedSeed));
         out.flush();
      }

      return in.readReusablePacket();
   }
}
