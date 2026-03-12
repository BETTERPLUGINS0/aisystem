package fr.xephi.authme.libs.at.favre.lib.crypto.bcrypt;

import fr.xephi.authme.libs.at.favre.lib.bytes.Bytes;
import fr.xephi.authme.libs.at.favre.lib.bytes.BytesTransformer;
import fr.xephi.authme.libs.at.favre.lib.bytes.BytesValidators;
import fr.xephi.authme.libs.at.favre.lib.bytes.MutableBytes;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class BCrypt {
   private static final Charset DEFAULT_CHARSET;
   public static final int SALT_LENGTH = 16;
   public static final int MIN_COST = 4;
   public static final int MAX_COST = 31;
   static final byte MAJOR_VERSION = 50;
   static final int HASH_OUT_LENGTH = 23;
   static final byte SEPARATOR = 36;

   private BCrypt() {
   }

   public static BCrypt.Hasher withDefaults() {
      return new BCrypt.Hasher(BCrypt.Version.VERSION_2A, new SecureRandom(), LongPasswordStrategies.strict(BCrypt.Version.VERSION_2A));
   }

   public static BCrypt.Hasher with(BCrypt.Version version) {
      return new BCrypt.Hasher(version, new SecureRandom(), LongPasswordStrategies.strict(version));
   }

   public static BCrypt.Hasher with(SecureRandom secureRandom) {
      return new BCrypt.Hasher(BCrypt.Version.VERSION_2A, secureRandom, LongPasswordStrategies.strict(BCrypt.Version.VERSION_2A));
   }

   public static BCrypt.Hasher with(LongPasswordStrategy longPasswordStrategy) {
      return new BCrypt.Hasher(BCrypt.Version.VERSION_2A, new SecureRandom(), longPasswordStrategy);
   }

   public static BCrypt.Hasher with(BCrypt.Version version, LongPasswordStrategy longPasswordStrategy) {
      return new BCrypt.Hasher(version, new SecureRandom(), longPasswordStrategy);
   }

   public static BCrypt.Hasher with(BCrypt.Version version, SecureRandom secureRandom, LongPasswordStrategy longPasswordStrategy) {
      return new BCrypt.Hasher(version, secureRandom, longPasswordStrategy);
   }

   public static BCrypt.Verifyer verifyer() {
      return verifyer((BCrypt.Version)null, (LongPasswordStrategy)null);
   }

   public static BCrypt.Verifyer verifyer(BCrypt.Version version) {
      return new BCrypt.Verifyer(version, LongPasswordStrategies.strict(version));
   }

   public static BCrypt.Verifyer verifyer(BCrypt.Version version, LongPasswordStrategy longPasswordStrategy) {
      return new BCrypt.Verifyer(version, longPasswordStrategy);
   }

   static {
      DEFAULT_CHARSET = StandardCharsets.UTF_8;
   }

   public static final class Version {
      private static final BCryptFormatter DEFAULT_FORMATTER;
      private static final BCryptParser DEFAULT_PARSER;
      public static final int MAX_PW_LENGTH_BYTE = 72;
      /** @deprecated */
      @Deprecated
      public static final int DEFAULT_MAX_PW_LENGTH_BYTE = 71;
      public static final BCrypt.Version VERSION_2A;
      public static final BCrypt.Version VERSION_2B;
      public static final BCrypt.Version VERSION_2X;
      public static final BCrypt.Version VERSION_2Y;
      public static final BCrypt.Version VERSION_2Y_NO_NULL_TERMINATOR;
      public static final BCrypt.Version VERSION_BC;
      public static final List<BCrypt.Version> SUPPORTED_VERSIONS;
      public final byte[] versionIdentifier;
      public final boolean useOnly23bytesForHash;
      public final boolean appendNullTerminator;
      public final int allowedMaxPwLength;
      public final BCryptFormatter formatter;
      public final BCryptParser parser;

      private Version(byte[] versionIdentifier, BCryptFormatter formatter, BCryptParser parser) {
         this(versionIdentifier, true, true, 72, formatter, parser);
      }

      public Version(byte[] versionIdentifier, boolean useOnly23bytesForHash, boolean appendNullTerminator, int allowedMaxPwLength, BCryptFormatter formatter, BCryptParser parser) {
         this.versionIdentifier = versionIdentifier;
         this.useOnly23bytesForHash = useOnly23bytesForHash;
         this.appendNullTerminator = appendNullTerminator;
         this.allowedMaxPwLength = allowedMaxPwLength;
         this.formatter = formatter;
         this.parser = parser;
         if (allowedMaxPwLength > 72) {
            throw new IllegalArgumentException("allowed max pw length cannot be gt 72");
         }
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            BCrypt.Version version = (BCrypt.Version)o;
            return this.useOnly23bytesForHash == version.useOnly23bytesForHash && this.appendNullTerminator == version.appendNullTerminator && this.allowedMaxPwLength == version.allowedMaxPwLength && Arrays.equals(this.versionIdentifier, version.versionIdentifier);
         } else {
            return false;
         }
      }

      public int hashCode() {
         int result = Objects.hash(new Object[]{this.useOnly23bytesForHash, this.appendNullTerminator, this.allowedMaxPwLength});
         result = 31 * result + Arrays.hashCode(this.versionIdentifier);
         return result;
      }

      public String toString() {
         return "$" + new String(this.versionIdentifier) + "$";
      }

      static {
         DEFAULT_FORMATTER = new BCryptFormatter.Default(new Radix64Encoder.Default(), BCrypt.DEFAULT_CHARSET);
         DEFAULT_PARSER = new BCryptParser.Default(new Radix64Encoder.Default(), BCrypt.DEFAULT_CHARSET);
         VERSION_2A = new BCrypt.Version(new byte[]{50, 97}, DEFAULT_FORMATTER, DEFAULT_PARSER);
         VERSION_2B = new BCrypt.Version(new byte[]{50, 98}, DEFAULT_FORMATTER, DEFAULT_PARSER);
         VERSION_2X = new BCrypt.Version(new byte[]{50, 120}, DEFAULT_FORMATTER, DEFAULT_PARSER);
         VERSION_2Y = new BCrypt.Version(new byte[]{50, 121}, DEFAULT_FORMATTER, DEFAULT_PARSER);
         VERSION_2Y_NO_NULL_TERMINATOR = new BCrypt.Version(new byte[]{50, 121}, true, false, 72, DEFAULT_FORMATTER, DEFAULT_PARSER);
         VERSION_BC = new BCrypt.Version(new byte[]{50, 99}, false, false, 72, DEFAULT_FORMATTER, DEFAULT_PARSER);
         SUPPORTED_VERSIONS = Collections.unmodifiableList(Arrays.asList(VERSION_2A, VERSION_2B, VERSION_2X, VERSION_2Y));
      }
   }

   public static final class Result {
      public final BCrypt.HashData details;
      public final boolean validFormat;
      public final boolean verified;
      public final String formatErrorMessage;

      Result(IllegalBCryptFormatException e) {
         this((BCrypt.HashData)null, false, false, e.getMessage());
      }

      Result(BCrypt.HashData details, boolean verified) {
         this(details, true, verified, (String)null);
      }

      private Result(BCrypt.HashData details, boolean validFormat, boolean verified, String formatErrorMessage) {
         this.details = details;
         this.validFormat = validFormat;
         this.verified = verified;
         this.formatErrorMessage = formatErrorMessage;
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            BCrypt.Result result = (BCrypt.Result)o;
            return this.validFormat == result.validFormat && this.verified == result.verified && Objects.equals(this.details, result.details) && Objects.equals(this.formatErrorMessage, result.formatErrorMessage);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.details, this.validFormat, this.verified, this.formatErrorMessage});
      }

      public String toString() {
         return "Result{details=" + this.details + ", validFormat=" + this.validFormat + ", verified=" + this.verified + ", formatErrorMessage='" + this.formatErrorMessage + '\'' + '}';
      }
   }

   public static final class Verifyer {
      private final Charset defaultCharset;
      private final LongPasswordStrategy longPasswordStrategy;
      private final BCrypt.Version version;

      private Verifyer(BCrypt.Version version, LongPasswordStrategy longPasswordStrategy) {
         this.defaultCharset = BCrypt.DEFAULT_CHARSET;
         this.version = version;
         this.longPasswordStrategy = longPasswordStrategy;
      }

      public BCrypt.Result verifyStrict(byte[] password, byte[] bcryptHash) {
         return this.innerVerifyBytes(password, bcryptHash, true);
      }

      public BCrypt.Result verify(byte[] password, byte[] bcryptHash) {
         return this.innerVerifyBytes(password, bcryptHash, false);
      }

      public BCrypt.Result verifyStrict(char[] password, char[] bcryptHash) {
         return this.innerVerifyChar(password, bcryptHash, true);
      }

      public BCrypt.Result verify(char[] password, char[] bcryptHash) {
         return this.innerVerifyChar(password, bcryptHash, false);
      }

      public BCrypt.Result verify(char[] password, CharSequence bcryptHash) {
         return this.innerVerifyChar(password, toCharArray(bcryptHash), false);
      }

      public BCrypt.Result verify(char[] password, byte[] bcryptHash) {
         MutableBytes pw = Bytes.from(password, this.defaultCharset).mutable();
         Throwable var4 = null;

         BCrypt.Result var5;
         try {
            var5 = this.innerVerifyBytes(pw.array(), bcryptHash, false);
         } catch (Throwable var14) {
            var4 = var14;
            throw var14;
         } finally {
            if (pw != null) {
               if (var4 != null) {
                  try {
                     pw.close();
                  } catch (Throwable var13) {
                     var4.addSuppressed(var13);
                  }
               } else {
                  pw.close();
               }
            }

         }

         return var5;
      }

      private static char[] toCharArray(CharSequence charSequence) {
         if (charSequence instanceof String) {
            return charSequence.toString().toCharArray();
         } else {
            char[] buffer = new char[charSequence.length()];

            for(int i = 0; i < charSequence.length(); ++i) {
               buffer[i] = charSequence.charAt(i);
            }

            return buffer;
         }
      }

      private BCrypt.Result innerVerifyChar(char[] password, char[] bcryptHash, boolean strict) {
         byte[] passwordBytes = null;
         byte[] bcryptHashBytes = null;

         BCrypt.Result var6;
         try {
            passwordBytes = Bytes.from(password, this.defaultCharset).array();
            bcryptHashBytes = Bytes.from(bcryptHash, this.defaultCharset).array();
            var6 = this.innerVerifyBytes(passwordBytes, bcryptHashBytes, strict);
         } finally {
            Bytes.wrapNullSafe(passwordBytes).mutable().secureWipe();
            Bytes.wrapNullSafe(bcryptHashBytes).mutable().secureWipe();
         }

         return var6;
      }

      private BCrypt.Result innerVerifyBytes(byte[] password, byte[] bcryptHash, boolean strict) {
         Objects.requireNonNull(bcryptHash);

         try {
            BCrypt.Version usedVersion;
            BCrypt.HashData hashData;
            if (this.version == null) {
               hashData = BCrypt.Version.VERSION_2A.parser.parse(bcryptHash);
               usedVersion = hashData.version;
            } else {
               usedVersion = this.version;
               hashData = usedVersion.parser.parse(bcryptHash);
            }

            if (strict) {
               if (this.version == null) {
                  throw new IllegalArgumentException("Using strict requires to define a Version. Try 'BCrypt.verifier(Version.VERSION_2A)'.");
               }

               if (hashData.version != this.version) {
                  return new BCrypt.Result(hashData, false);
               }
            }

            return verifyBCrypt(usedVersion, this.determinePasswordStrategy(usedVersion), password, hashData.cost, hashData.rawSalt, hashData.rawHash);
         } catch (IllegalBCryptFormatException var6) {
            return new BCrypt.Result(var6);
         }
      }

      private LongPasswordStrategy determinePasswordStrategy(BCrypt.Version usedVersion) {
         LongPasswordStrategy usedLongPasswordStrategy;
         if (this.longPasswordStrategy == null) {
            usedLongPasswordStrategy = LongPasswordStrategies.strict(usedVersion);
         } else {
            usedLongPasswordStrategy = this.longPasswordStrategy;
         }

         return usedLongPasswordStrategy;
      }

      public BCrypt.Result verify(byte[] password, BCrypt.HashData bcryptHashData) {
         return this.verify(password, bcryptHashData.cost, bcryptHashData.rawSalt, bcryptHashData.rawHash);
      }

      public BCrypt.Result verify(byte[] password, int cost, byte[] salt, byte[] rawBcryptHash23Bytes) {
         BCrypt.Version usedVersion = this.version == null ? BCrypt.Version.VERSION_2A : this.version;
         return verifyBCrypt(usedVersion, this.determinePasswordStrategy(usedVersion), password, cost, salt, rawBcryptHash23Bytes);
      }

      private static BCrypt.Result verifyBCrypt(BCrypt.Version version, LongPasswordStrategy longPasswordStrategy, byte[] password, int cost, byte[] salt, byte[] rawBcryptHash23Bytes) {
         BCrypt.HashData hashData = BCrypt.with((BCrypt.Version)Objects.requireNonNull(version), (LongPasswordStrategy)Objects.requireNonNull(longPasswordStrategy)).hashRaw(cost, (byte[])Objects.requireNonNull(salt), (byte[])Objects.requireNonNull(password));
         return new BCrypt.Result(hashData, Bytes.wrap(hashData.rawHash).equalsConstantTime((byte[])Objects.requireNonNull(rawBcryptHash23Bytes)));
      }

      // $FF: synthetic method
      Verifyer(BCrypt.Version x0, LongPasswordStrategy x1, Object x2) {
         this(x0, x1);
      }
   }

   public static final class HashData {
      public final int cost;
      public final BCrypt.Version version;
      public final byte[] rawSalt;
      public final byte[] rawHash;

      public HashData(int cost, BCrypt.Version version, byte[] rawSalt, byte[] rawHash) {
         Objects.requireNonNull(rawHash);
         Objects.requireNonNull(rawSalt);
         Objects.requireNonNull(version);
         if (Bytes.wrap(rawSalt).validate(BytesValidators.exactLength(16)) && Bytes.wrap(rawHash).validate(BytesValidators.or(BytesValidators.exactLength(23), BytesValidators.exactLength(24)))) {
            this.cost = cost;
            this.version = version;
            this.rawSalt = rawSalt;
            this.rawHash = rawHash;
         } else {
            throw new IllegalArgumentException("salt must be exactly 16 bytes and hash 23 bytes long");
         }
      }

      public void wipe() {
         Bytes.wrapNullSafe(this.rawSalt).mutable().secureWipe();
         Bytes.wrapNullSafe(this.rawHash).mutable().secureWipe();
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            BCrypt.HashData hashData = (BCrypt.HashData)o;
            return this.cost == hashData.cost && this.version == hashData.version && Bytes.wrap(this.rawSalt).equalsConstantTime(hashData.rawSalt) && Bytes.wrap(this.rawHash).equalsConstantTime(hashData.rawHash);
         } else {
            return false;
         }
      }

      public int hashCode() {
         int result = Objects.hash(new Object[]{this.cost, this.version});
         result = 31 * result + Arrays.hashCode(this.rawSalt);
         result = 31 * result + Arrays.hashCode(this.rawHash);
         return result;
      }

      public String toString() {
         return "HashData{cost=" + this.cost + ", version=" + this.version + ", rawSalt=" + Bytes.wrap(this.rawSalt).encodeHex() + ", rawHash=" + Bytes.wrap(this.rawHash).encodeHex() + '}';
      }
   }

   public static final class Hasher {
      private final Charset defaultCharset;
      private final BCrypt.Version version;
      private final SecureRandom secureRandom;
      private final LongPasswordStrategy longPasswordStrategy;

      private Hasher(BCrypt.Version version, SecureRandom secureRandom, LongPasswordStrategy longPasswordStrategy) {
         this.defaultCharset = BCrypt.DEFAULT_CHARSET;
         this.version = version;
         this.secureRandom = secureRandom;
         this.longPasswordStrategy = longPasswordStrategy;
      }

      public char[] hashToChar(int cost, char[] password) {
         return this.defaultCharset.decode(ByteBuffer.wrap(this.hash(cost, password))).array();
      }

      public String hashToString(int cost, char[] password) {
         return new String(this.hash(cost, password), this.defaultCharset);
      }

      public byte[] hash(int cost, char[] password) {
         if (password == null) {
            throw new IllegalArgumentException("provided password must not be null");
         } else {
            byte[] passwordBytes = null;

            byte[] var4;
            try {
               passwordBytes = Bytes.from(password, this.defaultCharset).array();
               var4 = this.hash(cost, Bytes.random(16, this.secureRandom).array(), passwordBytes);
            } finally {
               Bytes.wrapNullSafe(passwordBytes).mutable().secureWipe();
            }

            return var4;
         }
      }

      public byte[] hash(int cost, byte[] password) {
         return this.hash(cost, Bytes.random(16, this.secureRandom).array(), password);
      }

      public byte[] hash(int cost, byte[] salt, byte[] password) {
         return this.version.formatter.createHashMessage(this.hashRaw(cost, salt, password));
      }

      public BCrypt.HashData hashRaw(int cost, byte[] salt, byte[] password) {
         if (cost <= 31 && cost >= 4) {
            if (salt == null) {
               throw new IllegalArgumentException("salt must not be null");
            } else if (salt.length != 16) {
               throw new IllegalArgumentException("salt must be exactly 16 bytes, was " + salt.length);
            } else if (password == null) {
               throw new IllegalArgumentException("provided password must not be null");
            } else if (!this.version.appendNullTerminator && password.length == 0) {
               throw new IllegalArgumentException("provided password must at least be length 1 if no null terminator is appended");
            } else {
               if (password.length > this.version.allowedMaxPwLength) {
                  password = this.longPasswordStrategy.derive(password);
               }

               byte[] pwWithNullTerminator = this.version.appendNullTerminator ? Bytes.wrap(password).append((byte)0).array() : Bytes.wrap(password).copy().array();

               BCrypt.HashData var6;
               try {
                  byte[] hash = (new BCryptOpenBSDProtocol()).cryptRaw(1L << (int)((long)cost), salt, pwWithNullTerminator);
                  var6 = new BCrypt.HashData(cost, this.version, salt, this.version.useOnly23bytesForHash ? Bytes.wrap(hash).resize(23, BytesTransformer.ResizeTransformer.Mode.RESIZE_KEEP_FROM_ZERO_INDEX).array() : hash);
               } finally {
                  Bytes.wrapNullSafe(pwWithNullTerminator).mutable().secureWipe();
               }

               return var6;
            }
         } else {
            throw new IllegalArgumentException("cost factor must be between 4 and 31, was " + cost);
         }
      }

      // $FF: synthetic method
      Hasher(BCrypt.Version x0, SecureRandom x1, LongPasswordStrategy x2, Object x3) {
         this(x0, x1, x2);
      }
   }
}
