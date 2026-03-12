package ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.gson;

import ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent;
import com.google.gson.TypeAdapter;

final class ClickEventActionSerializer {
   static final TypeAdapter<ClickEvent.Action> INSTANCE;

   private ClickEventActionSerializer() {
   }

   static {
      INSTANCE = IndexedSerializer.lenient("click action", ClickEvent.Action.NAMES);
   }
}
