package org.terraform.structure.village.plains.forge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.Wall;
import org.terraform.structure.room.jigsaw.JigsawType;
import org.terraform.structure.village.plains.PlainsVillagePopulator;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.blockdata.ChestBuilder;
import org.terraform.utils.blockdata.DirectionalBuilder;

public class PlainsVillageForgeWeaponSmithPiece extends PlainsVillageForgeStandardPiece {
   public PlainsVillageForgeWeaponSmithPiece(PlainsVillagePopulator plainsVillagePopulator, int widthX, int height, int widthZ, JigsawType type, BlockFace[] validDirs) {
      super(plainsVillagePopulator, widthX, height, widthZ, type, validDirs);
   }

   public void postBuildDecoration(@NotNull Random random, @NotNull PopulatorDataAbstract data) {
      if (!this.getWalledFaces().isEmpty()) {
         ArrayList<BlockFace> walledFaces = this.getWalledFaces();
         Collections.shuffle(walledFaces);
         boolean placedJobBlock = false;
         Iterator var5 = walledFaces.iterator();

         while(var5.hasNext()) {
            BlockFace face = (BlockFace)var5.next();
            Entry<Wall, Integer> entry = this.getRoom().getWall(data, face, 0);
            Wall w = (Wall)entry.getKey();

            for(int i = 0; i < (Integer)entry.getValue(); ++i) {
               if (w.getRear().getDown().getType() != Material.CHISELED_STONE_BRICKS) {
                  int choice = random.nextInt(4);
                  switch(choice) {
                  case 0:
                     if (!placedJobBlock) {
                        placedJobBlock = true;
                        w.setType(new Material[]{Material.SMITHING_TABLE, Material.FLETCHING_TABLE});
                     }
                     break;
                  case 1:
                     if (!GenUtils.chance(random, 3, 4)) {
                        w.setType(Material.CRAFTING_TABLE);
                        w.getUp().setType(Material.LANTERN);
                        if (w.getUp().getRear().isSolid() && !placedJobBlock) {
                           placedJobBlock = true;
                           (new DirectionalBuilder(Material.GRINDSTONE)).setFacing(w.getDirection()).apply(w.getUp());
                        }
                     }
                     break;
                  case 2:
                     if (!GenUtils.chance(random, 3, 4)) {
                        (new DirectionalBuilder(new Material[]{Material.ANVIL, Material.CHIPPED_ANVIL, Material.DAMAGED_ANVIL})).setFacing(BlockUtils.getLeft(w.getDirection())).apply(w);
                     }
                     break;
                  case 3:
                     if (GenUtils.chance(random, 1, 4)) {
                        (new ChestBuilder(Material.CHEST)).setFacing(w.getDirection()).setLootTable(TerraLootTable.VILLAGE_ARMORER).apply(w);
                     }
                  }
               }

               w = w.getLeft();
            }
         }

      }
   }
}
