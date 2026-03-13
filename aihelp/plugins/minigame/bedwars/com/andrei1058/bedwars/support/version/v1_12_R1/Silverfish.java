/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_12_R1.Block
 *  net.minecraft.server.v1_12_R1.BlockMonsterEggs
 *  net.minecraft.server.v1_12_R1.BlockMonsterEggs$EnumMonsterEggVarient
 *  net.minecraft.server.v1_12_R1.BlockPosition
 *  net.minecraft.server.v1_12_R1.Blocks
 *  net.minecraft.server.v1_12_R1.DamageSource
 *  net.minecraft.server.v1_12_R1.Entity
 *  net.minecraft.server.v1_12_R1.EntityCreature
 *  net.minecraft.server.v1_12_R1.EntityHuman
 *  net.minecraft.server.v1_12_R1.EntityInsentient
 *  net.minecraft.server.v1_12_R1.EntitySilverfish
 *  net.minecraft.server.v1_12_R1.EnumDirection
 *  net.minecraft.server.v1_12_R1.GenericAttributes
 *  net.minecraft.server.v1_12_R1.IBlockData
 *  net.minecraft.server.v1_12_R1.IBlockState
 *  net.minecraft.server.v1_12_R1.PathfinderGoal
 *  net.minecraft.server.v1_12_R1.PathfinderGoalFloat
 *  net.minecraft.server.v1_12_R1.PathfinderGoalHurtByTarget
 *  net.minecraft.server.v1_12_R1.PathfinderGoalMeleeAttack
 *  net.minecraft.server.v1_12_R1.PathfinderGoalNearestAttackableTarget
 *  net.minecraft.server.v1_12_R1.PathfinderGoalRandomStroll
 *  net.minecraft.server.v1_12_R1.PathfinderGoalSelector
 *  net.minecraft.server.v1_12_R1.World
 *  net.minecraft.server.v1_12_R1.WorldServer
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Location
 *  org.bukkit.craftbukkit.v1_12_R1.CraftWorld
 *  org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity
 *  org.bukkit.craftbukkit.v1_12_R1.event.CraftEventFactory
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.entity.CreatureSpawnEvent$SpawnReason
 */
package com.andrei1058.bedwars.support.version.v1_12_R1;

