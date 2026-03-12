package org.apache.commons.lang3.math;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public class NumberUtils {
   public static final Long LONG_ZERO = 0L;
   public static final Long LONG_ONE = 1L;
   public static final Long LONG_MINUS_ONE = -1L;
   public static final Integer INTEGER_ZERO = 0;
   public static final Integer INTEGER_ONE = 1;
   public static final Integer INTEGER_TWO = 2;
   public static final Integer INTEGER_MINUS_ONE = -1;
   public static final Short SHORT_ZERO = Short.valueOf((short)0);
   public static final Short SHORT_ONE = Short.valueOf((short)1);
   public static final Short SHORT_MINUS_ONE = Short.valueOf((short)-1);
   public static final Byte BYTE_ZERO = 0;
   public static final Byte BYTE_ONE = 1;
   public static final Byte BYTE_MINUS_ONE = -1;
   public static final Double DOUBLE_ZERO = 0.0D;
   public static final Double DOUBLE_ONE = 1.0D;
   public static final Double DOUBLE_MINUS_ONE = -1.0D;
   public static final Float FLOAT_ZERO = 0.0F;
   public static final Float FLOAT_ONE = 1.0F;
   public static final Float FLOAT_MINUS_ONE = -1.0F;
   public static final Long LONG_INT_MAX_VALUE = 2147483647L;
   public static final Long LONG_INT_MIN_VALUE = -2147483648L;

   public static int toInt(String var0) {
      return toInt(var0, 0);
   }

   public static int toInt(String var0, int var1) {
      if (var0 == null) {
         return var1;
      } else {
         try {
            return Integer.parseInt(var0);
         } catch (NumberFormatException var3) {
            return var1;
         }
      }
   }

   public static long toLong(String var0) {
      return toLong(var0, 0L);
   }

   public static long toLong(String var0, long var1) {
      if (var0 == null) {
         return var1;
      } else {
         try {
            return Long.parseLong(var0);
         } catch (NumberFormatException var4) {
            return var1;
         }
      }
   }

   public static float toFloat(String var0) {
      return toFloat(var0, 0.0F);
   }

   public static float toFloat(String var0, float var1) {
      if (var0 == null) {
         return var1;
      } else {
         try {
            return Float.parseFloat(var0);
         } catch (NumberFormatException var3) {
            return var1;
         }
      }
   }

   public static double toDouble(String var0) {
      return toDouble(var0, 0.0D);
   }

   public static double toDouble(String var0, double var1) {
      if (var0 == null) {
         return var1;
      } else {
         try {
            return Double.parseDouble(var0);
         } catch (NumberFormatException var4) {
            return var1;
         }
      }
   }

   public static double toDouble(BigDecimal var0) {
      return toDouble(var0, 0.0D);
   }

   public static double toDouble(BigDecimal var0, double var1) {
      return var0 == null ? var1 : var0.doubleValue();
   }

   public static byte toByte(String var0) {
      return toByte(var0, (byte)0);
   }

   public static byte toByte(String var0, byte var1) {
      if (var0 == null) {
         return var1;
      } else {
         try {
            return Byte.parseByte(var0);
         } catch (NumberFormatException var3) {
            return var1;
         }
      }
   }

   public static short toShort(String var0) {
      return toShort(var0, (short)0);
   }

   public static short toShort(String var0, short var1) {
      if (var0 == null) {
         return var1;
      } else {
         try {
            return Short.parseShort(var0);
         } catch (NumberFormatException var3) {
            return var1;
         }
      }
   }

   public static BigDecimal toScaledBigDecimal(BigDecimal var0) {
      return toScaledBigDecimal(var0, INTEGER_TWO, RoundingMode.HALF_EVEN);
   }

   public static BigDecimal toScaledBigDecimal(BigDecimal var0, int var1, RoundingMode var2) {
      return var0 == null ? BigDecimal.ZERO : var0.setScale(var1, var2 == null ? RoundingMode.HALF_EVEN : var2);
   }

   public static BigDecimal toScaledBigDecimal(Float var0) {
      return toScaledBigDecimal(var0, INTEGER_TWO, RoundingMode.HALF_EVEN);
   }

   public static BigDecimal toScaledBigDecimal(Float var0, int var1, RoundingMode var2) {
      return var0 == null ? BigDecimal.ZERO : toScaledBigDecimal(BigDecimal.valueOf((double)var0), var1, var2);
   }

   public static BigDecimal toScaledBigDecimal(Double var0) {
      return toScaledBigDecimal(var0, INTEGER_TWO, RoundingMode.HALF_EVEN);
   }

   public static BigDecimal toScaledBigDecimal(Double var0, int var1, RoundingMode var2) {
      return var0 == null ? BigDecimal.ZERO : toScaledBigDecimal(BigDecimal.valueOf(var0), var1, var2);
   }

   public static BigDecimal toScaledBigDecimal(String var0) {
      return toScaledBigDecimal(var0, INTEGER_TWO, RoundingMode.HALF_EVEN);
   }

   public static BigDecimal toScaledBigDecimal(String var0, int var1, RoundingMode var2) {
      return var0 == null ? BigDecimal.ZERO : toScaledBigDecimal(createBigDecimal(var0), var1, var2);
   }

   public static Number createNumber(String var0) {
      if (var0 == null) {
         return null;
      } else if (StringUtils.isBlank(var0)) {
         throw new NumberFormatException("A blank string is not a valid number");
      } else {
         String[] var1 = new String[]{"0x", "0X", "-0x", "-0X", "#", "-#"};
         int var2 = var0.length();
         int var3 = 0;
         String[] var4 = var1;
         int var5 = var1.length;

         String var7;
         for(int var6 = 0; var6 < var5; ++var6) {
            var7 = var4[var6];
            if (var0.startsWith(var7)) {
               var3 += var7.length();
               break;
            }
         }

         char var21;
         if (var3 > 0) {
            var21 = 0;

            for(var5 = var3; var5 < var2; ++var5) {
               var21 = var0.charAt(var5);
               if (var21 != '0') {
                  break;
               }

               ++var3;
            }

            var5 = var2 - var3;
            if (var5 <= 16 && (var5 != 16 || var21 <= '7')) {
               return (Number)(var5 <= 8 && (var5 != 8 || var21 <= '7') ? createInteger(var0) : createLong(var0));
            } else {
               return createBigInteger(var0);
            }
         } else {
            var21 = var0.charAt(var2 - 1);
            int var8 = var0.indexOf(46);
            int var9 = var0.indexOf(101) + var0.indexOf(69) + 1;
            String var22;
            String var23;
            if (var8 <= -1) {
               if (var9 > -1) {
                  if (var9 > var2) {
                     throw new NumberFormatException(var0 + " is not a valid number.");
                  }

                  var22 = getMantissa(var0, var9);
               } else {
                  var22 = getMantissa(var0);
               }

               var23 = null;
            } else {
               if (var9 > -1) {
                  if (var9 < var8 || var9 > var2) {
                     throw new NumberFormatException(var0 + " is not a valid number.");
                  }

                  var23 = var0.substring(var8 + 1, var9);
               } else {
                  var23 = var0.substring(var8 + 1);
               }

               var22 = getMantissa(var0, var8);
            }

            Double var12;
            if (!Character.isDigit(var21) && var21 != '.') {
               if (var9 > -1 && var9 < var2 - 1) {
                  var7 = var0.substring(var9 + 1, var2 - 1);
               } else {
                  var7 = null;
               }

               String var24 = var0.substring(0, var2 - 1);
               boolean var25 = isAllZeros(var22) && isAllZeros(var7);
               switch(var21) {
               case 'D':
               case 'd':
                  break;
               case 'F':
               case 'f':
                  try {
                     Float var26 = createFloat(var0);
                     if (var26.isInfinite() || var26 == 0.0F && !var25) {
                        break;
                     }

                     return var26;
                  } catch (NumberFormatException var19) {
                     break;
                  }
               case 'L':
               case 'l':
                  if (var23 == null && var7 == null && (!var24.isEmpty() && var24.charAt(0) == '-' && isDigits(var24.substring(1)) || isDigits(var24))) {
                     try {
                        return createLong(var24);
                     } catch (NumberFormatException var15) {
                        return createBigInteger(var24);
                     }
                  } else {
                     throw new NumberFormatException(var0 + " is not a valid number.");
                  }
               default:
                  throw new NumberFormatException(var0 + " is not a valid number.");
               }

               try {
                  var12 = createDouble(var0);
                  if (!var12.isInfinite() && (var12 != 0.0D || var25)) {
                     return var12;
                  }
               } catch (NumberFormatException var18) {
               }

               try {
                  return createBigDecimal(var24);
               } catch (NumberFormatException var17) {
                  throw new NumberFormatException(var0 + " is not a valid number.");
               }
            } else {
               if (var9 > -1 && var9 < var2 - 1) {
                  var7 = var0.substring(var9 + 1);
               } else {
                  var7 = null;
               }

               if (var23 == null && var7 == null) {
                  try {
                     return createInteger(var0);
                  } catch (NumberFormatException var16) {
                     try {
                        return createLong(var0);
                     } catch (NumberFormatException var14) {
                        return createBigInteger(var0);
                     }
                  }
               } else {
                  boolean var10 = isAllZeros(var22) && isAllZeros(var7);

                  try {
                     Float var11 = createFloat(var0);
                     var12 = createDouble(var0);
                     if (!var11.isInfinite() && (var11 != 0.0F || var10) && var11.toString().equals(var12.toString())) {
                        return var11;
                     }

                     if (!var12.isInfinite() && (var12 != 0.0D || var10)) {
                        BigDecimal var13 = createBigDecimal(var0);
                        if (var13.compareTo(BigDecimal.valueOf(var12)) == 0) {
                           return var12;
                        }

                        return var13;
                     }
                  } catch (NumberFormatException var20) {
                  }

                  return createBigDecimal(var0);
               }
            }
         }
      }
   }

   private static String getMantissa(String var0) {
      return getMantissa(var0, var0.length());
   }

   private static String getMantissa(String var0, int var1) {
      char var2 = var0.charAt(0);
      boolean var3 = var2 == '-' || var2 == '+';
      return var3 ? var0.substring(1, var1) : var0.substring(0, var1);
   }

   private static boolean isAllZeros(String var0) {
      if (var0 == null) {
         return true;
      } else {
         for(int var1 = var0.length() - 1; var1 >= 0; --var1) {
            if (var0.charAt(var1) != '0') {
               return false;
            }
         }

         return !var0.isEmpty();
      }
   }

   public static Float createFloat(String var0) {
      return var0 == null ? null : Float.valueOf(var0);
   }

   public static Double createDouble(String var0) {
      return var0 == null ? null : Double.valueOf(var0);
   }

   public static Integer createInteger(String var0) {
      return var0 == null ? null : Integer.decode(var0);
   }

   public static Long createLong(String var0) {
      return var0 == null ? null : Long.decode(var0);
   }

   public static BigInteger createBigInteger(String var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = 0;
         byte var2 = 10;
         boolean var3 = false;
         if (var0.startsWith("-")) {
            var3 = true;
            var1 = 1;
         }

         if (!var0.startsWith("0x", var1) && !var0.startsWith("0X", var1)) {
            if (var0.startsWith("#", var1)) {
               var2 = 16;
               ++var1;
            } else if (var0.startsWith("0", var1) && var0.length() > var1 + 1) {
               var2 = 8;
               ++var1;
            }
         } else {
            var2 = 16;
            var1 += 2;
         }

         BigInteger var4 = new BigInteger(var0.substring(var1), var2);
         return var3 ? var4.negate() : var4;
      }
   }

   public static BigDecimal createBigDecimal(String var0) {
      if (var0 == null) {
         return null;
      } else if (StringUtils.isBlank(var0)) {
         throw new NumberFormatException("A blank string is not a valid number");
      } else {
         return new BigDecimal(var0);
      }
   }

   public static long min(long... var0) {
      validateArray(var0);
      long var1 = var0[0];

      for(int var3 = 1; var3 < var0.length; ++var3) {
         if (var0[var3] < var1) {
            var1 = var0[var3];
         }
      }

      return var1;
   }

   public static int min(int... var0) {
      validateArray(var0);
      int var1 = var0[0];

      for(int var2 = 1; var2 < var0.length; ++var2) {
         if (var0[var2] < var1) {
            var1 = var0[var2];
         }
      }

      return var1;
   }

   public static short min(short... var0) {
      validateArray(var0);
      short var1 = var0[0];

      for(int var2 = 1; var2 < var0.length; ++var2) {
         if (var0[var2] < var1) {
            var1 = var0[var2];
         }
      }

      return var1;
   }

   public static byte min(byte... var0) {
      validateArray(var0);
      byte var1 = var0[0];

      for(int var2 = 1; var2 < var0.length; ++var2) {
         if (var0[var2] < var1) {
            var1 = var0[var2];
         }
      }

      return var1;
   }

   public static double min(double... var0) {
      validateArray(var0);
      double var1 = var0[0];

      for(int var3 = 1; var3 < var0.length; ++var3) {
         if (Double.isNaN(var0[var3])) {
            return Double.NaN;
         }

         if (var0[var3] < var1) {
            var1 = var0[var3];
         }
      }

      return var1;
   }

   public static float min(float... var0) {
      validateArray(var0);
      float var1 = var0[0];

      for(int var2 = 1; var2 < var0.length; ++var2) {
         if (Float.isNaN(var0[var2])) {
            return Float.NaN;
         }

         if (var0[var2] < var1) {
            var1 = var0[var2];
         }
      }

      return var1;
   }

   public static long max(long... var0) {
      validateArray(var0);
      long var1 = var0[0];

      for(int var3 = 1; var3 < var0.length; ++var3) {
         if (var0[var3] > var1) {
            var1 = var0[var3];
         }
      }

      return var1;
   }

   public static int max(int... var0) {
      validateArray(var0);
      int var1 = var0[0];

      for(int var2 = 1; var2 < var0.length; ++var2) {
         if (var0[var2] > var1) {
            var1 = var0[var2];
         }
      }

      return var1;
   }

   public static short max(short... var0) {
      validateArray(var0);
      short var1 = var0[0];

      for(int var2 = 1; var2 < var0.length; ++var2) {
         if (var0[var2] > var1) {
            var1 = var0[var2];
         }
      }

      return var1;
   }

   public static byte max(byte... var0) {
      validateArray(var0);
      byte var1 = var0[0];

      for(int var2 = 1; var2 < var0.length; ++var2) {
         if (var0[var2] > var1) {
            var1 = var0[var2];
         }
      }

      return var1;
   }

   public static double max(double... var0) {
      validateArray(var0);
      double var1 = var0[0];

      for(int var3 = 1; var3 < var0.length; ++var3) {
         if (Double.isNaN(var0[var3])) {
            return Double.NaN;
         }

         if (var0[var3] > var1) {
            var1 = var0[var3];
         }
      }

      return var1;
   }

   public static float max(float... var0) {
      validateArray(var0);
      float var1 = var0[0];

      for(int var2 = 1; var2 < var0.length; ++var2) {
         if (Float.isNaN(var0[var2])) {
            return Float.NaN;
         }

         if (var0[var2] > var1) {
            var1 = var0[var2];
         }
      }

      return var1;
   }

   private static void validateArray(Object var0) {
      Validate.notNull(var0, "array");
      Validate.isTrue(Array.getLength(var0) != 0, "Array cannot be empty.");
   }

   public static long min(long var0, long var2, long var4) {
      if (var2 < var0) {
         var0 = var2;
      }

      if (var4 < var0) {
         var0 = var4;
      }

      return var0;
   }

   public static int min(int var0, int var1, int var2) {
      if (var1 < var0) {
         var0 = var1;
      }

      if (var2 < var0) {
         var0 = var2;
      }

      return var0;
   }

   public static short min(short var0, short var1, short var2) {
      if (var1 < var0) {
         var0 = var1;
      }

      if (var2 < var0) {
         var0 = var2;
      }

      return var0;
   }

   public static byte min(byte var0, byte var1, byte var2) {
      if (var1 < var0) {
         var0 = var1;
      }

      if (var2 < var0) {
         var0 = var2;
      }

      return var0;
   }

   public static double min(double var0, double var2, double var4) {
      return Math.min(Math.min(var0, var2), var4);
   }

   public static float min(float var0, float var1, float var2) {
      return Math.min(Math.min(var0, var1), var2);
   }

   public static long max(long var0, long var2, long var4) {
      if (var2 > var0) {
         var0 = var2;
      }

      if (var4 > var0) {
         var0 = var4;
      }

      return var0;
   }

   public static int max(int var0, int var1, int var2) {
      if (var1 > var0) {
         var0 = var1;
      }

      if (var2 > var0) {
         var0 = var2;
      }

      return var0;
   }

   public static short max(short var0, short var1, short var2) {
      if (var1 > var0) {
         var0 = var1;
      }

      if (var2 > var0) {
         var0 = var2;
      }

      return var0;
   }

   public static byte max(byte var0, byte var1, byte var2) {
      if (var1 > var0) {
         var0 = var1;
      }

      if (var2 > var0) {
         var0 = var2;
      }

      return var0;
   }

   public static double max(double var0, double var2, double var4) {
      return Math.max(Math.max(var0, var2), var4);
   }

   public static float max(float var0, float var1, float var2) {
      return Math.max(Math.max(var0, var1), var2);
   }

   public static boolean isDigits(String var0) {
      return StringUtils.isNumeric(var0);
   }

   /** @deprecated */
   @Deprecated
   public static boolean isNumber(String var0) {
      return isCreatable(var0);
   }

   public static boolean isCreatable(String var0) {
      if (StringUtils.isEmpty(var0)) {
         return false;
      } else {
         char[] var1 = var0.toCharArray();
         int var2 = var1.length;
         boolean var3 = false;
         boolean var4 = false;
         boolean var5 = false;
         boolean var6 = false;
         int var7 = var1[0] != '-' && var1[0] != '+' ? 0 : 1;
         int var8;
         if (var2 > var7 + 1 && var1[var7] == '0' && !StringUtils.contains(var0, 46)) {
            if (var1[var7 + 1] == 'x' || var1[var7 + 1] == 'X') {
               var8 = var7 + 2;
               if (var8 == var2) {
                  return false;
               }

               while(var8 < var1.length) {
                  if ((var1[var8] < '0' || var1[var8] > '9') && (var1[var8] < 'a' || var1[var8] > 'f') && (var1[var8] < 'A' || var1[var8] > 'F')) {
                     return false;
                  }

                  ++var8;
               }

               return true;
            }

            if (Character.isDigit(var1[var7 + 1])) {
               for(var8 = var7 + 1; var8 < var1.length; ++var8) {
                  if (var1[var8] < '0' || var1[var8] > '7') {
                     return false;
                  }
               }

               return true;
            }
         }

         --var2;

         for(var8 = var7; var8 < var2 || var8 < var2 + 1 && var5 && !var6; ++var8) {
            if (var1[var8] >= '0' && var1[var8] <= '9') {
               var6 = true;
               var5 = false;
            } else if (var1[var8] == '.') {
               if (var4 || var3) {
                  return false;
               }

               var4 = true;
            } else if (var1[var8] != 'e' && var1[var8] != 'E') {
               if (var1[var8] != '+' && var1[var8] != '-') {
                  return false;
               }

               if (!var5) {
                  return false;
               }

               var5 = false;
               var6 = false;
            } else {
               if (var3) {
                  return false;
               }

               if (!var6) {
                  return false;
               }

               var3 = true;
               var5 = true;
            }
         }

         if (var8 < var1.length) {
            if (var1[var8] >= '0' && var1[var8] <= '9') {
               return true;
            } else if (var1[var8] != 'e' && var1[var8] != 'E') {
               if (var1[var8] == '.') {
                  return !var4 && !var3 ? var6 : false;
               } else if (!var5 && (var1[var8] == 'd' || var1[var8] == 'D' || var1[var8] == 'f' || var1[var8] == 'F')) {
                  return var6;
               } else if (var1[var8] != 'l' && var1[var8] != 'L') {
                  return false;
               } else {
                  return var6 && !var3 && !var4;
               }
            } else {
               return false;
            }
         } else {
            return !var5 && var6;
         }
      }
   }

   public static boolean isParsable(String var0) {
      if (StringUtils.isEmpty(var0)) {
         return false;
      } else if (var0.charAt(var0.length() - 1) == '.') {
         return false;
      } else if (var0.charAt(0) == '-') {
         return var0.length() == 1 ? false : withDecimalsParsing(var0, 1);
      } else {
         return withDecimalsParsing(var0, 0);
      }
   }

   private static boolean withDecimalsParsing(String var0, int var1) {
      int var2 = 0;

      for(int var3 = var1; var3 < var0.length(); ++var3) {
         boolean var4 = var0.charAt(var3) == '.';
         if (var4) {
            ++var2;
         }

         if (var2 > 1) {
            return false;
         }

         if (!var4 && !Character.isDigit(var0.charAt(var3))) {
            return false;
         }
      }

      return true;
   }

   public static int compare(int var0, int var1) {
      if (var0 == var1) {
         return 0;
      } else {
         return var0 < var1 ? -1 : 1;
      }
   }

   public static int compare(long var0, long var2) {
      if (var0 == var2) {
         return 0;
      } else {
         return var0 < var2 ? -1 : 1;
      }
   }

   public static int compare(short var0, short var1) {
      if (var0 == var1) {
         return 0;
      } else {
         return var0 < var1 ? -1 : 1;
      }
   }

   public static int compare(byte var0, byte var1) {
      return var0 - var1;
   }
}
