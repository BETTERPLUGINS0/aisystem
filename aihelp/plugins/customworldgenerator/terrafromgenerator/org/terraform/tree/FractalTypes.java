package org.terraform.tree;

import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.version.V_1_19;
import org.terraform.utils.version.V_1_21_5;
import org.terraform.utils.version.Version;

public class FractalTypes {
   public static enum MushroomCap {
      ROUND,
      FLAT,
      FUNNEL,
      POINTY;

      // $FF: synthetic method
      private static FractalTypes.MushroomCap[] $values() {
         return new FractalTypes.MushroomCap[]{ROUND, FLAT, FUNNEL, POINTY};
      }
   }

   public static enum Mushroom {
      TINY_BROWN_MUSHROOM,
      TINY_RED_MUSHROOM,
      SMALL_RED_MUSHROOM,
      SMALL_POINTY_RED_MUSHROOM,
      SMALL_BROWN_MUSHROOM,
      MEDIUM_BROWN_MUSHROOM,
      MEDIUM_RED_MUSHROOM,
      MEDIUM_BROWN_FUNNEL_MUSHROOM,
      GIANT_BROWN_MUSHROOM,
      GIANT_BROWN_FUNNEL_MUSHROOM,
      GIANT_RED_MUSHROOM;

      // $FF: synthetic method
      private static FractalTypes.Mushroom[] $values() {
         return new FractalTypes.Mushroom[]{TINY_BROWN_MUSHROOM, TINY_RED_MUSHROOM, SMALL_RED_MUSHROOM, SMALL_POINTY_RED_MUSHROOM, SMALL_BROWN_MUSHROOM, MEDIUM_BROWN_MUSHROOM, MEDIUM_RED_MUSHROOM, MEDIUM_BROWN_FUNNEL_MUSHROOM, GIANT_BROWN_MUSHROOM, GIANT_BROWN_FUNNEL_MUSHROOM, GIANT_RED_MUSHROOM};
      }
   }

