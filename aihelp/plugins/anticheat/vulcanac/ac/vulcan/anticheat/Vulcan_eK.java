package ac.vulcan.anticheat;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;

public class Vulcan_eK implements Serializable {
   private static final long serialVersionUID = 1L;
   private static final transient String Vulcan_c;
   private Throwable Vulcan_z;
   public static boolean Vulcan_h;
   public static boolean Vulcan_x;
   public static boolean Vulcan__;
   // $FF: synthetic field
   static Class Vulcan_E;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-2468448315911780418L, 3398069621849834835L, (Object)null).a(183231150090253L);
   private static final String[] b;

   public Vulcan_eK(Vulcan_iX param1) {
      // $FF: Couldn't be decompiled
   }

   public String Vulcan_e(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public String Vulcan_S(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public String[] Vulcan_A(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Throwable Vulcan_m(Object[] var1) {
      long var3 = (Long)var1[0];
      int var2 = (Integer)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 115004119522784L;

      try {
         if (var2 == 0) {
            return this.Vulcan_z;
         }
      } catch (IllegalArgumentException var8) {
         throw a(var8);
      }

      Throwable[] var7 = this.Vulcan_W(new Object[]{new Long(var5)});
      return var7[var2];
   }

   public int Vulcan_N(Object[] var1) {
      long var2 = (Long)var1[0];
      var2 ^= a;
      long var4 = var2 ^ 122341135716389L;
      return Vulcan_e6.Vulcan_v(new Object[]{this.Vulcan_z, new Long(var4)});
   }

   public Throwable[] Vulcan_W(Object[] var1) {
      long var2 = (Long)var1[0];
      var2 ^= a;
      long var4 = var2 ^ 3042555699868L;
      Throwable var10001 = this.Vulcan_z;
      return Vulcan_e6.Vulcan_r(new Object[]{new Long(var4), var10001});
   }

   public int Vulcan_a(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_k(Object[] var1) {
      long var2 = (Long)var1[0];
      var2 ^= a;
      long var4 = var2 ^ 69394383727365L;
      this.Vulcan_b(new Object[]{System.err, new Long(var4)});
   }

   public void Vulcan_b(Object[] var1) {
      PrintStream var2 = (PrintStream)var1[0];
      long var3 = (Long)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 77861945540261L;
      synchronized(var2) {
         PrintWriter var8 = new PrintWriter(var2, false);
         this.Vulcan_s(new Object[]{var8, new Long(var5)});
         var8.flush();
      }
   }

   public void Vulcan_s(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected String[] Vulcan_o(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected void Vulcan_q(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   static Class Vulcan_V(Object[] var0) {
      String var1 = (String)var0[0];

      try {
         return Class.forName(me.frep.vulcan.spigot.Vulcan_s.a(var1));
      } catch (ClassNotFoundException var3) {
         throw new NoClassDefFoundError(var3.getMessage());
      }
   }

   static {
      String[] var5 = new String[12];
      int var3 = 0;
      String var2 = "uP_L\u000b\u0016\u000f\u0017\u001f3h47\u0017XL\u001d4\rL\u001a#`w4\u0000L\r8x}6\u0006\u0007\r\"\"B \u0002\u0001\r8S}\r\u0005\\@LBv\r\u0007\u000b\u0016\u0004$cc;N\u0003\u001fl,#\u0001\u0006\u0007L%xu'\u001aB\u00058hq-N\u0015\r%,{ \u001aB\u00030,v:\u001b\f\b%64#\u0001\u0006\u0007L%xu'\u001aB\u00058hq-N\u0015\r%,{ \u001aB\u00030,v:\u001b\f\b%64p\u0001\u0006\u0007L\u0018ig!\u000f\u0000\u00003,}8\u001e\u000e\t;iz!\u000f\u0016\u00059b4%\u000f\u0011\u001f3h4!\u0001B\u0018>i4\u001b\u000b\u0011\u00187nx0*\u0007\u00003ku!\u000bJ\"3\u007f`4\f\u000e\t\u007f,w:\u0000\u0011\u0018$yw!\u0001\u0010L;yg!N\u0007\u0014\"iz1N\b\r m:9\u000f\f\u000bxX|'\u0001\u0015\r4`qp\u0001\u0006\u0007L\u0018ig!\u000f\u0000\u00003,}8\u001e\u000e\t;iz!\u000f\u0016\u00059b4%\u000f\u0011\u001f3h4!\u0001B\u0018>i4\u001b\u000b\u0011\u00187nx0*\u0007\u00003ku!\u000bJ\"3\u007f`4\f\u000e\t\u007f,w:\u0000\u0011\u0018$yw!\u0001\u0010L;yg!N\u0007\u0014\"iz1N\b\r m:9\u000f\f\u000bxX|'\u0001\u0015\r4`q\u0005u\u0003\r\u001e3";
      int var4 = "uP_L\u000b\u0016\u000f\u0017\u001f3h47\u0017XL\u001d4\rL\u001a#`w4\u0000L\r8x}6\u0006\u0007\r\"\"B \u0002\u0001\r8S}\r\u0005\\@LBv\r\u0007\u000b\u0016\u0004$cc;N\u0003\u001fl,#\u0001\u0006\u0007L%xu'\u001aB\u00058hq-N\u0015\r%,{ \u001aB\u00030,v:\u001b\f\b%64#\u0001\u0006\u0007L%xu'\u001aB\u00058hq-N\u0015\r%,{ \u001aB\u00030,v:\u001b\f\b%64p\u0001\u0006\u0007L\u0018ig!\u000f\u0000\u00003,}8\u001e\u000e\t;iz!\u000f\u0016\u00059b4%\u000f\u0011\u001f3h4!\u0001B\u0018>i4\u001b\u000b\u0011\u00187nx0*\u0007\u00003ku!\u000bJ\"3\u007f`4\f\u000e\t\u007f,w:\u0000\u0011\u0018$yw!\u0001\u0010L;yg!N\u0007\u0014\"iz1N\b\r m:9\u000f\f\u000bxX|'\u0001\u0015\r4`qp\u0001\u0006\u0007L\u0018ig!\u000f\u0000\u00003,}8\u001e\u000e\t;iz!\u000f\u0016\u00059b4%\u000f\u0011\u001f3h4!\u0001B\u0018>i4\u001b\u000b\u0011\u00187nx0*\u0007\u00003ku!\u000bJ\"3\u007f`4\f\u000e\t\u007f,w:\u0000\u0011\u0018$yw!\u0001\u0010L;yg!N\u0007\u0014\"iz1N\b\r m:9\u000f\f\u000bxX|'\u0001\u0015\r4`q\u0005u\u0003\r\u001e3".length();
      char var1 = 4;
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
            var10 = 73;
            var10001 = var10002;
            var8 = var10003;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 73;
               var10005 = var7;
            } else {
               var10 = 73;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label83;
               }

               var10004 = var10002;
               var12 = 73;
               var10005 = var7;
            }

            while(true) {
               var22 = var10004[var10005];
               switch(var7 % 7) {
               case 0:
                  var23 = 28;
                  break;
               case 1:
                  var23 = 39;
                  break;
               case 2:
                  var23 = 43;
                  break;
               case 3:
                  var23 = 37;
                  break;
               case 4:
                  var23 = 31;
                  break;
               case 5:
                  var23 = 69;
                  break;
               default:
                  var23 = 93;
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
            var2 = "-\f\u001dvO\u000eXa\"5vB\u000eOz:?tDEO``\u0000b@COz\u0011?O";
            var4 = "-\f\u001dvO\u000eXa\"5vB\u000eOz:?tDEO``\u0000b@COz\u0011?O".length();
            var1 = 2;
            var0 = -1;

            while(true) {
               label62: {
                  ++var0;
                  var10002 = var2.substring(var0, var0 + var1).toCharArray();
                  var10003 = var10002.length;
                  var7 = 0;
                  var10 = 11;
                  var10001 = var10002;
                  var8 = var10003;
                  if (var10003 <= 1) {
                     var10004 = var10002;
                     var12 = 11;
                     var10005 = var7;
                  } else {
                     var10 = 11;
                     var8 = var10003;
                     if (var10003 <= var7) {
                        break label62;
                     }

                     var10004 = var10002;
                     var12 = 11;
                     var10005 = var7;
                  }

                  while(true) {
                     var22 = var10004[var10005];
                     switch(var7 % 7) {
                     case 0:
                        var23 = 28;
                        break;
                     case 1:
                        var23 = 39;
                        break;
                     case 2:
                        var23 = 43;
                        break;
                     case 3:
                        var23 = 37;
                        break;
                     case 4:
                        var23 = 31;
                        break;
                     case 5:
                        var23 = 69;
                        break;
                     default:
                        var23 = 93;
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
                  Vulcan_c = b[7];
                  Vulcan_h = true;
                  Vulcan_x = true;
                  Vulcan__ = true;
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
