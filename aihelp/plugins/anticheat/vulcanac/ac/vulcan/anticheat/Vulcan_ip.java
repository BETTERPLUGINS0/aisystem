package ac.vulcan.anticheat;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class Vulcan_ip {
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-8082982490676038653L, -8687661191788283977L, (Object)null).a(18800650279571L);
   private static final String[] b;

   public static void Vulcan_x(Object[] var0) {
      boolean var1 = (Boolean)var0[0];
      long var4 = (Long)var0[1];
      String var2 = (String)var0[2];
      Object var3 = (Object)var0[3];
      long var10000 = a ^ var4;

      try {
         if (!var1) {
            throw new IllegalArgumentException(var2 + var3);
         }
      } catch (IllegalArgumentException var6) {
         throw a(var6);
      }
   }

   public static void Vulcan_O(Object[] var0) {
      boolean var2 = (Boolean)var0[0];
      String var1 = (String)var0[1];
      long var3 = (Long)var0[2];
      long var5 = (Long)var0[3];
      long var10000 = a ^ var5;

      try {
         if (!var2) {
            throw new IllegalArgumentException(var1 + var3);
         }
      } catch (IllegalArgumentException var7) {
         throw a(var7);
      }
   }

   public static void Vulcan_v(Object[] var0) {
      boolean var6 = (Boolean)var0[0];
      String var1 = (String)var0[1];
      double var2 = (Double)var0[2];
      long var4 = (Long)var0[3];
      long var10000 = a ^ var4;

      try {
         if (!var6) {
            throw new IllegalArgumentException(var1 + var2);
         }
      } catch (IllegalArgumentException var7) {
         throw a(var7);
      }
   }

   public static void Vulcan_k(Object[] var0) {
      boolean var1 = (Boolean)var0[0];
      String var2 = (String)var0[1];
      long var3 = (Long)var0[2];
      long var10000 = a ^ var3;

      try {
         if (!var1) {
            throw new IllegalArgumentException(var2);
         }
      } catch (IllegalArgumentException var5) {
         throw a(var5);
      }
   }

   public static void Vulcan_p(Object[] var0) {
      long var2 = (Long)var0[0];
      boolean var1 = (Boolean)var0[1];
      long var10000 = a ^ var2;

      try {
         if (!var1) {
            throw new IllegalArgumentException(b[0]);
         }
      } catch (IllegalArgumentException var4) {
         throw a(var4);
      }
   }

   public static void Vulcan_u(Object[] var0) {
      Object var3 = (Object)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 9682598315682L;
      String var10002 = b[3];
      Vulcan_d(new Object[]{new Long(var4), var3, var10002});
   }

   public static void Vulcan_d(Object[] var0) {
      long var3 = (Long)var0[0];
      Object var2 = (Object)var0[1];
      String var1 = (String)var0[2];
      long var10000 = a ^ var3;

      try {
         if (var2 == null) {
            throw new IllegalArgumentException(var1);
         }
      } catch (IllegalArgumentException var5) {
         throw a(var5);
      }
   }

   public static void Vulcan_n(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_w(Object[] var0) {
      Object[] var1 = (Object[])var0[0];
      long var2 = (Long)var0[1];
      var2 ^= a;
      long var4 = var2 ^ 23649966901552L;
      String var10002 = b[1];
      Vulcan_n(new Object[]{new Long(var4), var1, var10002});
   }

   public static void Vulcan_s(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_o(Object[] var0) {
      Collection var3 = (Collection)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 122784619775076L;
      Vulcan_s(new Object[]{var3, b[4], new Long(var4)});
   }

   public static void Vulcan_t(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_J(Object[] var0) {
      long var2 = (Long)var0[0];
      Map var1 = (Map)var0[1];
      var2 ^= a;
      long var4 = var2 ^ 41420780897714L;
      Vulcan_t(new Object[]{var1, b[7], new Long(var4)});
   }

   public static void Vulcan_H(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_z(Object[] var0) {
      String var3 = (String)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 121257277114271L;
      Vulcan_H(new Object[]{var3, b[8], new Long(var4)});
   }

   public static void Vulcan_R(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_P(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_F(Object[] var0) {
      Collection var1 = (Collection)var0[0];
      String var2 = (String)var0[1];
      long var3 = (Long)var0[2];
      var3 ^= a;
      long var5 = var3 ^ 71410393314162L;
      String[] var10000 = Vulcan_XL.Vulcan_v();
      Vulcan_u(new Object[]{var1, new Long(var5)});
      Iterator var8 = var1.iterator();
      String[] var7 = var10000;

      while(var8.hasNext()) {
         Object var9 = var8.next();

         while(var9 == null) {
            var9 = new IllegalArgumentException(var2);
            if (var3 >= 0L && var7 != null) {
               throw var9;
            }
         }
      }

   }

   public static void Vulcan_V(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan__(Object[] var0) {
      long var4 = (Long)var0[0];
      Collection var3 = (Collection)var0[1];
      Class var1 = (Class)var0[2];
      String var2 = (String)var0[3];
      var4 ^= a;
      long var6 = var4 ^ 82666487022199L;
      Vulcan_u(new Object[]{var3, new Long(var6)});
      Vulcan_u(new Object[]{var1, new Long(var6)});
      Iterator var8 = var3.iterator();

      do {
         if (!var8.hasNext()) {
            return;
         }
      } while(var1.isInstance(var8.next()));

      throw new IllegalArgumentException(var2);
   }

   public static void Vulcan_W(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   static {
      String[] var5 = new String[10];
      int var3 = 0;
      String var2 = "\u0013vA\u0018\u0017J\u0017.zEL\u0004O[\"fTJ\u0004X\b.qJ\u0018\bX[!\u007fHK\u0004\u001c\u0013vA\u0018\u0017J\u0017.zEL\u0004O[&lVY\u0018\u000b\u00124>AU\u0011_\u0002\u000bg\u007fP\u0018\bE\u001f\"f\u001e\u0018\u001c\u0013vA\u0018\u0017J\u0017.zEL\u0004O[(|N]\u0002_[.m\u0004V\u0014G\u0017!\u0013vA\u0018\u0017J\u0017.zEL\u0004O[$qHT\u0004H\u000f.qJ\u0018\bX[\"sTL\u00184\u0013vA\u0018\u0017J\u0017.zEL\u0004O[&lVY\u0018\u000b\u0018(pPY\bE\bgpQT\r\u000b\u001e+{I]\u000f_[&j\u0004Q\u000fO\u001e?$\u00049\u0013vA\u0018\u0017J\u0017.zEL\u0004O[$qHT\u0004H\u000f.qJ\u0018\u0002D\u00153\u007fMV\u0012\u000b\u00152rH\u0018\u0004G\u001e*{JLAJ\u000fgwJ\\\u0004SAg\u001a\u0013vA\u0018\u0017J\u0017.zEL\u0004O[*\u007fT\u0018\bX[\"sTL\u0018";
      int var4 = "\u0013vA\u0018\u0017J\u0017.zEL\u0004O[\"fTJ\u0004X\b.qJ\u0018\bX[!\u007fHK\u0004\u001c\u0013vA\u0018\u0017J\u0017.zEL\u0004O[&lVY\u0018\u000b\u00124>AU\u0011_\u0002\u000bg\u007fP\u0018\bE\u001f\"f\u001e\u0018\u001c\u0013vA\u0018\u0017J\u0017.zEL\u0004O[(|N]\u0002_[.m\u0004V\u0014G\u0017!\u0013vA\u0018\u0017J\u0017.zEL\u0004O[$qHT\u0004H\u000f.qJ\u0018\bX[\"sTL\u00184\u0013vA\u0018\u0017J\u0017.zEL\u0004O[&lVY\u0018\u000b\u0018(pPY\bE\bgpQT\r\u000b\u001e+{I]\u000f_[&j\u0004Q\u000fO\u001e?$\u00049\u0013vA\u0018\u0017J\u0017.zEL\u0004O[$qHT\u0004H\u000f.qJ\u0018\u0002D\u00153\u007fMV\u0012\u000b\u00152rH\u0018\u0004G\u001e*{JLAJ\u000fgwJ\\\u0004SAg\u001a\u0013vA\u0018\u0017J\u0017.zEL\u0004O[*\u007fT\u0018\bX[\"sTL\u0018".length();
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
                  var23 = 25;
                  break;
               case 1:
                  var23 = 64;
                  break;
               case 2:
                  var23 = 122;
                  break;
               case 3:
                  var23 = 102;
                  break;
               case 4:
                  var23 = 63;
                  break;
               case 5:
                  var23 = 117;
                  break;
               default:
                  var23 = 37;
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
            var2 = "\u001e{L\u0015\u001aG\u001a#wHA\tBV9g[\\\u0002AV#`\tP\u0001V\u000239\u001e{L\u0015\u001aG\u001a#wHA\tBV)|EY\tE\u0002#|G\u0015\u000fI\u0018>r@[\u001f\u0006\u0017$3LY\tK\u0013$g\t[\u0003RV%u\tA\u0015V\u0013j";
            var4 = "\u001e{L\u0015\u001aG\u001a#wHA\tBV9g[\\\u0002AV#`\tP\u0001V\u000239\u001e{L\u0015\u001aG\u001a#wHA\tBV)|EY\tE\u0002#|G\u0015\u000fI\u0018>r@[\u001f\u0006\u0017$3LY\tK\u0013$g\t[\u0003RV%u\tA\u0015V\u0013j".length();
            var1 = 29;
            var0 = -1;

            while(true) {
               label62: {
                  ++var0;
                  var10002 = var2.substring(var0, var0 + var1).toCharArray();
                  var10003 = var10002.length;
                  var7 = 0;
                  var10 = 83;
                  var10001 = var10002;
                  var8 = var10003;
                  if (var10003 <= 1) {
                     var10004 = var10002;
                     var12 = 83;
                     var10005 = var7;
                  } else {
                     var10 = 83;
                     var8 = var10003;
                     if (var10003 <= var7) {
                        break label62;
                     }

                     var10004 = var10002;
                     var12 = 83;
                     var10005 = var7;
                  }

                  while(true) {
                     var22 = var10004[var10005];
                     switch(var7 % 7) {
                     case 0:
                        var23 = 25;
                        break;
                     case 1:
                        var23 = 64;
                        break;
                     case 2:
                        var23 = 122;
                        break;
                     case 3:
                        var23 = 102;
                        break;
                     case 4:
                        var23 = 63;
                        break;
                     case 5:
                        var23 = 117;
                        break;
                     default:
                        var23 = 37;
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

   private static IllegalArgumentException a(IllegalArgumentException var0) {
      return var0;
   }
}
