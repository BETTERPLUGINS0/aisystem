package org.terraform.structure.ancientcity;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Candle;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.data.SimpleBlock;
import org.terraform.schematic.SchematicParser;
import org.terraform.utils.GenUtils;

public class AncientCitySchematicParser extends SchematicParser {
   public void applyData(@NotNull SimpleBlock block, @NotNull BlockData data) {
      Random rand = new Random();
      if (((BlockData)data).getMaterial() == Material.DEEPSLATE_TILES) {
         if (rand.nextBoolean()) {
            data = Bukkit.createBlockData(Material.CRACKED_DEEPSLATE_TILES);
         }
      } else if (((BlockData)data).getMaterial() == Material.DEEPSLATE_BRICKS) {
         if (rand.nextBoolean()) {
            data = Bukkit.createBlockData(Material.CRACKED_DEEPSLATE_BRICKS);
         }
      } else if (((BlockData)data).getMaterial() != Material.DARK_OAK_PLANKS && ((BlockData)data).getMaterial() != Material.DARK_OAK_SLAB) {
         if (((BlockData)data).getMaterial() == Material.CANDLE) {
            Candle candle = (Candle)Bukkit.createBlockData(Material.CANDLE);
            candle.setLit(true);
            candle.setCandles(1 + rand.nextInt(4));
            data = candle;
         } else if (((BlockData)data).getMaterial() == Material.CHEST) {
            if (GenUtils.chance(rand, 2, 5)) {
               block.setType(Material.AIR);
               return;
            }

            super.applyData(block, (BlockData)data);
            block.getPopData().lootTableChest(block.getX(), block.getY(), block.getZ(), TerraLootTable.ANCIENT_CITY);
            return;
         }
      } else if (rand.nextBoolean()) {
         data = Bukkit.createBlockData(Material.AIR);
      }

      super.applyData(block, (BlockData)data);
   }
}
