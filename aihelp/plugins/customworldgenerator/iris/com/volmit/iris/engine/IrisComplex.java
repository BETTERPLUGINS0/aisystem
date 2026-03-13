package com.volmit.iris.engine;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.data.cache.Cache;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.InferredType;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisDecorationPart;
import com.volmit.iris.engine.object.IrisDecorator;
import com.volmit.iris.engine.object.IrisGenerator;
import com.volmit.iris.engine.object.IrisInterpolator;
import com.volmit.iris.engine.object.IrisRegion;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.context.IrisContext;
import com.volmit.iris.util.data.DataProvider;
import com.volmit.iris.util.interpolation.IrisInterpolation;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.noise.CNG;
import com.volmit.iris.util.stream.ProceduralStream;
import com.volmit.iris.util.stream.interpolation.Interpolated;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.Generated;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;

public class IrisComplex implements DataProvider {
   private static final BlockData AIR;
   private RNG rng;
   private double fluidHeight;
   private IrisData data;
   private Map<IrisInterpolator, Set<IrisGenerator>> generators;
   private ProceduralStream<IrisRegion> regionStream;
   private ProceduralStream<Double> regionStyleStream;
   private ProceduralStream<Double> regionIdentityStream;
   private ProceduralStream<UUID> regionIDStream;
   private ProceduralStream<InferredType> bridgeStream;
   private ProceduralStream<IrisBiome> landBiomeStream;
   private ProceduralStream<IrisBiome> caveBiomeStream;
   private ProceduralStream<IrisBiome> seaBiomeStream;
   private ProceduralStream<IrisBiome> shoreBiomeStream;
   private ProceduralStream<IrisBiome> baseBiomeStream;
   private ProceduralStream<UUID> baseBiomeIDStream;
   private ProceduralStream<IrisBiome> trueBiomeStream;
   private ProceduralStream<Biome> trueBiomeDerivativeStream;
   private ProceduralStream<Double> heightStream;
   private ProceduralStream<Integer> roundedHeighteightStream;
   private ProceduralStream<Double> maxHeightStream;
   private ProceduralStream<Double> overlayStream;
   private ProceduralStream<Double> heightFluidStream;
   private ProceduralStream<Double> slopeStream;
   private ProceduralStream<Integer> topSurfaceStream;
   private ProceduralStream<IrisDecorator> terrainSurfaceDecoration;
   private ProceduralStream<IrisDecorator> terrainCeilingDecoration;
   private ProceduralStream<IrisDecorator> terrainCaveSurfaceDecoration;
   private ProceduralStream<IrisDecorator> terrainCaveCeilingDecoration;
   private ProceduralStream<IrisDecorator> seaSurfaceDecoration;
   private ProceduralStream<IrisDecorator> seaFloorDecoration;
   private ProceduralStream<IrisDecorator> shoreSurfaceDecoration;
   private ProceduralStream<BlockData> rockStream;
   private ProceduralStream<BlockData> fluidStream;
   private IrisBiome focusBiome;
   private IrisRegion focusRegion;

   public IrisComplex(Engine engine) {
      this(var1, false);
   }

