package ac.vulcan.anticheat;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import me.frep.vulcan.spigot.check.AbstractCheck;

public abstract class Vulcan_z implements Comparable, Serializable {
   private static final long serialVersionUID = -487045951170455942L;
   private static final Map Vulcan_g;
   private static Map Vulcan_B;
   private final String Vulcan_m;
   private final transient int Vulcan_x;
   protected transient String Vulcan_W;
   static Class Vulcan_r;
   static Class Vulcan_V;
   private static String[] Vulcan_k;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-7980145911009783519L, 430830084923213431L, (Object)null).a(9921209145421L);
   private static final String[] c;

   protected Vulcan_z(String var1) {
      long var2 = a ^ 132931304108456L;
      long var4 = var2 ^ 49712283918660L;
      String[] var10000 = Vulcan_X();
      super();
      this.Vulcan_W = null;
      String[] var6 = var10000;
      this.Vulcan_Y(new Object[]{new Long(var4), var1});
      this.Vulcan_m = var1;
      this.Vulcan_x = 7 + this.Vulcan_T(new Object[0]).hashCode() + 3 * var1.hashCode();
      if (var6 != null) {
         int var7 = AbstractCheck.Vulcan_m();
         ++var7;
         AbstractCheck.Vulcan_H(var7);
      }

   }

   private void Vulcan_Y(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected Object readResolve(int param1, short param2, int param3) {
      // $FF: Couldn't be decompiled
   }

   protected static Vulcan_z Vulcan_m(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   protected static Map Vulcan_Z(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   protected static List Vulcan_x(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   protected static Iterator Vulcan_D(Object[] var0) {
      long var1 = (Long)var0[0];
      Class var3 = (Class)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 14348987302975L;
      return Vulcan_x(new Object[]{var3, new Long(var4)}).iterator();
   }

   private static Vulcan_Xg Vulcan_G(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static Vulcan_Xg Vulcan_y(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public final String Vulcan_f(Object[] var1) {
      return this.Vulcan_m;
   }

   public Class Vulcan_T(Object[] var1) {
      return this.getClass();
   }

   public final boolean equals(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public final int hashCode() {
      return this.Vulcan_x;
   }

   public int compareTo(Object param1) {
      // $FF: Couldn't be decompiled
   }

   private String Vulcan_Y(Object[] var1) {
      Object var2 = (Object)var1[0];

      try {
         Class var10000 = var2.getClass();
         String[] var5 = c;
         Method var3 = var10000.getMethod(me.frep.vulcan.spigot.Vulcan_s.b((String)var5[5], (Class)var10000, (Class[])null), (Class[])null);
         String var4 = (String)var3.invoke(var2, (Object[])null);
         return var4;
      } catch (NoSuchMethodException var6) {
      } catch (IllegalAccessException var7) {
      } catch (InvocationTargetException var8) {
      }

      throw new IllegalStateException(c[2]);
   }

   public String toString() {
      long var1 = a ^ 111577077837515L;
      long var3 = var1 ^ 106805069994618L;
      String[] var5 = Vulcan_X();

      String var10000;
      label20: {
         try {
            var10000 = this.Vulcan_W;
            if (var5 != null) {
               return var10000;
            }

            if (var10000 != null) {
               break label20;
            }
         } catch (IllegalArgumentException var7) {
            throw a(var7);
         }

         String var6 = Vulcan_eo.Vulcan_B(new Object[]{this.Vulcan_T(new Object[0]), new Long(var3)});
         this.Vulcan_W = var6 + "[" + this.Vulcan_f(new Object[0]) + "]";
      }

      var10000 = this.Vulcan_W;
      return var10000;
   }

   static Class Vulcan_Q(Object[] var0) {
      String var1 = (String)var0[0];

      try {
         return Class.forName(me.frep.vulcan.spigot.Vulcan_s.a(var1));
      } catch (ClassNotFoundException var3) {
         throw new NoClassDefFoundError(var3.getMessage());
      }
   }

   static {
      String[] var5 = new String[14];
      Vulcan_e((String[])null);
      int var3 = 0;
      String var2 = "Cn-|*8aduh1\u001c't7d-|\btsbd+0\b's7i.|,:uz\u001cvef*\u001c8cvhf=\u0007 itn-=\u001dzVbj+=\u0007\u000bZ\u0016Cn!/I'hxs$8I:oc& =\u0019$ey\u0016So.:\f&eyrh9\u0007!m7e$=\u001a' 0\u00180& =\u001ata{t-=\r- uc-2I5dsc,\u0007pc<\u0012\b9e5pc<\u0019\u0007!mTj)/\u001a|)7k=/\u001dtrrr=.\u0007ta7u=,\f&c{g;/I;f7r 5\u001atc{g;/\u001fpc<\u0019\u0007!mTj)/\u001a|)7k=/\u001dtnxrh>\ftnbj$\u001fCn-|,:uz&\u000b0\b's7k=/\u001dtnxrh>\ftnbj$\u001cvef*\u001c8cvhf=\u0007 itn-=\u001dzVbj+=\u0007\u000bz\u001fCn-|,:uz&&=\u00041 zs;(I6e7s&5\u0018!e;&o\u001cvef*\u001c8cvhf=\u0007 itn-=\u001dzVbj+=\u0007\u000bz";
      int var4 = "Cn-|*8aduh1\u001c't7d-|\btsbd+0\b's7i.|,:uz\u001cvef*\u001c8cvhf=\u0007 itn-=\u001dzVbj+=\u0007\u000bZ\u0016Cn!/I'hxs$8I:oc& =\u0019$ey\u0016So.:\f&eyrh9\u0007!m7e$=\u001a' 0\u00180& =\u001ata{t-=\r- uc-2I5dsc,\u0007pc<\u0012\b9e5pc<\u0019\u0007!mTj)/\u001a|)7k=/\u001dtrrr=.\u0007ta7u=,\f&c{g;/I;f7r 5\u001atc{g;/\u001fpc<\u0019\u0007!mTj)/\u001a|)7k=/\u001dtnxrh>\ftnbj$\u001fCn-|,:uz&\u000b0\b's7k=/\u001dtnxrh>\ftnbj$\u001cvef*\u001c8cvhf=\u0007 itn-=\u001dzVbj+=\u0007\u000bz\u001fCn-|,:uz&&=\u00041 zs;(I6e7s&5\u0018!e;&o\u001cvef*\u001c8cvhf=\u0007 itn-=\u001dzVbj+=\u0007\u000bz".length();
      char var1 = '$';
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
            var10 = 78;
            var10001 = var10002;
            var8 = var10003;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 78;
               var10005 = var7;
            } else {
               var10 = 78;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label83;
               }

               var10004 = var10002;
               var12 = 78;
               var10005 = var7;
            }

            while(true) {
               var22 = var10004[var10005];
               switch(var7 % 7) {
               case 0:
                  var23 = 89;
                  break;
               case 1:
                  var23 = 72;
                  break;
               case 2:
                  var23 = 6;
                  break;
               case 3:
                  var23 = 18;
                  break;
               case 4:
                  var23 = 39;
                  break;
               case 5:
                  var23 = 26;
                  break;
               default:
                  var23 = 78;
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
            var2 = "9\u0014W\u0006V@\u000f\u0000\\\\G~KZ\u0000\tAR3@\u0015\u0019\\PC3K\u0017\u001d\bK\u0006|\\Z\u0003\t^J\u001c\f\u001f\u001cPfB\u0019\f\u0012\u001cG}Z\u0013\u000e\u0014WGg\u0000,\u0018\u0010QG}q ";
            var4 = "9\u0014W\u0006V@\u000f\u0000\\\\G~KZ\u0000\tAR3@\u0015\u0019\\PC3K\u0017\u001d\bK\u0006|\\Z\u0003\t^J\u001c\f\u001f\u001cPfB\u0019\f\u0012\u001cG}Z\u0013\u000e\u0014WGg\u0000,\u0018\u0010QG}q ".length();
            var1 = '\'';
            var0 = -1;

            while(true) {
               label62: {
                  ++var0;
                  var10002 = var2.substring(var0, var0 + var1).toCharArray();
                  var10003 = var10002.length;
                  var7 = 0;
                  var10 = 52;
                  var10001 = var10002;
                  var8 = var10003;
                  if (var10003 <= 1) {
                     var10004 = var10002;
                     var12 = 52;
                     var10005 = var7;
                  } else {
                     var10 = 52;
                     var8 = var10003;
                     if (var10003 <= var7) {
                        break label62;
                     }

                     var10004 = var10002;
                     var12 = 52;
                     var10005 = var7;
                  }

                  while(true) {
                     var22 = var10004[var10005];
                     switch(var7 % 7) {
                     case 0:
                        var23 = 89;
                        break;
                     case 1:
                        var23 = 72;
                        break;
                     case 2:
                        var23 = 6;
                        break;
                     case 3:
                        var23 = 18;
                        break;
                     case 4:
                        var23 = 39;
                        break;
                     case 5:
                        var23 = 26;
                        break;
                     default:
                        var23 = 78;
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
                  Vulcan_g = Collections.unmodifiableMap(new HashMap(0));
                  Vulcan_B = new WeakHashMap();
                  return;
               }

               var1 = var2.charAt(var0);
            }
         }

         var1 = var2.charAt(var0);
      }
   }

   public static void Vulcan_e(String[] var0) {
      Vulcan_k = var0;
   }

   public static String[] Vulcan_X() {
      return Vulcan_k;
   }

   private static Exception a(Exception var0) {
      return var0;
   }
}
