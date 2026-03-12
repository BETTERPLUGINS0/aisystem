package fr.xephi.authme.libs.net.kyori.adventure.text.serializer.gson;

import fr.xephi.authme.libs.com.google.gson.TypeAdapter;
import fr.xephi.authme.libs.com.google.gson.stream.JsonReader;
import fr.xephi.authme.libs.com.google.gson.stream.JsonWriter;
import fr.xephi.authme.libs.net.kyori.adventure.key.Key;
import java.io.IOException;

final class KeySerializer extends TypeAdapter<Key> {
   static final TypeAdapter<Key> INSTANCE = (new KeySerializer()).nullSafe();

   private KeySerializer() {
   }

   public void write(final JsonWriter out, final Key value) throws IOException {
      out.value(value.asString());
   }

   public Key read(final JsonReader in) throws IOException {
      return Key.key(in.nextString());
   }
}
