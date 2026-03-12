package ac.vulcan.anticheat;

import java.io.File;
import java.io.PrintStream;

public class Vulcan_X5 {
   private static final int Vulcan_s = 3;
   private static final String Vulcan_Qf;
   private static final String Vulcan_QM;
   private static final String Vulcan_Qe;
   private static final String Vulcan_g;
   private static final String Vulcan_K;
   public static final String Vulcan_Q2;
   public static final String Vulcan_D;
   public static final String Vulcan_QN;
   public static final String Vulcan_Q;
   public static final String Vulcan_w;
   public static final String Vulcan_r;
   public static final String Vulcan_u;
   public static final String Vulcan_H;
   public static final String Vulcan_R;
   public static final String Vulcan_Qm;
   public static final String Vulcan_X;
   public static final String Vulcan_T;
   public static final String Vulcan_Q7;
   public static final String Vulcan_O;
   public static final String Vulcan_QD;
   public static final String Vulcan_F;
   public static final String Vulcan_h;
   public static final String Vulcan_z;
   public static final String Vulcan_QF;
   public static final String Vulcan_QS;
   public static final String Vulcan_y;
   public static final String Vulcan_C;
   public static final String Vulcan_d;
   public static final String Vulcan_o;
   public static final String Vulcan_Qw;
   public static final String Vulcan_Qn;
   public static final String Vulcan_q;
   public static final String Vulcan_i;
   public static final String Vulcan_Q6;
   public static final String Vulcan_x;
   public static final String Vulcan_m;
   public static final String Vulcan_e;
   public static final String Vulcan_f;
   public static final String Vulcan_n;
   public static final String Vulcan_B;
   public static final String Vulcan_a;
   public static final String Vulcan_j;
   public static final String Vulcan_k;
   public static final String Vulcan_V;
   public static final String Vulcan_Qc;
   public static final String Vulcan_QU;
   public static final String Vulcan_QA;
   public static final String Vulcan_L;
   public static final float Vulcan_A;
   public static final int Vulcan_l;
   public static final boolean Vulcan_S;
   public static final boolean Vulcan_J;
   public static final boolean Vulcan_b;
   public static final boolean Vulcan_U;
   public static final boolean Vulcan_p;
   public static final boolean Vulcan_Q8;
   public static final boolean Vulcan_Z;
   public static final boolean Vulcan_Q3;
   public static final boolean Vulcan_Y;
   public static final boolean Vulcan_QR;
   public static final boolean Vulcan_QB;
   public static final boolean Vulcan_QI;
   public static final boolean Vulcan_W;
   public static final boolean Vulcan_QT;
   public static final boolean Vulcan_t;
   public static final boolean Vulcan_N;
   public static final boolean Vulcan_QZ;
   public static final boolean Vulcan__;
   public static final boolean Vulcan_I;
   public static final boolean Vulcan_G;
   public static final boolean Vulcan_P;
   public static final boolean Vulcan_v;
   public static final boolean Vulcan_E;
   public static final boolean Vulcan_Qi;
   public static final boolean Vulcan_c;
   public static final boolean Vulcan_M;
   private static final long a;
   private static final String[] b;

   public static File Vulcan_H(Object[] var0) {
      return new File(System.getProperty(b[14]));
   }

   public static File Vulcan_Q(Object[] var0) {
      return new File(System.getProperty(b[28]));
   }

   /** @deprecated */
   public static float Vulcan_p(Object[] var0) {
      return Vulcan_A;
   }

   private static float Vulcan_n(Object[] var0) {
      long var1 = (Long)var0[0];
      var1 ^= a;
      long var3 = var1 ^ 99385812416208L;
      long var5 = var1 ^ 96149453215532L;
      return Vulcan_I(new Object[]{new Long(var5), Vulcan_Y(new Object[]{Vulcan_o, new Long(var3), new Integer(3)})});
   }

   private static int Vulcan_B(Object[] var0) {
      long var1 = (Long)var0[0];
      var1 ^= a;
      long var3 = var1 ^ 55539578228374L;
      long var5 = var1 ^ 62961837127938L;
      return Vulcan_Q(new Object[]{new Long(var5), Vulcan_Y(new Object[]{Vulcan_o, new Long(var3), new Integer(3)})});
   }

   private static boolean Vulcan_u(Object[] var0) {
      String var1 = (String)var0[0];
      long var2 = (Long)var0[1];
      var2 ^= a;
      long var4 = var2 ^ 78596040185067L;
      return Vulcan_p(new Object[]{Vulcan_L, var1, new Long(var4)});
   }

