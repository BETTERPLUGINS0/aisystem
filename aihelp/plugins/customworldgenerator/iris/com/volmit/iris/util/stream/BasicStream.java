package com.volmit.iris.util.stream;

public abstract class BasicStream<T> extends BasicLayer implements ProceduralStream<T> {
   private final ProceduralStream<T> source;

   public BasicStream(ProceduralStream<T> source) {
      this.source = var1;
   }

   public BasicStream() {
      this((ProceduralStream)null);
   }

   public ProceduralStream<T> getTypedSource() {
      return this.source;
   }

   public ProceduralStream<?> getSource() {
      return this.getTypedSource();
   }

   public abstract T get(double x, double z);

   public abstract T get(double x, double y, double z);

   public abstract double toDouble(T t);

   public abstract T fromDouble(double d);
}
