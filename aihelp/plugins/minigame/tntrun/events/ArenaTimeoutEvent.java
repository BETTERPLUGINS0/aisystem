package tntrun.events;

import tntrun.arena.Arena;

public class ArenaTimeoutEvent extends TNTRunEvent {
   public ArenaTimeoutEvent(Arena arena) {
      super(arena);
   }
}
