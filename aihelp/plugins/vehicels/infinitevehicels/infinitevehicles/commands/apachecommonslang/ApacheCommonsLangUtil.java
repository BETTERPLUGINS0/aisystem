package me.PM2.infinitevehicles.commands.apachecommonslang;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Locale;

public class ApacheCommonsLangUtil {
   public static final String EMPTY = "";
   public static final int INDEX_NOT_FOUND = -1;

   public static <T> T[] clone(final T[] array) {
      return var0 == null ? null : (Object[])var0.clone();
   }

   public static <T> T[] addAll(final T[] array1, final T... array2) {
      if (var0 == null) {
         return clone(var1);
      } else if (var1 == null) {
         return clone(var0);
      } else {
         Class var2 = var0.getClass().getComponentType();
         Object[] var3 = (Object[])Array.newInstance(var2, var0.length + var1.length);
         System.arraycopy(var0, 0, var3, 0, var0.length);

         try {
            System.arraycopy(var1, 0, var3, var0.length, var1.length);
            return var3;
         } catch (ArrayStoreException var6) {
            Class var5 = var1.getClass().getComponentType();
            if (!var2.isAssignableFrom(var5)) {
               throw new IllegalArgumentException("Cannot store " + var5.getName() + " in an array of " + var2.getName(), var6);
            } else {
               throw var6;
            }
         }
      }
   }

   public static String capitalizeFully(final String str) {
      return capitalizeFully(var0, (char[])null);
   }

   public static String capitalizeFully(String str, final char... delimiters) {
      int var2 = var1 == null ? -1 : var1.length;
      if (var0 != null && !var0.isEmpty() && var2 != 0) {
         var0 = var0.toLowerCase(Locale.ENGLISH);
         return capitalize(var0, var1);
      } else {
         return var0;
      }
   }

   public static String capitalize(final String str) {
      return capitalize(var0, (char[])null);
   }

   public static String capitalize(final String str, final char... delimiters) {
      int var2 = var1 == null ? -1 : var1.length;
      if (var0 != null && !var0.isEmpty() && var2 != 0) {
         char[] var3 = var0.toCharArray();
         boolean var4 = true;

         for(int var5 = 0; var5 < var3.length; ++var5) {
            char var6 = var3[var5];
            if (isDelimiter(var6, var1)) {
               var4 = true;
            } else if (var4) {
               var3[var5] = Character.toTitleCase(var6);
               var4 = false;
            }
         }

         return new String(var3);
      } else {
         return var0;
      }
   }

   public static boolean isDelimiter(final char ch, final char[] delimiters) {
      if (var1 == null) {
         return Character.isWhitespace(var0);
      } else {
         char[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            char var5 = var2[var4];
            if (var0 == var5) {
               return true;
            }
         }

         return false;
      }
   }

   @SafeVarargs
   public static <T> String join(final T... elements) {
      return join((Object[])var0, (String)null);
   }

   public static String join(final Object[] array, final char separator) {
      return var0 == null ? null : join((Object[])var0, var1, 0, var0.length);
   }

   public static String join(final long[] array, final char separator) {
      return var0 == null ? null : join((long[])var0, var1, 0, var0.length);
   }

   public static String join(final int[] array, final char separator) {
      return var0 == null ? null : join((int[])var0, var1, 0, var0.length);
   }

   public static String join(final short[] array, final char separator) {
      return var0 == null ? null : join((short[])var0, var1, 0, var0.length);
   }

   public static String join(final byte[] array, final char separator) {
      return var0 == null ? null : join((byte[])var0, var1, 0, var0.length);
   }

   public static String join(final char[] array, final char separator) {
      return var0 == null ? null : join((char[])var0, var1, 0, var0.length);
   }

   public static String join(final float[] array, final char separator) {
      return var0 == null ? null : join((float[])var0, var1, 0, var0.length);
   }

   public static String join(final double[] array, final char separator) {
      return var0 == null ? null : join((double[])var0, var1, 0, var0.length);
   }

