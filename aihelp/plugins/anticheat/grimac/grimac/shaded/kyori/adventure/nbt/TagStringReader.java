package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.IntStream.Builder;

final class TagStringReader {
   private static final int MAX_DEPTH = 512;
   private static final int HEX_RADIX = 16;
   private static final int BINARY_RADIX = 2;
   private static final int DECIMAL_RADIX = 10;
   private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
   private static final int[] EMPTY_INT_ARRAY = new int[0];
   private static final long[] EMPTY_LONG_ARRAY = new long[0];
   private final CharBuffer buffer;
   private boolean acceptLegacy;
   private boolean acceptHeterogeneousLists;
   private int depth;

   TagStringReader(final CharBuffer buffer) {
      this.buffer = buffer;
   }

   public CompoundBinaryTag compound() throws StringTagParseException {
      this.buffer.expect('{');
      if (this.buffer.takeIf('}')) {
         return CompoundBinaryTag.empty();
      } else {
         CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder();

         do {
            if (!this.buffer.hasMore()) {
               throw this.buffer.makeError("Unterminated compound tag!");
            }

            builder.put(this.key(), this.tag());
         } while(!this.separatorOrCompleteWith('}'));

         return builder.build();
      }
   }

   public ListBinaryTag list() throws StringTagParseException {
      ListBinaryTag.Builder<BinaryTag> builder = this.acceptHeterogeneousLists ? ListBinaryTag.heterogeneousListBinaryTag() : ListBinaryTag.builder();
      this.buffer.expect('[');
      boolean prefixedIndex = this.acceptLegacy && this.buffer.peek() == '0' && this.buffer.peek(1) == ':';
      if (!prefixedIndex && this.buffer.takeIf(']')) {
         return ListBinaryTag.empty();
      } else {
         do {
            if (!this.buffer.hasMore()) {
               throw this.buffer.makeError("Reached end of file without end of list tag!");
            }

            if (prefixedIndex) {
               this.buffer.takeUntil(':');
            }

            BinaryTag next = this.tag();
            builder.add(next);
         } while(!this.separatorOrCompleteWith(']'));

         return builder.build();
      }
   }

   public BinaryTag array(char elementType) throws StringTagParseException {
      this.buffer.expect('[').expect(elementType).expect(';');
      elementType = Character.toLowerCase(elementType);
      if (elementType == 'b') {
         return ByteArrayBinaryTag.byteArrayBinaryTag(this.byteArray());
      } else if (elementType == 'i') {
         return IntArrayBinaryTag.intArrayBinaryTag(this.intArray());
      } else if (elementType == 'l') {
         return LongArrayBinaryTag.longArrayBinaryTag(this.longArray());
      } else {
         throw this.buffer.makeError("Type " + elementType + " is not a valid element type in an array!");
      }
   }

   private byte[] byteArray() throws StringTagParseException {
      if (this.buffer.takeIf(']')) {
         return EMPTY_BYTE_ARRAY;
      } else {
         ArrayList bytes = new ArrayList();

         while(this.buffer.hasMore()) {
            CharSequence value = this.buffer.skipWhitespace().takeUntil('b');

            try {
               bytes.add(Byte.valueOf(value.toString()));
            } catch (NumberFormatException var5) {
               throw this.buffer.makeError("All elements of a byte array must be bytes!");
            }

            if (this.separatorOrCompleteWith(']')) {
               byte[] result = new byte[bytes.size()];

               for(int i = 0; i < bytes.size(); ++i) {
                  result[i] = (Byte)bytes.get(i);
               }

               return result;
            }
         }

         throw this.buffer.makeError("Reached end of document without array close");
      }
   }

   private int[] intArray() throws StringTagParseException {
      if (this.buffer.takeIf(']')) {
         return EMPTY_INT_ARRAY;
      } else {
         Builder builder = IntStream.builder();

         do {
            if (!this.buffer.hasMore()) {
               throw this.buffer.makeError("Reached end of document without array close");
            }

            BinaryTag value = this.tag();
            if (!(value instanceof IntBinaryTag)) {
               throw this.buffer.makeError("All elements of an int array must be ints!");
            }

            builder.add(((IntBinaryTag)value).intValue());
         } while(!this.separatorOrCompleteWith(']'));

         return builder.build().toArray();
      }
   }

