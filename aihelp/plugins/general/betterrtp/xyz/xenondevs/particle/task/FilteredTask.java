package xyz.xenondevs.particle.task;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class FilteredTask extends ParticleTask {
   private final Predicate<Player> filter;

   public FilteredTask(List<Object> packets, int tickDelay, Predicate<Player> filter) {
      super(packets, tickDelay);
      this.filter = (Predicate)Objects.requireNonNull(filter);
   }

   public Collection<Player> getTargetPlayers() {
      return (Collection)Bukkit.getOnlinePlayers().stream().filter(this.filter).collect(Collectors.toList());
   }
}
