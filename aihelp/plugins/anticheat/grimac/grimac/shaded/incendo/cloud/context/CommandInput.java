package ac.grim.grimac.shaded.incendo.cloud.context;

import ac.grim.grimac.shaded.incendo.cloud.type.range.ByteRange;
import ac.grim.grimac.shaded.incendo.cloud.type.range.DoubleRange;
import ac.grim.grimac.shaded.incendo.cloud.type.range.FloatRange;
import ac.grim.grimac.shaded.incendo.cloud.type.range.IntRange;
import ac.grim.grimac.shaded.incendo.cloud.type.range.LongRange;
import ac.grim.grimac.shaded.incendo.cloud.type.range.ShortRange;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.returnsreceiver.qual.This;
import org.checkerframework.dataflow.qual.Pure;
import org.checkerframework.dataflow.qual.SideEffectFree;

@API(
   status = Status.EXPERIMENTAL
)
public interface CommandInput {
   List<String> BOOLEAN_STRICT = Collections.unmodifiableList(Arrays.asList("TRUE", "FALSE"));
   List<String> BOOLEAN_LIBERAL = Collections.unmodifiableList(Arrays.asList("TRUE", "YES", "ON", "FALSE", "NO", "OFF"));
   List<String> BOOLEAN_LIBERAL_TRUE = Collections.unmodifiableList(Arrays.asList("TRUE", "YES", "ON"));

   @NonNull
   static CommandInput of(@NonNull final String input) {
      return new CommandInputImpl(input);
   }

   @NonNull
   static CommandInput of(@NonNull final Iterable<String> input) {
      return new CommandInputImpl(String.join(" ", input));
   }

   @NonNull
   static CommandInput empty() {
      return new CommandInputImpl("");
   }

   @Pure
   @NonNull
   String input();

   @SideEffectFree
   @NonNegative
   int cursor();

   @Pure
   @NonNegative
   default int length() {
      return this.input().length();
   }

   @SideEffectFree
   @NonNegative
   default int remainingLength() {
      return this.length() - this.cursor();
   }

   @SideEffectFree
   @NonNegative
   default int remainingTokens() {
      int count = (new StringTokenizer(this.remainingInput(), " ")).countTokens();
      return this.remainingInput().endsWith(" ") ? count + 1 : count;
   }

   @SideEffectFree
   @NonNull
   default String remainingInput() {
      return this.input().substring(this.cursor());
   }

   @SideEffectFree
   @NonNull
   default String readInput() {
      return this.input().substring(0, this.cursor());
   }

   @NonNull
   CommandInput appendString(@NonNull String string);

   @SideEffectFree
   default boolean hasRemainingInput() {
      return this.cursor() < this.length();
   }

   @SideEffectFree
   default boolean isEmpty() {
      return this.isEmpty(false);
   }

   @SideEffectFree
   default boolean isEmpty(final boolean ignoreWhitespace) {
      return !this.hasRemainingInput(ignoreWhitespace);
   }

   @SideEffectFree
   default boolean hasRemainingInput(final boolean ignoreWhitespace) {
      if (!this.hasRemainingInput()) {
         return false;
      } else {
         return ignoreWhitespace ? this.hasNonWhitespace() : true;
      }
   }

   void moveCursor(int chars);

   @This
   @NonNull
   CommandInput cursor(@NonNegative int position);

   @SideEffectFree
   @NonNull
   default String peekString(@NonNegative final int chars) {
      String remainingInput = this.remainingInput();
      if (chars > remainingInput.length()) {
         throw new CommandInput.CursorOutOfBoundsException(this.cursor() + chars, this.length());
      } else {
         return remainingInput.substring(0, chars);
      }
   }

   @NonNull
   default String read(@NonNegative final int chars) {
      String readString = this.peekString(chars);
      this.moveCursor(chars);
      return readString;
   }

   @SideEffectFree
   default char peek() {
      if (this.cursor() >= this.input().length()) {
         throw new CommandInput.CursorOutOfBoundsException(this.cursor(), this.length());
      } else {
         return this.input().charAt(this.cursor());
      }
   }

