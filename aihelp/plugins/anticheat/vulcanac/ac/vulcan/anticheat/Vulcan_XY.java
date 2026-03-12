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

/** @deprecated */
public abstract class Vulcan_XY implements Comparable, Serializable {
   private static final long serialVersionUID = -487045951170455942L;
   private static final Map Vulcan_r;
   private static Map Vulcan_X;
   private final String Vulcan_m;
   private final transient int Vulcan_D;
   protected transient String Vulcan_u;
   static Class Vulcan_z;
   static Class Vulcan_l;
   private static AbstractCheck[] Vulcan_e;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-7861116439147338187L, -8289810340984434592L, (Object)null).a(101722075784239L);
   private static final String[] c;

   protected Vulcan_XY(String var1) {
      long var2 = a ^ 24574877281027L;
      long var4 = var2 ^ 48834154194217L;
      super();
      this.Vulcan_u = null;
      this.Vulcan_j(new Object[]{new Long(var4), var1});
      this.Vulcan_m = var1;
      AbstractCheck[] var10000 = Vulcan_N();
      this.Vulcan_D = 7 + this.Vulcan_J(new Object[0]).hashCode() + 3 * var1.hashCode();
      AbstractCheck[] var6 = var10000;
      if (var6 != null) {
         int var7 = AbstractCheck.Vulcan_V();
         ++var7;
         AbstractCheck.Vulcan_H(var7);
      }

   }

   private void Vulcan_j(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected Object readResolve(long param1) {
      // $FF: Couldn't be decompiled
   }

   protected static Vulcan_XY Vulcan_n(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   protected static Map Vulcan_l(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   protected static List Vulcan_w(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   protected static Iterator Vulcan_B(Object[] var0) {
      Class var1 = (Class)var0[0];
      long var2 = (Long)var0[1];
      var2 ^= a;
      long var4 = var2 ^ 2003497983904L;
      return Vulcan_w(new Object[]{var1, new Long(var4)}).iterator();
   }

   private static Vulcan_iu Vulcan_u(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static Vulcan_iu Vulcan_F(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public final String Vulcan_q(Object[] var1) {
      return this.Vulcan_m;
   }

   public Class Vulcan_J(Object[] var1) {
      return this.getClass();
   }

   public final boolean equals(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public final int hashCode() {
      return this.Vulcan_D;
   }

   public int compareTo(Object param1) {
      // $FF: Couldn't be decompiled
   }

   private String Vulcan_E(Object[] var1) {
      Object var2 = (Object)var1[0];

      try {
         Class var10000 = var2.getClass();
         String[] var5 = c;
         Method var3 = var10000.getMethod(me.frep.vulcan.spigot.Vulcan_s.b((String)var5[6], (Class)var10000, (Class[])null), (Class[])null);
         String var4 = (String)var3.invoke(var2, (Object[])null);
         return var4;
      } catch (NoSuchMethodException var6) {
      } catch (IllegalAccessException var7) {
      } catch (InvocationTargetException var8) {
      }

      throw new IllegalStateException(c[8]);
   }

   public String toString() {
      long var1 = a ^ 129467540308778L;
      long var3 = var1 ^ 25683323454858L;
      AbstractCheck[] var5 = Vulcan_N();

      String var10000;
      label20: {
         try {
            var10000 = this.Vulcan_u;
            if (var5 != null) {
               return var10000;
            }

            if (var10000 != null) {
               break label20;
            }
         } catch (IllegalArgumentException var7) {
            throw a(var7);
         }

         String var6 = Vulcan_eo.Vulcan_B(new Object[]{this.Vulcan_J(new Object[0]), new Long(var3)});
         this.Vulcan_u = var6 + "[" + this.Vulcan_q(new Object[0]) + "]";
      }

      var10000 = this.Vulcan_u;
      return var10000;
   }

   static Class Vulcan_z(Object[] var0) {
      String var1 = (String)var0[0];

      try {
         return Class.forName(me.frep.vulcan.spigot.Vulcan_s.a(var1));
      } catch (ClassNotFoundException var3) {
         throw new NoClassDefFoundError(var3.getMessage());
      }
   }

   static {
      String[] var5 = new String[14];
      int var3 = 0;
      Vulcan_y((AbstractCheck[])null);
      String var2 = "|%\u0013LK.fEm\u0018\rc%3E8\u0005\u0018..|\\m\u0014\t.%~X9\u000fLa23F8\u001a\u0000\u0016l$\u0010\nk2vF9V\t`5~\b.\u001a\r}33\u000f5O(\u0002)`5~k!\u0017\u001f}h:\b \u0003\u001fz`aM9\u0003\u001e``r\b>\u0003\u001ck2pD,\u0005\u001f./u\b9\u001e\u0005}`pD,\u0005\u001f\u001f|%\u0013LK.fEm5\u0000o3`\b \u0003\u001fz`}G9V\u000ek`}]!\u001a\u001f|%\u0013LK.fEm\u0018\rc%3E8\u0005\u0018.\"v\b8\u0018\u0005\u007f5v\u0004mQ\u001dI.X\u001a{,pI#X\r`4zK%\u0013\rznE]!\u0015\r`\u001fKq\u0007O(\u0002\"o-v$|%\u0013LM,r[>V\u0001{3g\b/\u0013Lo``]/\u0015\u0000o3`\b\"\u0010LK.fE\u0016|%\u001f\u001f.3{G8\u001a\b..|\\m\u001e\r~0vF\u0018\u000fm\u001e\r}`rD?\u0013\rj93J(\u0013\u0002.!wL(\u0012\u001dI.X\u001a{,pI#X\r`4zK%\u0013\rznE]!\u0015\r`\u001fKI\u001dI.X\u001a{,pI#X\r`4zK%\u0013\rznE]!\u0015\r`\u001fKI";
      int var4 = "|%\u0013LK.fEm\u0018\rc%3E8\u0005\u0018..|\\m\u0014\t.%~X9\u000fLa23F8\u001a\u0000\u0016l$\u0010\nk2vF9V\t`5~\b.\u001a\r}33\u000f5O(\u0002)`5~k!\u0017\u001f}h:\b \u0003\u001fz`aM9\u0003\u001e``r\b>\u0003\u001ck2pD,\u0005\u001f./u\b9\u001e\u0005}`pD,\u0005\u001f\u001f|%\u0013LK.fEm5\u0000o3`\b \u0003\u001fz`}G9V\u000ek`}]!\u001a\u001f|%\u0013LK.fEm\u0018\rc%3E8\u0005\u0018.\"v\b8\u0018\u0005\u007f5v\u0004mQ\u001dI.X\u001a{,pI#X\r`4zK%\u0013\rznE]!\u0015\r`\u001fKq\u0007O(\u0002\"o-v$|%\u0013LM,r[>V\u0001{3g\b/\u0013Lo``]/\u0015\u0000o3`\b\"\u0010LK.fE\u0016|%\u001f\u001f.3{G8\u001a\b..|\\m\u001e\r~0vF\u0018\u000fm\u001e\r}`rD?\u0013\rj93J(\u0013\u0002.!wL(\u0012\u001dI.X\u001a{,pI#X\r`4zK%\u0013\rznE]!\u0015\r`\u001fKI\u001dI.X\u001a{,pI#X\r`4zK%\u0013\rznE]!\u0015\r`\u001fKI".length();
      char var1 = '\'';
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
            var10 = 120;
            var10001 = var10002;
            var8 = var10003;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 120;
               var10005 = var7;
            } else {
               var10 = 120;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label83;
               }

               var10004 = var10002;
               var12 = 120;
               var10005 = var7;
            }

            while(true) {
               var22 = var10004[var10005];
               switch(var7 % 7) {
               case 0:
                  var23 = 80;
                  break;
               case 1:
                  var23 = 53;
                  break;
               case 2:
                  var23 = 14;
                  break;
               case 3:
                  var23 = 20;
                  break;
               case 4:
                  var23 = 118;
                  break;
               case 5:
                  var23 = 56;
                  break;
               default:
                  var23 = 107;
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
            var2 = "s\u0014>\u0015\\\tBW\u001d+#AT\u00064\u001c?#F\\A{\u0005j2W\\Aa\u001d&\u001du\u0012d&G\u0010Lu\u001fd1\\\bFw\u0019/1FRya\u001d)1\\#wM";
            var4 = "s\u0014>\u0015\\\tBW\u001d+#AT\u00064\u001c?#F\\A{\u0005j2W\\Aa\u001d&\u001du\u0012d&G\u0010Lu\u001fd1\\\bFw\u0019/1FRya\u001d)1\\#wM".length();
            var1 = 31;
            var0 = -1;

            while(true) {
               label62: {
                  ++var0;
                  var10002 = var2.substring(var0, var0 + var1).toCharArray();
                  var10003 = var10002.length;
                  var7 = 0;
                  var10 = 68;
                  var10001 = var10002;
                  var8 = var10003;
                  if (var10003 <= 1) {
                     var10004 = var10002;
                     var12 = 68;
                     var10005 = var7;
                  } else {
                     var10 = 68;
                     var8 = var10003;
                     if (var10003 <= var7) {
                        break label62;
                     }

                     var10004 = var10002;
                     var12 = 68;
                     var10005 = var7;
                  }

                  while(true) {
                     var22 = var10004[var10005];
                     switch(var7 % 7) {
                     case 0:
                        var23 = 80;
                        break;
                     case 1:
                        var23 = 53;
                        break;
                     case 2:
                        var23 = 14;
                        break;
                     case 3:
                        var23 = 20;
                        break;
                     case 4:
                        var23 = 118;
                        break;
                     case 5:
                        var23 = 56;
                        break;
                     default:
                        var23 = 107;
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
                  Vulcan_r = Collections.unmodifiableMap(new HashMap(0));
                  Vulcan_X = new WeakHashMap();
                  return;
               }

               var1 = var2.charAt(var0);
            }
         }

         var1 = var2.charAt(var0);
      }
   }

   public static void Vulcan_y(AbstractCheck[] var0) {
      Vulcan_e = var0;
   }

   public static AbstractCheck[] Vulcan_N() {
      return Vulcan_e;
   }

   private static Exception a(Exception var0) {
      return var0;
   }
}
