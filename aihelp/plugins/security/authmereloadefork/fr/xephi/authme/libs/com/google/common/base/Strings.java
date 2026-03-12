package fr.xephi.authme.libs.com.google.common.base;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.com.google.errorprone.annotations.InlineMe;
import fr.xephi.authme.libs.com.google.errorprone.annotations.InlineMeValidationDisabled;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public final class Strings {
   private Strings() {
   }

   public static String nullToEmpty(@CheckForNull String string) {
      return Platform.nullToEmpty(string);
   }

   @CheckForNull
   public static String emptyToNull(@CheckForNull String string) {
      return Platform.emptyToNull(string);
   }

   public static boolean isNullOrEmpty(@CheckForNull String string) {
      return Platform.stringIsNullOrEmpty(string);
   }

   public static String padStart(String string, int minLength, char padChar) {
      Preconditions.checkNotNull(string);
      if (string.length() >= minLength) {
         return string;
      } else {
         StringBuilder sb = new StringBuilder(minLength);

         for(int i = string.length(); i < minLength; ++i) {
            sb.append(padChar);
         }

         sb.append(string);
         return sb.toString();
      }
   }

   public static String padEnd(String string, int minLength, char padChar) {
      Preconditions.checkNotNull(string);
      if (string.length() >= minLength) {
         return string;
      } else {
         StringBuilder sb = new StringBuilder(minLength);
         sb.append(string);

         for(int i = string.length(); i < minLength; ++i) {
            sb.append(padChar);
         }

         return sb.toString();
      }
   }

   @InlineMe(
      replacement = "string.repeat(count)"
   )
   @InlineMeValidationDisabled("Java 11+ API only")
   public static String repeat(String string, int count) {
      Preconditions.checkNotNull(string);
      if (count <= 1) {
         Preconditions.checkArgument(count >= 0, "invalid count: %s", count);
         return count == 0 ? "" : string;
      } else {
         int len = string.length();
         long longSize = (long)len * (long)count;
         int size = (int)longSize;
         if ((long)size != longSize) {
            throw new ArrayIndexOutOfBoundsException((new StringBuilder(51)).append("Required array size too large: ").append(longSize).toString());
         } else {
            char[] array = new char[size];
            string.getChars(0, len, array, 0);

            int n;
            for(n = len; n < size - n; n <<= 1) {
               System.arraycopy(array, 0, array, n, n);
            }

            System.arraycopy(array, 0, array, n, size - n);
            return new String(array);
         }
      }
   }

   public static String commonPrefix(CharSequence a, CharSequence b) {
      Preconditions.checkNotNull(a);
      Preconditions.checkNotNull(b);
      int maxPrefixLength = Math.min(a.length(), b.length());

      int p;
      for(p = 0; p < maxPrefixLength && a.charAt(p) == b.charAt(p); ++p) {
      }

      if (validSurrogatePairAt(a, p - 1) || validSurrogatePairAt(b, p - 1)) {
         --p;
      }

      return a.subSequence(0, p).toString();
   }

   public static String commonSuffix(CharSequence a, CharSequence b) {
      Preconditions.checkNotNull(a);
      Preconditions.checkNotNull(b);
      int maxSuffixLength = Math.min(a.length(), b.length());

      int s;
      for(s = 0; s < maxSuffixLength && a.charAt(a.length() - s - 1) == b.charAt(b.length() - s - 1); ++s) {
      }

      if (validSurrogatePairAt(a, a.length() - s - 1) || validSurrogatePairAt(b, b.length() - s - 1)) {
         --s;
      }

      return a.subSequence(a.length() - s, a.length()).toString();
   }

   @VisibleForTesting
   static boolean validSurrogatePairAt(CharSequence string, int index) {
      return index >= 0 && index <= string.length() - 2 && Character.isHighSurrogate(string.charAt(index)) && Character.isLowSurrogate(string.charAt(index + 1));
   }

   public static String lenientFormat(@CheckForNull String template, @CheckForNull Object... args) {
      template = String.valueOf(template);
      if (args == null) {
         args = new Object[]{"(Object[])null"};
      } else {
         for(int i = 0; i < args.length; ++i) {
            args[i] = lenientToString(args[i]);
         }
      }

      StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
      int templateStart = 0;

      int i;
      int placeholderStart;
      for(i = 0; i < args.length; templateStart = placeholderStart + 2) {
         placeholderStart = template.indexOf("%s", templateStart);
         if (placeholderStart == -1) {
            break;
         }

         builder.append(template, templateStart, placeholderStart);
         builder.append(args[i++]);
      }

      builder.append(template, templateStart, template.length());
      if (i < args.length) {
         builder.append(" [");
         builder.append(args[i++]);

         while(i < args.length) {
            builder.append(", ");
            builder.append(args[i++]);
         }

         builder.append(']');
      }

      return builder.toString();
   }

   private static String lenientToString(@CheckForNull Object o) {
      if (o == null) {
         return "null";
      } else {
         try {
            return o.toString();
         } catch (Exception var5) {
            String var3 = o.getClass().getName();
            String var4 = Integer.toHexString(System.identityHashCode(o));
            String objectToString = (new StringBuilder(1 + String.valueOf(var3).length() + String.valueOf(var4).length())).append(var3).append('@').append(var4).toString();
            Logger var10000 = Logger.getLogger("fr.xephi.authme.libs.com.google.common.base.Strings");
            Level var10001 = Level.WARNING;
            String var10003 = String.valueOf(objectToString);
            String var10002;
            if (var10003.length() != 0) {
               var10002 = "Exception during lenientFormat for ".concat(var10003);
            } else {
               String var10004 = new String;
               var10002 = var10004;
               var10004.<init>("Exception during lenientFormat for ");
            }

            var10000.log(var10001, var10002, var5);
            var3 = var5.getClass().getName();
            return (new StringBuilder(9 + String.valueOf(objectToString).length() + String.valueOf(var3).length())).append("<").append(objectToString).append(" threw ").append(var3).append(">").toString();
         }
      }
   }
}
