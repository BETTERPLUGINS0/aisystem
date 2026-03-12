package ac.vulcan.anticheat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class Vulcan_Z extends Vulcan_z {
   private static final long serialVersionUID = -7129650521543789085L;
   private final int Vulcan_u;
   private static final long b = me.frep.vulcan.spigot.Vulcan_n.a(5938685920337439064L, -6943775750324537614L, (Object)null).a(227625710363584L);
   private static final String[] d;

   protected Vulcan_Z(String var1, int var2) {
      super(var1);
      this.Vulcan_u = var2;
   }

   protected static Vulcan_z Vulcan_T(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public final int Vulcan_K(Object[] var1) {
      return this.Vulcan_u;
   }

   public int compareTo(Object param1) {
      // $FF: Couldn't be decompiled
   }

   private int Vulcan_S(Object[] var1) {
      Object var2 = (Object)var1[0];

      try {
         Class var10000 = var2.getClass();
         String[] var5 = d;
         Method var3 = var10000.getMethod(me.frep.vulcan.spigot.Vulcan_s.b((String)var5[0], (Class)var10000, (Class[])null), (Class[])null);
         Integer var4 = (Integer)var3.invoke(var2, (Object[])null);
         return var4;
      } catch (NoSuchMethodException var6) {
      } catch (IllegalAccessException var7) {
      } catch (InvocationTargetException var8) {
      }

      throw new IllegalStateException(d[3]);
   }

   public String toString() {
      long var1 = b ^ 35840882771731L;
      long var3 = var1 ^ 116150599470080L;
      String[] var5 = Vulcan_z.Vulcan_X();

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
         this.Vulcan_W = var6 + "[" + this.Vulcan_f(new Object[0]) + "=" + this.Vulcan_K(new Object[0]) + "]";
      }

      var10000 = this.Vulcan_W;
      return var10000;
   }

   static {
      String[] var5 = new String[4];
      int var3 = 0;
      String var2 = "P\u001f#\\rVYR\u0016s\u00131lvHIY\u000ewo}OA\u0017\u0019;k`I\f\u0010";
      int var4 = "P\u001f#\\rVYR\u0016s\u00131lvHIY\u000ewo}OA\u0017\u0019;k`I\f\u0010".length();
      char var1 = '\b';
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
            var10 = 38;
            var10001 = var10002;
            var8 = var10003;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 38;
               var10005 = var7;
            } else {
               var10 = 38;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label83;
               }

               var10004 = var10002;
               var12 = 38;
               var10005 = var7;
            }

            while(true) {
               var22 = var10004[var10005];
               switch(var7 % 7) {
               case 0:
                  var23 = 17;
                  break;
               case 1:
                  var23 = 92;
                  break;
               case 2:
                  var23 = 113;
                  break;
               case 3:
                  var23 = 44;
                  break;
               case 4:
                  var23 = 53;
                  break;
               case 5:
                  var23 = 28;
                  break;
               default:
                  var23 = 10;
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
            var2 = ")Xx`\u001c\u001e\u0013\u0010\u0010^,8\u0003\u0015]]h3-P\b\u0012D=\"<P\b\b\\q\u0016)Xt3y\u0003\u000e\u0012Eq$y\u001e\t\t\u0010u!)\u0000\u0003\u0013";
            var4 = ")Xx`\u001c\u001e\u0013\u0010\u0010^,8\u0003\u0015]]h3-P\b\u0012D=\"<P\b\b\\q\u0016)Xt3y\u0003\u000e\u0012Eq$y\u001e\t\t\u0010u!)\u0000\u0003\u0013".length();
            var1 = 31;
            var0 = -1;

            while(true) {
               label62: {
                  ++var0;
                  var10002 = var2.substring(var0, var0 + var1).toCharArray();
                  var10003 = var10002.length;
                  var7 = 0;
                  var10 = 108;
                  var10001 = var10002;
                  var8 = var10003;
                  if (var10003 <= 1) {
                     var10004 = var10002;
                     var12 = 108;
                     var10005 = var7;
                  } else {
                     var10 = 108;
                     var8 = var10003;
                     if (var10003 <= var7) {
                        break label62;
                     }

                     var10004 = var10002;
                     var12 = 108;
                     var10005 = var7;
                  }

                  while(true) {
                     var22 = var10004[var10005];
                     switch(var7 % 7) {
                     case 0:
                        var23 = 17;
                        break;
                     case 1:
                        var23 = 92;
                        break;
                     case 2:
                        var23 = 113;
                        break;
                     case 3:
                        var23 = 44;
                        break;
                     case 4:
                        var23 = 53;
                        break;
                     case 5:
                        var23 = 28;
                        break;
                     default:
                        var23 = 10;
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

         var1 = var2.charAt(var0);
      }
   }

   private static IllegalArgumentException a(IllegalArgumentException var0) {
      return var0;
   }
}
