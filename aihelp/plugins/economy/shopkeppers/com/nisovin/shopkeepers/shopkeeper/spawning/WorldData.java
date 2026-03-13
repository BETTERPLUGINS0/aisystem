package com.nisovin.shopkeepers.shopkeeper.spawning;

import com.nisovin.shopkeepers.util.java.Validate;

final class WorldData {
   private final String worldName;
   private WorldSaveDespawner.RespawnShopkeepersAfterWorldSaveTask worldSaveRespawnTask = null;

   WorldData(String worldName) {
      Validate.notNull(worldName, (String)"worldName is null");
      this.worldName = worldName;
   }

   public String getWorldName() {
      return this.worldName;
   }

   boolean isWorldSaveRespawnPending() {
      return this.worldSaveRespawnTask != null;
   }

   void setWorldSaveRespawnTask(WorldSaveDespawner.RespawnShopkeepersAfterWorldSaveTask worldSaveRespawnTask) {
      this.worldSaveRespawnTask = worldSaveRespawnTask;
   }

   void cancelWorldSaveRespawnTask() {
      if (this.worldSaveRespawnTask != null) {
         this.worldSaveRespawnTask.cancel();
      }

   }

   void cleanUp() {
      this.cancelWorldSaveRespawnTask();
   }
}