   default char read() {
      char readChar = this.peek();
      this.moveCursor(1);
      return readChar;
   }

   @NonNull
   default String peekString() {
      if (!this.hasRemainingInput()) {
         return "";
      } else {
         String remainingInput = this.remainingInput();
         int indexOfWhitespace = remainingInput.indexOf(32);
         if (indexOfWhitespace == -1) {
            return remainingInput;
         } else {
            StringBuilder builder = new StringBuilder();

            for(int i = 0; i < remainingInput.length(); ++i) {
               char currentChar = remainingInput.charAt(i);
               if (Character.isWhitespace(currentChar)) {
                  if (builder.length() != 0) {
                     break;
                  }
               } else {
                  builder.append(currentChar);
               }
            }

            return builder.toString();
         }
      }
   }

   @NonNull
   default String readStringSkipWhitespace(final boolean preserveSingleSpace) {
      String readString = this.readString();
      this.skipWhitespace(preserveSingleSpace);
      return readString;
   }

   @NonNull
   default String readStringSkipWhitespace() {
      return this.readStringSkipWhitespace(true);
   }

   @NonNull
   default String readString() {
      return this.skipWhitespace().readUntil(' ');
   }

   @NonNull
   default String readUntil(final char separator) {
      if (!this.hasRemainingInput()) {
         return "";
      } else {
         String remainingInput = this.remainingInput();
         int indexOfWhitespace = remainingInput.indexOf(separator);
         if (indexOfWhitespace == -1) {
            this.moveCursor(this.remainingLength());
            return remainingInput;
         } else {
            return this.read(indexOfWhitespace);
         }
      }
   }

   @NonNull
   default String readUntilAndSkip(final char separator) {
      String readString = this.readUntil(separator);
      if (!readString.isEmpty() && this.hasRemainingInput()) {
         char readChar = this.read();
         if (readChar != separator) {
            this.moveCursor(-1);
         }

         return readString;
      } else {
         return readString;
      }
   }

   @This
   @NonNull
   default CommandInput skipWhitespace(final int maxSpaces, final boolean preserveSingleSpace) {
      if (preserveSingleSpace && this.remainingLength() == 1 && this.peek() == ' ') {
         return this;
      } else {
         for(int i = 0; i < maxSpaces && this.hasRemainingInput() && Character.isWhitespace(this.peek()); ++i) {
            this.read();
         }

         return this;
      }
   }

   @This
   @NonNull
   default CommandInput skipWhitespace(final int maxSpaces) {
      return this.skipWhitespace(maxSpaces, false);
   }

   @This
   @NonNull
   default CommandInput skipWhitespace(final boolean preserveSingleSpace) {
      return this.skipWhitespace(Integer.MAX_VALUE, preserveSingleSpace);
   }

   @This
   @NonNull
   default CommandInput skipWhitespace() {
      return this.skipWhitespace(false);
   }

   default boolean hasNonWhitespace() {
      return this.remainingInput().chars().anyMatch((c) -> {
         return !Character.isWhitespace(c);
      });
   }

   @SideEffectFree
   default boolean isValidByte(final byte min, final byte max) {
      try {
         byte parsedByte = Byte.parseByte(this.peekString());
         return parsedByte >= min && parsedByte <= max;
      } catch (NumberFormatException var4) {
         return false;
      }
   }

   @SideEffectFree
   default boolean isValidByte(@NonNull final ByteRange range) {
      return this.isValidByte(range.minByte(), range.maxByte());
   }

   default byte readByte() {
      return Byte.parseByte(this.readString());
   }

   @SideEffectFree
   default boolean isValidShort(final short min, final short max) {
      try {
         short parsedShort = Short.parseShort(this.peekString());
         return parsedShort >= min && parsedShort <= max;
      } catch (NumberFormatException var4) {
         return false;
      }
   }

   @SideEffectFree
   default boolean isValidShort(@NonNull final ShortRange range) {
      return this.isValidShort(range.minShort(), range.maxShort());
   }

   default short readShort() {
      return Short.parseShort(this.readString());
   }

