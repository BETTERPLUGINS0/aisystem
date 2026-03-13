/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.io.output;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.function.Uncheck;

public final class UncheckedFilterOutputStream
extends FilterOutputStream {
    public static Builder builder() {
        return new Builder();
    }

    private UncheckedFilterOutputStream(OutputStream outputStream) {
        super(outputStream);
    }

    @Override
    public void close() throws UncheckedIOException {
        Uncheck.run(() -> super.close());
    }

    @Override
    public void flush() throws UncheckedIOException {
        Uncheck.run(() -> super.flush());
    }

    @Override
    public void write(byte[] b2) throws UncheckedIOException {
        Uncheck.accept(x$0 -> super.write((byte[])x$0), b2);
    }

    @Override
    public void write(byte[] b2, int off, int len) throws UncheckedIOException {
        Uncheck.accept((x$0, x$1, x$2) -> super.write((byte[])x$0, (int)x$1, (int)x$2), b2, off, len);
    }

    @Override
    public void write(int b2) throws UncheckedIOException {
        Uncheck.accept(x$0 -> super.write((int)x$0), b2);
    }

    public static class Builder
    extends AbstractStreamBuilder<UncheckedFilterOutputStream, Builder> {
        @Override
        public UncheckedFilterOutputStream get() throws IOException {
            return new UncheckedFilterOutputStream(this.getOutputStream());
        }
    }
}

