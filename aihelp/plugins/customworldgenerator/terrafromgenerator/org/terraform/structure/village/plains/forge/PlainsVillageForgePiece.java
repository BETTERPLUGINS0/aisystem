package org.terraform.structure.village.plains.forge;

import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.Nullable;
import org.terraform.structure.room.jigsaw.JigsawStructurePiece;
import org.terraform.structure.room.jigsaw.JigsawType;
import org.terraform.structure.village.plains.PlainsVillagePopulator;

public abstract class PlainsVillageForgePiece extends JigsawStructurePiece {
   protected final PlainsVillagePopulator plainsVillagePopulator;
   @Nullable
   private PlainsVillageForgeWallPiece.PlainsVillageForgeWallType wallType = null;

   public PlainsVillageForgePiece(PlainsVillagePopulator plainsVillagePopulator, int widthX, int height, int widthZ, JigsawType type, BlockFace[] validDirs) {
      super(widthX, height, widthZ, type, validDirs);
      this.plainsVillagePopulator = plainsVillagePopulator;
   }

   public PlainsVillageForgePiece(PlainsVillagePopulator plainsVillagePopulator, int widthX, int height, int widthZ, JigsawType type, boolean unique, BlockFace... validDirs) {
      super(widthX, height, widthZ, type, unique, validDirs);
      this.plainsVillagePopulator = plainsVillagePopulator;
   }

   @Nullable
   public PlainsVillageForgeWallPiece.PlainsVillageForgeWallType getWallType() {
      return this.wallType;
   }

   public void setWallType(@Nullable PlainsVillageForgeWallPiece.PlainsVillageForgeWallType wallType) {
      this.wallType = wallType;
   }
}
