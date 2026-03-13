/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.Cancellable
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.plugin.java.JavaPlugin;

public class CancelEventEffect
extends AdvancedEffect {
    public CancelEventEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "CANCEL_EVENT", "Cancel Event, e.g. an attack", "%e");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        if (!(executionTask.getBuilder().getEvent() instanceof Cancellable)) {
            return false;
        }
        ((Cancellable)executionTask.getBuilder().getEvent()).setCancelled(true);
        return true;
    }
}

