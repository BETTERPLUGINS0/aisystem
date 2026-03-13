package org.terraform.structure.stronghold;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.type.Chest;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class SilverfishDenPopulator extends RoomPopulatorAbstract {
   public SilverfishDenPopulator(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      SimpleBlock base = new SimpleBlock(data, room.getX(), room.getY() + room.getHeight() / 2 - 2, room.getZ());
      BlockUtils.replaceUpperSphere(this.rand.nextInt(9999), (float)(room.getWidthX() - 2) / 2.0F, (float)(room.getHeight() - 3), (float)(room.getWidthZ() - 2) / 2.0F, base, false, Material.INFESTED_STONE, Material.INFESTED_STONE, Material.CAVE_AIR, Material.STONE);
      data.setSpawner(room.getX(), room.getY() + 1, room.getZ(), EntityType.SILVERFISH);
      int[] upperBounds = room.getUpperCorner();
      int[] lowerBounds = room.getLowerCorner();

      for(int i = 0; i < GenUtils.randInt(this.rand, 1, 3); ++i) {
         int x = GenUtils.randInt(this.rand, lowerBounds[0] + 1, upperBounds[0] - 1);
         int z = GenUtils.randInt(this.rand, lowerBounds[1] + 1, upperBounds[1] - 1);

         int ny;
         for(ny = room.getY() + 1; data.getType(x, ny, z).isSolid() && ny < room.getHeight() + room.getY(); ++ny) {
         }

         if (ny != room.getHeight() + room.getY()) {
            data.setType(x, ny, z, Material.CHEST);
            Chest chest = (Chest)Bukkit.createBlockData(Material.CHEST);
            chest.setFacing(BlockUtils.getDirectBlockFace(this.rand));
            data.setBlockData(x, ny, z, chest);
            data.lootTableChest(x, ny, z, TerraLootTable.STRONGHOLD_CORRIDOR);
         }
      }

   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return !room.isBig();
   }
}
