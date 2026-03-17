/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.kyori.adventure.text.serializer.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.key.Key;

final class KeySerializer
extends TypeAdapter<Key> {
    static final TypeAdapter<Key> INSTANCE = new KeySerializer().nullSafe();

    private KeySerializer() {
    }

    @Override
    public void write(JsonWriter jsonWriter, Key key) {
        jsonWriter.value(key.asString());
    }

    @Override
    public Key read(JsonReader jsonReader) {
        return Key.key(jsonReader.nextString());
    }
}

