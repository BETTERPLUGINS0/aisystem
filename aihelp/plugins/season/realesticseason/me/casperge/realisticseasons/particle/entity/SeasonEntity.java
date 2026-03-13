package me.casperge.realisticseasons.particle.entity;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface SeasonEntity {
   void tick(List<Player> var1);

   boolean isDestroyed();

   Location getLocation();
}
