package org.terraform.tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Leaves;
import org.jetbrains.annotations.NotNull;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.CoralGenerator;
import org.terraform.utils.GenUtils;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;
import org.terraform.utils.version.V_1_19;
import org.terraform.utils.version.V_1_21_4;
import org.terraform.utils.version.Version;

public class FractalLeaves implements Cloneable {
   @NotNull
   final Random rand = new Random();
   private final HashSet<SimpleBlock> occupiedLeaves = new HashSet();
   public float radiusX = 4.0F;
   public float radiusY = 2.0F;
   public int numYSegments = 5;
   public float radiusZ = 4.0F;
   public int offsetY = 0;
   public Material[] material;
   int oriY;
   int maxHeight;
   TerraformWorld tw;
   boolean semiSphereLeaves;
   float leafNoiseMultiplier;
   float leafNoiseFrequency;
   double hollowLeaves;
   boolean coneLeaves;
   boolean snowy;
   float weepingLeavesChance;
   int weepingLeavesLength;
   float paleVinesChance;
   int paleVinesLength;
   boolean coralDecoration;
   boolean mangrovePropagules;
   int unitLeafSize;
   float unitLeafChance;

   public FractalLeaves() {
      this.material = new Material[]{Material.OAK_LEAVES};
      this.semiSphereLeaves = false;
      this.leafNoiseMultiplier = 0.7F;
      this.leafNoiseFrequency = 0.09F;
      this.hollowLeaves = 0.0D;
      this.coneLeaves = false;
      this.snowy = false;
      this.weepingLeavesChance = 0.0F;
      this.weepingLeavesLength = 0;
      this.paleVinesChance = 0.0F;
      this.paleVinesLength = 0;
      this.coralDecoration = false;
      this.mangrovePropagules = false;
      this.unitLeafSize = 0;
      this.unitLeafChance = 0.0F;
   }

   public void purgeOccupiedLeavesCache() {
      this.occupiedLeaves.clear();
   }

   public void placeLeaves(@NotNull SimpleBlock centre) {
      this.placeLeaves(centre.getPopData().getTerraformWorld(), -999, 999, centre);
   }