   public static String join(final Object[] array, final char separator, final int startIndex, final int endIndex) {
      if (var0 == null) {
         return null;
      } else {
         int var4 = var3 - var2;
         if (var4 <= 0) {
            return "";
         } else {
            StringBuilder var5 = new StringBuilder(var4 * 16);

            for(int var6 = var2; var6 < var3; ++var6) {
               if (var6 > var2) {
                  var5.append(var1);
               }

               if (var0[var6] != null) {
                  var5.append(var0[var6]);
               }
            }

            return var5.toString();
         }
      }
   }

   public static String join(final long[] array, final char separator, final int startIndex, final int endIndex) {
      if (var0 == null) {
         return null;
      } else {
         int var4 = var3 - var2;
         if (var4 <= 0) {
            return "";
         } else {
            StringBuilder var5 = new StringBuilder(var4 * 16);

            for(int var6 = var2; var6 < var3; ++var6) {
               if (var6 > var2) {
                  var5.append(var1);
               }

               var5.append(var0[var6]);
            }

            return var5.toString();
         }
      }
   }

   public static String join(final int[] array, final char separator, final int startIndex, final int endIndex) {
      if (var0 == null) {
         return null;
      } else {
         int var4 = var3 - var2;
         if (var4 <= 0) {
            return "";
         } else {
            StringBuilder var5 = new StringBuilder(var4 * 16);

            for(int var6 = var2; var6 < var3; ++var6) {
               if (var6 > var2) {
                  var5.append(var1);
               }

               var5.append(var0[var6]);
            }

            return var5.toString();
         }
      }
   }

   public static String join(final byte[] array, final char separator, final int startIndex, final int endIndex) {
      if (var0 == null) {
         return null;
      } else {
         int var4 = var3 - var2;
         if (var4 <= 0) {
            return "";
         } else {
            StringBuilder var5 = new StringBuilder(var4 * 16);

            for(int var6 = var2; var6 < var3; ++var6) {
               if (var6 > var2) {
                  var5.append(var1);
               }

               var5.append(var0[var6]);
            }

            return var5.toString();
         }
      }
   }

   public static String join(final short[] array, final char separator, final int startIndex, final int endIndex) {
      if (var0 == null) {
         return null;
      } else {
         int var4 = var3 - var2;
         if (var4 <= 0) {
            return "";
         } else {
            StringBuilder var5 = new StringBuilder(var4 * 16);

            for(int var6 = var2; var6 < var3; ++var6) {
               if (var6 > var2) {
                  var5.append(var1);
               }

               var5.append(var0[var6]);
            }

            return var5.toString();
         }
      }
   }

   public static String join(final char[] array, final char separator, final int startIndex, final int endIndex) {
      if (var0 == null) {
         return null;
      } else {
         int var4 = var3 - var2;
         if (var4 <= 0) {
            return "";
         } else {
            StringBuilder var5 = new StringBuilder(var4 * 16);

            for(int var6 = var2; var6 < var3; ++var6) {
               if (var6 > var2) {
                  var5.append(var1);
               }

               var5.append(var0[var6]);
            }

            return var5.toString();
         }
      }
   }

   public static String join(final double[] array, final char separator, final int startIndex, final int endIndex) {
      if (var0 == null) {
         return null;
      } else {
         int var4 = var3 - var2;
         if (var4 <= 0) {
            return "";
         } else {
            StringBuilder var5 = new StringBuilder(var4 * 16);

            for(int var6 = var2; var6 < var3; ++var6) {
               if (var6 > var2) {
                  var5.append(var1);
               }

               var5.append(var0[var6]);
            }

            return var5.toString();
         }
      }
   }

   public static String join(final float[] array, final char separator, final int startIndex, final int endIndex) {
      if (var0 == null) {
         return null;
      } else {
         int var4 = var3 - var2;
         if (var4 <= 0) {
            return "";
         } else {
            StringBuilder var5 = new StringBuilder(var4 * 16);

            for(int var6 = var2; var6 < var3; ++var6) {
               if (var6 > var2) {
                  var5.append(var1);
               }

               var5.append(var0[var6]);
            }

            return var5.toString();
         }
      }
   }

   public static String join(final Object[] array, final String separator) {
      return var0 == null ? null : join(var0, var1, 0, var0.length);
   }

