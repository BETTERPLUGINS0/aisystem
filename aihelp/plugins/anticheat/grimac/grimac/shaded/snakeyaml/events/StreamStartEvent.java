package ac.grim.grimac.shaded.snakeyaml.events;

import ac.grim.grimac.shaded.snakeyaml.error.Mark;

public final class StreamStartEvent extends Event {
   public StreamStartEvent(Mark startMark, Mark endMark) {
      super(startMark, endMark);
   }

   public Event.ID getEventId() {
      return Event.ID.StreamStart;
   }
}
