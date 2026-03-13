/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 *  org.jetbrains.annotations.NotNull
 */
package net.advancedplugins.as.impl.effects.api;

import java.util.Collections;
import java.util.List;
import net.advancedplugins.as.impl.effects.effects.abilities.AdvancedAbility;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class EffectsActivateEvent
extends Event
implements Cancellable {
    private final AdvancedAbility effect;
    private final LivingEntity mainEntity;
    private final LivingEntity otherEntity;
    private final ExecutionTask task;
    private List<LivingEntity> otherTargets = Collections.emptyList();
    private boolean cancelled = false;
    private static final HandlerList handlers = new HandlerList();

    public EffectsActivateEvent(AdvancedAbility advancedAbility, LivingEntity livingEntity, LivingEntity livingEntity2, ExecutionTask executionTask) {
        this.effect = advancedAbility;
        this.mainEntity = livingEntity;
        this.otherEntity = livingEntity2;
        this.task = executionTask;
    }

    public EffectsActivateEvent setOtherTargets(List<LivingEntity> list) {
        this.otherTargets = list;
        return this;
    }

    public ExecutionTask getExecutionTask() {
        return this.task;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean bl) {
        this.cancelled = bl;
    }

    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public AdvancedAbility getEffect() {
        return this.effect;
    }

    public LivingEntity getMainEntity() {
        return this.mainEntity;
    }

    public LivingEntity getOtherEntity() {
        return this.otherEntity;
    }

    public List<LivingEntity> getOtherTargets() {
        return this.otherTargets;
    }
}

