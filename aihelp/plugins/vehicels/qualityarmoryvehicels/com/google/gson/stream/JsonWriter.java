/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.google.gson.stream;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;

public class JsonWriter
implements Closeable,
Flushable {
    private static final String[] REPLACEMENT_CHARS = new String[128];
    private static final String[] HTML_SAFE_REPLACEMENT_CHARS;
    private final Writer out;
    private int[] stack = new int[32];
    private int stackSize = 0;
    private String indent;
    private String separator;
    private boolean lenient;
    private boolean htmlSafe;
    private String deferredName;
    private boolean serializeNulls;

    public JsonWriter(Writer writer) {
        this.push(6);
        this.separator = ":";
        this.serializeNulls = true;
        if (writer == null) {
            throw new NullPointerException("out == null");
        }
        this.out = writer;
    }

    public final void setIndent(String string) {
        if (string.length() == 0) {
            this.indent = null;
            this.separator = ":";
        } else {
            this.indent = string;
            this.separator = ": ";
        }
    }

    public final void setLenient(boolean bl) {
        this.lenient = bl;
    }

    public boolean isLenient() {
        return this.lenient;
    }

    public final void setHtmlSafe(boolean bl) {
        this.htmlSafe = bl;
    }

    public final boolean isHtmlSafe() {
        return this.htmlSafe;
    }

    public final void setSerializeNulls(boolean bl) {
        this.serializeNulls = bl;
    }

    public final boolean getSerializeNulls() {
        return this.serializeNulls;
    }

    public JsonWriter beginArray() {
        this.writeDeferredName();
        return this.open(1, "[");
    }

    public JsonWriter endArray() {
        return this.close(1, 2, "]");
    }

    public JsonWriter beginObject() {
        this.writeDeferredName();
        return this.open(3, "{");
    }

    public JsonWriter endObject() {
        return this.close(3, 5, "}");
    }

    private JsonWriter open(int n, String string) {
        this.beforeValue();
        this.push(n);
        this.out.write(string);
        return this;
    }

    private JsonWriter close(int n, int n2, String string) {
        int n3 = this.peek();
        if (n3 != n2 && n3 != n) {
            throw new IllegalStateException("Nesting problem.");
        }
        if (this.deferredName != null) {
            throw new IllegalStateException("Dangling name: " + this.deferredName);
        }
        --this.stackSize;
        if (n3 == n2) {
            this.newline();
        }
        this.out.write(string);
        return this;
    }

    private void push(int n) {
        if (this.stackSize == this.stack.length) {
            int[] nArray = new int[this.stackSize * 2];
            System.arraycopy(this.stack, 0, nArray, 0, this.stackSize);
            this.stack = nArray;
        }
        this.stack[this.stackSize++] = n;
    }

    private int peek() {
        if (this.stackSize == 0) {
            throw new IllegalStateException("JsonWriter is closed.");
        }
        return this.stack[this.stackSize - 1];
    }

    private void replaceTop(int n) {
        this.stack[this.stackSize - 1] = n;
    }

    public JsonWriter name(String string) {
        if (string == null) {
            throw new NullPointerException("name == null");
        }
        if (this.deferredName != null) {
            throw new IllegalStateException();
        }
        if (this.stackSize == 0) {
            throw new IllegalStateException("JsonWriter is closed.");
        }
        this.deferredName = string;
        return this;
    }

    private void writeDeferredName() {
        if (this.deferredName != null) {
            this.beforeName();
            this.string(this.deferredName);
            this.deferredName = null;
        }
    }

    public JsonWriter value(String string) {
        if (string == null) {
            return this.nullValue();
        }
        this.writeDeferredName();
        this.beforeValue();
        this.string(string);
        return this;
    }

    public JsonWriter jsonValue(String string) {
        if (string == null) {
            return this.nullValue();
        }
        this.writeDeferredName();
        this.beforeValue();
        this.out.append(string);
        return this;
    }

    public JsonWriter nullValue() {
        if (this.deferredName != null) {
            if (this.serializeNulls) {
                this.writeDeferredName();
            } else {
                this.deferredName = null;
                return this;
            }
        }
        this.beforeValue();
        this.out.write("null");
        return this;
    }

    public JsonWriter value(boolean bl) {
        this.writeDeferredName();
        this.beforeValue();
        this.out.write(bl ? "true" : "false");
        return this;
    }

    public JsonWriter value(Boolean bl) {
        if (bl == null) {
            return this.nullValue();
        }
        this.writeDeferredName();
        this.beforeValue();
        this.out.write(bl != false ? "true" : "false");
        return this;
    }

    public JsonWriter value(double d) {
        if (Double.isNaN(d) || Double.isInfinite(d)) {
            throw new IllegalArgumentException("Numeric values must be finite, but was " + d);
        }
        this.writeDeferredName();
        this.beforeValue();
        this.out.append(Double.toString(d));
        return this;
    }

    public JsonWriter value(long l) {
        this.writeDeferredName();
        this.beforeValue();
        this.out.write(Long.toString(l));
        return this;
    }

    public JsonWriter value(Number number) {
        if (number == null) {
            return this.nullValue();
        }
        this.writeDeferredName();
        String string = number.toString();
        if (!this.lenient && (string.equals("-Infinity") || string.equals("Infinity") || string.equals("NaN"))) {
            throw new IllegalArgumentException("Numeric values must be finite, but was " + number);
        }
        this.beforeValue();
        this.out.append(string);
        return this;
    }

    @Override
    public void flush() {
        if (this.stackSize == 0) {
            throw new IllegalStateException("JsonWriter is closed.");
        }
        this.out.flush();
    }

    @Override
    public void close() {
        this.out.close();
        int n = this.stackSize;
        if (n > 1 || n == 1 && this.stack[n - 1] != 7) {
            throw new IOException("Incomplete document");
        }
        this.stackSize = 0;
    }

    private void string(String string) {
        String[] stringArray = this.htmlSafe ? HTML_SAFE_REPLACEMENT_CHARS : REPLACEMENT_CHARS;
        this.out.write("\"");
        int n = 0;
        int n2 = string.length();
        for (int i = 0; i < n2; ++i) {
            String string2;
            char c = string.charAt(i);
            if (c < '\u0080') {
                string2 = stringArray[c];
                if (string2 == null) {
                    continue;
                }
            } else if (c == '\u2028') {
                string2 = "\\u2028";
            } else {
                if (c != '\u2029') continue;
                string2 = "\\u2029";
            }
            if (n < i) {
                this.out.write(string, n, i - n);
            }
            this.out.write(string2);
            n = i + 1;
        }
        if (n < n2) {
            this.out.write(string, n, n2 - n);
        }
        this.out.write("\"");
    }

    private void newline() {
        if (this.indent == null) {
            return;
        }
        this.out.write("\n");
        int n = this.stackSize;
        for (int i = 1; i < n; ++i) {
            this.out.write(this.indent);
        }
    }

    private void beforeName() {
        int n = this.peek();
        if (n == 5) {
            this.out.write(44);
        } else if (n != 3) {
            throw new IllegalStateException("Nesting problem.");
        }
        this.newline();
        this.replaceTop(4);
    }

    private void beforeValue() {
        switch (this.peek()) {
            case 7: {
                if (!this.lenient) {
                    throw new IllegalStateException("JSON must have only one top-level value.");
                }
            }
            case 6: {
                this.replaceTop(7);
                break;
            }
            case 1: {
                this.replaceTop(2);
                this.newline();
                break;
            }
            case 2: {
                this.out.append(',');
                this.newline();
                break;
            }
            case 4: {
                this.out.append(this.separator);
                this.replaceTop(5);
                break;
            }
            default: {
                throw new IllegalStateException("Nesting problem.");
            }
        }
    }

    static {
        for (int i = 0; i <= 31; ++i) {
            JsonWriter.REPLACEMENT_CHARS[i] = String.format("\\u%04x", i);
        }
        JsonWriter.REPLACEMENT_CHARS[34] = "\\\"";
        JsonWriter.REPLACEMENT_CHARS[92] = "\\\\";
        JsonWriter.REPLACEMENT_CHARS[9] = "\\t";
        JsonWriter.REPLACEMENT_CHARS[8] = "\\b";
        JsonWriter.REPLACEMENT_CHARS[10] = "\\n";
        JsonWriter.REPLACEMENT_CHARS[13] = "\\r";
        JsonWriter.REPLACEMENT_CHARS[12] = "\\f";
        HTML_SAFE_REPLACEMENT_CHARS = (String[])REPLACEMENT_CHARS.clone();
        JsonWriter.HTML_SAFE_REPLACEMENT_CHARS[60] = "\\u003c";
        JsonWriter.HTML_SAFE_REPLACEMENT_CHARS[62] = "\\u003e";
        JsonWriter.HTML_SAFE_REPLACEMENT_CHARS[38] = "\\u0026";
        JsonWriter.HTML_SAFE_REPLACEMENT_CHARS[61] = "\\u003d";
        JsonWriter.HTML_SAFE_REPLACEMENT_CHARS[39] = "\\u0027";
    }
}

