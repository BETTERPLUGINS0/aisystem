package tntrun.events;

import org.bukkit.entity.Player;
import tntrun.arena.Arena;

public class PlayerWinArenaEvent extends TNTRunEvent {
   public PlayerWinArenaEvent(Player player, Arena arena) {
      super(player, arena);
   }
}
