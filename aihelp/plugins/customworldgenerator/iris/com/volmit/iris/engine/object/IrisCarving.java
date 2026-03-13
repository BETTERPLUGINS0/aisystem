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

@Snippet("carving")
@Desc("Represents a carving configuration")
public class IrisCarving {
   @ArrayType(
      type = IrisCavePlacer.class,
      min = 1
   )
   @Desc("Define cave placers")
   private KList<IrisCavePlacer> caves = new KList();
   @ArrayType(
      type = IrisRavinePlacer.class,
      min = 1
   )
   @Desc("Define ravine placers")
   private KList<IrisRavinePlacer> ravines = new KList();
   @ArrayType(
      type = IrisElipsoid.class,
      min = 1
   )
   @Desc("Define elipsoids")
   private KList<IrisElipsoid> elipsoids = new KList();
   @ArrayType(
      type = IrisSphere.class,
      min = 1
   )
   @Desc("Define spheres")
   private KList<IrisSphere> spheres = new KList();
   @ArrayType(
      type = IrisPyramid.class,
      min = 1
   )
   @Desc("Define pyramids")
   private KList<IrisPyramid> pyramids = new KList();

   @BlockCoordinates
   public void doCarving(MantleWriter writer, RNG rng, Engine engine, int x, int y, int z, int depth) {
      this.doCarving(var1, var2, new RNG(var3.getSeedManager().getCarve()), var3, var4, var5, var6, var7, -1);
   }

   @BlockCoordinates
   public void doCarving(MantleWriter writer, RNG rng, RNG base, Engine engine, int x, int y, int z, int recursion, int waterHint) {
      int var10 = var8 + 1;
      Iterator var11;
      if (this.caves.isNotEmpty()) {
         var11 = this.caves.iterator();

         while(var11.hasNext()) {
            IrisCavePlacer var12 = (IrisCavePlacer)var11.next();
            if (var8 <= var12.getMaxRecursion()) {
               var12.generateCave(var1, var2, var3, var4, var5, var6, var7, var10, var9);
            }
         }
      }

      if (this.ravines.isNotEmpty()) {
         var11 = this.ravines.iterator();

         while(var11.hasNext()) {
            IrisRavinePlacer var13 = (IrisRavinePlacer)var11.next();
            if (var8 <= var13.getMaxRecursion()) {
               var13.generateRavine(var1, var2, var3, var4, var5, var6, var7, var10, var9);
            }
         }
      }

      if (this.spheres.isNotEmpty()) {
         var11 = this.spheres.iterator();

         while(var11.hasNext()) {
            IrisSphere var14 = (IrisSphere)var11.next();
            if (var2.nextInt(var14.getRarity()) == 0) {
               var14.generate(var3, var4, var1, var5, var6, var7);
            }
         }
      }

      if (this.elipsoids.isNotEmpty()) {
         var11 = this.elipsoids.iterator();

         while(var11.hasNext()) {
            IrisElipsoid var15 = (IrisElipsoid)var11.next();
            if (var2.nextInt(var15.getRarity()) == 0) {
               var15.generate(var3, var4, var1, var5, var6, var7);
            }
         }
      }

      if (this.pyramids.isNotEmpty()) {
         var11 = this.pyramids.iterator();

         while(var11.hasNext()) {
            IrisPyramid var16 = (IrisPyramid)var11.next();
            if (var2.nextInt(var16.getRarity()) == 0) {
               var16.generate(var3, var4, var1, var5, var6, var7);
            }
         }
      }

   }

   public int getMaxRange(IrisData data, int recursion) {
      int var3 = 0;
      int var4 = var2 + 1;
      Iterator var5 = this.caves.iterator();

      while(var5.hasNext()) {
         IrisCavePlacer var6 = (IrisCavePlacer)var5.next();
         if (var2 <= var6.getMaxRecursion()) {
            var3 = Math.max(var3, var6.getSize(var1, var4));
         }
      }

      var5 = this.ravines.iterator();

      while(var5.hasNext()) {
         IrisRavinePlacer var7 = (IrisRavinePlacer)var5.next();
         if (var2 <= var7.getMaxRecursion()) {
            var3 = Math.max(var3, var7.getSize(var1, var4));
         }
      }

      if (this.elipsoids.isNotEmpty()) {
         var3 = (int)Math.max(this.elipsoids.stream().mapToDouble(IrisElipsoid::maxSize).max().getAsDouble(), (double)var3);
      }

      if (this.spheres.isNotEmpty()) {
         var3 = (int)Math.max(this.spheres.stream().mapToDouble(IrisSphere::maxSize).max().getAsDouble(), (double)var3);
      }

      if (this.pyramids.isNotEmpty()) {
         var3 = (int)Math.max(this.pyramids.stream().mapToDouble(IrisPyramid::maxSize).max().getAsDouble(), (double)var3);
      }

      return var3;
   }

