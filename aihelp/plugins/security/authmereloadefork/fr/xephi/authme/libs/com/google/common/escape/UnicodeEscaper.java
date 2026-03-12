package fr.xephi.authme.libs.com.google.common.escape;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class UnicodeEscaper extends Escaper {
   private static final int DEST_PAD = 32;

   protected UnicodeEscaper() {
   }

   @CheckForNull
   protected abstract char[] escape(int var1);

   public String escape(String string) {
      Preconditions.checkNotNull(string);
      int end = string.length();
      int index = this.nextEscapeIndex(string, 0, end);
      return index == end ? string : this.escapeSlow(string, index);
   }

   protected int nextEscapeIndex(CharSequence csq, int start, int end) {
      int index;
      int cp;
      for(index = start; index < end; index += Character.isSupplementaryCodePoint(cp) ? 2 : 1) {
         cp = codePointAt(csq, index, end);
         if (cp < 0 || this.escape(cp) != null) {
            break;
         }
      }

      return index;
   }

   protected final String escapeSlow(String s, int index) {
      int end = s.length();
      char[] dest = Platform.charBufferFromThreadLocal();
      int destIndex = 0;

      int unescapedChunkStart;
      int charsSkipped;
      int nextIndex;
      for(unescapedChunkStart = 0; index < end; index = this.nextEscapeIndex(s, nextIndex, end)) {
         charsSkipped = codePointAt(s, index, end);
         if (charsSkipped < 0) {
            throw new IllegalArgumentException("Trailing high surrogate at end of input");
         }

         char[] escaped = this.escape(charsSkipped);
         nextIndex = index + (Character.isSupplementaryCodePoint(charsSkipped) ? 2 : 1);
         if (escaped != null) {
            int charsSkipped = index - unescapedChunkStart;
            int sizeNeeded = destIndex + charsSkipped + escaped.length;
            if (dest.length < sizeNeeded) {
               int destLength = sizeNeeded + (end - index) + 32;
               dest = growBuffer(dest, destIndex, destLength);
            }

            if (charsSkipped > 0) {
               s.getChars(unescapedChunkStart, index, dest, destIndex);
               destIndex += charsSkipped;
            }

            if (escaped.length > 0) {
               System.arraycopy(escaped, 0, dest, destIndex, escaped.length);
               destIndex += escaped.length;
            }

            unescapedChunkStart = nextIndex;
         }
      }

      charsSkipped = end - unescapedChunkStart;
      if (charsSkipped > 0) {
         int endIndex = destIndex + charsSkipped;
         if (dest.length < endIndex) {
            dest = growBuffer(dest, destIndex, endIndex);
         }

         s.getChars(unescapedChunkStart, end, dest, destIndex);
         destIndex = endIndex;
      }

      return new String(dest, 0, destIndex);
   }

   protected static int codePointAt(CharSequence seq, int index, int end) {
      Preconditions.checkNotNull(seq);
      if (index < end) {
         char c1 = seq.charAt(index++);
         if (c1 >= '\ud800' && c1 <= '\udfff') {
            if (c1 <= '\udbff') {
               if (index == end) {
                  return -c1;
               } else {
                  char c2 = seq.charAt(index);
                  if (Character.isLowSurrogate(c2)) {
                     return Character.toCodePoint(c1, c2);
                  } else {
                     String var7 = String.valueOf(seq);
                     throw new IllegalArgumentException((new StringBuilder(89 + String.valueOf(var7).length())).append("Expected low surrogate but got char '").append(c2).append("' with value ").append(c2).append(" at index ").append(index).append(" in '").append(var7).append("'").toString());
                  }
               }
            } else {
               int var5 = index - 1;
               String var6 = String.valueOf(seq);
               throw new IllegalArgumentException((new StringBuilder(88 + String.valueOf(var6).length())).append("Unexpected low surrogate character '").append(c1).append("' with value ").append(c1).append(" at index ").append(var5).append(" in '").append(var6).append("'").toString());
            }
         } else {
            return c1;
         }
      } else {
         throw new IndexOutOfBoundsException("Index exceeds specified range");
      }
   }

   private static char[] growBuffer(char[] dest, int index, int size) {
      if (size < 0) {
         throw new AssertionError("Cannot increase internal buffer any further");
      } else {
         char[] copy = new char[size];
         if (index > 0) {
            System.arraycopy(dest, 0, copy, 0, index);
         }

         return copy;
      }
   }
}
