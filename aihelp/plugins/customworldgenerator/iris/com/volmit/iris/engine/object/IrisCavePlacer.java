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

@Snippet("cave-placer")
@Desc("Translate objects")
public class IrisCavePlacer implements IRare {
   private final transient AtomicCache<IrisCave> caveCache = new AtomicCache();
   private final transient AtomicBoolean fail = new AtomicBoolean(false);
   @Required
   @Desc("Typically a 1 in RARITY on a per chunk/fork basis")
   @MinNumber(1.0D)
   private int rarity = 15;
   @MinNumber(1.0D)
   @Required
   @Desc("The cave to place")
   @RegistryListResource(IrisCave.class)
   private String cave;
   @MinNumber(1.0D)
   @MaxNumber(256.0D)
   @Desc("The maximum recursion depth")
   private int maxRecursion = 16;
   @Desc("If set to true, this cave is allowed to break the surface")
   private boolean breakSurface = true;
   @Desc("The height range this cave can spawn at. If breakSurface is false, the output of this range will be clamped by the current world height to prevent surface breaking.")
   private IrisStyledRange caveStartHeight;

   public IrisCave getRealCave(IrisData data) {
      return (IrisCave)this.caveCache.aquire(() -> {
         return (IrisCave)var1.getCaveLoader().load(this.getCave());
      });
   }

   public void generateCave(MantleWriter mantle, RNG rng, Engine engine, int x, int y, int z) {
      this.generateCave(var1, var2, new RNG(var3.getSeedManager().getCarve()), var3, var4, var5, var6, 0, -1);
   }

   public void generateCave(MantleWriter mantle, RNG rng, RNG base, Engine engine, int x, int y, int z, int recursion, int waterHint) {
      if (!this.fail.get()) {
         if (var2.nextInt(this.rarity) == 0) {
            IrisData var10 = var4.getData();
            IrisCave var11 = this.getRealCave(var10);
            if (var11 == null) {
               Iris.warn("Unable to locate cave for generation!");
               this.fail.set(true);
            } else {
               if (var6 == -1) {
                  int var12 = (int)this.caveStartHeight.get(var3, (double)var5, (double)var7, var10);
                  int var13 = this.breakSurface ? var12 : (int)((Double)var4.getComplex().getHeightStream().get((double)var5, (double)var7) - 9.0D);
                  var6 = Math.min(var12, var13);
               }

               try {
                  var11.generate(var1, var2, var3, var4, var5 + var2.nextInt(15), var6, var7 + var2.nextInt(15), var8, var9, this.breakSurface);
               } catch (Throwable var14) {
                  var14.printStackTrace();
                  this.fail.set(true);
               }

            }
         }
      }
   }

   public int getSize(IrisData data, int depth) {
      IrisCave var3 = this.getRealCave(var1);
      return var3 != null ? var3.getMaxSize(var1, var2) : 32;
   }

   @Generated
   public IrisCavePlacer() {
      this.caveStartHeight = new IrisStyledRange(13.0D, 120.0D, new IrisGeneratorStyle(NoiseStyle.STATIC));
   }

   @Generated
   public IrisCavePlacer(final int rarity, final String cave, final int maxRecursion, final boolean breakSurface, final IrisStyledRange caveStartHeight) {
      this.caveStartHeight = new IrisStyledRange(13.0D, 120.0D, new IrisGeneratorStyle(NoiseStyle.STATIC));
      this.rarity = var1;
      this.cave = var2;
      this.maxRecursion = var3;
      this.breakSurface = var4;
      this.caveStartHeight = var5;
   }

   @Generated
   public AtomicCache<IrisCave> getCaveCache() {
      return this.caveCache;
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
   public String getCave() {
      return this.cave;
   }

   @Generated
   public int getMaxRecursion() {
      return this.maxRecursion;
   }

   @Generated
   public boolean isBreakSurface() {
      return this.breakSurface;
   }

   @Generated
   public IrisStyledRange getCaveStartHeight() {
      return this.caveStartHeight;
   }

   @Generated
   public IrisCavePlacer setRarity(final int rarity) {
      this.rarity = var1;
      return this;
   }

   @Generated
   public IrisCavePlacer setCave(final String cave) {
      this.cave = var1;
      return this;
   }

   @Generated
   public IrisCavePlacer setMaxRecursion(final int maxRecursion) {
      this.maxRecursion = var1;
      return this;
   }

   @Generated
   public IrisCavePlacer setBreakSurface(final boolean breakSurface) {
      this.breakSurface = var1;
      return this;
   }

   @Generated
   public IrisCavePlacer setCaveStartHeight(final IrisStyledRange caveStartHeight) {
      this.caveStartHeight = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisCavePlacer)) {
         return false;
      } else {
         IrisCavePlacer var2 = (IrisCavePlacer)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getRarity() != var2.getRarity()) {
            return false;
         } else if (this.getMaxRecursion() != var2.getMaxRecursion()) {
            return false;
         } else if (this.isBreakSurface() != var2.isBreakSurface()) {
            return false;
         } else {
            String var3 = this.getCave();
            String var4 = var2.getCave();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            IrisStyledRange var5 = this.getCaveStartHeight();
            IrisStyledRange var6 = var2.getCaveStartHeight();
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
      return var1 instanceof IrisCavePlacer;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var5 = var2 * 59 + this.getRarity();
      var5 = var5 * 59 + this.getMaxRecursion();
      var5 = var5 * 59 + (this.isBreakSurface() ? 79 : 97);
      String var3 = this.getCave();
      var5 = var5 * 59 + (var3 == null ? 43 : var3.hashCode());
      IrisStyledRange var4 = this.getCaveStartHeight();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getCaveCache());
      return "IrisCavePlacer(caveCache=" + var10000 + ", fail=" + String.valueOf(this.getFail()) + ", rarity=" + this.getRarity() + ", cave=" + this.getCave() + ", maxRecursion=" + this.getMaxRecursion() + ", breakSurface=" + this.isBreakSurface() + ", caveStartHeight=" + String.valueOf(this.getCaveStartHeight()) + ")";
   }
}
