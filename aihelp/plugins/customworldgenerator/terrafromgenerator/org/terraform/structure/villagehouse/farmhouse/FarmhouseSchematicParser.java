package org.terraform.structure.villagehouse.farmhouse;

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
import org.terraform.utils.WoodUtils;

public class FarmhouseSchematicParser extends SchematicParser {
   private final BiomeBank biome;
   private final Random rand;
   private final PopulatorDataAbstract pop;

   public FarmhouseSchematicParser(BiomeBank biome, Random rand, PopulatorDataAbstract pop) {
      this.biome = biome;
      this.rand = rand;
      this.pop = pop;
   }

   public void applyData(@NotNull SimpleBlock block, @NotNull BlockData data) {
      if (data.getMaterial().toString().contains("COBBLESTONE")) {
         data = Bukkit.createBlockData(data.getAsString().replaceAll("cobblestone", ((Material)GenUtils.randChoice(this.rand, Material.COBBLESTONE, Material.COBBLESTONE, Material.COBBLESTONE, Material.MOSSY_COBBLESTONE)).toString().toLowerCase(Locale.ENGLISH)));
         super.applyData(block, data);
      } else if (data.getMaterial().toString().contains("OAK")) {
         data = Bukkit.createBlockData(data.getAsString().replaceAll(data.getMaterial().toString().toLowerCase(Locale.ENGLISH), WoodUtils.getWoodForBiome(this.biome, WoodUtils.WoodType.parse(data.getMaterial())).toString().toLowerCase(Locale.ENGLISH)).toString().toLowerCase(Locale.ENGLISH));
         super.applyData(block, data);
      } else if (data.getMaterial() == Material.CHEST) {
         if (GenUtils.chance(this.rand, 1, 5)) {
            block.setType(Material.AIR);
            return;
         }

         super.applyData(block, data);
         this.pop.lootTableChest(block.getX(), block.getY(), block.getZ(), TerraLootTable.VILLAGE_PLAINS_HOUSE);
      } else {
         super.applyData(block, data);
      }

   }
}
