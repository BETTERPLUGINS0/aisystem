/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.text.serializer.gson;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.gson.TextColorSerializer;
import org.jetbrains.annotations.Nullable;

final class TextColorWrapper {
    @Nullable
    final TextColor color;
    @Nullable
    final TextDecoration decoration;
    final boolean reset;

    TextColorWrapper(@Nullable TextColor textColor, @Nullable TextDecoration textDecoration, boolean bl) {
        this.color = textColor;
        this.decoration = textDecoration;
        this.reset = bl;
    }

    static final class Serializer
    extends TypeAdapter<TextColorWrapper> {
        static final Serializer INSTANCE = new Serializer();

        private Serializer() {
        }

        @Override
        public void write(JsonWriter jsonWriter, TextColorWrapper textColorWrapper) {
            throw new JsonSyntaxException("Cannot write TextColorWrapper instances");
        }

        @Override
        public TextColorWrapper read(JsonReader jsonReader) {
            boolean bl;
            String string = jsonReader.nextString();
            TextColor textColor = TextColorSerializer.fromString(string);
            TextDecoration textDecoration = TextDecoration.NAMES.value(string);
            boolean bl2 = bl = textDecoration == null && string.equals("reset");
            if (textColor == null && textDecoration == null && !bl) {
                throw new JsonParseException("Don't know how to parse " + string + " at " + jsonReader.getPath());
            }
            return new TextColorWrapper(textColor, textDecoration, bl);
        }
    }
}

