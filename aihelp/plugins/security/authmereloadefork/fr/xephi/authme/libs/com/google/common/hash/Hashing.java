package fr.xephi.authme.libs.com.google.common.hash;

import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.Immutable;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import javax.annotation.CheckForNull;
import javax.crypto.spec.SecretKeySpec;

@ElementTypesAreNonnullByDefault
public final class Hashing {
   static final int GOOD_FAST_HASH_SEED = (int)System.currentTimeMillis();

   public static HashFunction goodFastHash(int minimumBits) {
      int bits = checkPositiveAndMakeMultipleOf32(minimumBits);
      if (bits == 32) {
         return Murmur3_32HashFunction.GOOD_FAST_HASH_32;
      } else if (bits <= 128) {
         return Murmur3_128HashFunction.GOOD_FAST_HASH_128;
      } else {
         int hashFunctionsNeeded = (bits + 127) / 128;
         HashFunction[] hashFunctions = new HashFunction[hashFunctionsNeeded];
         hashFunctions[0] = Murmur3_128HashFunction.GOOD_FAST_HASH_128;
         int seed = GOOD_FAST_HASH_SEED;

         for(int i = 1; i < hashFunctionsNeeded; ++i) {
            seed += 1500450271;
            hashFunctions[i] = murmur3_128(seed);
         }

         return new Hashing.ConcatenatedHashFunction(hashFunctions);
      }
   }

   /** @deprecated */
   @Deprecated
   public static HashFunction murmur3_32(int seed) {
      return new Murmur3_32HashFunction(seed, false);
   }

   /** @deprecated */
   @Deprecated
   public static HashFunction murmur3_32() {
      return Murmur3_32HashFunction.MURMUR3_32;
   }

   public static HashFunction murmur3_32_fixed(int seed) {
      return new Murmur3_32HashFunction(seed, true);
   }

   public static HashFunction murmur3_32_fixed() {
      return Murmur3_32HashFunction.MURMUR3_32_FIXED;
   }

   public static HashFunction murmur3_128(int seed) {
      return new Murmur3_128HashFunction(seed);
   }

   public static HashFunction murmur3_128() {
      return Murmur3_128HashFunction.MURMUR3_128;
   }

   public static HashFunction sipHash24() {
      return SipHashFunction.SIP_HASH_24;
   }

   public static HashFunction sipHash24(long k0, long k1) {
      return new SipHashFunction(2, 4, k0, k1);
   }

   /** @deprecated */
   @Deprecated
   public static HashFunction md5() {
      return Hashing.Md5Holder.MD5;
   }

   /** @deprecated */
   @Deprecated
   public static HashFunction sha1() {
      return Hashing.Sha1Holder.SHA_1;
   }

   public static HashFunction sha256() {
      return Hashing.Sha256Holder.SHA_256;
   }

   public static HashFunction sha384() {
      return Hashing.Sha384Holder.SHA_384;
   }

   public static HashFunction sha512() {
      return Hashing.Sha512Holder.SHA_512;
   }

   public static HashFunction hmacMd5(Key key) {
      return new MacHashFunction("HmacMD5", key, hmacToString("hmacMd5", key));
   }

   public static HashFunction hmacMd5(byte[] key) {
      return hmacMd5((Key)(new SecretKeySpec((byte[])Preconditions.checkNotNull(key), "HmacMD5")));
   }

   public static HashFunction hmacSha1(Key key) {
      return new MacHashFunction("HmacSHA1", key, hmacToString("hmacSha1", key));
   }

   public static HashFunction hmacSha1(byte[] key) {
      return hmacSha1((Key)(new SecretKeySpec((byte[])Preconditions.checkNotNull(key), "HmacSHA1")));
   }

   public static HashFunction hmacSha256(Key key) {
      return new MacHashFunction("HmacSHA256", key, hmacToString("hmacSha256", key));
   }

   public static HashFunction hmacSha256(byte[] key) {
      return hmacSha256((Key)(new SecretKeySpec((byte[])Preconditions.checkNotNull(key), "HmacSHA256")));
   }

