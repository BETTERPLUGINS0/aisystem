package com.nisovin.shopkeepers.shopobjects.living;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopobjects.ShopObject;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopkeeper.registry.SKShopkeeperRegistry;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.Collection;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.PigZombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.PigZapEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.SheepDyeWoolEvent;
import org.bukkit.event.entity.CreeperPowerEvent.PowerCause;
import org.bukkit.event.entity.EntityPotionEffectEvent.Action;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;

class LivingEntityShopListener implements Listener {
   private final SKShopkeepersPlugin plugin;
   private final SKShopkeeperRegistry shopkeeperRegistry;

   LivingEntityShopListener(SKShopkeepersPlugin plugin) {
      this.plugin = plugin;
      this.shopkeeperRegistry = plugin.getShopkeeperRegistry();
   }

   void onEnable() {
      Bukkit.getPluginManager().registerEvents(this, this.plugin);
   }

   void onDisable() {
      HandlerList.unregisterAll(this);
   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onCreeperCharged(CreeperPowerEvent event) {
      if (event.getCause() == PowerCause.LIGHTNING) {
         if (this.shopkeeperRegistry.isShopkeeper((Entity)event.getEntity())) {
            event.setCancelled(true);
         }

      }
   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onPigZap(PigZapEvent event) {
      if (this.shopkeeperRegistry.isShopkeeper((Entity)event.getEntity())) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onSheepDyed(SheepDyeWoolEvent event) {
      if (this.shopkeeperRegistry.isShopkeeper((Entity)event.getEntity())) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onPotionSplash(PotionSplashEvent event) {
      Iterator var2 = event.getAffectedEntities().iterator();

      while(var2.hasNext()) {
         LivingEntity entity = (LivingEntity)var2.next();

         assert entity != null;

         if (this.shopkeeperRegistry.isShopkeeper((Entity)entity)) {
            event.setIntensity(entity, 0.0D);
         }
      }

   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onEntityPotionEffectEvent(EntityPotionEffectEvent event) {
      if (event.getAction() == Action.ADDED) {
         AbstractShopkeeper shopkeeper = this.shopkeeperRegistry.getShopkeeperByEntity(event.getEntity());
         if (shopkeeper != null) {
            ShopObject shopObject = shopkeeper.getShopObject();
            if (shopObject instanceof SKLivingShopObject) {
               SKLivingShopObject<?> livingShopObject = (SKLivingShopObject)shopObject;
               if (livingShopObject.getDefaultPotionEffects().contains(event.getNewEffect())) {
                  return;
               }
            }

            event.setCancelled(true);
         }
      }
   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onEntityPickupItemEvent(EntityPickupItemEvent event) {
      if (this.shopkeeperRegistry.isShopkeeper((Entity)event.getEntity())) {
         event.setCancelled(true);
      }

   }

   @EventHandler
   void onBlockDispenseArmorEvent(BlockDispenseArmorEvent event) {
      if (this.shopkeeperRegistry.isShopkeeper((Entity)event.getTargetEntity())) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = false
   )
   void onPlayerEnterBed(PlayerBedEnterEvent event) {
      if (event.getBedEnterResult() == BedEnterResult.NOT_SAFE) {
         Block bedBlock = event.getBed();
         Collection<Entity> monsters = (Collection)Unsafe.castNonNull(bedBlock.getWorld().getNearbyEntities(bedBlock.getLocation(), 8.0D, 5.0D, 8.0D, (entityx) -> {
            if (!(entityx instanceof Monster)) {
               return false;
            } else {
               return entityx instanceof PigZombie ? ((PigZombie)entityx).isAngry() : true;
            }
         }));
         Iterator var4 = monsters.iterator();

         Entity entity;
         do {
            if (!var4.hasNext()) {
               Log.debug(() -> {
                  return "Allowing sleeping of player '" + event.getPlayer().getName() + "': The only nearby monsters are shopkeepers.";
               });
               event.setUseBed(Result.ALLOW);
               return;
            }

            entity = (Entity)var4.next();
         } while(this.shopkeeperRegistry.isShopkeeper(entity));

      }
   }
}
