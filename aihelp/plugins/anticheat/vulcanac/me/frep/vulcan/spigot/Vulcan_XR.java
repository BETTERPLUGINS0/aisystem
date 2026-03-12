package me.frep.vulcan.spigot;

public enum Vulcan_XR {
   public static final Vulcan_XR POSITIVE;
   public static final Vulcan_XR NEGATIVE;
   private final int Vulcan_y;
   private final String Vulcan_k;
   private static final Vulcan_XR[] Vulcan_A;
   private static final String Vulcan_P;
   private static final Vulcan_XR[] Vulcan_W;

   private Vulcan_XR(String var3, int var4, int var5, String var6) {
      this.Vulcan_y = var5;
      this.Vulcan_k = var6;
   }

   public int Vulcan_M() {
      return this.Vulcan_y;
   }

   public String toString() {
      return this.Vulcan_k;
   }

   static {
      String[] var6 = new String[7];
      int var4 = 0;
      String var3 = "B\bDuDY\n3w)u\bO\u0001\\\u0004  lD\u0010U+l$\u0006\rI!*~\"\u0015\u001dSw!\bQ\u000bH\f  lD\bQ\u000bH\f  lD";
      int var5 = "B\bDuDY\n3w)u\bO\u0001\\\u0004  lD\u0010U+l$\u0006\rI!*~\"\u0015\u001dSw!\bQ\u000bH\f  lD\bQ\u000bH\f  lD".length();
      char var2 = 11;
      int var1 = -1;

      label55:
      while(true) {
         byte var10000 = 20;
         ++var1;
         String var10001 = var3.substring(var1, var1 + var2);
         byte var10002 = -1;

         while(true) {
            char[] var14;
            label50: {
               char[] var15 = var10001.toCharArray();
               int var10004 = var15.length;
               int var7 = 0;
               byte var18 = var10000;
               byte var17 = var10000;
               var14 = var15;
               int var11 = var10004;
               char[] var19;
               int var10006;
               if (var10004 <= 1) {
                  var19 = var15;
                  var10006 = var7;
               } else {
                  var17 = var10000;
                  var11 = var10004;
                  if (var10004 <= var7) {
                     break label50;
                  }

                  var19 = var15;
                  var10006 = var7;
               }

               while(true) {
                  char var28 = var19[var10006];
                  byte var29;
                  switch(var7 % 7) {
                  case 0:
                     var29 = 21;
                     break;
                  case 1:
                     var29 = 80;
                     break;
                  case 2:
                     var29 = 15;
                     break;
                  case 3:
                     var29 = 81;
                     break;
                  case 4:
                     var29 = 96;
                     break;
                  case 5:
                     var29 = 125;
                     break;
                  default:
                     var29 = 46;
                  }

                  var19[var10006] = (char)(var28 ^ var18 ^ var29);
                  ++var7;
                  if (var17 == 0) {
                     var10006 = var17;
                     var19 = var14;
                     var18 = var17;
                  } else {
                     if (var11 <= var7) {
                        break;
                     }

                     var19 = var14;
                     var18 = var17;
                     var10006 = var7;
                  }
               }
            }

            String var21 = (new String(var14)).intern();
            switch(var10002) {
            case 0:
               var6[var4++] = var21;
               if ((var1 += var2) >= var5) {
                  Vulcan_P = var6[0];
                  POSITIVE = new Vulcan_XR(var6[4], 0, var6[3], 0, 1, var6[5]);
                  NEGATIVE = new Vulcan_XR(var6[6], 1, var6[1], 1, -1, var6[2]);
                  Vulcan_W = new Vulcan_XR[]{POSITIVE, NEGATIVE};
                  Vulcan_A = new Vulcan_XR[]{POSITIVE, NEGATIVE};
                  return;
               }

               var2 = var3.charAt(var1);
               break;
            default:
               var6[var4++] = var21;
               if ((var1 += var2) < var5) {
                  var2 = var3.charAt(var1);
                  continue label55;
               }

               var3 = "5K\fDfm)AT\u0014V}}3\u0017A\b/a<d@@\f$";
               var5 = "5K\fDfm)AT\u0014V}}3\u0017A\b/a<d@@\f$".length();
               var2 = 16;
               var1 = -1;
            }

            var10000 = 116;
            ++var1;
            var10001 = var3.substring(var1, var1 + var2);
            var10002 = 0;
         }
      }
   }
}
