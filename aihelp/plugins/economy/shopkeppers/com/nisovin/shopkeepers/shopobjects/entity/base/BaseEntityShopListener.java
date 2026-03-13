package com.nisovin.shopkeepers.shopobjects.entity.base;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.shopobjects.DefaultShopObjectTypes;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopkeeper.registry.SKShopkeeperRegistry;
import com.nisovin.shopkeepers.util.bukkit.EntityUtils;
import com.nisovin.shopkeepers.util.bukkit.EventUtils;
import com.nisovin.shopkeepers.util.interaction.InteractionUtils;
import com.nisovin.shopkeepers.util.interaction.TestPlayerInteractEntityEvent;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityMountEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;
import org.bukkit.projectiles.ProjectileSource;

class BaseEntityShopListener implements Listener {
   private final SKShopkeepersPlugin plugin;
   private final SKShopkeeperRegistry shopkeeperRegistry;
   private final Map<UUID, BaseEntityShopListener.EntityInteraction> lastEntityInteractions = new HashMap();

   BaseEntityShopListener(SKShopkeepersPlugin plugin) {
      this.plugin = plugin;
      this.shopkeeperRegistry = plugin.getShopkeeperRegistry();
   }

   void onEnable() {
      Bukkit.getPluginManager().registerEvents(this, this.plugin);
      EventUtils.enforceExecuteFirst(PlayerInteractEntityEvent.class, EventPriority.LOWEST, (Plugin)this.plugin);
      EventUtils.enforceExecuteFirst(PlayerInteractAtEntityEvent.class, EventPriority.LOWEST, (Plugin)this.plugin);
   }

   void onDisable() {
      HandlerList.unregisterAll(this);
   }

   @EventHandler(
      priority = EventPriority.LOWEST,
      ignoreCancelled = false
   )
   void onEntityInteract(PlayerInteractEntityEvent event) {
      if (!(event instanceof TestPlayerInteractEntityEvent)) {
         Entity clickedEntity = EntityUtils.resolveComplexEntity(event.getRightClicked());
         Player player = event.getPlayer();
         boolean isInteractAtEvent = event instanceof PlayerInteractAtEntityEvent;
         Log.debug(() -> {
            String var10000 = player.getName();
            return "Player " + var10000 + " is interacting (" + String.valueOf(event.getHand()) + ") " + (isInteractAtEvent ? "at " : "with ") + String.valueOf(clickedEntity.getType()) + " at " + String.valueOf(clickedEntity.getLocation());
         });
         AbstractShopkeeper shopkeeper = this.shopkeeperRegistry.getShopkeeperByEntity(clickedEntity);
         if (shopkeeper == null) {
            Log.debug("  Non-shopkeeper");
         } else if (event.isCancelled()) {
            Log.debug("  Ignoring already cancelled event.");
         } else {
            if (Settings.cancelCitizenNpcInteractions || shopkeeper.getShopObject().getType() != DefaultShopObjectTypes.CITIZEN()) {
               Log.debug("  Cancelling entity interaction");
               event.setCancelled(true);
               player.updateInventory();
            }

            if (event.getHand() != EquipmentSlot.HAND) {
               Log.debug("  Ignoring off-hand interaction");
            } else {
               BaseEntityShopListener.EntityInteraction lastEntityInteraction = (BaseEntityShopListener.EntityInteraction)this.lastEntityInteractions.computeIfAbsent(player.getUniqueId(), (playerId) -> {
                  return new BaseEntityShopListener.EntityInteraction(this);
               });
               if (lastEntityInteraction.checkAndUpdate(clickedEntity.getUniqueId())) {
                  Log.debug("  Ignoring already handled entity interaction");
               } else if (Settings.checkShopInteractionResult && !InteractionUtils.checkEntityInteract(player, clickedEntity)) {
                  Log.debug("  Cancelled by another plugin");
               } else {
                  shopkeeper.onPlayerInteraction(player);
               }
            }
         }
      }
   }

