/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3.text.translate;

import java.io.Writer;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

@Deprecated
public abstract class CodePointTranslator
extends CharSequenceTranslator {
    @Override
    public final int translate(CharSequence charSequence, int n, Writer writer) {
        int n2 = Character.codePointAt(charSequence, n);
        boolean bl = this.translate(n2, writer);
        return bl ? 1 : 0;
    }

    public abstract boolean translate(int var1, Writer var2);
}

