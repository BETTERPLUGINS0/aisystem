package fr.xephi.authme.libs.at.favre.lib.bytes;

import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public interface BytesTransformer {
   byte[] transform(byte[] var1, boolean var2);

   boolean supportInPlaceTransformation();

   public static class MessageDigestTransformer implements BytesTransformer {
      static final String ALGORITHM_MD5 = "MD5";
      static final String ALGORITHM_SHA_1 = "SHA-1";
      static final String ALGORITHM_SHA_256 = "SHA-256";
      private final MessageDigest messageDigest;

      MessageDigestTransformer(String digestName) {
         try {
            this.messageDigest = MessageDigest.getInstance(digestName);
         } catch (NoSuchAlgorithmException var3) {
            throw new IllegalArgumentException("could not get message digest algorithm " + digestName, var3);
         }
      }

      public byte[] transform(byte[] currentArray, boolean inPlace) {
         this.messageDigest.update(currentArray);
         return this.messageDigest.digest();
      }

      public boolean supportInPlaceTransformation() {
         return false;
      }
   }

   public static class BitSwitchTransformer implements BytesTransformer {
      private final int position;
      private final Boolean newBitValue;

      BitSwitchTransformer(int position, Boolean newBitValue) {
         this.position = position;
         this.newBitValue = newBitValue;
      }

      public byte[] transform(byte[] currentArray, boolean inPlace) {
         byte[] out = inPlace ? currentArray : Bytes.from(currentArray).array();
         if (this.position >= 0 && this.position < 8 * currentArray.length) {
            int bytePosition = currentArray.length - 1 - this.position / 8;
            if (this.newBitValue == null) {
               out[bytePosition] = (byte)(out[bytePosition] ^ 1 << this.position % 8);
            } else if (this.newBitValue) {
               out[bytePosition] = (byte)(out[bytePosition] | 1 << this.position % 8);
            } else {
               out[bytePosition] = (byte)(out[bytePosition] & ~(1 << this.position % 8));
            }

            return out;
         } else {
            throw new IllegalArgumentException("bit index " + this.position * 8 + " out of bounds");
         }
      }

      public boolean supportInPlaceTransformation() {
         return true;
      }
   }

   public static final class ResizeTransformer implements BytesTransformer {
      private final int newSize;
      private final BytesTransformer.ResizeTransformer.Mode mode;

      ResizeTransformer(int newSize, BytesTransformer.ResizeTransformer.Mode mode) {
         this.newSize = newSize;
         this.mode = mode;
      }

      public byte[] transform(byte[] currentArray, boolean inPlace) {
         if (currentArray.length == this.newSize) {
            return currentArray;
         } else if (this.newSize < 0) {
            throw new IllegalArgumentException("cannot resize to smaller than 0");
         } else if (this.newSize == 0) {
            return new byte[0];
         } else {
            byte[] resizedArray = new byte[this.newSize];
            if (this.mode == BytesTransformer.ResizeTransformer.Mode.RESIZE_KEEP_FROM_MAX_LENGTH) {
               if (this.newSize > currentArray.length) {
                  System.arraycopy(currentArray, 0, resizedArray, Math.max(0, Math.abs(this.newSize - currentArray.length)), Math.min(this.newSize, currentArray.length));
               } else {
                  System.arraycopy(currentArray, Math.max(0, Math.abs(this.newSize - currentArray.length)), resizedArray, Math.min(0, Math.abs(this.newSize - currentArray.length)), Math.min(this.newSize, currentArray.length));
               }
            } else {
               System.arraycopy(currentArray, 0, resizedArray, 0, Math.min(currentArray.length, resizedArray.length));
            }

            return resizedArray;
         }
      }

      public boolean supportInPlaceTransformation() {
         return false;
      }

      public static enum Mode {
         RESIZE_KEEP_FROM_ZERO_INDEX,
         RESIZE_KEEP_FROM_MAX_LENGTH;
      }
   }

   public static final class CopyTransformer implements BytesTransformer {
      final int offset;
      final int length;

      CopyTransformer(int offset, int length) {
         this.offset = offset;
         this.length = length;
      }

      public byte[] transform(byte[] currentArray, boolean inPlace) {
         byte[] copy = new byte[this.length];
         System.arraycopy(currentArray, this.offset, copy, 0, copy.length);
         return copy;
      }

      public boolean supportInPlaceTransformation() {
         return false;
      }
   }

   public static final class ReverseTransformer implements BytesTransformer {
      public byte[] transform(byte[] currentArray, boolean inPlace) {
         byte[] out = inPlace ? currentArray : Bytes.from(currentArray).array();
         Util.Byte.reverse(out, 0, out.length);
         return out;
      }

      public boolean supportInPlaceTransformation() {
         return true;
      }
   }

   public static final class ConcatTransformer implements BytesTransformer {
      private final byte[] secondArray;

      ConcatTransformer(byte[] secondArrays) {
         this.secondArray = (byte[])Objects.requireNonNull(secondArrays, "the second byte array must not be null");
      }

      public byte[] transform(byte[] currentArray, boolean inPlace) {
         return Util.Byte.concat(currentArray, this.secondArray);
      }

      public boolean supportInPlaceTransformation() {
         return false;
      }
   }

   public static final class ShiftTransformer implements BytesTransformer {
      private final int shiftCount;
      private final BytesTransformer.ShiftTransformer.Type type;
      private final ByteOrder byteOrder;

      ShiftTransformer(int shiftCount, BytesTransformer.ShiftTransformer.Type type, ByteOrder byteOrder) {
         this.shiftCount = shiftCount;
         this.type = (BytesTransformer.ShiftTransformer.Type)Objects.requireNonNull(type, "passed shift type must not be null");
         this.byteOrder = (ByteOrder)Objects.requireNonNull(byteOrder, "passed byteOrder type must not be null");
      }

      public byte[] transform(byte[] currentArray, boolean inPlace) {
         byte[] out = inPlace ? currentArray : Bytes.from(currentArray).array();
         switch(this.type) {
         case RIGHT_SHIFT:
            return Util.Byte.shiftRight(out, this.shiftCount, this.byteOrder);
         case LEFT_SHIFT:
         default:
            return Util.Byte.shiftLeft(out, this.shiftCount, this.byteOrder);
         }
      }

      public boolean supportInPlaceTransformation() {
         return true;
      }

      public static enum Type {
         LEFT_SHIFT,
         RIGHT_SHIFT;
      }
   }

   public static final class NegateTransformer implements BytesTransformer {
      public byte[] transform(byte[] currentArray, boolean inPlace) {
         byte[] out = inPlace ? currentArray : Bytes.from(currentArray).array();

         for(int i = 0; i < out.length; ++i) {
            out[i] = (byte)(~out[i]);
         }

         return out;
      }

      public boolean supportInPlaceTransformation() {
         return true;
      }
   }

   public static final class BitWiseOperatorTransformer implements BytesTransformer {
      private final byte[] secondArray;
      private final BytesTransformer.BitWiseOperatorTransformer.Mode mode;

      BitWiseOperatorTransformer(byte[] secondArray, BytesTransformer.BitWiseOperatorTransformer.Mode mode) {
         this.secondArray = (byte[])Objects.requireNonNull(secondArray, "the second byte array must not be null");
         this.mode = (BytesTransformer.BitWiseOperatorTransformer.Mode)Objects.requireNonNull(mode, "passed bitwise mode must not be null");
      }

      public byte[] transform(byte[] currentArray, boolean inPlace) {
         if (currentArray.length != this.secondArray.length) {
            throw new IllegalArgumentException("all byte array must be of same length doing bit wise operation");
         } else {
            byte[] out = inPlace ? currentArray : new byte[currentArray.length];

            for(int i = 0; i < currentArray.length; ++i) {
               switch(this.mode) {
               case AND:
                  out[i] = (byte)(currentArray[i] & this.secondArray[i]);
                  break;
               case XOR:
                  out[i] = (byte)(currentArray[i] ^ this.secondArray[i]);
                  break;
               case OR:
               default:
                  out[i] = (byte)(currentArray[i] | this.secondArray[i]);
               }
            }

            return out;
         }
      }

      public boolean supportInPlaceTransformation() {
         return true;
      }

      public static enum Mode {
         AND,
         OR,
         XOR;
      }
   }
}
