package com.volmit.iris.util.stream.convert;

import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.stream.BasicLayer;
import com.volmit.iris.util.stream.ProceduralStream;
import java.util.function.Function;

public class CachedConversionStream<T, V> extends BasicLayer implements ProceduralStream<V> {
   private final ProceduralStream<T> stream;
   private final Function<T, V> converter;
   private final KMap<T, V> cache;

   public CachedConversionStream(ProceduralStream<T> stream, Function<T, V> converter) {
      this.stream = var1;
      this.converter = var2;
      this.cache = new KMap();
   }

   public double toDouble(V t) {
      return 0.0D;
   }

   public V fromDouble(double d) {
      return null;
   }

   public ProceduralStream<V> getTypedSource() {
      return null;
   }

   public ProceduralStream<?> getSource() {
      return this.stream;
   }

   public V get(double x, double z) {
      return this.cache.computeIfAbsent(this.stream.get(var1, var3), this.converter);
   }

   public V get(double x, double y, double z) {
      return this.cache.computeIfAbsent(this.stream.get(var1, var3, var5), this.converter);
   }
}
