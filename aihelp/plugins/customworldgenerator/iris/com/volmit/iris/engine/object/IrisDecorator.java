package com.volmit.iris.engine.object;

import com.volmit.iris.Iris;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.DependsOn;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.noise.CNG;
import java.util.Iterator;
import java.util.List;
import lombok.Generated;
import org.bukkit.block.data.BlockData;

@Snippet("decorator")
@Desc("A biome decorator is used for placing flowers, grass, cacti and so on")
public class IrisDecorator {
   private final transient AtomicCache<CNG> layerGenerator = new AtomicCache();
   private final transient AtomicCache<CNG> varianceGenerator = new AtomicCache();
   private final transient AtomicCache<CNG> heightGenerator = new AtomicCache();
   private final transient AtomicCache<KList<BlockData>> blockData = new AtomicCache();
   private final transient AtomicCache<KList<BlockData>> blockDataTops = new AtomicCache();
   @Desc("The varience dispersion is used when multiple blocks are put in the palette. Scatter scrambles them, Wispy shows streak-looking varience")
   private IrisGeneratorStyle variance;
   @Desc("Forcefully place this decorant anywhere it is supposed to go even if it should not go on a specific surface block. For example, you could force tallgrass to place on top of stone by using this.")
   private boolean forcePlace;
   @Desc("Forced the surface block of this decorant to be the specified block. Assumes forcePlace.")
   private IrisBlockData forceBlock;
   @ArrayType(
      min = 1,
      type = IrisBlockData.class
   )
   @Desc("When set, the decorator can only place onto any of these blocks.")
   private KList<IrisBlockData> whitelist;
   @ArrayType(
      min = 1,
      type = IrisBlockData.class
   )
   @Desc("When set, the decorator will never place onto any of these blocks.")
   private KList<IrisBlockData> blacklist;
   @Desc("The slope at which this decorator can be placed. Range from 0 to 10 by default. Calculated from a 3-block radius from the center of the decorator placement.")
   private IrisSlopeClip slopeCondition;
   @DependsOn({"scaleStack", "stackMin", "stackMax"})
   @Desc("If stackMax is set to true, use this to limit its max height for large caverns")
   private int absoluteMaxStack;
   @Desc("Dispersion is used to pick places to spawn. Scatter randomly places them (vanilla) or Wispy for a streak like patch system.")
   private IrisGeneratorStyle style;
   @DependsOn({"stackMin", "stackMax"})
   @Desc("If this decorator has a height more than 1 this changes how it picks the height between your maxes. Scatter = random, Wispy = wavy heights")
   private IrisGeneratorStyle heightVariance;
   @Desc("Tells iris where this decoration is a part of. I.e. SHORE_LINE or SEA_SURFACE")
   private IrisDecorationPart partOf;
   @DependsOn({"stackMin", "stackMax"})
   @MinNumber(1.0D)
   @MaxNumber(2032.0D)
   @Desc("The minimum repeat stack height (setting to 3 would stack 3 of <block> on top of each other")
   private int stackMin;
   @DependsOn({"stackMin", "stackMax"})
   @MinNumber(1.0D)
   @MaxNumber(2032.0D)
   @Desc("The maximum repeat stack height")
   private int stackMax;
   @DependsOn({"stackMin", "stackMax"})
   @Desc("Changes stackMin and stackMin from being absolute block heights and instead uses them as a percentage to scale the stack based on the cave height\n\nWithin a cave, setting them stackMin/max to 50 would make the stack 50% of the cave height")
   private boolean scaleStack;
   @Required
   @MinNumber(0.0D)
   @MaxNumber(1.0D)
   @Desc("The chance for this decorator to decorate at a given X,Y coordinate. This is hit 256 times per chunk (per surface block)")
   private double chance;
   @Required
   @ArrayType(
      min = 1,
      type = IrisBlockData.class
   )
   @Desc("The palette of blocks to pick from when this decorator needs to place.")
   private KList<IrisBlockData> palette;
   @ArrayType(
      min = 1,
      type = IrisBlockData.class
   )
   @Desc("The palette of blocks used at the very top of a 'stackMax' of higher than 1. For example, bamboo tops.")
   private KList<IrisBlockData> topPalette;
   @DependsOn({"topPalette"})
   @MinNumber(0.01D)
   @MaxNumber(1.0D)
   @Desc("When the stack passes the top threshold, the top palette will start being used instead of the normal palette.")
   private double topThreshold;

