/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Fireball
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.ExplosionPrimeEvent
 *  org.bukkit.event.entity.ProjectileHitEvent
 *  org.bukkit.projectiles.ProjectileSource
 *  org.bukkit.util.Vector
 */
package com.andrei1058.bedwars.listeners;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.LastHit;
import java.util.Collection;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class FireballListener
implements Listener {
    private final double fireballExplosionSize = BedWars.config.getYml().getDouble("fireball.explosion-size");
    private final boolean fireballMakeFire = BedWars.config.getYml().getBoolean("fireball.make-fire");
    private final double fireballHorizontal = BedWars.config.getYml().getDouble("fireball.knockback.horizontal") * -1.0;
    private final double fireballVertical = BedWars.config.getYml().getDouble("fireball.knockback.vertical");
    private final double damageSelf = BedWars.config.getYml().getDouble("fireball.damage.self");
    private final double damageEnemy = BedWars.config.getYml().getDouble("fireball.damage.enemy");
    private final double damageTeammates = BedWars.config.getYml().getDouble("fireball.damage.teammates");

    @EventHandler
    public void fireballHit(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof Fireball)) {
            return;
        }
        Location location = e.getEntity().getLocation();
        ProjectileSource projectileSource = e.getEntity().getShooter();
        if (!(projectileSource instanceof Player)) {
            return;
        }
        Player source = (Player)projectileSource;
        IArena arena = Arena.getArenaByPlayer(source);
        Vector vector = location.toVector();
        World world = location.getWorld();
        assert (world != null);
        Collection nearbyEntities = world.getNearbyEntities(location, this.fireballExplosionSize, this.fireballExplosionSize, this.fireballExplosionSize);
        for (Entity entity : nearbyEntities) {
            if (!(entity instanceof Player)) continue;
            Player player = (Player)entity;
            if (!BedWars.getAPI().getArenaUtil().isPlaying(player)) continue;
            Vector playerVector = player.getLocation().toVector();
            Vector normalizedVector = vector.subtract(playerVector).normalize();
            Vector horizontalVector = normalizedVector.multiply(this.fireballHorizontal);
            double y = normalizedVector.getY();
            if (y < 0.0) {
                y += 1.5;
            }
            y = y <= 0.5 ? this.fireballVertical * 1.5 : y * this.fireballVertical * 1.5;
            player.setVelocity(horizontalVector.setY(y));
            LastHit lh = LastHit.getLastHit(player);
            if (lh != null) {
                lh.setDamager((Entity)source);
                lh.setTime(System.currentTimeMillis());
            } else {
                new LastHit(player, (Entity)source, System.currentTimeMillis());
            }
            if (player.equals((Object)source)) {
                if (!(this.damageSelf > 0.0)) continue;
                player.damage(this.damageSelf);
                continue;
            }
            if (arena.getTeam(player).equals(arena.getTeam(source))) {
                if (!(this.damageTeammates > 0.0)) continue;
                player.damage(this.damageTeammates);
                continue;
            }
            if (!(this.damageEnemy > 0.0)) continue;
            player.damage(this.damageEnemy);
        }
    }

    @EventHandler
    public void fireballDirectHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Fireball)) {
            return;
        }
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        if (Arena.getArenaByPlayer((Player)e.getEntity()) == null) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void fireballPrime(ExplosionPrimeEvent e) {
        if (!(e.getEntity() instanceof Fireball)) {
            return;
        }
        ProjectileSource shooter = ((Fireball)e.getEntity()).getShooter();
        if (!(shooter instanceof Player)) {
            return;
        }
        Player player = (Player)shooter;
        if (Arena.getArenaByPlayer(player) == null) {
            return;
        }
        e.setFire(this.fireballMakeFire);
    }
}

