package github.nighter.smartspawner.libs.mariadb.plugin.authentication.standard;

import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.socket.Reader;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.plugin.AuthenticationPlugin;
import github.nighter.smartspawner.libs.mariadb.plugin.Credential;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.sql.SQLException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class ParsecPasswordPlugin implements AuthenticationPlugin {
   private static final byte[] pkcs8Ed25519header = new byte[]{48, 46, 2, 1, 0, 48, 5, 6, 3, 43, 101, 112, 4, 34, 4, 32};
   private final String authenticationData;
   private final byte[] seed;
   private byte[] hash;

   public ParsecPasswordPlugin(String authenticationData, byte[] seed) {
      this.seed = seed;
      this.authenticationData = authenticationData;
   }

   public ReadableByteBuf process(Writer out, Reader in, Context context, boolean sslFingerPrintValidation) throws SQLException, IOException {
      out.writeEmptyPacket();
      ReadableByteBuf buf = in.readReusablePacket();
      byte firstByte = 0;
      int iterations = 100;
      if (buf.getByte() == 1) {
         buf.skip();
      }

      if (buf.readableBytes() > 2) {
         firstByte = buf.readByte();
         iterations = buf.readByte();
      }

      if (firstByte == 80 && iterations <= 3) {
         byte[] salt = new byte[buf.readableBytes()];
         buf.readBytes(salt);
         char[] password = this.authenticationData == null ? new char[0] : this.authenticationData.toCharArray();

         KeyFactory ed25519KeyFactory;
         Signature ed25519Signature;
         try {
            ed25519KeyFactory = KeyFactory.getInstance("Ed25519");
            ed25519Signature = Signature.getInstance("Ed25519");
         } catch (NoSuchAlgorithmException var22) {
            try {
               ed25519KeyFactory = KeyFactory.getInstance("Ed25519", "BC");
               ed25519Signature = Signature.getInstance("Ed25519", "BC");
            } catch (NoSuchProviderException | NoSuchAlgorithmException var21) {
               throw new SQLException("Parsec authentication not available. Either use Java 15+ or add BouncyCastle dependency", var22);
            }
         }

         try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, 1024 << iterations, 256);
            SecretKey key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512").generateSecret(spec);
            byte[] derivedKey = key.getEncoded();
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(this.combineArray(pkcs8Ed25519header, derivedKey));
            PrivateKey privateKey = ed25519KeyFactory.generatePrivate(keySpec);
            byte[] rawPublicKey = ParsecPasswordPluginTool.process(derivedKey);
            this.hash = this.combineArray(this.combineArray(new byte[]{80, (byte)iterations}, salt), rawPublicKey);
            byte[] clientScramble = new byte[32];
            SecureRandom.getInstanceStrong().nextBytes(clientScramble);
            ed25519Signature.initSign(privateKey);
            ed25519Signature.update(this.combineArray(this.seed, clientScramble));
            byte[] signature = ed25519Signature.sign();
            out.writeBytes(clientScramble);
            out.writeBytes(signature);
            out.flush();
            return in.readReusablePacket();
         } catch (InvalidKeySpecException | InvalidKeyException | InvalidAlgorithmParameterException | SignatureException | NoSuchAlgorithmException var20) {
            throw new SQLException("Error during parsec authentication", var20);
         }
      } else {
         throw new SQLException("Wrong parsec authentication format", "S1009");
      }
   }

   public boolean isMitMProof() {
      return true;
   }

   public byte[] hash(Credential credential) {
      return this.hash;
   }

   private byte[] combineArray(byte[] arr1, byte[] arr2) {
      byte[] combined = new byte[arr1.length + arr2.length];
      System.arraycopy(arr1, 0, combined, 0, arr1.length);
      System.arraycopy(arr2, 0, combined, arr1.length, arr2.length);
      return combined;
   }
}
