package fr.xephi.authme.libs.com.google.common.hash;

import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.math.LongMath;
import fr.xephi.authme.libs.com.google.common.primitives.Ints;
import fr.xephi.authme.libs.com.google.common.primitives.Longs;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLongArray;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
enum BloomFilterStrategies implements BloomFilter.Strategy {
   MURMUR128_MITZ_32 {
      public <T> boolean put(@ParametricNullness T object, Funnel<? super T> funnel, int numHashFunctions, BloomFilterStrategies.LockFreeBitArray bits) {
         long bitSize = bits.bitSize();
         long hash64 = Hashing.murmur3_128().hashObject(object, funnel).asLong();
         int hash1 = (int)hash64;
         int hash2 = (int)(hash64 >>> 32);
         boolean bitsChanged = false;

         for(int i = 1; i <= numHashFunctions; ++i) {
            int combinedHash = hash1 + i * hash2;
            if (combinedHash < 0) {
               combinedHash = ~combinedHash;
            }

            bitsChanged |= bits.set((long)combinedHash % bitSize);
         }

         return bitsChanged;
      }

      public <T> boolean mightContain(@ParametricNullness T object, Funnel<? super T> funnel, int numHashFunctions, BloomFilterStrategies.LockFreeBitArray bits) {
         long bitSize = bits.bitSize();
         long hash64 = Hashing.murmur3_128().hashObject(object, funnel).asLong();
         int hash1 = (int)hash64;
         int hash2 = (int)(hash64 >>> 32);

         for(int i = 1; i <= numHashFunctions; ++i) {
            int combinedHash = hash1 + i * hash2;
            if (combinedHash < 0) {
               combinedHash = ~combinedHash;
            }

            if (!bits.get((long)combinedHash % bitSize)) {
               return false;
            }
         }

         return true;
      }
   },
   MURMUR128_MITZ_64 {
      public <T> boolean put(@ParametricNullness T object, Funnel<? super T> funnel, int numHashFunctions, BloomFilterStrategies.LockFreeBitArray bits) {
         long bitSize = bits.bitSize();
         byte[] bytes = Hashing.murmur3_128().hashObject(object, funnel).getBytesInternal();
         long hash1 = this.lowerEight(bytes);
         long hash2 = this.upperEight(bytes);
         boolean bitsChanged = false;
         long combinedHash = hash1;

         for(int i = 0; i < numHashFunctions; ++i) {
            bitsChanged |= bits.set((combinedHash & Long.MAX_VALUE) % bitSize);
            combinedHash += hash2;
         }

         return bitsChanged;
      }

      public <T> boolean mightContain(@ParametricNullness T object, Funnel<? super T> funnel, int numHashFunctions, BloomFilterStrategies.LockFreeBitArray bits) {
         long bitSize = bits.bitSize();
         byte[] bytes = Hashing.murmur3_128().hashObject(object, funnel).getBytesInternal();
         long hash1 = this.lowerEight(bytes);
         long hash2 = this.upperEight(bytes);
         long combinedHash = hash1;

         for(int i = 0; i < numHashFunctions; ++i) {
            if (!bits.get((combinedHash & Long.MAX_VALUE) % bitSize)) {
               return false;
            }

            combinedHash += hash2;
         }

         return true;
      }

      private long lowerEight(byte[] bytes) {
         return Longs.fromBytes(bytes[7], bytes[6], bytes[5], bytes[4], bytes[3], bytes[2], bytes[1], bytes[0]);
      }

      private long upperEight(byte[] bytes) {
         return Longs.fromBytes(bytes[15], bytes[14], bytes[13], bytes[12], bytes[11], bytes[10], bytes[9], bytes[8]);
      }
   };

   private BloomFilterStrategies() {
   }

   // $FF: synthetic method
   private static BloomFilterStrategies[] $values() {
      return new BloomFilterStrategies[]{MURMUR128_MITZ_32, MURMUR128_MITZ_64};
   }

   // $FF: synthetic method
   BloomFilterStrategies(Object x2) {
      this();
   }

   static final class LockFreeBitArray {
      private static final int LONG_ADDRESSABLE_BITS = 6;
      final AtomicLongArray data;
      private final LongAddable bitCount;

      LockFreeBitArray(long bits) {
         Preconditions.checkArgument(bits > 0L, "data length is zero!");
         this.data = new AtomicLongArray(Ints.checkedCast(LongMath.divide(bits, 64L, RoundingMode.CEILING)));
         this.bitCount = LongAddables.create();
      }

      LockFreeBitArray(long[] data) {
         Preconditions.checkArgument(data.length > 0, "data length is zero!");
         this.data = new AtomicLongArray(data);
         this.bitCount = LongAddables.create();
         long bitCount = 0L;
         long[] var4 = data;
         int var5 = data.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            long value = var4[var6];
            bitCount += (long)Long.bitCount(value);
         }

         this.bitCount.add(bitCount);
      }

      boolean set(long bitIndex) {
         if (this.get(bitIndex)) {
            return false;
         } else {
            int longIndex = (int)(bitIndex >>> 6);
            long mask = 1L << (int)bitIndex;

            long oldValue;
            long newValue;
            do {
               oldValue = this.data.get(longIndex);
               newValue = oldValue | mask;
               if (oldValue == newValue) {
                  return false;
               }
            } while(!this.data.compareAndSet(longIndex, oldValue, newValue));

            this.bitCount.increment();
            return true;
         }
      }

      boolean get(long bitIndex) {
         return (this.data.get((int)(bitIndex >>> 6)) & 1L << (int)bitIndex) != 0L;
      }

      public static long[] toPlainArray(AtomicLongArray atomicLongArray) {
         long[] array = new long[atomicLongArray.length()];

         for(int i = 0; i < array.length; ++i) {
            array[i] = atomicLongArray.get(i);
         }

         return array;
      }

      long bitSize() {
         return (long)this.data.length() * 64L;
      }

      long bitCount() {
         return this.bitCount.sum();
      }

      BloomFilterStrategies.LockFreeBitArray copy() {
         return new BloomFilterStrategies.LockFreeBitArray(toPlainArray(this.data));
      }

      void putAll(BloomFilterStrategies.LockFreeBitArray other) {
         Preconditions.checkArgument(this.data.length() == other.data.length(), "BitArrays must be of equal length (%s != %s)", this.data.length(), other.data.length());

         for(int i = 0; i < this.data.length(); ++i) {
            this.putData(i, other.data.get(i));
         }

      }

      void putData(int i, long longValue) {
         boolean changedAnyBits = true;

         long ourLongOld;
         long ourLongNew;
         do {
            ourLongOld = this.data.get(i);
            ourLongNew = ourLongOld | longValue;
            if (ourLongOld == ourLongNew) {
               changedAnyBits = false;
               break;
            }
         } while(!this.data.compareAndSet(i, ourLongOld, ourLongNew));

         if (changedAnyBits) {
            int bitsAdded = Long.bitCount(ourLongNew) - Long.bitCount(ourLongOld);
            this.bitCount.add((long)bitsAdded);
         }

      }

      int dataLength() {
         return this.data.length();
      }

      public boolean equals(@CheckForNull Object o) {
         if (o instanceof BloomFilterStrategies.LockFreeBitArray) {
            BloomFilterStrategies.LockFreeBitArray lockFreeBitArray = (BloomFilterStrategies.LockFreeBitArray)o;
            return Arrays.equals(toPlainArray(this.data), toPlainArray(lockFreeBitArray.data));
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Arrays.hashCode(toPlainArray(this.data));
      }
   }
}
