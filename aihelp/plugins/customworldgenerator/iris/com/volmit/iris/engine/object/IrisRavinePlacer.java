package com.volmit.iris.engine.object;

import com.volmit.iris.Iris;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.mantle.MantleWriter;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.math.RNG;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Generated;

@Snippet("ravine-placer")
@Desc("Translate objects")
public class IrisRavinePlacer implements IRare {
   private final transient AtomicCache<IrisRavine> ravineCache = new AtomicCache();
   private final transient AtomicBoolean fail = new AtomicBoolean(false);
   @Required
   @Desc("Typically a 1 in RARITY on a per chunk/fork basis")
   @MinNumber(1.0D)
   private int rarity = 15;
   @MinNumber(1.0D)
   @Required
   @Desc("The ravine to place")
   @RegistryListResource(IrisRavine.class)
   private String ravine;
   @MinNumber(1.0D)
   @MaxNumber(256.0D)
   @Desc("The maximum recursion depth")
   private int maxRecursion = 100;

   public IrisRavine getRealRavine(IrisData data) {
      return (IrisRavine)this.ravineCache.aquire(() -> {
         return (IrisRavine)var1.getRavineLoader().load(this.getRavine());
      });
   }

   public void generateRavine(MantleWriter mantle, RNG rng, Engine engine, int x, int y, int z) {
      this.generateRavine(var1, var2, new RNG(var3.getSeedManager().getCarve()), var3, var4, var5, var6, 0, -1);
   }

   public void generateRavine(MantleWriter mantle, RNG rng, RNG base, Engine engine, int x, int y, int z, int recursion, int waterHint) {
      if (!this.fail.get()) {
         if (var2.nextInt(this.rarity) == 0) {
            IrisData var10 = var4.getData();
            IrisRavine var11 = this.getRealRavine(var10);
            if (var11 == null) {
               Iris.warn("Unable to locate ravine for generation!");
               this.fail.set(true);
            } else {
               try {
                  int var12 = var5 + var2.nextInt(15);
                  int var13 = var7 + var2.nextInt(15);
                  var11.generate(var1, var2, var3, var4, var12, var6, var13, var8, var9);
               } catch (Throwable var14) {
                  var14.printStackTrace();
                  this.fail.set(true);
               }

            }
         }
      }
   }

   public int getSize(IrisData data, int depth) {
      return this.getRealRavine(var1).getMaxSize(var1, var2);
   }

   @Generated
   public IrisRavinePlacer() {
   }

   @Generated
   public IrisRavinePlacer(final int rarity, final String ravine, final int maxRecursion) {
      this.rarity = var1;
      this.ravine = var2;
      this.maxRecursion = var3;
   }

   @Generated
   public AtomicCache<IrisRavine> getRavineCache() {
      return this.ravineCache;
   }

   @Generated
   public AtomicBoolean getFail() {
      return this.fail;
   }

   @Generated
   public int getRarity() {
      return this.rarity;
   }

   @Generated
   public String getRavine() {
      return this.ravine;
   }

   @Generated
   public int getMaxRecursion() {
      return this.maxRecursion;
   }

   @Generated
   public IrisRavinePlacer setRarity(final int rarity) {
      this.rarity = var1;
      return this;
   }

   @Generated
   public IrisRavinePlacer setRavine(final String ravine) {
      this.ravine = var1;
      return this;
   }

   @Generated
   public IrisRavinePlacer setMaxRecursion(final int maxRecursion) {
      this.maxRecursion = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisRavinePlacer)) {
         return false;
      } else {
         IrisRavinePlacer var2 = (IrisRavinePlacer)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getRarity() != var2.getRarity()) {
            return false;
         } else if (this.getMaxRecursion() != var2.getMaxRecursion()) {
            return false;
         } else {
            String var3 = this.getRavine();
            String var4 = var2.getRavine();
            if (var3 == null) {
               if (var4 == null) {
                  return true;
               }
            } else if (var3.equals(var4)) {
               return true;
            }

            return false;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisRavinePlacer;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var4 = var2 * 59 + this.getRarity();
      var4 = var4 * 59 + this.getMaxRecursion();
      String var3 = this.getRavine();
      var4 = var4 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getRavineCache());
      return "IrisRavinePlacer(ravineCache=" + var10000 + ", fail=" + String.valueOf(this.getFail()) + ", rarity=" + this.getRarity() + ", ravine=" + this.getRavine() + ", maxRecursion=" + this.getMaxRecursion() + ")";
   }
}
