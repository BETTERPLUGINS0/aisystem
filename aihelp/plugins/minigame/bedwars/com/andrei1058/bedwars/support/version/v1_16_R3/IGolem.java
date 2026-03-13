/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_16_R3.DamageSource
 *  net.minecraft.server.v1_16_R3.Entity
 *  net.minecraft.server.v1_16_R3.EntityCreature
 *  net.minecraft.server.v1_16_R3.EntityHuman
 *  net.minecraft.server.v1_16_R3.EntityInsentient
 *  net.minecraft.server.v1_16_R3.EntityIronGolem
 *  net.minecraft.server.v1_16_R3.EntityTypes
 *  net.minecraft.server.v1_16_R3.GenericAttributes
 *  net.minecraft.server.v1_16_R3.PathfinderGoal
 *  net.minecraft.server.v1_16_R3.PathfinderGoalFloat
 *  net.minecraft.server.v1_16_R3.PathfinderGoalHurtByTarget
 *  net.minecraft.server.v1_16_R3.PathfinderGoalMeleeAttack
 *  net.minecraft.server.v1_16_R3.PathfinderGoalNearestAttackableTarget
 *  net.minecraft.server.v1_16_R3.PathfinderGoalRandomLookaround
 *  net.minecraft.server.v1_16_R3.PathfinderGoalRandomStroll
 *  net.minecraft.server.v1_16_R3.World
 *  net.minecraft.server.v1_16_R3.WorldServer
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Location
 *  org.bukkit.craftbukkit.v1_16_R3.CraftWorld
 *  org.bukkit.craftbukkit.v1_16_R3.entity.CraftLivingEntity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.entity.CreatureSpawnEvent$SpawnReason
 */
package com.andrei1058.bedwars.support.version.v1_16_R3;

import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.support.version.common.VersionCommon;
import com.andrei1058.bedwars.support.version.v1_16_R3.Silverfish;
import net.minecraft.server.v1_16_R3.DamageSource;
import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.EntityCreature;
import net.minecraft.server.v1_16_R3.EntityHuman;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityIronGolem;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.GenericAttributes;
import net.minecraft.server.v1_16_R3.PathfinderGoal;
import net.minecraft.server.v1_16_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_16_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_16_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_16_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_16_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_16_R3.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_16_R3.World;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class IGolem
extends EntityIronGolem {
    private ITeam team;

    private IGolem(EntityTypes<? extends EntityIronGolem> entitytypes, World world, ITeam bedWarsTeam) {
        super(entitytypes, world);
        this.team = bedWarsTeam;
    }

    public IGolem(EntityTypes entityTypes, World world) {
        super(entityTypes, world);
    }

    protected void initPathfinder() {
        this.goalSelector.a(1, (PathfinderGoal)new PathfinderGoalFloat((EntityInsentient)this));
        this.goalSelector.a(2, (PathfinderGoal)new PathfinderGoalMeleeAttack((EntityCreature)this, 1.5, false));
        this.targetSelector.a(1, (PathfinderGoal)new PathfinderGoalHurtByTarget((EntityCreature)this, new Class[0]));
        this.goalSelector.a(3, (PathfinderGoal)new PathfinderGoalRandomStroll((EntityCreature)this, 1.0));
        this.goalSelector.a(4, (PathfinderGoal)new PathfinderGoalRandomLookaround((EntityInsentient)this));
        this.targetSelector.a(2, (PathfinderGoal)new PathfinderGoalNearestAttackableTarget((EntityInsentient)this, EntityHuman.class, 20, true, false, player -> player.isAlive() && !this.team.wasMember(player.getUniqueID()) && !this.team.getArena().isReSpawning(player.getUniqueID()) && !this.team.getArena().isSpectator(player.getUniqueID())));
        this.targetSelector.a(3, (PathfinderGoal)new PathfinderGoalNearestAttackableTarget((EntityInsentient)this, IGolem.class, 20, true, false, golem -> ((IGolem)((Object)golem)).getTeam() != this.team));
        this.targetSelector.a(4, (PathfinderGoal)new PathfinderGoalNearestAttackableTarget((EntityInsentient)this, Silverfish.class, 20, true, false, sf -> ((Silverfish)((Object)sf)).getTeam() != this.team));
    }

    public ITeam getTeam() {
        return this.team;
    }

    public static LivingEntity spawn(Location loc, ITeam bedWarsTeam, double speed, double health, int despawn) {
        WorldServer mcWorld = ((CraftWorld)loc.getWorld()).getHandle();
        IGolem customEnt = new IGolem((EntityTypes<? extends EntityIronGolem>)EntityTypes.IRON_GOLEM, (World)mcWorld, bedWarsTeam);
        customEnt.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftLivingEntity)customEnt.getBukkitEntity()).setRemoveWhenFarAway(false);
        customEnt.setCustomNameVisible(true);
        customEnt.setPersistent();
        customEnt.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(health);
        customEnt.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(speed);
        mcWorld.addEntity((Entity)customEnt, CreatureSpawnEvent.SpawnReason.CUSTOM);
        customEnt.getBukkitEntity().setCustomName(Language.getDefaultLanguage().m(Messages.SHOP_UTILITY_NPC_IRON_GOLEM_NAME).replace("{despawn}", String.valueOf(despawn).replace("{health}", StringUtils.repeat((String)(Language.getDefaultLanguage().m(Messages.FORMATTING_DESPAWNABLE_UTILITY_NPC_HEALTH) + " "), (int)10)).replace("{TeamColor}", bedWarsTeam.getColor().chat().toString())));
        return (LivingEntity)customEnt.getBukkitEntity();
    }

    public void die() {
        super.die();
        this.team = null;
        VersionCommon.api.getVersionSupport().getDespawnablesList().remove(this.getUniqueID());
    }

    public void die(DamageSource damagesource) {
        super.die(damagesource);
        this.team = null;
        VersionCommon.api.getVersionSupport().getDespawnablesList().remove(this.getUniqueID());
    }
}

