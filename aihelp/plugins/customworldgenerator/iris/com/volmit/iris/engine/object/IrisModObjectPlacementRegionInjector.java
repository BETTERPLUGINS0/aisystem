package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KList;
import lombok.Generated;

@Snippet("object-placement-region-injector")
@Desc("An object placement injector")
public class IrisModObjectPlacementRegionInjector {
   @Required
   @Desc("The biome to find")
   @RegistryListResource(IrisRegion.class)
   private String biome = "";
   @Required
   @Desc("A biome to inject into the region")
   @ArrayType(
      type = IrisObjectPlacement.class,
      min = 1
   )
   private KList<IrisObjectPlacement> place = new KList();

   @Generated
   public IrisModObjectPlacementRegionInjector() {
   }

   @Generated
   public IrisModObjectPlacementRegionInjector(final String biome, final KList<IrisObjectPlacement> place) {
      this.biome = var1;
      this.place = var2;
   }

   @Generated
   public String getBiome() {
      return this.biome;
   }

   @Generated
   public KList<IrisObjectPlacement> getPlace() {
      return this.place;
   }

   @Generated
   public IrisModObjectPlacementRegionInjector setBiome(final String biome) {
      this.biome = var1;
      return this;
   }

   @Generated
   public IrisModObjectPlacementRegionInjector setPlace(final KList<IrisObjectPlacement> place) {
      this.place = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisModObjectPlacementRegionInjector)) {
         return false;
      } else {
         IrisModObjectPlacementRegionInjector var2 = (IrisModObjectPlacementRegionInjector)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            String var3 = this.getBiome();
            String var4 = var2.getBiome();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            KList var5 = this.getPlace();
            KList var6 = var2.getPlace();
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
      return var1 instanceof IrisModObjectPlacementRegionInjector;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      String var3 = this.getBiome();
      int var5 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      KList var4 = this.getPlace();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public String toString() {
      String var10000 = this.getBiome();
      return "IrisModObjectPlacementRegionInjector(biome=" + var10000 + ", place=" + String.valueOf(this.getPlace()) + ")";
   }
}
