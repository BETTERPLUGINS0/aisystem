package org.terraform.utils.datastructs;

import java.util.HashMap;
import org.terraform.data.CoordPair;
import org.terraform.data.SimpleLocation;

public class CompressedCoordBools {
   HashMap<CoordPair, CompressedChunkBools> chunks = new HashMap();

   public void set(SimpleLocation loc, boolean val) {
      if (val) {
         this.set(loc);
      } else {
         this.unSet(loc);
      }

   }

   public void set(SimpleLocation loc) {
      this.getOrCreate(loc).set(loc.getX() & 15, loc.getY(), loc.getZ() & 15);
   }

   public void unSet(SimpleLocation loc) {
      if (this.chunks.containsKey(new CoordPair(loc.getX() >> 4, loc.getZ() >> 4))) {
         this.getOrCreate(loc).unSet(loc.getX() & 15, loc.getY(), loc.getZ() & 15);
      }
   }

   public boolean isSet(SimpleLocation loc) {
      return !this.chunks.containsKey(new CoordPair(loc.getX() >> 4, loc.getZ() >> 4)) ? false : this.getOrCreate(loc).isSet(loc.getX() & 15, loc.getY(), loc.getZ() & 15);
   }

   private CompressedChunkBools getOrCreate(SimpleLocation loc) {
      CoordPair key = new CoordPair(loc.getX() >> 4, loc.getZ() >> 4);
      CompressedChunkBools compressed = (CompressedChunkBools)this.chunks.get(key);
      if (compressed == null) {
         compressed = new CompressedChunkBools();
         this.chunks.put(key, compressed);
      }

      return compressed;
   }
}
