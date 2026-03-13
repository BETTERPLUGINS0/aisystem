package com.volmit.iris.engine.mantle.components;

import com.volmit.iris.Iris;
import com.volmit.iris.engine.data.cache.Cache;
import com.volmit.iris.engine.mantle.ComponentFlag;
import com.volmit.iris.engine.mantle.EngineMantle;
import com.volmit.iris.engine.mantle.IrisMantleComponent;
import com.volmit.iris.engine.mantle.MantleWriter;
import com.volmit.iris.engine.object.CarveResult;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.engine.object.IrisObject;
import com.volmit.iris.engine.object.IrisObjectPlacement;
import com.volmit.iris.engine.object.IrisObjectScale;
import com.volmit.iris.engine.object.IrisRegion;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.collection.KSet;
import com.volmit.iris.util.context.ChunkContext;
import com.volmit.iris.util.data.B;
import com.volmit.iris.util.documentation.BlockCoordinates;
import com.volmit.iris.util.documentation.ChunkCoordinates;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.mantle.flag.ReservedFlag;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.matter.MatterStructurePOI;
import com.volmit.iris.util.noise.CNG;
import com.volmit.iris.util.noise.NoiseType;
import com.volmit.iris.util.parallel.BurstExecutor;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.util.BlockVector;

@ComponentFlag(ReservedFlag.OBJECT)
public class MantleObjectComponent extends IrisMantleComponent {
   public MantleObjectComponent(EngineMantle engineMantle) {
      super(var1, ReservedFlag.OBJECT, 1);
   }

   public void generateLayer(MantleWriter writer, int x, int z, ChunkContext context) {
      RNG var5 = this.applyNoise(var2, var3, Cache.key(var2, var3) + this.seed());
      int var6 = 8 + (var2 << 4);
      int var7 = 8 + (var3 << 4);
      IrisRegion var8 = (IrisRegion)this.getComplex().getRegionStream().get((double)var6, (double)var7);
      IrisBiome var9 = (IrisBiome)this.getComplex().getTrueBiomeStream().get((double)var6, (double)var7);
      this.placeObjects(var1, var5, var2, var3, var9, var8);
   }

   private RNG applyNoise(int x, int z, long seed) {
      CNG var5 = CNG.signatureFast(new RNG(var3), NoiseType.WHITE, NoiseType.GLOB);
      return new RNG((long)((double)var3 * var5.noise((double)var1, (double)var2)));
   }

   @ChunkCoordinates
   private void placeObjects(MantleWriter writer, RNG rng, int x, int z, IrisBiome biome, IrisRegion region) {
      Iterator var7 = var5.getSurfaceObjects().iterator();

      String var10000;
      IrisObjectPlacement var8;
      while(var7.hasNext()) {
         var8 = (IrisObjectPlacement)var7.next();
         if (var2.chance(var8.getChance() + var2.d(-0.005D, 0.005D))) {
            try {
               this.placeObject(var1, var2, var3 << 4, var4 << 4, var8);
            } catch (Throwable var11) {
               Iris.reportError(var11);
               Iris.error("Failed to place objects in the following biome: " + var5.getName());
               var10000 = var8.getPlace().toString(", ");
               Iris.error("Object(s) " + var10000 + " (" + var11.getClass().getSimpleName() + ").");
               Iris.error("Are these objects missing?");
               var11.printStackTrace();
            }
         }
      }

      var7 = var6.getSurfaceObjects().iterator();

      while(var7.hasNext()) {
         var8 = (IrisObjectPlacement)var7.next();
         if (var2.chance(var8.getChance() + var2.d(-0.005D, 0.005D))) {
            try {
               this.placeObject(var1, var2, var3 << 4, var4 << 4, var8);
            } catch (Throwable var10) {
               Iris.reportError(var10);
               Iris.error("Failed to place objects in the following region: " + var6.getName());
               var10000 = var8.getPlace().toString(", ");
               Iris.error("Object(s) " + var10000 + " (" + var10.getClass().getSimpleName() + ").");
               Iris.error("Are these objects missing?");
               var10.printStackTrace();
            }
         }
      }

   }

   @BlockCoordinates
   private void placeObject(MantleWriter writer, RNG rng, int x, int z, IrisObjectPlacement objectPlacement) {
      for(int var6 = 0; var6 < var5.getDensity(var2, (double)var3, (double)var4, this.getData()); ++var6) {
         IrisObject var7 = var5.getScale().get(var2, var5.getObject(this.getComplex(), var2));
         if (var7 == null) {
            return;
         }

         int var8 = var2.i(var3, var3 + 15);
         int var9 = var2.i(var4, var4 + 15);
         int var10 = var2.i(0, Integer.MAX_VALUE);
         var7.place(var8, -1, var9, var1, var5, var2, (var4x, var5x) -> {
            int var10001 = var4x.getX();
            int var10002 = var4x.getY();
            int var10003 = var4x.getZ();
            String var10004 = var7.getLoadKey();
            var1.setData(var10001, var10002, var10003, var10004 + "@" + var10);
            if (var5.isDolphinTarget() && var5.isUnderwater() && B.isStorageChest(var5x)) {
               var1.setData(var4x.getX(), var4x.getY(), var4x.getZ(), MatterStructurePOI.BURIED_TREASURE);
            }

         }, (CarveResult)null, this.getData());
      }

   }

   @BlockCoordinates
   private Set<String> guessPlacedKeys(RNG rng, int x, int z, IrisObjectPlacement objectPlacement) {
      KSet var5 = new KSet(new String[0]);

      for(int var6 = 0; var6 < var4.getDensity(var1, (double)var2, (double)var3, this.getData()); ++var6) {
         IrisObject var7 = var4.getScale().get(var1, var4.getObject(this.getComplex(), var1));
         if (var7 != null) {
            var5.add(var7.getLoadKey());
         }
      }

      return var5;
   }

