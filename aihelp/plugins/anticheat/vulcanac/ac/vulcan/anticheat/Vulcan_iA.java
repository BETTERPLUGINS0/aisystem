package ac.vulcan.anticheat;

import java.util.Collection;
import java.util.Set;

public class Vulcan_iA {
   private static final ThreadLocal Vulcan_h;
   private final int Vulcan_b = 37;
   private int Vulcan_m = 0;
   // $FF: synthetic field
   static Class Vulcan_Q;
   private static int Vulcan_G;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(1545461627108016300L, -9219804828989508557L, (Object)null).a(104458202828044L);
   private static final String[] b;

   static Set Vulcan_n(Object[] var0) {
      return (Set)Vulcan_h.get();
   }

   static boolean Vulcan_M(Object[] var0) {
      long var2 = (Long)var0[0];
      Object var1 = (Object)var0[1];
      long var10000 = a ^ var2;
      int var8 = Vulcan_V();
      Set var5 = Vulcan_n(new Object[0]);
      int var4 = var8;

      boolean var10;
      label46: {
         label34: {
            Set var9;
            label33: {
               try {
                  var9 = var5;
                  if (var4 == 0) {
                     break label33;
                  }

                  if (var5 == null) {
                     break label34;
                  }
               } catch (IllegalArgumentException var7) {
                  throw a(var7);
               }

               var9 = var5;
            }

            try {
               var10 = var9.contains(new Vulcan_i1(var1));
               if (var4 == 0) {
                  return var10;
               }

               if (var10) {
                  break label46;
               }
            } catch (IllegalArgumentException var6) {
               throw a(var6);
            }
         }

         var10 = false;
         return var10;
      }

      var10 = true;
      return var10;
   }

