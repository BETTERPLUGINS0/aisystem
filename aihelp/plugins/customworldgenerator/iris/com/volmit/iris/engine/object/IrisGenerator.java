package com.volmit.iris.engine.object;

import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.interpolation.IrisInterpolation;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.noise.CellGenerator;
import com.volmit.iris.util.plugin.VolmitSender;
import java.util.Iterator;
import java.util.List;
import lombok.Generated;

@Desc("Represents a composite generator of noise gens")
public class IrisGenerator extends IrisRegistrant {
   private final transient AtomicCache<CellGenerator> cellGen = new AtomicCache();
   @MinNumber(0.001D)
   @Desc("The zoom or frequency.")
   private double zoom = 1.0D;
   @MinNumber(0.0D)
   @Desc("The opacity, essentially a multiplier on the output.")
   private double opacity = 1.0D;
   @Desc("Multiply the compsites instead of adding them")
   private boolean multiplicitive = false;
   @MinNumber(0.001D)
   @Desc("The size of the cell fractures")
   private double cellFractureZoom = 1.0D;
   @MinNumber(0.0D)
   @Desc("Cell Fracture Coordinate Shuffling")
   private double cellFractureShuffle = 12.0D;
   @Desc("The height of fracture cells. Set to 0 to disable")
   private double cellFractureHeight = 0.0D;
   @MinNumber(0.0D)
   @MaxNumber(1.0D)
   @Desc("How big are the cells (X,Z) relative to the veins that touch them. Between 0 and 1. 0.1 means thick veins, small cells.")
   private double cellPercentSize = 0.75D;
   @Desc("The offset to shift this noise x")
   private double offsetX = 0.0D;
   @Desc("The offset to shift this noise z")
   private double offsetZ = 0.0D;
   @Required
   @Desc("The seed for this generator")
   private long seed = 1L;
   @Required
   @Desc("The interpolator to use when smoothing this generator into other regions & generators")
   private IrisInterpolator interpolator = new IrisInterpolator();
   @MinNumber(0.0D)
   @MaxNumber(8192.0D)
   @Desc("Cliff Height Max. Disable with 0 for min and max")
   private double cliffHeightMax = 0.0D;
   @MinNumber(0.0D)
   @MaxNumber(8192.0D)
   @Desc("Cliff Height Min. Disable with 0 for min and max")
   private double cliffHeightMin = 0.0D;
   @ArrayType(
      min = 1,
      type = IrisNoiseGenerator.class
   )
   @Desc("The list of noise gens this gen contains.")
   private KList<IrisNoiseGenerator> composite = new KList();
   @Desc("The noise gen for cliff height.")
   private IrisNoiseGenerator cliffHeightGenerator = new IrisNoiseGenerator();

   public double getMax() {
      return this.opacity;
   }

   public boolean hasCliffs() {
      return this.cliffHeightMax > 0.0D;
   }

   public CellGenerator getCellGenerator(long seed) {
      return (CellGenerator)this.cellGen.aquire(() -> {
         return new CellGenerator(new RNG(var1 + 239466L));
      });
   }

   public <T extends IRare> T fitRarity(KList<T> b, long superSeed, double rx, double rz) {
      if (var1.size() == 0) {
         return null;
      } else if (var1.size() == 1) {
         return (IRare)var1.get(0);
      } else {
         KList var8 = new KList();
         boolean var9 = false;
         int var10 = 1;
         Iterator var11 = var1.iterator();

         IRare var12;
         while(var11.hasNext()) {
            var12 = (IRare)var11.next();
            if (var12.getRarity() > var10) {
               var10 = var12.getRarity();
            }
         }

         ++var10;
         var11 = var1.iterator();

         while(var11.hasNext()) {
            var12 = (IRare)var11.next();

            for(int var13 = 0; var13 < var10 - var12.getRarity(); ++var13) {
               if (var9 = !var9) {
                  var8.add((Object)var12);
               } else {
                  var8.add(0, var12);
               }
            }
         }

         if (var8.size() == 1) {
            return (IRare)var8.get(0);
         } else if (var8.isEmpty()) {
            throw new RuntimeException("BAD RARITY MAP! RELATED TO: " + var1.toString(", or possibly "));
         } else {
            return (IRare)this.fit((List)var8, var2, var4, var6);
         }
      }
   }

