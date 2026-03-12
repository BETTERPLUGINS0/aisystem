package me.frep.vulcan.spigot;

public enum Vulcan_XE {
   public static final Vulcan_XE MISS;
   public static final Vulcan_XE BLOCK;
   public static final Vulcan_XE ENTITY;
   private static final Vulcan_XE[] Vulcan_j;

   static {
      String[] var6 = new String[3];
      int var4 = 0;
      String var3 = "y\u0003~m\u0018\u0004v\u0006b}\u0006~\u0001eg\u0007*";
      int var5 = "y\u0003~m\u0018\u0004v\u0006b}\u0006~\u0001eg\u0007*".length();
      char var2 = 5;
      int var1 = -1;

      while(true) {
         char[] var10002;
         label41: {
            ++var1;
            char[] var10003 = var3.substring(var1, var1 + var2).toCharArray();
            int var10004 = var10003.length;
            int var7 = 0;
            byte var11 = 19;
            var10002 = var10003;
            int var8 = var10004;
            byte var12;
            char[] var10005;
            int var10006;
            if (var10004 <= 1) {
               var10005 = var10003;
               var12 = 19;
               var10006 = var7;
            } else {
               var11 = 19;
               var8 = var10004;
               if (var10004 <= var7) {
                  break label41;
               }

               var10005 = var10003;
               var12 = 19;
               var10006 = var7;
            }

            while(true) {
               char var22 = var10005[var10006];
               byte var23;
               switch(var7 % 7) {
               case 0:
                  var23 = 40;
                  break;
               case 1:
                  var23 = 92;
                  break;
               case 2:
                  var23 = 34;
                  break;
               case 3:
                  var23 = 61;
                  break;
               case 4:
                  var23 = 64;
                  break;
               case 5:
                  var23 = 96;
                  break;
               default:
                  var23 = 23;
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
            MISS = new Vulcan_XE(var6[1], 0);
            BLOCK = new Vulcan_XE(var6[0], 1);
            ENTITY = new Vulcan_XE(var6[2], 2);
            Vulcan_j = new Vulcan_XE[]{MISS, BLOCK, ENTITY};
            return;
         }

         var2 = var3.charAt(var1);
      }
   }
}
