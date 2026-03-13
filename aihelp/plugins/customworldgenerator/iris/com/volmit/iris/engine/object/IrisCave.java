package com.volmit.iris.engine.object;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.mantle.MantleWriter;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KSet;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.matter.MatterCavern;
import com.volmit.iris.util.noise.CNG;
import com.volmit.iris.util.plugin.VolmitSender;
import java.util.Iterator;
import lombok.Generated;

@Desc("Translate objects")
public class IrisCave extends IrisRegistrant {
   @Desc("Define the shape of this cave")
   private IrisWorm worm = new IrisWorm();
   @Desc("Define potential forking features")
   private IrisCarving fork = new IrisCarving();
   @RegistryListResource(IrisBiome.class)
   @Desc("Force this cave to only generate the specified custom biome")
   private String customBiome = "";
   @Desc("Limit the worm from ever getting higher or lower than this range")
   private IrisRange verticalRange = new IrisRange(3.0D, 255.0D);
   @Desc("Shape of the caves")
   private IrisCaveShape shape = new IrisCaveShape();

   public String getFolderName() {
      return "caves";
   }

   public String getTypeName() {
      return "Cave";
   }

   public void generate(MantleWriter writer, RNG rng, Engine engine, int x, int y, int z) {
      this.generate(var1, var2, new RNG(var3.getSeedManager().getCarve()), var3, var4, var5, var6, 0, -1, true);
   }

   public void generate(MantleWriter writer, RNG rng, RNG base, Engine engine, int x, int y, int z, int recursion, int waterHint, boolean breakSurface) {
      double var11 = this.getWorm().getGirth().get(var3.nextParallelRNG(465156), (double)var5, (double)var7, var4.getData());
      KList var13 = this.getWorm().generate(var3.nextParallelRNG(784684), var4.getData(), var1, this.verticalRange, var5, var6, var7, var10, var11 + 9.0D);
      int var14 = Math.max(var9, -1);
      if (var14 == -1) {
         Iterator var15 = var13.iterator();

         while(var15.hasNext()) {
            IrisPosition var16 = (IrisPosition)var15.next();
            double var17 = (double)var16.getY() + var11;
            int var19 = var4.getHeight(var5, var7, true);
            if (var17 > (double)var19 && var19 < var4.getDimension().getFluidHeight()) {
               var14 = Math.max(var14, (int)var17);
               break;
            }
         }
      }

      int var20 = Math.min(var14, var4.getDimension().getFluidHeight());
      Iterator var21 = var13.iterator();

      while(var21.hasNext()) {
         IrisPosition var23 = (IrisPosition)var21.next();
         this.fork.doCarving(var1, var2, var3, var4, var23.getX(), var23.getY(), var23.getZ(), var8, var20);
      }

      MatterCavern var22 = new MatterCavern(true, this.customBiome, (byte)0);
      MatterCavern var24 = new MatterCavern(true, this.customBiome, (byte)1);
      CNG var18 = this.shape.getNoise(var3.nextParallelRNG(8131545), var4);
      KSet var25 = this.shape.getMasked(var2, var4);
      var1.setNoiseMasked(var13, var11, this.shape.getNoiseThreshold() < 0.0D ? var18.noise((double)var5, (double)var6, (double)var7) : this.shape.getNoiseThreshold(), var18, var25, true, (var3x, var4x, var5x) -> {
         return var4x <= var20 ? var24 : var22;
      });
   }

   public void scanForErrors(JSONObject p, VolmitSender sender) {
   }

   public int getMaxSize(IrisData data, int depth) {
      return (int)(Math.ceil(this.getWorm().getGirth().getMax() * 2.0D) + (double)this.getWorm().getMaxDistance() + (double)this.fork.getMaxRange(var1, var2));
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisCave)) {
         return false;
      } else {
         IrisCave var2 = (IrisCave)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (!super.equals(var1)) {
            return false;
         } else {
            label73: {
               IrisWorm var3 = this.getWorm();
               IrisWorm var4 = var2.getWorm();
               if (var3 == null) {
                  if (var4 == null) {
                     break label73;
                  }
               } else if (var3.equals(var4)) {
                  break label73;
               }

               return false;
            }

            IrisCarving var5 = this.getFork();
            IrisCarving var6 = var2.getFork();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            label59: {
               String var7 = this.getCustomBiome();
               String var8 = var2.getCustomBiome();
               if (var7 == null) {
                  if (var8 == null) {
                     break label59;
                  }
               } else if (var7.equals(var8)) {
                  break label59;
               }

               return false;
            }

            IrisRange var9 = this.getVerticalRange();
            IrisRange var10 = var2.getVerticalRange();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            IrisCaveShape var11 = this.getShape();
            IrisCaveShape var12 = var2.getShape();
            if (var11 == null) {
               if (var12 != null) {
                  return false;
               }
            } else if (!var11.equals(var12)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisCave;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      int var2 = super.hashCode();
      IrisWorm var3 = this.getWorm();
      var2 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      IrisCarving var4 = this.getFork();
      var2 = var2 * 59 + (var4 == null ? 43 : var4.hashCode());
      String var5 = this.getCustomBiome();
      var2 = var2 * 59 + (var5 == null ? 43 : var5.hashCode());
      IrisRange var6 = this.getVerticalRange();
      var2 = var2 * 59 + (var6 == null ? 43 : var6.hashCode());
      IrisCaveShape var7 = this.getShape();
      var2 = var2 * 59 + (var7 == null ? 43 : var7.hashCode());
      return var2;
   }

   @Generated
   public IrisCave() {
   }

   @Generated
   public IrisCave(final IrisWorm worm, final IrisCarving fork, final String customBiome, final IrisRange verticalRange, final IrisCaveShape shape) {
      this.worm = var1;
      this.fork = var2;
      this.customBiome = var3;
      this.verticalRange = var4;
      this.shape = var5;
   }

   @Generated
   public IrisWorm getWorm() {
      return this.worm;
   }

   @Generated
   public IrisCarving getFork() {
      return this.fork;
   }

   @Generated
   public String getCustomBiome() {
      return this.customBiome;
   }

   @Generated
   public IrisRange getVerticalRange() {
      return this.verticalRange;
   }

   @Generated
   public IrisCaveShape getShape() {
      return this.shape;
   }

   @Generated
   public IrisCave setWorm(final IrisWorm worm) {
      this.worm = var1;
      return this;
   }

   @Generated
   public IrisCave setFork(final IrisCarving fork) {
      this.fork = var1;
      return this;
   }

   @Generated
   public IrisCave setCustomBiome(final String customBiome) {
      this.customBiome = var1;
      return this;
   }

   @Generated
   public IrisCave setVerticalRange(final IrisRange verticalRange) {
      this.verticalRange = var1;
      return this;
   }

   @Generated
   public IrisCave setShape(final IrisCaveShape shape) {
      this.shape = var1;
      return this;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getWorm());
      return "IrisCave(worm=" + var10000 + ", fork=" + String.valueOf(this.getFork()) + ", customBiome=" + this.getCustomBiome() + ", verticalRange=" + String.valueOf(this.getVerticalRange()) + ", shape=" + String.valueOf(this.getShape()) + ")";
   }
}
