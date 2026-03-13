package org.terraform.structure.pyramid;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.type.Chest;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.structure.room.CubeRoom;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class GenericAntechamber extends Antechamber {
   public GenericAntechamber(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      super.populate(data, room);

      int[] coords;
      for(int i = 0; i < GenUtils.randInt(2, 5); ++i) {
         coords = room.randomCoords(this.rand, 2);
         data.setType(coords[0], room.getY() + 1, coords[2], (Material)GenUtils.randChoice((Object[])(Material.CHISELED_SANDSTONE, Material.CHISELED_SANDSTONE, Material.CHISELED_SANDSTONE, Material.BONE_BLOCK)));
      }

      this.randomRoomPlacement(data, room, 0, 5, new Material[]{Material.FLOWER_POT});
      Material[] deadCorals = new Material[]{Material.DEAD_BRAIN_CORAL, Material.DEAD_BUBBLE_CORAL, Material.DEAD_FIRE_CORAL, Material.DEAD_HORN_CORAL, Material.DEAD_TUBE_CORAL, Material.DEAD_BRAIN_CORAL_FAN, Material.DEAD_BUBBLE_CORAL_FAN, Material.DEAD_FIRE_CORAL_FAN, Material.DEAD_HORN_CORAL_FAN, Material.DEAD_TUBE_CORAL_FAN};
      if (GenUtils.chance(this.rand, 1, 2)) {
         this.randomRoomPlacement(data, room, 1, 5, deadCorals);
      }

      if (GenUtils.chance(this.rand, 1, 3)) {
         this.randomRoomPlacement(data, room, 1, 1, new Material[]{Material.TURTLE_EGG});
      }

      if (GenUtils.chance(this.rand, 1, 2)) {
         this.randomRoomPlacement(data, room, 1, 5, new Material[]{Material.ACACIA_LOG, Material.BIRCH_LOG, Material.DARK_OAK_LOG, Material.JUNGLE_LOG, Material.SPRUCE_LOG, Material.OAK_LOG});
      }

      if (GenUtils.chance(1, 3)) {
         this.randomRoomPlacement(data, room, 1, 1, new Material[]{Material.CREEPER_HEAD, Material.BIRCH_LOG, Material.DARK_OAK_LOG, Material.JUNGLE_LOG, Material.SPRUCE_LOG, Material.OAK_LOG});
      }

      if (GenUtils.chance(1, 10)) {
         coords = room.randomCoords(this.rand, 2);
         if (!data.getType(coords[0], room.getY() + 1, coords[2]).isSolid()) {
            Chest chest = (Chest)Bukkit.createBlockData(Material.CHEST);
            chest.setFacing(BlockUtils.getDirectBlockFace(this.rand));
            data.setBlockData(coords[0], room.getY() + 1, coords[2], chest);
            data.lootTableChest(coords[0], room.getY() + 1, coords[2], TerraLootTable.BURIED_TREASURE);
         }
      }

   }
}
