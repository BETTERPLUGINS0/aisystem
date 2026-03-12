package ac.vulcan.anticheat;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class Vulcan_em {
   private static final char Vulcan_r = ',';
   private static final char Vulcan_Z = '"';
   private static final String Vulcan_o;
   private static final char[] Vulcan_C;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(1129576177933735140L, -2226223032990281429L, (Object)null).a(206558271435802L);
   private static final String[] b;

   public static String Vulcan_o(Object[] var0) {
      long var1 = (Long)var0[0];
      String var3 = (String)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 107082642994748L;
      return Vulcan_W(new Object[]{var3, new Boolean(false), new Boolean(false), new Long(var4)});
   }

   public static void Vulcan_S(Object[] var0) {
      Writer var1 = (Writer)var0[0];
      long var2 = (Long)var0[1];
      String var4 = (String)var0[2];
      var2 ^= a;
      long var5 = var2 ^ 16622630989469L;
      Vulcan_I(new Object[]{new Long(var5), var1, var4, new Boolean(false), new Boolean(false)});
   }

   public static String Vulcan_E(Object[] var0) {
      long var1 = (Long)var0[0];
      String var3 = (String)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 997198936316L;
      return Vulcan_W(new Object[]{var3, new Boolean(true), new Boolean(true), new Long(var4)});
   }

   public static void Vulcan_k(Object[] var0) {
      Writer var2 = (Writer)var0[0];
      long var3 = (Long)var0[1];
      String var1 = (String)var0[2];
      var3 ^= a;
      long var5 = var3 ^ 86327100703952L;
      Vulcan_I(new Object[]{new Long(var5), var2, var1, new Boolean(true), new Boolean(true)});
   }

   private static String Vulcan_W(Object[] var0) {
      String var1 = (String)var0[0];
      boolean var2 = (Boolean)var0[1];
      boolean var5 = (Boolean)var0[2];
      long var3 = (Long)var0[3];
      var3 ^= a;
      long var6 = var3 ^ 104815019052844L;

      try {
         if (var1 == null) {
            return null;
         }
      } catch (IOException var10) {
         throw a(var10);
      }

      try {
         StringWriter var8 = new StringWriter(var1.length() * 2);
         Vulcan_I(new Object[]{new Long(var6), var8, var1, new Boolean(var2), new Boolean(var5)});
         return var8.toString();
      } catch (IOException var9) {
         throw new Vulcan_Xl(var9);
      }
   }

   private static void Vulcan_I(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static String Vulcan_t(Object[] var0) {
      int var1 = (Integer)var0[0];
      return Integer.toHexString(var1).toUpperCase(Locale.ENGLISH);
   }

   public static String Vulcan_T(Object[] var0) {
      long var1 = (Long)var0[0];
      String var3 = (String)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 114455557459396L;

      try {
         if (var3 == null) {
            return null;
         }
      } catch (IOException var8) {
         throw a(var8);
      }

      try {
         StringWriter var6 = new StringWriter(var3.length());
         Vulcan_B(new Object[]{var6, new Long(var4), var3});
         return var6.toString();
      } catch (IOException var7) {
         throw new Vulcan_Xl(var7);
      }
   }

   public static void Vulcan_B(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_r(Object[] var0) {
      long var2 = (Long)var0[0];
      String var1 = (String)var0[1];
      var2 ^= a;
      long var4 = var2 ^ 24996295180388L;
      return Vulcan_T(new Object[]{new Long(var4), var1});
   }

   public static void Vulcan_p(Object[] var0) {
      Writer var1 = (Writer)var0[0];
      long var2 = (Long)var0[1];
      String var4 = (String)var0[2];
      var2 ^= a;
      long var5 = var2 ^ 36501331750174L;
      Vulcan_B(new Object[]{var1, new Long(var5), var4});
   }

   public static String Vulcan_N(Object[] var0) {
      String var3 = (String)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 127931076716502L;

      try {
         if (var3 == null) {
            return null;
         }
      } catch (IOException var8) {
         throw a(var8);
      }

      try {
         StringWriter var6 = new StringWriter((int)((double)var3.length() * 1.5D));
         Vulcan_T(new Object[]{var6, var3, new Long(var4)});
         return var6.toString();
      } catch (IOException var7) {
         throw new Vulcan_Xl(var7);
      }
   }

   public static void Vulcan_T(Object[] var0) {
      Writer var4 = (Writer)var0[0];
      String var3 = (String)var0[1];
      long var1 = (Long)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 81830967968906L;

      try {
         if (var4 == null) {
            throw new IllegalArgumentException(b[5]);
         }
      } catch (IOException var7) {
         throw a(var7);
      }

      try {
         if (var1 < 0L) {
            return;
         }

         if (var3 == null) {
            return;
         }
      } catch (IOException var8) {
         throw a(var8);
      }

      Vulcan_Y.Vulcan_z.Vulcan_g(new Object[]{var4, new Long(var5), var3});
   }

   public static String Vulcan_F(Object[] var0) {
      long var2 = (Long)var0[0];
      String var1 = (String)var0[1];
      var2 ^= a;
      long var4 = var2 ^ 68748472346031L;

      try {
         if (var1 == null) {
            return null;
         }
      } catch (IOException var8) {
         throw a(var8);
      }

      try {
         StringWriter var6 = new StringWriter((int)((double)var1.length() * 1.5D));
         Vulcan_m(new Object[]{var6, var1, new Long(var4)});
         return var6.toString();
      } catch (IOException var7) {
         throw new Vulcan_Xl(var7);
      }
   }

   public static void Vulcan_m(Object[] var0) {
      Writer var2 = (Writer)var0[0];
      String var1 = (String)var0[1];
      long var3 = (Long)var0[2];
      var3 ^= a;
      long var5 = var3 ^ 47972181207758L;

      try {
         if (var2 == null) {
            throw new IllegalArgumentException(b[5]);
         }
      } catch (IOException var7) {
         throw a(var7);
      }

      try {
         if (var3 < 0L) {
            return;
         }

         if (var1 == null) {
            return;
         }
      } catch (IOException var8) {
         throw a(var8);
      }

      Vulcan_Y.Vulcan_z.Vulcan_v(new Object[]{new Long(var5), var2, var1});
   }

   public static void Vulcan_O(Object[] var0) {
      long var2 = (Long)var0[0];
      Writer var4 = (Writer)var0[1];
      String var1 = (String)var0[2];
      var2 ^= a;
      long var5 = var2 ^ 14264469187925L;

      try {
         if (var4 == null) {
            throw new IllegalArgumentException(b[5]);
         }
      } catch (IOException var7) {
         throw a(var7);
      }

      try {
         if (var2 <= 0L) {
            return;
         }

         if (var1 == null) {
            return;
         }
      } catch (IOException var8) {
         throw a(var8);
      }

      Vulcan_Y.Vulcan_V.Vulcan_g(new Object[]{var4, new Long(var5), var1});
   }

   public static String Vulcan_A(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_G(Object[] var0) {
      Writer var4 = (Writer)var0[0];
      String var1 = (String)var0[1];
      long var2 = (Long)var0[2];
      var2 ^= a;
      long var5 = var2 ^ 29957201377071L;

      try {
         if (var4 == null) {
            throw new IllegalArgumentException(b[6]);
         }
      } catch (IOException var7) {
         throw a(var7);
      }

      try {
         if (var2 < 0L) {
            return;
         }

         if (var1 == null) {
            return;
         }
      } catch (IOException var8) {
         throw a(var8);
      }

      Vulcan_Y.Vulcan_V.Vulcan_v(new Object[]{new Long(var5), var4, var1});
   }

   public static String Vulcan_f(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_X(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_s(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_u(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_G(Object[] var0) {
      String var1 = (String)var0[0];
      long var2 = (Long)var0[1];
      var2 ^= a;
      long var4 = var2 ^ 29315028392199L;

      try {
         if (var1 == null) {
            return null;
         }
      } catch (IOException var8) {
         throw a(var8);
      }

      try {
         StringWriter var6 = new StringWriter();
         Vulcan_P(new Object[]{new Long(var4), var6, var1});
         return var6.toString();
      } catch (IOException var7) {
         throw new Vulcan_Xl(var7);
      }
   }

   public static void Vulcan_P(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   static {
      String[] var5 = new String[11];
      int var3 = 0;
      String var2 = "E[\u0001(\n+2eV\u0016(0,(e\u0013\ng)y9t\u0013\n}15\u0004MFT8\u0002MF\u0005MFT8m\u001bE[\u0001(\n+2eV\u0016(0,(e\u0013\ng)y9t\u0013\n}15\u001cE[\u0001(\n+2eV\u0016(0,(e\u0013\ng)y9t\u0013\n}15u\u001cE[\u0001(\n+2eV\u0016(0,(e\u0013\ng)y9t\u0013\n}15u\u0003MFT\u0004MFT8";
      int var4 = "E[\u0001(\n+2eV\u0016(0,(e\u0013\ng)y9t\u0013\n}15\u0004MFT8\u0002MF\u0005MFT8m\u001bE[\u0001(\n+2eV\u0016(0,(e\u0013\ng)y9t\u0013\n}15\u001cE[\u0001(\n+2eV\u0016(0,(e\u0013\ng)y9t\u0013\n}15u\u001cE[\u0001(\n+2eV\u0016(0,(e\u0013\ng)y9t\u0013\n}15u\u0003MFT\u0004MFT8".length();
      char var1 = 27;
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
            var10 = 3;
            var10001 = var10002;
            var8 = var10003;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 3;
               var10005 = var7;
            } else {
               var10 = 3;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label83;
               }

               var10004 = var10002;
               var12 = 3;
               var10005 = var7;
            }

            while(true) {
               var22 = var10004[var10005];
               switch(var7 % 7) {
               case 0:
                  var23 = 18;
                  break;
               case 1:
                  var23 = 48;
                  break;
               case 2:
                  var23 = 103;
                  break;
               case 3:
                  var23 = 11;
                  break;
               case 4:
                  var23 = 94;
                  break;
               case 5:
                  var23 = 90;
                  break;
               default:
                  var23 = 88;
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
            var2 = "\u0002 \u001fpi1^\u0005\bOQhpL\b\u001f\u001c@'%R\u0000\u000e\u0000AbpJ\b\u0001\u001a@=p";
            var4 = "\u0002 \u001fpi1^\u0005\bOQhpL\b\u001f\u001c@'%R\u0000\u000e\u0000AbpJ\b\u0001\u001a@=p".length();
            var1 = 2;
            var0 = -1;

            while(true) {
               label62: {
                  ++var0;
                  var10002 = var2.substring(var0, var0 + var1).toCharArray();
                  var10003 = var10002.length;
                  var7 = 0;
                  var10 = 55;
                  var10001 = var10002;
                  var8 = var10003;
                  if (var10003 <= 1) {
                     var10004 = var10002;
                     var12 = 55;
                     var10005 = var7;
                  } else {
                     var10 = 55;
                     var8 = var10003;
                     if (var10003 <= var7) {
                        break label62;
                     }

                     var10004 = var10002;
                     var12 = 55;
                     var10005 = var7;
                  }

                  while(true) {
                     var22 = var10004[var10005];
                     switch(var7 % 7) {
                     case 0:
                        var23 = 18;
                        break;
                     case 1:
                        var23 = 48;
                        break;
                     case 2:
                        var23 = 103;
                        break;
                     case 3:
                        var23 = 11;
                        break;
                     case 4:
                        var23 = 94;
                        break;
                     case 5:
                        var23 = 90;
                        break;
                     default:
                        var23 = 88;
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
                  Vulcan_o = String.valueOf('"');
                  Vulcan_C = new char[]{',', '"', '\r', '\n'};
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
