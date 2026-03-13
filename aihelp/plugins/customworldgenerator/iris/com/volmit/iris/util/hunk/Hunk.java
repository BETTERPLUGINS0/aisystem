package com.volmit.iris.util.hunk;

import com.volmit.iris.Iris;
import com.volmit.iris.engine.object.IrisPosition;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.function.Consumer2;
import com.volmit.iris.util.function.Consumer3;
import com.volmit.iris.util.function.Consumer4;
import com.volmit.iris.util.function.Consumer4IO;
import com.volmit.iris.util.function.Consumer5;
import com.volmit.iris.util.function.Consumer6;
import com.volmit.iris.util.function.Consumer8;
import com.volmit.iris.util.function.Function3;
import com.volmit.iris.util.function.Function4;
import com.volmit.iris.util.function.NoiseProvider;
import com.volmit.iris.util.function.NoiseProvider3;
import com.volmit.iris.util.function.Supplier3R;
import com.volmit.iris.util.hunk.storage.ArrayHunk;
import com.volmit.iris.util.hunk.storage.AtomicDoubleHunk;
import com.volmit.iris.util.hunk.storage.AtomicHunk;
import com.volmit.iris.util.hunk.storage.AtomicIntegerHunk;
import com.volmit.iris.util.hunk.storage.AtomicLongHunk;
import com.volmit.iris.util.hunk.storage.MappedHunk;
import com.volmit.iris.util.hunk.storage.SynchronizedArrayHunk;
import com.volmit.iris.util.hunk.view.BiomeGridHunkView;
import com.volmit.iris.util.hunk.view.ChunkBiomeHunkView;
import com.volmit.iris.util.hunk.view.ChunkDataHunkView;
import com.volmit.iris.util.hunk.view.ChunkHunkView;
import com.volmit.iris.util.hunk.view.DriftHunkView;
import com.volmit.iris.util.hunk.view.FringedHunkView;
import com.volmit.iris.util.hunk.view.FunctionalHunkView;
import com.volmit.iris.util.hunk.view.HunkView;
import com.volmit.iris.util.hunk.view.InvertedHunkView;
import com.volmit.iris.util.hunk.view.ListeningHunk;
import com.volmit.iris.util.hunk.view.ReadOnlyHunk;
import com.volmit.iris.util.hunk.view.SynchronizedHunkView;
import com.volmit.iris.util.hunk.view.WriteTrackHunk;
import com.volmit.iris.util.interpolation.InterpolationMethod;
import com.volmit.iris.util.interpolation.InterpolationMethod3D;
import com.volmit.iris.util.interpolation.IrisInterpolation;
import com.volmit.iris.util.math.BlockPosition;
import com.volmit.iris.util.parallel.BurstExecutor;
import com.volmit.iris.util.parallel.MultiBurst;
import com.volmit.iris.util.stream.interpolation.Interpolated;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.bukkit.Chunk;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;

public interface Hunk<T> {
   static <T> Hunk<T> view(Hunk<T> src) {
      return new HunkView(src);
   }

   static <A, B> Hunk<B> convertedReadView(Hunk<A> src, Function<A, B> reader) {
      return new FunctionalHunkView(src, reader, (Function)null);
   }

   static <A, B> Hunk<B> convertedWriteView(Hunk<A> src, Function<B, A> writer) {
      return new FunctionalHunkView(src, (Function)null, writer);
   }

   static <A, B> Hunk<B> convertedReadWriteView(Hunk<A> src, Function<A, B> reader, Function<B, A> writer) {
      return new FunctionalHunkView(src, reader, writer);
   }

   static Hunk<Biome> view(BiomeGrid biome, int minHeight, int maxHeight) {
      return new BiomeGridHunkView(biome, minHeight, maxHeight);
   }

   static <T> Hunk<T> fringe(Hunk<T> i, Hunk<T> o) {
      return new FringedHunkView(i, o);
   }

   static Hunk<BlockData> view(ChunkData src) {
      return new ChunkDataHunkView(src);
   }

   static Hunk<BlockData> viewBlocks(Chunk src) {
      return new ChunkHunkView(src);
   }

   static Hunk<Biome> viewBiomes(Chunk src) {
      return new ChunkBiomeHunkView(src);
   }

   static <T> Hunk<T> newHunk(int w, int h, int d) {
      return newArrayHunk(w, h, d);
   }

   @SafeVarargs
   static <T> Hunk<T> newCombinedHunk(Hunk<T>... hunks) {
      return newCombinedArrayHunk(hunks);
   }

   static <T> Hunk<T> newArrayHunk(int w, int h, int d) {
      return new ArrayHunk(w, h, d);
   }

   @SafeVarargs
   static <T> Hunk<T> newCombinedArrayHunk(Hunk<T>... hunks) {
      return combined(Hunk::newArrayHunk, hunks);
   }

   static <T> Hunk<T> newSynchronizedArrayHunk(int w, int h, int d) {
      return new SynchronizedArrayHunk(w, h, d);
   }

   @SafeVarargs
   static <T> Hunk<T> newCombinedSynchronizedArrayHunk(Hunk<T>... hunks) {
      return combined(Hunk::newSynchronizedArrayHunk, hunks);
   }

   static <T> Hunk<T> newMappedHunk(int w, int h, int d) {
      return new MappedHunk(w, h, d);
   }

   static <T> Hunk<T> newMappedHunkSynced(int w, int h, int d) {
      return (new MappedHunk(w, h, d)).synchronize();
   }

   @SafeVarargs
   static <T> Hunk<T> newCombinedMappedHunk(Hunk<T>... hunks) {
      return combined(Hunk::newMappedHunk, hunks);
   }

