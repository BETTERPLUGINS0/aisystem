/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.hjson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.hjson.JsonType;
import org.hjson.JsonValue;

public class JsonArray
extends JsonValue
implements Iterable<JsonValue> {
    private final List<JsonValue> values;

    public JsonArray() {
        this.values = new ArrayList<JsonValue>();
    }

    public JsonArray(JsonArray jsonArray) {
        this(jsonArray, false);
    }

    private JsonArray(JsonArray jsonArray, boolean bl) {
        if (jsonArray == null) {
            throw new NullPointerException("array is null");
        }
        this.values = bl ? Collections.unmodifiableList(jsonArray.values) : new ArrayList<JsonValue>(jsonArray.values);
    }

    public static JsonArray unmodifiableArray(JsonArray jsonArray) {
        return new JsonArray(jsonArray, true);
    }

    public JsonArray add(int n) {
        this.values.add(JsonArray.valueOf(n));
        return this;
    }

    public JsonArray add(long l) {
        this.values.add(JsonArray.valueOf(l));
        return this;
    }

    public JsonArray add(float f) {
        this.values.add(JsonArray.valueOf(f));
        return this;
    }

    public JsonArray add(double d) {
        this.values.add(JsonArray.valueOf(d));
        return this;
    }

    public JsonArray add(boolean bl) {
        this.values.add(JsonArray.valueOf(bl));
        return this;
    }

    public JsonArray add(String string) {
        this.values.add(JsonArray.valueOf(string));
        return this;
    }

    public JsonArray add(JsonValue jsonValue) {
        if (jsonValue == null) {
            throw new NullPointerException("value is null");
        }
        this.values.add(jsonValue);
        return this;
    }

    public JsonArray set(int n, int n2) {
        this.values.set(n, JsonArray.valueOf(n2));
        return this;
    }

    public JsonArray set(int n, long l) {
        this.values.set(n, JsonArray.valueOf(l));
        return this;
    }

    public JsonArray set(int n, float f) {
        this.values.set(n, JsonArray.valueOf(f));
        return this;
    }

    public JsonArray set(int n, double d) {
        this.values.set(n, JsonArray.valueOf(d));
        return this;
    }

    public JsonArray set(int n, boolean bl) {
        this.values.set(n, JsonArray.valueOf(bl));
        return this;
    }

    public JsonArray set(int n, String string) {
        this.values.set(n, JsonArray.valueOf(string));
        return this;
    }

    public JsonArray set(int n, JsonValue jsonValue) {
        if (jsonValue == null) {
            throw new NullPointerException("value is null");
        }
        this.values.set(n, jsonValue);
        return this;
    }

    public JsonArray remove(int n) {
        this.values.remove(n);
        return this;
    }

    public int size() {
        return this.values.size();
    }

    public boolean isEmpty() {
        return this.values.isEmpty();
    }

    public JsonValue get(int n) {
        return this.values.get(n);
    }

    public List<JsonValue> values() {
        return Collections.unmodifiableList(this.values);
    }

    @Override
    public Iterator<JsonValue> iterator() {
        final Iterator<JsonValue> iterator = this.values.iterator();
        return new Iterator<JsonValue>(){

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public JsonValue next() {
                return (JsonValue)iterator.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public JsonType getType() {
        return JsonType.ARRAY;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public JsonArray asArray() {
        return this;
    }

    @Override
    public int hashCode() {
        return this.values.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        JsonArray jsonArray = (JsonArray)object;
        return this.values.equals(jsonArray.values);
    }
}

