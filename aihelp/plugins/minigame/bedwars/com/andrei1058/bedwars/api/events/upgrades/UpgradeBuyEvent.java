/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.andrei1058.bedwars.api.events.upgrades;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.upgrades.TeamUpgrade;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UpgradeBuyEvent
extends Event
implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private TeamUpgrade teamUpgrade;
    private Player player;
    private final IArena arena;
    private ITeam team;
    private boolean cancelled = false;

    public UpgradeBuyEvent(TeamUpgrade teamUpgrade, Player player, ITeam team) {
        this.teamUpgrade = teamUpgrade;
        this.player = player;
        this.team = team;
        this.arena = team.getArena();
    }

    public IArena getArena() {
        return this.arena;
    }

    public Player getPlayer() {
        return this.player;
    }

    public ITeam getTeam() {
        return this.team;
    }

    public TeamUpgrade getTeamUpgrade() {
        return this.teamUpgrade;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}

