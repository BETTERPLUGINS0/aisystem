package org.terraform.structure.village.plains;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Slab.Type;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.Wall;
import org.terraform.main.TLogger;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.PathPopulatorAbstract;
import org.terraform.structure.room.PathPopulatorData;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.blockdata.SlabBuilder;

public class PlainsPathRecursiveSpawner {
   private static final int minPathLength = 25;
   private static final int maxPathLength = 35;
   private final int range;
   private final ArrayList<RoomPopulatorAbstract> validRooms = new ArrayList();
   @NotNull
   private final SimpleBlock core;
   private final HashMap<SimpleLocation, DirectionalCubeRoom> rooms = new HashMap();
   private final HashMap<SimpleLocation, BlockFace> path = new HashMap();
   private final HashMap<SimpleLocation, PlainsPathRecursiveSpawner.CrossRoad> crossRoads = new HashMap();
   private int minRoomWidth = 15;
   private int maxRoomWidth = 20;
   private double villageDensity = 1.0D;
   private PathPopulatorAbstract pathPop;

   public PlainsPathRecursiveSpawner(@NotNull SimpleBlock core, int range, BlockFace... faces) {
      SimpleLocation start = new SimpleLocation(core.getX(), 0, core.getZ());
      this.crossRoads.put(start, new PlainsPathRecursiveSpawner.CrossRoad(start, faces));
      this.range = range;
      this.core = core;
   }

   private void advanceCrossRoad(@NotNull Random random, @NotNull PlainsPathRecursiveSpawner.CrossRoad target, @NotNull BlockFace direction) {
      target.satisfiedFaces.add(direction);
      boolean cull = false;
      SimpleLocation loc = (new SimpleLocation(target.loc)).getRelative(direction);
      int lastCrossroad = 0;

      for(int edgeTurns = 0; !cull; loc = loc.getRelative(direction)) {
         for(int i = 0; i < GenUtils.randInt(random, 25, 35); ++i) {
            if (this.isLocationValid(loc)) {
               this.path.put(loc, direction);
               if (GenUtils.chance(random, (int)(this.villageDensity * 10000.0D), 10000)) {
                  BlockFace adjDir = BlockUtils.getAdjacentFaces(direction)[random.nextInt(2)];
                  SimpleLocation adj = loc.getRelative(adjDir);
                  if (this.isLocationValid(adj)) {
                     BlockFace rF = adjDir.getOppositeFace();
                     int minRoomWidth = this.minRoomWidth;
                     int maxRoomWidth = this.maxRoomWidth;
                     int smallRoomChance = 10;
                     if (GenUtils.chance(random, smallRoomChance, 100)) {
                        minRoomWidth = 7;
                        maxRoomWidth = 10;
                     }

                     int roomWidthX = GenUtils.randInt(minRoomWidth, maxRoomWidth);
                     int roomWidthZ = GenUtils.randInt(minRoomWidth, maxRoomWidth);
                     DirectionalCubeRoom room = new DirectionalCubeRoom(rF, roomWidthX, roomWidthZ, 20, loc.getX() + adjDir.getModX() * (2 + roomWidthX / 2), loc.getY(), loc.getZ() + adjDir.getModZ() * (2 + roomWidthZ / 2));
                     if (!this.registerRoom(room) && GenUtils.chance(random, lastCrossroad, 20)) {
                        this.crossRoads.put(loc, new PlainsPathRecursiveSpawner.CrossRoad(loc, BlockUtils.getAdjacentFaces(direction)));
                        lastCrossroad = 0;
                     }
                  }
               } else if (GenUtils.chance(random, lastCrossroad, 20)) {
                  this.crossRoads.put(loc, new PlainsPathRecursiveSpawner.CrossRoad(loc, BlockUtils.getAdjacentFaces(direction)));
                  lastCrossroad = 0;
               }

               loc = loc.getRelative(direction);
            } else if ((double)loc.distanceSqr(this.core.getX(), this.core.getY(), this.core.getZ()) > Math.pow((double)this.range, 2.0D)) {
               loc = loc.getRelative(direction.getOppositeFace());
               direction = BlockUtils.getTurnBlockFace(random, direction);
               loc = loc.getRelative(direction);
               ++edgeTurns;
               if (edgeTurns > 3) {
                  cull = true;
               }
            } else {
               cull = true;
            }
         }

         edgeTurns = 0;
         direction = BlockUtils.getTurnBlockFace(random, direction);
      }

   }

