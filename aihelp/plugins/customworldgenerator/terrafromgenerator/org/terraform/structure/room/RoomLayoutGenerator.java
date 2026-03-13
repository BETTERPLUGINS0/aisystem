package org.terraform.structure.room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.data.Wall;
import org.terraform.main.TLogger;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.structure.room.carver.RoomCarver;
import org.terraform.structure.room.path.PathState;
import org.terraform.utils.GenUtils;
import org.terraform.utils.MazeSpawner;

public class RoomLayoutGenerator {
   private final HashSet<CubeRoom> rooms = new HashSet();
   @NotNull
   private final int[] upperBound;
   @NotNull
   private final int[] lowerBound;
   private final RoomLayout layout;
   private final HashSet<LegacyPathGenerator> pathGens = new HashSet();
   private final HashSet<PathPopulatorData> pathPopulators = new HashSet();
   private final ArrayList<RoomPopulatorAbstract> roomPops = new ArrayList();
   @Nullable
   public RoomCarver roomCarver = null;
   @NotNull
   public Material[] wallMaterials;
   boolean genPaths;
   boolean allowOverlaps;
   private PathState pathState;
   private int numRooms;
   private int centX;
   private int centY;
   private int centZ;
   private PathPopulatorAbstract pathPop;
   private int roomMaxHeight;
   private int roomMinHeight;
   private int roomMaxX;
   private int roomMinX;
   private int roomMaxZ;
   private int roomMinZ;
   private int range;
   private Random rand;
   private boolean carveRooms;
   private float xCarveMul;
   private float yCarveMul;
   private float zCarveMul;
   private boolean pyramidish;
   private MazeSpawner mazePathGenerator;
   private int tile;

   public RoomLayoutGenerator(Random random, RoomLayout layout, int numRooms, int centX, int centY, int centZ, int range) {
      this.wallMaterials = new Material[]{Material.CAVE_AIR};
      this.genPaths = true;
      this.allowOverlaps = false;
      this.roomMaxHeight = 7;
      this.roomMinHeight = 5;
      this.roomMaxX = 15;
      this.roomMinX = 10;
      this.roomMaxZ = 15;
      this.roomMinZ = 10;
      this.carveRooms = false;
      this.xCarveMul = 1.0F;
      this.yCarveMul = 1.0F;
      this.zCarveMul = 1.0F;
      this.pyramidish = false;
      this.tile = -1;
      this.numRooms = numRooms;
      this.layout = layout;
      this.centX = centX;
      this.centY = centY;
      this.centZ = centZ;
      this.rand = random;
      this.range = range;
      this.upperBound = new int[]{centX + range / 2, centZ + range / 2};
      this.lowerBound = new int[]{centX - range / 2, centZ - range / 2};
   }

   @NotNull
   public PathState getOrCalculatePathState(@NotNull TerraformWorld tw) {
      if (this.pathState == null) {
         this.pathState = new PathState(this, tw);
      }

      return this.pathState;
   }

   public int[] getCenter() {
      return new int[]{this.centX, this.centY, this.centZ};
   }

   public void setPathPopulator(PathPopulatorAbstract pop) {
      this.pathPop = pop;
   }

   public void setCarveRooms(boolean carve) {
      this.carveRooms = carve;
   }

   public void setCarveRoomsMultiplier(float xMul, float yMul, float zMul) {
      this.xCarveMul = xMul;
      this.yCarveMul = yMul;
      this.zCarveMul = zMul;
   }

   public void registerRoomPopulator(RoomPopulatorAbstract pop) {
      this.roomPops.add(pop);
   }

   public void reset() {
      this.rooms.clear();
   }

   public void calculateRoomPlacement() {
      this.calculateRoomPlacement(true);
   }

   public void setPyramid(boolean pyramidish) {
      this.pyramidish = pyramidish;
   }

