package fr.xephi.authme.libs.com.google.common.hash;

import fr.xephi.authme.libs.com.google.common.base.Charsets;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.primitives.Ints;
import fr.xephi.authme.libs.com.google.common.primitives.UnsignedBytes;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.Immutable;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import javax.annotation.CheckForNull;

@Immutable
@ElementTypesAreNonnullByDefault
final class Murmur3_32HashFunction extends AbstractHashFunction implements Serializable {
   static final HashFunction MURMUR3_32 = new Murmur3_32HashFunction(0, false);
   static final HashFunction MURMUR3_32_FIXED = new Murmur3_32HashFunction(0, true);
   static final HashFunction GOOD_FAST_HASH_32;
   private static final int CHUNK_SIZE = 4;
   private static final int C1 = -862048943;
   private static final int C2 = 461845907;
   private final int seed;
   private final boolean supplementaryPlaneFix;
   private static final long serialVersionUID = 0L;

   Murmur3_32HashFunction(int seed, boolean supplementaryPlaneFix) {
      this.seed = seed;
      this.supplementaryPlaneFix = supplementaryPlaneFix;
   }

   public int bits() {
      return 32;
   }

   public Hasher newHasher() {
      return new Murmur3_32HashFunction.Murmur3_32Hasher(this.seed);
   }

   public String toString() {
      int var1 = this.seed;
      return (new StringBuilder(31)).append("Hashing.murmur3_32(").append(var1).append(")").toString();
   }

   public boolean equals(@CheckForNull Object object) {
      if (!(object instanceof Murmur3_32HashFunction)) {
         return false;
      } else {
         Murmur3_32HashFunction other = (Murmur3_32HashFunction)object;
         return this.seed == other.seed && this.supplementaryPlaneFix == other.supplementaryPlaneFix;
      }
   }

   public int hashCode() {
      return this.getClass().hashCode() ^ this.seed;
   }

   public HashCode hashInt(int input) {
      int k1 = mixK1(input);
      int h1 = mixH1(this.seed, k1);
      return fmix(h1, 4);
   }

   public HashCode hashLong(long input) {
      int low = (int)input;
      int high = (int)(input >>> 32);
      int k1 = mixK1(low);
      int h1 = mixH1(this.seed, k1);
      k1 = mixK1(high);
      h1 = mixH1(h1, k1);
      return fmix(h1, 8);
   }

   public HashCode hashUnencodedChars(CharSequence input) {
      int h1 = this.seed;

      int k1;
      for(k1 = 1; k1 < input.length(); k1 += 2) {
         int k1 = input.charAt(k1 - 1) | input.charAt(k1) << 16;
         k1 = mixK1(k1);
         h1 = mixH1(h1, k1);
      }

      if ((input.length() & 1) == 1) {
         int k1 = input.charAt(input.length() - 1);
         k1 = mixK1(k1);
         h1 ^= k1;
      }

      return fmix(h1, 2 * input.length());
   }

   public HashCode hashString(CharSequence input, Charset charset) {
      if (!Charsets.UTF_8.equals(charset)) {
         return this.hashBytes(input.toString().getBytes(charset));
      } else {
         int utf16Length = input.length();
         int h1 = this.seed;
         int i = 0;

         int len;
         char c;
         int codePoint;
         for(len = 0; i + 4 <= utf16Length; len += 4) {
            char c0 = input.charAt(i);
            char c1 = input.charAt(i + 1);
            char c2 = input.charAt(i + 2);
            c = input.charAt(i + 3);
            if (c0 >= 128 || c1 >= 128 || c2 >= 128 || c >= 128) {
               break;
            }

            codePoint = c0 | c1 << 8 | c2 << 16 | c << 24;
            codePoint = mixK1(codePoint);
            h1 = mixH1(h1, codePoint);
            i += 4;
         }

         long buffer = 0L;

         for(int shift = 0; i < utf16Length; ++i) {
            c = input.charAt(i);
            if (c < 128) {
               buffer |= (long)c << shift;
               shift += 8;
               ++len;
            } else if (c < 2048) {
               buffer |= charToTwoUtf8Bytes(c) << shift;
               shift += 16;
               len += 2;
            } else if (c >= '\ud800' && c <= '\udfff') {
               codePoint = Character.codePointAt(input, i);
               if (codePoint == c) {
                  return this.hashBytes(input.toString().getBytes(charset));
               }

               ++i;
               buffer |= codePointToFourUtf8Bytes(codePoint) << shift;
               if (this.supplementaryPlaneFix) {
                  shift += 32;
               }

               len += 4;
            } else {
               buffer |= charToThreeUtf8Bytes(c) << shift;
               shift += 24;
               len += 3;
            }

            if (shift >= 32) {
               codePoint = mixK1((int)buffer);
               h1 = mixH1(h1, codePoint);
               buffer >>>= 32;
               shift -= 32;
            }
         }

         int k1 = mixK1((int)buffer);
         h1 ^= k1;
         return fmix(h1, len);
      }
   }

   public HashCode hashBytes(byte[] input, int off, int len) {
      Preconditions.checkPositionIndexes(off, off + len, input.length);
      int h1 = this.seed;

      int i;
      int k1;
      for(i = 0; i + 4 <= len; i += 4) {
         k1 = mixK1(getIntLittleEndian(input, off + i));
         h1 = mixH1(h1, k1);
      }

      k1 = 0;

      for(int shift = 0; i < len; shift += 8) {
         k1 ^= UnsignedBytes.toInt(input[off + i]) << shift;
         ++i;
      }

      h1 ^= mixK1(k1);
      return fmix(h1, len);
   }

