package me.frep.vulcan.spigot.check.impl.combat.autoclicker;

import me.frep.vulcan.spigot.Vulcan_c;
import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(
   name = "Auto Clicker",
   type = 'T',
   complexType = "Kurtosis",
   description = "Too low kurtosis"
)
public class AutoClickerT extends AbstractCheck {
   private final Vulcan_c Vulcan_W = new Vulcan_c(25);
   private static AbstractCheck[] Vulcan_R;
   private static final String b;

   public AutoClickerT(Vulcan_iE var1) {
      super(var1);
   }

   public void Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_i(Object[] var1) {
      long var2 = (Long)var1[0];
      Event var4 = (Event)var1[1];
   }

   public static void Vulcan_V(AbstractCheck[] var0) {
      Vulcan_R = var0;
   }

   public static AbstractCheck[] Vulcan_P() {
      return Vulcan_R;
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }

   static {
      if (Vulcan_P() == null) {
         Vulcan_V(new AbstractCheck[3]);
      }

      char[] var10003 = ">r\u00154h\u0016K&:".toCharArray();
      int var10004 = var10003.length;
      int var0 = 0;
      byte var4 = 96;
      char[] var10002 = var10003;
      int var1 = var10004;
      boolean var3;
      byte var5;
      String var6;
      char[] var10005;
      int var10006;
      if (var10004 <= 1) {
         var10005 = var10003;
         var5 = 96;
         var10006 = var0;
      } else {
         var4 = 96;
         var1 = var10004;
         if (var10004 <= var0) {
            var6 = (new String(var10003)).intern();
            var3 = true;
            b = var6;
            return;
         }

         var10005 = var10003;
         var5 = 96;
         var10006 = var0;
      }

      while(true) {
         char var15 = var10005[var10006];
         byte var16;
         switch(var0 % 7) {
         case 0:
            var16 = 53;
            break;
         case 1:
            var16 = 103;
            break;
         case 2:
            var16 = 7;
            break;
         case 3:
            var16 = 32;
            break;
         case 4:
            var16 = 103;
            break;
         case 5:
            var16 = 5;
            break;
         default:
            var16 = 66;
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
