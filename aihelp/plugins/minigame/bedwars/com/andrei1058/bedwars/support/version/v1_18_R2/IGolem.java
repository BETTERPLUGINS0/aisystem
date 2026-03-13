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
 *  net.minecraft.world.level.entity.EntityAccess
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Location
 *  org.bukkit.craftbukkit.v1_18_R2.CraftWorld
 *  org.bukkit.craftbukkit.v1_18_R2.entity.CraftLivingEntity
 *  org.bukkit.craftbukkit.v1_18_R2.event.CraftEventFactory
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.entity.CreatureSpawnEvent$SpawnReason
 */
package com.andrei1058.bedwars.support.version.v1_18_R2;

import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.support.version.common.VersionCommon;
import com.andrei1058.bedwars.support.version.v1_18_R2.Silverfish;
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
import net.minecraft.world.level.entity.EntityAccess;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_18_R2.event.CraftEventFactory;
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

    protected void u() {
        this.bQ.a(1, (PathfinderGoal)new PathfinderGoalFloat((EntityInsentient)this));
        this.bQ.a(2, (PathfinderGoal)new PathfinderGoalMeleeAttack((EntityCreature)this, 1.5, false));
        this.bR.a(3, (PathfinderGoal)new PathfinderGoalHurtByTarget((EntityCreature)this, new Class[0]));
        this.bQ.a(4, (PathfinderGoal)new PathfinderGoalRandomStroll((EntityCreature)this, 1.0));
        this.bQ.a(5, (PathfinderGoal)new PathfinderGoalRandomLookaround((EntityInsentient)this));
        this.bR.a(6, (PathfinderGoal)new PathfinderGoalNearestAttackableTarget((EntityInsentient)this, EntityHuman.class, 20, true, false, player -> !((EntityHuman)player).getBukkitEntity().isDead() && !this.team.wasMember(((EntityHuman)player).getBukkitEntity().getUniqueId()) && !this.team.getArena().isReSpawning(((EntityHuman)player).getBukkitEntity().getUniqueId()) && !this.team.getArena().isSpectator(((EntityHuman)player).getBukkitEntity().getUniqueId())));
        this.bR.a(7, (PathfinderGoal)new PathfinderGoalNearestAttackableTarget((EntityInsentient)this, IGolem.class, 20, true, false, golem -> ((IGolem)((Object)((Object)golem))).getTeam() != this.team));
        this.bR.a(8, (PathfinderGoal)new PathfinderGoalNearestAttackableTarget((EntityInsentient)this, Silverfish.class, 20, true, false, sf -> ((Silverfish)((Object)((Object)sf))).getTeam() != this.team));
    }

    public ITeam getTeam() {
        return this.team;
    }

    public static LivingEntity spawn(Location loc, ITeam bedWarsTeam, double speed, double health, int despawn) {
        WorldServer mcWorld = ((CraftWorld)Objects.requireNonNull(loc.getWorld())).getHandle();
        IGolem customEnt = new IGolem((EntityTypes<? extends EntityIronGolem>)EntityTypes.P, (World)mcWorld, bedWarsTeam);
        customEnt.a(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftLivingEntity)customEnt.getBukkitEntity()).setRemoveWhenFarAway(false);
        Objects.requireNonNull(customEnt.a(GenericAttributes.a)).a(health);
        Objects.requireNonNull(customEnt.a(GenericAttributes.d)).a(speed);
        if (!CraftEventFactory.doEntityAddEventCalling((World)mcWorld, (Entity)customEnt, (CreatureSpawnEvent.SpawnReason)CreatureSpawnEvent.SpawnReason.CUSTOM)) {
            mcWorld.O.a((EntityAccess)customEnt);
        }
        mcWorld.a((Entity)customEnt);
        customEnt.getBukkitEntity().setPersistent(true);
        customEnt.getBukkitEntity().setCustomNameVisible(true);
        customEnt.getBukkitEntity().setCustomName(Language.getDefaultLanguage().m(Messages.SHOP_UTILITY_NPC_IRON_GOLEM_NAME).replace("{despawn}", String.valueOf(despawn).replace("{health}", StringUtils.repeat((String)(Language.getDefaultLanguage().m(Messages.FORMATTING_DESPAWNABLE_UTILITY_NPC_HEALTH) + " "), (int)10)).replace("{TeamColor}", bedWarsTeam.getColor().chat().toString())));
        return (LivingEntity)customEnt.getBukkitEntity();
    }

    public void a(DamageSource damagesource) {
        super.a(damagesource);
        this.team = null;
        VersionCommon.api.getVersionSupport().getDespawnablesList().remove(this.getBukkitEntity().getUniqueId());
    }

    public boolean d_() {
        return super.d_();
    }
}

