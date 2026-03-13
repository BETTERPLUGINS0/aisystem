/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.api.events.spectator;

import com.andrei1058.bedwars.api.arena.IArena;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SpectatorFirstPersonEnterEvent
extends Event
implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player spectator;
    private final Player target;
    private final IArena arena;
    private boolean cancelled = false;
    private Function<Player, String> title;
    private Function<Player, String> subTitle;
    private int fadeIn = 0;
    private int stay = 40;
    private int fadeOut = 10;
    private static List<UUID> spectatingInFirstPerson = new ArrayList<UUID>();

    public SpectatorFirstPersonEnterEvent(@NotNull Player spectator, @NotNull Player target, IArena arena, Function<Player, String> title, Function<Player, String> subtitle) {
        this.spectator = spectator;
        this.target = target;
        this.arena = arena;
        this.title = title;
        this.subTitle = subtitle;
        if (!spectatingInFirstPerson.contains(spectator.getUniqueId())) {
            spectatingInFirstPerson.add(spectator.getUniqueId());
        }
    }

    public Player getSpectator() {
        return this.spectator;
    }

    public IArena getArena() {
        return this.arena;
    }

    public Player getTarget() {
        return this.target;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Function<Player, String> getSubTitle() {
        return this.subTitle;
    }

    public Function<Player, String> getTitle() {
        return this.title;
    }

    public void setTitle(Function<Player, String> title) {
        this.title = title;
    }

    public void setSubTitle(Function<Player, String> subTitle) {
        this.subTitle = subTitle;
    }

    public int getStay() {
        return this.stay;
    }

    public int getFadeOut() {
        return this.fadeOut;
    }

    public int getFadeIn() {
        return this.fadeIn;
    }

    public void setStay(int stay) {
        if (stay < 0) {
            return;
        }
        this.stay = stay;
    }

    public void setFadeOut(int fadeOut) {
        if (fadeOut < 0) {
            return;
        }
        this.fadeOut = fadeOut;
    }

    public void setFadeIn(int fadeIn) {
        if (fadeIn < 0) {
            return;
        }
        this.fadeIn = fadeIn;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