   static <T> Hunk<T> newAtomicHunk(int w, int h, int d) {
      return new AtomicHunk(w, h, d);
   }

   @SafeVarargs
   static <T> Hunk<T> newCombinedAtomicHunk(Hunk<T>... hunks) {
      return combined(Hunk::newAtomicHunk, hunks);
   }

   static Hunk<Double> newAtomicDoubleHunk(int w, int h, int d) {
      return new AtomicDoubleHunk(w, h, d);
   }

   @SafeVarargs
   static Hunk<Double> newCombinedAtomicDoubleHunk(Hunk<Double>... hunks) {
      return combined(Hunk::newAtomicDoubleHunk, hunks);
   }

   static Hunk<Long> newAtomicLongHunk(int w, int h, int d) {
      return new AtomicLongHunk(w, h, d);
   }

   @SafeVarargs
   static Hunk<Long> newCombinedAtomicLongHunk(Hunk<Long>... hunks) {
      return combined(Hunk::newAtomicLongHunk, hunks);
   }

   static Hunk<Integer> newAtomicIntegerHunk(int w, int h, int d) {
      return new AtomicIntegerHunk(w, h, d);
   }

   @SafeVarargs
   static Hunk<Integer> newCombinedAtomicIntegerHunk(Hunk<Integer>... hunks) {
      return combined(Hunk::newAtomicIntegerHunk, hunks);
   }

   @SafeVarargs
   static <T> Hunk<T> combined(Function3<Integer, Integer, Integer, Hunk<T>> factory, Hunk<T>... hunks) {
      int w = 0;
      int h = 0;
      int d = 0;
      Hunk[] var5 = hunks;
      int var6 = hunks.length;

      int var7;
      for(var7 = 0; var7 < var6; ++var7) {
         Hunk<T> i = var5[var7];
         w = Math.max(w, i.getWidth());
         h = Math.max(h, i.getHeight());
         d = Math.max(d, i.getDepth());
      }

      Hunk<T> b = (Hunk)factory.apply(w, h, d);
      Hunk[] var11 = hunks;
      var7 = hunks.length;

      for(int var12 = 0; var12 < var7; ++var12) {
         Hunk<T> i = var11[var12];
         b.insert(i);
      }

      return b;
   }

   static <A, B> void computeDual2D(int parallelism, Hunk<A> a, Hunk<B> b, Consumer5<Integer, Integer, Integer, Hunk<A>, Hunk<B>> v) {
      if (a.getWidth() == b.getWidth() && a.getHeight() == b.getHeight() && a.getDepth() == b.getDepth()) {
         if (a.get2DDimension(parallelism) == 1) {
            v.accept(0, 0, 0, a, b);
         } else {
            BurstExecutor e = MultiBurst.burst.burst(parallelism);
            KList<Runnable> rq = new KList(parallelism);
            getDualSections2D(parallelism, a, b, (xx, yy, zz, ha, hr, r) -> {
               e.queue(() -> {
                  v.accept(xx, yy, zz, ha, hr);
                  synchronized(rq) {
                     rq.add((Object)r);
                  }
               });
            }, (x, y, z, hax, hbx) -> {
               a.insert(x, y, z, hax);
               b.insert(x, y, z, hbx);
            });
            e.complete();
            rq.forEach(Runnable::run);
         }
      } else {
         throw new RuntimeException("Hunk sizes must match!");
      }
   }

   static <A, B> void getDualSections2D(int sections, Hunk<A> a, Hunk<B> b, Consumer6<Integer, Integer, Integer, Hunk<A>, Hunk<B>, Runnable> v, Consumer5<Integer, Integer, Integer, Hunk<A>, Hunk<B>> inserterAB) {
      if (a.getWidth() == b.getWidth() && a.getHeight() == b.getHeight() && a.getDepth() == b.getDepth()) {
         int dim = a.get2DDimension(sections);
         if (sections <= 1) {
            getDualSection(0, 0, 0, a.getWidth(), a.getHeight(), a.getDepth(), a, b, (ha, hr, r) -> {
               v.accept(0, 0, 0, ha, hr, r);
            }, inserterAB);
         } else {
            int w = a.getWidth() / dim;
            int wr = a.getWidth() - w * dim;
            int d = a.getDepth() / dim;
            int dr = a.getDepth() - d * dim;

            for(int i = 0; i < a.getWidth(); i += w) {
               int ii = i;

               for(int j = 0; j < a.getDepth(); j += d) {
                  getDualSection(i, 0, j, i + w + (i == 0 ? wr : 0), a.getHeight(), j + d + (j == 0 ? dr : 0), a, b, (ha, hr, r) -> {
                     v.accept(ii, 0, j, ha, hr, r);
                  }, inserterAB);
                  i = i == 0 ? i + wr : i;
                  j = j == 0 ? j + dr : j;
               }
            }

         }
      } else {
         throw new RuntimeException("Hunk sizes must match!");
      }
   }

   static <A, B> void getDualSection(int x, int y, int z, int x1, int y1, int z1, Hunk<A> a, Hunk<B> b, Consumer3<Hunk<A>, Hunk<B>, Runnable> v, Consumer5<Integer, Integer, Integer, Hunk<A>, Hunk<B>> inserter) {
      Hunk<A> copya = a.crop(x, y, z, x1, y1, z1);
      Hunk<B> copyb = b.crop(x, y, z, x1, y1, z1);
      v.accept(copya, copyb, () -> {
         inserter.accept(x, y, z, copya, copyb);
      });
   }

