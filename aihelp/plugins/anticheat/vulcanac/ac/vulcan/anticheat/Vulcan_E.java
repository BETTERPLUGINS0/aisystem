package ac.vulcan.anticheat;

final class Vulcan_E extends Vulcan_O {
   private static final long serialVersionUID = 1L;
   private static final long b = me.frep.vulcan.spigot.Vulcan_n.a(-6867815288317132157L, -7217465775711438890L, (Object)null).a(112291071372385L);

   Vulcan_E() {
      long var1 = b ^ 11472360936683L;
      long var3 = var1 ^ 1534348147329L;
      long var5 = var1 ^ 17126833189754L;
      long var7 = var1 ^ 100636396639227L;
      super();
      this.Vulcan_ZT(new Object[]{new Long(var3), "["});
      this.Vulcan_f(new Object[]{new Long(var5), Vulcan_X5.Vulcan_e + "  "});
      this.Vulcan_be(new Object[]{new Boolean(true)});
      this.Vulcan_X(new Object[]{Vulcan_X5.Vulcan_e + "]", new Long(var7)});
   }

   private Object readResolve() {
      return Vulcan_O.Vulcan_R;
   }
}