   public <T> T fit(T[] v, long superSeed, double rx, double rz) {
      if (var1.length == 0) {
         return null;
      } else {
         return var1.length == 1 ? var1[0] : var1[this.fit(0, var1.length - 1, var2, var4, var6)];
      }
   }

   public <T> T fit(List<T> v, long superSeed, double rx, double rz) {
      if (var1.size() == 0) {
         return null;
      } else {
         return var1.size() == 1 ? var1.get(0) : var1.get(this.fit(0, var1.size() - 1, var2, var4, var6));
      }
   }

   public int fit(int min, int max, long superSeed, double rx, double rz) {
      if (var1 == var2) {
         return var1;
      } else {
         double var9 = this.getHeight(var5, var7, var3);
         return (int)Math.round(IrisInterpolation.lerp((double)var1, (double)var2, var9));
      }
   }

   public int fit(double min, double max, long superSeed, double rx, double rz) {
      if (var1 == var3) {
         return (int)Math.round(var1);
      } else {
         double var11 = this.getHeight(var7, var9, var5);
         return (int)Math.round(IrisInterpolation.lerp(var1, var3, var11));
      }
   }

   public double fitDouble(double min, double max, long superSeed, double rx, double rz) {
      if (var1 == var3) {
         return var1;
      } else {
         double var11 = this.getHeight(var7, var9, var5);
         return IrisInterpolation.lerp(var1, var3, var11);
      }
   }

   public double getHeight(double rx, double rz, long superSeed) {
      return this.getHeight(var1, 0.0D, var3, var5, true);
   }

   public double getHeight(double rx, double ry, double rz, long superSeed) {
      return this.getHeight(var1, var3, var5, var7, false);
   }

   public double getHeight(double rx, double ry, double rz, long superSeed, boolean no3d) {
      if (this.composite.isEmpty()) {
         return 0.0D;
      } else {
         int var10 = (int)(this.cliffHeightMin * 10.0D + 10.0D + this.cliffHeightMax * (double)this.getSeed() + this.offsetX + this.offsetZ);
         double var11 = this.multiplicitive ? 1.0D : 0.0D;
         double var13 = 0.0D;
         if (this.composite.size() == 1) {
            if (this.multiplicitive) {
               var11 *= ((IrisNoiseGenerator)this.composite.get(0)).getNoise(this.getSeed() + var7 + (long)var10, (var1 + this.offsetX) / this.zoom, (var5 + this.offsetZ) / this.zoom, this.getLoader());
            } else {
               var13 += ((IrisNoiseGenerator)this.composite.get(0)).getOpacity();
               var11 += ((IrisNoiseGenerator)this.composite.get(0)).getNoise(this.getSeed() + var7 + (long)var10, (var1 + this.offsetX) / this.zoom, (var5 + this.offsetZ) / this.zoom, this.getLoader());
            }
         } else {
            Iterator var15 = this.composite.iterator();

            while(var15.hasNext()) {
               IrisNoiseGenerator var16 = (IrisNoiseGenerator)var15.next();
               if (this.multiplicitive) {
                  var11 *= var16.getNoise(this.getSeed() + var7 + (long)var10, (var1 + this.offsetX) / this.zoom, (var5 + this.offsetZ) / this.zoom, this.getLoader());
               } else {
                  var13 += var16.getOpacity();
                  var11 += var16.getNoise(this.getSeed() + var7 + (long)var10, (var1 + this.offsetX) / this.zoom, (var5 + this.offsetZ) / this.zoom, this.getLoader());
               }
            }
         }

         double var17 = this.multiplicitive ? var11 * this.opacity : var11 / var13 * this.opacity;
         if (Double.isNaN(var17)) {
            var17 = 0.0D;
         }

         var17 = this.hasCliffs() ? this.cliff(var1, var5, var17, (double)(var7 + 294596L + (long)var10)) : var17;
         var17 = this.hasCellCracks() ? this.cell(var1, var5, var17, (double)(var7 + 48622L + (long)var10)) : var17;
         return var17;
      }
   }

