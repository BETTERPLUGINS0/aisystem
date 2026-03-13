package tntrun.selectionget;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class OwnLocations {
   private HashMap<String, Location> loc1 = new HashMap();
   private HashMap<String, Location> loc2 = new HashMap();

   protected void putPlayerLoc1(String playername, Location loc) {
      Bukkit.getLogger().info("[TNTRun_reloaded] Location P1 set to " + loc.toString());
      this.loc1.put(playername, loc);
   }

   protected void putPlayerLoc2(String playername, Location loc) {
      Bukkit.getLogger().info("[TNTRun_reloaded] Location P2 set to " + loc.toString());
      this.loc2.put(playername, loc);
   }

   protected Location getPlayerLoc1(String playername) {
      return (Location)this.loc1.get(playername);
   }

   protected Location getPlayerLoc2(String playername) {
      return (Location)this.loc2.get(playername);
   }

   protected void clearPoints(String playername) {
      this.loc1.remove(playername);
      this.loc2.remove(playername);
   }

   protected Location[] getLocations(Player player) {
      try {
         return this.sortLoc(player);
      } catch (Exception var3) {
         return null;
      }
   }

   private Location[] sortLoc(Player player) {
      double x1 = ((Location)this.loc1.get(player.getName())).getX();
      double x2 = ((Location)this.loc2.get(player.getName())).getX();
      double y1 = ((Location)this.loc1.get(player.getName())).getY();
      double y2 = ((Location)this.loc2.get(player.getName())).getY();
      double z1 = ((Location)this.loc1.get(player.getName())).getZ();
      double z2 = ((Location)this.loc2.get(player.getName())).getZ();
      Location[] locs = new Location[]{new Location(((Location)this.loc1.get(player.getName())).getWorld(), Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2)), new Location(((Location)this.loc1.get(player.getName())).getWorld(), Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2))};
      locs[0].distanceSquared(locs[1]);
      return locs;
   }
}
