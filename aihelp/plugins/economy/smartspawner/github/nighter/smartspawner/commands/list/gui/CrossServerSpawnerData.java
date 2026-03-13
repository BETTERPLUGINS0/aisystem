package github.nighter.smartspawner.commands.list.gui;

import org.bukkit.entity.EntityType;

public class CrossServerSpawnerData {
   private final String spawnerId;
   private final String serverName;
   private final String worldName;
   private final int locX;
   private final int locY;
   private final int locZ;
   private final EntityType entityType;
   private final int stackSize;
   private final boolean active;
   private final String lastInteractedPlayer;
   private final int storedExp;
   private final long totalItems;

   public CrossServerSpawnerData(String spawnerId, String serverName, String worldName, int locX, int locY, int locZ, EntityType entityType, int stackSize, boolean active, String lastInteractedPlayer, int storedExp, long totalItems) {
      this.spawnerId = spawnerId;
      this.serverName = serverName;
      this.worldName = worldName;
      this.locX = locX;
      this.locY = locY;
      this.locZ = locZ;
      this.entityType = entityType;
      this.stackSize = stackSize;
      this.active = active;
      this.lastInteractedPlayer = lastInteractedPlayer;
      this.storedExp = storedExp;
      this.totalItems = totalItems;
   }

   public String getSpawnerId() {
      return this.spawnerId;
   }

   public String getServerName() {
      return this.serverName;
   }

   public String getWorldName() {
      return this.worldName;
   }

   public int getLocX() {
      return this.locX;
   }

   public int getLocY() {
      return this.locY;
   }

   public int getLocZ() {
      return this.locZ;
   }

   public EntityType getEntityType() {
      return this.entityType;
   }

   public int getStackSize() {
      return this.stackSize;
   }

   public boolean isActive() {
      return this.active;
   }

   public String getLastInteractedPlayer() {
      return this.lastInteractedPlayer;
   }

   public int getStoredExp() {
      return this.storedExp;
   }

   public long getTotalItems() {
      return this.totalItems;
   }

   public boolean isLocalServer(String currentServerName) {
      return this.serverName.equals(currentServerName);
   }
}
