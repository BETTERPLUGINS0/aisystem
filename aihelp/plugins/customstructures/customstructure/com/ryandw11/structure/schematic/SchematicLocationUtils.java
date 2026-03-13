package com.ryandw11.structure.schematic;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Location;

public class SchematicLocationUtils {
   private SchematicLocationUtils() {
   }

   protected static boolean isNotAlreadyIn(List<Location> locations, Location location) {
      Iterator var2 = locations.iterator();

      Location auxLocation;
      do {
         if (!var2.hasNext()) {
            return true;
         }

         auxLocation = (Location)var2.next();
      } while(!(location.distance(auxLocation) < 1.0D));

      return false;
   }

   public static BlockVector3 rotateAround(BlockVector3 point, BlockVector3 center, double angle) {
      angle = Math.toRadians(angle * -1.0D);
      double rotatedX = Math.cos(angle) * (double)(point.getX() - center.getX()) - Math.sin(angle) * (double)(point.getZ() - center.getZ()) + (double)center.getX();
      double rotatedZ = Math.sin(angle) * (double)(point.getX() - center.getX()) + Math.cos(angle) * (double)(point.getZ() - center.getZ()) + (double)center.getZ();
      return BlockVector3.at(rotatedX, (double)point.getY(), rotatedZ);
   }

   public static Location rotateAround(Location point, Location center, double angle) {
      angle = Math.toRadians(angle * -1.0D);
      double rotatedX = Math.cos(angle) * (double)(point.getBlockX() - center.getBlockX()) - Math.sin(angle) * (double)(point.getBlockZ() - center.getBlockZ()) + (double)center.getBlockX();
      double rotatedZ = Math.sin(angle) * (double)(point.getBlockX() - center.getBlockX()) + Math.cos(angle) * (double)(point.getBlockZ() - center.getBlockZ()) + (double)center.getBlockZ();
      return new Location(point.getWorld(), Math.floor(rotatedX), point.getY(), Math.floor(rotatedZ));
   }

   public static Location getMinimumLocation(Clipboard clipboard, Location pasteLocation, double rotation) {
      BlockVector3 originalOrigin = clipboard.getOrigin();
      BlockVector3 originalMinimumPoint = clipboard.getRegion().getMinimumPoint();
      BlockVector3 originalMinimumOffset = originalOrigin.subtract(originalMinimumPoint);
      BlockVector3 newOrigin = BukkitAdapter.asBlockVector(pasteLocation);
      BlockVector3 newMinimumPoint = newOrigin.subtract(originalMinimumOffset);
      BlockVector3 newRotatedMinimumPoint = rotateAround(newMinimumPoint, newOrigin, rotation);
      return new Location(pasteLocation.getWorld(), (double)newRotatedMinimumPoint.getX(), (double)newRotatedMinimumPoint.getY(), (double)newRotatedMinimumPoint.getZ());
   }

   public static Location getMaximumLocation(Clipboard clipboard, Location pasteLocation, double rotation) {
      BlockVector3 originalOrigin = clipboard.getOrigin();
      BlockVector3 originalMaximumPoint = clipboard.getRegion().getMaximumPoint();
      BlockVector3 originalMaximumOffset = originalOrigin.subtract(originalMaximumPoint);
      BlockVector3 newOrigin = BukkitAdapter.asBlockVector(pasteLocation);
      BlockVector3 newMaximumPoint = newOrigin.subtract(originalMaximumOffset);
      BlockVector3 newRotatedMaximumPoint = rotateAround(newMaximumPoint, newOrigin, rotation);
      return new Location(pasteLocation.getWorld(), (double)newRotatedMaximumPoint.getX(), (double)newRotatedMaximumPoint.getY(), (double)newRotatedMaximumPoint.getZ());
   }
}
