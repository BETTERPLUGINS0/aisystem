package ac.vulcan.anticheat;

import java.util.TimeZone;

public class Vulcan_i_ {
   public static final String Vulcan_c;
   static final Object Vulcan_q;
   static final Object Vulcan_t;
   static final Object Vulcan_S;
   static final Object Vulcan_d;
   static final Object Vulcan_a;
   static final Object Vulcan_p;
   static final Object Vulcan_f;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(4708855144202883195L, 628196618026356867L, (Object)null).a(270385624835402L);
   private static final String[] b;

   public static String Vulcan_D(Object[] var0) {
      long var1 = (Long)var0[0];
      long var3 = (Long)var0[1];
      var3 ^= a;
      long var5 = var3 ^ 25667162857304L;
      String var10001 = b[10];
      return Vulcan_d(new Object[]{new Long(var1), var10001, new Long(var5)});
   }

   public static String Vulcan_I(Object[] var0) {
      int var1 = (Integer)var0[0];
      int var5 = (Integer)var0[1];
      int var4 = (Integer)var0[2];
      long var2 = (Long)var0[3];
      long var6 = ((long)var1 << 48 | (long)var5 << 32 >>> 16 | (long)var4 << 48 >>> 48) ^ a;
      long var8 = var6 ^ 53762155761814L;
      String var10001 = b[4];
      return Vulcan_k(new Object[]{new Long(var2), var10001, new Long(var8), new Boolean(false)});
   }

   public static String Vulcan_d(Object[] var0) {
      long var4 = (Long)var0[0];
      String var3 = (String)var0[1];
      long var1 = (Long)var0[2];
      var1 ^= a;
      long var6 = var1 ^ 80580238165449L;
      return Vulcan_k(new Object[]{new Long(var4), var3, new Long(var6), new Boolean(true)});
   }

   public static String Vulcan_k(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_T(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static String Vulcan_n(Object[] var0) {
      long var1 = (Long)var0[0];
      long var3 = (Long)var0[1];
      long var5 = (Long)var0[2];
      var5 ^= a;
      long var7 = var5 ^ 4081780454890L;
      String var10003 = b[11];
      return Vulcan_K(new Object[]{new Long(var1), new Long(var3), new Long(var7), var10003, new Boolean(false), TimeZone.getDefault()});
   }

   public static String Vulcan_Z(Object[] var0) {
      long var2 = (Long)var0[0];
      long var4 = (Long)var0[1];
      String var1 = (String)var0[2];
      long var6 = (Long)var0[3];
      var6 ^= a;
      long var8 = var6 ^ 26218789441243L;
      return Vulcan_K(new Object[]{new Long(var2), new Long(var4), new Long(var8), var1, new Boolean(true), TimeZone.getDefault()});
   }

   public static String Vulcan_K(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   static String Vulcan_z(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   static Vulcan_bu[] Vulcan_l(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   static {
      String[] var5 = new String[20];
      int var3 = 0;
      String var2 = "p*8P9Tt>~k\tp+8P9Tt>~\bp+8K3Bi#\u0007p+8G=Nh!wJ?Z%NbwC?n{z<4=\\w{\u007f<\u0018=u\u0004\u0011\u0010h~I?p{\u0007p+8K3Bi\u0006p+8G=N\np*8P9Tt>~k\bp*8K3Bi#\np*8N5Yn$\u007fk\u000b\u0018 uNfDh~IKp!wJ?Z%NbwC?n{z<4=\\w{\u007f<\u0018=u\u0004\u0011\u0010h~I?p{\np+8N5Yn$\u007fk\u0007p*8G=Nh\np+8P9Tt>~k\u0007p*8G=Nh\np*8N5Yn$\u007fk\tp+8N5Yn$\u007f";
      int var4 = "p*8P9Tt>~k\tp+8P9Tt>~\bp+8K3Bi#\u0007p+8G=Nh!wJ?Z%NbwC?n{z<4=\\w{\u007f<\u0018=u\u0004\u0011\u0010h~I?p{\u0007p+8K3Bi\u0006p+8G=N\np*8P9Tt>~k\bp*8K3Bi#\np*8N5Yn$\u007fk\u000b\u0018 uNfDh~IKp!wJ?Z%NbwC?n{z<4=\\w{\u007f<\u0018=u\u0004\u0011\u0010h~I?p{\np+8N5Yn$\u007fk\u0007p*8G=Nh\np+8P9Tt>~k\u0007p*8G=Nh\np*8N5Yn$\u007fk\tp+8N5Yn$\u007f".length();
      char var1 = '\n';
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
            var10 = 5;
            var10001 = var10002;
            var8 = var10003;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 5;
               var10005 = var7;
            } else {
               var10 = 5;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label83;
               }

               var10004 = var10002;
               var12 = 5;
               var10005 = var7;
            }

            while(true) {
               var22 = var10004[var10005];
               switch(var7 % 7) {
               case 0:
                  var23 = 85;
                  break;
               case 1:
                  var23 = 31;
                  break;
               case 2:
                  var23 = 29;
                  break;
               case 3:
                  var23 = 38;
                  break;
               case 4:
                  var23 = 89;
                  break;
               case 5:
                  var23 = 50;
                  break;
               default:
                  var23 = 30;
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
            var2 = "#yk\u0018`\u0011:p*gnk\u0014n\u001d;#n\u0003W/\f'v;8P(\to#$\"\u001ez\u0010-pil\u0003(D;f*$\u001ek\u0017o";
            var4 = "#yk\u0018`\u0011:p*gnk\u0014n\u001d;#n\u0003W/\f'v;8P(\to#$\"\u001ez\u0010-pil\u0003(D;f*$\u001ek\u0017o".length();
            var1 = '\b';
            var0 = -1;

            while(true) {
               label62: {
                  ++var0;
                  var10002 = var2.substring(var0, var0 + var1).toCharArray();
                  var10003 = var10002.length;
                  var7 = 0;
                  var10 = 86;
                  var10001 = var10002;
                  var8 = var10003;
                  if (var10003 <= 1) {
                     var10004 = var10002;
                     var12 = 86;
                     var10005 = var7;
                  } else {
                     var10 = 86;
                     var8 = var10003;
                     if (var10003 <= var7) {
                        break label62;
                     }

                     var10004 = var10002;
                     var12 = 86;
                     var10005 = var7;
                  }

                  while(true) {
                     var22 = var10004[var10005];
                     switch(var7 % 7) {
                     case 0:
                        var23 = 85;
                        break;
                     case 1:
                        var23 = 31;
                        break;
                     case 2:
                        var23 = 29;
                        break;
                     case 3:
                        var23 = 38;
                        break;
                     case 4:
                        var23 = 89;
                        break;
                     case 5:
                        var23 = 50;
                        break;
                     default:
                        var23 = 30;
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
                  Vulcan_c = b[4];
                  Vulcan_q = "y";
                  Vulcan_t = "M";
                  Vulcan_S = "d";
                  Vulcan_d = "H";
                  Vulcan_a = "m";
                  Vulcan_p = "s";
                  Vulcan_f = "S";
                  return;
               }

               var1 = var2.charAt(var0);
            }
         }

         var1 = var2.charAt(var0);
      }
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
