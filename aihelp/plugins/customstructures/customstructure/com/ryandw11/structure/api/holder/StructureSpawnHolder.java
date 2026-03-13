package com.ryandw11.structure.api.holder;

import java.util.List;
import org.bukkit.Location;

public class StructureSpawnHolder {
   private final Location minimumPoint;
   private final Location maximumPoint;
   private final List<Location> containersAndSignsLocations;

   public StructureSpawnHolder(Location minimumPoint, Location maximumPoint, List<Location> containersAndSignsLocations) {
      this.maximumPoint = maximumPoint;
      this.minimumPoint = minimumPoint;
      this.containersAndSignsLocations = containersAndSignsLocations;
   }

   public Location getMinimumPoint() {
      return this.minimumPoint;
   }

   public Location getMaximumPoint() {
      return this.maximumPoint;
   }

   public List<Location> getContainersAndSignsLocations() {
      return this.containersAndSignsLocations;
   }
}
