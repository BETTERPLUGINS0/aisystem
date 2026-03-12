package ac.vulcan.anticheat;

import java.util.Calendar;

class Vulcan_Xz implements Vulcan_iM {
   private final int Vulcan_p;
   private final int Vulcan_R;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-1493319020742097295L, 6364207384428705647L, (Object)null).a(135665857185878L);
   private static final String b;

   Vulcan_Xz(int var1, int var2) {
      long var3 = a ^ 104189083106320L;
      super();
      if (var2 < 3) {
         throw new IllegalArgumentException();
      } else {
         this.Vulcan_p = var1;
         this.Vulcan_R = var2;
      }
   }

   public int Vulcan_p(Object[] var1) {
      long var2 = (Long)var1[0];
      return 4;
   }

   public void Vulcan_e(Object[] var1) {
      StringBuffer var4 = (StringBuffer)var1[0];
      Calendar var5 = (Calendar)var1[1];
      long var2 = (Long)var1[2];
      long var6 = var2 ^ 73487869171594L;
      this.Vulcan_Z(new Object[]{new Long(var6), var4, new Integer(var5.get(this.Vulcan_p))});
   }

   public final void Vulcan_Z(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   static {
      char[] var10002 = "z%]2+HYQ`L23TJG`I;0TCP`T<+\u0001MQ`J<,RFV,_".toCharArray();
      int var10003 = var10002.length;
      int var1 = 0;
      byte var13 = 45;
      char[] var10001 = var10002;
      int var11 = var10003;
      char[] var10004;
      int var10005;
      byte var14;
      if (var10003 <= 1) {
         var10004 = var10002;
         var14 = 45;
         var10005 = var1;
      } else {
         var13 = 45;
         var11 = var10003;
         if (var10003 <= var1) {
            b = (new String(var10002)).intern();
            return;
         }

         var10004 = var10002;
         var14 = 45;
         var10005 = var1;
      }

      while(true) {
         char var9 = var10004[var10005];
         byte var10;
         switch(var1 % 7) {
         case 0:
            var10 = 25;
            break;
         case 1:
            var10 = 109;
            break;
         case 2:
            var10 = 23;
            break;
         case 3:
            var10 = 126;
            break;
         case 4:
            var10 = 114;
            break;
         case 5:
            var10 = 12;
            break;
         default:
            var10 = 2;
         }

         var10004[var10005] = (char)(var9 ^ var14 ^ var10);
         ++var1;
         if (var13 == 0) {
            var10005 = var13;
            var10004 = var10001;
            var14 = var13;
         } else {
            if (var11 <= var1) {
               b = (new String(var10001)).intern();
               return;
            }

            var10004 = var10001;
            var14 = var13;
            var10005 = var1;
         }
      }
   }

   private static IllegalArgumentException a(IllegalArgumentException var0) {
      return var0;
   }
}
