package ac.grim.grimac.shaded.snakeyaml.events;

import ac.grim.grimac.shaded.snakeyaml.error.Mark;

public final class AliasEvent extends NodeEvent {
   public AliasEvent(String anchor, Mark startMark, Mark endMark) {
      super(anchor, startMark, endMark);
      if (anchor == null) {
         throw new NullPointerException("anchor is not specified for alias");
      }
   }

   public Event.ID getEventId() {
      return Event.ID.Alias;
   }
}
