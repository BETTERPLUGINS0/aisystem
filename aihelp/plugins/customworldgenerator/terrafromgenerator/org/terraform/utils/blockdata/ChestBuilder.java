package org.terraform.utils.blockdata;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Chest.Type;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.main.config.TConfig;
import org.terraform.utils.GenUtils;

public class ChestBuilder {
   @NotNull
   private final Chest blockData;
   private TerraLootTable lootTable;

   public ChestBuilder(@NotNull Material mat) {
      this.blockData = (Chest)Bukkit.createBlockData(mat);
   }

   public ChestBuilder(Material... mat) {
      this.blockData = (Chest)Bukkit.createBlockData((Material)GenUtils.randChoice((Object[])mat));
   }

   @NotNull
   public ChestBuilder setLootTable(@NotNull TerraLootTable... loottable) {
      this.lootTable = loottable[(new Random()).nextInt(loottable.length)];
      return this;
   }

   @NotNull
   public ChestBuilder setWaterlogged(boolean waterlogged) {
      this.blockData.setWaterlogged(waterlogged);
      return this;
   }

   @NotNull
   public ChestBuilder setFacing(@NotNull BlockFace face) {
      this.blockData.setFacing(face);
      return this;
   }

   @NotNull
   public ChestBuilder apply(@NotNull SimpleBlock block) {
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

   public void extend(@NotNull SimpleBlock original, @NotNull SimpleBlock extended, boolean lootTableExtendedChest) {
      extended.setBlockData(this.blockData);
      Wall originalWall = new Wall(original, this.blockData.getFacing());

      Chest originalChest;
      Chest extendedChest;
      try {
         originalChest = (Chest)original.getBlockData();
         extendedChest = (Chest)extended.getBlockData();
      } catch (ClassCastException var8) {
         original.setBlockData(this.blockData);
         extended.setBlockData(this.blockData);
         originalChest = (Chest)this.blockData.clone();
         extendedChest = (Chest)this.blockData.clone();
      }

      if (originalWall.getLeft().equals(extended)) {
         originalChest.setType(Type.LEFT);
         extendedChest.setType(Type.RIGHT);
      } else {
         if (!originalWall.getRight().equals(extended)) {
            throw new IllegalArgumentException("A request to extend a doublechest was made, but an invalid location was specified.");
         }

         originalChest.setType(Type.RIGHT);
         extendedChest.setType(Type.LEFT);
      }

      original.setBlockData(originalChest);
      extended.setBlockData(extendedChest);
      if (this.lootTable != null) {
         original.getPopData().lootTableChest(original.getX(), original.getY(), original.getZ(), this.lootTable);
      }

      if (lootTableExtendedChest && this.lootTable != null) {
         extended.getPopData().lootTableChest(extended.getX(), extended.getY(), extended.getZ(), this.lootTable);
      }

   }

   @NotNull
   public ChestBuilder apply(@NotNull PopulatorDataAbstract data, int x, int y, int z) {
      data.setBlockData(x, y, z, this.blockData);
      if (this.lootTable != null) {
         data.lootTableChest(x, y, z, this.lootTable);
      }

      return this;
   }

   @NotNull
   public Chest get() {
      return this.blockData;
   }
}