   public int getHeight(RNG rng, double x, double z, IrisData data) {
      return this.stackMin == this.stackMax ? this.stackMin : this.getHeightGenerator(var1, var6).fit(this.stackMin, this.stackMax, var2 / this.heightVariance.getZoom(), var4 / this.heightVariance.getZoom()) + 1;
   }

   public CNG getHeightGenerator(RNG rng, IrisData data) {
      return (CNG)this.heightGenerator.aquire(() -> {
         return this.heightVariance.create(var1.nextParallelRNG(this.getBlockData(var2).size() + this.stackMax + this.stackMin), var2);
      });
   }

   public CNG getGenerator(RNG rng, IrisData data) {
      return (CNG)this.layerGenerator.aquire(() -> {
         return this.style.create(var1.nextParallelRNG(this.getBlockData(var2).size()), var2);
      });
   }

   public CNG getVarianceGenerator(RNG rng, IrisData data) {
      return (CNG)this.varianceGenerator.aquire(() -> {
         return this.variance.create(var1.nextParallelRNG(this.getBlockData(var2).size()), var2).scale(1.0D / this.variance.getZoom());
      });
   }

   public KList<IrisBlockData> add(String b) {
      this.palette.add((Object)(new IrisBlockData(var1)));
      return this.palette;
   }

   public BlockData getBlockData(IrisBiome b, RNG rng, double x, double z, IrisData data) {
      if (this.getBlockData(var7).isEmpty()) {
         Iris.warn("Empty Block Data for " + var1.getName());
         return null;
      } else {
         double var8 = var3 / this.style.getZoom();
         double var10 = var5 / this.style.getZoom();
         if (this.getGenerator(var2, var7).fitDouble(0.0D, 1.0D, var8, var10) <= this.chance) {
            return this.getBlockData(var7).size() == 1 ? (BlockData)this.getBlockData(var7).get(0) : (BlockData)this.getVarianceGenerator(var2, var7).fit((List)this.getBlockData(var7), var5, var3);
         } else {
            return null;
         }
      }
   }

   public BlockData getBlockData100(IrisBiome b, RNG rng, double x, double y, double z, IrisData data) {
      if (this.getBlockData(var9).isEmpty()) {
         Iris.warn("Empty Block Data for " + var1.getName());
         return null;
      } else {
         if (!this.getVarianceGenerator(var2, var9).isStatic()) {
            double var10000 = var3 / this.style.getZoom();
            var10000 = var5 / this.style.getZoom();
            var10000 = var7 / this.style.getZoom();
         }

         return this.getBlockData(var9).size() == 1 ? (BlockData)this.getBlockData(var9).get(0) : ((BlockData)this.getVarianceGenerator(var2, var9).fit((List)this.getBlockData(var9), var7, var5, var3)).clone();
      }
   }

   public BlockData getBlockDataForTop(IrisBiome b, RNG rng, double x, double y, double z, IrisData data) {
      if (this.getBlockDataTops(var9).isEmpty()) {
         return this.getBlockData100(var1, var2, var3, var5, var7, var9);
      } else {
         double var10 = var3 / this.style.getZoom();
         double var12 = var7 / this.style.getZoom();
         if (this.getGenerator(var2, var9).fitDouble(0.0D, 1.0D, var10, var12) <= this.chance) {
            return this.getBlockData(var9).size() == 1 ? (BlockData)this.getBlockDataTops(var9).get(0) : (BlockData)this.getVarianceGenerator(var2, var9).fit((List)this.getBlockDataTops(var9), var7, var5, var3);
         } else {
            return null;
         }
      }
   }

   public KList<BlockData> getBlockData(IrisData data) {
      return (KList)this.blockData.aquire(() -> {
         KList var2 = new KList();
         Iterator var3 = this.palette.iterator();

         while(true) {
            IrisBlockData var4;
            BlockData var5;
            do {
               if (!var3.hasNext()) {
                  return var2;
               }

               var4 = (IrisBlockData)var3.next();
               var5 = var4.getBlockData(var1);
            } while(var5 == null);

            for(int var6 = 0; var6 < var4.getWeight(); ++var6) {
               var2.add((Object)var5);
            }
         }
      });
   }

   public KList<BlockData> getBlockDataTops(IrisData data) {
      return (KList)this.blockDataTops.aquire(() -> {
         KList var2 = new KList();
         Iterator var3 = this.topPalette.iterator();

         while(true) {
            IrisBlockData var4;
            BlockData var5;
            do {
               if (!var3.hasNext()) {
                  return var2;
               }

               var4 = (IrisBlockData)var3.next();
               var5 = var4.getBlockData(var1);
            } while(var5 == null);

            for(int var6 = 0; var6 < var4.getWeight(); ++var6) {
               var2.add((Object)var5);
            }
         }
      });
   }