   public static enum Tree {
      FOREST(new NewFractalTreeBuilder[]{(new NewFractalTreeBuilder()).setLengthVariance(2.0F).setOriginalTrunkLength(12).setInitialBranchRadius(1.6F).setMinBranchHorizontalComponent(0.7D).setGetBranchWidth((initialBranchWidth, branchRatio) -> {
         return initialBranchWidth * (1.0F - branchRatio / 1.7F);
      }).setBranchDecrement((currentBranchLength, totalTreeHeight) -> {
         return currentBranchLength / 1.7F;
      }).setBranchSpawnChance(0.15D).setCrownBranches(4).setFractalLeaves((new FractalLeaves()).setWeepingLeaves(0.3F, 3).setRadius(4.0F).setRadiusY(2.0F)).setSpawnBees(true).setPrePlacement((random, block) -> {
         leafLitter(random, block, 6.0F);
      }), (new NewFractalTreeBuilder()).setOriginalTrunkLength(18).setInitialBranchRadius(2.0F).setGetBranchWidth((initialBranchWidth, branchRatio) -> {
         return initialBranchWidth * (1.0F - branchRatio / 2.0F);
      }).setBranchDecrement((currentBranchLength, totalTreeHeight) -> {
         return currentBranchLength - 7.0F;
      }).setBranchSpawnChance(0.2D).setFractalLeaves((new FractalLeaves()).setWeepingLeaves(0.3F, 2).setRadius(4.0F).setRadiusY(2.5F)).setSpawnBees(true).setPrePlacement((random, block) -> {
         leafLitter(random, block, 6.0F);
      }), (new NewFractalTreeBuilder()).setOriginalTrunkLength(18).setInitialBranchRadius(2.0F).setCrownBranches(5).setMaxDepth(2).setMinBranchHorizontalComponent(0.7D).setGetBranchWidth((initialBranchWidth, branchRatio) -> {
         return initialBranchWidth * (1.0F - branchRatio / 1.5F);
      }).setBranchDecrement((currentBranchLength, totalTreeHeight) -> {
         return currentBranchLength / 2.0F;
      }).setBranchSpawnChance(0.2D).setFractalLeaves((new FractalLeaves()).setWeepingLeaves(0.3F, 1).setRadius(4.0F).setRadiusY(2.5F)).setSpawnBees(true).setPrePlacement((random, block) -> {
         leafLitter(random, block, 6.0F);
      })}),
      NORMAL_SMALL(new NewFractalTreeBuilder[]{(new NewFractalTreeBuilder()).setTreeRootThreshold(0).setOriginalTrunkLength(4).setLengthVariance(1.0F).setMaxDepth(1).setCrownBranches(3).setInitialBranchRadius(0.8F).setNoisePriority(0.05F).setFirstEnd(1.0F).setMinBranchHorizontalComponent(1.2D).setMaxBranchHorizontalComponent(2.0D).setMaxInitialNormalDelta(0.0D).setMinInitialNormalDelta(0.0D).setGetBranchWidth((initialBranchWidth, branchRatio) -> {
         return initialBranchWidth - branchRatio * 0.2F;
      }).setBranchDecrement((currentBranchLength, totalTreeHeight) -> {
         return currentBranchLength - 2.0F;
      }).setMinBranchSpawnLength(0.8F).setBranchSpawnChance(0.1D).setFractalLeaves((new FractalLeaves()).setWeepingLeaves(0.3F, 1).setRadius(3.0F).setRadiusY(3.0F).setLeafNoiseFrequency(0.5F).setSemiSphereLeaves(true).setMaterial(Material.OAK_LEAVES)).setPrePlacement((random, block) -> {
         leafLitter(random, block, 3.5F);
      })}),
      AZALEA_TOP(new NewFractalTreeBuilder[]{(new NewFractalTreeBuilder()).setTreeRootThreshold(0).setOriginalTrunkLength(6).setLengthVariance(1.0F).setMaxDepth(1).setCrownBranches(3).setInitialBranchRadius(0.8F).setNoisePriority(0.05F).setFirstEnd(1.0F).setMinBranchHorizontalComponent(1.2D).setMaxBranchHorizontalComponent(2.0D).setMaxInitialNormalDelta(0.0D).setMinInitialNormalDelta(0.0D).setGetBranchWidth((initialBranchWidth, branchRatio) -> {
         return initialBranchWidth - branchRatio * 0.2F;
      }).setBranchDecrement((currentBranchLength, totalTreeHeight) -> {
         return currentBranchLength - 2.0F;
      }).setMinBranchSpawnLength(0.8F).setBranchSpawnChance(0.1D).setFractalLeaves((new FractalLeaves()).setWeepingLeaves(0.5F, 2).setRadius(4.0F).setRadiusY(1.5F).setLeafNoiseFrequency(0.2F).setMaterial(Material.AZALEA_LEAVES, Material.FLOWERING_AZALEA_LEAVES))}),
      TAIGA_BIG(new NewFractalTreeBuilder[]{(new NewFractalTreeBuilder()).setFirstEnd(1.0F).setTreeRootThreshold(2).setTreeRootMultiplier(1.3F).setBranchMaterial(Material.SPRUCE_LOG).setRootMaterial(Material.SPRUCE_WOOD).setOriginalTrunkLength(30).setLengthVariance(2.0F).setInitialBranchRadius(1.8F).setBranchDecrement((currentBranchLength, totalTreeHeight) -> {
         return currentBranchLength < 10.0F ? 0.0F : 0.3F * Math.max(0.0F, 30.0F - totalTreeHeight);
      }).setCrownBranches(0).setMaxDepth(3).setMaxInitialNormalDelta(0.1D).setMinInitialNormalDelta(-0.1D).setMinBranchHorizontalComponent(1.5D).setMaxBranchHorizontalComponent(2.0D).setBranchSpawnChance(1.0D).setRandomBranchSpawnCooldown(4.0F).setRandomBranchClusterCount(5).setRandomBranchSegmentCount(5).setMinBranchSpawnLength(0.3F).setLeafSpawnDepth(0).setFractalLeaves((new FractalLeaves()).setWeepingLeaves(0.4F, 2).setConeLeaves(true).setRadius(1.5F).setRadiusY(2.3F).setLeafNoiseFrequency(0.3F).setMaterial(Material.SPRUCE_LEAVES)).setPrePlacement((random, block) -> {
         BlockUtils.lambdaCircularPatch(random.nextInt(132798), 5.0F, block, (b) -> {
            if (BlockUtils.isDirtLike(b.getType())) {
               b.setType(Material.PODZOL);
            }

         });
      })}),
      TAIGA_SMALL(new NewFractalTreeBuilder[]{(new NewFractalTreeBuilder()).setTreeRootThreshold(0).setBranchMaterial(Material.SPRUCE_LOG).setOriginalTrunkLength(16).setLengthVariance(1.0F).setInitialBranchRadius(0.8F).setGetBranchWidth((initialBranchWidth, branchRatio) -> {
         return initialBranchWidth;
      }).setBranchDecrement((currentBranchLength, totalTreeHeight) -> {
         return currentBranchLength < 10.0F ? 0.0F : Math.min(4.0F, 0.5F * Math.max(0.0F, 12.0F - totalTreeHeight));
      }).setCrownBranches(0).setMaxDepth(3).setMaxInitialNormalDelta(0.1D).setMinInitialNormalDelta(-0.1D).setMinBranchHorizontalComponent(1.5D).setMaxBranchHorizontalComponent(2.0D).setBranchSpawnChance(1.0D).setRandomBranchSpawnCooldown(4.0F).setRandomBranchClusterCount(4).setRandomBranchSegmentCount(4).setMinBranchSpawnLength(0.2F).setLeafSpawnDepth(0).setRootMaterial(Material.SPRUCE_WOOD).setFractalLeaves((new FractalLeaves()).setWeepingLeaves(0.4F, 1).setConeLeaves(true).setRadius(1.3F).setRadiusY(2.0F).setMaterial(Material.SPRUCE_LEAVES)).setPrePlacement((random, block) -> {
         BlockUtils.lambdaCircularPatch(random.nextInt(132798), 3.5F, block, (b) -> {
            if (BlockUtils.isDirtLike(b.getType())) {
               b.setType(Material.PODZOL);
            }

         });
      }), (new NewFractalTreeBuilder()).setTreeRootThreshold(0).setBranchMaterial(Material.SPRUCE_LOG).setOriginalTrunkLength(18).setLengthVariance(3.0F).setInitialBranchRadius(0.8F).setGetBranchWidth((initialBranchWidth, branchRatio) -> {
         return initialBranchWidth;
      }).setBranchDecrement((currentBranchLength, totalTreeHeight) -> {
         return currentBranchLength < 10.0F ? 0.0F : Math.min(4.0F, 0.5F * Math.max(0.0F, 12.0F - totalTreeHeight));
      }).setCrownBranches(0).setMaxDepth(3).setMaxInitialNormalDelta(0.0D).setMinInitialNormalDelta(0.0D).setMinBranchHorizontalComponent(1.5D).setMaxBranchHorizontalComponent(2.0D).setBranchSpawnChance(0.7D).setRandomBranchSpawnCooldown(2.0F).setRandomBranchClusterCount(4).setRandomBranchSegmentCount(4).setMinBranchSpawnLength(0.4F).setLeafSpawnDepth(0).setRootMaterial(Material.SPRUCE_WOOD).setFractalLeaves((new FractalLeaves()).setWeepingLeaves(0.4F, 1).setConeLeaves(true).setRadius(1.3F).setRadiusY(3.0F).setMaterial(Material.SPRUCE_LEAVES)).setPrePlacement((random, block) -> {
         BlockUtils.lambdaCircularPatch(random.nextInt(132798), 3.5F, block, (b) -> {
            if (BlockUtils.isDirtLike(b.getType())) {
               b.setType(Material.PODZOL);
            }

         });
      })}),
      SCARLET_BIG,
      SCARLET_SMALL,
      SAVANNA_SMALL,
      SAVANNA_BIG,
      WASTELAND_BIG,
      SWAMP_TOP(new NewFractalTreeBuilder[]{(new NewFractalTreeBuilder()).setOriginalTrunkLength(13).setLengthVariance(2.0F).setMaxDepth(4).setInitialBranchRadius(2.0F).setGetBranchWidth((initialBranchWidth, branchRatio) -> {
         return initialBranchWidth * (1.0F - branchRatio / 2.0F);
      }).setBranchDecrement((currentBranchLength, totalTreeHeight) -> {
         return currentBranchLength / 1.7F;
      }).setMinBranchHorizontalComponent(0.8999999761581421D).setMaxBranchHorizontalComponent(1.2999999523162842D).setBranchSpawnChance(0.2D).setBranchMaterial(V_1_19.MANGROVE_LOG).setRootMaterial(V_1_19.MANGROVE_ROOTS).setTreeRootMultiplier(2.0F).setTreeRootThreshold(3).setFractalLeaves((new FractalLeaves()).setWeepingLeaves(0.4F, 2).setRadius(4.0F).setRadiusY(1.5F).setMaterial(V_1_19.MANGROVE_LEAVES).setMangrovePropagules(true))}),
      BIRCH_BIG,
      BIRCH_SMALL,
      CHERRY_SMALL,
      CHERRY_THICK,
      JUNGLE_BIG,
      JUNGLE_SMALL,
      JUNGLE_EXTRA_SMALL,
      COCONUT_TOP,
      DARK_OAK_SMALL(new NewFractalTreeBuilder[]{(new NewFractalTreeBuilder()).setOriginalTrunkLength(7).setLengthVariance(1.0F).setInitialBranchRadius(2.7F).setCrownBranches(3).setMinBranchSpawnLength(0.2F).setMaxDepth(4).setBranchSpawnChance(0.0D).setMinBranchHorizontalComponent(0.5D).setMaxBranchHorizontalComponent(0.8999999761581421D).setGetBranchWidth((initialBranchWidth, branchRatio) -> {
         return initialBranchWidth * (1.0F - branchRatio / 2.0F);
      }).setBranchDecrement((currentBranchLength, totalTreeHeight) -> {
         return currentBranchLength - 1.0F;
      }).setBranchSpawnChance(0.05D).setTreeRootMultiplier(1.3F).setTreeRootThreshold(3).setRootMaterial(Material.DARK_OAK_WOOD).setBranchMaterial(Material.DARK_OAK_LOG).setFractalLeaves((new FractalLeaves()).setWeepingLeaves(0.3F, 1).setRadius(4.5F).setRadiusY(2.5F).setMaterial(Material.DARK_OAK_LEAVES))}),
      DARK_OAK_BIG_TOP(new NewFractalTreeBuilder[]{(new NewFractalTreeBuilder()).setOriginalTrunkLength(12).setLengthVariance(1.0F).setInitialBranchRadius(2.7F).setCrownBranches(3).setMinBranchSpawnLength(0.2F).setMaxDepth(3).setBranchSpawnChance(0.0D).setMinBranchHorizontalComponent(-1.2D).setMaxBranchHorizontalComponent(1.2000000476837158D).setGetBranchWidth((initialBranchWidth, branchRatio) -> {
         return initialBranchWidth * (1.0F - branchRatio / 3.0F);
      }).setBranchDecrement((currentBranchLength, totalTreeHeight) -> {
         return currentBranchLength - 0.5F;
      }).setRandomBranchClusterCount(3).setBranchSpawnChance(0.05D).setTreeRootMultiplier(1.6F).setTreeRootThreshold(5).setRootMaterial(Material.DARK_OAK_WOOD).setBranchMaterial(Material.DARK_OAK_LOG).setFractalLeaves((new FractalLeaves()).setWeepingLeaves(0.3F, 2).setRadius(4.0F).setRadiusY(2.5F).setMaterial(Material.DARK_OAK_LEAVES))}),
      FROZEN_TREE_BIG,
      FROZEN_TREE_SMALL,
      FIRE_CORAL,
      HORN_CORAL,
      BRAIN_CORAL,
      TUBE_CORAL,
      BUBBLE_CORAL,
      GIANT_PUMPKIN,
      ANDESITE_PETRIFIED_SMALL,
      GRANITE_PETRIFIED_SMALL,
      DIORITE_PETRIFIED_SMALL;

