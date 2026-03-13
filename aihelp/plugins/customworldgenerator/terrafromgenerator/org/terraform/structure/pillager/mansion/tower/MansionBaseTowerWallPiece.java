package org.terraform.structure.pillager.mansion.tower;

import java.util.Random;
import java.util.AbstractMap.SimpleEntry;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Slab.Type;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.Wall;
import org.terraform.structure.pillager.mansion.MansionJigsawBuilder;
import org.terraform.structure.room.jigsaw.JigsawType;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.blockdata.OrientableBuilder;
import org.terraform.utils.blockdata.SlabBuilder;
import org.terraform.utils.blockdata.StairBuilder;

public class MansionBaseTowerWallPiece extends MansionTowerWallPiece {
   public MansionBaseTowerWallPiece(MansionJigsawBuilder builder, int widthX, int height, int widthZ, JigsawType type, BlockFace[] validDirs) {
      super(builder, widthX, height, widthZ, type, validDirs);
   }

   public void build(@NotNull PopulatorDataAbstract data, @NotNull Random rand) {
      SimpleEntry<Wall, Integer> entry = this.getRoom().getWall(data, this.getRotation().getOppositeFace(), 0);
      Wall w = ((Wall)entry.getKey()).getDown();

      for(int i = 0; i < (Integer)entry.getValue(); ++i) {
         w.getUp().Pillar(this.getRoom().getHeight(), rand, new Material[]{Material.DARK_OAK_PLANKS});
         (new StairBuilder(Material.COBBLESTONE_STAIRS)).setFacing(w.getDirection().getOppositeFace()).apply(w.getUp().getFront());
         w = w.getLeft();
      }

   }

   public void postBuildDecoration(Random rand, @NotNull PopulatorDataAbstract data) {
      SimpleEntry<Wall, Integer> entry = this.getRoom().getWall(data, this.getRotation().getOppositeFace(), 0);
      Wall w = ((Wall)entry.getKey()).getDown();
      OrientableBuilder logBuilder = (new OrientableBuilder(Material.DARK_OAK_LOG)).setAxis(BlockUtils.getAxisFromBlockFace(BlockUtils.getRight(w.getDirection())));

      for(int i = 0; i < (Integer)entry.getValue(); ++i) {
         w.getRear().getUp().Pillar(6, new Material[]{Material.STONE});
         logBuilder.apply(w.getUp(7));
         if (i != 0 && i != (Integer)entry.getValue() - 1) {
            if (i != 1 && i != (Integer)entry.getValue() - 2) {
               if (i != 2 && i != (Integer)entry.getValue() - 3) {
                  (new SlabBuilder(Material.COBBLESTONE_SLAB)).setType(Type.BOTTOM).apply(w.getUp(7).getFront());
                  w.getUp(5).setType(Material.AIR);
                  (new SlabBuilder(Material.DARK_OAK_SLAB)).setType(Type.TOP).apply(w.getUp(4));
                  w.getUp(3).setType(Material.AIR);
               } else {
                  (new SlabBuilder(Material.COBBLESTONE_SLAB)).setType(Type.TOP).apply(w.getUp(6).getFront());
                  (new SlabBuilder(Material.DARK_OAK_SLAB)).setType(Type.TOP).apply(w.getUp(5));
               }
            } else {
               (new StairBuilder(Material.COBBLESTONE_STAIRS)).setFacing(i > 2 ? BlockUtils.getRight(w.getDirection()) : BlockUtils.getLeft(w.getDirection())).apply(w.getUp(6).getFront());
               w.getUp(3).setType(Material.AIR);
            }
         } else {
            (new SlabBuilder(Material.COBBLESTONE_SLAB)).setType(Type.TOP).apply(w.getUp(5).getFront());
            (new StairBuilder(Material.DARK_OAK_STAIRS)).setHalf(Half.TOP).setFacing(i > 0 ? BlockUtils.getLeft(w.getDirection()) : BlockUtils.getRight(w.getDirection())).apply(w.getUp(3));
         }

         w = w.getLeft();
      }

   }
}
