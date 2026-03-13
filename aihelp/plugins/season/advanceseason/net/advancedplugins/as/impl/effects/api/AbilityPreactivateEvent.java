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

import net.advancedplugins.as.impl.effects.effects.abilities.AdvancedAbility;
import net.advancedplugins.as.impl.effects.effects.actions.ActionExecution;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class AbilityPreactivateEvent
extends Event
implements Cancellable {
    private final AdvancedAbility effect;
    private final LivingEntity mainEntity;
    private final LivingEntity otherEntity;
    private final ActionExecution task;
    private boolean cancelled = false;
    private static final HandlerList handlers = new HandlerList();

    public AbilityPreactivateEvent(AdvancedAbility advancedAbility, LivingEntity livingEntity, LivingEntity livingEntity2, ActionExecution actionExecution) {
        this.effect = advancedAbility;
        this.mainEntity = livingEntity;
        this.otherEntity = livingEntity2;
        this.task = actionExecution;
    }

    public ActionExecution getActionExecution() {
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
}

