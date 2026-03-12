package me.SuperRonanCraft.BetterRTP.references.rtpinfo;

import org.bukkit.Location;

public class QueueData {
   final int database_id;
   Location location;
   final long generated;

   public QueueData(Location location, long generated, int database_id) {
      this.location = location;
      this.generated = generated;
      this.database_id = database_id;
   }

   public int getDatabase_id() {
      return this.database_id;
   }

   public Location getLocation() {
      return this.location;
   }

   public void setLocation(Location location) {
      this.location = location;
   }

   public long getGenerated() {
      return this.generated;
   }
}
