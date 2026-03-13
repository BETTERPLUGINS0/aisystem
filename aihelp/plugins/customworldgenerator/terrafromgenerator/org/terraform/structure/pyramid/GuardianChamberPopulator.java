package org.terraform.structure.pyramid;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.GenUtils;

public class GuardianChamberPopulator extends RoomPopulatorAbstract {
   public GuardianChamberPopulator(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      int[] lowerCorner = room.getLowerCorner(1);
      int[] upperCorner = room.getUpperCorner(1);

      int x;
      for(x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
         for(int z = lowerCorner[1]; z <= upperCorner[1]; ++z) {
            if (GenUtils.chance(this.rand, 1, 30)) {
               if (this.rand.nextBoolean()) {
                  data.setType(x, room.getY() + 1, z, (Material)GenUtils.randChoice((Object[])(Material.STONE, Material.ANDESITE, Material.ANDESITE_WALL, Material.COBBLESTONE, Material.COBBLESTONE_WALL)));
               }
            } else if (x != lowerCorner[0] && x != upperCorner[0] && z != lowerCorner[1] && z != upperCorner[1] || !this.rand.nextBoolean()) {
               double heightMultiplierX = (double)(2 * Math.abs(room.getX() - x)) / (double)Math.abs(room.getWidthX());
               double heightMultiplierZ = (double)(2 * Math.abs(room.getZ() - z)) / (double)Math.abs(room.getWidthZ());
               double heightMultiplier = 1.0D - (heightMultiplierX + heightMultiplierZ) / 2.0D;
               int poolDepth = (int)(1.0D + heightMultiplier * 4.0D);

               for(int y = room.getY(); y > room.getY() - poolDepth; --y) {
                  data.setType(x, y, z, Material.WATER);
                  if (data.getType(x, y + 1, z) == Material.STONE_PRESSURE_PLATE) {
                     data.setType(x, y + 1, z, Material.AIR);
                  }
               }
            }
         }
      }

      for(x = 0; x < GenUtils.randInt(3, 5); ++x) {
         data.addEntity(room.getX(), room.getY() + 2, room.getZ(), EntityType.GUARDIAN);
      }

   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getWidthX() >= 5 && room.getWidthZ() >= 5 && room.getWidthX() < 13 && room.getWidthZ() < 13;
   }
}
