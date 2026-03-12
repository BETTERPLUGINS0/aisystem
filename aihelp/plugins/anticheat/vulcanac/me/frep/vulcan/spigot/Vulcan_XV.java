package me.frep.vulcan.spigot;

public enum Vulcan_XV {
   public static final Vulcan_XV MISS;
   public static final Vulcan_XV BLOCK;
   public static final Vulcan_XV ENTITY;
   private static final Vulcan_XV[] Vulcan_n;

   static {
      String[] var6 = new String[3];
      int var4 = 0;
      String var3 = "yvK\"uL\u0005~tP(j\u0004qqL8";
      int var5 = "yvK\"uL\u0005~tP(j\u0004qqL8".length();
      char var2 = 6;
      int var1 = -1;

      while(true) {
         char[] var10002;
         label41: {
            ++var1;
            char[] var10003 = var3.substring(var1, var1 + var2).toCharArray();
            int var10004 = var10003.length;
            int var7 = 0;
            byte var11 = 118;
            var10002 = var10003;
            int var8 = var10004;
            byte var12;
            char[] var10005;
            int var10006;
            if (var10004 <= 1) {
               var10005 = var10003;
               var12 = 118;
               var10006 = var7;
            } else {
               var11 = 118;
               var8 = var10004;
               if (var10004 <= var7) {
                  break label41;
               }

               var10005 = var10003;
               var12 = 118;
               var10006 = var7;
            }

            while(true) {
               char var22 = var10005[var10006];
               byte var23;
               switch(var7 % 7) {
               case 0:
                  var23 = 74;
                  break;
               case 1:
                  var23 = 78;
                  break;
               case 2:
                  var23 = 105;
                  break;
               case 3:
                  var23 = 29;
                  break;
               case 4:
                  var23 = 87;
                  break;
               case 5:
                  var23 = 99;
                  break;
               default:
                  var23 = 95;
               }

               var10005[var10006] = (char)(var22 ^ var12 ^ var23);
               ++var7;
               if (var11 == 0) {
                  var10006 = var11;
                  var10005 = var10002;
                  var12 = var11;
               } else {
                  if (var8 <= var7) {
                     break;
                  }

                  var10005 = var10002;
                  var12 = var11;
                  var10006 = var7;
               }
            }
         }

         String var13 = (new String(var10002)).intern();
         boolean var10 = true;
         var6[var4++] = var13;
         if ((var1 += var2) >= var5) {
            MISS = new Vulcan_XV(var6[2], 0);
            BLOCK = new Vulcan_XV(var6[1], 1);
            ENTITY = new Vulcan_XV(var6[0], 2);
            Vulcan_n = new Vulcan_XV[]{MISS, BLOCK, ENTITY};
            return;
         }

         var2 = var3.charAt(var1);
      }
   }
}
