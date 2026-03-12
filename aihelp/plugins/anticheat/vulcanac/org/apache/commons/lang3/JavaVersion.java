package org.apache.commons.lang3;

import org.apache.commons.lang3.math.NumberUtils;

public enum JavaVersion {
   JAVA_0_9(1.5F, "0.9"),
   JAVA_1_1(1.1F, "1.1"),
   JAVA_1_2(1.2F, "1.2"),
   JAVA_1_3(1.3F, "1.3"),
   JAVA_1_4(1.4F, "1.4"),
   JAVA_1_5(1.5F, "1.5"),
   JAVA_1_6(1.6F, "1.6"),
   JAVA_1_7(1.7F, "1.7"),
   JAVA_1_8(1.8F, "1.8"),
   /** @deprecated */
   @Deprecated
   JAVA_1_9(9.0F, "9"),
   JAVA_9(9.0F, "9"),
   JAVA_10(10.0F, "10"),
   JAVA_11(11.0F, "11"),
   JAVA_12(12.0F, "12"),
   JAVA_13(13.0F, "13"),
   JAVA_14(14.0F, "14"),
   JAVA_15(15.0F, "15"),
   JAVA_16(16.0F, "16"),
   JAVA_17(17.0F, "17"),
   JAVA_RECENT(maxVersion(), Float.toString(maxVersion()));

   private final float value;
   private final String name;

   private JavaVersion(float var3, String var4) {
      this.value = var3;
      this.name = var4;
   }

   public boolean atLeast(JavaVersion var1) {
      return this.value >= var1.value;
   }

   public boolean atMost(JavaVersion var1) {
      return this.value <= var1.value;
   }

   static JavaVersion getJavaVersion(String var0) {
      return get(var0);
   }

   static JavaVersion get(String var0) {
      if (var0 == null) {
         return null;
      } else {
         byte var2 = -1;
         switch(var0.hashCode()) {
         case 57:
            if (var0.equals("9")) {
               var2 = 9;
            }
            break;
         case 1567:
            if (var0.equals("10")) {
               var2 = 10;
            }
            break;
         case 1568:
            if (var0.equals("11")) {
               var2 = 11;
            }
            break;
         case 1569:
            if (var0.equals("12")) {
               var2 = 12;
            }
            break;
         case 1570:
            if (var0.equals("13")) {
               var2 = 13;
            }
            break;
         case 1571:
            if (var0.equals("14")) {
               var2 = 14;
            }
            break;
         case 1572:
            if (var0.equals("15")) {
               var2 = 15;
            }
            break;
         case 1573:
            if (var0.equals("16")) {
               var2 = 16;
            }
            break;
         case 1574:
            if (var0.equals("17")) {
               var2 = 17;
            }
            break;
         case 47611:
            if (var0.equals("0.9")) {
               var2 = 0;
            }
            break;
         case 48564:
            if (var0.equals("1.1")) {
               var2 = 1;
            }
            break;
         case 48565:
            if (var0.equals("1.2")) {
               var2 = 2;
            }
            break;
         case 48566:
            if (var0.equals("1.3")) {
               var2 = 3;
            }
            break;
         case 48567:
            if (var0.equals("1.4")) {
               var2 = 4;
            }
            break;
         case 48568:
            if (var0.equals("1.5")) {
               var2 = 5;
            }
            break;
         case 48569:
            if (var0.equals("1.6")) {
               var2 = 6;
            }
            break;
         case 48570:
            if (var0.equals("1.7")) {
               var2 = 7;
            }
            break;
         case 48571:
            if (var0.equals("1.8")) {
               var2 = 8;
            }
         }

         switch(var2) {
         case 0:
            return JAVA_0_9;
         case 1:
            return JAVA_1_1;
         case 2:
            return JAVA_1_2;
         case 3:
            return JAVA_1_3;
         case 4:
            return JAVA_1_4;
         case 5:
            return JAVA_1_5;
         case 6:
            return JAVA_1_6;
         case 7:
            return JAVA_1_7;
         case 8:
            return JAVA_1_8;
         case 9:
            return JAVA_9;
         case 10:
            return JAVA_10;
         case 11:
            return JAVA_11;
         case 12:
            return JAVA_12;
         case 13:
            return JAVA_13;
         case 14:
            return JAVA_14;
         case 15:
            return JAVA_15;
         case 16:
            return JAVA_16;
         case 17:
            return JAVA_17;
         default:
            float var3 = toFloatVersion(var0);
            if ((double)var3 - 1.0D < 1.0D) {
               int var4 = Math.max(var0.indexOf(46), var0.indexOf(44));
               int var5 = Math.max(var0.length(), var0.indexOf(44, var4));
               if (Float.parseFloat(var0.substring(var4 + 1, var5)) > 0.9F) {
                  return JAVA_RECENT;
               }
            } else if (var3 > 10.0F) {
               return JAVA_RECENT;
            }

            return null;
         }
      }
   }

   public String toString() {
      return this.name;
   }

   private static float maxVersion() {
      float var0 = toFloatVersion(System.getProperty("java.specification.version", "99.0"));
      return var0 > 0.0F ? var0 : 99.0F;
   }

   private static float toFloatVersion(String var0) {
      boolean var1 = true;
      if (var0.contains(".")) {
         String[] var2 = var0.split("\\.");
         return var2.length >= 2 ? NumberUtils.toFloat(var2[0] + '.' + var2[1], -1.0F) : -1.0F;
      } else {
         return NumberUtils.toFloat(var0, -1.0F);
      }
   }
}
