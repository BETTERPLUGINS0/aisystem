package org.terraform.structure.ancientcity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.coregen.populatordata.PopulatorDataICABiomeWriterAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.carver.RoomCarver;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;
import org.terraform.utils.version.V_1_19;

public class AncientCityBFSCarver extends RoomCarver {
   private static final int PADDING = 7;
   private final SimpleLocation generatorCenter;

   public AncientCityBFSCarver(SimpleLocation generatorCenter) {
      this.generatorCenter = generatorCenter;
   }

   public void carveRoom(PopulatorDataAbstract data, CubeRoom room, Material... wallMaterial) {
      Random rand = data.getTerraformWorld().getHashedRand((long)room.getX(), room.getY(), room.getZ());
      PopulatorDataICABiomeWriterAbstract ica = (PopulatorDataICABiomeWriterAbstract)TerraformGeneratorPlugin.injector.getICAData(data);

      assert ica != null;

      FastNoise circleNoise = NoiseCacheHandler.getNoise(data.getTerraformWorld(), NoiseCacheHandler.NoiseCacheEntry.STRUCTURE_ANCIENTCITY_HOLE, (world) -> {
         FastNoise n = new FastNoise((int)(world.getSeed() ^ 14423311L));
         n.SetNoiseType(FastNoise.NoiseType.Simplex);
         n.SetFrequency(0.02F);
         return n;
      });
      SimpleBlock chunkCent = room.getCenterSimpleBlock(data);
      SimpleBlock bfsStart = null;

      for(int nx = -7; nx < 23; ++nx) {
         for(int nz = -7; nz < 23; ++nz) {
            for(int ny = -40; ny <= 40; ++ny) {
               SimpleBlock rel = new SimpleBlock(data, chunkCent.getChunkX() * 16 + nx, chunkCent.getY() + ny, chunkCent.getChunkZ() * 16 + nz);
               if (!this.rollCriteria(circleNoise, rel)) {
                  bfsStart = rel;
                  break;
               }
            }
         }
      }

      if (bfsStart != null) {
         HashSet<SimpleLocation> seen = new HashSet();
         Queue<SimpleBlock> bfsQueue = new LinkedList();
         bfsQueue.add(bfsStart);
         seen.add(bfsStart.getLoc());

         while(true) {
            while(!bfsQueue.isEmpty()) {
               SimpleBlock target = (SimpleBlock)bfsQueue.remove();
               ica.setBiome(target.getX(), target.getY(), target.getZ(), V_1_19.DEEP_DARK);
               boolean hasCorner = false;
               BlockFace[] var13 = BlockUtils.sixBlockFaces;
               int var14 = var13.length;

               for(int var15 = 0; var15 < var14; ++var15) {
                  BlockFace face = var13[var15];
                  SimpleBlock rel = target.getRelative(face);
                  if (!seen.contains(rel.getLoc()) && rel.getX() >= chunkCent.getChunkX() * 16 - 7 && rel.getX() <= chunkCent.getChunkX() * 16 + 16 + 7 && rel.getZ() >= chunkCent.getChunkZ() * 16 - 7 && rel.getZ() <= chunkCent.getChunkZ() * 16 + 16 + 7) {
                     if (this.rollCriteria(circleNoise, rel)) {
                        hasCorner = true;
                        if (face == BlockFace.DOWN && !target.getUp().isSolid() && target.isSolid()) {
                           if (GenUtils.chance(rand, 1, 230)) {
                              assert V_1_19.SCULK_CATALYST != null;

                              target.getUp().setType(V_1_19.SCULK_CATALYST);
                           } else if (GenUtils.chance(rand, 1, 150)) {
                              assert V_1_19.SCULK_SENSOR != null;

                              target.getUp().setType(V_1_19.SCULK_SENSOR);
                           } else if (GenUtils.chance(rand, 1, 600)) {
                              target.getUp().setBlockData(V_1_19.getActiveSculkShrieker());
                           }
                        }
                     } else {
                        seen.add(rel.getLoc());
                        bfsQueue.add(rel);
                     }
                  }
               }

               if (!BlockUtils.isWet(target) && target.getType() != Material.LAVA && (!hasCorner || !target.isSolid())) {
                  if (BlockUtils.caveCarveReplace.contains(target.getType())) {
                     target.setType(Material.CAVE_AIR);
                  }
               } else {
                  assert V_1_19.SCULK != null;

                  target.setType(V_1_19.SCULK);
               }
            }

            return;
         }
      }
   }

   boolean rollCriteria(FastNoise circleNoise, SimpleBlock rel) {
      double equationResult = Math.pow((double)(rel.getX() - this.generatorCenter.getX()), 2.0D) / Math.pow(50.0D, 2.0D) + Math.pow((double)(rel.getZ() - this.generatorCenter.getZ()), 2.0D) / Math.pow(50.0D, 2.0D) + Math.pow((double)(rel.getY() - this.generatorCenter.getY() - 20), 2.0D) / Math.pow(40.0D, 2.0D);
      float noiseVal = circleNoise.GetNoise((float)rel.getX(), (float)rel.getY(), (float)rel.getZ());
      return equationResult > 1.7D + 0.7D * (double)noiseVal;
   }
}
