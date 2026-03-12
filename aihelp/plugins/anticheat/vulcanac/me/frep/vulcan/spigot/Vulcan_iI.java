package me.frep.vulcan.spigot;

public final class Vulcan_iI {
   private static boolean Vulcan_n;
   private static final String a;

   public static void Vulcan_L(Object[] var0) {
      Runnable var1 = (Runnable)var0[0];
      Vulcan_Xs.Vulcan_w().Vulcan_H(new Object[]{var1});
   }

   private Vulcan_iI() {
      throw new UnsupportedOperationException(a);
   }

   public static void Vulcan_f(boolean var0) {
      Vulcan_n = var0;
   }

   public static boolean Vulcan_F() {
      return Vulcan_n;
   }

   public static boolean Vulcan_t() {
      boolean var0 = Vulcan_F();

      try {
         return !var0;
      } catch (UnsupportedOperationException var1) {
         throw a(var1);
      }
   }

   private static UnsupportedOperationException a(UnsupportedOperationException var0) {
      return var0;
   }

   static {
      if (!Vulcan_t()) {
         Vulcan_f(true);
      }

      char[] var10003 = "Z\u0007\u0011\u0000e\u0019#.\u000eX\u00061\u0019<g\u001b\u0001S&\u001c1}\u001cX\u0012+\u0014pm\u000e\u0016\u001d*\u0004pl\nX\u001a+\u0003$o\u0001\f\u001a$\u00045j".toCharArray();
      int var10004 = var10003.length;
      int var0 = 0;
      byte var4 = 79;
      char[] var10002 = var10003;
      int var1 = var10004;
      boolean var3;
      byte var5;
      String var6;
      char[] var10005;
      int var10006;
      if (var10004 <= 1) {
         var10005 = var10003;
         var5 = 79;
         var10006 = var0;
      } else {
         var4 = 79;
         var1 = var10004;
         if (var10004 <= var0) {
            var6 = (new String(var10003)).intern();
            var3 = true;
            a = var6;
            return;
         }

         var10005 = var10003;
         var5 = 79;
         var10006 = var0;
      }

      while(true) {
         char var15 = var10005[var10006];
         byte var16;
         switch(var0 % 7) {
         case 0:
            var16 = 65;
            break;
         case 1:
            var16 = 32;
            break;
         case 2:
            var16 = 55;
            break;
         case 3:
            var16 = 60;
            break;
         case 4:
            var16 = 10;
            break;
         case 5:
            var16 = 63;
            break;
         default:
            var16 = 31;
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
               a = var6;
               return;
            }

            var10005 = var10002;
            var5 = var4;
            var10006 = var0;
         }
      }
   }
}
