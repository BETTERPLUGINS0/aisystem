package me.frep.vulcan.spigot.check.impl.player.improbable;

import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(
   name = "Improbable",
   type = 'F',
   complexType = "Scaffold",
   description = "Too many combined Scaffold violations."
)
public class ImprobableF extends AbstractCheck {
   private long Vulcan_W;
   private static String Vulcan_n;
   private static final String d;

   public ImprobableF(Vulcan_iE var1) {
      super(var1);
   }

   public void Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_i(Object[] var1) {
      long var3 = (Long)var1[0];
      Event var2 = (Event)var1[1];
   }

   public static void Vulcan_m(String var0) {
      Vulcan_n = var0;
   }

   public static String Vulcan_B() {
      return Vulcan_n;
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }

   static {
      if (Vulcan_B() != null) {
         Vulcan_m("e5tlsc");
      }

      char[] var10003 = "duB@\u000bo\u001cs@JI\u0001a\u0004~yMUP".toCharArray();
      int var10004 = var10003.length;
      int var0 = 0;
      byte var4 = 87;
      char[] var10002 = var10003;
      int var1 = var10004;
      boolean var3;
      byte var5;
      String var6;
      char[] var10005;
      int var10006;
      if (var10004 <= 1) {
         var10005 = var10003;
         var5 = 87;
         var10006 = var0;
      } else {
         var4 = 87;
         var1 = var10004;
         if (var10004 <= var0) {
            var6 = (new String(var10003)).intern();
            var3 = true;
            d = var6;
            return;
         }

         var10005 = var10003;
         var5 = 87;
         var10006 = var0;
      }

      while(true) {
         char var15 = var10005[var10006];
         byte var16;
         switch(var0 % 7) {
         case 0:
            var16 = 64;
            break;
         case 1:
            var16 = 65;
            break;
         case 2:
            var16 = 116;
            break;
         case 3:
            var16 = 113;
            break;
         case 4:
            var16 = 58;
            break;
         case 5:
            var16 = 87;
            break;
         default:
            var16 = 39;
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
               d = var6;
               return;
            }

            var10005 = var10002;
            var5 = var4;
            var10006 = var0;
         }
      }
   }
}
