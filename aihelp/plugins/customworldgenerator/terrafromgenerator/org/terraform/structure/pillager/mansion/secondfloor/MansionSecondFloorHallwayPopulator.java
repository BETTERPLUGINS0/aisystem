package org.terraform.structure.pillager.mansion.secondfloor;

import java.util.HashMap;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Slab.Type;
import org.bukkit.block.data.type.Stairs.Shape;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.small_items.PlantBuilder;
import org.terraform.structure.pillager.mansion.MansionInternalWallState;
import org.terraform.structure.pillager.mansion.MansionRoomPopulator;
import org.terraform.structure.pillager.mansion.MansionRoomSize;
import org.terraform.structure.room.CubeRoom;
import org.terraform.utils.BannerUtils;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.PaintingUtils;
import org.terraform.utils.blockdata.DirectionalBuilder;
import org.terraform.utils.blockdata.SlabBuilder;
import org.terraform.utils.blockdata.StairBuilder;
import org.terraform.utils.blockdata.TrapdoorBuilder;

public class MansionSecondFloorHallwayPopulator extends MansionRoomPopulator {
   public MansionSecondFloorHallwayPopulator(CubeRoom room, HashMap<BlockFace, MansionInternalWallState> internalWalls) {
      super(room, internalWalls);
   }

   public void decorateRoom(@NotNull PopulatorDataAbstract data, @NotNull Random random) {
      SimpleBlock center = this.getRoom().getCenterSimpleBlock(data);
      center.getUp().setType(Material.RED_CARPET);
      BlockFace[] var4 = BlockUtils.xzPlaneBlockFaces;
      int var5 = var4.length;

      int var6;
      BlockFace face;
      for(var6 = 0; var6 < var5; ++var6) {
         face = var4[var6];
         center.getUp().getRelative(face).setType(Material.RED_CARPET);
      }

      var4 = BlockUtils.directBlockFaces;
      var5 = var4.length;

      for(var6 = 0; var6 < var5; ++var6) {
         face = var4[var6];
         if (this.getInternalWalls().get(face) != MansionInternalWallState.WINDOW) {
            Wall w = new Wall(center, face);
            if (this.getInternalWalls().get(face) == MansionInternalWallState.SOLID) {
               Wall target = w.getFront(3);
               this.applyHallwaySmoothing(target);
               this.applyHallwaySmoothing(target.getLeft(1));
               this.applyHallwaySmoothing(target.getLeft(2));
               this.applyHallwaySmoothing(target.getLeft(3));
               this.applyHallwaySmoothing(target.getRight(1));
               this.applyHallwaySmoothing(target.getRight(2));
               this.applyHallwaySmoothing(target.getRight(3));
               if (!target.getRight(4).getUp().isSolid()) {
                  this.applyHallwaySmoothing(target.getRight(4));
                  target.getRight(5).Pillar(6, new Material[]{Material.DARK_OAK_PLANKS});
               }

               if (!target.getLeft(4).getUp().isSolid()) {
                  this.applyHallwaySmoothing(target.getLeft(4));
                  target.getLeft(5).Pillar(6, new Material[]{Material.DARK_OAK_PLANKS});
               }

               this.decorateHallwayWall(random, new Wall(target.getRear().getUp().get(), w.getDirection().getOppositeFace()), false);
            } else if (this.getInternalWalls().get(face) == MansionInternalWallState.ROOM_ENTRANCE) {
               for(int length = 2; length < 6; ++length) {
                  Wall target = w.getFront(length).getUp();
                  target.setType(Material.RED_CARPET);
                  if (length < 5) {
                     target.getLeft().setType(Material.RED_CARPET);
                     target.getRight().setType(Material.RED_CARPET);
                  }
               }
            }
         } else {
            center.getUp().getRelative(face, 2).setType(Material.RED_CARPET);
            this.decorateHallwayWall(random, new Wall(center.getRelative(face, 3).getUp(), face.getOppositeFace()), true);
         }
      }

      this.spawnSmallChandelier(center.getUp(6));
   }

