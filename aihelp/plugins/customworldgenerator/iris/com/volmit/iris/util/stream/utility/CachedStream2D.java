package com.volmit.iris.util.stream.utility;

import com.volmit.iris.Iris;
import com.volmit.iris.core.service.PreservationSVC;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.framework.MeteredCache;
import com.volmit.iris.util.cache.WorldCache2D;
import com.volmit.iris.util.data.KCache;
import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.ProceduralStream;
import java.util.Objects;

public class CachedStream2D<T> extends BasicStream<T> implements ProceduralStream<T>, MeteredCache {
   private final ProceduralStream<T> stream;
   private final WorldCache2D<T> cache;
   private final Engine engine;
   private final boolean chunked = true;

   public CachedStream2D(String name, Engine engine, ProceduralStream<T> stream, int size) {
      this.stream = var3;
      this.engine = var2;
      Objects.requireNonNull(var3);
      this.cache = new WorldCache2D(var3::get, var4);
      ((PreservationSVC)Iris.service(PreservationSVC.class)).registerCache(this);
   }

   public double toDouble(T t) {
      return this.stream.toDouble(var1);
   }

   public T fromDouble(double d) {
      return this.stream.fromDouble(var1);
   }

   public T get(double x, double z) {
      return this.cache.get((int)var1, (int)var3);
   }

   public T get(double x, double y, double z) {
      return this.stream.get(var1, var3, var5);
   }

   public long getSize() {
      return this.cache.getSize();
   }

   public KCache<?, ?> getRawCache() {
      return null;
   }

   public long getMaxSize() {
      return this.cache.getMaxSize();
   }

   public boolean isClosed() {
      return this.engine.isClosed();
   }
}
