package org.terraform.structure.pillager.outpost;

import java.util.Locale;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.coregen.populatordata.PopulatorDataPostGen;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.schematic.SchematicParser;
import org.terraform.utils.BannerUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.WoodUtils;

public class OutpostSchematicParser extends SchematicParser {
   private final BiomeBank biome;
   private final Random rand;
   private final PopulatorDataAbstract pop;
   private final int baseY;
   private Material[] toReplace;

   public OutpostSchematicParser(BiomeBank biome, Random rand, PopulatorDataAbstract pop, int baseY) {
      this.biome = biome;
      this.rand = rand;
      this.pop = pop;
      this.baseY = baseY;
      this.toReplace = new Material[]{Material.COBBLESTONE, Material.COBBLESTONE, Material.COBBLESTONE, Material.MOSSY_COBBLESTONE};
      if (biome == BiomeBank.BADLANDS || biome == BiomeBank.DESERT) {
         this.toReplace = new Material[]{Material.COBBLESTONE, Material.COBBLESTONE, Material.COBBLESTONE, Material.ANDESITE};
      }

   }

   public void applyData(@NotNull SimpleBlock block, @NotNull BlockData data) {
      if (data.getMaterial().toString().contains("COBBLESTONE")) {
         data = Bukkit.createBlockData(data.getAsString().replaceAll("cobblestone", ((Material)GenUtils.randChoice(this.rand, this.toReplace)).toString().toLowerCase(Locale.ENGLISH)));
         super.applyData(block, data);
      } else if (data.getMaterial().toString().contains("OAK")) {
         data = Bukkit.createBlockData(data.getAsString().replaceAll(data.getMaterial().toString().toLowerCase(Locale.ENGLISH), WoodUtils.getWoodForBiome(this.biome, WoodUtils.WoodType.parse(data.getMaterial())).toString().toLowerCase(Locale.ENGLISH)).toString().toLowerCase(Locale.ENGLISH));
         super.applyData(block, data);
      } else if (data.getMaterial() == Material.CHEST) {
         if (GenUtils.chance(this.rand, 1, 5)) {
            block.setType(Material.AIR);
         } else {
            super.applyData(block, data);
            this.pop.lootTableChest(block.getX(), block.getY(), block.getZ(), TerraLootTable.PILLAGER_OUTPOST);
         }
      } else if (data.getMaterial() == Material.BARREL) {
         if (GenUtils.chance(this.rand, 3, 5)) {
            block.setType(Material.HAY_BLOCK);
         } else {
            super.applyData(block, data);
            this.pop.lootTableChest(block.getX(), block.getY(), block.getZ(), TerraLootTable.PILLAGER_OUTPOST);
         }
      } else if (data.getMaterial() != Material.WHITE_WALL_BANNER && data.getMaterial() != Material.WHITE_BANNER) {
         super.applyData(block, data);
      } else {
         super.applyData(block, data);
         if (block.getPopData() instanceof PopulatorDataPostGen) {
            Banner banner = (Banner)((PopulatorDataPostGen)block.getPopData()).getBlockState(block.getX(), block.getY(), block.getZ());
            banner.setPatterns(BannerUtils.getOminousBannerPatterns());
            banner.update();
         }
      }

      if (block.getY() == this.baseY) {
         (new Wall(block.getDown())).downUntilSolid(new Random(), this.toReplace);
      }

   }
}
