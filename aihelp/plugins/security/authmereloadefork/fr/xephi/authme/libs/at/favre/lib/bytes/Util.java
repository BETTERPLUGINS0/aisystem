package fr.xephi.authme.libs.at.favre.lib.bytes;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

final class Util {
   private Util() {
   }

   static final class BytesIterator implements Iterator<java.lang.Byte> {
      private final byte[] array;
      private int cursor = 0;

      BytesIterator(byte[] array) {
         this.array = array;
      }

      public boolean hasNext() {
         return this.cursor != this.array.length;
      }

      public java.lang.Byte next() {
         try {
            int i = this.cursor;
            java.lang.Byte next = this.array[i];
            this.cursor = i + 1;
            return next;
         } catch (IndexOutOfBoundsException var3) {
            throw new NoSuchElementException();
         }
      }

      public void remove() {
         throw new UnsupportedOperationException("The Bytes iterator does not support removing");
      }
   }

   static final class File {
      private static final int BUF_SIZE = 4096;

      private File() {
      }

      static byte[] readFromStream(InputStream inputStream, int maxLengthToRead) {
         boolean readWholeStream = maxLengthToRead == -1;
         int remaining = maxLengthToRead;

         try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(readWholeStream ? 32 : maxLengthToRead);
            byte[] buf = new byte[0];

            while(readWholeStream || remaining > 0) {
               int bufSize = Math.min(4096, readWholeStream ? 4096 : remaining);
               if (buf.length != bufSize) {
                  buf = new byte[bufSize];
               }

               int r = inputStream.read(buf);
               if (r == -1) {
                  break;
               }

               remaining -= r;
               out.write(buf, 0, r);
            }

            return out.toByteArray();
         } catch (Exception var8) {
            throw new IllegalStateException("could not read from input stream", var8);
         }
      }

      static byte[] readFromDataInput(DataInput dataInput, int length) {
         ByteArrayOutputStream out = new ByteArrayOutputStream(length);

         try {
            int remaining = length;

            for(int i = 0; i < length; ++i) {
               byte[] buf = new byte[Math.min(remaining, 4096)];
               dataInput.readFully(buf);
               out.write(buf);
               remaining -= buf.length;
            }

            return out.toByteArray();
         } catch (Exception var6) {
            throw new IllegalStateException("could not read from data input", var6);
         }
      }

      static byte[] readFromFile(java.io.File file) {
         Util.Validation.checkFileExists(file);

         try {
            return Files.readAllBytes(file.toPath());
         } catch (IOException var2) {
            throw new IllegalStateException("could not read from file", var2);
         }
      }

