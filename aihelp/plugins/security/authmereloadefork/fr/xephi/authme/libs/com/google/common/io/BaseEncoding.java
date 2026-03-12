package fr.xephi.authme.libs.com.google.common.io;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Ascii;
import fr.xephi.authme.libs.com.google.common.base.Objects;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.math.IntMath;
import fr.xephi.authme.libs.com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.RoundingMode;
import java.util.Arrays;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   emulated = true
)
public abstract class BaseEncoding {
   private static final BaseEncoding BASE64 = new BaseEncoding.Base64Encoding("base64()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", '=');
   private static final BaseEncoding BASE64_URL = new BaseEncoding.Base64Encoding("base64Url()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_", '=');
   private static final BaseEncoding BASE32 = new BaseEncoding.StandardBaseEncoding("base32()", "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567", '=');
   private static final BaseEncoding BASE32_HEX = new BaseEncoding.StandardBaseEncoding("base32Hex()", "0123456789ABCDEFGHIJKLMNOPQRSTUV", '=');
   private static final BaseEncoding BASE16 = new BaseEncoding.Base16Encoding("base16()", "0123456789ABCDEF");

   BaseEncoding() {
   }

   public String encode(byte[] bytes) {
      return this.encode(bytes, 0, bytes.length);
   }

   public final String encode(byte[] bytes, int off, int len) {
      Preconditions.checkPositionIndexes(off, off + len, bytes.length);
      StringBuilder result = new StringBuilder(this.maxEncodedSize(len));

      try {
         this.encodeTo(result, bytes, off, len);
      } catch (IOException var6) {
         throw new AssertionError(var6);
      }

      return result.toString();
   }

   @GwtIncompatible
   public abstract OutputStream encodingStream(Writer var1);

   @GwtIncompatible
   public final ByteSink encodingSink(final CharSink encodedSink) {
      Preconditions.checkNotNull(encodedSink);
      return new ByteSink() {
         public OutputStream openStream() throws IOException {
            return BaseEncoding.this.encodingStream(encodedSink.openStream());
         }
      };
   }

   private static byte[] extract(byte[] result, int length) {
      if (length == result.length) {
         return result;
      } else {
         byte[] trunc = new byte[length];
         System.arraycopy(result, 0, trunc, 0, length);
         return trunc;
      }
   }

   public abstract boolean canDecode(CharSequence var1);

   public final byte[] decode(CharSequence chars) {
      try {
         return this.decodeChecked(chars);
      } catch (BaseEncoding.DecodingException var3) {
         throw new IllegalArgumentException(var3);
      }
   }

   final byte[] decodeChecked(CharSequence chars) throws BaseEncoding.DecodingException {
      chars = this.trimTrailingPadding(chars);
      byte[] tmp = new byte[this.maxDecodedSize(chars.length())];
      int len = this.decodeTo(tmp, chars);
      return extract(tmp, len);
   }

   @GwtIncompatible
   public abstract InputStream decodingStream(Reader var1);

   @GwtIncompatible
   public final ByteSource decodingSource(final CharSource encodedSource) {
      Preconditions.checkNotNull(encodedSource);
      return new ByteSource() {
         public InputStream openStream() throws IOException {
            return BaseEncoding.this.decodingStream(encodedSource.openStream());
         }
      };
   }

   abstract int maxEncodedSize(int var1);

   abstract void encodeTo(Appendable var1, byte[] var2, int var3, int var4) throws IOException;

   abstract int maxDecodedSize(int var1);

   abstract int decodeTo(byte[] var1, CharSequence var2) throws BaseEncoding.DecodingException;

   CharSequence trimTrailingPadding(CharSequence chars) {
      return (CharSequence)Preconditions.checkNotNull(chars);
   }

   public abstract BaseEncoding omitPadding();

   public abstract BaseEncoding withPadChar(char var1);

   public abstract BaseEncoding withSeparator(String var1, int var2);

   public abstract BaseEncoding upperCase();

   public abstract BaseEncoding lowerCase();

   public static BaseEncoding base64() {
      return BASE64;
   }

