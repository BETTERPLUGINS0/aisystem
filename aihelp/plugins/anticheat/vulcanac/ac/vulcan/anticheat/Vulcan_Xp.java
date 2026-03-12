package ac.vulcan.anticheat;

class Vulcan_Xp implements Vulcan_iS {
   protected final int Vulcan_q;
   protected int Vulcan_x = 0;
   protected String[] Vulcan_r;
   protected int[] Vulcan_R;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-8000726643940453498L, -7131988305181109484L, (Object)null).a(9548580358558L);

   public Vulcan_Xp() {
      this.Vulcan_q = 100;
      this.Vulcan_r = new String[this.Vulcan_q];
      this.Vulcan_R = new int[this.Vulcan_q];
   }

   public Vulcan_Xp(int var1) {
      this.Vulcan_q = var1;
      this.Vulcan_r = new String[var1];
      this.Vulcan_R = new int[var1];
   }

   public void Vulcan_E(Object[] var1) {
      long var2 = (Long)var1[0];
      String var4 = (String)var1[1];
      int var5 = (Integer)var1[2];
      long var6 = var2 ^ 104058356891476L;
      this.Vulcan_v(new Object[]{new Integer(this.Vulcan_x + 1), new Long(var6)});
      this.Vulcan_r[this.Vulcan_x] = var4;
      this.Vulcan_R[this.Vulcan_x] = var5;
      ++this.Vulcan_x;
   }

   protected void Vulcan_v(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public String Vulcan_d(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public int Vulcan_q(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private static RuntimeException b(RuntimeException var0) {
      return var0;
   }
}
