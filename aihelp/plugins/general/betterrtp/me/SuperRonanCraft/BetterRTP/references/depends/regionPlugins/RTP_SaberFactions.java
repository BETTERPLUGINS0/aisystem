package me.SuperRonanCraft.BetterRTP.references.depends.regionPlugins;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import org.bukkit.Location;

public class RTP_SaberFactions implements RegionPluginCheck {
   public boolean check(Location loc) {
      boolean result = true;
      if (REGIONPLUGINS.SABERFACTIONS.isEnabled()) {
         try {
            FLocation fLoc = new FLocation(loc);
            Faction faction = Board.getInstance().getFactionAt(fLoc);
            result = faction == null || faction.isWilderness() || faction.isSafeZone();
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

      return result;
   }
}
