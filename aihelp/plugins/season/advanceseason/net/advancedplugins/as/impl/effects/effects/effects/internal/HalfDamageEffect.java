/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class HalfDamageEffect
extends AdvancedEffect {
    public HalfDamageEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "HALF_DAMAGE", "Half damage effect", "%e");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        EntityDamageEvent entityDamageEvent = (EntityDamageEvent)executionTask.getBuilder().getEvent();
        entityDamageEvent.setDamage(entityDamageEvent.getDamage() / 2.0);
        return true;
    }
}

