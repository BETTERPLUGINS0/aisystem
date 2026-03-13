package github.nighter.smartspawner.api.data;

import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class SpawnerDataDTO {
   private final String spawnerId;
   private final Location location;
   private final EntityType entityType;
   private final Material spawnedItemMaterial;
   private final int stackSize;
   private final int maxStackSize;
   private final int baseMaxStoragePages;
   private final int baseMinMobs;
   private final int baseMaxMobs;
   private final int baseMaxStoredExp;
   private final long baseSpawnerDelay;

   public SpawnerDataDTO(String spawnerId, Location location, EntityType entityType, Material spawnedItemMaterial, int stackSize, int maxStackSize, int baseMaxStoragePages, int baseMinMobs, int baseMaxMobs, int baseMaxStoredExp, long baseSpawnerDelay) {
      this.spawnerId = spawnerId;
      this.location = location;
      this.entityType = entityType;
      this.spawnedItemMaterial = spawnedItemMaterial;
      this.stackSize = stackSize;
      this.maxStackSize = maxStackSize;
      this.baseMaxStoragePages = baseMaxStoragePages;
      this.baseMinMobs = baseMinMobs;
      this.baseMaxMobs = baseMaxMobs;
      this.baseMaxStoredExp = baseMaxStoredExp;
      this.baseSpawnerDelay = baseSpawnerDelay;
   }

   public boolean isItemSpawner() {
      return this.entityType == EntityType.ITEM && this.spawnedItemMaterial != null;
   }

   @Generated
   public String getSpawnerId() {
      return this.spawnerId;
   }

   @Generated
   public Location getLocation() {
      return this.location;
   }

   @Generated
   public EntityType getEntityType() {
      return this.entityType;
   }

   @Generated
   public Material getSpawnedItemMaterial() {
      return this.spawnedItemMaterial;
   }

   @Generated
   public int getStackSize() {
      return this.stackSize;
   }

   @Generated
   public int getMaxStackSize() {
      return this.maxStackSize;
   }

   @Generated
   public int getBaseMaxStoragePages() {
      return this.baseMaxStoragePages;
   }

   @Generated
   public int getBaseMinMobs() {
      return this.baseMinMobs;
   }

   @Generated
   public int getBaseMaxMobs() {
      return this.baseMaxMobs;
   }

   @Generated
   public int getBaseMaxStoredExp() {
      return this.baseMaxStoredExp;
   }

   @Generated
   public long getBaseSpawnerDelay() {
      return this.baseSpawnerDelay;
   }
}
