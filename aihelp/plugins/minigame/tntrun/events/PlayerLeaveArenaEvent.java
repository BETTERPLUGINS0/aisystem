package tntrun.events;

import org.bukkit.entity.Player;
import tntrun.arena.Arena;

public class PlayerLeaveArenaEvent extends TNTRunEvent {
   public PlayerLeaveArenaEvent(Player player, Arena arena) {
      super(player, arena);
   }
}
