package com.volmit.iris.engine.object;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.mantle.MantleWriter;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.documentation.BlockCoordinates;
import com.volmit.iris.util.math.RNG;
import java.util.Iterator;
import lombok.Generated;

@Snippet("fluid-bodies")
@Desc("Represents a fluid body configuration")
public class IrisFluidBodies {
   @ArrayType(
      type = IrisRiver.class,
      min = 1
   )
   @Desc("Define rivers")
   private KList<IrisRiver> rivers = new KList();
   @ArrayType(
      type = IrisLake.class,
      min = 1
   )
   @Desc("Define lakes")
   private KList<IrisLake> lakes = new KList();

   @BlockCoordinates
   public void generate(MantleWriter writer, RNG rng, Engine engine, int x, int y, int z) {
      Iterator var7;
      if (this.rivers.isNotEmpty()) {
         var7 = this.rivers.iterator();

         while(var7.hasNext()) {
            IrisRiver var8 = (IrisRiver)var7.next();
            var8.generate(var1, var2, var3, var4, var5, var6);
         }
      }

      if (this.lakes.isNotEmpty()) {
         var7 = this.lakes.iterator();

         while(var7.hasNext()) {
            IrisLake var9 = (IrisLake)var7.next();
            var9.generate(var1, var2, var3, var4, var5, var6);
         }
      }

   }

   public int getMaxRange(IrisData data) {
      int var2 = 0;

      Iterator var3;
      IrisRiver var4;
      for(var3 = this.rivers.iterator(); var3.hasNext(); var2 = Math.max(var2, var4.getSize(var1))) {
         var4 = (IrisRiver)var3.next();
      }

      IrisLake var5;
      for(var3 = this.lakes.iterator(); var3.hasNext(); var2 = Math.max(var2, var5.getSize(var1))) {
         var5 = (IrisLake)var3.next();
      }

      return var2;
   }

   @Generated
   public IrisFluidBodies() {
   }

   @Generated
   public IrisFluidBodies(final KList<IrisRiver> rivers, final KList<IrisLake> lakes) {
      this.rivers = var1;
      this.lakes = var2;
   }

   @Generated
   public KList<IrisRiver> getRivers() {
      return this.rivers;
   }

   @Generated
   public KList<IrisLake> getLakes() {
      return this.lakes;
   }

   @Generated
   public IrisFluidBodies setRivers(final KList<IrisRiver> rivers) {
      this.rivers = var1;
      return this;
   }

   @Generated
   public IrisFluidBodies setLakes(final KList<IrisLake> lakes) {
      this.lakes = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisFluidBodies)) {
         return false;
      } else {
         IrisFluidBodies var2 = (IrisFluidBodies)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            KList var3 = this.getRivers();
            KList var4 = var2.getRivers();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            KList var5 = this.getLakes();
            KList var6 = var2.getLakes();
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
      return var1 instanceof IrisFluidBodies;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      KList var3 = this.getRivers();
      int var5 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      KList var4 = this.getLakes();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getRivers());
      return "IrisFluidBodies(rivers=" + var10000 + ", lakes=" + String.valueOf(this.getLakes()) + ")";
   }
}
