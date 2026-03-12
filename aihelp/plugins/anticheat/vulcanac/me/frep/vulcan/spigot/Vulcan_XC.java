package me.frep.vulcan.spigot;

import java.util.Iterator;

final class Vulcan_XC implements Iterable {
   private static final String Vulcan_Y;
   final Vulcan_iD Vulcan_s;
   final Vulcan_iD Vulcan_O;

   Vulcan_XC(Vulcan_iD var1, Vulcan_iD var2) {
      this.Vulcan_s = var1;
      this.Vulcan_O = var2;
   }

   public Iterator iterator() {
      return new Vulcan_e_(this);
   }

   static {
      char[] var10003 = "hsrb26s\u0019\f\u001ec".toCharArray();
      int var10004 = var10003.length;
      int var0 = 0;
      byte var4 = 46;
      char[] var10002 = var10003;
      int var1 = var10004;
      boolean var3;
      byte var5;
      String var6;
      String var10000;
      char[] var10005;
      int var10006;
      if (var10004 <= 1) {
         var10005 = var10003;
         var5 = 46;
         var10006 = var0;
      } else {
         var4 = 46;
         var1 = var10004;
         if (var10004 <= var0) {
            var6 = (new String(var10003)).intern();
            var3 = true;
            Vulcan_Y = var6;
            var10000 = Vulcan_Y;
            return;
         }

         var10005 = var10003;
         var5 = 46;
         var10006 = var0;
      }

      while(true) {
         char var15 = var10005[var10006];
         byte var16;
         switch(var0 % 7) {
         case 0:
            var16 = 5;
            break;
         case 1:
            var16 = 17;
            break;
         case 2:
            var16 = 3;
            break;
         case 3:
            var16 = 124;
            break;
         case 4:
            var16 = 44;
            break;
         case 5:
            var16 = 40;
            break;
         default:
            var16 = 109;
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
               Vulcan_Y = var6;
               var10000 = Vulcan_Y;
               return;
            }

            var10005 = var10002;
            var5 = var4;
            var10006 = var0;
         }
      }
   }
}
