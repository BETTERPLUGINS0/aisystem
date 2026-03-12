package ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson;

import ac.grim.grimac.shaded.kyori.adventure.text.format.ShadowColor;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.json.JSONOptions;
import ac.grim.grimac.shaded.kyori.option.OptionState;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

final class ShadowColorSerializer extends TypeAdapter<ShadowColor> {
   private final boolean emitArray;

   static TypeAdapter<ShadowColor> create(final OptionState options) {
      return (new ShadowColorSerializer(options.value(JSONOptions.SHADOW_COLOR_MODE) == JSONOptions.ShadowColorEmitMode.EMIT_ARRAY)).nullSafe();
   }

   private ShadowColorSerializer(final boolean emitArray) {
      this.emitArray = emitArray;
   }

   public void write(final JsonWriter out, final ShadowColor value) throws IOException {
      if (this.emitArray) {
         out.beginArray().value((double)componentAsFloat(value.red())).value((double)componentAsFloat(value.green())).value((double)componentAsFloat(value.blue())).value((double)componentAsFloat(value.alpha())).endArray();
      } else {
         out.value((long)value.value());
      }

   }

   public ShadowColor read(final JsonReader in) throws IOException {
      if (in.peek() == JsonToken.BEGIN_ARRAY) {
         in.beginArray();
         double r = in.nextDouble();
         double g = in.nextDouble();
         double b = in.nextDouble();
         double a = in.nextDouble();
         if (in.peek() != JsonToken.END_ARRAY) {
            throw new JsonParseException("Failed to parse shadow colour at " + in.getPath() + ": expected end of 4-element array but got " + in.peek() + " instead.");
         } else {
            in.endArray();
            return ShadowColor.shadowColor(componentFromFloat(r), componentFromFloat(g), componentFromFloat(b), componentFromFloat(a));
         }
      } else {
         return ShadowColor.shadowColor(in.nextInt());
      }
   }

   static float componentAsFloat(final int element) {
      return (float)element / 255.0F;
   }

   static int componentFromFloat(final double element) {
      return (int)((float)element * 255.0F);
   }
}
