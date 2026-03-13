/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Damageable
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.actions.handlers.DamageHandler;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class RemoveHealthDamageEffect
extends AdvancedEffect {
    public RemoveHealthDamageEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "REMOVE_HEALTH_DAMAGE", "Remove health from an entity with damage effect", "%e:<HEALTH>");
        this.addArgument(0, Integer.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        DamageHandler damageHandler = executionTask.getDamageHandler();
        damageHandler.damage((Damageable)livingEntity, (Entity)this.getOtherEntity(livingEntity, executionTask), 0.01);
        damageHandler.damageIgnoringArmor((Damageable)livingEntity, livingEntity, this.getOtherEntity(livingEntity, executionTask), ASManager.parseInt(stringArray[0]));
        return true;
    }
}