   public static BaseEncoding base64Url() {
      return BASE64_URL;
   }

   public static BaseEncoding base32() {
      return BASE32;
   }

   public static BaseEncoding base32Hex() {
      return BASE32_HEX;
   }

   public static BaseEncoding base16() {
      return BASE16;
   }

   @GwtIncompatible
   static Reader ignoringReader(final Reader delegate, final String toIgnore) {
      Preconditions.checkNotNull(delegate);
      Preconditions.checkNotNull(toIgnore);
      return new Reader() {
         public int read() throws IOException {
            int readChar;
            do {
               readChar = delegate.read();
            } while(readChar != -1 && toIgnore.indexOf((char)readChar) >= 0);

            return readChar;
         }

         public int read(char[] cbuf, int off, int len) throws IOException {
            throw new UnsupportedOperationException();
         }

         public void close() throws IOException {
            delegate.close();
         }
      };
   }

   static Appendable separatingAppendable(final Appendable delegate, final String separator, final int afterEveryChars) {
      Preconditions.checkNotNull(delegate);
      Preconditions.checkNotNull(separator);
      Preconditions.checkArgument(afterEveryChars > 0);
      return new Appendable() {
         int charsUntilSeparator = afterEveryChars;

         public Appendable append(char c) throws IOException {
            if (this.charsUntilSeparator == 0) {
               delegate.append(separator);
               this.charsUntilSeparator = afterEveryChars;
            }

            delegate.append(c);
            --this.charsUntilSeparator;
            return this;
         }

         public Appendable append(@CheckForNull CharSequence chars, int off, int len) {
            throw new UnsupportedOperationException();
         }

         public Appendable append(@CheckForNull CharSequence chars) {
            throw new UnsupportedOperationException();
         }
      };
   }

   @GwtIncompatible
   static Writer separatingWriter(final Writer delegate, String separator, int afterEveryChars) {
      final Appendable separatingAppendable = separatingAppendable(delegate, separator, afterEveryChars);
      return new Writer() {
         public void write(int c) throws IOException {
            separatingAppendable.append((char)c);
         }

         public void write(char[] chars, int off, int len) throws IOException {
            throw new UnsupportedOperationException();
         }

         public void flush() throws IOException {
            delegate.flush();
         }

         public void close() throws IOException {
            delegate.close();
         }
      };
   }

   static final class SeparatedBaseEncoding extends BaseEncoding {
      private final BaseEncoding delegate;
      private final String separator;
      private final int afterEveryChars;

      SeparatedBaseEncoding(BaseEncoding delegate, String separator, int afterEveryChars) {
         this.delegate = (BaseEncoding)Preconditions.checkNotNull(delegate);
         this.separator = (String)Preconditions.checkNotNull(separator);
         this.afterEveryChars = afterEveryChars;
         Preconditions.checkArgument(afterEveryChars > 0, "Cannot add a separator after every %s chars", afterEveryChars);
      }

      CharSequence trimTrailingPadding(CharSequence chars) {
         return this.delegate.trimTrailingPadding(chars);
      }

      int maxEncodedSize(int bytes) {
         int unseparatedSize = this.delegate.maxEncodedSize(bytes);
         return unseparatedSize + this.separator.length() * IntMath.divide(Math.max(0, unseparatedSize - 1), this.afterEveryChars, RoundingMode.FLOOR);
      }

      @GwtIncompatible
      public OutputStream encodingStream(Writer output) {
         return this.delegate.encodingStream(separatingWriter(output, this.separator, this.afterEveryChars));
      }

      void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
         this.delegate.encodeTo(separatingAppendable(target, this.separator, this.afterEveryChars), bytes, off, len);
      }

      int maxDecodedSize(int chars) {
         return this.delegate.maxDecodedSize(chars);
      }

      public boolean canDecode(CharSequence chars) {
         StringBuilder builder = new StringBuilder();

         for(int i = 0; i < chars.length(); ++i) {
            char c = chars.charAt(i);
            if (this.separator.indexOf(c) < 0) {
               builder.append(c);
            }
         }

         return this.delegate.canDecode(builder);
      }

