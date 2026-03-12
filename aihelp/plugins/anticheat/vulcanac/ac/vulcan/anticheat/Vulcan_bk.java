package ac.vulcan.anticheat;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Vulcan_bk implements Serializable {
   private static final long serialVersionUID = 5947847346149275958L;
   public static final Vulcan_bk Vulcan_v;
   public static final Vulcan_bk Vulcan_p;
   public static final Vulcan_bk Vulcan_P;
   public static final Vulcan_bk Vulcan_i;
   public static final Vulcan_bk Vulcan_E;
   protected static final Map Vulcan_K;
   private final Set Vulcan_I;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(4308867313004496756L, -2530189219806076047L, (Object)null).a(90330950422263L);

   public static Vulcan_bk Vulcan_Y(Object[] var0) {
      long var1 = (Long)var0[0];
      String var3 = (String)var0[1];
      long var10000 = a ^ var1;
      String[] var7 = Vulcan_XL.Vulcan_v();
      Object var5 = Vulcan_K.get(var3);
      String[] var4 = var7;

      Object var8;
      try {
         var8 = var5;
         if (var4 == null) {
            return (Vulcan_bk)var8;
         }

         if (var5 == null) {
            return new Vulcan_bk(var3);
         }
      } catch (RuntimeException var6) {
         throw a(var6);
      }

      var8 = var5;
      return (Vulcan_bk)var8;
   }

   public static Vulcan_bk Vulcan_w(Object[] var0) {
      long var2 = (Long)var0[0];
      String[] var1 = (String[])var0[1];
      long var10000 = a ^ var2;

      try {
         if (var1 == null) {
            return null;
         }
      } catch (RuntimeException var4) {
         throw a(var4);
      }

      return new Vulcan_bk(var1);
   }

   protected Vulcan_bk(String var1) {
      long var2 = a ^ 136941457988150L;
      long var4 = var2 ^ 109307518744180L;
      super();
      this.Vulcan_I = Collections.synchronizedSet(new HashSet());
      this.Vulcan_d(new Object[]{new Long(var4), var1});
   }

   protected Vulcan_bk(String[] var1) {
      long var2 = a ^ 11077591945157L;
      long var4 = var2 ^ 24142997414791L;
      String[] var10000 = Vulcan_XL.Vulcan_v();
      super();
      String[] var6 = var10000;
      this.Vulcan_I = Collections.synchronizedSet(new HashSet());
      int var7 = var1.length;
      int var8 = 0;

      while(var8 < var7) {
         this.Vulcan_d(new Object[]{new Long(var4), var1[var8]});
         ++var8;
         if (var6 == null) {
            break;
         }
      }

   }

   protected void Vulcan_d(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_XX[] Vulcan_x(Object[] var1) {
      return (Vulcan_XX[])((Vulcan_XX[])this.Vulcan_I.toArray(new Vulcan_XX[this.Vulcan_I.size()]));
   }

   public boolean Vulcan_g(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean equals(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public int hashCode() {
      return 89 + this.Vulcan_I.hashCode();
   }

   public String toString() {
      return this.Vulcan_I.toString();
   }

   static {
      String[] var6 = new String[9];
      int var4 = 0;
      String var3 = "wc)E\t.\u0003Wc\t\u0003&cj\u0003wc)\u0006Wc\te\t\u000e\u0003wc)\u0006wc)E\t.";
      int var5 = "wc)E\t.\u0003Wc\t\u0003&cj\u0003wc)\u0006Wc\te\t\u000e\u0003wc)\u0006wc)E\t.".length();
      char var2 = 6;
      int var1 = -1;

      while(true) {
         int var8;
         int var9;
         byte var11;
         byte var13;
         char[] var10001;
         char[] var10002;
         int var10003;
         char[] var10004;
         int var10005;
         char var23;
         byte var24;
         label83: {
            ++var1;
            var10002 = var3.substring(var1, var1 + var2).toCharArray();
            var10003 = var10002.length;
            var8 = 0;
            var11 = 74;
            var10001 = var10002;
            var9 = var10003;
            if (var10003 <= 1) {
               var10004 = var10002;
               var13 = 74;
               var10005 = var8;
            } else {
               var11 = 74;
               var9 = var10003;
               if (var10003 <= var8) {
                  break label83;
               }

               var10004 = var10002;
               var13 = 74;
               var10005 = var8;
            }

            while(true) {
               var23 = var10004[var10005];
               switch(var8 % 7) {
               case 0:
                  var24 = 92;
                  break;
               case 1:
                  var24 = 4;
                  break;
               case 2:
                  var24 = 25;
                  break;
               case 3:
                  var24 = 78;
                  break;
               case 4:
                  var24 = 110;
                  break;
               case 5:
                  var24 = 62;
                  break;
               default:
                  var24 = 73;
               }

               var10004[var10005] = (char)(var23 ^ var13 ^ var24);
               ++var8;
               if (var11 == 0) {
                  var10005 = var11;
                  var10004 = var10001;
                  var13 = var11;
               } else {
                  if (var9 <= var8) {
                     break;
                  }

                  var10004 = var10001;
                  var13 = var11;
                  var10005 = var8;
               }
            }
         }

         var6[var4++] = (new String(var10001)).intern();
         if ((var1 += var2) >= var5) {
            var3 = "%\u0011{\u0003T\u0011\u0018";
            var5 = "%\u0011{\u0003T\u0011\u0018".length();
            var2 = 3;
            var1 = -1;

            while(true) {
               label62: {
                  ++var1;
                  var10002 = var3.substring(var1, var1 + var2).toCharArray();
                  var10003 = var10002.length;
                  var8 = 0;
                  var11 = 56;
                  var10001 = var10002;
                  var9 = var10003;
                  if (var10003 <= 1) {
                     var10004 = var10002;
                     var13 = 56;
                     var10005 = var8;
                  } else {
                     var11 = 56;
                     var9 = var10003;
                     if (var10003 <= var8) {
                        break label62;
                     }

                     var10004 = var10002;
                     var13 = 56;
                     var10005 = var8;
                  }

                  while(true) {
                     var23 = var10004[var10005];
                     switch(var8 % 7) {
                     case 0:
                        var24 = 92;
                        break;
                     case 1:
                        var24 = 4;
                        break;
                     case 2:
                        var24 = 25;
                        break;
                     case 3:
                        var24 = 78;
                        break;
                     case 4:
                        var24 = 110;
                        break;
                     case 5:
                        var24 = 62;
                        break;
                     default:
                        var24 = 73;
                     }

                     var10004[var10005] = (char)(var23 ^ var13 ^ var24);
                     ++var8;
                     if (var11 == 0) {
                        var10005 = var11;
                        var10004 = var10001;
                        var13 = var11;
                     } else {
                        if (var9 <= var8) {
                           break;
                        }

                        var10004 = var10001;
                        var13 = var11;
                        var10005 = var8;
                     }
                  }
               }

               var6[var4++] = (new String(var10001)).intern();
               if ((var1 += var2) >= var5) {
                  Vulcan_v = new Vulcan_bk((String)null);
                  Vulcan_p = new Vulcan_bk(var6[6]);
                  Vulcan_P = new Vulcan_bk(var6[3]);
                  Vulcan_i = new Vulcan_bk(var6[7]);
                  Vulcan_E = new Vulcan_bk(var6[2]);
                  Vulcan_K = Collections.synchronizedMap(new HashMap());
                  Vulcan_K.put((Object)null, Vulcan_v);
                  Vulcan_K.put("", Vulcan_v);
                  Vulcan_K.put(var6[0], Vulcan_p);
                  Vulcan_K.put(var6[4], Vulcan_p);
                  Vulcan_K.put(var6[5], Vulcan_P);
                  Vulcan_K.put(var6[1], Vulcan_i);
                  Vulcan_K.put(var6[8], Vulcan_E);
                  return;
               }

               var2 = var3.charAt(var1);
            }
         }

         var2 = var3.charAt(var1);
      }
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
