/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.LivingEntity
 */
package com.andrei1058.bedwars.api.entity;

import com.andrei1058.bedwars.api.BedWars;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.events.player.PlayerKillEvent;
import com.andrei1058.bedwars.api.language.Messages;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

public class Despawnable {
    private LivingEntity e;
    private ITeam team;
    private int despawn = 250;
    private String namePath;
    private PlayerKillEvent.PlayerKillCause deathRegularCause;
    private PlayerKillEvent.PlayerKillCause deathFinalCause;
    private UUID uuid;
    private static BedWars api;

    public Despawnable(LivingEntity e, ITeam team, int despawn, String namePath, PlayerKillEvent.PlayerKillCause deathFinalCause, PlayerKillEvent.PlayerKillCause deathRegularCause) {
        this.e = e;
        if (e == null) {
            return;
        }
        this.uuid = e.getUniqueId();
        this.team = team;
        this.deathFinalCause = deathFinalCause;
        this.deathRegularCause = deathRegularCause;
        if (despawn != 0) {
            this.despawn = despawn;
        }
        this.namePath = namePath;
        if (api == null) {
            api = (BedWars)Bukkit.getServer().getServicesManager().getRegistration(BedWars.class).getProvider();
        }
        api.getVersionSupport().getDespawnablesList().put(this.uuid, this);
        this.setName();
    }

    public void refresh() {
        if (this.e.isDead() || this.e == null || this.team == null || this.team.getArena() == null) {
            api.getVersionSupport().getDespawnablesList().remove(this.uuid);
            if (this.team.getArena() == null) {
                this.e.damage(this.e.getHealth() + 100.0);
            }
            return;
        }
        this.setName();
        --this.despawn;
        if (this.despawn == 0) {
            this.e.damage(this.e.getHealth() + 100.0);
            api.getVersionSupport().getDespawnablesList().remove(this.e.getUniqueId());
        }
    }

    private void setName() {
        int percentuale = (int)(this.e.getHealth() * 100.0 / this.e.getMaxHealth() / 10.0);
        String name = api.getDefaultLang().m(this.namePath).replace("{despawn}", String.valueOf(this.despawn)).replace("{health}", new String(new char[percentuale]).replace("\u0000", api.getDefaultLang().m(Messages.FORMATTING_DESPAWNABLE_UTILITY_NPC_HEALTH)) + new String(new char[10 - percentuale]).replace("\u0000", "\u00a77" + api.getDefaultLang().m(Messages.FORMATTING_DESPAWNABLE_UTILITY_NPC_HEALTH)));
        if (this.team != null) {
            name = name.replace("{TeamColor}", this.team.getColor().chat().toString()).replace("{TeamName}", this.team.getDisplayName(api.getDefaultLang()));
        }
        this.e.setCustomName(name);
    }

    public LivingEntity getEntity() {
        return this.e;
    }

    public ITeam getTeam() {
        return this.team;
    }

    public int getDespawn() {
        return this.despawn;
    }

    public PlayerKillEvent.PlayerKillCause getDeathFinalCause() {
        return this.deathFinalCause;
    }

    public PlayerKillEvent.PlayerKillCause getDeathRegularCause() {
        return this.deathRegularCause;
    }

    public void destroy() {
        if (this.getEntity() != null) {
            this.getEntity().damage(2.147483647E9);
        }
        this.team = null;
        api.getVersionSupport().getDespawnablesList().remove(this.uuid);
    }

    public boolean equals(Object obj) {
        if (obj instanceof LivingEntity) {
            return ((LivingEntity)obj).getUniqueId().equals(this.e.getUniqueId());
        }
        return false;
    }
}

