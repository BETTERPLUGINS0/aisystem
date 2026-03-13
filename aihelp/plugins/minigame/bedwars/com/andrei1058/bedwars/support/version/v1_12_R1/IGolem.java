/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_12_R1.DamageSource
 *  net.minecraft.server.v1_12_R1.Entity
 *  net.minecraft.server.v1_12_R1.EntityCreature
 *  net.minecraft.server.v1_12_R1.EntityHuman
 *  net.minecraft.server.v1_12_R1.EntityInsentient
 *  net.minecraft.server.v1_12_R1.EntityIronGolem
 *  net.minecraft.server.v1_12_R1.GenericAttributes
 *  net.minecraft.server.v1_12_R1.MinecraftKey
 *  net.minecraft.server.v1_12_R1.Navigation
 *  net.minecraft.server.v1_12_R1.PathfinderGoal
 *  net.minecraft.server.v1_12_R1.PathfinderGoalFloat
 *  net.minecraft.server.v1_12_R1.PathfinderGoalHurtByTarget
 *  net.minecraft.server.v1_12_R1.PathfinderGoalMeleeAttack
 *  net.minecraft.server.v1_12_R1.PathfinderGoalNearestAttackableTarget
 *  net.minecraft.server.v1_12_R1.PathfinderGoalRandomLookaround
 *  net.minecraft.server.v1_12_R1.PathfinderGoalRandomStroll
 *  net.minecraft.server.v1_12_R1.PathfinderGoalSelector
 *  net.minecraft.server.v1_12_R1.World
 *  net.minecraft.server.v1_12_R1.WorldServer
 *  org.bukkit.Location
 *  org.bukkit.craftbukkit.v1_12_R1.CraftWorld
 *  org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.entity.CreatureSpawnEvent$SpawnReason
 */
package com.andrei1058.bedwars.support.version.v1_12_R1;

import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.support.version.common.VersionCommon;
import com.andrei1058.bedwars.support.version.v1_12_R1.Silverfish;
import com.google.common.collect.Sets;
import java.lang.reflect.Field;
import net.minecraft.server.v1_12_R1.DamageSource;
import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityCreature;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.EntityIronGolem;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.Navigation;
import net.minecraft.server.v1_12_R1.PathfinderGoal;
import net.minecraft.server.v1_12_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_12_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_12_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_12_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_12_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_12_R1.World;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class IGolem
extends EntityIronGolem {
    private ITeam team;

    private IGolem(World world, ITeam team) {
        super(world);
        this.team = team;
        try {
            Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
            bField.setAccessible(true);
            Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
            cField.setAccessible(true);
            bField.set(this.goalSelector, Sets.newLinkedHashSet());
            bField.set(this.targetSelector, Sets.newLinkedHashSet());
            cField.set(this.goalSelector, Sets.newLinkedHashSet());
            cField.set(this.targetSelector, Sets.newLinkedHashSet());
        } catch (IllegalAccessException | NoSuchFieldException e1) {
            e1.printStackTrace();
        }
        this.setSize(1.4f, 2.9f);
        ((Navigation)this.getNavigation()).a(true);
        this.goalSelector.a(1, (PathfinderGoal)new PathfinderGoalFloat((EntityInsentient)this));
        this.goalSelector.a(2, (PathfinderGoal)new PathfinderGoalMeleeAttack((EntityCreature)this, 1.5, false));
        this.targetSelector.a(1, (PathfinderGoal)new PathfinderGoalHurtByTarget((EntityCreature)this, true, new Class[0]));
        this.goalSelector.a(3, (PathfinderGoal)new PathfinderGoalRandomStroll((EntityCreature)this, 1.0));
        this.goalSelector.a(4, (PathfinderGoal)new PathfinderGoalRandomLookaround((EntityInsentient)this));
        this.targetSelector.a(2, (PathfinderGoal)new PathfinderGoalNearestAttackableTarget((EntityCreature)this, EntityHuman.class, 20, true, false, player -> ((EntityHuman)player).isAlive() && !team.wasMember(((EntityHuman)player).getUniqueID()) && !team.getArena().isReSpawning(((EntityHuman)player).getUniqueID()) && !team.getArena().isSpectator(((EntityHuman)player).getUniqueID())));
        this.targetSelector.a(3, (PathfinderGoal)new PathfinderGoalNearestAttackableTarget((EntityCreature)this, IGolem.class, 20, true, false, golem -> ((IGolem)((Object)((Object)golem))).getTeam() != team));
        this.targetSelector.a(4, (PathfinderGoal)new PathfinderGoalNearestAttackableTarget((EntityCreature)this, Silverfish.class, 20, true, false, sf -> ((Silverfish)((Object)((Object)sf))).getTeam() != team));
    }

    public ITeam getTeam() {
        return this.team;
    }

    public static LivingEntity spawn(Location loc, ITeam bedWarsTeam, double speed, double health, int despawn) {
        WorldServer mcWorld = ((CraftWorld)loc.getWorld()).getHandle();
        IGolem customEnt = new IGolem((World)mcWorld, bedWarsTeam);
        customEnt.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftLivingEntity)customEnt.getBukkitEntity()).setRemoveWhenFarAway(false);
        customEnt.setCustomNameVisible(true);
        customEnt.getAttributeInstance(GenericAttributes.maxHealth).setValue(health);
        customEnt.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(speed);
        customEnt.setCustomName(Language.getDefaultLanguage().m(Messages.SHOP_UTILITY_NPC_IRON_GOLEM_NAME).replace("{despawn}", String.valueOf(despawn).replace("{health}", StringUtils.repeat(Language.getDefaultLanguage().m(Messages.FORMATTING_DESPAWNABLE_UTILITY_NPC_HEALTH) + " ", 10)).replace("{TeamColor}", bedWarsTeam.getColor().chat().toString())));
        mcWorld.addEntity((Entity)customEnt, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (LivingEntity)customEnt.getBukkitEntity();
    }

    protected MinecraftKey J() {
        return null;
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