import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.support.version.common.VersionCommon;
import com.andrei1058.bedwars.support.version.v1_12_R1.IGolem;
import com.google.common.collect.Sets;
import java.lang.reflect.Field;
import java.util.Random;
import net.minecraft.server.v1_12_R1.Block;
import net.minecraft.server.v1_12_R1.BlockMonsterEggs;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.Blocks;
import net.minecraft.server.v1_12_R1.DamageSource;
import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityCreature;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.EntitySilverfish;
import net.minecraft.server.v1_12_R1.EnumDirection;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import net.minecraft.server.v1_12_R1.IBlockData;
import net.minecraft.server.v1_12_R1.IBlockState;
import net.minecraft.server.v1_12_R1.PathfinderGoal;
import net.minecraft.server.v1_12_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_12_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_12_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_12_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_12_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_12_R1.World;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_12_R1.event.CraftEventFactory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class Silverfish
extends EntitySilverfish {
    private ITeam team;

    public Silverfish(World world, ITeam team) {
        super(world);
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
        this.team = team;
        this.goalSelector.a(1, (PathfinderGoal)new PathfinderGoalFloat((EntityInsentient)this));
        this.goalSelector.a(2, (PathfinderGoal)new PathfinderGoalMeleeAttack((EntityCreature)this, 1.9, false));
        this.targetSelector.a(1, (PathfinderGoal)new PathfinderGoalHurtByTarget((EntityCreature)this, true, new Class[0]));
        this.goalSelector.a(3, (PathfinderGoal)new PathfinderGoalRandomStroll((EntityCreature)this, 2.0));
        this.targetSelector.a(2, (PathfinderGoal)new PathfinderGoalNearestAttackableTarget((EntityCreature)this, EntityHuman.class, 20, true, false, player -> ((EntityHuman)player).isAlive() && !team.wasMember(((EntityHuman)player).getUniqueID()) && !team.getArena().isReSpawning(((EntityHuman)player).getUniqueID()) && !team.getArena().isSpectator(((EntityHuman)player).getUniqueID())));
        this.targetSelector.a(3, (PathfinderGoal)new PathfinderGoalNearestAttackableTarget((EntityCreature)this, IGolem.class, 20, true, false, golem -> ((IGolem)((Object)((Object)golem))).getTeam() != team));
        this.targetSelector.a(4, (PathfinderGoal)new PathfinderGoalNearestAttackableTarget((EntityCreature)this, Silverfish.class, 20, true, false, sf -> ((Silverfish)((Object)((Object)sf))).getTeam() != team));
    }

    public ITeam getTeam() {
        return this.team;
    }

    public static LivingEntity spawn(Location loc, ITeam team, double speed, double health, int despawn, double damage) {
        WorldServer mcWorld = ((CraftWorld)loc.getWorld()).getHandle();
        Silverfish customEnt = new Silverfish((World)mcWorld, team);
        customEnt.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        customEnt.getAttributeInstance(GenericAttributes.maxHealth).setValue(health);
        customEnt.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(speed);
        customEnt.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(damage);
        ((CraftLivingEntity)customEnt.getBukkitEntity()).setRemoveWhenFarAway(false);
        customEnt.setCustomName(Language.getDefaultLanguage().m(Messages.SHOP_UTILITY_NPC_SILVERFISH_NAME).replace("{despawn}", String.valueOf(despawn).replace("{health}", StringUtils.repeat((String)(Language.getDefaultLanguage().m(Messages.FORMATTING_DESPAWNABLE_UTILITY_NPC_HEALTH) + " "), (int)10)).replace("{TeamColor}", team.getColor().chat().toString())));
        customEnt.setCustomNameVisible(true);
        mcWorld.addEntity((Entity)customEnt, CreatureSpawnEvent.SpawnReason.CUSTOM);
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

    static class PathfinderGoalSilverfishHideInBlock
    extends PathfinderGoalRandomStroll {
        private EnumDirection h;
        private boolean i;

        public PathfinderGoalSilverfishHideInBlock(EntitySilverfish entitysilverfish) {
            super((EntityCreature)entitysilverfish, 1.0, 10);
            this.a(1);
        }

        public boolean a() {
            if (this.a.getGoalTarget() != null) {
                return false;
            }
            if (!this.a.getNavigation().o()) {
                return false;
            }
            Random random = this.a.getRandom();
            if (this.a.world.getGameRules().getBoolean("mobGriefing") && random.nextInt(10) == 0) {
                this.h = EnumDirection.a((Random)random);
                BlockPosition blockposition = new BlockPosition(this.a.locX, this.a.locY + 0.5, this.a.locZ).shift(this.h);
                IBlockData iblockdata = this.a.world.getType(blockposition);
                if (BlockMonsterEggs.x((IBlockData)iblockdata)) {
                    this.i = true;
                    return true;
                }
            }
            this.i = false;
            return super.a();
        }

        public boolean b() {
            return !this.i && super.b();
        }

        public void c() {
            if (!this.i) {
                super.c();
            } else {
                World world = this.a.world;
                BlockPosition blockposition = new BlockPosition(this.a.locX, this.a.locY + 0.5, this.a.locZ).shift(this.h);
                IBlockData iblockdata = world.getType(blockposition);
                if (BlockMonsterEggs.x((IBlockData)iblockdata)) {
                    if (CraftEventFactory.callEntityChangeBlockEvent((Entity)this.a, (BlockPosition)blockposition, (Block)Blocks.MONSTER_EGG, (int)Block.getId((Block)BlockMonsterEggs.getById((int)iblockdata.getBlock().toLegacyData(iblockdata)))).isCancelled()) {
                        return;
                    }
                    world.setTypeAndData(blockposition, Blocks.MONSTER_EGG.getBlockData().set((IBlockState)BlockMonsterEggs.VARIANT, (Comparable)BlockMonsterEggs.EnumMonsterEggVarient.a((IBlockData)iblockdata)), 3);
                    this.a.doSpawnEffect();
                    this.a.die();
                }
            }
        }
    }
}

