package ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson;

import ac.grim.grimac.shaded.kyori.adventure.text.event.HoverEvent;
import com.google.gson.TypeAdapter;

final class HoverEventActionSerializer {
   static final TypeAdapter<HoverEvent.Action<?>> INSTANCE;

   private HoverEventActionSerializer() {
   }

   static {
      INSTANCE = IndexedSerializer.lenient("hover action", HoverEvent.Action.NAMES);
   }
}
