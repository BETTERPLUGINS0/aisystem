package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import lombok.Generated;

@Snippet("jigsaw-structure-min-distance")
@Desc("Represents the min distance between jigsaw structure placements")
public class IrisJigsawMinDistance {
   @Required
   @RegistryListResource(IrisJigsawStructure.class)
   @Desc("The structure to check against")
   private String structure;
   @Required
   @MinNumber(0.0D)
   @Desc("The min distance in blocks to a placed structure\nWARNING: The performance impact scales exponentially!")
   private int distance;

   @Generated
   public IrisJigsawMinDistance() {
   }

   @Generated
   public IrisJigsawMinDistance(final String structure, final int distance) {
      this.structure = var1;
      this.distance = var2;
   }

   @Generated
   public String getStructure() {
      return this.structure;
   }

   @Generated
   public int getDistance() {
      return this.distance;
   }

   @Generated
   public IrisJigsawMinDistance setStructure(final String structure) {
      this.structure = var1;
      return this;
   }

   @Generated
   public IrisJigsawMinDistance setDistance(final int distance) {
      this.distance = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisJigsawMinDistance)) {
         return false;
      } else {
         IrisJigsawMinDistance var2 = (IrisJigsawMinDistance)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getDistance() != var2.getDistance()) {
            return false;
         } else {
            String var3 = this.getStructure();
            String var4 = var2.getStructure();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisJigsawMinDistance;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var4 = var2 * 59 + this.getDistance();
      String var3 = this.getStructure();
      var4 = var4 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }

   @Generated
   public String toString() {
      String var10000 = this.getStructure();
      return "IrisJigsawMinDistance(structure=" + var10000 + ", distance=" + this.getDistance() + ")";
   }
}
