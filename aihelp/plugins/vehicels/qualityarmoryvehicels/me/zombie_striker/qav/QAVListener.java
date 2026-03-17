/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.block.BlockFace
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.player.PlayerArmorStandManipulateEvent
 *  org.bukkit.event.player.PlayerInteractEntityEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerKickEvent
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.inventory.EquipmentSlot
 *  org.bukkit.inventory.ItemStack
 */
package me.zombie_striker.qav;

import java.util.function.Consumer;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.MessagesConfig;
import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import me.zombie_striker.qav.api.events.VehicleDamageEvent;
import me.zombie_striker.qav.api.events.VehicleDestroyEvent;
import me.zombie_striker.qav.api.events.VehicleRepairEvent;
import me.zombie_striker.qav.hooks.model.Animation;
import me.zombie_striker.qav.menu.OverviewMenu;
import me.zombie_striker.qav.qamini.ParticleHandlers;
import me.zombie_striker.qav.util.ForksUtil;
import me.zombie_striker.qav.util.VehicleUtils;
import me.zombie_striker.qav.vehicles.AbstractCar;
import me.zombie_striker.qav.vehicles.AbstractHelicopter;
import me.zombie_striker.qav.vehicles.AbstractPlane;
import me.zombie_striker.qav.vehicles.AbstractVehicle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class QAVListener
implements Listener {
    private Main main;

    public QAVListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onClickVehicle(PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.getPlayer().getVehicle() != null) {
            return;
        }
        if (playerInteractEvent.getAction() == Action.RIGHT_CLICK_AIR || playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Main.DEBUG("Player is interacting.");
            VehicleEntity vehicleEntity = QualityArmoryVehicles.getVehiclePlayerLookingAt(playerInteractEvent.getPlayer());
            if (vehicleEntity == null) {
                return;
            }
            Main.DEBUG("Detected hitbox interaction.");
            playerInteractEvent.setCancelled(true);
            ItemStack itemStack = playerInteractEvent.getPlayer().getInventory().getItemInMainHand();
            if (playerInteractEvent.getPlayer().hasPermission("qualityarmoryvehicles.repair") && Main.repairItem.isItem(itemStack)) {
                if (vehicleEntity.getHealth() >= vehicleEntity.getType().getMaxHealth()) {
                    return;
                }
                VehicleRepairEvent vehicleRepairEvent = new VehicleRepairEvent(playerInteractEvent.getPlayer(), itemStack, vehicleEntity);
                Bukkit.getPluginManager().callEvent((Event)vehicleRepairEvent);
                if (vehicleRepairEvent.isCancelled()) {
                    return;
                }
                vehicleEntity.setHealth(vehicleEntity.getType().getMaxHealth());
                playerInteractEvent.getPlayer().sendMessage(Main.prefix + MessagesConfig.MESSAGE_REPAIR);
                return;
            }
            if (playerInteractEvent.getPlayer().isSneaking() && vehicleEntity.allowUserDriver(playerInteractEvent.getPlayer().getUniqueId())) {
                new OverviewMenu(playerInteractEvent.getPlayer(), vehicleEntity).open();
                return;
            }
            if (vehicleEntity.allowUserDriver(playerInteractEvent.getPlayer().getUniqueId())) {
                if (vehicleEntity.getDriverSeat().getPassenger() == null) {
                    vehicleEntity.getType().playAnimation(vehicleEntity, Animation.AnimationType.ENTER, "driver");
                    vehicleEntity.getDriverSeat().setPassenger((Entity)playerInteractEvent.getPlayer());
                    return;
                }
                if (vehicleEntity.allowUserPassager(playerInteractEvent.getPlayer().getUniqueId())) {
                    QualityArmoryVehicles.addPlayerToCar(vehicleEntity, playerInteractEvent.getPlayer(), false);
                }
            } else if (vehicleEntity.allowUserPassager(playerInteractEvent.getPlayer().getUniqueId())) {
                QualityArmoryVehicles.addPlayerToCar(vehicleEntity, playerInteractEvent.getPlayer(), false);
            }
        }
    }

    @EventHandler
    public void onPlace(PlayerInteractEvent playerInteractEvent) {
        AbstractVehicle abstractVehicle;
        if (playerInteractEvent.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }
        if (playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK && (abstractVehicle = QualityArmoryVehicles.getVehicleByItem(playerInteractEvent.getItem())) != null) {
            playerInteractEvent.setCancelled(true);
            VehicleEntity vehicleEntity = QualityArmoryVehicles.spawnVehicle(abstractVehicle, playerInteractEvent.getClickedBlock().getRelative(BlockFace.UP).getLocation(), playerInteractEvent.getPlayer());
            if (vehicleEntity == null) {
                return;
            }
            if (playerInteractEvent.getPlayer().getGameMode() != GameMode.CREATIVE) {
                playerInteractEvent.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            }
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent playerKickEvent) {
        if (!ForksUtil.isFlyKick(playerKickEvent)) {
            return;
        }
        if (playerKickEvent.getPlayer().getVehicle() == null) {
            return;
        }
        if (!QualityArmoryVehicles.isVehicle(playerKickEvent.getPlayer().getVehicle())) {
            return;
        }
        playerKickEvent.setCancelled(true);
        Main.DEBUG("Cancelled kick event for flying because player is on plane.");
    }

    @EventHandler
    public void oninteractEntity(PlayerInteractEntityEvent playerInteractEntityEvent) {
        if (playerInteractEntityEvent.getPlayer().getVehicle() == null) {
            VehicleEntity vehicleEntity = QualityArmoryVehicles.getVehicleEntityByEntity(playerInteractEntityEvent.getRightClicked());
            if (vehicleEntity == null && playerInteractEntityEvent.getRightClicked() instanceof Player && playerInteractEntityEvent.getRightClicked().getVehicle() != null) {
                vehicleEntity = QualityArmoryVehicles.getVehicleEntityByEntity(playerInteractEntityEvent.getRightClicked().getVehicle());
            }
            if (vehicleEntity != null) {
                playerInteractEntityEvent.setCancelled(true);
                if (playerInteractEntityEvent.getPlayer().isSneaking() && vehicleEntity.allowUserDriver(playerInteractEntityEvent.getPlayer().getUniqueId())) {
                    new OverviewMenu(playerInteractEntityEvent.getPlayer(), vehicleEntity).open();
                } else if (vehicleEntity.allowUserDriver(playerInteractEntityEvent.getPlayer().getUniqueId()) && vehicleEntity.getDriverSeat().getPassenger() == null) {
                    vehicleEntity.getType().playAnimation(vehicleEntity, Animation.AnimationType.ENTER, "driver");
                    vehicleEntity.getDriverSeat().setPassenger((Entity)playerInteractEntityEvent.getPlayer());
                }
            }
        }
    }

    @EventHandler
    public void onManipulate(PlayerArmorStandManipulateEvent playerArmorStandManipulateEvent) {
        VehicleEntity vehicleEntity = null;
        if (QualityArmoryVehicles.isPassager((Entity)playerArmorStandManipulateEvent.getRightClicked())) {
            for (VehicleEntity vehicleEntity2 : Main.vehicles) {
                if (!vehicleEntity2.getPassagers().containsValue(playerArmorStandManipulateEvent.getRightClicked())) continue;
                vehicleEntity = vehicleEntity2;
                break;
            }
        } else if (QualityArmoryVehicles.isVehicle((Entity)playerArmorStandManipulateEvent.getRightClicked())) {
            vehicleEntity = QualityArmoryVehicles.getVehicleEntityByEntity((Entity)playerArmorStandManipulateEvent.getRightClicked());
        }
        if (vehicleEntity == null) {
            return;
        }
        playerArmorStandManipulateEvent.setCancelled(true);
        if (vehicleEntity.allowUserPassager(playerArmorStandManipulateEvent.getPlayer().getUniqueId())) {
            if (playerArmorStandManipulateEvent.getPlayer().isSneaking() && vehicleEntity.allowUserDriver(playerArmorStandManipulateEvent.getPlayer().getUniqueId())) {
                if (playerArmorStandManipulateEvent.getPlayer().hasPermission("qualityarmoryvehicles.usevehiclegui")) {
                    new OverviewMenu(playerArmorStandManipulateEvent.getPlayer(), vehicleEntity).open();
                } else {
                    playerArmorStandManipulateEvent.getPlayer().sendMessage(ChatColor.RED + " You do not have permission to use this vehicle.");
                }
            } else if (playerArmorStandManipulateEvent.getPlayer().hasPermission("qualityarmoryvehicles.use")) {
                QualityArmoryVehicles.addPlayerToCar(vehicleEntity, playerArmorStandManipulateEvent.getPlayer(), vehicleEntity.allowUserDriver(playerArmorStandManipulateEvent.getPlayer().getUniqueId()));
            } else {
                playerArmorStandManipulateEvent.getPlayer().sendMessage(ChatColor.RED + " You do not have permission to use this vehicle.");
            }
        } else if (playerArmorStandManipulateEvent.getPlayer().hasPermission("qualityarmoryvehicles.overrideWhitelist") || VehicleUtils.isOverrideWhitelisted(playerArmorStandManipulateEvent.getPlayer().getUniqueId())) {
            if (vehicleEntity.getDriverSeat().getPassenger() == null) {
                if (playerArmorStandManipulateEvent.getPlayer().hasPermission("qualityarmoryvehicles.use")) {
                    vehicleEntity.getDriverSeat().setPassenger((Entity)playerArmorStandManipulateEvent.getPlayer());
                } else {
                    playerArmorStandManipulateEvent.getPlayer().sendMessage(ChatColor.RED + " You do not have permission to use this vehicle.");
                }
                return;
            }
            QualityArmoryVehicles.addPlayerToCar(vehicleEntity, playerArmorStandManipulateEvent.getPlayer(), vehicleEntity.allowUserDriver(playerArmorStandManipulateEvent.getPlayer().getUniqueId()));
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent playerMoveEvent) {
        if (!Main.enableVehiclePlayerCollision) {
            return;
        }
        for (Entity entity : playerMoveEvent.getPlayer().getNearbyEntities(5.0, 5.0, 5.0)) {
            if (!QualityArmoryVehicles.isVehicle(entity)) continue;
            VehicleEntity vehicleEntity = QualityArmoryVehicles.getVehicleEntityByEntity(entity);
            if (vehicleEntity == null) {
                return;
            }
            if (QualityArmoryVehicles.isWithinVehicle(playerMoveEvent.getTo(), vehicleEntity) && !QualityArmoryVehicles.isWithinVehicle(playerMoveEvent.getFrom(), vehicleEntity)) {
                if ((playerMoveEvent.getTo().getX() != playerMoveEvent.getFrom().getX() || playerMoveEvent.getTo().getZ() != playerMoveEvent.getFrom().getZ()) && playerMoveEvent.getPlayer().getVelocity().getY() < -0.05) {
                    playerMoveEvent.getPlayer().setVelocity(playerMoveEvent.getPlayer().getVelocity().setY(0.3));
                }
                playerMoveEvent.setCancelled(true);
                break;
            }
            Location location = playerMoveEvent.getTo().clone().add(0.0, 1.0, 0.0);
            Location location2 = playerMoveEvent.getFrom().clone().add(0.0, 1.0, 0.0);
            if (!QualityArmoryVehicles.isWithinVehicle(location, vehicleEntity) || QualityArmoryVehicles.isWithinVehicle(location2, vehicleEntity)) continue;
            if ((playerMoveEvent.getTo().getX() != playerMoveEvent.getFrom().getX() || playerMoveEvent.getTo().getZ() != playerMoveEvent.getFrom().getZ()) && playerMoveEvent.getPlayer().getVelocity().getY() < -0.05) {
                playerMoveEvent.getPlayer().setVelocity(playerMoveEvent.getPlayer().getVelocity().setY(0.3));
            }
            playerMoveEvent.setCancelled(true);
            break;
        }
    }

    @EventHandler(priority=EventPriority.LOW)
    public void onDamage(EntityDamageEvent entityDamageEvent) {
        QAVListener.handleDamage((Cancellable)entityDamageEvent, entityDamageEvent.getEntity(), entityDamageEvent.getDamage(), entityDamageEvent.getCause(), arg_0 -> ((EntityDamageEvent)entityDamageEvent).setDamage(arg_0));
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent playerQuitEvent) {
        Object object;
        Entity entity;
        if (Main.removeVehicleONLEAVE || Main.destroyVehicleONLEAVE) {
            entity = QualityArmoryVehicles.getOwnedVehicles(playerQuitEvent.getPlayer().getUniqueId());
            object = entity.iterator();
            while (object.hasNext()) {
                VehicleEntity vehicleEntity = (VehicleEntity)object.next();
                if (Main.destroyVehicleONLEAVE) {
                    vehicleEntity.deconstruct(playerQuitEvent.getPlayer(), "Quit");
                    continue;
                }
                VehicleUtils.callback(vehicleEntity, playerQuitEvent.getPlayer(), "Quit");
            }
        }
        if ((QualityArmoryVehicles.isVehicle(entity = playerQuitEvent.getPlayer().getVehicle()) || QualityArmoryVehicles.isPassager(entity)) && (object = QualityArmoryVehicles.getVehicleEntityByEntity(entity)) != null) {
            if (entity.equals((Object)((VehicleEntity)object).getDriverSeat())) {
                ((VehicleEntity)object).getDriverSeat().eject();
            }
            if (((VehicleEntity)object).getPassagerSeats().contains(entity)) {
                entity.eject();
                entity.remove();
            }
        }
    }

    public static void handleDamage(Cancellable cancellable, Entity entity, double d, EntityDamageEvent.DamageCause damageCause, Consumer<Double> consumer) {
        VehicleEntity vehicleEntity;
        if ((entity.getVehicle() != null && QualityArmoryVehicles.isVehicle(entity.getVehicle()) || QualityArmoryVehicles.isPassager(entity.getVehicle())) && (vehicleEntity = QualityArmoryVehicles.getVehicleEntityByEntity(entity.getVehicle())) != null && vehicleEntity.getType() != null && (vehicleEntity.getType() instanceof AbstractHelicopter || vehicleEntity.getType() instanceof AbstractPlane || vehicleEntity.getType() instanceof AbstractCar) && damageCause == EntityDamageEvent.DamageCause.FALL) {
            cancellable.setCancelled(true);
            return;
        }
        vehicleEntity = null;
        if (QualityArmoryVehicles.isVehicle(entity)) {
            vehicleEntity = QualityArmoryVehicles.getVehicleEntityByEntity(entity);
        } else if (entity.getVehicle() != null && QualityArmoryVehicles.isVehicle(entity.getVehicle())) {
            vehicleEntity = QualityArmoryVehicles.getVehicleEntityByEntity(entity.getVehicle());
        }
        if (vehicleEntity == null) {
            return;
        }
        if (!Main.enableVehicleDamage) {
            cancellable.setCancelled(true);
            return;
        }
        if (damageCause == EntityDamageEvent.DamageCause.SUFFOCATION || damageCause == EntityDamageEvent.DamageCause.DROWNING || damageCause == EntityDamageEvent.DamageCause.FALL) {
            cancellable.setCancelled(true);
            return;
        }
        if (damageCause == EntityDamageEvent.DamageCause.ENTITY_ATTACK && cancellable instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent)cancellable).getDamager().getType() == EntityType.ENDERMITE) {
            cancellable.setCancelled(true);
            return;
        }
        VehicleDamageEvent vehicleDamageEvent = new VehicleDamageEvent(vehicleEntity, d);
        Bukkit.getPluginManager().callEvent((Event)vehicleDamageEvent);
        if (vehicleDamageEvent.isCanceled()) {
            cancellable.setCancelled(true);
            return;
        }
        Main.DEBUG("Damaged vehicle: " + vehicleDamageEvent.getDamage() + " || Health= " + vehicleEntity.getHealth() + " || Cause= " + damageCause.name());
        consumer.accept(vehicleDamageEvent.getDamage());
        vehicleEntity.setHealth((float)(vehicleEntity.getHealth() - vehicleDamageEvent.getDamage()));
        cancellable.setCancelled(true);
        try {
            entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 1.0f);
        } catch (Error | Exception throwable) {
            try {
                entity.getWorld().playSound(entity.getLocation(), Sound.valueOf((String)"HURT"), 1.0f, 1.0f);
            } catch (Error | Exception throwable2) {
                // empty catch block
            }
        }
        if (vehicleEntity.getHealth() <= 0.0) {
            VehicleDestroyEvent vehicleDestroyEvent = new VehicleDestroyEvent(vehicleEntity);
            Bukkit.getPluginManager().callEvent((Event)vehicleDestroyEvent);
            if (vehicleDestroyEvent.isCanceled()) {
                cancellable.setCancelled(true);
                return;
            }
            if (!Main.freezeOnDestroy) {
                vehicleEntity.deconstruct(null, "Destroy");
            }
            vehicleEntity.getType().playAnimation(vehicleEntity, Animation.AnimationType.BREAK, new String[0]);
            try {
                ParticleHandlers.spawnMushroomCloud(entity.getLocation());
            } catch (Error | Exception throwable) {
                // empty catch block
            }
            try {
                entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2.5f, 1.0f);
            } catch (Error | Exception throwable) {
                try {
                    entity.getWorld().playSound(entity.getLocation(), Sound.valueOf((String)"EXPLODE"), 2.5f, 1.0f);
                } catch (Error | Exception throwable3) {
                    // empty catch block
                }
            }
        }
    }
}

