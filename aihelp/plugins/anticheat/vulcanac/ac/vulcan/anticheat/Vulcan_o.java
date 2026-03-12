package ac.vulcan.anticheat;

import java.io.Reader;
import java.io.Writer;

public class Vulcan_o implements Cloneable {
   static final int Vulcan_S = 32;
   private static final long serialVersionUID = 7628716375283629643L;
   protected char[] Vulcan_H;
   protected int Vulcan_r;
   private String Vulcan_L;
   private String Vulcan_h;
   private static int[] Vulcan_K;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(7002428592616187584L, -2701810340692589170L, (Object)null).a(37501772267667L);
   private static final String[] b;

   public Vulcan_o() {
      this(32);
   }

   public Vulcan_o(int var1) {
      long var2 = a ^ 65064409984275L;
      super();
      if (var1 <= 0) {
         var1 = 32;
      }

      this.Vulcan_H = new char[var1];
   }

   public Vulcan_o(String param1) {
      // $FF: Couldn't be decompiled
   }

   public String Vulcan_Y(Object[] var1) {
      return this.Vulcan_L;
   }

   public Vulcan_o Vulcan_F0(Object[] var1) {
      String var2 = (String)var1[0];
      this.Vulcan_L = var2;
      return this;
   }

   public String Vulcan_D(Object[] var1) {
      return this.Vulcan_h;
   }

