package ac.grim.grimac.shaded.snakeyaml.parser;

import ac.grim.grimac.shaded.snakeyaml.events.Event;

public interface Parser {
   boolean checkEvent(Event.ID var1);

   Event peekEvent();

   Event getEvent();
}
