/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.io.input;

import java.io.InputStream;

public class ClosedInputStream
extends InputStream {
    public static final ClosedInputStream INSTANCE;
    @Deprecated
    public static final ClosedInputStream CLOSED_INPUT_STREAM;

    @Override
    public int read() {
        return -1;
    }

    static {
        CLOSED_INPUT_STREAM = INSTANCE = new ClosedInputStream();
    }
}

