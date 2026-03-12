package me.frep.vulcan.spigot.bukkit;

import com.google.gson.JsonObject;
import java.util.concurrent.Callable;

public class Vulcan_i3 extends Vulcan_i7 {
   private final Callable Vulcan_P;
   private static final String c;

   public Vulcan_i3(String var1, Callable var2) {
      super(var1);
      this.Vulcan_P = var2;
   }

   protected JsonObject Vulcan_X(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private static Exception a(Exception var0) {
      return var0;
   }

   static {
      char[] var10003 = "!\u0007ffZ/".toCharArray();
      int var10004 = var10003.length;
      int var0 = 0;
      byte var4 = 66;
      char[] var10002 = var10003;
      int var1 = var10004;
      boolean var3;
      byte var5;
      String var6;
      char[] var10005;
      int var10006;
      if (var10004 <= 1) {
         var10005 = var10003;
         var5 = 66;
         var10006 = var0;
      } else {
         var4 = 66;
         var1 = var10004;
         if (var10004 <= var0) {
            var6 = (new String(var10003)).intern();
            var3 = true;
            c = var6;
            return;
         }

         var10005 = var10003;
         var5 = 66;
         var10006 = var0;
      }

      while(true) {
         char var15 = var10005[var10006];
         byte var16;
         switch(var0 % 7) {
         case 0:
            var16 = 21;
            break;
         case 1:
            var16 = 36;
            break;
         case 2:
            var16 = 72;
            break;
         case 3:
            var16 = 81;
            break;
         case 4:
            var16 = 125;
            break;
         case 5:
            var16 = 30;
            break;
         default:
            var16 = 27;
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
               c = var6;
               return;
            }

            var10005 = var10002;
            var5 = var4;
            var10006 = var0;
         }
      }
   }
}
