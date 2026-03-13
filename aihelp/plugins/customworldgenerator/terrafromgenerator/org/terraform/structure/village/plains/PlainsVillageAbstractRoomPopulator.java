package org.terraform.structure.village.plains;

import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.GenUtils;
import org.terraform.utils.SphereBuilder;

public abstract class PlainsVillageAbstractRoomPopulator extends RoomPopulatorAbstract {
   public PlainsVillageAbstractRoomPopulator(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      int roomY = this.calculateRoomY(data, room);
      int worldHeight = TerraformGeneratorPlugin.injector.getMaxY() - TerraformGeneratorPlugin.injector.getMinY() + 1;
      int[][] var5 = room.getAllCorners(2);
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         int[] corner = var5[var7];
         SimpleBlock sb = new SimpleBlock(data, corner[0], roomY, corner[1]);
         int lowSb = sb.findFloor(worldHeight).getY();
         if (Math.abs(lowSb - roomY) > TConfig.c.STRUCTURES_PLAINSVILLAGE_HEIGHT_TOLERANCE) {
            this.placeFixerPlatform(roomY, data, room);
            break;
         }
      }

   }

   public void placeFixerPlatform(int roomY, @NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      SimpleBlock core = room.getCenterSimpleBlock(data);
      core = core.getAtY(roomY);
      (new SphereBuilder(this.rand, core.getUp(), new Material[]{Material.AIR})).setRX((float)room.getWidthX() / 2.0F - 1.5F).setRZ((float)room.getWidthZ() / 2.0F - 1.5F).setRY((float)this.getRoomRoughNeededHeight()).setHardReplace(true).setSphereType(SphereBuilder.SphereType.UPPER_SEMISPHERE).build();
      (new SphereBuilder(this.rand, core.getDown(), new Material[]{Material.DIRT})).setRX((float)room.getWidthX() / 2.0F).setRZ((float)room.getWidthZ() / 2.0F).setRY(3.0F).setPadding(4).setSphereFrequency(0.11F).setMinRadius(0.8D).setHardReplace(false).setUpperType(Material.GRASS_BLOCK).setSphereType(SphereBuilder.SphereType.LOWER_SEMISPHERE).build();
   }

   public int getRoomRoughNeededHeight() {
      return 12;
   }

   protected int calculateRoomY(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      int centerHeight = GenUtils.getHighestGroundOrSeaLevel(data, room.getX(), room.getZ());
      int pathHeight = this.getPathHeight(data, room);
      return Math.abs(centerHeight - pathHeight) > TConfig.c.STRUCTURES_PLAINSVILLAGE_HEIGHT_TOLERANCE ? pathHeight : centerHeight;
   }

   protected boolean doesAreaFailTolerance(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      int roomY = this.calculateRoomY(data, room);
      int worldHeight = TerraformGeneratorPlugin.injector.getMaxY() - TerraformGeneratorPlugin.injector.getMinY() + 1;
      int[][] var5 = room.getAllCorners(2);
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         int[] corner = var5[var7];
         SimpleBlock sb = new SimpleBlock(data, corner[0], roomY, corner[1]);
         int lowSb = sb.findFloor(worldHeight).getY();
         if (Math.abs(lowSb - roomY) > TConfig.c.STRUCTURES_PLAINSVILLAGE_HEIGHT_TOLERANCE) {
            return true;
         }
      }

      return false;
   }

   protected int getPathHeight(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      BlockFace dir = ((DirectionalCubeRoom)room).getDirection();
      int pad = GenUtils.randInt(1, 3);
      Entry<Wall, Integer> openingWallSet = room.getWall(data, dir, pad);
      int pathHeight = ((Wall)openingWallSet.getKey()).getLeft((Integer)openingWallSet.getValue() / 2).getGroundOrSeaLevel().getY();
      return pathHeight;
   }
}
