package org.terraform.structure.room;

import java.util.HashMap;
import java.util.Random;
import java.util.AbstractMap.SimpleEntry;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.LimitedRegion;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.Wall;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.structure.room.carver.StandardRoomCarver;
import org.terraform.utils.GenUtils;

public class CubeRoom {
   int widthX;
   int widthZ;
   int height;
   int x;
   int y;
   int z;
   RoomPopulatorAbstract pop;
   boolean isActivated = false;

   public CubeRoom(int widthX, int widthZ, int height, int x, int y, int z) {
      this.widthX = widthX;
      this.widthZ = widthZ;
      this.height = height;
      this.x = x;
      this.y = y;
      this.z = z;
   }

   @NotNull
   public SimpleEntry<Wall, Integer> getWall(@NotNull PopulatorDataAbstract data, @NotNull BlockFace face, int padding) {
      int[] lowerBounds = this.getLowerCorner();
      int[] upperBounds = this.getUpperCorner();
      int length = 0;
      Wall wall;
      switch(face) {
      case SOUTH:
         wall = new Wall(new SimpleBlock(data, lowerBounds[0] + padding, this.y + 1, upperBounds[1] - padding), BlockFace.NORTH);
         length = this.widthX - 2 * padding;
         break;
      case NORTH:
         wall = new Wall(new SimpleBlock(data, upperBounds[0] - padding, this.y + 1, lowerBounds[1] + padding), BlockFace.SOUTH);
         length = this.widthX - 2 * padding;
         break;
      case WEST:
         wall = new Wall(new SimpleBlock(data, lowerBounds[0] + padding, this.y + 1, lowerBounds[1] + padding), BlockFace.EAST);
         length = this.widthZ - 2 * padding;
         break;
      case EAST:
         wall = new Wall(new SimpleBlock(data, upperBounds[0] - padding, this.y + 1, upperBounds[1] - padding), BlockFace.WEST);
         length = this.widthZ - 2 * padding;
         break;
      default:
         wall = null;
         TerraformGeneratorPlugin.logger.error("Invalid wall direction requested!");
      }

      return new SimpleEntry(wall, length);
   }

   @NotNull
   public HashMap<Wall, Integer> getFourWalls(@NotNull PopulatorDataAbstract data, int padding) {
      int[] lowerBounds = this.getLowerCorner();
      int[] upperBounds = this.getUpperCorner();
      HashMap<Wall, Integer> walls = new HashMap();
      Wall north = new Wall(new SimpleBlock(data, lowerBounds[0] + padding, this.y + 1, upperBounds[1] - padding), BlockFace.NORTH);
      Wall south = new Wall(new SimpleBlock(data, upperBounds[0] - padding, this.y + 1, lowerBounds[1] + padding), BlockFace.SOUTH);
      Wall east = new Wall(new SimpleBlock(data, lowerBounds[0] + padding, this.y + 1, lowerBounds[1] + padding), BlockFace.EAST);
      Wall west = new Wall(new SimpleBlock(data, upperBounds[0] - padding, this.y + 1, upperBounds[1] - padding), BlockFace.WEST);
      walls.put(north, this.widthX - 2 * padding);
      walls.put(south, this.widthX - 2 * padding);
      walls.put(east, this.widthZ - 2 * padding);
      walls.put(west, this.widthZ - 2 * padding);
      return walls;
   }

   public void setRoomPopulator(RoomPopulatorAbstract pop) {
      this.pop = pop;
   }

   public void populate(PopulatorDataAbstract data) {
      if (this.pop != null) {
         this.pop.populate(data, this);
      }

   }

   public void fillRoom(@NotNull PopulatorDataAbstract data, Material... mat) {
      this.fillRoom(data, -1, mat, Material.AIR);
   }

   public void fillRoom(@NotNull PopulatorDataAbstract data, int tile, Material[] mat, Material fillMat) {
      (new StandardRoomCarver(tile, fillMat)).carveRoom(data, this, mat);
   }

   public int[] getCenter() {
      return new int[]{this.x, this.y, this.z};
   }

   @NotNull
   public SimpleBlock getCenterSimpleBlock(@NotNull PopulatorDataAbstract data) {
      return new SimpleBlock(data, this.x, this.y, this.z);
   }

   public double centralDistanceSquared(@NotNull int[] other) {
      return Math.pow((double)(this.x - other[0]), 2.0D) + Math.pow((double)(this.y - other[1]), 2.0D) + Math.pow((double)(this.z - other[2]), 2.0D);
   }