   public boolean isStacking() {
      return this.getStackMax() > 1;
   }

   @Generated
   public IrisDecorator() {
      this.variance = NoiseStyle.STATIC.style();
      this.forcePlace = false;
      this.slopeCondition = new IrisSlopeClip();
      this.absoluteMaxStack = 30;
      this.style = NoiseStyle.STATIC.style();
      this.heightVariance = NoiseStyle.STATIC.style();
      this.partOf = IrisDecorationPart.NONE;
      this.stackMin = 1;
      this.stackMax = 1;
      this.scaleStack = false;
      this.chance = 0.1D;
      this.palette = (new KList()).qadd(new IrisBlockData("grass"));
      this.topPalette = new KList();
      this.topThreshold = 1.0D;
   }

   @Generated
   public IrisDecorator(final IrisGeneratorStyle variance, final boolean forcePlace, final IrisBlockData forceBlock, final KList<IrisBlockData> whitelist, final KList<IrisBlockData> blacklist, final IrisSlopeClip slopeCondition, final int absoluteMaxStack, final IrisGeneratorStyle style, final IrisGeneratorStyle heightVariance, final IrisDecorationPart partOf, final int stackMin, final int stackMax, final boolean scaleStack, final double chance, final KList<IrisBlockData> palette, final KList<IrisBlockData> topPalette, final double topThreshold) {
      this.variance = NoiseStyle.STATIC.style();
      this.forcePlace = false;
      this.slopeCondition = new IrisSlopeClip();
      this.absoluteMaxStack = 30;
      this.style = NoiseStyle.STATIC.style();
      this.heightVariance = NoiseStyle.STATIC.style();
      this.partOf = IrisDecorationPart.NONE;
      this.stackMin = 1;
      this.stackMax = 1;
      this.scaleStack = false;
      this.chance = 0.1D;
      this.palette = (new KList()).qadd(new IrisBlockData("grass"));
      this.topPalette = new KList();
      this.topThreshold = 1.0D;
      this.variance = var1;
      this.forcePlace = var2;
      this.forceBlock = var3;
      this.whitelist = var4;
      this.blacklist = var5;
      this.slopeCondition = var6;
      this.absoluteMaxStack = var7;
      this.style = var8;
      this.heightVariance = var9;
      this.partOf = var10;
      this.stackMin = var11;
      this.stackMax = var12;
      this.scaleStack = var13;
      this.chance = var14;
      this.palette = var16;
      this.topPalette = var17;
      this.topThreshold = var18;
   }

   @Generated
   public AtomicCache<CNG> getLayerGenerator() {
      return this.layerGenerator;
   }

   @Generated
   public AtomicCache<CNG> getVarianceGenerator() {
      return this.varianceGenerator;
   }

   @Generated
   public AtomicCache<CNG> getHeightGenerator() {
      return this.heightGenerator;
   }

   @Generated
   public AtomicCache<KList<BlockData>> getBlockData() {
      return this.blockData;
   }

   @Generated
   public AtomicCache<KList<BlockData>> getBlockDataTops() {
      return this.blockDataTops;
   }

   @Generated
   public IrisGeneratorStyle getVariance() {
      return this.variance;
   }

   @Generated
   public boolean isForcePlace() {
      return this.forcePlace;
   }

   @Generated
   public IrisBlockData getForceBlock() {
      return this.forceBlock;
   }

   @Generated
   public KList<IrisBlockData> getWhitelist() {
      return this.whitelist;
   }

   @Generated
   public KList<IrisBlockData> getBlacklist() {
      return this.blacklist;
   }

   @Generated
   public IrisSlopeClip getSlopeCondition() {
      return this.slopeCondition;
   }

   @Generated
   public int getAbsoluteMaxStack() {
      return this.absoluteMaxStack;
   }

   @Generated
   public IrisGeneratorStyle getStyle() {
      return this.style;
   }

   @Generated
   public IrisGeneratorStyle getHeightVariance() {
      return this.heightVariance;
   }

   @Generated
   public IrisDecorationPart getPartOf() {
      return this.partOf;
   }

   @Generated
   public int getStackMin() {
      return this.stackMin;
   }

   @Generated
   public int getStackMax() {
      return this.stackMax;
   }

   @Generated
   public boolean isScaleStack() {
      return this.scaleStack;
   }

   @Generated
   public double getChance() {
      return this.chance;
   }

   @Generated
   public KList<IrisBlockData> getPalette() {
      return this.palette;
   }

   @Generated
   public KList<IrisBlockData> getTopPalette() {
      return this.topPalette;
   }