   @Nullable
   public CubeRoom forceAddRoom(int widthX, int widthZ, int heightY) {
      if (this.layout != RoomLayout.RANDOM_BRUTEFORCE) {
         return null;
      } else {
         CubeRoom room = new CubeRoom(widthX, widthZ, heightY, this.centX + GenUtils.randInt(this.rand, -this.range / 2, this.range / 2), this.centY, this.centZ + GenUtils.randInt(this.rand, -this.range / 2, this.range / 2));
         boolean canAdd = false;

         while(true) {
            while(true) {
               do {
                  if (canAdd) {
                     this.rooms.add(room);
                     return room;
                  }

                  canAdd = true;
               } while(this.allowOverlaps);

               Iterator var6 = this.rooms.iterator();

               while(var6.hasNext()) {
                  CubeRoom other = (CubeRoom)var6.next();
                  if (other.isOverlapping(room)) {
                     canAdd = false;
                     room = new CubeRoom(widthX, widthZ, heightY, this.centX + GenUtils.randInt(this.rand, -this.range / 2, this.range / 2), this.centY, this.centZ + GenUtils.randInt(this.rand, -this.range / 2, this.range / 2));
                     break;
                  }
               }
            }
         }
      }
   }

   public void calculateRoomPlacement(boolean normalise) {
      for(int i = 0; i < this.numRooms; ++i) {
         int widthX = GenUtils.randInt(this.rand, this.roomMinX, this.roomMaxX);
         int widthZ = GenUtils.randInt(this.rand, this.roomMinZ, this.roomMaxZ);
         int nx = GenUtils.randInt(this.rand, -this.range / 2, this.range / 2);
         int nz = GenUtils.randInt(this.rand, -this.range / 2, this.range / 2);
         if (normalise) {
            if (widthX < widthZ / 2) {
               widthX = widthZ + GenUtils.randInt(this.rand, -2, 2);
            }

            if (widthZ < widthX / 2) {
               widthZ = widthX + GenUtils.randInt(this.rand, -2, 2);
            }
         }

         int heightY = GenUtils.randInt(this.rand, this.roomMinHeight, this.roomMaxHeight);
         if (this.pyramidish) {
            double heightRange = 1.0D - (Math.pow((double)nx, 2.0D) + Math.pow((double)nz, 2.0D)) / Math.pow((double)((float)this.range / 2.0F), 2.0D);
            if (heightRange * (double)this.roomMaxHeight < (double)this.roomMinHeight) {
               heightRange = (double)((float)this.roomMinHeight / (float)this.roomMaxHeight);
            }

            heightY = GenUtils.randInt(this.rand, this.roomMinHeight, (int)((double)this.roomMaxHeight * heightRange));
         }

         if (normalise) {
            if (heightY > widthX) {
               heightY = widthX + GenUtils.randInt(this.rand, -2, 2);
            }

            if (heightY < widthX / 3) {
               heightY = widthX / 3 + GenUtils.randInt(this.rand, -2, 2);
            }
         }

         CubeRoom room = new CubeRoom(widthX, widthZ, heightY, nx + this.centX, this.centY, nz + this.centZ);
         boolean canAdd;
         Iterator var10;
         CubeRoom other;
         if (this.layout == RoomLayout.RANDOM_BRUTEFORCE) {
            canAdd = true;
            if (!this.allowOverlaps) {
               var10 = this.rooms.iterator();

               while(var10.hasNext()) {
                  other = (CubeRoom)var10.next();
                  if (other.isOverlapping(room)) {
                     canAdd = false;
                     break;
                  }
               }
            }

            if (canAdd) {
               this.rooms.add(room);
            }
         } else if (this.layout == RoomLayout.OVERLAP_CONNECTED) {
            room.setX(this.centX + GenUtils.randInt(this.rand, -4, 4));
            room.setZ(this.centZ + GenUtils.randInt(this.rand, -4, 4));
            canAdd = true;
            var10 = this.rooms.iterator();

            while(var10.hasNext()) {
               other = (CubeRoom)var10.next();
               if (other.envelopesOrIsInside(room)) {
                  canAdd = false;
                  break;
               }
            }

            var10 = this.rooms.iterator();

            while(var10.hasNext()) {
               other = (CubeRoom)var10.next();
               if (!other.isOverlapping(room)) {
                  canAdd = false;
                  break;
               }
            }

            if (canAdd) {
               this.rooms.add(room);
            }
         }
      }

   }

