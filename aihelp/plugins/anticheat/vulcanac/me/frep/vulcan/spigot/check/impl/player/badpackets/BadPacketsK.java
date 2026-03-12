package me.frep.vulcan.spigot.check.impl.player.badpackets;

import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(
   name = "Bad Packets",
   type = 'K',
   complexType = "Sneak",
   description = "Sending EntityAction packets too quickly"
)
public class BadPacketsK extends AbstractCheck {
   private long Vulcan_z = -1L;
   private static final String b;

   public BadPacketsK(Vulcan_iE var1) {
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
      char[] var10003 = "0{`\u0001jj".toCharArray();
      int var10004 = var10003.length;
      int var0 = 0;
      byte var4 = 97;
      char[] var10002 = var10003;
      int var1 = var10004;
      boolean var3;
      byte var5;
      String var6;
      char[] var10005;
      int var10006;
      if (var10004 <= 1) {
         var10005 = var10003;
         var5 = 97;
         var10006 = var0;
      } else {
         var4 = 97;
         var1 = var10004;
         if (var10004 <= var0) {
            var6 = (new String(var10003)).intern();
            var3 = true;
            b = var6;
            return;
         }

         var10005 = var10003;
         var5 = 97;
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
            var16 = 127;
            break;
         case 2:
            var16 = 109;
            break;
         case 3:
            var16 = 1;
            break;
         case 4:
            var16 = 114;
            break;
         case 5:
            var16 = 54;
            break;
         default:
            var16 = 53;
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
