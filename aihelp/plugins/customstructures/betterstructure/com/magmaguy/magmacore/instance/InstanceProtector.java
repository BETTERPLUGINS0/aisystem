/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockBurnEvent
 *  org.bukkit.event.block.BlockCanBuildEvent
 *  org.bukkit.event.block.BlockDamageEvent
 *  org.bukkit.event.block.BlockExplodeEvent
 *  org.bukkit.event.block.BlockFadeEvent
 *  org.bukkit.event.block.BlockFertilizeEvent
 *  org.bukkit.event.block.BlockFromToEvent
 *  org.bukkit.event.block.BlockIgniteEvent
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.block.LeavesDecayEvent
 *  org.bukkit.event.block.SignChangeEvent
 *  org.bukkit.event.block.TNTPrimeEvent
 *  org.bukkit.event.entity.CreatureSpawnEvent
 *  org.bukkit.event.entity.CreatureSpawnEvent$SpawnReason
 *  org.bukkit.event.entity.EntityChangeBlockEvent
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityExplodeEvent
 *  org.bukkit.event.entity.LingeringPotionSplashEvent
 *  org.bukkit.event.entity.PotionSplashEvent
 *  org.bukkit.event.player.PlayerBucketEmptyEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.world.WorldUnloadEvent
 *  org.bukkit.potion.PotionEffectType
 */
package com.magmaguy.magmacore.instance;

import java.util.HashSet;
import java.util.Locale;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.block.TNTPrimeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.potion.PotionEffectType;

