package fr.xephi.authme.libs.net.kyori.adventure.text.serializer.gson;

import fr.xephi.authme.libs.com.google.gson.JsonParseException;
import fr.xephi.authme.libs.com.google.gson.JsonSyntaxException;
import fr.xephi.authme.libs.com.google.gson.TypeAdapter;
import fr.xephi.authme.libs.com.google.gson.stream.JsonReader;
import fr.xephi.authme.libs.com.google.gson.stream.JsonWriter;
import fr.xephi.authme.libs.net.kyori.adventure.text.format.TextColor;
import fr.xephi.authme.libs.net.kyori.adventure.text.format.TextDecoration;
import java.io.IOException;
import org.jetbrains.annotations.Nullable;

final class TextColorWrapper {
   @Nullable
   final TextColor color;
   @Nullable
   final TextDecoration decoration;
   final boolean reset;

   TextColorWrapper(@Nullable final TextColor color, @Nullable final TextDecoration decoration, final boolean reset) {
      this.color = color;
      this.decoration = decoration;
      this.reset = reset;
   }

   static final class Serializer extends TypeAdapter<TextColorWrapper> {
      static final TextColorWrapper.Serializer INSTANCE = new TextColorWrapper.Serializer();

      private Serializer() {
      }

      public void write(final JsonWriter out, final TextColorWrapper value) {
         throw new JsonSyntaxException("Cannot write TextColorWrapper instances");
      }

      public TextColorWrapper read(final JsonReader in) throws IOException {
         String input = in.nextString();
         TextColor color = TextColorSerializer.fromString(input);
         TextDecoration decoration = (TextDecoration)TextDecoration.NAMES.value(input);
         boolean reset = decoration == null && input.equals("reset");
         if (color == null && decoration == null && !reset) {
            throw new JsonParseException("Don't know how to parse " + input + " at " + in.getPath());
         } else {
            return new TextColorWrapper(color, decoration, reset);
         }
      }
   }
}
