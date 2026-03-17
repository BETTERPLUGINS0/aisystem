/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.kyori.adventure.text.serializer.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.util.UUID;
import net.kyori.adventure.text.serializer.json.JSONOptions;
import net.kyori.option.OptionState;

final class UUIDSerializer
extends TypeAdapter<UUID> {
    private final boolean emitIntArray;

    static TypeAdapter<UUID> uuidSerializer(OptionState optionState) {
        return new UUIDSerializer(optionState.value(JSONOptions.EMIT_HOVER_SHOW_ENTITY_ID_AS_INT_ARRAY)).nullSafe();
    }

    private UUIDSerializer(boolean bl) {
        this.emitIntArray = bl;
    }

    @Override
    public void write(JsonWriter jsonWriter, UUID uUID) {
        if (this.emitIntArray) {
            int n = (int)(uUID.getMostSignificantBits() >> 32);
            int n2 = (int)(uUID.getMostSignificantBits() & 0xFFFFFFFFL);
            int n3 = (int)(uUID.getLeastSignificantBits() >> 32);
            int n4 = (int)(uUID.getLeastSignificantBits() & 0xFFFFFFFFL);
            jsonWriter.beginArray().value(n).value(n2).value(n3).value(n4).endArray();
        } else {
            jsonWriter.value(uUID.toString());
        }
    }

    @Override
    public UUID read(JsonReader jsonReader) {
        if (jsonReader.peek() == JsonToken.BEGIN_ARRAY) {
            jsonReader.beginArray();
            int n = jsonReader.nextInt();
            int n2 = jsonReader.nextInt();
            int n3 = jsonReader.nextInt();
            int n4 = jsonReader.nextInt();
            jsonReader.endArray();
            return new UUID((long)n << 32 | (long)n2 & 0xFFFFFFFFL, (long)n3 << 32 | (long)n4 & 0xFFFFFFFFL);
        }
        return UUID.fromString(jsonReader.nextString());
    }
}

