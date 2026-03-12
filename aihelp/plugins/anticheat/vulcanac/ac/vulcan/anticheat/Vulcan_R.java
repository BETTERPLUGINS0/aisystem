package ac.vulcan.anticheat;

final class Vulcan_R extends Vulcan_O {
   private static final long serialVersionUID = 1L;
   private static final long b = me.frep.vulcan.spigot.Vulcan_n.a(3948327217976644868L, 1597386176261965562L, (Object)null).a(280927247008885L);

   Vulcan_R() {
      long var1 = b ^ 76182982086624L;
      long var3 = var1 ^ 133704065584023L;
      long var5 = var1 ^ 39000010089197L;
      super();
      this.Vulcan_e(new Object[]{new Boolean(false)});
      this.Vulcan_bE(new Object[]{new Boolean(false)});
      this.Vulcan_I(new Object[]{new Boolean(false)});
      this.Vulcan_ZT(new Object[]{new Long(var3), ""});
      this.Vulcan_X(new Object[]{"", new Long(var5)});
   }

   private Object readResolve() {
      return Vulcan_O.Vulcan_U;
   }
}
