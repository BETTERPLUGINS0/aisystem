/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3.builder;

import java.lang.reflect.Field;
import java.util.Objects;

final class Reflection {
    Reflection() {
    }

    static Object getUnchecked(Field field, Object object) {
        try {
            return Objects.requireNonNull(field, "field").get(object);
        } catch (IllegalAccessException illegalAccessException) {
            throw new IllegalArgumentException(illegalAccessException);
        }
    }
}

