package me.frep.vulcan.spigot.check.impl.movement.nosaddle;

import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(
   name = "No Saddle",
   type = 'A',
   complexType = "Spoof",
   description = "Controlling an entity without a saddle."
)
public class NoSaddleA extends AbstractCheck {
   private static String Vulcan_s;
   private static final String[] b;

   public NoSaddleA(Vulcan_iE var1) {
      super(var1);
   }

   public void Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_i(Object[] var1) {
      long var3 = (Long)var1[0];
      Event var2 = (Event)var1[1];
   }

   private void lambda$handle$0() {
      this.Vulcan_Q.Vulcan_q(new Object[0]).leaveVehicle();
   }

   public static void Vulcan_f(String var0) {
      Vulcan_s = var0;
   }

   public static String Vulcan_M() {
      return Vulcan_s;
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }

   static {
      String[] var5 = new String[2];
      int var3 = 0;
      String var2 = "\n|%9P\u0013Lp%\u0005Nq&3\u0019";
      int var4 = "\n|%9P\u0013Lp%\u0005Nq&3\u0019".length();
      Vulcan_f((String)null);
      char var1 = '\t';
      int var0 = -1;

      while(true) {
         char[] var10002;
         label41: {
            ++var0;
            char[] var10003 = var2.substring(var0, var0 + var1).toCharArray();
            int var10004 = var10003.length;
            int var6 = 0;
            byte var10 = 74;
            var10002 = var10003;
            int var7 = var10004;
            byte var11;
            char[] var10005;
            int var10006;
            if (var10004 <= 1) {
               var10005 = var10003;
               var11 = 74;
               var10006 = var6;
            } else {
               var10 = 74;
               var7 = var10004;
               if (var10004 <= var6) {
                  break label41;
               }

               var10005 = var10003;
               var11 = 74;
               var10006 = var6;
            }

            while(true) {
               char var21 = var10005[var10006];
               byte var22;
               switch(var6 % 7) {
               case 0:
                  var22 = 96;
                  break;
               case 1:
                  var22 = 82;
                  break;
               case 2:
                  var22 = 10;
                  break;
               case 3:
                  var22 = 31;
                  break;
               case 4:
                  var22 = 110;
                  break;
               case 5:
                  var22 = 56;
                  break;
               default:
                  var22 = 94;
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
