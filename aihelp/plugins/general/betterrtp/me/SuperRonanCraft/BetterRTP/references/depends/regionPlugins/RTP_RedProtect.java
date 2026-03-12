package me.SuperRonanCraft.BetterRTP.references.depends.regionPlugins;

import br.net.fabiozumbi12.RedProtect.Bukkit.RedProtect;
import org.bukkit.Location;

public class RTP_RedProtect implements RegionPluginCheck {
   public boolean check(Location loc) {
      boolean result = true;
      if (REGIONPLUGINS.REDPROTECT.isEnabled()) {
         try {
            result = RedProtect.get().getAPI().getRegion(loc) == null;
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      return result;
   }
}
