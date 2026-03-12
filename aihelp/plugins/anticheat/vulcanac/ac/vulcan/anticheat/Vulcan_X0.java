package ac.vulcan.anticheat;

public class Vulcan_X0 {
   private static int[] Vulcan_f;

   public static boolean Vulcan_j(Object[] var0) {
      String var1 = (String)var0[0];

      try {
         Class.forName(me.frep.vulcan.spigot.Vulcan_s.a(var1));
         return true;
      } catch (ClassNotFoundException var3) {
         return false;
      }
   }

   public static void Vulcan_l(int[] var0) {
      Vulcan_f = var0;
   }

   public static int[] Vulcan_K() {
      return Vulcan_f;
   }

   static {
      if (Vulcan_K() != null) {
         Vulcan_l(new int[2]);
      }

   }
}
