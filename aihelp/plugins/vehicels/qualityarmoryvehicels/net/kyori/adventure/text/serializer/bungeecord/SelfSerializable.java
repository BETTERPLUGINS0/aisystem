/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.kyori.adventure.text.serializer.bungeecord;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

interface SelfSerializable {
    public void write(JsonWriter var1) throws IOException;

    public static class AdapterFactory
    implements TypeAdapterFactory {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            if (!SelfSerializable.class.isAssignableFrom(typeToken.getRawType())) {
                return null;
            }
            return new SelfSerializableTypeAdapter<T>(typeToken);
        }

        static {
            SelfSerializableTypeAdapter.class.getName();
        }

        static class SelfSerializableTypeAdapter<T>
        extends TypeAdapter<T> {
            private final TypeToken<T> type;

            SelfSerializableTypeAdapter(TypeToken<T> typeToken) {
                this.type = typeToken;
            }

            @Override
            public void write(JsonWriter jsonWriter, T t) {
                ((SelfSerializable)t).write(jsonWriter);
            }

            @Override
            public T read(JsonReader jsonReader) {
                throw new UnsupportedOperationException("Cannot load values of type " + this.type.getType().getTypeName());
            }
        }
    }
}

