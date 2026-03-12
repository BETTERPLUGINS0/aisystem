package me.SuperRonanCraft.BetterRTP.references.depends.regionPlugins;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import org.bukkit.Location;

public class RTP_Residence implements RegionPluginCheck {
   public boolean check(Location loc) {
      boolean result = true;
      if (REGIONPLUGINS.RESIDENCE.isEnabled()) {
         try {
            Residence instance = Residence.getInstance();
            ClaimedResidence claim = instance.getResidenceManagerAPI().getByLoc(loc);
            result = claim == null;
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

      return result;
   }
}