   public IrisComplex(Engine engine, boolean simple) {
      int var3 = IrisSettings.get().getPerformance().getNoiseCacheSize();
      IrisBiome var4 = new IrisBiome();
      UUID var5 = UUID.nameUUIDFromBytes("focus".getBytes());
      this.rng = new RNG(var1.getSeedManager().getComplex());
      this.data = var1.getData();
      double var6 = (double)var1.getMaxHeight();
      this.fluidHeight = (double)var1.getDimension().getFluidHeight();
      this.generators = new HashMap();
      this.focusBiome = var1.getFocus();
      this.focusRegion = var1.getFocusRegion();
      HashMap var8 = new HashMap();
      if (this.focusBiome != null) {
         this.focusBiome.setInferredType(InferredType.LAND);
         this.focusRegion = this.findRegion(this.focusBiome, var1);
      }

      if (this.focusRegion != null) {
         this.focusRegion.getAllBiomes(this).forEach(this::registerGenerators);
      } else {
         var1.getDimension().getRegions().forEach((var1x) -> {
            ((IrisRegion)this.data.getRegionLoader().load(var1x)).getAllBiomes(this).forEach(this::registerGenerators);
         });
      }

      boolean var9 = var1.getDimension().isLegacyRarity();
      this.overlayStream = ProceduralStream.ofDouble((var0, var1x) -> {
         return 0.0D;
      }).waste("Overlay Stream");
      var1.getDimension().getOverlayNoise().forEach((var1x) -> {
         this.overlayStream = this.overlayStream.add((var2, var3) -> {
            return var1x.get(this.rng, this.getData(), var2, var3);
         });
      });
      this.rockStream = var1.getDimension().getRockPalette().getLayerGenerator(this.rng.nextParallelRNG(45), this.data).stream().select((List)var1.getDimension().getRockPalette().getBlockData(this.data)).waste("Rock Stream");
      this.fluidStream = var1.getDimension().getFluidPalette().getLayerGenerator(this.rng.nextParallelRNG(78), this.data).stream().select((List)var1.getDimension().getFluidPalette().getBlockData(this.data)).waste("Fluid Stream");
      this.regionStyleStream = var1.getDimension().getRegionStyle().create(this.rng.nextParallelRNG(883), this.getData()).stream().zoom(var1.getDimension().getRegionZoom()).waste("Region Style");
      this.regionIdentityStream = this.regionStyleStream.fit(-2.147483648E9D, 2.147483647E9D).waste("Region Identity Stream");
      this.regionStream = this.focusRegion != null ? ProceduralStream.of((var1x, var2x) -> {
         return this.focusRegion;
      }, Interpolated.of((var0) -> {
         return 0.0D;
      }, (var1x) -> {
         return this.focusRegion;
      })) : this.regionStyleStream.selectRarity(this.data.getRegionLoader().loadAll(var1.getDimension().getRegions()), var9).cache2D("regionStream", var1, var3).waste("Region Stream");
      this.regionIDStream = this.regionIdentityStream.convertCached((var0) -> {
         return new UUID(Double.doubleToLongBits(var0), (long)String.valueOf(var0 * 38445.0D).hashCode() * 3245556666L);
      }).waste("Region ID Stream");
      this.caveBiomeStream = this.regionStream.contextInjecting((var1x, var2x, var3x) -> {
         return (IrisRegion)IrisContext.getOr(var1).getChunkContext().getRegion().get(var2x, var3x);
      }).convert((var4x) -> {
         return var1.getDimension().getCaveBiomeStyle().create(this.rng.nextParallelRNG(InferredType.CAVE.ordinal()), this.getData()).stream().zoom(var1.getDimension().getBiomeZoom()).zoom(var4x.getCaveBiomeZoom()).selectRarity(this.data.getBiomeLoader().loadAll(var4x.getCaveBiomes()), var9).onNull(var4);
      }).convertAware2D(ProceduralStream::get).cache2D("caveBiomeStream", var1, var3).waste("Cave Biome Stream");
      var8.put(InferredType.CAVE, this.caveBiomeStream);
      this.landBiomeStream = this.regionStream.contextInjecting((var1x, var2x, var3x) -> {
         return (IrisRegion)IrisContext.getOr(var1).getChunkContext().getRegion().get(var2x, var3x);
      }).convert((var3x) -> {
         return var1.getDimension().getLandBiomeStyle().create(this.rng.nextParallelRNG(InferredType.LAND.ordinal()), this.getData()).stream().zoom(var1.getDimension().getBiomeZoom()).zoom(var1.getDimension().getLandZoom()).zoom(var3x.getLandBiomeZoom()).selectRarity(this.data.getBiomeLoader().loadAll(var3x.getLandBiomes(), (var0) -> {
            var0.setInferredType(InferredType.LAND);
         }), var9);
      }).convertAware2D(ProceduralStream::get).cache2D("landBiomeStream", var1, var3).waste("Land Biome Stream");
      var8.put(InferredType.LAND, this.landBiomeStream);
      this.seaBiomeStream = this.regionStream.contextInjecting((var1x, var2x, var3x) -> {
         return (IrisRegion)IrisContext.getOr(var1).getChunkContext().getRegion().get(var2x, var3x);
      }).convert((var3x) -> {
         return var1.getDimension().getSeaBiomeStyle().create(this.rng.nextParallelRNG(InferredType.SEA.ordinal()), this.getData()).stream().zoom(var1.getDimension().getBiomeZoom()).zoom(var1.getDimension().getSeaZoom()).zoom(var3x.getSeaBiomeZoom()).selectRarity(this.data.getBiomeLoader().loadAll(var3x.getSeaBiomes(), (var0) -> {
            var0.setInferredType(InferredType.SEA);
         }), var9);
      }).convertAware2D(ProceduralStream::get).cache2D("seaBiomeStream", var1, var3).waste("Sea Biome Stream");
      var8.put(InferredType.SEA, this.seaBiomeStream);
      this.shoreBiomeStream = this.regionStream.contextInjecting((var1x, var2x, var3x) -> {
         return (IrisRegion)IrisContext.getOr(var1).getChunkContext().getRegion().get(var2x, var3x);
      }).convert((var3x) -> {
         return var1.getDimension().getShoreBiomeStyle().create(this.rng.nextParallelRNG(InferredType.SHORE.ordinal()), this.getData()).stream().zoom(var1.getDimension().getBiomeZoom()).zoom(var3x.getShoreBiomeZoom()).selectRarity(this.data.getBiomeLoader().loadAll(var3x.getShoreBiomes(), (var0) -> {
            var0.setInferredType(InferredType.SHORE);
         }), var9);
      }).convertAware2D(ProceduralStream::get).cache2D("shoreBiomeStream", var1, var3).waste("Shore Biome Stream");
      var8.put(InferredType.SHORE, this.shoreBiomeStream);
      this.bridgeStream = this.focusBiome != null ? ProceduralStream.of((var1x, var2x) -> {
         return this.focusBiome.getInferredType();
      }, Interpolated.of((var0) -> {
         return 0.0D;
      }, (var1x) -> {
         return this.focusBiome.getInferredType();
      })) : var1.getDimension().getContinentalStyle().create(this.rng.nextParallelRNG(234234565), this.getData()).bake().scale(1.0D / var1.getDimension().getContinentZoom()).bake().stream().convert((var1x) -> {
         return var1x >= var1.getDimension().getLandChance() ? InferredType.SEA : InferredType.LAND;
      }).cache2D("bridgeStream", var1, var3).waste("Bridge Stream");
      this.baseBiomeStream = this.focusBiome != null ? ProceduralStream.of((var1x, var2x) -> {
         return this.focusBiome;
      }, Interpolated.of((var0) -> {
         return 0.0D;
      }, (var1x) -> {
         return this.focusBiome;
      })) : this.bridgeStream.convertAware2D((var1x, var2x, var3x) -> {
         return (IrisBiome)((ProceduralStream)var8.get(var1x)).get(var2x, var3x);
      }).convertAware2D(this::implode).cache2D("baseBiomeStream", var1, var3).waste("Base Biome Stream");
      this.heightStream = ProceduralStream.of((var2x, var3x) -> {
         IrisBiome var4 = this.focusBiome != null ? this.focusBiome : (IrisBiome)this.baseBiomeStream.get(var2x, var3x);
         return this.getHeight(var1, var4, var2x, var3x, var1.getSeedManager().getHeight());
      }, Interpolated.DOUBLE).cache2D("heightStream", var1, var3).waste("Height Stream");
      this.roundedHeighteightStream = this.heightStream.contextInjecting((var1x, var2x, var3x) -> {
         return (Double)IrisContext.getOr(var1).getChunkContext().getHeight().get(var2x, var3x);
      }).round().waste("Rounded Height Stream");
      this.slopeStream = this.heightStream.contextInjecting((var1x, var2x, var3x) -> {
         return (Double)IrisContext.getOr(var1).getChunkContext().getHeight().get(var2x, var3x);
      }).slope(3).cache2D("slopeStream", var1, var3).waste("Slope Stream");
      this.trueBiomeStream = (ProceduralStream)(this.focusBiome != null ? ProceduralStream.of((var1x, var2x) -> {
         return this.focusBiome;
      }, Interpolated.of((var0) -> {
         return 0.0D;
      }, (var1x) -> {
         return this.focusBiome;
      })).cache2D("trueBiomeStream-focus", var1, var3) : this.heightStream.convertAware2D((var2x, var3x, var4x) -> {
         return this.fixBiomeType(var2x, (IrisBiome)this.baseBiomeStream.get(var3x, var4x), (IrisRegion)this.regionStream.contextInjecting((var1x, var2, var3) -> {
            return (IrisRegion)IrisContext.getOr(var1).getChunkContext().getRegion().get(var2, var3);
         }).get(var3x, var4x), var3x, var4x, this.fluidHeight);
      }).cache2D("trueBiomeStream", var1, var3).waste("True Biome Stream"));
      this.trueBiomeDerivativeStream = this.trueBiomeStream.contextInjecting((var1x, var2x, var3x) -> {
         return (IrisBiome)IrisContext.getOr(var1).getChunkContext().getBiome().get(var2x, var3x);
      }).convert(IrisBiome::getDerivative).cache2D("trueBiomeDerivativeStream", var1, var3).waste("True Biome Derivative Stream");
      this.heightFluidStream = this.heightStream.contextInjecting((var1x, var2x, var3x) -> {
         return (Double)IrisContext.getOr(var1).getChunkContext().getHeight().get(var2x, var3x);
      }).max(this.fluidHeight).cache2D("heightFluidStream", var1, var3).waste("Height Fluid Stream");
      this.maxHeightStream = ProceduralStream.ofDouble((var2x, var3x) -> {
         return var6;
      }).waste("Max Height Stream");
      this.terrainSurfaceDecoration = this.trueBiomeStream.contextInjecting((var1x, var2x, var3x) -> {
         return (IrisBiome)IrisContext.getOr(var1).getChunkContext().getBiome().get(var2x, var3x);
      }).convertAware2D((var1x, var2x, var3x) -> {
         return this.decorateFor(var1x, var2x, var3x, IrisDecorationPart.NONE);
      }).cache2D("terrainSurfaceDecoration", var1, var3).waste("Surface Decoration Stream");
      this.terrainCeilingDecoration = this.trueBiomeStream.contextInjecting((var1x, var2x, var3x) -> {
         return (IrisBiome)IrisContext.getOr(var1).getChunkContext().getBiome().get(var2x, var3x);
      }).convertAware2D((var1x, var2x, var3x) -> {
         return this.decorateFor(var1x, var2x, var3x, IrisDecorationPart.CEILING);
      }).cache2D("terrainCeilingDecoration", var1, var3).waste("Ceiling Decoration Stream");
      this.terrainCaveSurfaceDecoration = this.caveBiomeStream.contextInjecting((var1x, var2x, var3x) -> {
         return (IrisBiome)IrisContext.getOr(var1).getChunkContext().getCave().get(var2x, var3x);
      }).convertAware2D((var1x, var2x, var3x) -> {
         return this.decorateFor(var1x, var2x, var3x, IrisDecorationPart.NONE);
      }).cache2D("terrainCaveSurfaceDecoration", var1, var3).waste("Cave Surface Stream");
      this.terrainCaveCeilingDecoration = this.caveBiomeStream.contextInjecting((var1x, var2x, var3x) -> {
         return (IrisBiome)IrisContext.getOr(var1).getChunkContext().getCave().get(var2x, var3x);
      }).convertAware2D((var1x, var2x, var3x) -> {
         return this.decorateFor(var1x, var2x, var3x, IrisDecorationPart.CEILING);
      }).cache2D("terrainCaveCeilingDecoration", var1, var3).waste("Cave Ceiling Stream");
      this.shoreSurfaceDecoration = this.trueBiomeStream.contextInjecting((var1x, var2x, var3x) -> {
         return (IrisBiome)IrisContext.getOr(var1).getChunkContext().getBiome().get(var2x, var3x);
      }).convertAware2D((var1x, var2x, var3x) -> {
         return this.decorateFor(var1x, var2x, var3x, IrisDecorationPart.SHORE_LINE);
      }).cache2D("shoreSurfaceDecoration", var1, var3).waste("Shore Surface Stream");
      this.seaSurfaceDecoration = this.trueBiomeStream.contextInjecting((var1x, var2x, var3x) -> {
         return (IrisBiome)IrisContext.getOr(var1).getChunkContext().getBiome().get(var2x, var3x);
      }).convertAware2D((var1x, var2x, var3x) -> {
         return this.decorateFor(var1x, var2x, var3x, IrisDecorationPart.SEA_SURFACE);
      }).cache2D("seaSurfaceDecoration", var1, var3).waste("Sea Surface Stream");
      this.seaFloorDecoration = this.trueBiomeStream.contextInjecting((var1x, var2x, var3x) -> {
         return (IrisBiome)IrisContext.getOr(var1).getChunkContext().getBiome().get(var2x, var3x);
      }).convertAware2D((var1x, var2x, var3x) -> {
         return this.decorateFor(var1x, var2x, var3x, IrisDecorationPart.SEA_FLOOR);
      }).cache2D("seaFloorDecoration", var1, var3).waste("Sea Floor Stream");
      this.baseBiomeIDStream = this.trueBiomeStream.contextInjecting((var1x, var2x, var3x) -> {
         return (IrisBiome)IrisContext.getOr(var1).getChunkContext().getBiome().get(var2x, var3x);
      }).convertAware2D((var1x, var2x, var3x) -> {
         UUID var4 = (UUID)this.regionIDStream.get(var2x, var3x);
         return new UUID((long)var1x.getLoadKey().hashCode() * 818223L, (long)var4.hashCode());
      }).cache2D("", var1, var3).waste("Biome ID Stream");
   }

