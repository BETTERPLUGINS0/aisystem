package ac.vulcan.anticheat;

import java.io.UnsupportedEncodingException;

public class Vulcan_eD {
   public static final String Vulcan_W;
   public static final String Vulcan_I;
   public static final String Vulcan_Y;
   public static final String Vulcan_K;
   public static final String Vulcan_D;
   public static final String Vulcan_M;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-1699520534545362474L, 2072885907363619538L, (Object)null).a(208023593704718L);

   public static boolean Vulcan_R(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   static {
      String[] var6 = new String[6];
      int var4 = 0;
      String var3 = "\\lm\u0010I>\b\\lm\u0010I>;L\n@kd\u0010@0L0\u0015\u001a\u0005\\lm\u0010@";
      int var5 = "\\lm\u0010I>\b\\lm\u0010I>;L\n@kd\u0010@0L0\u0015\u001a\u0005\\lm\u0010@".length();
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
            var11 = 57;
            var10001 = var10002;
            var9 = var10003;
            if (var10003 <= 1) {
               var10004 = var10002;
               var13 = 57;
               var10005 = var8;
            } else {
               var11 = 57;
               var9 = var10003;
               if (var10003 <= var8) {
                  break label83;
               }

               var10004 = var10002;
               var13 = 57;
               var10005 = var8;
            }

            while(true) {
               var23 = var10004[var10005];
               switch(var8 % 7) {
               case 0:
                  var24 = 48;
                  break;
               case 1:
                  var24 = 1;
                  break;
               case 2:
                  var24 = 18;
                  break;
               case 3:
                  var24 = 4;
                  break;
               case 4:
                  var24 = 65;
                  break;
               case 5:
                  var24 = 49;
                  break;
               default:
                  var24 = 64;
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
            var3 = "L{\u0016l;[ P\bL|}\u0000Y.%\\";
            var5 = "L{\u0016l;[ P\bL|}\u0000Y.%\\".length();
            var2 = '\b';
            var1 = -1;

            while(true) {
               label62: {
                  ++var1;
                  var10002 = var3.substring(var1, var1 + var2).toCharArray();
                  var10003 = var10002.length;
                  var8 = 0;
                  var11 = 41;
                  var10001 = var10002;
                  var9 = var10003;
                  if (var10003 <= 1) {
                     var10004 = var10002;
                     var13 = 41;
                     var10005 = var8;
                  } else {
                     var11 = 41;
                     var9 = var10003;
                     if (var10003 <= var8) {
                        break label62;
                     }

                     var10004 = var10002;
                     var13 = 41;
                     var10005 = var8;
                  }

                  while(true) {
                     var23 = var10004[var10005];
                     switch(var8 % 7) {
                     case 0:
                        var24 = 48;
                        break;
                     case 1:
                        var24 = 1;
                        break;
                     case 2:
                        var24 = 18;
                        break;
                     case 3:
                        var24 = 4;
                        break;
                     case 4:
                        var24 = 65;
                        break;
                     case 5:
                        var24 = 49;
                        break;
                     default:
                        var24 = 64;
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
                  Vulcan_D = var6[5];
                  Vulcan_Y = var6[0];
                  Vulcan_W = var6[2];
                  Vulcan_I = var6[4];
                  Vulcan_M = var6[3];
                  Vulcan_K = var6[1];
                  return;
               }

               var2 = var3.charAt(var1);
            }
         }

         var2 = var3.charAt(var1);
      }
   }

   private static UnsupportedEncodingException a(UnsupportedEncodingException var0) {
      return var0;
   }
}
