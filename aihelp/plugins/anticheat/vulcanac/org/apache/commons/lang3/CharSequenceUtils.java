package org.apache.commons.lang3;

public class CharSequenceUtils {
   private static final int NOT_FOUND = -1;
   static final int TO_STRING_LIMIT = 16;

   public static CharSequence subSequence(CharSequence var0, int var1) {
      return var0 == null ? null : var0.subSequence(var1, var0.length());
   }

   static int indexOf(CharSequence var0, int var1, int var2) {
      if (var0 instanceof String) {
         return ((String)var0).indexOf(var1, var2);
      } else {
         int var3 = var0.length();
         if (var2 < 0) {
            var2 = 0;
         }

         if (var1 < 65536) {
            for(int var8 = var2; var8 < var3; ++var8) {
               if (var0.charAt(var8) == var1) {
                  return var8;
               }
            }

            return -1;
         } else {
            if (var1 <= 1114111) {
               char[] var4 = Character.toChars(var1);

               for(int var5 = var2; var5 < var3 - 1; ++var5) {
                  char var6 = var0.charAt(var5);
                  char var7 = var0.charAt(var5 + 1);
                  if (var6 == var4[0] && var7 == var4[1]) {
                     return var5;
                  }
               }
            }

            return -1;
         }
      }
   }

   static int indexOf(CharSequence var0, CharSequence var1, int var2) {
      if (var0 instanceof String) {
         return ((String)var0).indexOf(var1.toString(), var2);
      } else if (var0 instanceof StringBuilder) {
         return ((StringBuilder)var0).indexOf(var1.toString(), var2);
      } else {
         return var0 instanceof StringBuffer ? ((StringBuffer)var0).indexOf(var1.toString(), var2) : var0.toString().indexOf(var1.toString(), var2);
      }
   }

   static int lastIndexOf(CharSequence var0, int var1, int var2) {
      if (var0 instanceof String) {
         return ((String)var0).lastIndexOf(var1, var2);
      } else {
         int var3 = var0.length();
         if (var2 < 0) {
            return -1;
         } else {
            if (var2 >= var3) {
               var2 = var3 - 1;
            }

            if (var1 < 65536) {
               for(int var8 = var2; var8 >= 0; --var8) {
                  if (var0.charAt(var8) == var1) {
                     return var8;
                  }
               }

               return -1;
            } else {
               if (var1 <= 1114111) {
                  char[] var4 = Character.toChars(var1);
                  if (var2 == var3 - 1) {
                     return -1;
                  }

                  for(int var5 = var2; var5 >= 0; --var5) {
                     char var6 = var0.charAt(var5);
                     char var7 = var0.charAt(var5 + 1);
                     if (var4[0] == var6 && var4[1] == var7) {
                        return var5;
                     }
                  }
               }

               return -1;
            }
         }
      }
   }

   static int lastIndexOf(CharSequence var0, CharSequence var1, int var2) {
      if (var1 != null && var0 != null) {
         if (var1 instanceof String) {
            if (var0 instanceof String) {
               return ((String)var0).lastIndexOf((String)var1, var2);
            }

            if (var0 instanceof StringBuilder) {
               return ((StringBuilder)var0).lastIndexOf((String)var1, var2);
            }

            if (var0 instanceof StringBuffer) {
               return ((StringBuffer)var0).lastIndexOf((String)var1, var2);
            }
         }

         int var3 = var0.length();
         int var4 = var1.length();
         if (var2 > var3) {
            var2 = var3;
         }

         if (var2 >= 0 && var4 >= 0 && var4 <= var3) {
            if (var4 == 0) {
               return var2;
            } else {
               if (var4 <= 16) {
                  if (var0 instanceof String) {
                     return ((String)var0).lastIndexOf(var1.toString(), var2);
                  }

                  if (var0 instanceof StringBuilder) {
                     return ((StringBuilder)var0).lastIndexOf(var1.toString(), var2);
                  }

                  if (var0 instanceof StringBuffer) {
                     return ((StringBuffer)var0).lastIndexOf(var1.toString(), var2);
                  }
               }

               if (var2 + var4 > var3) {
                  var2 = var3 - var4;
               }

               char var5 = var1.charAt(0);
               int var6 = var2;

               do {
                  while(var0.charAt(var6) == var5) {
                     if (checkLaterThan1(var0, var1, var4, var6)) {
                        return var6;
                     }

                     --var6;
                     if (var6 < 0) {
                        return -1;
                     }
                  }

                  --var6;
               } while(var6 >= 0);

               return -1;
            }
         } else {
            return -1;
         }
      } else {
         return -1;
      }
   }

   private static boolean checkLaterThan1(CharSequence var0, CharSequence var1, int var2, int var3) {
      int var4 = 1;

      for(int var5 = var2 - 1; var4 <= var5; --var5) {
         if (var0.charAt(var3 + var4) != var1.charAt(var4) || var0.charAt(var3 + var5) != var1.charAt(var5)) {
            return false;
         }

         ++var4;
      }

      return true;
   }

   public static char[] toCharArray(CharSequence var0) {
      int var1 = StringUtils.length(var0);
      if (var1 == 0) {
         return ArrayUtils.EMPTY_CHAR_ARRAY;
      } else if (var0 instanceof String) {
         return ((String)var0).toCharArray();
      } else {
         char[] var2 = new char[var1];

         for(int var3 = 0; var3 < var1; ++var3) {
            var2[var3] = var0.charAt(var3);
         }

         return var2;
      }
   }

   static boolean regionMatches(CharSequence var0, boolean var1, int var2, CharSequence var3, int var4, int var5) {
      if (var0 instanceof String && var3 instanceof String) {
         return ((String)var0).regionMatches(var1, var2, (String)var3, var4, var5);
      } else {
         int var6 = var2;
         int var7 = var4;
         int var8 = var5;
         int var9 = var0.length() - var2;
         int var10 = var3.length() - var4;
         if (var2 >= 0 && var4 >= 0 && var5 >= 0) {
            if (var9 >= var5 && var10 >= var5) {
               while(var8-- > 0) {
                  char var11 = var0.charAt(var6++);
                  char var12 = var3.charAt(var7++);
                  if (var11 != var12) {
                     if (!var1) {
                        return false;
                     }

                     char var13 = Character.toUpperCase(var11);
                     char var14 = Character.toUpperCase(var12);
                     if (var13 != var14 && Character.toLowerCase(var13) != Character.toLowerCase(var14)) {
                        return false;
                     }
                  }
               }

               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }
}
