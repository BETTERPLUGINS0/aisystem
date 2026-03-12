package ac.vulcan.anticheat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Vulcan_eo {
   public static final char Vulcan_G = '.';
   public static final String Vulcan_a;
   public static final char Vulcan_q = '$';
   public static final String Vulcan_Y;
   private static final Map Vulcan_c;
   private static final Map Vulcan_f;
   private static final Map Vulcan_i;
   private static final Map Vulcan_o;
   // $FF: synthetic field
   static Class Vulcan_Q;
   // $FF: synthetic field
   static Class Vulcan_X;
   // $FF: synthetic field
   static Class Vulcan_s;
   // $FF: synthetic field
   static Class Vulcan_p;
   // $FF: synthetic field
   static Class Vulcan_g;
   // $FF: synthetic field
   static Class Vulcan_y;
   // $FF: synthetic field
   static Class Vulcan_H;
   // $FF: synthetic field
   static Class Vulcan_I;
   // $FF: synthetic field
   static Class Vulcan_v;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-124476559551645987L, -6584212506010700257L, (Object)null).a(205215681367952L);
   private static final String[] b;

   private static void Vulcan_I(Object[] var0) {
      String var1 = (String)var0[0];
      String var2 = (String)var0[1];
      Vulcan_i.put(var1, var2);
      Vulcan_o.put(var2, var1);
   }

   public static String Vulcan_X(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_B(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_e(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_H(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_M(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_p(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static List Vulcan_e(Object[] var0) {
      Class var3 = (Class)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      String[] var4 = Vulcan_XL.Vulcan_v();

      try {
         if (var3 == null) {
            return null;
         }
      } catch (SecurityException var7) {
         throw a(var7);
      }

      ArrayList var5 = new ArrayList();
      Class var6 = var3.getSuperclass();

      ArrayList var10000;
      label31:
      while(true) {
         if (var6 != null) {
            var10000 = var5;
            if (var4 == null) {
               break;
            }

            var5.add(var6);
            var6 = var6.getSuperclass();
            if (var4 != null) {
               continue;
            }
         }

         while(var1 < 0L) {
            if (var4 != null) {
               continue label31;
            }
         }

         var10000 = var5;
         break;
      }

      return var10000;
   }

   public static List Vulcan_B(Object[] var0) {
      Class var1 = (Class)var0[0];
      long var2 = (Long)var0[1];
      var2 ^= a;
      long var4 = var2 ^ 140051285044620L;

      try {
         if (var1 == null) {
            return null;
         }
      } catch (SecurityException var7) {
         throw a(var7);
      }

      ArrayList var6 = new ArrayList();
      Vulcan_d(new Object[]{var1, new Long(var4), var6});
      return var6;
   }

   private static void Vulcan_d(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static List Vulcan_c(Object[] var0) {
      List var1 = (List)var0[0];
      long var2 = (Long)var0[1];
      var2 ^= a;
      String[] var4 = Vulcan_XL.Vulcan_v();

      try {
         if (var1 == null) {
            return null;
         }
      } catch (Exception var10) {
         throw a(var10);
      }

      ArrayList var5 = new ArrayList(var1.size());
      Iterator var6 = var1.iterator();

      ArrayList var10000;
      while(true) {
         if (var6.hasNext()) {
            String var7 = (String)var6.next();

            try {
               if (var2 > 0L) {
                  var10000 = var5;
                  if (var4 == null) {
                     break;
                  }

                  var5.add(Class.forName(me.frep.vulcan.spigot.Vulcan_s.a(var7)));
               }
            } catch (Exception var9) {
               var5.add((Object)null);
            }

            if (var4 != null) {
               continue;
            }
         }

         var10000 = var5;
         break;
      }

      return var10000;
   }

   public static List Vulcan_g(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_w(Object[] var0) {
      long var1 = (Long)var0[0];
      Class[] var3 = (Class[])var0[1];
      Class[] var4 = (Class[])var0[2];
      var1 ^= a;
      long var5 = var1 ^ 63402860836806L;
      return Vulcan_y(new Object[]{var3, var4, new Long(var5), new Boolean(false)});
   }

   public static boolean Vulcan_y(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_b(Object[] var0) {
      long var1 = (Long)var0[0];
      Class var3 = (Class)var0[1];
      Class var4 = (Class)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 112680456478119L;
      return Vulcan_F(new Object[]{var3, new Long(var5), var4, new Boolean(false)});
   }

   public static boolean Vulcan_F(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static Class Vulcan_B(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static Class[] Vulcan_i(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static Class Vulcan_t(Object[] var0) {
      Class var1 = (Class)var0[0];
      return (Class)Vulcan_f.get(var1);
   }

   public static Class[] Vulcan_S(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_M(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static Class Vulcan_e(Object[] var0) {
      long var3 = (Long)var0[0];
      ClassLoader var1 = (ClassLoader)var0[1];
      String var5 = (String)var0[2];
      boolean var2 = (Boolean)var0[3];
      var3 ^= a;
      long var6 = var3 ^ 86319193499675L;
      long var8 = var3 ^ 108090374030665L;
      String[] var10 = Vulcan_XL.Vulcan_v();

      try {
         Class var11;
         Object var10000;
         label38: {
            if (Vulcan_i.containsKey(var5)) {
               String var17 = "[" + Vulcan_i.get(var5);
               var11 = Class.forName(me.frep.vulcan.spigot.Vulcan_s.a(var17), var2, var1).getComponentType();
               var10000 = var10;
               if (var3 < 0L) {
                  break label38;
               }

               if (var10 != null) {
                  return var11;
               }
            }

            Object[] var10003 = new Object[]{null, var5};
            var10000 = var10003;
            var10003[0] = new Long(var8);
         }

         var11 = Class.forName(me.frep.vulcan.spigot.Vulcan_s.a(Vulcan_F((Object[])var10000)), var2, var1);
         return var11;
      } catch (ClassNotFoundException var16) {
         int var12 = var5.lastIndexOf(46);

         try {
            if (var12 != -1) {
               try {
                  return Vulcan_e(new Object[]{new Long(var6), var1, var5.substring(0, var12) + '$' + var5.substring(var12 + 1), new Boolean(var2)});
               } catch (ClassNotFoundException var14) {
               }
            }
         } catch (SecurityException var15) {
            throw a(var15);
         }

         throw var16;
      }
   }

   public static Class Vulcan_M(Object[] var0) {
      long var1 = (Long)var0[0];
      ClassLoader var3 = (ClassLoader)var0[1];
      String var4 = (String)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 57798542943411L;
      return Vulcan_e(new Object[]{new Long(var5), var3, var4, new Boolean(true)});
   }

   public static Class Vulcan_H(Object[] var0) {
      long var1 = (Long)var0[0];
      String var3 = (String)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 91995498079646L;
      return Vulcan_r(new Object[]{new Long(var4), var3, new Boolean(true)});
   }

   public static Class Vulcan_r(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static Method Vulcan_p(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static String Vulcan_F(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static Class[] Vulcan_u(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_J(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_R(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_D(Object[] var0) {
      long var2 = (Long)var0[0];
      String var1 = (String)var0[1];
      var2 ^= a;
      long var4 = var2 ^ 48227958782876L;
      long var6 = var2 ^ 83000955808157L;
      return Vulcan_e(new Object[]{new Long(var6), Vulcan_o(new Object[]{var1, new Long(var4)})});
   }

   public static String Vulcan_G(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_a(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_W(Object[] var0) {
      String var3 = (String)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 1723242033273L;
      long var6 = var1 ^ 46044301037107L;
      return Vulcan_p(new Object[]{Vulcan_o(new Object[]{var3, new Long(var4)}), new Long(var6)});
   }

   private static String Vulcan_o(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   static Class Vulcan_G(Object[] var0) {
      String var1 = (String)var0[0];

      try {
         return Class.forName(me.frep.vulcan.spigot.Vulcan_s.a(var1));
      } catch (ClassNotFoundException var3) {
         throw new NoClassDefFoundError(var3.getMessage());
      }
   }

   static {
      String[] var5 = new String[21];
      int var3 = 0;
      String var2 = "\u00128[\u0015@a\u0000\u0004\u0012.@\u001c\u000f\u001a6B\u0018\u000bl\u000f\u001e0\u001a?Io\u000f\u0004\t\u0013;U\nVN\u000f\u001d2\u0005\u0016;[\u0018Q\u0003\u00199@\u0002+\n\u0002+\n\u001f36Z^Q \b\u00199PYD \u001e\u00055X\u0010F \u0003\u0015#\\\u0016A \b\u001f%\u0014\u0011\u001a6B\u0018\u000bl\u000f\u001e0\u001a0Kt\u000b\u00172F\u0006\u00148A\u001bIe\u0011\u001a6B\u0018\u000bl\u000f\u001e0\u001a;Jo\u0002\u00156Z\u000e\u001a6B\u0018\u000bl\u000f\u001e0\u001a5Jn\t\u0005\u0003?[\u000bQ\u0010\u001a6B\u0018\u000bl\u000f\u001e0\u001a=Ju\f\u001c2\u0013\u001a6B\u0018\u000bl\u000f\u001e0\u001a:Ma\u001c\u00114@\u001cW\u001d\u00114\u001a\u000fPl\r\u00119\u001a\u0018Kt\u0007\u0013?Q\u0018Q.8\u0005;W\u0018K_\u000b\u001f\u0004\u0013?U\u000b\u000e\u001a6B\u0018\u000bl\u000f\u001e0\u001a;\\t\u000b";
      int var4 = "\u00128[\u0015@a\u0000\u0004\u0012.@\u001c\u000f\u001a6B\u0018\u000bl\u000f\u001e0\u001a?Io\u000f\u0004\t\u0013;U\nVN\u000f\u001d2\u0005\u0016;[\u0018Q\u0003\u00199@\u0002+\n\u0002+\n\u001f36Z^Q \b\u00199PYD \u001e\u00055X\u0010F \u0003\u0015#\\\u0016A \b\u001f%\u0014\u0011\u001a6B\u0018\u000bl\u000f\u001e0\u001a0Kt\u000b\u00172F\u0006\u00148A\u001bIe\u0011\u001a6B\u0018\u000bl\u000f\u001e0\u001a;Jo\u0002\u00156Z\u000e\u001a6B\u0018\u000bl\u000f\u001e0\u001a5Jn\t\u0005\u0003?[\u000bQ\u0010\u001a6B\u0018\u000bl\u000f\u001e0\u001a=Ju\f\u001c2\u0013\u001a6B\u0018\u000bl\u000f\u001e0\u001a:Ma\u001c\u00114@\u001cW\u001d\u00114\u001a\u000fPl\r\u00119\u001a\u0018Kt\u0007\u0013?Q\u0018Q.8\u0005;W\u0018K_\u000b\u001f\u0004\u0013?U\u000b\u000e\u001a6B\u0018\u000bl\u000f\u001e0\u001a;\\t\u000b".length();
      char var1 = 7;
      int var0 = -1;

      while(true) {
         int var7;
         char[] var10001;
         char[] var10002;
         int var10003;
         char[] var10004;
         int var21;
         int var10005;
         byte var23;
         byte var25;
         char var38;
         byte var39;
         label198: {
            ++var0;
            var10002 = var2.substring(var0, var0 + var1).toCharArray();
            var10003 = var10002.length;
            var7 = 0;
            var23 = 31;
            var10001 = var10002;
            var21 = var10003;
            if (var10003 <= 1) {
               var10004 = var10002;
               var25 = 31;
               var10005 = var7;
            } else {
               var23 = 31;
               var21 = var10003;
               if (var10003 <= var7) {
                  break label198;
               }

               var10004 = var10002;
               var25 = 31;
               var10005 = var7;
            }

            while(true) {
               var38 = var10004[var10005];
               switch(var7 % 7) {
               case 0:
                  var39 = 111;
                  break;
               case 1:
                  var39 = 72;
                  break;
               case 2:
                  var39 = 43;
                  break;
               case 3:
                  var39 = 102;
                  break;
               case 4:
                  var39 = 58;
                  break;
               case 5:
                  var39 = 31;
                  break;
               default:
                  var39 = 113;
               }

               var10004[var10005] = (char)(var38 ^ var25 ^ var39);
               ++var7;
               if (var23 == 0) {
                  var10005 = var23;
                  var10004 = var10001;
                  var25 = var23;
               } else {
                  if (var21 <= var7) {
                     break;
                  }

                  var10004 = var10001;
                  var25 = var23;
                  var10005 = var7;
               }
            }
         }

         var5[var3++] = (new String(var10001)).intern();
         if ((var0 += var1) >= var4) {
            var2 = "Kg\u0013IZ=^OaK{\u001c>MU\u0004Mi\u000bO";
            var4 = "Kg\u0013IZ=^OaK{\u001c>MU\u0004Mi\u000bO".length();
            var1 = 15;
            var0 = -1;

            while(true) {
               label177: {
                  ++var0;
                  var10002 = var2.substring(var0, var0 + var1).toCharArray();
                  var10003 = var10002.length;
                  var7 = 0;
                  var23 = 78;
                  var10001 = var10002;
                  var21 = var10003;
                  if (var10003 <= 1) {
                     var10004 = var10002;
                     var25 = 78;
                     var10005 = var7;
                  } else {
                     var23 = 78;
                     var21 = var10003;
                     if (var10003 <= var7) {
                        break label177;
                     }

                     var10004 = var10002;
                     var25 = 78;
                     var10005 = var7;
                  }

                  while(true) {
                     var38 = var10004[var10005];
                     switch(var7 % 7) {
                     case 0:
                        var39 = 111;
                        break;
                     case 1:
                        var39 = 72;
                        break;
                     case 2:
                        var39 = 43;
                        break;
                     case 3:
                        var39 = 102;
                        break;
                     case 4:
                        var39 = 58;
                        break;
                     case 5:
                        var39 = 31;
                        break;
                     default:
                        var39 = 113;
                     }

                     var10004[var10005] = (char)(var38 ^ var25 ^ var39);
                     ++var7;
                     if (var23 == 0) {
                        var10005 = var23;
                        var10004 = var10001;
                        var25 = var23;
                     } else {
                        if (var21 <= var7) {
                           break;
                        }

                        var10004 = var10001;
                        var25 = var23;
                        var10005 = var7;
                     }
                  }
               }

               var5[var3++] = (new String(var10001)).intern();
               if ((var0 += var1) >= var4) {
                  b = var5;

                  Map var27;
                  Class var29;
                  Class var37;
                  label157: {
                     try {
                        Vulcan_a = String.valueOf('.');
                        Vulcan_Y = String.valueOf('$');
                        Vulcan_c = new HashMap();
                        var27 = Vulcan_c;
                        var29 = Boolean.TYPE;
                        if (Vulcan_Q == null) {
                           var37 = Vulcan_Q = Vulcan_G(new Object[]{b[11]});
                           break label157;
                        }
                     } catch (SecurityException var19) {
                        throw a(var19);
                     }

                     var37 = Vulcan_Q;
                  }

                  label150: {
                     try {
                        var27.put(var29, var37);
                        var27 = Vulcan_c;
                        var29 = Byte.TYPE;
                        if (Vulcan_X == null) {
                           var37 = Vulcan_X = Vulcan_G(new Object[]{b[18]});
                           break label150;
                        }
                     } catch (SecurityException var18) {
                        throw a(var18);
                     }

                     var37 = Vulcan_X;
                  }

                  label143: {
                     try {
                        var27.put(var29, var37);
                        var27 = Vulcan_c;
                        var29 = Character.TYPE;
                        if (Vulcan_s == null) {
                           var37 = Vulcan_s = Vulcan_G(new Object[]{b[15]});
                           break label143;
                        }
                     } catch (SecurityException var17) {
                        throw a(var17);
                     }

                     var37 = Vulcan_s;
                  }

                  label136: {
                     try {
                        var27.put(var29, var37);
                        var27 = Vulcan_c;
                        var29 = Short.TYPE;
                        if (Vulcan_p == null) {
                           var37 = Vulcan_p = Vulcan_G(new Object[]{b[19]});
                           break label136;
                        }
                     } catch (SecurityException var16) {
                        throw a(var16);
                     }

                     var37 = Vulcan_p;
                  }

                  label129: {
                     try {
                        var27.put(var29, var37);
                        var27 = Vulcan_c;
                        var29 = Integer.TYPE;
                        if (Vulcan_g == null) {
                           var37 = Vulcan_g = Vulcan_G(new Object[]{b[9]});
                           break label129;
                        }
                     } catch (SecurityException var15) {
                        throw a(var15);
                     }

                     var37 = Vulcan_g;
                  }

                  label122: {
                     try {
                        var27.put(var29, var37);
                        var27 = Vulcan_c;
                        var29 = Long.TYPE;
                        if (Vulcan_y == null) {
                           var37 = Vulcan_y = Vulcan_G(new Object[]{b[12]});
                           break label122;
                        }
                     } catch (SecurityException var14) {
                        throw a(var14);
                     }

                     var37 = Vulcan_y;
                  }

                  label115: {
                     try {
                        var27.put(var29, var37);
                        var27 = Vulcan_c;
                        var29 = Double.TYPE;
                        if (Vulcan_H == null) {
                           var37 = Vulcan_H = Vulcan_G(new Object[]{b[14]});
                           break label115;
                        }
                     } catch (SecurityException var13) {
                        throw a(var13);
                     }

                     var37 = Vulcan_H;
                  }

                  label108: {
                     try {
                        var27.put(var29, var37);
                        var27 = Vulcan_c;
                        var29 = Float.TYPE;
                        if (Vulcan_I == null) {
                           var37 = Vulcan_I = Vulcan_G(new Object[]{b[2]});
                           break label108;
                        }
                     } catch (SecurityException var12) {
                        throw a(var12);
                     }

                     var37 = Vulcan_I;
                  }

                  var27.put(var29, var37);
                  Vulcan_c.put(Void.TYPE, Void.TYPE);
                  Vulcan_f = new HashMap();
                  Iterator var8 = Vulcan_c.keySet().iterator();

                  while(var8.hasNext()) {
                     Class var9 = (Class)var8.next();
                     Class var10 = (Class)Vulcan_c.get(var9);

                     try {
                        if (!var9.equals(var10)) {
                           Vulcan_f.put(var10, var9);
                        }
                     } catch (SecurityException var11) {
                        throw a(var11);
                     }
                  }

                  Vulcan_i = new HashMap();
                  Vulcan_o = new HashMap();
                  String[] var20 = b;
                  Vulcan_I(new Object[]{var20[5], "I"});
                  Vulcan_I(new Object[]{var20[0], "Z"});
                  Vulcan_I(new Object[]{var20[4], "F"});
                  Vulcan_I(new Object[]{var20[20], "J"});
                  Vulcan_I(new Object[]{var20[13], "S"});
                  Vulcan_I(new Object[]{var20[1], "B"});
                  Vulcan_I(new Object[]{var20[10], "D"});
                  Vulcan_I(new Object[]{var20[17], "C"});
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