   private long[] longArray() throws StringTagParseException {
      if (this.buffer.takeIf(']')) {
         return EMPTY_LONG_ARRAY;
      } else {
         java.util.stream.LongStream.Builder longs = LongStream.builder();

         while(this.buffer.hasMore()) {
            CharSequence value = this.buffer.skipWhitespace().takeUntil('l');

            try {
               longs.add(Long.parseLong(value.toString()));
            } catch (NumberFormatException var4) {
               throw this.buffer.makeError("All elements of a long array must be longs!");
            }

            if (this.separatorOrCompleteWith(']')) {
               return longs.build().toArray();
            }
         }

         throw this.buffer.makeError("Reached end of document without array close");
      }
   }

   public String key() throws StringTagParseException {
      this.buffer.skipWhitespace();
      char starChar = this.buffer.peek();

      try {
         if (starChar == '\'' || starChar == '"') {
            String var7 = unescape(this.buffer.takeUntil(this.buffer.take()).toString());
            return var7;
         } else {
            StringBuilder builder = new StringBuilder();

            while(true) {
               if (this.buffer.hasMore()) {
                  char peek = this.buffer.peek();
                  if (Tokens.id(peek)) {
                     builder.append(this.buffer.take());
                     continue;
                  }

                  if (this.acceptLegacy) {
                     if (peek == '\\') {
                        this.buffer.take();
                        continue;
                     }

                     if (peek != ':') {
                        builder.append(this.buffer.take());
                        continue;
                     }
                  }
               }

               String var8 = builder.toString();
               return var8;
            }
         }
      } finally {
         this.buffer.expect(':');
      }
   }

   public BinaryTag tag() throws StringTagParseException {
      if (this.depth++ > 512) {
         throw this.buffer.makeError("Exceeded maximum allowed depth of 512 when reading tag");
      } else {
         try {
            char startToken = this.buffer.skipWhitespace().peek();
            BinaryTag var6;
            switch(startToken) {
            case '"':
            case '\'':
               this.buffer.advance();
               StringBinaryTag var8 = StringBinaryTag.stringBinaryTag(unescape(this.buffer.takeUntil(startToken).toString()));
               return var8;
            case '[':
               if (!this.buffer.hasMore(2) || this.buffer.peek(2) != ';') {
                  ListBinaryTag var7 = this.list();
                  return var7;
               }

               var6 = this.array(this.buffer.peek(1));
               return var6;
            case '{':
               CompoundBinaryTag var2 = this.compound();
               return var2;
            default:
               var6 = this.scalar();
               return var6;
            }
         } finally {
            --this.depth;
         }
      }
   }

   private BinaryTag scalar() throws StringTagParseException {
      StringBuilder builder;
      char current;
      for(builder = new StringBuilder(); this.buffer.hasMore(); builder.append(current)) {
         current = this.buffer.peek();
         if (current == '\\') {
            this.buffer.advance();
            current = this.buffer.take();
         } else {
            if (!Tokens.id(current)) {
               break;
            }

            this.buffer.advance();
         }
      }

      if (builder.length() == 0) {
         throw this.buffer.makeError("Expected a value but got nothing");
      } else {
         String original = builder.toString();
         int radix = this.extractRadix(builder, original);
         if (builder.length() == 0) {
            throw this.buffer.makeError("Input is a radix, not a number");
         } else {
            char last = builder.charAt(builder.length() - 1);
            boolean hasSignToken = false;
            boolean signed = radix != 16;
            if (builder.length() > 2) {
               char signChar = builder.charAt(builder.length() - 2);
               if (signChar == 's' || signChar == 'u') {
                  hasSignToken = true;
                  signed = signChar == 's';
                  builder.deleteCharAt(builder.length() - 2);
               }
            }

            boolean hasTypeToken = false;
            char typeToken = 'i';
            if (Tokens.numericType(last) && (hasSignToken || radix != 16)) {
               hasTypeToken = true;
               typeToken = Character.toLowerCase(last);
               builder.deleteCharAt(builder.length() - 1);
            }

            if (!signed && (typeToken == 'f' || typeToken == 'd')) {
               throw this.buffer.makeError("Cannot create unsigned floating point numbers");
            } else {
               String strippedString = builder.toString().replace("_", "");
               if (hasTypeToken) {
                  try {
                     NumberBinaryTag tag = this.parseNumberTag(strippedString, typeToken, radix, signed);
                     if (tag != null) {
                        return tag;
                     }
                  } catch (NumberFormatException var12) {
                  }
               } else {
                  try {
                     return IntBinaryTag.intBinaryTag(this.parseInt(strippedString, radix, signed));
                  } catch (NumberFormatException var14) {
                     if (strippedString.indexOf(46) != -1) {
                        try {
                           return DoubleBinaryTag.doubleBinaryTag(Double.parseDouble(strippedString));
                        } catch (NumberFormatException var13) {
                        }
                     }
                  }
               }

               if (original.equalsIgnoreCase("true")) {
                  return ByteBinaryTag.ONE;
               } else {
                  return (BinaryTag)(original.equalsIgnoreCase("false") ? ByteBinaryTag.ZERO : StringBinaryTag.stringBinaryTag(original));
               }
            }
         }
      }
   }

