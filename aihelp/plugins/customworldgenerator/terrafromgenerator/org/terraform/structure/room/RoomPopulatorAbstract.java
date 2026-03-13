package org.terraform.structure.room;

import java.util.Random;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;

public abstract class RoomPopulatorAbstract {
   public final Random rand;
   private final boolean forceSpawn;
   private final boolean unique;

   public RoomPopulatorAbstract(Random rand, boolean forceSpawn, boolean unique) {
      this.rand = rand;
      this.forceSpawn = forceSpawn;
      this.unique = unique;
   }

   protected static int getNextIndex(int bfIndex) {
      ++bfIndex;
      if (bfIndex >= 8) {
         bfIndex = 0;
      }

      return bfIndex;
   }

   public Random getRand() {
      return this.rand;
   }

   public boolean isForceSpawn() {
      return this.forceSpawn;
   }

   public boolean isUnique() {
      return this.unique;
   }

   public abstract void populate(PopulatorDataAbstract var1, CubeRoom var2);

   public abstract boolean canPopulate(CubeRoom var1);
}
