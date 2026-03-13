package com.volmit.iris.engine.object;

import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.mantle.MantleWriter;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.matter.MatterCavern;
import com.volmit.iris.util.matter.slices.CavernMatter;
import lombok.Generated;

@Snippet("carving-sphere")
@Desc("Represents an procedural eliptical shape")
public class IrisSphere implements IRare {
   private final transient AtomicCache<MatterCavern> matterNodeCache = new AtomicCache();
   @Required
   @Desc("Typically a 1 in RARITY on a per fork basis")
   @MinNumber(1.0D)
   private int rarity = 1;
   @RegistryListResource(IrisBiome.class)
   @Desc("Force this cave to only generate the specified custom biome")
   private String customBiome = "";
   @Desc("The styled random radius for x")
   private IrisStyledRange radius;

   public void generate(RNG rng, Engine engine, MantleWriter writer, int x, int y, int z) {
      var3.setSphere(var4, var5, var6, this.radius.get(var1, (double)var6, (double)var5, var2.getData()), true, (MatterCavern)this.matterNodeCache.aquire(() -> {
         return CavernMatter.get(this.getCustomBiome(), 0);
      }));
   }

   public double maxSize() {
      return this.radius.getMax();
   }

   @Generated
   public IrisSphere() {
      this.radius = new IrisStyledRange(1.0D, 5.0D, new IrisGeneratorStyle(NoiseStyle.STATIC));
   }

   @Generated
   public AtomicCache<MatterCavern> getMatterNodeCache() {
      return this.matterNodeCache;
   }

   @Generated
   public int getRarity() {
      return this.rarity;
   }

   @Generated
   public String getCustomBiome() {
      return this.customBiome;
   }

   @Generated
   public IrisStyledRange getRadius() {
      return this.radius;
   }

   @Generated
   public void setRarity(final int rarity) {
      this.rarity = var1;
   }

   @Generated
   public void setCustomBiome(final String customBiome) {
      this.customBiome = var1;
   }

   @Generated
   public void setRadius(final IrisStyledRange radius) {
      this.radius = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisSphere)) {
         return false;
      } else {
         IrisSphere var2 = (IrisSphere)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getRarity() != var2.getRarity()) {
            return false;
         } else {
            String var3 = this.getCustomBiome();
            String var4 = var2.getCustomBiome();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            IrisStyledRange var5 = this.getRadius();
            IrisStyledRange var6 = var2.getRadius();
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
      return var1 instanceof IrisSphere;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var5 = var2 * 59 + this.getRarity();
      String var3 = this.getCustomBiome();
      var5 = var5 * 59 + (var3 == null ? 43 : var3.hashCode());
      IrisStyledRange var4 = this.getRadius();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getMatterNodeCache());
      return "IrisSphere(matterNodeCache=" + var10000 + ", rarity=" + this.getRarity() + ", customBiome=" + this.getCustomBiome() + ", radius=" + String.valueOf(this.getRadius()) + ")";
   }
}
