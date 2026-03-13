/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;

public class NullOutputStream
extends OutputStream {
    public static final NullOutputStream INSTANCE;
    @Deprecated
    public static final NullOutputStream NULL_OUTPUT_STREAM;

    @Deprecated
    public NullOutputStream() {
    }

    @Override
    public void write(byte[] b2) throws IOException {
    }

    @Override
    public void write(byte[] b2, int off, int len) {
    }

    @Override
    public void write(int b2) {
    }

    static {
        NULL_OUTPUT_STREAM = INSTANCE = new NullOutputStream();
    }
}

