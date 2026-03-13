/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.magmaguy.betterstructures.easyminecraftgoals.events;

import com.magmaguy.betterstructures.easyminecraftgoals.internal.AbstractWanderBackToPoint;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WanderBackToPointEndEvent
extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final boolean hardObjective;
    private final LivingEntity livingEntity;
    private AbstractWanderBackToPoint abstractWanderBackToPoint;

    public WanderBackToPointEndEvent(boolean hard, LivingEntity livingEntity, AbstractWanderBackToPoint abstractWanderBackToPoint) {
        this.hardObjective = hard;
        this.livingEntity = livingEntity;
        this.abstractWanderBackToPoint = abstractWanderBackToPoint;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public boolean isHardObjective() {
        return this.hardObjective;
    }

    public LivingEntity getLivingEntity() {
        return this.livingEntity;
    }

    public AbstractWanderBackToPoint getAbstractWanderBackToPoint() {
        return this.abstractWanderBackToPoint;
    }
}

