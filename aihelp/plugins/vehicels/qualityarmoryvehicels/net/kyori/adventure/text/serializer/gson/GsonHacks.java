/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.text.serializer.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.jetbrains.annotations.Nullable;

final class GsonHacks {
    private GsonHacks() {
    }

    static boolean isNullOrEmpty(@Nullable JsonElement jsonElement) {
        return jsonElement == null || jsonElement.isJsonNull() || jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() == 0 || jsonElement.isJsonObject() && jsonElement.getAsJsonObject().entrySet().isEmpty();
    }

    static boolean readBoolean(JsonReader jsonReader) {
        JsonToken jsonToken = jsonReader.peek();
        if (jsonToken == JsonToken.BOOLEAN) {
            return jsonReader.nextBoolean();
        }
        if (jsonToken == JsonToken.STRING || jsonToken == JsonToken.NUMBER) {
            return Boolean.parseBoolean(jsonReader.nextString());
        }
        throw new JsonParseException("Token of type " + (Object)((Object)jsonToken) + " cannot be interpreted as a boolean");
    }

    static String readString(JsonReader jsonReader) {
        JsonToken jsonToken = jsonReader.peek();
        if (jsonToken == JsonToken.STRING || jsonToken == JsonToken.NUMBER) {
            return jsonReader.nextString();
        }
        if (jsonToken == JsonToken.BOOLEAN) {
            return String.valueOf(jsonReader.nextBoolean());
        }
        throw new JsonParseException("Token of type " + (Object)((Object)jsonToken) + " cannot be interpreted as a string");
    }
}

