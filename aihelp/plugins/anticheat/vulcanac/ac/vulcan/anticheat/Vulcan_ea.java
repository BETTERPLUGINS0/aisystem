package ac.vulcan.anticheat;

import java.io.Serializable;

public final class Vulcan_ea extends Vulcan_eS implements Serializable {
   private static final long serialVersionUID = 71849363892720L;
   private final long Vulcan_a;
   private final long Vulcan_s;
   private transient Long Vulcan_X = null;
   private transient Long Vulcan_k = null;
   private transient int Vulcan_n = 0;
   private transient String Vulcan_f = null;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-6578207596846787896L, 2365776375328420526L, (Object)null).a(109347721971535L);
   private static final String[] d;

   public Vulcan_ea(long var1) {
      this.Vulcan_a = var1;
      this.Vulcan_s = var1;
   }

   public Vulcan_ea(Number param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_ea(long param1, long param3) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_ea(Number param1, Number param2) {
      // $FF: Couldn't be decompiled
   }

   public Number Vulcan_b(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public long Vulcan_H(Object[] var1) {
      long var2 = (Long)var1[0];
      return this.Vulcan_a;
   }

   public int Vulcan_C(Object[] var1) {
      long var2 = (Long)var1[0];
      return (int)this.Vulcan_a;
   }

   public double Vulcan_j(Object[] var1) {
      long var2 = (Long)var1[0];
      return (double)this.Vulcan_a;
   }

   public float Vulcan_a(Object[] var1) {
      long var2 = (Long)var1[0];
      return (float)this.Vulcan_a;
   }

   public Number Vulcan_t(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public long Vulcan_c(Object[] var1) {
      long var2 = (Long)var1[0];
      return this.Vulcan_s;
   }

   public int Vulcan_q(Object[] var1) {
      long var2 = (Long)var1[0];
      return (int)this.Vulcan_s;
   }

   public double Vulcan_R(Object[] var1) {
      long var2 = (Long)var1[0];
      return (double)this.Vulcan_s;
   }

   public float Vulcan_h(Object[] var1) {
      long var2 = (Long)var1[0];
      return (float)this.Vulcan_s;
   }

   public boolean Vulcan_V(Object[] var1) {
      long var3 = (Long)var1[0];
      Number var2 = (Number)var1[1];
      long var5 = var3 ^ 116889126566323L;

      try {
         if (var2 == null) {
            return false;
         }
      } catch (IllegalArgumentException var7) {
         throw a(var7);
      }

      return this.Vulcan_y(new Object[]{new Long(var2.longValue()), new Long(var5)});
   }

   public boolean Vulcan_y(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_S(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_B(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean equals(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public int hashCode() {
      // $FF: Couldn't be decompiled
   }

   public String toString() {
      long var1 = a ^ 129811039397104L;
      long var3 = var1 ^ 134089737785503L;
      long var5 = var1 ^ 115181291011590L;
      long var7 = var1 ^ 9010259924632L;
      String var9 = Vulcan_eS.Vulcan_y();

      String var10000;
      label20: {
         try {
            var10000 = this.Vulcan_f;
            if (var9 != null) {
               return var10000;
            }

            if (var10000 != null) {
               break label20;
            }
         } catch (IllegalArgumentException var11) {
            throw a(var11);
         }

         Vulcan_o var10 = new Vulcan_o(32);
         var10.Vulcan_FS(new Object[]{d[2], new Long(var3)});
         var10.Vulcan_l(new Object[]{new Long(this.Vulcan_a), new Long(var7)});
         var10.Vulcan__O(new Object[]{new Integer(44), new Long(var5)});
         var10.Vulcan_l(new Object[]{new Long(this.Vulcan_s), new Long(var7)});
         var10.Vulcan__O(new Object[]{new Integer(93), new Long(var5)});
         this.Vulcan_f = var10.toString();
      }

      var10000 = this.Vulcan_f;
      return var10000;
   }

   public long[] Vulcan_J(Object[] var1) {
      long var2 = (Long)var1[0];
      var2 ^= a;
      String var10000 = Vulcan_eS.Vulcan_y();
      long[] var5 = new long[(int)(this.Vulcan_s - this.Vulcan_a + 1L)];
      int var6 = 0;
      String var4 = var10000;

      long[] var8;
      label33:
      while(var6 < var5.length) {
         while(true) {
            try {
               if (var2 >= 0L) {
                  var8 = var5;
                  if (var4 != null) {
                     return var8;
                  }

                  var5[var6] = this.Vulcan_a + (long)var6;
                  ++var6;
               }

               if (var4 == null) {
                  continue;
               }
            } catch (IllegalArgumentException var7) {
               throw a(var7);
            }

            if (var2 >= 0L) {
               break label33;
            }
         }
      }

      var8 = var5;
      return var8;
   }

   static {
      String[] var5 = new String[3];
      int var3 = 0;
      String var2 = "\u007fQuH^\u0017KI\\b\u001b\u0010\u000fSXM0\u0006_\u0016\u0006I\\0\u0006E\u000eJ\u001b\u007fQuH^\u0017KI\\bH]\u0017U_\u0019~\u0007DBDN\u0019~\u001d\\\u000e\u0006yX~\u000fU9";
      int var4 = "\u007fQuH^\u0017KI\\b\u001b\u0010\u000fSXM0\u0006_\u0016\u0006I\\0\u0006E\u000eJ\u001b\u007fQuH^\u0017KI\\bH]\u0017U_\u0019~\u0007DBDN\u0019~\u001d\\\u000e\u0006yX~\u000fU9".length();
      char var1 = 28;
      int var0 = -1;

      while(true) {
         char[] var10001;
         label41: {
            ++var0;
            char[] var10002 = var2.substring(var0, var0 + var1).toCharArray();
            int var10003 = var10002.length;
            int var7 = 0;
            byte var10 = 13;
            var10001 = var10002;
            int var8 = var10003;
            byte var12;
            char[] var10004;
            int var10005;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 13;
               var10005 = var7;
            } else {
               var10 = 13;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label41;
               }

               var10004 = var10002;
               var12 = 13;
               var10005 = var7;
            }

            while(true) {
               char var22 = var10004[var10005];
               byte var23;
               switch(var7 % 7) {
               case 0:
                  var23 = 38;
                  break;
               case 1:
                  var23 = 52;
                  break;
               case 2:
                  var23 = 29;
                  break;
               case 3:
                  var23 = 101;
                  break;
               case 4:
                  var23 = 61;
                  break;
               case 5:
                  var23 = 111;
                  break;
               default:
                  var23 = 43;
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
            d = var5;
            return;
         }

         var1 = var2.charAt(var0);
      }
   }

   private static IllegalArgumentException a(IllegalArgumentException var0) {
      return var0;
   }
}
