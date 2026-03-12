package com.nisovin.shopkeepers.shopkeeper.spawning;

enum SpawnResult {
   IGNORED,
   IGNORED_INACTIVE,
   SPAWNED,
   ALREADY_SPAWNED,
   SPAWNING_FAILED,
   QUEUED,
   AWAITING_WORLD_SAVE_RESPAWN,
   DESPAWNED_AND_AWAITING_WORLD_SAVE_RESPAWN;

   // $FF: synthetic method
   private static SpawnResult[] $values() {
      return new SpawnResult[]{IGNORED, IGNORED_INACTIVE, SPAWNED, ALREADY_SPAWNED, SPAWNING_FAILED, QUEUED, AWAITING_WORLD_SAVE_RESPAWN, DESPAWNED_AND_AWAITING_WORLD_SAVE_RESPAWN};
   }
}
