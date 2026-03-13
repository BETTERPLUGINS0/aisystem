/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import java.util.Locale;
import net.advancedplugins.as.impl.effects.effects.actions.ActionExecution;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class DisableActivationEffect
extends AdvancedEffect {
    public DisableActivationEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "DISABLE_ACTIVATION", "Disable activation for seconds", "%e:<NAME>:<SECONDS>");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        String string = stringArray[0].toLowerCase(Locale.ROOT);
        int n = ASManager.parseInt(stringArray[1]);
        ActionExecution.addDisabledAbility(livingEntity.getUniqueId(), string, n);
        return true;
    }
}

