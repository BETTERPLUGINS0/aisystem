package org.terraform.structure.stronghold;

import java.util.Random;
import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.type.Chest;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class SupplyRoomPopulator extends RoomPopulatorAbstract {
   public SupplyRoomPopulator(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      int[] upperBounds = room.getUpperCorner();
      int[] lowerBounds = room.getLowerCorner();
      int y = room.getY();

      int i;
      int x;
      int z;
      int ny;
      for(i = 0; i < GenUtils.randInt(this.rand, 1, 4); ++i) {
         x = GenUtils.randInt(this.rand, lowerBounds[0] + 1, upperBounds[0] - 1);
         z = GenUtils.randInt(this.rand, lowerBounds[1] + 1, upperBounds[1] - 1);

         for(ny = y + 1; data.getType(x, ny, z).isSolid() && ny < room.getHeight() + room.getY(); ++ny) {
         }

         if (ny != room.getHeight() + room.getY()) {
            data.setType(x, ny, z, Material.TORCH);
         }
      }

      for(i = 0; i < GenUtils.randInt(this.rand, 1, 3); ++i) {
         x = GenUtils.randInt(this.rand, lowerBounds[0] + 1, upperBounds[0] - 1);
         z = GenUtils.randInt(this.rand, lowerBounds[1] + 1, upperBounds[1] - 1);
         BlockUtils.replaceUpperSphere(this.rand.nextInt(992), (float)GenUtils.randInt(this.rand, 1, 3), (float)GenUtils.randInt(this.rand, 1, 3), (float)GenUtils.randInt(this.rand, 1, 3), new SimpleBlock(data, x, y, z), false, (Material)GenUtils.randChoice(this.rand, Material.IRON_ORE, Material.HAY_BLOCK, Material.CHISELED_STONE_BRICKS, Material.COAL_BLOCK, Material.COAL_ORE));
      }

      for(i = 0; i < GenUtils.randInt(this.rand, 5, 20); ++i) {
         x = GenUtils.randInt(this.rand, lowerBounds[0] + 1, upperBounds[0] - 1);
         z = GenUtils.randInt(this.rand, lowerBounds[1] + 1, upperBounds[1] - 1);

         for(ny = y + 1; data.getType(x, ny, z).isSolid() && ny < room.getHeight() + room.getY(); ++ny) {
         }

         if (ny != room.getHeight() + room.getY()) {
            Material type = (Material)GenUtils.randChoice(this.rand, Material.CRAFTING_TABLE, Material.ANVIL, Material.CAULDRON, Material.FLETCHING_TABLE, Material.SMITHING_TABLE, Material.CARTOGRAPHY_TABLE, Material.BARREL, Material.OAK_LOG);
            BlockData typeData = Bukkit.createBlockData(type);
            if (typeData instanceof Rotatable) {
               ((Rotatable)typeData).setRotation(BlockUtils.getDirectBlockFace(this.rand));
            } else if (typeData instanceof Directional) {
               ((Directional)typeData).setFacing(BlockUtils.getDirectBlockFace(this.rand));
            } else if (typeData instanceof Orientable) {
               ((Orientable)typeData).setAxis(Axis.values()[GenUtils.randInt(this.rand, 0, 2)]);
            }

            data.setBlockData(x, ny, z, typeData);
         }
      }

      for(i = 0; i < GenUtils.randInt(this.rand, 5, 20); ++i) {
         x = GenUtils.randInt(this.rand, lowerBounds[0] + 1, upperBounds[0] - 1);
         z = GenUtils.randInt(this.rand, lowerBounds[1] + 1, upperBounds[1] - 1);

         for(ny = y + 1; data.getType(x, ny, z).isSolid() && ny < room.getHeight() + room.getY(); ++ny) {
         }

         if (ny != room.getHeight() + room.getY()) {
            data.setType(x, ny, z, Material.CHEST);
            Chest chest = (Chest)Bukkit.createBlockData(Material.CHEST);
            chest.setFacing(BlockUtils.getDirectBlockFace(this.rand));
            data.setBlockData(x, ny, z, chest);
            data.lootTableChest(x, ny, z, TerraLootTable.STRONGHOLD_CROSSING);
         }
      }

   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return !room.isBig();
   }
}
