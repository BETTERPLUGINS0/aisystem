package me.frep.vulcan.spigot.check.impl.movement.elytra;

import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(
   name = "Elytra",
   type = 'B',
   complexType = "Acceleration",
   description = "Invalid Y-Axis change."
)
public class ElytraB extends AbstractCheck {
   private static final String[] b;

   public ElytraB(Vulcan_iE var1) {
      super(var1);
   }

   public void Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_i(Object[] var1) {
      long var2 = (Long)var1[0];
      Event var4 = (Event)var1[1];
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }

   static {
      String[] var5 = new String[5];
      int var3 = 0;
      String var2 = "$\u007fkj\u001fF\bpwkb\t\u0017dm\tprmm\u0018\u001ae\n+";
      int var4 = "$\u007fkj\u001fF\bpwkb\t\u0017dm\tprmm\u0018\u001ae\n+".length();
      char var1 = 6;
      int var0 = -1;

      label55:
      while(true) {
         byte var10000 = 121;
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
                     var28 = 41;
                     break;
                  case 1:
                     var28 = 111;
                     break;
                  case 2:
                     var28 = 113;
                     break;
                  case 3:
                     var28 = 120;
                     break;
                  case 4:
                     var28 = 21;
                     break;
                  case 5:
                     var28 = 2;
                     break;
                  default:
                     var28 = 68;
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
                  b = var5;
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

               var2 = "/(4=VH:Ut\b/-22GE;2";
               var4 = "/(4=VH:Ut\b/-22GE;2".length();
               var1 = '\t';
               var0 = -1;
            }

            var10000 = 38;
            ++var0;
            var10001 = var2.substring(var0, var0 + var1);
            var10002 = 0;
         }
      }
   }
}