   @SideEffectFree
   default boolean isValidInteger(final int min, final int max) {
      try {
         int parsedInteger = Integer.parseInt(this.peekString());
         return parsedInteger >= min && parsedInteger <= max;
      } catch (NumberFormatException var4) {
         return false;
      }
   }

   @SideEffectFree
   default boolean isValidInteger(@NonNull final IntRange range) {
      return this.isValidInteger(range.minInt(), range.maxInt());
   }

   default int readInteger() {
      return Integer.parseInt(this.readString());
   }

   default int readInteger(final int radix) {
      return Integer.parseInt(this.readString(), radix);
   }

   @SideEffectFree
   default boolean isValidLong(final long min, final long max) {
      try {
         long parsedLong = Long.parseLong(this.peekString());
         return parsedLong >= min && parsedLong <= max;
      } catch (NumberFormatException var7) {
         return false;
      }
   }

   @SideEffectFree
   default boolean isValidLong(@NonNull final LongRange range) {
      return this.isValidLong(range.minLong(), range.maxLong());
   }

   default long readLong() {
      return Long.parseLong(this.readString());
   }

   @SideEffectFree
   default boolean isValidDouble(final double min, final double max) {
      try {
         double parsedDouble = Double.parseDouble(this.peekString());
         return parsedDouble >= min && parsedDouble <= max;
      } catch (NumberFormatException var7) {
         return false;
      }
   }

   @SideEffectFree
   default boolean isValidDouble(@NonNull final DoubleRange range) {
      return this.isValidDouble(range.minDouble(), range.maxDouble());
   }

   default double readDouble() {
      return Double.parseDouble(this.readString());
   }

   @SideEffectFree
   default boolean isValidFloat(final float min, final float max) {
      try {
         float parsedFloat = Float.parseFloat(this.peekString());
         return parsedFloat >= min && parsedFloat <= max;
      } catch (NumberFormatException var4) {
         return false;
      }
   }

   @SideEffectFree
   default boolean isValidFloat(@NonNull final FloatRange range) {
      return this.isValidFloat(range.minFloat(), range.maxFloat());
   }

   default float readFloat() {
      return Float.parseFloat(this.readString());
   }

   @SideEffectFree
   default boolean isValidBoolean(final boolean liberal) {
      return liberal ? BOOLEAN_LIBERAL.contains(this.peekString().toUpperCase(Locale.ROOT)) : BOOLEAN_STRICT.contains(this.peekString().toUpperCase(Locale.ROOT));
   }

   default boolean readBoolean() {
      return BOOLEAN_LIBERAL_TRUE.contains(this.readString().toUpperCase(Locale.ROOT));
   }

   @NonNull
   default String lastRemainingToken() {
      String remainingInput = this.remainingInput();
      if (!remainingInput.isEmpty() && !remainingInput.endsWith(" ")) {
         int lastSpace = remainingInput.lastIndexOf(32);
         return lastSpace == -1 ? remainingInput : remainingInput.substring(lastSpace + 1);
      } else {
         return "";
      }
   }

   default char lastRemainingCharacter() {
      String lastToken = this.lastRemainingToken();
      if (lastToken.isEmpty()) {
         throw new CommandInput.CursorOutOfBoundsException(this.cursor(), this.length());
      } else {
         return lastToken.charAt(lastToken.length() - 1);
      }
   }

   @NonNull
   CommandInput copy();

   @NonNull
   default String difference(@NonNull final CommandInput that, final boolean includeTrailingWhitespace) {
      if (!this.input().equals(that.input())) {
         return this.input();
      } else {
         String difference = this.input().substring(this.cursor(), that.cursor());
         return !includeTrailingWhitespace && difference.endsWith(" ") ? difference.substring(0, difference.length() - 1) : difference;
      }
   }

   @NonNull
   default String difference(@NonNull final CommandInput that) {
      return this.difference(that, false);
   }

   @API(
      status = Status.STABLE
   )
   public static class CursorOutOfBoundsException extends NoSuchElementException {
      CursorOutOfBoundsException(@NonNegative final int cursor, @NonNegative final int length) {
         super(String.format("Cursor exceeds input length (%d > %d)", cursor, length - 1));
      }
   }
}