   public ProceduralStream<IrisBiome> getBiomeStream(InferredType type) {
      switch(var1) {
      case CAVE:
         return this.caveBiomeStream;
      case LAND:
         return this.landBiomeStream;
      case SEA:
         return this.seaBiomeStream;
      case SHORE:
         return this.shoreBiomeStream;
      default:
         return null;
      }
   }

   private IrisRegion findRegion(IrisBiome focus, Engine engine) {
      Iterator var3 = var2.getDimension().getAllRegions(var2).iterator();

      IrisRegion var4;
      do {
         if (!var3.hasNext()) {
            String var5 = UUID.randomUUID().toString();
            var4 = new IrisRegion();
            var4.getLandBiomes().add((Object)var1.getLoadKey());
            var4.getSeaBiomes().add((Object)var1.getLoadKey());
            var4.getShoreBiomes().add((Object)var1.getLoadKey());
            var4.setLoadKey(var5);
            var4.setLoader(this.data);
            File var10003 = this.data.getDataFolder();
            String var10004 = this.data.getRegionLoader().getFolderName();
            var4.setLoadFile(new File(var10003, var10004 + "/" + var5 + ".json"));
            return var4;
         }

         var4 = (IrisRegion)var3.next();
      } while(!var4.getAllBiomeIds().contains(var1.getLoadKey()));

      return var4;
   }

