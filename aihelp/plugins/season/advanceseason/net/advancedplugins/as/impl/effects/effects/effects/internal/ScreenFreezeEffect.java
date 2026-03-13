/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class ScreenFreezeEffect
extends AdvancedEffect {
    public ScreenFreezeEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "SCREEN_FREEZE", "Screen Freeze Entity", "%e:<TICKS>");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        int n = ASManager.parseInt(stringArray[0]);
        livingEntity.setFreezeTicks(n);
        return true;
    }
}