   public double cell(double rx, double rz, double v, double superSeed) {
      this.getCellGenerator(this.getSeed() + 46222L).setShuffle(this.getCellFractureShuffle());
      return this.getCellGenerator(this.getSeed() + 46222L).getDistance(var1 / this.getCellFractureZoom(), var3 / this.getCellFractureZoom()) > this.getCellPercentSize() ? var5 * this.getCellFractureHeight() : var5;
   }

   private boolean hasCellCracks() {
      return this.getCellFractureHeight() != 0.0D;
   }

   public double getCliffHeight(double rx, double rz, double superSeed) {
      int var7 = (int)(this.cliffHeightMin * 10.0D + 10.0D + this.cliffHeightMax * (double)this.getSeed() + this.offsetX + this.offsetZ);
      double var8 = this.cliffHeightGenerator.getNoise((long)((double)this.getSeed() + var5 + (double)var7), (var1 + this.offsetX) / this.zoom, (var3 + this.offsetZ) / this.zoom, this.getLoader());
      return IrisInterpolation.lerp(this.cliffHeightMin, this.cliffHeightMax, var8);
   }

   public double cliff(double rx, double rz, double v, double superSeed) {
      double var9 = this.getCliffHeight(var1, var3, var7 - 34857.0D);
      return (double)Math.round(var5 * 255.0D / var9) * var9 / 255.0D;
   }

   public IrisGenerator rescale(double scale) {
      this.zoom /= var1;
      return this;
   }

   public KList<IrisNoiseGenerator> getAllComposites() {
      KList var1 = new KList();
      Iterator var2 = this.composite.iterator();

      while(var2.hasNext()) {
         IrisNoiseGenerator var3 = (IrisNoiseGenerator)var2.next();
         var1.addAll(var3.getAllComposites());
      }

      return var1;
   }

   public String getFolderName() {
      return "generators";
   }

   public String getTypeName() {
      return "Generator";
   }

   public void scanForErrors(JSONObject p, VolmitSender sender) {
   }

   @Generated
   public IrisGenerator() {
   }

   @Generated
   public IrisGenerator(final double zoom, final double opacity, final boolean multiplicitive, final double cellFractureZoom, final double cellFractureShuffle, final double cellFractureHeight, final double cellPercentSize, final double offsetX, final double offsetZ, final long seed, final IrisInterpolator interpolator, final double cliffHeightMax, final double cliffHeightMin, final KList<IrisNoiseGenerator> composite, final IrisNoiseGenerator cliffHeightGenerator) {
      this.zoom = var1;
      this.opacity = var3;
      this.multiplicitive = var5;
      this.cellFractureZoom = var6;
      this.cellFractureShuffle = var8;
      this.cellFractureHeight = var10;
      this.cellPercentSize = var12;
      this.offsetX = var14;
      this.offsetZ = var16;
      this.seed = var18;
      this.interpolator = var20;
      this.cliffHeightMax = var21;
      this.cliffHeightMin = var23;
      this.composite = var25;
      this.cliffHeightGenerator = var26;
   }

   @Generated
   public AtomicCache<CellGenerator> getCellGen() {
      return this.cellGen;
   }

   @Generated
   public double getZoom() {
      return this.zoom;
   }

   @Generated
   public double getOpacity() {
      return this.opacity;
   }

   @Generated
   public boolean isMultiplicitive() {
      return this.multiplicitive;
   }

   @Generated
   public double getCellFractureZoom() {
      return this.cellFractureZoom;
   }

   @Generated
   public double getCellFractureShuffle() {
      return this.cellFractureShuffle;
   }

   @Generated
   public double getCellFractureHeight() {
      return this.cellFractureHeight;
   }

   @Generated
   public double getCellPercentSize() {
      return this.cellPercentSize;
   }

