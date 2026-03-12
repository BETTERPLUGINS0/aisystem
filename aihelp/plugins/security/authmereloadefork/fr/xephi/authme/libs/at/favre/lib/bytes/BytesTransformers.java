package fr.xephi.authme.libs.at.favre.lib.bytes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.Random;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class BytesTransformers {
   private BytesTransformers() {
   }

   public static BytesTransformer shuffle() {
      return new BytesTransformers.ShuffleTransformer(new SecureRandom());
   }

   public static BytesTransformer shuffle(Random random) {
      return new BytesTransformers.ShuffleTransformer(random);
   }

   public static BytesTransformer sort() {
      return new BytesTransformers.SortTransformer();
   }

   public static BytesTransformer sortUnsigned() {
      return new BytesTransformers.SortTransformer(new BytesTransformers.SortTransformer.UnsignedByteComparator());
   }

   public static BytesTransformer sort(Comparator<Byte> comparator) {
      return new BytesTransformers.SortTransformer(comparator);
   }

   public static BytesTransformer checksumAppendCrc32() {
      return new BytesTransformers.ChecksumTransformer(new CRC32(), BytesTransformers.ChecksumTransformer.Mode.APPEND, 4);
   }

   public static BytesTransformer checksumCrc32() {
      return new BytesTransformers.ChecksumTransformer(new CRC32(), BytesTransformers.ChecksumTransformer.Mode.TRANSFORM, 4);
   }

   public static BytesTransformer checksum(Checksum checksum, BytesTransformers.ChecksumTransformer.Mode mode, int checksumLengthByte) {
      return new BytesTransformers.ChecksumTransformer(checksum, mode, checksumLengthByte);
   }

   public static BytesTransformer compressGzip() {
      return new BytesTransformers.GzipCompressor(true);
   }

   public static BytesTransformer decompressGzip() {
      return new BytesTransformers.GzipCompressor(false);
   }

   public static BytesTransformer hmacSha1(byte[] key) {
      return new BytesTransformers.HmacTransformer(key, "HmacSHA1");
   }

   public static BytesTransformer hmacSha256(byte[] key) {
      return new BytesTransformers.HmacTransformer(key, "HmacSHA256");
   }

   public static BytesTransformer hmac(byte[] key, String algorithmName) {
      return new BytesTransformers.HmacTransformer(key, algorithmName);
   }

   public static final class HmacTransformer implements BytesTransformer {
      static final String HMAC_SHA1 = "HmacSHA1";
      static final String HMAC_SHA256 = "HmacSHA256";
      private final byte[] secretKey;
      private final String macAlgorithmName;

      HmacTransformer(byte[] secretKey, String macAlgorithmName) {
         this.macAlgorithmName = macAlgorithmName;
         this.secretKey = secretKey;
      }

      public byte[] transform(byte[] currentArray, boolean inPlace) {
         try {
            Mac mac = Mac.getInstance(this.macAlgorithmName);
            mac.init(new SecretKeySpec(this.secretKey, this.macAlgorithmName));
            return mac.doFinal(currentArray);
         } catch (Exception var4) {
            throw new IllegalArgumentException(var4);
         }
      }

      public boolean supportInPlaceTransformation() {
         return false;
      }
   }

   public static final class GzipCompressor implements BytesTransformer {
      private final boolean compress;

      GzipCompressor(boolean compress) {
         this.compress = compress;
      }

      public byte[] transform(byte[] currentArray, boolean inPlace) {
         return this.compress ? this.compress(currentArray) : this.decompress(currentArray);
      }

      private byte[] decompress(byte[] compressedContent) {
         ByteArrayOutputStream bos = new ByteArrayOutputStream(Math.max(32, compressedContent.length / 2));

         try {
            GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(compressedContent));
            Throwable var4 = null;

            try {
               byte[] buffer = new byte[4096];

               int len;
               while((len = gzipInputStream.read(buffer)) > 0) {
                  bos.write(buffer, 0, len);
               }

               byte[] var7 = bos.toByteArray();
               return var7;
            } catch (Throwable var17) {
               var4 = var17;
               throw var17;
            } finally {
               if (gzipInputStream != null) {
                  if (var4 != null) {
                     try {
                        gzipInputStream.close();
                     } catch (Throwable var16) {
                        var4.addSuppressed(var16);
                     }
                  } else {
                     gzipInputStream.close();
                  }
               }

            }
         } catch (Exception var19) {
            throw new IllegalStateException("could not decompress gzip", var19);
         }
      }

      private byte[] compress(byte[] content) {
         ByteArrayOutputStream bos = new ByteArrayOutputStream(content.length);

         try {
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(bos);
            Throwable var4 = null;

            try {
               gzipOutputStream.write(content);
            } catch (Throwable var14) {
               var4 = var14;
               throw var14;
            } finally {
               if (gzipOutputStream != null) {
                  if (var4 != null) {
                     try {
                        gzipOutputStream.close();
                     } catch (Throwable var13) {
                        var4.addSuppressed(var13);
                     }
                  } else {
                     gzipOutputStream.close();
                  }
               }

            }
         } catch (Exception var16) {
            throw new IllegalStateException("could not compress gzip", var16);
         }

         return bos.toByteArray();
      }

      public boolean supportInPlaceTransformation() {
         return false;
      }
   }

   public static final class ChecksumTransformer implements BytesTransformer {
      private final Checksum checksum;
      private final BytesTransformers.ChecksumTransformer.Mode mode;
      private final int checksumLengthByte;

      ChecksumTransformer(Checksum checksum, BytesTransformers.ChecksumTransformer.Mode mode, int checksumLengthByte) {
         if (checksumLengthByte > 0 && checksumLengthByte <= 8) {
            Objects.requireNonNull(checksum, "checksum instance must not be null");
            this.checksum = checksum;
            this.mode = mode;
            this.checksumLengthByte = checksumLengthByte;
         } else {
            throw new IllegalArgumentException("checksum length must be between 1 and 8 bytes");
         }
      }

      public byte[] transform(byte[] currentArray, boolean inPlace) {
         this.checksum.update(currentArray, 0, currentArray.length);
         byte[] checksumBytes = Bytes.from(this.checksum.getValue()).resize(this.checksumLengthByte).array();
         return this.mode == BytesTransformers.ChecksumTransformer.Mode.TRANSFORM ? checksumBytes : Bytes.from(currentArray, checksumBytes).array();
      }

      public boolean supportInPlaceTransformation() {
         return false;
      }

      public static enum Mode {
         APPEND,
         TRANSFORM;
      }
   }

   public static final class SortTransformer implements BytesTransformer {
      private final Comparator comparator;

      SortTransformer() {
         this((Comparator)null);
      }

      SortTransformer(Comparator<Byte> comparator) {
         this.comparator = comparator;
      }

      public byte[] transform(byte[] currentArray, boolean inPlace) {
         if (this.comparator == null) {
            byte[] out = inPlace ? currentArray : Bytes.from(currentArray).array();
            Arrays.sort(out);
            return out;
         } else {
            Byte[] boxedArray = Bytes.wrap(currentArray).toBoxedArray();
            Arrays.sort(boxedArray, this.comparator);
            return Bytes.from(boxedArray).array();
         }
      }

      public boolean supportInPlaceTransformation() {
         return this.comparator == null;
      }

      static final class UnsignedByteComparator implements Comparator<Byte> {
         public int compare(Byte o1, Byte o2) {
            int byteA = o1 & 255;
            int byteB = o2 & 255;
            if (byteA == byteB) {
               return 0;
            } else {
               return byteA < byteB ? -1 : 1;
            }
         }
      }
   }

   public static final class ShuffleTransformer implements BytesTransformer {
      private final Random random;

      ShuffleTransformer(Random random) {
         Objects.requireNonNull(random, "passed random must not be null");
         this.random = random;
      }

      public byte[] transform(byte[] currentArray, boolean inPlace) {
         byte[] out = inPlace ? currentArray : Bytes.from(currentArray).array();
         Util.Byte.shuffle(out, this.random);
         return out;
      }

      public boolean supportInPlaceTransformation() {
         return true;
      }
   }
}
