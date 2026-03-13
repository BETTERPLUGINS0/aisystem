package org.terraform.structure.ancientcity;

import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.terraform.data.SimpleBlock;
import org.terraform.schematic.SchematicParser;
import org.terraform.utils.version.V_1_19;

public class AncientCityPillarSchematicParser extends SchematicParser {
   @NotNull
   final ArrayList<SimpleBlock> touchedOffsets = new ArrayList();
   private int failCount = 0;
   private int totalCount = 0;

   @NotNull
   public ArrayList<SimpleBlock> getTouchedOffsets() {
      return this.touchedOffsets;
   }

   public float calculateFailRate() {
      return (float)this.failCount / (float)this.totalCount;
   }

   public void applyData(@NotNull SimpleBlock block, @NotNull BlockData data) {
      Random rand = new Random();
      ++this.totalCount;
      if (block.isSolid() && block.getType() != V_1_19.SCULK_VEIN) {
         ++this.failCount;
      } else {
         if (!this.touchedOffsets.isEmpty() && ((SimpleBlock)this.touchedOffsets.get(0)).getY() != block.getY()) {
            if (((SimpleBlock)this.touchedOffsets.get(0)).getY() < block.getY()) {
               this.touchedOffsets.clear();
               this.touchedOffsets.add(block);
            }
         } else {
            this.touchedOffsets.add(block);
         }

         if (data.getMaterial() == Material.DEEPSLATE_TILES) {
            if (rand.nextBoolean()) {
               data = Bukkit.createBlockData(Material.CRACKED_DEEPSLATE_TILES);
            }
         } else if (data.getMaterial() == Material.DEEPSLATE_BRICKS && rand.nextBoolean()) {
            data = Bukkit.createBlockData(Material.CRACKED_DEEPSLATE_BRICKS);
         }

         super.applyData(block, data);
      }
   }
}
