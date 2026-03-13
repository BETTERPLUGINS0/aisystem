package com.volmit.iris.engine.object.matter;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.IrisEngine;
import com.volmit.iris.engine.object.IRare;
import com.volmit.iris.engine.object.IrisStyledRange;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.mantle.Mantle;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.matter.MatterSlice;
import java.util.Iterator;
import lombok.Generated;

@Snippet("matter-placer")
@Desc("Represents an iris object placer. It places matter objects.")
public class IrisMatterPlacement implements IRare {
   @RegistryListResource(IrisMatterObject.class)
   @Required
   @ArrayType(
      min = 1,
      type = String.class
   )
   @Desc("List of objects to place")
   private KList<String> place = new KList();
   @MinNumber(0.0D)
   @Desc("The rarity of this object placing")
   private int rarity = 0;
   @MinNumber(0.0D)
   @Desc("The styled density of this object")
   private IrisStyledRange densityRange;
   @Desc("The absolute density for this object")
   private double density = 1.0D;
   @Desc("Translate this matter object before placement")
   private IrisMatterTranslate translate;
   @Desc("Place this object on the surface height, bedrock or the sky, then use translate if need be.")
   private IrisMatterPlacementLocation location;

   public void place(IrisEngine engine, IrisData data, RNG rng, int ax, int az) {
      IrisMatterObject var6 = (IrisMatterObject)var2.getMatterLoader().load((String)this.place.getRandom(var3));
      int var7 = var4;
      int var8 = var5;
      int var9 = 0;
      if (this.translate != null) {
         var7 = var4 + this.translate.xOffset(var2, var3, var4, var5);
         var9 += this.translate.yOffset(var2, var3, var7, var5);
         var8 = var5 + this.translate.zOffset(var2, var3, var7, var5);
      }

      int var10 = var9 + this.location.at(var1, var7, var8);
      Mantle var11 = var1.getMantle().getMantle();
      int var12 = var7;
      int var13 = var10;
      int var14 = var8;
      Iterator var15 = var6.getMatter().getSliceMap().values().iterator();

      while(var15.hasNext()) {
         MatterSlice var16 = (MatterSlice)var15.next();
         var16.iterate((var4x, var5x, var6x, var7x) -> {
            var11.set(var12 + var4x, var13 + var5x, var14 + var6x, var7x);
         });
      }

   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisMatterPlacement)) {
         return false;
      } else {
         IrisMatterPlacement var2 = (IrisMatterPlacement)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getRarity() != var2.getRarity()) {
            return false;
         } else if (Double.compare(this.getDensity(), var2.getDensity()) != 0) {
            return false;
         } else {
            label64: {
               KList var3 = this.getPlace();
               KList var4 = var2.getPlace();
               if (var3 == null) {
                  if (var4 == null) {
                     break label64;
                  }
               } else if (var3.equals(var4)) {
                  break label64;
               }

               return false;
            }

            label57: {
               IrisStyledRange var5 = this.getDensityRange();
               IrisStyledRange var6 = var2.getDensityRange();
               if (var5 == null) {
                  if (var6 == null) {
                     break label57;
                  }
               } else if (var5.equals(var6)) {
                  break label57;
               }

               return false;
            }

            IrisMatterTranslate var7 = this.getTranslate();
            IrisMatterTranslate var8 = var2.getTranslate();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            IrisMatterPlacementLocation var9 = this.getLocation();
            IrisMatterPlacementLocation var10 = var2.getLocation();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisMatterPlacement;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var9 = var2 * 59 + this.getRarity();
      long var3 = Double.doubleToLongBits(this.getDensity());
      var9 = var9 * 59 + (int)(var3 >>> 32 ^ var3);
      KList var5 = this.getPlace();
      var9 = var9 * 59 + (var5 == null ? 43 : var5.hashCode());
      IrisStyledRange var6 = this.getDensityRange();
      var9 = var9 * 59 + (var6 == null ? 43 : var6.hashCode());
      IrisMatterTranslate var7 = this.getTranslate();
      var9 = var9 * 59 + (var7 == null ? 43 : var7.hashCode());
      IrisMatterPlacementLocation var8 = this.getLocation();
      var9 = var9 * 59 + (var8 == null ? 43 : var8.hashCode());
      return var9;
   }

   @Generated
   public IrisMatterPlacement() {
      this.location = IrisMatterPlacementLocation.SURFACE;
   }

   @Generated
   public KList<String> getPlace() {
      return this.place;
   }

   @Generated
   public int getRarity() {
      return this.rarity;
   }

   @Generated
   public IrisStyledRange getDensityRange() {
      return this.densityRange;
   }

   @Generated
   public double getDensity() {
      return this.density;
   }

   @Generated
   public IrisMatterTranslate getTranslate() {
      return this.translate;
   }

   @Generated
   public IrisMatterPlacementLocation getLocation() {
      return this.location;
   }

   @Generated
   public IrisMatterPlacement setPlace(final KList<String> place) {
      this.place = var1;
      return this;
   }

   @Generated
   public IrisMatterPlacement setRarity(final int rarity) {
      this.rarity = var1;
      return this;
   }

   @Generated
   public IrisMatterPlacement setDensityRange(final IrisStyledRange densityRange) {
      this.densityRange = var1;
      return this;
   }

   @Generated
   public IrisMatterPlacement setDensity(final double density) {
      this.density = var1;
      return this;
   }

   @Generated
   public IrisMatterPlacement setTranslate(final IrisMatterTranslate translate) {
      this.translate = var1;
      return this;
   }

   @Generated
   public IrisMatterPlacement setLocation(final IrisMatterPlacementLocation location) {
      this.location = var1;
      return this;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getPlace());
      return "IrisMatterPlacement(place=" + var10000 + ", rarity=" + this.getRarity() + ", densityRange=" + String.valueOf(this.getDensityRange()) + ", density=" + this.getDensity() + ", translate=" + String.valueOf(this.getTranslate()) + ", location=" + String.valueOf(this.getLocation()) + ")";
   }
}
