package fr.xephi.authme.libs.org.jboss.crypto;

import fr.xephi.authme.libs.org.jboss.crypto.digest.DigestCallback;
import fr.xephi.authme.libs.org.jboss.security.Base64Encoder;
import fr.xephi.authme.libs.org.jboss.security.Base64Utils;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtil {
   private static final int HASH_LEN = 20;
   public static final String BASE64_ENCODING = "BASE64";
   public static final String BASE16_ENCODING = "HEX";
   public static final String RFC2617_ENCODING = "RFC2617";
   private static char[] MD5_HEX = "0123456789abcdef".toCharArray();
   private static SecureRandom psuedoRng;
   private static MessageDigest sha1Digest;
   private static boolean initialized;

   public static void init() throws NoSuchAlgorithmException {
      if (!initialized) {
         init((byte[])null);
      }
   }

   public static void init(byte[] prngSeed) throws NoSuchAlgorithmException {
      sha1Digest = MessageDigest.getInstance("SHA");
      psuedoRng = SecureRandom.getInstance("SHA1PRNG");
      if (prngSeed != null) {
         psuedoRng.setSeed(prngSeed);
      }

      Provider provider = new JBossSXProvider();
      Security.addProvider(provider);
      initialized = true;
   }

   public static MessageDigest newDigest() {
      MessageDigest md = null;

      try {
         md = (MessageDigest)sha1Digest.clone();
      } catch (CloneNotSupportedException var2) {
      }

      return md;
   }

   public static MessageDigest copy(MessageDigest md) {
      MessageDigest copy = null;

      try {
         copy = (MessageDigest)md.clone();
      } catch (CloneNotSupportedException var3) {
      }

      return copy;
   }

   public static Random getPRNG() {
      return psuedoRng;
   }

   public static double nextDouble() {
      return psuedoRng.nextDouble();
   }

   public static long nextLong() {
      return psuedoRng.nextLong();
   }

   public static void nextBytes(byte[] bytes) {
      psuedoRng.nextBytes(bytes);
   }

   public static byte[] generateSeed(int numBytes) {
      return psuedoRng.generateSeed(numBytes);
   }

   public static byte[] calculatePasswordHash(String username, char[] password, byte[] salt) {
      MessageDigest xd = newDigest();
      byte[] user = null;
      byte[] colon = new byte[0];

      byte[] user;
      try {
         user = username.getBytes("UTF-8");
         colon = ":".getBytes("UTF-8");
      } catch (UnsupportedEncodingException var12) {
         PicketBoxLogger.LOGGER.errorConvertingUsernameUTF8(var12);
         user = username.getBytes();
         colon = ":".getBytes();
      }

      byte[] passBytes = new byte[2 * password.length];
      int passBytesLength = 0;

      for(int p = 0; p < password.length; ++p) {
         int c = password[p] & '\uffff';
         byte b0 = (byte)(c & 255);
         byte b1 = (byte)((c & '\uff00') >> 8);
         passBytes[passBytesLength++] = b0;
         if (c > 255) {
            passBytes[passBytesLength++] = b1;
         }
      }

      xd.update(user);
      xd.update(colon);
      xd.update(passBytes, 0, passBytesLength);
      byte[] h = xd.digest();
      xd.reset();
      xd.update(salt);
      xd.update(h);
      byte[] xb = xd.digest();
      return xb;
   }

   public static byte[] calculateVerifier(String username, char[] password, byte[] salt, byte[] Nb, byte[] gb) {
      BigInteger g = new BigInteger(1, gb);
      BigInteger N = new BigInteger(1, Nb);
      return calculateVerifier(username, password, salt, N, g);
   }

   public static byte[] calculateVerifier(String username, char[] password, byte[] salt, BigInteger N, BigInteger g) {
      byte[] xb = calculatePasswordHash(username, password, salt);
      BigInteger x = new BigInteger(1, xb);
      BigInteger v = g.modPow(x, N);
      return v.toByteArray();
   }

   public static byte[] sessionKeyHash(byte[] number) {
      int offset;
      for(offset = 0; offset < number.length && number[offset] == 0; ++offset) {
      }

      byte[] key = new byte[40];
      int klen = (number.length - offset) / 2;
      byte[] hbuf = new byte[klen];

      int i;
      for(i = 0; i < klen; ++i) {
         hbuf[i] = number[number.length - 2 * i - 1];
      }

      byte[] hout = newDigest().digest(hbuf);

      for(i = 0; i < 20; ++i) {
         key[2 * i] = hout[i];
      }

      for(i = 0; i < klen; ++i) {
         hbuf[i] = number[number.length - 2 * i - 2];
      }

      hout = newDigest().digest(hbuf);

      for(i = 0; i < 20; ++i) {
         key[2 * i + 1] = hout[i];
      }

      return key;
   }

   public static byte[] trim(byte[] in) {
      if (in.length != 0 && in[0] == 0) {
         int len = in.length;

         int i;
         for(i = 1; in[i] == 0 && i < len; ++i) {
         }

         byte[] ret = new byte[len - i];
         System.arraycopy(in, i, ret, 0, len - i);
         return ret;
      } else {
         return in;
      }
   }

   public static byte[] xor(byte[] b1, byte[] b2, int length) {
      byte[] result = new byte[length];

      for(int i = 0; i < length; ++i) {
         result[i] = (byte)(b1[i] ^ b2[i]);
      }

      return result;
   }

   public static String encodeRFC2617(byte[] data) {
      char[] hash = new char[32];

      for(int i = 0; i < 16; ++i) {
         int j = data[i] >> 4 & 15;
         hash[i * 2] = MD5_HEX[j];
         j = data[i] & 15;
         hash[i * 2 + 1] = MD5_HEX[j];
      }

      return new String(hash);
   }

   public static String encodeBase16(byte[] bytes) {
      StringBuffer sb = new StringBuffer(bytes.length * 2);

      for(int i = 0; i < bytes.length; ++i) {
         byte b = bytes[i];
         char c = (char)(b >> 4 & 15);
         if (c > '\t') {
            c = (char)(c - 10 + 97);
         } else {
            c = (char)(c + 48);
         }

         sb.append(c);
         c = (char)(b & 15);
         if (c > '\t') {
            c = (char)(c - 10 + 97);
         } else {
            c = (char)(c + 48);
         }

         sb.append(c);
      }

      return sb.toString();
   }

   public static String encodeBase64(byte[] bytes) {
      String base64 = null;

      try {
         base64 = Base64Encoder.encode(bytes);
      } catch (Exception var3) {
      }

      return base64;
   }

   public static String createPasswordHash(String hashAlgorithm, String hashEncoding, String hashCharset, String username, String password) {
      return createPasswordHash(hashAlgorithm, hashEncoding, hashCharset, username, password, (DigestCallback)null);
   }

   public static String createPasswordHash(String hashAlgorithm, String hashEncoding, String hashCharset, String username, String password, DigestCallback callback) {
      String passwordHash = null;

      byte[] passBytes;
      try {
         if (hashCharset == null) {
            passBytes = password.getBytes();
         } else {
            passBytes = password.getBytes(hashCharset);
         }
      } catch (UnsupportedEncodingException var11) {
         PicketBoxLogger.LOGGER.errorFindingCharset(hashCharset, var11);
         passBytes = password.getBytes();
      }

      try {
         MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
         if (callback != null) {
            callback.preDigest(md);
         }

         md.update(passBytes);
         if (callback != null) {
            callback.postDigest(md);
         }

         byte[] hash = md.digest();
         if (hashEncoding.equalsIgnoreCase("BASE64")) {
            passwordHash = encodeBase64(hash);
         } else if (hashEncoding.equalsIgnoreCase("HEX")) {
            passwordHash = encodeBase16(hash);
         } else if (hashEncoding.equalsIgnoreCase("RFC2617")) {
            passwordHash = encodeRFC2617(hash);
         } else {
            PicketBoxLogger.LOGGER.unsupportedHashEncodingFormat(hashEncoding);
         }
      } catch (Exception var10) {
         PicketBoxLogger.LOGGER.errorCalculatingPasswordHash(var10);
      }

      return passwordHash;
   }

   public static String tob64(byte[] buffer) {
      return Base64Utils.tob64(buffer);
   }

   public static byte[] fromb64(String str) throws NumberFormatException {
      return Base64Utils.fromb64(str);
   }

   public static boolean hasUnlimitedCrypto() {
      boolean hasUnlimitedCrypto = false;

      try {
         hasUnlimitedCrypto = Cipher.getMaxAllowedKeyLength("Blowfish") == Integer.MAX_VALUE;
      } catch (Throwable var2) {
         PicketBoxLogger.LOGGER.errorCheckingStrongJurisdictionPolicyFiles(var2);
      }

      return hasUnlimitedCrypto;
   }

   public static Object createSecretKey(String cipherAlgorithm, Object key) throws KeyException {
      Class<?>[] signature = new Class[]{key.getClass(), String.class};
      Object[] args = new Object[]{key, cipherAlgorithm};
      Object secretKey = null;

      try {
         Class<?> secretKeySpecClass = SecretKeySpec.class;
         Constructor<?> ctor = secretKeySpecClass.getDeclaredConstructor(signature);
         secretKey = ctor.newInstance(args);
         return secretKey;
      } catch (Exception var7) {
         throw PicketBoxMessages.MESSAGES.failedToCreateSecretKeySpec(var7);
      } catch (Throwable var8) {
         throw PicketBoxMessages.MESSAGES.unexpectedExceptionDuringSecretKeyCreation(var8);
      }
   }

   public static Object createCipher(String cipherAlgorithm) throws GeneralSecurityException {
      Cipher cipher = Cipher.getInstance(cipherAlgorithm);
      return cipher;
   }

   public static Object createSealedObject(String cipherAlgorithm, Object key, byte[] cipherIV, Serializable data) throws GeneralSecurityException {
      SealedObject sealedObject = null;

      try {
         Cipher cipher = Cipher.getInstance(cipherAlgorithm);
         SecretKey skey = (SecretKey)key;
         if (cipherIV != null) {
            IvParameterSpec iv = new IvParameterSpec(cipherIV);
            cipher.init(1, skey, iv);
         } else {
            cipher.init(1, skey);
         }

         sealedObject = new SealedObject(data, cipher);
         return sealedObject;
      } catch (GeneralSecurityException var8) {
         throw var8;
      } catch (Throwable var9) {
         throw PicketBoxMessages.MESSAGES.failedToCreateSealedObject(var9);
      }
   }

   public static Object accessSealedObject(String cipherAlgorithm, Object key, byte[] cipherIV, Object obj) throws GeneralSecurityException {
      Object data = null;

      try {
         Cipher cipher = Cipher.getInstance(cipherAlgorithm);
         SecretKey skey = (SecretKey)key;
         if (cipherIV != null) {
            IvParameterSpec iv = new IvParameterSpec(cipherIV);
            cipher.init(2, skey, iv);
         } else {
            cipher.init(2, skey);
         }

         SealedObject sealedObj = (SealedObject)obj;
         data = sealedObj.getObject(cipher);
         return data;
      } catch (GeneralSecurityException var8) {
         throw var8;
      } catch (Throwable var9) {
         throw PicketBoxMessages.MESSAGES.failedToCreateSealedObject(var9);
      }
   }
}
