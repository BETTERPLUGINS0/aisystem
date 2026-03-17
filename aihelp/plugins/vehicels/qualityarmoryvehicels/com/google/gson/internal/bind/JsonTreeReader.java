/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;

public final class JsonTreeReader
extends JsonReader {
    private static final Reader UNREADABLE_READER = new Reader(){

        @Override
        public int read(char[] cArray, int n, int n2) {
            throw new AssertionError();
        }

        @Override
        public void close() {
            throw new AssertionError();
        }
    };
    private static final Object SENTINEL_CLOSED = new Object();
    private Object[] stack = new Object[32];
    private int stackSize = 0;
    private String[] pathNames = new String[32];
    private int[] pathIndices = new int[32];

    public JsonTreeReader(JsonElement jsonElement) {
        super(UNREADABLE_READER);
        this.push(jsonElement);
    }

    @Override
    public void beginArray() {
        this.expect(JsonToken.BEGIN_ARRAY);
        JsonArray jsonArray = (JsonArray)this.peekStack();
        this.push(jsonArray.iterator());
        this.pathIndices[this.stackSize - 1] = 0;
    }

    @Override
    public void endArray() {
        this.expect(JsonToken.END_ARRAY);
        this.popStack();
        this.popStack();
        if (this.stackSize > 0) {
            int n = this.stackSize - 1;
            this.pathIndices[n] = this.pathIndices[n] + 1;
        }
    }

    @Override
    public void beginObject() {
        this.expect(JsonToken.BEGIN_OBJECT);
        JsonObject jsonObject = (JsonObject)this.peekStack();
        this.push(jsonObject.entrySet().iterator());
    }

    @Override
    public void endObject() {
        this.expect(JsonToken.END_OBJECT);
        this.popStack();
        this.popStack();
        if (this.stackSize > 0) {
            int n = this.stackSize - 1;
            this.pathIndices[n] = this.pathIndices[n] + 1;
        }
    }

    @Override
    public boolean hasNext() {
        JsonToken jsonToken = this.peek();
        return jsonToken != JsonToken.END_OBJECT && jsonToken != JsonToken.END_ARRAY;
    }

    @Override
    public JsonToken peek() {
        if (this.stackSize == 0) {
            return JsonToken.END_DOCUMENT;
        }
        Object object = this.peekStack();
        if (object instanceof Iterator) {
            boolean bl = this.stack[this.stackSize - 2] instanceof JsonObject;
            Iterator iterator = (Iterator)object;
            if (iterator.hasNext()) {
                if (bl) {
                    return JsonToken.NAME;
                }
                this.push(iterator.next());
                return this.peek();
            }
            return bl ? JsonToken.END_OBJECT : JsonToken.END_ARRAY;
        }
        if (object instanceof JsonObject) {
            return JsonToken.BEGIN_OBJECT;
        }
        if (object instanceof JsonArray) {
            return JsonToken.BEGIN_ARRAY;
        }
        if (object instanceof JsonPrimitive) {
            JsonPrimitive jsonPrimitive = (JsonPrimitive)object;
            if (jsonPrimitive.isString()) {
                return JsonToken.STRING;
            }
            if (jsonPrimitive.isBoolean()) {
                return JsonToken.BOOLEAN;
            }
            if (jsonPrimitive.isNumber()) {
                return JsonToken.NUMBER;
            }
            throw new AssertionError();
        }
        if (object instanceof JsonNull) {
            return JsonToken.NULL;
        }
        if (object == SENTINEL_CLOSED) {
            throw new IllegalStateException("JsonReader is closed");
        }
        throw new AssertionError();
    }

    private Object peekStack() {
        return this.stack[this.stackSize - 1];
    }

    private Object popStack() {
        Object object = this.stack[--this.stackSize];
        this.stack[this.stackSize] = null;
        return object;
    }

    private void expect(JsonToken jsonToken) {
        if (this.peek() != jsonToken) {
            throw new IllegalStateException("Expected " + (Object)((Object)jsonToken) + " but was " + (Object)((Object)this.peek()) + this.locationString());
        }
    }

    @Override
    public String nextName() {
        String string;
        this.expect(JsonToken.NAME);
        Iterator iterator = (Iterator)this.peekStack();
        Map.Entry entry = (Map.Entry)iterator.next();
        this.pathNames[this.stackSize - 1] = string = (String)entry.getKey();
        this.push(entry.getValue());
        return string;
    }

    @Override
    public String nextString() {
        JsonToken jsonToken = this.peek();
        if (jsonToken != JsonToken.STRING && jsonToken != JsonToken.NUMBER) {
            throw new IllegalStateException("Expected " + (Object)((Object)JsonToken.STRING) + " but was " + (Object)((Object)jsonToken) + this.locationString());
        }
        String string = ((JsonPrimitive)this.popStack()).getAsString();
        if (this.stackSize > 0) {
            int n = this.stackSize - 1;
            this.pathIndices[n] = this.pathIndices[n] + 1;
        }
        return string;
    }

    @Override
    public boolean nextBoolean() {
        this.expect(JsonToken.BOOLEAN);
        boolean bl = ((JsonPrimitive)this.popStack()).getAsBoolean();
        if (this.stackSize > 0) {
            int n = this.stackSize - 1;
            this.pathIndices[n] = this.pathIndices[n] + 1;
        }
        return bl;
    }

    @Override
    public void nextNull() {
        this.expect(JsonToken.NULL);
        this.popStack();
        if (this.stackSize > 0) {
            int n = this.stackSize - 1;
            this.pathIndices[n] = this.pathIndices[n] + 1;
        }
    }

    @Override
    public double nextDouble() {
        JsonToken jsonToken = this.peek();
        if (jsonToken != JsonToken.NUMBER && jsonToken != JsonToken.STRING) {
            throw new IllegalStateException("Expected " + (Object)((Object)JsonToken.NUMBER) + " but was " + (Object)((Object)jsonToken) + this.locationString());
        }
        double d = ((JsonPrimitive)this.peekStack()).getAsDouble();
        if (!this.isLenient() && (Double.isNaN(d) || Double.isInfinite(d))) {
            throw new NumberFormatException("JSON forbids NaN and infinities: " + d);
        }
        this.popStack();
        if (this.stackSize > 0) {
            int n = this.stackSize - 1;
            this.pathIndices[n] = this.pathIndices[n] + 1;
        }
        return d;
    }

    @Override
    public long nextLong() {
        JsonToken jsonToken = this.peek();
        if (jsonToken != JsonToken.NUMBER && jsonToken != JsonToken.STRING) {
            throw new IllegalStateException("Expected " + (Object)((Object)JsonToken.NUMBER) + " but was " + (Object)((Object)jsonToken) + this.locationString());
        }
        long l = ((JsonPrimitive)this.peekStack()).getAsLong();
        this.popStack();
        if (this.stackSize > 0) {
            int n = this.stackSize - 1;
            this.pathIndices[n] = this.pathIndices[n] + 1;
        }
        return l;
    }

    @Override
    public int nextInt() {
        JsonToken jsonToken = this.peek();
        if (jsonToken != JsonToken.NUMBER && jsonToken != JsonToken.STRING) {
            throw new IllegalStateException("Expected " + (Object)((Object)JsonToken.NUMBER) + " but was " + (Object)((Object)jsonToken) + this.locationString());
        }
        int n = ((JsonPrimitive)this.peekStack()).getAsInt();
        this.popStack();
        if (this.stackSize > 0) {
            int n2 = this.stackSize - 1;
            this.pathIndices[n2] = this.pathIndices[n2] + 1;
        }
        return n;
    }

    @Override
    public void close() {
        this.stack = new Object[]{SENTINEL_CLOSED};
        this.stackSize = 1;
    }

    @Override
    public void skipValue() {
        if (this.peek() == JsonToken.NAME) {
            this.nextName();
            this.pathNames[this.stackSize - 2] = "null";
        } else {
            this.popStack();
            this.pathNames[this.stackSize - 1] = "null";
        }
        int n = this.stackSize - 1;
        this.pathIndices[n] = this.pathIndices[n] + 1;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public void promoteNameToValue() {
        this.expect(JsonToken.NAME);
        Iterator iterator = (Iterator)this.peekStack();
        Map.Entry entry = (Map.Entry)iterator.next();
        this.push(entry.getValue());
        this.push(new JsonPrimitive((String)entry.getKey()));
    }

    private void push(Object object) {
        if (this.stackSize == this.stack.length) {
            Object[] objectArray = new Object[this.stackSize * 2];
            int[] nArray = new int[this.stackSize * 2];
            String[] stringArray = new String[this.stackSize * 2];
            System.arraycopy(this.stack, 0, objectArray, 0, this.stackSize);
            System.arraycopy(this.pathIndices, 0, nArray, 0, this.stackSize);
            System.arraycopy(this.pathNames, 0, stringArray, 0, this.stackSize);
            this.stack = objectArray;
            this.pathIndices = nArray;
            this.pathNames = stringArray;
        }
        this.stack[this.stackSize++] = object;
    }

    @Override
    public String getPath() {
        StringBuilder stringBuilder = new StringBuilder().append('$');
        for (int i = 0; i < this.stackSize; ++i) {
            if (this.stack[i] instanceof JsonArray) {
                if (!(this.stack[++i] instanceof Iterator)) continue;
                stringBuilder.append('[').append(this.pathIndices[i]).append(']');
                continue;
            }
            if (!(this.stack[i] instanceof JsonObject) || !(this.stack[++i] instanceof Iterator)) continue;
            stringBuilder.append('.');
            if (this.pathNames[i] == null) continue;
            stringBuilder.append(this.pathNames[i]);
        }
        return stringBuilder.toString();
    }

    private String locationString() {
        return " at path " + this.getPath();
    }
}

