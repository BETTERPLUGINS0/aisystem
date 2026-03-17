/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Damageable
 *  org.bukkit.entity.Entity
 */
package me.zombie_striker.qav.qamini;

import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;

public class ExplosionHandler {
    public static void handleExplosion(Location location, int n, int n2) {
        location.getWorld().createExplosion(location, (float)n2);
    }

    public static void handleAOEExplosion(Entity entity, Location location, double d, double d2) {
        for (Entity entity2 : location.getWorld().getNearbyEntities(location, d2, d2, d2)) {
            if (!(entity2 instanceof Damageable)) continue;
            Damageable damageable = (Damageable)entity2;
            damageable.damage(d * d2 / entity2.getLocation().distanceSquared(location), entity);
        }
    }
}

