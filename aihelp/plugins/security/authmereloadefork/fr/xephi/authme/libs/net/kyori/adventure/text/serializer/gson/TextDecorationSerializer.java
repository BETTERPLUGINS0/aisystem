package fr.xephi.authme.libs.net.kyori.adventure.text.serializer.gson;

import fr.xephi.authme.libs.com.google.gson.TypeAdapter;
import fr.xephi.authme.libs.net.kyori.adventure.text.format.TextDecoration;

final class TextDecorationSerializer {
   static final TypeAdapter<TextDecoration> INSTANCE;

   private TextDecorationSerializer() {
   }

   static {
      INSTANCE = IndexedSerializer.strict("text decoration", TextDecoration.NAMES);
   }
}
