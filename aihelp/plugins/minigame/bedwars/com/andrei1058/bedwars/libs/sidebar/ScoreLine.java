/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.libs.sidebar;

import com.andrei1058.bedwars.libs.sidebar.SidebarLine;
import org.bukkit.entity.Player;

public interface ScoreLine
extends Comparable<ScoreLine> {
    public SidebarLine getLine();

    public void setLine(SidebarLine var1);

    public int getScoreAmount();

    public void setScoreAmount(int var1);

    public void sendCreateToAllReceivers();

    public void sendCreate(Player var1);

    public boolean setContent(SidebarLine var1);

    public void sendUpdateToAllReceivers();

    public void sendUpdate(Player var1);

    public void sendRemoveToAllReceivers();

    public void sendRemove(Player var1);

    public String getColor();

    public boolean refreshContent();
}