   static <T> Hunk<T> newHunk(int w, int h, int d, Class<T> type, boolean packed, boolean concurrent) {
      if (type.equals(Double.class)) {
         return concurrent ? (packed ? newAtomicDoubleHunk(w, h, d) : newMappedHunk(w, h, d)) : (packed ? newArrayHunk(w, h, d) : newMappedHunkSynced(w, h, d));
      } else if (type.equals(Integer.class)) {
         return concurrent ? (packed ? newAtomicIntegerHunk(w, h, d) : newMappedHunk(w, h, d)) : (packed ? newArrayHunk(w, h, d) : newMappedHunkSynced(w, h, d));
      } else if (type.equals(Long.class)) {
         return concurrent ? (packed ? newAtomicLongHunk(w, h, d) : newMappedHunk(w, h, d)) : (packed ? newArrayHunk(w, h, d) : newMappedHunkSynced(w, h, d));
      } else {
         return concurrent ? (packed ? newAtomicHunk(w, h, d) : newMappedHunk(w, h, d)) : (packed ? newArrayHunk(w, h, d) : newMappedHunkSynced(w, h, d));
      }
   }

   static IrisPosition rotatedBounding(int w, int h, int d, double x, double y, double z) {
      int[] iii = new int[]{0, 0, 0};
      int[] aaa = new int[]{w, h, d};
      int[] aai = new int[]{w, h, 0};
      int[] iaa = new int[]{0, h, d};
      int[] aia = new int[]{w, 0, d};
      int[] iai = new int[]{0, h, 0};
      int[] iia = new int[]{0, 0, d};
      int[] aii = new int[]{w, 0, 0};
      rotate(x, y, z, iii);
      rotate(x, y, z, aaa);
      rotate(x, y, z, aai);
      rotate(x, y, z, iaa);
      rotate(x, y, z, aia);
      rotate(x, y, z, iai);
      rotate(x, y, z, iia);
      rotate(x, y, z, aii);
      int maxX = max(iii[0], aaa[0], aai[0], iaa[0], aia[0], iai[0], iia[0], aii[0]);
      int minX = min(iii[0], aaa[0], aai[0], iaa[0], aia[0], iai[0], iia[0], aii[0]);
      int maxY = max(iii[1], aaa[1], aai[1], iaa[1], aia[1], iai[1], iia[1], aii[1]);
      int minY = min(iii[1], aaa[1], aai[1], iaa[1], aia[1], iai[1], iia[1], aii[1]);
      int maxZ = max(iii[2], aaa[2], aai[2], iaa[2], aia[2], iai[2], iia[2], aii[2]);
      int minZ = min(iii[2], aaa[2], aai[2], iaa[2], aia[2], iai[2], iia[2], aii[2]);
      return new IrisPosition(maxX - minX, maxY - minY, maxZ - minZ);
   }

   static int max(int a1, int a2, int a3, int a4, int a5, int a6, int a7, int a8) {
      return Math.max(Math.max(Math.max(a5, a6), Math.max(a7, a8)), Math.max(Math.max(a1, a2), Math.max(a3, a4)));
   }

   static int min(int a1, int a2, int a3, int a4, int a5, int a6, int a7, int a8) {
      return Math.min(Math.min(Math.min(a5, a6), Math.min(a7, a8)), Math.min(Math.min(a1, a2), Math.min(a3, a4)));
   }

   static void rotate(double x, double y, double z, int[] c) {
      if (x % 360.0D != 0.0D) {
         rotateAroundX(Math.toRadians(x), c);
      }

      if (y % 360.0D != 0.0D) {
         rotateAroundY(Math.toRadians(y), c);
      }

      if (z % 360.0D != 0.0D) {
         rotateAroundZ(Math.toRadians(z), c);
      }

   }

   static void rotateAroundX(double a, int[] c) {
      rotateAroundX(Math.cos(a), Math.sin(a), c);
   }

   static void rotateAroundX(double cos, double sin, int[] c) {
      int y = (int)Math.floor(cos * ((double)c[1] + 0.5D) - sin * ((double)c[2] + 0.5D));
      int z = (int)Math.floor(sin * ((double)c[1] + 0.5D) + cos * ((double)c[2] + 0.5D));
      c[1] = y;
      c[2] = z;
   }

   static void rotateAroundY(double a, int[] c) {
      rotateAroundY(Math.cos(a), Math.sin(a), c);
   }

   static void rotateAroundY(double cos, double sin, int[] c) {
      int x = (int)Math.floor(cos * ((double)c[0] + 0.5D) + sin * ((double)c[2] + 0.5D));
      int z = (int)Math.floor(-sin * ((double)c[0] + 0.5D) + cos * ((double)c[2] + 0.5D));
      c[0] = x;
      c[2] = z;
   }

   static void rotateAroundZ(double a, int[] c) {
      rotateAroundZ(Math.cos(a), Math.sin(a), c);
   }

   static void rotateAroundZ(double cos, double sin, int[] c) {
      int x = (int)Math.floor(cos * ((double)c[0] + 0.5D) - sin * ((double)c[1] + 0.5D));
      int y = (int)Math.floor(sin * ((double)c[0] + 0.5D) + cos * ((double)c[1] + 0.5D));
      c[0] = x;
      c[1] = y;
   }

   default boolean isMapped() {
      return false;
   }

   default int getEntryCount() {
      return this.getWidth() * this.getHeight() * this.getDepth();
   }

   default Hunk<T> listen(Consumer4<Integer, Integer, Integer, T> l) {
      return new ListeningHunk(this, l);
   }

   default Hunk<T> synchronize() {
      return new SynchronizedHunkView(this);
   }

   default Hunk<T> trackWrite(AtomicBoolean b) {
      return new WriteTrackHunk(this, b);
   }

