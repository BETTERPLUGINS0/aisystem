package github.nighter.smartspawner.spawner.lootgen;

import github.nighter.smartspawner.spawner.properties.SpawnerData;
import java.util.List;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.World;

class RangeMath {
   private final List<SpawnerData> spawners;
   private final PlayerRangeWrapper[] rangePlayers;

   public RangeMath(PlayerRangeWrapper[] players, List<SpawnerData> spawners) {
      this.spawners = spawners;
      this.rangePlayers = players;
   }

   public boolean[] getActiveSpawners() {
      boolean[] activeSpawners = new boolean[this.spawners.size()];

      for(int i = 0; i < this.spawners.size(); ++i) {
         SpawnerData s = (SpawnerData)this.spawners.get(i);
         Location spawnerLoc = s.getSpawnerLocation();
         if (spawnerLoc != null) {
            World locWorld = spawnerLoc.getWorld();
            if (locWorld != null) {
               UUID worldUID = locWorld.getUID();
               double rangeSq = (double)(s.getSpawnerRange() * s.getSpawnerRange());
               boolean playerFound = false;
               PlayerRangeWrapper[] var10 = this.rangePlayers;
               int var11 = var10.length;

               for(int var12 = 0; var12 < var11; ++var12) {
                  PlayerRangeWrapper p = var10[var12];
                  if (p.spawnConditions() && worldUID.equals(p.worldUID()) && p.distanceSquared(spawnerLoc) <= rangeSq) {
                     playerFound = true;
                     break;
                  }
               }

               activeSpawners[i] = playerFound;
            }
         }
      }

      return activeSpawners;
   }
}
