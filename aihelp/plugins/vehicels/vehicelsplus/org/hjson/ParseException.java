/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.hjson;

public class ParseException
extends RuntimeException {
    private final int offset;
    private final int line;
    private final int column;

    ParseException(String string, int n, int n2, int n3) {
        super(string + " at " + n2 + ":" + n3);
        this.offset = n;
        this.line = n2;
        this.column = n3;
    }

    public int getOffset() {
        return this.offset;
    }

    public int getLine() {
        return this.line;
    }

    public int getColumn() {
        return this.column;
    }
}

