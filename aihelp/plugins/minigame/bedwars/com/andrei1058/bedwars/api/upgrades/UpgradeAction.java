/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.api.upgrades;

import com.andrei1058.bedwars.api.arena.team.ITeam;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public interface UpgradeAction {
    @Deprecated
    default public void onBuy(ITeam bwt) {
        this.onBuy(null, bwt);
    }

    public void onBuy(@Nullable Player var1, ITeam var2);
}

