package me.frep.vulcan.spigot.check.impl.combat.autoclicker;

import me.frep.vulcan.spigot.Vulcan_c;
import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(
   name = "Auto Clicker",
   type = 'N',
   complexType = "Deviation Difference",
   description = "Low deviation difference."
)
public class AutoClickerN extends AbstractCheck {
   private final Vulcan_c Vulcan_B = new Vulcan_c(25);
   private double Vulcan_l;
   private static final String[] b;

   public AutoClickerN(Vulcan_iE var1) {
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
      String[] var5 = new String[2];
      int var3 = 0;
      String var2 = "{a.qp{\u001a>=\u000b?i>rgh\u00185c=)";
      int var4 = "{a.qp{\u001a>=\u000b?i>rgh\u00185c=)".length();
      char var1 = '\t';
      int var0 = -1;

      while(true) {
         char[] var10002;
         label41: {
            ++var0;
            char[] var10003 = var2.substring(var0, var0 + var1).toCharArray();
            int var10004 = var10003.length;
            int var6 = 0;
            byte var10 = 78;
            var10002 = var10003;
            int var7 = var10004;
            byte var11;
            char[] var10005;
            int var10006;
            if (var10004 <= 1) {
               var10005 = var10003;
               var11 = 78;
               var10006 = var6;
            } else {
               var10 = 78;
               var7 = var10004;
               if (var10004 <= var6) {
                  break label41;
               }

               var10005 = var10003;
               var11 = 78;
               var10006 = var6;
            }

            while(true) {
               char var21 = var10005[var10006];
               byte var22;
               switch(var6 % 7) {
               case 0:
                  var22 = 21;
                  break;
               case 1:
                  var22 = 78;
                  break;
               case 2:
                  var22 = 22;
                  break;
               case 3:
                  var22 = 90;
                  break;
               case 4:
                  var22 = 76;
                  break;
               case 5:
                  var22 = 84;
                  break;
               default:
                  var22 = 51;
               }

               var10005[var10006] = (char)(var21 ^ var11 ^ var22);
               ++var6;
               if (var10 == 0) {
                  var10006 = var10;
                  var10005 = var10002;
                  var11 = var10;
               } else {
                  if (var7 <= var6) {
                     break;
                  }

                  var10005 = var10002;
                  var11 = var10;
                  var10006 = var6;
               }
            }
         }

         String var12 = (new String(var10002)).intern();
         boolean var9 = true;
         var5[var3++] = var12;
         if ((var0 += var1) >= var4) {
            b = var5;
            return;
         }

         var1 = var2.charAt(var0);
      }
   }
}
