package me.frep.vulcan.spigot;

import org.bukkit.entity.Entity;

public class Vulcan_ih {
   private Vulcan_iD Vulcan_I;
   public Vulcan_XV Vulcan_U;
   public Vulcan_Xy Vulcan_k;
   public Vulcan_XH Vulcan_t;
   public Entity Vulcan_Z;
   private static final String[] a;

   public Vulcan_ih(Vulcan_XH var1, Vulcan_Xy var2, Vulcan_iD var3) {
      this(Vulcan_XV.BLOCK, var1, var2, var3);
   }

   public Vulcan_ih(Vulcan_XH var1, Vulcan_Xy var2) {
      this(Vulcan_XV.BLOCK, var1, var2, Vulcan_iD.Vulcan_t);
   }

   public Vulcan_ih(Entity var1) {
      this(var1, new Vulcan_XH(var1.getLocation().getX(), var1.getLocation().getY(), var1.getLocation().getZ()));
   }

   public Vulcan_ih(Vulcan_XV var1, Vulcan_XH var2, Vulcan_Xy var3, Vulcan_iD var4) {
      this.Vulcan_U = var1;
      this.Vulcan_I = var4;
      this.Vulcan_k = var3;
      this.Vulcan_t = new Vulcan_XH(var2.Vulcan_F, var2.Vulcan_g, var2.Vulcan_k);
   }

   public Vulcan_ih(Entity var1, Vulcan_XH var2) {
      this.Vulcan_U = Vulcan_XV.ENTITY;
      this.Vulcan_Z = var1;
      this.Vulcan_t = var2;
   }

   public Vulcan_iD Vulcan_o(Object[] var1) {
      return this.Vulcan_I;
   }

   public String toString() {
      StringBuilder var10000 = new StringBuilder();
      String[] var1 = a;
      return var10000.append(var1[3]).append(this.Vulcan_U).append(var1[1]).append(this.Vulcan_I).append(var1[2]).append(this.Vulcan_k).append(var1[0]).append(this.Vulcan_t).append(var1[4]).append(this.Vulcan_Z).append('}').toString();
   }

   static {
      String[] var5 = new String[5];
      int var3 = 0;
      String var2 = "*P]\b[?\u000b*PO\u000bGaTv\u001f^Z\u0004*PKZ";
      int var4 = "*P]\b[?\u000b*PO\u000bGaTv\u001f^Z\u0004*PKZ".length();
      char var1 = 6;
      int var0 = -1;

      label55:
      while(true) {
         byte var10000 = 11;
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
                     var28 = 13;
                     break;
                  case 1:
                     var28 = 123;
                     break;
                  case 2:
                     var28 = 38;
                     break;
                  case 3:
                     var28 = 108;
                     break;
                  case 4:
                     var28 = 35;
                     break;
                  case 5:
                     var28 = 9;
                     break;
                  default:
                     var28 = 52;
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

               var2 = "~)i\u0005}AzZ4f#aBj\u000b\t\u001a`x9l[{O}";
               var4 = "~)i\u0005}AzZ4f#aBj\u000b\t\u001a`x9l[{O}".length();
               var1 = 15;
               var0 = -1;
            }

            var10000 = 59;
            ++var0;
            var10001 = var2.substring(var0, var0 + var1);
            var10002 = 0;
         }
      }
   }
}