   public Vulcan_o Vulcan_m(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public int Vulcan_S(Object[] var1) {
      return this.Vulcan_r;
   }

   public Vulcan_o Vulcan_c(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public int Vulcan_q(Object[] var1) {
      return this.Vulcan_H.length;
   }

   public Vulcan_o Vulcan_aW(Object[] var1) {
      int var2 = (Integer)var1[0];
      long var3 = (Long)var1[1];
      long var10000 = a ^ var3;
      if (var2 > this.Vulcan_H.length) {
         char[] var5 = this.Vulcan_H;
         this.Vulcan_H = new char[var2 * 2];
         System.arraycopy(var5, 0, this.Vulcan_H, 0, this.Vulcan_r);
      }

      return this;
   }

   public Vulcan_o Vulcan_IR(Object[] var1) {
      long var2 = (Long)var1[0];
      long var10000 = a ^ var2;
      int[] var4 = Vulcan_h();

      Vulcan_o var7;
      label20: {
         try {
            var7 = this;
            if (var4 != null) {
               return var7;
            }

            if (this.Vulcan_H.length <= this.Vulcan_S(new Object[0])) {
               break label20;
            }
         } catch (StringIndexOutOfBoundsException var6) {
            throw a(var6);
         }

         char[] var5 = this.Vulcan_H;
         this.Vulcan_H = new char[this.Vulcan_S(new Object[0])];
         System.arraycopy(var5, 0, this.Vulcan_H, 0, this.Vulcan_r);
      }

      var7 = this;
      return var7;
   }

   public int Vulcan_i(Object[] var1) {
      return this.Vulcan_r;
   }

   public boolean Vulcan_z(Object[] var1) {
      long var2 = (Long)var1[0];
      long var10000 = a ^ var2;
      int[] var4 = Vulcan_h();

      int var6;
      label32: {
         try {
            var6 = this.Vulcan_r;
            if (var4 != null) {
               return (boolean)var6;
            }

            if (var6 == 0) {
               break label32;
            }
         } catch (StringIndexOutOfBoundsException var5) {
            throw a(var5);
         }

         var6 = 0;
         return (boolean)var6;
      }

      var6 = 1;
      return (boolean)var6;
   }

   public Vulcan_o Vulcan_IG(Object[] var1) {
      this.Vulcan_r = 0;
      return this;
   }

   public char Vulcan_N(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_E(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_aP(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public char[] Vulcan_E(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public char[] Vulcan_a(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public char[] Vulcan__(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_p(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_e(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_I(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_Dw(Object[] var1) {
      long var3 = (Long)var1[0];
      Object var2 = (Object)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 121562308439054L;
      long var7 = var3 ^ 137544138912961L;

      try {
         if (var2 == null) {
            return this.Vulcan_I(new Object[]{new Long(var7)});
         }
      } catch (StringIndexOutOfBoundsException var9) {
         throw a(var9);
      }

      return this.Vulcan_FS(new Object[]{var2.toString(), new Long(var5)});
   }

   public Vulcan_o Vulcan_FS(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_N(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_BD(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_k(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_x(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_B(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_X(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_PB(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_hy(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan__O(Object[] var1) {
      int var2 = (Integer)var1[0];
      long var3 = (Long)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 117310267076863L;
      int var7 = this.Vulcan_S(new Object[0]);
      this.Vulcan_aW(new Object[]{new Integer(var7 + 1), new Long(var5)});
      this.Vulcan_H[this.Vulcan_r++] = (char)var2;
      return this;
   }

   public Vulcan_o Vulcan_Q(Object[] var1) {
      long var2 = (Long)var1[0];
      int var4 = (Integer)var1[1];
      var2 ^= a;
      long var5 = var2 ^ 46825272999390L;
      return this.Vulcan_FS(new Object[]{String.valueOf(var4), new Long(var5)});
   }

   public Vulcan_o Vulcan_l(Object[] var1) {
      long var2 = (Long)var1[0];
      long var4 = (Long)var1[1];
      var4 ^= a;
      long var6 = var4 ^ 6019906176185L;
      return this.Vulcan_FS(new Object[]{String.valueOf(var2), new Long(var6)});
   }

   public Vulcan_o Vulcan_z(Object[] var1) {
      float var2 = (Float)var1[0];
      long var3 = (Long)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 60813175217130L;
      return this.Vulcan_FS(new Object[]{String.valueOf(var2), new Long(var5)});
   }

   public Vulcan_o Vulcan_b(Object[] var1) {
      long var2 = (Long)var1[0];
      double var4 = (Double)var1[1];
      var2 ^= a;
      long var6 = var2 ^ 28090256843193L;
      return this.Vulcan_FS(new Object[]{String.valueOf(var4), new Long(var6)});
   }

   public Vulcan_o Vulcan_D(Object[] var1) {
      Object var4 = (Object)var1[0];
      long var2 = (Long)var1[1];
      var2 ^= a;
      long var5 = var2 ^ 68863640716316L;
      long var7 = var2 ^ 20612235973255L;
      return this.Vulcan_Dw(new Object[]{new Long(var7), var4}).Vulcan_e(new Object[]{new Long(var5)});
   }

   public Vulcan_o Vulcan_F(Object[] var1) {
      String var2 = (String)var1[0];
      long var3 = (Long)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 54347937339989L;
      long var7 = var3 ^ 7973517124222L;
      return this.Vulcan_FS(new Object[]{var2, new Long(var5)}).Vulcan_e(new Object[]{new Long(var7)});
   }

   public Vulcan_o Vulcan_a7(Object[] var1) {
      String var6 = (String)var1[0];
      int var5 = (Integer)var1[1];
      long var3 = (Long)var1[2];
      int var2 = (Integer)var1[3];
      var3 ^= a;
      long var7 = var3 ^ 14106237398458L;
      long var9 = var3 ^ 105879824618346L;
      return this.Vulcan_N(new Object[]{new Long(var9), var6, new Integer(var5), new Integer(var2)}).Vulcan_e(new Object[]{new Long(var7)});
   }

   public Vulcan_o Vulcan_BJ(Object[] var1) {
      long var2 = (Long)var1[0];
      StringBuffer var4 = (StringBuffer)var1[1];
      var2 ^= a;
      long var5 = var2 ^ 49763827340211L;
      long var7 = var2 ^ 98567833459851L;
      return this.Vulcan_BD(new Object[]{new Long(var7), var4}).Vulcan_e(new Object[]{new Long(var5)});
   }

   public Vulcan_o Vulcan_FZ(Object[] var1) {
      StringBuffer var5 = (StringBuffer)var1[0];
      long var2 = (Long)var1[1];
      int var6 = (Integer)var1[2];
      int var4 = (Integer)var1[3];
      var2 ^= a;
      long var7 = var2 ^ 60172028316598L;
      long var9 = var2 ^ 71826078691712L;
      return this.Vulcan_k(new Object[]{var5, new Integer(var6), new Long(var9), new Integer(var4)}).Vulcan_e(new Object[]{new Long(var7)});
   }

   public Vulcan_o Vulcan_o(Object[] var1) {
      long var2 = (Long)var1[0];
      Vulcan_o var4 = (Vulcan_o)var1[1];
      var2 ^= a;
      long var5 = var2 ^ 84441987040089L;
      long var7 = var2 ^ 63565574928660L;
      return this.Vulcan_x(new Object[]{new Long(var7), var4}).Vulcan_e(new Object[]{new Long(var5)});
   }

   public Vulcan_o Vulcan_v(Object[] var1) {
      long var2 = (Long)var1[0];
      Vulcan_o var5 = (Vulcan_o)var1[1];
      int var6 = (Integer)var1[2];
      int var4 = (Integer)var1[3];
      var2 ^= a;
      long var7 = var2 ^ 29575279906270L;
      long var9 = var2 ^ 118942631999856L;
      return this.Vulcan_B(new Object[]{new Long(var9), var5, new Integer(var6), new Integer(var4)}).Vulcan_e(new Object[]{new Long(var7)});
   }

   public Vulcan_o Vulcan_q(Object[] var1) {
      long var3 = (Long)var1[0];
      char[] var2 = (char[])var1[1];
      var3 ^= a;
      long var5 = var3 ^ 43356219925581L;
      long var7 = var3 ^ 17301867835314L;
      return this.Vulcan_X(new Object[]{var2, new Long(var7)}).Vulcan_e(new Object[]{new Long(var5)});
   }

   public Vulcan_o Vulcan_PH(Object[] var1) {
      char[] var6 = (char[])var1[0];
      int var3 = (Integer)var1[1];
      int var2 = (Integer)var1[2];
      long var4 = (Long)var1[3];
      var4 ^= a;
      long var7 = var4 ^ 9276321707568L;
      long var9 = var4 ^ 38241229096548L;
      return this.Vulcan_PB(new Object[]{var6, new Integer(var3), new Long(var9), new Integer(var2)}).Vulcan_e(new Object[]{new Long(var7)});
   }

   public Vulcan_o Vulcan_h(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      long var3 = (Long)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 57300462142636L;
      long var7 = var3 ^ 39146041949577L;
      return this.Vulcan_hy(new Object[]{new Long(var7), new Boolean(var2)}).Vulcan_e(new Object[]{new Long(var5)});
   }

   public Vulcan_o Vulcan_H(Object[] var1) {
      long var2 = (Long)var1[0];
      int var4 = (Integer)var1[1];
      var2 ^= a;
      long var5 = var2 ^ 86958153865902L;
      long var7 = var2 ^ 114412205116444L;
      return this.Vulcan__O(new Object[]{new Integer(var4), new Long(var7)}).Vulcan_e(new Object[]{new Long(var5)});
   }

   public Vulcan_o Vulcan_a(Object[] var1) {
      long var3 = (Long)var1[0];
      int var2 = (Integer)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 206210584520L;
      long var7 = var3 ^ 114587835418755L;
      return this.Vulcan_Q(new Object[]{new Long(var5), new Integer(var2)}).Vulcan_e(new Object[]{new Long(var7)});
   }

   public Vulcan_o Vulcan_i(Object[] var1) {
      long var2 = (Long)var1[0];
      long var4 = (Long)var1[1];
      var4 ^= a;
      long var6 = var4 ^ 89971288457412L;
      long var8 = var4 ^ 24448481607400L;
      return this.Vulcan_l(new Object[]{new Long(var2), new Long(var8)}).Vulcan_e(new Object[]{new Long(var6)});
   }

   public Vulcan_o Vulcan_yf(Object[] var1) {
      float var2 = (Float)var1[0];
      long var3 = (Long)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 113835020744562L;
      long var7 = var3 ^ 20163725153805L;
      return this.Vulcan_z(new Object[]{new Boolean(var2), new Long(var7)}).Vulcan_e(new Object[]{new Long(var5)});
   }

   public Vulcan_o Vulcan_CQ(Object[] var1) {
      double var2 = (Double)var1[0];
      long var4 = (Long)var1[1];
      var4 ^= a;
      long var6 = var4 ^ 59918224912832L;
      long var8 = var4 ^ 120273557023468L;
      return this.Vulcan_b(new Object[]{new Long(var8), new Double(var2)}).Vulcan_e(new Object[]{new Long(var6)});
   }

   public Vulcan_o Vulcan_yW(Object[] var1) {
      Object[] var4 = (Object[])var1[0];
      long var2 = (Long)var1[1];
      var2 ^= a;
      long var5 = var2 ^ 136692985810158L;
      int[] var7 = Vulcan_h();

      Vulcan_o var13;
      label66: {
         Object[] var10000;
         label60: {
            try {
               var10000 = var4;
               if (var7 != null) {
                  break label60;
               }

               if (var4 == null) {
                  break label66;
               }
            } catch (StringIndexOutOfBoundsException var11) {
               throw a(var11);
            }

            var10000 = var4;
         }

         int var12;
         label52: {
            try {
               var12 = var10000.length;
               if (var7 != null) {
                  break label52;
               }

               if (var12 <= 0) {
                  break label66;
               }
            } catch (StringIndexOutOfBoundsException var10) {
               throw a(var10);
            }

            var12 = 0;
         }

         int var8 = var12;

         label44:
         while(var8 < var4.length) {
            while(true) {
               try {
                  if (var2 > 0L) {
                     var13 = this.Vulcan_Dw(new Object[]{new Long(var5), var4[var8]});
                     if (var7 != null) {
                        return var13;
                     }

                     ++var8;
                  }

                  if (var7 == null) {
                     continue;
                  }
               } catch (StringIndexOutOfBoundsException var9) {
                  throw a(var9);
               }

               if (var2 > 0L) {
                  break label44;
               }
            }
         }
      }

      var13 = this;
      return var13;
   }

   public Vulcan_o Vulcan_mV(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_L(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_U(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_s1(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_t(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_Fu(Object[] var1) {
      String var4 = (String)var1[0];
      long var2 = (Long)var1[1];
      var2 ^= a;
      long var5 = var2 ^ 110603725107850L;
      return this.Vulcan_V(new Object[]{var4, new Long(var5), null});
   }

   public Vulcan_o Vulcan_V(Object[] var1) {
      String var3 = (String)var1[0];
      long var4 = (Long)var1[1];
      String var2 = (String)var1[2];
      var4 ^= a;
      long var6 = var4 ^ 100620168017390L;
      long var8 = var4 ^ 89739091722519L;

      String var10000;
      label29: {
         try {
            if (this.Vulcan_z(new Object[]{new Long(var8)})) {
               var10000 = var2;
               break label29;
            }
         } catch (StringIndexOutOfBoundsException var12) {
            throw a(var12);
         }

         var10000 = var3;
      }

      String var10 = var10000;

      try {
         if (var4 >= 0L && var10 != null) {
            this.Vulcan_FS(new Object[]{var10, new Long(var6)});
         }

         return this;
      } catch (StringIndexOutOfBoundsException var11) {
         throw a(var11);
      }
   }

   public Vulcan_o Vulcan_d(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_T(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_Fk(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_S(Object[] var1) {
      int var2 = (Integer)var1[0];
      int var3 = (Integer)var1[1];
      long var4 = (Long)var1[2];
      var4 ^= a;
      long var6 = var4 ^ 34744081891056L;

      try {
         if (var3 > 0) {
            this.Vulcan__O(new Object[]{new Integer(var2), new Long(var6)});
         }

         return this;
      } catch (StringIndexOutOfBoundsException var8) {
         throw a(var8);
      }
   }

   public Vulcan_o Vulcan_y(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_u(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_Pc(Object[] var1) {
      int var5 = (Integer)var1[0];
      int var6 = (Integer)var1[1];
      int var2 = (Integer)var1[2];
      long var3 = (Long)var1[3];
      var3 ^= a;
      long var7 = var3 ^ 98856284475570L;
      return this.Vulcan_u(new Object[]{new Long(var7), String.valueOf(var5), new Integer(var6), new Integer(var2)});
   }

   public Vulcan_o Vulcan_r(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_P(Object[] var1) {
      int var2 = (Integer)var1[0];
      long var4 = (Long)var1[1];
      int var6 = (Integer)var1[2];
      int var3 = (Integer)var1[3];
      var4 ^= a;
      long var7 = var4 ^ 7012818330250L;
      return this.Vulcan_r(new Object[]{String.valueOf(var2), new Integer(var6), new Integer(var3), new Long(var7)});
   }

   public Vulcan_o Vulcan_g(Object[] var1) {
      int var4 = (Integer)var1[0];
      Object var5 = (Object)var1[1];
      long var2 = (Long)var1[2];
      var2 ^= a;
      long var6 = var2 ^ 67396212635353L;

      try {
         if (var5 == null) {
            String var10003 = this.Vulcan_h;
            return this.Vulcan_s(new Object[]{new Integer(var4), new Long(var6), var10003});
         }
      } catch (StringIndexOutOfBoundsException var8) {
         throw a(var8);
      }

      return this.Vulcan_s(new Object[]{new Integer(var4), new Long(var6), var5.toString()});
   }

   public Vulcan_o Vulcan_s(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_K(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_M(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_R(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_Y(Object[] var1) {
      long var4 = (Long)var1[0];
      int var3 = (Integer)var1[1];
      int var2 = (Integer)var1[2];
      var4 ^= a;
      long var6 = var4 ^ 42582875892088L;
      long var8 = var4 ^ 94006028278863L;
      this.Vulcan_d(new Object[]{new Long(var8), new Integer(var3)});
      this.Vulcan_aW(new Object[]{new Integer(this.Vulcan_r + 1), new Long(var6)});
      System.arraycopy(this.Vulcan_H, var3, this.Vulcan_H, var3 + 1, this.Vulcan_r - var3);
      this.Vulcan_H[var3] = (char)var2;
      ++this.Vulcan_r;
      return this;
   }

   public Vulcan_o Vulcan_J(Object[] var1) {
      int var4 = (Integer)var1[0];
      int var5 = (Integer)var1[1];
      long var2 = (Long)var1[2];
      var2 ^= a;
      long var6 = var2 ^ 55294897462752L;
      return this.Vulcan_s(new Object[]{new Integer(var4), new Long(var6), String.valueOf(var5)});
   }

   public Vulcan_o Vulcan_n(Object[] var1) {
      long var3 = (Long)var1[0];
      int var2 = (Integer)var1[1];
      long var5 = (Long)var1[2];
      var3 ^= a;
      long var7 = var3 ^ 40952509623434L;
      return this.Vulcan_s(new Object[]{new Integer(var2), new Long(var7), String.valueOf(var5)});
   }

   public Vulcan_o Vulcan_p(Object[] var1) {
      long var4 = (Long)var1[0];
      int var3 = (Integer)var1[1];
      float var2 = (Float)var1[2];
      var4 ^= a;
      long var6 = var4 ^ 58260806649126L;
      return this.Vulcan_s(new Object[]{new Integer(var3), new Long(var6), String.valueOf(var2)});
   }

   public Vulcan_o Vulcan_j(Object[] var1) {
      int var6 = (Integer)var1[0];
      long var2 = (Long)var1[1];
      double var4 = (Double)var1[2];
      var2 ^= a;
      long var7 = var2 ^ 26810108112948L;
      return this.Vulcan_s(new Object[]{new Integer(var6), new Long(var7), String.valueOf(var4)});
   }

   private void Vulcan_N(Object[] var1) {
      int var4 = (Integer)var1[0];
      int var3 = (Integer)var1[1];
      int var2 = (Integer)var1[2];
      System.arraycopy(this.Vulcan_H, var3, this.Vulcan_H, var4, this.Vulcan_r - var3);
      this.Vulcan_r -= var2;
   }

   public Vulcan_o Vulcan_W(Object[] var1) {
      int var4 = (Integer)var1[0];
      long var2 = (Long)var1[1];
      int var5 = (Integer)var1[2];
      var2 ^= a;
      long var6 = var2 ^ 51044262245322L;
      var5 = this.Vulcan_A(new Object[]{new Integer(var4), new Long(var6), new Integer(var5)});
      int var8 = var5 - var4;

      try {
         if (var8 > 0) {
            this.Vulcan_N(new Object[]{new Integer(var4), new Integer(var5), new Integer(var8)});
         }

         return this;
      } catch (StringIndexOutOfBoundsException var9) {
         throw a(var9);
      }
   }

   public Vulcan_o Vulcan__(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_C(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_Fv(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_f(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_Ac(Object[] var1) {
      Vulcan_eb var4 = (Vulcan_eb)var1[0];
      long var2 = (Long)var1[1];
      var2 ^= a;
      long var5 = var2 ^ 43709297862643L;
      int var10004 = this.Vulcan_r;
      return this.Vulcan_O(new Object[]{var4, null, new Integer(0), new Integer(var10004), new Integer(-1), new Long(var5)});
   }

   public Vulcan_o Vulcan_Z(Object[] var1) {
      long var3 = (Long)var1[0];
      Vulcan_eb var2 = (Vulcan_eb)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 112627149378024L;
      int var10004 = this.Vulcan_r;
      return this.Vulcan_O(new Object[]{var2, null, new Integer(0), new Integer(var10004), new Integer(1), new Long(var5)});
   }

   private void Vulcan_t(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_gH(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_iS(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_A(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_V_(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_V2(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_jD(Object[] var1) {
      long var2 = (Long)var1[0];
      Vulcan_eb var4 = (Vulcan_eb)var1[1];
      String var5 = (String)var1[2];
      var2 ^= a;
      long var6 = var2 ^ 94360855946353L;
      int var10004 = this.Vulcan_r;
      return this.Vulcan_O(new Object[]{var4, var5, new Integer(0), new Integer(var10004), new Integer(-1), new Long(var6)});
   }

   public Vulcan_o Vulcan_jF(Object[] var1) {
      long var3 = (Long)var1[0];
      Vulcan_eb var2 = (Vulcan_eb)var1[1];
      String var5 = (String)var1[2];
      var3 ^= a;
      long var6 = var3 ^ 66969717165753L;
      int var10004 = this.Vulcan_r;
      return this.Vulcan_O(new Object[]{var2, var5, new Integer(0), new Integer(var10004), new Integer(1), new Long(var6)});
   }

   public Vulcan_o Vulcan_O(Object[] var1) {
      Vulcan_eb var2 = (Vulcan_eb)var1[0];
      String var7 = (String)var1[1];
      int var5 = (Integer)var1[2];
      int var8 = (Integer)var1[3];
      int var6 = (Integer)var1[4];
      long var3 = (Long)var1[5];
      var3 ^= a;
      long var9 = var3 ^ 127114713972213L;
      long var11 = var3 ^ 7223696767265L;
      var8 = this.Vulcan_A(new Object[]{new Integer(var5), new Long(var11), new Integer(var8)});
      return this.Vulcan__z(new Object[]{var2, var7, new Integer(var5), new Integer(var8), new Long(var9), new Integer(var6)});
   }

   private Vulcan_o Vulcan__z(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_w(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_o Vulcan_G(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_y(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_P(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public String Vulcan_K(Object[] var1) {
      long var3 = (Long)var1[0];
      int var2 = (Integer)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 123896117005205L;
      int var10003 = this.Vulcan_r;
      return this.Vulcan_w(new Object[]{new Integer(var2), new Long(var5), new Integer(var10003)});
   }

   public String Vulcan_w(Object[] var1) {
      int var3 = (Integer)var1[0];
      long var4 = (Long)var1[1];
      int var2 = (Integer)var1[2];
      var4 ^= a;
      long var6 = var4 ^ 127537604168899L;
      var2 = this.Vulcan_A(new Object[]{new Integer(var3), new Long(var6), new Integer(var2)});
      return new String(this.Vulcan_H, var3, var2 - var3);
   }

   public String Vulcan_f(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public String Vulcan_x(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public String Vulcan_s(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_C(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_S(Object[] var1) {
      long var3 = (Long)var1[0];
      String var2 = (String)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 17890265329943L;
      int[] var7 = Vulcan_h();

      int var10000;
      label32: {
         try {
            var10000 = this.Vulcan_Q(new Object[]{var2, new Integer(0), new Long(var5)});
            if (var7 != null) {
               return (boolean)var10000;
            }

            if (var10000 >= 0) {
               break label32;
            }
         } catch (StringIndexOutOfBoundsException var8) {
            throw a(var8);
         }

         var10000 = 0;
         return (boolean)var10000;
      }

      var10000 = 1;
      return (boolean)var10000;
   }

   public boolean Vulcan_j(Object[] var1) {
      long var2 = (Long)var1[0];
      Vulcan_eb var4 = (Vulcan_eb)var1[1];
      var2 ^= a;
      long var5 = var2 ^ 61051736909082L;
      int[] var7 = Vulcan_h();

      int var10000;
      label32: {
         try {
            var10000 = this.Vulcan_J(new Object[]{var4, new Long(var5), new Integer(0)});
            if (var7 != null) {
               return (boolean)var10000;
            }

            if (var10000 >= 0) {
               break label32;
            }
         } catch (StringIndexOutOfBoundsException var8) {
            throw a(var8);
         }

         var10000 = 0;
         return (boolean)var10000;
      }

      var10000 = 1;
      return (boolean)var10000;
   }

   public int Vulcan_X(Object[] var1) {
      long var3 = (Long)var1[0];
      int var2 = (Integer)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 47509217439669L;
      return this.Vulcan_d(new Object[]{new Integer(var2), new Long(var5), new Integer(0)});
   }

   public int Vulcan_d(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public int Vulcan_V(Object[] var1) {
      long var3 = (Long)var1[0];
      String var2 = (String)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 31685596122299L;
      return this.Vulcan_Q(new Object[]{var2, new Integer(0), new Long(var5)});
   }

   public int Vulcan_Q(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public int Vulcan_G(Object[] var1) {
      Vulcan_eb var2 = (Vulcan_eb)var1[0];
      long var3 = (Long)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 38440360696789L;
      return this.Vulcan_J(new Object[]{var2, new Long(var5), new Integer(0)});
   }

   public int Vulcan_J(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public int Vulcan_M(Object[] var1) {
      int var2 = (Integer)var1[0];
      long var3 = (Long)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 117047303392146L;
      int var10002 = this.Vulcan_r - 1;
      return this.Vulcan_D(new Object[]{new Integer(var2), new Integer(var10002), new Long(var5)});
   }

   public int Vulcan_D(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public int Vulcan_x(Object[] var1) {
      long var3 = (Long)var1[0];
      String var2 = (String)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 118190781457679L;
      int var10003 = this.Vulcan_r - 1;
      return this.Vulcan_B(new Object[]{var2, new Long(var5), new Integer(var10003)});
   }

   public int Vulcan_B(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public int Vulcan_e(Object[] var1) {
      Vulcan_eb var4 = (Vulcan_eb)var1[0];
      long var2 = (Long)var1[1];
      var2 ^= a;
      long var5 = var2 ^ 68068737946202L;
      int var10003 = this.Vulcan_r;
      return this.Vulcan_u(new Object[]{var4, new Long(var5), new Integer(var10003)});
   }

   public int Vulcan_u(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_iy Vulcan_Z(Object[] var1) {
      return new Vulcan_iT(this);
   }

   public Reader Vulcan_O(Object[] var1) {
      return new Vulcan_eg(this);
   }

   public Writer Vulcan_L(Object[] var1) {
      return new Vulcan_XF(this);
   }

   public boolean Vulcan_R(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_c(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean equals(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public int hashCode() {
      long var1 = a ^ 86765831099273L;
      int[] var10000 = Vulcan_h();
      char[] var4 = this.Vulcan_H;
      int[] var3 = var10000;
      int var5 = 0;
      int var6 = this.Vulcan_r - 1;

      int var7;
      while(true) {
         if (var6 >= 0) {
            var7 = 31 * var5 + var4[var6];
            if (var3 != null) {
               break;
            }

            var5 = var7;
            --var6;
            if (var3 == null) {
               continue;
            }
         }

         var7 = var5;
         break;
      }

      return var7;
   }

   public String toString() {
      return new String(this.Vulcan_H, 0, this.Vulcan_r);
   }

   public StringBuffer Vulcan_s(Object[] var1) {
      return (new StringBuffer(this.Vulcan_r)).append(this.Vulcan_H, 0, this.Vulcan_r);
   }

   public Object clone() {
      Vulcan_o var1 = (Vulcan_o)super.clone();
      var1.Vulcan_H = new char[this.Vulcan_H.length];
      System.arraycopy(this.Vulcan_H, 0, var1.Vulcan_H, 0, this.Vulcan_H.length);
      return var1;
   }

   protected int Vulcan_A(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected void Vulcan_d(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_n(int[] var0) {
      Vulcan_K = var0;
   }

   public static int[] Vulcan_h() {
      return Vulcan_K;
   }

   static {
      String[] var5 = new String[10];
      int var3 = 0;
      String var2 = "p)\u0007\u007f=?P\u0019+\u0014p6\"\\\u0003g\u0014U\"\u001fy%>\u0014T2\u0002jq4Q\u00191\u0010r82\u000b\\)\u0015>mvGM&\u0003j\u0018J3\u0010l%\u001fZ]\"\t><#GMg\u0013{q UU.\u0015\u0014U\"\u001fy%>\u0014T2\u0002jq4Q\u00191\u0010r82\u0014p)\u0007\u007f=?P\u00194\u0005\u007f#\"}W#\u0014fkv\u0010p)\u0007\u007f=?P\u0019+\u0014p6\"\\\u0003g\u0010p)\u0007\u007f=?P\u0019(\u0017x\"3@\u0003g";
      int var4 = "p)\u0007\u007f=?P\u0019+\u0014p6\"\\\u0003g\u0014U\"\u001fy%>\u0014T2\u0002jq4Q\u00191\u0010r82\u000b\\)\u0015>mvGM&\u0003j\u0018J3\u0010l%\u001fZ]\"\t><#GMg\u0013{q UU.\u0015\u0014U\"\u001fy%>\u0014T2\u0002jq4Q\u00191\u0010r82\u0014p)\u0007\u007f=?P\u00194\u0005\u007f#\"}W#\u0014fkv\u0010p)\u0007\u007f=?P\u0019+\u0014p6\"\\\u0003g\u0010p)\u0007\u007f=?P\u0019(\u0017x\"3@\u0003g".length();
      char var1 = 16;
      int var0 = -1;
      Vulcan_n((int[])null);

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
            var10 = 79;
            var10001 = var10002;
            var8 = var10003;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 79;
               var10005 = var7;
            } else {
               var10 = 79;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label83;
               }

               var10004 = var10002;
               var12 = 79;
               var10005 = var7;
            }

            while(true) {
               var22 = var10004[var10005];
               switch(var7 % 7) {
               case 0:
                  var23 = 118;
                  break;
               case 1:
                  var23 = 8;
                  break;
               case 2:
                  var23 = 62;
                  break;
               case 3:
                  var23 = 81;
                  break;
               case 4:
                  var23 = 30;
                  break;
               case 5:
                  var23 = 25;
                  break;
               default:
                  var23 = 123;
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
            var2 = "\rxDo<'\u0016\u001cwR;\u0018\u001bbA=tN\u000b\fsXomr\u0016\u001c6B* q\u0004\u0004\u007fD";
            var4 = "\rxDo<'\u0016\u001cwR;\u0018\u001bbA=tN\u000b\fsXomr\u0016\u001c6B* q\u0004\u0004\u007fD".length();
            var1 = 11;
            var0 = -1;

            while(true) {
               label62: {
                  ++var0;
                  var10002 = var2.substring(var0, var0 + var1).toCharArray();
                  var10003 = var10002.length;
                  var7 = 0;
                  var10 = 30;
                  var10001 = var10002;
                  var8 = var10003;
                  if (var10003 <= 1) {
                     var10004 = var10002;
                     var12 = 30;
                     var10005 = var7;
                  } else {
                     var10 = 30;
                     var8 = var10003;
                     if (var10003 <= var7) {
                        break label62;
                     }

                     var10004 = var10002;
                     var12 = 30;
                     var10005 = var7;
                  }

                  while(true) {
                     var22 = var10004[var10005];
                     switch(var7 % 7) {
                     case 0:
                        var23 = 118;
                        break;
                     case 1:
                        var23 = 8;
                        break;
                     case 2:
                        var23 = 62;
                        break;
                     case 3:
                        var23 = 81;
                        break;
                     case 4:
                        var23 = 30;
                        break;
                     case 5:
                        var23 = 25;
                        break;
                     default:
                        var23 = 123;
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

   private static StringIndexOutOfBoundsException a(StringIndexOutOfBoundsException var0) {
      return var0;
   }
}
