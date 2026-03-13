package github.nighter.smartspawner.spawner.lootgen;

import java.util.UUID;
import org.bukkit.Location;

record PlayerRangeWrapper(UUID worldUID, double x, double y, double z, boolean spawnConditions) {
   PlayerRangeWrapper(UUID worldUID, double x, double y, double z, boolean spawnConditions) {
      this.worldUID = worldUID;
      this.x = x;
      this.y = y;
      this.z = z;
      this.spawnConditions = spawnConditions;
   }

   double distanceSquared(Location loc2) {
      double dx = this.x - loc2.getX();
      double dy = this.y - loc2.getY();
      double dz = this.z - loc2.getZ();
      return dx * dx + dy * dy + dz * dz;
   }

   public UUID worldUID() {
      return this.worldUID;
   }

   public double x() {
      return this.x;
   }

   public double y() {
      return this.y;
   }

   public double z() {
      return this.z;
   }

   public boolean spawnConditions() {
      return this.spawnConditions;
   }
}
