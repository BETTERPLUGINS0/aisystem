package me.SuperRonanCraft.BetterRTP.references.depends.regionPlugins;

import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.claim.Claim;
import java.util.Iterator;
import org.bukkit.Location;

public class RTP_GriefDefender implements RegionPluginCheck {
   public boolean check(Location loc) {
      boolean result = true;
      if (REGIONPLUGINS.GRIEFDEFENDER.isEnabled()) {
         try {
            Iterator var3 = GriefDefender.getCore().getAllClaims().iterator();

            while(var3.hasNext()) {
               Claim claim = (Claim)var3.next();
               if (claim.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
                  result = false;
                  break;
               }
            }
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

      return result;
   }
}
