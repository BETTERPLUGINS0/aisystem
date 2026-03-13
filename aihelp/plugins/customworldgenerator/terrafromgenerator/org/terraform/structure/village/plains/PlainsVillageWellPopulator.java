package org.terraform.structure.village.plains;

import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Random;
import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.schematic.SchematicParser;
import org.terraform.schematic.TerraSchematic;
import org.terraform.structure.room.CubeRoom;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class PlainsVillageWellPopulator extends PlainsVillageAbstractRoomPopulator {
   private static final String[] villageWellSchems = new String[]{"plainsvillage-well1", "plainsvillage-well2"};
   private final PlainsVillagePopulator plainsVillagePopulator;

   public PlainsVillageWellPopulator(PlainsVillagePopulator plainsVillagePopulator, Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
      this.plainsVillagePopulator = plainsVillagePopulator;
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      int y = super.calculateRoomY(data, room);
      int worldHeight = TerraformGeneratorPlugin.injector.getMaxY() - TerraformGeneratorPlugin.injector.getMinY() + 1;
      int[][] var5 = room.getAllCorners();
      int z = var5.length;

      SimpleBlock core;
      for(int var7 = 0; var7 < z; ++var7) {
         int[] corner = var5[var7];
         core = new SimpleBlock(data, corner[0], y, corner[1]);
         int lowSb = core.findFloor(worldHeight).getY();
         if (Math.abs(lowSb - y) > TConfig.c.STRUCTURES_PLAINSVILLAGE_HEIGHT_TOLERANCE) {
            this.placeFixerPlatform(y, data, room);
            break;
         }
      }

      int x = room.getX();
      z = room.getZ();
      SimpleBlock tester = new SimpleBlock(data, x, y + 1, z);
      if (BlockUtils.isWet(tester)) {
         y = tester.getGroundOrDry().getY();
      }

      BlockFace roomDir = ((DirectionalCubeRoom)room).getDirection();

      try {
         core = new SimpleBlock(data, x, y + 1, z);
         TerraSchematic schem = TerraSchematic.load(villageWellSchems[this.rand.nextInt(villageWellSchems.length)], core.getDown());
         schem.parser = new PlainsVillageWellPopulator.PlainsVillageWellSchematicParser();
         schem.apply();
         int depth = GenUtils.randInt(this.rand, 5, 20);

         int i;
         for(i = 0; i < depth; ++i) {
            boolean breakOut = false;
            BlockFace[] var14;
            int var15;
            int var16;
            BlockFace face;
            if (i > 0) {
               var14 = BlockUtils.flatBlockFaces3x3;
               var15 = var14.length;

               for(var16 = 0; var16 < var15; ++var16) {
                  face = var14[var16];
                  if (!core.getRelative(face).getDown(depth + 1).isSolid()) {
                     breakOut = true;
                     break;
                  }
               }
            }

            if (breakOut) {
               break;
            }

            if (i == 0) {
               core.setType(Material.AIR);
            } else {
               core.getRelative(0, -i, 0).setType(Material.WATER);
            }

            var14 = BlockUtils.xzPlaneBlockFaces;
            var15 = var14.length;

            for(var16 = 0; var16 < var15; ++var16) {
               face = var14[var16];
               if (i == 0) {
                  core.getRelative(face).setType(Material.AIR);
               } else {
                  core.getRelative(0, -i, 0).getRelative(face).setType(Material.WATER);
               }
            }
         }

         int pathLength;
         for(i = -3; i <= 3; ++i) {
            for(pathLength = -3; pathLength <= 3; ++pathLength) {
               Wall target = new Wall(core.getRelative(i, -1, pathLength));
               if (target.getType() == Material.COBBLESTONE || target.getType() == Material.MOSSY_COBBLESTONE) {
                  target.getDown().downUntilSolid(this.rand, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
               }
            }
         }

         Wall w = new Wall(core.getRelative(roomDir, 3), roomDir);
         pathLength = room.getWidthX() / 2;
         if (BlockUtils.getAxisFromBlockFace(roomDir) == Axis.Z) {
            pathLength = room.getWidthZ() / 2;
         }

         for(int i = 0; i < pathLength - 1; ++i) {
            w.getGround().setType(Material.DIRT_PATH);
            w.getLeft().getGround().setType(Material.DIRT_PATH);
            w.getRight().getGround().setType(Material.DIRT_PATH);
            if (GenUtils.chance(this.rand, 1, 10)) {
               BlockFace lampFace = BlockUtils.getTurnBlockFace(this.rand, roomDir);
               SimpleBlock target = w.getRelative(lampFace, 2).getGround().getUp().get();
               if (target.getDown().getType() != Material.DIRT_PATH && PlainsVillagePathPopulator.canPlaceLamp(target)) {
                  PlainsVillagePathPopulator.placeLamp(this.rand, target);
               }
            }

            w = w.getRelative(roomDir);
         }
      } catch (FileNotFoundException var18) {
         TerraformGeneratorPlugin.logger.stackTrace(var18);
      }

   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getWidthX() <= 10;
   }

   private class PlainsVillageWellSchematicParser extends SchematicParser {
      public void applyData(@NotNull SimpleBlock block, @NotNull BlockData data) {
         if (data.getMaterial().toString().contains("COBBLESTONE")) {
            data = Bukkit.createBlockData(data.getAsString().replaceAll("cobblestone", ((Material)GenUtils.randChoice(PlainsVillageWellPopulator.this.rand, Material.COBBLESTONE, Material.COBBLESTONE, Material.COBBLESTONE, Material.MOSSY_COBBLESTONE)).toString().toLowerCase(Locale.ENGLISH)));
            super.applyData(block, data);
         } else if (data.getMaterial() == Material.IRON_BARS) {
            data = Bukkit.createBlockData(Material.CHAIN);
            super.applyData(block, data);
         } else if (data.getMaterial().toString().startsWith("OAK_")) {
            data = Bukkit.createBlockData(data.getAsString().replaceAll("oak_", PlainsVillageWellPopulator.this.plainsVillagePopulator.wood));
            super.applyData(block, data);
         } else {
            super.applyData(block, data);
         }

      }
   }
}
