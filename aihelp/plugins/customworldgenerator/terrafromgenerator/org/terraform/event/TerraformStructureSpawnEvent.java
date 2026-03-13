package org.terraform.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class TerraformStructureSpawnEvent extends Event {
   private static final HandlerList handlers = new HandlerList();
   private final String structureName;
   private final int x;
   private final int z;

   public TerraformStructureSpawnEvent(int x, int z, String structureName) {
      this.structureName = structureName;
      this.x = x;
      this.z = z;
   }

   @NotNull
   public static HandlerList getHandlerList() {
      return handlers;
   }

   @NotNull
   public HandlerList getHandlers() {
      return handlers;
   }

   public String getStructureName() {
      return this.structureName;
   }

   public int getX() {
      return this.x;
   }

   public int getZ() {
      return this.z;
   }
}
