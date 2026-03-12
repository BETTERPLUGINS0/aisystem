package org.apache.commons.lang3;

public class CharSetUtils {
   public static boolean containsAny(String var0, String... var1) {
      if (!StringUtils.isEmpty(var0) && !deepEmpty(var1)) {
         CharSet var2 = CharSet.getInstance(var1);
         char[] var3 = var0.toCharArray();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            char var6 = var3[var5];
            if (var2.contains(var6)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static int count(String var0, String... var1) {
      if (!StringUtils.isEmpty(var0) && !deepEmpty(var1)) {
         CharSet var2 = CharSet.getInstance(var1);
         int var3 = 0;
         char[] var4 = var0.toCharArray();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            char var7 = var4[var6];
            if (var2.contains(var7)) {
               ++var3;
            }
         }

         return var3;
      } else {
         return 0;
      }
   }

   private static boolean deepEmpty(String[] var0) {
      if (var0 != null) {
         String[] var1 = var0;
         int var2 = var0.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            String var4 = var1[var3];
            if (StringUtils.isNotEmpty(var4)) {
               return false;
            }
         }
      }

      return true;
   }

   public static String delete(String var0, String... var1) {
      return !StringUtils.isEmpty(var0) && !deepEmpty(var1) ? modify(var0, var1, false) : var0;
   }

   public static String keep(String var0, String... var1) {
      if (var0 == null) {
         return null;
      } else {
         return !var0.isEmpty() && !deepEmpty(var1) ? modify(var0, var1, true) : "";
      }
   }

   private static String modify(String var0, String[] var1, boolean var2) {
      CharSet var3 = CharSet.getInstance(var1);
      StringBuilder var4 = new StringBuilder(var0.length());
      char[] var5 = var0.toCharArray();
      char[] var6 = var5;
      int var7 = var5.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         char var9 = var6[var8];
         if (var3.contains(var9) == var2) {
            var4.append(var9);
         }
      }

      return var4.toString();
   }

   public static String squeeze(String var0, String... var1) {
      if (!StringUtils.isEmpty(var0) && !deepEmpty(var1)) {
         CharSet var2 = CharSet.getInstance(var1);
         StringBuilder var3 = new StringBuilder(var0.length());
         char[] var4 = var0.toCharArray();
         int var5 = var4.length;
         char var6 = var4[0];
         boolean var7 = true;
         Character var8 = null;
         Character var9 = null;
         var3.append(var6);

         for(int var10 = 1; var10 < var5; ++var10) {
            char var11 = var4[var10];
            if (var11 == var6) {
               if (var8 != null && var11 == var8) {
                  continue;
               }

               if (var9 == null || var11 != var9) {
                  if (var2.contains(var11)) {
                     var8 = var11;
                     continue;
                  }

                  var9 = var11;
               }
            }

            var3.append(var11);
            var6 = var11;
         }

         return var3.toString();
      } else {
         return var0;
      }
   }
}
