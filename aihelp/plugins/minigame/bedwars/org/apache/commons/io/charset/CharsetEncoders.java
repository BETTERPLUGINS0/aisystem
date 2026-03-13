/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.io.charset;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.function.Supplier;

public final class CharsetEncoders {
    public static CharsetEncoder toCharsetEncoder(CharsetEncoder charsetEncoder) {
        return CharsetEncoders.toCharsetEncoder(charsetEncoder, () -> Charset.defaultCharset().newEncoder());
    }

    public static CharsetEncoder toCharsetEncoder(CharsetEncoder charsetEncoder, Supplier<CharsetEncoder> defaultSupplier) {
        return charsetEncoder != null ? charsetEncoder : defaultSupplier.get();
    }

    private CharsetEncoders() {
    }
}

