/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.EntityCreature
 *  net.minecraft.world.entity.EntityInsentient
 *  net.minecraft.world.entity.ai.goal.PathfinderGoal
 *  net.minecraft.world.entity.ai.goal.PathfinderGoal$Type
 *  net.minecraft.world.entity.player.EntityHuman
 *  net.minecraft.world.level.pathfinder.PathEntity
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.attribute.Attribute
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.Event
 */
package com.magmaguy.betterstructures.easyminecraftgoals.v1_21_R7_spigot.wanderbacktopoint;

import com.magmaguy.betterstructures.easyminecraftgoals.events.WanderBackToPointEndEvent;
import com.magmaguy.betterstructures.easyminecraftgoals.events.WanderBackToPointStartEvent;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.AbstractWanderBackToPoint;
import com.magmaguy.betterstructures.easyminecraftgoals.utils.Utils;
import java.util.EnumSet;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.level.pathfinder.PathEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;

public class WanderBackToPointGoal
extends PathfinderGoal
implements AbstractWanderBackToPoint {
    private final double maximumDistanceFromPoint;
    private final LivingEntity livingEntity;
    private final EntityCreature pathfinderMob;
    private final EntityInsentient mob;
    Location returnLocation;
    private int priority;
    private PathEntity path = null;
    private long lastTime;
    private float speed;
    private int stopReturnDistance = 0;
    private int goalRefreshCooldownTicks = 60;
    private int maxDurationTicks = 100;
    private boolean hardObjective = false;
    private boolean teleportOnFail = false;
    private boolean startWithCooldown = false;

    public WanderBackToPointGoal(EntityInsentient mob, LivingEntity livingEntity, EntityCreature pathfinderMob, Location location, double maximumDistanceFromPoint, int priority, int maxDurationTicks) {
        this.mob = mob;
        this.livingEntity = livingEntity;
        this.pathfinderMob = pathfinderMob;
        this.returnLocation = location;
        this.priority = priority;
        this.maxDurationTicks = maxDurationTicks;
        this.speed = (float)livingEntity.getAttribute(Attribute.MOVEMENT_SPEED).getValue();
        this.maximumDistanceFromPoint = maximumDistanceFromPoint;
        this.lastTime = 0L;
        this.a(EnumSet.of(PathfinderGoal.Type.a, PathfinderGoal.Type.c));
    }

    public void d() {
        this.pathfinderMob.N().a(this.path, (double)this.speed);
    }

    public void a() {
    }

    public void e() {
        if (this.pathfinderMob != null) {
            this.pathfinderMob.N().n();
            this.path = null;
        }
        if (this.hardObjective && (this.pathfinderMob == null || this.pathfinderMob.gL())) {
            this.livingEntity.teleport(this.returnLocation);
        }
        WanderBackToPointEndEvent wanderBackToPointEndEvent = new WanderBackToPointEndEvent(this.hardObjective, this.livingEntity, this);
        Bukkit.getPluginManager().callEvent((Event)wanderBackToPointEndEvent);
        this.updateCooldown();
    }

    public boolean b() {
        if (!this.hardObjective && this.mob.ag_() instanceof EntityHuman) {
            this.updateCooldown();
            return false;
        }
        if (this.lastTime + 50L * (long)this.goalRefreshCooldownTicks - System.currentTimeMillis() > 0L) {
            return false;
        }
        this.updateCooldown();
        if (Utils.distanceShorterThan(this.returnLocation.toVector(), this.livingEntity.getLocation().toVector(), this.maximumDistanceFromPoint)) {
            return false;
        }
        if (this.pathfinderMob != null) {
            this.path = this.pathfinderMob.N().a(this.returnLocation.getX(), this.returnLocation.getY(), this.returnLocation.getZ(), this.stopReturnDistance);
            if (this.path == null) {
                return false;
            }
        }
        WanderBackToPointStartEvent wanderBackToPointStartEvent = new WanderBackToPointStartEvent(this.hardObjective, this.livingEntity, this);
        Bukkit.getPluginManager().callEvent((Event)wanderBackToPointStartEvent);
        if (wanderBackToPointStartEvent.isCancelled()) {
            return false;
        }
        if (this.teleportOnFail && (this.pathfinderMob == null || !this.path.j())) {
            this.earlyPathfindingTermination();
            return false;
        }
        return true;
    }

    private void earlyPathfindingTermination() {
        this.livingEntity.teleport(this.returnLocation);
        WanderBackToPointEndEvent wanderBackToPointEndEvent = new WanderBackToPointEndEvent(this.hardObjective, this.livingEntity, this);
        Bukkit.getPluginManager().callEvent((Event)wanderBackToPointEndEvent);
    }

    public boolean c() {
        if (this.lastTime + 50L * (long)this.maxDurationTicks - System.currentTimeMillis() < 0L) {
            return false;
        }
        if (!this.hardObjective && this.mob.ag_() instanceof EntityHuman) {
            return false;
        }
        return !this.pathfinderMob.N().l();
    }

    public boolean W_() {
        return !this.hardObjective;
    }

    @Override
    public double getMaximumDistanceFromPoint() {
        return this.maximumDistanceFromPoint;
    }

    @Override
    public long getLastTime() {
        return this.lastTime;
    }

    @Override
    public LivingEntity getLivingEntity() {
        return this.livingEntity;
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    @Override
    public int getMaxDurationTicks() {
        return this.maxDurationTicks;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public AbstractWanderBackToPoint setSpeed(float speed) {
        this.speed = speed;
        return this;
    }

    @Override
    public Location getReturnLocation() {
        return this.returnLocation;
    }

    @Override
    public int getStopReturnDistance() {
        return this.stopReturnDistance;
    }

    @Override
    public AbstractWanderBackToPoint setStopReturnDistance(int distance) {
        this.stopReturnDistance = distance;
        return this;
    }

    @Override
    public int getGoalRefreshCooldownTicks() {
        return this.goalRefreshCooldownTicks;
    }

    @Override
    public AbstractWanderBackToPoint setGoalRefreshCooldownTicks(int ticks) {
        this.goalRefreshCooldownTicks = ticks;
        return this;
    }

    @Override
    public boolean isHardObjective() {
        return this.hardObjective;
    }

    @Override
    public AbstractWanderBackToPoint setHardObjective(boolean hardObjective) {
        this.priority = -1;
        this.hardObjective = hardObjective;
        return this;
    }

    @Override
    public boolean isTeleportOnFail() {
        return this.teleportOnFail;
    }

    @Override
    public AbstractWanderBackToPoint setTeleportOnFail(boolean teleportOnFail) {
        this.teleportOnFail = teleportOnFail;
        return this;
    }

    @Override
    public boolean isStartWithCooldown() {
        return this.startWithCooldown;
    }

    @Override
    public AbstractWanderBackToPoint setStartWithCooldown(boolean startWithCooldown) {
        this.startWithCooldown = startWithCooldown;
        return this;
    }

    @Override
    public void updateCooldown() {
        this.lastTime = System.currentTimeMillis();
    }

    @Override
    public void register() {
        if (this.startWithCooldown) {
            this.updateCooldown();
        }
        this.mob.cs.a(this.priority, (PathfinderGoal)this);
    }

    @Override
    public void unregister() {
        this.mob.cs.a((PathfinderGoal)this);
    }
}

