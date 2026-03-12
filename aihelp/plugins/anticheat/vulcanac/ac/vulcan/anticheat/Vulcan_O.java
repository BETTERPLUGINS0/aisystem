package ac.vulcan.anticheat;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;

public abstract class Vulcan_O implements Serializable {
   public static final Vulcan_O Vulcan_b;
   public static final Vulcan_O Vulcan_R;
   public static final Vulcan_O Vulcan_P;
   public static final Vulcan_O Vulcan_M;
   public static final Vulcan_O Vulcan_U;
   private static final ThreadLocal Vulcan_F;
   private boolean Vulcan_W = true;
   private boolean Vulcan_w = true;
   private boolean Vulcan_T = false;
   private boolean Vulcan_N = true;
   private String Vulcan__ = "[";
   private String Vulcan_S = "]";
   private String Vulcan_A = "=";
   private boolean Vulcan_L = false;
   private boolean Vulcan_l = false;
   private String Vulcan_v = ",";
   private String Vulcan_u = "{";
   private String Vulcan_g = ",";
   private boolean Vulcan_Z = true;
   private String Vulcan_t = "}";
   private boolean Vulcan_n = true;
   private String Vulcan_j;
   private String Vulcan_p;
   private String Vulcan_K;
   private String Vulcan_C;
   private String Vulcan_y;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-7240614522008456514L, 2818732005081187540L, (Object)null).a(161315132670300L);
   private static final String[] c;

   static Map Vulcan_j(Object[] var0) {
      return (Map)Vulcan_F.get();
   }

   static boolean Vulcan_B(Object[] var0) {
      long var1 = (Long)var0[0];
      Object var3 = (Object)var0[1];
      long var10000 = a ^ var1;
      int var8 = Vulcan_iA.Vulcan_O();
      Map var5 = Vulcan_j(new Object[0]);
      int var4 = var8;

      boolean var10;
      label46: {
         label34: {
            Map var9;
            label33: {
               try {
                  var9 = var5;
                  if (var4 != 0) {
                     break label33;
                  }

                  if (var5 == null) {
                     break label34;
                  }
               } catch (RuntimeException var7) {
                  throw a(var7);
               }

               var9 = var5;
            }

            try {
               var10 = var9.containsKey(var3);
               if (var4 != 0) {
                  return var10;
               }

               if (var10) {
                  break label46;
               }
            } catch (RuntimeException var6) {
               throw a(var6);
            }
         }

         var10 = false;
         return var10;
      }

      var10 = true;
      return var10;
   }

   static void Vulcan_b(Object[] var0) {
      long var2 = (Long)var0[0];
      Object var1 = (Object)var0[1];
      long var10000 = a ^ var2;
      int var4 = Vulcan_iA.Vulcan_V();
      if (var1 != null) {
         Object var5 = Vulcan_j(new Object[0]);

         label22: {
            try {
               if (var4 == 0) {
                  return;
               }

               if (var5 != null) {
                  break label22;
               }
            } catch (RuntimeException var6) {
               throw a(var6);
            }

            var5 = new WeakHashMap();
            Vulcan_F.set(var5);
         }

         ((Map)var5).put(var1, (Object)null);
      }

   }

   static void Vulcan_Q(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   protected Vulcan_O() {
      String[] var1 = c;
      this.Vulcan_j = var1[0];
      this.Vulcan_p = var1[1];
      this.Vulcan_K = ">";
      this.Vulcan_C = "<";
      this.Vulcan_y = ">";
   }

   public void Vulcan_S(Object[] var1) {
      StringBuffer var5 = (StringBuffer)var1[0];
      String var4 = (String)var1[1];
      long var2 = (Long)var1[2];
      var2 ^= a;
      long var6 = var2 ^ 36482018854763L;
      this.Vulcan_y(new Object[]{new Long(var6), var5, var4});
   }

   public void Vulcan_y(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_k(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_z(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected void Vulcan_m(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_aj(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected void Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected void Vulcan_c(Object[] var1) {
      long var3 = (Long)var1[0];
      StringBuffer var5 = (StringBuffer)var1[1];
      String var2 = (String)var1[2];
      Object var6 = (Object)var1[3];
      var3 ^= a;
      long var7 = var3 ^ 35452140769931L;
      Vulcan_e8.Vulcan_A(new Object[]{var5, new Long(var7), var6});
   }

   protected void Vulcan_L(Object[] var1) {
      StringBuffer var2 = (StringBuffer)var1[0];
      String var4 = (String)var1[1];
      Object var3 = (Object)var1[2];
      var2.append(var3);
   }

   protected void Vulcan__(Object[] var1) {
      StringBuffer var3 = (StringBuffer)var1[0];
      String var2 = (String)var1[1];
      Collection var4 = (Collection)var1[2];
      var3.append(var4);
   }

   protected void Vulcan_mN(Object[] var1) {
      StringBuffer var2 = (StringBuffer)var1[0];
      String var4 = (String)var1[1];
      Map var3 = (Map)var1[2];
      var2.append(var3);
   }

   protected void Vulcan_W(Object[] var1) {
      StringBuffer var2 = (StringBuffer)var1[0];
      long var3 = (Long)var1[1];
      String var5 = (String)var1[2];
      Object var6 = (Object)var1[3];
      var3 ^= a;
      long var7 = var3 ^ 54702845167203L;
      var2.append(this.Vulcan_C);
      var2.append(this.Vulcan_S(new Object[]{new Long(var7), var6.getClass()}));
      var2.append(this.Vulcan_y);
   }

   public void Vulcan_nG(Object[] var1) {
      StringBuffer var6 = (StringBuffer)var1[0];
      String var7 = (String)var1[1];
      long var2 = (Long)var1[2];
      long var4 = (Long)var1[3];
      var4 ^= a;
      long var8 = var4 ^ 20485924148781L;
      this.Vulcan_N(new Object[]{new Long(var8), var6, var7});
      this.Vulcan_n(new Object[]{var6, var7, new Long(var2)});
      this.Vulcan_y5(new Object[]{var6, var7});
   }

   protected void Vulcan_n(Object[] var1) {
      StringBuffer var5 = (StringBuffer)var1[0];
      String var4 = (String)var1[1];
      long var2 = (Long)var1[2];
      var5.append(var2);
   }

   public void Vulcan_I5(Object[] var1) {
      StringBuffer var6 = (StringBuffer)var1[0];
      String var5 = (String)var1[1];
      int var4 = (Integer)var1[2];
      long var2 = (Long)var1[3];
      var2 ^= a;
      long var7 = var2 ^ 115953580042319L;
      this.Vulcan_N(new Object[]{new Long(var7), var6, var5});
      this.Vulcan_I4(new Object[]{var6, var5, new Integer(var4)});
      this.Vulcan_y5(new Object[]{var6, var5});
   }

   protected void Vulcan_I4(Object[] var1) {
      StringBuffer var2 = (StringBuffer)var1[0];
      String var3 = (String)var1[1];
      int var4 = (Integer)var1[2];
      var2.append(var4);
   }

   public void Vulcan_p(Object[] var1) {
      StringBuffer var6 = (StringBuffer)var1[0];
      String var2 = (String)var1[1];
      long var3 = (Long)var1[2];
      int var5 = (Integer)var1[3];
      var3 ^= a;
      long var7 = var3 ^ 76801982366176L;
      this.Vulcan_N(new Object[]{new Long(var7), var6, var2});
      this.Vulcan_pO(new Object[]{var6, var2, new Integer(var5)});
      this.Vulcan_y5(new Object[]{var6, var2});
   }

   protected void Vulcan_pO(Object[] var1) {
      StringBuffer var3 = (StringBuffer)var1[0];
      String var2 = (String)var1[1];
      int var4 = (Integer)var1[2];
      var3.append(var4);
   }

   public void Vulcan_h(Object[] var1) {
      long var4 = (Long)var1[0];
      StringBuffer var2 = (StringBuffer)var1[1];
      String var6 = (String)var1[2];
      int var3 = (Integer)var1[3];
      var4 ^= a;
      long var7 = var4 ^ 76734970019255L;
      this.Vulcan_N(new Object[]{new Long(var7), var2, var6});
      this.Vulcan_l(new Object[]{var2, var6, new Integer(var3)});
      this.Vulcan_y5(new Object[]{var2, var6});
   }

   protected void Vulcan_l(Object[] var1) {
      StringBuffer var3 = (StringBuffer)var1[0];
      String var4 = (String)var1[1];
      int var2 = (Integer)var1[2];
      var3.append(var2);
   }

   public void Vulcan_lN(Object[] var1) {
      StringBuffer var5 = (StringBuffer)var1[0];
      long var2 = (Long)var1[1];
      String var4 = (String)var1[2];
      int var6 = (Integer)var1[3];
      var2 ^= a;
      long var7 = var2 ^ 108202455774802L;
      this.Vulcan_N(new Object[]{new Long(var7), var5, var4});
      this.Vulcan_lL(new Object[]{var5, var4, new Integer(var6)});
      this.Vulcan_y5(new Object[]{var5, var4});
   }

   protected void Vulcan_lL(Object[] var1) {
      StringBuffer var4 = (StringBuffer)var1[0];
      String var3 = (String)var1[1];
      int var2 = (Integer)var1[2];
      var4.append((char)var2);
   }

   public void Vulcan_F(Object[] var1) {
      StringBuffer var3 = (StringBuffer)var1[0];
      String var2 = (String)var1[1];
      long var4 = (Long)var1[2];
      double var6 = (Double)var1[3];
      var4 ^= a;
      long var8 = var4 ^ 139111828970641L;
      this.Vulcan_N(new Object[]{new Long(var8), var3, var2});
      this.Vulcan_FS(new Object[]{var3, var2, new Double(var6)});
      this.Vulcan_y5(new Object[]{var3, var2});
   }

   protected void Vulcan_FS(Object[] var1) {
      StringBuffer var2 = (StringBuffer)var1[0];
      String var3 = (String)var1[1];
      double var4 = (Double)var1[2];
      var2.append(var4);
   }

   public void Vulcan_n5(Object[] var1) {
      StringBuffer var3 = (StringBuffer)var1[0];
      String var6 = (String)var1[1];
      float var2 = (Float)var1[2];
      long var4 = (Long)var1[3];
      var4 ^= a;
      long var7 = var4 ^ 30117119860892L;
      this.Vulcan_N(new Object[]{new Long(var7), var3, var6});
      this.Vulcan_nf(new Object[]{var3, var6, new Boolean(var2)});
      this.Vulcan_y5(new Object[]{var3, var6});
   }

   protected void Vulcan_nf(Object[] var1) {
      StringBuffer var4 = (StringBuffer)var1[0];
      String var3 = (String)var1[1];
      float var2 = (Float)var1[2];
      var4.append(var2);
   }

   public void Vulcan_j(Object[] var1) {
      long var4 = (Long)var1[0];
      StringBuffer var6 = (StringBuffer)var1[1];
      String var3 = (String)var1[2];
      boolean var2 = (Boolean)var1[3];
      var4 ^= a;
      long var7 = var4 ^ 127159891535217L;
      this.Vulcan_N(new Object[]{new Long(var7), var6, var3});
      this.Vulcan_q1(new Object[]{var6, var3, new Boolean(var2)});
      this.Vulcan_y5(new Object[]{var6, var3});
   }

   protected void Vulcan_q1(Object[] var1) {
      StringBuffer var3 = (StringBuffer)var1[0];
      String var4 = (String)var1[1];
      boolean var2 = (Boolean)var1[2];
      var3.append(var2);
   }

   public void Vulcan_E(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected void Vulcan_g(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected void Vulcan_A(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected void Vulcan_gF(Object[] var1) {
      StringBuffer var4 = (StringBuffer)var1[0];
      String var2 = (String)var1[1];
      Object[] var3 = (Object[])var1[2];
      this.Vulcan_Y(new Object[]{var4, var2, new Integer(var3.length)});
   }

   public void Vulcan_yv(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected void Vulcan_T(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected void Vulcan_Tm(Object[] var1) {
      StringBuffer var2 = (StringBuffer)var1[0];
      String var4 = (String)var1[1];
      long[] var3 = (long[])var1[2];
      this.Vulcan_Y(new Object[]{var2, var4, new Integer(var3.length)});
   }

   public void Vulcan_OC(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected void Vulcan_J(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected void Vulcan_H(Object[] var1) {
      StringBuffer var4 = (StringBuffer)var1[0];
      String var2 = (String)var1[1];
      int[] var3 = (int[])var1[2];
      this.Vulcan_Y(new Object[]{var4, var2, new Integer(var3.length)});
   }

   public void Vulcan_yD(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected void Vulcan_qB(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected void Vulcan_q(Object[] var1) {
      StringBuffer var4 = (StringBuffer)var1[0];
      String var3 = (String)var1[1];
      short[] var2 = (short[])var1[2];
      this.Vulcan_Y(new Object[]{var4, var3, new Integer(var2.length)});
   }

   public void Vulcan_w(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected void Vulcan_to(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected void Vulcan_t(Object[] var1) {
      StringBuffer var2 = (StringBuffer)var1[0];
      String var4 = (String)var1[1];
      byte[] var3 = (byte[])var1[2];
      this.Vulcan_Y(new Object[]{var2, var4, new Integer(var3.length)});
   }

   public void Vulcan_KX(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected void Vulcan_B(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected void Vulcan_ZF(Object[] var1) {
      StringBuffer var4 = (StringBuffer)var1[0];
      String var2 = (String)var1[1];
      char[] var3 = (char[])var1[2];
      this.Vulcan_Y(new Object[]{var4, var2, new Integer(var3.length)});
   }

   public void Vulcan_nm(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected void Vulcan_K4(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected void Vulcan_d(Object[] var1) {
      StringBuffer var4 = (StringBuffer)var1[0];
      String var2 = (String)var1[1];
      double[] var3 = (double[])var1[2];
      this.Vulcan_Y(new Object[]{var4, var2, new Integer(var3.length)});
   }

   public void Vulcan_pJ(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected void Vulcan_r(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected void Vulcan_rX(Object[] var1) {
      StringBuffer var3 = (StringBuffer)var1[0];
      String var2 = (String)var1[1];
      float[] var4 = (float[])var1[2];
      this.Vulcan_Y(new Object[]{var3, var2, new Integer(var4.length)});
   }

   public void Vulcan_C(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected void Vulcan_Vc(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected void Vulcan_G(Object[] var1) {
      StringBuffer var3 = (StringBuffer)var1[0];
      String var2 = (String)var1[1];
      boolean[] var4 = (boolean[])var1[2];
      this.Vulcan_Y(new Object[]{var3, var2, new Integer(var4.length)});
   }

   protected void Vulcan_kk(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected void Vulcan_k0(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected void Vulcan_K(Object[] var1) {
      StringBuffer var2 = (StringBuffer)var1[0];
      var2.append(this.Vulcan__);
   }

   protected void Vulcan_pA(Object[] var1) {
      StringBuffer var2 = (StringBuffer)var1[0];
      var2.append(this.Vulcan_S);
   }

   protected void Vulcan_u(Object[] var1) {
      StringBuffer var3 = (StringBuffer)var1[0];
      String var2 = (String)var1[1];
      var3.append(this.Vulcan_j);
   }

   protected void Vulcan_x(Object[] var1) {
      StringBuffer var2 = (StringBuffer)var1[0];
      var2.append(this.Vulcan_v);
   }

   protected void Vulcan_N(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected void Vulcan_y5(Object[] var1) {
      StringBuffer var2 = (StringBuffer)var1[0];
      String var3 = (String)var1[1];
      this.Vulcan_x(new Object[]{var2});
   }

   protected void Vulcan_Y(Object[] var1) {
      StringBuffer var4 = (StringBuffer)var1[0];
      String var2 = (String)var1[1];
      int var3 = (Integer)var1[2];
      var4.append(this.Vulcan_p);
      var4.append(var3);
      var4.append(this.Vulcan_K);
   }

   protected boolean Vulcan_f(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected String Vulcan_S(Object[] var1) {
      long var2 = (Long)var1[0];
      Class var4 = (Class)var1[1];
      var2 ^= a;
      long var5 = var2 ^ 73377623570842L;
      return Vulcan_eo.Vulcan_B(new Object[]{var4, new Long(var5)});
   }

   protected boolean Vulcan_I(Object[] var1) {
      return this.Vulcan_w;
   }

   protected void Vulcan_e(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_w = var2;
   }

   protected boolean Vulcan_H(Object[] var1) {
      return this.Vulcan_T;
   }

   /** @deprecated */
   protected boolean Vulcan_G(Object[] var1) {
      return this.Vulcan_T;
   }

   protected void Vulcan_i(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_T = var2;
   }

   /** @deprecated */
   protected void Vulcan_s(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_T = var2;
   }

   protected boolean Vulcan_v(Object[] var1) {
      return this.Vulcan_N;
   }

   protected void Vulcan_bE(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_N = var2;
   }

   protected boolean Vulcan_V(Object[] var1) {
      return this.Vulcan_W;
   }

   protected void Vulcan_I(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_W = var2;
   }

   protected boolean Vulcan_k(Object[] var1) {
      return this.Vulcan_n;
   }

   protected void Vulcan_U(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_n = var2;
   }

   protected boolean Vulcan_g(Object[] var1) {
      return this.Vulcan_Z;
   }

   protected void Vulcan_a(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_Z = var2;
   }

   protected String Vulcan_y(Object[] var1) {
      return this.Vulcan_u;
   }

   protected void Vulcan_V(Object[] var1) {
      long var3 = (Long)var1[0];
      String var2 = (String)var1[1];
      int var5 = Vulcan_iA.Vulcan_V();

      label20: {
         try {
            if (var5 == 0) {
               return;
            }

            if (var2 != null) {
               break label20;
            }
         } catch (RuntimeException var6) {
            throw a(var6);
         }

         var2 = "";
      }

      this.Vulcan_u = var2;
   }

   protected String Vulcan_E(Object[] var1) {
      return this.Vulcan_t;
   }

   protected void Vulcan_v(Object[] var1) {
      long var3 = (Long)var1[0];
      String var2 = (String)var1[1];
      int var5 = Vulcan_iA.Vulcan_V();

      label20: {
         try {
            if (var5 == 0) {
               return;
            }

            if (var2 != null) {
               break label20;
            }
         } catch (RuntimeException var6) {
            throw a(var6);
         }

         var2 = "";
      }

      this.Vulcan_t = var2;
   }

   protected String Vulcan_x(Object[] var1) {
      return this.Vulcan_g;
   }

   protected void Vulcan_D(Object[] var1) {
      long var3 = (Long)var1[0];
      String var2 = (String)var1[1];
      int var5 = Vulcan_iA.Vulcan_O();

      label20: {
         try {
            if (var5 != 0) {
               return;
            }

            if (var2 != null) {
               break label20;
            }
         } catch (RuntimeException var6) {
            throw a(var6);
         }

         var2 = "";
      }

      this.Vulcan_g = var2;
   }

   protected String Vulcan_U(Object[] var1) {
      return this.Vulcan__;
   }

   protected void Vulcan_ZT(Object[] var1) {
      long var3 = (Long)var1[0];
      String var2 = (String)var1[1];
      if (var3 > 0L) {
         if (var2 == null) {
            var2 = "";
         }

         this.Vulcan__ = var2;
      }

   }

   protected String Vulcan_F(Object[] var1) {
      return this.Vulcan_S;
   }

   protected void Vulcan_X(Object[] var1) {
      String var4 = (String)var1[0];
      long var2 = (Long)var1[1];
      if (var2 > 0L) {
         if (var4 == null) {
            var4 = "";
         }

         this.Vulcan_S = var4;
      }

   }

   protected String Vulcan_K(Object[] var1) {
      return this.Vulcan_A;
   }

   protected void Vulcan_M(Object[] var1) {
      String var4 = (String)var1[0];
      long var2 = (Long)var1[1];
      int var5 = Vulcan_iA.Vulcan_V();

      label20: {
         try {
            if (var5 == 0) {
               return;
            }

            if (var4 != null) {
               break label20;
            }
         } catch (RuntimeException var6) {
            throw a(var6);
         }

         var4 = "";
      }

      this.Vulcan_A = var4;
   }

   protected String Vulcan_O(Object[] var1) {
      return this.Vulcan_v;
   }

   protected void Vulcan_f(Object[] var1) {
      long var3 = (Long)var1[0];
      String var2 = (String)var1[1];
      if (var3 > 0L) {
         if (var2 == null) {
            var2 = "";
         }

         this.Vulcan_v = var2;
      }

   }

   protected boolean Vulcan_x(Object[] var1) {
      return this.Vulcan_L;
   }

   protected void Vulcan_be(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_L = var2;
   }

   protected boolean Vulcan_o(Object[] var1) {
      return this.Vulcan_l;
   }

   protected void Vulcan_o(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_l = var2;
   }

   protected String Vulcan_t(Object[] var1) {
      return this.Vulcan_j;
   }

   protected void Vulcan_R(Object[] var1) {
      long var2 = (Long)var1[0];
      String var4 = (String)var1[1];
      int var5 = Vulcan_iA.Vulcan_V();

      label20: {
         try {
            if (var5 == 0) {
               return;
            }

            if (var4 != null) {
               break label20;
            }
         } catch (RuntimeException var6) {
            throw a(var6);
         }

         var4 = "";
      }

      this.Vulcan_j = var4;
   }

   protected String Vulcan_o(Object[] var1) {
      return this.Vulcan_p;
   }

   protected void Vulcan_Z(Object[] var1) {
      int var5 = (Integer)var1[0];
      int var4 = (Integer)var1[1];
      String var2 = (String)var1[2];
      int var3 = (Integer)var1[3];
      long var6 = (long)var5 << 48 | (long)var4 << 48 >>> 16 | (long)var3 << 32 >>> 32;
      int var8 = Vulcan_iA.Vulcan_V();

      label20: {
         try {
            if (var8 == 0) {
               return;
            }

            if (var2 != null) {
               break label20;
            }
         } catch (RuntimeException var9) {
            throw a(var9);
         }

         var2 = "";
      }

      this.Vulcan_p = var2;
   }

   protected String Vulcan__(Object[] var1) {
      return this.Vulcan_K;
   }

   protected void Vulcan_P(Object[] var1) {
      String var2 = (String)var1[0];
      long var3 = (Long)var1[1];
      int var5 = Vulcan_iA.Vulcan_V();

      label20: {
         try {
            if (var5 == 0) {
               return;
            }

            if (var2 != null) {
               break label20;
            }
         } catch (RuntimeException var6) {
            throw a(var6);
         }

         var2 = "";
      }

      this.Vulcan_K = var2;
   }

   protected String Vulcan_L(Object[] var1) {
      return this.Vulcan_C;
   }

   protected void Vulcan_Zl(Object[] var1) {
      long var3 = (Long)var1[0];
      String var2 = (String)var1[1];
      int var5 = Vulcan_iA.Vulcan_V();

      label20: {
         try {
            if (var5 == 0) {
               return;
            }

            if (var2 != null) {
               break label20;
            }
         } catch (RuntimeException var6) {
            throw a(var6);
         }

         var2 = "";
      }

      this.Vulcan_C = var2;
   }

   protected String Vulcan_z(Object[] var1) {
      return this.Vulcan_y;
   }

   protected void Vulcan_Z1(Object[] var1) {
      String var4 = (String)var1[0];
      long var2 = (Long)var1[1];
      int var5 = Vulcan_iA.Vulcan_O();

      label20: {
         try {
            if (var5 != 0) {
               return;
            }

            if (var4 != null) {
               break label20;
            }
         } catch (RuntimeException var6) {
            throw a(var6);
         }

         var4 = "";
      }

      this.Vulcan_y = var4;
   }

   static {
      String[] var5 = new String[2];
      int var3 = 0;
      String var2 = ".\u000f|U\u0005\u001f\u0006.\u0012`C\f\u001c";
      int var4 = ".\u000f|U\u0005\u001f\u0006.\u0012`C\f\u001c".length();
      char var1 = 6;
      int var0 = -1;

      while(true) {
         char[] var10001;
         label41: {
            ++var0;
            char[] var10002 = var2.substring(var0, var0 + var1).toCharArray();
            int var10003 = var10002.length;
            int var7 = 0;
            byte var10 = 93;
            var10001 = var10002;
            int var8 = var10003;
            byte var12;
            char[] var10004;
            int var10005;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 93;
               var10005 = var7;
            } else {
               var10 = 93;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label41;
               }

               var10004 = var10002;
               var12 = 93;
               var10005 = var7;
            }

            while(true) {
               char var22 = var10004[var10005];
               byte var23;
               switch(var7 % 7) {
               case 0:
                  var23 = 79;
                  break;
               case 1:
                  var23 = 60;
                  break;
               case 2:
                  var23 = 84;
                  break;
               case 3:
                  var23 = 100;
                  break;
               case 4:
                  var23 = 52;
                  break;
               case 5:
                  var23 = 124;
                  break;
               default:
                  var23 = 101;
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
            c = var5;
            Vulcan_b = new Vulcan_h();
            Vulcan_R = new Vulcan_E();
            Vulcan_P = new Vulcan_v();
            Vulcan_M = new Vulcan_p();
            Vulcan_U = new Vulcan_R();
            Vulcan_F = new ThreadLocal();
            return;
         }

         var1 = var2.charAt(var0);
      }
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
