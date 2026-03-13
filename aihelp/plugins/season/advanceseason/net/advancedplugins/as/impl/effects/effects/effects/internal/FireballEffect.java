/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Fireball
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.util.Vector
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class FireballEffect
extends AdvancedEffect {
    public FireballEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "FIREBALL", "Shoot a fireball", "%e");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        return this.executeEffect(executionTask, livingEntity.getLocation(), stringArray);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, Location location, String[] stringArray) {
        Fireball fireball = (Fireball)location.getWorld().spawnEntity(location, EntityType.FIREBALL);
        fireball.setIsIncendiary(false);
        fireball.setYield(0.0f);
        Vector vector = new Vector(location.getX(), location.getY(), location.getZ());
        Vector vector2 = new Vector(fireball.getLocation().getX(), fireball.getLocation().getY(), fireball.getLocation().getZ());
        if (ASManager.isExcessVelocity(vector.subtract(vector2))) {
            return false;
        }
        fireball.setVelocity(vector.subtract(vector2));
        fireball.teleport(fireball.getLocation());
        try {
            fireball.setVelocity(vector.subtract(vector2));
        } catch (Exception exception) {
            // empty catch block
        }
        fireball.setYield(0.0f);
        return true;
    }
}

