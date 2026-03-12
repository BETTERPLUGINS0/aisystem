package xyz.xenondevs.particle.task;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class SingularTask extends ParticleTask {
   private final UUID target;

   public SingularTask(List<Object> packets, int tickDelay, UUID target) {
      super(packets, tickDelay);
      this.target = (UUID)Objects.requireNonNull(target);
   }

   public SingularTask(List<Object> packets, int tickDelay, Player target) {
      super(packets, tickDelay);
      this.target = ((Player)Objects.requireNonNull(target)).getUniqueId();
   }

   public List<Player> getTargetPlayers() {
      Player player = Bukkit.getPlayer(this.target);
      return player == null ? Collections.EMPTY_LIST : Collections.singletonList(player);
   }
}
