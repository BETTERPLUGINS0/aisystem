/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityCreature
 *  net.minecraft.world.entity.EntityInsentient
 *  net.minecraft.world.entity.EntityLiving
 *  net.minecraft.world.entity.ai.attributes.GenericAttributes
 *  net.minecraft.world.entity.ai.goal.PathfinderGoal
 *  net.minecraft.world.entity.ai.goal.PathfinderGoalSelector
 *  net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget
 *  net.minecraft.world.entity.player.EntityHuman
 *  org.bukkit.Location
 *  org.bukkit.craftbukkit.v1_19_R2.entity.CraftEntity
 *  org.bukkit.entity.LivingEntity
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.support.version.v1_19_R2.despawnable;

import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.entity.Despawnable;
import com.andrei1058.bedwars.api.server.VersionSupport;
import com.andrei1058.bedwars.support.version.v1_19_R2.despawnable.DespawnableAttributes;
import com.andrei1058.bedwars.support.version.v1_19_R2.despawnable.DespawnableType;
import java.util.Objects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalSelector;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.player.EntityHuman;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@Deprecated
public abstract class DespawnableProvider<T> {
    abstract DespawnableType getType();

    abstract String getDisplayName(DespawnableAttributes var1, ITeam var2);

    abstract T spawn(@NotNull DespawnableAttributes var1, @NotNull Location var2, @NotNull ITeam var3, VersionSupport var4);

    protected boolean notSameTeam(@NotNull Entity entity, ITeam team, @NotNull VersionSupport api) {
        Despawnable despawnable = api.getDespawnablesList().getOrDefault(entity.getBukkitEntity().getUniqueId(), null);
        return null == despawnable || despawnable.getTeam() != team;
    }

    protected PathfinderGoalSelector getTargetSelector(@NotNull EntityCreature entityLiving) {
        return entityLiving.bT;
    }

    protected PathfinderGoalSelector getGoalSelector(@NotNull EntityCreature entityLiving) {
        return entityLiving.bS;
    }

    protected void clearSelectors(@NotNull EntityCreature entityLiving) {
        entityLiving.bS.b().clear();
        entityLiving.bT.b().clear();
    }

    protected PathfinderGoal getTargetGoal(EntityInsentient entity, ITeam team, VersionSupport api) {
        return new PathfinderGoalNearestAttackableTarget(entity, EntityLiving.class, 20, true, false, entityLiving -> {
            if (entityLiving instanceof EntityHuman) {
                return !((EntityHuman)entityLiving).getBukkitEntity().isDead() && !team.wasMember(((EntityHuman)entityLiving).getBukkitEntity().getUniqueId()) && !team.getArena().isReSpawning(((EntityHuman)entityLiving).getBukkitEntity().getUniqueId()) && !team.getArena().isSpectator(((EntityHuman)entityLiving).getBukkitEntity().getUniqueId());
            }
            return this.notSameTeam((Entity)entityLiving, team, api);
        });
    }

    protected void applyDefaultSettings(@NotNull LivingEntity bukkitEntity, DespawnableAttributes attr, ITeam team) {
        bukkitEntity.setRemoveWhenFarAway(false);
        bukkitEntity.setPersistent(true);
        bukkitEntity.setCustomNameVisible(true);
        bukkitEntity.setCustomName(this.getDisplayName(attr, team));
        EntityInsentient entity = (EntityInsentient)((CraftEntity)bukkitEntity).getHandle();
        Objects.requireNonNull(entity.a(GenericAttributes.a)).a(attr.health());
        Objects.requireNonNull(entity.a(GenericAttributes.d)).a(attr.speed());
        Objects.requireNonNull(entity.a(GenericAttributes.f)).a(attr.damage());
    }
}

