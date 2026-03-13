/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityCreature
 *  net.minecraft.world.entity.EntityInsentient
 *  net.minecraft.world.entity.EntityLiving
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.block.data.BlockData
 *  org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity
 *  org.bukkit.craftbukkit.v1_20_R1.entity.CraftLivingEntity
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 */
package com.magmaguy.betterstructures.easyminecraftgoals.v1_20_R1;

import com.magmaguy.betterstructures.easyminecraftgoals.constants.OverridableWanderPriority;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.AbstractPacketBundle;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.AbstractWanderBackToPoint;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.FakeText;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.FakeTextSettings;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.PacketEntityInterface;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.PacketTextEntity;
import com.magmaguy.betterstructures.easyminecraftgoals.v1_20_R1.Move.Move;
import com.magmaguy.betterstructures.easyminecraftgoals.v1_20_R1.entitydata.BodyRotation;
import com.magmaguy.betterstructures.easyminecraftgoals.v1_20_R1.hitbox.Hitbox;
import com.magmaguy.betterstructures.easyminecraftgoals.v1_20_R1.massblockedit.MassEditBlocks;
import com.magmaguy.betterstructures.easyminecraftgoals.v1_20_R1.packets.FakeTextImpl;
import com.magmaguy.betterstructures.easyminecraftgoals.v1_20_R1.packets.PacketArmorStandEntity;
import com.magmaguy.betterstructures.easyminecraftgoals.v1_20_R1.packets.PacketBundle;
import com.magmaguy.betterstructures.easyminecraftgoals.v1_20_R1.packets.PacketDisplayEntity;
import com.magmaguy.betterstructures.easyminecraftgoals.v1_20_R1.packets.PacketGenericEntity;
import com.magmaguy.betterstructures.easyminecraftgoals.v1_20_R1.wanderbacktopoint.WanderBackToPointBehavior;
import com.magmaguy.betterstructures.easyminecraftgoals.v1_20_R1.wanderbacktopoint.WanderBackToPointGoal;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class NMSAdapter
extends com.magmaguy.betterstructures.easyminecraftgoals.NMSAdapter {
    private EntityCreature getPathfinderMob(Entity entity) {
        net.minecraft.world.entity.Entity entity2 = ((CraftEntity)entity).getHandle();
        if (entity2 instanceof EntityCreature) {
            EntityCreature pathfinderMob1 = (EntityCreature)entity2;
            return pathfinderMob1;
        }
        return null;
    }

    @Override
    public PacketArmorStandEntity createPacketArmorStandEntity(Location location) {
        return new PacketArmorStandEntity(location);
    }

    @Override
    public PacketDisplayEntity createPacketDisplayEntity(Location location) {
        return new PacketDisplayEntity(location);
    }

    @Override
    public PacketTextEntity createPacketTextArmorStandEntity(Location location) {
        return new PacketArmorStandEntity(location);
    }

    @Override
    public boolean canReach(LivingEntity livingEntity, Location destination) {
        EntityCreature pathfinderMob = this.getPathfinderMob((Entity)livingEntity);
        if (pathfinderMob == null) {
            return false;
        }
        return Move.canReach(pathfinderMob, destination);
    }

    @Override
    public boolean setCustomHitbox(Entity entity, float width, float height, boolean fixed) {
        if (entity == null) {
            return false;
        }
        return Hitbox.setCustomHitbox(((CraftEntity)entity).getHandle(), width, height, fixed);
    }

    @Override
    public float getBodyRotation(Entity entity) {
        return BodyRotation.getBodyRotation(((CraftEntity)entity).getHandle());
    }

    @Override
    public boolean move(LivingEntity livingEntity, double speedModifier, Location location) {
        EntityCreature pathfinderMob1;
        EntityLiving entityLiving = ((CraftLivingEntity)livingEntity).getHandle();
        EntityCreature pathfinderMob = entityLiving instanceof EntityCreature ? (pathfinderMob1 = (EntityCreature)entityLiving) : null;
        entityLiving = ((CraftLivingEntity)livingEntity).getHandle();
        if (!(entityLiving instanceof EntityInsentient)) {
            Bukkit.getLogger().info("[EasyMinecraftPathfinding] Entity type " + String.valueOf(livingEntity.getType()) + " does not extend Mob and is therefore unable to have goals! It will not be able to pathfind.");
            return false;
        }
        EntityInsentient mob = (EntityInsentient)entityLiving;
        return Move.simpleMove(pathfinderMob, speedModifier, location);
    }

    @Override
    public void doNotMove(LivingEntity livingEntity) {
        EntityCreature pathfinderMob = this.getPathfinderMob((Entity)livingEntity);
        if (pathfinderMob == null) {
            return;
        }
        Move.doNotMove(pathfinderMob);
    }

    @Override
    public boolean forcedMove(LivingEntity livingEntity, double speedModifier, Location location) {
        EntityLiving entityLiving = ((CraftLivingEntity)livingEntity).getHandle();
        if (!(entityLiving instanceof EntityInsentient)) {
            Bukkit.getLogger().info("[EasyMinecraftPathfinding] Entity type " + String.valueOf(livingEntity.getType()) + " does not extend Mob and is therefore unable to have goals! It will not be able to pathfind.");
            return false;
        }
        EntityInsentient mob = (EntityInsentient)entityLiving;
        return Move.forcedMove(mob, speedModifier, location);
    }

    @Override
    public void universalMove(LivingEntity livingEntity, double speedModifier, Location location) {
        EntityLiving entityLiving = ((CraftLivingEntity)livingEntity).getHandle();
        if (!(entityLiving instanceof EntityInsentient)) {
            Bukkit.getLogger().info("[EasyMinecraftPathfinding] Entity type " + String.valueOf(livingEntity.getType()) + " does not extend Mob and is therefore unable to have goals! It will not be able to pathfind.");
            return;
        }
        EntityInsentient mob = (EntityInsentient)entityLiving;
        Move.universalMove(mob, speedModifier, location);
    }

    @Override
    public AbstractWanderBackToPoint wanderBackToPoint(LivingEntity livingEntity, Location blockLocation, double maximumDistanceFromPoint, int maxDurationTicks, OverridableWanderPriority overridableWanderPriority) {
        EntityCreature pathfinderMob1;
        EntityLiving entityLiving = ((CraftLivingEntity)livingEntity).getHandle();
        EntityCreature pathfinderMob = entityLiving instanceof EntityCreature ? (pathfinderMob1 = (EntityCreature)entityLiving) : null;
        entityLiving = ((CraftLivingEntity)livingEntity).getHandle();
        if (!(entityLiving instanceof EntityInsentient)) {
            Bukkit.getLogger().info("[EasyMinecraftPathfinding] Entity type " + String.valueOf(livingEntity.getType()) + " does not extend Mob and is therefore unable to have goals! It will not be able to pathfind.");
            return null;
        }
        EntityInsentient mob = (EntityInsentient)entityLiving;
        if (overridableWanderPriority.brain) {
            return new WanderBackToPointBehavior(livingEntity, mob, blockLocation, maximumDistanceFromPoint, overridableWanderPriority.priority, maxDurationTicks);
        }
        return new WanderBackToPointGoal(mob, livingEntity, pathfinderMob, blockLocation, maximumDistanceFromPoint, overridableWanderPriority.priority, maxDurationTicks);
    }

    @Override
    public void setBlockInNativeDataPalette(World world, int x, int y, int z, BlockData blockData, boolean applyPhysics) {
        MassEditBlocks.setBlockInNativeDataPalette(world, x, y, z, blockData, applyPhysics);
    }

    @Override
    public AbstractPacketBundle createPacketBundle() {
        return new PacketBundle();
    }

    @Override
    public PacketEntityInterface createPacketEntity(EntityType entityType, Location location) {
        return new PacketGenericEntity(entityType, location);
    }

    @Override
    public FakeText createFakeText(Location location, FakeTextSettings settings) {
        return new FakeTextImpl(location, settings);
    }
}

