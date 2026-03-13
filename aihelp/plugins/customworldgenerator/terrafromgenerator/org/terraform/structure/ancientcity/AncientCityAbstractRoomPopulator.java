package org.terraform.structure.ancientcity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.data.Wall;
import org.terraform.structure.room.CarvedRoom;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.PathPopulatorAbstract;
import org.terraform.structure.room.RoomLayoutGenerator;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.structure.room.path.PathState;
import org.terraform.utils.GenUtils;
import org.terraform.utils.StairwayBuilder;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public abstract class AncientCityAbstractRoomPopulator extends RoomPopulatorAbstract {
   protected final RoomLayoutGenerator gen;
   final TerraformWorld tw;
   protected int shrunkenWidth = 0;
   @NotNull
   protected CubeRoom effectiveRoom = null;
   protected HashSet<SimpleBlock> containsPaths = new HashSet();
   protected boolean doCarve = true;

   public AncientCityAbstractRoomPopulator(TerraformWorld tw, RoomLayoutGenerator gen, Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
      this.tw = tw;
      this.gen = gen;
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      this.shrunkenWidth = GenUtils.randInt(this.rand, 2, 4);
      int depression = this.shrunkenWidth;
      if (this.rand.nextBoolean()) {
         depression *= -1;
      }

      this.effectiveRoom = new CarvedRoom(new CubeRoom(room.getWidthX() - this.shrunkenWidth * 2 - 1, room.getWidthZ() - this.shrunkenWidth * 2 - 1, room.getHeight(), room.getX(), room.getY() + depression, room.getZ()));
      if (this.doCarve) {
         this.effectiveRoom.fillRoom(data, Material.CAVE_AIR);
      }

      int[] lowerCorner = this.effectiveRoom.getLowerCorner(0);
      int[] upperCorner = this.effectiveRoom.getUpperCorner(0);
      int y = this.effectiveRoom.getY();

      int relZ;
      for(int x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
         for(int z = lowerCorner[1]; z <= upperCorner[1]; ++z) {
            SimpleBlock b = new SimpleBlock(data, x, y, z);
            if (x != lowerCorner[0] && x != upperCorner[0] && z != lowerCorner[1] && z != upperCorner[1]) {
               b.lsetType(AncientCityUtils.deepslateBricks);
            } else if (this.rand.nextBoolean()) {
               b.lsetType(AncientCityUtils.deepslateBricks);
            }

            int relX = this.effectiveRoom.getX() - x;
            relZ = this.effectiveRoom.getZ() - z;
            if (relX % 5 == 0 && relZ % 5 == 0 && this.effectiveRoom.isPointInside(b.getRelative(BlockFace.NORTH)) && this.effectiveRoom.isPointInside(b.getRelative(BlockFace.SOUTH)) && this.effectiveRoom.isPointInside(b.getRelative(BlockFace.EAST)) && this.effectiveRoom.isPointInside(b.getRelative(BlockFace.WEST))) {
               AncientCityUtils.placeSupportPillar(b.getDown());
            }
         }
      }

      boolean placedStairs = false;
      Iterator var19 = room.getFourWalls(data, 0).entrySet().iterator();

      while(var19.hasNext()) {
         Entry<Wall, Integer> entry = (Entry)var19.next();
         Wall w = ((Wall)entry.getKey()).getDown();

         for(relZ = this.shrunkenWidth; relZ < (Integer)entry.getValue() - this.shrunkenWidth; ++relZ) {
            if (!placedStairs) {
               label80:
               for(int j = 1; j <= 7; ++j) {
                  SimpleLocation target = w.getRear(j).getAtY(room.getY()).getLoc();
                  if (this.gen.getOrCalculatePathState(this.tw).nodes.contains(new PathState.PathNode(target, 1, (PathPopulatorAbstract)null, new BlockFace[0]))) {
                     placedStairs = true;
                     Wall roomEdge = w.getFront(this.shrunkenWidth + 1).getAtY(this.effectiveRoom.getY() + 1);
                     this.containsPaths.add(roomEdge);
                     this.containsPaths.add(roomEdge.getLeft());
                     this.containsPaths.add(roomEdge.getRight());
                     if (depression > 0) {
                        (new StairwayBuilder(new Material[]{Material.DEEPSLATE_BRICK_STAIRS})).setDownTypes(AncientCityUtils.deepslateBricks).setStairwayDirection(BlockFace.DOWN).setStopAtY(room.getY()).build(roomEdge.getDown().getRear().flip()).build(roomEdge.getDown().getRear().getLeft().flip()).build(roomEdge.getDown().getRear().getRight().flip());
                     } else {
                        (new StairwayBuilder(new Material[]{Material.DEEPSLATE_BRICK_STAIRS})).setDownTypes(AncientCityUtils.deepslateBricks).setStairwayDirection(BlockFace.UP).setUpwardsCarveUntilNotSolid(false).setStopAtY(room.getY()).build(roomEdge.getRear().flip()).build(roomEdge.getRear().getLeft().flip()).build(roomEdge.getRear().getRight().flip());
                     }

                     Wall conn = roomEdge.getUp(-depression - 1).getRear(Math.abs(depression) + 1);
                     int conni = 0;

                     while(true) {
                        if (conni > 5) {
                           break label80;
                        }

                        boolean wasBlocked = conn.getRear(conni).lsetType(Material.GRAY_WOOL);
                        wasBlocked &= conn.getRear(conni).getLeft().lsetType(Material.GRAY_WOOL);
                        wasBlocked &= conn.getRear(conni).getRight().lsetType(Material.GRAY_WOOL);
                        conn.getRear(conni).getRight(2).lsetType(AncientCityUtils.deepslateBricks);
                        conn.getRear(conni).getLeft(2).lsetType(AncientCityUtils.deepslateBricks);
                        if (wasBlocked) {
                           break label80;
                        }

                        ++conni;
                     }
                  }
               }
            } else {
               placedStairs = false;
            }

            w = w.getLeft();
         }
      }

   }

   public void sculkUp(TerraformWorld tw, @NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      FastNoise circleNoise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_CAVECLUSTER_CIRCLENOISE, (world) -> {
         FastNoise n = new FastNoise((int)(world.getSeed() * 11L));
         n.SetNoiseType(FastNoise.NoiseType.Simplex);
         n.SetFrequency(0.09F);
         return n;
      });

      for(int i = 0; i <= room.getWidthX() * room.getWidthZ() / 150; ++i) {
         int[] coords = room.randomCoords(this.rand);
         int y = this.rand.nextInt(5);
         SimpleBlock target = new SimpleBlock(data, coords[0], room.getY() + y, coords[2]);
         AncientCityUtils.spreadSculk(circleNoise, this.rand, 5.0F, target);
      }

   }
}
