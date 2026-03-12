package fr.xephi.authme.libs.net.kyori.adventure.text.serializer.gson;

import fr.xephi.authme.libs.com.google.gson.Gson;
import fr.xephi.authme.libs.com.google.gson.TypeAdapter;
import fr.xephi.authme.libs.com.google.gson.stream.JsonReader;
import fr.xephi.authme.libs.com.google.gson.stream.JsonWriter;
import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.ComponentLike;
import fr.xephi.authme.libs.net.kyori.adventure.text.TranslationArgument;
import java.io.IOException;
import java.lang.reflect.Type;

final class TranslationArgumentSerializer extends TypeAdapter<TranslationArgument> {
   private final Gson gson;

   static TypeAdapter<TranslationArgument> create(final Gson gson) {
      return (new TranslationArgumentSerializer(gson)).nullSafe();
   }

   private TranslationArgumentSerializer(final Gson gson) {
      this.gson = gson;
   }

   public void write(final JsonWriter out, final TranslationArgument value) throws IOException {
      Object raw = value.value();
      if (raw instanceof Boolean) {
         out.value((Boolean)raw);
      } else if (raw instanceof Number) {
         out.value((Number)raw);
      } else {
         if (!(raw instanceof Component)) {
            throw new IllegalStateException("Unable to serialize translatable argument of type " + raw.getClass() + ": " + raw);
         }

         this.gson.toJson(raw, SerializerFactory.COMPONENT_TYPE, (JsonWriter)out);
      }

   }

   public TranslationArgument read(final JsonReader in) throws IOException {
      switch(in.peek()) {
      case BOOLEAN:
         return TranslationArgument.bool(in.nextBoolean());
      case NUMBER:
         return TranslationArgument.numeric((Number)this.gson.fromJson((JsonReader)in, (Type)Number.class));
      default:
         return TranslationArgument.component((ComponentLike)this.gson.fromJson((JsonReader)in, (Type)SerializerFactory.COMPONENT_TYPE));
      }
   }
}
