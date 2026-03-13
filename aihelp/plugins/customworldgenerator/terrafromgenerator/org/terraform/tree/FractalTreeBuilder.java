package org.terraform.tree;

import java.util.ArrayList;
import java.util.Random;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.CoralGenerator;
import org.terraform.utils.GenUtils;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;
import org.terraform.utils.version.BeeHiveSpawner;
import org.terraform.utils.version.V_1_19;
import org.terraform.utils.version.V_1_20;

public class FractalTreeBuilder {
   protected TerraformWorld tw;
   protected boolean coralDecoration = false;
   int height = 0;
   SimpleBlock top;
   float baseThickness = 3.0F;
   int baseHeight = 7;
   float thicknessDecrement = 0.5F;
   float minThickness = 0.0F;
   int maxDepth = 4;
   int maxHeight = 999;
   float lengthDecrement = 1.0F;
   float lengthDecrementMultiplier = 1.0F;
   @Nullable
   Material trunkType;
   FractalLeaves fractalLeaves;
   Random rand;
   double minBend;
   double maxBend;
   float depthPitchMultiplier;
   int heightVariation;
   double initialTilt;
   double minInitialTilt;
   int alwaysOneStraight;
   int alwaysOneStraightBranchLength;
   int alwaysOneStraightBranchSpawningDepth;
   boolean alwaysOneStraightExtendedBranches;
   double alwaysOneStraightBranchYawLowerMultiplier;
   double alwaysOneStraightBranchYawUpperMultiplier;
   boolean noMainStem;
   double beeChance;
   int vines;
   int cocoaBeans;
   int fractalThreshold;
   int fractalsDone;
   double maxPitch;
   double minPitch;
   float branchNoiseMultiplier;
   float branchNoiseFrequency;
   int oriX;
   int oriY;
   int oriZ;
   private SimpleBlock beeHive;
   private double initialAngle;
   private int initialHeight;
   private boolean heightGradientChecked;

