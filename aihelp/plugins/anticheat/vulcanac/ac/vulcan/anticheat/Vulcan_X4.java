package ac.vulcan.anticheat;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

public class Vulcan_X4 {
   public static final TimeZone Vulcan_m;
   public static final long Vulcan_j = 1000L;
   public static final long Vulcan_Z = 60000L;
   public static final long Vulcan__ = 3600000L;
   public static final long Vulcan_R = 86400000L;
   public static final int Vulcan_g = 1001;
   private static final int[][] Vulcan_f;
   public static final int Vulcan_b = 1;
   public static final int Vulcan_k = 2;
   public static final int Vulcan_O = 3;
   public static final int Vulcan_Y = 4;
   public static final int Vulcan_V = 5;
   public static final int Vulcan_e = 6;
   private static final int Vulcan_y = 0;
   private static final int Vulcan_D = 1;
   private static final int Vulcan_p = 2;
   /** @deprecated */
   public static final int Vulcan_d = 1000;
   /** @deprecated */
   public static final int Vulcan_X = 60000;
   /** @deprecated */
   public static final int Vulcan_u = 3600000;
   /** @deprecated */
   public static final int Vulcan_w = 86400000;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(2703175581320164688L, 2689188822505505845L, (Object)null).a(109460306878292L);
   private static final String[] b;

