package org.terraform.structure.village.plains.forge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.room.jigsaw.JigsawType;
import org.terraform.structure.village.plains.PlainsVillagePopulator;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.blockdata.DirectionalBuilder;
import org.terraform.utils.blockdata.StairBuilder;

public class PlainsVillageForgeChimneyPiece extends PlainsVillageForgeStandardPiece {
   public PlainsVillageForgeChimneyPiece(PlainsVillagePopulator plainsVillagePopulator, int widthX, int height, int widthZ, JigsawType type, BlockFace[] validDirs) {
      super(plainsVillagePopulator, widthX, height, widthZ, type, validDirs);
   }

   public void postBuildDecoration(@NotNull Random random, @NotNull PopulatorDataAbstract data) {
      SimpleBlock core = new SimpleBlock(data, this.getRoom().getX(), this.getRoom().getY(), this.getRoom().getZ());
      if (this.getWalledFaces().isEmpty()) {
         this.spawnStraightChimney(random, new Wall(core));
      }

      if (this.getWalledFaces().size() == 1 && core.getRelative((BlockFace)this.getWalledFaces().get(0), 3).getType() == Material.CHISELED_STONE_BRICKS) {
         this.spawnStraightChimney(random, new Wall(core));
      } else {
         ArrayList<BlockFace> walledFaces = this.getWalledFaces();
         Collections.shuffle(walledFaces);
         Iterator var5 = walledFaces.iterator();

         BlockFace face;
         do {
            if (!var5.hasNext()) {
               return;
            }

            face = (BlockFace)var5.next();
         } while(core.getRelative(face, 3).getType() == Material.CHISELED_STONE_BRICKS);

         Wall target = new Wall(core, face.getOppositeFace());
         this.spawnWallChimney(random, target.getRear(2));
      }
   }