   @NotNull
   public CubeRoom getCloneSubsetRoom(int paddingX, int paddingZ) {
      return new CubeRoom(this.widthX - paddingX * 2, this.widthZ - paddingZ * 2, this.height, this.x, this.y, this.z);
   }

   public boolean isClone(@NotNull CubeRoom other) {
      return this.x == other.x && this.y == other.y && this.z == other.z && this.widthX == other.widthX && this.height == other.height && this.widthZ == other.widthZ;
   }

   public boolean isOverlapping(@NotNull CubeRoom room) {
      return Math.abs(room.x - this.x) < Math.abs(room.widthX + this.widthX) / 2 && Math.abs(room.z - this.z) < Math.abs(room.widthZ + this.widthZ) / 2;
   }

   public int[] randomCoords(@NotNull Random rand) {
      return this.randomCoords(rand, 0);
   }

   public int[] randomCoords(@NotNull Random rand, int pad) {
      return GenUtils.randomCoords(rand, new int[]{this.x - this.widthX / 2 + pad, this.y + pad, this.z - this.widthZ / 2 + pad}, new int[]{this.x + this.widthX / 2 - pad, this.y + this.height - 1 - pad, this.z + this.widthZ / 2 - pad});
   }

   public boolean isPointInside(@NotNull int[] point, int pad) {
      int[] boundOne = this.getUpperCorner(pad);
      int[] boundTwo = this.getLowerCorner(pad);
      if (boundOne[0] >= point[0] && boundOne[1] >= point[1]) {
         return boundTwo[0] <= point[0] && boundTwo[1] <= point[1];
      } else {
         return false;
      }
   }

   public boolean isPointInside(@NotNull int[] point) {
      int[] boundOne = this.getUpperCorner();
      int[] boundTwo = this.getLowerCorner();
      if (boundOne[0] >= point[0] && boundOne[1] >= point[1]) {
         return boundTwo[0] <= point[0] && boundTwo[1] <= point[1];
      } else {
         return false;
      }
   }

   public boolean isPointInside(@NotNull SimpleLocation loc) {
      int[] boundOne = this.getUpperCorner();
      int[] boundTwo = this.getLowerCorner();
      if (boundOne[0] >= loc.getX() && boundOne[1] >= loc.getZ()) {
         return boundTwo[0] <= loc.getX() && boundTwo[1] <= loc.getZ();
      } else {
         return false;
      }
   }

   @NotNull
   public SimpleLocation getSimpleLocation() {
      return new SimpleLocation(this.getX(), this.getY(), this.getZ());
   }

   public boolean isPointInside(@NotNull SimpleBlock point) {
      int[] boundOne = this.getUpperCorner();
      int[] boundTwo = this.getLowerCorner();
      if (boundOne[0] >= point.getX() && boundOne[1] >= point.getZ()) {
         return boundTwo[0] <= point.getX() && boundTwo[1] <= point.getZ();
      } else {
         return false;
      }
   }

   public boolean isInside(@NotNull CubeRoom other) {
      int[][] var2 = this.getAllCorners();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int[] corner = var2[var4];
         if (!other.isPointInside(corner)) {
            return false;
         }
      }

