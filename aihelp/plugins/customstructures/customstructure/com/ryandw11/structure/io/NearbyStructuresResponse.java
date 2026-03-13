package com.ryandw11.structure.io;

import com.ryandw11.structure.structure.Structure;
import java.util.List;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NearbyStructuresResponse {
   private final List<NearbyStructuresResponse.NearbyStructureContainer> response;

   public NearbyStructuresResponse(@NotNull List<NearbyStructuresResponse.NearbyStructureContainer> response) {
      this.response = response;
   }

   public List<NearbyStructuresResponse.NearbyStructureContainer> getResponse() {
      return this.response;
   }

   @Nullable
   public NearbyStructuresResponse.NearbyStructureContainer getFirst() {
      return this.response.isEmpty() ? null : (NearbyStructuresResponse.NearbyStructureContainer)this.response.get(0);
   }

   public boolean hasEntries() {
      return !this.response.isEmpty();
   }

   public static class NearbyStructureContainer {
      private final Location location;
      private final Structure structure;
      private final double distance;

      public NearbyStructureContainer(@NotNull Location location, @NotNull Structure structure, double distance) {
         this.location = location;
         this.structure = structure;
         this.distance = distance;
      }

      public Location getLocation() {
         return this.location;
      }

      public Structure getStructure() {
         return this.structure;
      }

      public double getDistance() {
         return this.distance;
      }
   }
}
