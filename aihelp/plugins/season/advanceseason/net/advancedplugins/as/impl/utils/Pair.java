/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.impl.utils;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class Pair<T, V> {
    private T key;
    private V value;

    public static <S, U> Pair<S, U> of(S s, U u) {
        return new Pair<S, U>(s, u);
    }

    public Pair(T t, V v) {
        this.key = t;
        this.value = v;
    }

    public T getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    public void setKey(T t) {
        this.key = t;
    }

    public void setValue(V v) {
        this.value = v;
    }
}

