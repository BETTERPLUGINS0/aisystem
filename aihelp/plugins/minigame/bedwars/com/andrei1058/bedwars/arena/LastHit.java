/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.arena;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LastHit {
    private UUID victim;
    private Entity damager;
    private long time;
    private static ConcurrentHashMap<UUID, LastHit> lastHit = new ConcurrentHashMap();

    public LastHit(@NotNull Player victim, Entity damager, long time) {
        this.victim = victim.getUniqueId();
        this.damager = damager;
        this.time = time;
        lastHit.put(victim.getUniqueId(), this);
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setDamager(Entity damager) {
        this.damager = damager;
    }

    public Entity getDamager() {
        return this.damager;
    }

    public UUID getVictim() {
        return this.victim;
    }

    public void remove() {
        lastHit.remove(this.victim);
    }

    public long getTime() {
        return this.time;
    }

    public static LastHit getLastHit(@NotNull Player player) {
        return lastHit.getOrDefault(player.getUniqueId(), null);
    }
}

