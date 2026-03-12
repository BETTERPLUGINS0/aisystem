package org.apache.commons.lang3;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.Collector.Characteristics;

/** @deprecated */
@Deprecated
public class Streams {
   public static <O> Streams.FailableStream<O> stream(Stream<O> var0) {
      return new Streams.FailableStream(var0);
   }

   public static <O> Streams.FailableStream<O> stream(Collection<O> var0) {
      return stream(var0.stream());
   }

   public static <O> Collector<O, ?, O[]> toArray(Class<O> var0) {
      return new Streams.ArrayCollector(var0);
   }

   /** @deprecated */
   @Deprecated
   public static class ArrayCollector<O> implements Collector<O, List<O>, O[]> {
      private static final Set<Characteristics> characteristics = Collections.emptySet();
      private final Class<O> elementType;

      public ArrayCollector(Class<O> var1) {
         this.elementType = var1;
      }

      public Supplier<List<O>> supplier() {
         return ArrayList::new;
      }

      public BiConsumer<List<O>, O> accumulator() {
         return List::add;
      }

      public BinaryOperator<List<O>> combiner() {
         return (var0, var1) -> {
            var0.addAll(var1);
            return var0;
         };
      }

      public Function<List<O>, O[]> finisher() {
         return (var1) -> {
            Object[] var2 = (Object[])((Object[])Array.newInstance(this.elementType, var1.size()));
            return var1.toArray(var2);
         };
      }

      public Set<Characteristics> characteristics() {
         return characteristics;
      }
   }

   /** @deprecated */
   @Deprecated
   public static class FailableStream<O> {
      private Stream<O> stream;
      private boolean terminated;

      public FailableStream(Stream<O> var1) {
         this.stream = var1;
      }

      protected void assertNotTerminated() {
         if (this.terminated) {
            throw new IllegalStateException("This stream is already terminated.");
         }
      }

      protected void makeTerminated() {
         this.assertNotTerminated();
         this.terminated = true;
      }

      public Streams.FailableStream<O> filter(Functions.FailablePredicate<O, ?> var1) {
         this.assertNotTerminated();
         this.stream = this.stream.filter(Functions.asPredicate(var1));
         return this;
      }

      public void forEach(Functions.FailableConsumer<O, ?> var1) {
         this.makeTerminated();
         this.stream().forEach(Functions.asConsumer(var1));
      }

      public <A, R> R collect(Collector<? super O, A, R> var1) {
         this.makeTerminated();
         return this.stream().collect(var1);
      }

      public <A, R> R collect(Supplier<R> var1, BiConsumer<R, ? super O> var2, BiConsumer<R, R> var3) {
         this.makeTerminated();
         return this.stream().collect(var1, var2, var3);
      }

      public O reduce(O var1, BinaryOperator<O> var2) {
         this.makeTerminated();
         return this.stream().reduce(var1, var2);
      }

      public <R> Streams.FailableStream<R> map(Functions.FailableFunction<O, R, ?> var1) {
         this.assertNotTerminated();
         return new Streams.FailableStream(this.stream.map(Functions.asFunction(var1)));
      }

      public Stream<O> stream() {
         return this.stream;
      }

      public boolean allMatch(Functions.FailablePredicate<O, ?> var1) {
         this.assertNotTerminated();
         return this.stream().allMatch(Functions.asPredicate(var1));
      }

      public boolean anyMatch(Functions.FailablePredicate<O, ?> var1) {
         this.assertNotTerminated();
         return this.stream().anyMatch(Functions.asPredicate(var1));
      }
   }
}
