package org.terraform.structure.small;

import java.util.Random;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.small_items.PlantBuilder;
import org.terraform.tree.FractalTreeBuilder;
import org.terraform.tree.FractalTypes;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class GiantPumpkinPopulator {
   public void populate(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      if (TConfig.areStructuresEnabled()) {
         if (TConfig.c.STRUCTURES_SWAMPHUT_ENABLED) {
            int x = data.getChunkX() * 16 + random.nextInt(16);
            int z = data.getChunkZ() * 16 + random.nextInt(16);
            int height = GenUtils.getHighestGround(data, x, z);
            this.spawnGiantPumpkin(tw, random, data, x, height, z);
         }
      }
   }

   public void spawnGiantPumpkin(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data, int x, int y, int z) {
      (new FractalTreeBuilder(FractalTypes.Tree.GIANT_PUMPKIN)).build(tw, data, x, y + 1, z);

      int i;
      int nx;
      int nz;
      int ny;
      for(i = 0; i < GenUtils.randInt(random, 15, 30); ++i) {
         nx = x + GenUtils.getSign(random) * GenUtils.randInt(5, 12);
         nz = z + GenUtils.getSign(random) * GenUtils.randInt(5, 12);
         ny = GenUtils.getHighestGround(data, nx, nz);
         PlantBuilder.PUMPKIN.build(data, nx, ny + 1, nz);
      }

      for(i = 0; i < GenUtils.randInt(random, 1, 5); ++i) {
         nx = x + GenUtils.getSign(random) * GenUtils.randInt(4, 5);
         nz = z + GenUtils.getSign(random) * GenUtils.randInt(4, 5);
         ny = GenUtils.getHighestGround(data, nx, nz);
         BlockUtils.setPersistentLeaves(data, nx, ny + 1, nz);
      }

      if (TConfig.arePlantsEnabled()) {
         for(i = 0; i < GenUtils.randInt(random, 4, 6); ++i) {
            nx = x + GenUtils.getSign(random) * GenUtils.randInt(4, 6);
            nz = z + GenUtils.getSign(random) * GenUtils.randInt(4, 6);
            ny = GenUtils.getHighestGround(data, nx, nz);
            BlockUtils.replaceSphere(random.nextInt(9992), 3.0F, 6.0F, 3.0F, new SimpleBlock(data, nx, ny, nz), false, Material.ACACIA_LEAVES);
         }
      }

   }
}
