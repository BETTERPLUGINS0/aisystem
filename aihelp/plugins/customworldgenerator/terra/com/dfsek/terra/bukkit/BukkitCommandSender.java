package com.dfsek.terra.bukkit;

import com.dfsek.terra.api.command.CommandSender;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.Player;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import java.util.Optional;
import org.bukkit.ChatColor;

public class BukkitCommandSender implements CommandSender {
   private final org.bukkit.command.CommandSender delegate;

   public BukkitCommandSender(org.bukkit.command.CommandSender delegate) {
      this.delegate = delegate;
   }

   public void sendMessage(String message) {
      this.delegate.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
   }

   public Optional<Entity> getEntity() {
      org.bukkit.command.CommandSender var2 = this.delegate;
      if (var2 instanceof org.bukkit.entity.Entity) {
         org.bukkit.entity.Entity entity = (org.bukkit.entity.Entity)var2;
         return Optional.of(BukkitAdapter.adapt(entity));
      } else {
         return Optional.empty();
      }
   }

   public Optional<Player> getPlayer() {
      org.bukkit.command.CommandSender var2 = this.delegate;
      if (var2 instanceof org.bukkit.entity.Player) {
         org.bukkit.entity.Player player = (org.bukkit.entity.Player)var2;
         return Optional.of(BukkitAdapter.adapt(player));
      } else {
         return Optional.empty();
      }
   }

   public org.bukkit.command.CommandSender getHandle() {
      return this.delegate;
   }
}