   @Generated
   public double getTopThreshold() {
      return this.topThreshold;
   }

   @Generated
   public IrisDecorator setVariance(final IrisGeneratorStyle variance) {
      this.variance = var1;
      return this;
   }

   @Generated
   public IrisDecorator setForcePlace(final boolean forcePlace) {
      this.forcePlace = var1;
      return this;
   }

   @Generated
   public IrisDecorator setForceBlock(final IrisBlockData forceBlock) {
      this.forceBlock = var1;
      return this;
   }

   @Generated
   public IrisDecorator setWhitelist(final KList<IrisBlockData> whitelist) {
      this.whitelist = var1;
      return this;
   }

   @Generated
   public IrisDecorator setBlacklist(final KList<IrisBlockData> blacklist) {
      this.blacklist = var1;
      return this;
   }

   @Generated
   public IrisDecorator setSlopeCondition(final IrisSlopeClip slopeCondition) {
      this.slopeCondition = var1;
      return this;
   }

   @Generated
   public IrisDecorator setAbsoluteMaxStack(final int absoluteMaxStack) {
      this.absoluteMaxStack = var1;
      return this;
   }

   @Generated
   public IrisDecorator setStyle(final IrisGeneratorStyle style) {
      this.style = var1;
      return this;
   }

   @Generated
   public IrisDecorator setHeightVariance(final IrisGeneratorStyle heightVariance) {
      this.heightVariance = var1;
      return this;
   }

   @Generated
   public IrisDecorator setPartOf(final IrisDecorationPart partOf) {
      this.partOf = var1;
      return this;
   }

   @Generated
   public IrisDecorator setStackMin(final int stackMin) {
      this.stackMin = var1;
      return this;
   }

   @Generated
   public IrisDecorator setStackMax(final int stackMax) {
      this.stackMax = var1;
      return this;
   }

   @Generated
   public IrisDecorator setScaleStack(final boolean scaleStack) {
      this.scaleStack = var1;
      return this;
   }

   @Generated
   public IrisDecorator setChance(final double chance) {
      this.chance = var1;
      return this;
   }

   @Generated
   public IrisDecorator setPalette(final KList<IrisBlockData> palette) {
      this.palette = var1;
      return this;
   }

   @Generated
   public IrisDecorator setTopPalette(final KList<IrisBlockData> topPalette) {
      this.topPalette = var1;
      return this;
   }

