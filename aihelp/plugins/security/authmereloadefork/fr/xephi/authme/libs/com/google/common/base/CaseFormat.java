package fr.xephi.authme.libs.com.google.common.base;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public enum CaseFormat {
   LOWER_HYPHEN(CharMatcher.is('-'), "-") {
      String normalizeWord(String word) {
         return Ascii.toLowerCase(word);
      }

      String convert(CaseFormat format, String s) {
         if (format == LOWER_UNDERSCORE) {
            return s.replace('-', '_');
         } else {
            return format == UPPER_UNDERSCORE ? Ascii.toUpperCase(s.replace('-', '_')) : super.convert(format, s);
         }
      }
   },
   LOWER_UNDERSCORE(CharMatcher.is('_'), "_") {
      String normalizeWord(String word) {
         return Ascii.toLowerCase(word);
      }

      String convert(CaseFormat format, String s) {
         if (format == LOWER_HYPHEN) {
            return s.replace('_', '-');
         } else {
            return format == UPPER_UNDERSCORE ? Ascii.toUpperCase(s) : super.convert(format, s);
         }
      }
   },
   LOWER_CAMEL(CharMatcher.inRange('A', 'Z'), "") {
      String normalizeWord(String word) {
         return CaseFormat.firstCharOnlyToUpper(word);
      }

      String normalizeFirstWord(String word) {
         return Ascii.toLowerCase(word);
      }
   },
   UPPER_CAMEL(CharMatcher.inRange('A', 'Z'), "") {
      String normalizeWord(String word) {
         return CaseFormat.firstCharOnlyToUpper(word);
      }
   },
   UPPER_UNDERSCORE(CharMatcher.is('_'), "_") {
      String normalizeWord(String word) {
         return Ascii.toUpperCase(word);
      }

      String convert(CaseFormat format, String s) {
         if (format == LOWER_HYPHEN) {
            return Ascii.toLowerCase(s.replace('_', '-'));
         } else {
            return format == LOWER_UNDERSCORE ? Ascii.toLowerCase(s) : super.convert(format, s);
         }
      }
   };

   private final CharMatcher wordBoundary;
   private final String wordSeparator;

   private CaseFormat(CharMatcher wordBoundary, String wordSeparator) {
      this.wordBoundary = wordBoundary;
      this.wordSeparator = wordSeparator;
   }

   public final String to(CaseFormat format, String str) {
      Preconditions.checkNotNull(format);
      Preconditions.checkNotNull(str);
      return format == this ? str : this.convert(format, str);
   }

   String convert(CaseFormat format, String s) {
      StringBuilder out = null;
      int i = 0;
      int j = -1;

      while(true) {
         ++j;
         if ((j = this.wordBoundary.indexIn(s, j)) == -1) {
            return i == 0 ? format.normalizeFirstWord(s) : ((StringBuilder)java.util.Objects.requireNonNull(out)).append(format.normalizeWord(s.substring(i))).toString();
         }

         if (i == 0) {
            out = new StringBuilder(s.length() + 4 * format.wordSeparator.length());
            out.append(format.normalizeFirstWord(s.substring(i, j)));
         } else {
            ((StringBuilder)java.util.Objects.requireNonNull(out)).append(format.normalizeWord(s.substring(i, j)));
         }

         out.append(format.wordSeparator);
         i = j + this.wordSeparator.length();
      }
   }

   public Converter<String, String> converterTo(CaseFormat targetFormat) {
      return new CaseFormat.StringConverter(this, targetFormat);
   }

   abstract String normalizeWord(String var1);

   String normalizeFirstWord(String word) {
      return this.normalizeWord(word);
   }

   private static String firstCharOnlyToUpper(String word) {
      String var10000;
      if (word.isEmpty()) {
         var10000 = word;
      } else {
         char var1 = Ascii.toUpperCase(word.charAt(0));
         String var2 = Ascii.toLowerCase(word.substring(1));
         var10000 = (new StringBuilder(1 + String.valueOf(var2).length())).append(var1).append(var2).toString();
      }

      return var10000;
   }

   // $FF: synthetic method
   private static CaseFormat[] $values() {
      return new CaseFormat[]{LOWER_HYPHEN, LOWER_UNDERSCORE, LOWER_CAMEL, UPPER_CAMEL, UPPER_UNDERSCORE};
   }

   // $FF: synthetic method
   CaseFormat(CharMatcher x2, String x3, Object x4) {
      this(x2, x3);
   }

   private static final class StringConverter extends Converter<String, String> implements Serializable {
      private final CaseFormat sourceFormat;
      private final CaseFormat targetFormat;
      private static final long serialVersionUID = 0L;

      StringConverter(CaseFormat sourceFormat, CaseFormat targetFormat) {
         this.sourceFormat = (CaseFormat)Preconditions.checkNotNull(sourceFormat);
         this.targetFormat = (CaseFormat)Preconditions.checkNotNull(targetFormat);
      }

      protected String doForward(String s) {
         return this.sourceFormat.to(this.targetFormat, s);
      }

      protected String doBackward(String s) {
         return this.targetFormat.to(this.sourceFormat, s);
      }

      public boolean equals(@CheckForNull Object object) {
         if (!(object instanceof CaseFormat.StringConverter)) {
            return false;
         } else {
            CaseFormat.StringConverter that = (CaseFormat.StringConverter)object;
            return this.sourceFormat.equals(that.sourceFormat) && this.targetFormat.equals(that.targetFormat);
         }
      }

      public int hashCode() {
         return this.sourceFormat.hashCode() ^ this.targetFormat.hashCode();
      }

      public String toString() {
         String var1 = String.valueOf(this.sourceFormat);
         String var2 = String.valueOf(this.targetFormat);
         return (new StringBuilder(14 + String.valueOf(var1).length() + String.valueOf(var2).length())).append(var1).append(".converterTo(").append(var2).append(")").toString();
      }
   }
}
