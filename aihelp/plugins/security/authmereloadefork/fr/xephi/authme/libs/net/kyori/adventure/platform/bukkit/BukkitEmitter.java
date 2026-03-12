package fr.xephi.authme.libs.net.kyori.adventure.platform.bukkit;

import fr.xephi.authme.libs.net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Entity;

final class BukkitEmitter implements Sound.Emitter {
   final Entity entity;

   BukkitEmitter(final Entity entity) {
      this.entity = entity;
   }
}
