package me.frep.vulcan.spigot.check.impl.movement.speed;

import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(
   name = "Speed",
   type = 'E',
   complexType = "Prediction",
   experimental = true,
   description = "Invalid friction."
)
public class SpeedE extends AbstractCheck {
   private static int[] Vulcan_H;
   private static final String[] b;

   public SpeedE(Vulcan_iE var1) {
      super(var1);
   }

   public void Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_i(Object[] var1) {
      long var3 = (Long)var1[0];
      Event var2 = (Event)var1[1];
   }

   public static void Vulcan_R(int[] var0) {
      Vulcan_H = var0;
   }

   public static int[] Vulcan_I() {
      return Vulcan_H;
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }

   static {
      String[] var5 = new String[6];
      int var3 = 0;
      int[] var10000 = new int[2];
      String var2 = "@6M\u0003@'M\u0006@$\u0019\u0017@J\u0005@-\u0019\u001f\u001b";
      int var4 = "@6M\u0003@'M\u0006@$\u0019\u0017@J\u0005@-\u0019\u001f\u001b".length();
      char var1 = 3;
      int var0 = -1;
      Vulcan_R(var10000);

      label55:
      while(true) {
         byte var7 = 78;
         ++var0;
         String var10001 = var2.substring(var0, var0 + var1);
         byte var10002 = -1;

         while(true) {
            char[] var14;
            label50: {
               char[] var15 = var10001.toCharArray();
               int var10004 = var15.length;
               int var6 = 0;
               byte var18 = var7;
               byte var17 = var7;
               var14 = var15;
               int var11 = var10004;
               char[] var19;
               int var10006;
               if (var10004 <= 1) {
                  var19 = var15;
                  var10006 = var6;
               } else {
                  var17 = var7;
                  var11 = var10004;
                  if (var10004 <= var6) {
                     break label50;
                  }

                  var19 = var15;
                  var10006 = var6;
               }

               while(true) {
                  char var28 = var19[var10006];
                  byte var29;
                  switch(var6 % 7) {
                  case 0:
                     var29 = 46;
                     break;
                  case 1:
                     var29 = 14;
                     break;
                  case 2:
                     var29 = 62;
                     break;
                  case 3:
                     var29 = 63;
                     break;
                  case 4:
                     var29 = 104;
                     break;
                  case 5:
                     var29 = 57;
                     break;
                  default:
                     var29 = 72;
                  }

                  var19[var10006] = (char)(var28 ^ var18 ^ var29);
                  ++var6;
                  if (var17 == 0) {
                     var10006 = var17;
                     var19 = var14;
                     var18 = var17;
                  } else {
                     if (var11 <= var6) {
                        break;
                     }

                     var19 = var14;
                     var18 = var17;
                     var10006 = var6;
                  }
               }
            }

            String var21 = (new String(var14)).intern();
            switch(var10002) {
            case 0:
               var5[var3++] = var21;
               if ((var0 += var1) >= var4) {
                  b = var5;
                  return;
               }

               var1 = var2.charAt(var0);
               break;
            default:
               var5[var3++] = var21;
               if ((var0 += var1) < var4) {
                  var1 = var2.charAt(var0);
                  continue label55;
               }

               var2 = "\u0006\u001a(N\u0004B6\u0002N";
               var4 = "\u0006\u001a(N\u0004B6\u0002N".length();
               var1 = 4;
               var0 = -1;
            }

            var7 = 76;
            ++var0;
            var10001 = var2.substring(var0, var0 + var1);
            var10002 = 0;
         }
      }
   }
}
