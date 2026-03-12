package ac.vulcan.anticheat;

import java.io.Serializable;

public final class Vulcan_eY extends Vulcan_eS implements Serializable {
   private static final long serialVersionUID = 71849363892730L;
   private final int Vulcan_h;
   private final int Vulcan_X;
   private transient Integer Vulcan_G = null;
   private transient Integer Vulcan_g = null;
   private transient int Vulcan_Z = 0;
   private transient String Vulcan_y = null;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(5274925964025567185L, -6907225352983276189L, (Object)null).a(208186552607596L);
   private static final String[] d;

   public Vulcan_eY(int var1) {
      this.Vulcan_h = var1;
      this.Vulcan_X = var1;
   }

   public Vulcan_eY(Number param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_eY(int param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_eY(Number param1, Number param2) {
      // $FF: Couldn't be decompiled
   }

   public Number Vulcan_b(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public long Vulcan_H(Object[] var1) {
      long var2 = (Long)var1[0];
      return (long)this.Vulcan_h;
   }

   public int Vulcan_C(Object[] var1) {
      long var2 = (Long)var1[0];
      return this.Vulcan_h;
   }

   public double Vulcan_j(Object[] var1) {
      long var2 = (Long)var1[0];
      return (double)this.Vulcan_h;
   }

   public float Vulcan_a(Object[] var1) {
      long var2 = (Long)var1[0];
      return (float)this.Vulcan_h;
   }

   public Number Vulcan_t(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public long Vulcan_c(Object[] var1) {
      long var2 = (Long)var1[0];
      return (long)this.Vulcan_X;
   }

   public int Vulcan_q(Object[] var1) {
      long var2 = (Long)var1[0];
      return this.Vulcan_X;
   }

   public double Vulcan_R(Object[] var1) {
      long var2 = (Long)var1[0];
      return (double)this.Vulcan_X;
   }

   public float Vulcan_h(Object[] var1) {
      long var2 = (Long)var1[0];
      return (float)this.Vulcan_X;
   }

   public boolean Vulcan_V(Object[] var1) {
      long var3 = (Long)var1[0];
      Number var2 = (Number)var1[1];
      long var5 = var3 ^ 17089925211987L;

      try {
         if (var2 == null) {
            return false;
         }
      } catch (IllegalArgumentException var7) {
         throw a(var7);
      }

      return this.Vulcan_Y(new Object[]{new Long(var5), new Integer(var2.intValue())});
   }

   public boolean Vulcan_Y(Object[] param1) {
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
      long var1 = a ^ 28843209249522L;
      long var3 = var1 ^ 134816758297087L;
      long var5 = var1 ^ 40369790570143L;
      long var7 = var1 ^ 118373327560550L;
      String var9 = Vulcan_eS.Vulcan_y();

      String var10000;
      label20: {
         try {
            var10000 = this.Vulcan_y;
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
         var10.Vulcan_FS(new Object[]{d[0], new Long(var3)});
         int var10002 = this.Vulcan_h;
         var10.Vulcan_Q(new Object[]{new Long(var5), new Integer(var10002)});
         var10.Vulcan__O(new Object[]{new Integer(44), new Long(var7)});
         var10002 = this.Vulcan_X;
         var10.Vulcan_Q(new Object[]{new Long(var5), new Integer(var10002)});
         var10.Vulcan__O(new Object[]{new Integer(93), new Long(var7)});
         this.Vulcan_y = var10.toString();
      }

      var10000 = this.Vulcan_y;
      return var10000;
   }

   public int[] Vulcan_B(Object[] var1) {
      long var2 = (Long)var1[0];
      var2 ^= a;
      String var10000 = Vulcan_eS.Vulcan_y();
      int[] var5 = new int[this.Vulcan_X - this.Vulcan_h + 1];
      int var6 = 0;
      String var4 = var10000;

      int[] var8;
      label33:
      while(var6 < var5.length) {
         while(true) {
            try {
               if (var2 > 0L) {
                  var8 = var5;
                  if (var4 != null) {
                     return var8;
                  }

                  var5[var6] = this.Vulcan_h + var6;
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
      String var2 = "\u0015r\u001d^\u0012U\u001b\u0013{\u0016\u0019\u0019{^%v\u0001\u0019\u001a{@33\u001dV\u0003.Q\"3\u001dL\u001bb\u001c\u0013{\u0016\u0019\u0019{^%v\u0001JWcF4gSW\u0018z\u0013%vSW\u0002b_";
      int var4 = "\u0015r\u001d^\u0012U\u001b\u0013{\u0016\u0019\u0019{^%v\u0001\u0019\u001a{@33\u001dV\u0003.Q\"3\u001dL\u001bb\u001c\u0013{\u0016\u0019\u0019{^%v\u0001JWcF4gSW\u0018z\u0013%vSW\u0002b_".length();
      char var1 = 6;
      int var0 = -1;

      while(true) {
         char[] var10001;
         label41: {
            ++var0;
            char[] var10002 = var2.substring(var0, var0 + var1).toCharArray();
            int var10003 = var10002.length;
            int var7 = 0;
            byte var10 = 118;
            var10001 = var10002;
            int var8 = var10003;
            byte var12;
            char[] var10004;
            int var10005;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 118;
               var10005 = var7;
            } else {
               var10 = 118;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label41;
               }

               var10004 = var10002;
               var12 = 118;
               var10005 = var7;
            }

            while(true) {
               char var22 = var10004[var10005];
               byte var23;
               switch(var7 % 7) {
               case 0:
                  var23 = 49;
                  break;
               case 1:
                  var23 = 101;
                  break;
               case 2:
                  var23 = 5;
                  break;
               case 3:
                  var23 = 79;
                  break;
               case 4:
                  var23 = 1;
                  break;
               case 5:
                  var23 = 120;
                  break;
               default:
                  var23 = 69;
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