   private static int getIntLittleEndian(byte[] input, int offset) {
      return Ints.fromBytes(input[offset + 3], input[offset + 2], input[offset + 1], input[offset]);
   }

   private static int mixK1(int k1) {
      k1 *= -862048943;
      k1 = Integer.rotateLeft(k1, 15);
      k1 *= 461845907;
      return k1;
   }

   private static int mixH1(int h1, int k1) {
      h1 ^= k1;
      h1 = Integer.rotateLeft(h1, 13);
      h1 = h1 * 5 + -430675100;
      return h1;
   }

   private static HashCode fmix(int h1, int length) {
      h1 ^= length;
      h1 ^= h1 >>> 16;
      h1 *= -2048144789;
      h1 ^= h1 >>> 13;
      h1 *= -1028477387;
      h1 ^= h1 >>> 16;
      return HashCode.fromInt(h1);
   }

   private static long codePointToFourUtf8Bytes(int codePoint) {
      return 240L | (long)(codePoint >>> 18) | (128L | (long)(63 & codePoint >>> 12)) << 8 | (128L | (long)(63 & codePoint >>> 6)) << 16 | (128L | (long)(63 & codePoint)) << 24;
   }

   private static long charToThreeUtf8Bytes(char c) {
      return 224L | (long)(c >>> 12) | (long)((128 | 63 & c >>> 6) << 8) | (long)((128 | 63 & c) << 16);
   }

   private static long charToTwoUtf8Bytes(char c) {
      return 192L | (long)(c >>> 6) | (long)((128 | 63 & c) << 8);
   }

   static {
      GOOD_FAST_HASH_32 = new Murmur3_32HashFunction(Hashing.GOOD_FAST_HASH_SEED, true);
   }

   @CanIgnoreReturnValue
   private static final class Murmur3_32Hasher extends AbstractHasher {
      private int h1;
      private long buffer;
      private int shift;
      private int length;
      private boolean isDone;

      Murmur3_32Hasher(int seed) {
         this.h1 = seed;
         this.length = 0;
         this.isDone = false;
      }

      private void update(int nBytes, long update) {
         this.buffer |= (update & 4294967295L) << this.shift;
         this.shift += nBytes * 8;
         this.length += nBytes;
         if (this.shift >= 32) {
            this.h1 = Murmur3_32HashFunction.mixH1(this.h1, Murmur3_32HashFunction.mixK1((int)this.buffer));
            this.buffer >>>= 32;
            this.shift -= 32;
         }

      }

      public Hasher putByte(byte b) {
         this.update(1, (long)(b & 255));
         return this;
      }

      public Hasher putBytes(byte[] bytes, int off, int len) {
         Preconditions.checkPositionIndexes(off, off + len, bytes.length);

         int i;
         for(i = 0; i + 4 <= len; i += 4) {
            this.update(4, (long)Murmur3_32HashFunction.getIntLittleEndian(bytes, off + i));
         }

         while(i < len) {
            this.putByte(bytes[off + i]);
            ++i;
         }

         return this;
      }

      public Hasher putBytes(ByteBuffer buffer) {
         ByteOrder bo = buffer.order();
         buffer.order(ByteOrder.LITTLE_ENDIAN);

         while(buffer.remaining() >= 4) {
            this.putInt(buffer.getInt());
         }

         while(buffer.hasRemaining()) {
            this.putByte(buffer.get());
         }

         buffer.order(bo);
         return this;
      }

      public Hasher putInt(int i) {
         this.update(4, (long)i);
         return this;
      }

      public Hasher putLong(long l) {
         this.update(4, (long)((int)l));
         this.update(4, l >>> 32);
         return this;
      }

      public Hasher putChar(char c) {
         this.update(2, (long)c);
         return this;
      }

      public Hasher putString(CharSequence input, Charset charset) {
         if (!Charsets.UTF_8.equals(charset)) {
            return super.putString(input, charset);
         } else {
            int utf16Length = input.length();

            int i;
            char c;
            for(i = 0; i + 4 <= utf16Length; i += 4) {
               c = input.charAt(i);
               char c1 = input.charAt(i + 1);
               char c2 = input.charAt(i + 2);
               char c3 = input.charAt(i + 3);
               if (c >= 128 || c1 >= 128 || c2 >= 128 || c3 >= 128) {
                  break;
               }

               this.update(4, (long)(c | c1 << 8 | c2 << 16 | c3 << 24));
            }

            for(; i < utf16Length; ++i) {
               c = input.charAt(i);
               if (c < 128) {
                  this.update(1, (long)c);
               } else if (c < 2048) {
                  this.update(2, Murmur3_32HashFunction.charToTwoUtf8Bytes(c));
               } else if (c >= '\ud800' && c <= '\udfff') {
                  int codePoint = Character.codePointAt(input, i);
                  if (codePoint == c) {
                     this.putBytes((byte[])input.subSequence(i, utf16Length).toString().getBytes(charset));
                     return this;
                  }

                  ++i;
                  this.update(4, Murmur3_32HashFunction.codePointToFourUtf8Bytes(codePoint));
               } else {
                  this.update(3, Murmur3_32HashFunction.charToThreeUtf8Bytes(c));
               }
            }

            return this;
         }
      }

      public HashCode hash() {
         Preconditions.checkState(!this.isDone);
         this.isDone = true;
         this.h1 ^= Murmur3_32HashFunction.mixK1((int)this.buffer);
         return Murmur3_32HashFunction.fmix(this.h1, this.length);
      }
   }
}
