package org.terraform.biome.cavepopulators;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.data.Wall;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public abstract class AbstractCaveClusterPopulator extends AbstractCavePopulator {
   private final float radius;
   protected SimpleBlock center;
   protected SimpleBlock lowestYCenter;

   public AbstractCaveClusterPopulator(float radius) {
      this.radius = radius;
   }

   protected abstract void oneUnit(TerraformWorld var1, Random var2, SimpleBlock var3, SimpleBlock var4, boolean var5);

   public void populate(TerraformWorld tw, Random random, @NotNull SimpleBlock ceil, @NotNull SimpleBlock floor) {
      if (!(this.radius <= 0.0F)) {
         ArrayList<SimpleBlock[]> ceilFloorPairs = new ArrayList();
         ArrayList<Boolean> boundaries = new ArrayList();
         FastNoise circleNoise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_CAVECLUSTER_CIRCLENOISE, (world) -> {
            FastNoise n = new FastNoise((int)(world.getSeed() * 11L));
            n.SetNoiseType(FastNoise.NoiseType.Simplex);
            n.SetFrequency(0.09F);
            return n;
         });
         this.center = new SimpleBlock(ceil.getPopData(), ceil.getX(), (ceil.getY() + floor.getY()) / 2, ceil.getZ());
         int lowest = this.center.getY();
         HashMap<SimpleBlock, Wall[]> seen = new HashMap();
         Queue<SimpleBlock> queue = new ArrayDeque();
         queue.add(this.center);
         seen.put(this.center, new Wall[]{new Wall(ceil), new Wall(floor)});

         while(!queue.isEmpty()) {
            SimpleBlock v = (SimpleBlock)queue.remove();
            Wall vCeil = ((Wall[])seen.get(v))[0];
            Wall vFloor = ((Wall[])seen.get(v))[1];
            lowest = Math.min(vFloor.getY(), lowest);
            ceilFloorPairs.add(new SimpleBlock[]{vCeil.get(), vFloor.get()});
            boolean sawFailCondition = false;
            BlockFace[] var15 = BlockUtils.directBlockFaces;
            int var16 = var15.length;

            for(int var17 = 0; var17 < var16; ++var17) {
               BlockFace face = var15[var17];
               SimpleBlock neighbour = v.getRelative(face);
               if (!seen.containsKey(neighbour)) {
                  double equationResult = Math.pow((double)(neighbour.getX() - this.center.getX()), 2.0D) / Math.pow((double)this.radius, 2.0D) + Math.pow((double)(neighbour.getZ() - this.center.getZ()), 2.0D) / Math.pow((double)this.radius, 2.0D);
                  if (equationResult > 1.0D + 0.7D * (double)circleNoise.GetNoise((float)neighbour.getX(), (float)neighbour.getZ())) {
                     sawFailCondition = true;
                  } else {
                     Wall candidateFloorWall = (new Wall(neighbour)).findStonelikeFloor(60);
                     Wall candidateCeilWall = (new Wall(neighbour)).findStonelikeCeiling(60);
                     if (candidateFloorWall != null && candidateCeilWall != null && !BlockUtils.amethysts.contains(floor.getType()) && !BlockUtils.fluids.contains(floor.getUp().getType()) && !BlockUtils.amethysts.contains(ceil.getDown().getType()) && candidateFloorWall.getType() != Material.MOSS_BLOCK && candidateFloorWall.getType() != Material.DRIPSTONE_BLOCK && !candidateFloorWall.getUp().isSolid() && !candidateCeilWall.getDown().isSolid()) {
                        seen.put(neighbour, new Wall[]{candidateCeilWall, candidateFloorWall});
                        queue.add(neighbour);
                     }
                  }
               }
            }

            boundaries.add(sawFailCondition);
         }

         this.lowestYCenter = this.center.getAtY(lowest);

         for(int i = 0; i < ceilFloorPairs.size(); ++i) {
            SimpleBlock[] candidates = (SimpleBlock[])ceilFloorPairs.get(i);
            if (!BlockUtils.fluids.contains(candidates[1].getAtY(lowest + 1).getType())) {
               this.oneUnit(tw, random, candidates[0], candidates[1], (Boolean)boundaries.get(i));
            }
         }

      }
   }
}
