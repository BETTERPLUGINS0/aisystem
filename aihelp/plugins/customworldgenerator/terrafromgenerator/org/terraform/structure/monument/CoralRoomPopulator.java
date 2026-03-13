package org.terraform.structure.monument;

import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;
import org.terraform.utils.CoralGenerator;
import org.terraform.utils.GenUtils;

public class CoralRoomPopulator extends LevelledRoomPopulator {
   public CoralRoomPopulator(Random rand, MonumentDesign design, boolean forceSpawn, boolean unique) {
      super(rand, design, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      super.populate(data, room);
      Iterator var3 = room.getFourWalls(data, 1).entrySet().iterator();

      while(var3.hasNext()) {
         Entry<Wall, Integer> entry = (Entry)var3.next();
         Wall w = ((Wall)entry.getKey()).getUp(5);
         int length = (Integer)entry.getValue();

         for(int i = 0; i < length; ++i) {
            int x = w.get().getX();
            int y = w.get().getY() + GenUtils.randInt(this.rand, 0, room.getHeight() - 6);
            int z = w.get().getZ();
            if (GenUtils.chance(this.rand, 1, 15)) {
               CoralGenerator.generateCoral(data, x, y, z);
            }

            if (GenUtils.chance(this.rand, 1, 5)) {
               CoralGenerator.generateSponge(data, x, y, z);
            }

            w = w.getLeft();
         }
      }

   }
}
