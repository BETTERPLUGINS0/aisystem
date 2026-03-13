/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Projectile
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.projectiles.ProjectileSource
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;

public class ProjectileEffect
extends AdvancedEffect {
    public ProjectileEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "PROJECTILE", "Fire a projectile", "%e:<TYPE>");
        this.addArgument(0, EntityType.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        EntityType entityType = EntityType.fromName((String)stringArray[0]);
        if (entityType == null) {
            return false;
        }
        Projectile projectile = (Projectile)livingEntity.getWorld().spawnEntity(livingEntity.getEyeLocation(), entityType);
        projectile.setShooter((ProjectileSource)livingEntity);
        projectile.setVelocity(livingEntity.getEyeLocation().getDirection().multiply(1.5));
        return true;
    }
}

