package tntrun.selectionget;

import org.bukkit.Location;

public class PlayerCuboidSelection {
   private Location minpoint;
   private Location maxpoint;

   public PlayerCuboidSelection(Location minpoint, Location maxpoint) {
      this.minpoint = minpoint;
      this.maxpoint = maxpoint;
   }

   public Location getMinimumLocation() {
      return this.minpoint;
   }

   public Location getMaximumLocation() {
      return this.maxpoint;
   }
}