   @Generated
   public double getOffsetX() {
      return this.offsetX;
   }

   @Generated
   public double getOffsetZ() {
      return this.offsetZ;
   }

   @Generated
   public long getSeed() {
      return this.seed;
   }

   @Generated
   public IrisInterpolator getInterpolator() {
      return this.interpolator;
   }

   @Generated
   public double getCliffHeightMax() {
      return this.cliffHeightMax;
   }

   @Generated
   public double getCliffHeightMin() {
      return this.cliffHeightMin;
   }

   @Generated
   public KList<IrisNoiseGenerator> getComposite() {
      return this.composite;
   }

   @Generated
   public IrisNoiseGenerator getCliffHeightGenerator() {
      return this.cliffHeightGenerator;
   }

   @Generated
   public IrisGenerator setZoom(final double zoom) {
      this.zoom = var1;
      return this;
   }

   @Generated
   public IrisGenerator setOpacity(final double opacity) {
      this.opacity = var1;
      return this;
   }

   @Generated
   public IrisGenerator setMultiplicitive(final boolean multiplicitive) {
      this.multiplicitive = var1;
      return this;
   }

   @Generated
   public IrisGenerator setCellFractureZoom(final double cellFractureZoom) {
      this.cellFractureZoom = var1;
      return this;
   }

   @Generated
   public IrisGenerator setCellFractureShuffle(final double cellFractureShuffle) {
      this.cellFractureShuffle = var1;
      return this;
   }

   @Generated
   public IrisGenerator setCellFractureHeight(final double cellFractureHeight) {
      this.cellFractureHeight = var1;
      return this;
   }

   @Generated
   public IrisGenerator setCellPercentSize(final double cellPercentSize) {
      this.cellPercentSize = var1;
      return this;
   }

   @Generated
   public IrisGenerator setOffsetX(final double offsetX) {
      this.offsetX = var1;
      return this;
   }

   @Generated
   public IrisGenerator setOffsetZ(final double offsetZ) {
      this.offsetZ = var1;
      return this;
   }

   @Generated
   public IrisGenerator setSeed(final long seed) {
      this.seed = var1;
      return this;
   }

   @Generated
   public IrisGenerator setInterpolator(final IrisInterpolator interpolator) {
      this.interpolator = var1;
      return this;
   }

   @Generated
   public IrisGenerator setCliffHeightMax(final double cliffHeightMax) {
      this.cliffHeightMax = var1;
      return this;
   }

   @Generated
   public IrisGenerator setCliffHeightMin(final double cliffHeightMin) {
      this.cliffHeightMin = var1;
      return this;
   }

   @Generated
   public IrisGenerator setComposite(final KList<IrisNoiseGenerator> composite) {
      this.composite = var1;
      return this;
   }