   default Hunk<T> readOnly() {
      return new ReadOnlyHunk(this);
   }

   default int getNonNullEntries() {
      AtomicInteger count = new AtomicInteger();
      this.iterate((x, y, z, v) -> {
         count.getAndAdd(1);
      });
      return count.get();
   }

   default boolean isAtomic() {
      return false;
   }

   default Hunk<T> invertY() {
      return new InvertedHunkView(this);
   }

   default int getMaximumDimension() {
      return Math.max(this.getWidth(), Math.max(this.getHeight(), this.getDepth()));
   }

   default int getIdeal2DParallelism() {
      return this.getMax2DParallelism() / 4;
   }

   default int getIdeal3DParallelism() {
      return this.getMax3DParallelism() / 8;
   }

   default int getMinimumDimension() {
      return Math.min(this.getWidth(), Math.min(this.getHeight(), this.getDepth()));
   }

   default int getMax2DParallelism() {
      return (int)Math.pow((double)((float)this.getMinimumDimension() / 2.0F), 2.0D);
   }

   default int getMax3DParallelism() {
      return (int)Math.pow((double)((float)this.getMinimumDimension() / 2.0F), 3.0D);
   }

   default int filterDimension(int dim) {
      if (dim <= 1) {
         return 1;
      } else {
         dim = dim % 2 != 0 ? dim + 1 : dim;
         if (dim > this.getMinimumDimension() / 2) {
            if (dim <= 2) {
               return 1;
            }

            dim -= 2;
         }

         return dim;
      }
   }

   default int get2DDimension(int sections) {
      return sections <= 1 ? 1 : this.filterDimension((int)Math.ceil(Math.sqrt((double)sections)));
   }

   default int get3DDimension(int sections) {
      return sections <= 1 ? 1 : this.filterDimension((int)Math.ceil(Math.cbrt((double)sections)));
   }

   default Hunk<T> iterateSurfaces2D(Predicate<T> p, Consumer8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Hunk<T>> c) {
      return this.iterateSurfaces2D(this.getIdeal2DParallelism(), p, c);
   }

   default Hunk<T> iterateSurfaces2D(int parallelism, Predicate<T> p, Consumer8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Hunk<T>> c) {
      this.iterate2DTop(parallelism, (ax, az, hox, hoz, h) -> {
         int last = -1;
         int in = this.getHeight() - 1;
         boolean hitting = false;

         for(int i = this.getHeight() - 1; i >= 0; --i) {
            boolean solid = p.test(h.get(ax, i, az));
            if (!hitting && solid) {
               in = i;
               hitting = true;
            } else if (hitting && !solid) {
               hitting = false;
               c.accept(ax, az, hox, hoz, in, i - 1, last, h);
               last = i - 1;
            }
         }

         if (hitting) {
            c.accept(ax, az, hox, hoz, in, 0, last, h);
         }

      });
      return this;
   }

   default Hunk<T> iterate2DTop(Consumer5<Integer, Integer, Integer, Integer, Hunk<T>> c) {
      return this.iterate2DTop(this.getIdeal2DParallelism(), c);
   }

   default Hunk<T> drift(int x, int y, int z) {
      return new DriftHunkView(this, x, y, z);
   }

   default Hunk<T> iterate2DTop(int parallelism, Consumer5<Integer, Integer, Integer, Integer, Hunk<T>> c) {
      this.compute2D(parallelism, (x, y, z, h) -> {
         for(int i = 0; i < h.getWidth(); ++i) {
            for(int k = 0; k < h.getDepth(); ++k) {
               c.accept(i, k, x, z, h);
            }
         }

      });
      return this;
   }

   default Hunk<T> iterate(Predicate<T> p, Consumer3<Integer, Integer, Integer> c) {
      return this.iterate(this.getIdeal3DParallelism(), p, c);
   }

   default Hunk<T> iterate(int parallelism, Predicate<T> p, Consumer3<Integer, Integer, Integer> c) {
      this.iterate(parallelism, (x, y, z, t) -> {
         if (p.test(t)) {
            c.accept(x, y, z);
         }

      });
      return this;
   }

   default Hunk<T> iterate(Predicate<T> p, Consumer4<Integer, Integer, Integer, T> c) {
      return this.iterate(this.getIdeal3DParallelism(), p, c);
   }

   default Hunk<T> iterate(int parallelism, Predicate<T> p, Consumer4<Integer, Integer, Integer, T> c) {
      this.iterate(parallelism, (x, y, z, t) -> {
         if (p.test(t)) {
            c.accept(x, y, z, t);
         }

      });
      return this;
   }

   default Hunk<T> iterate(Consumer3<Integer, Integer, Integer> c) {
      return this.iterate(this.getIdeal3DParallelism(), c);
   }

   default Hunk<T> iterateSync(Consumer3<Integer, Integer, Integer> c) {
      for(int i = 0; i < this.getWidth(); ++i) {
         for(int j = 0; j < this.getHeight(); ++j) {
            for(int k = 0; k < this.getDepth(); ++k) {
               c.accept(i, j, k);
            }
         }
      }

      return this;
   }

   default Hunk<T> iterateSync(Consumer4<Integer, Integer, Integer, T> c) {
      for(int i = 0; i < this.getWidth(); ++i) {
         for(int j = 0; j < this.getHeight(); ++j) {
            for(int k = 0; k < this.getDepth(); ++k) {
               c.accept(i, j, k, this.get(i, j, k));
            }
         }
      }

      return this;
   }

