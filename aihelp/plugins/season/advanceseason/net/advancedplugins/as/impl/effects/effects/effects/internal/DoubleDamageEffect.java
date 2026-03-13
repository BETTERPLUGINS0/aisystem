/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
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
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class DoubleDamageEffect
extends AdvancedEffect {
    public DoubleDamageEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "DOUBLE_DAMAGE", "Double damage", "%e");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        EntityDamageEvent entityDamageEvent = (EntityDamageEvent)executionTask.getBuilder().getEvent();
        if (executionTask.getBuilder().isDamageEventNotGoingToRun()) {
            livingEntity.setMetadata("ae_damage_event_not_going_to_run", (MetadataValue)new FixedMetadataValue((Plugin)EffectsHandler.getInstance(), (Object)true));
        }
        executionTask.getDamageHandler().damage(entityDamageEvent, entityDamageEvent.getFinalDamage(), false);
        if (executionTask.getBuilder().isDamageEventNotGoingToRun()) {
            livingEntity.removeMetadata("ae_damage_event_not_going_to_run", (Plugin)EffectsHandler.getInstance());
        }
        return true;
    }
}

