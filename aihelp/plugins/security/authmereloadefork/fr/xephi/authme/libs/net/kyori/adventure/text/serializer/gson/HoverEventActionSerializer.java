package fr.xephi.authme.libs.net.kyori.adventure.text.serializer.gson;

import fr.xephi.authme.libs.com.google.gson.TypeAdapter;
import fr.xephi.authme.libs.net.kyori.adventure.text.event.HoverEvent;

final class HoverEventActionSerializer {
   static final TypeAdapter<HoverEvent.Action<?>> INSTANCE;

   private HoverEventActionSerializer() {
   }

   static {
      INSTANCE = IndexedSerializer.lenient("hover action", HoverEvent.Action.NAMES);
   }
}
