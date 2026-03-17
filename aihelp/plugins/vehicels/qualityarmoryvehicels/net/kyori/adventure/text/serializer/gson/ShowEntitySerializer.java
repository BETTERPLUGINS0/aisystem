/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.text.serializer.gson;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.gson.SerializerFactory;
import org.jetbrains.annotations.Nullable;

final class ShowEntitySerializer
extends TypeAdapter<HoverEvent.ShowEntity> {
    private final Gson gson;

    static TypeAdapter<HoverEvent.ShowEntity> create(Gson gson) {
        return new ShowEntitySerializer(gson).nullSafe();
    }

    private ShowEntitySerializer(Gson gson) {
        this.gson = gson;
    }

    @Override
    public HoverEvent.ShowEntity read(JsonReader jsonReader) {
        jsonReader.beginObject();
        Key key = null;
        UUID uUID = null;
        Component component = null;
        while (jsonReader.hasNext()) {
            String string = jsonReader.nextName();
            if (string.equals("type")) {
                key = (Key)this.gson.fromJson(jsonReader, SerializerFactory.KEY_TYPE);
                continue;
            }
            if (string.equals("id")) {
                uUID = (UUID)this.gson.fromJson(jsonReader, SerializerFactory.UUID_TYPE);
                continue;
            }
            if (string.equals("name")) {
                component = (Component)this.gson.fromJson(jsonReader, SerializerFactory.COMPONENT_TYPE);
                continue;
            }
            jsonReader.skipValue();
        }
        if (key == null || uUID == null) {
            throw new JsonParseException("A show entity hover event needs type and id fields to be deserialized");
        }
        jsonReader.endObject();
        return HoverEvent.ShowEntity.showEntity(key, uUID, component);
    }

    @Override
    public void write(JsonWriter jsonWriter, HoverEvent.ShowEntity showEntity) {
        jsonWriter.beginObject();
        jsonWriter.name("type");
        this.gson.toJson((Object)showEntity.type(), SerializerFactory.KEY_TYPE, jsonWriter);
        jsonWriter.name("id");
        this.gson.toJson((Object)showEntity.id(), SerializerFactory.UUID_TYPE, jsonWriter);
        @Nullable Component component = showEntity.name();
        if (component != null) {
            jsonWriter.name("name");
            this.gson.toJson((Object)component, SerializerFactory.COMPONENT_TYPE, jsonWriter);
        }
        jsonWriter.endObject();
    }
}