      static byte[] readFromFile(java.io.File file, int offset, int length) {
         Util.Validation.checkFileExists(file);

         try {
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            Throwable var4 = null;

            byte[] var5;
            try {
               raf.seek((long)offset);
               var5 = readFromDataInput(raf, length);
            } catch (Throwable var15) {
               var4 = var15;
               throw var15;
            } finally {
               if (raf != null) {
                  if (var4 != null) {
                     try {
                        raf.close();
                     } catch (Throwable var14) {
                        var4.addSuppressed(var14);
                     }
                  } else {
                     raf.close();
                  }
               }

            }

            return var5;
         } catch (Exception var17) {
            throw new IllegalStateException("could not read from random access file", var17);
         }
      }
   }

   static final class Validation {
      private Validation() {
      }

      static void checkIndexBounds(int length, int index, int primitiveLength, String type) {
         if (index < 0 || index + primitiveLength > length) {
            throw new IndexOutOfBoundsException("cannot get " + type + " from index out of bounds: " + index);
         }
      }

      static void checkExactLength(int length, int expectedLength, String type) {
         if (length != expectedLength) {
            throw new IllegalArgumentException("cannot convert to " + type + " if length != " + expectedLength + " bytes (was " + length + ")");
         }
      }

      static void checkModLength(int length, int modFactor, String errorSubject) {
         if (length % modFactor != 0) {
            throw new IllegalArgumentException("Illegal length for " + errorSubject + ". Byte array length must be multiple of " + modFactor + ", length was " + length);
         }
      }

      private static void checkFileExists(java.io.File file) {
         if (file == null || !file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("file must not be null, has to exist and must be a file (not a directory) " + file);
         }
      }
   }

   static final class Obj {
      private Obj() {
      }

      static boolean equals(byte[] obj, java.lang.Byte[] anotherArray) {
         if (anotherArray == null) {
            return false;
         } else if (obj.length != anotherArray.length) {
            return false;
         } else {
            for(int i = 0; i < obj.length; ++i) {
               if (anotherArray[i] == null || obj[i] != anotherArray[i]) {
                  return false;
               }
            }

            return true;
         }
      }

      static int hashCode(byte[] byteArray, ByteOrder byteOrder) {
         int result = Arrays.hashCode(byteArray);
         result = 31 * result + (byteOrder != null ? byteOrder.hashCode() : 0);
         return result;
      }

      static String toString(Bytes bytes) {
         String preview;
         if (bytes.isEmpty()) {
            preview = "";
         } else if (bytes.length() > 8) {
            preview = "(0x" + bytes.copy(0, 4).encodeHex() + "..." + bytes.copy(bytes.length() - 4, 4).encodeHex() + ")";
         } else {
            preview = "(0x" + bytes.encodeHex() + ")";
         }

         return bytes.length() + " " + (bytes.length() == 1 ? "byte" : "bytes") + " " + preview;
      }
   }

   static final class Converter {
      private Converter() {
      }

      static byte[] toArray(Collection<java.lang.Byte> collection) {
         int len = collection.size();
         byte[] array = new byte[len];
         int i = 0;

         for(Iterator var4 = collection.iterator(); var4.hasNext(); ++i) {
            java.lang.Byte b = (java.lang.Byte)var4.next();
            array[i] = b;
         }

         return array;
      }

      static java.lang.Byte[] toBoxedArray(byte[] array) {
         java.lang.Byte[] objectArray = new java.lang.Byte[array.length];

         for(int i = 0; i < array.length; ++i) {
            objectArray[i] = array[i];
         }

         return objectArray;
      }

      static List<java.lang.Byte> toList(byte[] array) {
         List<java.lang.Byte> list = new ArrayList(array.length);
         byte[] var2 = array;
         int var3 = array.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            byte b = var2[var4];
            list.add(b);
         }

         return list;
      }

      static byte[] toPrimitiveArray(java.lang.Byte[] objectArray) {
         byte[] primitivesArray = new byte[objectArray.length];

         for(int i = 0; i < objectArray.length; ++i) {
            primitivesArray[i] = objectArray[i];
         }

         return primitivesArray;
      }

      static byte[] toByteArray(int[] intArray) {
         byte[] primitivesArray = new byte[intArray.length * 4];
         ByteBuffer buffer = ByteBuffer.allocate(4);

         for(int i = 0; i < intArray.length; ++i) {
            buffer.clear();
            byte[] intBytes = buffer.putInt(intArray[i]).array();
            System.arraycopy(intBytes, 0, primitivesArray, i * 4, intBytes.length);
         }

         return primitivesArray;
      }

      static byte[] toByteArray(float[] floatArray) {
         byte[] primitivesArray = new byte[floatArray.length * 4];
         ByteBuffer buffer = ByteBuffer.allocate(4);

         for(int i = 0; i < floatArray.length; ++i) {
            buffer.clear();
            byte[] floatBytes = buffer.putFloat(floatArray[i]).array();
            System.arraycopy(floatBytes, 0, primitivesArray, i * 4, floatBytes.length);
         }

         return primitivesArray;
      }

      static byte[] toByteArray(long[] longArray) {
         byte[] primitivesArray = new byte[longArray.length * 8];
         ByteBuffer buffer = ByteBuffer.allocate(8);

         for(int i = 0; i < longArray.length; ++i) {
            buffer.clear();
            byte[] longBytes = buffer.putLong(longArray[i]).array();
            System.arraycopy(longBytes, 0, primitivesArray, i * 8, longBytes.length);
         }

         return primitivesArray;
      }

      static byte[] toByteArray(double[] doubleArray) {
         byte[] primitivesArray = new byte[doubleArray.length * 8];
         ByteBuffer buffer = ByteBuffer.allocate(8);

         for(int i = 0; i < doubleArray.length; ++i) {
            buffer.clear();
            byte[] doubleBytes = buffer.putDouble(doubleArray[i]).array();
            System.arraycopy(doubleBytes, 0, primitivesArray, i * 8, doubleBytes.length);
         }

         return primitivesArray;
      }

      static byte[] charToByteArray(char[] charArray, Charset charset, int offset, int length) {
         if (offset >= 0 && offset <= charArray.length) {
            if (length >= 0 && length <= charArray.length) {
               if (offset + length > charArray.length) {
                  throw new IllegalArgumentException("length + offset must be smaller than array length");
               } else if (length == 0) {
                  return new byte[0];
               } else {
                  CharBuffer charBuffer = CharBuffer.wrap(charArray);
                  if (offset != 0 || length != charBuffer.remaining()) {
                     charBuffer = charBuffer.subSequence(offset, offset + length);
                  }

                  ByteBuffer bb = charset.encode(charBuffer);
                  if (bb.capacity() != bb.limit()) {
                     byte[] bytes = new byte[bb.remaining()];
                     bb.get(bytes);
                     return bytes;
                  } else {
                     return bb.array();
                  }
               }
            } else {
               throw new IllegalArgumentException("length must be at least 1 and less than array length");
            }
         } else {
            throw new IllegalArgumentException("offset must be gt 0 and smaller than array length");
         }
      }

      static char[] byteToCharArray(byte[] bytes, Charset charset, ByteOrder byteOrder) {
         Objects.requireNonNull(bytes, "bytes must not be null");
         Objects.requireNonNull(charset, "charset must not be null");

         try {
            CharBuffer charBuffer = charset.newDecoder().decode(ByteBuffer.wrap(bytes).order(byteOrder));
            if (charBuffer.capacity() != charBuffer.limit()) {
               char[] compacted = new char[charBuffer.remaining()];
               charBuffer.get(compacted);
               return compacted;
            } else {
               return charBuffer.array();
            }
         } catch (CharacterCodingException var5) {
            throw new IllegalStateException(var5);
         }
      }

      static int[] toIntArray(byte[] bytes, ByteOrder byteOrder) {
         IntBuffer buffer = ByteBuffer.wrap(bytes).order(byteOrder).asIntBuffer();
         int[] array = new int[buffer.remaining()];
         buffer.get(array);
         return array;
      }

      static long[] toLongArray(byte[] bytes, ByteOrder byteOrder) {
         LongBuffer buffer = ByteBuffer.wrap(bytes).order(byteOrder).asLongBuffer();
         long[] array = new long[buffer.remaining()];
         buffer.get(array);
         return array;
      }

      static float[] toFloatArray(byte[] bytes, ByteOrder byteOrder) {
         FloatBuffer buffer = ByteBuffer.wrap(bytes).order(byteOrder).asFloatBuffer();
         float[] array = new float[buffer.remaining()];
         buffer.get(array);
         return array;
      }

      static double[] toDoubleArray(byte[] bytes, ByteOrder byteOrder) {
         DoubleBuffer buffer = ByteBuffer.wrap(bytes).order(byteOrder).asDoubleBuffer();
         double[] array = new double[buffer.remaining()];
         buffer.get(array);
         return array;
      }

      static ByteBuffer toBytesFromUUID(UUID uuid) {
         ByteBuffer bb = ByteBuffer.allocate(16);
         bb.putLong(uuid.getMostSignificantBits());
         bb.putLong(uuid.getLeastSignificantBits());
         return bb;
      }
   }

   static final class Byte {
      private Byte() {
      }

      static byte[] concat(byte[]... arrays) {
         int length = 0;
         byte[][] var2 = arrays;
         int pos = arrays.length;

         for(int var4 = 0; var4 < pos; ++var4) {
            byte[] array = var2[var4];
            length += array.length;
         }

         byte[] result = new byte[length];
         pos = 0;
         byte[][] var9 = arrays;
         int var10 = arrays.length;

         for(int var6 = 0; var6 < var10; ++var6) {
            byte[] array = var9[var6];
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
         }

         return result;
      }

      static byte[] concatVararg(byte firstByte, byte[] moreBytes) {
         return moreBytes == null ? new byte[]{firstByte} : concat(new byte[]{firstByte}, moreBytes);
      }

      static int indexOf(byte[] array, byte[] target, int start, int end) {
         Objects.requireNonNull(array, "array must not be null");
         Objects.requireNonNull(target, "target must not be null");
         if (target.length != 0 && start >= 0) {
            label27:
            for(int i = start; i < Math.min(end, array.length - target.length + 1); ++i) {
               for(int j = 0; j < target.length; ++j) {
                  if (array[i + j] != target[j]) {
                     continue label27;
                  }
               }

               return i;
            }

            return -1;
         } else {
            return -1;
         }
      }

      static int lastIndexOf(byte[] array, byte target, int start, int end) {
         for(int i = end - 1; i >= start; --i) {
            if (array[i] == target) {
               return i;
            }
         }

         return -1;
      }

      static int countByte(byte[] array, byte target) {
         int count = 0;
         byte[] var3 = array;
         int var4 = array.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            byte b = var3[var5];
            if (b == target) {
               ++count;
            }
         }

         return count;
      }

      static int countByteArray(byte[] array, byte[] pattern) {
         Objects.requireNonNull(pattern, "pattern must not be null");
         if (pattern.length != 0 && array.length != 0) {
            int count = 0;

            label27:
            for(int i = 0; i < array.length - pattern.length + 1; ++i) {
               for(int j = 0; j < pattern.length; ++j) {
                  if (array[i + j] != pattern[j]) {
                     continue label27;
                  }
               }

               ++count;
            }

            return count;
         } else {
            return 0;
         }
      }

      static void shuffle(byte[] array, Random random) {
         for(int i = array.length - 1; i > 0; --i) {
            int index = random.nextInt(i + 1);
            byte a = array[index];
            array[index] = array[i];
            array[i] = a;
         }

      }

      static void reverse(byte[] array, int fromIndex, int toIndex) {
         Objects.requireNonNull(array);
         int i = fromIndex;

         for(int j = toIndex - 1; i < j; --j) {
            byte tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
            ++i;
         }

      }

      static byte[] shiftLeft(byte[] byteArray, int shiftBitCount, ByteOrder byteOrder) {
         int shiftMod = shiftBitCount % 8;
         byte carryMask = (byte)((1 << shiftMod) - 1);
         int offsetBytes = shiftBitCount / 8;
         int sourceIndex;
         int i;
         byte src;
         byte dst;
         if (byteOrder == ByteOrder.BIG_ENDIAN) {
            for(i = 0; i < byteArray.length; ++i) {
               sourceIndex = i + offsetBytes;
               if (sourceIndex >= byteArray.length) {
                  byteArray[i] = 0;
               } else {
                  src = byteArray[sourceIndex];
                  dst = (byte)(src << shiftMod);
                  if (sourceIndex + 1 < byteArray.length) {
                     dst = (byte)(dst | byteArray[sourceIndex + 1] >>> 8 - shiftMod & carryMask);
                  }

                  byteArray[i] = dst;
               }
            }
         } else {
            for(i = byteArray.length - 1; i >= 0; --i) {
               sourceIndex = i - offsetBytes;
               if (sourceIndex < 0) {
                  byteArray[i] = 0;
               } else {
                  src = byteArray[sourceIndex];
                  dst = (byte)(src << shiftMod);
                  if (sourceIndex - 1 >= 0) {
                     dst = (byte)(dst | byteArray[sourceIndex - 1] >>> 8 - shiftMod & carryMask);
                  }

                  byteArray[i] = dst;
               }
            }
         }

         return byteArray;
      }

      static byte[] shiftRight(byte[] byteArray, int shiftBitCount, ByteOrder byteOrder) {
         int shiftMod = shiftBitCount % 8;
         byte carryMask = (byte)(255 << 8 - shiftMod);
         int offsetBytes = shiftBitCount / 8;
         int sourceIndex;
         int i;
         byte src;
         byte dst;
         if (byteOrder == ByteOrder.BIG_ENDIAN) {
            for(i = byteArray.length - 1; i >= 0; --i) {
               sourceIndex = i - offsetBytes;
               if (sourceIndex < 0) {
                  byteArray[i] = 0;
               } else {
                  src = byteArray[sourceIndex];
                  dst = (byte)((255 & src) >>> shiftMod);
                  if (sourceIndex - 1 >= 0) {
                     dst = (byte)(dst | byteArray[sourceIndex - 1] << 8 - shiftMod & carryMask);
                  }

                  byteArray[i] = dst;
               }
            }
         } else {
            for(i = 0; i < byteArray.length; ++i) {
               sourceIndex = i + offsetBytes;
               if (sourceIndex >= byteArray.length) {
                  byteArray[i] = 0;
               } else {
                  src = byteArray[sourceIndex];
                  dst = (byte)((255 & src) >>> shiftMod);
                  if (sourceIndex + 1 < byteArray.length) {
                     dst = (byte)(dst | byteArray[sourceIndex + 1] << 8 - shiftMod & carryMask);
                  }

                  byteArray[i] = dst;
               }
            }
         }

         return byteArray;
      }

      static boolean constantTimeEquals(byte[] array, byte[] anotherArray) {
         if (anotherArray != null && array.length == anotherArray.length) {
            int result = 0;

            for(int i = 0; i < array.length; ++i) {
               result |= array[i] ^ anotherArray[i];
            }

            return result == 0;
         } else {
            return false;
         }
      }

      static double entropy(byte[] array) {
         int[] buffer = new int[256];
         Arrays.fill(buffer, -1);
         byte[] var2 = array;
         int var3 = array.length;

         int unsigned;
         for(int var4 = 0; var4 < var3; ++var4) {
            byte element = var2[var4];
            unsigned = 255 & element;
            if (buffer[unsigned] == -1) {
               buffer[unsigned] = 0;
            }

            int var10002 = buffer[unsigned]++;
         }

         double entropy = 0.0D;
         int[] var11 = buffer;
         int var12 = buffer.length;

         for(unsigned = 0; unsigned < var12; ++unsigned) {
            int count = var11[unsigned];
            if (count != -1) {
               double prob = (double)count / (double)array.length;
               entropy -= prob * (Math.log(prob) / Math.log(2.0D));
            }
         }

         return entropy;
      }
   }
}
