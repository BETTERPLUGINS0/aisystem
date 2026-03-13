/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.io.function;

import java.io.IOException;
import java.util.Objects;
import org.apache.commons.io.function.IOFunction;

@FunctionalInterface
public interface IOTriFunction<T, U, V, R> {
    default public <W> IOTriFunction<T, U, V, W> andThen(IOFunction<? super R, ? extends W> after) {
        Objects.requireNonNull(after);
        return (t, u, v) -> after.apply((R)this.apply(t, u, v));
    }

    public R apply(T var1, U var2, V var3) throws IOException;
}