   default Hunk<T> updateSync(Function4<Integer, Integer, Integer, T, T> c) {
      for(int i = 0; i < this.getWidth(); ++i) {
         for(int j = 0; j < this.getHeight(); ++j) {
            for(int k = 0; k < this.getDepth(); ++k) {
               this.set(i, j, k, c.apply(i, j, k, this.get(i, j, k)));
            }
         }
      }

      return this;
   }

   default Hunk<T> iterateSyncIO(Consumer4IO<Integer, Integer, Integer, T> c) throws IOException {
      for(int i = 0; i < this.getWidth(); ++i) {
         for(int j = 0; j < this.getHeight(); ++j) {
            for(int k = 0; k < this.getDepth(); ++k) {
               c.accept(i, j, k, this.get(i, j, k));
            }
         }
      }

      return this;
   }

   default Hunk<T> iterate(int parallelism, Consumer3<Integer, Integer, Integer> c) {
      this.compute3D(parallelism, (x, y, z, h) -> {
         for(int i = 0; i < h.getWidth(); ++i) {
            for(int j = 0; j < h.getHeight(); ++j) {
               for(int k = 0; k < h.getDepth(); ++k) {
                  c.accept(i + x, j + y, k + z);
               }
            }
         }

      });
      return this;
   }

   default Hunk<T> iterate(Consumer4<Integer, Integer, Integer, T> c) {
      return this.iterate(this.getIdeal3DParallelism(), c);
   }

   default Hunk<T> iterate(int parallelism, Consumer4<Integer, Integer, Integer, T> c) {
      this.compute3D(parallelism, (x, y, z, h) -> {
         for(int i = 0; i < h.getWidth(); ++i) {
            for(int j = 0; j < h.getHeight(); ++j) {
               for(int k = 0; k < h.getDepth(); ++k) {
                  c.accept(i + x, j + y, k + z, h.get(i, j, k));
               }
            }
         }

      });
      return this;
   }

   default Hunk<T> compute2D(Consumer4<Integer, Integer, Integer, Hunk<T>> v) {
      return this.compute2D(this.getIdeal2DParallelism(), v);
   }

   default Hunk<T> compute2D(int parallelism, Consumer4<Integer, Integer, Integer, Hunk<T>> v) {
      if (this.get2DDimension(parallelism) == 1) {
         v.accept(0, 0, 0, this);
         return this;
      } else {
         BurstExecutor e = MultiBurst.burst.burst(parallelism);
         if (this.isAtomic()) {
            this.getSectionsAtomic2D(parallelism, (xx, yy, zz, h) -> {
               e.queue(() -> {
                  v.accept(xx, yy, zz, h);
               });
            });
            e.complete();
         } else {
            KList<Runnable> rq = new KList(parallelism);
            this.getSections2D(parallelism, (xx, yy, zz, h, r) -> {
               e.queue(() -> {
                  v.accept(xx, yy, zz, h);
                  synchronized(rq) {
                     rq.add((Object)r);
                  }
               });
            }, this::insert);
            e.complete();
            rq.forEach(Runnable::run);
         }

         return this;
      }
   }

   default Hunk<T> compute2DYRange(int parallelism, int ymin, int ymax, Consumer4<Integer, Integer, Integer, Hunk<T>> v) {
      if (this.get2DDimension(parallelism) == 1) {
         v.accept(0, 0, 0, this);
         return this;
      } else {
         BurstExecutor e = MultiBurst.burst.burst(parallelism);
         KList<Runnable> rq = new KList(parallelism);
         this.getSections2DYLimit(parallelism, ymin, ymax, (xx, yy, zz, h, r) -> {
            e.queue(() -> {
               v.accept(xx, yy, zz, h);
               synchronized(rq) {
                  rq.add((Object)r);
               }
            });
         }, this::insert);
         e.complete();
         rq.forEach(Runnable::run);
         return this;
      }
   }

   default Hunk<T> compute3D(Consumer4<Integer, Integer, Integer, Hunk<T>> v) {
      return this.compute3D(this.getIdeal3DParallelism(), v);
   }

   default Hunk<T> compute3D(int parallelism, Consumer4<Integer, Integer, Integer, Hunk<T>> v) {
      if (this.get3DDimension(parallelism) == 1) {
         v.accept(0, 0, 0, this);
         return this;
      } else {
         BurstExecutor e = MultiBurst.burst.burst(parallelism);
         KList<Runnable> rq = new KList(parallelism);
         this.getSections3D(parallelism, (xx, yy, zz, h, r) -> {
            e.queue(() -> {
               v.accept(xx, yy, zz, h);
               synchronized(rq) {
                  rq.add((Object)r);
               }
            });
         }, this::insert);
         e.complete();
         rq.forEach(Runnable::run);
         return this;
      }
   }

   default Hunk<T> getSections2D(int sections, Consumer5<Integer, Integer, Integer, Hunk<T>, Runnable> v) {
      return this.getSections2D(sections, v, this::insert);
   }

   default Hunk<T> getSectionsAtomic2D(int sections, Consumer4<Integer, Integer, Integer, Hunk<T>> v) {
      int dim = this.get2DDimension(sections);
      if (sections <= 1) {
         this.getAtomicSection(0, 0, 0, this.getWidth(), this.getHeight(), this.getDepth(), (hh) -> {
            v.accept(0, 0, 0, hh);
         });
         return this;
      } else {
         int w = this.getWidth() / dim;
         int wr = this.getWidth() - w * dim;
         int d = this.getDepth() / dim;
         int dr = this.getDepth() - d * dim;

         for(int i = 0; i < this.getWidth(); i += w) {
            int ii = i;

            for(int j = 0; j < this.getDepth(); j += d) {
               this.getAtomicSection(i, 0, j, i + w + (i == 0 ? wr : 0), this.getHeight(), j + d + (j == 0 ? dr : 0), (h) -> {
                  v.accept(ii, 0, j, h);
               });
               i = i == 0 ? i + wr : i;
               j = j == 0 ? j + dr : j;
            }
         }

         return this;
      }
   }

