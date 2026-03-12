package ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.format.NamedTextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextColor;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Locale;

final class TextColorSerializer extends TypeAdapter<TextColor> {
   static final TypeAdapter<TextColor> INSTANCE = (new TextColorSerializer(false)).nullSafe();
   static final TypeAdapter<TextColor> DOWNSAMPLE_COLOR = (new TextColorSerializer(true)).nullSafe();
   private final boolean downsampleColor;

   private TextColorSerializer(final boolean downsampleColor) {
      this.downsampleColor = downsampleColor;
   }

   public void write(final JsonWriter out, final TextColor value) throws IOException {
      if (value instanceof NamedTextColor) {
         out.value((String)NamedTextColor.NAMES.key((NamedTextColor)value));
      } else if (this.downsampleColor) {
         out.value((String)NamedTextColor.NAMES.key(NamedTextColor.nearestTo(value)));
      } else {
         out.value(asUpperCaseHexString(value));
      }

   }

   private static String asUpperCaseHexString(final TextColor color) {
      return String.format(Locale.ROOT, "%c%06X", '#', color.value());
   }

   @Nullable
   public TextColor read(final JsonReader in) throws IOException {
      TextColor color = fromString(in.nextString());
      if (color == null) {
         return null;
      } else {
         return (TextColor)(this.downsampleColor ? NamedTextColor.nearestTo(color) : color);
      }
   }

   @Nullable
   static TextColor fromString(@NotNull final String value) {
      return value.startsWith("#") ? TextColor.fromHexString(value) : (TextColor)NamedTextColor.NAMES.value(value);
   }
}
