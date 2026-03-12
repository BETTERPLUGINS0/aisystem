package me.SuperRonanCraft.BetterRTP.player.events;

import me.SuperRonanCraft.BetterRTP.references.player.HelperPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class Damage {
   static boolean canCancel(DamageCause damageCause) {
      return true;
   }

   static boolean isInInvincibleMode(Player player) {
      return HelperPlayer.getData(player).getInvincibleEndTime() > System.currentTimeMillis();
   }

   static void onEntityDamage(EntityDamageEvent event) {
      Entity entity = event.getEntity();
      if (entity instanceof Player) {
         Player player = (Player)entity;
         if (canCancel(event.getCause())) {
            if (isInInvincibleMode(player)) {
               event.setCancelled(true);
            }
         }
      }
   }
}
