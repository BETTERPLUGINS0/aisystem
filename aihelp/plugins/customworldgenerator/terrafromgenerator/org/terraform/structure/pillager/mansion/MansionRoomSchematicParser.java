package org.terraform.structure.pillager.mansion;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.main.config.TConfig;
import org.terraform.schematic.SchematicParser;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class MansionRoomSchematicParser extends SchematicParser {
   protected final Random rand;
   protected final PopulatorDataAbstract pop;

   public MansionRoomSchematicParser(Random rand, PopulatorDataAbstract pop) {
      this.rand = rand;
      this.pop = pop;
   }

   public void applyData(@NotNull SimpleBlock block, @NotNull BlockData data) {
      if (!TConfig.areDecorationsEnabled() || data.getMaterial() != Material.CHEST && data.getMaterial() != Material.BARREL) {
         if (data.getMaterial() == Material.POTTED_POPPY) {
            BlockUtils.pickPottedPlant().build(block);
            return;
         }

         super.applyData(block, data);
      } else {
         if (GenUtils.chance(this.rand, 2, 5)) {
            block.setType(Material.AIR);
            return;
         }

         super.applyData(block, data);
         this.pop.lootTableChest(block.getX(), block.getY(), block.getZ(), TerraLootTable.WOODLAND_MANSION);
      }

   }
}
