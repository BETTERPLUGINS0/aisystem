package com.ryandw11.structure.io;

import com.ryandw11.structure.structure.Structure;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

public class NearbyStructuresRequest {
   private final Location location;
   private final String name;
   private final int limit;

   public NearbyStructuresRequest(Location location) {
      this(location, 1);
   }

   public NearbyStructuresRequest(Location location, String structureName) {
      this(location, (String)structureName, 1);
   }

   public NearbyStructuresRequest(Location location, Structure structure) {
      this(location, (String)structure.getName(), 1);
   }

   public NearbyStructuresRequest(Location location, int limit) {
      this(location, "", limit);
   }

   public NearbyStructuresRequest(Location location, Structure structure, int limit) {
      this(location, structure.getName(), limit);
   }

   public NearbyStructuresRequest(Location location, String structureName, int limit) {
      this.location = location;
      this.name = structureName;
      this.limit = limit;
   }

   public Location getLocation() {
      return this.location;
   }

   @Nullable
   public String getName() {
      return this.name.isEmpty() ? null : this.name;
   }

   public boolean hasName() {
      return !this.name.isEmpty();
   }

   public int getLimit() {
      return this.limit;
   }
}
