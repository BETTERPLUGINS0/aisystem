package ac.grim.grimac.shaded.snakeyaml.events;

import ac.grim.grimac.shaded.snakeyaml.DumperOptions;
import ac.grim.grimac.shaded.snakeyaml.error.Mark;

public final class MappingStartEvent extends CollectionStartEvent {
   public MappingStartEvent(String anchor, String tag, boolean implicit, Mark startMark, Mark endMark, DumperOptions.FlowStyle flowStyle) {
      super(anchor, tag, implicit, startMark, endMark, flowStyle);
   }

   public Event.ID getEventId() {
      return Event.ID.MappingStart;
   }
}
