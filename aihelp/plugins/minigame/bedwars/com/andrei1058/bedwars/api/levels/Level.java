/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.api.levels;

import com.andrei1058.bedwars.api.events.player.PlayerXpGainEvent;
import org.bukkit.entity.Player;

public interface Level {
    public String getLevel(Player var1);

    public int getPlayerLevel(Player var1);

    public String getRequiredXpFormatted(Player var1);

    public String getProgressBar(Player var1);

    public int getCurrentXp(Player var1);

    public String getCurrentXpFormatted(Player var1);

    public int getRequiredXp(Player var1);

    public void addXp(Player var1, int var2, PlayerXpGainEvent.XpSource var3);

    public void setXp(Player var1, int var2);

    public void setLevel(Player var1, int var2);
}