   public static String join(final Object[] array, String separator, final int startIndex, final int endIndex) {
      if (var0 == null) {
         return null;
      } else {
         if (var1 == null) {
            var1 = "";
         }

         int var4 = var3 - var2;
         if (var4 <= 0) {
            return "";
         } else {
            StringBuilder var5 = new StringBuilder(var4 * 16);

            for(int var6 = var2; var6 < var3; ++var6) {
               if (var6 > var2) {
                  var5.append(var1);
               }

               if (var0[var6] != null) {
                  var5.append(var0[var6]);
               }
            }

            return var5.toString();
         }
      }
   }

   public static String join(final Iterator<?> iterator, final char separator) {
      if (var0 == null) {
         return null;
      } else if (!var0.hasNext()) {
         return "";
      } else {
         Object var2 = var0.next();
         if (!var0.hasNext()) {
            String var5 = var2 != null ? var2.toString() : "";
            return var5;
         } else {
            StringBuilder var3 = new StringBuilder(256);
            if (var2 != null) {
               var3.append(var2);
            }

            while(var0.hasNext()) {
               var3.append(var1);
               Object var4 = var0.next();
               if (var4 != null) {
                  var3.append(var4);
               }
            }

            return var3.toString();
         }
      }
   }

   public static String join(final Iterator<?> iterator, final String separator) {
      if (var0 == null) {
         return null;
      } else if (!var0.hasNext()) {
         return "";
      } else {
         Object var2 = var0.next();
         if (!var0.hasNext()) {
            String var5 = var2 != null ? var2.toString() : "";
            return var5;
         } else {
            StringBuilder var3 = new StringBuilder(256);
            if (var2 != null) {
               var3.append(var2);
            }

            while(var0.hasNext()) {
               if (var1 != null) {
                  var3.append(var1);
               }

               Object var4 = var0.next();
               if (var4 != null) {
                  var3.append(var4);
               }
            }

            return var3.toString();
         }
      }
   }

   public static String join(final Iterable<?> iterable, final char separator) {
      return var0 == null ? null : join(var0.iterator(), var1);
   }

   public static String join(final Iterable<?> iterable, final String separator) {
      return var0 == null ? null : join(var0.iterator(), var1);
   }

   public static boolean isNumeric(final CharSequence cs) {
      if (var0 != null && var0.length() != 0) {
         int var1 = var0.length();

         for(int var2 = 0; var2 < var1; ++var2) {
            if (!Character.isDigit(var0.charAt(var2))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean startsWith(final CharSequence str, final CharSequence prefix) {
      return startsWith(var0, var1, false);
   }

   public static boolean startsWithIgnoreCase(final CharSequence str, final CharSequence prefix) {
      return startsWith(var0, var1, true);
   }

   private static boolean startsWith(final CharSequence str, final CharSequence prefix, final boolean ignoreCase) {
      if (var0 != null && var1 != null) {
         return var1.length() > var0.length() ? false : regionMatches(var0, var2, 0, var1, 0, var1.length());
      } else {
         return var0 == null && var1 == null;
      }
   }

   static boolean regionMatches(final CharSequence cs, final boolean ignoreCase, final int thisStart, final CharSequence substring, final int start, final int length) {
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

                     if (Character.toUpperCase(var11) != Character.toUpperCase(var12) && Character.toLowerCase(var11) != Character.toLowerCase(var12)) {
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

   public static int indexOf(Object[] array, Object objectToFind) {
      return indexOf(var0, var1, 0);
   }

   public static int indexOf(Object[] array, Object objectToFind, int startIndex) {
      if (var0 == null) {
         return -1;
      } else {
         if (var2 < 0) {
            var2 = 0;
         }

         int var3;
         if (var1 == null) {
            for(var3 = var2; var3 < var0.length; ++var3) {
               if (var0[var3] == null) {
                  return var3;
               }
            }
         } else {
            for(var3 = var2; var3 < var0.length; ++var3) {
               if (var1.equals(var0[var3])) {
                  return var3;
               }
            }
         }

         return -1;
      }
   }
}