   @Generated
   public IrisCarving() {
   }

   @Generated
   public IrisCarving(final KList<IrisCavePlacer> caves, final KList<IrisRavinePlacer> ravines, final KList<IrisElipsoid> elipsoids, final KList<IrisSphere> spheres, final KList<IrisPyramid> pyramids) {
      this.caves = var1;
      this.ravines = var2;
      this.elipsoids = var3;
      this.spheres = var4;
      this.pyramids = var5;
   }

   @Generated
   public KList<IrisCavePlacer> getCaves() {
      return this.caves;
   }

   @Generated
   public KList<IrisRavinePlacer> getRavines() {
      return this.ravines;
   }

   @Generated
   public KList<IrisElipsoid> getElipsoids() {
      return this.elipsoids;
   }

   @Generated
   public KList<IrisSphere> getSpheres() {
      return this.spheres;
   }

   @Generated
   public KList<IrisPyramid> getPyramids() {
      return this.pyramids;
   }

   @Generated
   public IrisCarving setCaves(final KList<IrisCavePlacer> caves) {
      this.caves = var1;
      return this;
   }

   @Generated
   public IrisCarving setRavines(final KList<IrisRavinePlacer> ravines) {
      this.ravines = var1;
      return this;
   }

   @Generated
   public IrisCarving setElipsoids(final KList<IrisElipsoid> elipsoids) {
      this.elipsoids = var1;
      return this;
   }

   @Generated
   public IrisCarving setSpheres(final KList<IrisSphere> spheres) {
      this.spheres = var1;
      return this;
   }

   @Generated
   public IrisCarving setPyramids(final KList<IrisPyramid> pyramids) {
      this.pyramids = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisCarving)) {
         return false;
      } else {
         IrisCarving var2 = (IrisCarving)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            label71: {
               KList var3 = this.getCaves();
               KList var4 = var2.getCaves();
               if (var3 == null) {
                  if (var4 == null) {
                     break label71;
                  }
               } else if (var3.equals(var4)) {
                  break label71;
               }

               return false;
            }

            KList var5 = this.getRavines();
            KList var6 = var2.getRavines();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            label57: {
               KList var7 = this.getElipsoids();
               KList var8 = var2.getElipsoids();
               if (var7 == null) {
                  if (var8 == null) {
                     break label57;
                  }
               } else if (var7.equals(var8)) {
                  break label57;
               }

               return false;
            }

            KList var9 = this.getSpheres();
            KList var10 = var2.getSpheres();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            KList var11 = this.getPyramids();
            KList var12 = var2.getPyramids();
            if (var11 == null) {
               if (var12 == null) {
                  return true;
               }
            } else if (var11.equals(var12)) {
               return true;
            }

            return false;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisCarving;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      KList var3 = this.getCaves();
      int var8 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      KList var4 = this.getRavines();
      var8 = var8 * 59 + (var4 == null ? 43 : var4.hashCode());
      KList var5 = this.getElipsoids();
      var8 = var8 * 59 + (var5 == null ? 43 : var5.hashCode());
      KList var6 = this.getSpheres();
      var8 = var8 * 59 + (var6 == null ? 43 : var6.hashCode());
      KList var7 = this.getPyramids();
      var8 = var8 * 59 + (var7 == null ? 43 : var7.hashCode());
      return var8;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getCaves());
      return "IrisCarving(caves=" + var10000 + ", ravines=" + String.valueOf(this.getRavines()) + ", elipsoids=" + String.valueOf(this.getElipsoids()) + ", spheres=" + String.valueOf(this.getSpheres()) + ", pyramids=" + String.valueOf(this.getPyramids()) + ")";
   }
}