   @Generated
   public IrisGenerator setCliffHeightGenerator(final IrisNoiseGenerator cliffHeightGenerator) {
      this.cliffHeightGenerator = var1;
      return this;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getCellGen());
      return "IrisGenerator(cellGen=" + var10000 + ", zoom=" + this.getZoom() + ", opacity=" + this.getOpacity() + ", multiplicitive=" + this.isMultiplicitive() + ", cellFractureZoom=" + this.getCellFractureZoom() + ", cellFractureShuffle=" + this.getCellFractureShuffle() + ", cellFractureHeight=" + this.getCellFractureHeight() + ", cellPercentSize=" + this.getCellPercentSize() + ", offsetX=" + this.getOffsetX() + ", offsetZ=" + this.getOffsetZ() + ", seed=" + this.getSeed() + ", interpolator=" + String.valueOf(this.getInterpolator()) + ", cliffHeightMax=" + this.getCliffHeightMax() + ", cliffHeightMin=" + this.getCliffHeightMin() + ", composite=" + String.valueOf(this.getComposite()) + ", cliffHeightGenerator=" + String.valueOf(this.getCliffHeightGenerator()) + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisGenerator)) {
         return false;
      } else {
         IrisGenerator var2 = (IrisGenerator)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (Double.compare(this.getZoom(), var2.getZoom()) != 0) {
            return false;
         } else if (Double.compare(this.getOpacity(), var2.getOpacity()) != 0) {
            return false;
         } else if (this.isMultiplicitive() != var2.isMultiplicitive()) {
            return false;
         } else if (Double.compare(this.getCellFractureZoom(), var2.getCellFractureZoom()) != 0) {
            return false;
         } else if (Double.compare(this.getCellFractureShuffle(), var2.getCellFractureShuffle()) != 0) {
            return false;
         } else if (Double.compare(this.getCellFractureHeight(), var2.getCellFractureHeight()) != 0) {
            return false;
         } else if (Double.compare(this.getCellPercentSize(), var2.getCellPercentSize()) != 0) {
            return false;
         } else if (Double.compare(this.getOffsetX(), var2.getOffsetX()) != 0) {
            return false;
         } else if (Double.compare(this.getOffsetZ(), var2.getOffsetZ()) != 0) {
            return false;
         } else if (this.getSeed() != var2.getSeed()) {
            return false;
         } else if (Double.compare(this.getCliffHeightMax(), var2.getCliffHeightMax()) != 0) {
            return false;
         } else if (Double.compare(this.getCliffHeightMin(), var2.getCliffHeightMin()) != 0) {
            return false;
         } else {
            IrisInterpolator var3 = this.getInterpolator();
            IrisInterpolator var4 = var2.getInterpolator();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            label70: {
               KList var5 = this.getComposite();
               KList var6 = var2.getComposite();
               if (var5 == null) {
                  if (var6 == null) {
                     break label70;
                  }
               } else if (var5.equals(var6)) {
                  break label70;
               }

               return false;
            }

            IrisNoiseGenerator var7 = this.getCliffHeightGenerator();
            IrisNoiseGenerator var8 = var2.getCliffHeightGenerator();
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
      return var1 instanceof IrisGenerator;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = Double.doubleToLongBits(this.getZoom());
      int var28 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      long var5 = Double.doubleToLongBits(this.getOpacity());
      var28 = var28 * 59 + (int)(var5 >>> 32 ^ var5);
      var28 = var28 * 59 + (this.isMultiplicitive() ? 79 : 97);
      long var7 = Double.doubleToLongBits(this.getCellFractureZoom());
      var28 = var28 * 59 + (int)(var7 >>> 32 ^ var7);
      long var9 = Double.doubleToLongBits(this.getCellFractureShuffle());
      var28 = var28 * 59 + (int)(var9 >>> 32 ^ var9);
      long var11 = Double.doubleToLongBits(this.getCellFractureHeight());
      var28 = var28 * 59 + (int)(var11 >>> 32 ^ var11);
      long var13 = Double.doubleToLongBits(this.getCellPercentSize());
      var28 = var28 * 59 + (int)(var13 >>> 32 ^ var13);
      long var15 = Double.doubleToLongBits(this.getOffsetX());
      var28 = var28 * 59 + (int)(var15 >>> 32 ^ var15);
      long var17 = Double.doubleToLongBits(this.getOffsetZ());
      var28 = var28 * 59 + (int)(var17 >>> 32 ^ var17);
      long var19 = this.getSeed();
      var28 = var28 * 59 + (int)(var19 >>> 32 ^ var19);
      long var21 = Double.doubleToLongBits(this.getCliffHeightMax());
      var28 = var28 * 59 + (int)(var21 >>> 32 ^ var21);
      long var23 = Double.doubleToLongBits(this.getCliffHeightMin());
      var28 = var28 * 59 + (int)(var23 >>> 32 ^ var23);
      IrisInterpolator var25 = this.getInterpolator();
      var28 = var28 * 59 + (var25 == null ? 43 : var25.hashCode());
      KList var26 = this.getComposite();
      var28 = var28 * 59 + (var26 == null ? 43 : var26.hashCode());
      IrisNoiseGenerator var27 = this.getCliffHeightGenerator();
      var28 = var28 * 59 + (var27 == null ? 43 : var27.hashCode());
      return var28;
   }
}
