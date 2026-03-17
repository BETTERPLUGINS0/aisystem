/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.kyori.adventure.text.serializer.gson;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.text.BlockNBTComponent;

final class BlockNBTComponentPosSerializer
extends TypeAdapter<BlockNBTComponent.Pos> {
    static final TypeAdapter<BlockNBTComponent.Pos> INSTANCE = new BlockNBTComponentPosSerializer().nullSafe();

    private BlockNBTComponentPosSerializer() {
    }

    @Override
    public BlockNBTComponent.Pos read(JsonReader jsonReader) {
        String string = jsonReader.nextString();
        try {
            return BlockNBTComponent.Pos.fromString(string);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new JsonParseException("Don't know how to turn " + string + " into a Position");
        }
    }

    @Override
    public void write(JsonWriter jsonWriter, BlockNBTComponent.Pos pos) {
        jsonWriter.value(pos.asString());
    }
}

