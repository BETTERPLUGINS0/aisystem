package org.terraform.structure.warmoceanruins;

import java.util.Random;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.GenUtils;
import org.terraform.utils.version.V_1_20;
import org.terraform.utils.version.Version;

public abstract class WarmOceanBaseRoom extends RoomPopulatorAbstract {
   public WarmOceanBaseRoom(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      room.setY(GenUtils.getHighestGround(data, room.getX(), room.getZ()) + 1);

      for(int i = 0; i < room.getWidthX() * room.getWidthZ() / 70; ++i) {
         int[] coords = room.randomCoords(this.rand);
         coords[1] = GenUtils.getHighestGround(data, coords[0], coords[2]);
         if (data.getType(coords[0], coords[1], coords[2]) != Material.SAND && data.getType(coords[0], coords[1], coords[2]) != Material.GRAVEL) {
            data.setType(coords[0], coords[1], coords[2], Material.MAGMA_BLOCK);
         } else if (Version.VERSION.isAtLeast(Version.v1_20) && GenUtils.chance(this.rand, 3, 4)) {
            if (data.getType(coords[0], coords[1], coords[2]) == Material.SAND) {
               data.setType(coords[0], coords[1], coords[2], V_1_20.SUSPICIOUS_SAND);
               data.lootTableChest(coords[0], coords[1], coords[2], TerraLootTable.OCEAN_RUIN_WARM_ARCHAEOLOGY);
            } else {
               data.setType(coords[0], coords[1], coords[2], V_1_20.SUSPICIOUS_GRAVEL);
               data.lootTableChest(coords[0], coords[1], coords[2], TerraLootTable.OCEAN_RUIN_COLD_ARCHAEOLOGY);
            }
         }
      }

   }
}
