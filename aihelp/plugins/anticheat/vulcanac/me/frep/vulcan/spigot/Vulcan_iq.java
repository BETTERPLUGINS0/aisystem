package me.frep.vulcan.spigot;

public final class Vulcan_iq extends Vulcan_iD {
   public int Vulcan_D;
   public int Vulcan_G;
   public int Vulcan_x;
   private static final String Vulcan_o;

   private Vulcan_iq(int var1, int var2, int var3) {
      super(0, 0, 0);
      this.Vulcan_D = var1;
      this.Vulcan_G = var2;
      this.Vulcan_x = var3;
   }

   public int Vulcan_Q(Object[] var1) {
      return this.Vulcan_D;
   }

   public int Vulcan_x(Object[] var1) {
      return this.Vulcan_G;
   }

   public int Vulcan_w(Object[] var1) {
      return this.Vulcan_x;
   }

   public Vulcan_il Vulcan_K(Object[] var1) {
      Vulcan_il var2 = (Vulcan_il)var1[0];
      return super.Vulcan_T(new Object[]{var2});
   }

   Vulcan_iq(int var1, int var2, int var3, Object var4) {
      this(var1, var2, var3);
   }

   static {
      char[] var10003 = "Wm[\u000f[\u0000\u0001&\u00126\u0006".toCharArray();
      int var10004 = var10003.length;
      int var0 = 0;
      byte var4 = 68;
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
         var5 = 68;
         var10006 = var0;
      } else {
         var4 = 68;
         var1 = var10004;
         if (var10004 <= var0) {
            var6 = (new String(var10003)).intern();
            var3 = true;
            Vulcan_o = var6;
            var10000 = Vulcan_o;
            return;
         }

         var10005 = var10003;
         var5 = 68;
         var10006 = var0;
      }

      while(true) {
         char var15 = var10005[var10006];
         byte var16;
         switch(var0 % 7) {
         case 0:
            var16 = 80;
            break;
         case 1:
            var16 = 101;
            break;
         case 2:
            var16 = 64;
            break;
         case 3:
            var16 = 123;
            break;
         case 4:
            var16 = 47;
            break;
         case 5:
            var16 = 116;
            break;
         default:
            var16 = 117;
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
               Vulcan_o = var6;
               var10000 = Vulcan_o;
               return;
            }

            var10005 = var10002;
            var5 = var4;
            var10006 = var0;
         }
      }
   }
}
