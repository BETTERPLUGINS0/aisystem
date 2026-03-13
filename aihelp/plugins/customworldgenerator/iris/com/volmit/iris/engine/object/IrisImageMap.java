package com.volmit.iris.engine.object;

import com.volmit.iris.Iris;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.interpolation.InterpolationMethod;
import com.volmit.iris.util.interpolation.IrisInterpolation;
import lombok.Generated;

@Snippet("image-map")
@Desc("Represents an image map")
public class IrisImageMap {
   @RegistryListResource(IrisImage.class)
   @Desc("Define the png image to read in this noise map")
   private String image = "";
   @MinNumber(1.0D)
   @Desc("The amount of distance a single pixel is when reading this map, reading x=13, would still read pixel 0 if the scale is 32. You can zoom this externally through noise styles for zooming out.")
   private double coordinateScale = 32.0D;
   @Desc("The interpolation method if the coordinateScale is greater than 1. This blends the image into noise. For nearest neighbor, use NONE.")
   private InterpolationMethod interpolationMethod;
   @Desc("The channel of the image to read from. This basically converts image data into a number betwen 0 to 1 per pixel using a certain 'channel/filter'")
   private IrisImageChannel channel;
   @Desc("Invert the channel input")
   private boolean inverted;
   @Desc("Tile the image coordinates")
   private boolean tiled;
   @Desc("Center 0,0 to the center of the image instead of the top left.")
   private boolean centered;
   private transient AtomicCache<IrisImage> imageCache;

   public double getNoise(IrisData data, int x, int z) {
      IrisImage var4 = (IrisImage)this.imageCache.aquire(() -> {
         return (IrisImage)var1.getImageLoader().load(this.image);
      });
      if (var4 == null) {
         Iris.error("NULL IMAGE FOR " + this.image);
         return 0.0D;
      } else {
         return IrisInterpolation.getNoise(this.interpolationMethod, var2, var3, this.coordinateScale, (var2x, var4x) -> {
            return this.rawNoise(var4, var2x, var4x);
         });
      }
   }

   private double rawNoise(IrisImage i, double x, double z) {
      var2 /= this.coordinateScale;
      var4 /= this.coordinateScale;
      if (this.isCentered()) {
         var2 += (double)var1.getWidth() / 2.0D;
         var4 += (double)var1.getHeight() / 2.0D;
      }

      if (this.isTiled()) {
         var2 %= (double)var1.getWidth();
         var2 = var2 < 0.0D ? var2 + (double)var1.getWidth() : var2;
         var4 %= (double)var1.getHeight();
         var4 = var4 < 0.0D ? var4 + (double)var1.getHeight() : var4;
      }

      double var6 = var1.getValue(this.getChannel(), (int)var2, (int)var4);
      return this.isInverted() ? 1.0D - var6 : var6;
   }

   @Generated
   public IrisImageMap() {
      this.interpolationMethod = InterpolationMethod.BILINEAR_STARCAST_6;
      this.channel = IrisImageChannel.COMPOSITE_ADD_HSB;
      this.inverted = false;
      this.tiled = false;
      this.centered = true;
      this.imageCache = new AtomicCache();
   }

   @Generated
   public IrisImageMap(final String image, final double coordinateScale, final InterpolationMethod interpolationMethod, final IrisImageChannel channel, final boolean inverted, final boolean tiled, final boolean centered, final AtomicCache<IrisImage> imageCache) {
      this.interpolationMethod = InterpolationMethod.BILINEAR_STARCAST_6;
      this.channel = IrisImageChannel.COMPOSITE_ADD_HSB;
      this.inverted = false;
      this.tiled = false;
      this.centered = true;
      this.imageCache = new AtomicCache();
      this.image = var1;
      this.coordinateScale = var2;
      this.interpolationMethod = var4;
      this.channel = var5;
      this.inverted = var6;
      this.tiled = var7;
      this.centered = var8;
      this.imageCache = var9;
   }

