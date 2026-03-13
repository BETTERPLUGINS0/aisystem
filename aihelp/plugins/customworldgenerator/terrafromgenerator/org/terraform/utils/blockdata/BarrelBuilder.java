package org.terraform.utils.blockdata;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.data.SimpleBlock;
import org.terraform.main.config.TConfig;

public class BarrelBuilder {
   @NotNull
   private final BlockData blockData;
   private TerraLootTable lootTable;

   public BarrelBuilder() {
      this.blockData = Bukkit.createBlockData(Material.BARREL);
   }

   @NotNull
   public BarrelBuilder setLootTable(@NotNull TerraLootTable... loottable) {
      this.lootTable = loottable[(new Random()).nextInt(loottable.length)];
      return this;
   }

   @NotNull
   public BarrelBuilder setFacing(@NotNull BlockFace face) {
      ((Directional)this.blockData).setFacing(face);
      return this;
   }

   @NotNull
   public BarrelBuilder apply(@NotNull SimpleBlock block) {
      if (!TConfig.areDecorationsEnabled()) {
         return this;
      } else {
         block.setBlockData(this.blockData);
         if (this.lootTable != null) {
            block.getPopData().lootTableChest(block.getX(), block.getY(), block.getZ(), this.lootTable);
         }

         return this;
      }
   }
}