   default Hunk<T> getSections2D(int sections, Consumer5<Integer, Integer, Integer, Hunk<T>, Runnable> v, Consumer4<Integer, Integer, Integer, Hunk<T>> inserter) {
      int dim = this.get2DDimension(sections);
      if (sections <= 1) {
         this.getSection(0, 0, 0, this.getWidth(), this.getHeight(), this.getDepth(), (hh, r) -> {
            v.accept(0, 0, 0, hh, r);
         }, inserter);
         return this;
      } else {
         int w = this.getWidth() / dim;
         int wr = this.getWidth() - w * dim;
         int d = this.getDepth() / dim;
         int dr = this.getDepth() - d * dim;

         for(int i = 0; i < this.getWidth(); i += w) {
            int ii = i;

            for(int j = 0; j < this.getDepth(); j += d) {
               this.getSection(i, 0, j, i + w + (i == 0 ? wr : 0), this.getHeight(), j + d + (j == 0 ? dr : 0), (h, r) -> {
                  v.accept(ii, 0, j, h, r);
               }, inserter);
               i = i == 0 ? i + wr : i;
               j = j == 0 ? j + dr : j;
            }
         }

         return this;
      }
   }

   default Hunk<T> getSections2DYLimit(int sections, int ymin, int ymax, Consumer5<Integer, Integer, Integer, Hunk<T>, Runnable> v, Consumer4<Integer, Integer, Integer, Hunk<T>> inserter) {
      int dim = this.get2DDimension(sections);
      if (sections <= 1) {
         this.getSection(0, 0, 0, this.getWidth(), this.getHeight(), this.getDepth(), (hh, r) -> {
            v.accept(0, 0, 0, hh, r);
         }, inserter);
         return this;
      } else {
         int w = this.getWidth() / dim;
         int wr = this.getWidth() - w * dim;
         int d = this.getDepth() / dim;
         int dr = this.getDepth() - d * dim;

         for(int i = 0; i < this.getWidth(); i += w) {
            int ii = i;

            for(int j = 0; j < this.getDepth(); j += d) {
               this.getSection(i, ymin, j, i + w + (i == 0 ? wr : 0), ymax, j + d + (j == 0 ? dr : 0), (h, r) -> {
                  v.accept(ii, ymin, j, h, r);
               }, inserter);
               i = i == 0 ? i + wr : i;
               j = j == 0 ? j + dr : j;
            }
         }

         return this;
      }
   }

   default Hunk<T> getSections3D(int sections, Consumer5<Integer, Integer, Integer, Hunk<T>, Runnable> v) {
      return this.getSections3D(sections, v, (xx, yy, zz, c) -> {
         this.insert(xx, yy, zz, c);
      });
   }

   default Hunk<T> getSections3D(int sections, Consumer5<Integer, Integer, Integer, Hunk<T>, Runnable> v, Consumer4<Integer, Integer, Integer, Hunk<T>> inserter) {
      int dim = this.get3DDimension(sections);
      if (sections <= 1) {
         this.getSection(0, 0, 0, this.getWidth(), this.getHeight(), this.getDepth(), (hh, r) -> {
            v.accept(0, 0, 0, hh, r);
         }, inserter);
         return this;
      } else {
         int w = this.getWidth() / dim;
         int h = this.getHeight() / dim;
         int d = this.getDepth() / dim;
         int wr = this.getWidth() - w * dim;
         int hr = this.getHeight() - h * dim;
         int dr = this.getDepth() - d * dim;

         for(int i = 0; i < this.getWidth(); i += w) {
            int ii = i;

            for(int j = 0; j < this.getHeight(); j += d) {
               int jj = j;

               for(int k = 0; k < this.getDepth(); k += d) {
                  this.getSection(ii, jj, k, i + w + (i == 0 ? wr : 0), j + h + (j == 0 ? hr : 0), k + d + (k == 0 ? dr : 0), (hh, r) -> {
                     v.accept(ii, jj, k, hh, r);
                  }, inserter);
                  i = i == 0 ? i + wr : i;
                  j = j == 0 ? j + hr : j;
                  k = k == 0 ? k + dr : k;
               }
            }
         }

         return this;
      }
   }

   default Hunk<T> getSection(int x, int y, int z, int x1, int y1, int z1, Consumer2<Hunk<T>, Runnable> v) {
      return this.getSection(x, y, z, x1, y1, z1, v, (xx, yy, zz, c) -> {
         this.insert(xx, yy, zz, c);
      });
   }

   default Hunk<T> getSection(int x, int y, int z, int x1, int y1, int z1, Consumer2<Hunk<T>, Runnable> v, Consumer4<Integer, Integer, Integer, Hunk<T>> inserter) {
      Hunk<T> copy = this.crop(x, y, z, x1, y1, z1);
      v.accept(copy, () -> {
         inserter.accept(x, y, z, copy);
      });
      return this;
   }

   default Hunk<T> getAtomicSection(int x, int y, int z, int x1, int y1, int z1, Consumer<Hunk<T>> v) {
      Hunk<T> copy = this.croppedView(x, y, z, x1, y1, z1);
      v.accept(copy);
      return this;
   }