   private IrisDecorator decorateFor(IrisBiome b, double x, double z, IrisDecorationPart part) {
      RNG var7 = new RNG(Cache.key((int)var2, (int)var4));
      Iterator var8 = var1.getDecorators().iterator();

      while(var8.hasNext()) {
         IrisDecorator var9 = (IrisDecorator)var8.next();
         if (var9.getPartOf().equals(var6)) {
            BlockData var10 = var9.getBlockData(var1, var7, var2, var4, this.data);
            if (var10 != null) {
               return var9;
            }
         }
      }

      return null;
   }

   private IrisBiome fixBiomeType(Double height, IrisBiome biome, IrisRegion region, Double x, Double z, double fluidHeight) {
      double var8 = var3.getShoreHeight(var4, var5);
      if (var1 >= var6 - 1.0D && var1 <= var6 + var8 && !var2.isShore()) {
         return (IrisBiome)this.shoreBiomeStream.get(var4, var5);
      } else if (var1 > var6 + var8 && !var2.isLand()) {
         return (IrisBiome)this.landBiomeStream.get(var4, var5);
      } else if (var1 < var6 && !var2.isAquatic()) {
         return (IrisBiome)this.seaBiomeStream.get(var4, var5);
      } else {
         return var1 == var6 && !var2.isShore() ? (IrisBiome)this.shoreBiomeStream.get(var4, var5) : var2;
      }
   }

   private double interpolateGenerators(Engine engine, IrisInterpolator interpolator, Set<IrisGenerator> generators, double x, double z, long seed) {
      if (var3.isEmpty()) {
         return 0.0D;
      } else {
         HashMap var10 = new HashMap(64);
         double var11 = var2.interpolate(var4, var6, (var4x, var6x) -> {
            try {
               IrisBiome var8 = (IrisBiome)this.baseBiomeStream.get(var4x, var6x);
               var10.put(new IrisInterpolation.NoiseKey(var4x, var6x), var8);
               double var9 = 0.0D;

               IrisGenerator var12;
               for(Iterator var11 = var3.iterator(); var11.hasNext(); var9 += var8.getGenLinkMax(var12.getLoadKey(), var1)) {
                  var12 = (IrisGenerator)var11.next();
               }

               return var9;
            } catch (Throwable var13) {
               Iris.reportError(var13);
               var13.printStackTrace();
               Iris.error("Failed to sample hi biome at " + var4x + " " + var6x + "...");
               return 0.0D;
            }
         });
         double var13 = var2.interpolate(var4, var6, (var4x, var6x) -> {
            try {
               IrisBiome var8 = (IrisBiome)var10.get(new IrisInterpolation.NoiseKey(var4x, var6x));
               if (var8 == null) {
                  var8 = (IrisBiome)this.baseBiomeStream.get(var4x, var6x);
                  var10.put(new IrisInterpolation.NoiseKey(var4x, var6x), var8);
               }

               double var9 = 0.0D;

               IrisGenerator var12;
               for(Iterator var11 = var3.iterator(); var11.hasNext(); var9 += var8.getGenLinkMin(var12.getLoadKey(), var1)) {
                  var12 = (IrisGenerator)var11.next();
               }

               return var9;
            } catch (Throwable var13) {
               Iris.reportError(var13);
               var13.printStackTrace();
               Iris.error("Failed to sample lo biome at " + var4x + " " + var6x + "...");
               return 0.0D;
            }
         });
         double var15 = 0.0D;

         IrisGenerator var18;
         for(Iterator var17 = var3.iterator(); var17.hasNext(); var15 += M.lerp(var13, var11, var18.getHeight(var4, var6, var8 + 239945L))) {
            var18 = (IrisGenerator)var17.next();
         }

         return var15 / (double)var3.size();
      }
   }

   private double getInterpolatedHeight(Engine engine, double x, double z, long seed) {
      double var8 = 0.0D;

      IrisInterpolator var11;
      for(Iterator var10 = this.generators.keySet().iterator(); var10.hasNext(); var8 += this.interpolateGenerators(var1, var11, (Set)this.generators.get(var11), var2, var4, var6)) {
         var11 = (IrisInterpolator)var10.next();
      }

      return var8;
   }

   private double getHeight(Engine engine, IrisBiome b, double x, double z, long seed) {
      return Math.max(Math.min(this.getInterpolatedHeight(var1, var3, var5, var7) + this.fluidHeight + (Double)this.overlayStream.get(var3, var5), (double)var1.getHeight()), 0.0D);
   }

   private void registerGenerators(IrisBiome biome) {
      var1.getGenerators().forEach((var1x) -> {
         this.registerGenerator(var1x.getCachedGenerator(this));
      });
   }

   private void registerGenerator(IrisGenerator cachedGenerator) {
      ((Set)this.generators.computeIfAbsent(var1.getInterpolator(), (var0) -> {
         return new HashSet();
      })).add(var1);
   }

   private IrisBiome implode(IrisBiome b, Double x, Double z) {
      return var1.getChildren().isEmpty() ? var1 : this.implode(var1, var2, var3, 3);
   }

   private IrisBiome implode(IrisBiome b, Double x, Double z, int max) {
      if (var4 < 0) {
         return var1;
      } else if (var1.getChildren().isEmpty()) {
         return var1;
      } else {
         CNG var5 = var1.getChildrenGenerator(this.rng, 123, var1.getChildShrinkFactor());
         KList var6 = var1.getRealChildren(this).copy();
         var6.add((Object)var1);
         IrisBiome var7 = (IrisBiome)var5.fitRarity(var6, var2, var3);
         var7.setInferredType(var1.getInferredType());
         return this.implode(var7, var2, var3, var4 - 1);
      }
   }

   public void close() {
   }

   @Generated
   public RNG getRng() {
      return this.rng;
   }

   @Generated
   public double getFluidHeight() {
      return this.fluidHeight;
   }

   @Generated
   public IrisData getData() {
      return this.data;
   }

   @Generated
   public Map<IrisInterpolator, Set<IrisGenerator>> getGenerators() {
      return this.generators;
   }

   @Generated
   public ProceduralStream<IrisRegion> getRegionStream() {
      return this.regionStream;
   }

   @Generated
   public ProceduralStream<Double> getRegionStyleStream() {
      return this.regionStyleStream;
   }

   @Generated
   public ProceduralStream<Double> getRegionIdentityStream() {
      return this.regionIdentityStream;
   }

   @Generated
   public ProceduralStream<UUID> getRegionIDStream() {
      return this.regionIDStream;
   }

   @Generated
   public ProceduralStream<InferredType> getBridgeStream() {
      return this.bridgeStream;
   }

   @Generated
   public ProceduralStream<IrisBiome> getLandBiomeStream() {
      return this.landBiomeStream;
   }

