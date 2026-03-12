package fr.xephi.authme.libs.net.kyori.adventure.text.serializer.gson;

import fr.xephi.authme.libs.com.google.gson.Gson;
import fr.xephi.authme.libs.com.google.gson.JsonParseException;
import fr.xephi.authme.libs.com.google.gson.TypeAdapter;
import fr.xephi.authme.libs.com.google.gson.stream.JsonReader;
import fr.xephi.authme.libs.com.google.gson.stream.JsonWriter;
import fr.xephi.authme.libs.net.kyori.adventure.key.Key;
import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.event.HoverEvent;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.UUID;

final class ShowEntitySerializer extends TypeAdapter<HoverEvent.ShowEntity> {
   private final Gson gson;

   static TypeAdapter<HoverEvent.ShowEntity> create(final Gson gson) {
      return (new ShowEntitySerializer(gson)).nullSafe();
   }

   private ShowEntitySerializer(final Gson gson) {
      this.gson = gson;
   }

   public HoverEvent.ShowEntity read(final JsonReader in) throws IOException {
      in.beginObject();
      Key type = null;
      UUID id = null;
      Component name = null;

      while(in.hasNext()) {
         String fieldName = in.nextName();
         if (fieldName.equals("type")) {
            type = (Key)this.gson.fromJson((JsonReader)in, (Type)SerializerFactory.KEY_TYPE);
         } else if (fieldName.equals("id")) {
            id = (UUID)this.gson.fromJson((JsonReader)in, (Type)SerializerFactory.UUID_TYPE);
         } else if (fieldName.equals("name")) {
            name = (Component)this.gson.fromJson((JsonReader)in, (Type)SerializerFactory.COMPONENT_TYPE);
         } else {
            in.skipValue();
         }
      }

      if (type != null && id != null) {
         in.endObject();
         return HoverEvent.ShowEntity.showEntity(type, id, name);
      } else {
         throw new JsonParseException("A show entity hover event needs type and id fields to be deserialized");
      }
   }

   public void write(final JsonWriter out, final HoverEvent.ShowEntity value) throws IOException {
      out.beginObject();
      out.name("type");
      this.gson.toJson(value.type(), SerializerFactory.KEY_TYPE, (JsonWriter)out);
      out.name("id");
      this.gson.toJson(value.id(), SerializerFactory.UUID_TYPE, (JsonWriter)out);
      Component name = value.name();
      if (name != null) {
         out.name("name");
         this.gson.toJson(name, SerializerFactory.COMPONENT_TYPE, (JsonWriter)out);
      }

      out.endObject();
   }
}
