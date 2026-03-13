package github.nighter.smartspawner.libs.mariadb.plugin.authentication.standard;

import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.socket.Reader;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.message.server.AuthSwitchPacket;
import github.nighter.smartspawner.libs.mariadb.plugin.AuthenticationPlugin;
import github.nighter.smartspawner.libs.mariadb.plugin.Credential;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class NativePasswordPlugin implements AuthenticationPlugin {
   private final String authenticationData;
   private final byte[] seed;

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
         throw new IllegalStateException("Could not use SHA-1, failing", var9);
      }
   }

   public NativePasswordPlugin(String authenticationData, byte[] seed) {
      this.seed = seed;
      this.authenticationData = authenticationData;
   }

   public ReadableByteBuf process(Writer out, Reader in, Context context, boolean sslFingerPrintValidation) throws IOException {
      if (this.authenticationData == null) {
         out.writeEmptyPacket();
      } else {
         byte[] truncatedSeed = AuthSwitchPacket.getTruncatedSeed(this.seed);
         out.writeBytes(encryptPassword(this.authenticationData, truncatedSeed));
         out.flush();
      }

      return in.readReusablePacket();
   }

   public boolean isMitMProof() {
      return true;
   }

   public byte[] hash(Credential credential) {
      try {
         MessageDigest messageDigestSHA1 = MessageDigest.getInstance("SHA-1");
         byte[] bytePwd = credential.getPassword().getBytes(StandardCharsets.UTF_8);
         byte[] stage1 = messageDigestSHA1.digest(bytePwd);
         messageDigestSHA1.reset();
         byte[] stage2 = messageDigestSHA1.digest(stage1);
         messageDigestSHA1.reset();
         return stage2;
      } catch (NoSuchAlgorithmException var6) {
         throw new IllegalStateException("Could not use SHA-1, failing", var6);
      }
   }
}