   @Generated
   public ProceduralStream<IrisBiome> getCaveBiomeStream() {
      return this.caveBiomeStream;
   }

   @Generated
   public ProceduralStream<IrisBiome> getSeaBiomeStream() {
      return this.seaBiomeStream;
   }

   @Generated
   public ProceduralStream<IrisBiome> getShoreBiomeStream() {
      return this.shoreBiomeStream;
   }

   @Generated
   public ProceduralStream<IrisBiome> getBaseBiomeStream() {
      return this.baseBiomeStream;
   }

   @Generated
   public ProceduralStream<UUID> getBaseBiomeIDStream() {
      return this.baseBiomeIDStream;
   }

   @Generated
   public ProceduralStream<IrisBiome> getTrueBiomeStream() {
      return this.trueBiomeStream;
   }

   @Generated
   public ProceduralStream<Biome> getTrueBiomeDerivativeStream() {
      return this.trueBiomeDerivativeStream;
   }

   @Generated
   public ProceduralStream<Double> getHeightStream() {
      return this.heightStream;
   }

   @Generated
   public ProceduralStream<Integer> getRoundedHeighteightStream() {
      return this.roundedHeighteightStream;
   }

   @Generated
   public ProceduralStream<Double> getMaxHeightStream() {
      return this.maxHeightStream;
   }

   @Generated
   public ProceduralStream<Double> getOverlayStream() {
      return this.overlayStream;
   }

   @Generated
   public ProceduralStream<Double> getHeightFluidStream() {
      return this.heightFluidStream;
   }

   @Generated
   public ProceduralStream<Double> getSlopeStream() {
      return this.slopeStream;
   }

   @Generated
   public ProceduralStream<Integer> getTopSurfaceStream() {
      return this.topSurfaceStream;
   }

   @Generated
   public ProceduralStream<IrisDecorator> getTerrainSurfaceDecoration() {
      return this.terrainSurfaceDecoration;
   }

   @Generated
   public ProceduralStream<IrisDecorator> getTerrainCeilingDecoration() {
      return this.terrainCeilingDecoration;
   }

   @Generated
   public ProceduralStream<IrisDecorator> getTerrainCaveSurfaceDecoration() {
      return this.terrainCaveSurfaceDecoration;
   }

   @Generated
   public ProceduralStream<IrisDecorator> getTerrainCaveCeilingDecoration() {
      return this.terrainCaveCeilingDecoration;
   }

   @Generated
   public ProceduralStream<IrisDecorator> getSeaSurfaceDecoration() {
      return this.seaSurfaceDecoration;
   }

   @Generated
   public ProceduralStream<IrisDecorator> getSeaFloorDecoration() {
      return this.seaFloorDecoration;
   }

   @Generated
   public ProceduralStream<IrisDecorator> getShoreSurfaceDecoration() {
      return this.shoreSurfaceDecoration;
   }

   @Generated
   public ProceduralStream<BlockData> getRockStream() {
      return this.rockStream;
   }

   @Generated
   public ProceduralStream<BlockData> getFluidStream() {
      return this.fluidStream;
   }

   @Generated
   public IrisBiome getFocusBiome() {
      return this.focusBiome;
   }

   @Generated
   public IrisRegion getFocusRegion() {
      return this.focusRegion;
   }

   @Generated
   public void setRng(final RNG rng) {
      this.rng = var1;
   }

   @Generated
   public void setFluidHeight(final double fluidHeight) {
      this.fluidHeight = var1;
   }

   @Generated
   public void setData(final IrisData data) {
      this.data = var1;
   }

   @Generated
   public void setGenerators(final Map<IrisInterpolator, Set<IrisGenerator>> generators) {
      this.generators = var1;
   }

   @Generated
   public void setRegionStream(final ProceduralStream<IrisRegion> regionStream) {
      this.regionStream = var1;
   }

   @Generated
   public void setRegionStyleStream(final ProceduralStream<Double> regionStyleStream) {
      this.regionStyleStream = var1;
   }

   @Generated
   public void setRegionIdentityStream(final ProceduralStream<Double> regionIdentityStream) {
      this.regionIdentityStream = var1;
   }

   @Generated
   public void setRegionIDStream(final ProceduralStream<UUID> regionIDStream) {
      this.regionIDStream = var1;
   }

   @Generated
   public void setBridgeStream(final ProceduralStream<InferredType> bridgeStream) {
      this.bridgeStream = var1;
   }

   @Generated
   public void setLandBiomeStream(final ProceduralStream<IrisBiome> landBiomeStream) {
      this.landBiomeStream = var1;
   }

   @Generated
   public void setCaveBiomeStream(final ProceduralStream<IrisBiome> caveBiomeStream) {
      this.caveBiomeStream = var1;
   }

   @Generated
   public void setSeaBiomeStream(final ProceduralStream<IrisBiome> seaBiomeStream) {
      this.seaBiomeStream = var1;
   }

   @Generated
   public void setShoreBiomeStream(final ProceduralStream<IrisBiome> shoreBiomeStream) {
      this.shoreBiomeStream = var1;
   }

   @Generated
   public void setBaseBiomeStream(final ProceduralStream<IrisBiome> baseBiomeStream) {
      this.baseBiomeStream = var1;
   }

   @Generated
   public void setBaseBiomeIDStream(final ProceduralStream<UUID> baseBiomeIDStream) {
      this.baseBiomeIDStream = var1;
   }

   @Generated
   public void setTrueBiomeStream(final ProceduralStream<IrisBiome> trueBiomeStream) {
      this.trueBiomeStream = var1;
   }

   @Generated
   public void setTrueBiomeDerivativeStream(final ProceduralStream<Biome> trueBiomeDerivativeStream) {
      this.trueBiomeDerivativeStream = var1;
   }

   @Generated
   public void setHeightStream(final ProceduralStream<Double> heightStream) {
      this.heightStream = var1;
   }

   @Generated
   public void setRoundedHeighteightStream(final ProceduralStream<Integer> roundedHeighteightStream) {
      this.roundedHeighteightStream = var1;
   }

   @Generated
   public void setMaxHeightStream(final ProceduralStream<Double> maxHeightStream) {
      this.maxHeightStream = var1;
   }

   @Generated
   public void setOverlayStream(final ProceduralStream<Double> overlayStream) {
      this.overlayStream = var1;
   }

   @Generated
   public void setHeightFluidStream(final ProceduralStream<Double> heightFluidStream) {
      this.heightFluidStream = var1;
   }

