package tntrun.events;

import org.bukkit.entity.Player;
import tntrun.arena.Arena;

public class PlayerSpectateArenaEvent extends TNTRunEvent {
   public PlayerSpectateArenaEvent(Player player, Arena arena) {
      super(player, arena);
   }
}
