package com.volmit.iris.engine.platform.studio.generators;

import com.volmit.iris.engine.data.cache.Cache;
import com.volmit.iris.engine.data.chunk.TerrainChunk;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.platform.studio.EnginedStudioGenerator;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

public class BiomeBuffetGenerator extends EnginedStudioGenerator {
   private static final BlockData FLOOR;
   private final IrisBiome[] biomes;
   private final int width;
   private final int biomeSize;

   public BiomeBuffetGenerator(Engine engine, int biomeSize) {
      super(var1);
      this.biomeSize = var2;
      this.biomes = (IrisBiome[])var1.getDimension().getAllBiomes(var1).toArray(new IrisBiome[0]);
      this.width = Math.max((int)Math.sqrt((double)this.biomes.length), 1);
   }

   public void generateChunk(Engine engine, TerrainChunk tc, int x, int z) {
      int var5 = Cache.to1D(var3 / this.biomeSize, 0, var4 / this.biomeSize, this.width, 1);
      if (var5 >= 0 && var5 < this.biomes.length) {
         IrisBiome var6 = this.biomes[var5];
         String var7 = var1.getDimension().getFocus();
         if (!Objects.equals(var7, var6.getLoadKey())) {
            var1.getDimension().setFocus(var6.getLoadKey());
            var1.hotloadComplex();
         }

         var1.generate(var3 << 4, var4 << 4, var2, true);
      } else {
         var2.setRegion(0, 0, 0, 16, 1, 16, FLOOR);
      }

   }

   static {
      FLOOR = Material.BARRIER.createBlockData();
   }
}
