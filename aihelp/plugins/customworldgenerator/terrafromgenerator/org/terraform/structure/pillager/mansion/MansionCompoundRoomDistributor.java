package org.terraform.structure.pillager.mansion;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.data.SimpleLocation;
import org.terraform.main.TLogger;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.structure.room.jigsaw.JigsawStructurePiece;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class MansionCompoundRoomDistributor {
   @NotNull
   public static final HashMap<MansionRoomSize, ArrayList<MansionRoomPopulator>> groundFloorPopulators;
   @NotNull
   public static final HashMap<MansionRoomSize, ArrayList<MansionRoomPopulator>> secondFloorPopulators;

   public static void distributeRooms(@NotNull Collection<JigsawStructurePiece> pieces, @NotNull Random random, boolean isGround) {
      ArrayList<JigsawStructurePiece> shuffledList = new ArrayList(pieces);
      ArrayList<MansionRoomSize> potentialRoomSizes = new ArrayList();
      int occupiedCells = 13;
      HashMap activeRoomPool;
      if (isGround) {
         activeRoomPool = groundFloorPopulators;
         potentialRoomSizes.add(new MansionRoomSize(3, 3));
      } else {
         activeRoomPool = secondFloorPopulators;
      }

      potentialRoomSizes.add(new MansionRoomSize(2, 2));

      while(true) {
         while((double)occupiedCells / (double)pieces.size() < 0.7D || GenUtils.chance(random, pieces.size() - occupiedCells / 4, pieces.size())) {
            if ((double)occupiedCells / (double)pieces.size() < 0.5D && GenUtils.chance(random, 1, 3)) {
               occupiedCells += 4;
               potentialRoomSizes.add(new MansionRoomSize(2, 2));
            } else {
               occupiedCells += 2;
               if (random.nextBoolean()) {
                  potentialRoomSizes.add(new MansionRoomSize(2, 1));
               } else {
                  potentialRoomSizes.add(new MansionRoomSize(1, 2));
               }
            }
         }

         Iterator var7 = potentialRoomSizes.iterator();

         while(true) {
            TLogger var10000;
            String var10001;
            while(var7.hasNext()) {
               MansionRoomSize roomSize = (MansionRoomSize)var7.next();
               Collections.shuffle(shuffledList);
               Iterator var9 = shuffledList.iterator();

               while(var9.hasNext()) {
                  JigsawStructurePiece piece = (JigsawStructurePiece)var9.next();
                  Collections.shuffle((List)activeRoomPool.get(roomSize), random);
                  ArrayList<MansionRoomPopulator> populators = (ArrayList)activeRoomPool.get(roomSize);
                  if (populators.isEmpty()) {
                     activeRoomPool.put(roomSize, MansionRoomPopulatorRegistry.getByRoomSize(roomSize, isGround).getPopulators());
                     populators = (ArrayList)activeRoomPool.get(roomSize);
                  }

                  MansionRoomPopulator populator = ((MansionRoomPopulator)populators.get(0)).getInstance(piece.getRoom(), ((MansionStandardRoomPiece)piece).internalWalls);
                  if (canRoomSizeFitWithCenter((MansionStandardRoomPiece)piece, pieces, roomSize, populator, false)) {
                     var10000 = TerraformGeneratorPlugin.logger;
                     var10001 = populator.getClass().getSimpleName();
                     var10000.info(var10001 + " generating at " + String.valueOf(piece.getRoom().getSimpleLocation()));
                     ((MansionStandardRoomPiece)piece).setRoomPopulator(populator);
                     populators.remove(0);
                     break;
                  }
               }
            }

            var7 = pieces.iterator();

            while(var7.hasNext()) {
               JigsawStructurePiece piece = (JigsawStructurePiece)var7.next();
               MansionRoomSize roomSize = new MansionRoomSize(1, 1);
               if (((MansionStandardRoomPiece)piece).getRoomPopulator() == null) {
                  Collections.shuffle((List)activeRoomPool.get(roomSize), random);
                  MansionRoomPopulator populator = ((MansionRoomPopulator)((ArrayList)activeRoomPool.get(roomSize)).get(0)).getInstance(piece.getRoom(), ((MansionStandardRoomPiece)piece).internalWalls);
                  var10000 = TerraformGeneratorPlugin.logger;
                  var10001 = populator.getClass().getSimpleName();
                  var10000.info(var10001 + " generating at " + String.valueOf(piece.getRoom().getSimpleLocation()));
                  ((MansionStandardRoomPiece)piece).setRoomPopulator(populator);
               }
            }

            return;
         }
      }
   }

   public static boolean canRoomSizeFitWithCenter(@NotNull MansionStandardRoomPiece piece, @NotNull Collection<JigsawStructurePiece> pieces, @NotNull MansionRoomSize roomSize, @NotNull MansionRoomPopulator defaultPopulator, boolean force) {
      SimpleLocation center = piece.getRoom().getSimpleLocation();
      ArrayList<SimpleLocation> relevantLocations = new ArrayList();
      relevantLocations.add(center);
      if (roomSize.getWidthX() == 2) {
         relevantLocations.add(center.getRelative(BlockFace.EAST, 9));
      }

      if (roomSize.getWidthZ() == 2) {
         relevantLocations.add(center.getRelative(BlockFace.SOUTH, 9));
      }

      if (roomSize.getWidthZ() == 2 && roomSize.getWidthX() == 2) {
         relevantLocations.add(center.getRelative(BlockFace.SOUTH_EAST, 9));
      }

      if (roomSize.getWidthX() == 3 && roomSize.getWidthZ() == 3) {
         BlockFace[] var7 = BlockUtils.xzPlaneBlockFaces;
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            BlockFace face = var7[var9];
            relevantLocations.add(center.getRelative(face, 9));
         }
      }

      int hits = 0;
      Iterator var14 = pieces.iterator();

      JigsawStructurePiece p;
      while(var14.hasNext()) {
         p = (JigsawStructurePiece)var14.next();
         if (relevantLocations.contains(p.getRoom().getSimpleLocation())) {
            if (!force && ((MansionStandardRoomPiece)p).getRoomPopulator() != null) {
               return false;
            }

            ++hits;
         }
      }

      if (hits < relevantLocations.size()) {
         return false;
      } else {
         var14 = pieces.iterator();

         while(true) {
            do {
               if (!var14.hasNext()) {
                  return true;
               }

               p = (JigsawStructurePiece)var14.next();
            } while(!relevantLocations.contains(p.getRoom().getSimpleLocation()));

            MansionStandardRoomPiece spiece = (MansionStandardRoomPiece)p;
            spiece.setRoomPopulator(defaultPopulator.getInstance(p.getRoom(), spiece.internalWalls), false);
            Iterator var11 = spiece.adjacentPieces.keySet().iterator();

            while(var11.hasNext()) {
               BlockFace face = (BlockFace)var11.next();
               if (relevantLocations.contains(((MansionStandardRoomPiece)spiece.adjacentPieces.get(face)).getRoom().getSimpleLocation())) {
                  spiece.internalWalls.remove(face);
                  ((MansionStandardRoomPiece)spiece.adjacentPieces.get(face)).internalWalls.remove(face.getOppositeFace());
               }
            }
         }
      }
   }

   static {
      groundFloorPopulators = Maps.newHashMap(Map.of(new MansionRoomSize(3, 3), MansionRoomPopulatorRegistry.GROUND_3_3.getPopulators(), new MansionRoomSize(2, 2), MansionRoomPopulatorRegistry.GROUND_2_2.getPopulators(), new MansionRoomSize(1, 2), MansionRoomPopulatorRegistry.GROUND_1_2.getPopulators(), new MansionRoomSize(2, 1), MansionRoomPopulatorRegistry.GROUND_2_1.getPopulators(), new MansionRoomSize(1, 1), MansionRoomPopulatorRegistry.GROUND_1_1.getPopulators()));
      secondFloorPopulators = Maps.newHashMap(Map.of(new MansionRoomSize(3, 3), MansionRoomPopulatorRegistry.SECOND_3_3.getPopulators(), new MansionRoomSize(2, 2), MansionRoomPopulatorRegistry.SECOND_2_2.getPopulators(), new MansionRoomSize(1, 2), MansionRoomPopulatorRegistry.SECOND_1_2.getPopulators(), new MansionRoomSize(2, 1), MansionRoomPopulatorRegistry.SECOND_2_1.getPopulators(), new MansionRoomSize(1, 1), MansionRoomPopulatorRegistry.SECOND_1_1.getPopulators()));
   }
}