   @EventHandler(
      priority = EventPriority.LOWEST,
      ignoreCancelled = false
   )
   void onEntityInteractAt(PlayerInteractAtEntityEvent event) {
      this.onEntityInteract(event);
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onPlayerQuit(PlayerQuitEvent event) {
      this.clearLastEntityInteraction(event.getPlayer());
   }

   private void clearLastEntityInteraction(Player player) {
      this.lastEntityInteractions.remove(player.getUniqueId());
   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onEntityTarget(EntityTargetEvent event) {
      Entity entity = event.getEntity();
      Entity target = event.getTarget();
      if (this.shopkeeperRegistry.isShopkeeper(entity) || target != null && this.shopkeeperRegistry.isShopkeeper(target)) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onEntityDamage(EntityDamageEvent event) {
      Entity entity = event.getEntity();
      EntityDamageByEntityEvent entityDamageByEntityEvent;
      if (this.shopkeeperRegistry.isShopkeeper(entity)) {
         event.setCancelled(true);
         if (event instanceof EntityDamageByEntityEvent) {
            entityDamageByEntityEvent = (EntityDamageByEntityEvent)event;
            Entity var5 = entityDamageByEntityEvent.getDamager();
            if (var5 instanceof Mob) {
               Mob attacker = (Mob)var5;
               if (entity.equals(attacker.getTarget())) {
                  attacker.setTarget((LivingEntity)null);
               }
            }
         }

      } else {
         if (event instanceof EntityDamageByEntityEvent) {
            entityDamageByEntityEvent = (EntityDamageByEntityEvent)event;
            if (this.shopkeeperRegistry.isShopkeeper(entityDamageByEntityEvent.getDamager())) {
               event.setCancelled(true);
            }
         }

      }
   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onEntityEnterVehicle(VehicleEnterEvent event) {
      Entity entity = event.getEntered();
      if (this.shopkeeperRegistry.isShopkeeper(entity)) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onEntityMount(EntityMountEvent event) {
      Entity entity = event.getEntity();
      if (this.shopkeeperRegistry.isShopkeeper(entity)) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onExplodePrime(ExplosionPrimeEvent event) {
      if (this.shopkeeperRegistry.isShopkeeper(event.getEntity())) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onExplode(EntityExplodeEvent event) {
      if (this.shopkeeperRegistry.isShopkeeper(event.getEntity())) {
         event.setCancelled(true);
         Log.debug(() -> {
            return "Cancelled event for entity shop: " + event.getEventName();
         });
      }

   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onEntityChangeBlock(EntityChangeBlockEvent event) {
      if (this.shopkeeperRegistry.isShopkeeper(event.getEntity())) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onEntityTeleport(EntityTeleportEvent event) {
      if (this.shopkeeperRegistry.isShopkeeper(event.getEntity())) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onEntityPortalTeleport(EntityPortalEvent event) {
      if (this.shopkeeperRegistry.isShopkeeper(event.getEntity())) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onEntityTransform(EntityTransformEvent event) {
      if (this.shopkeeperRegistry.isShopkeeper(event.getEntity())) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onEntityLaunchProjectile(ProjectileLaunchEvent event) {
      ProjectileSource source = event.getEntity().getShooter();
      if (source instanceof Entity) {
         if (this.shopkeeperRegistry.isShopkeeper((Entity)source)) {
            event.setCancelled(true);
         }

      }
   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onEntityBlockForm(EntityBlockFormEvent event) {
      if (this.shopkeeperRegistry.isShopkeeper(event.getEntity())) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onEntityDropItem(EntityDropItemEvent event) {
      if (this.shopkeeperRegistry.isShopkeeper(event.getEntity())) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onEntityCombustEvent(EntityCombustEvent event) {
      if (!(event instanceof EntityCombustByBlockEvent)) {
         Entity entity = event.getEntity();
         if (this.shopkeeperRegistry.isShopkeeper(entity)) {
            event.setCancelled(true);
         }

      }
   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onBlockIgnited(BlockIgniteEvent event) {
      Entity entity = event.getIgnitingEntity();
      if (entity != null && this.shopkeeperRegistry.isShopkeeper(entity)) {
         event.setCancelled(true);
      }

   }

   private class EntityInteraction {
      private static final long TIMEOUT_NANOS;
      private static final UUID UUID_NIL;
      private long lastTimestamp = 0L;
      private UUID lastEntityId;

      public EntityInteraction(final BaseEntityShopListener param1) {
         this.lastEntityId = UUID_NIL;
      }

      public boolean checkAndUpdate(UUID entityId) {
         long nowNanos = System.nanoTime();
         if (entityId.equals(this.lastEntityId) && nowNanos - this.lastTimestamp < TIMEOUT_NANOS) {
            return true;
         } else {
            this.lastTimestamp = nowNanos;
            this.lastEntityId = entityId;
            return false;
         }
      }

      static {
         TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(250L);
         UUID_NIL = new UUID(0L, 0L);
      }
   }
}
