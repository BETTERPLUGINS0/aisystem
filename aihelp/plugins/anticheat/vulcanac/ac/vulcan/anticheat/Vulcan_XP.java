package ac.vulcan.anticheat;

class Vulcan_XP extends Vulcan_Xp {
   private static final long b = me.frep.vulcan.spigot.Vulcan_n.a(-8895069649138733929L, 5242594822387945527L, (Object)null).a(280151873982672L);

   public Vulcan_XP() {
   }

   public Vulcan_XP(int var1) {
      super(var1);
   }

   private int Vulcan_P(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_E(Object[] var1) {
      long var2 = (Long)var1[0];
      String var4 = (String)var1[1];
      int var5 = (Integer)var1[2];
      long var6 = var2 ^ 104058356891476L;
      long var8 = var2 ^ 114922038752114L;
      String[] var10000 = Vulcan_XL.Vulcan_v();
      this.Vulcan_v(new Object[]{new Integer(this.Vulcan_x + 1), new Long(var6)});
      String[] var10 = var10000;
      int var11 = this.Vulcan_P(new Object[]{new Long(var8), new Integer(var5)});

      int var13;
      label22: {
         try {
            var13 = var11;
            if (var10 == null) {
               break label22;
            }

            if (var11 > 0) {
               return;
            }
         } catch (RuntimeException var12) {
            throw a(var12);
         }

         var13 = -(var11 + 1);
      }

      var11 = var13;
      System.arraycopy(this.Vulcan_R, var11, this.Vulcan_R, var11 + 1, this.Vulcan_x - var11);
      this.Vulcan_R[var11] = var5;
      System.arraycopy(this.Vulcan_r, var11, this.Vulcan_r, var11 + 1, this.Vulcan_x - var11);
      this.Vulcan_r[var11] = var4;
      ++this.Vulcan_x;
   }

   public String Vulcan_d(Object[] var1) {
      long var3 = (Long)var1[0];
      int var2 = (Integer)var1[1];
      long var5 = var3 ^ 46630279233443L;
      int var7 = this.Vulcan_P(new Object[]{new Long(var5), new Integer(var2)});

      try {
         if (var7 < 0) {
            return null;
         }
      } catch (RuntimeException var8) {
         throw a(var8);
      }

      return this.Vulcan_r[var7];
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
