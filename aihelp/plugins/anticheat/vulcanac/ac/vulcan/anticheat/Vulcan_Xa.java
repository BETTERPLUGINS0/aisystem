package ac.vulcan.anticheat;

import me.frep.vulcan.spigot.check.AbstractCheck;

/** @deprecated */
public abstract class Vulcan_Xa extends Vulcan_XY {
   private static final long serialVersionUID = -7129650521543789085L;
   private final int Vulcan_L;
   private static final long b = me.frep.vulcan.spigot.Vulcan_n.a(-7531686406455901827L, 2107033672998569606L, (Object)null).a(80795518096847L);
   private static final String d;

   protected Vulcan_Xa(String var1, int var2) {
      super(var1);
      this.Vulcan_L = var2;
   }

   protected static Vulcan_XY Vulcan_L(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public final int Vulcan_s(Object[] var1) {
      return this.Vulcan_L;
   }

   public int compareTo(Object var1) {
      return this.Vulcan_L - ((Vulcan_Xa)var1).Vulcan_L;
   }

   public String toString() {
      long var1 = b ^ 124354240477558L;
      long var3 = var1 ^ 593567800682L;
      AbstractCheck[] var5 = Vulcan_XY.Vulcan_N();

      String var10000;
      label20: {
         try {
            var10000 = this.Vulcan_u;
            if (var5 != null) {
               return var10000;
            }

            if (var10000 != null) {
               break label20;
            }
         } catch (IllegalArgumentException var7) {
            throw a(var7);
         }

         String var6 = Vulcan_eo.Vulcan_B(new Object[]{this.Vulcan_J(new Object[0]), new Long(var3)});
         this.Vulcan_u = var6 + "[" + this.Vulcan_q(new Object[0]) + "=" + this.Vulcan_s(new Object[0]) + "]";
      }

      var10000 = this.Vulcan_u;
      return var10000;
   }

   static {
      char[] var10002 = "[= ?>%\u0004bu\u0006s\u001a8\u0002/80l\u000fk\u001f`!e}\u001ek\u001fz9)".toCharArray();
      int var10003 = var10002.length;
      int var1 = 0;
      byte var13 = 39;
      char[] var10001 = var10002;
      int var11 = var10003;
      char[] var10004;
      int var10005;
      byte var14;
      if (var10003 <= 1) {
         var10004 = var10002;
         var14 = 39;
         var10005 = var1;
      } else {
         var13 = 39;
         var11 = var10003;
         if (var10003 <= var1) {
            d = (new String(var10002)).intern();
            return;
         }

         var10004 = var10002;
         var14 = 39;
         var10005 = var1;
      }

      while(true) {
         char var9 = var10004[var10005];
         byte var10;
         switch(var1 % 7) {
         case 0:
            var10 = 40;
            break;
         case 1:
            var10 = 114;
            break;
         case 2:
            var10 = 98;
            break;
         case 3:
            var10 = 56;
            break;
         case 4:
            var10 = 92;
            break;
         case 5:
            var10 = 108;
            break;
         default:
            var10 = 86;
         }

         var10004[var10005] = (char)(var9 ^ var14 ^ var10);
         ++var1;
         if (var13 == 0) {
            var10005 = var13;
            var10004 = var10001;
            var14 = var13;
         } else {
            if (var11 <= var1) {
               d = (new String(var10001)).intern();
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
