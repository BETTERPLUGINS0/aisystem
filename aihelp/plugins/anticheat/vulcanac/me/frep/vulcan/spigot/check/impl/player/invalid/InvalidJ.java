package me.frep.vulcan.spigot.check.impl.player.invalid;

import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(
   name = "Invalid",
   type = 'J',
   complexType = "Motion",
   description = "Moving too quickly.",
   experimental = true
)
public class InvalidJ extends AbstractCheck {
   private static AbstractCheck[] Vulcan_k;
   private static final String[] b;

   public InvalidJ(Vulcan_iE var1) {
      super(var1);
   }

   public void Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_i(Object[] var1) {
      long var2 = (Long)var1[0];
      Event var4 = (Event)var1[1];
   }

   public static void Vulcan_a(AbstractCheck[] var0) {
      Vulcan_k = var0;
   }

   public static AbstractCheck[] Vulcan_M() {
      return Vulcan_k;
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }

   static {
      String[] var5 = new String[5];
      int var3 = 0;
      AbstractCheck[] var10000 = new AbstractCheck[3];
      String var2 = "+CV\\2\r\u0007+SVY?C9\b+CZV Q]6";
      int var4 = "+CV\\2\r\u0007+SVY?C9\b+CZV Q]6".length();
      char var1 = 6;
      Vulcan_a(var10000);
      int var0 = -1;

      label55:
      while(true) {
         byte var7 = 35;
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
                     var29 = 40;
                     break;
                  case 1:
                     var29 = 4;
                     break;
                  case 2:
                     var29 = 28;
                     break;
                  case 3:
                     var29 = 25;
                     break;
                  case 4:
                     var29 = 119;
                     break;
                  case 5:
                     var29 = 19;
                     break;
                  default:
                     var29 = 39;
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

               var2 = "\u0001`thC\u0006R}pu\u001a'";
               var4 = "\u0001`thC\u0006R}pu\u001a'".length();
               var1 = 5;
               var0 = -1;
            }

            var7 = 9;
            ++var0;
            var10001 = var2.substring(var0, var0 + var1);
            var10002 = 0;
         }
      }
   }
}
