/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.EntityEffect
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import java.util.Locale;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import org.bukkit.EntityEffect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayEntityEffect
extends AdvancedEffect {
    public PlayEntityEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "ENTITY_EFFECT", "Play Entity Effect", "%e:<ENTITY_EFFECT>");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        EntityEffect entityEffect;
        try {
            entityEffect = EntityEffect.valueOf((String)stringArray[0].toUpperCase(Locale.ROOT));
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        livingEntity.playEffect(entityEffect);
        return true;
    }
}

