package org.terraform.structure;

import java.util.ArrayList;
import org.terraform.structure.room.RoomLayoutGenerator;

public class JigsawState {
   public final ArrayList<RoomLayoutGenerator> roomPopulatorStates = new ArrayList();
   boolean calculatedRange = false;
   int minChunkX = Integer.MAX_VALUE;
   int minChunkZ = Integer.MAX_VALUE;
   int maxChunkX = Integer.MIN_VALUE;
   int maxChunkZ = Integer.MIN_VALUE;

   public boolean isInRange(int chunkX, int chunkZ) {
      if (!this.calculatedRange) {
         this.calculatedRange = true;
         this.roomPopulatorStates.forEach((gen) -> {
            gen.getRooms().forEach((room) -> {
               int[] lowerCorner = room.getLowerCorner();
               int[] upperCorner = room.getUpperCorner();
               this.minChunkX = Math.min(this.minChunkX, lowerCorner[0] >> 4);
               this.maxChunkX = Math.max(this.maxChunkX, upperCorner[0] >> 4);
               this.minChunkZ = Math.min(this.minChunkZ, lowerCorner[1] >> 4);
               this.maxChunkZ = Math.max(this.maxChunkZ, upperCorner[1] >> 4);
            });
            this.minChunkX = Math.min(this.minChunkX, gen.getCentX() - gen.getRange() >> 4);
            this.maxChunkX = Math.max(this.maxChunkX, gen.getCentX() + gen.getRange() >> 4);
            this.minChunkZ = Math.min(this.minChunkZ, gen.getCentZ() - gen.getRange() >> 4);
            this.maxChunkZ = Math.max(this.maxChunkZ, gen.getCentZ() + gen.getRange() >> 4);
         });
      }

      return chunkX >= this.minChunkX && chunkX <= this.maxChunkX && chunkZ >= this.minChunkZ && chunkZ <= this.maxChunkZ;
   }
}
