/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.api.arena.generator;

import org.bukkit.entity.Player;

@Deprecated
public interface IGenHolo {
    public void setTimerName(String var1);

    public void setTierName(String var1);

    public String getIso();

    public void updateForPlayer(Player var1, String var2);

    public void updateForAll();

    public void destroy();
}

