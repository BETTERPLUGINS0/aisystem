package ac.vulcan.anticheat;

import java.lang.reflect.Constructor;
import me.frep.vulcan.spigot.check.AbstractCheck;

public class Vulcan_eT {
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-91680364197522590L, -6857794461494836047L, (Object)null).a(17403945226867L);
   private static final String[] b;

   public static Object Vulcan_Y(Object[] var0) {
      Class var3 = (Class)var0[0];
      Object var4 = (Object)var0[1];
      long var1 = (Long)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 105852485437019L;
      return Vulcan_r(new Object[]{var3, new Object[]{var4}, new Long(var5)});
   }

   public static Object Vulcan_r(Object[] var0) {
      Class var1 = (Class)var0[0];
      Object[] var2 = (Object[])var0[1];
      long var3 = (Long)var0[2];
      var3 ^= a;
      long var5 = var3 ^ 139057207178163L;
      String var7 = Vulcan_N.Vulcan_V();
      if (null == var2) {
         var2 = Vulcan_XL.Vulcan_r;
      }

      Class[] var8 = new Class[var2.length];
      int var9 = 0;

      Object var10000;
      label37:
      while(var9 < var2.length) {
         while(true) {
            try {
               if (var3 > 0L) {
                  var10000 = var8;
                  if (var7 != null) {
                     return var10000;
                  }

                  var8[var9] = var2[var9].getClass();
                  ++var9;
               }

               if (var7 == null) {
                  continue;
               }
            } catch (NoSuchMethodException var10) {
               throw a(var10);
            }

            if (var3 > 0L) {
               break label37;
            }
         }
      }

      var10000 = Vulcan_q(new Object[]{var1, var2, var8, new Long(var5)});
      return var10000;
   }

   public static Object Vulcan_q(Object[] var0) {
      Class var4 = (Class)var0[0];
      Object var5 = (Object[])var0[1];
      Class[] var3 = (Class[])var0[2];
      long var1 = (Long)var0[3];
      var1 ^= a;
      long var10001 = var1 ^ 96133883965212L;
      long var6 = (var1 ^ 96133883965212L) >>> 16;
      int var8 = (int)(var10001 << 48 >>> 48);
      String var9 = Vulcan_N.Vulcan_V();

      Object var10000;
      label49: {
         label48: {
            try {
               var10000 = var3;
               if (var9 != null) {
                  break label49;
               }

               if (var3 != null) {
                  break label48;
               }
            } catch (NoSuchMethodException var13) {
               throw a(var13);
            }

            var3 = Vulcan_XL.Vulcan_o;
         }

         var10000 = var5;
      }

      label40: {
         label39: {
            try {
               if (var9 != null) {
                  break label39;
               }

               if (var10000 != null) {
                  break label40;
               }
            } catch (NoSuchMethodException var12) {
               throw a(var12);
            }

            var10000 = Vulcan_XL.Vulcan_r;
         }

         var5 = var10000;
      }

      Constructor var10 = Vulcan_i(new Object[]{new Long(var6), var4, var3, new Integer((short)var8)});

      try {
         if (null == var10) {
            throw new NoSuchMethodException(b[0] + var4.getName());
         }
      } catch (NoSuchMethodException var11) {
         throw a(var11);
      }

      return var10.newInstance((Object[])var5);
   }

   public static Object Vulcan_X(Object[] var0) {
      Class var4 = (Class)var0[0];
      Object var3 = (Object)var0[1];
      long var1 = (Long)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 101364970912755L;
      return Vulcan_i(new Object[]{var4, new Object[]{var3}, new Long(var5)});
   }

   public static Object Vulcan_i(Object[] var0) {
      Class var1 = (Class)var0[0];
      Object[] var2 = (Object[])var0[1];
      long var3 = (Long)var0[2];
      var3 ^= a;
      long var5 = var3 ^ 58816361178524L;
      String var7 = Vulcan_N.Vulcan_V();
      if (null == var2) {
         var2 = Vulcan_XL.Vulcan_r;
      }

      int var9 = var2.length;
      Class[] var10 = new Class[var9];
      int var11 = 0;

      Object var10000;
      label38:
      while(var11 < var9) {
         do {
            try {
               if (var3 > 0L) {
                  var10000 = var10;
                  if (var7 != null) {
                     return var10000;
                  }

                  var10[var11] = var2[var11].getClass();
                  ++var11;
               }

               if (var7 == null) {
                  continue label38;
               }
            } catch (NoSuchMethodException var12) {
               throw a(var12);
            }
         } while(var3 < 0L);

         int var8 = AbstractCheck.Vulcan_m();
         ++var8;
         AbstractCheck.Vulcan_H(var8);
         break;
      }

      var10000 = Vulcan_a(new Object[]{var1, var2, var10, new Long(var5)});
      return var10000;
   }

   public static Object Vulcan_a(Object[] var0) {
      Class var4 = (Class)var0[0];
      Object[] var5 = (Object[])var0[1];
      Class[] var3 = (Class[])var0[2];
      long var1 = (Long)var0[3];
      var1 ^= a;
      long var6 = var1 ^ 9026287603441L;
      String var8 = Vulcan_N.Vulcan_V();

      label49: {
         Object[] var10000;
         label48: {
            try {
               var10000 = var5;
               if (var8 != null) {
                  break label48;
               }

               if (var5 != null) {
                  break label49;
               }
            } catch (NoSuchMethodException var12) {
               throw a(var12);
            }

            var10000 = Vulcan_XL.Vulcan_r;
         }

         var5 = var10000;
      }

      label40: {
         Class[] var13;
         label39: {
            try {
               var13 = var3;
               if (var8 != null) {
                  break label39;
               }

               if (var3 != null) {
                  break label40;
               }
            } catch (NoSuchMethodException var11) {
               throw a(var11);
            }

            var13 = Vulcan_XL.Vulcan_o;
         }

         var3 = var13;
      }

      Constructor var9 = Vulcan_r(new Object[]{var4, new Long(var6), var3});

      try {
         if (null == var9) {
            throw new NoSuchMethodException(b[1] + var4.getName());
         }
      } catch (NoSuchMethodException var10) {
         throw a(var10);
      }

      return var9.newInstance(var5);
   }

   public static Constructor Vulcan_N(Object[] var0) {
      long var3 = (Long)var0[0];
      Class var2 = (Class)var0[1];
      Class var1 = (Class)var0[2];
      var3 ^= a;
      long var5 = var3 ^ 91644994407559L;
      return Vulcan_r(new Object[]{var2, new Long(var5), new Class[]{var1}});
   }

   public static Constructor Vulcan_r(Object[] var0) {
      Class var4 = (Class)var0[0];
      long var1 = (Long)var0[1];
      Class[] var3 = (Class[])var0[2];
      var1 ^= a;
      long var5 = var1 ^ 14020532684562L;

      try {
         return Vulcan_g(new Object[]{new Long(var5), var4.getConstructor(var3)});
      } catch (NoSuchMethodException var8) {
         return null;
      }
   }

   public static Constructor Vulcan_g(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static Constructor Vulcan_i(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   static {
      String[] var5 = new String[2];
      int var3 = 0;
      String var2 = "Do\u0014ig/;*aWyw? cbX\u007f2/<ds@hg/'er\u0014u|l<hjQyfvs*Do\u0014ig/;*aWyw? cbX\u007f2/<ds@hg/'er\u0014u|l<hjQyfvs";
      int var4 = "Do\u0014ig/;*aWyw? cbX\u007f2/<ds@hg/'er\u0014u|l<hjQyfvs*Do\u0014ig/;*aWyw? cbX\u007f2/<ds@hg/'er\u0014u|l<hjQyfvs".length();
      char var1 = '*';
      int var0 = -1;

      while(true) {
         char[] var10001;
         label41: {
            ++var0;
            char[] var10002 = var2.substring(var0, var0 + var1).toCharArray();
            int var10003 = var10002.length;
            int var7 = 0;
            byte var10 = 3;
            var10001 = var10002;
            int var8 = var10003;
            byte var12;
            char[] var10004;
            int var10005;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 3;
               var10005 = var7;
            } else {
               var10 = 3;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label41;
               }

               var10004 = var10002;
               var12 = 3;
               var10005 = var7;
            }

            while(true) {
               char var22 = var10004[var10005];
               byte var23;
               switch(var7 % 7) {
               case 0:
                  var23 = 9;
                  break;
               case 1:
                  var23 = 3;
                  break;
               case 2:
                  var23 = 55;
                  break;
               case 3:
                  var23 = 25;
                  break;
               case 4:
                  var23 = 17;
                  break;
               case 5:
                  var23 = 79;
                  break;
               default:
                  var23 = 80;
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

   private static Exception a(Exception var0) {
      return var0;
   }
}