      int decodeTo(byte[] target, CharSequence chars) throws BaseEncoding.DecodingException {
         StringBuilder stripped = new StringBuilder(chars.length());

         for(int i = 0; i < chars.length(); ++i) {
            char c = chars.charAt(i);
            if (this.separator.indexOf(c) < 0) {
               stripped.append(c);
            }
         }

         return this.delegate.decodeTo(target, stripped);
      }

      @GwtIncompatible
      public InputStream decodingStream(Reader reader) {
         return this.delegate.decodingStream(ignoringReader(reader, this.separator));
      }

      public BaseEncoding omitPadding() {
         return this.delegate.omitPadding().withSeparator(this.separator, this.afterEveryChars);
      }

      public BaseEncoding withPadChar(char padChar) {
         return this.delegate.withPadChar(padChar).withSeparator(this.separator, this.afterEveryChars);
      }

      public BaseEncoding withSeparator(String separator, int afterEveryChars) {
         throw new UnsupportedOperationException("Already have a separator");
      }

      public BaseEncoding upperCase() {
         return this.delegate.upperCase().withSeparator(this.separator, this.afterEveryChars);
      }

      public BaseEncoding lowerCase() {
         return this.delegate.lowerCase().withSeparator(this.separator, this.afterEveryChars);
      }

