/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3.stream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.function.Failable;
import org.apache.commons.lang3.function.FailableConsumer;
import org.apache.commons.lang3.function.FailableFunction;
import org.apache.commons.lang3.function.FailablePredicate;

public class Streams {
    public static <T> FailableStream<T> failableStream(Collection<T> collection) {
        return Streams.failableStream(Streams.of(collection));
    }

    public static <T> FailableStream<T> failableStream(Stream<T> stream) {
        return new FailableStream<T>(stream);
    }

    public static <T> FailableStream<T> failableStream(T t) {
        return Streams.failableStream(Streams.streamOf(t));
    }

    @SafeVarargs
    public static <T> FailableStream<T> failableStream(T ... TArray) {
        return Streams.failableStream(Streams.of(TArray));
    }

    public static <E> Stream<E> instancesOf(Class<? super E> clazz, Collection<? super E> collection) {
        return Streams.instancesOf(clazz, Streams.of(collection));
    }

    private static <E> Stream<E> instancesOf(Class<? super E> clazz, Stream<?> stream) {
        return Streams.of(stream).filter(clazz::isInstance);
    }

    public static <E> Stream<E> nonNull(Collection<E> collection) {
        return Streams.of(collection).filter(Objects::nonNull);
    }

    public static <E> Stream<E> nonNull(E e) {
        return Streams.nonNull(Streams.streamOf(e));
    }

    @SafeVarargs
    public static <E> Stream<E> nonNull(E ... EArray) {
        return Streams.nonNull(Streams.of(EArray));
    }

    public static <E> Stream<E> nonNull(Stream<E> stream) {
        return Streams.of(stream).filter(Objects::nonNull);
    }

    public static <E> Stream<E> of(Collection<E> collection) {
        return collection == null ? Stream.empty() : collection.stream();
    }

    public static <E> Stream<E> of(Enumeration<E> enumeration) {
        return StreamSupport.stream(new EnumerationSpliterator<E>(Long.MAX_VALUE, 16, enumeration), false);
    }

    public static <E> Stream<E> of(Iterable<E> iterable) {
        return iterable == null ? Stream.empty() : StreamSupport.stream(iterable.spliterator(), false);
    }

    public static <E> Stream<E> of(Iterator<E> iterator) {
        return iterator == null ? Stream.empty() : StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 16), false);
    }

    private static <E> Stream<E> of(Stream<E> stream) {
        return stream == null ? Stream.empty() : stream;
    }

    @SafeVarargs
    public static <T> Stream<T> of(T ... TArray) {
        return TArray == null ? Stream.empty() : Stream.of(TArray);
    }

    @Deprecated
    public static <E> FailableStream<E> stream(Collection<E> collection) {
        return Streams.failableStream(collection);
    }

    @Deprecated
    public static <T> FailableStream<T> stream(Stream<T> stream) {
        return Streams.failableStream(stream);
    }

    private static <T> Stream<T> streamOf(T t) {
        return t == null ? Stream.empty() : Stream.of(t);
    }

    public static <T> Collector<T, ?, T[]> toArray(Class<T> clazz) {
        return new ArrayCollector<T>(clazz);
    }

    @Deprecated
    public Streams() {
    }

    public static class FailableStream<T> {
        private Stream<T> stream;
        private boolean terminated;

        public FailableStream(Stream<T> stream) {
            this.stream = stream;
        }

        public boolean allMatch(FailablePredicate<T, ?> failablePredicate) {
            this.assertNotTerminated();
            return this.stream().allMatch(Failable.asPredicate(failablePredicate));
        }

        public boolean anyMatch(FailablePredicate<T, ?> failablePredicate) {
            this.assertNotTerminated();
            return this.stream().anyMatch(Failable.asPredicate(failablePredicate));
        }

        protected void assertNotTerminated() {
            if (this.terminated) {
                throw new IllegalStateException("This stream is already terminated.");
            }
        }

        public <A, R> R collect(Collector<? super T, A, R> collector) {
            this.makeTerminated();
            return this.stream().collect(collector);
        }

        public <A, R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> biConsumer, BiConsumer<R, R> biConsumer2) {
            this.makeTerminated();
            return this.stream().collect(supplier, biConsumer, biConsumer2);
        }

        public FailableStream<T> filter(FailablePredicate<T, ?> failablePredicate) {
            this.assertNotTerminated();
            this.stream = this.stream.filter(Failable.asPredicate(failablePredicate));
            return this;
        }

        public void forEach(FailableConsumer<T, ?> failableConsumer) {
            this.makeTerminated();
            this.stream().forEach(Failable.asConsumer(failableConsumer));
        }

        protected void makeTerminated() {
            this.assertNotTerminated();
            this.terminated = true;
        }

        public <R> FailableStream<R> map(FailableFunction<T, R, ?> failableFunction) {
            this.assertNotTerminated();
            return new FailableStream<R>(this.stream.map(Failable.asFunction(failableFunction)));
        }

        public T reduce(T t, BinaryOperator<T> binaryOperator) {
            this.makeTerminated();
            return this.stream().reduce(t, binaryOperator);
        }

        public Stream<T> stream() {
            return this.stream;
        }
    }

    private static final class EnumerationSpliterator<T>
    extends Spliterators.AbstractSpliterator<T> {
        private final Enumeration<T> enumeration;

        protected EnumerationSpliterator(long l, int n, Enumeration<T> enumeration) {
            super(l, n);
            this.enumeration = Objects.requireNonNull(enumeration, "enumeration");
        }

        @Override
        public void forEachRemaining(Consumer<? super T> consumer) {
            while (this.enumeration.hasMoreElements()) {
                this.next(consumer);
            }
        }

        private boolean next(Consumer<? super T> consumer) {
            consumer.accept(this.enumeration.nextElement());
            return true;
        }

        @Override
        public boolean tryAdvance(Consumer<? super T> consumer) {
            return this.enumeration.hasMoreElements() && this.next(consumer);
        }
    }

    public static class ArrayCollector<E>
    implements Collector<E, List<E>, E[]> {
        private static final Set<Collector.Characteristics> characteristics = Collections.emptySet();
        private final Class<E> elementType;

        public ArrayCollector(Class<E> clazz) {
            this.elementType = Objects.requireNonNull(clazz, "elementType");
        }

        @Override
        public BiConsumer<List<E>, E> accumulator() {
            return List::add;
        }

        @Override
        public Set<Collector.Characteristics> characteristics() {
            return characteristics;
        }

        @Override
        public BinaryOperator<List<E>> combiner() {
            return (list, list2) -> {
                list.addAll(list2);
                return list;
            };
        }

        @Override
        public Function<List<E>, E[]> finisher() {
            return list -> list.toArray(ArrayUtils.newInstance(this.elementType, list.size()));
        }

        @Override
        public Supplier<List<E>> supplier() {
            return ArrayList::new;
        }
    }
}

