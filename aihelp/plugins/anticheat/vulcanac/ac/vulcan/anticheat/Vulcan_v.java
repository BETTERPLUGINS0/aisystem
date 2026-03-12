package ac.vulcan.anticheat;

final class Vulcan_v extends Vulcan_O {
   private static final long serialVersionUID = 1L;

   Vulcan_v() {
      this.Vulcan_I(new Object[]{new Boolean(false)});
   }

   private Object readResolve() {
      return Vulcan_O.Vulcan_P;
   }
}
