package com.nisovin.shopkeepers.shopkeeper.spawning;

import com.nisovin.shopkeepers.component.Component;

public final class ShopkeeperSpawnState extends Component {
   private ShopkeeperSpawnState.State state;

   public ShopkeeperSpawnState() {
      this.state = ShopkeeperSpawnState.State.DESPAWNED;
   }

   ShopkeeperSpawnState.State getState() {
      return this.state;
   }

   void setState(ShopkeeperSpawnState.State state) {
      assert state != null;

      this.state = state;
   }

   public boolean isSpawningScheduled() {
      switch(this.state.ordinal()) {
      case 2:
      case 3:
      case 4:
         return true;
      default:
         return false;
      }
   }

   public static enum State {
      DESPAWNED,
      SPAWNED,
      QUEUED,
      PENDING_WORLD_SAVE_RESPAWN,
      SPAWNING,
      DESPAWNING;

      // $FF: synthetic method
      private static ShopkeeperSpawnState.State[] $values() {
         return new ShopkeeperSpawnState.State[]{DESPAWNED, SPAWNED, QUEUED, PENDING_WORLD_SAVE_RESPAWN, SPAWNING, DESPAWNING};
      }
   }
}
