/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.examination.string;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import net.kyori.examination.AbstractExaminer;
import net.kyori.examination.string.StringExaminer;
import net.kyori.examination.string.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MultiLineStringExaminer
extends AbstractExaminer<Stream<String>> {
    private static final String INDENT_2 = "  ";
    private final StringExaminer examiner;

    @NotNull
    public static MultiLineStringExaminer simpleEscaping() {
        return Instances.SIMPLE_ESCAPING;
    }

    public MultiLineStringExaminer(@NotNull StringExaminer stringExaminer) {
        this.examiner = stringExaminer;
    }

    @Override
    @NotNull
    protected <E> Stream<String> array(E @NotNull [] EArray, @NotNull Stream<Stream<String>> stream) {
        return this.arrayLike(stream);
    }

    @Override
    @NotNull
    protected <E> Stream<String> collection(@NotNull Collection<E> collection, @NotNull Stream<Stream<String>> stream) {
        return this.arrayLike(stream);
    }

    @Override
    @NotNull
    protected Stream<String> examinable(@NotNull String string, @NotNull Stream<Map.Entry<String, Stream<String>>> stream) {
        Stream<String> stream2 = MultiLineStringExaminer.flatten(",", stream.map(entry -> MultiLineStringExaminer.association((Stream<String>)this.examine((String)entry.getKey()), " = ", (Stream)entry.getValue())));
        Stream<String> stream3 = MultiLineStringExaminer.indent(stream2);
        return MultiLineStringExaminer.enclose(stream3, string + "{", "}");
    }

    @Override
    @NotNull
    protected <K, V> Stream<String> map(@NotNull Map<K, V> map, @NotNull Stream<Map.Entry<Stream<String>, Stream<String>>> stream) {
        Stream<String> stream2 = MultiLineStringExaminer.flatten(",", stream.map(entry -> MultiLineStringExaminer.association((Stream)entry.getKey(), " = ", (Stream)entry.getValue())));
        Stream<String> stream3 = MultiLineStringExaminer.indent(stream2);
        return MultiLineStringExaminer.enclose(stream3, "{", "}");
    }

    @Override
    @NotNull
    protected Stream<String> nil() {
        return Stream.of(this.examiner.nil());
    }

    @Override
    @NotNull
    protected Stream<String> scalar(@NotNull Object object) {
        return Stream.of(this.examiner.scalar(object));
    }

    @Override
    @NotNull
    public Stream<String> examine(boolean bl) {
        return Stream.of(this.examiner.examine(bl));
    }

    @Override
    @NotNull
    public Stream<String> examine(byte by) {
        return Stream.of(this.examiner.examine(by));
    }

    @Override
    @NotNull
    public Stream<String> examine(char c) {
        return Stream.of(this.examiner.examine(c));
    }

    @Override
    @NotNull
    public Stream<String> examine(double d) {
        return Stream.of(this.examiner.examine(d));
    }

    @Override
    @NotNull
    public Stream<String> examine(float f) {
        return Stream.of(this.examiner.examine(f));
    }

    @Override
    @NotNull
    public Stream<String> examine(int n) {
        return Stream.of(this.examiner.examine(n));
    }

    @Override
    @NotNull
    public Stream<String> examine(long l) {
        return Stream.of(this.examiner.examine(l));
    }

    @Override
    @NotNull
    public Stream<String> examine(short s) {
        return Stream.of(this.examiner.examine(s));
    }

    @Override
    @NotNull
    protected Stream<String> array(int n, IntFunction<Stream<String>> intFunction) {
        return this.arrayLike(n == 0 ? Stream.empty() : IntStream.range(0, n).mapToObj(intFunction));
    }

    @Override
    @NotNull
    protected <T> Stream<String> stream(@NotNull Stream<T> stream) {
        return this.arrayLike(stream.map(this::examine));
    }

    @Override
    @NotNull
    protected Stream<String> stream(@NotNull DoubleStream doubleStream) {
        return this.arrayLike(doubleStream.mapToObj(d -> this.examine(d)));
    }

    @Override
    @NotNull
    protected Stream<String> stream(@NotNull IntStream intStream) {
        return this.arrayLike(intStream.mapToObj(n -> this.examine(n)));
    }

    @Override
    @NotNull
    protected Stream<String> stream(@NotNull LongStream longStream) {
        return this.arrayLike(longStream.mapToObj(l -> this.examine(l)));
    }

    @Override
    @NotNull
    public Stream<String> examine(@Nullable String string) {
        return Stream.of(this.examiner.examine(string));
    }

    private Stream<String> arrayLike(Stream<Stream<String>> stream) {
        Stream<String> stream2 = MultiLineStringExaminer.flatten(",", stream);
        Stream<String> stream3 = MultiLineStringExaminer.indent(stream2);
        return MultiLineStringExaminer.enclose(stream3, "[", "]");
    }

    private static Stream<String> enclose(Stream<String> stream, String string, String string2) {
        return MultiLineStringExaminer.enclose(stream.collect(Collectors.toList()), string, string2);
    }

    private static Stream<String> enclose(List<String> list, String string, String string2) {
        if (list.isEmpty()) {
            return Stream.of(string + string2);
        }
        return Stream.of(Stream.of(string), MultiLineStringExaminer.indent(list.stream()), Stream.of(string2)).reduce(Stream.empty(), Stream::concat);
    }

    private static Stream<String> flatten(String string, Stream<Stream<String>> stream2) {
        ArrayList arrayList = new ArrayList();
        stream2.forEachOrdered(stream -> {
            if (!arrayList.isEmpty()) {
                int n = arrayList.size() - 1;
                arrayList.set(n, (String)arrayList.get(n) + string);
            }
            stream.forEachOrdered(arrayList::add);
        });
        return arrayList.stream();
    }

    private static Stream<String> association(Stream<String> stream, String string, Stream<String> stream2) {
        return MultiLineStringExaminer.association(stream.collect(Collectors.toList()), string, stream2.collect(Collectors.toList()));
    }

    private static Stream<String> association(List<String> list, String string, List<String> list2) {
        int n = list.size();
        int n2 = list2.size();
        int n3 = Math.max(n, n2);
        int n4 = Strings.maxLength(list.stream());
        String string2 = n < 2 ? "" : Strings.repeat(" ", n4);
        String string3 = n < 2 ? "" : Strings.repeat(" ", string.length());
        ArrayList<String> arrayList = new ArrayList<String>(n3);
        for (int i = 0; i < n3; ++i) {
            String string4 = i < n ? Strings.padEnd(list.get(i), n4, ' ') : string2;
            String string5 = i == 0 ? string : string3;
            String string6 = i < n2 ? list2.get(i) : "";
            arrayList.add(string4 + string5 + string6);
        }
        return arrayList.stream();
    }

    private static Stream<String> indent(Stream<String> stream) {
        return stream.map(string -> INDENT_2 + string);
    }

    private static final class Instances {
        static final MultiLineStringExaminer SIMPLE_ESCAPING = new MultiLineStringExaminer(StringExaminer.simpleEscaping());

        private Instances() {
        }
    }
}

