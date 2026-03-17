/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.hjson;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import org.hjson.HjsonOptions;
import org.hjson.HjsonParser;
import org.hjson.HjsonWriter;
import org.hjson.JsonArray;
import org.hjson.JsonDsf;
import org.hjson.JsonLiteral;
import org.hjson.JsonNumber;
import org.hjson.JsonObject;
import org.hjson.JsonParser;
import org.hjson.JsonString;
import org.hjson.JsonType;
import org.hjson.JsonWriter;
import org.hjson.Stringify;
import org.hjson.WritingBuffer;

public abstract class JsonValue
implements Serializable {
    public static final JsonValue TRUE = JsonLiteral.TRUE;
    public static final JsonValue FALSE = JsonLiteral.FALSE;
    public static final JsonValue NULL = JsonLiteral.NULL;
    static String eol = System.getProperty("line.separator");

    public static String getEol() {
        return eol;
    }

    public static void setEol(String string) {
        if (string.equals("\r\n") || string.equals("\n")) {
            eol = string;
        }
    }

    JsonValue() {
    }

    public static JsonValue readHjson(Reader reader) {
        return new HjsonParser(reader, null).parse();
    }

    public static JsonValue readHjson(String string) {
        try {
            return new HjsonParser(string, null).parse();
        } catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }

    public static JsonValue readHjson(Reader reader, HjsonOptions hjsonOptions) {
        return new HjsonParser(reader, hjsonOptions).parse();
    }

    public static JsonValue readHjson(String string, HjsonOptions hjsonOptions) {
        try {
            return new HjsonParser(string, hjsonOptions).parse();
        } catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }

    public static JsonValue readJSON(Reader reader) {
        return new JsonParser(reader).parse();
    }

    public static JsonValue readJSON(String string) {
        try {
            return new JsonParser(string).parse();
        } catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }

    public static JsonValue valueOf(int n) {
        return new JsonNumber(n);
    }

    public static JsonValue valueOf(long l) {
        return new JsonNumber(l);
    }

    public static JsonValue valueOf(float f) {
        return new JsonNumber(f);
    }

    public static JsonValue valueOf(double d) {
        return new JsonNumber(d);
    }

    public static JsonValue valueOf(String string) {
        return string == null ? NULL : new JsonString(string);
    }

    public static JsonValue valueOf(boolean bl) {
        return bl ? TRUE : FALSE;
    }

    public static JsonValue valueOfDsf(Object object) {
        return new JsonDsf(object);
    }

    public abstract JsonType getType();

    public boolean isObject() {
        return false;
    }

    public boolean isArray() {
        return false;
    }

    public boolean isNumber() {
        return false;
    }

    public boolean isString() {
        return false;
    }

    public boolean isBoolean() {
        return false;
    }

    public boolean isTrue() {
        return false;
    }

    public boolean isFalse() {
        return false;
    }

    public boolean isNull() {
        return false;
    }

    public JsonObject asObject() {
        throw new UnsupportedOperationException("Not an object: " + this.toString());
    }

    public JsonArray asArray() {
        throw new UnsupportedOperationException("Not an array: " + this.toString());
    }

    public int asInt() {
        throw new UnsupportedOperationException("Not a number: " + this.toString());
    }

    public long asLong() {
        throw new UnsupportedOperationException("Not a number: " + this.toString());
    }

    public float asFloat() {
        throw new UnsupportedOperationException("Not a number: " + this.toString());
    }

    public double asDouble() {
        throw new UnsupportedOperationException("Not a number: " + this.toString());
    }

    public String asString() {
        throw new UnsupportedOperationException("Not a string: " + this.toString());
    }

    public boolean asBoolean() {
        throw new UnsupportedOperationException("Not a boolean: " + this.toString());
    }

    public Object asDsf() {
        throw new UnsupportedOperationException("Not a DSF");
    }

    public void writeTo(Writer writer) {
        this.writeTo(writer, Stringify.PLAIN);
    }

    public void writeTo(Writer writer, Stringify stringify) {
        WritingBuffer writingBuffer = new WritingBuffer(writer, 128);
        switch (stringify) {
            case PLAIN: {
                new JsonWriter(false).save(this, writingBuffer, 0);
                break;
            }
            case FORMATTED: {
                new JsonWriter(true).save(this, writingBuffer, 0);
                break;
            }
            case HJSON: {
                new HjsonWriter(null).save(this, writingBuffer, 0, "", true);
            }
        }
        writingBuffer.flush();
    }

    public void writeTo(Writer writer, HjsonOptions hjsonOptions) {
        if (hjsonOptions == null) {
            throw new NullPointerException("options is null");
        }
        WritingBuffer writingBuffer = new WritingBuffer(writer, 128);
        new HjsonWriter(hjsonOptions).save(this, writingBuffer, 0, "", true);
        writingBuffer.flush();
    }

    public String toString() {
        return this.toString(Stringify.PLAIN);
    }

    public String toString(Stringify stringify) {
        StringWriter stringWriter = new StringWriter();
        try {
            this.writeTo((Writer)stringWriter, stringify);
        } catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
        return stringWriter.toString();
    }

    public String toString(HjsonOptions hjsonOptions) {
        StringWriter stringWriter = new StringWriter();
        try {
            this.writeTo((Writer)stringWriter, hjsonOptions);
        } catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
        return stringWriter.toString();
    }

    public boolean equals(Object object) {
        return super.equals(object);
    }

    public int hashCode() {
        return super.hashCode();
    }

    static boolean isPunctuatorChar(int n) {
        return n == 123 || n == 125 || n == 91 || n == 93 || n == 44 || n == 58;
    }
}

