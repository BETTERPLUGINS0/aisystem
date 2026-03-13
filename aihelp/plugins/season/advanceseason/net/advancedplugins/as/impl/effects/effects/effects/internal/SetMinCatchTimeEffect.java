/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.player.PlayerFishEvent
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SetMinCatchTimeEffect
extends AdvancedEffect {
    public SetMinCatchTimeEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "SET_MIN_CATCH_TIME", "Set min. Time for fishing", "%e:<TICKS>");
        this.addArgument(0, Integer.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        PlayerFishEvent playerFishEvent = (PlayerFishEvent)executionTask.getBuilder().getEvent();
        int n = ASManager.parseInt(stringArray[0]);
        playerFishEvent.getHook().setMinWaitTime(n);
        return true;
    }
}

