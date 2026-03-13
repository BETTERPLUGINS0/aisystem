package com.volmit.iris.util.stream.utility;

import com.volmit.iris.Iris;
import com.volmit.iris.core.service.PreservationSVC;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.framework.MeteredCache;
import com.volmit.iris.util.data.KCache;
import com.volmit.iris.util.math.BlockPosition;
import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.ProceduralStream;

public class CachedStream3D<T> extends BasicStream<T> implements ProceduralStream<T>, MeteredCache {
   private final ProceduralStream<T> stream;
   private final KCache<BlockPosition, T> cache;
   private final Engine engine;

   public CachedStream3D(String name, Engine engine, ProceduralStream<T> stream, int size) {
      this.stream = var3;
      this.engine = var2;
      this.cache = new KCache((var1x) -> {
         return var3.get((double)var1x.getX(), (double)var1x.getY(), (double)var1x.getZ());
      }, (long)var4);
      ((PreservationSVC)Iris.service(PreservationSVC.class)).registerCache(this);
   }

   public double toDouble(T t) {
      return this.stream.toDouble(var1);
   }

   public T fromDouble(double d) {
      return this.stream.fromDouble(var1);
   }

   public T get(double x, double z) {
      return this.cache.get(new BlockPosition((int)var1, 0, (int)var3));
   }

   public T get(double x, double y, double z) {
      return this.cache.get(new BlockPosition((int)var1, (int)var3, (int)var5));
   }

   public long getSize() {
      return this.cache.getSize();
   }

   public KCache<?, ?> getRawCache() {
      return this.cache;
   }

   public long getMaxSize() {
      return this.cache.getMaxSize();
   }

   public boolean isClosed() {
      return this.engine.isClosed();
   }
}