   public Set<String> guess(int x, int z) {
      RNG var3 = this.applyNoise(var1, var2, Cache.key(var1, var2) + this.seed());
      IrisBiome var4 = this.getEngineMantle().getEngine().getSurfaceBiome((var1 << 4) + 8, (var2 << 4) + 8);
      IrisRegion var5 = this.getEngineMantle().getEngine().getRegion((var1 << 4) + 8, (var2 << 4) + 8);
      KSet var6 = new KSet(new String[0]);
      Iterator var7 = var4.getSurfaceObjects().iterator();

      IrisObjectPlacement var8;
      while(var7.hasNext()) {
         var8 = (IrisObjectPlacement)var7.next();
         if (var3.chance(var8.getChance() + var3.d(-0.005D, 0.005D))) {
            var6.addAll(this.guessPlacedKeys(var3, var1, var2, var8));
         }
      }

      var7 = var5.getSurfaceObjects().iterator();

      while(var7.hasNext()) {
         var8 = (IrisObjectPlacement)var7.next();
         if (var3.chance(var8.getChance() + var3.d(-0.005D, 0.005D))) {
            var6.addAll(this.guessPlacedKeys(var3, var1, var2, var8));
         }
      }

      return var6;
   }

   protected int computeRadius() {
      IrisDimension var1 = this.getDimension();
      AtomicInteger var2 = new AtomicInteger();
      AtomicInteger var3 = new AtomicInteger();
      KSet var4 = new KSet(new String[0]);
      KMap var5 = new KMap();
      Iterator var6 = var1.getAllRegions(this::getData).iterator();

      Iterator var8;
      IrisObjectPlacement var9;
      while(var6.hasNext()) {
         IrisRegion var7 = (IrisRegion)var6.next();
         var8 = var7.getObjects().iterator();

         while(var8.hasNext()) {
            var9 = (IrisObjectPlacement)var8.next();
            if (var9.getScale().canScaleBeyond()) {
               var5.put(var9.getScale(), var9.getPlace());
            } else {
               var4.addAll(var9.getPlace());
            }
         }
      }

      var6 = var1.getAllBiomes(this::getData).iterator();

      while(var6.hasNext()) {
         IrisBiome var15 = (IrisBiome)var6.next();
         var8 = var15.getObjects().iterator();

         while(var8.hasNext()) {
            var9 = (IrisObjectPlacement)var8.next();
            if (var9.getScale().canScaleBeyond()) {
               var5.put(var9.getScale(), var9.getPlace());
            } else {
               var4.addAll(var9.getPlace());
            }
         }
      }

      BurstExecutor var14 = this.getEngineMantle().getTarget().getBurster().burst(var4.size());
      KMap var16 = new KMap();
      var8 = var4.iterator();

      while(var8.hasNext()) {
         String var17 = (String)var8.next();
         var14.queue(() -> {
            try {
               BlockVector var5 = (BlockVector)var16.computeIfAbsent(var17, (var2x) -> {
                  try {
                     return IrisObject.sampleSize(this.getData().getObjectLoader().findFile(var17));
                  } catch (IOException var4) {
                     Iris.reportError(var4);
                     var4.printStackTrace();
                     return null;
                  }
               });
               if (var5 == null) {
                  throw new RuntimeException();
               }

               if (Math.max(var5.getBlockX(), var5.getBlockZ()) > 128) {
                  Iris.warn("Object " + var17 + " has a large size (" + String.valueOf(var5) + ") and may increase memory usage!");
               }

               synchronized(var2) {
                  var2.getAndSet(Math.max(var5.getBlockX(), var2.get()));
               }

               synchronized(var3) {
                  var3.getAndSet(Math.max(var5.getBlockZ(), var3.get()));
               }
            } catch (Throwable var11) {
               Iris.reportError(var11);
            }

         });
      }

      var8 = var5.entrySet().iterator();

      while(var8.hasNext()) {
         Entry var18 = (Entry)var8.next();
         double var10 = ((IrisObjectScale)var18.getKey()).getMaximumScale();
         Iterator var12 = ((KList)var18.getValue()).iterator();

         while(var12.hasNext()) {
            String var13 = (String)var12.next();
            var14.queue(() -> {
               try {
                  BlockVector var7 = (BlockVector)var16.computeIfAbsent(var13, (var2x) -> {
                     try {
                        return IrisObject.sampleSize(this.getData().getObjectLoader().findFile(var13));
                     } catch (IOException var4) {
                        Iris.reportError(var4);
                        var4.printStackTrace();
                        return null;
                     }
                  });
                  if (var7 == null) {
                     throw new RuntimeException();
                  }

                  if (Math.max(var7.getBlockX(), var7.getBlockZ()) > 128) {
                     Iris.warn("Object " + var13 + " has a large size (" + String.valueOf(var7) + ") and may increase memory usage! (Object scaled up to " + Form.pc(var10, 2) + ")");
                  }

                  synchronized(var2) {
                     var2.getAndSet((int)Math.max(Math.ceil((double)var7.getBlockX() * var10), (double)var2.get()));
                  }

                  synchronized(var3) {
                     var3.getAndSet((int)Math.max(Math.ceil((double)var7.getBlockZ() * var10), (double)var3.get()));
                  }
               } catch (Throwable var13x) {
                  Iris.reportError(var13x);
               }

            });
         }
      }

      var14.complete();
      return Math.max(var2.get(), var3.get());
   }
}
