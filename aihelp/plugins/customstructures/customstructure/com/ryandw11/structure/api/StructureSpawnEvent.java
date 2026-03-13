package com.ryandw11.structure.api;

import com.ryandw11.structure.api.holder.StructureSpawnHolder;
import com.ryandw11.structure.structure.Structure;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class StructureSpawnEvent extends Event {
   private static final HandlerList handlers = new HandlerList();
   private final Structure structure;
   private final Location location;
   private final double rotation;
   private final StructureSpawnHolder holder;

   public StructureSpawnEvent(Structure structure, Location location, double rotation, StructureSpawnHolder holder) {
      this.structure = structure;
      this.location = location;
      this.rotation = rotation;
      this.holder = holder;
   }

   public Structure getStructure() {
      return this.structure;
   }

   public Location getLocation() {
      return this.location;
   }

   public double getRotation() {
      return this.rotation;
   }

   public Location getMinimumPoint() {
      return this.holder.getMinimumPoint();
   }

   public Location getMaximumPoint() {
      return this.holder.getMaximumPoint();
   }

   public List<Location> getContainersAndSignsLocations() {
      return this.holder.getContainersAndSignsLocations();
   }

   @NotNull
   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }
}
