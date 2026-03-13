package org.terraform.biome.cavepopulators;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.custombiomes.CustomBiomeType;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.coregen.populatordata.PopulatorDataICABiomeWriterAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.blockdata.DirectionalBuilder;
import org.terraform.utils.blockdata.MultipleFacingBuilder;

public class CrystallineClusterCavePopulator extends AbstractCaveClusterPopulator {
   public CrystallineClusterCavePopulator(float radius) {
      super(radius);
   }

   protected void oneUnit(TerraformWorld tw, @NotNull Random random, @NotNull SimpleBlock ceil, @NotNull SimpleBlock floor, boolean boundary) {
      int caveHeight = ceil.getY() - floor.getY();
      if (!Tag.SLABS.isTagged(floor.getType()) && !Tag.WALLS.isTagged(floor.getType())) {
         if (Material.MOSS_BLOCK != ceil.getType() && !BlockUtils.isOre(ceil.getType())) {
            ceil.setType(Material.AMETHYST_BLOCK);
            if (GenUtils.chance(random, 1, 5)) {
               (new DirectionalBuilder(Material.AMETHYST_CLUSTER)).setFacing(BlockFace.DOWN).apply(ceil.getDown());
            }
         }

         if (Material.MOSS_BLOCK != floor.getType() && !BlockUtils.isOre(floor.getType())) {
            floor.setType(Material.AMETHYST_BLOCK);
            if (GenUtils.chance(random, 1, 5)) {
               (new DirectionalBuilder(Material.AMETHYST_CLUSTER)).setFacing(BlockFace.UP).apply(floor.getUp());
            } else if (GenUtils.chance(random, 1, 20)) {
               floor.setType(Material.CALCITE);
               floor.getUp().LPillar(2 * caveHeight, new Random(), Material.CALCITE);
            }
         }

         for(SimpleBlock target = floor; target.getY() != ceil.getY(); target = target.getUp()) {
            BlockFace[] var8 = BlockUtils.directBlockFaces;
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               BlockFace face = var8[var10];
               SimpleBlock rel = target.getRelative(face);
               if (rel.getType() != Material.CALCITE && !BlockUtils.isOre(ceil.getType()) && BlockUtils.isStoneLike(rel.getType())) {
                  rel.setType(Material.AMETHYST_BLOCK);
                  if (BlockUtils.isAir(target.getType()) && GenUtils.chance(random, 1, 3)) {
                     (new MultipleFacingBuilder(Material.GLOW_LICHEN)).setFace(face, true).apply(target);
                  }
               }
            }
         }

         PopulatorDataAbstract d = TerraformGeneratorPlugin.injector.getICAData(ceil.getPopData());
         if (d instanceof PopulatorDataICABiomeWriterAbstract) {
            for(PopulatorDataICABiomeWriterAbstract data = (PopulatorDataICABiomeWriterAbstract)d; floor.getY() < ceil.getY(); floor = floor.getUp()) {
               data.setBiome(floor.getX(), floor.getY(), floor.getZ(), CustomBiomeType.CRYSTALLINE_CLUSTER, Biome.DRIPSTONE_CAVES);
            }
         }

      }
   }
}
