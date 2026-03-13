/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.api.events.player;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import java.util.function.Function;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerKillEvent
extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final IArena arena;
    private final Player victim;
    private ITeam victimTeam;
    private final Player killer;
    private ITeam killerTeam;
    private final PlayerKillCause cause;
    private Function<Player, String> message;
    private boolean playSound = true;

    @Deprecated
    public PlayerKillEvent(@NotNull IArena arena, Player victim, Player killer, Function<Player, String> message, PlayerKillCause cause) {
        this.victimTeam = arena.getTeam(victim);
        if (null == this.victimTeam) {
            this.victimTeam = arena.getExTeam(victim.getUniqueId());
        }
        if (null != killer) {
            this.killerTeam = arena.getTeam(killer);
            if (null == this.killerTeam) {
                this.killerTeam = arena.getExTeam(killer.getUniqueId());
            }
        }
        this.arena = arena;
        this.victim = victim;
        this.killer = killer;
        this.message = message;
        this.cause = cause;
    }

    public PlayerKillEvent(@NotNull IArena arena, @NotNull Player victim, @Nullable ITeam victimTeam, @Nullable Player killer, @Nullable ITeam killerTeam, @Nullable Function<Player, String> message, @NotNull PlayerKillCause cause) {
        this.arena = arena;
        this.victim = victim;
        this.killer = killer;
        this.message = message;
        this.cause = cause;
        this.victimTeam = victimTeam;
        this.killerTeam = killerTeam;
    }

    public Player getKiller() {
        return this.killer;
    }

    @Nullable
    public Function<Player, String> getMessage() {
        return this.message;
    }

    public void setMessage(@Nullable Function<Player, String> message) {
        this.message = message;
    }

    public PlayerKillCause getCause() {
        return this.cause;
    }

    public IArena getArena() {
        return this.arena;
    }

    public Player getVictim() {
        return this.victim;
    }

    public boolean playSound() {
        return this.playSound;
    }

    public void setPlaySound(boolean playSound) {
        this.playSound = playSound;
    }

    public ITeam getKillerTeam() {
        return this.killerTeam;
    }

    public ITeam getVictimTeam() {
        return this.victimTeam;
    }

    public void setKillerTeam(ITeam killerTeam) {
        this.killerTeam = killerTeam;
    }

    public void setVictimTeam(ITeam victimTeam) {
        this.victimTeam = victimTeam;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public static enum PlayerKillCause {
        UNKNOWN(false, false, false),
        UNKNOWN_FINAL_KILL(true, false, false),
        EXPLOSION(false, false, false),
        EXPLOSION_FINAL_KILL(true, false, false),
        VOID(false, false, false),
        VOID_FINAL_KILL(true, false, false),
        PVP(false, false, false),
        PVP_FINAL_KILL(true, false, false),
        PLAYER_SHOOT(false, false, false),
        PLAYER_SHOOT_FINAL_KILL(true, false, false),
        SILVERFISH(false, true, false),
        SILVERFISH_FINAL_KILL(true, true, false),
        IRON_GOLEM(false, true, false),
        IRON_GOLEM_FINAL_KILL(true, true, false),
        PLAYER_PUSH(false, false, false),
        PLAYER_PUSH_FINAL(true, false, false),
        PLAYER_DISCONNECT(false, false, true),
        PLAYER_DISCONNECT_FINAL(true, false, true);

        private final boolean finalKill;
        private final boolean despawnable;
        private final boolean pvpLogOut;

        private PlayerKillCause(boolean finalKill, boolean despawnable, boolean pvpLogOut) {
            this.finalKill = finalKill;
            this.despawnable = despawnable;
            this.pvpLogOut = pvpLogOut;
        }

        public boolean isFinalKill() {
            return this.finalKill;
        }

        public boolean isDespawnable() {
            return this.despawnable;
        }

        public boolean isPvpLogOut() {
            return this.pvpLogOut;
        }
    }
}

