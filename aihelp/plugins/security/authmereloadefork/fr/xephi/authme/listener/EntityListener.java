package fr.xephi.authme.listener;

import fr.xephi.authme.libs.javax.inject.Inject;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.projectiles.ProjectileSource;

public class EntityListener implements Listener {
   private final ListenerService listenerService;

   @Inject
   EntityListener(ListenerService listenerService) {
      this.listenerService = listenerService;
   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.LOWEST
   )
   public void onDamage(EntityDamageEvent event) {
      if (this.listenerService.shouldCancelEvent((EntityEvent)event)) {
         event.getEntity().setFireTicks(0);
         event.setDamage(0.0D);
         event.setCancelled(true);
      }

   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.LOWEST
   )
   public void onAttack(EntityDamageByEntityEvent event) {
      if (this.listenerService.shouldCancelEvent(event.getDamager())) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.LOWEST
   )
   public void onEntityTarget(EntityTargetEvent event) {
      if (this.listenerService.shouldCancelEvent(event.getTarget())) {
         event.setTarget((Entity)null);
         event.setCancelled(true);
      }

   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.LOWEST
   )
   public void onFoodLevelChange(FoodLevelChangeEvent event) {
      if (this.listenerService.shouldCancelEvent((EntityEvent)event)) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.LOWEST
   )
   public void entityRegainHealthEvent(EntityRegainHealthEvent event) {
      if (this.listenerService.shouldCancelEvent((EntityEvent)event)) {
         event.setAmount(0.0D);
         event.setCancelled(true);
      }

   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.HIGHEST
   )
   public void onEntityInteract(EntityInteractEvent event) {
      if (this.listenerService.shouldCancelEvent((EntityEvent)event)) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.LOWEST
   )
   public void onLowestEntityInteract(EntityInteractEvent event) {
      if (this.listenerService.shouldCancelEvent((EntityEvent)event)) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.LOWEST
   )
   public void onProjectileLaunch(ProjectileLaunchEvent event) {
      Projectile projectile = event.getEntity();
      ProjectileSource shooter = projectile.getShooter();
      if (shooter instanceof Player && this.listenerService.shouldCancelEvent((Player)shooter)) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.NORMAL
   )
   public void onShoot(EntityShootBowEvent event) {
      if (this.listenerService.shouldCancelEvent((EntityEvent)event)) {
         event.setCancelled(true);
      }

   }
}
