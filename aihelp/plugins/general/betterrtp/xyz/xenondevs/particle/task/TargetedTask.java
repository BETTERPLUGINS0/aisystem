package xyz.xenondevs.particle.task;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.bukkit.entity.Player;

public final class TargetedTask extends ParticleTask {
   private final Collection<Player> targets;

   public TargetedTask(List<Object> packets, int tickDelay, Collection<Player> targets) {
      super(packets, tickDelay);
      this.targets = (Collection)Objects.requireNonNull(targets);
   }

   public Collection<Player> getTargetPlayers() {
      return this.targets;
   }
}
