package ac.vulcan.anticheat;

import java.lang.reflect.Method;

abstract class Vulcan_Xm {
   private static final int Vulcan_l = 7;
   private static final Method Vulcan_v;
   private static final Class[] Vulcan_n;
   // $FF: synthetic field
   static Class Vulcan_m;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-8822352846244622098L, 3282527355003527255L, (Object)null).a(89324284371598L);

   static void Vulcan_i(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   static boolean Vulcan_q(Object[] var0) {
      long var1 = (Long)var0[0];
      int var3 = (Integer)var0[1];
      long var10000 = a ^ var1;
      String var4 = Vulcan_N.Vulcan_V();

      int var6;
      label32: {
         try {
            var6 = var3 & 7;
            if (var4 != null) {
               return (boolean)var6;
            }

            if (var6 == 0) {
               break label32;
            }
         } catch (SecurityException var5) {
            throw a(var5);
         }

         var6 = 0;
         return (boolean)var6;
      }

      var6 = 1;
      return (boolean)var6;
   }

   static boolean Vulcan_z(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   static boolean Vulcan_o(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   static int Vulcan_R(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static float Vulcan_X(Object[] var0) {
      Class[] var1 = (Class[])var0[0];
      long var2 = (Long)var0[1];
      Class[] var4 = (Class[])var0[2];
      var2 ^= a;
      long var5 = var2 ^ 18363500915028L;
      String var10000 = Vulcan_N.Vulcan_V();
      float var8 = 0.0F;
      String var7 = var10000;
      int var9 = 0;

      float var12;
      while(true) {
         if (var9 < var1.length) {
            Class var10 = var1[var9];
            Class var11 = var4[var9];
            if (var2 > 0L) {
               var12 = var8 + Vulcan_m(new Object[]{var10, var11, new Long(var5)});
               if (var7 != null) {
                  break;
               }

               var8 = var12;
               ++var9;
            }

            if (var7 == null) {
               continue;
            }
         }

         var12 = var8;
         break;
      }

      return var12;
   }

   private static float Vulcan_m(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static float Vulcan_J(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   static Class Vulcan_b(Object[] var0) {
      String var1 = (String)var0[0];

      try {
         return Class.forName(me.frep.vulcan.spigot.Vulcan_s.a(var1));
      } catch (ClassNotFoundException var3) {
         throw new NoClassDefFoundError(var3.getMessage());
      }
   }

   static {
      long var9 = a ^ 66714154899448L;
      long var11 = var9 ^ 126940866717382L;
      String[] var6 = new String[2];
      int var4 = 0;
      String var3 = "G\u0014m'ev\u000fC\u001254.|\u0002H\u0016oh\u0006\u007f\u0003O\u0010i\u000bD\u0006H?%n\u0006H\u0001r%";
      int var5 = "G\u0014m'ev\u000fC\u001254.|\u0002H\u0016oh\u0006\u007f\u0003O\u0010i\u000bD\u0006H?%n\u0006H\u0001r%".length();
      char var2 = 24;
      int var1 = -1;

      while(true) {
         char[] var10001;
         label63: {
            ++var1;
            char[] var10002 = var3.substring(var1, var1 + var2).toCharArray();
            int var10003 = var10002.length;
            int var8 = 0;
            byte var20 = 59;
            var10001 = var10002;
            int var17 = var10003;
            char[] var10004;
            int var10005;
            byte var22;
            if (var10003 <= 1) {
               var10004 = var10002;
               var22 = 59;
               var10005 = var8;
            } else {
               var20 = 59;
               var17 = var10003;
               if (var10003 <= var8) {
                  break label63;
               }

               var10004 = var10002;
               var22 = 59;
               var10005 = var8;
            }

            while(true) {
               char var32 = var10004[var10005];
               byte var33;
               switch(var8 % 7) {
               case 0:
                  var33 = 22;
                  break;
               case 1:
                  var33 = 78;
                  break;
               case 2:
                  var33 = 32;
                  break;
               case 3:
                  var33 = 125;
                  break;
               case 4:
                  var33 = 112;
                  break;
               case 5:
                  var33 = 33;
                  break;
               default:
                  var33 = 85;
               }

               var10004[var10005] = (char)(var32 ^ var22 ^ var33);
               ++var8;
               if (var20 == 0) {
                  var10005 = var20;
                  var10004 = var10001;
                  var22 = var20;
               } else {
                  if (var17 <= var8) {
                     break;
                  }

                  var10004 = var10001;
                  var22 = var20;
                  var10005 = var8;
               }
            }
         }

         var6[var4++] = (new String(var10001)).intern();
         if ((var1 += var2) >= var5) {
            String[] var0 = var6;
            Method var13 = null;
            if (Vulcan_X5.Vulcan_R(new Object[]{new Long(var11), new Boolean(1.5F)})) {
               try {
                  Class var19;
                  label41: {
                     try {
                        if (Vulcan_m == null) {
                           var19 = Vulcan_m = Vulcan_b(new Object[]{var0[0]});
                           break label41;
                        }
                     } catch (Exception var15) {
                        throw a(var15);
                     }

                     var19 = Vulcan_m;
                  }

                  var13 = var19.getMethod(var0[1], Vulcan_XL.Vulcan_o);
               } catch (Exception var16) {
               }
            }

            Vulcan_v = var13;
            Vulcan_n = new Class[]{Byte.TYPE, Short.TYPE, Character.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE};
            return;
         }

         var2 = var3.charAt(var1);
      }
   }

   private static Exception a(Exception var0) {
      return var0;
   }
}
