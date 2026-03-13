package com.volmit.iris.engine.object;

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

@Snippet("biome-palette")
@Desc("A layer of surface / subsurface material in biomes")
public class IrisBiomePaletteLayer {
   private final transient AtomicCache<KList<BlockData>> blockData = new AtomicCache();
   private final transient AtomicCache<CNG> layerGenerator = new AtomicCache();
   private final transient AtomicCache<CNG> heightGenerator = new AtomicCache();
   @Desc("The style of noise")
   private IrisGeneratorStyle style;
   @DependsOn({"minHeight", "maxHeight"})
   @MinNumber(0.0D)
   @MaxNumber(2032.0D)
   @Desc("The min thickness of this layer")
   private int minHeight;
   @DependsOn({"minHeight", "maxHeight"})
   @MinNumber(1.0D)
   @MaxNumber(2032.0D)
   @Desc("The max thickness of this layer")
   private int maxHeight;
   @Desc("If set, this layer will change size depending on the slope. If in bounds, the layer will get larger (taller) the closer to the center of this slope clip it is. If outside of the slipe's bounds, this layer will not show.")
   private IrisSlopeClip slopeCondition;
   @MinNumber(1.0E-4D)
   @Desc("The terrain zoom mostly for zooming in on a wispy palette")
   private double zoom;
   @Required
   @ArrayType(
      min = 1,
      type = IrisBlockData.class
   )
   @Desc("The palette of blocks to be used in this layer")
   private KList<IrisBlockData> palette;

   public CNG getHeightGenerator(RNG rng, IrisData data) {
      return (CNG)this.heightGenerator.aquire(() -> {
         return CNG.signature(var1.nextParallelRNG(this.minHeight * this.maxHeight + this.getBlockData(var2).size()));
      });
   }

   public BlockData get(RNG rng, double x, double y, double z, IrisData data) {
      if (this.getBlockData(var8).isEmpty()) {
         return null;
      } else {
         return this.getBlockData(var8).size() == 1 ? (BlockData)this.getBlockData(var8).get(0) : (BlockData)this.getLayerGenerator(var1, var8).fit((List)this.getBlockData(var8), var2 / this.zoom, var4 / this.zoom, var6 / this.zoom);
      }
   }

   public CNG getLayerGenerator(RNG rng, IrisData data) {
      return (CNG)this.layerGenerator.aquire(() -> {
         RNG var3 = var1.nextParallelRNG(this.minHeight + this.maxHeight + this.getBlockData(var2).size());
         return this.style.create(var3, var2);
      });
   }

