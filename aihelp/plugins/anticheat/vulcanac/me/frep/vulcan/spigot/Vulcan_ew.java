package me.frep.vulcan.spigot;

final class Vulcan_ew {
   static final int[] Vulcan_U;
   static final int[] Vulcan_b;
   static final int[] Vulcan_g;
   private static final String Vulcan_v;

   static {
      char[] var10003 = "\u0013t\fu\t=\u0011b\u000baw".toCharArray();
      int var10004 = var10003.length;
      int var0 = 0;
      byte var27 = 107;
      char[] var10002 = var10003;
      int var24 = var10004;
      byte var32;
      String var10000;
      int var10005;
      char var10006;
      byte var10007;
      char var10008;
      boolean var26;
      char[] var28;
      String var29;
      if (var10004 <= 1) {
         var28 = var10003;
         var10005 = var0;
         var10008 = var10003[var0];
         var10007 = 107;
         var10006 = var10008;
         switch(var0 % 7) {
         case 0:
            var32 = 59;
            break;
         case 1:
            var32 = 83;
            break;
         case 2:
            var32 = 56;
            break;
         case 3:
            var32 = 46;
            break;
         case 4:
            var32 = 82;
            break;
         case 5:
            var32 = 102;
            break;
         default:
            var32 = 74;
         }
      } else {
         var27 = 107;
         var24 = var10004;
         if (var10004 <= var0) {
            var29 = (new String(var10003)).intern();
            var26 = true;
            Vulcan_v = var29;
            var10000 = Vulcan_v;
            Vulcan_g = new int[Vulcan_e2.values().length];

            try {
               Vulcan_g[Vulcan_e2.HORIZONTAL.ordinal()] = 1;
            } catch (NoSuchFieldError var13) {
            }

            try {
               Vulcan_g[Vulcan_e2.VERTICAL.ordinal()] = 2;
            } catch (NoSuchFieldError var14) {
            }

            Vulcan_b = new int[Vulcan_Xy.values().length];

            try {
               Vulcan_b[Vulcan_Xy.NORTH.ordinal()] = 1;
            } catch (NoSuchFieldError var15) {
            }

            try {
               Vulcan_b[Vulcan_Xy.EAST.ordinal()] = 2;
            } catch (NoSuchFieldError var16) {
            }

            try {
               Vulcan_b[Vulcan_Xy.SOUTH.ordinal()] = 3;
            } catch (NoSuchFieldError var17) {
            }

            try {
               Vulcan_b[Vulcan_Xy.WEST.ordinal()] = 4;
            } catch (NoSuchFieldError var18) {
            }

            try {
               Vulcan_b[Vulcan_Xy.UP.ordinal()] = 5;
            } catch (NoSuchFieldError var19) {
            }

            try {
               Vulcan_b[Vulcan_Xy.DOWN.ordinal()] = 6;
            } catch (NoSuchFieldError var20) {
            }

            Vulcan_U = new int[Vulcan_iK.values().length];

            try {
               Vulcan_U[Vulcan_iK.X.ordinal()] = 1;
            } catch (NoSuchFieldError var21) {
            }

            try {
               Vulcan_U[Vulcan_iK.Y.ordinal()] = 2;
            } catch (NoSuchFieldError var22) {
            }

            try {
               Vulcan_U[Vulcan_iK.Z.ordinal()] = 3;
            } catch (NoSuchFieldError var23) {
            }

            return;
         }

         var28 = var10003;
         var10005 = var0;
         var10008 = var10003[var0];
         var10007 = 107;
         var10006 = var10008;
         switch(var0 % 7) {
         case 0:
            var32 = 59;
            break;
         case 1:
            var32 = 83;
            break;
         case 2:
            var32 = 56;
            break;
         case 3:
            var32 = 46;
            break;
         case 4:
            var32 = 82;
            break;
         case 5:
            var32 = 102;
            break;
         default:
            var32 = 74;
         }
      }

      while(true) {
         while(true) {
            var28[var10005] = (char)(var10006 ^ var10007 ^ var32);
            ++var0;
            if (var27 == 0) {
               var28 = var10002;
               var10005 = var27;
               var10008 = var10002[var27];
               var10007 = var27;
               var10006 = var10008;
               switch(var0 % 7) {
               case 0:
                  var32 = 59;
                  break;
               case 1:
                  var32 = 83;
                  break;
               case 2:
                  var32 = 56;
                  break;
               case 3:
                  var32 = 46;
                  break;
               case 4:
                  var32 = 82;
                  break;
               case 5:
                  var32 = 102;
                  break;
               default:
                  var32 = 74;
               }
            } else {
               if (var24 <= var0) {
                  var29 = (new String(var10002)).intern();
                  var26 = true;
                  Vulcan_v = var29;
                  var10000 = Vulcan_v;
                  Vulcan_g = new int[Vulcan_e2.values().length];

                  try {
                     Vulcan_g[Vulcan_e2.HORIZONTAL.ordinal()] = 1;
                  } catch (NoSuchFieldError var12) {
                  }

                  try {
                     Vulcan_g[Vulcan_e2.VERTICAL.ordinal()] = 2;
                  } catch (NoSuchFieldError var11) {
                  }

                  Vulcan_b = new int[Vulcan_Xy.values().length];

                  try {
                     Vulcan_b[Vulcan_Xy.NORTH.ordinal()] = 1;
                  } catch (NoSuchFieldError var10) {
                  }

                  try {
                     Vulcan_b[Vulcan_Xy.EAST.ordinal()] = 2;
                  } catch (NoSuchFieldError var9) {
                  }

                  try {
                     Vulcan_b[Vulcan_Xy.SOUTH.ordinal()] = 3;
                  } catch (NoSuchFieldError var8) {
                  }

                  try {
                     Vulcan_b[Vulcan_Xy.WEST.ordinal()] = 4;
                  } catch (NoSuchFieldError var7) {
                  }

                  try {
                     Vulcan_b[Vulcan_Xy.UP.ordinal()] = 5;
                  } catch (NoSuchFieldError var6) {
                  }

                  try {
                     Vulcan_b[Vulcan_Xy.DOWN.ordinal()] = 6;
                  } catch (NoSuchFieldError var5) {
                  }

                  Vulcan_U = new int[Vulcan_iK.values().length];

                  try {
                     Vulcan_U[Vulcan_iK.X.ordinal()] = 1;
                  } catch (NoSuchFieldError var4) {
                  }

                  try {
                     Vulcan_U[Vulcan_iK.Y.ordinal()] = 2;
                  } catch (NoSuchFieldError var3) {
                  }

                  try {
                     Vulcan_U[Vulcan_iK.Z.ordinal()] = 3;
                  } catch (NoSuchFieldError var2) {
                  }

                  return;
               }

               var28 = var10002;
               var10005 = var0;
               var10008 = var10002[var0];
               var10007 = var27;
               var10006 = var10008;
               switch(var0 % 7) {
               case 0:
                  var32 = 59;
                  break;
               case 1:
                  var32 = 83;
                  break;
               case 2:
                  var32 = 56;
                  break;
               case 3:
                  var32 = 46;
                  break;
               case 4:
                  var32 = 82;
                  break;
               case 5:
                  var32 = 102;
                  break;
               default:
                  var32 = 74;
               }
            }
         }
      }
   }
}
