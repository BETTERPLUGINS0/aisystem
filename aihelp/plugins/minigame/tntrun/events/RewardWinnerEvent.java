package tntrun.events;

import org.bukkit.entity.Player;
import tntrun.arena.Arena;

public class RewardWinnerEvent extends TNTRunEvent {
   public RewardWinnerEvent(Player player, Arena arena) {
      super(player, arena);
   }
}
