package me.frep.vulcan.spigot.check.impl.movement.jump;

import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(
   name = "Jump",
   type = 'C',
   complexType = "Motion",
   description = "Invalid jump motion."
)
public class JumpC extends AbstractCheck {
   private static int Vulcan_l;
   private static final String[] b;

   public JumpC(Vulcan_iE var1) {
      super(var1);
   }

   public void Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_i(Object[] var1) {
      long var2 = (Long)var1[0];
      Event var4 = (Event)var1[1];
   }

   public static void Vulcan_a(int var0) {
      Vulcan_l = var0;
   }

   public static int Vulcan_Q() {
      return Vulcan_l;
   }

   public static int Vulcan_b() {
      int var0 = Vulcan_Q();

      try {
         return var0 == 0 ? 109 : 0;
      } catch (RuntimeException var1) {
         throw a(var1);
      }
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }

   static {
      String[] var5 = new String[3];
      int var3 = 0;
      String var2 = "\u0019\f\nJ<nu\u0007]\u001d\u000f]6Du\f]\r\u000fX;R:\u0018\u0007\u0005[`";
      int var4 = "\u0019\f\nJ<nu\u0007]\u001d\u000f]6Du\f]\r\u000fX;R:\u0018\u0007\u0005[`".length();
      char var1 = 7;
      Vulcan_a(90);
      int var0 = -1;

      while(true) {
         char[] var10002;
         label41: {
            ++var0;
            char[] var10003 = var2.substring(var0, var0 + var1).toCharArray();
            int var10004 = var10003.length;
            int var6 = 0;
            byte var10 = 100;
            var10002 = var10003;
            int var7 = var10004;
            byte var11;
            char[] var10005;
            int var10006;
            if (var10004 <= 1) {
               var10005 = var10003;
               var11 = 100;
               var10006 = var6;
            } else {
               var10 = 100;
               var7 = var10004;
               if (var10004 <= var6) {
                  break label41;
               }

               var10005 = var10003;
               var11 = 100;
               var10006 = var6;
            }

            while(true) {
               char var21 = var10005[var10006];
               byte var22;
               switch(var6 % 7) {
               case 0:
                  var22 = 25;
                  break;
               case 1:
                  var22 = 13;
                  break;
               case 2:
                  var22 = 2;
                  break;
               case 3:
                  var22 = 90;
                  break;
               case 4:
                  var22 = 57;
                  break;
               case 5:
                  var22 = 83;
                  break;
               default:
                  var22 = 44;
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
