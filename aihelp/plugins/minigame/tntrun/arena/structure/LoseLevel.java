package tntrun.arena.structure;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.Vector;

public class LoseLevel {
   private Vector p1 = null;
   private Vector p2 = null;

   public boolean isConfigured() {
      return this.p1 != null;
   }

   public boolean isLoseLocation(Location loc) {
      return loc.getY() < (double)(this.p1.getBlockY() + 1);
   }

   protected void setLoseLocation(Location p1) {
      this.p1 = p1.toVector();
   }

   protected void saveToConfig(FileConfiguration config) {
      config.set("loselevel.p1", this.p1);
      config.set("loselevel.p2", (Object)null);
   }

   protected void loadFromConfig(FileConfiguration config) {
      this.p1 = config.getVector("loselevel.p1", (Vector)null);
      this.p2 = config.getVector("loselevel.p2", (Vector)null);
      if (this.p2 != null && this.p1 != null && this.p2.getY() > this.p1.getY()) {
         this.p1.setY(this.p2.getY());
      }

   }
}
