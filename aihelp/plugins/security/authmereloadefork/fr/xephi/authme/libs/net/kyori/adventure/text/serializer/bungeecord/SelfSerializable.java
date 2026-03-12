package fr.xephi.authme.libs.net.kyori.adventure.text.serializer.bungeecord;

import fr.xephi.authme.libs.com.google.gson.Gson;
import fr.xephi.authme.libs.com.google.gson.TypeAdapter;
import fr.xephi.authme.libs.com.google.gson.TypeAdapterFactory;
import fr.xephi.authme.libs.com.google.gson.reflect.TypeToken;
import fr.xephi.authme.libs.com.google.gson.stream.JsonReader;
import fr.xephi.authme.libs.com.google.gson.stream.JsonWriter;
import java.io.IOException;

interface SelfSerializable {
   void write(JsonWriter out) throws IOException;

   public static class AdapterFactory implements TypeAdapterFactory {
      public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
         return !SelfSerializable.class.isAssignableFrom(type.getRawType()) ? null : new SelfSerializable.AdapterFactory.SelfSerializableTypeAdapter(type);
      }

      static {
         SelfSerializable.AdapterFactory.SelfSerializableTypeAdapter.class.getName();
      }

      static class SelfSerializableTypeAdapter<T> extends TypeAdapter<T> {
         private final TypeToken<T> type;

         SelfSerializableTypeAdapter(final TypeToken<T> type) {
            this.type = type;
         }

         public void write(final JsonWriter out, final T value) throws IOException {
            ((SelfSerializable)value).write(out);
         }

         public T read(final JsonReader in) {
            throw new UnsupportedOperationException("Cannot load values of type " + this.type.getType().getTypeName());
         }
      }
   }
}