   public void registerRoomPopulator(RoomPopulatorAbstract roomPop) {
      this.validRooms.add(roomPop);
   }

   private boolean isLocationValid(@NotNull SimpleLocation loc) {
      Iterator var2 = this.rooms.values().iterator();

      DirectionalCubeRoom room;
      do {
         if (!var2.hasNext()) {
            if ((double)loc.distanceSqr(this.core.getX(), this.core.getY(), this.core.getZ()) > Math.pow((double)this.range, 2.0D)) {
               return false;
            }

            return !this.path.containsKey(loc);
         }

         room = (DirectionalCubeRoom)var2.next();
      } while(!room.isPointInside(loc));

      return false;
   }

   public boolean registerRoom(@NotNull DirectionalCubeRoom room) {
      if (this.core.getPopData().getType(room.getX(), GenUtils.getHighestGround(this.core.getPopData(), room.getX(), room.getZ()) + 1, room.getZ()) == Material.WATER) {
         return false;
      } else {
         Iterator var2 = this.rooms.values().iterator();

         DirectionalCubeRoom other;
         do {
            if (!var2.hasNext()) {
               var2 = this.path.keySet().iterator();

               SimpleLocation loc;
               do {
                  if (!var2.hasNext()) {
                     this.rooms.put(new SimpleLocation(room.getX(), 0, room.getZ()), room);
                     return true;
                  }

                  loc = (SimpleLocation)var2.next();
               } while(!room.isPointInside(loc));

               return false;
            }

            other = (DirectionalCubeRoom)var2.next();
         } while(!other.isOverlapping(room));

         return false;
      }
   }

   public void forceRegisterRoom(@NotNull DirectionalCubeRoom room) {
      this.rooms.put(new SimpleLocation(room.getX(), 0, room.getZ()), room);
   }

   public void generate(@NotNull Random random) {
      while(this.getFirstUnsatisfiedCrossRoad() != null) {
         PlainsPathRecursiveSpawner.CrossRoad target = this.getFirstUnsatisfiedCrossRoad();
         BlockFace direction = target.getFirstUnsatisfiedDirection();
         this.advanceCrossRoad(random, target, direction);
      }

   }

