package org.terraform.small_items;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.utils.GenUtils;

public enum DecorationsBuilder {
   LANTERN(Material.LANTERN),
   CRAFTING_TABLE(Material.CRAFTING_TABLE),
   MELON(Material.MELON),
   PUMPKIN(Material.PUMPKIN),
   CAKE(Material.CAKE),
   OAK_PRESSURE_PLATE(Material.OAK_PRESSURE_PLATE),
   CARTOGRAPHY_TABLE(Material.CARTOGRAPHY_TABLE),
   BREWING_STAND(Material.BREWING_STAND),
   NOTE_BLOCK(Material.NOTE_BLOCK),
   FLETCHING_TABLE(Material.FLETCHING_TABLE),
   ENCHANTING_TABLE(Material.ENCHANTING_TABLE),
   ANVIL(Material.ANVIL),
   JUKEBOX(Material.JUKEBOX);

   private final Material material;

   private DecorationsBuilder(final Material param3) {
      this.material = material;
   }

   public static void build(@NotNull SimpleBlock block, @NotNull DecorationsBuilder... options) {
      ((DecorationsBuilder)GenUtils.randChoice((Object[])options)).build(block.getPopData(), block.getX(), block.getY(), block.getZ());
   }

   public static void build(@NotNull PopulatorDataAbstract data, int x, int y, int z, @NotNull DecorationsBuilder... options) {
      ((DecorationsBuilder)GenUtils.randChoice((Object[])options)).build(data, x, y, z);
   }

   public void build(@NotNull PopulatorDataAbstract data, int x, int y, int z) {
      data.setType(x, y, z, this.material);
   }

   public void build(@NotNull SimpleBlock block) {
      this.build(block.getPopData(), block.getX(), block.getY(), block.getZ());
   }

   // $FF: synthetic method
   private static DecorationsBuilder[] $values() {
      return new DecorationsBuilder[]{LANTERN, CRAFTING_TABLE, MELON, PUMPKIN, CAKE, OAK_PRESSURE_PLATE, CARTOGRAPHY_TABLE, BREWING_STAND, NOTE_BLOCK, FLETCHING_TABLE, ENCHANTING_TABLE, ANVIL, JUKEBOX};
   }
}
