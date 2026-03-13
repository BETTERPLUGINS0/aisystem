/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityCreature
 *  net.minecraft.world.entity.EntityInsentient
 *  net.minecraft.world.entity.EnumMoveType
 *  net.minecraft.world.entity.ai.attributes.GenericAttributes
 *  net.minecraft.world.level.pathfinder.PathEntity
 *  net.minecraft.world.phys.Vec3D
 *  org.bukkit.Location
 *  org.bukkit.util.Vector
 */
package com.magmaguy.betterstructures.easyminecraftgoals.v1_21_R7_paper.move;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EnumMoveType;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.level.pathfinder.PathEntity;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Move {
    public static boolean canReach(EntityCreature pathfinderMob, Location destination) {
        if (pathfinderMob.N() == null) {
            return true;
        }
        PathEntity path = pathfinderMob.N().a(destination.getX(), destination.getY(), destination.getZ(), 0);
        if (path == null) {
            return true;
        }
        return path.j();
    }

    public static boolean simpleMove(EntityCreature pathfinderMob, double speedModifier, Location destination) {
        PathEntity path = pathfinderMob.N().a(destination.getX(), destination.getY(), destination.getZ(), 0);
        return pathfinderMob.N().a(path, speedModifier);
    }

    public static void doNotMove(EntityCreature pathfinderMob) {
        pathfinderMob.N().a((Entity)pathfinderMob, 0.0);
    }

    public static void universalMove(EntityInsentient mob, double speedModifier, Location destination) {
        double speed = mob.i(GenericAttributes.x) * 0.75;
        Vec3D movementInTick = new Vec3D(destination.getX(), destination.getY(), destination.getZ()).d(mob.dI());
        movementInTick = movementInTick.d().d(speedModifier * speed, speedModifier * speed, speedModifier * speed);
        mob.a(EnumMoveType.a, movementInTick);
        Move.rotateHead((Entity)mob, destination.toVector(), new Vector(mob.dI().g, mob.dI().h, mob.dI().i));
    }

    private static void rotateHead(Entity entity, Vector destination, Vector currentLocation) {
        Vector newVector = destination.subtract(currentLocation);
        double x = newVector.getX();
        double z = newVector.getZ();
        double targetRot = Math.abs(x) > Math.abs(z) ? (x > 0.0 ? -90.0 : 90.0) : (z > 0.0 ? 0.0 : 180.0);
        double currentRot = entity.ec();
        if (currentRot == targetRot) {
            return;
        }
        if (targetRot - currentRot > 0.0) {
            entity.b(90.0, 0.0);
        } else {
            entity.b(-90.0, 0.0);
        }
    }

    public static boolean forcedMove(EntityInsentient mob, double speedModifier, Location destination) {
        mob.gJ();
        Move.universalMove(mob, speedModifier, destination);
        return true;
    }
}

