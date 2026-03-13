package org.terraform.tree;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import org.bukkit.Material;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;
import org.terraform.utils.version.BeeHiveSpawner;

public class NewFractalTreeBuilder implements Cloneable {
   private static final int[][] rotationMatrixX = new int[][]{{1, 0, 0}, {0, 0, -1}, {0, 1, 0}};
   private static final int[][] rotationMatrixZ = new int[][]{{0, -1, 0}, {1, 0, 0}, {0, 0, 1}};
   final int maxHeight = 9999;
   private final Vector initialNormal = new Vector(0, 1, 0);
   private final double displacementThetaDelta = 6.283185307179586D;
   private int maxDepth = 3;
   private int originalTrunkLength = 20;
   private float lengthVariance = 4.0F;
   private float firstEnd = 0.8F;
   private int crownBranches = 4;
   private float initialBranchRadius = 3.0F;
   private double branchSpawnChance = 0.07999999821186066D;
   private float minBranchSpawnLength = 0.4F;
   private int randomBranchSegmentCount = 3;
   private float randomBranchSpawnCooldown = 0.0F;
   private int randomBranchClusterCount = 1;
   private double maxInitialNormalDelta = 0.3D;
   private double minInitialNormalDelta = -0.3D;
   private double minBranchHorizontalComponent = 0.5D;
   private double maxBranchHorizontalComponent = 1.3D;
   private int treeRootThreshold = 2;
   private float treeRootMultiplier = 1.5F;
   private float noisePriority = 0.1F;
   private int leafSpawnDepth = 1;
   private FractalLeaves fractalLeaves;
   private BiConsumer<Random, SimpleBlock> prePlacement = null;
   private BiFunction<Float, Float, Float> branchDecrement = (currentBranchLength, totalTreeHeight) -> {
      return currentBranchLength * 0.7F;
   };
   private BiFunction<Float, Float, Float> getBranchWidth = (initialBranchWidth, branchRatio) -> {
      return initialBranchWidth * (1.0F - branchRatio / 2.0F);
   };
   private Material branchMaterial;
   private Material rootMaterial;
   private boolean spawnBees;
   private boolean checkGradient;

   public NewFractalTreeBuilder() {
      this.branchMaterial = Material.OAK_LOG;
      this.rootMaterial = Material.OAK_WOOD;
      this.spawnBees = false;
      this.checkGradient = true;
   }

   public boolean build(@NotNull TerraformWorld tw, @NotNull SimpleBlock base) {
      if (!TConfig.areTreesEnabled()) {
         return false;
      } else if (!this.checkGradient(base.getPopData(), base.getX(), base.getZ())) {
         return false;
      } else {
         int oriY = base.getY();
         Random random = tw.getHashedRand((long)base.getX(), base.getY(), base.getZ());
         if (this.prePlacement != null) {
            this.prePlacement.accept(random, base);
         }

         double displacementTheta = GenUtils.randDouble(random, 0.0D, 6.283185307179586D);
         HashSet<SimpleBlock> prospectiveHives = new HashSet();
         double currentBranchTheta = (double)GenUtils.randInt(random, 0, this.randomBranchSegmentCount);
         this.fractalLeaves.purgeOccupiedLeavesCache();
         this.branch(tw, random, base, this.initialNormal.clone().add(new Vector(GenUtils.randDouble(random, this.minInitialNormalDelta, this.maxInitialNormalDelta), 0.0D, GenUtils.randDouble(random, this.minInitialNormalDelta, this.maxInitialNormalDelta))).normalize(), prospectiveHives, currentBranchTheta, oriY, displacementTheta, (float)this.originalTrunkLength + (float)GenUtils.randDouble(random, (double)(-this.lengthVariance), (double)this.lengthVariance), this.firstEnd, 0, this.initialBranchRadius, 0.0F);
         if (this.spawnBees) {
            Iterator var10 = prospectiveHives.iterator();

            while(var10.hasNext()) {
               SimpleBlock b = (SimpleBlock)var10.next();
               if (!b.isSolid()) {
                  BeeHiveSpawner.spawnFullBeeNest(b);
                  break;
               }
            }
         }

         this.fractalLeaves.setSnowy(false);
         return true;
      }
   }