   public static boolean Vulcan_p(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_s(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_z(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_S(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_T(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static Date Vulcan_H(Object[] var0) {
      long var2 = (Long)var0[0];
      String var1 = (String)var0[1];
      String[] var4 = (String[])var0[2];
      var2 ^= a;
      long var5 = var2 ^ 880942792406L;
      return Vulcan_f(new Object[]{var1, var4, new Boolean(true), new Long(var5)});
   }

   public static Date Vulcan_F(Object[] var0) {
      long var2 = (Long)var0[0];
      String var1 = (String)var0[1];
      String[] var4 = (String[])var0[2];
      var2 ^= a;
      long var5 = var2 ^ 72939514810799L;
      return Vulcan_f(new Object[]{var1, var4, new Boolean(false), new Long(var5)});
   }

   private static Date Vulcan_f(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static int Vulcan_y(Object[] var0) {
      long var1 = (Long)var0[0];
      String var3 = (String)var0[1];
      int var4 = (Integer)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 110488288883338L;
      String[] var10000 = Vulcan_i6.Vulcan_x();
      int var8 = Vulcan_Xt.Vulcan_Q(new Object[]{var3, new Integer(43), new Long(var5), new Integer(var4)});
      String[] var7 = var10000;

      int var10;
      label20: {
         try {
            var10 = var8;
            if (var7 != null) {
               return var10;
            }

            if (var8 >= 0) {
               break label20;
            }
         } catch (IllegalArgumentException var9) {
            throw a(var9);
         }

         var8 = Vulcan_Xt.Vulcan_Q(new Object[]{var3, new Integer(45), new Long(var5), new Integer(var4)});
      }

      var10 = var8;
      return var10;
   }

   private static String Vulcan_n(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static Date Vulcan_t(Object[] var0) {
      Date var1 = (Date)var0[0];
      long var2 = (Long)var0[1];
      int var4 = (Integer)var0[2];
      var2 ^= a;
      long var5 = var2 ^ 78045758200354L;
      return Vulcan_i(new Object[]{var1, new Long(var5), new Integer(1), new Integer(var4)});
   }

   public static Date Vulcan_j(Object[] var0) {
      Date var2 = (Date)var0[0];
      int var1 = (Integer)var0[1];
      long var3 = (Long)var0[2];
      var3 ^= a;
      long var5 = var3 ^ 26829642086230L;
      return Vulcan_i(new Object[]{var2, new Long(var5), new Integer(2), new Integer(var1)});
   }

   public static Date Vulcan_h(Object[] var0) {
      long var1 = (Long)var0[0];
      Date var3 = (Date)var0[1];
      int var4 = (Integer)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 32234462469655L;
      return Vulcan_i(new Object[]{var3, new Long(var5), new Integer(3), new Integer(var4)});
   }

   public static Date Vulcan_W(Object[] var0) {
      Date var1 = (Date)var0[0];
      int var4 = (Integer)var0[1];
      long var2 = (Long)var0[2];
      var2 ^= a;
      long var5 = var2 ^ 120630985726017L;
      return Vulcan_i(new Object[]{var1, new Long(var5), new Integer(5), new Integer(var4)});
   }

   public static Date Vulcan_n(Object[] var0) {
      Date var2 = (Date)var0[0];
      int var1 = (Integer)var0[1];
      long var3 = (Long)var0[2];
      var3 ^= a;
      long var5 = var3 ^ 21042500643824L;
      return Vulcan_i(new Object[]{var2, new Long(var5), new Integer(11), new Integer(var1)});
   }

   public static Date Vulcan_p(Object[] var0) {
      long var2 = (Long)var0[0];
      Date var4 = (Date)var0[1];
      int var1 = (Integer)var0[2];
      var2 ^= a;
      long var5 = var2 ^ 93763552149190L;
      return Vulcan_i(new Object[]{var4, new Long(var5), new Integer(12), new Integer(var1)});
   }

   public static Date Vulcan_N(Object[] var0) {
      Date var4 = (Date)var0[0];
      long var2 = (Long)var0[1];
      int var1 = (Integer)var0[2];
      var2 ^= a;
      long var5 = var2 ^ 13795681835439L;
      return Vulcan_i(new Object[]{var4, new Long(var5), new Integer(13), new Integer(var1)});
   }

   public static Date Vulcan_g(Object[] var0) {
      Date var2 = (Date)var0[0];
      long var3 = (Long)var0[1];
      int var1 = (Integer)var0[2];
      var3 ^= a;
      long var5 = var3 ^ 82965907833017L;
      return Vulcan_i(new Object[]{var2, new Long(var5), new Integer(14), new Integer(var1)});
   }

   /** @deprecated */
   public static Date Vulcan_i(Object[] var0) {
      Date var4 = (Date)var0[0];
      long var2 = (Long)var0[1];
      int var5 = (Integer)var0[2];
      int var1 = (Integer)var0[3];
      long var10000 = a ^ var2;

      try {
         if (var4 == null) {
            throw new IllegalArgumentException(b[4]);
         }
      } catch (IllegalArgumentException var7) {
         throw a(var7);
      }

      Calendar var6 = Calendar.getInstance();
      var6.setTime(var4);
      var6.add(var5, var1);
      return var6.getTime();
   }

   public static Date Vulcan_J(Object[] var0) {
      long var2 = (Long)var0[0];
      Date var4 = (Date)var0[1];
      int var1 = (Integer)var0[2];
      var2 ^= a;
      long var5 = var2 ^ 15418370689444L;
      return Vulcan_k(new Object[]{var4, new Long(var5), new Integer(1), new Integer(var1)});
   }

   public static Date Vulcan_U(Object[] var0) {
      long var3 = (Long)var0[0];
      Date var1 = (Date)var0[1];
      int var2 = (Integer)var0[2];
      var3 ^= a;
      long var5 = var3 ^ 100517511832693L;
      return Vulcan_k(new Object[]{var1, new Long(var5), new Integer(2), new Integer(var2)});
   }

   public static Date Vulcan_y(Object[] var0) {
      Date var1 = (Date)var0[0];
      int var4 = (Integer)var0[1];
      long var2 = (Long)var0[2];
      var2 ^= a;
      long var5 = var2 ^ 56978567805209L;
      return Vulcan_k(new Object[]{var1, new Long(var5), new Integer(5), new Integer(var4)});
   }

   public static Date Vulcan_Y(Object[] var0) {
      Date var3 = (Date)var0[0];
      int var4 = (Integer)var0[1];
      long var1 = (Long)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 137557683312113L;
      return Vulcan_k(new Object[]{var3, new Long(var5), new Integer(11), new Integer(var4)});
   }

   public static Date Vulcan_x(Object[] var0) {
      Date var4 = (Date)var0[0];
      int var1 = (Integer)var0[1];
      long var2 = (Long)var0[2];
      var2 ^= a;
      long var5 = var2 ^ 627326182307L;
      return Vulcan_k(new Object[]{var4, new Long(var5), new Integer(12), new Integer(var1)});
   }

   public static Date Vulcan_E(Object[] var0) {
      Date var3 = (Date)var0[0];
      int var4 = (Integer)var0[1];
      long var1 = (Long)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 16644519388034L;
      return Vulcan_k(new Object[]{var3, new Long(var5), new Integer(13), new Integer(var4)});
   }

   public static Date Vulcan_b(Object[] var0) {
      long var3 = (Long)var0[0];
      Date var1 = (Date)var0[1];
      int var2 = (Integer)var0[2];
      var3 ^= a;
      long var5 = var3 ^ 7412987415486L;
      return Vulcan_k(new Object[]{var1, new Long(var5), new Integer(14), new Integer(var2)});
   }

   private static Date Vulcan_k(Object[] var0) {
      Date var3 = (Date)var0[0];
      long var4 = (Long)var0[1];
      int var1 = (Integer)var0[2];
      int var2 = (Integer)var0[3];
      long var10000 = a ^ var4;

      try {
         if (var3 == null) {
            throw new IllegalArgumentException(b[4]);
         }
      } catch (IllegalArgumentException var7) {
         throw a(var7);
      }

      Calendar var6 = Calendar.getInstance();
      var6.setLenient(false);
      var6.setTime(var3);
      var6.set(var1, var2);
      return var6.getTime();
   }

   public static Calendar Vulcan_D(Object[] var0) {
      Date var1 = (Date)var0[0];
      Calendar var2 = Calendar.getInstance();
      var2.setTime(var1);
      return var2;
   }

   public static Date Vulcan_G(Object[] var0) {
      long var2 = (Long)var0[0];
      Date var4 = (Date)var0[1];
      int var1 = (Integer)var0[2];
      var2 ^= a;
      long var5 = var2 ^ 72128260374099L;

      try {
         if (var4 == null) {
            throw new IllegalArgumentException(b[4]);
         }
      } catch (IllegalArgumentException var8) {
         throw a(var8);
      }

      Calendar var7 = Calendar.getInstance();
      var7.setTime(var4);
      Vulcan_y(new Object[]{var7, new Long(var5), new Integer(var1), new Integer(1)});
      return var7.getTime();
   }

   public static Calendar Vulcan_g(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static Date Vulcan_I(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static Date Vulcan_C(Object[] var0) {
      long var1 = (Long)var0[0];
      Date var4 = (Date)var0[1];
      int var3 = (Integer)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 99710088812442L;

      try {
         if (var4 == null) {
            throw new IllegalArgumentException(b[4]);
         }
      } catch (IllegalArgumentException var8) {
         throw a(var8);
      }

      Calendar var7 = Calendar.getInstance();
      var7.setTime(var4);
      Vulcan_y(new Object[]{var7, new Long(var5), new Integer(var3), new Integer(0)});
      return var7.getTime();
   }

   public static Calendar Vulcan_X(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static Date Vulcan_A(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static Date Vulcan_z(Object[] var0) {
      Date var4 = (Date)var0[0];
      int var3 = (Integer)var0[1];
      long var1 = (Long)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 37716795047661L;

      try {
         if (var4 == null) {
            throw new IllegalArgumentException(b[4]);
         }
      } catch (IllegalArgumentException var8) {
         throw a(var8);
      }

      Calendar var7 = Calendar.getInstance();
      var7.setTime(var4);
      Vulcan_y(new Object[]{var7, new Long(var5), new Integer(var3), new Integer(2)});
      return var7.getTime();
   }

   public static Calendar Vulcan_k(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static Date Vulcan__(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static void Vulcan_y(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static Iterator Vulcan_s(Object[] var0) {
      Date var3 = (Date)var0[0];
      int var4 = (Integer)var0[1];
      long var1 = (Long)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 135291826171796L;

      try {
         if (var3 == null) {
            throw new IllegalArgumentException(b[4]);
         }
      } catch (IllegalArgumentException var8) {
         throw a(var8);
      }

      Calendar var7 = Calendar.getInstance();
      var7.setTime(var3);
      return Vulcan_D(new Object[]{var7, new Integer(var4), new Long(var5)});
   }

   public static Iterator Vulcan_D(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static Iterator Vulcan_n(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static long Vulcan_W(Object[] var0) {
      Date var1 = (Date)var0[0];
      long var3 = (Long)var0[1];
      int var2 = (Integer)var0[2];
      var3 ^= a;
      long var5 = var3 ^ 123692986735493L;
      return Vulcan_R(new Object[]{var1, new Integer(var2), new Integer(14), new Long(var5)});
   }

   public static long Vulcan_t(Object[] var0) {
      Date var4 = (Date)var0[0];
      long var1 = (Long)var0[1];
      int var3 = (Integer)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 43287883022707L;
      return Vulcan_R(new Object[]{var4, new Integer(var3), new Integer(13), new Long(var5)});
   }

   public static long Vulcan_c(Object[] var0) {
      Date var4 = (Date)var0[0];
      long var2 = (Long)var0[1];
      int var1 = (Integer)var0[2];
      var2 ^= a;
      long var5 = var2 ^ 8378237961237L;
      return Vulcan_R(new Object[]{var4, new Integer(var1), new Integer(12), new Long(var5)});
   }

   public static long Vulcan_H(Object[] var0) {
      Date var2 = (Date)var0[0];
      int var1 = (Integer)var0[1];
      long var3 = (Long)var0[2];
      var3 ^= a;
      long var5 = var3 ^ 103612895582729L;
      return Vulcan_R(new Object[]{var2, new Integer(var1), new Integer(11), new Long(var5)});
   }

   public static long Vulcan_x(Object[] var0) {
      Date var2 = (Date)var0[0];
      long var3 = (Long)var0[1];
      int var1 = (Integer)var0[2];
      var3 ^= a;
      long var5 = var3 ^ 102274269125488L;
      return Vulcan_R(new Object[]{var2, new Integer(var1), new Integer(6), new Long(var5)});
   }

   public static long Vulcan_J(Object[] var0) {
      Calendar var4 = (Calendar)var0[0];
      int var3 = (Integer)var0[1];
      long var1 = (Long)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 78234300704874L;
      return Vulcan_L(new Object[]{var4, new Long(var5), new Integer(var3), new Integer(14)});
   }

   public static long Vulcan_p(Object[] var0) {
      long var2 = (Long)var0[0];
      Calendar var4 = (Calendar)var0[1];
      int var1 = (Integer)var0[2];
      var2 ^= a;
      long var5 = var2 ^ 59201052033384L;
      return Vulcan_L(new Object[]{var4, new Long(var5), new Integer(var1), new Integer(13)});
   }

   public static long Vulcan_s(Object[] var0) {
      Calendar var2 = (Calendar)var0[0];
      long var3 = (Long)var0[1];
      int var1 = (Integer)var0[2];
      var3 ^= a;
      long var5 = var3 ^ 59383972153005L;
      return Vulcan_L(new Object[]{var2, new Long(var5), new Integer(var1), new Integer(12)});
   }

   public static long Vulcan_C(Object[] var0) {
      Calendar var4 = (Calendar)var0[0];
      long var1 = (Long)var0[1];
      int var3 = (Integer)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 29834487130481L;
      return Vulcan_L(new Object[]{var4, new Long(var5), new Integer(var3), new Integer(11)});
   }

   public static long Vulcan_Y(Object[] var0) {
      Calendar var3 = (Calendar)var0[0];
      int var4 = (Integer)var0[1];
      long var1 = (Long)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 26214395256850L;
      return Vulcan_L(new Object[]{var3, new Long(var5), new Integer(var4), new Integer(6)});
   }

   private static long Vulcan_R(Object[] var0) {
      Date var2 = (Date)var0[0];
      int var1 = (Integer)var0[1];
      int var5 = (Integer)var0[2];
      long var3 = (Long)var0[3];
      var3 ^= a;
      long var6 = var3 ^ 15650666441689L;

      try {
         if (var2 == null) {
            throw new IllegalArgumentException(b[4]);
         }
      } catch (IllegalArgumentException var9) {
         throw a(var9);
      }

      Calendar var8 = Calendar.getInstance();
      var8.setTime(var2);
      return Vulcan_L(new Object[]{var8, new Long(var6), new Integer(var1), new Integer(var5)});
   }

   private static long Vulcan_L(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_Z(Object[] var0) {
      Calendar var3 = (Calendar)var0[0];
      long var1 = (Long)var0[1];
      Calendar var5 = (Calendar)var0[2];
      int var4 = (Integer)var0[3];
      var1 ^= a;
      long var6 = var1 ^ 122760633580099L;
      String[] var8 = Vulcan_i6.Vulcan_x();

      int var10000;
      label32: {
         try {
            var10000 = Vulcan_n(new Object[]{var3, new Long(var6), var5, new Integer(var4)});
            if (var8 != null) {
               return (boolean)var10000;
            }

            if (var10000 == 0) {
               break label32;
            }
         } catch (IllegalArgumentException var9) {
            throw a(var9);
         }

         var10000 = 0;
         return (boolean)var10000;
      }

      var10000 = 1;
      return (boolean)var10000;
   }

   public static boolean Vulcan_X(Object[] var0) {
      long var3 = (Long)var0[0];
      Date var1 = (Date)var0[1];
      Date var5 = (Date)var0[2];
      int var2 = (Integer)var0[3];
      var3 ^= a;
      long var6 = var3 ^ 140314149015749L;
      String[] var8 = Vulcan_i6.Vulcan_x();

      int var10000;
      label32: {
         try {
            var10000 = Vulcan_j(new Object[]{var1, var5, new Integer(var2), new Long(var6)});
            if (var8 != null) {
               return (boolean)var10000;
            }

            if (var10000 == 0) {
               break label32;
            }
         } catch (IllegalArgumentException var9) {
            throw a(var9);
         }

         var10000 = 0;
         return (boolean)var10000;
      }

      var10000 = 1;
      return (boolean)var10000;
   }

   public static int Vulcan_n(Object[] var0) {
      Calendar var4 = (Calendar)var0[0];
      long var1 = (Long)var0[1];
      Calendar var3 = (Calendar)var0[2];
      int var5 = (Integer)var0[3];
      var1 ^= a;
      long var6 = var1 ^ 5622193132230L;
      Calendar var8 = Vulcan_X(new Object[]{var4, new Integer(var5), new Long(var6)});
      Calendar var9 = Vulcan_X(new Object[]{var3, new Integer(var5), new Long(var6)});
      return var8.getTime().compareTo(var9.getTime());
   }

   public static int Vulcan_j(Object[] var0) {
      Date var5 = (Date)var0[0];
      Date var1 = (Date)var0[1];
      int var4 = (Integer)var0[2];
      long var2 = (Long)var0[3];
      var2 ^= a;
      long var6 = var2 ^ 109195179183862L;
      Date var8 = Vulcan_C(new Object[]{new Long(var6), var5, new Integer(var4)});
      Date var9 = Vulcan_C(new Object[]{new Long(var6), var1, new Integer(var4)});
      return var8.compareTo(var9);
   }

   private static long Vulcan_N(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   static {
      String[] var5 = new String[20];
      int var3 = 0;
      String var2 = "?1sG{\r5?+u\u0017e\r3k=d\u0002E\u0002\u0003X\u0015T\rK0eGs\u0010 x5e\taB\u0019K0eGq\u00035zxm\u0012f\u0016aq7tGw\u0007aq-l\u000b\u000e?1sG{\r5?.a\u000b|\u0006o\nK0eGs\u000b$s< $\\7u\u000bqB/p, \u0001|\f%?;e\u000ey\u000b/xxo\u00015\u0004.mxt\u001ee\u0007{?\u001aJ6a\u0005y\u0007ak7 \u0017t\u00102zxt\u000fpB%~,e]5\u0010K0eGg\u0003/x= \u0014a\u001b-zx\u0002E\u0002\u001b\\7u\u000bqB/p, \u000ea\u00073~,eGw\u00032z< \b{B\u0011?1sG{\r5?+u\u0017e\r3k=d\u0019K0eGq\u00035zxm\u0012f\u0016aq7tGw\u0007aq-l\u000b\u0010\\7u\u000bqB/p, \u0015z\u0017/{x\u0013\\7u\u000bqB/p, \u0013g\u0017/|9t\u00025\tK0eG`\f(kx&?;a\t{\r5?:eGg\u00071m=s\u0002{\u0016${xi\u00145\u000f(s4e\u0014p\u0001.q<s";
      int var4 = "?1sG{\r5?+u\u0017e\r3k=d\u0002E\u0002\u0003X\u0015T\rK0eGs\u0010 x5e\taB\u0019K0eGq\u00035zxm\u0012f\u0016aq7tGw\u0007aq-l\u000b\u000e?1sG{\r5?.a\u000b|\u0006o\nK0eGs\u000b$s< $\\7u\u000bqB/p, \u0001|\f%?;e\u000ey\u000b/xxo\u00015\u0004.mxt\u001ee\u0007{?\u001aJ6a\u0005y\u0007ak7 \u0017t\u00102zxt\u000fpB%~,e]5\u0010K0eGg\u0003/x= \u0014a\u001b-zx\u0002E\u0002\u001b\\7u\u000bqB/p, \u000ea\u00073~,eGw\u00032z< \b{B\u0011?1sG{\r5?+u\u0017e\r3k=d\u0019K0eGq\u00035zxm\u0012f\u0016aq7tGw\u0007aq-l\u000b\u0010\\7u\u000bqB/p, \u0015z\u0017/{x\u0013\\7u\u000bqB/p, \u0013g\u0017/|9t\u00025\tK0eG`\f(kx&?;a\t{\r5?:eGg\u00071m=s\u0002{\u0016${xi\u00145\u000f(s4e\u0014p\u0001.q<s".length();
      char var1 = 17;
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
                  break label83;
               }

               var10004 = var10002;
               var12 = 11;
               var10005 = var7;
            }

            while(true) {
               var22 = var10004[var10005];
               switch(var7 % 7) {
               case 0:
                  var23 = 20;
                  break;
               case 1:
                  var23 = 83;
                  break;
               case 2:
                  var23 = 11;
                  break;
               case 3:
                  var23 = 108;
                  break;
               case 4:
                  var23 = 30;
                  break;
               case 5:
                  var23 = 105;
                  break;
               default:
                  var23 = 74;
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
            var2 = "T1d\ns\u000e(ep~\u000eq\u001f,7$g\u0000=\u0006(e7mO{\u0005;71k\fh\u0018(c5(\f|\u0006*b<i\u001bt\u0005'd\"S1|\n=\u000b'spX\u000ei\u001e,e>{Op\u001f:cpf\u0000iJ+rpf\u001aq\u0006";
            var4 = "T1d\ns\u000e(ep~\u000eq\u001f,7$g\u0000=\u0006(e7mO{\u0005;71k\fh\u0018(c5(\f|\u0006*b<i\u001bt\u0005'd\"S1|\n=\u000b'spX\u000ei\u001e,e>{Op\u001f:cpf\u0000iJ+rpf\u001aq\u0006".length();
            var1 = '2';
            var0 = -1;

            while(true) {
               label62: {
                  ++var0;
                  var10002 = var2.substring(var0, var0 + var1).toCharArray();
                  var10003 = var10002.length;
                  var7 = 0;
                  var10 = 3;
                  var10001 = var10002;
                  var8 = var10003;
                  if (var10003 <= 1) {
                     var10004 = var10002;
                     var12 = 3;
                     var10005 = var7;
                  } else {
                     var10 = 3;
                     var8 = var10003;
                     if (var10003 <= var7) {
                        break label62;
                     }

                     var10004 = var10002;
                     var12 = 3;
                     var10005 = var7;
                  }

                  while(true) {
                     var22 = var10004[var10005];
                     switch(var7 % 7) {
                     case 0:
                        var23 = 20;
                        break;
                     case 1:
                        var23 = 83;
                        break;
                     case 2:
                        var23 = 11;
                        break;
                     case 3:
                        var23 = 108;
                        break;
                     case 4:
                        var23 = 30;
                        break;
                     case 5:
                        var23 = 105;
                        break;
                     default:
                        var23 = 74;
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
                  Vulcan_m = TimeZone.getTimeZone(b[2]);
                  Vulcan_f = new int[][]{{14}, {13}, {12}, {11, 10}, {5, 5, 9}, {2, 1001}, {1}, {0}};
                  return;
               }

               var1 = var2.charAt(var0);
            }
         }

         var1 = var2.charAt(var0);
      }
   }

   private static Exception a(Exception var0) {
      return var0;
   }
}
