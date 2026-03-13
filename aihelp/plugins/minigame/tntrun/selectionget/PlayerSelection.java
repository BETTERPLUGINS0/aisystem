package tntrun.selectionget;

import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerSelection {
   private WEIntegration weintegration = null;
   private OwnLocations ownlocations = new OwnLocations();

   public PlayerSelection() {
      if (Bukkit.getPluginManager().getPlugin("WorldEdit") != null) {
         this.weintegration = new WEIntegration();
      }

   }

   public PlayerCuboidSelection getPlayerSelection(Player player) {
      Location[] locs = this.ownlocations.getLocations(player);
      if (locs != null) {
         return new PlayerCuboidSelection(locs[0], locs[1]);
      } else {
         if (this.weintegration != null) {
            locs = this.weintegration.getLocations(player);
            if (locs != null) {
               return new PlayerCuboidSelection(locs[0], locs[1]);
            }
         }

         return null;
      }
   }

   public void setSelectionPoint1(Player player) {
      this.ownlocations.putPlayerLoc1(player.getName(), player.getTargetBlock((Set)null, 30).getLocation());
   }

   public void setSelectionPoint2(Player player) {
      this.ownlocations.putPlayerLoc2(player.getName(), player.getTargetBlock((Set)null, 30).getLocation());
   }

   public void clearSelectionPoints(Player player) {
      this.ownlocations.clearPoints(player.getName());
   }

   public Location getSelectionPoint1(Player player) {
      return this.ownlocations.getPlayerLoc1(player.getName());
   }

   public Location getSelectionPoint2(Player player) {
      return this.ownlocations.getPlayerLoc2(player.getName());
   }
}
