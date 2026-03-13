/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.EntityCreature
 *  net.minecraft.world.entity.EntityInsentient
 *  net.minecraft.world.entity.ai.goal.PathfinderGoal
 *  net.minecraft.world.entity.ai.goal.PathfinderGoalFloat
 *  net.minecraft.world.entity.ai.goal.PathfinderGoalMeleeAttack
 *  net.minecraft.world.entity.ai.goal.PathfinderGoalRandomLookaround
 *  net.minecraft.world.entity.ai.goal.PathfinderGoalRandomStroll
 *  net.minecraft.world.entity.ai.goal.PathfinderGoalSelector
 *  net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget
 *  net.minecraft.world.entity.animal.EntityIronGolem
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Location
 *  org.bukkit.craftbukkit.v1_19_R3.entity.CraftEntity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.IronGolem
 *  org.bukkit.entity.LivingEntity
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.support.version.v1_19_R3.despawnable;

import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.server.VersionSupport;
import com.andrei1058.bedwars.support.version.v1_19_R3.despawnable.DespawnableAttributes;
import com.andrei1058.bedwars.support.version.v1_19_R3.despawnable.DespawnableProvider;
import com.andrei1058.bedwars.support.version.v1_19_R3.despawnable.DespawnableType;
import java.util.Objects;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFloat;
import net.minecraft.world.entity.ai.goal.PathfinderGoalMeleeAttack;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomLookaround;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomStroll;
import net.minecraft.world.entity.ai.goal.PathfinderGoalSelector;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget;
import net.minecraft.world.entity.animal.EntityIronGolem;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class TeamIronGolem
extends DespawnableProvider<IronGolem> {
    @Override
    public DespawnableType getType() {
        return DespawnableType.IRON_GOLEM;
    }

    @Override
    String getDisplayName(@NotNull DespawnableAttributes attr, @NotNull ITeam team) {
        Language lang = Language.getDefaultLanguage();
        return lang.m(Messages.SHOP_UTILITY_NPC_IRON_GOLEM_NAME).replace("{despawn}", String.valueOf(attr.despawnSeconds()).replace("{health}", StringUtils.repeat((String)(lang.m(Messages.FORMATTING_DESPAWNABLE_UTILITY_NPC_HEALTH) + " "), (int)10)).replace("{TeamColor}", team.getColor().chat().toString()));
    }

    @Override
    @NotNull
    public IronGolem spawn(@NotNull DespawnableAttributes attr, @NotNull Location location, @NotNull ITeam team, VersionSupport api) {
        IronGolem bukkitEntity = (IronGolem)Objects.requireNonNull(location.getWorld()).spawnEntity(location, EntityType.IRON_GOLEM);
        this.applyDefaultSettings((LivingEntity)bukkitEntity, attr, team);
        EntityIronGolem entity = (EntityIronGolem)((CraftEntity)bukkitEntity).getHandle();
        this.clearSelectors((EntityCreature)entity);
        PathfinderGoalSelector goalSelector = this.getGoalSelector((EntityCreature)entity);
        PathfinderGoalSelector targetSelector = this.getTargetSelector((EntityCreature)entity);
        goalSelector.a(1, (PathfinderGoal)new PathfinderGoalFloat((EntityInsentient)entity));
        goalSelector.a(2, (PathfinderGoal)new PathfinderGoalMeleeAttack((EntityCreature)entity, 1.5, false));
        goalSelector.a(3, (PathfinderGoal)new PathfinderGoalRandomStroll((EntityCreature)entity, 1.0));
        goalSelector.a(4, (PathfinderGoal)new PathfinderGoalRandomLookaround((EntityInsentient)entity));
        targetSelector.a(1, (PathfinderGoal)new PathfinderGoalHurtByTarget((EntityCreature)entity, new Class[0]));
        targetSelector.a(2, this.getTargetGoal((EntityInsentient)entity, team, api));
        return bukkitEntity;
    }
}