      return true;
   }

   public boolean envelopesOrIsInside(@NotNull CubeRoom other) {
      return this.isInside(other) || other.isInside(this);
   }

   public boolean isActivated() {
      return this.isActivated;
   }

   public void setActivated(boolean isActivated) {
      this.isActivated = isActivated;
   }

   public int[][] getAllCorners() {
      return this.getAllCorners(0);
   }

   public int[][] getAllCorners(int padding) {
      int[][] corners = new int[4][2];
      corners[0] = new int[]{this.x + this.widthX / 2 - padding, this.z + this.widthZ / 2 - padding};
      corners[1] = new int[]{this.x - this.widthX / 2 + padding, this.z + this.widthZ / 2 - padding};
      corners[2] = new int[]{this.x + this.widthX / 2 - padding, this.z - this.widthZ / 2 + padding};
      corners[3] = new int[]{this.x - this.widthX / 2 + padding, this.z - this.widthZ / 2 + padding};
      return corners;
   }

   public int[][] getCornersAlongFace(BlockFace face, int padding) {
      int[][] corners = new int[2][2];
      if (face == BlockFace.NORTH) {
         corners[0] = new int[]{this.x - this.widthX / 2 + padding, this.z - this.widthZ / 2 + padding};
         corners[1] = new int[]{this.x + this.widthX / 2 - padding, this.z - this.widthZ / 2 + padding};
      } else if (face == BlockFace.SOUTH) {
         corners[0] = new int[]{this.x - this.widthX / 2 + padding, this.z + this.widthZ / 2 - padding};
         corners[1] = new int[]{this.x + this.widthX / 2 - padding, this.z + this.widthZ / 2 - padding};
      } else if (face == BlockFace.WEST) {
         corners[0] = new int[]{this.x - this.widthX / 2 + padding, this.z - this.widthZ / 2 + padding};
         corners[1] = new int[]{this.x - this.widthX / 2 + padding, this.z + this.widthZ / 2 - padding};
      } else if (face == BlockFace.EAST) {
         corners[0] = new int[]{this.x + this.widthX / 2 - padding, this.z - this.widthZ / 2 + padding};
         corners[1] = new int[]{this.x + this.widthX / 2 - padding, this.z + this.widthZ / 2 - padding};
      }

      return corners;
   }

   public int[] getUpperCorner() {
      return new int[]{this.x + this.widthX / 2, this.z + this.widthZ / 2};
   }

   public int[] getLowerCorner() {
      return new int[]{this.x - this.widthX / 2, this.z - this.widthZ / 2};
   }

   public boolean isInRegion(@NotNull LimitedRegion region) {
      return region.isInRegion(this.x - this.widthX / 2, this.y, this.z - this.widthZ / 2) && region.isInRegion(this.x + this.widthX / 2, this.y, this.z + this.widthZ / 2);
   }

   public int[] getUpperCorner(int pad) {
      int Z = this.z - pad + this.widthZ / 2;
      int X = this.x - pad + this.widthX / 2;
      if (pad > this.widthZ / 2) {
         Z = this.z;
      }

      if (pad > this.widthX / 2) {
         X = this.x;
      }

      return new int[]{X, Z};
   }

   public int[] getLowerCorner(int pad) {
      int Z = this.z + pad - this.widthZ / 2;
      int X = this.x + pad - this.widthX / 2;
      if (pad > this.widthZ / 2) {
         Z = this.z;
      }

      if (pad > this.widthX / 2) {
         X = this.x;
      }

      return new int[]{X, Z};
   }

   public void purgeRoomContents(@NotNull PopulatorDataAbstract data, int padding) {
      int[] lowerCorner = this.getLowerCorner(padding);
      int[] upperCorner = this.getUpperCorner(padding);
      int lowestY = this.y + padding;
      int upperY = this.y + this.height - padding;

      for(int x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
         for(int z = lowerCorner[1]; z <= upperCorner[1]; ++z) {
            for(int y = lowestY; y <= upperY; ++y) {
               data.setType(x, y, z, Material.AIR);
            }
         }
      }

   }

   public boolean isBig() {
      return this.widthX * this.widthZ * this.height >= 2000;
   }

   public boolean isHuge() {
      return this.widthX * this.widthZ * this.height >= 7000;
   }

   public boolean largerThanVolume(int vol) {
      return this.widthX * this.widthZ * this.height >= vol;
   }

   public int getWidthX() {
      return this.widthX;
   }

   public void setWidthX(int widthX) {
      this.widthX = widthX;
   }

   public int getWidthZ() {
      return this.widthZ;
   }

   public void setWidthZ(int widthZ) {
      this.widthZ = widthZ;
   }

   public int getHeight() {
      return this.height;
   }

   public void setHeight(int height) {
      this.height = height;
   }

   public RoomPopulatorAbstract getPop() {
      return this.pop;
   }

   public int getX() {
      return this.x;
   }

   public void setX(int x) {
      this.x = x;
   }

   public int getY() {
      return this.y;
   }

   public void setY(int y) {
      this.y = y;
   }

   public int getZ() {
      return this.z;
   }

   public void setZ(int z) {
      this.z = z;
   }

   public boolean canLRCarve(int chunkX, int chunkZ, LimitedRegion lr) {
      return chunkX == this.x >> 4 && chunkZ == this.z >> 4 && this.isInRegion(lr);
   }

   public void debugRedGround(@NotNull PopulatorDataAbstract data) {
      int[] lowerCorner = this.getLowerCorner();
      int[] upperCorner = this.getUpperCorner();

      for(int x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
         for(int z = lowerCorner[1]; z <= upperCorner[1]; ++z) {
            data.setType(x, GenUtils.getHighestGround(data, x, z), z, Material.RED_WOOL);
         }
      }

   }
}
