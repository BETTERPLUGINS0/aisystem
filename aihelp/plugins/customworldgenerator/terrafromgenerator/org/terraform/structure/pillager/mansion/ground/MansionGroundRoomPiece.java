package org.terraform.structure.pillager.mansion.ground;

import org.bukkit.block.BlockFace;
import org.terraform.structure.room.jigsaw.JigsawType;

public class MansionGroundRoomPiece extends MansionStandardGroundRoomPiece {
   public MansionGroundRoomPiece(int widthX, int height, int widthZ, JigsawType type, BlockFace[] validDirs) {
      super(widthX, height, widthZ, type, validDirs);
   }
}
