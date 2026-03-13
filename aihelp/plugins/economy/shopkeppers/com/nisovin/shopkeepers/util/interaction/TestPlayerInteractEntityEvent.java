package com.nisovin.shopkeepers.util.interaction;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class TestPlayerInteractEntityEvent extends PlayerInteractEntityEvent {
   public TestPlayerInteractEntityEvent(Player who, Entity clickedEntity) {
      super(who, clickedEntity);
   }
}
