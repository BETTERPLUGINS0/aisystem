package tntrun.arena.structure;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.Vector;

public class PlayerSpawn {
   private Vector p1 = null;
   private float yaw;
   private float pitch;

   protected Vector getVector() {
      return this.p1;
   }

   protected float getYaw() {
      return this.yaw;
   }

   protected float getPitch() {
      return this.pitch;
   }

   protected boolean isConfigured() {
      return this.p1 != null;
   }

   protected void setPlayerSpawn(Location loc) {
      this.p1 = loc.toVector();
      this.yaw = loc.getYaw();
      this.pitch = loc.getPitch();
   }

   protected void remove() {
      this.p1 = null;
   }

   protected void saveToConfig(FileConfiguration config) {
      config.set("spawnpoint.p1", this.p1);
      config.set("spawnpoint.yaw", this.yaw);
      config.set("spawnpoint.pitch", this.pitch);
   }

   protected void loadFromConfig(FileConfiguration config) {
      this.p1 = config.isSet("spawnpoint.p1") ? config.getVector("spawnpoint.p1", (Vector)null) : config.getVector("spawnpoint", (Vector)null);
      this.yaw = (float)config.getDouble("spawnpoint.yaw", 0.0D);
      this.pitch = (float)config.getDouble("spawnpoint.pitch", 0.0D);
   }
}
