package org.terraform.structure.catacombs;

import java.util.Random;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.structure.room.CubeRoom;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.CoralGenerator;
import org.terraform.utils.CylinderBuilder;
import org.terraform.utils.GenUtils;
import org.terraform.utils.SphereBuilder;

public class CatacombsDripstoneBasinPopulator extends CatacombsStandardPopulator {
   public CatacombsDripstoneBasinPopulator(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      super.populate(data, room);
      SimpleBlock center = room.getCenterSimpleBlock(data).getUp();
      (new SphereBuilder(new Random(), center.getDown(), new Material[]{Material.WATER})).setRadius(3.0F).setSphereType(SphereBuilder.SphereType.LOWER_SEMISPHERE).setDoLiquidContainment(true).setHardReplace(true).build();
      (new CylinderBuilder(new Random(), center.getUp(10), new Material[]{Material.CAVE_AIR})).setRadius(2.5F).setRY(6.0F).setHardReplace(true).build();

      for(int i = 2; i <= GenUtils.randInt(2, 5); ++i) {
         int[] coords = room.randomCoords(this.rand, 2);
         SimpleBlock target = new SimpleBlock(data, coords[0], room.getY() + 1, coords[2]);
         target = target.findFloor(room.getHeight());
         if (target != null && BlockUtils.isWet(target.getUp())) {
            CoralGenerator.generateSeaPickles(data, target.getX(), target.getY() + 1, target.getZ());
         }
      }

   }
}