public class InstanceProtector
implements Listener {
    private static final HashSet<UUID> bypassingPlayers = new HashSet();
    private static final HashSet<UUID> protectedWorldUUIDs = new HashSet();
    private static final HashSet<UUID> pvpPreventedWorldUUIDs = new HashSet();
    private static final HashSet<UUID> redstonePreventedWorldUUIDs = new HashSet();

    public static void addProtectedWorld(World world) {
        protectedWorldUUIDs.add(world.getUID());
    }

    public static void addPvpPreventedWorld(World world) {
        pvpPreventedWorldUUIDs.add(world.getUID());
    }

    public static void removePvpPreventedWorld(World world) {
        pvpPreventedWorldUUIDs.remove(world.getUID());
    }

    public static void addRedstonePreventedWorld(World world) {
        redstonePreventedWorldUUIDs.add(world.getUID());
    }

    public static void removeRedstonePreventedWorld(World world) {
        redstonePreventedWorldUUIDs.remove(world.getUID());
    }

    private static boolean isProtectedWorld(World world) {
        return protectedWorldUUIDs.contains(world.getUID());
    }

    private static boolean removeProtectedWorld(World world) {
        return protectedWorldUUIDs.remove(world.getUID());
    }

    private static boolean isPvpPreventedWorld(World world) {
        return pvpPreventedWorldUUIDs.contains(world.getUID());
    }

    private static boolean isRedstonePreventedWorld(World world) {
        return redstonePreventedWorldUUIDs.contains(world.getUID());
    }

    public static boolean toggleBypass(UUID playerUUID) {
        if (bypassingPlayers.contains(playerUUID)) {
            bypassingPlayers.remove(playerUUID);
            return false;
        }
        bypassingPlayers.add(playerUUID);
        return true;
    }

    private boolean shouldBypass(Player player) {
        return bypassingPlayers.contains(player.getUniqueId());
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void removeProtectedInstance(WorldUnloadEvent event) {
        protectedWorldUUIDs.remove(event.getWorld().getUID());
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void preventPlayerBlockDamage(BlockDamageEvent event) {
        if (!InstanceProtector.isProtectedWorld(event.getBlock().getWorld())) {
            return;
        }
        if (this.shouldBypass(event.getPlayer())) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void preventPlayerBlockBreak(BlockBreakEvent event) {
        if (!InstanceProtector.isProtectedWorld(event.getBlock().getWorld())) {
            return;
        }
        if (this.shouldBypass(event.getPlayer())) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void preventBlockBurnDamage(BlockBurnEvent event) {
        if (!InstanceProtector.isProtectedWorld(event.getBlock().getWorld())) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void preventPlayerBlockPlace(BlockCanBuildEvent event) {
        if (!InstanceProtector.isProtectedWorld(event.getBlock().getWorld())) {
            return;
        }
        if (event.getPlayer() != null && this.shouldBypass(event.getPlayer())) {
            return;
        }
        event.setBuildable(false);
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void preventBlockExplosionEvent(BlockExplodeEvent event) {
        if (!InstanceProtector.isProtectedWorld(event.getBlock().getWorld())) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void preventEntityExplosionEvent(EntityExplodeEvent event) {
        if (!InstanceProtector.isProtectedWorld(event.getLocation().getWorld())) {
            return;
        }
        event.blockList().clear();
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void preventTntPrimeEvent(TNTPrimeEvent event) {
        if (!InstanceProtector.isProtectedWorld(event.getBlock().getWorld())) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void preventBlockFadeEvent(BlockFadeEvent event) {
        if (!InstanceProtector.isProtectedWorld(event.getBlock().getWorld())) {
            return;
        }
        if (event.getBlock().getType().equals((Object)Material.FROSTED_ICE)) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void preventBonemeal(BlockFertilizeEvent event) {
        if (!InstanceProtector.isProtectedWorld(event.getBlock().getWorld())) {
            return;
        }
        if (event.getPlayer() != null && this.shouldBypass(event.getPlayer())) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void preventLiquidFlow(BlockFromToEvent event) {
        if (!InstanceProtector.isProtectedWorld(event.getBlock().getWorld())) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void preventBlockFire(BlockIgniteEvent event) {
        if (!InstanceProtector.isProtectedWorld(event.getBlock().getWorld())) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void preventBlockPlace(BlockPlaceEvent event) {
        if (!InstanceProtector.isProtectedWorld(event.getBlock().getWorld())) {
            return;
        }
        if (this.shouldBypass(event.getPlayer())) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void preventLiquidPlace(PlayerBucketEmptyEvent event) {
        if (!InstanceProtector.isProtectedWorld(event.getBlock().getWorld())) {
            return;
        }
        if (this.shouldBypass(event.getPlayer())) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void preventLeafDecay(LeavesDecayEvent event) {
        if (!InstanceProtector.isProtectedWorld(event.getBlock().getWorld())) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void preventVanillaMobSpawning(CreatureSpawnEvent event) {
        if (event.getSpawnReason().equals((Object)CreatureSpawnEvent.SpawnReason.CUSTOM)) {
            return;
        }
        if (!InstanceProtector.isProtectedWorld(event.getLocation().getWorld())) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void preventFriendlyFireInDungeon(EntityDamageByEntityEvent event) {
        Projectile projectile;
        Entity entity;
        if (!InstanceProtector.isPvpPreventedWorld(event.getEntity().getLocation().getWorld())) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (event.getDamager() instanceof Player || (entity = event.getDamager()) instanceof Projectile && (projectile = (Projectile)entity).getShooter() instanceof Player) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void preventDoorOpening(PlayerInteractEvent event) {
        if (!InstanceProtector.isProtectedWorld(event.getPlayer().getWorld())) {
            return;
        }
        if (event.getClickedBlock() == null) {
            return;
        }
        if (this.shouldBypass(event.getPlayer())) {
            return;
        }
        Material material = event.getClickedBlock().getType();
        if (material.toString().toLowerCase(Locale.ROOT).endsWith("_sign")) {
            event.setCancelled(true);
        }
        if (material.toString().toLowerCase(Locale.ROOT).endsWith("_door") || material.toString().toLowerCase(Locale.ROOT).endsWith("_trapdoor")) {
            event.setCancelled(true);
        }
        if (event.getAction() == Action.PHYSICAL && InstanceProtector.isRedstonePreventedWorld(event.getPlayer().getWorld())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void preventEntityChangeBlockEvent(EntityChangeBlockEvent event) {
        if (!InstanceProtector.isProtectedWorld(event.getEntity().getWorld())) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void preventCobwebPotions(LingeringPotionSplashEvent event) {
        if (!InstanceProtector.isProtectedWorld(event.getEntity().getWorld())) {
            return;
        }
        if (event.getEntity().getShooter() == null) {
            return;
        }
        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }
        event.getEntity().getEffects().forEach(potionEffect -> {
            if (potionEffect.getType().equals(PotionEffectType.WEAVING)) {
                event.setCancelled(true);
            }
        });
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void preventCobwebPotions(PotionSplashEvent event) {
        if (!InstanceProtector.isProtectedWorld(event.getEntity().getWorld())) {
            return;
        }
        if (event.getEntity().getShooter() == null) {
            return;
        }
        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }
        event.getEntity().getEffects().forEach(potionEffect -> {
            if (potionEffect.getType().equals(PotionEffectType.WEAVING)) {
                event.setCancelled(true);
            }
        });
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void preventSignEdits(SignChangeEvent event) {
        if (!InstanceProtector.isProtectedWorld(event.getPlayer().getWorld())) {
            return;
        }
        event.setCancelled(true);
    }
}

