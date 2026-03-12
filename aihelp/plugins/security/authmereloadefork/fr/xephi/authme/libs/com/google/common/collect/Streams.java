package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Optional;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.math.LongMath;
import fr.xephi.authme.libs.com.google.errorprone.annotations.InlineMe;
import fr.xephi.authme.libs.com.google.errorprone.annotations.InlineMeValidationDisabled;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.Spliterator.OfDouble;
import java.util.Spliterator.OfInt;
import java.util.Spliterator.OfLong;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.stream.BaseStream;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public final class Streams {
   public static <T> Stream<T> stream(Iterable<T> iterable) {
      return iterable instanceof Collection ? ((Collection)iterable).stream() : StreamSupport.stream(iterable.spliterator(), false);
   }

   /** @deprecated */
   @Deprecated
   @Beta
   @InlineMe(
      replacement = "collection.stream()"
   )
   public static <T> Stream<T> stream(Collection<T> collection) {
      return collection.stream();
   }

   @Beta
   public static <T> Stream<T> stream(Iterator<T> iterator) {
      return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
   }

   @Beta
   public static <T> Stream<T> stream(Optional<T> optional) {
      return optional.isPresent() ? Stream.of(optional.get()) : Stream.empty();
   }

   @Beta
   @InlineMe(
      replacement = "optional.stream()"
   )
   @InlineMeValidationDisabled("Java 9+ API only")
   public static <T> Stream<T> stream(java.util.Optional<T> optional) {
      return optional.isPresent() ? Stream.of(optional.get()) : Stream.empty();
   }

   @Beta
   @InlineMe(
      replacement = "optional.stream()"
   )
   @InlineMeValidationDisabled("Java 9+ API only")
   public static IntStream stream(OptionalInt optional) {
      return optional.isPresent() ? IntStream.of(optional.getAsInt()) : IntStream.empty();
   }

   @Beta
   @InlineMe(
      replacement = "optional.stream()"
   )
   @InlineMeValidationDisabled("Java 9+ API only")
   public static LongStream stream(OptionalLong optional) {
      return optional.isPresent() ? LongStream.of(optional.getAsLong()) : LongStream.empty();
   }

   @Beta
   @InlineMe(
      replacement = "optional.stream()"
   )
   @InlineMeValidationDisabled("Java 9+ API only")
   public static DoubleStream stream(OptionalDouble optional) {
      return optional.isPresent() ? DoubleStream.of(optional.getAsDouble()) : DoubleStream.empty();
   }

   private static void closeAll(BaseStream<?, ?>[] toClose) {
      BaseStream[] var1 = toClose;
      int var2 = toClose.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         BaseStream<?, ?> stream = var1[var3];
         stream.close();
      }

   }

   @SafeVarargs
   public static <T> Stream<T> concat(Stream<? extends T>... streams) {
      boolean isParallel = false;
      int characteristics = 336;
      long estimatedSize = 0L;
      ImmutableList.Builder<Spliterator<? extends T>> splitrsBuilder = new ImmutableList.Builder(streams.length);
      Stream[] var6 = streams;
      int var7 = streams.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         Stream<? extends T> stream = var6[var8];
         isParallel |= stream.isParallel();
         Spliterator<? extends T> splitr = stream.spliterator();
         splitrsBuilder.add((Object)splitr);
         characteristics &= splitr.characteristics();
         estimatedSize = LongMath.saturatedAdd(estimatedSize, splitr.estimateSize());
      }

      return (Stream)StreamSupport.stream(CollectSpliterators.flatMap(splitrsBuilder.build().spliterator(), (splitrx) -> {
         return splitrx;
      }, characteristics, estimatedSize), isParallel).onClose(() -> {
         closeAll(streams);
      });
   }

   public static IntStream concat(IntStream... streams) {
      boolean isParallel = false;
      int characteristics = 336;
      long estimatedSize = 0L;
      ImmutableList.Builder<OfInt> splitrsBuilder = new ImmutableList.Builder(streams.length);
      IntStream[] var6 = streams;
      int var7 = streams.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         IntStream stream = var6[var8];
         isParallel |= stream.isParallel();
         OfInt splitr = stream.spliterator();
         splitrsBuilder.add((Object)splitr);
         characteristics &= splitr.characteristics();
         estimatedSize = LongMath.saturatedAdd(estimatedSize, splitr.estimateSize());
      }

      return (IntStream)StreamSupport.intStream(CollectSpliterators.flatMapToInt(splitrsBuilder.build().spliterator(), (splitrx) -> {
         return splitrx;
      }, characteristics, estimatedSize), isParallel).onClose(() -> {
         closeAll(streams);
      });
   }

   public static LongStream concat(LongStream... streams) {
      boolean isParallel = false;
      int characteristics = 336;
      long estimatedSize = 0L;
      ImmutableList.Builder<OfLong> splitrsBuilder = new ImmutableList.Builder(streams.length);
      LongStream[] var6 = streams;
      int var7 = streams.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         LongStream stream = var6[var8];
         isParallel |= stream.isParallel();
         OfLong splitr = stream.spliterator();
         splitrsBuilder.add((Object)splitr);
         characteristics &= splitr.characteristics();
         estimatedSize = LongMath.saturatedAdd(estimatedSize, splitr.estimateSize());
      }

      return (LongStream)StreamSupport.longStream(CollectSpliterators.flatMapToLong(splitrsBuilder.build().spliterator(), (splitrx) -> {
         return splitrx;
      }, characteristics, estimatedSize), isParallel).onClose(() -> {
         closeAll(streams);
      });
   }

   public static DoubleStream concat(DoubleStream... streams) {
      boolean isParallel = false;
      int characteristics = 336;
      long estimatedSize = 0L;
      ImmutableList.Builder<OfDouble> splitrsBuilder = new ImmutableList.Builder(streams.length);
      DoubleStream[] var6 = streams;
      int var7 = streams.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         DoubleStream stream = var6[var8];
         isParallel |= stream.isParallel();
         OfDouble splitr = stream.spliterator();
         splitrsBuilder.add((Object)splitr);
         characteristics &= splitr.characteristics();
         estimatedSize = LongMath.saturatedAdd(estimatedSize, splitr.estimateSize());
      }

      return (DoubleStream)StreamSupport.doubleStream(CollectSpliterators.flatMapToDouble(splitrsBuilder.build().spliterator(), (splitrx) -> {
         return splitrx;
      }, characteristics, estimatedSize), isParallel).onClose(() -> {
         closeAll(streams);
      });
   }

   @Beta
   public static <A, B, R> Stream<R> zip(Stream<A> streamA, Stream<B> streamB, final BiFunction<? super A, ? super B, R> function) {
      Preconditions.checkNotNull(streamA);
      Preconditions.checkNotNull(streamB);
      Preconditions.checkNotNull(function);
      boolean isParallel = streamA.isParallel() || streamB.isParallel();
      Spliterator<A> splitrA = streamA.spliterator();
      Spliterator<B> splitrB = streamB.spliterator();
      int characteristics = splitrA.characteristics() & splitrB.characteristics() & 80;
      final Iterator<A> itrA = Spliterators.iterator(splitrA);
      final Iterator<B> itrB = Spliterators.iterator(splitrB);
      Stream var10000 = StreamSupport.stream(new AbstractSpliterator<R>(Math.min(splitrA.estimateSize(), splitrB.estimateSize()), characteristics) {
         public boolean tryAdvance(Consumer<? super R> action) {
            if (itrA.hasNext() && itrB.hasNext()) {
               action.accept(function.apply(itrA.next(), itrB.next()));
               return true;
            } else {
               return false;
            }
         }
      }, isParallel);
      Objects.requireNonNull(streamA);
      var10000 = (Stream)var10000.onClose(streamA::close);
      Objects.requireNonNull(streamB);
      return (Stream)var10000.onClose(streamB::close);
   }

   @Beta
   public static <A, B> void forEachPair(Stream<A> streamA, Stream<B> streamB, BiConsumer<? super A, ? super B> consumer) {
      Preconditions.checkNotNull(consumer);
      if (!streamA.isParallel() && !streamB.isParallel()) {
         Iterator<A> iterA = streamA.iterator();
         Iterator iterB = streamB.iterator();

         while(iterA.hasNext() && iterB.hasNext()) {
            consumer.accept(iterA.next(), iterB.next());
         }
      } else {
         zip(streamA, streamB, Streams.TemporaryPair::new).forEach((pair) -> {
            consumer.accept(pair.a, pair.b);
         });
      }

   }

   public static <T, R> Stream<R> mapWithIndex(Stream<T> stream, final Streams.FunctionWithIndex<? super T, ? extends R> function) {
      Preconditions.checkNotNull(stream);
      Preconditions.checkNotNull(function);
      boolean isParallel = stream.isParallel();
      Spliterator<T> fromSpliterator = stream.spliterator();
      Stream var10000;
      if (!fromSpliterator.hasCharacteristics(16384)) {
         final Iterator<T> fromIterator = Spliterators.iterator(fromSpliterator);
         var10000 = StreamSupport.stream(new AbstractSpliterator<R>(fromSpliterator.estimateSize(), fromSpliterator.characteristics() & 80) {
            long index = 0L;

            public boolean tryAdvance(Consumer<? super R> action) {
               if (fromIterator.hasNext()) {
                  action.accept(function.apply(fromIterator.next(), (long)(this.index++)));
                  return true;
               } else {
                  return false;
               }
            }
         }, isParallel);
         Objects.requireNonNull(stream);
         return (Stream)var10000.onClose(stream::close);
      } else {
         class Splitr extends Streams.MapWithIndexSpliterator<Spliterator<T>, R, Splitr> implements Consumer<T> {
            @CheckForNull
            T holder;

            Splitr(Spliterator<T> splitr, long index) {
               super(splitr, index);
            }

            public void accept(@ParametricNullness T t) {
               this.holder = t;
            }

            public boolean tryAdvance(Consumer<? super R> action) {
               if (this.fromSpliterator.tryAdvance(this)) {
                  boolean var2;
                  try {
                     action.accept(function.apply(NullnessCasts.uncheckedCastNullableTToT(this.holder), (long)(this.index++)));
                     var2 = true;
                  } finally {
                     this.holder = null;
                  }

                  return var2;
               } else {
                  return false;
               }
            }

            Splitr createSplit(Spliterator<T> from, long i) {
               return new Splitr(from, i);
            }
         }

         var10000 = StreamSupport.stream(new Splitr(fromSpliterator, 0L), isParallel);
         Objects.requireNonNull(stream);
         return (Stream)var10000.onClose(stream::close);
      }
   }

   public static <R> Stream<R> mapWithIndex(IntStream stream, final Streams.IntFunctionWithIndex<R> function) {
      Preconditions.checkNotNull(stream);
      Preconditions.checkNotNull(function);
      boolean isParallel = stream.isParallel();
      OfInt fromSpliterator = stream.spliterator();
      Stream var10000;
      if (!fromSpliterator.hasCharacteristics(16384)) {
         final java.util.PrimitiveIterator.OfInt fromIterator = Spliterators.iterator(fromSpliterator);
         var10000 = StreamSupport.stream(new AbstractSpliterator<R>(fromSpliterator.estimateSize(), fromSpliterator.characteristics() & 80) {
            long index = 0L;

            public boolean tryAdvance(Consumer<? super R> action) {
               if (fromIterator.hasNext()) {
                  action.accept(function.apply(fromIterator.nextInt(), (long)(this.index++)));
                  return true;
               } else {
                  return false;
               }
            }
         }, isParallel);
         Objects.requireNonNull(stream);
         return (Stream)var10000.onClose(stream::close);
      } else {
         class Splitr extends Streams.MapWithIndexSpliterator<OfInt, R, Splitr> implements IntConsumer, Spliterator<R> {
            int holder;

            Splitr(OfInt splitr, long index) {
               super(splitr, index);
            }

            public void accept(int t) {
               this.holder = t;
            }

            public boolean tryAdvance(Consumer<? super R> action) {
               if (((OfInt)this.fromSpliterator).tryAdvance(this)) {
                  action.accept(function.apply(this.holder, (long)(this.index++)));
                  return true;
               } else {
                  return false;
               }
            }

            Splitr createSplit(OfInt from, long i) {
               return new Splitr(from, i);
            }
         }

         var10000 = StreamSupport.stream(new Splitr(fromSpliterator, 0L), isParallel);
         Objects.requireNonNull(stream);
         return (Stream)var10000.onClose(stream::close);
      }
   }

   public static <R> Stream<R> mapWithIndex(LongStream stream, final Streams.LongFunctionWithIndex<R> function) {
      Preconditions.checkNotNull(stream);
      Preconditions.checkNotNull(function);
      boolean isParallel = stream.isParallel();
      OfLong fromSpliterator = stream.spliterator();
      Stream var10000;
      if (!fromSpliterator.hasCharacteristics(16384)) {
         final java.util.PrimitiveIterator.OfLong fromIterator = Spliterators.iterator(fromSpliterator);
         var10000 = StreamSupport.stream(new AbstractSpliterator<R>(fromSpliterator.estimateSize(), fromSpliterator.characteristics() & 80) {
            long index = 0L;

            public boolean tryAdvance(Consumer<? super R> action) {
               if (fromIterator.hasNext()) {
                  action.accept(function.apply(fromIterator.nextLong(), (long)(this.index++)));
                  return true;
               } else {
                  return false;
               }
            }
         }, isParallel);
         Objects.requireNonNull(stream);
         return (Stream)var10000.onClose(stream::close);
      } else {
         class Splitr extends Streams.MapWithIndexSpliterator<OfLong, R, Splitr> implements LongConsumer, Spliterator<R> {
            long holder;

            Splitr(OfLong splitr, long index) {
               super(splitr, index);
            }

            public void accept(long t) {
               this.holder = t;
            }

            public boolean tryAdvance(Consumer<? super R> action) {
               if (((OfLong)this.fromSpliterator).tryAdvance(this)) {
                  action.accept(function.apply(this.holder, (long)(this.index++)));
                  return true;
               } else {
                  return false;
               }
            }

            Splitr createSplit(OfLong from, long i) {
               return new Splitr(from, i);
            }
         }

         var10000 = StreamSupport.stream(new Splitr(fromSpliterator, 0L), isParallel);
         Objects.requireNonNull(stream);
         return (Stream)var10000.onClose(stream::close);
      }
   }

   public static <R> Stream<R> mapWithIndex(DoubleStream stream, final Streams.DoubleFunctionWithIndex<R> function) {
      Preconditions.checkNotNull(stream);
      Preconditions.checkNotNull(function);
      boolean isParallel = stream.isParallel();
      OfDouble fromSpliterator = stream.spliterator();
      Stream var10000;
      if (!fromSpliterator.hasCharacteristics(16384)) {
         final java.util.PrimitiveIterator.OfDouble fromIterator = Spliterators.iterator(fromSpliterator);
         var10000 = StreamSupport.stream(new AbstractSpliterator<R>(fromSpliterator.estimateSize(), fromSpliterator.characteristics() & 80) {
            long index = 0L;

            public boolean tryAdvance(Consumer<? super R> action) {
               if (fromIterator.hasNext()) {
                  action.accept(function.apply(fromIterator.nextDouble(), (long)(this.index++)));
                  return true;
               } else {
                  return false;
               }
            }
         }, isParallel);
         Objects.requireNonNull(stream);
         return (Stream)var10000.onClose(stream::close);
      } else {
         class Splitr extends Streams.MapWithIndexSpliterator<OfDouble, R, Splitr> implements DoubleConsumer, Spliterator<R> {
            double holder;

            Splitr(OfDouble splitr, long index) {
               super(splitr, index);
            }

            public void accept(double t) {
               this.holder = t;
            }

            public boolean tryAdvance(Consumer<? super R> action) {
               if (((OfDouble)this.fromSpliterator).tryAdvance(this)) {
                  action.accept(function.apply(this.holder, (long)(this.index++)));
                  return true;
               } else {
                  return false;
               }
            }

            Splitr createSplit(OfDouble from, long i) {
               return new Splitr(from, i);
            }
         }

         var10000 = StreamSupport.stream(new Splitr(fromSpliterator, 0L), isParallel);
         Objects.requireNonNull(stream);
         return (Stream)var10000.onClose(stream::close);
      }
   }

   public static <T> java.util.Optional<T> findLast(Stream<T> stream) {
      class OptionalState {
         boolean set = false;
         @CheckForNull
         T value = null;

         void set(T value) {
            this.set = true;
            this.value = value;
         }

         T get() {
            return Objects.requireNonNull(this.value);
         }
      }

      OptionalState state = new OptionalState();
      Deque<Spliterator<T>> splits = new ArrayDeque();
      splits.addLast(stream.spliterator());

      while(true) {
         while(true) {
            Spliterator spliterator;
            do {
               if (splits.isEmpty()) {
                  return java.util.Optional.empty();
               }

               spliterator = (Spliterator)splits.removeLast();
            } while(spliterator.getExactSizeIfKnown() == 0L);

            Spliterator prefix;
            if (spliterator.hasCharacteristics(16384)) {
               while(true) {
                  prefix = spliterator.trySplit();
                  if (prefix == null || prefix.getExactSizeIfKnown() == 0L) {
                     break;
                  }

                  if (spliterator.getExactSizeIfKnown() == 0L) {
                     spliterator = prefix;
                     break;
                  }
               }

               Objects.requireNonNull(state);
               spliterator.forEachRemaining(state::set);
               return java.util.Optional.of(state.get());
            }

            prefix = spliterator.trySplit();
            if (prefix != null && prefix.getExactSizeIfKnown() != 0L) {
               splits.addLast(prefix);
               splits.addLast(spliterator);
            } else {
               Objects.requireNonNull(state);
               spliterator.forEachRemaining(state::set);
               if (state.set) {
                  return java.util.Optional.of(state.get());
               }
            }
         }
      }
   }

   public static OptionalInt findLast(IntStream stream) {
      java.util.Optional<Integer> boxedLast = findLast(stream.boxed());
      return (OptionalInt)boxedLast.map(OptionalInt::of).orElseGet(OptionalInt::empty);
   }

   public static OptionalLong findLast(LongStream stream) {
      java.util.Optional<Long> boxedLast = findLast(stream.boxed());
      return (OptionalLong)boxedLast.map(OptionalLong::of).orElseGet(OptionalLong::empty);
   }

   public static OptionalDouble findLast(DoubleStream stream) {
      java.util.Optional<Double> boxedLast = findLast(stream.boxed());
      return (OptionalDouble)boxedLast.map(OptionalDouble::of).orElseGet(OptionalDouble::empty);
   }

   private Streams() {
   }

   public interface DoubleFunctionWithIndex<R> {
      @ParametricNullness
      R apply(double var1, long var3);
   }

   public interface LongFunctionWithIndex<R> {
      @ParametricNullness
      R apply(long var1, long var3);
   }

   public interface IntFunctionWithIndex<R> {
      @ParametricNullness
      R apply(int var1, long var2);
   }

   private abstract static class MapWithIndexSpliterator<F extends Spliterator<?>, R, S extends Streams.MapWithIndexSpliterator<F, R, S>> implements Spliterator<R> {
      final F fromSpliterator;
      long index;

      MapWithIndexSpliterator(F fromSpliterator, long index) {
         this.fromSpliterator = fromSpliterator;
         this.index = index;
      }

      abstract S createSplit(F var1, long var2);

      @CheckForNull
      public S trySplit() {
         Spliterator<?> splitOrNull = this.fromSpliterator.trySplit();
         if (splitOrNull == null) {
            return null;
         } else {
            S result = this.createSplit(splitOrNull, this.index);
            this.index += splitOrNull.getExactSizeIfKnown();
            return result;
         }
      }

      public long estimateSize() {
         return this.fromSpliterator.estimateSize();
      }

      public int characteristics() {
         return this.fromSpliterator.characteristics() & 16464;
      }
   }

   public interface FunctionWithIndex<T, R> {
      @ParametricNullness
      R apply(@ParametricNullness T var1, long var2);
   }

   private static class TemporaryPair<A, B> {
      @ParametricNullness
      final A a;
      @ParametricNullness
      final B b;

      TemporaryPair(@ParametricNullness A a, @ParametricNullness B b) {
         this.a = a;
         this.b = b;
      }
   }
}
