package com.volmit.iris.util.stream;

import com.volmit.iris.Iris;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.data.cache.Cache;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.IRare;
import com.volmit.iris.engine.object.IrisStyledRange;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.context.ChunkContext;
import com.volmit.iris.util.function.Function2;
import com.volmit.iris.util.function.Function3;
import com.volmit.iris.util.function.Function4;
import com.volmit.iris.util.hunk.Hunk;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.parallel.BurstExecutor;
import com.volmit.iris.util.parallel.MultiBurst;
import com.volmit.iris.util.stream.arithmetic.AddingStream;
import com.volmit.iris.util.stream.arithmetic.ClampedStream;
import com.volmit.iris.util.stream.arithmetic.CoordinateBitShiftLeftStream;
import com.volmit.iris.util.stream.arithmetic.CoordinateBitShiftRightStream;
import com.volmit.iris.util.stream.arithmetic.DividingStream;
import com.volmit.iris.util.stream.arithmetic.FittedStream;
import com.volmit.iris.util.stream.arithmetic.MaxingStream;
import com.volmit.iris.util.stream.arithmetic.MinningStream;
import com.volmit.iris.util.stream.arithmetic.ModuloStream;
import com.volmit.iris.util.stream.arithmetic.MultiplyingStream;
import com.volmit.iris.util.stream.arithmetic.OffsetStream;
import com.volmit.iris.util.stream.arithmetic.RadialStream;
import com.volmit.iris.util.stream.arithmetic.RoundingDoubleStream;
import com.volmit.iris.util.stream.arithmetic.SlopeStream;
import com.volmit.iris.util.stream.arithmetic.SubtractingStream;
import com.volmit.iris.util.stream.arithmetic.ZoomStream;
import com.volmit.iris.util.stream.convert.AwareConversionStream2D;
import com.volmit.iris.util.stream.convert.AwareConversionStream3D;
import com.volmit.iris.util.stream.convert.CachedConversionStream;
import com.volmit.iris.util.stream.convert.ConversionStream;
import com.volmit.iris.util.stream.convert.ForceDoubleStream;
import com.volmit.iris.util.stream.convert.RoundingStream;
import com.volmit.iris.util.stream.convert.SelectionStream;
import com.volmit.iris.util.stream.convert.SignificanceStream;
import com.volmit.iris.util.stream.convert.To3DStream;
import com.volmit.iris.util.stream.interpolation.Interpolated;
import com.volmit.iris.util.stream.sources.FunctionStream;
import com.volmit.iris.util.stream.utility.CachedStream2D;
import com.volmit.iris.util.stream.utility.CachedStream3D;
import com.volmit.iris.util.stream.utility.ContextInjectingStream;
import com.volmit.iris.util.stream.utility.NullSafeStream;
import com.volmit.iris.util.stream.utility.ProfiledStream;
import com.volmit.iris.util.stream.utility.SemaphoreStream;
import com.volmit.iris.util.stream.utility.SynchronizedStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public interface ProceduralStream<T> extends ProceduralLayer, Interpolated<T> {
   static ProceduralStream<Double> ofDouble(Function2<Double, Double, Double> f) {
      try {
         return of(f, Interpolated.DOUBLE);
      } catch (IncompatibleClassChangeError var2) {
         Iris.warn(f.toString());
         Iris.reportError(var2);
         var2.printStackTrace();
         return null;
      }
   }

   static ProceduralStream<Double> ofDouble(Function3<Double, Double, Double, Double> f) {
      return of(f, Interpolated.DOUBLE);
   }

   static <T> ProceduralStream<T> of(Function2<Double, Double, T> f, Interpolated<T> helper) {
      return of(f, (x, y, z) -> {
         return f.apply(x, z);
      }, helper);
   }

   static <T> ProceduralStream<T> of(Function3<Double, Double, Double, T> f, Interpolated<T> helper) {
      return of((x, z) -> {
         return f.apply(x, 0.0D, z);
      }, f, helper);
   }

   static <T> ProceduralStream<T> of(Function2<Double, Double, T> f, Function3<Double, Double, Double, T> f2, Interpolated<T> helper) {
      return new FunctionStream(f, f2, helper);
   }

   default ProceduralStream<Boolean> chance(double chance) {
      return of((x, z) -> {
         return this.getDouble(x, z) < chance;
      }, Interpolated.BOOLEAN);
   }

   default ProceduralStream<Boolean> seededChance(RNG brng, long rootSeed, double chance) {
      RNG rng = brng.nextParallelRNG(rootSeed - 3995L);
      return of((x, z) -> {
         double ch = this.getDouble(x, z);
         rng.setSeed((long)(ch * 9.223372036854776E18D));
         return rng.chance(chance);
      }, Interpolated.BOOLEAN);
   }

   default ProceduralStream<T> profile() {
      return this.profile(256);
   }

   default ProceduralStream<T> profile(int memory) {
      return new ProfiledStream(this, memory);
   }

   default ProceduralStream<T> onNull(T v) {
      return new NullSafeStream(this, v);
   }

   default ProceduralStream<T> add(Function3<Double, Double, Double, Double> a) {
      return new AddingStream(this, a);
   }

   default ProceduralStream<T> add(Function2<Double, Double, Double> a) {
      return new AddingStream(this, a);
   }

   default ProceduralStream<T> contextInjecting(Function3<ChunkContext, Integer, Integer, T> contextAccessor) {
      return new ContextInjectingStream(this, contextAccessor);
   }

   default ProceduralStream<T> add(ProceduralStream<Double> a) {
      return this.add2D((x, z) -> {
         return (Double)a.get(x, z);
      });
   }

   default ProceduralStream<T> waste(String name) {
      return this;
   }

   default ProceduralStream<T> subtract(ProceduralStream<Double> a) {
      return this.subtract2D((x, z) -> {
         return (Double)a.get(x, z);
      });
   }

   default ProceduralStream<T> add2D(Function2<Double, Double, Double> a) {
      return new AddingStream(this, a);
   }

   default ProceduralStream<T> subtract2D(Function2<Double, Double, Double> a) {
      return new SubtractingStream(this, a);
   }

   default ProceduralStream<T> add(double a) {
      return new AddingStream(this, a);
   }

   default ProceduralStream<T> blockToChunkCoords() {
      return this.bitShiftCoordsRight(4);
   }

   default ProceduralStream<T> chunkToRegionCoords() {
      return this.bitShiftCoordsRight(5);
   }

   default ProceduralStream<T> blockToRegionCoords() {
      return this.blockToChunkCoords().chunkToRegionCoords();
   }

   default ProceduralStream<T> regionToBlockCoords() {
      return this.regionToChunkCoords().chunkToBlockCoords();
   }

   default ProceduralStream<T> regionToChunkCoords() {
      return this.bitShiftCoordsLeft(5);
   }

   default ProceduralStream<T> chunkToBlockCoords() {
      return this.bitShiftCoordsLeft(4);
   }

   default ProceduralStream<T> bitShiftCoordsRight(int a) {
      return new CoordinateBitShiftRightStream(this, a);
   }

   default ProceduralStream<T> synchronize() {
      return new SynchronizedStream(this);
   }

   default ProceduralStream<T> semaphore(int permits) {
      return new SemaphoreStream(this, permits);
   }

   default ProceduralStream<T> bitShiftCoordsLeft(int a) {
      return new CoordinateBitShiftLeftStream(this, a);
   }

   default ProceduralStream<T> max(Function3<Double, Double, Double, Double> a) {
      return new MaxingStream(this, a);
   }

   default ProceduralStream<T> max(Function2<Double, Double, Double> a) {
      return new MaxingStream(this, a);
   }

   default ProceduralStream<T> slope() {
      return this.slope(1);
   }

   default ProceduralStream<T> slope(int range) {
      return new SlopeStream(this, range);
   }

   default ProceduralStream<T> max(double a) {
      return new MaxingStream(this, a);
   }

   default ProceduralStream<T> min(Function3<Double, Double, Double, Double> a) {
      return new MinningStream(this, a);
   }

   default ProceduralStream<T> min(Function2<Double, Double, Double> a) {
      return new MinningStream(this, a);
   }

   default ProceduralStream<T> min(double a) {
      return new MinningStream(this, a);
   }

   default ProceduralStream<T> subtract(Function3<Double, Double, Double, Double> a) {
      return new SubtractingStream(this, a);
   }

   default ProceduralStream<T> subtract(Function2<Double, Double, Double> a) {
      return new SubtractingStream(this, a);
   }

   default ProceduralStream<T> subtract(double a) {
      return new SubtractingStream(this, a);
   }

   default ProceduralStream<T> multiply(Function3<Double, Double, Double, Double> a) {
      return new MultiplyingStream(this, a);
   }

   default ProceduralStream<T> multiply(Function2<Double, Double, Double> a) {
      return new MultiplyingStream(this, a);
   }

   default ProceduralStream<T> multiply(double a) {
      return new MultiplyingStream(this, a);
   }

   default ProceduralStream<T> divide(Function3<Double, Double, Double, Double> a) {
      return new DividingStream(this, a);
   }

   default ProceduralStream<T> divide(Function2<Double, Double, Double> a) {
      return new DividingStream(this, a);
   }

   default ProceduralStream<T> divide(double a) {
      return new DividingStream(this, a);
   }

   default ProceduralStream<T> modulo(Function3<Double, Double, Double, Double> a) {
      return new ModuloStream(this, a);
   }

   default ProceduralStream<T> modulo(Function2<Double, Double, Double> a) {
      return new ModuloStream(this, a);
   }

   default ProceduralStream<T> modulo(double a) {
      return new ModuloStream(this, a);
   }

   default ProceduralStream<Integer> round() {
      return new RoundingStream(this);
   }

   default ProceduralStream<Double> roundDouble() {
      return new RoundingDoubleStream(this);
   }

   default ProceduralStream<Double> forceDouble() {
      return new ForceDoubleStream(this);
   }

   default ProceduralStream<Significance<T>> significance(double radius, int checks) {
      return new SignificanceStream(this, radius, checks);
   }

   default ProceduralStream<T> to3D() {
      return new To3DStream(this);
   }

   default CachedStream2D<T> cache2D(String name, Engine engine, int size) {
      return new CachedStream2D(name, engine, this, size);
   }

   default ProceduralStream<T> cache3D(String name, Engine engine, int maxSize) {
      return new CachedStream3D(name, engine, this, maxSize);
   }

   default <V> ProceduralStream<V> convert(Function<T, V> converter) {
      return new ConversionStream(this, converter);
   }

   default <V> ProceduralStream<V> convertAware2D(Function3<T, Double, Double, V> converter) {
      return new AwareConversionStream2D(this, converter);
   }

   default <V> ProceduralStream<V> convertAware3D(Function4<T, Double, Double, Double, V> converter) {
      return new AwareConversionStream3D(this, converter);
   }

   default <V> ProceduralStream<V> convertCached(Function<T, V> converter) {
      return new CachedConversionStream(this, converter);
   }

   default ProceduralStream<T> offset(double x, double y, double z) {
      return new OffsetStream(this, x, y, z);
   }

   default ProceduralStream<T> offset(double x, double z) {
      return new OffsetStream(this, x, 0.0D, z);
   }

   default ProceduralStream<T> zoom(double x, double y, double z) {
      return new ZoomStream(this, x, y, z);
   }

   default ProceduralStream<T> zoom(double x, double z) {
      return new ZoomStream(this, x, 1.0D, z);
   }

   default ProceduralStream<T> zoom(double all) {
      return new ZoomStream(this, all, all, all);
   }

   default ProceduralStream<T> radial(double scale) {
      return new RadialStream(this, scale);
   }

   default ProceduralStream<T> radial() {
      return this.radial(1.0D);
   }

   default <V> ProceduralStream<V> select(V... types) {
      return new SelectionStream(this, types);
   }

   default <V> ProceduralStream<V> select(List<V> types) {
      return new SelectionStream(this, types);
   }

   /** @deprecated */
   @Deprecated(
      forRemoval = true
   )
   default <V> ProceduralStream<V> selectRarity(V... types) {
      KList<V> rarityTypes = new KList();
      int totalRarity = 0;
      Object[] var4 = types;
      int var5 = types.length;

      int var6;
      Object i;
      for(var6 = 0; var6 < var5; ++var6) {
         i = var4[var6];
         totalRarity += IRare.get(i);
      }

      var4 = types;
      var5 = types.length;

      for(var6 = 0; var6 < var5; ++var6) {
         i = var4[var6];
         rarityTypes.addMultiple(i, totalRarity / IRare.get(i));
      }

      return new SelectionStream(this, rarityTypes);
   }

   default <V extends IRare> ProceduralStream<V> selectRarity(List<V> types, boolean legacy) {
      return IRare.stream(this.forceDouble(), types, legacy);
   }

   default <V> ProceduralStream<IRare> selectRarity(List<V> types, Function<V, IRare> loader, boolean legacy) {
      List<IRare> r = new ArrayList();
      Iterator var5 = types.iterator();

      while(var5.hasNext()) {
         V f = var5.next();
         r.add((IRare)loader.apply(f));
      }

      return this.selectRarity(r, legacy);
   }

   default <V> int countPossibilities(List<V> types, Function<V, IRare> loader) {
      KList<V> rarityTypes = new KList();
      int totalRarity = 0;

      Iterator var5;
      Object i;
      for(var5 = types.iterator(); var5.hasNext(); totalRarity += IRare.get(loader.apply(i))) {
         i = var5.next();
      }

      var5 = types.iterator();

      while(var5.hasNext()) {
         i = var5.next();
         rarityTypes.addMultiple(i, totalRarity / IRare.get(loader.apply(i)));
      }

      return rarityTypes.size();
   }

   default ProceduralStream<T> clamp(double min, double max) {
      return new ClampedStream(this, min, max);
   }

   default ProceduralStream<T> fit(double min, double max) {
      return new FittedStream(this, min, max);
   }

   default ProceduralStream<Double> style(RNG rng, IrisStyledRange range, IrisData data) {
      return of((x, z) -> {
         double d = this.getDouble(x, z);
         return range.get(rng, d, -d, data);
      }, Interpolated.DOUBLE);
   }

   default Hunk<T> fastFill2DParallel(int x, int z) {
      Hunk<T> hunk = Hunk.newAtomicHunk(16, 16, 1);
      BurstExecutor e = MultiBurst.burst.burst(256);

      for(int i = 0; i < 16; ++i) {
         for(int j = 0; j < 16; ++j) {
            e.queue(() -> {
               hunk.setRaw(i, j, 0, this.get((double)(x + i), (double)(z + j)));
            });
         }
      }

      e.complete();
      return hunk;
   }

   default void fastFill2DParallel(Hunk<T> hunk, BurstExecutor e, int x, int z) {
      for(int i = 0; i < 16; ++i) {
         for(int j = 0; j < 16; ++j) {
            e.queue(() -> {
               hunk.setRaw(i, j, 0, this.get((double)(x + i), (double)(z + j)));
            });
         }
      }

   }

   default Hunk<T> fastFill2D(int x, int z) {
      Hunk<T> hunk = Hunk.newArrayHunk(16, 16, 1);

      for(int i = 0; i < 16; ++i) {
         for(int j = 0; j < 16; ++j) {
            hunk.setRaw(i, j, 0, this.get((double)(x + i), (double)(z + j)));
         }
      }

      return hunk;
   }

   default ProceduralStream<T> fit(double inMin, double inMax, double min, double max) {
      return new FittedStream(this, inMin, inMax, min, max);
   }

   default void fill(Hunk<T> h, double x, double y, double z, int parallelism) {
      h.compute3D(parallelism, (xx, yy, zz, hh) -> {
         hh.iterate((xv, yv, zv) -> {
            hh.set(xv, yv, zv, this.get((double)(xx + xv) + x, (double)(yy + yv) + y, (double)(zz + zv) + z));
         });
      });
   }

   default <V> void fill2D(Hunk<V> h, double x, double z, V v, int parallelism) {
      h.compute2D(parallelism, (xx, __, zz, hh) -> {
         for(int i = 0; i < hh.getWidth(); ++i) {
            for(int k = 0; k < hh.getDepth(); ++k) {
               double n = this.getDouble((double)i + x + (double)xx, (double)k + z + (double)zz);

               for(int j = 0; (double)j < Math.min((double)h.getHeight(), n); ++j) {
                  hh.set(i, j, k, v);
               }
            }
         }

      });
   }

   default <V> void fill2D(Hunk<V> h, double x, double z, ProceduralStream<V> v, int parallelism) {
      h.compute2D(parallelism, (xx, yy, zz, hh) -> {
         for(int i = 0; i < hh.getWidth(); ++i) {
            for(int k = 0; k < hh.getDepth(); ++k) {
               double n = this.getDouble((double)i + x + (double)xx, (double)k + z + (double)zz);

               for(int j = 0; (double)j < Math.min((double)h.getHeight(), n); ++j) {
                  hh.set(i, j, k, v.get((double)i + x + (double)xx, (double)(j + yy), (double)k + z + (double)zz));
               }
            }
         }

      });
   }

   default <V> void fill2DYLocked(Hunk<V> h, double x, double z, V v, int parallelism) {
      h.compute2D(parallelism, (xx, yy, zz, hh) -> {
         for(int i = 0; i < hh.getWidth(); ++i) {
            for(int k = 0; k < hh.getDepth(); ++k) {
               double n = this.getDouble((double)i + x + (double)xx, (double)k + z + (double)zz);

               for(int j = 0; (double)j < Math.min((double)h.getHeight(), n); ++j) {
                  hh.set(i, j, k, v);
               }
            }
         }

      });
   }

   default <V> void fill2DYLocked(Hunk<V> h, double x, double z, ProceduralStream<V> v, int parallelism) {
      h.compute2D(parallelism, (xx, yy, zz, hh) -> {
         for(int i = 0; i < hh.getWidth(); ++i) {
            for(int k = 0; k < hh.getDepth(); ++k) {
               double n = this.getDouble((double)i + x + (double)xx, (double)k + z + (double)zz);

               for(int j = 0; (double)j < Math.min((double)h.getHeight(), n); ++j) {
                  hh.set(i, j, k, v.get((double)i + x + (double)xx, (double)k + z + (double)zz));
               }
            }
         }

      });
   }

   default <V> void fill3D(Hunk<V> h, double x, int y, double z, V v, int parallelism) {
      h.compute3D(parallelism, (xx, yy, zz, hh) -> {
         hh.iterate((xv, yv, zv) -> {
            if (this.getDouble((double)(xx + xv) + x, (double)(yy + yv + y), (double)(zz + zv) + z) > 0.5D) {
               hh.set(xv, yv, zv, v);
            }

         });
      });
   }

   default <V> void fill3D(Hunk<V> h, double x, int y, double z, ProceduralStream<V> v, int parallelism) {
      h.compute3D(parallelism, (xx, yy, zz, hh) -> {
         hh.iterate((xv, yv, zv) -> {
            if (this.getDouble((double)(xx + xv) + x, (double)(yy + yv + y), (double)(zz + zv) + z) > 0.5D) {
               hh.set(xv, yv, zv, v.get((double)(xx + xv) + x, (double)(yy + yv + y), (double)(zz + zv) + z));
            }

         });
      });
   }

   default void fill(Hunk<T> h, double x, double y, double z) {
      this.fill(h, x, z, 4.0D);
   }

   default <V> void fill2D(Hunk<V> h, double x, double z, V v) {
      this.fill2D(h, x, z, (Object)v, 4);
   }

   default <V> void fill2D(Hunk<V> h, double x, double z, ProceduralStream<V> v) {
      this.fill2D(h, x, z, (ProceduralStream)v, 4);
   }

   default <V> void fill2DYLocked(Hunk<V> h, double x, double z, V v) {
      this.fill2DYLocked(h, x, z, (Object)v, 4);
   }

   default <V> void fill2DYLocked(Hunk<V> h, double x, double z, ProceduralStream<V> v) {
      this.fill2DYLocked(h, x, z, (ProceduralStream)v, 4);
   }

   default <V> void fill3D(Hunk<V> h, double x, int y, double z, V v) {
      this.fill3D(h, x, y, z, (Object)v, 4);
   }

   default <V> void fill3D(Hunk<V> h, double x, int y, double z, ProceduralStream<V> v) {
      this.fill3D(h, x, y, z, (ProceduralStream)v, 4);
   }

   default double getDouble(double x, double z) {
      return this.toDouble(this.get(x, z));
   }

   default double getDouble(double x, double y, double z) {
      return this.toDouble(this.get(x, y, z));
   }

   ProceduralStream<T> getTypedSource();

   ProceduralStream<?> getSource();

   default void fillChunk(int x, int z, T[] c) {
      if (c.length != 256) {
         throw new RuntimeException("Not 256 Length for chunk get");
      } else {
         int xs = x << 4;
         int zs = z << 4;

         for(int i = 0; i < 16; ++i) {
            for(int j = 0; j < 16; ++j) {
               c[Cache.to1D(i + xs, j + zs, 0, 16, 16)] = this.get((double)(i + xs), (double)(j + zs));
            }
         }

      }
   }

   T get(double x, double z);

   T get(double x, double y, double z);
}
