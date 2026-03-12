package fr.xephi.authme.libs.de.mkammerer.argon2;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import fr.xephi.authme.libs.de.mkammerer.argon2.jna.Argon2Library;
import fr.xephi.authme.libs.de.mkammerer.argon2.jna.Argon2_context;
import fr.xephi.authme.libs.de.mkammerer.argon2.jna.JnaUint32;
import fr.xephi.authme.libs.de.mkammerer.argon2.jna.Size_t;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class BaseArgon2 implements Argon2, Argon2Advanced {
   private static final String ASCII = "ASCII";
   private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
   private static final Pattern HASH_PATTERN = Pattern.compile("^\\$argon2[id]{1,2}\\$v=\\d+\\$m=(\\d+),t=(\\d+),p=(\\d+)\\$.+$");
   private final SecureRandom secureRandom = new SecureRandom();
   private final int defaultSaltLength;
   private final int defaultHashLength;

   BaseArgon2(int defaultSaltLength, int defaultHashLength) {
      this.defaultSaltLength = defaultSaltLength;
      this.defaultHashLength = defaultHashLength;
   }

   public String hash(int iterations, int memory, int parallelism, char[] password) {
      return this.hash(iterations, memory, parallelism, password, DEFAULT_CHARSET);
   }

   public String hash(int iterations, int memory, int parallelism, byte[] data) {
      return this.hashBytes(iterations, memory, parallelism, data);
   }

   public boolean verify(String hash, byte[] data) {
      return this.verifyBytes(hash, data);
   }

   public String hash(int iterations, int memory, int parallelism, char[] password, Charset charset) {
      byte[] pwd = toByteArray(password, charset);

      String var7;
      try {
         var7 = this.hashBytes(iterations, memory, parallelism, pwd);
      } finally {
         this.wipeArray(pwd);
      }

      return var7;
   }

   public String hash(int iterations, int memory, int parallelism, String password, Charset charset) {
      byte[] pwd = password.getBytes(charset);

      String var7;
      try {
         var7 = this.hashBytes(iterations, memory, parallelism, pwd);
      } finally {
         this.wipeArray(pwd);
      }

      return var7;
   }

   public String hash(int iterations, int memory, int parallelism, String password) {
      return this.hash(iterations, memory, parallelism, password, DEFAULT_CHARSET);
   }

   public String hash(int iterations, int memory, int parallelism, char[] password, Charset charset, byte[] salt) {
      byte[] pwd = toByteArray(password, charset);

      String var8;
      try {
         var8 = this.hashBytes(iterations, memory, parallelism, pwd, salt);
      } finally {
         this.wipeArray(pwd);
      }

      return var8;
   }

   public byte[] rawHash(int iterations, int memory, int parallelism, char[] password, byte[] salt) {
      return this.rawHash(iterations, memory, parallelism, password, DEFAULT_CHARSET, salt);
   }

   public byte[] rawHash(int iterations, int memory, int parallelism, byte[] data, byte[] salt) {
      return this.rawHashBytes(iterations, memory, parallelism, data, salt, this.defaultHashLength);
   }

   public byte[] rawHash(int iterations, int memory, int parallelism, char[] password, Charset charset, byte[] salt) {
      byte[] pwd = toByteArray(password, charset);

      byte[] var8;
      try {
         var8 = this.rawHashBytes(iterations, memory, parallelism, pwd, salt, this.defaultHashLength);
      } finally {
         this.wipeArray(pwd);
      }

      return var8;
   }

   public byte[] rawHash(int iterations, int memory, int parallelism, String password, Charset charset, byte[] salt) {
      byte[] pwd = password.getBytes(charset);

      byte[] var8;
      try {
         var8 = this.rawHashBytes(iterations, memory, parallelism, pwd, salt, this.defaultHashLength);
      } finally {
         this.wipeArray(pwd);
      }

      return var8;
   }

   public byte[] rawHash(int iterations, int memory, int parallelism, String password, byte[] salt) {
      return this.rawHash(iterations, memory, parallelism, password, DEFAULT_CHARSET, salt);
   }

   public boolean verify(String hash, String password) {
      return this.verify(hash, password, DEFAULT_CHARSET);
   }

   public boolean verify(String hash, String password, Charset charset) {
      byte[] pwd = password.getBytes(charset);

      boolean var5;
      try {
         var5 = this.verifyBytes(hash, pwd);
      } finally {
         this.wipeArray(pwd);
      }

      return var5;
   }

   public boolean verify(String hash, char[] password, Charset charset) {
      byte[] pwd = toByteArray(password, charset);

      boolean var5;
      try {
         var5 = this.verifyBytes(hash, pwd);
      } finally {
         this.wipeArray(pwd);
      }

      return var5;
   }

   public boolean verify(String hash, char[] password) {
      return this.verify(hash, password, DEFAULT_CHARSET);
   }

   public void wipeArray(byte[] array) {
      Arrays.fill(array, (byte)0);
   }

   public void wipeArray(char[] array) {
      Arrays.fill(array, '\u0000');
   }

   public byte[] pbkdf(int iterations, int memory, int parallelism, char[] password, Charset charset, byte[] salt, int keyLength) {
      byte[] pwd = toByteArray(password, charset);

      byte[] var9;
      try {
         var9 = this.pbkdf(iterations, memory, parallelism, pwd, salt, keyLength);
      } finally {
         this.wipeArray(pwd);
      }

      return var9;
   }

   public byte[] pbkdf(int iterations, int memory, int parallelism, byte[] password, byte[] salt, int keyLength) {
      return this.rawHashBytes(iterations, memory, parallelism, password, salt, keyLength);
   }

   public boolean needsRehash(String hash, int iterations, int memory, int parallelism) {
      Matcher matcher = HASH_PATTERN.matcher(hash);
      if (!matcher.matches()) {
         throw new IllegalArgumentException("Invalid hash '" + hash + "'");
      } else {
         int actualMemory = Integer.parseInt(matcher.group(1));
         int actualIterations = Integer.parseInt(matcher.group(2));
         int actualParallelism = Integer.parseInt(matcher.group(3));
         return actualMemory < memory || actualIterations < iterations || actualParallelism < parallelism;
      }
   }

   public HashResult hashAdvanced(int iterations, int memory, int parallelism, byte[] password, byte[] salt, int hashLength, Argon2Version version) {
      JnaUint32 jnaIterations = new JnaUint32(iterations);
      JnaUint32 jnaMemory = new JnaUint32(memory);
      JnaUint32 jnaParallelism = new JnaUint32(parallelism);
      byte[] hash = new byte[hashLength];
      int len = Argon2Library.INSTANCE.argon2_encodedlen(jnaIterations, jnaMemory, jnaParallelism, new JnaUint32(salt.length), new JnaUint32(hashLength), this.getType().getJnaType()).intValue();
      byte[] encoded = new byte[len];
      int result = Argon2Library.INSTANCE.argon2_hash(jnaIterations, jnaMemory, jnaParallelism, password, new Size_t((long)password.length), salt, new Size_t((long)salt.length), hash, new Size_t((long)hash.length), encoded, new Size_t((long)encoded.length), this.getType().getJnaType(), version.getJnaType());
      this.checkResult(result);
      return new HashResult(hash, Native.toString(encoded, "ASCII"));
   }

   public byte[] rawHashAdvanced(int iterations, int memory, int parallelism, char[] password, Charset charset, byte[] salt, byte[] secret, byte[] associatedData) {
      byte[] pwd = toByteArray(password, charset);
      return this.rawHashAdvanced(iterations, memory, parallelism, pwd, salt, secret, associatedData, this.defaultHashLength, Argon2Version.DEFAULT_VERSION);
   }

   public byte[] rawHashAdvanced(int iterations, int memory, int parallelism, byte[] password, byte[] salt, byte[] secret, byte[] associatedData, int hashLength, Argon2Version version) {
      if (hashLength <= 0) {
         throw new IllegalArgumentException("hashLength must be greater than zero");
      } else {
         Argon2_context.ByReference context = buildContextReference(iterations, memory, parallelism, hashLength, password, salt, version, secret, associatedData);
         int result = this.callLibraryContext(context);
         wipeMemory(context);
         this.checkResult(result);
         return context.out.getByteArray(0L, hashLength);
      }
   }

   public boolean verifyAdvanced(int iterations, int memory, int parallelism, char[] password, Charset charset, byte[] salt, byte[] secret, byte[] associatedData, byte[] rawHash) {
      byte[] pwd = toByteArray(password, charset);
      return this.verifyAdvanced(iterations, memory, parallelism, pwd, salt, secret, associatedData, this.defaultHashLength, Argon2Version.DEFAULT_VERSION, rawHash);
   }

   public boolean verifyAdvanced(int iterations, int memory, int parallelism, byte[] password, byte[] salt, byte[] secret, byte[] associatedData, int hashLength, Argon2Version version, byte[] rawHash) {
      if (hashLength <= 0) {
         throw new IllegalArgumentException("hashLength must be greater than zero");
      } else {
         Argon2_context.ByReference context = buildContextReference(iterations, memory, parallelism, hashLength, password, salt, version, secret, associatedData);
         int result = this.callLibraryVerifyContext(context, rawHash);
         wipeMemory(context);
         return result == 0;
      }
   }

   public byte[] generateSalt() {
      return this.generateSalt(this.defaultSaltLength);
   }

   public byte[] generateSalt(int lengthInBytes) {
      byte[] salt = new byte[lengthInBytes];
      this.secureRandom.nextBytes(salt);
      return salt;
   }

   protected int getDefaultHashLength() {
      return this.defaultHashLength;
   }

   protected abstract Argon2Factory.Argon2Types getType();

   protected abstract int callLibraryHash(byte[] var1, byte[] var2, JnaUint32 var3, JnaUint32 var4, JnaUint32 var5, byte[] var6);

   protected abstract int callLibraryRawHash(byte[] var1, byte[] var2, JnaUint32 var3, JnaUint32 var4, JnaUint32 var5, byte[] var6);

   protected abstract int callLibraryVerify(byte[] var1, byte[] var2);

   protected abstract int callLibraryContext(Argon2_context.ByReference var1);

   protected abstract int callLibraryVerifyContext(Argon2_context.ByReference var1, byte[] var2);

   private String hashBytes(int iterations, int memory, int parallelism, byte[] pwd) {
      byte[] salt = this.generateSalt();
      return this.hashBytes(iterations, memory, parallelism, pwd, salt);
   }

   private String hashBytes(int iterations, int memory, int parallelism, byte[] pwd, byte[] salt) {
      JnaUint32 jnaIterations = new JnaUint32(iterations);
      JnaUint32 jnaMemory = new JnaUint32(memory);
      JnaUint32 jnaParallelism = new JnaUint32(parallelism);
      int len = Argon2Library.INSTANCE.argon2_encodedlen(jnaIterations, jnaMemory, jnaParallelism, new JnaUint32(salt.length), new JnaUint32(this.defaultHashLength), this.getType().getJnaType()).intValue();
      byte[] encoded = new byte[len];
      int result = this.callLibraryHash(pwd, salt, jnaIterations, jnaMemory, jnaParallelism, encoded);
      this.checkResult(result);
      return Native.toString(encoded, "ASCII");
   }

   private void checkResult(int result) {
      if (result != 0) {
         String errMsg = Argon2Library.INSTANCE.argon2_error_message(result);
         throw new IllegalStateException(String.format("%s (%d)", errMsg, result));
      }
   }

   private byte[] rawHashBytes(int iterations, int memory, int parallelism, byte[] pwd, byte[] salt, int hashLength) {
      JnaUint32 jnaIterations = new JnaUint32(iterations);
      JnaUint32 jnaMemory = new JnaUint32(memory);
      JnaUint32 jnaParallelism = new JnaUint32(parallelism);
      byte[] hash = new byte[hashLength];
      int result = this.callLibraryRawHash(pwd, salt, jnaIterations, jnaMemory, jnaParallelism, hash);
      this.checkResult(result);
      return hash;
   }

   private boolean verifyBytes(String hash, byte[] pwd) {
      byte[] encoded = Native.toByteArray(hash, "ASCII");
      int result = this.callLibraryVerify(encoded, pwd);
      return result == 0;
   }

   private static byte[] toByteArray(char[] chars, Charset charset) {
      assert chars != null;

      CharBuffer charBuffer = CharBuffer.wrap(chars);
      ByteBuffer byteBuffer = charset.encode(charBuffer);
      byte[] bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
      Arrays.fill(byteBuffer.array(), (byte)0);
      return bytes;
   }

   private static Argon2_context.ByReference buildContextReference(int iterations, int memory, int parallelism, int hashLength, byte[] password, byte[] salt, Argon2Version version, byte[] secret, byte[] associatedData) {
      Argon2_context.ByReference context = new Argon2_context.ByReference();
      context.out = new Memory((long)hashLength);
      context.outlen = new JnaUint32(hashLength);
      context.pwd = new Memory((long)password.length);
      context.pwd.write(0L, (byte[])password, 0, password.length);
      context.pwdlen = new JnaUint32(password.length);
      context.salt = new Memory((long)salt.length);
      context.salt.write(0L, (byte[])salt, 0, salt.length);
      context.saltlen = new JnaUint32(salt.length);
      context.t_cost = new JnaUint32(iterations);
      context.m_cost = new JnaUint32(memory);
      context.lanes = new JnaUint32(parallelism);
      context.threads = new JnaUint32(parallelism);
      context.version = version.getJnaType();
      if (secret != null) {
         context.secret = new Memory((long)secret.length);
         context.secret.write(0L, (byte[])secret, 0, secret.length);
         context.secretlen = new JnaUint32(secret.length);
      }

      if (associatedData != null) {
         context.ad = new Memory((long)associatedData.length);
         context.ad.write(0L, (byte[])associatedData, 0, associatedData.length);
         context.adlen = new JnaUint32(associatedData.length);
      }

      context.allocate_cbk = Pointer.NULL;
      context.free_cbk = Pointer.NULL;
      context.flags = new JnaUint32(0);
      return context;
   }

   private static void wipeMemory(Argon2_context context) {
      context.pwd.clear(context.pwdlen.longValue());
      context.salt.clear(context.saltlen.longValue());
      if (context.secretlen.longValue() != 0L && context.secret != Pointer.NULL) {
         context.secret.clear(context.secretlen.longValue());
      }

      if (context.adlen.longValue() != 0L && context.ad != Pointer.NULL) {
         context.ad.clear(context.adlen.longValue());
      }

   }
}
