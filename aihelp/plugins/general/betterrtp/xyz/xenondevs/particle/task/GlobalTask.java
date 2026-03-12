package xyz.xenondevs.particle.task;

import java.util.Collection;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class GlobalTask extends ParticleTask {
   public GlobalTask(List<Object> packets, int tickDelay) {
      super(packets, tickDelay);
   }

   public Collection<Player> getTargetPlayers() {
      return Bukkit.getOnlinePlayers();
   }
}
