/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.WorldServer
 *  net.minecraft.world.entity.EntityCreature
 *  net.minecraft.world.entity.EntityInsentient
 *  net.minecraft.world.entity.EntityLiving
 *  net.minecraft.world.entity.ai.behavior.Behavior
 *  net.minecraft.world.entity.ai.memory.MemoryModuleType
 *  net.minecraft.world.entity.ai.memory.MemoryStatus
 *  net.minecraft.world.entity.player.EntityHuman
 *  net.minecraft.world.entity.schedule.Activity
 *  net.minecraft.world.level.pathfinder.PathEntity
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.Event
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.magmaguy.betterstructures.easyminecraftgoals.v1_21_R7_paper.wanderbacktopoint;

import com.google.common.collect.ImmutableList;
import com.magmaguy.betterstructures.easyminecraftgoals.NMSManager;
import com.magmaguy.betterstructures.easyminecraftgoals.events.WanderBackToPointEndEvent;
import com.magmaguy.betterstructures.easyminecraftgoals.events.WanderBackToPointStartEvent;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.AbstractWanderBackToPoint;
import com.magmaguy.betterstructures.easyminecraftgoals.utils.Utils;
import java.util.Map;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.pathfinder.PathEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;

public class WanderBackToPointBehavior
extends Behavior<EntityLiving>
implements AbstractWanderBackToPoint {
    private final Location returnLocation;
    private final double maximumDistanceFromPoint;
    private final LivingEntity livingEntity;
    private final EntityInsentient mob;
    private final int maxDurationTicks;
    private long lastTime;
    private int priority;
    private float speed;
    private int stopReturnDistance = 0;
    private int goalRefreshCooldownTicks = 60;
    private boolean hardObjective = false;
    private boolean teleportOnFail = false;
    private boolean startWithCooldown = false;
    private PathEntity path = null;

    public WanderBackToPointBehavior(LivingEntity livingEntity, EntityInsentient mob, Location location, double maximumDistanceFromPoint, int priority, int maxDurationTicks) {
        super(Map.of((Object)MemoryModuleType.n, (Object)MemoryStatus.c), 0, maxDurationTicks);
        this.livingEntity = livingEntity;
        this.mob = mob;
        this.returnLocation = location;
        this.maximumDistanceFromPoint = maximumDistanceFromPoint;
        this.maxDurationTicks = maxDurationTicks;
        this.priority = priority;
        this.lastTime = 0L;
    }

    protected boolean a(WorldServer var0, EntityLiving nmsLivingEntity) {
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
        WanderBackToPointStartEvent wanderBackToPointStartEvent = new WanderBackToPointStartEvent(this.hardObjective, this.livingEntity, this);
        Bukkit.getPluginManager().callEvent((Event)wanderBackToPointStartEvent);
        if (wanderBackToPointStartEvent.isCancelled()) {
            return false;
        }
        this.path = ((EntityCreature)nmsLivingEntity).N().a(this.returnLocation.getX(), this.returnLocation.getY(), this.returnLocation.getZ(), this.stopReturnDistance);
        if (this.teleportOnFail && (this.path == null || !this.path.j())) {
            this.path = null;
            this.livingEntity.teleport(this.returnLocation);
            WanderBackToPointEndEvent wanderBackToPointEndEvent = new WanderBackToPointEndEvent(this.hardObjective, this.livingEntity, this);
            Bukkit.getPluginManager().callEvent((Event)wanderBackToPointEndEvent);
            return false;
        }
        return true;
    }

    protected void d(WorldServer var0, EntityLiving var1, long var2) {
        this.mob.N().n();
        this.mob.N().a(this.path, (double)this.speed);
        this.mob.ev().a(Activity.a);
        if (this.hardObjective) {
            new BukkitRunnable(){

                public void run() {
                    if (!WanderBackToPointBehavior.this.livingEntity.isValid() || WanderBackToPointBehavior.this.mob.N().l() || WanderBackToPointBehavior.this.path == null || !WanderBackToPointBehavior.this.path.j()) {
                        this.cancel();
                        if (WanderBackToPointBehavior.this.livingEntity.isValid() && (WanderBackToPointBehavior.this.path == null || !WanderBackToPointBehavior.this.path.j()) && WanderBackToPointBehavior.this.teleportOnFail) {
                            WanderBackToPointBehavior.this.livingEntity.teleport(WanderBackToPointBehavior.this.returnLocation);
                        }
                        return;
                    }
                    WanderBackToPointBehavior.this.mob.N().a(WanderBackToPointBehavior.this.path, (double)WanderBackToPointBehavior.this.speed);
                }
            }.runTaskTimer(NMSManager.pluginProvider, 0L, 1L);
        }
    }

    protected void b(WorldServer var0, EntityLiving var1, long var2) {
        this.path = null;
        if (this.teleportOnFail && this.a(this.maxDurationTicks)) {
            this.livingEntity.teleport(this.returnLocation);
        }
        WanderBackToPointEndEvent wanderBackToPointEndEvent = new WanderBackToPointEndEvent(this.hardObjective, this.livingEntity, this);
        Bukkit.getPluginManager().callEvent((Event)wanderBackToPointEndEvent);
        this.updateCooldown();
        this.mob.w(true);
    }

    protected boolean a(WorldServer var0, EntityLiving var1, long var2) {
        this.mob.g(null);
        if (this.path == null) {
            return false;
        }
        if (!this.hardObjective && this.mob.ag_() instanceof EntityHuman) {
            return false;
        }
        return !this.path.c();
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
        this.mob.ev().a(Activity.a, this.priority, ImmutableList.of(this));
    }

    @Override
    public void unregister() {
    }
}

