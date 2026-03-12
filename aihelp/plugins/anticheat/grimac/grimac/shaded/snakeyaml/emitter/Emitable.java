package ac.grim.grimac.shaded.snakeyaml.emitter;

import ac.grim.grimac.shaded.snakeyaml.events.Event;
import java.io.IOException;

public interface Emitable {
   void emit(Event var1) throws IOException;
}
