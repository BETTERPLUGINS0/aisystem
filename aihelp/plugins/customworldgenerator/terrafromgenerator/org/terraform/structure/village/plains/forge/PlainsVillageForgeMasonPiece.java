package org.terraform.structure.village.plains.forge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.room.jigsaw.JigsawType;
import org.terraform.structure.village.plains.PlainsVillagePopulator;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.blockdata.DirectionalBuilder;

public class PlainsVillageForgeMasonPiece extends PlainsVillageForgeStandardPiece {
   public PlainsVillageForgeMasonPiece(PlainsVillagePopulator plainsVillagePopulator, int widthX, int height, int widthZ, JigsawType type, BlockFace[] validDirs) {
      super(plainsVillagePopulator, widthX, height, widthZ, type, validDirs);
   }

   public void postBuildDecoration(@NotNull Random random, @NotNull PopulatorDataAbstract data) {
      SimpleBlock core = new SimpleBlock(data, this.getRoom().getX(), this.getRoom().getY(), this.getRoom().getZ());
      if (this.getWalledFaces().isEmpty()) {
         this.spawnCenteredPileOfRocks(random, new Wall(core));
      }

      if (this.getWalledFaces().size() == 1 && core.getRelative((BlockFace)this.getWalledFaces().get(0), 3).getType() == Material.CHISELED_STONE_BRICKS) {
         this.spawnCenteredPileOfRocks(random, new Wall(core));
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

         Wall target = new Wall(core, face);
         this.spawnedWalledPileOfRocks(random, target);
      }
   }

   private void spawnCenteredPileOfRocks(@NotNull Random random, Wall core) {
      core = core.getUp();
      Material[] ores = new Material[]{Material.IRON_ORE, Material.COAL_ORE, Material.GOLD_ORE, Material.LAPIS_ORE, Material.ANDESITE, Material.DIORITE, Material.GRANITE, Material.STONE, Material.ANDESITE, Material.DIORITE, Material.GRANITE, Material.STONE, Material.ANDESITE, Material.DIORITE, Material.GRANITE, Material.STONE};
      core.Pillar(random.nextInt(3) + 1, random, ores);
      BlockFace[] var4 = BlockUtils.xzPlaneBlockFaces;
      int i = var4.length;

      for(int var6 = 0; var6 < i; ++var6) {
         BlockFace face = var4[var6];
         core.getRelative(face).Pillar(random.nextInt(3), random, ores);
      }

      Wall target;
      for(target = core.getRelative(BlockUtils.getXZPlaneBlockFace(random)); target.isSolid(); target = target.getUp()) {
      }

      (new DirectionalBuilder(Material.STONECUTTER)).setFacing(BlockUtils.getDirectBlockFace(new Random())).apply(target);

      for(i = 0; i < random.nextInt(2) + 1; ++i) {
         for(target = target.getAtY(core.getY() + 1).getRelative(BlockUtils.getDirectBlockFace(random)); target.isSolid(); target = target.getUp()) {
         }

         if (target.getDown().getType() != Material.LANTERN) {
            target.setType(Material.LANTERN);
         }
      }

   }

   private void spawnedWalledPileOfRocks(@NotNull Random random, Wall core) {
      core = core.getUp();
      Material[] ores = new Material[]{Material.IRON_ORE, Material.COAL_ORE, Material.GOLD_ORE, Material.LAPIS_ORE, Material.ANDESITE, Material.DIORITE, Material.GRANITE, Material.STONE, Material.ANDESITE, Material.DIORITE, Material.GRANITE, Material.STONE, Material.ANDESITE, Material.DIORITE, Material.GRANITE, Material.STONE};
      (new DirectionalBuilder(Material.STONECUTTER)).setFacing(BlockUtils.getDirectBlockFace(new Random())).apply(core);
      core = core.getRelative(core.getDirection(), 2);
      core.Pillar(random.nextInt(3) + 1, random, ores);
      core.getLeft().Pillar(random.nextInt(3) + 1, random, ores);
      core.getRight().Pillar(random.nextInt(3) + 1, random, ores);
      core.getLeft(2).Pillar(random.nextInt(3), random, ores);
      core.getRight(2).Pillar(random.nextInt(3), random, ores);
      core.getRear().Pillar(random.nextInt(3), random, ores);
      core.getRear().getLeft().Pillar(random.nextInt(3), random, ores);
      core.getRear().getRight().Pillar(random.nextInt(3), random, ores);

      for(int i = 0; i < random.nextInt(2) + 1; ++i) {
         Wall target;
         for(target = core.getRelative(core.getDirection().getOppositeFace(), 2).getRelative(BlockUtils.getDirectBlockFace(random)); target.isSolid(); target = target.getUp()) {
         }

         target.setType(Material.LANTERN);
      }

   }
}
