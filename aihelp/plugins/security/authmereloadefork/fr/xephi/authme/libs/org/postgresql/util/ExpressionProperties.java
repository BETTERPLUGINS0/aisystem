package fr.xephi.authme.libs.org.postgresql.util;

import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.PolyNull;
import org.checkerframework.checker.regex.qual.Regex;

public class ExpressionProperties extends Properties {
   @Regex(1)
   private static final Pattern EXPRESSION = Pattern.compile("\\$\\{([^}]+)\\}");
   private final Properties[] defaults;

   public ExpressionProperties(Properties... defaults) {
      this.defaults = defaults;
   }

   @Nullable
   public String getProperty(String key) {
      String value = this.getRawPropertyValue(key);
      return this.replaceProperties(value);
   }

   @PolyNull
   public String getProperty(String key, @PolyNull String defaultValue) {
      String value = this.getRawPropertyValue(key);
      if (value == null) {
         value = defaultValue;
      }

      return this.replaceProperties(value);
   }

   @Nullable
   public String getRawPropertyValue(String key) {
      String value = super.getProperty(key);
      if (value != null) {
         return value;
      } else {
         Properties[] var3 = this.defaults;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Properties properties = var3[var5];
            value = properties.getProperty(key);
            if (value != null) {
               return value;
            }
         }

         return null;
      }
   }

   @PolyNull
   private String replaceProperties(@PolyNull String value) {
      if (value == null) {
         return null;
      } else {
         Matcher matcher = EXPRESSION.matcher(value);

         StringBuffer sb;
         String propValue;
         for(sb = null; matcher.find(); matcher.appendReplacement(sb, Matcher.quoteReplacement(propValue))) {
            if (sb == null) {
               sb = new StringBuffer();
            }

            propValue = this.getProperty((String)Nullness.castNonNull(matcher.group(1)));
            if (propValue == null) {
               propValue = matcher.group();
            }
         }

         if (sb == null) {
            return value;
         } else {
            matcher.appendTail(sb);
            return sb.toString();
         }
      }
   }
}
