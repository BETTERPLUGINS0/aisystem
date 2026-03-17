/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.kyori.adventure.text.serializer.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.Type;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.serializer.gson.SerializerFactory;

final class TranslationArgumentSerializer
extends TypeAdapter<TranslationArgument> {
    private final Gson gson;

    static TypeAdapter<TranslationArgument> create(Gson gson) {
        return new TranslationArgumentSerializer(gson).nullSafe();
    }

    private TranslationArgumentSerializer(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter jsonWriter, TranslationArgument translationArgument) {
        Object object = translationArgument.value();
        if (object instanceof Boolean) {
            jsonWriter.value((Boolean)object);
        } else if (object instanceof Number) {
            jsonWriter.value((Number)object);
        } else if (object instanceof Component) {
            this.gson.toJson(object, SerializerFactory.COMPONENT_TYPE, jsonWriter);
        } else {
            throw new IllegalStateException("Unable to serialize translatable argument of type " + object.getClass() + ": " + object);
        }
    }

    @Override
    public TranslationArgument read(JsonReader jsonReader) {
        switch (jsonReader.peek()) {
            case BOOLEAN: {
                return TranslationArgument.bool(jsonReader.nextBoolean());
            }
            case NUMBER: {
                return TranslationArgument.numeric((Number)this.gson.fromJson(jsonReader, (Type)((Object)Number.class)));
            }
        }
        return TranslationArgument.component((ComponentLike)this.gson.fromJson(jsonReader, SerializerFactory.COMPONENT_TYPE));
    }
}

