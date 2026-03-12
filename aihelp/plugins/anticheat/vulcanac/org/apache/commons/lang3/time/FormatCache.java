package org.apache.commons.lang3.time;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.Validate;

abstract class FormatCache<F extends Format> {
   static final int NONE = -1;
   private final ConcurrentMap<FormatCache.ArrayKey, F> cInstanceCache = new ConcurrentHashMap(7);
   private static final ConcurrentMap<FormatCache.ArrayKey, String> cDateTimeInstanceCache = new ConcurrentHashMap(7);

   public F getInstance() {
      return this.getDateTimeInstance(3, 3, TimeZone.getDefault(), Locale.getDefault());
   }

   public F getInstance(String var1, TimeZone var2, Locale var3) {
      Validate.notNull(var1, "pattern");
      if (var2 == null) {
         var2 = TimeZone.getDefault();
      }

      var3 = LocaleUtils.toLocale(var3);
      FormatCache.ArrayKey var4 = new FormatCache.ArrayKey(new Object[]{var1, var2, var3});
      Format var5 = (Format)this.cInstanceCache.get(var4);
      if (var5 == null) {
         var5 = this.createInstance(var1, var2, var3);
         Format var6 = (Format)this.cInstanceCache.putIfAbsent(var4, var5);
         if (var6 != null) {
            var5 = var6;
         }
      }

      return var5;
   }

   protected abstract F createInstance(String var1, TimeZone var2, Locale var3);

   private F getDateTimeInstance(Integer var1, Integer var2, TimeZone var3, Locale var4) {
      var4 = LocaleUtils.toLocale(var4);
      String var5 = getPatternForStyle(var1, var2, var4);
      return this.getInstance(var5, var3, var4);
   }

   F getDateTimeInstance(int var1, int var2, TimeZone var3, Locale var4) {
      return this.getDateTimeInstance(var1, var2, var3, var4);
   }

   F getDateInstance(int var1, TimeZone var2, Locale var3) {
      return this.getDateTimeInstance(var1, (Integer)null, var2, var3);
   }

   F getTimeInstance(int var1, TimeZone var2, Locale var3) {
      return this.getDateTimeInstance((Integer)null, var1, var2, var3);
   }

   static String getPatternForStyle(Integer var0, Integer var1, Locale var2) {
      Locale var3 = LocaleUtils.toLocale(var2);
      FormatCache.ArrayKey var4 = new FormatCache.ArrayKey(new Object[]{var0, var1, var3});
      String var5 = (String)cDateTimeInstanceCache.get(var4);
      if (var5 == null) {
         try {
            DateFormat var6;
            if (var0 == null) {
               var6 = DateFormat.getTimeInstance(var1, var3);
            } else if (var1 == null) {
               var6 = DateFormat.getDateInstance(var0, var3);
            } else {
               var6 = DateFormat.getDateTimeInstance(var0, var1, var3);
            }

            var5 = ((SimpleDateFormat)var6).toPattern();
            String var7 = (String)cDateTimeInstanceCache.putIfAbsent(var4, var5);
            if (var7 != null) {
               var5 = var7;
            }
         } catch (ClassCastException var8) {
            throw new IllegalArgumentException("No date time pattern for locale: " + var3);
         }
      }

      return var5;
   }

   private static final class ArrayKey {
      private final Object[] keys;
      private final int hashCode;

      private static int computeHashCode(Object[] var0) {
         boolean var1 = true;
         byte var2 = 1;
         int var3 = 31 * var2 + Arrays.hashCode(var0);
         return var3;
      }

      ArrayKey(Object... var1) {
         this.keys = var1;
         this.hashCode = computeHashCode(var1);
      }

      public int hashCode() {
         return this.hashCode;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (var1 == null) {
            return false;
         } else if (this.getClass() != var1.getClass()) {
            return false;
         } else {
            FormatCache.ArrayKey var2 = (FormatCache.ArrayKey)var1;
            return Arrays.deepEquals(this.keys, var2.keys);
         }
      }
   }
}
