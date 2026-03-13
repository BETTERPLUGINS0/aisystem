package org.terraform.structure.mineshaft;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.terraform.data.SimpleBlock;
import org.terraform.schematic.SchematicParser;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class OreLiftSchematicParser extends SchematicParser {
   boolean isBadlands = false;

   public OreLiftSchematicParser() {
   }

   public OreLiftSchematicParser(boolean isBadlands) {
      this.isBadlands = isBadlands;
   }

   public void applyData(@NotNull SimpleBlock block, @NotNull BlockData data) {
      if (BlockUtils.ores.contains(data.getMaterial())) {
         data = Bukkit.createBlockData((Material)GenUtils.randChoice(BlockUtils.ores));
      }

      if (this.isBadlands && data.getMaterial().toString().contains("OAK")) {
         data = Bukkit.createBlockData(data.getAsString().replaceAll("oak_", "dark_oak_"));
         super.applyData(block, data);
      }

      super.applyData(block, data);
   }
}
