package ac.vulcan.anticheat;

class Vulcan_bu {
   private Object Vulcan_I;
   private int Vulcan_w;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-8414411708181359499L, -1261776381785259808L, (Object)null).a(242942034689268L);

   static boolean Vulcan_n(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   Vulcan_bu(Object var1) {
      this.Vulcan_I = var1;
      this.Vulcan_w = 1;
   }

   Vulcan_bu(Object var1, int var2) {
      this.Vulcan_I = var1;
      this.Vulcan_w = var2;
   }

   void Vulcan_s(Object[] var1) {
      ++this.Vulcan_w;
   }

   int Vulcan_M(Object[] var1) {
      return this.Vulcan_w;
   }

   Object Vulcan_h(Object[] var1) {
      return this.Vulcan_I;
   }

   public boolean equals(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public int hashCode() {
      return this.Vulcan_I.hashCode();
   }

   public String toString() {
      long var1 = a ^ 58760577002499L;
      long var3 = var1 ^ 44253862789218L;
      String var10000 = this.Vulcan_I.toString();
      int var10002 = this.Vulcan_w;
      return Vulcan_Xt.Vulcan_g(new Object[]{var10000, new Long(var3), new Integer(var10002)});
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