   public void build(@NotNull Random random) {
      Iterator it = this.path.keySet().iterator();

      while(true) {
         while(it.hasNext()) {
            SimpleLocation loc = (SimpleLocation)it.next();
            Wall w = new Wall(new SimpleBlock(this.core.getPopData(), loc.getX(), loc.getY(), loc.getZ()), (BlockFace)this.path.get(loc));
            w = w.getGround();
            if (BlockUtils.isWet(w.getUp().get())) {
               if (BlockUtils.isWet(w.getAtY(TerraformGenerator.seaLevel))) {
                  w = w.getAtY(TerraformGenerator.seaLevel);
               } else {
                  w = w.getGroundOrDry().getDown();
               }

               (new SlabBuilder(Material.OAK_SLAB)).setWaterlogged(true).setType(Type.TOP).apply(w).lapply(w.getRelative(0, 0, 1)).lapply(w.getRelative(0, 0, -1)).lapply(w.getRelative(1, 0, 1)).lapply(w.getRelative(1, 0, -1)).lapply(w.getRelative(-1, 0, 1)).lapply(w.getRelative(-1, 0, -1)).lapply(w.getRelative(1, 0, 0)).lapply(w.getRelative(-1, 0, 0));
            } else {
               if (!w.getUp(2).isSolid() && w.getUp(2).getType() != Material.AIR) {
                  w.getUp(2).setType(Material.AIR);
               }

               if (!w.getUp().isSolid() && w.getUp().getType() != Material.AIR) {
                  w.getUp().setType(Material.AIR);
               }

               w.setType(Material.DIRT_PATH);
               BlockFace[] var5 = BlockUtils.xzPlaneBlockFaces;
               int var6 = var5.length;

               for(int var7 = 0; var7 < var6; ++var7) {
                  BlockFace face = var5[var7];
                  Wall target = w.getRelative(face).getGround();
                  if (random.nextInt(3) != 0) {
                     if (!target.getUp(2).isSolid() && target.getUp(2).getType() != Material.AIR) {
                        target.getUp(2).setType(Material.AIR);
                     }

                     if (!target.getUp().isSolid() && target.getUp().getType() != Material.AIR) {
                        target.getUp().setType(Material.AIR);
                     }

                     target.setType(Material.DIRT_PATH);
                  }
               }
            }
         }

         if (this.validRooms.isEmpty()) {
            return;
         }

         it = this.validRooms.iterator();

         while(true) {
            while(true) {
               RoomPopulatorAbstract pops;
               do {
                  if (!it.hasNext()) {
                     if (this.validRooms.isEmpty()) {
                        return;
                     }

                     Iterator var11 = this.rooms.values().iterator();

                     while(var11.hasNext()) {
                        CubeRoom room = (CubeRoom)var11.next();
                        if (room.getPop() == null) {
                           List<RoomPopulatorAbstract> shuffled = (List)this.validRooms.clone();
                           Collections.shuffle(shuffled, random);
                           Iterator var16 = shuffled.iterator();

                           while(var16.hasNext()) {
                              RoomPopulatorAbstract roomPop = (RoomPopulatorAbstract)var16.next();
                              if (roomPop.canPopulate(room)) {
                                 room.setRoomPopulator(roomPop);
                                 if (roomPop.isUnique()) {
                                    this.validRooms.remove(roomPop);
                                 }
                                 break;
                              }
                           }
                        }

                        if (room.getPop() != null) {
                           TLogger var10000 = TerraformGeneratorPlugin.logger;
                           String var10001 = room.getPop().getClass().getName();
                           var10000.info("Registered: " + var10001 + " at " + room.getX() + " " + room.getY() + " " + room.getZ() + " in a room of size " + room.getWidthX() + "x" + room.getWidthZ());
                           room.populate(this.core.getPopData());
                        }
                     }

                     var11 = this.path.keySet().iterator();

                     while(var11.hasNext()) {
                        SimpleLocation loc = (SimpleLocation)var11.next();
                        if (this.pathPop != null) {
                           this.pathPop.populate(new PathPopulatorData(new SimpleBlock(this.core.getPopData(), loc.getX(), loc.getY(), loc.getZ()), (BlockFace)this.path.get(loc), 3, false));
                        }
                     }

                     return;
                  }

                  pops = (RoomPopulatorAbstract)it.next();
               } while(!pops.isForceSpawn());

               Iterator var13 = this.rooms.values().iterator();

               while(var13.hasNext()) {
                  CubeRoom room = (CubeRoom)var13.next();
                  if (room.getPop() == null && pops.canPopulate(room)) {
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
   }

   @Nullable
   private PlainsPathRecursiveSpawner.CrossRoad getFirstUnsatisfiedCrossRoad() {
      Iterator var1 = this.crossRoads.values().iterator();

      PlainsPathRecursiveSpawner.CrossRoad road;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         road = (PlainsPathRecursiveSpawner.CrossRoad)var1.next();
      } while(road.isSatisfied());

      return road;
   }

   public int getMinRoomWidth() {
      return this.minRoomWidth;
   }

   public void setMinRoomWidth(int minRoomWidth) {
      this.minRoomWidth = minRoomWidth;
   }

   public int getMaxRoomWidth() {
      return this.maxRoomWidth;
   }

   public void setMaxRoomWidth(int maxRoomWidth) {
      this.maxRoomWidth = maxRoomWidth;
   }

   public PathPopulatorAbstract getPathPop() {
      return this.pathPop;
   }

   public void setPathPop(PathPopulatorAbstract pathPop) {
      this.pathPop = pathPop;
   }

   @NotNull
   public HashMap<SimpleLocation, DirectionalCubeRoom> getRooms() {
      return this.rooms;
   }

   public double getVillageDensity() {
      return this.villageDensity;
   }

   public void setVillageDensity(double villageDensity) {
      this.villageDensity = villageDensity;
   }

   private static class CrossRoad {
      public final SimpleLocation loc;
      public final BlockFace[] faces;
      @NotNull
      public final ArrayList<BlockFace> satisfiedFaces = new ArrayList();

      public CrossRoad(SimpleLocation loc, BlockFace[] faces) {
         this.loc = loc;
         this.faces = faces;
      }

      public boolean isSatisfied() {
         return this.getFirstUnsatisfiedDirection() == null;
      }

      @Nullable
      public BlockFace getFirstUnsatisfiedDirection() {
         BlockFace[] var1 = this.faces;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            BlockFace face = var1[var3];
            if (!this.satisfiedFaces.contains(face)) {
               return face;
            }
         }

         return null;
      }
   }
}
