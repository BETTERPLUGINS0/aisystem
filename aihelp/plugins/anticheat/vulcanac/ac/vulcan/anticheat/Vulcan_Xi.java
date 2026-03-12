package ac.vulcan.anticheat;

public class Vulcan_Xi extends IllegalArgumentException {
   private static final long serialVersionUID = 1174360235354917591L;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(9173069417847801342L, -2523725440053026160L, (Object)null).a(260811364039821L);
   private static final String[] b;

   public Vulcan_Xi(String var1) {
      long var2 = a ^ 30680062753501L;
      StringBuffer var10001 = new StringBuffer();
      String var10002;
      if (var1 == null) {
         String[] var4 = b;
         var10002 = var4[1];
      } else {
         var10002 = var1;
      }

      super(var10001.append(var10002).append(b[0]).toString());
   }

   static {
      String[] var5 = new String[2];
      int var3 = 0;
      String var2 = "\u0017=VU05QX$\u0003D!5QB<O\b\bv\"DS)pQC";
      int var4 = "\u0017=VU05QX$\u0003D!5QB<O\b\bv\"DS)pQC".length();
      char var1 = 18;
      int var0 = -1;

      while(true) {
         char[] var10001;
         label41: {
            ++var0;
            char[] var10002 = var2.substring(var0, var0 + var1).toCharArray();
            int var10003 = var10002.length;
            int var7 = 0;
            byte var10 = 74;
            var10001 = var10002;
            int var8 = var10003;
            byte var12;
            char[] var10004;
            int var10005;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 74;
               var10005 = var7;
            } else {
               var10 = 74;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label41;
               }

               var10004 = var10002;
               var12 = 74;
               var10005 = var7;
            }

            while(true) {
               char var22 = var10004[var10005];
               byte var23;
               switch(var7 % 7) {
               case 0:
                  var23 = 125;
                  break;
               case 1:
                  var23 = 26;
                  break;
               case 2:
                  var23 = 105;
                  break;
               case 3:
                  var23 = 108;
                  break;
               case 4:
                  var23 = 14;
                  break;
               case 5:
                  var23 = 95;
                  break;
               default:
                  var23 = 117;
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
}
