/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Damageable
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.advancedplugins.as.impl.effects.effects.actions.handlers;

import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.utils.MathUtils;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class DamageHandler {
    public void damage(EntityDamageEvent entityDamageEvent, double d, boolean bl) {
        if (!(entityDamageEvent.getEntity() instanceof Damageable)) {
            return;
        }
        Entity entity = entityDamageEvent instanceof EntityDamageByEntityEvent ? ((EntityDamageByEntityEvent)entityDamageEvent).getDamager() : null;
        this.damage((Damageable)entityDamageEvent.getEntity(), entity, d);
    }

    public boolean damageIgnoringArmor(Damageable damageable, LivingEntity livingEntity, LivingEntity livingEntity2, double d) {
        double d2;
        double d3 = damageable.getAbsorptionAmount();
        if (d3 > 0.0) {
            d2 = d3 - d;
            if (d2 >= 0.0) {
                damageable.setAbsorptionAmount(d2);
                return false;
            }
            d -= d3;
            damageable.setAbsorptionAmount(0.0);
        }
        if (damageable.isDead() || damageable.getHealth() <= 0.0) {
            return true;
        }
        d2 = livingEntity.getHealth() - d;
        if (d2 < 0.0) {
            damageable.setHealth(0.0);
            return true;
        }
        damageable.setHealth(d2);
        return false;
    }

    public void damage(Damageable damageable, double d) {
        this.damage(damageable, null, d);
    }

    public void damage(Damageable damageable, Entity entity, double d) {
        this.damage(damageable, entity, d, EntityDamageEvent.DamageCause.CUSTOM);
    }

    public void damage(Damageable damageable, Entity entity, double d, EntityDamageEvent.DamageCause damageCause) {
        if (damageable.isDead() || damageable.getHealth() <= 0.0) {
            return;
        }
        damageable.setMetadata("ae_ignore", (MetadataValue)new FixedMetadataValue((Plugin)EffectsHandler.getInstance(), (Object)true));
        d = Math.abs(d);
        damageable.damage(d, entity);
        damageable.removeMetadata("ae_ignore", (Plugin)EffectsHandler.getInstance());
    }

    public void heal(Entity entity, double d) {
        Damageable damageable = (Damageable)entity;
        if (damageable.isDead() || damageable.getHealth() <= 0.0) {
            return;
        }
        double d2 = damageable.getHealth();
        double d3 = Math.abs(d);
        double d4 = MathUtils.clamp(d2 + d3, 0.0, damageable.getMaxHealth());
        damageable.setHealth(d4);
    }

    public void revive(Entity entity, final Location location) {
        Damageable damageable = (Damageable)entity;
        if (!(entity instanceof Player)) {
            return;
        }
        if (damageable.isDead() || damageable.getHealth() <= 0.0) {
            final Player player = (Player)entity;
            player.spigot().respawn();
            new BukkitRunnable(){

                public void run() {
                    player.teleport(location);
                }
            }.runTaskLater((Plugin)EffectsHandler.getInstance(), 1L);
        }
    }
}