   boolean checkGradient(PopulatorDataAbstract data, int x, int z) {
      return !this.checkGradient || HeightMap.getTrueHeightGradient(data, x, z, 3) <= TConfig.c.MISC_TREES_GRADIENT_LIMIT;
   }

   @NotNull
   public NewFractalTreeBuilder setCheckGradient(boolean checkGradient) {
      this.checkGradient = checkGradient;
      return this;
   }

   void branch(TerraformWorld tw, @NotNull Random random, @NotNull SimpleBlock base, @NotNull Vector normal, @NotNull HashSet<SimpleBlock> prospectiveHives, double currentBranchTheta, int oriY, double displacementTheta, float length, float end, int depth, float currentWidth, float startingBranchIndex) {
      boolean spawnedNewBranch = false;
      SimpleBlock lastOperatedCentre = base;
      if (length > 0.0F && depth < this.maxDepth) {
         float initialWidth = currentWidth;
         FastNoise noiseGen = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.FRACTALTREES_BASE_NOISE, (world) -> {
            FastNoise n = new FastNoise((int)world.getSeed());
            n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
            n.SetFractalOctaves(5);
            n.SetFrequency(0.05F);
            return n;
         });
         Vector branchVect = normal.clone().multiply(length);
         float randomBranchSpawnCooldownCurrent = 0.0F;

         for(float i = 0.0F; i < length - startingBranchIndex && !(i / length > end); i += 0.5F) {
            float appliedWidth = currentWidth;
            float appliedNoisePriority = this.noisePriority;
            Vector appliedNormal = normal;
            Material temp = this.branchMaterial;
            if (depth == 0 && i < (float)this.treeRootThreshold) {
               appliedWidth = currentWidth * (float)((double)this.treeRootMultiplier + (1.0D - (double)this.treeRootMultiplier) / (double)this.treeRootThreshold * (double)i);
               appliedNoisePriority = (float)(0.7D + ((double)this.noisePriority - 0.4D) / (double)this.treeRootThreshold * (double)i);
               appliedNormal = new Vector(0, 1, 0);
               this.branchMaterial = this.rootMaterial;
            }

            lastOperatedCentre = this.generateRotatedCircle(random, oriY, lastOperatedCentre.getPopData(), branchVect.clone().multiply(i / length).add(base.toVector()), appliedNormal, prospectiveHives, appliedNoisePriority, appliedWidth, noiseGen, i);
            this.branchMaterial = temp;
            currentWidth = (Float)this.getBranchWidth.apply(initialWidth, i / length);
            randomBranchSpawnCooldownCurrent -= 0.5F;
            if (i / length > this.minBranchSpawnLength && GenUtils.chance(random, (int)(100.0D * this.branchSpawnChance), 100) && randomBranchSpawnCooldownCurrent <= 0.0F) {
               randomBranchSpawnCooldownCurrent = this.randomBranchSpawnCooldown;
               spawnedNewBranch = true;
               double effectiveDisplacementTheta = displacementTheta;
               if (this.randomBranchClusterCount > 0) {
                  displacementTheta = GenUtils.randDouble(random, 0.0D, 6.283185307179586D);
               }

               for(int y = 0; y < this.randomBranchClusterCount; ++y) {
                  ++currentBranchTheta;
                  this.branch(tw, random, lastOperatedCentre, this.calculateNextProjection(random, normal, this.getNextTheta(currentBranchTheta, this.randomBranchSegmentCount, effectiveDisplacementTheta)), prospectiveHives, currentBranchTheta, oriY, displacementTheta, (Float)this.branchDecrement.apply(length, (float)(lastOperatedCentre.getY() - oriY)), 1.0F, depth + 1, currentWidth, 0.0F);
               }
            }
         }

         if (depth == 0 && this.crownBranches > 0) {
            double thetaDelta = 6.283185307179586D / (double)this.crownBranches;

            for(int i = 0; i < this.crownBranches; ++i) {
               spawnedNewBranch = true;
               this.branch(tw, random, lastOperatedCentre, this.calculateNextProjection(random, normal, thetaDelta * (double)i), prospectiveHives, currentBranchTheta, oriY, displacementTheta, (Float)this.branchDecrement.apply(length, (float)(lastOperatedCentre.getY() - oriY)), 1.0F, depth + 1, currentWidth, 0.0F);
            }
         }
      }

