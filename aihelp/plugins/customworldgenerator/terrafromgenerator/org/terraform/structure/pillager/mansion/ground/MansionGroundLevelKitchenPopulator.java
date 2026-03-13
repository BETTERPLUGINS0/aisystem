package org.terraform.structure.pillager.mansion.ground;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Slab.Type;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.main.TLogger;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.schematic.TerraSchematic;
import org.terraform.small_items.DecorationsBuilder;
import org.terraform.structure.pillager.mansion.MansionInternalWallState;
import org.terraform.structure.pillager.mansion.MansionRoomPopulator;
import org.terraform.structure.pillager.mansion.MansionRoomSchematicParser;
import org.terraform.structure.pillager.mansion.MansionRoomSize;
import org.terraform.structure.room.CubeRoom;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.blockdata.DirectionalBuilder;
import org.terraform.utils.blockdata.SlabBuilder;
import org.terraform.utils.blockdata.StairBuilder;

public class MansionGroundLevelKitchenPopulator extends MansionRoomPopulator {
   private static final int roomWidthX = 6;
   private static final int roomWidthZ = 15;

   public MansionGroundLevelKitchenPopulator(CubeRoom room, HashMap<BlockFace, MansionInternalWallState> internalWalls) {
      super(room, internalWalls);
   }

   public void decorateRoom(@NotNull PopulatorDataAbstract data, @NotNull Random random) {
      int[] lowerBounds = this.getRoom().getLowerCorner(1);
      BlockFace randomFace = (new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH})[random.nextInt(2)];
      TLogger var10000 = TerraformGeneratorPlugin.logger;
      String var10001 = String.valueOf(this.getRoom().getSimpleLocation());
      var10000.info("Kitchen at " + var10001 + " picking face: " + String.valueOf(randomFace));

