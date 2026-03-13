/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.GuardEffect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class StealGuardEffect
extends AdvancedEffect {
    private final Map<UUID, UUID> guards = new HashMap<UUID, UUID>();

    public StealGuardEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "STEAL_GUARD", "Steal a guard from player", "%e");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        GuardEffect.getGuardEffect().steal(this.getOtherEntity(livingEntity, executionTask).getUniqueId(), livingEntity.getUniqueId());
        return true;
    }

    public Map<UUID, UUID> getGuards() {
        return this.guards;
    }
}

