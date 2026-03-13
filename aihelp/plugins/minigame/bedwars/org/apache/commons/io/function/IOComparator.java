/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.io.function;

import java.io.IOException;
import java.util.Comparator;
import org.apache.commons.io.function.Uncheck;

@FunctionalInterface
public interface IOComparator<T> {
    default public Comparator<T> asComparator() {
        return (t, u) -> Uncheck.compare(this, t, u);
    }

    public int compare(T var1, T var2) throws IOException;
}

