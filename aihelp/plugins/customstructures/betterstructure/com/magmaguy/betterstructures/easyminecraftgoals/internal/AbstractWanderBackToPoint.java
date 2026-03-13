/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.LivingEntity
 */
package com.magmaguy.betterstructures.easyminecraftgoals.internal;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public interface AbstractWanderBackToPoint {
    public double getMaximumDistanceFromPoint();

    public long getLastTime();

    public LivingEntity getLivingEntity();

    public int getPriority();

    public int getMaxDurationTicks();

    public float getSpeed();

    public AbstractWanderBackToPoint setSpeed(float var1);

    public Location getReturnLocation();

    public int getStopReturnDistance();

    public AbstractWanderBackToPoint setStopReturnDistance(int var1);

    public int getGoalRefreshCooldownTicks();

    public AbstractWanderBackToPoint setGoalRefreshCooldownTicks(int var1);

    public boolean isHardObjective();

    public AbstractWanderBackToPoint setHardObjective(boolean var1);

    public boolean isTeleportOnFail();

    public AbstractWanderBackToPoint setTeleportOnFail(boolean var1);

    public boolean isStartWithCooldown();

    public AbstractWanderBackToPoint setStartWithCooldown(boolean var1);

    public void updateCooldown();

    public void register();

    public void unregister();
}

