package ac.vulcan.anticheat;

public class Vulcan_Xt {
   public static final String Vulcan_p = "";
   public static final int Vulcan_u = -1;
   private static final int Vulcan_e = 8192;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(7546708076338881075L, -8284967387184713360L, (Object)null).a(132694746022202L);
   private static final String[] b;

   public static boolean Vulcan_X(Object[] var0) {
      String var1 = (String)var0[0];
      long var2 = (Long)var0[1];
      long var10000 = a ^ var2;
      String[] var4 = Vulcan_XL.Vulcan_v();

      int var8;
      label46: {
         String var7;
         label33: {
            try {
               var7 = var1;
               if (var4 == null) {
                  break label33;
               }

               if (var1 == null) {
                  break label46;
               }
            } catch (IllegalStateException var6) {
               throw a(var6);
            }

            var7 = var1;
         }

         try {
            var8 = var7.length();
            if (var4 == null) {
               return (boolean)var8;
            }

            if (var8 == 0) {
               break label46;
            }
         } catch (IllegalStateException var5) {
            throw a(var5);
         }

         var8 = 0;
         return (boolean)var8;
      }

      var8 = 1;
      return (boolean)var8;
   }

   public static boolean Vulcan_P(Object[] var0) {
      String var3 = (String)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 8381990979539L;
      String[] var6 = Vulcan_XL.Vulcan_v();

      boolean var10000;
      label32: {
         try {
            var10000 = Vulcan_X(new Object[]{var3, new Long(var4)});
            if (var6 == null) {
               return var10000;
            }

            if (!var10000) {
               break label32;
            }
         } catch (IllegalStateException var7) {
            throw a(var7);
         }

         var10000 = false;
         return var10000;
      }

      var10000 = true;
      return var10000;
   }

