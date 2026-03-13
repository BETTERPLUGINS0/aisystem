package org.terraform.structure.pillager.mansion.ground;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.AbstractMap.SimpleEntry;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.pillager.mansion.MansionJigsawBuilder;
import org.terraform.structure.room.jigsaw.JigsawStructurePiece;
import org.terraform.structure.room.jigsaw.JigsawType;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.blockdata.DirectionalBuilder;
import org.terraform.utils.blockdata.OrientableBuilder;
import org.terraform.utils.blockdata.StairBuilder;

public class MansionEntrancePiece extends JigsawStructurePiece {
   final MansionJigsawBuilder builder;

   public MansionEntrancePiece(MansionJigsawBuilder builder, int widthX, int height, int widthZ, JigsawType type, BlockFace[] validDirs) {
      super(widthX, height, widthZ, type, validDirs);
      this.builder = builder;
   }

   public void build(@NotNull PopulatorDataAbstract data, @NotNull Random rand) {
      SimpleEntry<Wall, Integer> entry = this.getRoom().getWall(data, this.getRotation().getOppositeFace(), 0);
      Wall w = ((Wall)entry.getKey()).getDown();

      for(int i = 0; i < (Integer)entry.getValue(); ++i) {
         w.getDown().downUntilSolid(rand, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
         w.Pillar(1, rand, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
         w.getUp().Pillar(this.getRoom().getHeight(), rand, new Material[]{Material.DARK_OAK_PLANKS});
         w = w.getLeft();
      }

      w = w.getRight(5).getUp();
      w.getRight().Pillar(2, new Random(), new Material[]{Material.AIR});
      w.Pillar(3, new Random(), new Material[]{Material.AIR});
      w.getLeft().Pillar(2, new Random(), new Material[]{Material.AIR});
      w.getFront().getRight(3).Pillar(3, new Random(), new Material[]{Material.DARK_OAK_LOG});
      w.getFront().getLeft(3).Pillar(3, new Random(), new Material[]{Material.DARK_OAK_LOG});
      (new StairBuilder(Material.DARK_OAK_STAIRS)).setHalf(Half.TOP).setFacing(BlockUtils.getRight(w.getDirection())).apply(w.getFront().getRight(2).getUp(2)).apply(w.getFront().getRight(1).getUp(3));
      (new StairBuilder(Material.DARK_OAK_STAIRS)).setHalf(Half.TOP).setFacing(BlockUtils.getLeft(w.getDirection())).apply(w.getFront().getLeft(2).getUp(2)).apply(w.getFront().getLeft(1).getUp(3));
      (new OrientableBuilder(Material.DARK_OAK_LOG)).setAxis(BlockUtils.getAxisFromBlockFace(w.getDirection())).apply(w.getUp(4).getFront()).apply(w.getUp(3).getFront().getLeft(2)).apply(w.getUp(3).getFront().getRight(2));
      (new DirectionalBuilder(Material.STONE_BUTTON)).setFacing(w.getDirection()).apply(w.getUp(4).getFront(2)).apply(w.getUp(3).getFront(2).getLeft(2)).apply(w.getUp(3).getFront(2).getRight(2));
      (new OrientableBuilder(Material.DARK_OAK_LOG)).setAxis(BlockUtils.getAxisFromBlockFace(BlockUtils.getRight(w.getDirection()))).apply(w.getUp(4).getFront().getLeft()).apply(w.getUp(4).getFront().getRight());
      (new StairBuilder(Material.DARK_OAK_STAIRS)).setHalf(Half.BOTTOM).setFacing(BlockUtils.getLeft(w.getDirection())).apply(w.getFront().getRight(3).getUp(3)).apply(w.getFront().getRight(2).getUp(4));
      (new StairBuilder(Material.DARK_OAK_STAIRS)).setHalf(Half.BOTTOM).setFacing(BlockUtils.getRight(w.getDirection())).apply(w.getFront().getLeft(3).getUp(3)).apply(w.getFront().getLeft(2).getUp(4));
      (new StairBuilder(Material.DARK_OAK_STAIRS)).setHalf(Half.TOP).setFacing(w.getDirection().getOppositeFace()).apply(w.getUp(3));
      (new StairBuilder(Material.DARK_OAK_STAIRS)).setHalf(Half.TOP).setFacing(BlockUtils.getRight(w.getDirection())).apply(w.getRight().getUp(2));
      (new StairBuilder(Material.DARK_OAK_STAIRS)).setHalf(Half.TOP).setFacing(BlockUtils.getLeft(w.getDirection())).apply(w.getLeft().getUp(2));
      w.getFront(2).getRight(3).setType(Material.COBBLESTONE);
      w.getFront(2).getLeft(3).setType(Material.COBBLESTONE);
      w.getFront(2).getRight(3).getUp().setType(Material.COBBLESTONE_WALL);
      w.getFront(2).getLeft(3).getUp().setType(Material.COBBLESTONE_WALL);
      w.getFront(2).getRight(3).getUp(2).setType(Material.LANTERN);
      w.getFront(2).getLeft(3).getUp(2).setType(Material.LANTERN);
      w = new Wall(new SimpleBlock(data, this.getRoom().getX(), this.getRoom().getY(), this.getRoom().getZ()), w.getDirection());
      ArrayList<BlockFace> directions = new ArrayList();
      Iterator var6 = this.builder.getOverlapperPieces().iterator();

      while(var6.hasNext()) {
         JigsawStructurePiece p = (JigsawStructurePiece)var6.next();
         if (p.getRoom().getSimpleLocation().equals(this.getRoom().getSimpleLocation())) {
            directions.add(p.getRotation());
         }
      }

      if (directions.size() == 3) {
         var6 = directions.iterator();

         while(var6.hasNext()) {
            BlockFace face = (BlockFace)var6.next();
            if (!directions.contains(face.getOppositeFace())) {
               w = new Wall(w.get(), face);
               w = w.getFront(4);
            }
         }
      } else {
         w = w.getRear(3);
      }

      int radius = 10;
      int radiusSquared = radius * radius;

      for(int nx = -radius; nx <= radius; ++nx) {
         for(int nz = -radius; nz <= radius; ++nz) {
            Wall rel = w.getRelative(nx, 0, nz);
            if (rel.get().distanceSquared(w.get()) < (double)radiusSquared) {
               if (rel.getType() != Material.STONE_BRICKS) {
                  rel.setType(Material.COBBLESTONE);
               }

               rel.getDown().downUntilSolid(new Random(), new Material[]{Material.COBBLESTONE});
               rel.getRelative(0, this.getRoom().getHeight() + 1, 0).setType(Material.STONE_BRICKS);
            }
         }
      }

      Wall stairway = w.getFront(10);
      BlockUtils.stairwayUntilSolid(stairway.get(), stairway.getDirection(), new Material[]{Material.COBBLESTONE}, Material.COBBLESTONE_STAIRS);
      BlockUtils.stairwayUntilSolid(stairway.getLeft().get(), stairway.getDirection(), new Material[]{Material.COBBLESTONE}, Material.COBBLESTONE_STAIRS);
      BlockUtils.stairwayUntilSolid(stairway.getRight().get(), stairway.getDirection(), new Material[]{Material.COBBLESTONE}, Material.COBBLESTONE_STAIRS);
      BlockUtils.stairwayUntilSolid(stairway.getLeft(2).get(), stairway.getDirection(), new Material[]{Material.COBBLESTONE}, Material.COBBLESTONE_STAIRS);
      BlockUtils.stairwayUntilSolid(stairway.getRight(2).get(), stairway.getDirection(), new Material[]{Material.COBBLESTONE}, Material.COBBLESTONE_STAIRS);
      Wall ceilingCenter = stairway.getRear().getRelative(0, 8, 0);
      ceilingCenter.setType(Material.POLISHED_DIORITE);
      (new StairBuilder(Material.COBBLESTONE_STAIRS)).setHalf(Half.TOP).setFacing(BlockUtils.getLeft(ceilingCenter.getDirection())).apply(ceilingCenter.getLeft()).setFacing(BlockUtils.getRight(ceilingCenter.getDirection())).apply(ceilingCenter.getRight()).setFacing(ceilingCenter.getDirection().getOppositeFace()).apply(ceilingCenter.getDown());
      ceilingCenter.getUp().setType(Material.COBBLESTONE_SLAB);

      for(int i = 1; i <= 3; ++i) {
         ceilingCenter.getUp().getLeft(i).setType(Material.STONE_BRICK_WALL);
         ceilingCenter.getUp().getLeft(i).CorrectMultipleFacing(1);
         ceilingCenter.getUp().getRight(i).setType(Material.STONE_BRICK_WALL);
         ceilingCenter.getUp().getRight(i).CorrectMultipleFacing(1);
      }

      BlockFace[] var24 = BlockUtils.getAdjacentFaces(stairway.getDirection());
      int var11 = var24.length;

      for(int var12 = 0; var12 < var11; ++var12) {
         BlockFace face = var24[var12];
         Wall target = stairway.getRear().getRelative(face, 3);

         for(int i = 0; i < 6; ++i) {
            int maxRecursion;
            for(maxRecursion = 3; maxRecursion > 0 && target.getType() != Material.COBBLESTONE; --maxRecursion) {
               target = target.getRear();
            }

            if (maxRecursion <= 0) {
               break;
            }

            if (i > 1) {
               target.getRelative(0, 9, 0).setType(Material.COBBLESTONE_WALL);
               target.getRelative(0, 9, 0).CorrectMultipleFacing(1);
            }

            if (i % 2 != 0 && i <= 4) {
               target.getRear().Pillar(8, new Material[]{Material.DARK_OAK_LOG});
               target.getUp().Pillar(7, new Material[]{Material.COBBLESTONE_WALL});
               target.getUp().CorrectMultipleFacing(7);
               target.getUp(5).Pillar(5, new Material[]{Material.COBBLESTONE});
               (new StairBuilder(Material.STONE_BRICK_STAIRS)).setFacing(face).setHalf(Half.TOP).apply(target.getRear().getRelative(0, 6, 0).getRelative(face.getOppositeFace())).apply(target.getRear().getRelative(0, 7, 0).getRelative(face.getOppositeFace())).apply(target.getRear().getRelative(0, 7, 0).getRelative(face.getOppositeFace(), 2));
               (new StairBuilder(Material.COBBLESTONE_STAIRS)).setFacing(face).setHalf(Half.TOP).apply(target.getRelative(0, 7, 0).getRelative(face.getOppositeFace()));
            } else if (i != 0 && i <= 4) {
               target.getRear().Pillar(2, new Material[]{Material.STONE_BRICKS});
            } else {
               target.Pillar(2, new Material[]{Material.STONE_BRICKS});
            }

            target = target.getRelative(face);
         }
      }

   }
}
