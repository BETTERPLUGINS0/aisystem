package ac.vulcan.anticheat;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class Vulcan_e6 {
   static final String Vulcan_r;
   private static final Object Vulcan_v;
   private static String[] Vulcan_m;
   private static final Method Vulcan_A;
   private static final Method Vulcan_Y;
   // $FF: synthetic field
   static Class Vulcan_X;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(3729915871783557051L, 6613635572413723112L, (Object)null).a(37268022668096L);
   private static final String[] b;

   public static void Vulcan_K(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_B(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_T(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static String[] Vulcan_P(Object[] var0) {
      List var1 = (List)var0[0];
      return (String[])((String[])var1.toArray(new String[var1.size()]));
   }

   private static ArrayList Vulcan_D(Object[] var0) {
      synchronized(Vulcan_v) {
         return new ArrayList(Arrays.asList(Vulcan_m));
      }
   }

   public static boolean Vulcan_q(Object[] var0) {
      String var3 = (String)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 16335361128273L;
      String var10000 = Vulcan_XW.Vulcan_x();
      Object var7;
      synchronized(var7 = Vulcan_v){}
      String var6 = var10000;

      try {
         int var13;
         label58: {
            try {
               var13 = Vulcan_XL.Vulcan_q(new Object[]{Vulcan_m, new Long(var4), var3});
               if (var6 != null) {
                  return (boolean)var13;
               }

               if (var13 >= 0) {
                  break label58;
               }
            } catch (Vulcan_Xi var11) {
               throw a(var11);
            }

            var13 = 0;
            return (boolean)var13;
         }

         var13 = 1;
         return (boolean)var13;
      } finally {
         ;
      }
   }

   public static Throwable Vulcan_G(Object[] var0) {
      long var2 = (Long)var0[0];
      Throwable var1 = (Throwable)var0[1];
      var2 ^= a;
      long var4 = var2 ^ 88113566943658L;
      synchronized(Vulcan_v) {
         String[] var10002 = Vulcan_m;
         return Vulcan_K(new Object[]{var1, new Long(var4), var10002});
      }
   }

   public static Throwable Vulcan_K(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static Throwable Vulcan_E(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static Throwable Vulcan_f(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static Throwable Vulcan_s(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static Throwable Vulcan_D(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_U(Object[] var0) {
      long var1 = (Long)var0[0];
      long var10000 = a ^ var1;

      boolean var4;
      try {
         if (Vulcan_A != null) {
            var4 = true;
            return var4;
         }
      } catch (Vulcan_Xi var3) {
         throw a(var3);
      }

      var4 = false;
      return var4;
   }

   public static boolean Vulcan_x(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_v(Object[] var0) {
      Throwable var3 = (Throwable)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 42612929849949L;
      return Vulcan_i(new Object[]{new Long(var4), var3}).size();
   }

   public static Throwable[] Vulcan_r(Object[] var0) {
      long var2 = (Long)var0[0];
      Throwable var1 = (Throwable)var0[1];
      var2 ^= a;
      long var4 = var2 ^ 65038348764277L;
      List var6 = Vulcan_i(new Object[]{new Long(var4), var1});
      return (Throwable[])((Throwable[])var6.toArray(new Throwable[var6.size()]));
   }

   public static List Vulcan_i(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_b(Object[] var0) {
      long var1 = (Long)var0[0];
      Throwable var3 = (Throwable)var0[1];
      Class var4 = (Class)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 96921359981446L;
      return Vulcan_R(new Object[]{new Long(var5), var3, var4, new Integer(0), new Boolean(false)});
   }

   public static int Vulcan_s(Object[] var0) {
      Throwable var2 = (Throwable)var0[0];
      long var3 = (Long)var0[1];
      Class var1 = (Class)var0[2];
      int var5 = (Integer)var0[3];
      var3 ^= a;
      long var6 = var3 ^ 132076402701572L;
      return Vulcan_R(new Object[]{new Long(var6), var2, var1, new Integer(var5), new Boolean(false)});
   }

   public static int Vulcan_P(Object[] var0) {
      long var1 = (Long)var0[0];
      Throwable var4 = (Throwable)var0[1];
      Class var3 = (Class)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 130636463284111L;
      return Vulcan_R(new Object[]{new Long(var5), var4, var3, new Integer(0), new Boolean(true)});
   }

   public static int Vulcan_i(Object[] var0) {
      long var2 = (Long)var0[0];
      Throwable var1 = (Throwable)var0[1];
      Class var5 = (Class)var0[2];
      int var4 = (Integer)var0[3];
      var2 ^= a;
      long var6 = var2 ^ 62127037380896L;
      return Vulcan_R(new Object[]{new Long(var6), var1, var5, new Integer(var4), new Boolean(true)});
   }

   private static int Vulcan_R(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_S(Object[] var0) {
      long var1 = (Long)var0[0];
      Throwable var3 = (Throwable)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 115334344322156L;
      Vulcan_Q(new Object[]{var3, System.err, new Long(var4)});
   }

   public static void Vulcan_Q(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_c(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String[] Vulcan_G(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_u(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_a(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_S(Object[] var0) {
      Throwable var1 = (Throwable)var0[0];
      StringWriter var2 = new StringWriter();
      PrintWriter var3 = new PrintWriter(var2, true);
      var1.printStackTrace(var3);
      return var2.getBuffer().toString();
   }

   public static String[] Vulcan_B(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   static String[] Vulcan_l(Object[] var0) {
      String var3 = (String)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      String var10000 = Vulcan_XW.Vulcan_x();
      String var5 = Vulcan_X5.Vulcan_e;
      StringTokenizer var6 = new StringTokenizer(var3, var5);
      ArrayList var7 = new ArrayList();
      String var4 = var10000;

      ArrayList var9;
      label33:
      while(var6.hasMoreTokens()) {
         while(true) {
            try {
               var9 = var7;
               String var10001 = var4;
               if (var1 > 0L) {
                  if (var4 != null) {
                     return Vulcan_P(new Object[]{var9});
                  }

                  var10001 = var6.nextToken();
               }

               var7.add(var10001);
               if (var4 == null) {
                  continue;
               }
            } catch (Vulcan_Xi var8) {
               throw a(var8);
            }

            if (var1 > 0L) {
               break label33;
            }
         }
      }

      var9 = var7;
      return Vulcan_P(new Object[]{var9});
   }

   static List Vulcan_D(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_i(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_d(Object[] var0) {
      Throwable var3 = (Throwable)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 124828717872634L;
      long var6 = var1 ^ 100646970943660L;
      String var10000 = Vulcan_XW.Vulcan_x();
      Throwable var9 = Vulcan_E(new Object[]{var3, new Long(var6)});
      String var8 = var10000;

      Throwable var11;
      label22: {
         label21: {
            try {
               var11 = var9;
               if (var8 != null) {
                  break label22;
               }

               if (var9 == null) {
                  break label21;
               }
            } catch (Vulcan_Xi var10) {
               throw a(var10);
            }

            var11 = var9;
            break label22;
         }

         var11 = var3;
      }

      var9 = var11;
      return Vulcan_i(new Object[]{var9, new Long(var4)});
   }

   // $FF: synthetic method
   static Class Vulcan_z(Object[] var0) {
      String var1 = (String)var0[0];

      try {
         return Class.forName(me.frep.vulcan.spigot.Vulcan_s.a(var1));
      } catch (ClassNotFoundException var3) {
         throw new NoClassDefFoundError(var3.getMessage());
      }
   }

   static {
      String[] var5 = new String[27];
      int var3 = 0;
      String var2 = "n\u001d/L\u0000{\by\u001d/n\bb\u0007o\tc\u00162Y*v\u0001y\u001d\tm\u001d/c\fd\u0000o\u001c\u0012m\u001d/c\fd\u0000o\u001c\u001eU\nr\u0004~\u00114C ^\u0010>\r9e\u001dd\f\bY\u001br\u0015gX6X\u001acTd\u0017/\r\u000brTd\r7A\fm\u001d/h\u0011t\u0011z\f2B\u0007\bm\u001d/n\bb\u0007o ^\u0010>\r9e\u001dd\f\f_\u0000c\u0011xX6X\u001acTd\u0017/\r\u000brTd\r7A\bm\u001d/n\bb\u0007o\u0012m\u001d/a\u0000y\u001fo\u001c\u001eU\nr\u0004~\u00114C\u0012m\u001d/~\u0006b\u0006i\u001d\u001eU\nr\u0004~\u00114C\u0014m\u001d/n\bb\u0007o\u001c\u0019T,o\u0017o\b/D\u0006y\u0002k\f\u000b*#,_\bg\u0004o\u001c\u0006\r\u0006~\u0019)J\fc\u0013`\u0019-LG{\u0015d\u001fuy\u0001e\u001b}\u00199A\f\u0010m\u001d/c\fo\u0000O\u00008H\u0019c\u001de\u0016\u0013`\u0019-LG{\u0015d\u001fuy\u0001e\u001b}\u00199A\f\u000b*#,_\bg\u0004o\u001c\u0006\r\u0006n\u001d/L\u0000{\u000em\u001d/a\u0000y\u001fo\u001c\u0018L\u001cd\u0011\fm\u001d/y\u0001e\u001b}\u00199A\f\fm\u001d/\u007f\u0006x\u0000I\u0019.^\f\u00020X";
      int var4 = "n\u001d/L\u0000{\by\u001d/n\bb\u0007o\tc\u00162Y*v\u0001y\u001d\tm\u001d/c\fd\u0000o\u001c\u0012m\u001d/c\fd\u0000o\u001c\u001eU\nr\u0004~\u00114C ^\u0010>\r9e\u001dd\f\bY\u001br\u0015gX6X\u001acTd\u0017/\r\u000brTd\r7A\fm\u001d/h\u0011t\u0011z\f2B\u0007\bm\u001d/n\bb\u0007o ^\u0010>\r9e\u001dd\f\f_\u0000c\u0011xX6X\u001acTd\u0017/\r\u000brTd\r7A\bm\u001d/n\bb\u0007o\u0012m\u001d/a\u0000y\u001fo\u001c\u001eU\nr\u0004~\u00114C\u0012m\u001d/~\u0006b\u0006i\u001d\u001eU\nr\u0004~\u00114C\u0014m\u001d/n\bb\u0007o\u001c\u0019T,o\u0017o\b/D\u0006y\u0002k\f\u000b*#,_\bg\u0004o\u001c\u0006\r\u0006~\u0019)J\fc\u0013`\u0019-LG{\u0015d\u001fuy\u0001e\u001b}\u00199A\f\u0010m\u001d/c\fo\u0000O\u00008H\u0019c\u001de\u0016\u0013`\u0019-LG{\u0015d\u001fuy\u0001e\u001b}\u00199A\f\u000b*#,_\bg\u0004o\u001c\u0006\r\u0006n\u001d/L\u0000{\u000em\u001d/a\u0000y\u001fo\u001c\u0018L\u001cd\u0011\fm\u001d/y\u0001e\u001b}\u00199A\f\fm\u001d/\u007f\u0006x\u0000I\u0019.^\f\u00020X".length();
      char var1 = 6;
      int var0 = -1;

      while(true) {
         int var7;
         int var14;
         byte var16;
         char[] var10001;
         char[] var10002;
         byte var18;
         int var10003;
         char[] var10004;
         int var10005;
         char var31;
         byte var32;
         label121: {
            ++var0;
            var10002 = var2.substring(var0, var0 + var1).toCharArray();
            var10003 = var10002.length;
            var7 = 0;
            var16 = 55;
            var10001 = var10002;
            var14 = var10003;
            if (var10003 <= 1) {
               var10004 = var10002;
               var18 = 55;
               var10005 = var7;
            } else {
               var16 = 55;
               var14 = var10003;
               if (var10003 <= var7) {
                  break label121;
               }

               var10004 = var10002;
               var18 = 55;
               var10005 = var7;
            }

            while(true) {
               var31 = var10004[var10005];
               switch(var7 % 7) {
               case 0:
                  var32 = 61;
                  break;
               case 1:
                  var32 = 79;
                  break;
               case 2:
                  var32 = 108;
                  break;
               case 3:
                  var32 = 26;
                  break;
               case 4:
                  var32 = 94;
                  break;
               case 5:
                  var32 = 32;
                  break;
               default:
                  var32 = 67;
               }

               var10004[var10005] = (char)(var31 ^ var18 ^ var32);
               ++var7;
               if (var16 == 0) {
                  var10005 = var16;
                  var10004 = var10001;
                  var18 = var16;
               } else {
                  if (var14 <= var7) {
                     break;
                  }

                  var10004 = var10001;
                  var18 = var16;
                  var10005 = var7;
               }
            }
         }

         var5[var3++] = (new String(var10001)).intern();
         if ((var0 += var1) >= var4) {
            var2 = "X(\u001aL=P&Z9+`?G1K$\u0001v\u0019k%\u000b8\u0010K2Km\u0003m/VaQ\"\u001a8>GaQ8\u0002t";
            var4 = "X(\u001aL=P&Z9+`?G1K$\u0001v\u0019k%\u000b8\u0010K2Km\u0003m/VaQ\"\u001a8>GaQ8\u0002t".length();
            var1 = 18;
            var0 = -1;

            while(true) {
               label100: {
                  ++var0;
                  var10002 = var2.substring(var0, var0 + var1).toCharArray();
                  var10003 = var10002.length;
                  var7 = 0;
                  var16 = 2;
                  var10001 = var10002;
                  var14 = var10003;
                  if (var10003 <= 1) {
                     var10004 = var10002;
                     var18 = 2;
                     var10005 = var7;
                  } else {
                     var16 = 2;
                     var14 = var10003;
                     if (var10003 <= var7) {
                        break label100;
                     }

                     var10004 = var10002;
                     var18 = 2;
                     var10005 = var7;
                  }

                  while(true) {
                     var31 = var10004[var10005];
                     switch(var7 % 7) {
                     case 0:
                        var32 = 61;
                        break;
                     case 1:
                        var32 = 79;
                        break;
                     case 2:
                        var32 = 108;
                        break;
                     case 3:
                        var32 = 26;
                        break;
                     case 4:
                        var32 = 94;
                        break;
                     case 5:
                        var32 = 32;
                        break;
                     default:
                        var32 = 67;
                     }

                     var10004[var10005] = (char)(var31 ^ var18 ^ var32);
                     ++var7;
                     if (var16 == 0) {
                        var10005 = var16;
                        var10004 = var10001;
                        var18 = var16;
                     } else {
                        if (var14 <= var7) {
                           break;
                        }

                        var10004 = var10001;
                        var18 = var16;
                        var10005 = var7;
                     }
                  }
               }

               var5[var3++] = (new String(var10001)).intern();
               if ((var0 += var1) >= var4) {
                  b = var5;
                  Vulcan_r = b[14];
                  Vulcan_v = new Object();
                  String[] var20 = new String[12];
                  String[] var13 = b;
                  var20[0] = var13[9];
                  var20[1] = var13[17];
                  var20[2] = var13[25];
                  var20[3] = var13[6];
                  var20[4] = var13[11];
                  var20[5] = var13[23];
                  var20[6] = var13[12];
                  var20[7] = var13[3];
                  var20[8] = var13[10];
                  var20[9] = var13[4];
                  var20[10] = var13[21];
                  var20[11] = var13[22];
                  Vulcan_m = var20;

                  Method var8;
                  Class var22;
                  try {
                     var22 = Vulcan_X == null ? (Vulcan_X = Vulcan_z(new Object[]{var13[16]})) : Vulcan_X;
                     var13 = b;
                     var8 = var22.getMethod(var13[7], (Class[])null);
                  } catch (Exception var10) {
                     var8 = null;
                  }

                  Vulcan_A = var8;

                  try {
                     if (Vulcan_X == null) {
                        var13 = b;
                        var22 = Vulcan_X = Vulcan_z(new Object[]{var13[18]});
                     } else {
                        var22 = Vulcan_X;
                     }

                     String var17;
                     byte var23;
                     Class[] var30;
                     Class[] var33;
                     Class var34;
                     label77: {
                        try {
                           var17 = b[2];
                           var30 = new Class[1];
                           var33 = var30;
                           var23 = 0;
                           if (Vulcan_X == null) {
                              var34 = Vulcan_X = Vulcan_z(new Object[]{b[18]});
                              break label77;
                           }
                        } catch (Exception var11) {
                           throw a(var11);
                        }

                        var34 = Vulcan_X;
                     }

                     var33[var23] = var34;
                     var8 = var22.getMethod(var17, var30);
                  } catch (Exception var12) {
                     var8 = null;
                  }

                  Vulcan_Y = var8;
                  return;
               }

               var1 = var2.charAt(var0);
            }
         }

         var1 = var2.charAt(var0);
      }
   }

   private static Exception a(Exception var0) {
      return var0;
   }
}
