package org.terraform.structure.village.plains.temple;

import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.room.jigsaw.JigsawStructurePiece;
import org.terraform.structure.room.jigsaw.JigsawType;
import org.terraform.structure.village.plains.PlainsVillagePopulator;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.CoralGenerator;
import org.terraform.utils.blockdata.StairBuilder;

public class PlainsVillageTempleClericAltarPiece extends PlainsVillageTempleStandardPiece {
   private static final Material[] stairTypes;
   PlainsVillageTempleJigsawBuilder builder;

   public PlainsVillageTempleClericAltarPiece(PlainsVillagePopulator plainsVillagePopulator, int widthX, int height, int widthZ, JigsawType type, boolean unique, PlainsVillageTempleJigsawBuilder builder, BlockFace[] validDirs) {
      super(plainsVillagePopulator, widthX, height, widthZ, type, unique, validDirs);
      this.builder = builder;
   }

   @NotNull
   public JigsawStructurePiece getInstance(@NotNull Random rand, int depth) {
      PlainsVillageTempleClericAltarPiece p = (PlainsVillageTempleClericAltarPiece)super.getInstance(rand, depth);
      p.builder = this.builder;
      return p;
   }

   public void postBuildDecoration(@NotNull Random random, @NotNull PopulatorDataAbstract data) {
      super.postBuildDecoration(random, data);
      Material stairType = stairTypes[random.nextInt(stairTypes.length)];
      SimpleBlock core = new SimpleBlock(data, this.getRoom().getX(), this.getRoom().getY() + 1, this.getRoom().getZ());
      BlockFace dir = this.builder.getEntranceDirection();
      if (dir == null) {
         dir = BlockUtils.getDirectBlockFace(random);
      }

      core.setType(Material.CHISELED_STONE_BRICKS);
      core.getUp().setType(Material.BREWING_STAND);
      Iterator var6 = this.getRoom().getFourWalls(data, 0).entrySet().iterator();

      while(var6.hasNext()) {
         Entry<Wall, Integer> entry = (Entry)var6.next();
         Wall w = ((Wall)entry.getKey()).getDown();

         for(int i = 0; i < (Integer)entry.getValue(); ++i) {
            w.getUp().setType(Material.AIR);
            (new StairBuilder(stairType)).setFacing(w.getDirection().getOppositeFace()).setWaterlogged(true).apply(w);
            if (!Tag.STAIRS.isTagged(w.getFront().getType())) {
               w.getFront().getUp().setType(Material.AIR);
               w.getFront().setType(Material.WATER);
               w.getFront().getDown().setType(Material.CHISELED_STONE_BRICKS);
               if (random.nextBoolean()) {
                  w.getFront().setType(CoralGenerator.CORAL_FANS);
               }
            }

            w = w.getLeft();
         }
      }

      (new StairBuilder(new Material[]{Material.COBBLESTONE_STAIRS, Material.STONE_BRICK_STAIRS})).setFacing(dir.getOppositeFace()).apply(core.getRelative(dir));
      core.getDown().setType(Material.STONE_BRICKS);
      core.getRelative(dir).getDown().setType(Material.STONE_BRICKS);
      core.getRelative(dir, 2).getDown().setType(Material.STONE_BRICKS);
      int[][] var10 = this.getRoom().getAllCorners();
      int var11 = var10.length;

      for(int var12 = 0; var12 < var11; ++var12) {
         int[] corner = var10[var12];
         data.setType(corner[0], this.getRoom().getY(), corner[1], Material.CHISELED_STONE_BRICKS);
         data.setType(corner[0], this.getRoom().getY() + 1, corner[1], Material.LANTERN);
      }

   }

   static {
      stairTypes = new Material[]{Material.POLISHED_GRANITE_STAIRS, Material.POLISHED_ANDESITE_STAIRS, Material.POLISHED_DIORITE_STAIRS, Material.STONE_BRICK_STAIRS};
   }
}
