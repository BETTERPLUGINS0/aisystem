package org.terraform.structure.pillager.mansion.secondfloor;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.schematic.SchematicParser;
import org.terraform.schematic.TerraSchematic;
import org.terraform.structure.pillager.mansion.MansionInternalWallState;
import org.terraform.structure.pillager.mansion.MansionRoomPopulator;
import org.terraform.structure.pillager.mansion.MansionRoomSize;
import org.terraform.structure.room.CubeRoom;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class MansionSecondFloorStoreroomPopulator extends MansionRoomPopulator {
   private static final int roomWidthX = 15;
   private static final int roomWidthZ = 6;

   public MansionSecondFloorStoreroomPopulator(CubeRoom room, HashMap<BlockFace, MansionInternalWallState> internalWalls) {
      super(room, internalWalls);
   }

   public void decorateRoom(@NotNull PopulatorDataAbstract data, @NotNull Random random) {
      int[] lowerBounds = this.getRoom().getLowerCorner(1);
      BlockFace randomFace = (new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH})[random.nextInt(2)];

      try {
         SimpleBlock target;
         TerraSchematic schema;
         if (randomFace == BlockFace.NORTH) {
            target = new SimpleBlock(data, lowerBounds[0], this.getRoom().getY(), lowerBounds[1]);
            schema = TerraSchematic.load("mansion/mansion-storageroom", target);
            schema.parser = new MansionSecondFloorStoreroomPopulator.MansionStoreroomSchematicParser(random);
            schema.setFace(randomFace);
            schema.apply();
         } else if (randomFace == BlockFace.SOUTH) {
            target = new SimpleBlock(data, lowerBounds[0] + 15, this.getRoom().getY(), lowerBounds[1] + 6);
            schema = TerraSchematic.load("mansion/mansion-storageroom", target);
            schema.parser = new MansionSecondFloorStoreroomPopulator.MansionStoreroomSchematicParser(random);
            schema.setFace(randomFace);
            schema.apply();
         }
      } catch (FileNotFoundException var7) {
         TerraformGeneratorPlugin.logger.stackTrace(var7);
      }

   }

   @NotNull
   public MansionRoomSize getSize() {
      return new MansionRoomSize(2, 1);
   }

   private static class MansionStoreroomSchematicParser extends SchematicParser {
      private final Random rand;

      public MansionStoreroomSchematicParser(Random rand) {
         this.rand = rand;
      }

      public void applyData(@NotNull SimpleBlock block, @NotNull BlockData data) {
         if (data.getMaterial() == Material.CHEST) {
            Material replacement = (Material)GenUtils.randChoice(this.rand, Material.CHEST, Material.CHEST, Material.CHEST, Material.CHEST, Material.CHEST, Material.BARREL, Material.BARREL, Material.BARREL, Material.BARREL, Material.BARREL, Material.CRAFTING_TABLE, Material.DARK_OAK_LOG, Material.CAKE, Material.LANTERN, Material.COAL_BLOCK);
            data = Bukkit.createBlockData(replacement);
            BlockUtils.randRotateBlockData(this.rand, data);
         }

         super.applyData(block, data);
         if (data.getMaterial() == Material.CHEST || data.getMaterial() == Material.BARREL) {
            block.getPopData().lootTableChest(block.getX(), block.getY(), block.getZ(), TerraLootTable.WOODLAND_MANSION);
         }

      }
   }
}
