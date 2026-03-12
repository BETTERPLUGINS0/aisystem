package me.frep.vulcan.spigot.check.impl.combat.autoclicker;

import com.google.common.collect.Lists;
import java.util.Deque;
import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(
   name = "Auto Clicker",
   type = 'K',
   complexType = "Average Difference",
   description = "Similar average values."
)
public class AutoClickerK extends AbstractCheck {
   private final Deque Vulcan_M = Lists.newLinkedList();
   private double Vulcan_J;
   private static final String b;

   public AutoClickerK(Vulcan_iE var1) {
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
      char[] var10003 = "{c[Y(o5qiX\u0002".toCharArray();
      int var10004 = var10003.length;
      int var0 = 0;
      byte var4 = 30;
      char[] var10002 = var10003;
      int var1 = var10004;
      boolean var3;
      byte var5;
      String var6;
      char[] var10005;
      int var10006;
      if (var10004 <= 1) {
         var10005 = var10003;
         var5 = 30;
         var10006 = var0;
      } else {
         var4 = 30;
         var1 = var10004;
         if (var10004 <= var0) {
            var6 = (new String(var10003)).intern();
            var3 = true;
            b = var6;
            return;
         }

         var10005 = var10003;
         var5 = 30;
         var10006 = var0;
      }

      while(true) {
         char var15 = var10005[var10006];
         byte var16;
         switch(var0 % 7) {
         case 0:
            var16 = 1;
            break;
         case 1:
            var16 = 20;
            break;
         case 2:
            var16 = 35;
            break;
         case 3:
            var16 = 33;
            break;
         case 4:
            var16 = 83;
            break;
         case 5:
            var16 = 3;
            break;
         default:
            var16 = 78;
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
