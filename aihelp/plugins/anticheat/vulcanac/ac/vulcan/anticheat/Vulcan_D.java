package ac.vulcan.anticheat;

class Vulcan_D extends Vulcan_S {
   private String[] Vulcan_J;
   private static final int Vulcan_W = 256;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(2999911030613090385L, -3745485059674635592L, (Object)null).a(180739295626535L);

   public String Vulcan_d(Object[] var1) {
      long var2 = (Long)var1[0];
      int var4 = (Integer)var1[1];
      long var5 = var2 ^ 0L;
      long var7 = var2 ^ 57229219157186L;

      try {
         if (var4 < 256) {
            return this.Vulcan_o(new Object[]{new Long(var7)})[var4];
         }
      } catch (RuntimeException var9) {
         throw b(var9);
      }

      return super.Vulcan_d(new Object[]{new Long(var5), new Integer(var4)});
   }

   private String[] Vulcan_o(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private void Vulcan_s(Object[] var1) {
      long var2 = (Long)var1[0];
      var2 ^= a;
      long var4 = var2 ^ 25268320912876L;
      this.Vulcan_J = new String[256];
      String[] var10000 = Vulcan_XL.Vulcan_v();
      int var7 = 0;
      String[] var6 = var10000;

      label21:
      while(true) {
         if (var7 < 256) {
            this.Vulcan_J[var7] = super.Vulcan_d(new Object[]{new Long(var4), new Integer(var7)});
            ++var7;
            if (var6 != null) {
               continue;
            }
         }

         while(var2 < 0L) {
            if (var6 != null) {
               continue label21;
            }
         }

         return;
      }
   }

   private static RuntimeException b(RuntimeException var0) {
      return var0;
   }
}
