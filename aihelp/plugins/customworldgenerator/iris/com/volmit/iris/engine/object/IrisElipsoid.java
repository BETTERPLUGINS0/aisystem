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

@Snippet("carving-elipsoid")
@Desc("Represents an procedural eliptical shape")
public class IrisElipsoid implements IRare {
   private final transient AtomicCache<MatterCavern> matterNodeCache = new AtomicCache();
   @Required
   @Desc("Typically a 1 in RARITY on a per fork basis")
   @MinNumber(1.0D)
   private int rarity = 1;
   @RegistryListResource(IrisBiome.class)
   @Desc("Force this cave to only generate the specified custom biome")
   private String customBiome = "";
   @Desc("The styled random radius for x")
   private IrisStyledRange xRadius;
   @Desc("The styled random radius for y")
   private IrisStyledRange yRadius;
   @Desc("The styled random radius for z")
   private IrisStyledRange zRadius;

   public void generate(RNG rng, Engine engine, MantleWriter writer, int x, int y, int z) {
      var3.setElipsoid(var4, var5, var6, this.xRadius.get(var1, (double)var6, (double)var5, var2.getData()), this.yRadius.get(var1, (double)var4, (double)var6, var2.getData()), this.zRadius.get(var1, (double)var5, (double)var4, var2.getData()), true, (MatterCavern)this.matterNodeCache.aquire(() -> {
         return CavernMatter.get(this.getCustomBiome(), 0);
      }));
   }

   public double maxSize() {
      return Math.max(this.xRadius.getMax(), Math.max(this.yRadius.getMax(), this.zRadius.getMax()));
   }

   @Generated
   public IrisElipsoid() {
      this.xRadius = new IrisStyledRange(1.0D, 5.0D, new IrisGeneratorStyle(NoiseStyle.STATIC));
      this.yRadius = new IrisStyledRange(1.0D, 5.0D, new IrisGeneratorStyle(NoiseStyle.STATIC));
      this.zRadius = new IrisStyledRange(1.0D, 5.0D, new IrisGeneratorStyle(NoiseStyle.STATIC));
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
   public IrisStyledRange getXRadius() {
      return this.xRadius;
   }

   @Generated
   public IrisStyledRange getYRadius() {
      return this.yRadius;
   }

   @Generated
   public IrisStyledRange getZRadius() {
      return this.zRadius;
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
   public void setXRadius(final IrisStyledRange xRadius) {
      this.xRadius = var1;
   }

   @Generated
   public void setYRadius(final IrisStyledRange yRadius) {
      this.yRadius = var1;
   }

   @Generated
   public void setZRadius(final IrisStyledRange zRadius) {
      this.zRadius = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisElipsoid)) {
         return false;
      } else {
         IrisElipsoid var2 = (IrisElipsoid)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getRarity() != var2.getRarity()) {
            return false;
         } else {
            label61: {
               String var3 = this.getCustomBiome();
               String var4 = var2.getCustomBiome();
               if (var3 == null) {
                  if (var4 == null) {
                     break label61;
                  }
               } else if (var3.equals(var4)) {
                  break label61;
               }

               return false;
            }

            label54: {
               IrisStyledRange var5 = this.getXRadius();
               IrisStyledRange var6 = var2.getXRadius();
               if (var5 == null) {
                  if (var6 == null) {
                     break label54;
                  }
               } else if (var5.equals(var6)) {
                  break label54;
               }

               return false;
            }

            IrisStyledRange var7 = this.getYRadius();
            IrisStyledRange var8 = var2.getYRadius();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            IrisStyledRange var9 = this.getZRadius();
            IrisStyledRange var10 = var2.getZRadius();
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
      return var1 instanceof IrisElipsoid;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var7 = var2 * 59 + this.getRarity();
      String var3 = this.getCustomBiome();
      var7 = var7 * 59 + (var3 == null ? 43 : var3.hashCode());
      IrisStyledRange var4 = this.getXRadius();
      var7 = var7 * 59 + (var4 == null ? 43 : var4.hashCode());
      IrisStyledRange var5 = this.getYRadius();
      var7 = var7 * 59 + (var5 == null ? 43 : var5.hashCode());
      IrisStyledRange var6 = this.getZRadius();
      var7 = var7 * 59 + (var6 == null ? 43 : var6.hashCode());
      return var7;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getMatterNodeCache());
      return "IrisElipsoid(matterNodeCache=" + var10000 + ", rarity=" + this.getRarity() + ", customBiome=" + this.getCustomBiome() + ", xRadius=" + String.valueOf(this.getXRadius()) + ", yRadius=" + String.valueOf(this.getYRadius()) + ", zRadius=" + String.valueOf(this.getZRadius()) + ")";
   }
}