   @Generated
   public void setSlopeStream(final ProceduralStream<Double> slopeStream) {
      this.slopeStream = var1;
   }

   @Generated
   public void setTopSurfaceStream(final ProceduralStream<Integer> topSurfaceStream) {
      this.topSurfaceStream = var1;
   }

   @Generated
   public void setTerrainSurfaceDecoration(final ProceduralStream<IrisDecorator> terrainSurfaceDecoration) {
      this.terrainSurfaceDecoration = var1;
   }

   @Generated
   public void setTerrainCeilingDecoration(final ProceduralStream<IrisDecorator> terrainCeilingDecoration) {
      this.terrainCeilingDecoration = var1;
   }

   @Generated
   public void setTerrainCaveSurfaceDecoration(final ProceduralStream<IrisDecorator> terrainCaveSurfaceDecoration) {
      this.terrainCaveSurfaceDecoration = var1;
   }

   @Generated
   public void setTerrainCaveCeilingDecoration(final ProceduralStream<IrisDecorator> terrainCaveCeilingDecoration) {
      this.terrainCaveCeilingDecoration = var1;
   }

   @Generated
   public void setSeaSurfaceDecoration(final ProceduralStream<IrisDecorator> seaSurfaceDecoration) {
      this.seaSurfaceDecoration = var1;
   }

   @Generated
   public void setSeaFloorDecoration(final ProceduralStream<IrisDecorator> seaFloorDecoration) {
      this.seaFloorDecoration = var1;
   }

   @Generated
   public void setShoreSurfaceDecoration(final ProceduralStream<IrisDecorator> shoreSurfaceDecoration) {
      this.shoreSurfaceDecoration = var1;
   }

   @Generated
   public void setRockStream(final ProceduralStream<BlockData> rockStream) {
      this.rockStream = var1;
   }

   @Generated
   public void setFluidStream(final ProceduralStream<BlockData> fluidStream) {
      this.fluidStream = var1;
   }

   @Generated
   public void setFocusBiome(final IrisBiome focusBiome) {
      this.focusBiome = var1;
   }

