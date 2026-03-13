package tntrun.arena.structure;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.Vector;

public class SpectatorSpawn {
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

   protected void setSpectatorSpawn(Location loc) {
      this.p1 = loc.toVector();
      this.yaw = loc.getYaw();
      this.pitch = loc.getPitch();
   }

   protected void remove() {
      this.p1 = null;
   }

   protected void saveToConfig(FileConfiguration config) {
      if (!this.isConfigured()) {
         config.set("spectatorspawn", (Object)null);
      } else {
         config.set("spectatorspawn.p1", this.p1);
         config.set("spectatorspawn.yaw", this.yaw);
         config.set("spectatorspawn.pitch", this.pitch);
      }
   }

   protected void loadFromConfig(FileConfiguration config) {
      if (config.isSet("spectatorspawn.p1")) {
         this.p1 = config.getVector("spectatorspawn.p1");
      } else {
         this.p1 = config.getVector("spectatorspawn", (Vector)null);
      }

      this.yaw = (float)config.getDouble("spectatorspawn.yaw", 0.0D);
      this.pitch = (float)config.getDouble("spectatorspawn.pitch", 0.0D);
   }
}