      public String toString() {
         String var1 = String.valueOf(this.delegate);
         String var2 = this.separator;
         int var3 = this.afterEveryChars;
         return (new StringBuilder(31 + String.valueOf(var1).length() + String.valueOf(var2).length())).append(var1).append(".withSeparator(\"").append(var2).append("\", ").append(var3).append(")").toString();
      }
   }

   static final class Base64Encoding extends BaseEncoding.StandardBaseEncoding {
      Base64Encoding(String name, String alphabetChars, @CheckForNull Character paddingChar) {
         this(new BaseEncoding.Alphabet(name, alphabetChars.toCharArray()), paddingChar);
      }

      private Base64Encoding(BaseEncoding.Alphabet alphabet, @CheckForNull Character paddingChar) {
         super(alphabet, paddingChar);
         Preconditions.checkArgument(alphabet.chars.length == 64);
      }

      void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
         Preconditions.checkNotNull(target);
         Preconditions.checkPositionIndexes(off, off + len, bytes.length);
         int i = off;

         for(int remaining = len; remaining >= 3; remaining -= 3) {
            int chunk = (bytes[i++] & 255) << 16 | (bytes[i++] & 255) << 8 | bytes[i++] & 255;
            target.append(this.alphabet.encode(chunk >>> 18));
            target.append(this.alphabet.encode(chunk >>> 12 & 63));
            target.append(this.alphabet.encode(chunk >>> 6 & 63));
            target.append(this.alphabet.encode(chunk & 63));
         }

         if (i < off + len) {
            this.encodeChunkTo(target, bytes, i, off + len - i);
         }

      }

      int decodeTo(byte[] target, CharSequence chars) throws BaseEncoding.DecodingException {
         Preconditions.checkNotNull(target);
         chars = this.trimTrailingPadding(chars);
         int bytesWritten;
         if (!this.alphabet.isValidPaddingStartPosition(chars.length())) {
            bytesWritten = chars.length();
            throw new BaseEncoding.DecodingException((new StringBuilder(32)).append("Invalid input length ").append(bytesWritten).toString());
         } else {
            bytesWritten = 0;
            int i = 0;

            while(i < chars.length()) {
               int chunk = this.alphabet.decode(chars.charAt(i++)) << 18;
               chunk |= this.alphabet.decode(chars.charAt(i++)) << 12;
               target[bytesWritten++] = (byte)(chunk >>> 16);
               if (i < chars.length()) {
                  chunk |= this.alphabet.decode(chars.charAt(i++)) << 6;
                  target[bytesWritten++] = (byte)(chunk >>> 8 & 255);
                  if (i < chars.length()) {
                     chunk |= this.alphabet.decode(chars.charAt(i++));
                     target[bytesWritten++] = (byte)(chunk & 255);
                  }
               }
            }

            return bytesWritten;
         }
      }

      BaseEncoding newInstance(BaseEncoding.Alphabet alphabet, @CheckForNull Character paddingChar) {
         return new BaseEncoding.Base64Encoding(alphabet, paddingChar);
      }
   }

   static final class Base16Encoding extends BaseEncoding.StandardBaseEncoding {
      final char[] encoding;

      Base16Encoding(String name, String alphabetChars) {
         this(new BaseEncoding.Alphabet(name, alphabetChars.toCharArray()));
      }

      private Base16Encoding(BaseEncoding.Alphabet alphabet) {
         super(alphabet, (Character)null);
         this.encoding = new char[512];
         Preconditions.checkArgument(alphabet.chars.length == 16);

         for(int i = 0; i < 256; ++i) {
            this.encoding[i] = alphabet.encode(i >>> 4);
            this.encoding[i | 256] = alphabet.encode(i & 15);
         }

      }

      void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
         Preconditions.checkNotNull(target);
         Preconditions.checkPositionIndexes(off, off + len, bytes.length);

         for(int i = 0; i < len; ++i) {
            int b = bytes[off + i] & 255;
            target.append(this.encoding[b]);
            target.append(this.encoding[b | 256]);
         }

      }

      int decodeTo(byte[] target, CharSequence chars) throws BaseEncoding.DecodingException {
         Preconditions.checkNotNull(target);
         int bytesWritten;
         if (chars.length() % 2 == 1) {
            bytesWritten = chars.length();
            throw new BaseEncoding.DecodingException((new StringBuilder(32)).append("Invalid input length ").append(bytesWritten).toString());
         } else {
            bytesWritten = 0;

            for(int i = 0; i < chars.length(); i += 2) {
               int decoded = this.alphabet.decode(chars.charAt(i)) << 4 | this.alphabet.decode(chars.charAt(i + 1));
               target[bytesWritten++] = (byte)decoded;
            }

            return bytesWritten;
         }
      }

      BaseEncoding newInstance(BaseEncoding.Alphabet alphabet, @CheckForNull Character paddingChar) {
         return new BaseEncoding.Base16Encoding(alphabet);
      }
   }

   static class StandardBaseEncoding extends BaseEncoding {
      final BaseEncoding.Alphabet alphabet;
      @CheckForNull
      final Character paddingChar;
      @LazyInit
      @CheckForNull
      private transient BaseEncoding upperCase;
      @LazyInit
      @CheckForNull
      private transient BaseEncoding lowerCase;

      StandardBaseEncoding(String name, String alphabetChars, @CheckForNull Character paddingChar) {
         this(new BaseEncoding.Alphabet(name, alphabetChars.toCharArray()), paddingChar);
      }

      StandardBaseEncoding(BaseEncoding.Alphabet alphabet, @CheckForNull Character paddingChar) {
         this.alphabet = (BaseEncoding.Alphabet)Preconditions.checkNotNull(alphabet);
         Preconditions.checkArgument(paddingChar == null || !alphabet.matches(paddingChar), "Padding character %s was already in alphabet", (Object)paddingChar);
         this.paddingChar = paddingChar;
      }

      int maxEncodedSize(int bytes) {
         return this.alphabet.charsPerChunk * IntMath.divide(bytes, this.alphabet.bytesPerChunk, RoundingMode.CEILING);
      }

      @GwtIncompatible
      public OutputStream encodingStream(final Writer out) {
         Preconditions.checkNotNull(out);
         return new OutputStream() {
            int bitBuffer = 0;
            int bitBufferLength = 0;
            int writtenChars = 0;

            public void write(int b) throws IOException {
               this.bitBuffer <<= 8;
               this.bitBuffer |= b & 255;

               for(this.bitBufferLength += 8; this.bitBufferLength >= StandardBaseEncoding.this.alphabet.bitsPerChar; this.bitBufferLength -= StandardBaseEncoding.this.alphabet.bitsPerChar) {
                  int charIndex = this.bitBuffer >> this.bitBufferLength - StandardBaseEncoding.this.alphabet.bitsPerChar & StandardBaseEncoding.this.alphabet.mask;
                  out.write(StandardBaseEncoding.this.alphabet.encode(charIndex));
                  ++this.writtenChars;
               }

            }

            public void flush() throws IOException {
               out.flush();
            }

            public void close() throws IOException {
               if (this.bitBufferLength > 0) {
                  int charIndex = this.bitBuffer << StandardBaseEncoding.this.alphabet.bitsPerChar - this.bitBufferLength & StandardBaseEncoding.this.alphabet.mask;
                  out.write(StandardBaseEncoding.this.alphabet.encode(charIndex));
                  ++this.writtenChars;
                  if (StandardBaseEncoding.this.paddingChar != null) {
                     while(this.writtenChars % StandardBaseEncoding.this.alphabet.charsPerChunk != 0) {
                        out.write(StandardBaseEncoding.this.paddingChar);
                        ++this.writtenChars;
                     }
                  }
               }

               out.close();
            }
         };
      }

      void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
         Preconditions.checkNotNull(target);
         Preconditions.checkPositionIndexes(off, off + len, bytes.length);

         for(int i = 0; i < len; i += this.alphabet.bytesPerChunk) {
            this.encodeChunkTo(target, bytes, off + i, Math.min(this.alphabet.bytesPerChunk, len - i));
         }

      }

      void encodeChunkTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
         Preconditions.checkNotNull(target);
         Preconditions.checkPositionIndexes(off, off + len, bytes.length);
         Preconditions.checkArgument(len <= this.alphabet.bytesPerChunk);
         long bitBuffer = 0L;

         int bitOffset;
         for(bitOffset = 0; bitOffset < len; ++bitOffset) {
            bitBuffer |= (long)(bytes[off + bitOffset] & 255);
            bitBuffer <<= 8;
         }

         bitOffset = (len + 1) * 8 - this.alphabet.bitsPerChar;

         int bitsProcessed;
         for(bitsProcessed = 0; bitsProcessed < len * 8; bitsProcessed += this.alphabet.bitsPerChar) {
            int charIndex = (int)(bitBuffer >>> bitOffset - bitsProcessed) & this.alphabet.mask;
            target.append(this.alphabet.encode(charIndex));
         }

         if (this.paddingChar != null) {
            while(bitsProcessed < this.alphabet.bytesPerChunk * 8) {
               target.append(this.paddingChar);
               bitsProcessed += this.alphabet.bitsPerChar;
            }
         }

      }

      int maxDecodedSize(int chars) {
         return (int)(((long)this.alphabet.bitsPerChar * (long)chars + 7L) / 8L);
      }

      CharSequence trimTrailingPadding(CharSequence chars) {
         Preconditions.checkNotNull(chars);
         if (this.paddingChar == null) {
            return chars;
         } else {
            char padChar = this.paddingChar;

            int l;
            for(l = chars.length() - 1; l >= 0 && chars.charAt(l) == padChar; --l) {
            }

            return chars.subSequence(0, l + 1);
         }
      }

      public boolean canDecode(CharSequence chars) {
         Preconditions.checkNotNull(chars);
         chars = this.trimTrailingPadding(chars);
         if (!this.alphabet.isValidPaddingStartPosition(chars.length())) {
            return false;
         } else {
            for(int i = 0; i < chars.length(); ++i) {
               if (!this.alphabet.canDecode(chars.charAt(i))) {
                  return false;
               }
            }

            return true;
         }
      }

      int decodeTo(byte[] target, CharSequence chars) throws BaseEncoding.DecodingException {
         Preconditions.checkNotNull(target);
         chars = this.trimTrailingPadding(chars);
         int bytesWritten;
         if (!this.alphabet.isValidPaddingStartPosition(chars.length())) {
            bytesWritten = chars.length();
            throw new BaseEncoding.DecodingException((new StringBuilder(32)).append("Invalid input length ").append(bytesWritten).toString());
         } else {
            bytesWritten = 0;

            for(int charIdx = 0; charIdx < chars.length(); charIdx += this.alphabet.charsPerChunk) {
               long chunk = 0L;
               int charsProcessed = 0;

               int minOffset;
               for(minOffset = 0; minOffset < this.alphabet.charsPerChunk; ++minOffset) {
                  chunk <<= this.alphabet.bitsPerChar;
                  if (charIdx + minOffset < chars.length()) {
                     chunk |= (long)this.alphabet.decode(chars.charAt(charIdx + charsProcessed++));
                  }
               }

               minOffset = this.alphabet.bytesPerChunk * 8 - charsProcessed * this.alphabet.bitsPerChar;

               for(int offset = (this.alphabet.bytesPerChunk - 1) * 8; offset >= minOffset; offset -= 8) {
                  target[bytesWritten++] = (byte)((int)(chunk >>> offset & 255L));
               }
            }

            return bytesWritten;
         }
      }

      @GwtIncompatible
      public InputStream decodingStream(final Reader reader) {
         Preconditions.checkNotNull(reader);
         return new InputStream() {
            int bitBuffer = 0;
            int bitBufferLength = 0;
            int readChars = 0;
            boolean hitPadding = false;

            public int read() throws IOException {
               while(true) {
                  int readChar = reader.read();
                  if (readChar == -1) {
                     if (!this.hitPadding && !StandardBaseEncoding.this.alphabet.isValidPaddingStartPosition(this.readChars)) {
                        int var4 = this.readChars;
                        throw new BaseEncoding.DecodingException((new StringBuilder(32)).append("Invalid input length ").append(var4).toString());
                     }

                     return -1;
                  }

                  ++this.readChars;
                  char ch = (char)readChar;
                  int var3;
                  if (StandardBaseEncoding.this.paddingChar != null && StandardBaseEncoding.this.paddingChar == ch) {
                     if (!this.hitPadding && (this.readChars == 1 || !StandardBaseEncoding.this.alphabet.isValidPaddingStartPosition(this.readChars - 1))) {
                        var3 = this.readChars;
                        throw new BaseEncoding.DecodingException((new StringBuilder(41)).append("Padding cannot start at index ").append(var3).toString());
                     }

                     this.hitPadding = true;
                  } else {
                     if (this.hitPadding) {
                        var3 = this.readChars;
                        throw new BaseEncoding.DecodingException((new StringBuilder(61)).append("Expected padding character but found '").append(ch).append("' at index ").append(var3).toString());
                     }

                     this.bitBuffer <<= StandardBaseEncoding.this.alphabet.bitsPerChar;
                     this.bitBuffer |= StandardBaseEncoding.this.alphabet.decode(ch);
                     this.bitBufferLength += StandardBaseEncoding.this.alphabet.bitsPerChar;
                     if (this.bitBufferLength >= 8) {
                        this.bitBufferLength -= 8;
                        return this.bitBuffer >> this.bitBufferLength & 255;
                     }
                  }
               }
            }

            public int read(byte[] buf, int off, int len) throws IOException {
               Preconditions.checkPositionIndexes(off, off + len, buf.length);

               int i;
               for(i = off; i < off + len; ++i) {
                  int b = this.read();
                  if (b == -1) {
                     int read = i - off;
                     return read == 0 ? -1 : read;
                  }

                  buf[i] = (byte)b;
               }

               return i - off;
            }

            public void close() throws IOException {
               reader.close();
            }
         };
      }

      public BaseEncoding omitPadding() {
         return (BaseEncoding)(this.paddingChar == null ? this : this.newInstance(this.alphabet, (Character)null));
      }

      public BaseEncoding withPadChar(char padChar) {
         return (BaseEncoding)(8 % this.alphabet.bitsPerChar != 0 && (this.paddingChar == null || this.paddingChar != padChar) ? this.newInstance(this.alphabet, padChar) : this);
      }

      public BaseEncoding withSeparator(String separator, int afterEveryChars) {
         for(int i = 0; i < separator.length(); ++i) {
            Preconditions.checkArgument(!this.alphabet.matches(separator.charAt(i)), "Separator (%s) cannot contain alphabet characters", (Object)separator);
         }

         if (this.paddingChar != null) {
            Preconditions.checkArgument(separator.indexOf(this.paddingChar) < 0, "Separator (%s) cannot contain padding character", (Object)separator);
         }

         return new BaseEncoding.SeparatedBaseEncoding(this, separator, afterEveryChars);
      }

      public BaseEncoding upperCase() {
         BaseEncoding result = this.upperCase;
         if (result == null) {
            BaseEncoding.Alphabet upper = this.alphabet.upperCase();
            result = this.upperCase = (BaseEncoding)(upper == this.alphabet ? this : this.newInstance(upper, this.paddingChar));
         }

         return result;
      }

      public BaseEncoding lowerCase() {
         BaseEncoding result = this.lowerCase;
         if (result == null) {
            BaseEncoding.Alphabet lower = this.alphabet.lowerCase();
            result = this.lowerCase = (BaseEncoding)(lower == this.alphabet ? this : this.newInstance(lower, this.paddingChar));
         }

         return result;
      }

      BaseEncoding newInstance(BaseEncoding.Alphabet alphabet, @CheckForNull Character paddingChar) {
         return new BaseEncoding.StandardBaseEncoding(alphabet, paddingChar);
      }

      public String toString() {
         StringBuilder builder = new StringBuilder("BaseEncoding.");
         builder.append(this.alphabet.toString());
         if (8 % this.alphabet.bitsPerChar != 0) {
            if (this.paddingChar == null) {
               builder.append(".omitPadding()");
            } else {
               builder.append(".withPadChar('").append(this.paddingChar).append("')");
            }
         }

         return builder.toString();
      }

      public boolean equals(@CheckForNull Object other) {
         if (!(other instanceof BaseEncoding.StandardBaseEncoding)) {
            return false;
         } else {
            BaseEncoding.StandardBaseEncoding that = (BaseEncoding.StandardBaseEncoding)other;
            return this.alphabet.equals(that.alphabet) && Objects.equal(this.paddingChar, that.paddingChar);
         }
      }

      public int hashCode() {
         return this.alphabet.hashCode() ^ Objects.hashCode(this.paddingChar);
      }
   }

   private static final class Alphabet {
      private final String name;
      private final char[] chars;
      final int mask;
      final int bitsPerChar;
      final int charsPerChunk;
      final int bytesPerChunk;
      private final byte[] decodabet;
      private final boolean[] validPadding;

      Alphabet(String name, char[] chars) {
         this.name = (String)Preconditions.checkNotNull(name);
         this.chars = (char[])Preconditions.checkNotNull(chars);

         try {
            this.bitsPerChar = IntMath.log2(chars.length, RoundingMode.UNNECESSARY);
         } catch (ArithmeticException var7) {
            int var4 = chars.length;
            throw new IllegalArgumentException((new StringBuilder(35)).append("Illegal alphabet length ").append(var4).toString(), var7);
         }

         int gcd = Math.min(8, Integer.lowestOneBit(this.bitsPerChar));

         try {
            this.charsPerChunk = 8 / gcd;
            this.bytesPerChunk = this.bitsPerChar / gcd;
         } catch (ArithmeticException var8) {
            IllegalArgumentException var10000 = new IllegalArgumentException;
            String var10003 = String.valueOf(new String(chars));
            String var10002;
            if (var10003.length() != 0) {
               var10002 = "Illegal alphabet ".concat(var10003);
            } else {
               String var10004 = new String;
               var10002 = var10004;
               var10004.<init>("Illegal alphabet ");
            }

            var10000.<init>(var10002, var8);
            throw var10000;
         }

         this.mask = chars.length - 1;
         byte[] decodabet = new byte[128];
         Arrays.fill(decodabet, (byte)-1);

         for(int i = 0; i < chars.length; ++i) {
            char c = chars[i];
            Preconditions.checkArgument(c < decodabet.length, "Non-ASCII character: %s", c);
            Preconditions.checkArgument(decodabet[c] == -1, "Duplicate character: %s", c);
            decodabet[c] = (byte)i;
         }

         this.decodabet = decodabet;
         boolean[] validPadding = new boolean[this.charsPerChunk];

         for(int i = 0; i < this.bytesPerChunk; ++i) {
            validPadding[IntMath.divide(i * 8, this.bitsPerChar, RoundingMode.CEILING)] = true;
         }

         this.validPadding = validPadding;
      }

      char encode(int bits) {
         return this.chars[bits];
      }

      boolean isValidPaddingStartPosition(int index) {
         return this.validPadding[index % this.charsPerChunk];
      }

      boolean canDecode(char ch) {
         return ch <= 127 && this.decodabet[ch] != -1;
      }

      int decode(char ch) throws BaseEncoding.DecodingException {
         BaseEncoding.DecodingException var10000;
         String var10002;
         String var10003;
         String var10004;
         if (ch > 127) {
            var10000 = new BaseEncoding.DecodingException;
            var10003 = String.valueOf(Integer.toHexString(ch));
            if (var10003.length() != 0) {
               var10002 = "Unrecognized character: 0x".concat(var10003);
            } else {
               var10004 = new String;
               var10002 = var10004;
               var10004.<init>("Unrecognized character: 0x");
            }

            var10000.<init>(var10002);
            throw var10000;
         } else {
            int result = this.decodabet[ch];
            if (result == -1) {
               if (ch > ' ' && ch != 127) {
                  throw new BaseEncoding.DecodingException((new StringBuilder(25)).append("Unrecognized character: ").append(ch).toString());
               } else {
                  var10000 = new BaseEncoding.DecodingException;
                  var10003 = String.valueOf(Integer.toHexString(ch));
                  if (var10003.length() != 0) {
                     var10002 = "Unrecognized character: 0x".concat(var10003);
                  } else {
                     var10004 = new String;
                     var10002 = var10004;
                     var10004.<init>("Unrecognized character: 0x");
                  }

                  var10000.<init>(var10002);
                  throw var10000;
               }
            } else {
               return result;
            }
         }
      }

      private boolean hasLowerCase() {
         char[] var1 = this.chars;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            char c = var1[var3];
            if (Ascii.isLowerCase(c)) {
               return true;
            }
         }

         return false;
      }

      private boolean hasUpperCase() {
         char[] var1 = this.chars;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            char c = var1[var3];
            if (Ascii.isUpperCase(c)) {
               return true;
            }
         }

         return false;
      }

      BaseEncoding.Alphabet upperCase() {
         if (!this.hasLowerCase()) {
            return this;
         } else {
            Preconditions.checkState(!this.hasUpperCase(), "Cannot call upperCase() on a mixed-case alphabet");
            char[] upperCased = new char[this.chars.length];

            for(int i = 0; i < this.chars.length; ++i) {
               upperCased[i] = Ascii.toUpperCase(this.chars[i]);
            }

            return new BaseEncoding.Alphabet(String.valueOf(this.name).concat(".upperCase()"), upperCased);
         }
      }

      BaseEncoding.Alphabet lowerCase() {
         if (!this.hasUpperCase()) {
            return this;
         } else {
            Preconditions.checkState(!this.hasLowerCase(), "Cannot call lowerCase() on a mixed-case alphabet");
            char[] lowerCased = new char[this.chars.length];

            for(int i = 0; i < this.chars.length; ++i) {
               lowerCased[i] = Ascii.toLowerCase(this.chars[i]);
            }

            return new BaseEncoding.Alphabet(String.valueOf(this.name).concat(".lowerCase()"), lowerCased);
         }
      }

      public boolean matches(char c) {
         return c < this.decodabet.length && this.decodabet[c] != -1;
      }

      public String toString() {
         return this.name;
      }

      public boolean equals(@CheckForNull Object other) {
         if (other instanceof BaseEncoding.Alphabet) {
            BaseEncoding.Alphabet that = (BaseEncoding.Alphabet)other;
            return Arrays.equals(this.chars, that.chars);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Arrays.hashCode(this.chars);
      }
   }

   public static final class DecodingException extends IOException {
      DecodingException(String message) {
         super(message);
      }

      DecodingException(Throwable cause) {
         super(cause);
      }
   }
}
