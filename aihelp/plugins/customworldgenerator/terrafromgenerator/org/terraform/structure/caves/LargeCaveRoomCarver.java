package org.terraform.structure.caves;

import java.util.ArrayDeque;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.CoordPair;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.carver.RoomCarver;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.datastructs.CompressedCoordBools;

public class LargeCaveRoomCarver extends RoomCarver {
   final Material fluid;
   int waterLevel = -64;
   public static final int FLOOR_CEIL_NULL;

   public LargeCaveRoomCarver(Material fluid) {
      this.fluid = fluid;
   }

   public void carveRoom(@NotNull PopulatorDataAbstract data, CubeRoom room, Material... wallMaterial) {
      if (!(room instanceof LargeCaveRoomPiece)) {
         throw new NotImplementedException("room for LargeCaveRoomCarver was not a LargeCaveRoomPiece");
      } else {
         LargeCaveRoomPiece caveRoom = (LargeCaveRoomPiece)room;
         if (caveRoom.startingLoc != null) {
            CompressedCoordBools explored = new CompressedCoordBools();
            ArrayDeque<SimpleLocation> queue = new ArrayDeque();
            explored.set(caveRoom.startingLoc);
            queue.add(caveRoom.startingLoc);

            while(true) {
               while(!queue.isEmpty()) {
                  SimpleLocation loc = (SimpleLocation)queue.remove();
                  boolean boundary = caveRoom.boundaries.isSet(loc);
                  BlockFace[] var9 = BlockUtils.sixBlockFaces;
                  int var10 = var9.length;

                  for(int var11 = 0; var11 < var10; ++var11) {
                     BlockFace face = var9[var11];
                     SimpleLocation neighbour = loc.getRelative(face);
                     boolean isNeighbour = caveRoom.toCarve.isSet(neighbour);
                     if (isNeighbour && !explored.isSet(neighbour)) {
                        explored.set(neighbour);
                        queue.add(neighbour);
                     }
                  }

                  if (!boundary) {
                     data.setType(loc.getX(), loc.getY(), loc.getZ(), loc.getY() > this.waterLevel ? Material.CAVE_AIR : this.fluid);
                  } else {
                     if (loc.getY() <= this.waterLevel || BlockUtils.isWet(new SimpleBlock(data, loc)) || BlockUtils.isWet(new SimpleBlock(data, loc.getUp()))) {
                        data.setType(loc.getX(), loc.getY(), loc.getZ(), BlockUtils.stoneOrSlate(loc.getY()));
                     }

                     if (data.getType(loc.getX(), loc.getY(), loc.getZ()).isSolid()) {
                        CoordPair key = new CoordPair(loc.getX(), loc.getZ());
                        CoordPair def;
                        if (!caveRoom.toCarve.isSet(loc.getDown())) {
                           def = (CoordPair)caveRoom.ceilFloorPairs.getOrDefault(key, new CoordPair(FLOOR_CEIL_NULL, FLOOR_CEIL_NULL));
                           caveRoom.ceilFloorPairs.put(key, new CoordPair(def.x(), loc.getY()));
                        } else if (!caveRoom.toCarve.isSet(loc.getUp())) {
                           def = (CoordPair)caveRoom.ceilFloorPairs.getOrDefault(key, new CoordPair(FLOOR_CEIL_NULL, FLOOR_CEIL_NULL));
                           caveRoom.ceilFloorPairs.put(key, new CoordPair(loc.getY(), def.z()));
                        }
                     }
                  }
               }

               return;
            }
         }
      }
   }

   static {
      FLOOR_CEIL_NULL = TerraformGeneratorPlugin.injector.getMinY() - 1;
   }
}
