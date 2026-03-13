/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3.text.translate;

import java.io.Writer;
import org.apache.commons.lang3.text.translate.CodePointTranslator;

@Deprecated
public class UnicodeUnpairedSurrogateRemover
extends CodePointTranslator {
    @Override
    public boolean translate(int n, Writer writer) {
        return n >= 55296 && n <= 57343;
    }
}