   private void spawnWallChimney(@NotNull Random random, Wall core) {
      core = core.getUp();
      int chimneyCoreHeight = random.nextInt(3) + 5;
      BlockFace[] var4 = BlockUtils.xzDiagonalPlaneBlockFaces;
      int var5 = var4.length;

      int var6;
      BlockFace face;
      for(var6 = 0; var6 < var5; ++var6) {
         face = var4[var6];
         Wall target = core.getRelative(face);
         target.Pillar(chimneyCoreHeight + 1, true, random, new Material[]{Material.COBBLESTONE_WALL, Material.COBBLESTONE});
         target.CorrectMultipleFacing(chimneyCoreHeight + 1);
      }

      var4 = BlockUtils.directBlockFaces;
      var5 = var4.length;

      for(var6 = 0; var6 < var5; ++var6) {
         face = var4[var6];
         core.getRelative(face).setType(Material.COBBLESTONE);

         for(int i = 0; i < chimneyCoreHeight; ++i) {
            if (i % 2 == 0) {
               (new StairBuilder(new Material[]{Material.COBBLESTONE_STAIRS, Material.MOSSY_COBBLESTONE_STAIRS})).setFacing(face.getOppositeFace()).apply(core.getRelative(0, 2 + i, 0).getRelative(face));
            } else {
               core.getRelative(0, 2 + i, 0).getRelative(face).setType(new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
            }
         }

         if (face == core.getDirection()) {
            (new StairBuilder(Material.COBBLESTONE_STAIRS)).setFacing(face.getOppositeFace()).apply(core.getFront(2).getLeft()).apply(core.getFront(2).getRight());
            core.getFront().getLeft().setType(Material.COBBLESTONE);
            core.getFront().getRight().setType(Material.COBBLESTONE);
         } else if (face != core.getDirection().getOppositeFace()) {
            (new StairBuilder(Material.COBBLESTONE_STAIRS)).setFacing(face.getOppositeFace()).apply(core.getRelative(face, 2));
         } else {
            (new StairBuilder(Material.COBBLESTONE_STAIRS)).setFacing(face.getOppositeFace()).apply(core.getRelative(face, 2)).setHalf(Half.TOP).apply(core.getUp(2).getRelative(face, 2));
            core.getRelative(face, 2).getUp(3).Pillar(2, random, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
            core.getRelative(face, 2).getLeft().getDown().Pillar(6, random, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
            core.getRelative(face, 2).getRight().getDown().Pillar(6, random, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
            (new StairBuilder(Material.COBBLESTONE_STAIRS)).setFacing(BlockUtils.getLeft(face)).apply(core.getRelative(face, 2).getUp(4).getLeft()).setFacing(BlockUtils.getRight(face)).apply(core.getRelative(face, 2).getUp(4).getRight());
            core.getRelative(face, 2).getUp(5).setType(new Material[]{Material.COBBLESTONE_SLAB, Material.MOSSY_COBBLESTONE_SLAB});
            core.getRelative(face, 2).getDown(2).getLeft().downUntilSolid(random, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
            core.getRelative(face, 2).getDown().downUntilSolid(random, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
            core.getRelative(face, 2).getDown(2).getRight().downUntilSolid(random, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
            core.getRelative(face).Pillar(6, random, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
            core.getRelative(face).getLeft().Pillar(6, random, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
            core.getRelative(face).getRight().Pillar(6, random, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
         }

         core.getUp().getRelative(face).setType(Material.IRON_BARS);
         core.getUp().getRelative(face).CorrectMultipleFacing(1);
      }

      (new DirectionalBuilder(Material.BLAST_FURNACE)).setFacing(core.getDirection()).apply(core.getFront());
      core.Pillar(chimneyCoreHeight + 2, random, new Material[]{Material.AIR});
      core.getDown().setType(Material.CAMPFIRE);
      core.getDown(2).setType(Material.HAY_BLOCK);
      core.setType(Material.LAVA);
   }

   private void spawnStraightChimney(@NotNull Random random, Wall core) {
      core = core.getUp();
      int chimneyCoreHeight = random.nextInt(3) + 6;
      BlockFace[] var4 = BlockUtils.directBlockFaces;
      int var5 = var4.length;

      int var6;
      BlockFace face;
      for(var6 = 0; var6 < var5; ++var6) {
         face = var4[var6];
         core.getRelative(face).setType(Material.COBBLESTONE);
         core.getUp().getRelative(face).setType(Material.IRON_BARS);

         for(int i = 0; i < chimneyCoreHeight; ++i) {
            if (i % 2 == 0) {
               (new StairBuilder(new Material[]{Material.COBBLESTONE_STAIRS, Material.MOSSY_COBBLESTONE_STAIRS})).setFacing(face.getOppositeFace()).apply(core.getRelative(0, 2 + i, 0).getRelative(face));
            } else {
               core.getRelative(0, 2 + i, 0).getRelative(face).setType(new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
            }
         }
      }

      var4 = BlockUtils.xzDiagonalPlaneBlockFaces;
      var5 = var4.length;

      for(var6 = 0; var6 < var5; ++var6) {
         face = var4[var6];
         Wall target = core.getRelative(face);
         target.Pillar(chimneyCoreHeight + 1, true, random, new Material[]{Material.COBBLESTONE_WALL, Material.COBBLESTONE});
         target.CorrectMultipleFacing(chimneyCoreHeight + 1);
      }

      BlockFace blastFurnaceDir = BlockUtils.getDirectBlockFace(random);
      (new DirectionalBuilder(Material.BLAST_FURNACE)).setFacing(blastFurnaceDir).apply(core.getRelative(blastFurnaceDir));
      core.Pillar(chimneyCoreHeight + 2, random, new Material[]{Material.AIR});
      core.getDown().setType(Material.CAMPFIRE);
      core.getDown(2).setType(Material.HAY_BLOCK);
      core.setType(Material.LAVA);
   }
}
