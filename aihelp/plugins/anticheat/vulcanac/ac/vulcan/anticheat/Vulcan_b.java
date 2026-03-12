package ac.vulcan.anticheat;

public class Vulcan_b extends IllegalArgumentException {
   private static final long serialVersionUID = 4954193403612068178L;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(421979593491947636L, 8569660087422140653L, (Object)null).a(8608296182309L);
   private static final String[] b;

   public Vulcan_b(String var1) {
      super(var1 + b[0]);
   }

   public Vulcan_b(String var1, String[] var2) {
      long var3 = a ^ 14344704127641L;
      long var5 = var3 ^ 56348240723434L;
      super(var1 + b[1] + Vulcan_I(new Object[]{var2, new Long(var5)}));
   }

   private static final String Vulcan_I(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   static {
      String[] var5 = new String[2];
      int var3 = 0;
      String var2 = "@b>F<j>\u000ff=\n0p8N!@b>F8m.\u0013b#\u0001up5\u0005++\t9h2\u0017b#\u0001um)\u0005f>\\u";
      int var4 = "@b>F<j>\u000ff=\n0p8N!@b>F8m.\u0013b#\u0001up5\u0005++\t9h2\u0017b#\u0001um)\u0005f>\\u".length();
      char var1 = 15;
      int var0 = -1;

      while(true) {
         char[] var10001;
         label41: {
            ++var0;
            char[] var10002 = var2.substring(var0, var0 + var1).toCharArray();
            int var10003 = var10002.length;
            int var7 = 0;
            byte var10 = 112;
            var10001 = var10002;
            int var8 = var10003;
            byte var12;
            char[] var10004;
            int var10005;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 112;
               var10005 = var7;
            } else {
               var10 = 112;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label41;
               }

               var10004 = var10002;
               var12 = 112;
               var10005 = var7;
            }

            while(true) {
               char var22 = var10004[var10005];
               byte var23;
               switch(var7 % 7) {
               case 0:
                  var23 = 16;
                  break;
               case 1:
                  var23 = 123;
                  break;
               case 2:
                  var23 = 61;
                  break;
               case 3:
                  var23 = 22;
                  break;
               case 4:
                  var23 = 37;
                  break;
               case 5:
                  var23 = 116;
                  break;
               default:
                  var23 = 45;
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

   private static Vulcan_b a(Vulcan_b var0) {
      return var0;
   }
}
