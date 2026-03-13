package org.terraform.structure.villagehouse.mountainhouse;

import java.util.Locale;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.schematic.SchematicParser;
import org.terraform.utils.GenUtils;

public class MountainhouseSchematicParser extends SchematicParser {
   private final Random rand;
   private final PopulatorDataAbstract pop;

   public MountainhouseSchematicParser(BiomeBank biome, Random rand, PopulatorDataAbstract pop) {
      this.rand = rand;
      this.pop = pop;
   }

   public void applyData(@NotNull SimpleBlock block, @NotNull BlockData data) {
      if (data.getMaterial().toString().contains("COBBLESTONE")) {
         data = Bukkit.createBlockData(data.getAsString().replaceAll("cobblestone", ((Material)GenUtils.randChoice(this.rand, Material.COBBLESTONE, Material.ANDESITE, Material.STONE_BRICKS, Material.CRACKED_STONE_BRICKS, Material.COBBLESTONE, Material.ANDESITE)).toString().toLowerCase(Locale.ENGLISH)));
         super.applyData(block, data);
      }

      if (data.getMaterial() == Material.BRICKS) {
         data = Bukkit.createBlockData(data.getAsString().replaceAll("bricks", ((Material)GenUtils.randChoice(this.rand, Material.BRICKS, Material.GRANITE, Material.POLISHED_GRANITE)).toString().toLowerCase(Locale.ENGLISH)));
         super.applyData(block, data);
      } else if (data.getMaterial().toString().contains("WHITE_CONCRETE")) {
         data = Bukkit.createBlockData(data.getAsString().replaceAll("white_concrete", ((Material)GenUtils.randChoice(this.rand, Material.WHITE_CONCRETE, Material.WHITE_CONCRETE, Material.WHITE_WOOL, Material.DIORITE, Material.DIORITE)).toString().toLowerCase(Locale.ENGLISH)));
         super.applyData(block, data);
      } else if (data.getMaterial() == Material.CHEST) {
         if (GenUtils.chance(this.rand, 1, 5)) {
            block.setType(Material.AIR);
            return;
         }

         super.applyData(block, data);
         this.pop.lootTableChest(block.getX(), block.getY(), block.getZ(), TerraLootTable.VILLAGE_TAIGA_HOUSE);
      } else {
         super.applyData(block, data);
      }

   }
}
