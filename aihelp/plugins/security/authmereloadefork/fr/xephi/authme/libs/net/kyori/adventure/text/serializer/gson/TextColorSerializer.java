package fr.xephi.authme.libs.net.kyori.adventure.text.serializer.gson;

import fr.xephi.authme.libs.com.google.gson.TypeAdapter;
import fr.xephi.authme.libs.com.google.gson.stream.JsonReader;
import fr.xephi.authme.libs.com.google.gson.stream.JsonWriter;
import fr.xephi.authme.libs.net.kyori.adventure.text.format.NamedTextColor;
import fr.xephi.authme.libs.net.kyori.adventure.text.format.TextColor;
import java.io.IOException;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
