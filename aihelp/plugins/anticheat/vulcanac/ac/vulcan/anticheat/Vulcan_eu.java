package ac.vulcan.anticheat;

public class Vulcan_eu extends IllegalArgumentException {
   private static final long serialVersionUID = 8063272569377254819L;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-376057088647711212L, 5853027815096702039L, (Object)null).a(44370590873622L);
   private static final String[] b;

   public Vulcan_eu(Class var1, Object var2) {
      long var3 = a ^ 26479496816087L;
      long var5 = var3 ^ 13353661203074L;
      StringBuffer var10001 = new StringBuffer();
      String[] var7 = b;
      super(var10001.append(var7[2]).append(Vulcan_T(new Object[]{new Long(var5), var1})).append(var7[1]).append(var2 == null ? var7[0] : var2.getClass().getName()).toString());
   }

   public Vulcan_eu(Class var1, Class var2) {
      long var3 = a ^ 128506616672195L;
      long var5 = var3 ^ 106448046983318L;
      StringBuffer var10001 = new StringBuffer();
      String[] var7 = b;
      super(var10001.append(var7[3]).append(Vulcan_T(new Object[]{new Long(var5), var1})).append(var7[4]).append(Vulcan_T(new Object[]{new Long(var5), var2})).toString());
   }

   public Vulcan_eu(String var1) {
      super(var1);
   }

   private static final String Vulcan_T(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   static {
      String[] var5 = new String[5];
      int var3 = 0;
      String var2 = "v]:,\n4\b7#L<^t\u0012v\n]P&%[=Z|\u0012v";
      int var4 = "v]:,\n4\b7#L<^t\u0012v\n]P&%[=Z|\u0012v".length();
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
            var10 = 87;
            var10001 = var10002;
            var8 = var10003;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 87;
               var10005 = var7;
            } else {
               var10 = 87;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label83;
               }

               var10004 = var10002;
               var12 = 87;
               var10005 = var7;
            }

            while(true) {
               var22 = var10004[var10005];
               switch(var7 % 7) {
               case 0:
                  var23 = 79;
                  break;
               case 1:
                  var23 = 127;
                  break;
               case 2:
                  var23 = 1;
                  break;
               case 3:
                  var23 = 23;
                  break;
               case 4:
                  var23 = 111;
                  break;
               case 5:
                  var23 = 30;
                  break;
               default:
                  var23 = 104;
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
            var2 = "\u007fr\u0004\u0007y\u001fx^0T\n\u0016*\u0015\u0001n\u001e|V0T";
            var4 = "\u007fr\u0004\u0007y\u001fx^0T\n\u0016*\u0015\u0001n\u001e|V0T".length();
            var1 = '\n';
            var0 = -1;

            while(true) {
               label62: {
                  ++var0;
                  var10002 = var2.substring(var0, var0 + var1).toCharArray();
                  var10003 = var10002.length;
                  var7 = 0;
                  var10 = 117;
                  var10001 = var10002;
                  var8 = var10003;
                  if (var10003 <= 1) {
                     var10004 = var10002;
                     var12 = 117;
                     var10005 = var7;
                  } else {
                     var10 = 117;
                     var8 = var10003;
                     if (var10003 <= var7) {
                        break label62;
                     }

                     var10004 = var10002;
                     var12 = 117;
                     var10005 = var7;
                  }

                  while(true) {
                     var22 = var10004[var10005];
                     switch(var7 % 7) {
                     case 0:
                        var23 = 79;
                        break;
                     case 1:
                        var23 = 127;
                        break;
                     case 2:
                        var23 = 1;
                        break;
                     case 3:
                        var23 = 23;
                        break;
                     case 4:
                        var23 = 111;
                        break;
                     case 5:
                        var23 = 30;
                        break;
                     default:
                        var23 = 104;
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
                  return;
               }

               var1 = var2.charAt(var0);
            }
         }

         var1 = var2.charAt(var0);
      }
   }

   private static Vulcan_eu a(Vulcan_eu var0) {
      return var0;
   }
}
