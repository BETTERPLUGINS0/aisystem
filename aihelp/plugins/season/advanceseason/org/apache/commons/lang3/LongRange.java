/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3;

import org.apache.commons.lang3.NumberRange;

public final class LongRange
extends NumberRange<Long> {
    private static final long serialVersionUID = 1L;

    public static LongRange of(long l, long l2) {
        return LongRange.of((Long)l, (Long)l2);
    }

    public static LongRange of(Long l, Long l2) {
        return new LongRange(l, l2);
    }

    private LongRange(Long l, Long l2) {
        super(l, l2, null);
    }
}

