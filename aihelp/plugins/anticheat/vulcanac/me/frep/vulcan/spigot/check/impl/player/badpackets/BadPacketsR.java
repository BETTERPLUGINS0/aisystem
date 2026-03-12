package me.frep.vulcan.spigot.check.impl.player.badpackets;

import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(
   name = "Bad Packets",
   type = 'R',
   complexType = "Post HeldItemSlot",
   experimental = false,
   description = "Post HeldItemSlot packets."
)
public class BadPacketsR extends AbstractCheck {
   private long Vulcan_n = 0L;
   private boolean Vulcan_R = false;
   private static final String b;

   public BadPacketsR(Vulcan_iE var1) {
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
      char[] var10003 = "F1c!D\u0011".toCharArray();
      int var10004 = var10003.length;
      int var0 = 0;
      byte var4 = 48;
      char[] var10002 = var10003;
      int var1 = var10004;
      boolean var3;
      byte var5;
      String var6;
      char[] var10005;
      int var10006;
      if (var10004 <= 1) {
         var10005 = var10003;
         var5 = 48;
         var10006 = var0;
      } else {
         var4 = 48;
         var1 = var10004;
         if (var10004 <= var0) {
            var6 = (new String(var10003)).intern();
            var3 = true;
            b = var6;
            return;
         }

         var10005 = var10003;
         var5 = 48;
         var10006 = var0;
      }

      while(true) {
         char var15 = var10005[var10006];
         byte var16;
         switch(var0 % 7) {
         case 0:
            var16 = 18;
            break;
         case 1:
            var16 = 100;
            break;
         case 2:
            var16 = 63;
            break;
         case 3:
            var16 = 112;
            break;
         case 4:
            var16 = 13;
            break;
         case 5:
            var16 = 28;
            break;
         default:
            var16 = 72;
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