   public FractalTreeBuilder(@NotNull FractalTypes.Tree type) {
      this.trunkType = Material.OAK_WOOD;
      this.fractalLeaves = new FractalLeaves();
      this.minBend = 0.41887902047863906D;
      this.maxBend = 0.6283185307179586D;
      this.depthPitchMultiplier = 1.0F;
      this.heightVariation = 0;
      this.initialTilt = 0.0D;
      this.minInitialTilt = -1.0D;
      this.alwaysOneStraight = 0;
      this.alwaysOneStraightBranchLength = 0;
      this.alwaysOneStraightBranchSpawningDepth = 1;
      this.alwaysOneStraightExtendedBranches = false;
      this.alwaysOneStraightBranchYawLowerMultiplier = 0.9D;
      this.alwaysOneStraightBranchYawUpperMultiplier = 1.1D;
      this.noMainStem = false;
      this.beeChance = 0.0D;
      this.vines = 0;
      this.cocoaBeans = 0;
      this.fractalThreshold = 1;
      this.fractalsDone = 0;
      this.maxPitch = 9999.0D;
      this.minPitch = -9999.0D;
      this.branchNoiseMultiplier = 0.7F;
      this.branchNoiseFrequency = 0.09F;
      this.heightGradientChecked = false;
      switch(type) {
      case FOREST:
         this.setBeeChance(TConfig.c.ANIMALS_BEE_HIVEFREQUENCY).setBaseHeight(9).setBaseThickness(3.0F).setThicknessDecrement(0.3F).setLengthDecrement(1.3F).setMinBend(0.3665191429188092D).setMaxBend(0.44505895925855404D).setMaxDepth(4).setHeightVariation(2).setLeafBranchFrequency(0.05F).setFractalLeaves((new FractalLeaves()).setRadius(3.0F).setLeafNoiseFrequency(1.0F).setLeafNoiseMultiplier(1.0F));
         break;
      case NORMAL_SMALL:
         this.setBeeChance(TConfig.c.ANIMALS_BEE_HIVEFREQUENCY).setBaseHeight(5).setBaseThickness(1.0F).setThicknessDecrement(1.0F).setMaxDepth(1).setFractalLeaves((new FractalLeaves()).setRadius(3.0F).setLeafNoiseFrequency(1.0F).setLeafNoiseMultiplier(1.0F)).setHeightVariation(1);
         break;
      case AZALEA_TOP:
         this.setBeeChance(TConfig.c.ANIMALS_BEE_HIVEFREQUENCY).setBaseHeight(3).setBaseThickness(1.0F).setThicknessDecrement(0.3F).setLengthDecrement(0.3F).setMaxDepth(2).setFractalLeaves((new FractalLeaves()).setMaterial(Material.AZALEA_LEAVES, Material.FLOWERING_AZALEA_LEAVES).setRadiusX(3.0F).setRadiusZ(3.0F).setRadiusY(1.5F).setLeafNoiseFrequency(1.0F).setLeafNoiseMultiplier(1.0F).setWeepingLeaves(0.3F, 3)).setVines(3).setMinBend(0.47123889803846897D).setMaxBend(0.5759586531581288D).setHeightVariation(0);
         break;
      case BIRCH_BIG:
         this.setBaseHeight(6).setBaseThickness(1.0F).setThicknessDecrement(0.0F).setMaxDepth(4).setHeightVariation(2).setMinBend(0.47123889803846897D).setMaxBend(0.5759586531581288D).setLengthDecrement(0.5F).setTrunkType(Material.BIRCH_WOOD).setFractalLeaves((new FractalLeaves()).setMaterial(Material.BIRCH_LEAVES).setRadius(3.0F, 2.3F, 3.0F));
         break;
      case BIRCH_SMALL:
         this.setBaseHeight(3).setBaseThickness(1.0F).setThicknessDecrement(0.0F).setMaxDepth(3).setHeightVariation(1).setMinBend(0.47123889803846897D).setMaxBend(0.5759586531581288D).setLengthDecrement(0.5F).setTrunkType(Material.BIRCH_WOOD).setFractalLeaves((new FractalLeaves()).setMaterial(Material.BIRCH_LEAVES).setRadius(3.0F, 2.3F, 3.0F));
         break;
      case CHERRY_SMALL:
         this.setBaseHeight(4).setBaseThickness(2.5F).setThicknessDecrement(0.5F).setMaxDepth(3).setDepthPitchMultiplier(0.8F).setInitialTilt(0.39269908169872414D).setHeightVariation(1).setMinBend(0.47123889803846897D).setMaxBend(0.5759586531581288D).setLengthDecrement(-0.5F).setMinThickness(1.0F).setTrunkType(V_1_20.CHERRY_LOG).setFractalLeaves((new FractalLeaves()).setMaterial(V_1_20.CHERRY_LEAVES).setRadius(3.0F, 2.0F, 3.0F));
         break;
      case CHERRY_THICK:
         this.setBaseHeight(5).setBaseThickness(3.0F).setThicknessDecrement(0.4F).setMaxDepth(4).setDepthPitchMultiplier(-0.6F).setInitialTilt(0.6806784082777885D).setMinInitialTilt(0.5235987755982988D).setHeightVariation(0).setMinBend(0.47123889803846897D).setMaxBend(0.5759586531581288D).setLengthDecrement(0.3F).setMinThickness(1.0F).setTrunkType(V_1_20.CHERRY_WOOD).setFractalLeaves((new FractalLeaves()).setMaterial(V_1_20.CHERRY_LEAVES).setRadius(3.0F, 2.0F, 3.0F).setLeafNoiseFrequency(0.15F));
         break;
      case ANDESITE_PETRIFIED_SMALL:
         this.setBaseHeight(6).setBaseThickness(3.0F).setThicknessDecrement(0.5F).setMaxDepth(3).setTrunkType(Material.ANDESITE).setMinBend(0.5759586531581288D).setMaxBend(0.6806784082777885D).setLengthDecrement(1.0F).setHeightVariation(2).setVines(3).setFractalLeaves((new FractalLeaves()).setRadius(4.0F, 2.0F, 4.0F).setMaterial(Material.ANDESITE, Material.POLISHED_ANDESITE, Material.ANDESITE).setWeepingLeaves(0.3F, 3));
         break;
      case GRANITE_PETRIFIED_SMALL:
         this.setBaseHeight(6).setBaseThickness(3.0F).setThicknessDecrement(0.5F).setMaxDepth(3).setTrunkType(Material.GRANITE).setMinBend(0.5759586531581288D).setMaxBend(0.6806784082777885D).setLengthDecrement(1.0F).setHeightVariation(2).setVines(3).setFractalLeaves((new FractalLeaves()).setRadius(4.0F, 2.0F, 4.0F).setMaterial(Material.GRANITE, Material.POLISHED_GRANITE, Material.GRANITE).setWeepingLeaves(0.3F, 3));
         break;
      case DIORITE_PETRIFIED_SMALL:
         this.setBaseHeight(6).setBaseThickness(3.0F).setThicknessDecrement(0.5F).setMaxDepth(3).setTrunkType(Material.DIORITE).setMinBend(0.5759586531581288D).setMaxBend(0.6806784082777885D).setLengthDecrement(1.0F).setHeightVariation(2).setVines(3).setFractalLeaves((new FractalLeaves()).setRadius(4.0F, 2.0F, 4.0F).setMaterial(Material.DIORITE, Material.POLISHED_DIORITE, Material.DIORITE).setWeepingLeaves(0.3F, 3));
         break;
      case SAVANNA_SMALL:
         this.setBaseHeight(7).setBaseThickness(1.0F).setThicknessDecrement(0.0F).setMaxDepth(2).setTrunkType(Material.ACACIA_LOG).setMinBend(0.7853981633974483D).setMaxBend(1.2566370614359172D).setLengthDecrement(1.0F).setHeightVariation(1).setFractalLeaves((new FractalLeaves()).setRadius(4.0F, 2.0F, 4.0F).setMaterial(Material.ACACIA_LEAVES));
         break;
      case JUNGLE_BIG:
         this.setBaseHeight(15).setBaseThickness(5.0F).setThicknessDecrement(1.0F).setMaxDepth(3).setHeightVariation(6).setMaxBend(0.5235987755982988D).setLengthDecrement(2.0F).setVines(7).setTrunkType(Material.JUNGLE_WOOD).setCocoaBeans(3).setFractalLeaves((new FractalLeaves()).setRadius(4.0F, 1.0F, 4.0F).setMaterial(Material.JUNGLE_LEAVES).setOffsetY(1).setWeepingLeaves(0.4F, 7));
         break;
      case JUNGLE_SMALL:
         this.setBaseHeight(5).setHeightVariation(1).setLengthDecrement(1.5F).setMaxDepth(2).setBaseThickness(3.0F).setThicknessDecrement(1.5F).setMaxBend(1.0471975511965976D).setVines(3).setTrunkType(Material.JUNGLE_WOOD).setCocoaBeans(1).setFractalLeaves((new FractalLeaves()).setRadius(4.0F, 2.0F, 4.0F).setMaterial(Material.JUNGLE_LEAVES).setWeepingLeaves(0.3F, 3));
         break;
      case JUNGLE_EXTRA_SMALL:
         this.setBaseHeight(3).setMaxDepth(1).setBaseThickness(1.5F).setThicknessDecrement(0.0F).setVines(3).setTrunkType(Material.JUNGLE_WOOD).setCocoaBeans(1).setFractalLeaves((new FractalLeaves()).setRadius(3.0F, 2.0F, 3.0F).setMaterial(Material.JUNGLE_LEAVES).setWeepingLeaves(0.3F, 3));
         break;
      case SAVANNA_BIG:
         this.setBaseHeight(10).setBaseThickness(15.0F).setThicknessDecrement(4.0F).setMaxDepth(4).setTrunkType(Material.ACACIA_LOG).setLengthDecrement(0.4F).setHeightVariation(2).setFractalLeaves((new FractalLeaves()).setRadius(4.0F, 2.5F, 4.0F).setMaterial(Material.ACACIA_LEAVES).setLeafNoiseFrequency(0.7F).setLeafNoiseMultiplier(0.8F));
         break;
      case WASTELAND_BIG:
         this.setBaseHeight(6).setBaseThickness(4.0F).setThicknessDecrement(1.0F).setMaxDepth(4).setTrunkType(Material.SPRUCE_WOOD).setLengthDecrement(0.5F).setHeightVariation(1).setFractalLeaves((new FractalLeaves()).setRadius(0.0F).setMaterial(Material.AIR));
         break;
      case TAIGA_BIG:
         this.setBaseHeight(10).setBaseThickness(3.5F).setThicknessDecrement(0.5F).setMaxDepth(5).setTrunkType(Material.SPRUCE_WOOD).setLengthDecrement(2.0F).setHeightVariation(2).setAlwaysOneStraight(4).setAlwaysOneStraightExtendedBranches(true).setMinBend(1.5707963267948966D).setMaxBend(1.5707963267948966D).setFractalLeaves((new FractalLeaves()).setRadius(3.0F, 5.0F, 3.0F).setMaterial(Material.SPRUCE_LEAVES).setConeLeaves(true).setLeafNoiseFrequency(0.3F).setLeafNoiseMultiplier(0.7F));
         break;
      case TAIGA_SMALL:
         this.setBaseHeight(5).setBaseThickness(1.0F).setThicknessDecrement(0.3F).setMaxDepth(4).setTrunkType(Material.SPRUCE_WOOD).setFractalLeaves((new FractalLeaves()).setLeafNoiseFrequency(0.65F).setLeafNoiseMultiplier(0.8F).setRadius(2.0F).setMaterial(Material.SPRUCE_LEAVES).setConeLeaves(true)).setLengthDecrement(1.0F).setAlwaysOneStraight(4).setAlwaysOneStraightExtendedBranches(true).setMinBend(1.5707963267948966D).setMaxBend(1.5707963267948966D).setHeightVariation(2);
         break;
      case SCARLET_BIG:
         this.setBaseHeight(10).setBaseThickness(6.0F).setThicknessDecrement(0.7F).setLengthDecrement(0.5F).setLengthDecrementMultiplier(1.5F).setMinThickness(0.5F).setMaxDepth(7).setTrunkType(Material.BIRCH_WOOD).setHeightVariation(2).setAlwaysOneStraightBranchLength(14).setAlwaysOneStraight(6).setAlwaysOneStraightExtendedBranches(false).setAlwaysOneStraightBranchYawLowerMultiplier(0.7D).setAlwaysOneStraightBranchYawUpperMultiplier(1.3D).setAlwaysOneStraightBranchSpawningDepth(3).setMinBend(1.0471975511965976D).setMaxBend(1.5707963267948966D).setFractalLeaves((new FractalLeaves()).setRadius(5.0F, 2.0F, 5.0F).setMaterial(Material.OAK_LEAVES).setConeLeaves(true).setLeafNoiseFrequency(0.5F).setLeafNoiseMultiplier(0.8F));
         break;
      case SCARLET_SMALL:
         this.setBaseHeight(2).setBaseThickness(1.0F).setThicknessDecrement(0.3F).setMaxDepth(1).setTrunkType(Material.BIRCH_LOG).setFractalLeaves((new FractalLeaves()).setLeafNoiseFrequency(0.65F).setLeafNoiseMultiplier(0.8F).setRadius(2.0F).setMaterial(Material.OAK_LEAVES).setConeLeaves(true)).setLengthDecrement(1.0F).setHeightVariation(1);
         break;
      case SWAMP_TOP:
         this.setBaseHeight(8).setBaseThickness(3.0F).setThicknessDecrement(0.5F).setMaxDepth(4).setLengthDecrement(0.0F).setHeightVariation(2).setTrunkType(V_1_19.MANGROVE_WOOD).setVines(7).setFractalLeaves((new FractalLeaves()).setWeepingLeaves(0.4F, 7).setMaterial(V_1_19.MANGROVE_LEAVES).setRadius(5.0F, 2.0F, 5.0F).setMangrovePropagules(true));
         break;
      case COCONUT_TOP:
         this.setBaseHeight(8).setInitialTilt(0.5235987755982988D).setBaseThickness(1.0F).setThicknessDecrement(0.0F).setMaxDepth(1).setLengthDecrement(2.0F).setHeightVariation(1).setVines(3).setTrunkType(Material.JUNGLE_WOOD).setFractalLeaves((new FractalLeaves()).setWeepingLeaves(0.3F, 3).setRadius(3.0F, 1.2F, 3.0F));
         break;
      case GIANT_PUMPKIN:
         this.setBaseHeight(6).setBaseThickness(1.0F).setThicknessDecrement(1.0F).setMaxDepth(0).setLengthDecrement(-0.5F).setHeightVariation(0).setTrunkType(Material.OAK_LOG).setFractalLeaves((new FractalLeaves()).setRadius(4.0F).setMaterial(Material.PUMPKIN));
      case DARK_OAK_SMALL:
         this.setBaseHeight(3).setBaseThickness(3.0F).setThicknessDecrement(0.5F).setMaxDepth(3).setTrunkType(Material.DARK_OAK_WOOD).setLengthDecrement(0.0F).setHeightVariation(0).setFractalThreshold(4).setMaxBend(0.7330382858376184D).setMinBend(0.5235987755982988D).setMaxPitch(2.0943951023931953D).setMinPitch(0.0D).setFractalLeaves((new FractalLeaves()).setRadius(5.0F, 1.0F, 5.0F).setMaterial(Material.DARK_OAK_LEAVES));
         break;
      case DARK_OAK_BIG_TOP:
         this.setBaseHeight(6).setBaseThickness(8.0F).setThicknessDecrement(2.5F).setMaxDepth(3).setTrunkType(Material.DARK_OAK_WOOD).setLengthDecrement(0.0F).setHeightVariation(1).setFractalThreshold(4).setMaxBend(0.7330382858376184D).setMinBend(0.5235987755982988D).setMaxPitch(2.0943951023931953D).setMinPitch(0.0D).setFractalLeaves((new FractalLeaves()).setRadius(6.0F, 2.0F, 6.0F).setMaterial(Material.DARK_OAK_LEAVES).setOffsetY(1));
         break;
      case FROZEN_TREE_BIG:
         this.setBaseHeight(4).setBaseThickness(4.0F).setThicknessDecrement(2.0F).setMaxDepth(4).setVines(4).setTrunkType(Material.SPRUCE_WOOD).setLengthDecrement(0.0F).setHeightVariation(1).setFractalThreshold(4).setMaxBend(0.8377580409572781D).setMinBend(0.6283185307179586D).setMaxPitch(3.141592653589793D).setMinPitch(0.0D).setFractalLeaves((new FractalLeaves()).setRadius(4.0F, 2.0F, 4.0F).setMaterial(Material.ICE));
         break;
      case FROZEN_TREE_SMALL:
         this.setBaseHeight(1).setBaseThickness(2.0F).setThicknessDecrement(0.2F).setMaxDepth(4).setVines(4).setTrunkType(Material.SPRUCE_WOOD).setLengthDecrement(0.0F).setHeightVariation(0).setFractalThreshold(4).setMaxBend(0.8377580409572781D).setMinBend(0.6283185307179586D).setMaxPitch(3.141592653589793D).setMinPitch(0.0D).setFractalLeaves((new FractalLeaves()).setRadius(4.0F, 1.0F, 4.0F).setMaterial(Material.ICE));
         break;
      case FIRE_CORAL:
         this.setBaseHeight(2).setInitialTilt(1.5707963267948966D).setBaseThickness(1.0F).setThicknessDecrement(0.0F).setMaxDepth(3).setFractalLeaves((new FractalLeaves()).setRadius(1.0F, 4.0F, 1.0F).setMaterial(Material.FIRE_CORAL_BLOCK)).setTrunkType(Material.FIRE_CORAL_BLOCK).setLengthDecrement(-2.0F).setHeightVariation(0).setMaxBend(1.5707963267948966D).setMinBend(1.2566370614359172D).setMaxPitch(3.141592653589793D).setMinPitch(0.0D).setCoralDecoration(true);
         break;
      case HORN_CORAL:
         this.setBaseHeight(2).setBaseThickness(2.0F).setThicknessDecrement(0.0F).setMaxDepth(3).setFractalLeaves((new FractalLeaves()).setRadius(3.0F, 1.0F, 3.0F).setMaterial(Material.HORN_CORAL_BLOCK)).setTrunkType(Material.HORN_CORAL_BLOCK).setLengthDecrement(-1.0F).setHeightVariation(0).setMaxBend(1.0471975511965976D).setMinBend(0.7853981633974483D).setMaxPitch(3.141592653589793D).setMinPitch(0.0D).setCoralDecoration(true).setNoMainStem(true);
         break;
      case BRAIN_CORAL:
         this.setBaseHeight(1).setBaseThickness(1.0F).setThicknessDecrement(0.0F).setMaxDepth(3).setFractalLeaves((new FractalLeaves()).setRadius(1.0F, 2.0F, 1.0F).setHollowLeaves(0.9D).setMaterial(Material.BRAIN_CORAL_BLOCK)).setTrunkType(Material.BRAIN_CORAL_BLOCK).setLengthDecrement(0.0F).setHeightVariation(0).setFractalThreshold(3).setMaxBend(1.0471975511965976D).setMinBend(0.7853981633974483D).setMaxPitch(3.141592653589793D).setMinPitch(0.0D).setCoralDecoration(true);
         break;
      case TUBE_CORAL:
         this.setBaseHeight(3).setAlwaysOneStraight(3).setBaseThickness(3.0F).setThicknessDecrement(0.0F).setMaxDepth(3).setFractalLeaves((new FractalLeaves()).setRadius(1.0F, 1.0F, 1.0F).setHollowLeaves(0.9D).setMaterial(Material.TUBE_CORAL_BLOCK)).setTrunkType(Material.TUBE_CORAL_BLOCK).setLengthDecrement(0.0F).setHeightVariation(1).setMaxBend(1.0471975511965976D).setMinBend(0.7853981633974483D).setMaxPitch(3.141592653589793D).setMinPitch(0.0D).setCoralDecoration(true);
         break;
      case BUBBLE_CORAL:
         this.setBaseHeight(3).setBaseThickness(1.0F).setThicknessDecrement(0.0F).setMaxDepth(3).setFractalLeaves((new FractalLeaves()).setRadius(3.0F, 3.0F, 3.0F).setHollowLeaves(0.9D).setMaterial(Material.BUBBLE_CORAL_BLOCK)).setTrunkType(Material.BUBBLE_CORAL_BLOCK).setLengthDecrement(-1.0F).setHeightVariation(1).setMaxBend(1.5707963267948966D).setMinBend(1.0471975511965976D).setMaxPitch(3.141592653589793D).setMinPitch(0.0D).setCoralDecoration(true).setNoMainStem(true);
      }

   }