   private void decorateHallwayWall(@NotNull Random random, @NotNull Wall center, boolean isWindow) {
      int decorationType = random.nextInt(3);
      if (!isWindow) {
         switch(decorationType) {
         case 0:
            PaintingUtils.placePainting(center.getUp().get(), center.getDirection(), PaintingUtils.getArtFromDimensions(random, 1, 2));
            PaintingUtils.placePainting(center.getRight(2).getUp().get(), center.getDirection(), PaintingUtils.getArtFromDimensions(random, 1, 2));
            PaintingUtils.placePainting(center.getLeft(2).getUp().get(), center.getDirection(), PaintingUtils.getArtFromDimensions(random, 1, 2));
            (new StairBuilder(Material.DARK_OAK_STAIRS)).setFacing(center.getDirection().getOppositeFace()).setHalf(Half.TOP).apply(center.getLeft()).apply(center.getRight());
            center.getLeft().getUp().Pillar(3, new Material[]{Material.DARK_OAK_FENCE});
            center.getLeft().getUp().CorrectMultipleFacing(3);
            center.getRight().getUp().Pillar(3, new Material[]{Material.DARK_OAK_FENCE});
            center.getRight().getUp().CorrectMultipleFacing(3);
            center.getRight().getUp(4).setType(Material.DARK_OAK_PLANKS);
            center.getLeft().getUp(4).setType(Material.DARK_OAK_PLANKS);
            break;
         case 1:
            BannerUtils.generateBanner(random, center.getUp(2).get(), center.getDirection(), true);
            BannerUtils.generateBanner(random, center.getRight(2).getUp(2).get(), center.getDirection(), true);
            BannerUtils.generateBanner(random, center.getLeft(2).getUp(2).get(), center.getDirection(), true);
            center.getLeft().getRear().Pillar(4, new Material[]{Material.DARK_OAK_LOG});
            center.getRight().getRear().Pillar(4, new Material[]{Material.DARK_OAK_LOG});
            (new DirectionalBuilder(Material.WALL_TORCH)).setFacing(center.getDirection()).apply(center.getLeft().getUp(2)).apply(center.getRight().getUp(2));
            break;
         case 2:
            (new StairBuilder(Material.POLISHED_ANDESITE_STAIRS)).setFacing(center.getDirection().getOppositeFace()).apply(center).setFacing(BlockUtils.getLeft(center.getDirection())).setShape(Shape.INNER_RIGHT).apply(center.getLeft()).setFacing(BlockUtils.getRight(center.getDirection())).setShape(Shape.INNER_LEFT).apply(center.getRight());
            center.getLeft(2).setType(Material.DARK_OAK_LOG);
            center.getRight(2).setType(Material.DARK_OAK_LOG);
            if (random.nextBoolean()) {
               center.getLeft(2).getUp().setType(Material.LANTERN);
            }

            if (random.nextBoolean()) {
               center.getRight(2).getUp().setType(Material.LANTERN);
            }
         }
      } else {
         switch(decorationType) {
         case 0:
            (new StairBuilder(Material.POLISHED_ANDESITE_STAIRS)).setFacing(center.getDirection().getOppositeFace()).apply(center).setFacing(BlockUtils.getLeft(center.getDirection())).setShape(Shape.INNER_RIGHT).apply(center.getLeft()).setFacing(BlockUtils.getRight(center.getDirection())).setShape(Shape.INNER_LEFT).apply(center.getRight());
            break;
         case 1:
            center.setType(Material.DARK_OAK_PLANKS);
            center.getLeft().setType(Material.GRASS_BLOCK);
            center.getRight().setType(Material.GRASS_BLOCK);
            center.getLeft().getUp().setType(Material.OAK_FENCE);
            center.getRight().getUp().setType(Material.OAK_FENCE);
            PlantBuilder.OAK_LEAVES.build(center.getLeft().getUp(2));
            PlantBuilder.OAK_LEAVES.build(center.getRight().getUp(2));
            (new TrapdoorBuilder(Material.DARK_OAK_TRAPDOOR)).setFacing(center.getDirection()).setOpen(true).apply(center.getLeft().getFront()).apply(center.getRight().getFront());
            (new TrapdoorBuilder(Material.DARK_OAK_TRAPDOOR)).setFacing(center.getDirection().getOppositeFace()).setOpen(true).apply(center.getLeft().getRear()).apply(center.getRight().getRear());
            (new TrapdoorBuilder(Material.DARK_OAK_TRAPDOOR)).setFacing(BlockUtils.getLeft(center.getDirection())).setOpen(true).apply(center.getLeft(2)).setFacing(BlockUtils.getRight(center.getDirection())).apply(center.getRight(2));
         }
      }

   }

   private void spawnSmallChandelier(@NotNull SimpleBlock target) {
      target.setType(Material.DARK_OAK_FENCE);
      target.getDown().setType(Material.DARK_OAK_FENCE);
      target.getDown(2).setType(Material.DARK_OAK_FENCE);
      target = target.getDown(2);
      BlockFace[] var2 = BlockUtils.directBlockFaces;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         BlockFace face = var2[var4];
         target.getRelative(face).setType(Material.DARK_OAK_FENCE);
         target.getRelative(face).getUp().setType(Material.TORCH);
      }

      BlockUtils.correctSurroundingMultifacingData(target);
   }

   private void applyHallwaySmoothing(@NotNull Wall w) {
      w.Pillar(7, new Material[]{Material.DARK_OAK_PLANKS});
      w = w.getRear();
      (new StairBuilder(Material.DARK_OAK_STAIRS)).setFacing(w.getDirection()).setHalf(Half.TOP).lapply(w.getUp(5));
      w.getUp(6).Pillar(2, new Material[]{Material.DARK_OAK_PLANKS});
      w = w.getRear();
      w.getUp(6).Pillar(2, new Material[]{Material.DARK_OAK_PLANKS});
      w = w.getRear();
      (new SlabBuilder(Material.DARK_OAK_SLAB)).setType(Type.TOP).lapply(w.getUp(6));
      w.getUp(7).setType(Material.DARK_OAK_PLANKS);
      w = w.getRear();
      (new StairBuilder(Material.DARK_OAK_STAIRS)).setFacing(w.getDirection()).setHalf(Half.TOP).lapply(w.getUp(7));
   }

   @NotNull
   public MansionRoomSize getSize() {
      return new MansionRoomSize(1, 1);
   }
}
