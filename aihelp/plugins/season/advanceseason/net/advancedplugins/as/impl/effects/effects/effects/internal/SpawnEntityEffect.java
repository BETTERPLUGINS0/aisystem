/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.EntitySpawnUtils;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnEntityEffect
extends AdvancedEffect {
    public SpawnEntityEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "SPAWN_ENTITY", "Spawn an entity", "%e:<ENTITY>");
        this.addArgument(0, EntityType.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        EntityType entityType = EntityType.fromName((String)stringArray[0]);
        if (entityType == null) {
            return false;
        }
        EntitySpawnUtils.spawnEntity((Plugin)this.getPlugin(), livingEntity.getWorld(), livingEntity.getLocation(), entityType);
        return true;
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, Location location, String[] stringArray) {
        EntityType entityType = EntityType.fromName((String)stringArray[0]);
        if (entityType == null) {
            return false;
        }
        EntitySpawnUtils.spawnEntity((Plugin)this.getPlugin(), location.getWorld(), location, entityType);
        return true;
    }
}

