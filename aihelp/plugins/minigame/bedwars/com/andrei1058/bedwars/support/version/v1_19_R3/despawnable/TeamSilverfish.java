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
 *  net.minecraft.world.entity.monster.EntitySilverfish
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Location
 *  org.bukkit.craftbukkit.v1_19_R3.entity.CraftEntity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Silverfish
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
import net.minecraft.world.entity.monster.EntitySilverfish;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Silverfish;
import org.jetbrains.annotations.NotNull;

public class TeamSilverfish
extends DespawnableProvider<Silverfish> {
    @Override
    public DespawnableType getType() {
        return DespawnableType.SILVERFISH;
    }

    @Override
    String getDisplayName(@NotNull DespawnableAttributes attr, @NotNull ITeam team) {
        Language lang = Language.getDefaultLanguage();
        return lang.m(Messages.SHOP_UTILITY_NPC_SILVERFISH_NAME).replace("{despawn}", String.valueOf(attr.despawnSeconds()).replace("{health}", StringUtils.repeat((String)(lang.m(Messages.FORMATTING_DESPAWNABLE_UTILITY_NPC_HEALTH) + " "), (int)10)).replace("{TeamColor}", team.getColor().chat().toString()));
    }

    @Override
    public Silverfish spawn(@NotNull DespawnableAttributes attr, @NotNull Location location, @NotNull ITeam team, VersionSupport api) {
        Silverfish bukkitEntity = (Silverfish)Objects.requireNonNull(location.getWorld()).spawnEntity(location, EntityType.SILVERFISH);
        this.applyDefaultSettings((LivingEntity)bukkitEntity, attr, team);
        EntitySilverfish entity = (EntitySilverfish)((CraftEntity)bukkitEntity).getHandle();
        this.clearSelectors((EntityCreature)entity);
        PathfinderGoalSelector goalSelector = this.getGoalSelector((EntityCreature)entity);
        PathfinderGoalSelector targetSelector = this.getTargetSelector((EntityCreature)entity);
        goalSelector.a(1, (PathfinderGoal)new PathfinderGoalFloat((EntityInsentient)entity));
        goalSelector.a(2, (PathfinderGoal)new PathfinderGoalMeleeAttack((EntityCreature)entity, 1.9, false));
        goalSelector.a(3, (PathfinderGoal)new PathfinderGoalRandomStroll((EntityCreature)entity, 2.0));
        goalSelector.a(4, (PathfinderGoal)new PathfinderGoalRandomLookaround((EntityInsentient)entity));
        targetSelector.a(1, (PathfinderGoal)new PathfinderGoalHurtByTarget((EntityCreature)entity, new Class[0]));
        targetSelector.a(2, this.getTargetGoal((EntityInsentient)entity, team, api));
        return bukkitEntity;
    }
}

