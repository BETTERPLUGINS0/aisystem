/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3.stream;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public final class LangCollectors {
    private static final Set<Collector.Characteristics> CH_NOID = Collections.emptySet();

    public static <T, R, A> R collect(Collector<? super T, A, R> collector, T ... TArray) {
        return Arrays.stream(TArray).collect(collector);
    }

    public static Collector<Object, ?, String> joining() {
        return new SimpleCollector(StringBuilder::new, StringBuilder::append, StringBuilder::append, StringBuilder::toString, CH_NOID);
    }

    public static Collector<Object, ?, String> joining(CharSequence charSequence) {
        return LangCollectors.joining(charSequence, "", "");
    }

    public static Collector<Object, ?, String> joining(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3) {
        return LangCollectors.joining(charSequence, charSequence2, charSequence3, Objects::toString);
    }

    public static Collector<Object, ?, String> joining(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, Function<Object, String> function) {
        return new SimpleCollector(() -> new StringJoiner(charSequence, charSequence2, charSequence3), (stringJoiner, object) -> stringJoiner.add((CharSequence)function.apply(object)), StringJoiner::merge, StringJoiner::toString, CH_NOID);
    }

    private LangCollectors() {
    }

    private static final class SimpleCollector<T, A, R>
    implements Collector<T, A, R> {
        private final BiConsumer<A, T> accumulator;
        private final Set<Collector.Characteristics> characteristics;
        private final BinaryOperator<A> combiner;
        private final Function<A, R> finisher;
        private final Supplier<A> supplier;

        private SimpleCollector(Supplier<A> supplier, BiConsumer<A, T> biConsumer, BinaryOperator<A> binaryOperator, Function<A, R> function, Set<Collector.Characteristics> set) {
            this.supplier = supplier;
            this.accumulator = biConsumer;
            this.combiner = binaryOperator;
            this.finisher = function;
            this.characteristics = set;
        }

        @Override
        public BiConsumer<A, T> accumulator() {
            return this.accumulator;
        }

        @Override
        public Set<Collector.Characteristics> characteristics() {
            return this.characteristics;
        }

        @Override
        public BinaryOperator<A> combiner() {
            return this.combiner;
        }

        @Override
        public Function<A, R> finisher() {
            return this.finisher;
        }

        @Override
        public Supplier<A> supplier() {
            return this.supplier;
        }
    }
}

