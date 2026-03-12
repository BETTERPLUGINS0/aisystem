package ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.gson;

import ac.grim.grimac.shaded.kyori.adventure.text.object.PlayerHeadObjectContents;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

final class ProfilePropertySerializer extends TypeAdapter<PlayerHeadObjectContents.ProfileProperty> {
   static final TypeAdapter<PlayerHeadObjectContents.ProfileProperty> INSTANCE = (new ProfilePropertySerializer()).nullSafe();

   private ProfilePropertySerializer() {
   }

   public void write(final JsonWriter out, final PlayerHeadObjectContents.ProfileProperty property) throws IOException {
      out.beginObject();
      out.name("name");
      out.value(property.name());
      out.name("value");
      out.value(property.value());
      if (property.signature() != null) {
         out.name("signature");
         out.value(property.signature());
      }

      out.endObject();
   }

   public PlayerHeadObjectContents.ProfileProperty read(final JsonReader in) throws IOException {
      in.beginObject();
      String name = null;
      String value = null;
      String signature = null;

      while(in.hasNext()) {
         String fieldName = in.nextName();
         if (fieldName.equals("name")) {
            name = in.nextString();
         } else if (fieldName.equals("value")) {
            value = in.nextString();
         } else if (fieldName.equals("signature")) {
            signature = in.nextString();
         } else {
            in.skipValue();
         }
      }

      in.endObject();
      if (name != null && value != null) {
         return PlayerHeadObjectContents.property(name, value, signature);
      } else {
         throw new JsonParseException("A profile property requires both a name and value");
      }
   }
}
