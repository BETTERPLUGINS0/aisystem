package ac.vulcan.anticheat;

public class Vulcan_el {
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(3224443167404041718L, 7406595104932749504L, (Object)null).a(263159711098454L);
   private static final String[] b;

   public static double Vulcan_i(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static float Vulcan_U(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static double Vulcan_t(Object[] var0) {
      long var1 = (Long)var0[0];
      double var3 = (Double)var0[1];
      double var5 = (Double)var0[2];
      double var7 = (Double)var0[3];
      var1 ^= a;
      long var9 = var1 ^ 4081192563783L;
      return Vulcan_A(new Object[]{new Double(Vulcan_A(new Object[]{new Double(var3), new Long(var9), new Double(var5)})), new Long(var9), new Double(var7)});
   }

   public static double Vulcan_A(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static float Vulcan_v(Object[] var0) {
      float var5 = (Float)var0[0];
      long var3 = (Long)var0[1];
      float var2 = (Float)var0[2];
      float var1 = (Float)var0[3];
      var3 ^= a;
      long var6 = var3 ^ 113178103194392L;
      return Vulcan_W(new Object[]{new Boolean(Vulcan_W(new Object[]{new Boolean(var5), new Boolean(var2), new Long(var6)})), new Boolean(var1), new Long(var6)});
   }

   public static float Vulcan_W(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static double Vulcan_I(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static float Vulcan_H(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static double Vulcan_d(Object[] var0) {
      double var1 = (Double)var0[0];
      long var3 = (Long)var0[1];
      double var5 = (Double)var0[2];
      double var7 = (Double)var0[3];
      var3 ^= a;
      long var9 = var3 ^ 109290799854493L;
      return Vulcan_w(new Object[]{new Long(var9), new Double(Vulcan_w(new Object[]{new Long(var9), new Double(var1), new Double(var5)})), new Double(var7)});
   }

   public static double Vulcan_w(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static float Vulcan_E(Object[] var0) {
      float var1 = (Float)var0[0];
      float var4 = (Float)var0[1];
      float var5 = (Float)var0[2];
      long var2 = (Long)var0[3];
      var2 ^= a;
      long var6 = var2 ^ 63314031868189L;
      return Vulcan_t(new Object[]{new Boolean(Vulcan_t(new Object[]{new Boolean(var1), new Boolean(var4), new Long(var6)})), new Boolean(var5), new Long(var6)});
   }

   public static float Vulcan_t(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   static {
      String[] var5 = new String[4];
      int var3 = 0;
      String var2 = "}|B%\u0013F0Hm\u0007h'G6\tzHqrV'\tzRi>\u0016hfUd+\u0014!HzIj&\u0014 L4Bh\"@;\u0007";
      int var4 = "}|B%\u0013F0Hm\u0007h'G6\tzHqrV'\tzRi>\u0016hfUd+\u0014!HzIj&\u0014 L4Bh\"@;\u0007".length();
      char var1 = 26;
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
            var10 = 38;
            var10001 = var10002;
            var8 = var10003;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 38;
               var10005 = var7;
            } else {
               var10 = 38;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label83;
               }

               var10004 = var10002;
               var12 = 38;
               var10005 = var7;
            }

            while(true) {
               var22 = var10004[var10005];
               switch(var7 % 7) {
               case 0:
                  var23 = 15;
                  break;
               case 1:
                  var23 = 50;
                  break;
               case 2:
                  var23 = 1;
                  break;
               case 3:
                  var23 = 35;
                  break;
               case 4:
                  var23 = 116;
                  break;
               case 5:
                  var23 = 18;
                  break;
               default:
                  var23 = 100;
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
            var2 = "\u0019\u0018&Aw\"T,\tc\fC#Rm\u001e,\u0015\u00162Cm\u001e6\rZ\u0016\f\u00021\u0000OpE,\u001e-\u000eBpD(P&\fF$_c";
            var4 = "\u0019\u0018&Aw\"T,\tc\fC#Rm\u001e,\u0015\u00162Cm\u001e6\rZ\u0016\f\u00021\u0000OpE,\u001e-\u000eBpD(P&\fF$_c".length();
            var1 = 26;
            var0 = -1;

            while(true) {
               label62: {
                  ++var0;
                  var10002 = var2.substring(var0, var0 + var1).toCharArray();
                  var10003 = var10002.length;
                  var7 = 0;
                  var10 = 66;
                  var10001 = var10002;
                  var8 = var10003;
                  if (var10003 <= 1) {
                     var10004 = var10002;
                     var12 = 66;
                     var10005 = var7;
                  } else {
                     var10 = 66;
                     var8 = var10003;
                     if (var10003 <= var7) {
                        break label62;
                     }

                     var10004 = var10002;
                     var12 = 66;
                     var10005 = var7;
                  }

                  while(true) {
                     var22 = var10004[var10005];
                     switch(var7 % 7) {
                     case 0:
                        var23 = 15;
                        break;
                     case 1:
                        var23 = 50;
                        break;
                     case 2:
                        var23 = 1;
                        break;
                     case 3:
                        var23 = 35;
                        break;
                     case 4:
                        var23 = 116;
                        break;
                     case 5:
                        var23 = 18;
                        break;
                     default:
                        var23 = 100;
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

   private static IllegalArgumentException a(IllegalArgumentException var0) {
      return var0;
   }
}
