package com.volmit.iris.engine.mantle.components;

import com.volmit.iris.engine.data.cache.Cache;
import com.volmit.iris.engine.mantle.ComponentFlag;
import com.volmit.iris.engine.mantle.EngineMantle;
import com.volmit.iris.engine.mantle.IrisMantleComponent;
import com.volmit.iris.engine.mantle.MantleWriter;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisFluidBodies;
import com.volmit.iris.engine.object.IrisRegion;
import com.volmit.iris.util.context.ChunkContext;
import com.volmit.iris.util.documentation.ChunkCoordinates;
import com.volmit.iris.util.mantle.flag.ReservedFlag;
import com.volmit.iris.util.math.RNG;
import java.util.Iterator;

@ComponentFlag(ReservedFlag.FLUID_BODIES)
public class MantleFluidBodyComponent extends IrisMantleComponent {
   public MantleFluidBodyComponent(EngineMantle engineMantle) {
      super(var1, ReservedFlag.FLUID_BODIES, 0);
   }

   public void generateLayer(MantleWriter writer, int x, int z, ChunkContext context) {
      RNG var5 = new RNG(Cache.key(var2, var3) + this.seed() + 405666L);
      int var6 = 8 + (var2 << 4);
      int var7 = 8 + (var3 << 4);
      IrisRegion var8 = (IrisRegion)this.getComplex().getRegionStream().get((double)var6, (double)var7);
      IrisBiome var9 = (IrisBiome)this.getComplex().getTrueBiomeStream().get((double)var6, (double)var7);
      this.generate(var1, var5, var2, var3, var8, var9);
   }

   @ChunkCoordinates
   private void generate(MantleWriter writer, RNG rng, int cx, int cz, IrisRegion region, IrisBiome biome) {
      this.generate(this.getDimension().getFluidBodies(), var1, new RNG(var2.nextLong() * (long)var3 + 490495L + (long)var4), var3, var4);
      this.generate(var6.getFluidBodies(), var1, new RNG(var2.nextLong() * (long)var3 + 490495L + (long)var4), var3, var4);
      this.generate(var5.getFluidBodies(), var1, new RNG(var2.nextLong() * (long)var3 + 490495L + (long)var4), var3, var4);
   }

   @ChunkCoordinates
   private void generate(IrisFluidBodies bodies, MantleWriter writer, RNG rng, int cx, int cz) {
      var1.generate(var2, var3, this.getEngineMantle().getEngine(), var4 << 4, -1, var5 << 4);
   }

   protected int computeRadius() {
      byte var1 = 0;
      int var4 = Math.max(var1, this.getDimension().getFluidBodies().getMaxRange(this.getData()));

      Iterator var2;
      IrisRegion var3;
      for(var2 = this.getDimension().getAllRegions(this::getData).iterator(); var2.hasNext(); var4 = Math.max(var4, var3.getFluidBodies().getMaxRange(this.getData()))) {
         var3 = (IrisRegion)var2.next();
      }

      IrisBiome var5;
      for(var2 = this.getDimension().getAllBiomes(this::getData).iterator(); var2.hasNext(); var4 = Math.max(var4, var5.getFluidBodies().getMaxRange(this.getData()))) {
         var5 = (IrisBiome)var2.next();
      }

      return var4;
   }
}