   @Generated
   public IrisDecorator setTopThreshold(final double topThreshold) {
      this.topThreshold = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisDecorator)) {
         return false;
      } else {
         IrisDecorator var2 = (IrisDecorator)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.isForcePlace() != var2.isForcePlace()) {
            return false;
         } else if (this.getAbsoluteMaxStack() != var2.getAbsoluteMaxStack()) {
            return false;
         } else if (this.getStackMin() != var2.getStackMin()) {
            return false;
         } else if (this.getStackMax() != var2.getStackMax()) {
            return false;
         } else if (this.isScaleStack() != var2.isScaleStack()) {
            return false;
         } else if (Double.compare(this.getChance(), var2.getChance()) != 0) {
            return false;
         } else if (Double.compare(this.getTopThreshold(), var2.getTopThreshold()) != 0) {
            return false;
         } else {
            IrisGeneratorStyle var3 = this.getVariance();
            IrisGeneratorStyle var4 = var2.getVariance();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            label141: {
               IrisBlockData var5 = this.getForceBlock();
               IrisBlockData var6 = var2.getForceBlock();
               if (var5 == null) {
                  if (var6 == null) {
                     break label141;
                  }
               } else if (var5.equals(var6)) {
                  break label141;
               }

               return false;
            }

            label134: {
               KList var7 = this.getWhitelist();
               KList var8 = var2.getWhitelist();
               if (var7 == null) {
                  if (var8 == null) {
                     break label134;
                  }
               } else if (var7.equals(var8)) {
                  break label134;
               }

               return false;
            }

            KList var9 = this.getBlacklist();
            KList var10 = var2.getBlacklist();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            label120: {
               IrisSlopeClip var11 = this.getSlopeCondition();
               IrisSlopeClip var12 = var2.getSlopeCondition();
               if (var11 == null) {
                  if (var12 == null) {
                     break label120;
                  }
               } else if (var11.equals(var12)) {
                  break label120;
               }

               return false;
            }

            IrisGeneratorStyle var13 = this.getStyle();
            IrisGeneratorStyle var14 = var2.getStyle();
            if (var13 == null) {
               if (var14 != null) {
                  return false;
               }
            } else if (!var13.equals(var14)) {
               return false;
            }

            label106: {
               IrisGeneratorStyle var15 = this.getHeightVariance();
               IrisGeneratorStyle var16 = var2.getHeightVariance();
               if (var15 == null) {
                  if (var16 == null) {
                     break label106;
                  }
               } else if (var15.equals(var16)) {
                  break label106;
               }

               return false;
            }

            IrisDecorationPart var17 = this.getPartOf();
            IrisDecorationPart var18 = var2.getPartOf();
            if (var17 == null) {
               if (var18 != null) {
                  return false;
               }
            } else if (!var17.equals(var18)) {
               return false;
            }

            KList var19 = this.getPalette();
            KList var20 = var2.getPalette();
            if (var19 == null) {
               if (var20 != null) {
                  return false;
               }
            } else if (!var19.equals(var20)) {
               return false;
            }

            KList var21 = this.getTopPalette();
            KList var22 = var2.getTopPalette();
            if (var21 == null) {
               if (var22 != null) {
                  return false;
               }
            } else if (!var21.equals(var22)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisDecorator;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var17 = var2 * 59 + (this.isForcePlace() ? 79 : 97);
      var17 = var17 * 59 + this.getAbsoluteMaxStack();
      var17 = var17 * 59 + this.getStackMin();
      var17 = var17 * 59 + this.getStackMax();
      var17 = var17 * 59 + (this.isScaleStack() ? 79 : 97);
      long var3 = Double.doubleToLongBits(this.getChance());
      var17 = var17 * 59 + (int)(var3 >>> 32 ^ var3);
      long var5 = Double.doubleToLongBits(this.getTopThreshold());
      var17 = var17 * 59 + (int)(var5 >>> 32 ^ var5);
      IrisGeneratorStyle var7 = this.getVariance();
      var17 = var17 * 59 + (var7 == null ? 43 : var7.hashCode());
      IrisBlockData var8 = this.getForceBlock();
      var17 = var17 * 59 + (var8 == null ? 43 : var8.hashCode());
      KList var9 = this.getWhitelist();
      var17 = var17 * 59 + (var9 == null ? 43 : var9.hashCode());
      KList var10 = this.getBlacklist();
      var17 = var17 * 59 + (var10 == null ? 43 : var10.hashCode());
      IrisSlopeClip var11 = this.getSlopeCondition();
      var17 = var17 * 59 + (var11 == null ? 43 : var11.hashCode());
      IrisGeneratorStyle var12 = this.getStyle();
      var17 = var17 * 59 + (var12 == null ? 43 : var12.hashCode());
      IrisGeneratorStyle var13 = this.getHeightVariance();
      var17 = var17 * 59 + (var13 == null ? 43 : var13.hashCode());
      IrisDecorationPart var14 = this.getPartOf();
      var17 = var17 * 59 + (var14 == null ? 43 : var14.hashCode());
      KList var15 = this.getPalette();
      var17 = var17 * 59 + (var15 == null ? 43 : var15.hashCode());
      KList var16 = this.getTopPalette();
      var17 = var17 * 59 + (var16 == null ? 43 : var16.hashCode());
      return var17;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getLayerGenerator());
      return "IrisDecorator(layerGenerator=" + var10000 + ", varianceGenerator=" + String.valueOf(this.getVarianceGenerator()) + ", heightGenerator=" + String.valueOf(this.getHeightGenerator()) + ", blockData=" + String.valueOf(this.getBlockData()) + ", blockDataTops=" + String.valueOf(this.getBlockDataTops()) + ", variance=" + String.valueOf(this.getVariance()) + ", forcePlace=" + this.isForcePlace() + ", forceBlock=" + String.valueOf(this.getForceBlock()) + ", whitelist=" + String.valueOf(this.getWhitelist()) + ", blacklist=" + String.valueOf(this.getBlacklist()) + ", slopeCondition=" + String.valueOf(this.getSlopeCondition()) + ", absoluteMaxStack=" + this.getAbsoluteMaxStack() + ", style=" + String.valueOf(this.getStyle()) + ", heightVariance=" + String.valueOf(this.getHeightVariance()) + ", partOf=" + String.valueOf(this.getPartOf()) + ", stackMin=" + this.getStackMin() + ", stackMax=" + this.getStackMax() + ", scaleStack=" + this.isScaleStack() + ", chance=" + this.getChance() + ", palette=" + String.valueOf(this.getPalette()) + ", topPalette=" + String.valueOf(this.getTopPalette()) + ", topThreshold=" + this.getTopThreshold() + ")";
   }
}
