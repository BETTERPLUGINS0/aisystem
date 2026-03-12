package me.SuperRonanCraft.BetterRTP.references.depends.regionPlugins;

import me.RonanCraft.Pueblos.Pueblos;
import org.bukkit.Location;

public class RTP_Pueblos implements RegionPluginCheck {
   public boolean check(Location loc) {
      boolean result = true;
      if (REGIONPLUGINS.PUEBLOS.isEnabled()) {
         try {
            result = Pueblos.getInstance().getClaimHandler().getClaimMain(loc) == null;
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      return result;
   }
}
