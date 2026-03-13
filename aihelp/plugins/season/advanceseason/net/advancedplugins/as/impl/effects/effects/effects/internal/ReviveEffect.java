/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ReviveEffect
extends AdvancedEffect {
    public ReviveEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "REVIVE", "Revive an entity", "%e");
    }

    @Override
    public boolean executeEffect(final ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        final EntityDamageEvent entityDamageEvent = (EntityDamageEvent)executionTask.getBuilder().getEvent();
        final Location location = entityDamageEvent.getEntity().getLocation().clone();
        new BukkitRunnable(){

            public void run() {
                executionTask.getDamageHandler().revive(entityDamageEvent.getEntity(), location);
            }
        }.runTask((Plugin)EffectsHandler.getInstance());
        return true;
    }
}