   public boolean anyOverlaps() {
      if (this.allowOverlaps) {
         return false;
      } else {
         Iterator var1 = this.rooms.iterator();

         while(var1.hasNext()) {
            CubeRoom room = (CubeRoom)var1.next();
            Iterator var3 = this.rooms.iterator();

            while(var3.hasNext()) {
               CubeRoom other = (CubeRoom)var3.next();
               if (!other.isClone(room) && room.isOverlapping(other)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public boolean isInRoom(@NotNull int[] coords) {
      Iterator var2 = this.rooms.iterator();

      CubeRoom room;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         room = (CubeRoom)var2.next();
      } while(!room.isPointInside(coords));

      return true;
   }

   public void setGenPaths(boolean genPaths) {
      this.genPaths = genPaths;
   }

   public void setAllowOverlaps(boolean allowOverlaps) {
      this.allowOverlaps = allowOverlaps;
   }

   public void calculateRoomPopulators(@NotNull TerraformWorld tw) {
      Iterator it = this.roomPops.iterator();

      while(true) {
         while(true) {
            RoomPopulatorAbstract pops;
            do {
               if (!it.hasNext()) {
                  if (this.roomPops.isEmpty()) {
                     return;
                  }

                  Iterator var9 = this.rooms.iterator();

                  while(var9.hasNext()) {
                     CubeRoom room = (CubeRoom)var9.next();
                     if (room.pop == null) {
                        Random rand = tw.getHashedRand((long)room.getX(), room.getY(), room.getZ());
                        List<RoomPopulatorAbstract> shuffled = (List)this.roomPops.clone();
                        Collections.shuffle(shuffled, rand);
                        Iterator var7 = shuffled.iterator();

                        while(var7.hasNext()) {
                           RoomPopulatorAbstract roomPop = (RoomPopulatorAbstract)var7.next();
                           if (roomPop.canPopulate(room)) {
                              room.setRoomPopulator(roomPop);
                              if (roomPop.isUnique()) {
                                 this.roomPops.remove(roomPop);
                              }
                              break;
                           }
                        }
                     }

                     TLogger var10000;
                     if (room.pop != null) {
                        var10000 = TerraformGeneratorPlugin.logger;
                        String var10001 = room.pop.getClass().getName();
                        var10000.info("Registered: " + var10001 + " at " + room.getX() + " " + room.getY() + " " + room.getZ() + " in a room of size " + room.getWidthX() + "x" + room.getWidthZ());
                     } else {
                        var10000 = TerraformGeneratorPlugin.logger;
                        int var12 = room.getX();
                        var10000.info("Registered: plain room at " + var12 + " " + room.getY() + " " + room.getZ() + " in a room of size " + room.getWidthX() + "x" + room.getWidthZ());
                     }
                  }

                  return;
               }

               pops = (RoomPopulatorAbstract)it.next();
            } while(!pops.isForceSpawn());

            Iterator var4 = this.rooms.iterator();

            while(var4.hasNext()) {
               CubeRoom room = (CubeRoom)var4.next();
               if (room.pop == null && pops.canPopulate(room)) {
                  room.setRoomPopulator(pops);
                  if (pops.isUnique()) {
                     it.remove();
                  }
                  break;
               }
            }
         }
      }
   }

   /** @deprecated */
   @Deprecated
   public void runRoomPopulators(PopulatorDataAbstract data, @NotNull TerraformWorld tw) {
      this.calculateRoomPopulators(tw);
      if (!this.roomPops.isEmpty()) {
         this.rooms.forEach((room) -> {
            room.populate(data);
         });
      }
   }

   public void fill(@NotNull PopulatorDataAbstract data, @NotNull TerraformWorld tw, Material... mat) {
      Iterator var4;
      if (this.genPaths) {
         if (this.mazePathGenerator != null) {
            this.mazePathGenerator.setRand(this.rand);
            this.mazePathGenerator.setCore(new SimpleBlock(data, this.getCentX(), this.getCentY() + 1, this.getCentZ()));
            if (this.mazePathGenerator.getWidthX() == -1) {
               this.mazePathGenerator.setWidthX(this.range);
            }

            if (this.mazePathGenerator.getWidthZ() == -1) {
               this.mazePathGenerator.setWidthZ(this.range);
            }

            this.mazePathGenerator.prepareMaze();
            this.mazePathGenerator.carveMaze(false, mat);
         } else {
            var4 = this.rooms.iterator();

            while(var4.hasNext()) {
               CubeRoom room = (CubeRoom)var4.next();
               SimpleBlock base = new SimpleBlock(data, room.getX() + room.getX() % (this.pathPop.getPathWidth() * 2 + 1), room.getY(), room.getZ() + room.getZ() % (this.pathPop.getPathWidth() * 2 + 1));
               LegacyPathGenerator gen = new LegacyPathGenerator(base, mat, this.rand, this.upperBound, this.lowerBound, this.pathPop.getPathMaxBend());
               if (this.pathPop != null) {
                  gen.setPopulator(this.pathPop);
               }

               while(!gen.isDead()) {
                  gen.placeNext();
               }

               this.pathGens.add(gen);
            }
         }
      }

      var4 = this.rooms.iterator();

      while(var4.hasNext()) {
         CubeRoom room = (CubeRoom)var4.next();
         if (this.carveRooms) {
            room = new CarvedRoom((CubeRoom)room);
            if (((CubeRoom)room).largerThanVolume(40000)) {
               ((CarvedRoom)room).setFrequency(0.03F);
            }

            ((CarvedRoom)room).setxMultiplier(this.xCarveMul);
            ((CarvedRoom)room).setyMultiplier(this.yCarveMul);
            ((CarvedRoom)room).setzMultiplier(this.zCarveMul);
         }

         if (this.allowOverlaps) {
            ((CubeRoom)room).fillRoom(data, this.tile, mat, Material.CAVE_AIR);
         } else {
            ((CubeRoom)room).fillRoom(data, this.tile, mat, Material.AIR);
         }
      }

      if (this.mazePathGenerator != null && this.pathPop != null) {
         var4 = this.mazePathGenerator.pathPopDatas.iterator();

         while(var4.hasNext()) {
            PathPopulatorData pPData = (PathPopulatorData)var4.next();
            if (!this.isInRoom(new int[]{pPData.base.getX(), pPData.base.getZ()})) {
               this.pathPop.populate(pPData);
            }
         }
      } else {
         var4 = this.pathGens.iterator();

         while(var4.hasNext()) {
            LegacyPathGenerator pGen = (LegacyPathGenerator)var4.next();
            pGen.populate();
         }
      }

      if (!this.roomPops.isEmpty()) {
         this.runRoomPopulators(data, tw);
      }
   }

   public void carveRoomsOnly(@NotNull PopulatorDataAbstract data, TerraformWorld tw, Material... mat) {
      Iterator var4 = this.rooms.iterator();

      while(var4.hasNext()) {
         CubeRoom room = (CubeRoom)var4.next();
         if (this.carveRooms) {
            room = new CarvedRoom((CubeRoom)room);
            if (((CubeRoom)room).largerThanVolume(40000)) {
               ((CarvedRoom)room).setFrequency(0.03F);
            }

            ((CarvedRoom)room).setxMultiplier(this.xCarveMul);
            ((CarvedRoom)room).setyMultiplier(this.yCarveMul);
            ((CarvedRoom)room).setzMultiplier(this.zCarveMul);
         }

         if (this.allowOverlaps) {
            ((CubeRoom)room).fillRoom(data, this.tile, mat, Material.CAVE_AIR);
         } else {
            ((CubeRoom)room).fillRoom(data, this.tile, mat, Material.AIR);
         }
      }

   }

   public void fillRoomsOnly(@NotNull PopulatorDataAbstract data, @NotNull TerraformWorld tw, Material... mat) {
      this.carveRoomsOnly(data, tw, mat);
      if (!this.roomPops.isEmpty()) {
         this.runRoomPopulators(data, tw);
      }
   }

   public void carvePathsOnly(@NotNull PopulatorDataAbstract data, TerraformWorld tw, Material... mat) {
      if (this.genPaths) {
         Iterator var4 = this.rooms.iterator();

         while(var4.hasNext()) {
            CubeRoom room = (CubeRoom)var4.next();
            SimpleBlock base = new SimpleBlock(data, room.getX(), room.getY(), room.getZ());
            LegacyPathGenerator gen = new LegacyPathGenerator(base, mat, this.rand, this.upperBound, this.lowerBound, this.pathPop.getPathMaxBend());
            if (this.pathPop != null) {
               gen.setPopulator(this.pathPop);
            }

            while(!gen.isDead()) {
               gen.placeNext();
            }

            this.pathGens.add(gen);
         }
      }

   }

   public void populatePathsOnly() {
      Iterator var1 = this.pathGens.iterator();

      while(var1.hasNext()) {
         LegacyPathGenerator pGen = (LegacyPathGenerator)var1.next();
         pGen.populate();
      }

   }

   public void fillPathsOnly(@NotNull PopulatorDataAbstract data, TerraformWorld tw, Material... mat) {
      this.carvePathsOnly(data, tw, mat);
      this.populatePathsOnly();
   }

   public int getNumRooms() {
      return this.numRooms;
   }

   public void setNumRooms(int numRooms) {
      this.numRooms = numRooms;
   }

   public int getRange() {
      return this.range;
   }

   public void setRange(int range) {
      this.range = range;
   }

   public int getRoomMaxHeight() {
      return this.roomMaxHeight;
   }

   public void setRoomMaxHeight(int roomMaxHeight) {
      this.roomMaxHeight = roomMaxHeight;
   }

   public int getRoomMinHeight() {
      return this.roomMinHeight;
   }

   public void setRoomMinHeight(int roomMinHeight) {
      this.roomMinHeight = roomMinHeight;
   }

   @NotNull
   public HashSet<CubeRoom> getRooms() {
      return this.rooms;
   }

   public int getRoomMaxX() {
      return this.roomMaxX;
   }

   public void setRoomMaxX(int roomMaxX) {
      this.roomMaxX = roomMaxX;
   }

   public int getRoomMinX() {
      return this.roomMinX;
   }

   public void setRoomMinX(int roomMinX) {
      this.roomMinX = roomMinX;
   }

   public int getRoomMaxZ() {
      return this.roomMaxZ;
   }

   public void setRoomMaxZ(int roomMaxZ) {
      this.roomMaxZ = roomMaxZ;
   }

   public int getRoomMinZ() {
      return this.roomMinZ;
   }

   public void setRoomMinZ(int roomMinZ) {
      this.roomMinZ = roomMinZ;
   }

   public int getCentX() {
      return this.centX;
   }

   public void setCentX(int centX) {
      this.centX = centX;
   }

   public int getCentY() {
      return this.centY;
   }

   public void setCentY(int centY) {
      this.centY = centY;
   }

   public int getCentZ() {
      return this.centZ;
   }

   public void setCentZ(int centZ) {
      this.centZ = centZ;
   }

   public Random getRand() {
      return this.rand;
   }

   public void setRand(Random rand) {
      this.rand = rand;
   }

   public PathPopulatorAbstract getPathPop() {
      return this.pathPop;
   }

   public void setTile(int tile) {
      this.tile = tile;
   }

   public void setMazePathGenerator(MazeSpawner mazePathGenerator) {
      this.mazePathGenerator = mazePathGenerator;
   }

   @NotNull
   public HashSet<PathPopulatorData> getPathPopulators() {
      if (this.pathPopulators.isEmpty()) {
         Iterator var1 = this.pathGens.iterator();

         while(var1.hasNext()) {
            LegacyPathGenerator pGen = (LegacyPathGenerator)var1.next();
            this.pathPopulators.addAll(pGen.path);
         }
      }

      return this.pathPopulators;
   }

   /** @deprecated */
   @Deprecated
   public boolean isPointInPath(@NotNull Wall w, int rearOffset, int includeWidth) {
      if (this.getPathPopulators().contains(new PathPopulatorData(w.getRear(rearOffset).getAtY(this.centY), 3))) {
         return true;
      } else {
         if (includeWidth != 0) {
            for(int i = 1; i < includeWidth; ++i) {
               if (this.getPathPopulators().contains(new PathPopulatorData(w.getRear(rearOffset).getLeft(i).getAtY(this.centY), 3))) {
                  return true;
               }

               if (this.getPathPopulators().contains(new PathPopulatorData(w.getRear(rearOffset).getRight(i).getAtY(this.centY), 3))) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public boolean genPaths() {
      return this.genPaths;
   }
}