   public static HashFunction hmacSha512(Key key) {
      return new MacHashFunction("HmacSHA512", key, hmacToString("hmacSha512", key));
   }

   public static HashFunction hmacSha512(byte[] key) {
      return hmacSha512((Key)(new SecretKeySpec((byte[])Preconditions.checkNotNull(key), "HmacSHA512")));
   }

   private static String hmacToString(String methodName, Key key) {
      return String.format("Hashing.%s(Key[algorithm=%s, format=%s])", methodName, key.getAlgorithm(), key.getFormat());
   }

   public static HashFunction crc32c() {
      return Crc32cHashFunction.CRC_32_C;
   }

   public static HashFunction crc32() {
      return Hashing.ChecksumType.CRC_32.hashFunction;
   }

   public static HashFunction adler32() {
      return Hashing.ChecksumType.ADLER_32.hashFunction;
   }

   public static HashFunction farmHashFingerprint64() {
      return FarmHashFingerprint64.FARMHASH_FINGERPRINT_64;
   }

   public static HashFunction fingerprint2011() {
      return Fingerprint2011.FINGERPRINT_2011;
   }

   public static int consistentHash(HashCode hashCode, int buckets) {
      return consistentHash(hashCode.padToLong(), buckets);
   }

   public static int consistentHash(long input, int buckets) {
      Preconditions.checkArgument(buckets > 0, "buckets must be positive: %s", buckets);
      Hashing.LinearCongruentialGenerator generator = new Hashing.LinearCongruentialGenerator(input);
      int candidate = 0;

      while(true) {
         int next = (int)((double)(candidate + 1) / generator.nextDouble());
         if (next < 0 || next >= buckets) {
            return candidate;
         }

         candidate = next;
      }
   }

   public static HashCode combineOrdered(Iterable<HashCode> hashCodes) {
      Iterator<HashCode> iterator = hashCodes.iterator();
      Preconditions.checkArgument(iterator.hasNext(), "Must be at least 1 hash code to combine.");
      int bits = ((HashCode)iterator.next()).bits();
      byte[] resultBytes = new byte[bits / 8];
      Iterator var4 = hashCodes.iterator();

      while(var4.hasNext()) {
         HashCode hashCode = (HashCode)var4.next();
         byte[] nextBytes = hashCode.asBytes();
         Preconditions.checkArgument(nextBytes.length == resultBytes.length, "All hashcodes must have the same bit length.");

         for(int i = 0; i < nextBytes.length; ++i) {
            resultBytes[i] = (byte)(resultBytes[i] * 37 ^ nextBytes[i]);
         }
      }

      return HashCode.fromBytesNoCopy(resultBytes);
   }

   public static HashCode combineUnordered(Iterable<HashCode> hashCodes) {
      Iterator<HashCode> iterator = hashCodes.iterator();
      Preconditions.checkArgument(iterator.hasNext(), "Must be at least 1 hash code to combine.");
      byte[] resultBytes = new byte[((HashCode)iterator.next()).bits() / 8];
      Iterator var3 = hashCodes.iterator();

      while(var3.hasNext()) {
         HashCode hashCode = (HashCode)var3.next();
         byte[] nextBytes = hashCode.asBytes();
         Preconditions.checkArgument(nextBytes.length == resultBytes.length, "All hashcodes must have the same bit length.");

         for(int i = 0; i < nextBytes.length; ++i) {
            resultBytes[i] += nextBytes[i];
         }
      }

      return HashCode.fromBytesNoCopy(resultBytes);
   }

   static int checkPositiveAndMakeMultipleOf32(int bits) {
      Preconditions.checkArgument(bits > 0, "Number of bits must be positive");
      return bits + 31 & -32;
   }

   public static HashFunction concatenating(HashFunction first, HashFunction second, HashFunction... rest) {
      List<HashFunction> list = new ArrayList();
      list.add(first);
      list.add(second);
      Collections.addAll(list, rest);
      return new Hashing.ConcatenatedHashFunction((HashFunction[])list.toArray(new HashFunction[0]));
   }

