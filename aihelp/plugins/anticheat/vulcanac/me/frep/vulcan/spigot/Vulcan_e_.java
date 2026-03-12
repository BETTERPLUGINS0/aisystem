package me.frep.vulcan.spigot;

import com.google.common.collect.AbstractIterator;
import java.lang.invoke.MethodHandles;

class Vulcan_e_ extends AbstractIterator {
   private Vulcan_iq Vulcan_r;
   private static final String Vulcan_p;
   final Vulcan_XC Vulcan_v;
   private static final long a = Vulcan_n.a(-6408815451986574850L, -8238708964132415685L, MethodHandles.lookup().lookupClass()).a(33051095805389L);

   Vulcan_e_(Vulcan_XC var1) {
      this.Vulcan_v = var1;
      this.Vulcan_r = null;
   }

   protected Vulcan_iq Vulcan_v(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected Object computeNext() {
      long var1 = a ^ 129759602069610L;
      long var3 = var1 ^ 84328871209515L;
      return this.Vulcan_v(new Object[]{var3});
   }

   static {
      char[] var10003 = "JCy\u0007W4\\;<\u0015\u0007".toCharArray();
      int var10004 = var10003.length;
      int var0 = 0;
      byte var4 = 51;
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
         var5 = 51;
         var10006 = var0;
      } else {
         var4 = 51;
         var1 = var10004;
         if (var10004 <= var0) {
            var6 = (new String(var10003)).intern();
            var3 = true;
            Vulcan_p = var6;
            var10000 = Vulcan_p;
            return;
         }

         var10005 = var10003;
         var5 = 51;
         var10006 = var0;
      }

      while(true) {
         char var15 = var10005[var10006];
         byte var16;
         switch(var0 % 7) {
         case 0:
            var16 = 58;
            break;
         case 1:
            var16 = 60;
            break;
         case 2:
            var16 = 21;
            break;
         case 3:
            var16 = 4;
            break;
         case 4:
            var16 = 84;
            break;
         case 5:
            var16 = 55;
            break;
         default:
            var16 = 95;
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
               Vulcan_p = var6;
               var10000 = Vulcan_p;
               return;
            }

            var10005 = var10002;
            var5 = var4;
            var10006 = var0;
         }
      }
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
