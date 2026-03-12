package me.frep.vulcan.spigot;

enum Vulcan_iP {
   public static final Vulcan_iP NETHERITE;
   public static final Vulcan_iP DIAMOND;
   public static final Vulcan_iP GOLD;
   public static final Vulcan_iP IRON;
   public static final Vulcan_iP STONE;
   public static final Vulcan_iP WOOD;
   private final int Vulcan_I;
   private static final Vulcan_iP[] Vulcan_c;

   private Vulcan_iP(int var3) {
      this.Vulcan_I = var3;
   }

   private int Vulcan_q() {
      return this.Vulcan_I;
   }

   static int Vulcan_W(Vulcan_iP var0) {
      return var0.Vulcan_q();
   }

   static {
      String[] var6 = new String[6];
      int var4 = 0;
      String var3 = "\nnxs\u0004\u0014sxy\u0004\u0004s{y\t\rycu\\0O\u0017y";
      int var5 = "\nnxs\u0004\u0014sxy\u0004\u0004s{y\t\rycu\\0O\u0017y".length();
      char var2 = 4;
      int var1 = -1;

      label55:
      while(true) {
         byte var10000 = 31;
         ++var1;
         String var10001 = var3.substring(var1, var1 + var2);
         byte var10002 = -1;

         while(true) {
            char[] var14;
            label50: {
               char[] var15 = var10001.toCharArray();
               int var10004 = var15.length;
               int var7 = 0;
               byte var18 = var10000;
               byte var17 = var10000;
               var14 = var15;
               int var11 = var10004;
               char[] var19;
               int var10006;
               if (var10004 <= 1) {
                  var19 = var15;
                  var10006 = var7;
               } else {
                  var17 = var10000;
                  var11 = var10004;
                  if (var10004 <= var7) {
                     break label50;
                  }

                  var19 = var15;
                  var10006 = var7;
               }

               while(true) {
                  char var28 = var19[var10006];
                  byte var29;
                  switch(var7 % 7) {
                  case 0:
                     var29 = 92;
                     break;
                  case 1:
                     var29 = 35;
                     break;
                  case 2:
                     var29 = 40;
                     break;
                  case 3:
                     var29 = 34;
                     break;
                  case 4:
                     var29 = 6;
                     break;
                  case 5:
                     var29 = 125;
                     break;
                  default:
                     var29 = 25;
                  }

                  var19[var10006] = (char)(var28 ^ var18 ^ var29);
                  ++var7;
                  if (var17 == 0) {
                     var10006 = var17;
                     var19 = var14;
                     var18 = var17;
                  } else {
                     if (var11 <= var7) {
                        break;
                     }

                     var19 = var14;
                     var18 = var17;
                     var10006 = var7;
                  }
               }
            }

            String var21 = (new String(var14)).intern();
            switch(var10002) {
            case 0:
               var6[var4++] = var21;
               if ((var1 += var2) >= var5) {
                  NETHERITE = new Vulcan_iP(var6[3], 0, 9);
                  DIAMOND = new Vulcan_iP(var6[4], 1, 8);
                  GOLD = new Vulcan_iP(var6[2], 2, 12);
                  IRON = new Vulcan_iP(var6[0], 3, 6);
                  STONE = new Vulcan_iP(var6[5], 4, 4);
                  WOOD = new Vulcan_iP(var6[1], 5, 2);
                  Vulcan_c = new Vulcan_iP[]{NETHERITE, DIAMOND, GOLD, IRON, STONE, WOOD};
                  return;
               }

               var2 = var3.charAt(var1);
               break;
            default:
               var6[var4++] = var21;
               if ((var1 += var2) < var5) {
                  var2 = var3.charAt(var1);
                  continue label55;
               }

               var3 = "9KHNh\u0012|\u0005.VFMb";
               var5 = "9KHNh\u0012|\u0005.VFMb".length();
               var2 = 7;
               var1 = -1;
            }

            var10000 = 33;
            ++var1;
            var10001 = var3.substring(var1, var1 + var2);
            var10002 = 0;
         }
      }
   }
}
