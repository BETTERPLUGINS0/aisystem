package xyz.xenondevs.particle.task;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import org.bukkit.entity.Player;

public final class SuppliedTask extends ParticleTask {
   private final Supplier<Collection<Player>> supplier;

   public SuppliedTask(List<Object> packets, int tickDelay, Supplier<Collection<Player>> supplier) {
      super(packets, tickDelay);
      this.supplier = (Supplier)Objects.requireNonNull(supplier);
   }

   public Collection<Player> getTargetPlayers() {
      return (Collection)this.supplier.get();
   }
}