   @Generated
   public String getImage() {
      return this.image;
   }

   @Generated
   public double getCoordinateScale() {
      return this.coordinateScale;
   }

   @Generated
   public InterpolationMethod getInterpolationMethod() {
      return this.interpolationMethod;
   }

   @Generated
   public IrisImageChannel getChannel() {
      return this.channel;
   }

   @Generated
   public boolean isInverted() {
      return this.inverted;
   }

   @Generated
   public boolean isTiled() {
      return this.tiled;
   }

   @Generated
   public boolean isCentered() {
      return this.centered;
   }

   @Generated
   public AtomicCache<IrisImage> getImageCache() {
      return this.imageCache;
   }

   @Generated
   public IrisImageMap setImage(final String image) {
      this.image = var1;
      return this;
   }

   @Generated
   public IrisImageMap setCoordinateScale(final double coordinateScale) {
      this.coordinateScale = var1;
      return this;
   }

   @Generated
   public IrisImageMap setInterpolationMethod(final InterpolationMethod interpolationMethod) {
      this.interpolationMethod = var1;
      return this;
   }

   @Generated
   public IrisImageMap setChannel(final IrisImageChannel channel) {
      this.channel = var1;
      return this;
   }

   @Generated
   public IrisImageMap setInverted(final boolean inverted) {
      this.inverted = var1;
      return this;
   }

   @Generated
   public IrisImageMap setTiled(final boolean tiled) {
      this.tiled = var1;
      return this;
   }

   @Generated
   public IrisImageMap setCentered(final boolean centered) {
      this.centered = var1;
      return this;
   }

   @Generated
   public IrisImageMap setImageCache(final AtomicCache<IrisImage> imageCache) {
      this.imageCache = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisImageMap)) {
         return false;
      } else {
         IrisImageMap var2 = (IrisImageMap)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (Double.compare(this.getCoordinateScale(), var2.getCoordinateScale()) != 0) {
            return false;
         } else if (this.isInverted() != var2.isInverted()) {
            return false;
         } else if (this.isTiled() != var2.isTiled()) {
            return false;
         } else if (this.isCentered() != var2.isCentered()) {
            return false;
         } else {
            label57: {
               String var3 = this.getImage();
               String var4 = var2.getImage();
               if (var3 == null) {
                  if (var4 == null) {
                     break label57;
                  }
               } else if (var3.equals(var4)) {
                  break label57;
               }

               return false;
            }

            InterpolationMethod var5 = this.getInterpolationMethod();
            InterpolationMethod var6 = var2.getInterpolationMethod();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            IrisImageChannel var7 = this.getChannel();
            IrisImageChannel var8 = var2.getChannel();
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
      return var1 instanceof IrisImageMap;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = Double.doubleToLongBits(this.getCoordinateScale());
      int var8 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      var8 = var8 * 59 + (this.isInverted() ? 79 : 97);
      var8 = var8 * 59 + (this.isTiled() ? 79 : 97);
      var8 = var8 * 59 + (this.isCentered() ? 79 : 97);
      String var5 = this.getImage();
      var8 = var8 * 59 + (var5 == null ? 43 : var5.hashCode());
      InterpolationMethod var6 = this.getInterpolationMethod();
      var8 = var8 * 59 + (var6 == null ? 43 : var6.hashCode());
      IrisImageChannel var7 = this.getChannel();
      var8 = var8 * 59 + (var7 == null ? 43 : var7.hashCode());
      return var8;
   }

   @Generated
   public String toString() {
      String var10000 = this.getImage();
      return "IrisImageMap(image=" + var10000 + ", coordinateScale=" + this.getCoordinateScale() + ", interpolationMethod=" + String.valueOf(this.getInterpolationMethod()) + ", channel=" + String.valueOf(this.getChannel()) + ", inverted=" + this.isInverted() + ", tiled=" + this.isTiled() + ", centered=" + this.isCentered() + ", imageCache=" + String.valueOf(this.getImageCache()) + ")";
   }
}