      if (length <= 0.0F || !spawnedNewBranch || depth >= this.leafSpawnDepth) {
         this.fractalLeaves.placeLeaves(tw, oriY, 9999, lastOperatedCentre);
      }

   }

   double getNextTheta(double currentBranchTheta, int numSegments, double displacementTheta) {
      double thetaDelta = 6.283185307179586D / (double)numSegments;
      return displacementTheta + currentBranchTheta * thetaDelta;
   }

   @NotNull
   Vector calculateNextProjection(@NotNull Random random, @NotNull Vector normal, double theta) {
      Vector A = normal.clone();
      A.setX((double)rotationMatrixX[0][0] * normal.getX() + (double)rotationMatrixX[0][1] * normal.getY() + (double)rotationMatrixX[0][2] * normal.getZ());
      A.setY((double)rotationMatrixX[1][0] * normal.getX() + (double)rotationMatrixX[1][1] * normal.getY() + (double)rotationMatrixX[1][2] * normal.getZ());
      A.setZ((double)rotationMatrixX[2][0] * normal.getX() + (double)rotationMatrixX[2][1] * normal.getY() + (double)rotationMatrixX[2][2] * normal.getZ());
      double x = A.getX();
      double y = A.getY();
      double z = A.getZ();
      double u = normal.getX();
      double v = normal.getY();
      double w = normal.getZ();
      double AdotNormal = A.dot(normal);
      double xPrime = u * AdotNormal * (1.0D - Math.cos(theta)) + x * Math.cos(theta) + (-w * y + v * z) * Math.sin(theta);
      double yPrime = v * AdotNormal * (1.0D - Math.cos(theta)) + y * Math.cos(theta) + (w * x - u * z) * Math.sin(theta);
      double zPrime = w * AdotNormal * (1.0D - Math.cos(theta)) + z * Math.cos(theta) + (-v * x + u * y) * Math.sin(theta);
      A = new Vector(xPrime, yPrime, zPrime);
      return normal.clone().add(A.multiply(GenUtils.randDouble(random, this.minBranchHorizontalComponent, this.maxBranchHorizontalComponent))).normalize();
   }

   @NotNull
   SimpleBlock generateRotatedCircle(@NotNull Random random, int oriY, @NotNull PopulatorDataAbstract data, @NotNull Vector centre, @NotNull Vector normal, @NotNull HashSet<SimpleBlock> prospectiveHives, float noisePriority, float radius, @NotNull FastNoise noiseGen, float heightIndex) {
      if (radius <= 0.5F) {
         data.rsetType(centre, BlockUtils.replacableByTrees, this.branchMaterial);
         return new SimpleBlock(data, centre);
      } else {
         Vector A = normal.clone();
         Vector B = normal.clone();
         A.setX((double)rotationMatrixX[0][0] * normal.getX() + (double)rotationMatrixX[0][1] * normal.getY() + (double)rotationMatrixX[0][2] * normal.getZ());
         A.setY((double)rotationMatrixX[1][0] * normal.getX() + (double)rotationMatrixX[1][1] * normal.getY() + (double)rotationMatrixX[1][2] * normal.getZ());
         A.setZ((double)rotationMatrixX[2][0] * normal.getX() + (double)rotationMatrixX[2][1] * normal.getY() + (double)rotationMatrixX[2][2] * normal.getZ());
         B.setX((double)rotationMatrixZ[0][0] * normal.getX() + (double)rotationMatrixZ[0][1] * normal.getY() + (double)rotationMatrixZ[0][2] * normal.getZ());
         B.setY((double)rotationMatrixZ[1][0] * normal.getX() + (double)rotationMatrixZ[1][1] * normal.getY() + (double)rotationMatrixZ[1][2] * normal.getZ());
         B.setZ((double)rotationMatrixZ[2][0] * normal.getX() + (double)rotationMatrixZ[2][1] * normal.getY() + (double)rotationMatrixZ[2][2] * normal.getZ());
         boolean didNotGenerate = true;
         double maxPossibleRadius = (double)((1.0F + noisePriority * 2.0F) * radius);

         for(double rA = -maxPossibleRadius; rA <= maxPossibleRadius; ++rA) {
            for(double rB = -maxPossibleRadius; rB <= maxPossibleRadius; ++rB) {
               double distFromCentre = Math.sqrt(Math.pow(rA, 2.0D) + Math.pow(rB, 2.0D));
               double theta = Math.atan2(rB, rA);
               if (theta < 0.0D) {
                  theta = 6.283185307179586D - theta;
               }

               double newRadius;
               if (noisePriority > 0.0F) {
                  newRadius = (double)(radius + noisePriority * radius * noiseGen.GetNoise((float)Objects.hash(new Object[]{centre.getX(), centre.getZ()}), (float)theta, heightIndex));
               } else {
                  newRadius = (double)radius;
               }

               if (distFromCentre <= newRadius) {
                  data.rsetType(centre.clone().add(A.clone().multiply(rA)).add(B.clone().multiply(rB)), BlockUtils.replacableByTrees, this.branchMaterial);
                  didNotGenerate = false;
                  if (this.spawnBees && centre.getY() > (double)((float)oriY + (float)this.originalTrunkLength / 2.0F) && GenUtils.chance(random, 1, 200)) {
                     prospectiveHives.add(new SimpleBlock(data, centre.clone().add(A.clone().multiply(rA)).add(B.clone().multiply(rB)).add(new Vector(0, -1, 0))));
                  }
               }
            }
         }

         if (didNotGenerate) {
            data.rsetType(centre, BlockUtils.replacableByTrees, this.branchMaterial);
         }

         return new SimpleBlock(data, centre);
      }
   }

   @NotNull
   NewFractalTreeBuilder setMaxDepth(int maxDepth) {
      this.maxDepth = maxDepth;
      return this;
   }

   @NotNull
   NewFractalTreeBuilder setOriginalTrunkLength(int originalTrunkLength) {
      this.originalTrunkLength = originalTrunkLength;
      return this;
   }

   @NotNull
   NewFractalTreeBuilder setFirstEnd(float firstEnd) {
      this.firstEnd = firstEnd;
      return this;
   }

   @NotNull
   NewFractalTreeBuilder setCrownBranches(int crownBranches) {
      this.crownBranches = crownBranches;
      return this;
   }

   @NotNull
   NewFractalTreeBuilder setInitialBranchRadius(float initialBranchRadius) {
      this.initialBranchRadius = initialBranchRadius;
      return this;
   }

   @NotNull
   NewFractalTreeBuilder setBranchSpawnChance(double branchSpawnChance) {
      this.branchSpawnChance = branchSpawnChance;
      return this;
   }

   @NotNull
   NewFractalTreeBuilder setMinBranchSpawnLength(float minBranchSpawnLength) {
      this.minBranchSpawnLength = minBranchSpawnLength;
      return this;
   }

   @NotNull
   NewFractalTreeBuilder setTreeRootThreshold(int treeRootThreshold) {
      this.treeRootThreshold = treeRootThreshold;
      return this;
   }

   @NotNull
   NewFractalTreeBuilder setRandomBranchClusterCount(int randomBranchClusterCount) {
      this.randomBranchClusterCount = randomBranchClusterCount;
      return this;
   }

   @NotNull
   NewFractalTreeBuilder setRandomBranchSegmentCount(int randomBranchSegmentCount) {
      this.randomBranchSegmentCount = randomBranchSegmentCount;
      return this;
   }

   @NotNull
   NewFractalTreeBuilder setMaxInitialNormalDelta(double maxInitialNormalDelta) {
      this.maxInitialNormalDelta = maxInitialNormalDelta;
      return this;
   }

   @NotNull
   NewFractalTreeBuilder setRandomBranchSpawnCooldown(float randomBranchSpawnCooldown) {
      this.randomBranchSpawnCooldown = randomBranchSpawnCooldown;
      return this;
   }

   @NotNull
   NewFractalTreeBuilder setTreeRootMultiplier(float treeRootMultiplier) {
      this.treeRootMultiplier = treeRootMultiplier;
      return this;
   }

   @NotNull
   NewFractalTreeBuilder setLeafSpawnDepth(int leafSpawnDepth) {
      this.leafSpawnDepth = leafSpawnDepth;
      return this;
   }

   @NotNull
   NewFractalTreeBuilder setMinBranchHorizontalComponent(double minBranchHorizontalComponent) {
      this.minBranchHorizontalComponent = minBranchHorizontalComponent;
      return this;
   }

   @NotNull
   NewFractalTreeBuilder setMaxBranchHorizontalComponent(double maxBranchHorizontalComponent) {
      this.maxBranchHorizontalComponent = maxBranchHorizontalComponent;
      return this;
   }

   @NotNull
   NewFractalTreeBuilder setBranchDecrement(BiFunction<Float, Float, Float> branchDecrement) {
      this.branchDecrement = branchDecrement;
      return this;
   }

   @NotNull
   NewFractalTreeBuilder setGetBranchWidth(BiFunction<Float, Float, Float> getBranchWidth) {
      this.getBranchWidth = getBranchWidth;
      return this;
   }

   @NotNull
   NewFractalTreeBuilder setPrePlacement(BiConsumer<Random, SimpleBlock> func) {
      this.prePlacement = func;
      return this;
   }

   @NotNull
   NewFractalTreeBuilder setSpawnBees(boolean spawnBees) {
      this.spawnBees = spawnBees;
      return this;
   }

   @NotNull
   NewFractalTreeBuilder setLengthVariance(float lengthVariance) {
      this.lengthVariance = lengthVariance;
      return this;
   }

   @NotNull
   public NewFractalTreeBuilder setBranchMaterial(Material branchMaterial) {
      this.branchMaterial = branchMaterial;
      return this;
   }

   @NotNull
   public NewFractalTreeBuilder setRootMaterial(Material rootMaterial) {
      this.rootMaterial = rootMaterial;
      return this;
   }

   @NotNull
   NewFractalTreeBuilder setNoisePriority(float noisePriority) {
      this.noisePriority = noisePriority;
      return this;
   }

   @NotNull
   NewFractalTreeBuilder setMinInitialNormalDelta(double minInitialNormalDelta) {
      this.minInitialNormalDelta = minInitialNormalDelta;
      return this;
   }

   public FractalLeaves getFractalLeaves() {
      return this.fractalLeaves;
   }

   @NotNull
   NewFractalTreeBuilder setFractalLeaves(FractalLeaves fractalLeaves) {
      this.fractalLeaves = fractalLeaves;
      return this;
   }

   @NotNull
   protected Object clone() throws CloneNotSupportedException {
      NewFractalTreeBuilder cl = (NewFractalTreeBuilder)super.clone();
      cl.setFractalLeaves(this.fractalLeaves.clone());
      return cl;
   }
}
