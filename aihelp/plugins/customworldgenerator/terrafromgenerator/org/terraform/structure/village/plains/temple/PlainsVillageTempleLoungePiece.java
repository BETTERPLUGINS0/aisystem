package org.terraform.structure.village.plains.temple;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.main.config.TConfig;
import org.terraform.structure.room.jigsaw.JigsawType;
import org.terraform.structure.village.plains.PlainsVillagePopulator;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.blockdata.StairBuilder;

public class PlainsVillageTempleLoungePiece extends PlainsVillageTempleStandardPiece {
   private static final Material[] stairTypes;

   public PlainsVillageTempleLoungePiece(PlainsVillagePopulator plainsVillagePopulator, int widthX, int height, int widthZ, JigsawType type, BlockFace[] validDirs) {
      super(plainsVillagePopulator, widthX, height, widthZ, type, validDirs);
   }

   public void postBuildDecoration(@NotNull Random random, @NotNull PopulatorDataAbstract data) {
      super.postBuildDecoration(random, data);
      Material stairType = stairTypes[random.nextInt(stairTypes.length)];
      SimpleBlock core = new SimpleBlock(data, this.getRoom().getX(), this.getRoom().getY() + 1, this.getRoom().getZ());
      BlockFace[] var5 = BlockUtils.getRandomBlockfaceAxis(random);
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         BlockFace face = var5[var7];
         (new StairBuilder(stairType)).setFacing(face).apply(core.getRelative(face).getRelative(BlockUtils.getAdjacentFaces(face)[0])).apply(core.getRelative(face).getRelative(BlockUtils.getAdjacentFaces(face)[1]));
      }

      if (TConfig.areDecorationsEnabled()) {
         core.setType(this.plainsVillagePopulator.woodLog, Material.CRAFTING_TABLE, this.plainsVillagePopulator.woodPlank);
         if (TConfig.arePlantsEnabled() && !random.nextBoolean()) {
            BlockUtils.pickPottedPlant().build(core.getUp());
         } else {
            core.getUp().setType(Material.LANTERN);
         }
      }

   }

   static {
      stairTypes = new Material[]{Material.POLISHED_GRANITE_STAIRS, Material.POLISHED_ANDESITE_STAIRS, Material.POLISHED_DIORITE_STAIRS};
   }
}
