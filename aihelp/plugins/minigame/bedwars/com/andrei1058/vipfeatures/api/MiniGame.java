/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package com.andrei1058.vipfeatures.api;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public abstract class MiniGame {
    private final Plugin owner;

    public MiniGame(Plugin plugin) {
        this.owner = plugin;
    }

    public abstract boolean isPlaying(Player var1);

    public abstract boolean hasBoosters();

    public abstract String getDisplayName();

    public Plugin getOwner() {
        return this.owner;
    }
}