   default ArrayHunk<T> crop(int x1, int y1, int z1, int x2, int y2, int z2) {
      ArrayHunk<T> h = new ArrayHunk(x2 - x1, y2 - y1, z2 - z1);

      for(int i = x1; i < x2; ++i) {
         for(int j = y1; j < y2; ++j) {
            for(int k = z1; k < z2; ++k) {
               h.setRaw(i - x1, j - y1, k - z1, this.getRaw(i, j, k));
            }
         }
      }

      return h;
   }

   default Hunk<T> croppedView(int x1, int y1, int z1, int x2, int y2, int z2) {
      return new HunkView(this, x2 - x1, y2 - y1, z2 - z1, x1, y1, z1);
   }

   int getWidth();

   int getDepth();

   int getHeight();

   default void set(int x1, int y1, int z1, int x2, int y2, int z2, T t) {
      for(int i = x1; i <= x2; ++i) {
         for(int j = y1; j <= y2; ++j) {
            for(int k = z1; k <= z2; ++k) {
               this.setRaw(i, j, k, t);
            }
         }
      }

   }

   default T getClosest(int x, int y, int z) {
      return this.getRaw(x >= this.getWidth() ? this.getWidth() - 1 : (x < 0 ? 0 : x), y >= this.getHeight() ? this.getHeight() - 1 : (y < 0 ? 0 : y), z >= this.getDepth() ? this.getDepth() - 1 : (z < 0 ? 0 : z));
   }

   default BlockPosition getCenter() {
      return new BlockPosition(this.getCenterX(), this.getCenterY(), this.getCenterZ());
   }

   default int getCenterX() {
      return (int)Math.floor((double)(this.getWidth() / 2));
   }

   default int getCenterY() {
      return (int)Math.floor((double)(this.getHeight() / 2));
   }

   default int getCenterZ() {
      return (int)Math.floor((double)(this.getDepth() / 2));
   }

   default void fill(T t) {
      this.set(0, 0, 0, this.getWidth() - 1, this.getHeight() - 1, this.getDepth() - 1, t);
   }

   default Hunk<T> viewFace(HunkFace f) {
      switch(f) {
      case BOTTOM:
         return this.croppedView(0, 0, 0, this.getWidth() - 1, 0, this.getDepth() - 1);
      case EAST:
         return this.croppedView(this.getWidth() - 1, 0, 0, this.getWidth() - 1, this.getHeight() - 1, this.getDepth() - 1);
      case NORTH:
         return this.croppedView(0, 0, 0, this.getWidth() - 1, this.getHeight() - 1, 0);
      case SOUTH:
         return this.croppedView(0, 0, 0, 0, this.getHeight() - 1, this.getDepth() - 1);
      case TOP:
         return this.croppedView(0, this.getHeight() - 1, 0, this.getWidth() - 1, this.getHeight() - 1, this.getDepth() - 1);
      case WEST:
         return this.croppedView(0, 0, this.getDepth() - 1, this.getWidth() - 1, this.getHeight() - 1, this.getDepth() - 1);
      default:
         return null;
      }
   }

   default Hunk<T> cropFace(HunkFace f) {
      switch(f) {
      case BOTTOM:
         return this.crop(0, 0, 0, this.getWidth() - 1, 0, this.getDepth() - 1);
      case EAST:
         return this.crop(this.getWidth() - 1, 0, 0, this.getWidth() - 1, this.getHeight() - 1, this.getDepth() - 1);
      case NORTH:
         return this.crop(0, 0, 0, this.getWidth() - 1, this.getHeight() - 1, 0);
      case SOUTH:
         return this.crop(0, 0, 0, 0, this.getHeight() - 1, this.getDepth() - 1);
      case TOP:
         return this.crop(0, this.getHeight() - 1, 0, this.getWidth() - 1, this.getHeight() - 1, this.getDepth() - 1);
      case WEST:
         return this.crop(0, 0, this.getDepth() - 1, this.getWidth() - 1, this.getHeight() - 1, this.getDepth() - 1);
      default:
         return null;
      }
   }

   default void set(int x, int y, int z, T t) {
      if (!this.contains(x, y, z)) {
         Iris.warn("OUT OF BOUNDS " + x + " " + y + " " + z + " in bounds " + this.getWidth() + " " + this.getHeight() + " " + this.getDepth());
      } else {
         this.setRaw(x, y, z, t);
      }
   }

   default void setIfExists(int x, int y, int z, T t) {
      if (x >= 0 && x < this.getWidth() && y >= 0 && y < this.getHeight() && z >= 0 && z < this.getDepth()) {
         this.setRaw(x, y, z, t);
      }
   }

   default T getIfExists(int x, int y, int z, T t) {
      return x >= 0 && x < this.getWidth() && y >= 0 && y < this.getHeight() && z >= 0 && z < this.getDepth() ? this.getOr(x, y, z, t) : t;
   }

   default T getIfExists(int x, int y, int z) {
      return this.getIfExists(x, y, z, (Object)null);
   }

   void setRaw(int x, int y, int z, T t);

   T getRaw(int x, int y, int z);

   default T get(int x, int y, int z) {
      return this.getRaw(x, y, z);
   }

   default T getOr(int x, int y, int z, T t) {
      T v = this.getRaw(x, y, z);
      return v == null ? t : v;
   }

   default void insert(int offX, int offY, int offZ, Hunk<T> hunk) {
      this.insert(offX, offY, offZ, hunk, false);
   }

   default void insertSoftly(int offX, int offY, int offZ, Hunk<T> hunk, Predicate<T> shouldOverwrite) {
      this.insertSoftly(offX, offY, offZ, hunk, false, shouldOverwrite);
   }

   default void insert(Hunk<T> hunk) {
      this.insert(0, 0, 0, hunk, false);
   }

   default Hunk<T> getSource() {
      return null;
   }

