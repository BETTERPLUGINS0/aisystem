package ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.gson;

import ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.option.OptionState;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.json.JSONOptions;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.UUID;

final class UUIDSerializer extends TypeAdapter<UUID> {
   private final boolean emitIntArray;

   static TypeAdapter<UUID> uuidSerializer(final OptionState features) {
      return (new UUIDSerializer((Boolean)features.value(JSONOptions.EMIT_HOVER_SHOW_ENTITY_ID_AS_INT_ARRAY))).nullSafe();
   }

   private UUIDSerializer(final boolean emitIntArray) {
      this.emitIntArray = emitIntArray;
   }

   public void write(final JsonWriter out, final UUID value) throws IOException {
      if (this.emitIntArray) {
         int msb0 = (int)(value.getMostSignificantBits() >> 32);
         int msb1 = (int)(value.getMostSignificantBits() & 4294967295L);
         int lsb0 = (int)(value.getLeastSignificantBits() >> 32);
         int lsb1 = (int)(value.getLeastSignificantBits() & 4294967295L);
         out.beginArray().value((long)msb0).value((long)msb1).value((long)lsb0).value((long)lsb1).endArray();
      } else {
         out.value(value.toString());
      }

   }

   public UUID read(final JsonReader in) throws IOException {
      if (in.peek() == JsonToken.BEGIN_ARRAY) {
         in.beginArray();
         int msb0 = in.nextInt();
         int msb1 = in.nextInt();
         int lsb0 = in.nextInt();
         int lsb1 = in.nextInt();
         in.endArray();
         return new UUID((long)msb0 << 32 | (long)msb1 & 4294967295L, (long)lsb0 << 32 | (long)lsb1 & 4294967295L);
      } else {
         return UUID.fromString(in.nextString());
      }
   }
}