   public KList<IrisBlockData> add(String b) {
      this.palette.add((Object)(new IrisBlockData(var1)));
      return this.palette;
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

   public IrisBiomePaletteLayer zero() {
      this.palette.clear();
      return this;
   }

   @Generated
   public IrisBiomePaletteLayer() {
      this.style = NoiseStyle.STATIC.style();
      this.minHeight = 1;
      this.maxHeight = 1;
      this.slopeCondition = new IrisSlopeClip();
      this.zoom = 5.0D;
      this.palette = (new KList()).qadd(new IrisBlockData("GRASS_BLOCK"));
   }

   @Generated
   public IrisBiomePaletteLayer(final IrisGeneratorStyle style, final int minHeight, final int maxHeight, final IrisSlopeClip slopeCondition, final double zoom, final KList<IrisBlockData> palette) {
      this.style = NoiseStyle.STATIC.style();
      this.minHeight = 1;
      this.maxHeight = 1;
      this.slopeCondition = new IrisSlopeClip();
      this.zoom = 5.0D;
      this.palette = (new KList()).qadd(new IrisBlockData("GRASS_BLOCK"));
      this.style = var1;
      this.minHeight = var2;
      this.maxHeight = var3;
      this.slopeCondition = var4;
      this.zoom = var5;
      this.palette = var7;
   }

   @Generated
   public AtomicCache<KList<BlockData>> getBlockData() {
      return this.blockData;
   }

   @Generated
   public AtomicCache<CNG> getLayerGenerator() {
      return this.layerGenerator;
   }

   @Generated
   public AtomicCache<CNG> getHeightGenerator() {
      return this.heightGenerator;
   }

   @Generated
   public IrisGeneratorStyle getStyle() {
      return this.style;
   }

   @Generated
   public int getMinHeight() {
      return this.minHeight;
   }

   @Generated
   public int getMaxHeight() {
      return this.maxHeight;
   }

   @Generated
   public IrisSlopeClip getSlopeCondition() {
      return this.slopeCondition;
   }

   @Generated
   public double getZoom() {
      return this.zoom;
   }

   @Generated
   public KList<IrisBlockData> getPalette() {
      return this.palette;
   }

   @Generated
   public IrisBiomePaletteLayer setStyle(final IrisGeneratorStyle style) {
      this.style = var1;
      return this;
   }

   @Generated
   public IrisBiomePaletteLayer setMinHeight(final int minHeight) {
      this.minHeight = var1;
      return this;
   }

   @Generated
   public IrisBiomePaletteLayer setMaxHeight(final int maxHeight) {
      this.maxHeight = var1;
      return this;
   }

   @Generated
   public IrisBiomePaletteLayer setSlopeCondition(final IrisSlopeClip slopeCondition) {
      this.slopeCondition = var1;
      return this;
   }

   @Generated
   public IrisBiomePaletteLayer setZoom(final double zoom) {
      this.zoom = var1;
      return this;
   }

   @Generated
   public IrisBiomePaletteLayer setPalette(final KList<IrisBlockData> palette) {
      this.palette = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisBiomePaletteLayer)) {
         return false;
      } else {
         IrisBiomePaletteLayer var2 = (IrisBiomePaletteLayer)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getMinHeight() != var2.getMinHeight()) {
            return false;
         } else if (this.getMaxHeight() != var2.getMaxHeight()) {
            return false;
         } else if (Double.compare(this.getZoom(), var2.getZoom()) != 0) {
            return false;
         } else {
            label54: {
               IrisGeneratorStyle var3 = this.getStyle();
               IrisGeneratorStyle var4 = var2.getStyle();
               if (var3 == null) {
                  if (var4 == null) {
                     break label54;
                  }
               } else if (var3.equals(var4)) {
                  break label54;
               }

               return false;
            }

            IrisSlopeClip var5 = this.getSlopeCondition();
            IrisSlopeClip var6 = var2.getSlopeCondition();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            KList var7 = this.getPalette();
            KList var8 = var2.getPalette();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisBiomePaletteLayer;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var8 = var2 * 59 + this.getMinHeight();
      var8 = var8 * 59 + this.getMaxHeight();
      long var3 = Double.doubleToLongBits(this.getZoom());
      var8 = var8 * 59 + (int)(var3 >>> 32 ^ var3);
      IrisGeneratorStyle var5 = this.getStyle();
      var8 = var8 * 59 + (var5 == null ? 43 : var5.hashCode());
      IrisSlopeClip var6 = this.getSlopeCondition();
      var8 = var8 * 59 + (var6 == null ? 43 : var6.hashCode());
      KList var7 = this.getPalette();
      var8 = var8 * 59 + (var7 == null ? 43 : var7.hashCode());
      return var8;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getBlockData());
      return "IrisBiomePaletteLayer(blockData=" + var10000 + ", layerGenerator=" + String.valueOf(this.getLayerGenerator()) + ", heightGenerator=" + String.valueOf(this.getHeightGenerator()) + ", style=" + String.valueOf(this.getStyle()) + ", minHeight=" + this.getMinHeight() + ", maxHeight=" + this.getMaxHeight() + ", slopeCondition=" + String.valueOf(this.getSlopeCondition()) + ", zoom=" + this.getZoom() + ", palette=" + String.valueOf(this.getPalette()) + ")";
   }
}
