/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.examination.string;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import net.kyori.examination.AbstractExaminer;
import net.kyori.examination.string.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StringExaminer
extends AbstractExaminer<String> {
    private static final Function<String, String> DEFAULT_ESCAPER = string -> string.replace("\"", "\\\"").replace("\\", "\\\\").replace("\b", "\\b").replace("\f", "\\f").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    private static final Collector<CharSequence, ?, String> COMMA_CURLY = Collectors.joining(", ", "{", "}");
    private static final Collector<CharSequence, ?, String> COMMA_SQUARE = Collectors.joining(", ", "[", "]");
    private final Function<String, String> escaper;

    @NotNull
    public static StringExaminer simpleEscaping() {
        return Instances.SIMPLE_ESCAPING;
    }

    public StringExaminer(@NotNull Function<String, String> function) {
        this.escaper = function;
    }

    @Override
    @NotNull
    protected <E> String array(E @NotNull [] EArray, @NotNull Stream<String> stream) {
        return stream.collect(COMMA_SQUARE);
    }

    @Override
    @NotNull
    protected <E> String collection(@NotNull Collection<E> collection, @NotNull Stream<String> stream) {
        return stream.collect(COMMA_SQUARE);
    }

    @Override
    @NotNull
    protected String examinable(@NotNull String string, @NotNull Stream<Map.Entry<String, String>> stream) {
        return string + stream.map(entry -> (String)entry.getKey() + '=' + (String)entry.getValue()).collect(COMMA_CURLY);
    }

    @Override
    @NotNull
    protected <K, V> String map(@NotNull Map<K, V> map, @NotNull Stream<Map.Entry<String, String>> stream) {
        return stream.map(entry -> (String)entry.getKey() + '=' + (String)entry.getValue()).collect(COMMA_CURLY);
    }

    @Override
    @NotNull
    protected String nil() {
        return "null";
    }

    @Override
    @NotNull
    protected String scalar(@NotNull Object object) {
        return String.valueOf(object);
    }

    @Override
    @NotNull
    public String examine(boolean bl) {
        return String.valueOf(bl);
    }

    @Override
    @NotNull
    public String examine(byte by) {
        return String.valueOf(by);
    }

    @Override
    @NotNull
    public String examine(char c) {
        return Strings.wrapIn(this.escaper.apply(String.valueOf(c)), '\'');
    }

    @Override
    @NotNull
    public String examine(double d) {
        return Strings.withSuffix(String.valueOf(d), 'd');
    }

    @Override
    @NotNull
    public String examine(float f) {
        return Strings.withSuffix(String.valueOf(f), 'f');
    }

    @Override
    @NotNull
    public String examine(int n) {
        return String.valueOf(n);
    }

    @Override
    @NotNull
    public String examine(long l) {
        return String.valueOf(l);
    }

    @Override
    @NotNull
    public String examine(short s) {
        return String.valueOf(s);
    }

    @Override
    @NotNull
    protected <T> String stream(@NotNull Stream<T> stream) {
        return stream.map(this::examine).collect(COMMA_SQUARE);
    }

    @Override
    @NotNull
    protected String stream(@NotNull DoubleStream doubleStream) {
        return doubleStream.mapToObj(this::examine).collect(COMMA_SQUARE);
    }

    @Override
    @NotNull
    protected String stream(@NotNull IntStream intStream) {
        return intStream.mapToObj(this::examine).collect(COMMA_SQUARE);
    }

    @Override
    @NotNull
    protected String stream(@NotNull LongStream longStream) {
        return longStream.mapToObj(this::examine).collect(COMMA_SQUARE);
    }

    @Override
    @NotNull
    public String examine(@Nullable String string) {
        if (string == null) {
            return this.nil();
        }
        return Strings.wrapIn(this.escaper.apply(string), '\"');
    }

    @Override
    @NotNull
    protected String array(int n, IntFunction<String> intFunction) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[');
        for (int i = 0; i < n; ++i) {
            stringBuilder.append(intFunction.apply(i));
            if (i + 1 >= n) continue;
            stringBuilder.append(", ");
        }
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    static /* synthetic */ Function access$000() {
        return DEFAULT_ESCAPER;
    }

    private static final class Instances {
        static final StringExaminer SIMPLE_ESCAPING = new StringExaminer(StringExaminer.access$000());

        private Instances() {
        }
    }
}

