/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.io.input;

import java.io.IOException;
import java.io.Reader;

public class ClosedReader
extends Reader {
    public static final ClosedReader INSTANCE;
    @Deprecated
    public static final ClosedReader CLOSED_READER;

    @Override
    public void close() throws IOException {
    }

    @Override
    public int read(char[] cbuf, int off, int len) {
        return -1;
    }

    static {
        CLOSED_READER = INSTANCE = new ClosedReader();
    }
}

