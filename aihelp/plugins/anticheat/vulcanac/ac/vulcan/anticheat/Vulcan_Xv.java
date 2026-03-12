package ac.vulcan.anticheat;

import java.math.BigDecimal;
import java.math.BigInteger;

/** @deprecated */
public final class Vulcan_Xv {
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-601840082615937796L, 5783445684942616124L, (Object)null).a(144073250378811L);
   private static final String[] b;

   public static int Vulcan_s(Object[] var0) {
      String var1 = (String)var0[0];
      return Vulcan_o(new Object[]{var1, new Integer(0)});
   }

   public static int Vulcan_o(Object[] var0) {
      String var2 = (String)var0[0];
      int var1 = (Integer)var0[1];

      try {
         return Integer.parseInt(var2);
      } catch (NumberFormatException var4) {
         return var1;
      }
   }

   public static Number Vulcan_u(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static boolean Vulcan_u(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static Float Vulcan_g(Object[] var0) {
      String var1 = (String)var0[0];
      return Float.valueOf(var1);
   }

   public static Double Vulcan_e(Object[] var0) {
      String var1 = (String)var0[0];
      return Double.valueOf(var1);
   }

   public static Integer Vulcan_p(Object[] var0) {
      String var1 = (String)var0[0];
      return Integer.decode(var1);
   }

   public static Long Vulcan_B(Object[] var0) {
      String var1 = (String)var0[0];
      return Long.valueOf(var1);
   }

   public static BigInteger Vulcan_V(Object[] var0) {
      String var1 = (String)var0[0];
      BigInteger var2 = new BigInteger(var1);
      return var2;
   }

   public static BigDecimal Vulcan_D(Object[] var0) {
      String var1 = (String)var0[0];
      BigDecimal var2 = new BigDecimal(var1);
      return var2;
   }

   public static long Vulcan_K(Object[] var0) {
      long var1 = (Long)var0[0];
      long var3 = (Long)var0[1];
      long var5 = (Long)var0[2];
      long var7 = (Long)var0[3];
      long var10000 = a ^ var7;
      String[] var9 = Vulcan_XL.Vulcan_v();

      int var12;
      label34: {
         label33: {
            try {
               long var13;
               var12 = (var13 = var3 - var1) == 0L ? 0 : (var13 < 0L ? -1 : 1);
               if (var9 == null) {
                  break label34;
               }

               if (var12 >= 0) {
                  break label33;
               }
            } catch (NumberFormatException var11) {
               throw a(var11);
            }

            var1 = var3;
         }

         try {
            var10000 = var5;
            if (var9 == null) {
               return var10000;
            }

            long var14;
            var12 = (var14 = var5 - var1) == 0L ? 0 : (var14 < 0L ? -1 : 1);
         } catch (NumberFormatException var10) {
            throw a(var10);
         }
      }

      if (var12 < 0) {
         var1 = var5;
      }

      var10000 = var1;
      return var10000;
   }

   public static int Vulcan_x(Object[] var0) {
      int var5 = (Integer)var0[0];
      int var2 = (Integer)var0[1];
      int var1 = (Integer)var0[2];
      long var3 = (Long)var0[3];
      long var10000 = a ^ var3;
      String[] var6 = Vulcan_XL.Vulcan_v();

      int var10001;
      int var9;
      label34: {
         label33: {
            try {
               var9 = var2;
               var10001 = var5;
               if (var6 == null) {
                  break label34;
               }

               if (var2 >= var5) {
                  break label33;
               }
            } catch (NumberFormatException var8) {
               throw a(var8);
            }

            var5 = var2;
         }

         try {
            var9 = var1;
            if (var6 == null) {
               return var9;
            }

            var10001 = var5;
         } catch (NumberFormatException var7) {
            throw a(var7);
         }
      }

      if (var9 < var10001) {
         var5 = var1;
      }

      var9 = var5;
      return var9;
   }

   public static long Vulcan_r(Object[] var0) {
      long var1 = (Long)var0[0];
      long var3 = (Long)var0[1];
      long var5 = (Long)var0[2];
      long var7 = (Long)var0[3];
      long var10000 = a ^ var7;
      String[] var9 = Vulcan_XL.Vulcan_v();

      int var12;
      label34: {
         label33: {
            try {
               long var13;
               var12 = (var13 = var3 - var1) == 0L ? 0 : (var13 < 0L ? -1 : 1);
               if (var9 == null) {
                  break label34;
               }

               if (var12 <= 0) {
                  break label33;
               }
            } catch (NumberFormatException var11) {
               throw a(var11);
            }

            var1 = var3;
         }

         try {
            var10000 = var5;
            if (var9 == null) {
               return var10000;
            }

            long var14;
            var12 = (var14 = var5 - var1) == 0L ? 0 : (var14 < 0L ? -1 : 1);
         } catch (NumberFormatException var10) {
            throw a(var10);
         }
      }

      if (var12 > 0) {
         var1 = var5;
      }

      var10000 = var1;
      return var10000;
   }

   public static int Vulcan_Q(Object[] var0) {
      long var4 = (Long)var0[0];
      int var2 = (Integer)var0[1];
      int var1 = (Integer)var0[2];
      int var3 = (Integer)var0[3];
      long var10000 = a ^ var4;
      String[] var6 = Vulcan_XL.Vulcan_v();

      int var10001;
      int var9;
      label34: {
         label33: {
            try {
               var9 = var1;
               var10001 = var2;
               if (var6 == null) {
                  break label34;
               }

               if (var1 <= var2) {
                  break label33;
               }
            } catch (NumberFormatException var8) {
               throw a(var8);
            }

            var2 = var1;
         }

         try {
            var9 = var3;
            if (var6 == null) {
               return var9;
            }

            var10001 = var2;
         } catch (NumberFormatException var7) {
            throw a(var7);
         }
      }

      if (var9 > var10001) {
         var2 = var3;
      }

      var9 = var2;
      return var9;
   }

   public static int Vulcan_e(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_q(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_o(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_e(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   static {
      String[] var5 = new String[6];
      int var3 = 0;
      String var2 = "Juo0\u0012byJ}<f\u001dad\u000e<re\u0011oh\u00182\u0019H><y\u000f-c\u0005h<q\\{l\u0006ux0\u0012x`\byn>\u0002Zd\u0003G,d";
      int var4 = "Juo0\u0012byJ}<f\u001dad\u000e<re\u0011oh\u00182\u0019H><y\u000f-c\u0005h<q\\{l\u0006ux0\u0012x`\byn>\u0002Zd\u0003G,d".length();
      char var1 = 23;
      int var0 = -1;

      while(true) {
         int var7;
         int var8;
         byte var10;
         byte var12;
         char[] var10001;
         char[] var10002;
         int var10003;
         char[] var10004;
         int var10005;
         char var22;
         byte var23;
         label83: {
            ++var0;
            var10002 = var2.substring(var0, var0 + var1).toCharArray();
            var10003 = var10002.length;
            var7 = 0;
            var10 = 82;
            var10001 = var10002;
            var8 = var10003;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 82;
               var10005 = var7;
            } else {
               var10 = 82;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label83;
               }

               var10004 = var10002;
               var12 = 82;
               var10005 = var7;
            }

            while(true) {
               var22 = var10004[var10005];
               switch(var7 % 7) {
               case 0:
                  var23 = 56;
                  break;
               case 1:
                  var23 = 78;
                  break;
               case 2:
                  var23 = 78;
                  break;
               case 3:
                  var23 = 66;
                  break;
               case 4:
                  var23 = 46;
                  break;
               case 5:
                  var23 = 95;
                  break;
               default:
                  var23 = 95;
               }

               var10004[var10005] = (char)(var22 ^ var12 ^ var23);
               ++var7;
               if (var10 == 0) {
                  var10005 = var10;
                  var10004 = var10001;
                  var12 = var10;
               } else {
                  if (var8 <= var7) {
                     break;
                  }

                  var10004 = var10001;
                  var12 = var10;
                  var10005 = var7;
               }
            }
         }

         var5[var3++] = (new String(var10001)).intern();
         if ((var0 += var1) >= var4) {
            var2 = "\u0013,6iK; \u0013$e?D8=We+<H61Ak\u0002\u001eh";
            var4 = "\u0013,6iK; \u0013$e?D8=We+<H61Ak\u0002\u001eh".length();
            var1 = 23;
            var0 = -1;

            while(true) {
               label62: {
                  ++var0;
                  var10002 = var2.substring(var0, var0 + var1).toCharArray();
                  var10003 = var10002.length;
                  var7 = 0;
                  var10 = 11;
                  var10001 = var10002;
                  var8 = var10003;
                  if (var10003 <= 1) {
                     var10004 = var10002;
                     var12 = 11;
                     var10005 = var7;
                  } else {
                     var10 = 11;
                     var8 = var10003;
                     if (var10003 <= var7) {
                        break label62;
                     }

                     var10004 = var10002;
                     var12 = 11;
                     var10005 = var7;
                  }

                  while(true) {
                     var22 = var10004[var10005];
                     switch(var7 % 7) {
                     case 0:
                        var23 = 56;
                        break;
                     case 1:
                        var23 = 78;
                        break;
                     case 2:
                        var23 = 78;
                        break;
                     case 3:
                        var23 = 66;
                        break;
                     case 4:
                        var23 = 46;
                        break;
                     case 5:
                        var23 = 95;
                        break;
                     default:
                        var23 = 95;
                     }

                     var10004[var10005] = (char)(var22 ^ var12 ^ var23);
                     ++var7;
                     if (var10 == 0) {
                        var10005 = var10;
                        var10004 = var10001;
                        var12 = var10;
                     } else {
                        if (var8 <= var7) {
                           break;
                        }

                        var10004 = var10001;
                        var12 = var10;
                        var10005 = var7;
                     }
                  }
               }

               var5[var3++] = (new String(var10001)).intern();
               if ((var0 += var1) >= var4) {
                  b = var5;
                  return;
               }

               var1 = var2.charAt(var0);
            }
         }

         var1 = var2.charAt(var0);
      }
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
