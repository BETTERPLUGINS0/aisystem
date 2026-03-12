package me.SuperRonanCraft.BetterRTP.references.depends.regionPlugins;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import org.bukkit.Location;

public class RTP_FactionsUUID implements RegionPluginCheck {
   public boolean check(Location loc) {
      boolean result = true;
      if (REGIONPLUGINS.FACTIONSUUID.isEnabled()) {
         try {
            Faction faction = Board.getInstance().getFactionAt(new FLocation(loc));
            result = faction.isWilderness() || faction.isSafeZone();
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      return result;
   }
}
