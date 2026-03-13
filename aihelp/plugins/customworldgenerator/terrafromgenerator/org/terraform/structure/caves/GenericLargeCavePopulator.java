package org.terraform.structure.caves;

import java.util.Random;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.type.SeaPickle;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.StalactiteBuilder;

public class GenericLargeCavePopulator extends RoomPopulatorAbstract {
   public GenericLargeCavePopulator(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   protected void populateFloor(SimpleBlock floor, int waterLevel) {
   }

   protected void populateCeilFloorPair(@NotNull SimpleBlock ceil, @NotNull SimpleBlock floor, int height) {
      byte r;
      int h;
      if (GenUtils.chance(this.rand, 1, 200)) {
         r = 2;
         h = GenUtils.randInt(this.rand, height / 4, (int)(1.5F * ((float)height / 2.0F)));
         (new StalactiteBuilder(new Material[]{BlockUtils.stoneOrSlateWall(ceil.getY())})).setSolidBlockType(BlockUtils.stoneOrSlate(ceil.getY())).makeSpike(ceil, (double)r, h, false);
      }

      if (GenUtils.chance(this.rand, 1, 200)) {
         r = 2;
         h = GenUtils.randInt(this.rand, height / 4, (int)(1.5F * ((float)height / 2.0F)));
         (new StalactiteBuilder(new Material[]{BlockUtils.stoneOrSlateWall(floor.getY())})).setSolidBlockType(BlockUtils.stoneOrSlate(floor.getY())).makeSpike(floor, (double)r, h, true);
      }

      if (BlockUtils.isWet(floor.getUp()) && GenUtils.chance(this.rand, 4, 100)) {
         SeaPickle sp = (SeaPickle)Bukkit.createBlockData(Material.SEA_PICKLE);
         sp.setPickles(GenUtils.randInt(1, 2));
         floor.getUp().setBlockData(sp);
      }

   }

   public void populate(@NotNull PopulatorDataAbstract data, CubeRoom room) {
      if (room instanceof LargeCaveRoomPiece) {
         LargeCaveRoomPiece caveRoom = (LargeCaveRoomPiece)room;

         assert data.getChunkX() == room.getX() >> 4;

         assert data.getChunkZ() == room.getZ() >> 4;

         caveRoom.ceilFloorPairs.forEach((l, pair) -> {
            if (pair.z() != LargeCaveRoomCarver.FLOOR_CEIL_NULL) {
               this.populateFloor(new SimpleBlock(data, l.x(), pair.z(), l.z()), caveRoom.waterLevel);
            }

            if (pair.x() != LargeCaveRoomCarver.FLOOR_CEIL_NULL && pair.z() != LargeCaveRoomCarver.FLOOR_CEIL_NULL) {
               SimpleBlock ceil = new SimpleBlock(data, l.x(), pair.x(), l.z());
               SimpleBlock floor = new SimpleBlock(data, l.x(), pair.z(), l.z());
               int height = ceil.getY() - floor.getY();
               this.populateCeilFloorPair(ceil, floor, height);
            }

         });
      } else {
         throw new NotImplementedException("room for LargeCavePopulator was not a LargeCaveRoomPiece");
      }
   }

   public boolean canPopulate(CubeRoom room) {
      return true;
   }
}
