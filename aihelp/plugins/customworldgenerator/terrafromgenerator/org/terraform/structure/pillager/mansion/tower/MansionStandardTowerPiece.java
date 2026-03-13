package org.terraform.structure.pillager.mansion.tower;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Slab.Type;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.pillager.mansion.MansionJigsawBuilder;
import org.terraform.structure.room.jigsaw.JigsawStructurePiece;
import org.terraform.structure.room.jigsaw.JigsawType;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.blockdata.SlabBuilder;
import org.terraform.utils.blockdata.StairBuilder;

public class MansionStandardTowerPiece extends JigsawStructurePiece {
   private final MansionJigsawBuilder builder;
   boolean isHighestPieceInTower = false;

   public MansionStandardTowerPiece(MansionJigsawBuilder builder, int widthX, int height, int widthZ, JigsawType type, BlockFace[] validDirs) {
      super(widthX, height, widthZ, type, validDirs);
      this.builder = builder;
   }

   public void build(@NotNull PopulatorDataAbstract data, Random rand) {
      int[] lowerCorner = this.getRoom().getLowerCorner(0);
      int[] upperCorner = this.getRoom().getUpperCorner(0);

      for(int x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
         for(int z = lowerCorner[1]; z <= upperCorner[1]; ++z) {
            data.setType(x, this.getRoom().getY(), z, Material.DARK_OAK_PLANKS);
         }
      }

   }

   public void decorateAwkwardCorners(Random random) {
      SimpleBlock core = new SimpleBlock(this.builder.getCore().getPopData(), this.getRoom().getX(), this.getRoom().getY(), this.getRoom().getZ());
      Wall target;
      if (this.getWalledFaces().contains(BlockFace.NORTH) && this.getWalledFaces().contains(BlockFace.WEST)) {
         target = new Wall(core.getRelative(-4, 1, -4));
         this.decorateAwkwardCorner(target, random, BlockFace.NORTH, BlockFace.WEST);
      }

      if (this.getWalledFaces().contains(BlockFace.NORTH) && this.getWalledFaces().contains(BlockFace.EAST)) {
         target = new Wall(core.getRelative(4, 1, -4));
         this.decorateAwkwardCorner(target, random, BlockFace.NORTH, BlockFace.EAST);
      }

      if (this.getWalledFaces().contains(BlockFace.SOUTH) && this.getWalledFaces().contains(BlockFace.WEST)) {
         target = new Wall(core.getRelative(-4, 1, 4));
         this.decorateAwkwardCorner(target, random, BlockFace.SOUTH, BlockFace.WEST);
      }

      if (this.getWalledFaces().contains(BlockFace.SOUTH) && this.getWalledFaces().contains(BlockFace.EAST)) {
         target = new Wall(core.getRelative(4, 1, 4));
         this.decorateAwkwardCorner(target, random, BlockFace.SOUTH, BlockFace.EAST);
      }

   }

   public void decorateAwkwardCorner(@NotNull Wall target, Random random, BlockFace one, BlockFace two) {
      target.Pillar(7, new Material[]{Material.STONE_BRICKS});
   }

   public void placeTentRoof(@NotNull PopulatorDataAbstract data, BlockFace roofFacing, Random random) {
      Wall core = (new Wall(this.getRoom().getCenterSimpleBlock(data).getRelative(0, 7, 0), roofFacing)).getFront(5);
      BlockFace[] var5 = BlockUtils.getAdjacentFaces(core.getDirection());
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         BlockFace dir = var5[var7];

         for(int i = 0; i <= 6; ++i) {
            for(int depth = 0; depth < 11; ++depth) {
               Wall w = core.getRear(depth).getRelative(dir, i);
               if (i == 0) {
                  w.getUp(5).Pillar(2, new Material[]{Material.COBBLESTONE});
                  w.getUp(7).setType(Material.COBBLESTONE_WALL);
                  w.getUp(7).CorrectMultipleFacing(1);
               } else if (i == 1) {
                  (new StairBuilder(this.getStairs(i, depth))).setFacing(dir.getOppositeFace()).apply(w.getUp(5));
                  w.getUp(3).Pillar(2, new Material[]{this.getBlock(i, depth)});
               } else if (i == 2) {
                  (new StairBuilder(this.getStairs(i, depth))).setFacing(dir.getOppositeFace()).apply(w.getUp(3));
                  w.getUp(2).setType(this.getBlock(i, depth));
               } else if (i == 3) {
                  (new StairBuilder(this.getStairs(i, depth))).setFacing(dir.getOppositeFace()).apply(w.getUp(2));
                  (new SlabBuilder(this.getSlab(i, depth))).setType(Type.TOP).apply(w.getUp());
               } else if (i == 4) {
                  w.getUp().setType(this.getBlock(i, depth));
               } else if (i == 5) {
                  w.getUp().setType(this.getSlab(i, depth));
                  (new SlabBuilder(this.getSlab(i, depth))).setType(Type.TOP).apply(w);
               } else {
                  (new SlabBuilder(this.getSlab(i, depth))).setType(Type.TOP).apply(w);
               }

               if (i <= 3 && (depth == 1 || depth == 9)) {
                  w.getUp().setType(Material.DARK_OAK_PLANKS);
                  w.getUp(2).LPillar(4, new Random(), new Material[]{Material.DARK_OAK_PLANKS});
               }
            }
         }
      }

   }

   @NotNull
   private Material getStairs(int i, int depth) {
      return i != 0 && i != 6 && depth != 0 && depth != 10 ? Material.DARK_OAK_STAIRS : Material.COBBLESTONE_STAIRS;
   }

   @NotNull
   private Material getSlab(int i, int depth) {
      return i != 0 && i != 6 && depth != 0 && depth != 10 ? Material.DARK_OAK_SLAB : Material.COBBLESTONE_SLAB;
   }

   @NotNull
   private Material getBlock(int i, int depth) {
      return i != 0 && i != 6 && depth != 0 && depth != 10 ? Material.DARK_OAK_PLANKS : Material.COBBLESTONE;
   }

   public boolean isHighestPieceInTower() {
      return this.isHighestPieceInTower;
   }

   public void setHighestPieceInTower(boolean isHighestPieceInTower) {
      this.isHighestPieceInTower = isHighestPieceInTower;
   }
}
