package tntrun.events;

import org.bukkit.entity.Player;
import tntrun.arena.Arena;

public class PlayerJoinArenaEvent extends TNTRunEvent {
   public PlayerJoinArenaEvent(Player player, Arena arena) {
      super(player, arena);
   }
}
