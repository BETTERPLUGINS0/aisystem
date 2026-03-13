package org.terraform.structure.village.plains.temple;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.room.jigsaw.JigsawStructurePiece;
import org.terraform.structure.room.jigsaw.JigsawType;
import org.terraform.structure.village.plains.PlainsVillagePopulator;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class PlainsVillageTempleStandardPiece extends JigsawStructurePiece {
   final PlainsVillagePopulator plainsVillagePopulator;
   private boolean isTower = false;

   public PlainsVillageTempleStandardPiece(PlainsVillagePopulator plainsVillagePopulator, int widthX, int height, int widthZ, JigsawType type, boolean unique, BlockFace[] validDirs) {
      super(widthX, height, widthZ, type, unique, validDirs);
      this.plainsVillagePopulator = plainsVillagePopulator;
   }

   public PlainsVillageTempleStandardPiece(PlainsVillagePopulator plainsVillagePopulator, int widthX, int height, int widthZ, JigsawType type, BlockFace[] validDirs) {
      super(widthX, height, widthZ, type, validDirs);
      this.plainsVillagePopulator = plainsVillagePopulator;
   }

   public void build(@NotNull PopulatorDataAbstract data, @NotNull Random rand) {
      int[] lowerCorner = this.getRoom().getLowerCorner(0);
      int[] upperCorner = this.getRoom().getUpperCorner(0);

      for(int x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
         for(int z = lowerCorner[1]; z <= upperCorner[1]; ++z) {
            data.setType(x, this.getRoom().getY(), z, (Material)GenUtils.randChoice((Object[])(Material.STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS, Material.CRACKED_STONE_BRICKS)));
            (new Wall(new SimpleBlock(data, x, this.getRoom().getY() - 1, z))).downUntilSolid(rand, new Material[]{Material.STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS, Material.CRACKED_STONE_BRICKS});
         }
      }

   }

   public void postBuildDecoration(Random random, @NotNull PopulatorDataAbstract data) {
      int[] lowerCorner = this.getRoom().getLowerCorner(0);
      int[] upperCorner = this.getRoom().getUpperCorner(0);

      for(int x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
         for(int z = lowerCorner[1]; z <= upperCorner[1]; ++z) {
            if (x % 2 == 0 && z % 2 == 0) {
               data.setType(x, this.getRoom().getY(), z, Material.TORCH);
            }

            if (!data.getType(x, this.getRoom().getY() + 1, z).isSolid()) {
               boolean canPlace = true;
               BlockFace[] var8 = BlockUtils.directBlockFaces;
               int var9 = var8.length;

               for(int var10 = 0; var10 < var9; ++var10) {
                  BlockFace face = var8[var10];
                  if (data.getType(x + face.getModX(), this.getRoom().getY() + 1, z + face.getModZ()) == this.plainsVillagePopulator.woodDoor) {
                     canPlace = false;
                     data.setType(x, this.getRoom().getY(), z, Material.WHITE_WOOL);
                  }
               }

               if (canPlace) {
                  data.setType(x, this.getRoom().getY() + 1, z, Material.WHITE_CARPET);
               }
            }
         }
      }

   }

   public boolean isTower() {
      return this.isTower;
   }

   public void setTower(boolean isTower) {
      this.isTower = isTower;
   }
}
