package com.ryandw11.structure.api;

import com.ryandw11.structure.loottables.LootTable;
import com.ryandw11.structure.structure.Structure;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class LootPopulateEvent extends Event {
   private static final HandlerList handlers = new HandlerList();
   private final Structure structure;
   private final Location location;
   private final LootTable lootTable;
   private boolean canceled;

   public LootPopulateEvent(Structure structure, Location location, LootTable lootTable) {
      this.structure = structure;
      this.lootTable = lootTable;
      this.location = location;
      this.canceled = false;
   }

   public Structure getStructure() {
      return this.structure;
   }

   public LootTable getLootTable() {
      return this.lootTable;
   }

   public Location getLocation() {
      return this.location;
   }

   public void setCanceled(boolean canceled) {
      this.canceled = canceled;
   }

   public boolean isCanceled() {
      return this.canceled;
   }

   @NotNull
   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }
}
