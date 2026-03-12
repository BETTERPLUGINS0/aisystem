package me.frep.vulcan.spigot.check.impl.combat.aim;

import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(
   name = "Aim",
   type = 'L',
   complexType = "Direction",
   description = "Switching directions too quickly."
)
public class AimL extends AbstractCheck {
   private float Vulcan_y;
   private int Vulcan_G;
   private static final String b;

   public AimL(Vulcan_iE var1) {
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
      char[] var10003 = "BTPSR(\u0006RRT\u001a".toCharArray();
      int var10004 = var10003.length;
      int var0 = 0;
      byte var4 = 20;
      char[] var10002 = var10003;
      int var1 = var10004;
      boolean var3;
      byte var5;
      String var6;
      char[] var10005;
      int var10006;
      if (var10004 <= 1) {
         var10005 = var10003;
         var5 = 20;
         var10006 = var0;
      } else {
         var4 = 20;
         var1 = var10004;
         if (var10004 <= var0) {
            var6 = (new String(var10003)).intern();
            var3 = true;
            b = var6;
            return;
         }

         var10005 = var10003;
         var5 = 20;
         var10006 = var0;
      }

      while(true) {
         char var15 = var10005[var10006];
         byte var16;
         switch(var0 % 7) {
         case 0:
            var16 = 50;
            break;
         case 1:
            var16 = 37;
            break;
         case 2:
            var16 = 40;
            break;
         case 3:
            var16 = 51;
            break;
         case 4:
            var16 = 39;
            break;
         case 5:
            var16 = 108;
            break;
         default:
            var16 = 123;
         }

         var10005[var10006] = (char)(var15 ^ var5 ^ var16);
         ++var0;
         if (var4 == 0) {
            var10006 = var4;
            var10005 = var10002;
            var5 = var4;
         } else {
            if (var1 <= var0) {
               var6 = (new String(var10002)).intern();
               var3 = true;
               b = var6;
               return;
            }

            var10005 = var10002;
            var5 = var4;
            var10006 = var0;
         }
      }
   }
}
