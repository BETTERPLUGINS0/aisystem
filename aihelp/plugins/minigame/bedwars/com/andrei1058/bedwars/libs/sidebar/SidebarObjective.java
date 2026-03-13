/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.libs.sidebar;

import com.andrei1058.bedwars.libs.sidebar.SidebarLine;
import org.bukkit.entity.Player;

public interface SidebarObjective {
    public void setTitle(SidebarLine var1);

    public SidebarLine getTitle();

    public void sendCreate(Player var1);

    public void sendUpdate();

    public void sendRemove(Player var1);

    public String getName();

    public boolean refreshTitle();
}

