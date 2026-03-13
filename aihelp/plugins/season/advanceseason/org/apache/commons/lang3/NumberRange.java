/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3;

import java.util.Comparator;
import org.apache.commons.lang3.Range;

public class NumberRange<N extends Number>
extends Range<N> {
    private static final long serialVersionUID = 1L;

    public NumberRange(N n, N n2, Comparator<N> comparator) {
        super(n, n2, comparator);
    }
}

