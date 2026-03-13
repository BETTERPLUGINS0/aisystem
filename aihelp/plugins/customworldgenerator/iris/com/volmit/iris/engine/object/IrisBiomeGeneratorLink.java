package com.volmit.iris.engine.object;

import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.object.annotations.DependsOn;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.data.DataProvider;
import com.volmit.iris.util.interpolation.IrisInterpolation;
import lombok.Generated;

@Snippet("generator-layer")
@Desc("This represents a link to a generator for a biome")
public class IrisBiomeGeneratorLink {
   private final transient AtomicCache<IrisGenerator> gen = new AtomicCache();
   @RegistryListResource(IrisGenerator.class)
   @Desc("The generator id")
   private String generator = "default";
   @DependsOn({"min", "max"})
   @Required
   @MinNumber(-2032.0D)
   @MaxNumber(2032.0D)
   @Desc("The min block value (value + fluidHeight)")
   private int min = 0;
   @DependsOn({"min", "max"})
   @Required
   @MinNumber(-2032.0D)
   @MaxNumber(2032.0D)
   @Desc("The max block value (value + fluidHeight)")
   private int max = 0;

   public IrisGenerator getCachedGenerator(DataProvider g) {
      return (IrisGenerator)this.gen.aquire(() -> {
         IrisGenerator var2 = (IrisGenerator)var1.getData().getGeneratorLoader().load(this.getGenerator());
         if (var2 == null) {
            var2 = new IrisGenerator();
         }

         return var2;
      });
   }

   public double getHeight(DataProvider xg, double x, double z, long seed) {
      double var8 = this.getCachedGenerator(var1).getHeight(var2, var4, var6);
      var8 = var8 < 0.0D ? 0.0D : var8;
      var8 = var8 > 1.0D ? 1.0D : var8;
      return IrisInterpolation.lerp((double)this.min, (double)this.max, var8);
   }

   @Generated
   public IrisBiomeGeneratorLink() {
   }

   @Generated
   public IrisBiomeGeneratorLink(final String generator, final int min, final int max) {
      this.generator = var1;
      this.min = var2;
      this.max = var3;
   }

   @Generated
   public AtomicCache<IrisGenerator> getGen() {
      return this.gen;
   }

   @Generated
   public String getGenerator() {
      return this.generator;
   }

   @Generated
   public int getMin() {
      return this.min;
   }

   @Generated
   public int getMax() {
      return this.max;
   }

   @Generated
   public IrisBiomeGeneratorLink setGenerator(final String generator) {
      this.generator = var1;
      return this;
   }

   @Generated
   public IrisBiomeGeneratorLink setMin(final int min) {
      this.min = var1;
      return this;
   }

   @Generated
   public IrisBiomeGeneratorLink setMax(final int max) {
      this.max = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisBiomeGeneratorLink)) {
         return false;
      } else {
         IrisBiomeGeneratorLink var2 = (IrisBiomeGeneratorLink)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getMin() != var2.getMin()) {
            return false;
         } else if (this.getMax() != var2.getMax()) {
            return false;
         } else {
            String var3 = this.getGenerator();
            String var4 = var2.getGenerator();
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
      return var1 instanceof IrisBiomeGeneratorLink;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var4 = var2 * 59 + this.getMin();
      var4 = var4 * 59 + this.getMax();
      String var3 = this.getGenerator();
      var4 = var4 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getGen());
      return "IrisBiomeGeneratorLink(gen=" + var10000 + ", generator=" + this.getGenerator() + ", min=" + this.getMin() + ", max=" + this.getMax() + ")";
   }
}
