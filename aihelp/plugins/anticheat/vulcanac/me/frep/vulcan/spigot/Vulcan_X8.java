package me.frep.vulcan.spigot;

import org.bukkit.entity.Entity;

public class Vulcan_X8 {
   private Vulcan_iD Vulcan_u;
   public Vulcan_XE Vulcan_l;
   public Vulcan_Xy Vulcan_F;
   public Vulcan_ez Vulcan_v;
   public Entity Vulcan_Z;
   private static final String[] a;

   public Vulcan_X8(Vulcan_ez var1, Vulcan_Xy var2, Vulcan_iD var3) {
      this(Vulcan_XE.BLOCK, var1, var2, var3);
   }

   public Vulcan_X8(Vulcan_ez var1, Vulcan_Xy var2) {
      this(Vulcan_XE.BLOCK, var1, var2, Vulcan_iD.Vulcan_t);
   }

   public Vulcan_X8(Entity var1) {
      this(var1, new Vulcan_ez(var1.getLocation().getX(), var1.getLocation().getY(), var1.getLocation().getZ()));
   }

   public Vulcan_X8(Vulcan_XE var1, Vulcan_ez var2, Vulcan_Xy var3, Vulcan_iD var4) {
      this.Vulcan_l = var1;
      this.Vulcan_u = var4;
      this.Vulcan_F = var3;
      this.Vulcan_v = new Vulcan_ez(var2.Vulcan_N(new Object[0]), var2.Vulcan_H(new Object[0]), var2.Vulcan_P(new Object[0]));
   }

   public Vulcan_X8(Entity var1, Vulcan_ez var2) {
      this.Vulcan_l = Vulcan_XE.ENTITY;
      this.Vulcan_Z = var1;
      this.Vulcan_v = var2;
   }

   public Vulcan_iD Vulcan_Y(Object[] var1) {
      return this.Vulcan_u;
   }

   public String toString() {
      StringBuilder var10000 = new StringBuilder();
      String[] var1 = a;
      return var10000.append(var1[0]).append(this.Vulcan_l).append(var1[2]).append(this.Vulcan_u).append(var1[1]).append(this.Vulcan_F).append(var1[4]).append(this.Vulcan_v).append(var1[3]).append(this.Vulcan_Z).append('}').toString();
   }

   static {
      String[] var5 = new String[5];
      int var3 = 0;
      String var2 = "SuVN5MdwhYh)Nt&\u00047<D!\u000b7<@p?]zksQ!";
      int var4 = "SuVN5MdwhYh)Nt&\u00047<D!\u000b7<@p?]zksQ!".length();
      char var1 = 15;
      int var0 = -1;

      label55:
      while(true) {
         byte var10000 = 106;
         ++var0;
         String var10001 = var2.substring(var0, var0 + var1);
         byte var10002 = -1;

         while(true) {
            char[] var13;
            label50: {
               char[] var14 = var10001.toCharArray();
               int var10004 = var14.length;
               int var6 = 0;
               byte var17 = var10000;
               byte var16 = var10000;
               var13 = var14;
               int var10 = var10004;
               char[] var18;
               int var10006;
               if (var10004 <= 1) {
                  var18 = var14;
                  var10006 = var6;
               } else {
                  var16 = var10000;
                  var10 = var10004;
                  if (var10004 <= var6) {
                     break label50;
                  }

                  var18 = var14;
                  var10006 = var6;
               }

               while(true) {
                  char var27 = var18[var10006];
                  byte var28;
                  switch(var6 % 7) {
                  case 0:
                     var28 = 113;
                     break;
                  case 1:
                     var28 = 118;
                     break;
                  case 2:
                     var28 = 72;
                     break;
                  case 3:
                     var28 = 118;
                     break;
                  case 4:
                     var28 = 58;
                     break;
                  case 5:
                     var28 = 84;
                     break;
                  default:
                     var28 = 123;
                  }

                  var18[var10006] = (char)(var27 ^ var17 ^ var28);
                  ++var6;
                  if (var16 == 0) {
                     var10006 = var16;
                     var18 = var13;
                     var17 = var16;
                  } else {
                     if (var10 <= var6) {
                        break;
                     }

                     var18 = var13;
                     var17 = var16;
                     var10006 = var6;
                  }
               }
            }

            String var20 = (new String(var13)).intern();
            switch(var10002) {
            case 0:
               var5[var3++] = var20;
               if ((var0 += var1) >= var4) {
                  a = var5;
                  return;
               }

               var1 = var2.charAt(var0);
               break;
            default:
               var5[var3++] = var20;
               if ((var0 += var1) < var4) {
                  var1 = var2.charAt(var0);
                  continue label55;
               }

               var2 = "ne\u001e+}\u000e<;x\u0006ne\u000b*zZ";
               var4 = "ne\u001e+}\u000e<;x\u0006ne\u000b*zZ".length();
               var1 = '\t';
               var0 = -1;
            }

            var10000 = 51;
            ++var0;
            var10001 = var2.substring(var0, var0 + var1);
            var10002 = 0;
         }
      }
   }
}
