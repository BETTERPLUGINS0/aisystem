/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.hjson;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.hjson.JsonType;
import org.hjson.JsonValue;

public class JsonObject
extends JsonValue
implements Iterable<Member> {
    private final List<String> names;
    private final List<JsonValue> values;
    private transient HashIndexTable table;

    public JsonObject() {
        this.names = new ArrayList<String>();
        this.values = new ArrayList<JsonValue>();
        this.table = new HashIndexTable();
    }

    public JsonObject(JsonObject jsonObject) {
        this(jsonObject, false);
    }

    private JsonObject(JsonObject jsonObject, boolean bl) {
        if (jsonObject == null) {
            throw new NullPointerException("object is null");
        }
        if (bl) {
            this.names = Collections.unmodifiableList(jsonObject.names);
            this.values = Collections.unmodifiableList(jsonObject.values);
        } else {
            this.names = new ArrayList<String>(jsonObject.names);
            this.values = new ArrayList<JsonValue>(jsonObject.values);
        }
        this.table = new HashIndexTable();
        this.updateHashIndex();
    }

    public static JsonObject unmodifiableObject(JsonObject jsonObject) {
        return new JsonObject(jsonObject, true);
    }

    public JsonObject add(String string, int n) {
        this.add(string, JsonObject.valueOf(n));
        return this;
    }

    public JsonObject add(String string, long l) {
        this.add(string, JsonObject.valueOf(l));
        return this;
    }

    public JsonObject add(String string, float f) {
        this.add(string, JsonObject.valueOf(f));
        return this;
    }

    public JsonObject add(String string, double d) {
        this.add(string, JsonObject.valueOf(d));
        return this;
    }

    public JsonObject add(String string, boolean bl) {
        this.add(string, JsonObject.valueOf(bl));
        return this;
    }

    public JsonObject add(String string, String string2) {
        this.add(string, JsonObject.valueOf(string2));
        return this;
    }

    public JsonObject add(String string, JsonValue jsonValue) {
        if (string == null) {
            throw new NullPointerException("name is null");
        }
        if (jsonValue == null) {
            throw new NullPointerException("value is null");
        }
        this.table.add(string, this.names.size());
        this.names.add(string);
        this.values.add(jsonValue);
        return this;
    }

    public JsonObject set(String string, int n) {
        this.set(string, JsonObject.valueOf(n));
        return this;
    }

    public JsonObject set(String string, long l) {
        this.set(string, JsonObject.valueOf(l));
        return this;
    }

    public JsonObject set(String string, float f) {
        this.set(string, JsonObject.valueOf(f));
        return this;
    }

    public JsonObject set(String string, double d) {
        this.set(string, JsonObject.valueOf(d));
        return this;
    }

    public JsonObject set(String string, boolean bl) {
        this.set(string, JsonObject.valueOf(bl));
        return this;
    }

    public JsonObject set(String string, String string2) {
        this.set(string, JsonObject.valueOf(string2));
        return this;
    }

    public JsonObject set(String string, JsonValue jsonValue) {
        if (string == null) {
            throw new NullPointerException("name is null");
        }
        if (jsonValue == null) {
            throw new NullPointerException("value is null");
        }
        int n = this.indexOf(string);
        if (n != -1) {
            this.values.set(n, jsonValue);
        } else {
            this.table.add(string, this.names.size());
            this.names.add(string);
            this.values.add(jsonValue);
        }
        return this;
    }

    public JsonObject remove(String string) {
        if (string == null) {
            throw new NullPointerException("name is null");
        }
        int n = this.indexOf(string);
        if (n != -1) {
            this.table.remove(n);
            this.names.remove(n);
            this.values.remove(n);
        }
        return this;
    }

    public JsonValue get(String string) {
        if (string == null) {
            throw new NullPointerException("name is null");
        }
        int n = this.indexOf(string);
        return n != -1 ? this.values.get(n) : null;
    }

    public int getInt(String string, int n) {
        JsonValue jsonValue = this.get(string);
        return jsonValue != null ? jsonValue.asInt() : n;
    }

    public long getLong(String string, long l) {
        JsonValue jsonValue = this.get(string);
        return jsonValue != null ? jsonValue.asLong() : l;
    }

    public float getFloat(String string, float f) {
        JsonValue jsonValue = this.get(string);
        return jsonValue != null ? jsonValue.asFloat() : f;
    }

    public double getDouble(String string, double d) {
        JsonValue jsonValue = this.get(string);
        return jsonValue != null ? jsonValue.asDouble() : d;
    }

    public boolean getBoolean(String string, boolean bl) {
        JsonValue jsonValue = this.get(string);
        return jsonValue != null ? jsonValue.asBoolean() : bl;
    }

    public String getString(String string, String string2) {
        JsonValue jsonValue = this.get(string);
        return jsonValue != null ? jsonValue.asString() : string2;
    }

    public int size() {
        return this.names.size();
    }

    public boolean isEmpty() {
        return this.names.isEmpty();
    }

    public List<String> names() {
        return Collections.unmodifiableList(this.names);
    }

    @Override
    public Iterator<Member> iterator() {
        final Iterator<String> iterator = this.names.iterator();
        final Iterator<JsonValue> iterator2 = this.values.iterator();
        return new Iterator<Member>(){

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Member next() {
                String string = (String)iterator.next();
                JsonValue jsonValue = (JsonValue)iterator2.next();
                return new Member(string, jsonValue);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public JsonType getType() {
        return JsonType.OBJECT;
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public JsonObject asObject() {
        return this;
    }

    @Override
    public int hashCode() {
        int n = 1;
        n = 31 * n + this.names.hashCode();
        n = 31 * n + this.values.hashCode();
        return n;
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
        JsonObject jsonObject = (JsonObject)object;
        return this.names.equals(jsonObject.names) && this.values.equals(jsonObject.values);
    }

    int indexOf(String string) {
        int n = this.table.get(string);
        if (n != -1 && string.equals(this.names.get(n))) {
            return n;
        }
        return this.names.lastIndexOf(string);
    }

    private synchronized void readObject(ObjectInputStream objectInputStream) {
        objectInputStream.defaultReadObject();
        this.table = new HashIndexTable();
        this.updateHashIndex();
    }

    private void updateHashIndex() {
        int n = this.names.size();
        for (int i = 0; i < n; ++i) {
            this.table.add(this.names.get(i), i);
        }
    }

    static class HashIndexTable {
        private final byte[] hashTable = new byte[32];

        public HashIndexTable() {
        }

        public HashIndexTable(HashIndexTable hashIndexTable) {
            System.arraycopy(hashIndexTable.hashTable, 0, this.hashTable, 0, this.hashTable.length);
        }

        void add(String string, int n) {
            int n2 = this.hashSlotfor(string);
            this.hashTable[n2] = n < 255 ? (byte)(n + 1) : (byte)0;
        }

        void remove(int n) {
            for (int i = 0; i < this.hashTable.length; ++i) {
                if (this.hashTable[i] == n + 1) {
                    this.hashTable[i] = 0;
                    continue;
                }
                if (this.hashTable[i] <= n + 1) continue;
                int n2 = i;
                this.hashTable[n2] = (byte)(this.hashTable[n2] - 1);
            }
        }

        int get(Object object) {
            int n = this.hashSlotfor(object);
            return (this.hashTable[n] & 0xFF) - 1;
        }

        private int hashSlotfor(Object object) {
            return object.hashCode() & this.hashTable.length - 1;
        }
    }

    public static class Member {
        private final String name;
        private final JsonValue value;

        Member(String string, JsonValue jsonValue) {
            this.name = string;
            this.value = jsonValue;
        }

        public String getName() {
            return this.name;
        }

        public JsonValue getValue() {
            return this.value;
        }

        public int hashCode() {
            int n = 1;
            n = 31 * n + this.name.hashCode();
            n = 31 * n + this.value.hashCode();
            return n;
        }

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
            Member member = (Member)object;
            return this.name.equals(member.name) && this.value.equals(member.value);
        }
    }
}

