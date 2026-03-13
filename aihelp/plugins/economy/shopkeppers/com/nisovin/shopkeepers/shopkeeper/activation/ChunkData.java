package com.nisovin.shopkeepers.shopkeeper.activation;

import com.nisovin.shopkeepers.api.util.ChunkCoords;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.nullness.qual.Nullable;

final class ChunkData {
   private final ChunkCoords chunkCoords;
   private boolean shouldBeActive;
   private boolean active;
   @Nullable
   private BukkitTask delayedActivationTask = null;

   ChunkData(ChunkCoords chunkCoords) {
      Validate.notNull(chunkCoords, (String)"chunkCoords is null");
      this.chunkCoords = chunkCoords;
      this.setActive(chunkCoords.isChunkLoaded());
   }

   public ChunkCoords getChunkCoords() {
      return this.chunkCoords;
   }

   boolean isShouldBeActive() {
      return this.shouldBeActive;
   }

   void setShouldBeActive(boolean shouldBeActive) {
      this.shouldBeActive = shouldBeActive;
   }

   public boolean isActive() {
      return this.active;
   }

   void setActive(boolean active) {
      this.active = active;
      this.setShouldBeActive(active);
   }

   public boolean isActivationDelayed() {
      return this.delayedActivationTask != null;
   }

   void setDelayedActivationTask(@Nullable BukkitTask delayedActivationTask) {
      this.delayedActivationTask = delayedActivationTask;
   }

   void cancelDelayedActivation() {
      if (this.delayedActivationTask != null) {
         this.delayedActivationTask.cancel();
         this.delayedActivationTask = null;
      }

   }

   boolean needsActivation() {
      return !this.isActive() && !this.isShouldBeActive() && !this.isActivationDelayed() ? this.chunkCoords.isChunkLoaded() : false;
   }

   void cleanUp() {
      this.setShouldBeActive(false);
      this.cancelDelayedActivation();
   }
}
