/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3;

import java.nio.charset.Charset;

final class Charsets {
    Charsets() {
    }

    static Charset toCharset(Charset charset) {
        return charset == null ? Charset.defaultCharset() : charset;
    }

    static Charset toCharset(String string) {
        return string == null ? Charset.defaultCharset() : Charset.forName(string);
    }

    static String toCharsetName(String string) {
        return string == null ? Charset.defaultCharset().name() : string;
    }
}

