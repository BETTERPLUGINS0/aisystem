/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Damageable
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.Event
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class KillEffect
extends AdvancedEffect {
    public KillEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "KILL", "Kill an entity", "%e");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        Event event = executionTask.getBuilder().getEvent();
        if (executionTask.getBuilder().isDamageEventNotGoingToRun()) {
            livingEntity.setMetadata("ae_damage_event_not_going_to_run", (MetadataValue)new FixedMetadataValue((Plugin)EffectsHandler.getInstance(), (Object)true));
        }
        if (event instanceof EntityDamageEvent) {
            executionTask.getDamageHandler().damage((EntityDamageEvent)event, 2.147483647E9, true);
        } else {
            executionTask.getDamageHandler().damage((Damageable)livingEntity, 2.147483647E9);
        }
        if (executionTask.getBuilder().isDamageEventNotGoingToRun()) {
            livingEntity.removeMetadata("ae_damage_event_not_going_to_run", (Plugin)EffectsHandler.getInstance());
        }
        return true;
    }
}

