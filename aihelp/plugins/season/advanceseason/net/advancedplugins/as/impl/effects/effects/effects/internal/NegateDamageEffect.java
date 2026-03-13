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
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class NegateDamageEffect
extends AdvancedEffect {
    public NegateDamageEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "NEGATE_DAMAGE", "Negate a specified amount of damage", "%e:<AMOUNT>");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        EntityDamageEvent entityDamageEvent = (EntityDamageEvent)executionTask.getBuilder().getEvent();
        double d = entityDamageEvent.getDamage();
        int n = ASManager.parseInt(stringArray[0]);
        double d2 = Math.max(d - (double)n, 0.0);
        entityDamageEvent.setDamage(d2);
        return true;
    }
}