      private final NewFractalTreeBuilder[] builders;

      private Tree() {
         this.builders = new NewFractalTreeBuilder[0];
      }

      private Tree(NewFractalTreeBuilder... param3) {
         this.builders = builder;
      }

      public boolean build(@NotNull TerraformWorld tw, @NotNull SimpleBlock base) {
         return this.build(tw, base, (Consumer)null);
      }

      public boolean build(@NotNull TerraformWorld tw, @NotNull SimpleBlock base, @Nullable Consumer<NewFractalTreeBuilder> treeMutator) {
         if (this.builders.length > 0) {
            NewFractalTreeBuilder b = (NewFractalTreeBuilder)Objects.requireNonNull((NewFractalTreeBuilder)GenUtils.choice(tw.getHashedRand((long)base.getX(), base.getY(), base.getZ()), this.builders));
            if (treeMutator != null) {
               try {
                  b = (NewFractalTreeBuilder)b.clone();
                  treeMutator.accept(b);
               } catch (CloneNotSupportedException var6) {
                  TerraformGeneratorPlugin.logger.stackTrace(var6);
                  return b.build(tw, base);
               }
            }

            return b.build(tw, base);
         } else {
            return (new FractalTreeBuilder(this)).build(tw, base);
         }
      }

      private static void leafLitter(Random random, SimpleBlock base, float radius) {
         if (Version.VERSION.isAtLeast(Version.v1_21_5)) {
            BlockUtils.lambdaCircularPatch(random.nextInt(8903245), radius, base, (b) -> {
               if (b.getUp().getType() == Material.AIR && random.nextInt(6) == 0) {
                  V_1_21_5.leafLitter(random, b.getPopData(), b.getX(), b.getY() + 1, b.getZ());
               }

            });
         }
      }

      // $FF: synthetic method
      private static FractalTypes.Tree[] $values() {
         return new FractalTypes.Tree[]{FOREST, NORMAL_SMALL, AZALEA_TOP, TAIGA_BIG, TAIGA_SMALL, SCARLET_BIG, SCARLET_SMALL, SAVANNA_SMALL, SAVANNA_BIG, WASTELAND_BIG, SWAMP_TOP, BIRCH_BIG, BIRCH_SMALL, CHERRY_SMALL, CHERRY_THICK, JUNGLE_BIG, JUNGLE_SMALL, JUNGLE_EXTRA_SMALL, COCONUT_TOP, DARK_OAK_SMALL, DARK_OAK_BIG_TOP, FROZEN_TREE_BIG, FROZEN_TREE_SMALL, FIRE_CORAL, HORN_CORAL, BRAIN_CORAL, TUBE_CORAL, BUBBLE_CORAL, GIANT_PUMPKIN, ANDESITE_PETRIFIED_SMALL, GRANITE_PETRIFIED_SMALL, DIORITE_PETRIFIED_SMALL};
      }
   }
}