   private static void Vulcan_y(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_C(Object[] var0) {
      int var4 = (Integer)var0[0];
      long var2 = (Long)var0[1];
      int var5 = (Integer)var0[2];
      Object var1 = (Object)var0[3];
      var2 ^= a;
      long var6 = var2 ^ 1315341863336L;
      return Vulcan_z(new Object[]{new Integer(var4), new Integer(var5), var1, new Boolean(false), null, new Long(var6), null});
   }

   public static int Vulcan_v(Object[] var0) {
      int var5 = (Integer)var0[0];
      int var1 = (Integer)var0[1];
      Object var4 = (Object)var0[2];
      long var2 = (Long)var0[3];
      boolean var6 = (Boolean)var0[4];
      var2 ^= a;
      long var7 = var2 ^ 131867873477596L;
      return Vulcan_z(new Object[]{new Integer(var5), new Integer(var1), var4, new Boolean(var6), null, new Long(var7), null});
   }

   public static int Vulcan_T(Object[] var0) {
      int var6 = (Integer)var0[0];
      int var4 = (Integer)var0[1];
      Object var7 = (Object)var0[2];
      boolean var1 = (Boolean)var0[3];
      Class var5 = (Class)var0[4];
      long var2 = (Long)var0[5];
      var2 ^= a;
      long var8 = var2 ^ 12828791413457L;
      return Vulcan_z(new Object[]{new Integer(var6), new Integer(var4), var7, new Boolean(var1), var5, new Long(var8), null});
   }

   public static int Vulcan_z(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_r(Object[] var0) {
      long var2 = (Long)var0[0];
      Object var1 = (Object)var0[1];
      var2 ^= a;
      long var4 = var2 ^ 79545079815273L;
      return Vulcan_z(new Object[]{new Integer(17), new Integer(37), var1, new Boolean(false), null, new Long(var4), null});
   }

   public static int Vulcan_o(Object[] var0) {
      long var2 = (Long)var0[0];
      Object var1 = (Object)var0[1];
      boolean var4 = (Boolean)var0[2];
      var2 ^= a;
      long var5 = var2 ^ 68408020858348L;
      return Vulcan_z(new Object[]{new Integer(17), new Integer(37), var1, new Boolean(var4), null, new Long(var5), null});
   }

   public static int Vulcan_m(Object[] var0) {
      Object var1 = (Object)var0[0];
      Collection var4 = (Collection)var0[1];
      long var2 = (Long)var0[2];
      var2 ^= a;
      long var5 = var2 ^ 55371300010352L;
      long var7 = var2 ^ 120712953625678L;
      return Vulcan_n(new Object[]{var1, new Long(var5), Vulcan_Xn.Vulcan_y(new Object[]{new Long(var7), var4})});
   }

   public static int Vulcan_n(Object[] var0) {
      Object var3 = (Object)var0[0];
      long var1 = (Long)var0[1];
      String[] var4 = (String[])var0[2];
      var1 ^= a;
      long var5 = var1 ^ 83787712166436L;
      return Vulcan_z(new Object[]{new Integer(17), new Integer(37), var3, new Boolean(false), null, new Long(var5), var4});
   }

   static void Vulcan_x(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   static void Vulcan_e(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_iA() {
      this.Vulcan_m = 17;
   }

   public Vulcan_iA(int param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_iA Vulcan_p(Object[] var1) {
      byte var4 = (Boolean)var1[0];
      long var2 = (Long)var1[1];
      long var10000 = a ^ var2;
      int var5 = Vulcan_V();

      int var10001;
      byte var10002;
      Vulcan_iA var7;
      label22: {
         label21: {
            try {
               var7 = this;
               var10001 = this.Vulcan_m * this.Vulcan_b;
               var10002 = var4;
               if (var5 == 0) {
                  break label22;
               }

               if (var4 != 0) {
                  break label21;
               }
            } catch (IllegalArgumentException var6) {
               throw a(var6);
            }

            var10002 = 1;
            break label22;
         }

         var10002 = 0;
      }

      var7.Vulcan_m = var10001 + var10002;
      return this;
   }

   public Vulcan_iA Vulcan_I(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_iA Vulcan_m(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_m = this.Vulcan_m * this.Vulcan_b + var2;
      return this;
   }

   public Vulcan_iA Vulcan_R(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_iA Vulcan_X(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_m = this.Vulcan_m * this.Vulcan_b + var2;
      return this;
   }

   public Vulcan_iA Vulcan_e(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_iA Vulcan_g(Object[] var1) {
      double var2 = (Double)var1[0];
      return this.Vulcan_y(new Object[]{new Long(Double.doubleToLongBits(var2))});
   }

   public Vulcan_iA Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_iA Vulcan_U(Object[] var1) {
      float var2 = (Float)var1[0];
      this.Vulcan_m = this.Vulcan_m * this.Vulcan_b + Float.floatToIntBits(var2);
      return this;
   }

   public Vulcan_iA Vulcan_J(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_iA Vulcan_w(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_m = this.Vulcan_m * this.Vulcan_b + var2;
      return this;
   }

   public Vulcan_iA Vulcan_n(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_iA Vulcan_y(Object[] var1) {
      long var2 = (Long)var1[0];
      this.Vulcan_m = this.Vulcan_m * this.Vulcan_b + (int)(var2 ^ var2 >> 32);
      return this;
   }

   public Vulcan_iA Vulcan_i(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_iA Vulcan_k(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_iA Vulcan_f(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_iA Vulcan_a(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_m = this.Vulcan_m * this.Vulcan_b + var2;
      return this;
   }

   public Vulcan_iA Vulcan_b(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_iA Vulcan_x(Object[] var1) {
      int var2 = (Integer)var1[0];
      this.Vulcan_m = this.Vulcan_m * this.Vulcan_b + var2;
      return this;
   }

   public int Vulcan_U(Object[] var1) {
      return this.Vulcan_m;
   }

   public int hashCode() {
      return this.Vulcan_U(new Object[0]);
   }

   // $FF: synthetic method
   static Class Vulcan_O(Object[] var0) {
      String var1 = (String)var0[0];

      try {
         return Class.forName(me.frep.vulcan.spigot.Vulcan_s.a(var1));
      } catch (ClassNotFoundException var3) {
         throw new NoClassDefFoundError(var3.getMessage());
      }
   }

   static {
      String[] var5 = new String[8];
      int var3 = 0;
      String var2 = "-D!\u0001\u0002&h\fO Y;/g\u001dM%\u00153 h\u001dY7<\n n\b^-\u0016\u001c4,B!Y\u001d!a\u001dI0Y\u0006,+\u001a_-\u0015\u0016cjXB%\n\u001ach\u0017N!Y\u0014,yXG1\n\u0006ce\u0017^d\u001b\u0017ce\rF(\u001d\u0019Ij\u000f\u0007/h\u0019Dj\u0018\u001c7b\u001bB!\u0018\u0006m]\rF'\u0018\u001c\u001cb9-0K7\u00111,o\u001dh1\u0010\u001e'n\n\n6\u001c\u00036b\nO7Y\u0013-+\u0017N Y\u001b-b\fC%\u0015R5j\u0014_!\u001d\u0019Ij\u000f\u0007/h\u0019Dj\u0018\u001c7b\u001bB!\u0018\u0006m]\rF'\u0018\u001c\u001cb9.0K7\u00111,o\u001dh1\u0010\u001e'n\n\n6\u001c\u00036b\nO7Y\u0013ce\u0017Dd\u0003\u00171dXG1\u0015\u0006*{\u0014C!\u000b";
      int var4 = "-D!\u0001\u0002&h\fO Y;/g\u001dM%\u00153 h\u001dY7<\n n\b^-\u0016\u001c4,B!Y\u001d!a\u001dI0Y\u0006,+\u001a_-\u0015\u0016cjXB%\n\u001ach\u0017N!Y\u0014,yXG1\n\u0006ce\u0017^d\u001b\u0017ce\rF(\u001d\u0019Ij\u000f\u0007/h\u0019Dj\u0018\u001c7b\u001bB!\u0018\u0006m]\rF'\u0018\u001c\u001cb9-0K7\u00111,o\u001dh1\u0010\u001e'n\n\n6\u001c\u00036b\nO7Y\u0013-+\u0017N Y\u001b-b\fC%\u0015R5j\u0014_!\u001d\u0019Ij\u000f\u0007/h\u0019Dj\u0018\u001c7b\u001bB!\u0018\u0006m]\rF'\u0018\u001c\u001cb9.0K7\u00111,o\u001dh1\u0010\u001e'n\n\n6\u001c\u00036b\nO7Y\u0013ce\u0017Dd\u0003\u00171dXG1\u0015\u0006*{\u0014C!\u000b".length();
      Vulcan_d(13);
      char var1 = '!';
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
            var10 = 94;
            var10001 = var10002;
            var8 = var10003;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 94;
               var10005 = var7;
            } else {
               var10 = 94;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label83;
               }

               var10004 = var10002;
               var12 = 94;
               var10005 = var7;
            }

            while(true) {
               var22 = var10004[var10005];
               switch(var7 % 7) {
               case 0:
                  var23 = 38;
                  break;
               case 1:
                  var23 = 116;
                  break;
               case 2:
                  var23 = 26;
                  break;
               case 3:
                  var23 = 39;
                  break;
               case 4:
                  var23 = 44;
                  break;
               case 5:
                  var23 = 29;
                  break;
               default:
                  var23 = 85;
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
            var2 = "\u001ee\u0019?\u001f\u0002A3F\u001f>0\t@$$\u00182-\u0018L$a\u0019w=MK9jJ-9\u001fJvm\u0004>(\u0004D:$\u001c60\u0018@*\u001ee\u0019?\u001f\u0002A3F\u001f>0\t@$$\u00182-\u0018L$a\u0019w=\u0003\u00059`\u000ew1\u0018I\"m\u001a;5\bW";
            var4 = "\u001ee\u0019?\u001f\u0002A3F\u001f>0\t@$$\u00182-\u0018L$a\u0019w=MK9jJ-9\u001fJvm\u0004>(\u0004D:$\u001c60\u0018@*\u001ee\u0019?\u001f\u0002A3F\u001f>0\t@$$\u00182-\u0018L$a\u0019w=\u0003\u00059`\u000ew1\u0018I\"m\u001a;5\bW".length();
            var1 = '1';
            var0 = -1;

            while(true) {
               label62: {
                  ++var0;
                  var10002 = var2.substring(var0, var0 + var1).toCharArray();
                  var10003 = var10002.length;
                  var7 = 0;
                  var10 = 112;
                  var10001 = var10002;
                  var8 = var10003;
                  if (var10003 <= 1) {
                     var10004 = var10002;
                     var12 = 112;
                     var10005 = var7;
                  } else {
                     var10 = 112;
                     var8 = var10003;
                     if (var10003 <= var7) {
                        break label62;
                     }

                     var10004 = var10002;
                     var12 = 112;
                     var10005 = var7;
                  }

                  while(true) {
                     var22 = var10004[var10005];
                     switch(var7 % 7) {
                     case 0:
                        var23 = 38;
                        break;
                     case 1:
                        var23 = 116;
                        break;
                     case 2:
                        var23 = 26;
                        break;
                     case 3:
                        var23 = 39;
                        break;
                     case 4:
                        var23 = 44;
                        break;
                     case 5:
                        var23 = 29;
                        break;
                     default:
                        var23 = 85;
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
                  Vulcan_h = new ThreadLocal();
                  return;
               }

               var1 = var2.charAt(var0);
            }
         }

         var1 = var2.charAt(var0);
      }
   }

   public static void Vulcan_d(int var0) {
      Vulcan_G = var0;
   }

   public static int Vulcan_V() {
      return Vulcan_G;
   }

   public static int Vulcan_O() {
      int var0 = Vulcan_V();

      try {
         return var0 == 0 ? 5 : 0;
      } catch (IllegalArgumentException var1) {
         throw a(var1);
      }
   }

   private static Exception a(Exception var0) {
      return var0;
   }
}
