package com.volmit.iris.engine.object;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.noise.CNG;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import lombok.Generated;
import org.bukkit.block.data.BlockData;

@Snippet("palette")
@Desc("A palette of materials")
public class IrisMaterialPalette {
   private final transient AtomicCache<KList<BlockData>> blockData = new AtomicCache();
   private final transient AtomicCache<CNG> layerGenerator = new AtomicCache();
   private final transient AtomicCache<CNG> heightGenerator = new AtomicCache();
   @Desc("The style of noise")
   private IrisGeneratorStyle style;
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

   public BlockData get(RNG rng, double x, double y, double z, IrisData rdata) {
      if (this.getBlockData(var8).isEmpty()) {
         return null;
      } else {
         return this.getBlockData(var8).size() == 1 ? (BlockData)this.getBlockData(var8).get(0) : (BlockData)this.getLayerGenerator(var1, var8).fit((List)this.getBlockData(var8), var2 / this.zoom, var4 / this.zoom, var6 / this.zoom);
      }
   }

   public Optional<TileData> getTile(RNG rng, double x, double y, double z, IrisData rdata) {
      if (this.getBlockData(var8).isEmpty()) {
         return Optional.empty();
      } else {
         TileData var9 = this.getBlockData(var8).size() == 1 ? ((IrisBlockData)this.palette.get(0)).tryGetTile(var8) : ((IrisBlockData)this.palette.getRandom(var1)).tryGetTile(var8);
         return var9 != null ? Optional.of(var9) : Optional.empty();
      }
   }

   public CNG getLayerGenerator(RNG rng, IrisData rdata) {
      return (CNG)this.layerGenerator.aquire(() -> {
         RNG var3 = var1.nextParallelRNG(-23498896 + this.getBlockData(var2).size());
         return this.style.create(var3, var2);
      });
   }

   public IrisMaterialPalette qclear() {
      this.palette.clear();
      return this;
   }

   public KList<IrisBlockData> add(String b) {
      this.palette.add((Object)(new IrisBlockData(var1)));
      return this.palette;
   }

   public IrisMaterialPalette qadd(String b) {
      this.palette.add((Object)(new IrisBlockData(var1)));
      return this;
   }

   public KList<BlockData> getBlockData(IrisData rdata) {
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

   public IrisMaterialPalette zero() {
      this.palette.clear();
      return this;
   }

   @Generated
   public IrisMaterialPalette() {
      this.style = NoiseStyle.STATIC.style();
      this.zoom = 5.0D;
      this.palette = (new KList()).qadd(new IrisBlockData("STONE"));
   }

   @Generated
   public IrisMaterialPalette(final IrisGeneratorStyle style, final double zoom, final KList<IrisBlockData> palette) {
      this.style = NoiseStyle.STATIC.style();
      this.zoom = 5.0D;
      this.palette = (new KList()).qadd(new IrisBlockData("STONE"));
      this.style = var1;
      this.zoom = var2;
      this.palette = var4;
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
   public double getZoom() {
      return this.zoom;
   }

   @Generated
   public KList<IrisBlockData> getPalette() {
      return this.palette;
   }

   @Generated
   public IrisMaterialPalette setStyle(final IrisGeneratorStyle style) {
      this.style = var1;
      return this;
   }

   @Generated
   public IrisMaterialPalette setZoom(final double zoom) {
      this.zoom = var1;
      return this;
   }

   @Generated
   public IrisMaterialPalette setPalette(final KList<IrisBlockData> palette) {
      this.palette = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisMaterialPalette)) {
         return false;
      } else {
         IrisMaterialPalette var2 = (IrisMaterialPalette)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (Double.compare(this.getZoom(), var2.getZoom()) != 0) {
            return false;
         } else {
            IrisGeneratorStyle var3 = this.getStyle();
            IrisGeneratorStyle var4 = var2.getStyle();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            KList var5 = this.getPalette();
            KList var6 = var2.getPalette();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisMaterialPalette;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = Double.doubleToLongBits(this.getZoom());
      int var7 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      IrisGeneratorStyle var5 = this.getStyle();
      var7 = var7 * 59 + (var5 == null ? 43 : var5.hashCode());
      KList var6 = this.getPalette();
      var7 = var7 * 59 + (var6 == null ? 43 : var6.hashCode());
      return var7;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getBlockData());
      return "IrisMaterialPalette(blockData=" + var10000 + ", layerGenerator=" + String.valueOf(this.getLayerGenerator()) + ", heightGenerator=" + String.valueOf(this.getHeightGenerator()) + ", style=" + String.valueOf(this.getStyle()) + ", zoom=" + this.getZoom() + ", palette=" + String.valueOf(this.getPalette()) + ")";
   }
}
