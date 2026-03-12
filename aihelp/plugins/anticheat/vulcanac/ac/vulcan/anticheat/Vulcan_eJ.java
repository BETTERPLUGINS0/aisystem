package ac.vulcan.anticheat;

import java.util.Map;

public abstract class Vulcan_eJ {
   private static final Vulcan_eJ Vulcan_I = new Vulcan_ev((Map)null);
   private static final Vulcan_eJ Vulcan_c;

   public static Vulcan_eJ Vulcan_g(Object[] var0) {
      return Vulcan_I;
   }

   public static Vulcan_eJ Vulcan_Z(Object[] var0) {
      return Vulcan_c;
   }

   public static Vulcan_eJ Vulcan_Q(Object[] var0) {
      Map var1 = (Map)var0[0];
      return new Vulcan_ev(var1);
   }

   protected Vulcan_eJ() {
   }

   public abstract String Vulcan_Y(Object[] var1);

   static {
      Object var0 = null;

      try {
         var0 = new Vulcan_ev(System.getProperties());
      } catch (SecurityException var2) {
         var0 = Vulcan_I;
      }

      Vulcan_c = (Vulcan_eJ)var0;
   }
}
