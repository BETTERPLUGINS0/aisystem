/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.kyori.adventure.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

public final class MonkeyBars {
    private MonkeyBars() {
    }

    @SafeVarargs
    @NotNull
    public static <E extends Enum<E>> Set<E> enumSet(Class<E> clazz, E @NotNull ... EArray) {
        EnumSet<E> enumSet = EnumSet.noneOf(clazz);
        Collections.addAll(enumSet, EArray);
        return Collections.unmodifiableSet(enumSet);
    }

    @NotNull
    public static <T> List<T> addOne(@NotNull List<T> list, T t) {
        if (list.isEmpty()) {
            return Collections.singletonList(t);
        }
        ArrayList<T> arrayList = new ArrayList<T>(list.size() + 1);
        arrayList.addAll(list);
        arrayList.add(t);
        return Collections.unmodifiableList(arrayList);
    }

    @SafeVarargs
    @NotNull
    public static <I, O> List<O> nonEmptyArrayToList(@NotNull Function<I, O> function, @NotNull I i, @NotNull @NotNull I @NotNull ... IArray) {
        ArrayList<O> arrayList = new ArrayList<O>(IArray.length + 1);
        arrayList.add(function.apply(i));
        for (I i2 : IArray) {
            arrayList.add(Objects.requireNonNull(function.apply(Objects.requireNonNull(i2, "source[?]")), "mapper(source[?])"));
        }
        return Collections.unmodifiableList(arrayList);
    }

    @NotNull
    public static <I, O> List<O> toUnmodifiableList(@NotNull Function<I, O> function, @NotNull Iterable<? extends I> iterable) {
        ArrayList<O> arrayList = iterable instanceof Collection ? new ArrayList<O>(((Collection)iterable).size()) : new ArrayList();
        for (I i : iterable) {
            arrayList.add(Objects.requireNonNull(function.apply(Objects.requireNonNull(i, "source[?]")), "mapper(source[?])"));
        }
        return Collections.unmodifiableList(arrayList);
    }
}

