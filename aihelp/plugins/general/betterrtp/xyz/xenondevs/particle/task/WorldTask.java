package xyz.xenondevs.particle.task;

import java.util.Collection;
import java.util.List;
import org.bukkit.World;
import org.bukkit.entity.Player;

public final class WorldTask extends ParticleTask {
   private final World world;

   public WorldTask(List<Object> packets, int tickDelay, World world) {
      super(packets, tickDelay);
      this.world = world;
   }

   public Collection<Player> getTargetPlayers() {
      return this.world.getPlayers();
   }
}