   public static HashFunction concatenating(Iterable<HashFunction> hashFunctions) {
      Preconditions.checkNotNull(hashFunctions);
      List<HashFunction> list = new ArrayList();
      Iterator var2 = hashFunctions.iterator();

      while(var2.hasNext()) {
         HashFunction hashFunction = (HashFunction)var2.next();
         list.add(hashFunction);
      }

      Preconditions.checkArgument(!list.isEmpty(), "number of hash functions (%s) must be > 0", list.size());
      return new Hashing.ConcatenatedHashFunction((HashFunction[])list.toArray(new HashFunction[0]));
   }

   private Hashing() {
   }

   private static final class LinearCongruentialGenerator {
      private long state;

      public LinearCongruentialGenerator(long seed) {
         this.state = seed;
      }

      public double nextDouble() {
         this.state = 2862933555777941757L * this.state + 1L;
         return (double)((int)(this.state >>> 33) + 1) / 2.147483648E9D;
      }
   }

   private static final class ConcatenatedHashFunction extends AbstractCompositeHashFunction {
      private ConcatenatedHashFunction(HashFunction... functions) {
         super(functions);
         HashFunction[] var2 = functions;
         int var3 = functions.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            HashFunction function = var2[var4];
            Preconditions.checkArgument(function.bits() % 8 == 0, "the number of bits (%s) in hashFunction (%s) must be divisible by 8", (int)function.bits(), function);
         }

      }

      HashCode makeHash(Hasher[] hashers) {
         byte[] bytes = new byte[this.bits() / 8];
         int i = 0;
         Hasher[] var4 = hashers;
         int var5 = hashers.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Hasher hasher = var4[var6];
            HashCode newHash = hasher.hash();
            i += newHash.writeBytesTo(bytes, i, newHash.bits() / 8);
         }

         return HashCode.fromBytesNoCopy(bytes);
      }

      public int bits() {
         int bitSum = 0;
         HashFunction[] var2 = this.functions;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            HashFunction function = var2[var4];
            bitSum += function.bits();
         }

         return bitSum;
      }

      public boolean equals(@CheckForNull Object object) {
         if (object instanceof Hashing.ConcatenatedHashFunction) {
            Hashing.ConcatenatedHashFunction other = (Hashing.ConcatenatedHashFunction)object;
            return Arrays.equals(this.functions, other.functions);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Arrays.hashCode(this.functions);
      }

      // $FF: synthetic method
      ConcatenatedHashFunction(HashFunction[] x0, Object x1) {
         this(x0);
      }
   }

   @Immutable
   static enum ChecksumType implements ImmutableSupplier<Checksum> {
      CRC_32("Hashing.crc32()") {
         public Checksum get() {
            return new CRC32();
         }
      },
      ADLER_32("Hashing.adler32()") {
         public Checksum get() {
            return new Adler32();
         }
      };

      public final HashFunction hashFunction;

      private ChecksumType(String toString) {
         this.hashFunction = new ChecksumHashFunction(this, 32, toString);
      }

      // $FF: synthetic method
      private static Hashing.ChecksumType[] $values() {
         return new Hashing.ChecksumType[]{CRC_32, ADLER_32};
      }

      // $FF: synthetic method
      ChecksumType(String x2, Object x3) {
         this(x2);
      }
   }

   private static class Sha512Holder {
      static final HashFunction SHA_512 = new MessageDigestHashFunction("SHA-512", "Hashing.sha512()");
   }

   private static class Sha384Holder {
      static final HashFunction SHA_384 = new MessageDigestHashFunction("SHA-384", "Hashing.sha384()");
   }

   private static class Sha256Holder {
      static final HashFunction SHA_256 = new MessageDigestHashFunction("SHA-256", "Hashing.sha256()");
   }

   private static class Sha1Holder {
      static final HashFunction SHA_1 = new MessageDigestHashFunction("SHA-1", "Hashing.sha1()");
   }

   private static class Md5Holder {
      static final HashFunction MD5 = new MessageDigestHashFunction("MD5", "Hashing.md5()");
   }
}
