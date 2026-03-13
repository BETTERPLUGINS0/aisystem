/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.api.upgrades;

import com.andrei1058.bedwars.api.arena.team.ITeam;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface TrapAction {
    public String getName();

    public void onTrigger(@NotNull Player var1, ITeam var2, ITeam var3);
}

