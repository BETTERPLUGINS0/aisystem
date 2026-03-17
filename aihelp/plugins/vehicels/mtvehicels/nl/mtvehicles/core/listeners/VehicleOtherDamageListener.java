/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.block.BlockExplodeEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.entity.EntityExplodeEvent
 */
package nl.mtvehicles.core.listeners;

import java.util.Collection;
import java.util.List;
import nl.mtvehicles.core.events.VehicleDamageEvent;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import nl.mtvehicles.core.listeners.VehicleEntityListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class VehicleOtherDamageListener
extends MTVListener {
    public VehicleOtherDamageListener() {
        super(new VehicleDamageEvent());
    }

    @EventHandler
    public void onVehicleDamage(EntityDamageEvent event) {
        this.event = event;
        Entity victim = event.getEntity();
        if (!VehicleUtils.isVehicle(victim)) {
            return;
        }
        String license = VehicleUtils.getLicensePlate(victim);
        if (license == null) {
            return;
        }
        EntityDamageEvent.DamageCause damageCause = event.getCause();
        if (damageCause != EntityDamageEvent.DamageCause.FIRE && damageCause != EntityDamageEvent.DamageCause.LAVA && damageCause != EntityDamageEvent.DamageCause.PROJECTILE) {
            return;
        }
        VehicleDamageEvent api = (VehicleDamageEvent)this.getAPI();
        api.setDamage(event.getDamage());
        api.setDamageCause(damageCause);
        api.setLicensePlate(license);
        this.callAPI();
        if (this.isCancelled()) {
            return;
        }
        VehicleEntityListener.damage(api.getLicensePlate(), api.getDamage());
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        this.event = event;
        Collection nearbyEntities = event.getBlock().getLocation().getWorld().getNearbyEntities(event.getBlock().getLocation(), 5.0, 5.0, 5.0);
        this.damageNearbyVehicles(nearbyEntities, 40.0, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        this.event = event;
        List nearbyEntities = event.getEntity().getNearbyEntities(5.0, 5.0, 5.0);
        if (event.getEntity().getType().equals((Object)EntityType.WITHER)) {
            this.damageNearbyVehicles(nearbyEntities, 80.0, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION);
        } else {
            this.damageNearbyVehicles(nearbyEntities, 40.0, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION);
        }
    }

    private void damageNearbyVehicles(Collection<Entity> entities, double damage, EntityDamageEvent.DamageCause cause) {
        for (Entity entity : entities) {
            String license;
            if (!VehicleUtils.isVehicle(entity) || (license = VehicleUtils.getLicensePlate(entity)) == null) continue;
            VehicleDamageEvent api = (VehicleDamageEvent)this.getAPI();
            api.setDamage(damage);
            api.setDamageCause(cause);
            api.setLicensePlate(license);
            this.callAPI();
            if (this.isCancelled()) continue;
            VehicleEntityListener.damage(api.getLicensePlate(), api.getDamage());
        }
    }
}

