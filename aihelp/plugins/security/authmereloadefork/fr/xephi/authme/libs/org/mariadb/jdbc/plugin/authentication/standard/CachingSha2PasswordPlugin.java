package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Reader;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.export.SslMode;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.client.AuthMoreRawPacket;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.server.AuthSwitchPacket;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.AuthenticationPlugin;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;

public class CachingSha2PasswordPlugin implements AuthenticationPlugin {
   public static final String TYPE = "caching_sha2_password";
   private String authenticationData;
   private byte[] seed;
   private Configuration conf;

   public static byte[] sha256encryptPassword(CharSequence password, byte[] seed) {
      if (password == null) {
         return new byte[0];
      } else {
         byte[] truncatedSeed = AuthSwitchPacket.getTruncatedSeed(seed);

         try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] bytePwd = password.toString().getBytes(StandardCharsets.UTF_8);
            byte[] stage1 = messageDigest.digest(bytePwd);
            messageDigest.reset();
            byte[] stage2 = messageDigest.digest(stage1);
            messageDigest.reset();
            messageDigest.update(stage2);
            messageDigest.update(truncatedSeed);
            byte[] digest = messageDigest.digest();
            byte[] returnBytes = new byte[digest.length];

            for(int i = 0; i < digest.length; ++i) {
               returnBytes[i] = (byte)(stage1[i] ^ digest[i]);
            }

            return returnBytes;
         } catch (NoSuchAlgorithmException var10) {
            throw new RuntimeException("Could not use SHA-256, failing", var10);
         }
      }
   }

   public static PublicKey readPublicKeyFromFile(String serverRsaPublicKeyFile) throws SQLException {
      byte[] keyBytes;
      try {
         keyBytes = Files.readAllBytes(Paths.get(serverRsaPublicKeyFile));
      } catch (IOException var3) {
         throw new SQLException("Could not read server RSA public key from file : serverRsaPublicKeyFile=" + serverRsaPublicKeyFile, "S1009", var3);
      }

      return generatePublicKey(keyBytes);
   }

   public static PublicKey generatePublicKey(byte[] publicKeyBytes) throws SQLException {
      try {
         String publicKey = (new String(publicKeyBytes, StandardCharsets.US_ASCII)).replaceAll("(-+BEGIN PUBLIC KEY-+\\r?\\n|\\n?-+END PUBLIC KEY-+\\r?\\n?)", "");
         byte[] keyBytes = Base64.getMimeDecoder().decode(publicKey);
         X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
         KeyFactory kf = KeyFactory.getInstance("RSA");
         return kf.generatePublic(spec);
      } catch (Exception var5) {
         throw new SQLException("Could read server RSA public key: " + var5.getMessage(), "S1009", var5);
      }
   }

   public static byte[] encrypt(PublicKey publicKey, String password, byte[] seed) throws SQLException {
      byte[] correctedSeed = Arrays.copyOfRange(seed, 0, seed.length - 1);
      byte[] bytePwd = password.getBytes(StandardCharsets.UTF_8);
      byte[] nullFinishedPwd = Arrays.copyOf(bytePwd, bytePwd.length + 1);
      byte[] xorBytes = new byte[nullFinishedPwd.length];
      int seedLength = correctedSeed.length;

      for(int i = 0; i < xorBytes.length; ++i) {
         xorBytes[i] = (byte)(nullFinishedPwd[i] ^ correctedSeed[i % seedLength]);
      }

      try {
         Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
         cipher.init(1, publicKey);
         return cipher.doFinal(xorBytes);
      } catch (Exception var9) {
         throw new SQLException("Error encoding password with public key : " + var9.getMessage(), "S1009", var9);
      }
   }

   public String type() {
      return "caching_sha2_password";
   }

   public void initialize(String authenticationData, byte[] seed, Configuration conf) {
      this.seed = seed;
      this.authenticationData = authenticationData;
      this.conf = conf;
   }

   public ReadableByteBuf process(Writer out, Reader in, Context context) throws IOException, SQLException {
      byte[] fastCryptPwd = sha256encryptPassword(this.authenticationData, this.seed);
      (new AuthMoreRawPacket(fastCryptPwd)).encode(out, context);
      ReadableByteBuf buf = in.readReusablePacket();
      switch(buf.getByte()) {
      case -1:
      case 0:
         return buf;
      default:
         byte[] authResult = new byte[buf.readIntLengthEncodedNotNull()];
         buf.readBytes(authResult);
         switch(authResult[0]) {
         case 3:
            return in.readReusablePacket();
         case 4:
            byte[] authMoreData;
            if (this.conf.sslMode() != SslMode.DISABLE) {
               byte[] bytePwd = this.authenticationData.getBytes();
               authMoreData = new byte[bytePwd.length + 1];
               System.arraycopy(bytePwd, 0, authMoreData, 0, bytePwd.length);
               (new AuthMoreRawPacket(authMoreData)).encode(out, context);
               out.flush();
            } else {
               PublicKey publicKey;
               if (this.conf.serverRsaPublicKeyFile() != null) {
                  if (this.conf.serverRsaPublicKeyFile().contains("BEGIN PUBLIC KEY")) {
                     publicKey = generatePublicKey(this.conf.serverRsaPublicKeyFile().getBytes());
                  } else {
                     publicKey = readPublicKeyFromFile(this.conf.serverRsaPublicKeyFile());
                  }
               } else {
                  if (!this.conf.allowPublicKeyRetrieval()) {
                     throw new SQLException("RSA public key is not available client side (option serverRsaPublicKeyFile not set)", "S1009");
                  }

                  out.writeByte(2);
                  out.flush();
                  buf = in.readReusablePacket();
                  switch(buf.getByte(0)) {
                  case -2:
                  case -1:
                     return buf;
                  default:
                     buf.skip();
                     authMoreData = new byte[buf.readableBytes()];
                     buf.readBytes(authMoreData);
                     publicKey = generatePublicKey(authMoreData);
                  }
               }

               authMoreData = encrypt(publicKey, this.authenticationData, this.seed);
               out.writeBytes(authMoreData);
               out.flush();
            }

            return in.readReusablePacket();
         default:
            throw new SQLException("Protocol exchange error. Expect login success or RSA login request message", "S1009");
         }
      }
   }
}
