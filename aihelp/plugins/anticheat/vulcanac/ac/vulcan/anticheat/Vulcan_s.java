package ac.vulcan.anticheat;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class Vulcan_s extends Vulcan_C {
   private static int[] Vulcan_h;

   public Vulcan_s(Plugin var1) {
      super(var1);
   }

   public boolean Vulcan_L(Object[] var1) {
      return Bukkit.getServer().isPrimaryThread();
   }

   public static void Vulcan_r(int[] var0) {
      Vulcan_h = var0;
   }

   public static int[] Vulcan_T() {
      return Vulcan_h;
   }

   static {
      if (Vulcan_T() == null) {
         Vulcan_r(new int[5]);
      }

   }
}
