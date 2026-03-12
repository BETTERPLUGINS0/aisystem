package me.frep.vulcan.spigot.check.impl.combat.hitbox;

import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@CheckInfo(
   name = "Hitbox",
   type = 'B',
   complexType = "Simple",
   description = "Attacked while not looking at target."
)
public class HitboxB extends AbstractCheck {
   private boolean Vulcan_o;
   private Player Vulcan_r;
   private static AbstractCheck[] Vulcan_m;
   private static final String[] b;

   public HitboxB(Vulcan_iE var1) {
      super(var1);
   }

   public void Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_i(Object[] var1) {
      long var3 = (Long)var1[0];
      Event var2 = (Event)var1[1];
   }

   public static void Vulcan_O(AbstractCheck[] var0) {
      Vulcan_m = var0;
   }

   public static AbstractCheck[] Vulcan_r() {
      return Vulcan_m;
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }

   static {
      String[] var5 = new String[2];
      AbstractCheck[] var10000 = new AbstractCheck[2];
      int var3 = 0;
      String var2 = "D3:|R\u0012\n\u000594cCNgF8`";
      int var4 = "D3:|R\u0012\n\u000594cCNgF8`".length();
      Vulcan_O(var10000);
      char var1 = 6;
      int var0 = -1;

      while(true) {
         char[] var10002;
         label41: {
            ++var0;
            char[] var10003 = var2.substring(var0, var0 + var1).toCharArray();
            int var10004 = var10003.length;
            int var6 = 0;
            byte var10 = 43;
            var10002 = var10003;
            int var7 = var10004;
            byte var11;
            char[] var10005;
            int var10006;
            if (var10004 <= 1) {
               var10005 = var10003;
               var11 = 43;
               var10006 = var6;
            } else {
               var10 = 43;
               var7 = var10004;
               if (var10004 <= var6) {
                  break label41;
               }

               var10005 = var10003;
               var11 = 43;
               var10006 = var6;
            }

            while(true) {
               char var21 = var10005[var10006];
               byte var22;
               switch(var6 % 7) {
               case 0:
                  var22 = 14;
                  break;
               case 1:
                  var22 = 118;
                  break;
               case 2:
                  var22 = 118;
                  break;
               case 3:
                  var22 = 59;
                  break;
               case 4:
                  var22 = 28;
                  break;
               case 5:
                  var22 = 4;
                  break;
               default:
                  var22 = 34;
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
