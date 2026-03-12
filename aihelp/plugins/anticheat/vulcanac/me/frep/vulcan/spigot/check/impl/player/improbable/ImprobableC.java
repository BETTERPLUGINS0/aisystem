package me.frep.vulcan.spigot.check.impl.player.improbable;

import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(
   name = "Improbable",
   type = 'C',
   complexType = "Player",
   description = "Too many combined player violations."
)
public class ImprobableC extends AbstractCheck {
   private long Vulcan_W;
   private static final String b;

   public ImprobableC(Vulcan_iE var1) {
      super(var1);
   }

   public void Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_i(Object[] var1) {
      long var3 = (Long)var1[0];
      Event var2 = (Event)var1[1];
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }

   static {
      char[] var10003 = "i\u0006^=\u00024tp\u0005S%\u0013/Mw\u0019\u0002".toCharArray();
      int var10004 = var10003.length;
      int var0 = 0;
      byte var4 = 98;
      char[] var10002 = var10003;
      int var1 = var10004;
      boolean var3;
      byte var5;
      String var6;
      char[] var10005;
      int var10006;
      if (var10004 <= 1) {
         var10005 = var10003;
         var5 = 98;
         var10006 = var0;
      } else {
         var4 = 98;
         var1 = var10004;
         if (var10004 <= var0) {
            var6 = (new String(var10003)).intern();
            var3 = true;
            b = var6;
            return;
         }

         var10005 = var10003;
         var5 = 98;
         var10006 = var0;
      }

      while(true) {
         char var15 = var10005[var10006];
         byte var16;
         switch(var0 % 7) {
         case 0:
            var16 = 123;
            break;
         case 1:
            var16 = 8;
            break;
         case 2:
            var16 = 93;
            break;
         case 3:
            var16 = 38;
            break;
         case 4:
            var16 = 5;
            break;
         case 5:
            var16 = 36;
            break;
         default:
            var16 = 64;
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
