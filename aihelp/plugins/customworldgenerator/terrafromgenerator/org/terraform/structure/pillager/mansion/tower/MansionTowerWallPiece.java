package org.terraform.structure.pillager.mansion.tower;

import java.util.Random;
import java.util.AbstractMap.SimpleEntry;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.Wall;
import org.terraform.structure.pillager.mansion.MansionJigsawBuilder;
import org.terraform.structure.room.jigsaw.JigsawStructurePiece;
import org.terraform.structure.room.jigsaw.JigsawType;

public class MansionTowerWallPiece extends JigsawStructurePiece {
   public boolean isTentRoofFace = false;

   public MansionTowerWallPiece(MansionJigsawBuilder builder, int widthX, int height, int widthZ, JigsawType type, BlockFace[] validDirs) {
      super(widthX, height, widthZ, type, validDirs);
   }

   public void build(@NotNull PopulatorDataAbstract data, @NotNull Random rand) {
      SimpleEntry<Wall, Integer> entry = this.getRoom().getWall(data, this.getRotation().getOppositeFace(), 0);
      Wall w = ((Wall)entry.getKey()).getDown();

      for(int i = 0; i < (Integer)entry.getValue(); ++i) {
         w.getUp().Pillar(this.getRoom().getHeight(), rand, new Material[]{Material.DARK_OAK_PLANKS});
         w = w.getLeft();
      }

   }
}