      try {
         SimpleBlock target;
         TerraSchematic schema;
         if (randomFace == BlockFace.NORTH) {
            target = new SimpleBlock(data, lowerBounds[0], this.getRoom().getY(), lowerBounds[1] + 15);
            schema = TerraSchematic.load("mansion/mansion-kitchen", target);
            schema.setFace(randomFace);
            schema.parser = new MansionGroundLevelKitchenPopulator.MansionKitchenSchematicParser(random, data);
            schema.apply();
         } else if (randomFace == BlockFace.SOUTH) {
            target = new SimpleBlock(data, lowerBounds[0] + 6, this.getRoom().getY(), lowerBounds[1]);
            schema = TerraSchematic.load("mansion/mansion-kitchen", target);
            schema.setFace(randomFace);
            schema.parser = new MansionGroundLevelKitchenPopulator.MansionKitchenSchematicParser(random, data);
            schema.apply();
         }
      } catch (FileNotFoundException var7) {
         TerraformGeneratorPlugin.logger.stackTrace(var7);
      }

   }

   public void decorateExit(Random rand, @NotNull Wall w) {
      w.getUp(6).setType(Material.DARK_OAK_PLANKS);
   }

   public void decorateWindow(@NotNull Random rand, @NotNull Wall w) {
      w.getUp(6).setType(Material.DARK_OAK_PLANKS);
      int choice = rand.nextInt(3);
      switch(choice) {
      case 0:
         (new DirectionalBuilder(Material.SMOKER)).setFacing(w.getDirection()).apply(w).apply(w.getLeft()).apply(w.getRight()).apply(w.getLeft(2)).apply(w.getRight(2));
         w.getUp().setType(Material.DARK_OAK_PRESSURE_PLATE);
         w.getUp().getLeft().setType(Material.DARK_OAK_PRESSURE_PLATE);
         w.getUp().getLeft(2).setType(Material.DARK_OAK_PRESSURE_PLATE);
         w.getUp().getRight().setType(Material.DARK_OAK_PRESSURE_PLATE);
         w.getUp().getRight(2).setType(Material.DARK_OAK_PRESSURE_PLATE);
         break;
      case 1:
         Wall target = w.getRight(2);

         for(int i = 0; i < 5; ++i) {
            if (GenUtils.chance(rand, 1, 3)) {
               target.setBlockData(BlockUtils.getRandomBarrel());
               target.get().getPopData().lootTableChest(target.getX(), target.getY(), target.getZ(), TerraLootTable.VILLAGE_BUTCHER);
            }

            target = target.getLeft();
         }

         return;
      default:
         (new SlabBuilder(Material.DARK_OAK_SLAB)).setType(Type.TOP).apply(w).apply(w.getLeft()).apply(w.getRight());
         (new StairBuilder(Material.DARK_OAK_STAIRS)).setHalf(Half.TOP).setFacing(BlockUtils.getLeft(w.getDirection())).apply(w.getLeft(2)).setFacing(BlockUtils.getRight(w.getDirection())).apply(w.getRight(2));
         if (rand.nextBoolean()) {
            DecorationsBuilder.CRAFTING_TABLE.build(w.getLeft());
         }

         if (rand.nextBoolean()) {
            DecorationsBuilder.build(w.getRight(), DecorationsBuilder.MELON, DecorationsBuilder.PUMPKIN, DecorationsBuilder.CAKE);
         }

         if (rand.nextBoolean()) {
            DecorationsBuilder.OAK_PRESSURE_PLATE.build(w.getUp());
         }
      }

   }

   public void decorateWall(@NotNull Random rand, @NotNull Wall w) {
      w.getRear().getLeft().Pillar(7, new Material[]{Material.DARK_OAK_LOG});
      if (rand.nextBoolean()) {
         (new DirectionalBuilder(Material.FURNACE)).setFacing(w.getDirection()).apply(w.getLeft());
         w.getLeft().getUp().Pillar(6, new Material[]{Material.COBBLESTONE_WALL});
         w.getLeft().getUp().CorrectMultipleFacing(6);
      }

      w.getRear().getRight().Pillar(7, new Material[]{Material.DARK_OAK_LOG});
      if (rand.nextBoolean()) {
         (new DirectionalBuilder(Material.FURNACE)).setFacing(w.getDirection()).apply(w.getRight());
         w.getRight().getUp().Pillar(6, new Material[]{Material.COBBLESTONE_WALL});
         w.getRight().getUp().CorrectMultipleFacing(6);
      }

      this.shelfify(rand, w.getRear());
      this.shelfify(rand, w.getLeft(2).getRear());
      this.shelfify(rand, w.getRight(2).getRear());
   }

   private void shelfify(@NotNull Random rand, @NotNull Wall w) {
      (new SlabBuilder(Material.POLISHED_ANDESITE_SLAB)).setType(Type.TOP).apply(w.getUp()).apply(w.getUp(3));
      w.setType(new Material[]{Material.AIR, Material.AIR, Material.CRAFTING_TABLE, Material.MELON, Material.PUMPKIN});
      w.getUp(2).setType(new Material[]{Material.POTTED_RED_MUSHROOM, Material.POTTED_BROWN_MUSHROOM, Material.CAKE, Material.TURTLE_EGG, Material.AIR, Material.AIR});
      w.getUp(4).setType(new Material[]{Material.POTTED_RED_MUSHROOM, Material.POTTED_BROWN_MUSHROOM, Material.CAKE, Material.TURTLE_EGG, Material.AIR, Material.AIR});
      Wall target;
      if (GenUtils.chance(rand, 1, 5)) {
         target = w.getUp(2);
         target.setBlockData(BlockUtils.getRandomBarrel());
         target.get().getPopData().lootTableChest(target.getX(), target.getY(), target.getZ(), TerraLootTable.VILLAGE_BUTCHER);
      }

      if (GenUtils.chance(rand, 1, 5)) {
         target = w.getUp(4);
         target.setBlockData(BlockUtils.getRandomBarrel());
         target.get().getPopData().lootTableChest(target.getX(), target.getY(), target.getZ(), TerraLootTable.VILLAGE_PLAINS_HOUSE);
      }

   }

   @NotNull
   public MansionRoomSize getSize() {
      return new MansionRoomSize(1, 2);
   }

   private static class MansionKitchenSchematicParser extends MansionRoomSchematicParser {
      public MansionKitchenSchematicParser(Random rand, PopulatorDataAbstract pop) {
         super(rand, pop);
      }

      public void applyData(@NotNull SimpleBlock block, @NotNull BlockData data) {
         if (data.getMaterial() == Material.MELON) {
            block.setType(Material.MELON, Material.PUMPKIN, Material.HAY_BLOCK, Material.DRIED_KELP_BLOCK);
         } else {
            super.applyData(block, data);
         }

      }
   }
}