   @Generated
   public void setFocusRegion(final IrisRegion focusRegion) {
      this.focusRegion = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisComplex)) {
         return false;
      } else {
         IrisComplex var2 = (IrisComplex)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (Double.compare(this.getFluidHeight(), var2.getFluidHeight()) != 0) {
            return false;
         } else {
            label409: {
               RNG var3 = this.getRng();
               RNG var4 = var2.getRng();
               if (var3 == null) {
                  if (var4 == null) {
                     break label409;
                  }
               } else if (var3.equals(var4)) {
                  break label409;
               }

               return false;
            }

            Map var5 = this.getGenerators();
            Map var6 = var2.getGenerators();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            label395: {
               ProceduralStream var7 = this.getRegionStream();
               ProceduralStream var8 = var2.getRegionStream();
               if (var7 == null) {
                  if (var8 == null) {
                     break label395;
                  }
               } else if (var7.equals(var8)) {
                  break label395;
               }

               return false;
            }

            ProceduralStream var9 = this.getRegionStyleStream();
            ProceduralStream var10 = var2.getRegionStyleStream();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            ProceduralStream var11 = this.getRegionIdentityStream();
            ProceduralStream var12 = var2.getRegionIdentityStream();
            if (var11 == null) {
               if (var12 != null) {
                  return false;
               }
            } else if (!var11.equals(var12)) {
               return false;
            }

            label374: {
               ProceduralStream var13 = this.getRegionIDStream();
               ProceduralStream var14 = var2.getRegionIDStream();
               if (var13 == null) {
                  if (var14 == null) {
                     break label374;
                  }
               } else if (var13.equals(var14)) {
                  break label374;
               }

               return false;
            }

            label367: {
               ProceduralStream var15 = this.getBridgeStream();
               ProceduralStream var16 = var2.getBridgeStream();
               if (var15 == null) {
                  if (var16 == null) {
                     break label367;
                  }
               } else if (var15.equals(var16)) {
                  break label367;
               }

               return false;
            }

            ProceduralStream var17 = this.getLandBiomeStream();
            ProceduralStream var18 = var2.getLandBiomeStream();
            if (var17 == null) {
               if (var18 != null) {
                  return false;
               }
            } else if (!var17.equals(var18)) {
               return false;
            }

            ProceduralStream var19 = this.getCaveBiomeStream();
            ProceduralStream var20 = var2.getCaveBiomeStream();
            if (var19 == null) {
               if (var20 != null) {
                  return false;
               }
            } else if (!var19.equals(var20)) {
               return false;
            }

            label346: {
               ProceduralStream var21 = this.getSeaBiomeStream();
               ProceduralStream var22 = var2.getSeaBiomeStream();
               if (var21 == null) {
                  if (var22 == null) {
                     break label346;
                  }
               } else if (var21.equals(var22)) {
                  break label346;
               }

               return false;
            }

            label339: {
               ProceduralStream var23 = this.getShoreBiomeStream();
               ProceduralStream var24 = var2.getShoreBiomeStream();
               if (var23 == null) {
                  if (var24 == null) {
                     break label339;
                  }
               } else if (var23.equals(var24)) {
                  break label339;
               }

               return false;
            }

            ProceduralStream var25 = this.getBaseBiomeStream();
            ProceduralStream var26 = var2.getBaseBiomeStream();
            if (var25 == null) {
               if (var26 != null) {
                  return false;
               }
            } else if (!var25.equals(var26)) {
               return false;
            }

            ProceduralStream var27 = this.getBaseBiomeIDStream();
            ProceduralStream var28 = var2.getBaseBiomeIDStream();
            if (var27 == null) {
               if (var28 != null) {
                  return false;
               }
            } else if (!var27.equals(var28)) {
               return false;
            }

            label318: {
               ProceduralStream var29 = this.getTrueBiomeStream();
               ProceduralStream var30 = var2.getTrueBiomeStream();
               if (var29 == null) {
                  if (var30 == null) {
                     break label318;
                  }
               } else if (var29.equals(var30)) {
                  break label318;
               }

               return false;
            }

            label311: {
               ProceduralStream var31 = this.getTrueBiomeDerivativeStream();
               ProceduralStream var32 = var2.getTrueBiomeDerivativeStream();
               if (var31 == null) {
                  if (var32 == null) {
                     break label311;
                  }
               } else if (var31.equals(var32)) {
                  break label311;
               }

               return false;
            }

            ProceduralStream var33 = this.getHeightStream();
            ProceduralStream var34 = var2.getHeightStream();
            if (var33 == null) {
               if (var34 != null) {
                  return false;
               }
            } else if (!var33.equals(var34)) {
               return false;
            }

            label297: {
               ProceduralStream var35 = this.getRoundedHeighteightStream();
               ProceduralStream var36 = var2.getRoundedHeighteightStream();
               if (var35 == null) {
                  if (var36 == null) {
                     break label297;
                  }
               } else if (var35.equals(var36)) {
                  break label297;
               }

               return false;
            }

            ProceduralStream var37 = this.getMaxHeightStream();
            ProceduralStream var38 = var2.getMaxHeightStream();
            if (var37 == null) {
               if (var38 != null) {
                  return false;
               }
            } else if (!var37.equals(var38)) {
               return false;
            }

            label283: {
               ProceduralStream var39 = this.getOverlayStream();
               ProceduralStream var40 = var2.getOverlayStream();
               if (var39 == null) {
                  if (var40 == null) {
                     break label283;
                  }
               } else if (var39.equals(var40)) {
                  break label283;
               }

               return false;
            }

            ProceduralStream var41 = this.getHeightFluidStream();
            ProceduralStream var42 = var2.getHeightFluidStream();
            if (var41 == null) {
               if (var42 != null) {
                  return false;
               }
            } else if (!var41.equals(var42)) {
               return false;
            }

            ProceduralStream var43 = this.getSlopeStream();
            ProceduralStream var44 = var2.getSlopeStream();
            if (var43 == null) {
               if (var44 != null) {
                  return false;
               }
            } else if (!var43.equals(var44)) {
               return false;
            }

            label262: {
               ProceduralStream var45 = this.getTopSurfaceStream();
               ProceduralStream var46 = var2.getTopSurfaceStream();
               if (var45 == null) {
                  if (var46 == null) {
                     break label262;
                  }
               } else if (var45.equals(var46)) {
                  break label262;
               }

               return false;
            }

            label255: {
               ProceduralStream var47 = this.getTerrainSurfaceDecoration();
               ProceduralStream var48 = var2.getTerrainSurfaceDecoration();
               if (var47 == null) {
                  if (var48 == null) {
                     break label255;
                  }
               } else if (var47.equals(var48)) {
                  break label255;
               }

               return false;
            }

            ProceduralStream var49 = this.getTerrainCeilingDecoration();
            ProceduralStream var50 = var2.getTerrainCeilingDecoration();
            if (var49 == null) {
               if (var50 != null) {
                  return false;
               }
            } else if (!var49.equals(var50)) {
               return false;
            }

            ProceduralStream var51 = this.getTerrainCaveSurfaceDecoration();
            ProceduralStream var52 = var2.getTerrainCaveSurfaceDecoration();
            if (var51 == null) {
               if (var52 != null) {
                  return false;
               }
            } else if (!var51.equals(var52)) {
               return false;
            }

            label234: {
               ProceduralStream var53 = this.getTerrainCaveCeilingDecoration();
               ProceduralStream var54 = var2.getTerrainCaveCeilingDecoration();
               if (var53 == null) {
                  if (var54 == null) {
                     break label234;
                  }
               } else if (var53.equals(var54)) {
                  break label234;
               }

               return false;
            }

            label227: {
               ProceduralStream var55 = this.getSeaSurfaceDecoration();
               ProceduralStream var56 = var2.getSeaSurfaceDecoration();
               if (var55 == null) {
                  if (var56 == null) {
                     break label227;
                  }
               } else if (var55.equals(var56)) {
                  break label227;
               }

               return false;
            }

            ProceduralStream var57 = this.getSeaFloorDecoration();
            ProceduralStream var58 = var2.getSeaFloorDecoration();
            if (var57 == null) {
               if (var58 != null) {
                  return false;
               }
            } else if (!var57.equals(var58)) {
               return false;
            }

            ProceduralStream var59 = this.getShoreSurfaceDecoration();
            ProceduralStream var60 = var2.getShoreSurfaceDecoration();
            if (var59 == null) {
               if (var60 != null) {
                  return false;
               }
            } else if (!var59.equals(var60)) {
               return false;
            }

            label206: {
               ProceduralStream var61 = this.getRockStream();
               ProceduralStream var62 = var2.getRockStream();
               if (var61 == null) {
                  if (var62 == null) {
                     break label206;
                  }
               } else if (var61.equals(var62)) {
                  break label206;
               }

               return false;
            }

            label199: {
               ProceduralStream var63 = this.getFluidStream();
               ProceduralStream var64 = var2.getFluidStream();
               if (var63 == null) {
                  if (var64 == null) {
                     break label199;
                  }
               } else if (var63.equals(var64)) {
                  break label199;
               }

               return false;
            }

            IrisBiome var65 = this.getFocusBiome();
            IrisBiome var66 = var2.getFocusBiome();
            if (var65 == null) {
               if (var66 != null) {
                  return false;
               }
            } else if (!var65.equals(var66)) {
               return false;
            }

            IrisRegion var67 = this.getFocusRegion();
            IrisRegion var68 = var2.getFocusRegion();
            if (var67 == null) {
               if (var68 != null) {
                  return false;
               }
            } else if (!var67.equals(var68)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisComplex;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = Double.doubleToLongBits(this.getFluidHeight());
      int var38 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      RNG var5 = this.getRng();
      var38 = var38 * 59 + (var5 == null ? 43 : var5.hashCode());
      Map var6 = this.getGenerators();
      var38 = var38 * 59 + (var6 == null ? 43 : var6.hashCode());
      ProceduralStream var7 = this.getRegionStream();
      var38 = var38 * 59 + (var7 == null ? 43 : var7.hashCode());
      ProceduralStream var8 = this.getRegionStyleStream();
      var38 = var38 * 59 + (var8 == null ? 43 : var8.hashCode());
      ProceduralStream var9 = this.getRegionIdentityStream();
      var38 = var38 * 59 + (var9 == null ? 43 : var9.hashCode());
      ProceduralStream var10 = this.getRegionIDStream();
      var38 = var38 * 59 + (var10 == null ? 43 : var10.hashCode());
      ProceduralStream var11 = this.getBridgeStream();
      var38 = var38 * 59 + (var11 == null ? 43 : var11.hashCode());
      ProceduralStream var12 = this.getLandBiomeStream();
      var38 = var38 * 59 + (var12 == null ? 43 : var12.hashCode());
      ProceduralStream var13 = this.getCaveBiomeStream();
      var38 = var38 * 59 + (var13 == null ? 43 : var13.hashCode());
      ProceduralStream var14 = this.getSeaBiomeStream();
      var38 = var38 * 59 + (var14 == null ? 43 : var14.hashCode());
      ProceduralStream var15 = this.getShoreBiomeStream();
      var38 = var38 * 59 + (var15 == null ? 43 : var15.hashCode());
      ProceduralStream var16 = this.getBaseBiomeStream();
      var38 = var38 * 59 + (var16 == null ? 43 : var16.hashCode());
      ProceduralStream var17 = this.getBaseBiomeIDStream();
      var38 = var38 * 59 + (var17 == null ? 43 : var17.hashCode());
      ProceduralStream var18 = this.getTrueBiomeStream();
      var38 = var38 * 59 + (var18 == null ? 43 : var18.hashCode());
      ProceduralStream var19 = this.getTrueBiomeDerivativeStream();
      var38 = var38 * 59 + (var19 == null ? 43 : var19.hashCode());
      ProceduralStream var20 = this.getHeightStream();
      var38 = var38 * 59 + (var20 == null ? 43 : var20.hashCode());
      ProceduralStream var21 = this.getRoundedHeighteightStream();
      var38 = var38 * 59 + (var21 == null ? 43 : var21.hashCode());
      ProceduralStream var22 = this.getMaxHeightStream();
      var38 = var38 * 59 + (var22 == null ? 43 : var22.hashCode());
      ProceduralStream var23 = this.getOverlayStream();
      var38 = var38 * 59 + (var23 == null ? 43 : var23.hashCode());
      ProceduralStream var24 = this.getHeightFluidStream();
      var38 = var38 * 59 + (var24 == null ? 43 : var24.hashCode());
      ProceduralStream var25 = this.getSlopeStream();
      var38 = var38 * 59 + (var25 == null ? 43 : var25.hashCode());
      ProceduralStream var26 = this.getTopSurfaceStream();
      var38 = var38 * 59 + (var26 == null ? 43 : var26.hashCode());
      ProceduralStream var27 = this.getTerrainSurfaceDecoration();
      var38 = var38 * 59 + (var27 == null ? 43 : var27.hashCode());
      ProceduralStream var28 = this.getTerrainCeilingDecoration();
      var38 = var38 * 59 + (var28 == null ? 43 : var28.hashCode());
      ProceduralStream var29 = this.getTerrainCaveSurfaceDecoration();
      var38 = var38 * 59 + (var29 == null ? 43 : var29.hashCode());
      ProceduralStream var30 = this.getTerrainCaveCeilingDecoration();
      var38 = var38 * 59 + (var30 == null ? 43 : var30.hashCode());
      ProceduralStream var31 = this.getSeaSurfaceDecoration();
      var38 = var38 * 59 + (var31 == null ? 43 : var31.hashCode());
      ProceduralStream var32 = this.getSeaFloorDecoration();
      var38 = var38 * 59 + (var32 == null ? 43 : var32.hashCode());
      ProceduralStream var33 = this.getShoreSurfaceDecoration();
      var38 = var38 * 59 + (var33 == null ? 43 : var33.hashCode());
      ProceduralStream var34 = this.getRockStream();
      var38 = var38 * 59 + (var34 == null ? 43 : var34.hashCode());
      ProceduralStream var35 = this.getFluidStream();
      var38 = var38 * 59 + (var35 == null ? 43 : var35.hashCode());
      IrisBiome var36 = this.getFocusBiome();
      var38 = var38 * 59 + (var36 == null ? 43 : var36.hashCode());
      IrisRegion var37 = this.getFocusRegion();
      var38 = var38 * 59 + (var37 == null ? 43 : var37.hashCode());
      return var38;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getRng());
      return "IrisComplex(rng=" + var10000 + ", fluidHeight=" + this.getFluidHeight() + ", generators=" + String.valueOf(this.getGenerators()) + ", regionStream=" + String.valueOf(this.getRegionStream()) + ", regionStyleStream=" + String.valueOf(this.getRegionStyleStream()) + ", regionIdentityStream=" + String.valueOf(this.getRegionIdentityStream()) + ", regionIDStream=" + String.valueOf(this.getRegionIDStream()) + ", bridgeStream=" + String.valueOf(this.getBridgeStream()) + ", landBiomeStream=" + String.valueOf(this.getLandBiomeStream()) + ", caveBiomeStream=" + String.valueOf(this.getCaveBiomeStream()) + ", seaBiomeStream=" + String.valueOf(this.getSeaBiomeStream()) + ", shoreBiomeStream=" + String.valueOf(this.getShoreBiomeStream()) + ", baseBiomeStream=" + String.valueOf(this.getBaseBiomeStream()) + ", baseBiomeIDStream=" + String.valueOf(this.getBaseBiomeIDStream()) + ", trueBiomeStream=" + String.valueOf(this.getTrueBiomeStream()) + ", trueBiomeDerivativeStream=" + String.valueOf(this.getTrueBiomeDerivativeStream()) + ", heightStream=" + String.valueOf(this.getHeightStream()) + ", roundedHeighteightStream=" + String.valueOf(this.getRoundedHeighteightStream()) + ", maxHeightStream=" + String.valueOf(this.getMaxHeightStream()) + ", overlayStream=" + String.valueOf(this.getOverlayStream()) + ", heightFluidStream=" + String.valueOf(this.getHeightFluidStream()) + ", slopeStream=" + String.valueOf(this.getSlopeStream()) + ", topSurfaceStream=" + String.valueOf(this.getTopSurfaceStream()) + ", terrainSurfaceDecoration=" + String.valueOf(this.getTerrainSurfaceDecoration()) + ", terrainCeilingDecoration=" + String.valueOf(this.getTerrainCeilingDecoration()) + ", terrainCaveSurfaceDecoration=" + String.valueOf(this.getTerrainCaveSurfaceDecoration()) + ", terrainCaveCeilingDecoration=" + String.valueOf(this.getTerrainCaveCeilingDecoration()) + ", seaSurfaceDecoration=" + String.valueOf(this.getSeaSurfaceDecoration()) + ", seaFloorDecoration=" + String.valueOf(this.getSeaFloorDecoration()) + ", shoreSurfaceDecoration=" + String.valueOf(this.getShoreSurfaceDecoration()) + ", rockStream=" + String.valueOf(this.getRockStream()) + ", fluidStream=" + String.valueOf(this.getFluidStream()) + ", focusBiome=" + String.valueOf(this.getFocusBiome()) + ", focusRegion=" + String.valueOf(this.getFocusRegion()) + ")";
   }

   static {
      AIR = Material.AIR.createBlockData();
   }
}
