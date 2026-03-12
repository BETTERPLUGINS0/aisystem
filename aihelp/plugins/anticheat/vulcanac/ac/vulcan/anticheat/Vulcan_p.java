package ac.vulcan.anticheat;

final class Vulcan_p extends Vulcan_O {
   private static final long serialVersionUID = 1L;

   Vulcan_p() {
      this.Vulcan_i(new Object[]{new Boolean(true)});
      this.Vulcan_bE(new Object[]{new Boolean(false)});
   }

   private Object readResolve() {
      return Vulcan_O.Vulcan_M;
   }
}
