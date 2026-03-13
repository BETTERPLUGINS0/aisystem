/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.WorldServer
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityCreature
 *  net.minecraft.world.entity.EntityInsentient
 *  net.minecraft.world.entity.EntityTypes
 *  net.minecraft.world.entity.ai.attributes.GenericAttributes
 *  net.minecraft.world.entity.ai.goal.PathfinderGoal
 *  net.minecraft.world.entity.ai.goal.PathfinderGoalFloat
 *  net.minecraft.world.entity.ai.goal.PathfinderGoalMeleeAttack
 *  net.minecraft.world.entity.ai.goal.PathfinderGoalRandomLookaround
 *  net.minecraft.world.entity.ai.goal.PathfinderGoalRandomStroll
 *  net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget
 *  net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget
 *  net.minecraft.world.entity.animal.EntityIronGolem
 *  net.minecraft.world.entity.player.EntityHuman
 *  net.minecraft.world.level.World
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Location
 *  org.bukkit.craftbukkit.v1_17_R1.CraftWorld
 *  org.bukkit.craftbukkit.v1_17_R1.entity.CraftLivingEntity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.entity.CreatureSpawnEvent$SpawnReason
 */
package com.andrei1058.bedwars.support.version.v1_17_R1;

import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.support.version.common.VersionCommon;
import com.andrei1058.bedwars.support.version.v1_17_R1.Silverfish;
import java.util.Objects;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFloat;
import net.minecraft.world.entity.ai.goal.PathfinderGoalMeleeAttack;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomLookaround;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomStroll;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.animal.EntityIronGolem;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.level.World;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

@Deprecated
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
        this.bP.a(1, (PathfinderGoal)new PathfinderGoalFloat((EntityInsentient)this));
        this.bP.a(2, (PathfinderGoal)new PathfinderGoalMeleeAttack((EntityCreature)this, 1.5, false));
        this.bQ.a(1, (PathfinderGoal)new PathfinderGoalHurtByTarget((EntityCreature)this, new Class[0]));
        this.bP.a(3, (PathfinderGoal)new PathfinderGoalRandomStroll((EntityCreature)this, 1.0));
        this.bP.a(4, (PathfinderGoal)new PathfinderGoalRandomLookaround((EntityInsentient)this));
        this.bQ.a(2, (PathfinderGoal)new PathfinderGoalNearestAttackableTarget((EntityInsentient)this, EntityHuman.class, 20, true, false, player -> ((EntityHuman)player).isAlive() && !this.team.wasMember(((EntityHuman)player).getUniqueID()) && !this.team.getArena().isReSpawning(((EntityHuman)player).getUniqueID()) && !this.team.getArena().isSpectator(((EntityHuman)player).getUniqueID())));
        this.bQ.a(3, (PathfinderGoal)new PathfinderGoalNearestAttackableTarget((EntityInsentient)this, IGolem.class, 20, true, false, golem -> ((IGolem)((Object)((Object)golem))).getTeam() != this.team));
        this.bQ.a(4, (PathfinderGoal)new PathfinderGoalNearestAttackableTarget((EntityInsentient)this, Silverfish.class, 20, true, false, sf -> ((Silverfish)((Object)((Object)sf))).getTeam() != this.team));
    }

    public ITeam getTeam() {
        return this.team;
    }

    public static LivingEntity spawn(Location loc, ITeam bedWarsTeam, double speed, double health, int despawn) {
        WorldServer mcWorld = ((CraftWorld)Objects.requireNonNull(loc.getWorld())).getHandle();
        IGolem customEnt = new IGolem((EntityTypes<? extends EntityIronGolem>)EntityTypes.P, (World)mcWorld, bedWarsTeam);
        customEnt.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftLivingEntity)customEnt.getBukkitEntity()).setRemoveWhenFarAway(false);
        customEnt.setCustomNameVisible(true);
        customEnt.setPersistent();
        Objects.requireNonNull(customEnt.getAttributeInstance(GenericAttributes.a)).setValue(health);
        Objects.requireNonNull(customEnt.getAttributeInstance(GenericAttributes.d)).setValue(speed);
        mcWorld.addEntity((Entity)customEnt, CreatureSpawnEvent.SpawnReason.CUSTOM);
        customEnt.getBukkitEntity().setCustomName(Language.getDefaultLanguage().m(Messages.SHOP_UTILITY_NPC_IRON_GOLEM_NAME).replace("{despawn}", String.valueOf(despawn).replace("{health}", StringUtils.repeat((String)(Language.getDefaultLanguage().m(Messages.FORMATTING_DESPAWNABLE_UTILITY_NPC_HEALTH) + " "), (int)10)).replace("{TeamColor}", bedWarsTeam.getColor().chat().toString())));
        return (LivingEntity)customEnt.getBukkitEntity();
    }

    public void die(DamageSource damagesource) {
        super.die(damagesource);
        this.team = null;
        VersionCommon.api.getVersionSupport().getDespawnablesList().remove(this.getUniqueID());
    }
}

