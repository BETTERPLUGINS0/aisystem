package me.frep.vulcan.spigot;

import com.google.common.collect.AbstractIterator;
import java.lang.invoke.MethodHandles;

class Vulcan_ic extends AbstractIterator {
   private Vulcan_iD Vulcan_N;
   private static final String Vulcan_O;
   final Vulcan_ix Vulcan_X;
   private static final long a = Vulcan_n.a(5438229288000226244L, -5881388194664327440L, MethodHandles.lookup().lookupClass()).a(57992569908364L);

   Vulcan_ic(Vulcan_ix var1) {
      this.Vulcan_X = var1;
      this.Vulcan_N = null;
   }

   protected Vulcan_iD Vulcan_D(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected Object computeNext() {
      long var1 = a ^ 41500517191340L;
      long var3 = var1 ^ 130803138579505L;
      return this.Vulcan_D(new Object[]{var3});
   }

   static {
      char[] var10003 = "9\u001a3>\u0001dUHe_<".toCharArray();
      int var10004 = var10003.length;
      int var0 = 0;
      byte var4 = 81;
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
         var5 = 81;
         var10006 = var0;
      } else {
         var4 = 81;
         var1 = var10004;
         if (var10004 <= var0) {
            var6 = (new String(var10003)).intern();
            var3 = true;
            Vulcan_O = var6;
            var10000 = Vulcan_O;
            return;
         }

         var10005 = var10003;
         var5 = 81;
         var10006 = var0;
      }

      while(true) {
         char var15 = var10005[var10006];
         byte var16;
         switch(var0 % 7) {
         case 0:
            var16 = 43;
            break;
         case 1:
            var16 = 7;
            break;
         case 2:
            var16 = 61;
            break;
         case 3:
            var16 = 95;
            break;
         case 4:
            var16 = 96;
            break;
         case 5:
            var16 = 5;
            break;
         default:
            var16 = 52;
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
               Vulcan_O = var6;
               var10000 = Vulcan_O;
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