   public void placeLeaves(TerraformWorld tw, int oriY, int maxHeight, @NotNull SimpleBlock centre) {
      this.tw = tw;
      this.oriY = oriY;
      this.maxHeight = maxHeight;
      FastNoise noiseGen = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.FRACTALTREES_LEAVES_NOISE, (world) -> {
         FastNoise n = new FastNoise((int)world.getSeed());
         n.SetFractalOctaves(5);
         n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
         return n;
      });
      noiseGen.SetFrequency(this.leafNoiseFrequency);
      if (!(this.radiusX <= 0.0F) || !(this.radiusY <= 0.0F) || !(this.radiusZ <= 0.0F)) {
         if ((double)this.radiusX <= 0.5D && (double)this.radiusY <= 0.5D && (double)this.radiusZ <= 0.5D) {
            centre.setType(this.material);
         } else {
            float noiseMultiplier = this.leafNoiseMultiplier;
            int minRadiusY = -Math.round(this.radiusY);
            if (this.semiSphereLeaves) {
               minRadiusY = 0;
            }

            ArrayList<SimpleBlock> changed = new ArrayList();

            int z;
            for(int y = minRadiusY; (float)y <= this.radiusY; ++y) {
               for(int x = -Math.round(this.radiusX); (float)x <= this.radiusX; ++x) {
                  for(z = -Math.round(this.radiusZ); (float)z <= this.radiusZ; ++z) {
                     Material material = this.material[this.rand.nextInt(this.material.length)];
                     SimpleBlock relativeBlock = centre.getRelative(x, y + this.offsetY, z);
                     if (relativeBlock.getY() - oriY > maxHeight) {
                        return;
                     }

                     if (relativeBlock.getY() - oriY == maxHeight && this.rand.nextBoolean()) {
                        return;
                     }

                     float effectiveY = (float)y;
                     if (this.coneLeaves) {
                        effectiveY += this.radiusY / 2.0F;
                        if (effectiveY < 0.0F) {
                           effectiveY *= 2.0F;
                        }

                        if (effectiveY > 0.0F) {
                           effectiveY *= 0.6666667F;
                           effectiveY = (float)Math.pow((double)effectiveY, 1.3D);
                           if (effectiveY > this.radiusY) {
                              effectiveY = this.radiusY;
                           }
                        }

                        relativeBlock = relativeBlock.getRelative(0, (int)(this.radiusY / 2.0F), 0);
                     }

                     if (!this.occupiedLeaves.contains(relativeBlock)) {
                        double equationResult = Math.pow((double)x, 2.0D) / Math.pow((double)this.radiusX, 2.0D) + Math.pow((double)effectiveY, 2.0D) / Math.pow((double)this.radiusY, 2.0D) + Math.pow((double)z, 2.0D) / Math.pow((double)this.radiusZ, 2.0D);
                        if (equationResult <= (double)(1.0F + noiseMultiplier * noiseGen.GetNoise((float)relativeBlock.getX(), (float)relativeBlock.getY(), (float)relativeBlock.getZ())) && !(equationResult < this.hollowLeaves)) {
                           this.occupiedLeaves.add(relativeBlock);
                           if (this.mangrovePropagules && Version.VERSION.isAtLeast(Version.v1_19_4) && !BlockUtils.isWet(relativeBlock.getDown()) && GenUtils.chance(1, 50)) {
                              relativeBlock.getDown().rsetBlockData(BlockUtils.replacableByTrees, V_1_19.getHangingMangrovePropagule());
                           }

                           if (Tag.CORALS.isTagged(material) && !changed.contains(relativeBlock)) {
                              changed.add(relativeBlock);
                           }

                           if (this.coralDecoration) {
                              CoralGenerator.generateSingleCoral(relativeBlock.getPopData(), relativeBlock.getX(), relativeBlock.getY(), relativeBlock.getZ(), this.material[0].toString());
                           }

                           if (!relativeBlock.isSolid()) {
                              this.unitSet(relativeBlock, material);
                              if (this.unitLeafSize > 0 && this.unitLeafChance > 0.0F && GenUtils.chance(this.rand, (int)(this.unitLeafChance * 100.0F), 100)) {
                                 for(int scaleX = -this.unitLeafSize; scaleX < this.unitLeafSize; ++scaleX) {
                                    for(int scaleZ = -this.unitLeafSize; scaleZ < this.unitLeafSize; ++scaleZ) {
                                       for(int scaleY = -this.unitLeafSize; scaleY < this.unitLeafSize; ++scaleY) {
                                          this.unitSet(relativeBlock.getRelative(scaleX, scaleY, scaleZ), material);
                                       }
                                    }
                                 }
                              }
                           }

                           if (this.snowy && !relativeBlock.getUp().isSolid()) {
                              relativeBlock.getUp().setType(Material.SNOW);
                           }

                           if (this.weepingLeavesChance > 0.0F && Math.random() < (double)this.weepingLeavesChance) {
                              this.weepingLeaves(relativeBlock, Math.round(this.weepingLeavesChance * (float)this.weepingLeavesLength), this.weepingLeavesLength);
                           }

                           if (Version.VERSION.isAtLeast(Version.v1_21_4) && this.paleVinesChance > 0.0F && Math.random() < (double)this.paleVinesChance) {
                              this.paleVines(relativeBlock, Math.round(this.paleVinesChance * (float)this.paleVinesLength), this.paleVinesLength);
                           }
                        }
                     }
                  }
               }
            }

            while(true) {
               while(!changed.isEmpty()) {
                  SimpleBlock sb = (SimpleBlock)changed.remove((new Random()).nextInt(changed.size()));
                  if (!CoralGenerator.isSaturatedCoral(sb)) {
                     BlockFace[] var21 = BlockUtils.directBlockFaces;
                     z = var21.length;

                     for(int var22 = 0; var22 < z; ++var22) {
                        BlockFace face = var21[var22];
                        if (Tag.WALL_CORALS.isTagged(sb.getRelative(face).getType())) {
                           sb.getRelative(face).setType(Material.WATER);
                        }
                     }

                     if (sb.getUp().getType() == Material.SEA_PICKLE || Tag.CORAL_PLANTS.isTagged(sb.getUp().getType())) {
                        sb.getUp().setType(Material.WATER);
                     }

                     sb.setType(Material.WATER);
                  } else {
                     sb.setType(this.material);
                  }
               }

               return;
            }
         }
      }
   }

   private void unitSet(@NotNull SimpleBlock relativeBlock, @NotNull Material material) {
      if (Tag.LEAVES.isTagged(material)) {
         Leaves leaf = (Leaves)Bukkit.createBlockData(material);
         leaf.setDistance(1);
         relativeBlock.rsetBlockData(BlockUtils.replacableByTrees, leaf);
      } else {
         relativeBlock.rsetType(BlockUtils.replacableByTrees, material);
      }

   }

   private void paleVines(@NotNull SimpleBlock base, int minDist, int maxDist) {
      int lowest = 0;

      for(int i = 1; i <= GenUtils.randInt(minDist, maxDist) && BlockUtils.isAir(base.getRelative(0, -i, 0).getType()); ++i) {
         base.getRelative(0, -i, 0).rsetBlockData(BlockUtils.replacableByTrees, V_1_21_4.PALE_HANGING_MOSS);
         ++lowest;
      }

      if (lowest > 0) {
         base.getDown(lowest).setBlockData(V_1_21_4.PALE_HANGING_MOSS_TIP);
      }

   }

   private void weepingLeaves(@NotNull SimpleBlock base, int minDist, int maxDist) {
      Material material = this.material[this.rand.nextInt(this.material.length)];
      BlockData type = Bukkit.createBlockData(material);
      if (Tag.LEAVES.isTagged(material)) {
         Leaves leaf = (Leaves)type;
         leaf.setDistance(1);
      }

      for(int i = 1; i <= GenUtils.randInt(minDist, maxDist) && BlockUtils.isAir(base.getRelative(0, -i, 0).getType()); ++i) {
         base.getRelative(0, -i, 0).rsetBlockData(BlockUtils.replacableByTrees, type);
      }

   }

   @NotNull
   public FractalLeaves setMaterial(Material... material) {
      this.material = material;
      return this;
   }

   @NotNull
   public FractalLeaves setOffsetY(int offsetY) {
      this.offsetY = offsetY;
      return this;
   }

   @NotNull
   public FractalLeaves setRadiusX(float radiusX) {
      this.radiusX = radiusX;
      return this;
   }

   @NotNull
   public FractalLeaves setRadiusY(float radiusY) {
      this.radiusY = radiusY;
      this.numYSegments = (int)Math.ceil((double)(radiusY * 2.0F + 1.0F));
      return this;
   }

   @NotNull
   public FractalLeaves setRadiusZ(float radiusZ) {
      this.radiusZ = radiusZ;
      return this;
   }

   @NotNull
   public FractalLeaves setRadius(float radius) {
      this.radiusX = radius;
      this.setRadiusY(radius);
      this.radiusZ = radius;
      return this;
   }

   @NotNull
   public FractalLeaves setRadius(float x, float y, float z) {
      this.radiusX = x;
      this.setRadiusY(y);
      this.radiusZ = z;
      return this;
   }

   @NotNull
   public FractalLeaves setHollowLeaves(double hollow) {
      this.hollowLeaves = hollow;
      return this;
   }

   @NotNull
   public FractalLeaves setConeLeaves(boolean coneLeaves) {
      this.coneLeaves = coneLeaves;
      return this;
   }

   @NotNull
   public FractalLeaves setLeafNoiseMultiplier(float multiplier) {
      this.leafNoiseMultiplier = multiplier;
      return this;
   }

   @NotNull
   public FractalLeaves setLeafNoiseFrequency(float freq) {
      this.leafNoiseFrequency = freq;
      return this;
   }

   @NotNull
   public FractalLeaves setSnowy(boolean snowy) {
      this.snowy = snowy;
      return this;
   }

   @NotNull
   public FractalLeaves setMangrovePropagules(boolean mangrovePropagules) {
      this.mangrovePropagules = mangrovePropagules;
      return this;
   }

   @NotNull
   public FractalLeaves setPaleMossVines(float chance, int maxLength) {
      this.paleVinesChance = chance;
      this.paleVinesLength = maxLength;
      return this;
   }

   @NotNull
   public FractalLeaves setWeepingLeaves(float chance, int maxLength) {
      this.weepingLeavesChance = chance;
      this.weepingLeavesLength = maxLength;
      return this;
   }

   @NotNull
   public FractalLeaves setUnitLeafChance(float unitLeafChance) {
      this.unitLeafChance = unitLeafChance;
      return this;
   }

   @NotNull
   public FractalLeaves setUnitLeafSize(int unitSize) {
      this.unitLeafSize = unitSize;
      return this;
   }

   @NotNull
   public FractalLeaves setSemiSphereLeaves(boolean semiSphereLeaves) {
      this.semiSphereLeaves = semiSphereLeaves;
      return this;
   }

   public int getOriY() {
      return this.oriY;
   }

   public void setOriY(int oriY) {
      this.oriY = oriY;
   }

   public int getMaxHeight() {
      return this.maxHeight;
   }

   public void setMaxHeight(int maxHeight) {
      this.maxHeight = maxHeight;
   }

   public TerraformWorld getTw() {
      return this.tw;
   }

   public void setTw(TerraformWorld tw) {
      this.tw = tw;
   }

   public FractalLeaves clone() throws CloneNotSupportedException {
      return (FractalLeaves)super.clone();
   }
}