   private int extractRadix(final StringBuilder builder, final String original) {
      int radixPrefixOffset = 0;
      char first = builder.charAt(0);
      if (first == '+' || first == '-') {
         radixPrefixOffset = 1;
      }

      int radixEndIndex = 2 + radixPrefixOffset;
      byte radix;
      if (original.length() <= radixEndIndex || !original.startsWith("0b", radixPrefixOffset) && !original.startsWith("0B", radixPrefixOffset)) {
         if (!original.startsWith("0x", radixPrefixOffset) && !original.startsWith("0X", radixPrefixOffset)) {
            radix = 10;
         } else {
            radix = 16;
         }
      } else {
         radix = 2;
      }

      if (radix != 10) {
         builder.delete(radixPrefixOffset, radixEndIndex);
      }

      return radix;
   }

   @Nullable
   private NumberBinaryTag parseNumberTag(final String s, final char typeToken, final int radix, final boolean signed) {
      switch(typeToken) {
      case 'b':
         return ByteBinaryTag.byteBinaryTag(this.parseByte(s, radix, signed));
      case 'c':
      case 'e':
      case 'g':
      case 'h':
      case 'j':
      case 'k':
      case 'm':
      case 'n':
      case 'o':
      case 'p':
      case 'q':
      case 'r':
      default:
         break;
      case 'd':
         double doubleValue = Double.parseDouble(s);
         if (Double.isFinite(doubleValue)) {
            return DoubleBinaryTag.doubleBinaryTag(doubleValue);
         }
         break;
      case 'f':
         float floatValue = Float.parseFloat(s);
         if (Float.isFinite(floatValue)) {
            return FloatBinaryTag.floatBinaryTag(floatValue);
         }
         break;
      case 'i':
         return IntBinaryTag.intBinaryTag(this.parseInt(s, radix, signed));
      case 'l':
         return LongBinaryTag.longBinaryTag(this.parseLong(s, radix, signed));
      case 's':
         return ShortBinaryTag.shortBinaryTag(this.parseShort(s, radix, signed));
      }

      return null;
   }

   private byte parseByte(final String s, final int radix, final boolean signed) {
      if (signed) {
         return Byte.parseByte(s, radix);
      } else {
         int parsedInt = Integer.parseInt(s, radix);
         if (parsedInt >> 8 == 0) {
            return (byte)parsedInt;
         } else {
            throw new NumberFormatException();
         }
      }
   }

   private short parseShort(final String s, final int radix, final boolean signed) {
      if (signed) {
         return Short.parseShort(s, radix);
      } else {
         int parsedInt = Integer.parseInt(s, radix);
         if (parsedInt >> 16 == 0) {
            return (short)parsedInt;
         } else {
            throw new NumberFormatException();
         }
      }
   }

   private int parseInt(final String s, final int radix, final boolean signed) {
      return signed ? Integer.parseInt(s, radix) : Integer.parseUnsignedInt(s, radix);
   }

   private long parseLong(final String s, final int radix, final boolean signed) {
      return signed ? Long.parseLong(s, radix) : Long.parseUnsignedLong(s, radix);
   }

   private boolean separatorOrCompleteWith(final char endCharacter) throws StringTagParseException {
      if (this.buffer.takeIf(endCharacter)) {
         return true;
      } else {
         this.buffer.expect(',');
         return this.buffer.takeIf(endCharacter);
      }
   }

   private static String unescape(final String withEscapes) {
      int escapeIdx = withEscapes.indexOf(92);
      if (escapeIdx == -1) {
         return withEscapes;
      } else {
         int lastEscape = 0;
         StringBuilder output = new StringBuilder(withEscapes.length());

         do {
            output.append(withEscapes, lastEscape, escapeIdx);
            lastEscape = escapeIdx + 1;
         } while((escapeIdx = withEscapes.indexOf(92, lastEscape + 1)) != -1);

         output.append(withEscapes.substring(lastEscape));
         return output.toString();
      }
   }

   public void legacy(final boolean acceptLegacy) {
      this.acceptLegacy = acceptLegacy;
   }

   public void heterogeneousLists(final boolean acceptHeterogeneousLists) {
      this.acceptHeterogeneousLists = acceptHeterogeneousLists;
   }
}