   private static String Vulcan_b(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static boolean Vulcan_I(Object[] var0) {
      long var1 = (Long)var0[0];
      String var4 = (String)var0[1];
      String var3 = (String)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 137268209250557L;
      return Vulcan_C(new Object[]{Vulcan_n, Vulcan_B, var4, new Long(var5), var3});
   }

   private static boolean Vulcan_X(Object[] var0) {
      String var3 = (String)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 114683740782707L;
      return Vulcan_g(new Object[]{Vulcan_n, var3, new Long(var4)});
   }

   private static String Vulcan_s(Object[] var0) {
      String var1 = (String)var0[0];

      try {
         return System.getProperty(var1);
      } catch (SecurityException var4) {
         PrintStream var10000 = System.err;
         StringBuffer var10001 = new StringBuffer();
         String[] var3 = b;
         var10000.println(var10001.append(var3[42]).append(var1).append(var3[41]).toString());
         return null;
      }
   }

   public static File Vulcan_j(Object[] var0) {
      return new File(System.getProperty(b[26]));
   }

   public static File Vulcan_s(Object[] var0) {
      return new File(System.getProperty(b[77]));
   }

   public static boolean Vulcan_M(Object[] var0) {
      long var1 = (Long)var0[0];
      long var10000 = a ^ var1;
      String[] var3 = Vulcan_XL.Vulcan_v();

      String var5;
      boolean var6;
      label32: {
         label22: {
            try {
               var5 = Vulcan_r;
               if (var3 == null) {
                  break label32;
               }

               if (var5 == null) {
                  break label22;
               }
            } catch (SecurityException var4) {
               throw a(var4);
            }

            var5 = Vulcan_r;
            break label32;
         }

         var6 = false;
         return var6;
      }

      var6 = var5.equals(Boolean.TRUE.toString());
      return var6;
   }

   public static boolean Vulcan_R(Object[] var0) {
      long var2 = (Long)var0[0];
      float var1 = (Float)var0[1];
      long var10000 = a ^ var2;

      boolean var5;
      try {
         if (Vulcan_A >= var1) {
            var5 = true;
            return var5;
         }
      } catch (SecurityException var4) {
         throw a(var4);
      }

      var5 = false;
      return var5;
   }

   public static boolean Vulcan_F(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   static boolean Vulcan_p(Object[] var0) {
      String var1 = (String)var0[0];
      String var4 = (String)var0[1];
      long var2 = (Long)var0[2];
      long var10000 = a ^ var2;

      try {
         if (var1 == null) {
            return false;
         }
      } catch (SecurityException var5) {
         throw a(var5);
      }

      return var1.startsWith(var4);
   }

   static boolean Vulcan_C(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   static boolean Vulcan_g(Object[] var0) {
      String var4 = (String)var0[0];
      String var3 = (String)var0[1];
      long var1 = (Long)var0[2];
      long var10000 = a ^ var1;

      try {
         if (var4 == null) {
            return false;
         }
      } catch (SecurityException var5) {
         throw a(var5);
      }

      return var4.startsWith(var3);
   }

   static float Vulcan_C(Object[] var0) {
      String var1 = (String)var0[0];
      long var2 = (Long)var0[1];
      var2 ^= a;
      long var4 = var2 ^ 74997459117442L;
      long var6 = var2 ^ 80415459279998L;
      return Vulcan_I(new Object[]{new Long(var6), Vulcan_Y(new Object[]{var1, new Long(var4), new Integer(3)})});
   }

   static int Vulcan_g(Object[] var0) {
      long var2 = (Long)var0[0];
      String var1 = (String)var0[1];
      var2 ^= a;
      long var4 = var2 ^ 33288347900286L;
      long var6 = var2 ^ 23668174204650L;
      return Vulcan_Q(new Object[]{new Long(var6), Vulcan_Y(new Object[]{var1, new Long(var4), new Integer(3)})});
   }

   static int[] Vulcan_X(Object[] var0) {
      String var3 = (String)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 16671910379242L;
      return Vulcan_Y(new Object[]{var3, new Long(var4), new Integer(Integer.MAX_VALUE)});
   }

   private static int[] Vulcan_Y(Object[] var0) {
      String var3 = (String)var0[0];
      long var1 = (Long)var0[1];
      int var4 = (Integer)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 92968860945201L;

      try {
         if (var3 == null) {
            return Vulcan_XL.Vulcan_R;
         }
      } catch (Exception var15) {
         throw a(var15);
      }

      String var10002 = b[67];
      String[] var7 = Vulcan_Xt.Vulcan_C(new Object[]{var3, new Long(var5), var10002});
      int[] var8 = new int[Math.min(var4, var7.length)];
      int var9 = 0;
      int var10 = 0;

      int var10000;
      int var10001;
      int[] var17;
      label68: {
         do {
            try {
               if (var10 >= var7.length) {
                  break;
               }

               var10000 = var9;
               var10001 = var4;
               if (var1 <= 0L || var1 <= 0L) {
                  break label68;
               }

               if (var9 >= var4) {
                  break;
               }
            } catch (Exception var14) {
               throw a(var14);
            }

            String var11 = var7[var10];
            if (var1 > 0L) {
               if (var11.length() > 0) {
                  try {
                     var8[var9] = Integer.parseInt(var11);
                     ++var9;
                  } catch (Exception var13) {
                  }
               }

               ++var10;
            }
         } while(var1 >= 0L);

         var17 = var8;
         if (var1 <= 0L) {
            return var17;
         }

         var10000 = var8.length;
         var10001 = var9;
      }

      if (var10000 > var10001) {
         int[] var16 = new int[var9];
         System.arraycopy(var8, 0, var16, 0, var9);
         var8 = var16;
      }

      var17 = var8;
      return var17;
   }

   private static float Vulcan_I(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static int Vulcan_Q(Object[] var0) {
      long var1 = (Long)var0[0];
      int[] var3 = (int[])var0[1];
      var1 ^= a;

      try {
         if (var3 == null) {
            return 0;
         }
      } catch (SecurityException var6) {
         throw a(var6);
      }

      int var4 = 0;
      int var5 = var3.length;
      int var10000 = var5;
      byte var10001 = 1;
      if (var1 >= 0L) {
         if (var5 >= 1) {
            var4 = var3[0] * 100;
         }

         var10000 = var5;
         var10001 = 2;
      }

      if (var1 >= 0L) {
         if (var10000 >= var10001) {
            var4 += var3[1] * 10;
         }

         var10000 = var5;
         if (var1 < 0L) {
            return var10000;
         }

         var10001 = 3;
      }

      if (var10000 >= var10001) {
         var4 += var3[2];
      }

      var10000 = var4;
      return var10000;
   }

   static {
      // $FF: Couldn't be decompiled
   }

   private static Exception a(Exception var0) {
      return var0;
   }
}