   default void insert(Hunk<T> hunk, boolean inverted) {
      this.insert(0, 0, 0, hunk, inverted);
   }

   default void insert(int offX, int offY, int offZ, Hunk<T> hunk, boolean invertY) {
      for(int i = offX; i < offX + hunk.getWidth(); ++i) {
         for(int j = offY; j < offY + hunk.getHeight(); ++j) {
            for(int k = offZ; k < offZ + hunk.getDepth(); ++k) {
               this.setRaw(i, j, k, hunk.getRaw(i - offX, j - offY, k - offZ));
            }
         }
      }

   }

   default void insertSoftly(int offX, int offY, int offZ, Hunk<T> hunk, boolean invertY, Predicate<T> shouldOverwrite) {
      for(int i = offX; i < offX + hunk.getWidth(); ++i) {
         for(int j = offY; j < offY + hunk.getHeight(); ++j) {
            for(int k = offZ; k < offZ + hunk.getDepth(); ++k) {
               if (shouldOverwrite.test(this.getRaw(i, j, k))) {
                  this.setRaw(i, j, k, hunk.getRaw(i - offX, j - offY, k - offZ));
               }
            }
         }
      }

   }

   default void empty(T b) {
      this.fill(b);
   }

   default Hunk<T> interpolate3D(double scale, InterpolationMethod3D d, Interpolated<T> interpolated) {
      Hunk<T> t = newArrayHunk((int)((double)this.getWidth() * scale), (int)((double)this.getHeight() * scale), (int)((double)this.getDepth() * scale));
      NoiseProvider3 n3 = (x, y, z) -> {
         return interpolated.toDouble(t.get((int)(x / scale), (int)(y / scale), (int)(z / scale)));
      };

      for(int i = 0; i < t.getWidth(); ++i) {
         for(int j = 0; j < t.getHeight(); ++j) {
            for(int k = 0; k < t.getDepth(); ++k) {
               t.set(i, j, k, interpolated.fromDouble(IrisInterpolation.getNoise3D(d, i, j, k, scale, n3)));
            }
         }
      }

      return t;
   }

   default Hunk<T> interpolate2D(double scale, InterpolationMethod d, Interpolated<T> interpolated) {
      Hunk<T> t = newArrayHunk((int)((double)this.getWidth() * scale), 1, (int)((double)this.getDepth() * scale));
      NoiseProvider n2 = (x, z) -> {
         return interpolated.toDouble(t.get((int)(x / scale), 0, (int)(z / scale)));
      };

      for(int i = 0; i < t.getWidth(); ++i) {
         for(int j = 0; j < t.getDepth(); ++j) {
            t.set(i, 0, j, interpolated.fromDouble(IrisInterpolation.getNoise(d, i, j, scale, n2)));
         }
      }

      return t;
   }

   default Hunk<T> rotate(double x, double y, double z, Supplier3R<Integer, Integer, Integer, Hunk<T>> builder) {
      int w = this.getWidth();
      int h = this.getHeight();
      int d = this.getDepth();
      int[] c = new int[]{w / 2, h / 2, d / 2};
      int[] b = new int[]{0, 0, 0};
      int[] iii = new int[]{0, 0, 0};
      int[] aaa = new int[]{w, h, d};
      int[] aai = new int[]{w, h, 0};
      int[] iaa = new int[]{0, h, d};
      int[] aia = new int[]{w, 0, d};
      int[] iai = new int[]{0, h, 0};
      int[] iia = new int[]{0, 0, d};
      int[] aii = new int[]{w, 0, 0};
      rotate(x, y, z, iii);
      rotate(x, y, z, aaa);
      rotate(x, y, z, aai);
      rotate(x, y, z, iaa);
      rotate(x, y, z, aia);
      rotate(x, y, z, iai);
      rotate(x, y, z, iia);
      rotate(x, y, z, aii);
      int maxX = max(iii[0], aaa[0], aai[0], iaa[0], aia[0], iai[0], iia[0], aii[0]);
      int minX = min(iii[0], aaa[0], aai[0], iaa[0], aia[0], iai[0], iia[0], aii[0]);
      int maxY = max(iii[1], aaa[1], aai[1], iaa[1], aia[1], iai[1], iia[1], aii[1]);
      int minY = min(iii[1], aaa[1], aai[1], iaa[1], aia[1], iai[1], iia[1], aii[1]);
      int maxZ = max(iii[2], aaa[2], aai[2], iaa[2], aia[2], iai[2], iia[2], aii[2]);
      int minZ = min(iii[2], aaa[2], aai[2], iaa[2], aia[2], iai[2], iia[2], aii[2]);
      Hunk<T> r = (Hunk)builder.get(maxX - minX, maxY - minY, maxZ - minZ);
      int[] cr = new int[]{(maxX - minX) / 2, (maxY - minY) / 2, (maxZ - minZ) / 2};

      for(int i = 0; i < w; ++i) {
         for(int j = 0; j < h; ++j) {
            for(int k = 0; k < d; ++k) {
               b[0] = i - c[0];
               b[1] = j - c[1];
               b[2] = k - c[2];
               rotate(x, y, z, b);

               try {
                  r.set(b[0] + cr[0], b[1] + cr[1], b[2] + cr[2], this.get(i, j, k));
               } catch (Throwable var33) {
               }
            }
         }
      }

      return r;
   }

   default boolean isEmpty() {
      return false;
   }

   default boolean contains(int x, int y, int z) {
      return x < this.getWidth() && x >= 0 && y < this.getHeight() && y >= 0 && z < this.getDepth() && z >= 0;
   }

   default int volume() {
      return this.getWidth() * this.getDepth() * this.getHeight();
   }
}