   public boolean checkGradient(PopulatorDataAbstract data, int x, int z) {
      this.heightGradientChecked = true;
      return HeightMap.getTrueHeightGradient(data, x, z, 3) <= TConfig.c.MISC_TREES_GRADIENT_LIMIT;
   }

   public boolean build(@NotNull TerraformWorld tw, @NotNull SimpleBlock block) {
      return this.build(tw, block.getPopData(), block.getX(), block.getY(), block.getZ());
   }

   public boolean build(@NotNull TerraformWorld tw, @NotNull PopulatorDataAbstract data, int x, int y, int z) {
      if (!TConfig.areTreesEnabled()) {
         return false;
      } else {
         this.fractalLeaves.purgeOccupiedLeavesCache();
         if (!this.heightGradientChecked && !this.checkGradient(data, x, z)) {
            return false;
         } else {
            if (TConfig.c.MISC_TREES_FORCE_LOGS) {
               this.trunkType = Material.getMaterial(StringUtils.replace(this.trunkType.toString(), "WOOD", "LOG"));
            }

            this.oriX = x;
            this.oriY = y;
            this.oriZ = z;
            this.tw = tw;
            this.fractalLeaves.setOriY(this.oriY);
            this.fractalLeaves.setTw(tw);
            this.fractalLeaves.setMaxHeight(this.maxHeight);
            FastNoise noiseGen = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.FRACTALTREES_BASE_NOISE, (world) -> {
               FastNoise n = new FastNoise((int)world.getSeed());
               n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
               n.SetFractalOctaves(5);
               return n;
            });
            noiseGen.SetFrequency(this.branchNoiseFrequency);
            this.rand = tw.getRand(256L * (long)x + 16L * (long)y + (long)z);
            SimpleBlock base = new SimpleBlock(data, x, y, z);
            if (this.top == null) {
               this.top = base;
            }

            this.initialAngle = 1.5707963267948966D + GenUtils.randDouble(this.rand, -this.initialTilt, this.initialTilt);
            if (this.alwaysOneStraightBranchLength == 0) {
               this.alwaysOneStraightBranchLength = this.baseHeight;
            }

            double initialPitch;
            if (this.minInitialTilt != -1.0D) {
               initialPitch = (double)(new int[]{-1, 1})[this.rand.nextInt(2)] * GenUtils.randDouble(this.rand, this.minInitialTilt, this.initialTilt);
            } else {
               initialPitch = GenUtils.randDouble(this.rand, -this.initialTilt, this.initialTilt);
            }

            if (this.alwaysOneStraight > 0) {
               this.fractalBranch(this.rand, base, this.initialAngle, initialPitch, 0, (double)this.baseThickness, (double)this.baseHeight);
            } else {
               this.initialHeight = this.baseHeight + GenUtils.randInt(-this.heightVariation, this.heightVariation);
               this.fractalBranch(this.rand, base, this.initialAngle, initialPitch, 0, (double)this.baseThickness, (double)this.initialHeight);
            }

            if (this.beeHive != null) {
               for(int i = 0; i < 8; ++i) {
                  if (!this.beeHive.isSolid()) {
                     BeeHiveSpawner.spawnFullBeeNest(this.beeHive);
                     break;
                  }

                  this.beeHive = this.beeHive.getDown();
               }
            }

            return true;
         }
      }
   }

   private void fractalBranch(@NotNull Random rand, @NotNull SimpleBlock base, double pitch, double yaw, int depth, double thickness, double size) {
      if (thickness < (double)this.minThickness) {
         thickness = (double)this.minThickness;
      }

      if (pitch > this.maxPitch) {
         pitch = this.maxPitch - this.rta();
      } else if (pitch < this.minPitch) {
         pitch = this.minPitch + this.rta();
      }

      if (depth >= this.maxDepth) {
         this.fractalLeaves.placeLeaves(this.tw, this.oriY, this.maxHeight, base);
         base.setType(this.trunkType);
      } else if (size <= 0.0D) {
         this.fractalLeaves.placeLeaves(this.tw, this.oriY, this.maxHeight, base);
         base.setType(this.trunkType);
      } else {
         boolean restore = false;
         if (this.noMainStem && size == (double)this.initialHeight) {
            restore = true;
            size = 0.0D;
         }

         int y = (int)Math.round(size * Math.sin(pitch));
         int x = (int)Math.round(size * Math.cos(pitch) * Math.sin(yaw));
         int z = (int)Math.round(size * Math.cos(pitch) * Math.cos(yaw));
         SimpleBlock two = base.getRelative(x, y, z);
         if (two.getY() > this.top.getY()) {
            this.top = two;
         }

         if (two.getY() - this.oriY > this.height) {
            this.height = two.getY() - this.oriY;
         }

         if (restore) {
            two = base;
            size = (double)this.baseHeight;
         }

         this.drawLine(base, two, (int)size, thickness);
         if (this.beeHive == null && GenUtils.chance(rand, (int)(this.beeChance * 1000.0D), 1000)) {
            for(int i = 0; i < 3; ++i) {
               if (!two.getRelative(0, -i, 0).isSolid()) {
                  this.beeHive = two.getRelative(0, -i, 0);
                  break;
               }
            }
         }

         ++this.fractalsDone;
         if (this.fractalsDone % this.fractalThreshold != 0 && thickness >= 1.0D && size >= 1.0D) {
            this.fractalBranch(rand, two, pitch - this.randomAngle(depth), yaw + (double)(GenUtils.randInt(rand, 1, 5) * GenUtils.getSign(rand)) * this.rta(), depth, thickness, size);
         } else if (this.alwaysOneStraight > 0 && pitch != this.initialAngle) {
            this.fractalBranch(rand, two, pitch - this.randomAngle(depth), yaw - this.rta(), 99, thickness - (double)this.thicknessDecrement, size - (double)this.lengthDecrement);
         } else {
            if (this.alwaysOneStraight > 0) {
               this.alwaysOneStraightBranchLength -= (int)this.lengthDecrement;
               this.lengthDecrement *= this.lengthDecrementMultiplier;
               if (depth >= this.alwaysOneStraightBranchSpawningDepth) {
                  this.fractalBranch(rand, two, pitch + this.randomAngle(depth), -this.ra(0.7853981633974483D, this.alwaysOneStraightBranchYawLowerMultiplier, this.alwaysOneStraightBranchYawUpperMultiplier), depth + 1, thickness - (double)this.thicknessDecrement, (double)this.alwaysOneStraightBranchLength);
                  this.fractalBranch(rand, two, pitch + this.randomAngle(depth), this.ra(0.7853981633974483D, this.alwaysOneStraightBranchYawLowerMultiplier, this.alwaysOneStraightBranchYawUpperMultiplier), depth + 1, thickness - (double)this.thicknessDecrement, (double)this.alwaysOneStraightBranchLength);
                  this.fractalBranch(rand, two, pitch + this.randomAngle(depth), 5.0D * this.ra(0.7853981633974483D, this.alwaysOneStraightBranchYawLowerMultiplier, this.alwaysOneStraightBranchYawUpperMultiplier), depth + 1, thickness - (double)this.thicknessDecrement, (double)this.alwaysOneStraightBranchLength);
                  this.fractalBranch(rand, two, pitch + this.randomAngle(depth), -5.0D * this.ra(0.7853981633974483D, this.alwaysOneStraightBranchYawLowerMultiplier, this.alwaysOneStraightBranchYawUpperMultiplier), depth + 1, thickness - (double)this.thicknessDecrement, (double)this.alwaysOneStraightBranchLength);
                  if (this.alwaysOneStraightExtendedBranches) {
                     this.fractalBranch(rand, two, pitch + this.randomAngle(depth), this.ra(0.0D, this.alwaysOneStraightBranchYawLowerMultiplier, this.alwaysOneStraightBranchYawUpperMultiplier), depth + 1, thickness - (double)this.thicknessDecrement, (double)this.alwaysOneStraightBranchLength);
                     this.fractalBranch(rand, two, pitch + this.randomAngle(depth), this.ra(1.5707963267948966D, this.alwaysOneStraightBranchYawLowerMultiplier, this.alwaysOneStraightBranchYawUpperMultiplier), depth + 1, thickness - (double)this.thicknessDecrement, (double)this.alwaysOneStraightBranchLength);
                     this.fractalBranch(rand, two, pitch + this.randomAngle(depth), this.ra(3.141592653589793D, this.alwaysOneStraightBranchYawLowerMultiplier, this.alwaysOneStraightBranchYawUpperMultiplier), depth + 1, thickness - (double)this.thicknessDecrement, (double)this.alwaysOneStraightBranchLength);
                     this.fractalBranch(rand, two, pitch + this.randomAngle(depth), -this.ra(1.5707963267948966D, this.alwaysOneStraightBranchYawLowerMultiplier, this.alwaysOneStraightBranchYawUpperMultiplier), depth + 1, thickness - (double)this.thicknessDecrement, (double)this.alwaysOneStraightBranchLength);
                  }
               }

               this.fractalBranch(rand, two, pitch, yaw, depth + 1, thickness - (double)this.thicknessDecrement, (double)this.alwaysOneStraight);
            } else {
               this.fractalBranch(rand, two, pitch - this.randomAngle(depth), yaw - this.rta(), depth + 1, thickness - (double)this.thicknessDecrement, size - (double)this.lengthDecrement);
               this.fractalBranch(rand, two, pitch + this.randomAngle(depth), yaw + this.rta(), depth + 1, thickness - (double)this.thicknessDecrement, size - (double)this.lengthDecrement);
               this.fractalBranch(rand, two, pitch + this.randomAngle(depth), yaw + 5.0D * this.rta(), depth + 1, thickness - (double)this.thicknessDecrement, size - (double)this.lengthDecrement);
               this.fractalBranch(rand, two, pitch + this.randomAngle(depth), yaw - 5.0D * this.rta(), depth + 1, thickness - (double)this.thicknessDecrement, size - (double)this.lengthDecrement);
            }

         }
      }
   }

   private void drawLine(@NotNull SimpleBlock one, @NotNull SimpleBlock two, int segments, double thickness) {
      if (!one.equals(two)) {
         Vector v = two.toVector().subtract(one.toVector());

         for(int i = 0; i <= segments; ++i) {
            Vector seg = v.clone().multiply((float)i / (float)segments);
            SimpleBlock segment = one.getRelative(seg);
            this.replaceSphere((float)thickness / 2.0F, segment, this.trunkType);
         }

      }
   }

   private void replaceSphere(float radius, @NotNull SimpleBlock base, @NotNull Material type) {
      if (!(radius <= 0.0F)) {
         this.replaceSphere(radius, radius, radius, base, type);
      }
   }

   private void replaceSphere(float rX, float rY, float rZ, @NotNull SimpleBlock block, @NotNull Material type) {
      if (!(rX <= 0.0F) || !(rY <= 0.0F) || !(rZ <= 0.0F)) {
         if ((double)rX <= 0.5D && (double)rY <= 0.5D && (double)rZ <= 0.5D) {
            block.rsetType(BlockUtils.replacableByTrees, type);
            if (Tag.WALLS.isTagged(type)) {
               BlockUtils.correctMultifacingData(block);
            }

         } else {
            float noiseMultiplier = this.branchNoiseMultiplier;
            FastNoise noiseGen = NoiseCacheHandler.getNoise(this.tw, NoiseCacheHandler.NoiseCacheEntry.FRACTALTREES_BASE_NOISE, (world) -> {
               FastNoise n = new FastNoise((int)world.getSeed());
               n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
               n.SetFractalOctaves(5);
               return n;
            });
            noiseGen.SetFrequency(this.branchNoiseFrequency);
            ArrayList<SimpleBlock> changed = new ArrayList();

            for(float y = -rY; y <= rY; ++y) {
               for(float x = -rX; x <= rX; ++x) {
                  for(float z = -rZ; z <= rZ; ++z) {
                     SimpleBlock rel = block.getRelative(Math.round(x), Math.round(y), Math.round(z));
                     if (rel.getY() - this.oriY > this.maxHeight) {
                        return;
                     }

                     if (rel.getY() - this.oriY == this.maxHeight && this.rand.nextBoolean()) {
                        return;
                     }

                     double equationResult = Math.pow((double)x, 2.0D) / Math.pow((double)rX, 2.0D) + Math.pow((double)y, 2.0D) / Math.pow((double)rY, 2.0D) + Math.pow((double)z, 2.0D) / Math.pow((double)rZ, 2.0D);
                     if (equationResult <= (double)(1.0F + noiseMultiplier * noiseGen.GetNoise((float)rel.getX(), (float)rel.getY(), (float)rel.getZ()))) {
                        rel.rsetType(BlockUtils.replacableByTrees, type);
                        if (Tag.WALLS.isTagged(type)) {
                           BlockUtils.correctMultifacingData(rel);
                        }

                        if (this.coralDecoration && !changed.contains(rel)) {
                           changed.add(rel);
                        }

                        if (this.cocoaBeans > 0 && Math.abs(x) >= rX - 2.0F && Math.abs(z) >= rZ - 2.0F && GenUtils.chance(this.cocoaBeans, 100)) {
                           BlockFace[] var15 = BlockUtils.directBlockFaces;
                           int var16 = var15.length;

                           for(int var17 = 0; var17 < var16; ++var17) {
                              BlockFace face = var15[var17];
                              Directional dir = (Directional)Bukkit.createBlockData(Material.COCOA);
                              dir.setFacing(face.getOppositeFace());
                              ((Ageable)dir).setAge(GenUtils.randInt(this.rand, 0, ((Ageable)dir).getMaximumAge()));
                              SimpleBlock beans = rel.getRelative(face);
                              if (!beans.isSolid() && beans.getType() != Material.WATER) {
                                 beans.setBlockData(dir);
                              }
                           }
                        }

                        if (this.vines > 0 && Math.abs(x) >= rX - 2.0F && Math.abs(z) >= rZ - 2.0F) {
                           if (GenUtils.chance(2, 10)) {
                              this.dangleLeavesDown(rel, this.vines / 2, this.vines);
                           } else if (GenUtils.chance(1, 10)) {
                              rel.rsetType(BlockUtils.replacableByTrees, this.trunkType);
                              BlockUtils.vineUp(rel, 4);
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
                     BlockFace[] var22 = BlockUtils.directBlockFaces;
                     int var23 = var22.length;

                     for(int var24 = 0; var24 < var23; ++var24) {
                        BlockFace face = var22[var24];
                        if (Tag.WALL_CORALS.isTagged(sb.getRelative(face).getType())) {
                           sb.getRelative(face).setType(Material.WATER);
                        }
                     }

                     if (sb.getUp().getType() == Material.SEA_PICKLE || Tag.CORAL_PLANTS.isTagged(sb.getUp().getType())) {
                        sb.getUp().setType(Material.WATER);
                     }

                     sb.setType(Material.WATER);
                  } else {
                     sb.setType(this.trunkType);
                  }
               }

               return;
            }
         }
      }
   }

   void dangleLeavesDown(@NotNull SimpleBlock block, int min, int max) {
      Material material = this.fractalLeaves.material[this.rand.nextInt(this.fractalLeaves.material.length)];
      BlockData type = Bukkit.createBlockData(material);
      if (Tag.LEAVES.isTagged(material)) {
         Leaves leaf = (Leaves)type;
         leaf.setDistance(1);
      }

      for(int i = 1; i <= GenUtils.randInt(min, max) && !block.getRelative(0, -i, 0).isSolid(); ++i) {
         block.getRelative(0, -i, 0).rsetBlockData(BlockUtils.replacableByTrees, type);
      }

      if (Tag.LEAVES.isTagged(material)) {
         block.rsetType(BlockUtils.replacableByTrees, this.trunkType);
      }

      BlockFace[] var12 = BlockUtils.directBlockFaces;
      int var7 = var12.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         BlockFace face = var12[var8];
         material = this.fractalLeaves.material[this.rand.nextInt(this.fractalLeaves.material.length)];
         type = Bukkit.createBlockData(material);
         if (Tag.LEAVES.isTagged(material)) {
            Leaves leaf = (Leaves)type;
            leaf.setDistance(1);
         }

         block.getRelative(face).rsetBlockData(BlockUtils.replacableByTrees, type);
      }

      block.getUp().rsetBlockData(BlockUtils.replacableByTrees, type);
   }

   @NotNull
   public FractalTreeBuilder setSnowyLeaves(boolean snowy) {
      this.fractalLeaves.setSnowy(snowy);
      return this;
   }

   @NotNull
   private FractalTreeBuilder setVines(int vines) {
      this.vines = vines;
      return this;
   }

   @NotNull
   private FractalTreeBuilder setHeightVariation(int heightVariation) {
      this.heightVariation = heightVariation;
      return this;
   }

   @NotNull
   private FractalTreeBuilder setMinBend(double bend) {
      this.minBend = bend;
      return this;
   }

   @NotNull
   private FractalTreeBuilder setMaxBend(double bend) {
      this.maxBend = bend;
      return this;
   }

   @NotNull
   private FractalTreeBuilder setInitialTilt(double initialTilt) {
      this.initialTilt = initialTilt;
      return this;
   }

   @NotNull
   private FractalTreeBuilder setMinInitialTilt(double minInitialTilt) {
      this.minInitialTilt = minInitialTilt;
      return this;
   }

   @NotNull
   public FractalTreeBuilder setFractalLeaves(FractalLeaves fractalLeaves) {
      this.fractalLeaves = fractalLeaves;
      return this;
   }

   @NotNull
   public FractalTreeBuilder setTrunkType(Material log) {
      this.trunkType = log;
      return this;
   }

   @NotNull
   private FractalTreeBuilder setLengthDecrement(float d) {
      this.lengthDecrement = d;
      return this;
   }

   @NotNull
   private FractalTreeBuilder setMaxDepth(int d) {
      this.maxDepth = d;
      return this;
   }

   @NotNull
   private FractalTreeBuilder setAlwaysOneStraight(int val) {
      this.alwaysOneStraight = val;
      return this;
   }

   @NotNull
   private FractalTreeBuilder setAlwaysOneStraightExtendedBranches(boolean bool) {
      this.alwaysOneStraightExtendedBranches = bool;
      return this;
   }

   @NotNull
   private FractalTreeBuilder setNoMainStem(boolean bool) {
      this.noMainStem = bool;
      return this;
   }

   @NotNull
   private FractalTreeBuilder setBeeChance(double beeChance) {
      this.beeChance = beeChance;
      return this;
   }

   private int getCocoaBeans() {
      return this.cocoaBeans;
   }

   @NotNull
   private FractalTreeBuilder setCocoaBeans(int cocoaBeans) {
      this.cocoaBeans = cocoaBeans;
      return this;
   }

   @NotNull
   private FractalTreeBuilder setThicknessDecrement(float d) {
      this.thicknessDecrement = d;
      return this;
   }

   @NotNull
   private FractalTreeBuilder setBaseThickness(float baseThickness) {
      this.baseThickness = baseThickness;
      return this;
   }

   @NotNull
   private FractalTreeBuilder setMinThickness(float minThickness) {
      this.minThickness = minThickness;
      return this;
   }

   @NotNull
   private FractalTreeBuilder setBaseHeight(int h) {
      this.baseHeight = h;
      return this;
   }

   @NotNull
   private FractalTreeBuilder setFractalThreshold(int i) {
      this.fractalThreshold = i;
      return this;
   }

   @NotNull
   private FractalTreeBuilder setMaxPitch(double max) {
      this.maxPitch = max;
      return this;
   }

   @NotNull
   private FractalTreeBuilder setBranchNoiseMultiplier(float multiplier) {
      this.branchNoiseMultiplier = multiplier;
      return this;
   }

   @NotNull
   private FractalTreeBuilder setDepthPitchMultiplier(float depthPitchMultiplier) {
      this.depthPitchMultiplier = depthPitchMultiplier;
      return this;
   }

   @NotNull
   private FractalTreeBuilder setLeafBranchFrequency(float freq) {
      this.branchNoiseFrequency = freq;
      return this;
   }

   @NotNull
   public FractalTreeBuilder setMaxHeight(int max) {
      this.maxHeight = max;
      return this;
   }

   @NotNull
   public FractalTreeBuilder skipGradientCheck() {
      this.heightGradientChecked = true;
      return this;
   }

   private int getHeight() {
      return this.height;
   }

   @NotNull
   private FractalTreeBuilder setMinPitch(double min) {
      this.minPitch = min;
      return this;
   }

   private double randomAngle(int depth) {
      return Math.pow((double)this.depthPitchMultiplier, (double)depth) * GenUtils.randDouble(this.rand, this.minBend, this.maxBend);
   }

   private double rta() {
      return GenUtils.randDouble(new Random(), 0.41887902047863906D, 0.6283185307179586D);
   }

   private double ra(double base, double lowerBound, double upperBound) {
      return GenUtils.randDouble(new Random(), lowerBound * base, upperBound * base);
   }

   @NotNull
   private FractalTreeBuilder setCoralDecoration(boolean d) {
      this.coralDecoration = d;
      this.fractalLeaves.coralDecoration = d;
      return this;
   }

   @NotNull
   private FractalTreeBuilder setAlwaysOneStraightBranchLength(int alwaysOneStraightBranchLength) {
      this.alwaysOneStraightBranchLength = alwaysOneStraightBranchLength;
      return this;
   }

   @NotNull
   private FractalTreeBuilder setLengthDecrementMultiplier(float lengthDecrementMultiplier) {
      this.lengthDecrementMultiplier = lengthDecrementMultiplier;
      return this;
   }

   @NotNull
   private FractalTreeBuilder setAlwaysOneStraightBranchYawLowerMultiplier(double alwaysOneStraightBranchYawLowerMultiplier) {
      this.alwaysOneStraightBranchYawLowerMultiplier = alwaysOneStraightBranchYawLowerMultiplier;
      return this;
   }

   @NotNull
   private FractalTreeBuilder setAlwaysOneStraightBranchYawUpperMultiplier(double alwaysOneStraightBranchYawUpperMultiplier) {
      this.alwaysOneStraightBranchYawUpperMultiplier = alwaysOneStraightBranchYawUpperMultiplier;
      return this;
   }

   @NotNull
   private FractalTreeBuilder setAlwaysOneStraightBranchSpawningDepth(int alwaysOneStraightBranchSpawningDepth) {
      this.alwaysOneStraightBranchSpawningDepth = alwaysOneStraightBranchSpawningDepth;
      return this;
   }
}
