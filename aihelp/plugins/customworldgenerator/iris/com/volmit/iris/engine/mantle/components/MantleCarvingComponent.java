package com.volmit.iris.engine.mantle.components;

import com.volmit.iris.engine.data.cache.Cache;
import com.volmit.iris.engine.mantle.ComponentFlag;
import com.volmit.iris.engine.mantle.EngineMantle;
import com.volmit.iris.engine.mantle.IrisMantleComponent;
import com.volmit.iris.engine.mantle.MantleWriter;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisCarving;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.engine.object.IrisRegion;
import com.volmit.iris.util.context.ChunkContext;
import com.volmit.iris.util.documentation.ChunkCoordinates;
import com.volmit.iris.util.mantle.flag.ReservedFlag;
import com.volmit.iris.util.math.RNG;
import java.util.Iterator;

@ComponentFlag(ReservedFlag.CARVED)
public class MantleCarvingComponent extends IrisMantleComponent {
   public MantleCarvingComponent(EngineMantle engineMantle) {
      super(var1, ReservedFlag.CARVED, 0);
   }

   public void generateLayer(MantleWriter writer, int x, int z, ChunkContext context) {
      RNG var5 = new RNG(Cache.key(var2, var3) + this.seed());
      int var6 = 8 + (var2 << 4);
      int var7 = 8 + (var3 << 4);
      IrisRegion var8 = (IrisRegion)this.getComplex().getRegionStream().get((double)var6, (double)var7);
      IrisBiome var9 = (IrisBiome)this.getComplex().getTrueBiomeStream().get((double)var6, (double)var7);
      this.carve(var1, var5, var2, var3, var8, var9);
   }

   @ChunkCoordinates
   private void carve(MantleWriter writer, RNG rng, int cx, int cz, IrisRegion region, IrisBiome biome) {
      this.carve(this.getDimension().getCarving(), var1, new RNG(var2.nextLong() * (long)var3 + 490495L + (long)var4), var3, var4);
      this.carve(var6.getCarving(), var1, new RNG(var2.nextLong() * (long)var3 + 490495L + (long)var4), var3, var4);
      this.carve(var5.getCarving(), var1, new RNG(var2.nextLong() * (long)var3 + 490495L + (long)var4), var3, var4);
   }

   @ChunkCoordinates
   private void carve(IrisCarving carving, MantleWriter writer, RNG rng, int cx, int cz) {
      var1.doCarving(var2, var3, this.getEngineMantle().getEngine(), var4 << 4, -1, var5 << 4, 0);
   }

   protected int computeRadius() {
      IrisDimension var1 = this.getDimension();
      byte var2 = 0;
      int var5 = Math.max(var2, var1.getCarving().getMaxRange(this.getData(), 0));

      Iterator var3;
      IrisRegion var4;
      for(var3 = var1.getAllRegions(this::getData).iterator(); var3.hasNext(); var5 = Math.max(var5, var4.getCarving().getMaxRange(this.getData(), 0))) {
         var4 = (IrisRegion)var3.next();
      }

      IrisBiome var6;
      for(var3 = var1.getAllBiomes(this::getData).iterator(); var3.hasNext(); var5 = Math.max(var5, var6.getCarving().getMaxRange(this.getData(), 0))) {
         var6 = (IrisBiome)var3.next();
      }

      return var5;
   }
}