   public static boolean Vulcan_Y(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_x(Object[] var0) {
      long var1 = (Long)var0[0];
      String var3 = (String)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 11415624266876L;
      String[] var6 = Vulcan_XL.Vulcan_v();

      boolean var10000;
      label32: {
         try {
            var10000 = Vulcan_Y(new Object[]{var3, new Long(var4)});
            if (var6 == null) {
               return var10000;
            }

            if (!var10000) {
               break label32;
            }
         } catch (IllegalStateException var7) {
            throw a(var7);
         }

         var10000 = false;
         return var10000;
      }

      var10000 = true;
      return var10000;
   }

   /** @deprecated */
   public static String Vulcan_s(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_mi(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_m9(Object[] var0) {
      String var1 = (String)var0[0];
      long var2 = (Long)var0[1];
      var2 ^= a;
      long var4 = var2 ^ 12255426169389L;
      long var6 = var2 ^ 10076267486497L;
      String var8 = Vulcan_mi(new Object[]{new Long(var6), var1});

      String var10000;
      try {
         if (Vulcan_X(new Object[]{var8, new Long(var4)})) {
            var10000 = null;
            return var10000;
         }
      } catch (IllegalStateException var9) {
         throw a(var9);
      }

      var10000 = var8;
      return var10000;
   }

   public static String Vulcan_mu(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_p(Object[] var0) {
      long var1 = (Long)var0[0];
      String var3 = (String)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 75903831792516L;
      return Vulcan_tv(new Object[]{new Long(var4), var3, null});
   }

   public static String Vulcan_J(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_W(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_tv(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_R(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_F(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String[] Vulcan_T(Object[] var0) {
      long var1 = (Long)var0[0];
      String[] var3 = (String[])var0[1];
      var1 ^= a;
      long var4 = var1 ^ 27404967747646L;
      return Vulcan_S(new Object[]{var3, new Long(var4), null});
   }

   public static String[] Vulcan_S(Object[] var0) {
      String[] var4 = (String[])var0[0];
      long var1 = (Long)var0[1];
      String var3 = (String)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 35471505764313L;
      String[] var7 = Vulcan_XL.Vulcan_v();

      int var8;
      int var14;
      String[] var10000;
      label81: {
         label72: {
            try {
               var10000 = var4;
               if (var7 == null) {
                  return var10000;
               }

               if (var4 == null) {
                  break label72;
               }
            } catch (IllegalStateException var13) {
               throw a(var13);
            }

            var14 = var8 = var4.length;

            try {
               if (var7 == null) {
                  break label81;
               }

               if (var14 == 0) {
                  break label72;
               }
            } catch (IllegalStateException var12) {
               throw a(var12);
            }

            var14 = var8;
            break label81;
         }

         var10000 = var4;
         return var10000;
      }

      String[] var9 = new String[var14];
      int var10 = 0;

      label48:
      while(var10 < var8) {
         while(true) {
            try {
               var10000 = var9;
               if (var1 > 0L) {
                  if (var7 == null) {
                     return var10000;
                  }

                  var9[var10] = Vulcan_tv(new Object[]{new Long(var5), var4[var10], var3});
                  ++var10;
                  var10000 = var7;
               }

               if (var10000 != null) {
                  continue;
               }
            } catch (IllegalStateException var11) {
               throw a(var11);
            }

            if (var1 >= 0L) {
               break label48;
            }
         }
      }

      var10000 = var9;
      return var10000;
   }

   public static boolean Vulcan_i(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_I(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_S(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_Q(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_u(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_C(Object[] var0) {
      long var2 = (Long)var0[0];
      String var4 = (String)var0[1];
      String var1 = (String)var0[2];
      int var5 = (Integer)var0[3];
      var2 ^= a;
      long var6 = var2 ^ 105258616626110L;
      return Vulcan_P(new Object[]{var4, new Long(var6), var1, new Integer(var5), new Boolean(false)});
   }

   private static int Vulcan_P(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_w(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_Y(Object[] var0) {
      long var2 = (Long)var0[0];
      String var4 = (String)var0[1];
      String var1 = (String)var0[2];
      var2 ^= a;
      long var5 = var2 ^ 63625382810830L;
      return Vulcan_I(new Object[]{var4, new Long(var5), var1, new Integer(0)});
   }

   public static int Vulcan_I(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_B(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_z(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_W(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_j(Object[] var0) {
      String var4 = (String)var0[0];
      String var5 = (String)var0[1];
      long var2 = (Long)var0[2];
      int var1 = (Integer)var0[3];
      var2 ^= a;
      long var6 = var2 ^ 70193197329527L;
      return Vulcan_P(new Object[]{var4, new Long(var6), var5, new Integer(var1), new Boolean(true)});
   }

   public static int Vulcan_t(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_G(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_T(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_S(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_F(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan__(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_q(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_c(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_G(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_w(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_N(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_s(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_c(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_H(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_E(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_C(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_y(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_X(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_c(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_E(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_q(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_IS(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_T(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_x(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_a(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_tt(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_Q(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_tk(Object[] var0) {
      String var1 = (String)var0[0];
      long var3 = (Long)var0[1];
      String var2 = (String)var0[2];
      var3 ^= a;
      long var5 = var3 ^ 42384269926982L;
      return Vulcan_H(new Object[]{var1, var2, new Long(var5), var2});
   }

   public static String Vulcan_H(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String[] Vulcan_e(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   /** @deprecated */
   public static String Vulcan_u(Object[] var0) {
      long var3 = (Long)var0[0];
      String var2 = (String)var0[1];
      String var1 = (String)var0[2];
      var3 ^= a;
      long var5 = var3 ^ 116060033670103L;
      return Vulcan_H(new Object[]{var2, var1, new Long(var5), var1});
   }

   /** @deprecated */
   public static String Vulcan_N(Object[] var0) {
      String var5 = (String)var0[0];
      String var4 = (String)var0[1];
      String var3 = (String)var0[2];
      long var1 = (Long)var0[3];
      var1 ^= a;
      long var6 = var1 ^ 82549170695069L;
      return Vulcan_H(new Object[]{var5, var4, new Long(var6), var3});
   }

   public static String[] Vulcan_X(Object[] var0) {
      long var1 = (Long)var0[0];
      String var3 = (String)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 121905205228711L;
      return Vulcan_y(new Object[]{var3, null, new Long(var4), new Integer(-1)});
   }

   public static String[] Vulcan__(Object[] var0) {
      String var1 = (String)var0[0];
      long var2 = (Long)var0[1];
      int var4 = (Integer)var0[2];
      var2 ^= a;
      long var5 = var2 ^ 14358493755784L;
      return Vulcan_g(new Object[]{var1, new Integer(var4), new Boolean(false), new Long(var5)});
   }

   public static String[] Vulcan_C(Object[] var0) {
      String var3 = (String)var0[0];
      long var1 = (Long)var0[1];
      String var4 = (String)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 38357746669343L;
      return Vulcan_Y(new Object[]{var3, new Long(var5), var4, new Integer(-1), new Boolean(false)});
   }

   public static String[] Vulcan_y(Object[] var0) {
      String var1 = (String)var0[0];
      String var5 = (String)var0[1];
      long var2 = (Long)var0[2];
      int var4 = (Integer)var0[3];
      var2 ^= a;
      long var6 = var2 ^ 7870110712237L;
      return Vulcan_Y(new Object[]{var1, new Long(var6), var5, new Integer(var4), new Boolean(false)});
   }

   public static String[] Vulcan_A(Object[] var0) {
      String var2 = (String)var0[0];
      long var3 = (Long)var0[1];
      String var1 = (String)var0[2];
      var3 ^= a;
      long var5 = var3 ^ 21450064967936L;
      return Vulcan_N(new Object[]{var2, new Long(var5), var1, new Integer(-1), new Boolean(false)});
   }

   public static String[] Vulcan_u(Object[] var0) {
      String var5 = (String)var0[0];
      String var4 = (String)var0[1];
      long var2 = (Long)var0[2];
      int var1 = (Integer)var0[3];
      var2 ^= a;
      long var6 = var2 ^ 45710258437879L;
      return Vulcan_N(new Object[]{var5, new Long(var6), var4, new Integer(var1), new Boolean(false)});
   }

   public static String[] Vulcan_o(Object[] var0) {
      long var1 = (Long)var0[0];
      String var3 = (String)var0[1];
      String var4 = (String)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 75902065939697L;
      return Vulcan_N(new Object[]{var3, new Long(var5), var4, new Integer(-1), new Boolean(true)});
   }

   public static String[] Vulcan_b(Object[] var0) {
      String var2 = (String)var0[0];
      String var1 = (String)var0[1];
      long var4 = (Long)var0[2];
      int var3 = (Integer)var0[3];
      var4 ^= a;
      long var6 = var4 ^ 132074303499695L;
      return Vulcan_N(new Object[]{var2, new Long(var6), var1, new Integer(var3), new Boolean(true)});
   }

   private static String[] Vulcan_N(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String[] Vulcan_r(Object[] var0) {
      long var2 = (Long)var0[0];
      String var1 = (String)var0[1];
      var2 ^= a;
      long var4 = var2 ^ 74354283818317L;
      return Vulcan_Y(new Object[]{var1, new Long(var4), null, new Integer(-1), new Boolean(true)});
   }

   public static String[] Vulcan_n(Object[] var0) {
      String var1 = (String)var0[0];
      int var2 = (Integer)var0[1];
      long var3 = (Long)var0[2];
      var3 ^= a;
      long var5 = var3 ^ 69965331004263L;
      return Vulcan_g(new Object[]{var1, new Integer(var2), new Boolean(true), new Long(var5)});
   }

   private static String[] Vulcan_g(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String[] Vulcan_D(Object[] var0) {
      long var2 = (Long)var0[0];
      String var1 = (String)var0[1];
      String var4 = (String)var0[2];
      var2 ^= a;
      long var5 = var2 ^ 127104410039339L;
      return Vulcan_Y(new Object[]{var1, new Long(var5), var4, new Integer(-1), new Boolean(true)});
   }

   public static String[] Vulcan_c(Object[] var0) {
      String var1 = (String)var0[0];
      long var3 = (Long)var0[1];
      String var2 = (String)var0[2];
      int var5 = (Integer)var0[3];
      var3 ^= a;
      long var6 = var3 ^ 124533586196903L;
      return Vulcan_Y(new Object[]{var1, new Long(var6), var2, new Integer(var5), new Boolean(true)});
   }

   private static String[] Vulcan_Y(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String[] Vulcan_a(Object[] var0) {
      String var1 = (String)var0[0];
      long var2 = (Long)var0[1];
      var2 ^= a;
      long var4 = var2 ^ 90325138809872L;
      return Vulcan_J(new Object[]{var1, new Long(var4), new Boolean(false)});
   }

   public static String[] Vulcan_W(Object[] var0) {
      String var3 = (String)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 24249150670470L;
      return Vulcan_J(new Object[]{var3, new Long(var4), new Boolean(true)});
   }

   private static String[] Vulcan_J(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   /** @deprecated */
   public static String Vulcan_BL(Object[] var0) {
      long var2 = (Long)var0[0];
      Object[] var1 = (Object[])var0[1];
      var2 ^= a;
      long var4 = var2 ^ 40813079949131L;
      return Vulcan_FT(new Object[]{new Long(var4), var1, null});
   }

   public static String Vulcan_Bu(Object[] var0) {
      Object[] var3 = (Object[])var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 90511501067716L;
      return Vulcan_FT(new Object[]{new Long(var4), var3, null});
   }

   public static String Vulcan_i(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_K7(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_FT(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_Ra(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_O(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_Wv(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_HH(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_J7(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   /** @deprecated */
   public static String Vulcan_ml(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_mf(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_tw(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_tr(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_e(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_d(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_tZ(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_b(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_C(Object[] var0) {
      String var5 = (String)var0[0];
      String var1 = (String)var0[1];
      String var2 = (String)var0[2];
      long var3 = (Long)var0[3];
      var3 ^= a;
      long var6 = var3 ^ 77265851847808L;
      return Vulcan_P(new Object[]{var5, var1, new Long(var6), var2, new Integer(1)});
   }

   public static String Vulcan_U(Object[] var0) {
      long var1 = (Long)var0[0];
      String var5 = (String)var0[1];
      String var3 = (String)var0[2];
      String var4 = (String)var0[3];
      var1 ^= a;
      long var6 = var1 ^ 62078759979566L;
      return Vulcan_P(new Object[]{var5, var3, new Long(var6), var4, new Integer(-1)});
   }

   public static String Vulcan_P(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_fh(Object[] var0) {
      String var1 = (String)var0[0];
      long var2 = (Long)var0[1];
      String[] var4 = (String[])var0[2];
      String[] var5 = (String[])var0[3];
      var2 ^= a;
      long var6 = var2 ^ 41741968522472L;
      return Vulcan_Q3(new Object[]{new Long(var6), var1, var4, var5, new Boolean(false), new Integer(0)});
   }

   public static String Vulcan_f(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static String Vulcan_Q3(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_xV(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_UJ(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   /** @deprecated */
   public static String Vulcan_v(Object[] var0) {
      String var3 = (String)var0[0];
      String var5 = (String)var0[1];
      long var1 = (Long)var0[2];
      int var4 = (Integer)var0[3];
      int var6 = (Integer)var0[4];
      var1 ^= a;
      long var7 = var1 ^ 45464082799095L;
      return (new Vulcan_o(var4 + var5.length() + var3.length() - var6 + 1)).Vulcan_FS(new Object[]{var3.substring(0, var4), new Long(var7)}).Vulcan_FS(new Object[]{var5, new Long(var7)}).Vulcan_FS(new Object[]{var3.substring(var6), new Long(var7)}).toString();
   }

   public static String Vulcan_K(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_w(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_t(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   /** @deprecated */
   public static String Vulcan_L(Object[] var0) {
      String var3 = (String)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 97034504658515L;
      return Vulcan_X(new Object[]{var3, new Long(var4), "\n"});
   }

   /** @deprecated */
   public static String Vulcan_X(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   /** @deprecated */
   public static String Vulcan_n(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   /** @deprecated */
   public static String Vulcan_tW(Object[] var0) {
      String var3 = (String)var0[0];
      String var4 = (String)var0[1];
      long var1 = (Long)var0[2];
      long var10000 = a ^ var1;
      int var5 = var3.indexOf(var4);

      try {
         if (var5 == -1) {
            return var3;
         }
      } catch (IllegalStateException var6) {
         throw a(var6);
      }

      return var3.substring(var5 + var4.length());
   }

   /** @deprecated */
   public static String Vulcan_B(Object[] var0) {
      long var1 = (Long)var0[0];
      String var3 = (String)var0[1];
      String var4 = (String)var0[2];
      long var10000 = a ^ var1;
      int var5 = var3.indexOf(var4);

      try {
         if (var5 == -1) {
            return "";
         }
      } catch (IllegalStateException var6) {
         throw a(var6);
      }

      return var3.substring(0, var5 + var4.length());
   }

   public static String Vulcan_mj(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   /** @deprecated */
   public static String Vulcan_m8(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   /** @deprecated */
   public static String Vulcan_Y(Object[] var0) {
      long var1 = (Long)var0[0];
      String var3 = (String)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 60723373625387L;
      return Vulcan_em.Vulcan_o(new Object[]{new Long(var4), var3});
   }

   public static String Vulcan_g(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_D(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static String Vulcan_Il(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_j(Object[] var0) {
      String var3 = (String)var0[0];
      long var1 = (Long)var0[1];
      int var4 = (Integer)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 16077494159732L;
      return Vulcan_o(new Object[]{new Long(var5), var3, new Integer(var4), new Integer(32)});
   }

   public static String Vulcan_o(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_q_(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_I(Object[] var0) {
      String var4 = (String)var0[0];
      int var1 = (Integer)var0[1];
      long var2 = (Long)var0[2];
      var2 ^= a;
      long var5 = var2 ^ 70960700230954L;
      return Vulcan_h(new Object[]{new Long(var5), var4, new Integer(var1), new Integer(32)});
   }

   public static String Vulcan_h(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_l(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_x(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_IV(Object[] var0) {
      long var3 = (Long)var0[0];
      String var2 = (String)var0[1];
      int var1 = (Integer)var0[2];
      var3 ^= a;
      long var5 = var3 ^ 126382408370199L;
      return Vulcan_y(new Object[]{var2, new Integer(var1), new Integer(32), new Long(var5)});
   }

   public static String Vulcan_y(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_qE(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_mx(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_Qe(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_mp(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_Qm(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_k(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   /** @deprecated */
   public static String Vulcan_mk(Object[] var0) {
      String var3 = (String)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 136838259208608L;
      return Vulcan_k(new Object[]{var3, new Long(var4)});
   }

   public static String Vulcan_mK(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   /** @deprecated */
   public static String Vulcan_A(Object[] var0) {
      String var3 = (String)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 117715418485874L;
      return Vulcan_mK(new Object[]{var3, new Long(var4)});
   }

   public static String Vulcan_mt(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   /** @deprecated */
   public static String Vulcan_m(Object[] var0) {
      long var1 = (Long)var0[0];
      String var3 = (String)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 124040350556900L;
      return Vulcan_Xo.Vulcan_p(new Object[]{var3, new Long(var4)});
   }

   public static int Vulcan_L(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_n(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_U(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_N(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_k(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_j(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_z(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_b(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_v(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_K(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_B(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_m5(Object[] var0) {
      String var3 = (String)var0[0];
      long var1 = (Long)var0[1];
      long var10000 = a ^ var1;
      String[] var4 = Vulcan_XL.Vulcan_v();

      String var6;
      label32: {
         try {
            var6 = var3;
            if (var4 == null) {
               return var6;
            }

            if (var3 == null) {
               break label32;
            }
         } catch (IllegalStateException var5) {
            throw a(var5);
         }

         var6 = var3;
         return var6;
      }

      var6 = "";
      return var6;
   }

   public static String Vulcan_r(Object[] var0) {
      String var4 = (String)var0[0];
      long var2 = (Long)var0[1];
      String var1 = (String)var0[2];
      long var10000 = a ^ var2;
      String[] var5 = Vulcan_XL.Vulcan_v();

      String var7;
      label32: {
         try {
            var7 = var4;
            if (var5 == null) {
               return var7;
            }

            if (var4 == null) {
               break label32;
            }
         } catch (IllegalStateException var6) {
            throw a(var6);
         }

         var7 = var4;
         return var7;
      }

      var7 = var1;
      return var7;
   }

   public static String Vulcan_tb(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_z(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_mQ(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_Z(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   /** @deprecated */
   public static String Vulcan_G(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan__(Object[] var0) {
      long var2 = (Long)var0[0];
      String var4 = (String)var0[1];
      int var1 = (Integer)var0[2];
      var2 ^= a;
      long var5 = var2 ^ 68653194496349L;
      return Vulcan_T7(new Object[]{var4, new Integer(0), new Integer(var1), new Long(var5)});
   }

   public static String Vulcan_T7(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_V(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_S(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_p(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_K(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_M(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_f(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_M(Object[] var0) {
      String var2 = (String)var0[0];
      String var1 = (String)var0[1];
      long var3 = (Long)var0[2];
      var3 ^= a;
      long var5 = var3 ^ 17244649069531L;
      return Vulcan_t(new Object[]{var2, var1, new Boolean(false), new Long(var5)});
   }

   public static boolean Vulcan_u(Object[] var0) {
      String var1 = (String)var0[0];
      String var2 = (String)var0[1];
      long var3 = (Long)var0[2];
      var3 ^= a;
      long var5 = var3 ^ 118529879753651L;
      return Vulcan_t(new Object[]{var1, var2, new Boolean(true), new Long(var5)});
   }

   private static boolean Vulcan_t(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_a(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_A(Object[] var0) {
      String var4 = (String)var0[0];
      String var1 = (String)var0[1];
      long var2 = (Long)var0[2];
      var2 ^= a;
      long var5 = var2 ^ 22924640092135L;
      return Vulcan_V(new Object[]{var4, var1, new Long(var5), new Boolean(false)});
   }

   public static boolean Vulcan_l(Object[] var0) {
      long var3 = (Long)var0[0];
      String var1 = (String)var0[1];
      String var2 = (String)var0[2];
      var3 ^= a;
      long var5 = var3 ^ 50554225942580L;
      return Vulcan_V(new Object[]{var1, var2, new Long(var5), new Boolean(true)});
   }

   private static boolean Vulcan_V(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_ms(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_R(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   static {
      String[] var5 = new String[11];
      int var3 = 0;
      String var2 = "DA;\b\f\f7y^3M7\u0005[+]A8\u00045\u0016\u00160I4\u000f*\u0006\ryI\"\u00047\r[gA2\u00190C\fy\\>M7\u0005\u001dcM\"M1\u0010['\u00040^%M.CM7\u001f;\u000b[qF2M\n\u0006\u000b|I5\bx\u0002\tbI/M4\u0006\u0015w\\>\u001ex\u0007\u0014~\u000f\"M5\u0002\u000fs@lM\u00110A%M4\u0006\bc\b\"\u00059\r[ \u0012v\u001f]A8\u00045\u0016\u00160I4\u000f*\u0006\ryI\"\u00047\r[gA2\u00190C\u0012c\bb\u001eSI8\u00037\u0017[`I2M9C\u0015uO7\u00191\u0015\u001e0I;\u0002-\r\u000f*\b\u0003>\u0006x\u0018C\\$\u00046\u0004\b0E#\u001e,C\u0015\u007f\\v\u000f=C\u0015eD:";
      int var4 = "DA;\b\f\f7y^3M7\u0005[+]A8\u00045\u0016\u00160I4\u000f*\u0006\ryI\"\u00047\r[gA2\u00190C\fy\\>M7\u0005\u001dcM\"M1\u0010['\u00040^%M.CM7\u001f;\u000b[qF2M\n\u0006\u000b|I5\bx\u0002\tbI/M4\u0006\u0015w\\>\u001ex\u0007\u0014~\u000f\"M5\u0002\u000fs@lM\u00110A%M4\u0006\bc\b\"\u00059\r[ \u0012v\u001f]A8\u00045\u0016\u00160I4\u000f*\u0006\ryI\"\u00047\r[gA2\u00190C\u0012c\bb\u001eSI8\u00037\u0017[`I2M9C\u0015uO7\u00191\u0015\u001e0I;\u0002-\r\u000f*\b\u0003>\u0006x\u0018C\\$\u00046\u0004\b0E#\u001e,C\u0015\u007f\\v\u000f=C\u0015eD:".length();
      char var1 = 14;
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
            var10 = 31;
            var10001 = var10002;
            var8 = var10003;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 31;
               var10005 = var7;
            } else {
               var10 = 31;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label83;
               }

               var10004 = var10002;
               var12 = 31;
               var10005 = var7;
            }

            while(true) {
               var22 = var10004[var10005];
               switch(var7 % 7) {
               case 0:
                  var23 = 15;
                  break;
               case 1:
                  var23 = 55;
                  break;
               case 2:
                  var23 = 73;
                  break;
               case 3:
                  var23 = 114;
                  break;
               case 4:
                  var23 = 71;
                  break;
               case 5:
                  var23 = 124;
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
            var2 = "\r\u001cfZm\u0003\u0003;E";
            var4 = "\r\u001cfZm\u0003\u0003;E".length();
            var1 = 5;
            var0 = -1;

            while(true) {
               label62: {
                  ++var0;
                  var10002 = var2.substring(var0, var0 + var1).toCharArray();
                  var10003 = var10002.length;
                  var7 = 0;
                  var10 = 34;
                  var10001 = var10002;
                  var8 = var10003;
                  if (var10003 <= 1) {
                     var10004 = var10002;
                     var12 = 34;
                     var10005 = var7;
                  } else {
                     var10 = 34;
                     var8 = var10003;
                     if (var10003 <= var7) {
                        break label62;
                     }

                     var10004 = var10002;
                     var12 = 34;
                     var10005 = var7;
                  }

                  while(true) {
                     var22 = var10004[var10005];
                     switch(var7 % 7) {
                     case 0:
                        var23 = 15;
                        break;
                     case 1:
                        var23 = 55;
                        break;
                     case 2:
                        var23 = 73;
                        break;
                     case 3:
                        var23 = 114;
                        break;
                     case 4:
                        var23 = 71;
                        break;
                     case 5:
                        var23 = 124;
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

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
